package br.com.pitdog.model.mov;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.pitdog.model.conf.Usuario;
import br.com.pitdog.model.mov.type.StatusVenda;
import br.com.sysge.infraestrutura.dao.GenericDomain;

@Entity
@Table(name = "tbl_venda")
public class Venda extends GenericDomain{
	
	private static final long serialVersionUID = -997149987670519720L;

	private int numero = 0;
	
	@Enumerated(EnumType.STRING)
	private StatusVenda status = StatusVenda.DIGITACAO;
	
	@Temporal(TemporalType.DATE)
	private Date data = new Date();
	
	private Double valorLiquido = 0.0;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Usuario balconista;
	
	@OneToMany(mappedBy = "venda", fetch = FetchType.EAGER, targetEntity = ItemVenda.class, cascade=CascadeType.ALL)
	private List<ItemVenda> itens = new ArrayList<>();

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Usuario getBalconista() {
		return balconista;
	}

	public void setBalconista(Usuario balconista) {
		this.balconista = balconista;
	}

	public Double getValorLiquido() {
		return valorLiquido;
	}

	public void setValorLiquido(Double valorLiquido) {
		this.valorLiquido = valorLiquido;
	}

	public List<ItemVenda> getItens() {
		return itens;
	}

	public void setItens(List<ItemVenda> itens) {
		this.itens = itens;
	}
	
}
