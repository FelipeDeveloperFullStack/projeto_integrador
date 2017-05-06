package br.com.pitdog.model.estoque;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.sysge.infraestrutura.dao.GenericDomain;

@Entity
@Table(name = "tbl_composicao")
public class Composicao extends GenericDomain {

	private static final long serialVersionUID = -3548720830154789431L;

	@OneToOne(fetch = FetchType.EAGER)
	private Produto produto;

	@OneToOne(fetch = FetchType.EAGER)
	private Produto insumo;

	@Column(name = "comp_qtde")
	private double quantidade;

	public double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(double quantidade) {
		this.quantidade = quantidade;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Produto getInsumo() {
		return insumo;
	}

	public void setInsumo(Produto insumo) {
		this.insumo = insumo;
	}

}
