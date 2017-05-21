package br.com.pitdog.model.mov;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.pitdog.model.estoque.Produto;
import br.com.pitdog.model.mov.type.UnidadeMedida;
import br.com.sysge.infraestrutura.dao.GenericDomain;

@Entity
@Table(name = "tbl_item_pedido")
public class ItemPedido extends GenericDomain{

	private static final long serialVersionUID = 5481547816769339488L;

	@OneToOne(fetch = FetchType.EAGER)
	private Produto produto;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pedido_id")
	private Pedido pedido;
	
	private double quantidade;
	
	@Enumerated(EnumType.STRING)
	private UnidadeMedida unidadeMedida;

	private BigDecimal preco = BigDecimal.ZERO;

	private BigDecimal desconto = BigDecimal.ZERO;

	public Produto getProduto() {
		return produto == null ? new Produto() : this.produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Pedido getPedido() {
		return pedido == null ? new Pedido() : this.pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public UnidadeMedida getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

	public double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(double quantidade) {
		this.quantidade = quantidade;
	}
	
	

}
