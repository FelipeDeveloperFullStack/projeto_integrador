package br.com.pitdog.controller.mov;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pitdog.controller.sys.LoginController;
import br.com.pitdog.model.estoque.Composicao;
import br.com.pitdog.model.estoque.Produto;
import br.com.pitdog.model.global.Cliente;
import br.com.pitdog.model.mov.ItemVenda;
import br.com.pitdog.model.mov.ItemVendaInsumo;
import br.com.pitdog.model.mov.Venda;
import br.com.pitdog.model.mov.type.StatusVenda;
import br.com.pitdog.model.type.Situacao;
import br.com.pitdog.service.estoque.ComposicaoService;
import br.com.pitdog.service.estoque.ProdutoService;
import br.com.pitdog.service.global.ClienteService;
import br.com.pitdog.service.mov.VendaService;

@Named
@SessionScoped
public class VendaController implements Serializable{

	private static final long serialVersionUID = 8644665195188273211L;

	private Venda venda;

	private ItemVenda itemVenda = null;
	
	private List<Produto> insumosItem = new ArrayList<>();
	
	private List<Produto> insumos = new ArrayList<>();
	
	private List<Produto> produtos = new ArrayList<>();
	
	private String pesquisa = "";
	
	private String pesquisaInsumo = "";
	
	private String pesquisaCliente = "";
	
	private List<Cliente> clientes = new ArrayList<>();
	
	@Inject
	private LoginController loginController;

	@Inject
	private VendaService vendaService;

	@Inject
	private ProdutoService produtoService;

	@Inject
	private ComposicaoService composicaoService;
	
	@Inject
	private ClienteService clienteService;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	@PostConstruct
	public void init() {
		this.venda = vendaService.buscarVendaAbertaPorBalconista(loginController.getUsuario().getFuncionario());
		if(venda == null){
			novaVenda();
		}
	}
	
	public void removerInsumo(Produto produto) {
		produto = produtoService.findById(produto.getId());
		insumosItem.remove(produto);
		processarInsumos(itemVenda);
	}


	public void cancelarEdicaoItem() {
		itemVenda = null;
	}

	public void novaVenda() {
		this.venda  = new Venda();
		venda.setBalconista(loginController.getUsuario().getFuncionario());
	}
	
	public void addOrUpdateItem() {
		if(itemVenda.getId() == null){
			itemVenda.setVenda(venda);
			itemVenda.setNumero(venda.getItens().size());
			venda.getItens().add(itemVenda);
		}else{
			itemVenda.setVenda(venda);
			removerItem(itemVenda.getNumero());
			venda.getItens().add(itemVenda);
		}
		itemVenda = null;
		venda.totalizar();
		insumosItem.clear();
		venda = vendaService.save(venda);
	}
	
	public void removerItem(int numero){
		for (ItemVenda itemVenda : venda.getItens()) {
			if(itemVenda.getNumero() == numero){
				venda.getItens().remove(itemVenda);
				venda.totalizar();
				return;
			}
		}
	}
	
	public void editarItem(int numero){
		for (ItemVenda itemVenda : venda.getItens()) {
			if(itemVenda.getNumero() == numero){
				this.itemVenda = itemVenda.clone();
				insumosItem.clear();
				for(Composicao composicao : composicaoService.listarComposicaProduto(itemVenda.getProduto())){
					if(!contemProdutoNosRemovidos(composicao.getInsumo()))
						insumosItem.add(composicao.getInsumo());
				}
				
				for (ItemVendaInsumo itemVendaInsumo : itemVenda.getInsumosAdicionais()) {
					insumosItem.add(itemVendaInsumo.getInsumo());
				}
				
				return;
			}
		}
	}
	
	private boolean contemProdutoNosRemovidos(Produto produto){
		for (ItemVendaInsumo itemVendaInsumo : itemVenda.getInsumosRemovidos()) {
			if(produto.getId().equals(itemVendaInsumo.getInsumo().getId())){
				return true;
			}
		}
		return false;
	}
	
	public void finalizar(){
		vendaService.finalizarVenda(venda);
		novaVenda();
	}
	
	public void cancelar(){
		venda.setStatus(StatusVenda.CANCELADA);
		venda = vendaService.save(venda);
		novaVenda();
	}
	
	public void pesquisarInsumos() {
		insumos = produtoService.pesquisarInsumos(pesquisaInsumo); 
	}
	
	public void pesquisar() {
		produtos = produtoService.pesquisarProdutoNaoInsumo(pesquisa); 
	}
	
