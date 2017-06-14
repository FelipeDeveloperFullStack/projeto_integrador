package br.com.pitdog.controller.estoque;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pitdog.model.estoque.Composicao;
import br.com.pitdog.model.estoque.Fabricante;
import br.com.pitdog.model.estoque.Produto;
import br.com.pitdog.model.global.Categoria;
import br.com.pitdog.model.mov.type.UnidadeMedida;
import br.com.pitdog.model.type.Situacao;
import br.com.pitdog.model.type.TipoProduto;
import br.com.pitdog.service.estoque.ComposicaoService;
import br.com.pitdog.service.estoque.FabricanteService;
import br.com.pitdog.service.estoque.ProdutoService;
import br.com.pitdog.service.global.CategoriaService;
import br.com.pitdog.util.FacesUtil;
import br.com.pitdog.util.RequestContextUtil;

@Named
@ViewScoped
public class ProdutoController implements Serializable{

	private static final long serialVersionUID = 7321092633765106412L;
	
	private Produto produto;
	
	private Composicao composicao;
	
	private List<Produto> produtos;
	
	@SuppressWarnings("unused")
	private List<Produto> insumos;
	
	@SuppressWarnings("unused")
	private List<Fabricante> fabricantes;
	
	@SuppressWarnings("unused")
	private List<Categoria> categorias;
	
	private List<Composicao> composicoes;
	
	@Inject
	private ProdutoService produtoService;
	
	@Inject
	private FabricanteService fabricanteService;
	
	@Inject
	private CategoriaService categoriaService;
	
	@Inject
	private ComposicaoService composicaoService;
	
	@PostConstruct
	public void init(){
		novo();
	}
	
	public Situacao[] getSituacoes(){
		return Situacao.values();
	}
	public TipoProduto[] getTipoProdutos(){
		return TipoProduto.values();
	}
	
	public UnidadeMedida[] getUnidadeMedidas(){
		return UnidadeMedida.values();
	}
	
	public boolean getVerificarSeExisteComposicaoProduto(){
		return produtoService.isVerificarSeExisteComposicaoProduto(produto);
	}
	
	public void pesquisar(){
		this.produtos = new ArrayList<Produto>();
		this.produtos = produtoService.pesquisarProduto(produto);
	}
	
	public void cancelar(){
		novo();
		fecharDialogs();
	}
	
	public void novo(){
		this.produto = new Produto();
		this.produtos = new ArrayList<Produto>();
		this.fabricantes = new ArrayList<Fabricante>();
		this.composicao = new Composicao();
		this.composicoes = new ArrayList<Composicao>();
	}
	
	public void adicionarComposicao(){
		if(composicaoService.verificarSeExisteComposicaoNaTabela(composicoes, composicao)){
			FacesUtil.mensagemWarn("Já existe um insumo '"+this.composicao.getInsumo().getDescricaoProduto() +
					"' na lista, por favor escolha outro insumo!");
		}else if(composicaoService.verificarQuantidadeComposicao(composicao)){
			FacesUtil.mensagemWarn("A quantidade não pode ser igual a ZERO!");
		}else{
			composicao.setProduto(produto);
			this.composicoes.add(composicao);
			this.composicao = new Composicao();
		}
	}
	
	public void removerComposicao(Composicao composicao){
		this.composicoes = composicaoService.removerComposicao(composicao, composicoes);
	}
	
	public void salvarComposicao(){
		try {
			for(Composicao c : composicoes){
				composicaoService.save(c);
			}
			FacesUtil.mensagemInfo("Composição salva com sucesso!");
			fecharDialogs();
			novaListaProduto();
		} catch (RuntimeException e) {
			FacesUtil.mensagemErro(e.getMessage());
		}
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
		RequestContextUtil.execute("PF('dialogNovoProduto').hide();");
		RequestContextUtil.execute("PF('dialogEditarProduto').hide();");
		RequestContextUtil.execute("PF('dialogComposicao').hide();");
	}
	
	public void novaListaProduto(){
		novo();
	}
	
	public void setarProduto(Produto produto){
		this.produto = produto;
	}
	
	public void setarComposicao(Produto produto){
		this.produto = produto;
		this.composicoes = composicaoService.findByListProperty(produto.getId(), "produto.id");
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
		if(produtos.isEmpty()){
			return listarProdutosAtivos();
		}
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public List<Fabricante> getFabricantes() {
		return fabricanteService.findBySituation(Situacao.ATIVO);
	}

	public void setFabricantes(List<Fabricante> fabricantes) {
		this.fabricantes = fabricantes;
	}

	public List<Categoria> getCategorias() {
		return categoriaService.findBySituation(Situacao.ATIVO);
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}
	public List<Produto> getInsumos() {
		return produtoService.findByParametersForSituation
				(TipoProduto.INSUMO, Situacao.ATIVO, "tipo", "=", "", "");
	}
	public void setInsumos(List<Produto> insumos) {
		this.insumos = insumos;
	}

	public Composicao getComposicao() {
		return composicao == null ? new Composicao() : this.composicao;
	}

	public void setComposicao(Composicao composicao) {
		this.composicao = composicao;
	}

	public List<Composicao> getComposicoes() {
		return composicoes;
	}

	public void setComposicoes(List<Composicao> composicoes) {
		this.composicoes = composicoes;
	}
	
	public List<Produto> listarProdutosAtivos(){
		return produtos = produtoService.findBySituation(Situacao.ATIVO);
	}

}
