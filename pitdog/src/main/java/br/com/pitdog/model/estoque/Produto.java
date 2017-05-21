package br.com.pitdog.model.estoque;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;


import br.com.pitdog.model.global.Categoria;
import br.com.pitdog.model.type.Situacao;
import br.com.pitdog.model.type.TipoProduto;
import br.com.sysge.infraestrutura.dao.GenericDomain;

@Entity
@Table(name = "tbl_produto")
public class Produto extends GenericDomain{

	private static final long serialVersionUID = -7328127398997221454L;

	private String descricaoProduto;
	
	private BigDecimal valorCusto = BigDecimal.ZERO;
	
	private BigDecimal valorVenda = BigDecimal.ZERO;
	
	private double quantidadeEstoque;
	
	@OneToOne(fetch = FetchType.EAGER)
	private Categoria categoria;
	
	@OneToOne(fetch = FetchType.EAGER)
	private Fabricante fabricante;
	
	@Enumerated(EnumType.STRING)
	private Situacao situacao;
	
	@Enumerated(EnumType.STRING)
	private TipoProduto tipo;
	
	private boolean mostrarEstoqueMinimoTelaInicial;

	public String getDescricaoProduto() {
		return descricaoProduto;
	}

	public void setDescricaoProduto(String descricaoProduto) {
		this.descricaoProduto = descricaoProduto;
	}

	public BigDecimal getValorCusto() {
		return valorCusto;
	}

	public void setValorCusto(BigDecimal valorCusto) {
		this.valorCusto = valorCusto;
	}

	public BigDecimal getValorVenda() {
		return valorVenda;
	}

	public void setValorVenda(BigDecimal valorVenda) {
		this.valorVenda = valorVenda;
	}

	public Categoria getCategoria() {
		if(categoria == null){
			categoria = new Categoria();
		}
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	public boolean isMostrarEstoqueMinimoTelaInicial() {
		return mostrarEstoqueMinimoTelaInicial;
	}

	public void setMostrarEstoqueMinimoTelaInicial(boolean mostrarEstoqueMinimoTelaInicial) {
		this.mostrarEstoqueMinimoTelaInicial = mostrarEstoqueMinimoTelaInicial;
	}

	public Fabricante getFabricante() {
		return fabricante;
	}

	public void setFabricante(Fabricante fabricante) {
		this.fabricante = fabricante;
	}

	public TipoProduto getTipo() {
		return tipo;
	}

	public void setTipo(TipoProduto tipo) {
		this.tipo = tipo;
	}

	public double getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(double quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}
	
	
}