	public void pesquisarCliente() {
		clientes = clienteService.findByParametersForSituation(pesquisaCliente, 
				Situacao.ATIVO, "nomeDaPessoaFisica", "LIKE", "", "%"); 
		
		if (clientes.isEmpty()) {
			clientes = clienteService.findByParametersForSituation(pesquisaCliente, 
					Situacao.ATIVO, "telefone", "LIKE", "", "%"); 
		}
	}
	
	public void selecionarCliente(Cliente cliente){
		cliente = clienteService.findById(cliente.getId());
		venda.setCliente(cliente);
	}
	
	public void selecionarProduto(Produto produto){
		produto = produtoService.findById(produto.getId());
		itemVenda = new ItemVenda();
		itemVenda.setProduto(produto);
		itemVenda.setValorUnitario(itemVenda.getProduto().getValorVenda());
		itemVenda.setValorLiquido(itemVenda.getProduto().getValorVenda());
		itemVenda.setQuantidade(BigDecimal.ONE);
		itemVenda.calcularValores();
		insumosItem.clear();
		for(Composicao composicao : composicaoService.listarComposicaProduto(produto)){
			insumosItem.add(composicao.getInsumo());
		}
	}
	
	public void selecionarInsumo(Produto produto){
		produto = produtoService.findById(produto.getId());
		insumosItem.add(produto);
		processarInsumos(itemVenda);
	}
	
	private void processarInsumos(ItemVenda itemVenda) {
		List<Composicao> composicaos = composicaoService.listarComposicaProduto(itemVenda.getProduto());
		List<Produto> produtosComposicao = new ArrayList<>();
		for (Composicao composicao : composicaos) {
			produtosComposicao.add(composicao.getInsumo());
		}
		
		itemVenda.getInsumosAdicionais().clear();
		itemVenda.getInsumosRemovidos().clear();
		
		
		
		for (Produto produto : insumosItem) {
			ItemVendaInsumo itemVendaInsumo = new ItemVendaInsumo();
			itemVendaInsumo.setInsumo(produto);
			itemVendaInsumo.setItemVenda(itemVenda);
			itemVenda.getInsumosAdicionais().add(itemVendaInsumo);
		}
		
		for (Produto insumo : produtosComposicao) {
			for (int i = 0; i < itemVenda.getInsumosAdicionais().size(); i++) {
				if(insumo.getId().equals(itemVenda.getInsumosAdicionais().get(i).getInsumo().getId())){
					itemVenda.getInsumosAdicionais().remove(i);
					break;
				}
			}
		}
		
		for (Produto produto : produtosComposicao) {
			if(!insumosItem.contains(produto)){
				ItemVendaInsumo itemVendaInsumo = new ItemVendaInsumo();
				itemVendaInsumo.setInsumo(produto);
				itemVendaInsumo.setItemVenda(itemVenda);
				itemVenda.getInsumosRemovidos().add(itemVendaInsumo);
			}
		}
		itemVenda.calcularValores();
	}
	
	public void quantidadeChange() {
		itemVenda.calcularValores();
		
	}
	
	public void descontoChange() {
		itemVenda.calcularValores();
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public ItemVenda getItemVenda() {
		return itemVenda;
	}

	public void setItemVenda(ItemVenda itemVenda) {
		this.itemVenda = itemVenda;
	}
	
	public String getData(){
		return dateFormat.format(venda.getData());
	}
	
	public String getItemHeader(){
		return "Novo";
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public String getPesquisa() {
		return pesquisa;
	}
	
	public List<ItemVenda> getItens() {
		return venda.getItens();
	}

	public void setPesquisa(String pesquisa) {
		this.pesquisa = pesquisa;
	}

	public String getPesquisaCliente() {
		return pesquisaCliente;
	}

	public void setPesquisaCliente(String pesquisaCliente) {
		this.pesquisaCliente = pesquisaCliente;
	}
	public List<Cliente> getClientes() {
		return clientes;
	}
	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public List<Produto> getInsumosItem() {
		return insumosItem;
	}

	public void setInsumosItem(List<Produto> insumosItem) {
		this.insumosItem = insumosItem;
	}
	
	public String getHeaderVenda(){
		if(venda.getId() == null){
			return "Nova venda";
		}else{
			return "Venda " + venda.getNumero();
		}
	}

	public List<Produto> getInsumos() {
		return insumos;
	}

	public void setInsumos(List<Produto> insumos) {
		this.insumos = insumos;
	}

	public String getPesquisaInsumo() {
		return pesquisaInsumo;
	}

	public void setPesquisaInsumo(String pesquisaInsumo) {
		this.pesquisaInsumo = pesquisaInsumo;
	}
	
	public boolean getNaoPoderEditar() {
		return venda.getStatus() != StatusVenda.DIGITACAO;

	}
	
	
	
}
