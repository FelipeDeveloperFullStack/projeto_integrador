package br.com.pitdog.service.mov;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.Query;

import br.com.pitdog.model.estoque.Composicao;
import br.com.pitdog.model.mov.ItemVenda;
import br.com.pitdog.model.mov.InsumoAdicional;
import br.com.pitdog.model.mov.Venda;
import br.com.pitdog.model.mov.type.StatusVenda;
import br.com.pitdog.model.rh.Funcionario;
import br.com.pitdog.service.estoque.ComposicaoService;
import br.com.pitdog.service.estoque.ProdutoService;
import br.com.sysge.infraestrutura.dao.GenericDaoImpl;
import br.com.sysge.infraestrutura.reports.ReportFactory;
import br.com.sysge.infraestrutura.reports.TiposRelatorio;

public class VendaService extends GenericDaoImpl<Venda, Long>{

	private static final long serialVersionUID = 1L;

	@Inject
	private ProdutoService produtoService;
	
	@Inject 
	private ComposicaoService composicaoService;
	
	public Venda finalizarVenda(Venda venda){
		venda.validarCLiente();
		venda.validarItens();
		venda.setStatus(StatusVenda.CONCLUIDA);
		movimentarEstoque(venda);
		return save(venda);
	}
	
	private void movimentarEstoque(Venda venda) {
		List<Composicao> composicaos;
		for (ItemVenda itemVenda : venda.getItens()) {
			composicaos = composicaoService.listarComposicaProduto(itemVenda.getProduto());
			for (Composicao composicao : composicaos) {
				if(!itemVenda.getInsumosRemovidos().contains(composicao.getInsumo())){
					composicao.getInsumo().setQuantidadeEstoque(
							composicao.getInsumo().getQuantidadeEstoque() - composicao.getQuantidade());
					produtoService.salvar(composicao.getInsumo());
				}
			}
			
			for (InsumoAdicional itemVendaInsumo : itemVenda.getInsumosAdicionais()) {
				itemVendaInsumo.getInsumo().setQuantidadeEstoque(
							itemVendaInsumo.getInsumo().getQuantidadeEstoque() - 1);
				produtoService.salvar(itemVendaInsumo.getInsumo()); 
			}
			
		}
	}
	
	@Override
	public Venda save(Venda entity) {
		checarNumero(entity);
		return super.save(entity);
	}
	
	private void checarNumero(Venda venda){
		if(venda.getNumero() <= 0){
			Integer numero = (Integer) getEntityManager().createQuery("select max(v.numero) from Venda v")
			  .getSingleResult();
			if(numero == null || numero == 0){
				venda.setNumero(1);
			}else{
				venda.setNumero(numero + 1);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public Venda buscarVendaAbertaPorBalconista(Funcionario funcionario){
		Query query = getEntityManager().createQuery
				("SELECT v FROM " + getEntityClass().getSimpleName() +  " v "
						+ " WHERE v.balconista = :balconista AND v.status = :status");
		
		query.setParameter("balconista", funcionario);
		query.setParameter("status", StatusVenda.DIGITACAO);
		List<Venda> vendas = query.getResultList();
		if(vendas.isEmpty()){
			return null;
		}
		return vendas.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public List<Venda> listarVenda(String cliente, Date dataInicio, Date dataFim){
		
		String jpql = "SELECT v FROM " + getEntityClass().getSimpleName() +  " v "
				+ "WHERE v.cliente.nomeDaPessoaFisica LIKE :cliente ";
		
		if(dataInicio != null){
			jpql = jpql + "AND v.data >= :dataInicio ";
		}
		
		if(dataFim != null){
			jpql = jpql + "AND v.data <= :dataFim ";
		}
		
		
		
		Query query = getEntityManager().createQuery(jpql);
		
		query.setParameter("cliente", cliente + "%");
		
		if(dataInicio != null){
			query.setParameter("dataInicio", dataInicio);
		}
		
		if(dataFim != null){
			query.setParameter("dataFim", dataFim);
		}
		
		return query.getResultList();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Venda> buscarDadosVendaPorPeriodo(String balconista, String cliente, 
			StatusVenda statusVenda, Date dataInicial, Date dataFinal){
		
		String jpql_balconista = " AND v.balconista.nome = :balconista ";
		String jpql_cliente = " AND v.cliente.nomeDaPessoaFisica = :cliente ";
		String jpql_status = " AND v.status = :statusVenda ";
		
		if(balconista.equals("Todos")){
			jpql_balconista = "";
		}
		if(cliente.equals("todos_clientes")){
			jpql_cliente = "";
		}
		
		Query query = getEntityManager().createQuery("SELECT v FROM "+getEntityClass().getSimpleName() + " v "
				+ " WHERE v.data >= :dataInicial AND v.data <= :dataFinal " + jpql_balconista 
				+ jpql_cliente + jpql_status);
		
		query.setParameter("dataInicial", dataInicial);
		query.setParameter("dataFinal", dataFinal);
		query.setParameter("statusVenda", statusVenda);
		
		if(!balconista.equals("Todos")){
			query.setParameter("balconista", balconista);
		}
		if(!cliente.equals("todos_clientes")){
			query.setParameter("cliente", cliente);
		}
		
		return query.getResultList();
	}
	
	public void gerarRelatorioVendaPorPeriodo(String balconista, String cliente, 
			StatusVenda statusVenda, Date dataInicial, Date dataFinal){
		
		List<Venda> vendas = buscarDadosVendaPorPeriodo(balconista, cliente, statusVenda, dataInicial, dataFinal);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dataInicial", dataInicial);
		params.put("dataFinal", dataFinal);
		params.put("balconista", balconista);
		ReportFactory reportFactory = new ReportFactory("vendasPorPeriodoReport.jasper", params, TiposRelatorio.PDF, vendas);
		reportFactory.getReportStream();
		
	}
	
	public List<StatusVenda> buscarStatusVenda(){
		List<StatusVenda> statusVenda = new ArrayList<StatusVenda>();
		List<Venda> vendas = super.findAll();
		for(int i = 0; i < vendas.size(); i++){
			if(i == 0){
				statusVenda.add(vendas.get(0).getStatus());
			}
			for(int y = 1; y < vendas.size(); y++){
				if(vendas.get(i).getStatus() != vendas.get(y).getStatus()){
					List<StatusVenda> newListStatusVenda = new ArrayList<StatusVenda>();
					for(StatusVenda s : statusVenda){
						if(s != vendas.get(y).getStatus()){
							newListStatusVenda.add(vendas.get(y).getStatus());
						}
					}
					for(int a = 0; a < newListStatusVenda.size(); a++){
						for(int b = 0; b < statusVenda.size(); b++){
							if(!newListStatusVenda.isEmpty()){
								if(newListStatusVenda.get(a) == statusVenda.get(b)){
									newListStatusVenda.remove(a);
								}
							}
						}
					}
					statusVenda.addAll(newListStatusVenda);
				}
			}
		}
		return statusVenda;
	}
	
	
}
