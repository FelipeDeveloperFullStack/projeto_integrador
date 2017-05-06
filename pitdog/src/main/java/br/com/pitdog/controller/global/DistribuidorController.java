package br.com.pitdog.controller.global;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pitdog.model.global.Distribuidor;
import br.com.pitdog.model.type.Situacao;
import br.com.pitdog.model.type.UnidadeFederativa;
import br.com.pitdog.service.global.DistribuidorService;
import br.com.pitdog.util.FacesUtil;
import br.com.pitdog.util.RequestContextUtil;

@Named
@ViewScoped
public class DistribuidorController implements Serializable{

	private static final long serialVersionUID = -2621380932032617211L;
	
	private Distribuidor distribuidor;
	
	@Inject
	private DistribuidorService distribuidorService;

	private List<Distribuidor> distribuidores;
	
	@PostConstruct
	public void init(){
		novo();
	}
	
	public Situacao[] getSituacoes(){
		return Situacao.values();
	}
	public UnidadeFederativa[] getUnidadeFederativas(){
		return UnidadeFederativa.values();
	}
	
	public void novo(){
		this.distribuidor = new Distribuidor();
		this.distribuidores = new ArrayList<Distribuidor>();
	}
	
	public void pesquisar(){
		this.distribuidores = new ArrayList<Distribuidor>();
		this.distribuidores = distribuidorService.pesquisarDistribuidor(distribuidor);
	}
	
	public void fecharDialogs(){
		RequestContextUtil.execute("PF('dialogNovoDistribuidor').hide();");
		RequestContextUtil.execute("PF('dialogEditarDistribuidor').hide();");
	}
	
	public void salvar(){
		try {
			distribuidorService.salvar(distribuidor);
			FacesUtil.mensagemInfo("Distribuidor salvo com sucesso!");
			fecharDialogs();
			novo();
		} catch (RuntimeException e) {
			FacesUtil.mensagemErro(e.getMessage());
		}
	}
	
	public void setarDistribuidor(Distribuidor distribuidor){
		this.distribuidor = distribuidor;
	}

	public Distribuidor getDistribuidor() {
		return distribuidor == null ? new Distribuidor() : this.distribuidor;
	}

	public void setDistribuidor(Distribuidor distribuidor) {
		this.distribuidor = distribuidor;
	}

	public List<Distribuidor> getDistribuidores() {
		if(distribuidores.isEmpty()){
			return listarDistribuidoresAtivos();
		}
		return distribuidores;
	}

	public void setDistribuidores(List<Distribuidor> distribuidores) {
		this.distribuidores = distribuidores;
	}
	
	public List<Distribuidor> listarDistribuidoresAtivos(){
		return distribuidores = distribuidorService.findBySituation(Situacao.ATIVO);
	}
	
	
}
