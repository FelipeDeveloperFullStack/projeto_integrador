package br.com.pitdog.model.global;


import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import br.com.pitdog.model.type.Situacao;
import br.com.sysge.infraestrutura.dao.GenericDomain;

@Entity
@Table(name = "tbl_funcao")
public class Funcao extends GenericDomain{

	private static final long serialVersionUID = -5231540423162420798L;
	
	private String funcao;
	
	@Enumerated(EnumType.STRING)
	private Situacao situacao;

	public String getFuncao() {
		return funcao;
	}

	public void setFuncao(String funcao) {
		this.funcao = funcao;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}
	
	

}
