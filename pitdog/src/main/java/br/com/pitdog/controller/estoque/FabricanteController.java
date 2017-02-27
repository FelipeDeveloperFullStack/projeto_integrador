package br.com.pitdog.controller.estoque;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pitdog.model.estoque.Fabricante;
import br.com.pitdog.model.type.Situacao;
import br.com.pitdog.service.estoque.FabricanteService;
import br.com.pitdog.util.FacesUtil;
import br.com.pitdog.util.RequestContextUtil;

@Named
@ViewScoped
public class FabricanteController implements Serializable {

	private static final long serialVersionUID = -7737649918693147045L;

	private Fabricante fabricante;

	private List<Fabricante> fabricantes;

	@Inject
	private FabricanteService fabricanteService;
	
	@PostConstruct
	public void init(){
		novo();
	}

	public Situacao[] getSituacoes() {
		return Situacao.values();
	}

	public void pesquisar() {
		this.fabricantes = new ArrayList<Fabricante>();
		this.fabricantes = fabricanteService.pesquisarFabricante(fabricante);
	}

	public void novo() {
		this.fabricante = new Fabricante();
		this.fabricantes = new ArrayList<Fabricante>();
	}

	public void salvar() {
		try {
			fabricanteService.salvar(fabricante);
			FacesUtil.mensagemInfo("Fabricante salvo com sucesso!");
			novaListaFabricante();
			fecharDialogs();
		} catch (RuntimeException e) {
			FacesUtil.mensagemErro(e.getMessage());
		}
	}

	public void fecharDialogs() {
		RequestContextUtil.execute("PF('dialogNovoFabricante').hide();");
		RequestContextUtil.execute("PF('dialogEditarFabricante').hide();");
	}

	public void novaListaFabricante() {
		novo();
	}
	
	public void setarFabricante(Fabricante fabricante){
		this.fabricante = fabricante;
	}

	public Fabricante getFabricante() {
		return fabricante == null ? new Fabricante() : fabricante;
	}

	public void setFabricante(Fabricante fabricante) {
		this.fabricante = fabricante;
	}

	public List<Fabricante> getFabricantes() {
		return fabricantes;
	}

	public void setFabricantes(List<Fabricante> fabricantes) {
		this.fabricantes = fabricantes;
	}

}
