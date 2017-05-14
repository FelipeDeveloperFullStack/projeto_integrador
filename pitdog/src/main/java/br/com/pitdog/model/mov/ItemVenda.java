package br.com.pitdog.model.mov;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.pitdog.model.estoque.Produto;
import br.com.sysge.infraestrutura.dao.GenericDomain;

@Entity
@Table(name = "tbl_item_venda")
public class ItemVenda extends GenericDomain {
	

	private static final long serialVersionUID = -1504173395650419930L;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "produto_id")
	private Produto produto;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "venda_id")
	private Venda venda;
	
	private Double quantidade = 0.0;
	
	private Double desconto = 0.0;
	
	private Double valorLiquido = 0.0;
	
	@OneToMany
	@JoinTable (name="tbl_item_venda_insumo_adicional",
		    joinColumns={ @JoinColumn(name="item_venda_id", referencedColumnName="id") },
		    inverseJoinColumns={ @JoinColumn(name="produto_id", referencedColumnName="id", unique=true) })	
	private List<Produto> insumosAdicionais = new ArrayList<>();
	
	@OneToMany
	@JoinTable (name="tbl_item_venda_insumo_removido",
		    joinColumns={ @JoinColumn(name="item_venda_id", referencedColumnName="id") },
		    inverseJoinColumns={ @JoinColumn(name="produto_id", referencedColumnName="id", unique=true) })	
	private List<Produto> insumosRemovidos = new ArrayList<>();

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public Double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}

	public Double getDesconto() {
		return desconto;
	}

	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}

	public Double getValorLiquido() {
		return valorLiquido;
	}

	public void setValorLiquido(Double valorLiquido) {
		this.valorLiquido = valorLiquido;
	}

	public List<Produto> getInsumosAdicionais() {
		return insumosAdicionais;
	}

	public void setInsumosAdicionais(List<Produto> insumosAdicionais) {
		this.insumosAdicionais = insumosAdicionais;
	}

	public List<Produto> getInsumosRemovidos() {
		return insumosRemovidos;
	}

	public void setInsumosRemovidos(List<Produto> insumosRemovidos) {
		this.insumosRemovidos = insumosRemovidos;
	}
	

}