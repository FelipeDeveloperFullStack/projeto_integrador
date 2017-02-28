package br.com.pitdog.controller.mov;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pitdog.model.estoque.Produto;
import br.com.pitdog.model.global.Distribuidor;
import br.com.pitdog.model.mov.Pedido;
import br.com.pitdog.model.type.Situacao;
import br.com.pitdog.service.estoque.ProdutoService;
import br.com.pitdog.service.global.DistribuidorService;
import br.com.pitdog.service.mov.PedidoService;
import br.com.pitdog.util.FacesUtil;
import br.com.pitdog.util.RequestContextUtil;

@Named
@ViewScoped
public class PedidoController implements Serializable{

	private static final long serialVersionUID = 8644665195188273211L;
	
	private Pedido pedido;

	@Inject
	private PedidoService pedidoService;
	
	@Inject
	private DistribuidorService distribuidorService;
	
	@Inject
	private ProdutoService produtoService;

	private List<Pedido> pedidos;
	
	@PostConstruct
	public void init() {
		novo();
	}

	public Situacao[] getSituacoes() {
		return Situacao.values();
	}

	public void novo() {
		this.pedido = new Pedido();
		this.pedidos = new ArrayList<Pedido>();
	}

	public void pesquisar() {
		this.pedidos = new ArrayList<Pedido>();
		this.pedidos = pedidoService.pesquisarPedido(pedido);
	}

	public void fecharDialogs() {
		RequestContextUtil.execute("PF('dialogNovoPedido').hide();");
		RequestContextUtil.execute("PF('dialogEditarPedido').hide();");
	}

	public void salvar() {
		try {
			pedidoService.salvar(pedido);
			FacesUtil.mensagemInfo("Pedido salvo com sucesso!");
			fecharDialogs();
			novo();
		} catch (RuntimeException e) {
			FacesUtil.mensagemErro(e.getMessage());
		}
	}

	public void setarPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public Pedido getPedido() {
		return pedido == null ? new Pedido() : this.pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}

	public List<Produto> getProdutos() {
		return produtoService.findBySituation(Situacao.ATIVO);
	}

	public List<Distribuidor> getDistribuidores() {
		return distribuidorService.findBySituation(Situacao.ATIVO);
	}

}
