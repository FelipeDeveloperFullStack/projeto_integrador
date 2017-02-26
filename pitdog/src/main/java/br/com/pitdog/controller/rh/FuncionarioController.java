package br.com.pitdog.controller.rh;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pitdog.model.rh.Funcionario;
import br.com.pitdog.model.type.Sexo;
import br.com.pitdog.model.type.Situacao;
import br.com.pitdog.model.type.UnidadeFederativa;
import br.com.pitdog.service.rh.FuncionarioService;
import br.com.pitdog.service.sys.WebServiceCEPService;
import br.com.pitdog.util.FacesUtil;
import br.com.pitdog.util.RequestContextUtil;

@Named
@ViewScoped
public class FuncionarioController implements Serializable{

	private static final long serialVersionUID = -6386708531534319371L;
	
	private Funcionario funcionario;
	
	protected List<Funcionario> funcionarios;
	
	@Inject
	private FuncionarioService funcionarioService;
	
	@PostConstruct
	public void init(){
		this.funcionarios = new ArrayList<Funcionario>();
		novo();
	}
	
	public void procurarCep(){
		try {
			Map<Object, Object> mapCep = new HashMap<Object, Object>();
			mapCep.putAll(WebServiceCEPService.procurarCEP(funcionario.getCep()));
			this.funcionario.setEndereco(mapCep.get(5).toString() + " " + mapCep.get(1).toString());
			this.funcionario.setCidade(mapCep.get(2).toString());
			this.funcionario.setUf(UnidadeFederativa.valueOf(mapCep.get(3).toString()));
			this.funcionario.setBairro(mapCep.get(4).toString());
		} catch (Exception e) {
			FacesUtil.mensagemErro(e.getMessage());
		}
	}
	
	private int currentTab = 0;
	
	public void novo(){
		this.funcionario = new Funcionario();
		setCurrentTab(0);
	}
	
	public void setarFuncionario(Funcionario funcionario){
		this.funcionario = funcionario;
		setCurrentTab(0);
	}
	
	public void salvar(){
		try {
			funcionarioService.salvar(funcionario);
			fecharDialogs();
			FacesUtil.mensagemInfo("Funcionário salvo com sucesso!");
			this.funcionarios = new ArrayList<Funcionario>();
		} catch (RuntimeException e) {
			FacesUtil.mensagemErro(e.getMessage());
		}
	}
	
	public void pesquisar(){
		this.funcionarios = new ArrayList<Funcionario>();
		this.funcionarios = funcionarioService.pesquisarFuncionario(funcionario);
	}
	
	public void cancelar(){
		this.funcionarios = new ArrayList<Funcionario>();
	}
	
	private void fecharDialogs(){
		RequestContextUtil.execute("PF('dialogNovoFuncionario').hide();");
		RequestContextUtil.execute("PF('dialogEditarFuncionario').hide();");
	}
	

	public Funcionario getFuncionario() {
		if(funcionario == null){
			funcionario = new Funcionario();
		}
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public int getCurrentTab() {
		return currentTab;
	}

	public void setCurrentTab(int currentTab) {
		this.currentTab = currentTab;
	}

	public List<Funcionario> getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(List<Funcionario> funcionarios) {
		this.funcionarios = funcionarios;
	}

	public UnidadeFederativa[] getUnidadesFederativa(){
		return UnidadeFederativa.values();
	}
	
	public Sexo[] getSexos(){
		return Sexo.values();
	}
	
	public Situacao[] getSituacoes(){
		return Situacao.values();
	}

}
