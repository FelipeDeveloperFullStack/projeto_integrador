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
import br.com.pitdog.model.mov.ItemPedido;
import br.com.pitdog.model.mov.Pedido;
import br.com.pitdog.model.mov.type.UnidadeMedida;
import br.com.pitdog.model.type.Situacao;
import br.com.pitdog.service.estoque.ProdutoService;
import br.com.pitdog.service.global.DistribuidorService;
import br.com.pitdog.service.mov.ItemPedidoService;
import br.com.pitdog.service.mov.PedidoService;
import br.com.pitdog.util.FacesUtil;
import br.com.pitdog.util.RequestContextUtil;

@Named
@ViewScoped
public class PedidoController implements Serializable{

	private static final long serialVersionUID = 8644665195188273211L;
	
	private Pedido pedido;
	
	private List<ItemPedido> itensPedidos;
	
	private ItemPedido itemPedido;

	@Inject
	private PedidoService pedidoService;
	
	@Inject
	private DistribuidorService distribuidorService;
	
	@Inject
	private ProdutoService produtoService;
	
	@Inject
	private ItemPedidoService itemPedidoService;

	private List<Pedido> pedidos;
	
	@PostConstruct
	public void init() {
		novo();
	}

	public Situacao[] getSituacoes() {
		return Situacao.values();
	}
	
	public UnidadeMedida[] getUnidadeMedidas(){
		return UnidadeMedida.values();
	}

	public void novo() {
		this.pedido = new Pedido();
		this.itemPedido = new ItemPedido();
		this.itensPedidos = new ArrayList<ItemPedido>();
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
	
	public void adicionar(){
		try {
			this.itensPedidos.add(itemPedidoService.consistir(pedido, itemPedido));
			this.itemPedido = new ItemPedido();
		} catch (RuntimeException e) {
			FacesUtil.mensagemWarn(e.getMessage());
		}
	}

	public void salvar() {
		try {
			pedido = pedidoService.salvar(pedido);
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

	public List<ItemPedido> getItensPedidos() {
		return itensPedidos;
	}

	public void setItensPedidos(List<ItemPedido> itensPedidos) {
		this.itensPedidos = itensPedidos;
	}

	public ItemPedido getItemPedido() {
		return itemPedido == null ? new ItemPedido() : itemPedido;
	}

	public void setItemPedido(ItemPedido itemPedido) {
		this.itemPedido = itemPedido;
	}
	
	

}
