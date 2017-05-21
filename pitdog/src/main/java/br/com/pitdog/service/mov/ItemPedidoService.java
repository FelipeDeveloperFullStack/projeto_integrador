package br.com.pitdog.service.mov;

import java.util.List;

import br.com.pitdog.model.mov.ItemPedido;
import br.com.pitdog.model.mov.Pedido;
import br.com.sysge.infraestrutura.dao.GenericDaoImpl;

public class ItemPedidoService extends GenericDaoImpl<ItemPedido, Long>{

	private static final long serialVersionUID = -3397596835771217684L;
	
	public ItemPedido consistir(Pedido pedido, ItemPedido itemPedido){
		itemPedido.setPedido(verificarSeDistribuidorIgualNull(pedido));
		return verificarQuantidadeIgualAZero(verificarSeProdutoNull(itemPedido));
		
	}
	
	public ItemPedido consistir(ItemPedido itemPedido, List<ItemPedido> itemPedidos){
		for(ItemPedido i : itemPedidos){
			if(itemPedido.getProduto().getDescricaoProduto().trim().equalsIgnoreCase(i.getProduto().getDescricaoProduto().trim())){
				throw new RuntimeException("O produto já existe na lista, escolha outro produto!");
			}
		}
		return itemPedido;
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
