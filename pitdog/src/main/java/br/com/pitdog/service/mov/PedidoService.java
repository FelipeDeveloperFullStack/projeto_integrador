package br.com.pitdog.service.mov;

import java.util.List;

import br.com.pitdog.model.mov.Pedido;
import br.com.pitdog.model.type.Situacao;
import br.com.sysge.infraestrutura.dao.GenericDaoImpl;

public class PedidoService extends GenericDaoImpl<Pedido, Long>{

	private static final long serialVersionUID = 2256364587065936773L;
	
	public Pedido salvar(Pedido pedido){
		try {
			if(pedido.getDescricaoPedido().trim().isEmpty()){
				throw new RuntimeException("A descrição do pedido é obrigatório!");
			}
			return super.save(consistirPedido(pedido));
		} catch (RuntimeException e) {
			e.getStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private Pedido consistirPedido(Pedido pedido){
		if(pedido.getId() == null){
			pedido.setSituacao(Situacao.ATIVO);
		}
		return pedido;
	}
	
	public List<Pedido> pesquisarPedido(Pedido pedido){
		if(pedido.getDescricaoPedido().trim().isEmpty()){
			return super.findBySituation(pedido.getSituacao());
		}else{
			return super.findByParametersForSituation(pedido.getDescricaoPedido(), 
					pedido.getSituacao(), "descricaoPedido", "LIKE", "%", "%");
		}
}
	

}
