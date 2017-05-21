package br.com.pitdog.service.mov;

import java.util.List;

import javax.inject.Inject;

import br.com.pitdog.model.estoque.EstoqueMinimoIdeal;
import br.com.pitdog.model.mov.ItemPedido;
import br.com.pitdog.model.mov.Pedido;
import br.com.pitdog.service.estoque.EstoqueMinimoIdealService;
import br.com.sysge.infraestrutura.dao.GenericDaoImpl;

public class ItemPedidoService extends GenericDaoImpl<ItemPedido, Long>{

	private static final long serialVersionUID = -3397596835771217684L;
	
	@Inject
	private EstoqueMinimoIdealService estoqueMinimoIdealService;
	
	//First
	public ItemPedido consistir(Pedido pedido, ItemPedido itemPedido){
		itemPedido.setPedido(verificarSeDistribuidorIgualNull(pedido));
		return sugerirQuantidade(pedido, verificarSeProdutoNull(itemPedido));
	}
	
	private ItemPedido sugerirQuantidade(Pedido pedido, ItemPedido itemPedido){
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
		return itemPedido;
	}
	
	private double calcular(ItemPedido itemPedido, double quantidade1, double quantidade2){
		itemPedido.setQuantidade((quantidade1 - quantidade2) * -1);
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
			return super.findByListProperty(itemPedido.getProduto().getId(), "produto.id");
		}
		return itensPedidos;
	}

}
