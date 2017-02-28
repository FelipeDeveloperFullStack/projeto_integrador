package br.com.pitdog.model.mov;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.pitdog.model.estoque.Produto;
import br.com.pitdog.model.global.Distribuidor;
import br.com.pitdog.model.type.Situacao;
import br.com.sysge.infraestrutura.dao.GenericDomain;

@Entity
@Table(name = "tbl_pedido")
public class Pedido extends GenericDomain {

	private static final long serialVersionUID = 2751426098729464001L;

	private String descricaoPedido;

	@Temporal(TemporalType.DATE)
	private Date dataPedido = Calendar.getInstance().getTime();

	@OneToOne(fetch = FetchType.EAGER)
	private Distribuidor distribuidor;
	
	@OneToOne(fetch = FetchType.EAGER)
	private Produto produto;
	
	private long quantidade = 1L;;
	
	private BigDecimal preco = BigDecimal.ZERO;
	
	private BigDecimal desconto = BigDecimal.ZERO;
	
	@Enumerated(EnumType.STRING)
	private Situacao situacao;

	public String getDescricaoPedido() {
		return descricaoPedido;
	}

	public void setDescricaoPedido(String descricaoPedido) {
		this.descricaoPedido = descricaoPedido;
	}

	public Date getDataPedido() {
		return dataPedido;
	}

	public void setDataPedido(Date dataPedido) {
		this.dataPedido = dataPedido;
	}

	public Distribuidor getDistribuidor() {
		return distribuidor == null ? new Distribuidor() : this.distribuidor;
	}

	public void setDistribuidor(Distribuidor distribuidor) {
		this.distribuidor = distribuidor;
	}

	public Produto getProduto() {
		return produto == null ? new Produto() : this.produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(long quantidade) {
		this.quantidade = quantidade;
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

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}
	
	

}
