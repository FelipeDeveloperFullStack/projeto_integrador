package br.com.pitdog.controller.global;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pitdog.model.global.Categoria;
import br.com.pitdog.model.type.Situacao;
import br.com.pitdog.service.global.CategoriaService;
import br.com.pitdog.util.FacesUtil;
import br.com.pitdog.util.RequestContextUtil;

@Named
@ViewScoped
public class CategoriaController implements Serializable{

	private static final long serialVersionUID = 6316763040583596193L;
	
	private Categoria categoria;

	private List<Categoria> categorias;

	@Inject
	private CategoriaService categoriaService;
	
	@PostConstruct
	public void init(){
		novo();
	}

	public Situacao[] getSituacoes() {
		return Situacao.values();
	}

	public void pesquisar() {
		this.categorias = new ArrayList<Categoria>();
		this.categorias = categoriaService.pesquisarCategoria(categoria);
	}

	public void novo() {
		this.categoria = new Categoria();
		this.categorias = new ArrayList<Categoria>();
	}

	public void salvar() {
		try {
			categoriaService.salvar(categoria);
			FacesUtil.mensagemInfo("Categoria salva com sucesso!");
			novaListaCategoria();
			fecharDialogs();
		} catch (RuntimeException e) {
			FacesUtil.mensagemErro(e.getMessage());
		}
	}

	public void fecharDialogs() {
		RequestContextUtil.execute("PF('dialogNovaCategoria').hide();");
		RequestContextUtil.execute("PF('dialogEditarCategoria').hide();");
	}

	public void novaListaCategoria() {
		novo();
	}
	
	public void setarCategoria(Categoria categoria){
		this.categoria = categoria;
	}

	public Categoria getCategoria() {
		return categoria == null ? new Categoria() : categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public List<Categoria> getCategorias() {
		if(categorias.isEmpty()){
			return listarCategoriasAtivos();
		}
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

	public List<Categoria> listarCategoriasAtivos(){
		return categorias = categoriaService.findBySituation(Situacao.ATIVO);
	}


}
