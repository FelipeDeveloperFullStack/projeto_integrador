package br.com.pitdog.model.mov;

import java.math.BigDecimal;
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
public class ItemVenda extends GenericDomain implements Cloneable{
	

	private static final long serialVersionUID = -1504173395650419930L;
	
	private int numero;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "produto_id")
	private Produto produto;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "venda_id")
	private Venda venda;
	
	private BigDecimal quantidade = BigDecimal.ZERO;
	
	private BigDecimal desconto = BigDecimal.ZERO;
	
	private BigDecimal valorUnitario = BigDecimal.ZERO;
	
	private BigDecimal valorTotal = BigDecimal.ZERO;
	
	private BigDecimal valorLiquido = BigDecimal.ZERO;
	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="itemVenda")	
	private List<ItemVendaInsumo> insumosAdicionais = new ArrayList<>();
	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="itemVenda")
	private List<ItemVendaInsumo> insumosRemovidos = new ArrayList<>();

	
	public void calcularValores(){
		BigDecimal valorTotalInsumosRemovidos = BigDecimal.ZERO;
		BigDecimal valorTotalInsumosAdicionais = BigDecimal.ZERO;
		
		for (ItemVendaInsumo insumo : insumosRemovidos) {
			valorTotalInsumosRemovidos = valorTotalInsumosRemovidos.add(insumo.getInsumo().getValorVenda());
		}
		
		for (ItemVendaInsumo insumo : insumosAdicionais) {
			valorTotalInsumosAdicionais = valorTotalInsumosAdicionais.add(insumo.getInsumo().getValorVenda());
		}
		
		valorUnitario = valorUnitario.add(valorTotalInsumosAdicionais);
		valorUnitario = valorUnitario.subtract(valorTotalInsumosRemovidos);
		
		valorTotal = getValorUnitario().multiply(getQuantidade());
		if(BigDecimal.ZERO.equals(valorLiquido)){
			valorLiquido = valorTotal;
		}else{
			BigDecimal valorDesconto = valorTotal.divide(new BigDecimal(100)).multiply(desconto);
			valorLiquido = valorTotal.subtract(valorDesconto);
		}
	}
	
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

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public BigDecimal getValorLiquido() {
		return valorLiquido;
	}

	public void setValorLiquido(BigDecimal valorLiquido) {
		this.valorLiquido = valorLiquido;
	}

	

	public List<ItemVendaInsumo> getInsumosAdicionais() {
		return insumosAdicionais;
	}

	public void setInsumosAdicionais(List<ItemVendaInsumo> insumosAdicionais) {
		this.insumosAdicionais = insumosAdicionais;
	}

	public List<ItemVendaInsumo> getInsumosRemovidos() {
		return insumosRemovidos;
	}

	public void setInsumosRemovidos(List<ItemVendaInsumo> insumosRemovidos) {
		this.insumosRemovidos = insumosRemovidos;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}
	
	@Override
	public ItemVenda clone(){
		try {
			return (ItemVenda) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
	
}
