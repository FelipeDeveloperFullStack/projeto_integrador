package br.com.pitdog.model.global;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import br.com.pitdog.model.type.Situacao;
import br.com.sysge.infraestrutura.dao.GenericDomain;

@Entity
@Table(name = "tbl_categoria")
public class Categoria extends GenericDomain{

	private static final long serialVersionUID = 5472076166102918854L;
	
	private String categoria;
	
	private String sigla;
	
	@Enumerated(EnumType.STRING)
	private Situacao situacao;

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}
	
	

}
