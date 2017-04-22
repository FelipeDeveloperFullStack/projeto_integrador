package br.com.pitdog.model.estoque;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.sysge.infraestrutura.dao.GenericDomain;

@Entity
@Table(name = "tbl_estoque_minimo_ideal")
public class EstoqueMinimoIdeal extends GenericDomain{
	
	private static final long serialVersionUID = 5495713565664692215L;

	@OneToOne(fetch =FetchType.EAGER)
	private Produto produto;
	
	private Long quantidade = 1L;
	
	@OneToOne(fetch = FetchType.EAGER)
	private EstoqueIdeal estoqueIdeal;
	
	public Produto getProduto() {
		return produto == null ? new Produto() : this.produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}

	public EstoqueIdeal getEstoqueIdeal() {
		return estoqueIdeal == null ? new EstoqueIdeal() : this.estoqueIdeal;
	}

	public void setEstoqueIdeal(EstoqueIdeal estoqueIdeal) {
		this.estoqueIdeal = estoqueIdeal;
	}
	
	

}
