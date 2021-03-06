package br.com.pitdog.model.estoque;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.pitdog.model.type.Situacao;
import br.com.sysge.infraestrutura.dao.GenericDomain;

@Entity
@Table(name = "tbl_estoque_ideal")
public class EstoqueIdeal extends GenericDomain{
	
	private static final long serialVersionUID = 3517563996442092854L;

	private String diaSemana;
	
	@Temporal(TemporalType.DATE)
	private Date dataSemana;
	
	@Enumerated(EnumType.STRING)
	private Situacao situacao;

	public String getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana(String diaSemana) {
		this.diaSemana = diaSemana;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	public Date getDataSemana() {
		return dataSemana;
	}

	public void setDataSemana(Date dataSemana) {
		this.dataSemana = dataSemana;
	}
	
	
	
}
