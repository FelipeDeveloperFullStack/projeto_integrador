package br.com.pitdog.controller.rh;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pitdog.model.global.Funcao;
import br.com.pitdog.model.rh.Funcionario;
import br.com.pitdog.model.type.Situacao;
import br.com.pitdog.service.global.FuncaoService;
import br.com.pitdog.service.rh.FuncionarioService;
import br.com.pitdog.util.FacesUtil;
import br.com.pitdog.util.RequestContextUtil;

@Named
@ViewScoped
public class FuncionarioController implements Serializable{

	private static final long serialVersionUID = -6386708531534319371L;
	
	private Funcionario funcionario;
	
	private List<Funcionario> funcionarios;
	
	@SuppressWarnings("unused")
	private List<Funcao> funcoes;
	
	@Inject
	private FuncaoService funcaoService;
	
	@Inject
	private FuncionarioService funcionarioService;
	
	@PostConstruct
	public void init(){
		this.funcionarios = new ArrayList<Funcionario>();
		novo();
	}
	
	public void novo(){
		this.funcionario = new Funcionario();
		this.funcoes = new ArrayList<Funcao>();
	}
	
	public void setarFuncionario(Funcionario funcionario){
		this.funcionario = funcionario;
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

	public List<Funcionario> getFuncionarios() {
		if(funcionarios.isEmpty()){
			return listarFuncionariosAtivos();
		}
		return funcionarios;
	}

	public void setFuncionarios(List<Funcionario> funcionarios) {
		this.funcionarios = funcionarios;
	}

	public Situacao[] getSituacoes(){
		return Situacao.values();
	}

	public List<Funcao> getFuncoes() {
		return funcaoService.findBySituation(Situacao.ATIVO);
	}
	
	public List<Funcionario> listarFuncionariosAtivos(){
		return funcionarios = funcionarioService.findBySituation(Situacao.ATIVO);
	}

}
