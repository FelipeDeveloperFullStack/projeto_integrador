package br.com.pitdog.service.mov;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.Query;

import br.com.pitdog.model.estoque.EstoqueMinimoIdeal;
import br.com.pitdog.model.mov.ItemPedido;
import br.com.pitdog.model.mov.Pedido;
import br.com.pitdog.model.mov.type.TipoPE;
import br.com.pitdog.service.estoque.EstoqueMinimoIdealService;
import br.com.pitdog.service.estoque.ProdutoService;
import br.com.sysge.infraestrutura.dao.GenericDaoImpl;

public class ItemPedidoService extends GenericDaoImpl<ItemPedido, Long>{

	private static final long serialVersionUID = -3397596835771217684L;
	
	@Inject
	private EstoqueMinimoIdealService estoqueMinimoIdealService;
	
	@Inject
	private ProdutoService produtoService;
	
	//First
	public ItemPedido consistir(Pedido pedido, ItemPedido itemPedido){
		itemPedido.setPedido(verificarSeDistribuidorIgualNull(pedido));
		return sugerirQuantidade(pedido, verificarSeProdutoNull(itemPedido));
	}
	
	private ItemPedido sugerirQuantidade(Pedido pedido, ItemPedido itemPedido){
		
		if((pedido.getDataEntrada() != null && pedido.getDataPedido() != null) || 
				(pedido.getDataEntrada() == null && pedido.getDataPedido() == null)){
			throw new RuntimeException("Não é possível adicionar o registro,"
					+ " por favor informe uma data de pedido ou uma data de entrada!");
		}
		
		if(pedido.getDataPedido() != null){
			List<EstoqueMinimoIdeal> listaEstoqueMinimoIdeal = estoqueMinimoIdealService.findByListProperty(itemPedido.getProduto().getId(), "produto.id");
			if(listaEstoqueMinimoIdeal.isEmpty()){
				throw new RuntimeException("Nenhum registro encontrado no estoque mínimo ideal para esse produto!");
			}
			for(EstoqueMinimoIdeal estoqueMinimoIdeal : listaEstoqueMinimoIdeal){
				if(estoqueMinimoIdeal.getEstoqueIdeal().getDataSemana().equals(pedido.getDataPedido())){
					if(itemPedido.getQuantidade() > 0){
						itemPedido.setQuantidade(calcular(itemPedido, itemPedido.getQuantidade(), estoqueMinimoIdeal.getProduto().
								getQuantidadeEstoque()));
					}else{
						itemPedido.setQuantidade(calcular(itemPedido, estoqueMinimoIdeal.getQuantidade(), estoqueMinimoIdeal.getProduto().
								getQuantidadeEstoque()));
					}
					return itemPedido;
				}
			}
		}
		return itemPedido;
	}
	
	private double calcular(ItemPedido itemPedido, double quantidade1, double quantidade2){
		itemPedido.setQuantidade((quantidade1 - quantidade2));
		itemPedido.setQuantidade(itemPedido.getQuantidade() * -1);
		return itemPedido.getQuantidade();
	}
	
	public ItemPedido consistir(ItemPedido itemPedido, List<ItemPedido> itemPedidos){
		for(ItemPedido i : itemPedidos){
			if(itemPedido.getProduto().getDescricaoProduto().trim().equalsIgnoreCase(i.getProduto().getDescricaoProduto().trim())){
				throw new RuntimeException("O produto já existe na lista, escolha outro produto!");
			}
		}
		return itemPedido;
	}
	
	private ItemPedido verificarSeProdutoNull(ItemPedido itemPedido){
		if(itemPedido.getProduto() == null){
			throw new RuntimeException("O produto é obrigatório!");
		}else{
			return itemPedido;
		}
	}
	
	private Pedido verificarSeDistribuidorIgualNull(Pedido pedido){
		if(pedido.getDistribuidor() == null){
			throw new RuntimeException("O distribuidor é obrigatório!");
		}else{
			return pedido;
		}
	}
	
	public List<ItemPedido> removerItem(List<ItemPedido> itensPedidos, ItemPedido itemPedido){
		if(itemPedido.getId() == null){
			for(int i = 0; i < itensPedidos.size(); i++){
				if(itensPedidos.get(i).getProduto().getDescricaoProduto().trim().equals(itemPedido.getProduto().getDescricaoProduto())){
					itensPedidos.remove(i);
				}
			}
		}else{
			super.remove(itemPedido.getId());
			subtrairQuantidadeEstoqueProduto(itemPedido);
			return buscarItensDoPedido(itemPedido.getPedido());
		}
		return itensPedidos;
	}
	
	private void subtrairQuantidadeEstoqueProduto(ItemPedido itemPedido){
		if(itemPedido.getPedido().getTipoPe() == TipoPE.ENTRADA){
			itemPedido.getProduto().setQuantidadeEstoque(itemPedido.getProduto().getQuantidadeEstoque() 
					- itemPedido.getQuantidade());
			produtoService.salvar(itemPedido.getProduto());
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ItemPedido> buscarItensDoPedido(Pedido pedido){
		Query query = getEntityManager().createQuery("SELECT p FROM "+getEntityClass().getSimpleName()+" p "
				+ "WHERE p.pedido = :pedido");
		query.setParameter("pedido", pedido);
		return query.getResultList();
	}

}
