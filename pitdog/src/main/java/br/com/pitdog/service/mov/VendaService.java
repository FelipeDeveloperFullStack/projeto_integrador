package br.com.pitdog.service.mov;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.Query;

import br.com.pitdog.model.estoque.Composicao;
import br.com.pitdog.model.mov.ItemVenda;
import br.com.pitdog.model.mov.InsumoAdicional;
import br.com.pitdog.model.mov.InsumoRemovido;
import br.com.pitdog.model.mov.Venda;
import br.com.pitdog.model.mov.type.StatusVenda;
import br.com.pitdog.model.rh.Funcionario;
import br.com.pitdog.service.estoque.ComposicaoService;
import br.com.pitdog.service.estoque.ProdutoService;
import br.com.sysge.infraestrutura.dao.GenericDaoImpl;

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
}
