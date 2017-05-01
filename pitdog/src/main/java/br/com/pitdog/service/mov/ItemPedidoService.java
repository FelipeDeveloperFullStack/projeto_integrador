package br.com.pitdog.service.mov;

import br.com.pitdog.model.mov.ItemPedido;
import br.com.pitdog.model.mov.Pedido;
import br.com.sysge.infraestrutura.dao.GenericDaoImpl;

public class ItemPedidoService extends GenericDaoImpl<ItemPedido, Long>{

	private static final long serialVersionUID = -3397596835771217684L;
	
	public ItemPedido consistir(Pedido pedido, ItemPedido itemPedido){
		itemPedido.setPedido(verificarSeDistribuidorIgualNull(pedido));
		return verificarQuantidadeIgualAZero(verificarSeProdutoNull(itemPedido));
		
	}
	
	private ItemPedido verificarQuantidadeIgualAZero(ItemPedido itemPedido){
		if(itemPedido.getQuantidade() <= 0){
			throw new RuntimeException("A quantidade tem que ser maior que zero!");
		}else{
			return itemPedido;
		}
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

}
