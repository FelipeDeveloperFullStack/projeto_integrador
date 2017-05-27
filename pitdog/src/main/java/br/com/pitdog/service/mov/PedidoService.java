package br.com.pitdog.service.mov;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.Query;

import br.com.pitdog.model.estoque.Produto;
import br.com.pitdog.model.mov.ItemPedido;
import br.com.pitdog.model.mov.Pedido;
import br.com.pitdog.model.mov.type.TipoPE;
import br.com.pitdog.model.type.Situacao;
import br.com.pitdog.relatorios.to.PedidoReportTO;
import br.com.pitdog.service.estoque.ProdutoService;
import br.com.sysge.infraestrutura.dao.GenericDaoImpl;
import br.com.sysge.infraestrutura.reports.ReportFactory;
import br.com.sysge.infraestrutura.reports.TiposRelatorio;

public class PedidoService extends GenericDaoImpl<Pedido, Long>{

	private static final long serialVersionUID = 2256364587065936773L;
	
	@Inject
	private ProdutoService produtoService;
	
	@Inject
	private ItemPedidoService itemPedidoService;
	
	public Pedido salvar(Pedido pedido, List<ItemPedido> itensPedidos){
		try {
			if(pedido.getDataEntrada() == null && pedido.getDataPedido() == null){
				throw new RuntimeException("Não é possível salvar o registro, "
						+ "é necessário informar a data do pedido ou a data de entrada!");
			}
			return verificarTipoPE(pedido, itensPedidos);
		} catch (RuntimeException e) {
			e.getStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public Pedido verificarTipoPE(Pedido pedido, List<ItemPedido> itensPedidos){
		if(pedido.getDataPedido() != null && pedido.getDataEntrada() != null){
			throw new RuntimeException("Não é possível salvar o registro,"
					+ " por favor informe uma data de pedido ou uma data de entrada!!");
		}
			if(pedido.getDataPedido() != null){
				pedido.setTipoPe(TipoPE.PEDIDO);
				return salvarItemPedido(super.save(consistirPedido(pedido)), itensPedidos);
			}
			
			if(pedido.getDataEntrada() != null){
				pedido.setTipoPe(TipoPE.ENTRADA);
				return salvarItemPedido(atualizarEstoqueProduto(super.save(consistirPedido(pedido)), itensPedidos), itensPedidos);
			}
			
			return pedido;
		
	}
	
	public Pedido salvarItemPedido(Pedido pedido, List<ItemPedido> itensPedidos){
		for(ItemPedido item : itensPedidos){
			item.setPedido(pedido);
			itemPedidoService.save(item);
		}
		return pedido;
	}
	
	public Pedido atualizarEstoqueProduto(Pedido pedido, List<ItemPedido> itensPedidos){
		for(Produto p : produtoService.findBySituation(Situacao.ATIVO)){
			for(ItemPedido item : itensPedidos){
				if(p.getId() == item.getProduto().getId()){
					p.setQuantidadeEstoque(p.getQuantidadeEstoque() + item.getQuantidade());
					p.setValorCusto(item.getPreco());
					produtoService.salvar(p);
				}
			}
		}
		return pedido;
	}
	
	private Pedido consistirPedido(Pedido pedido){
		if(pedido.getId() == null){
			pedido.setSituacao(Situacao.ATIVO);
		}
		return pedido;
	}
	
	public List<Pedido> pesquisarPedido(Date data, Pedido pedido){
		if(pedido.getTipoPe() == TipoPE.ENTRADA){
			return pesquisar(data, pedido, "dataEntrada");
		}else{
			return pesquisar(data, pedido, "dataPedido");
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Pedido> pesquisar(Date data, Pedido pedido, String atributo){
		Query query = null;
		if(data == null){
			query = getEntityManager().createQuery("SELECT p FROM Pedido p "
					+ "WHERE p.situacao = :situacao AND p.tipoPe = :tipoPe");
			query.setParameter("situacao", pedido.getSituacao());
			query.setParameter("tipoPe", pedido.getTipoPe());
		}else{
			query = getEntityManager().createQuery("SELECT p FROM Pedido p "
					+ "WHERE p."+atributo+" = :data AND p.situacao = :situacao");
			query.setParameter("data", data);
			query.setParameter("situacao", pedido.getSituacao());
		}
		return query.getResultList();
			
	}
	
	public void gerarRelatorio(Pedido pedido, List<ItemPedido> itensPedidos){
		
		List<PedidoReportTO> pedidoReportTOs = new ArrayList<PedidoReportTO>();
		for(ItemPedido item : itensPedidos){
			PedidoReportTO pedidoReportTO = new PedidoReportTO();
			pedidoReportTO.setDesconto(item.getDesconto());
			pedidoReportTO.setFabricante(item.getProduto().getFabricante().getRazaoSocial());
			pedidoReportTO.setPreco(item.getPreco());
			pedidoReportTO.setProduto(item.getProduto().getDescricaoProduto());
			pedidoReportTO.setQuantidade(item.getQuantidade());
			pedidoReportTO.setUnidadeMedida(item.getUnidadeMedida().getUnidadeMedida());
			pedidoReportTOs.add(pedidoReportTO);
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		if(pedido.getTipoPe() == TipoPE.ENTRADA){
			params.put("pedido_entrada", pedido.getTipoPe().getTipoPE().toLowerCase());
			params.put("data", pedido.getDataEntrada());
		}else{
			params.put("pedido_entrada", pedido.getTipoPe().getTipoPE().toLowerCase());
			params.put("data", pedido.getDataPedido());
		}
		
		
		ReportFactory reportFactory = new ReportFactory("pedidoReport.jasper", 
				params, TiposRelatorio.PDF, pedidoReportTOs);
		reportFactory.getReportStream();
	}

}
