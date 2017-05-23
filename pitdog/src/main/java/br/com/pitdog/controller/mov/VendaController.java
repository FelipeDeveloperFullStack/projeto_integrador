package br.com.pitdog.controller.mov;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pitdog.model.estoque.Produto;
import br.com.pitdog.model.mov.ItemVenda;
import br.com.pitdog.model.mov.Venda;
import br.com.pitdog.model.mov.type.StatusVenda;
import br.com.pitdog.model.type.Situacao;
import br.com.pitdog.service.estoque.ComposicaoService;
import br.com.pitdog.service.estoque.ProdutoService;
import br.com.pitdog.service.mov.VendaService;

@Named
@SessionScoped
public class VendaController implements Serializable{

	private static final long serialVersionUID = 8644665195188273211L;

	private Venda venda = new Venda();;

	private ItemVenda itemVenda = null;
	
	private boolean exibirItem = false;
	
	private List<Produto> produtos = new ArrayList<>();
	
	private String pesquisa = "";

	@Inject
	private VendaService vendaService;

	@Inject
	private ProdutoService produtoService;

	@Inject
	private ComposicaoService composicaoService;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyyy");

	public void novo() {
		this.venda = vendaService.save(venda);
	}
	
	public void addItem() {
		itemVenda.setVenda(venda);
		itemVenda.setNumero(venda.getItens().size());
		venda.getItens().add(itemVenda);
		itemVenda = null;
		exibirItem = false;
		venda.totalizar();
		venda = vendaService.save(venda);
	}
	
	public void finalizar(){
		venda.setStatus(StatusVenda.CONCLUIDA);
		venda = vendaService.save(venda);
	}
	
	public void cancelar(){
		venda.setStatus(StatusVenda.CANCELADA);
		venda = vendaService.save(venda);
		venda = new Venda();
	}
	
	public void pesquisar() {
		produtos = produtoService.findByParametersForSituation(pesquisa, 
				Situacao.ATIVO, "descricaoProduto", "LIKE", "%", "%"); 
	}
	
	public void selecionarProduto(Produto produto){
		produto = produtoService.findById(produto.getId());
		itemVenda = new ItemVenda();
		itemVenda.setProduto(produto);
		itemVenda.setValorUnitario(itemVenda.getProduto().getValorVenda());
		itemVenda.setValorLiquido(itemVenda.getProduto().getValorVenda());
		itemVenda.setQuantidade(BigDecimal.ONE);
		exibirItem = true;
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

	public boolean isExibirItem() {
		return exibirItem;
	}

	public void setExibirItem(boolean exibirItem) {
		this.exibirItem = exibirItem;
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
	
	
	
}
