package br.com.pitdog.model.estoque;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import br.com.pitdog.model.type.Situacao;
import br.com.sysge.infraestrutura.dao.GenericDomain;

@Entity
@Table(name = "tbl_fabricante")
public class Fabricante extends GenericDomain{

	private static final long serialVersionUID = -4676080896389590427L;
	
	private String razaoSocial;
	
	private String cnpj;
	
	@Enumerated(EnumType.STRING)
	private Situacao situacao;

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}
	
	

}
