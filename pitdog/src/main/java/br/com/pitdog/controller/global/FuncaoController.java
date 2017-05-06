package br.com.pitdog.controller.global;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pitdog.model.global.Funcao;
import br.com.pitdog.model.type.Situacao;
import br.com.pitdog.service.global.FuncaoService;
import br.com.pitdog.util.FacesUtil;
import br.com.pitdog.util.RequestContextUtil;

@Named
@ViewScoped
public class FuncaoController implements Serializable {

	private static final long serialVersionUID = 4665513466161297424L;

	private Funcao funcao;

	@Inject
	private FuncaoService funcaoService;

	private List<Funcao> funcoes;

	@PostConstruct
	public void init() {
		novo();
	}

	public Situacao[] getSituacoes() {
		return Situacao.values();
	}

	public void novo() {
		this.funcao = new Funcao();
		this.funcoes = new ArrayList<Funcao>();
	}

	public void pesquisar() {
		this.funcoes = new ArrayList<Funcao>();
		this.funcoes = funcaoService.pesquisarFuncao(funcao);
	}

	public void fecharDialogs() {
		RequestContextUtil.execute("PF('dialogNovoFuncao').hide();");
		RequestContextUtil.execute("PF('dialogEditarFuncao').hide();");
	}

	public void salvar() {
		try {
			funcaoService.salvar(funcao);
			FacesUtil.mensagemInfo("Função salvo com sucesso!");
			fecharDialogs();
			novo();
		} catch (RuntimeException e) {
			FacesUtil.mensagemErro(e.getMessage());
		}
	}

	public void setarFuncao(Funcao funcao) {
		this.funcao = funcao;
	}

	public Funcao getFuncao() {
		return funcao == null ? new Funcao() : this.funcao;
	}

	public void setFuncao(Funcao funcao) {
		this.funcao = funcao;
	}

	public List<Funcao> getFuncoes() {
		if(funcoes.isEmpty()){
			return listarFuncoesAtivos();
		}
		return funcoes;
	}

	public void setFuncoes(List<Funcao> funcoes) {
		this.funcoes = funcoes;
	}
	
	public List<Funcao> listarFuncoesAtivos(){
		return funcoes = funcaoService.findBySituation(Situacao.ATIVO);
	}

}
