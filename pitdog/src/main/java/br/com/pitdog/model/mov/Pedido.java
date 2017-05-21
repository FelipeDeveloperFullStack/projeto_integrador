package br.com.pitdog.model.mov;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.pitdog.model.global.Distribuidor;
import br.com.pitdog.model.mov.type.TipoPE;
import br.com.pitdog.model.type.Situacao;
import br.com.sysge.infraestrutura.dao.GenericDomain;

@Entity
@Table(name = "tbl_pedido")
public class Pedido extends GenericDomain {

	private static final long serialVersionUID = 2751426098729464001L;

	@Temporal(TemporalType.DATE)
	private Date dataPedido;

	@Temporal(TemporalType.DATE)
	private Date dataEntrada;
	
	private String numeroNotaFiscal;
	
	@OneToOne(fetch = FetchType.EAGER)
	private Distribuidor distribuidor;
	
	@OneToMany(mappedBy = "pedido", fetch = FetchType.EAGER, targetEntity = ItemPedido.class)
	private List<ItemPedido> itensPedidos;

	@Enumerated(EnumType.STRING)
	private TipoPE tipoPe;
	
	@Enumerated(EnumType.STRING)
	private Situacao situacao;

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

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	public String getNumeroNotaFiscal() {
		return numeroNotaFiscal;
	}

	public void setNumeroNotaFiscal(String numeroNotaFiscal) {
		this.numeroNotaFiscal = numeroNotaFiscal;
	}

	public Date getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(Date dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

	public List<ItemPedido> getItensPedidos() {
		return itensPedidos;
	}

	public void setItensPedidos(List<ItemPedido> itensPedidos) {
		this.itensPedidos = itensPedidos;
	}

	public TipoPE getTipoPe() {
		return tipoPe;
	}

	public void setTipoPe(TipoPE tipoPe) {
		this.tipoPe = tipoPe;
	}
	
	

}
