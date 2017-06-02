package br.com.pitdog.model.mov;


import java.math.BigDecimal;
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

import br.com.pitdog.model.global.Cliente;
import br.com.pitdog.model.mov.type.StatusVenda;
import br.com.pitdog.model.rh.Funcionario;
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
	
	private BigDecimal valorLiquido = BigDecimal.ZERO;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Funcionario balconista;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Cliente cliente;
	
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

	public Funcionario getBalconista() {
		return balconista;
	}

	public void setBalconista(Funcionario balconista) {
		this.balconista = balconista;
	}

	public BigDecimal getValorLiquido() {
		return valorLiquido;
	}

	public void setValorLiquido(BigDecimal valorLiquido) {
		this.valorLiquido = valorLiquido;
	}

	public List<ItemVenda> getItens() {
		return itens;
	}

	public void setItens(List<ItemVenda> itens) {
		this.itens = itens;
	}

	public StatusVenda getStatus() {
		return status;
	}

	public void setStatus(StatusVenda status) {
		this.status = status;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public void totalizar() {
		valorLiquido = BigDecimal.ZERO;
		
		for (ItemVenda itemVenda : itens) {
			valorLiquido = valorLiquido.add(itemVenda.getValorLiquido());
		}
	}
	
	public void validarCLiente(){
		if(cliente == null){
			throw new RuntimeException("Selecione um cliente!");
		}
	}
	
	public void validarItens(){
		if(itens == null || itens.isEmpty()){
			throw new RuntimeException("Adicione ao menos um item para a venda!");
		}
	}
	
	
}
