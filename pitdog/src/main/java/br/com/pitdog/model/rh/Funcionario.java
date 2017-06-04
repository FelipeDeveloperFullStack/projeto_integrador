package br.com.pitdog.model.rh;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.pitdog.model.global.Funcao;
import br.com.pitdog.model.type.Situacao;
import br.com.sysge.infraestrutura.dao.GenericDomain;

@Entity
@Table(name = "tbl_funcionario")
public class Funcionario extends GenericDomain{

	private static final long serialVersionUID = -3257275101789304500L;

	private String nome;
	
	@OneToOne(fetch = FetchType.EAGER)
	private Funcao funcao;
	
	@Enumerated(EnumType.STRING)
	private Situacao situacao;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Funcao getFuncao() {
		return funcao == null ? new Funcao() : this.funcao;
	}

	public void setFuncao(Funcao funcao) {
		this.funcao = funcao;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	@Override
	public String toString() {
		return "[nome=" + nome + ", funcao=" + funcao + ", situacao=" + situacao + "]";
	}

	
}
