package br.com.pitdog.controller.estoque;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pitdog.model.estoque.Produto;
import br.com.pitdog.model.global.Cliente;
import br.com.pitdog.model.global.Fornecedor;
import br.com.pitdog.model.type.Situacao;
import br.com.pitdog.service.estoque.ProdutoService;
import br.com.pitdog.service.global.FornecedorService;
import br.com.pitdog.util.FacesUtil;
import br.com.pitdog.util.RequestContextUtil;

@Named
@ViewScoped
public class ProdutoController implements Serializable{

	private static final long serialVersionUID = 7321092633765106412L;
	
	private Produto produto;
	
	private List<Produto> produtos;
	
	@SuppressWarnings("unused")
	private List<Produto> produtosEstoqueMinimo;
	
	@SuppressWarnings("unused")
	private List<Cliente> fornecedores;
	
	@Inject
	private ProdutoService produtoService;
	
	@Inject
	private FornecedorService fornecedorService;
	
	public Situacao[] getSituacoes(){
		return Situacao.values();
	}
	
	public void pesquisar(){
		this.produtos = new ArrayList<Produto>();
		this.produtos = produtoService.pesquisarProduto(produto);
	}
	
	public void novo(){
		this.produto = new Produto();
		this.produtos = new ArrayList<Produto>();
		this.fornecedores = new ArrayList<Cliente>();
	}
	
	public void salvar(){
		try {
			produtoService.salvar(produto);
			FacesUtil.mensagemInfo("Produto salvo com sucesso!");
			novaListaProduto();
			fecharDialogs();
		} catch (RuntimeException e) {
			FacesUtil.mensagemErro(e.getMessage());
		}
	}
	
	public void fecharDialogs(){
		RequestContextUtil.execute("PF('dialogNovoServico').hide();");
		RequestContextUtil.execute("PF('dialogEditarServico').hide();");
	}
	
	public void novaListaProduto(){
		novo();
	}
	
	public void setarProduto(Produto produto){
		this.produto = produto;
	}

	public Produto getProduto() {
		if(produto == null){
			produto = new Produto();
		}
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public List<Fornecedor> getFornecedores() {
		return fornecedorService.findBySituation(Situacao.ATIVO);
	}

	public void setFornecedores(List<Cliente> fornecedores) {
		this.fornecedores = fornecedores;
	}

	public List<Produto> getProdutosEstoqueMinimo() {
		return produtoService.obterProdutoQuantidadeMinimoEstoque();
	}

	public void setProdutosEstoqueMinimo(List<Produto> produtosEstoqueMinimo) {
		this.produtosEstoqueMinimo = produtosEstoqueMinimo;
	}
	
	

}
