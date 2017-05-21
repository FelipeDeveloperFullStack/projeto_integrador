package br.com.pitdog.relatorios.to;

import java.io.Serializable;
import java.math.BigDecimal;


public class PedidoReportTO implements Serializable{

	private static final long serialVersionUID = 6909102204559828074L;
	
	private String produto;
	
	private String fabricante;
	
	private double quantidade;
	
	private String unidadeMedida;
	
	private BigDecimal preco;
	
	private BigDecimal desconto;

	public String getProduto() {
		return produto;
	}

	public void setProduto(String produto) {
		this.produto = produto;
	}

	public String getFabricante() {
		return fabricante;
	}

	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}

	public double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(double quantidade) {
		this.quantidade = quantidade;
	}

	public String getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(String unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
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
	
	

}
