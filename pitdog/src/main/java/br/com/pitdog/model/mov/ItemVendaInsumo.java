package br.com.pitdog.model.mov;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.pitdog.model.estoque.Produto;
import br.com.sysge.infraestrutura.dao.GenericDomain;

@Entity
@Table(name = "tbl_item_venda_insumo")
public class ItemVendaInsumo extends GenericDomain{

	private static final long serialVersionUID = -654827184510643503L;

	@ManyToOne(fetch=FetchType.EAGER)
	private Produto insumo;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private ItemVenda itemVenda;

	public Produto getInsumo() {
		return insumo;
	}

	public void setInsumo(Produto insumo) {
		this.insumo = insumo;
	}

	public ItemVenda getItemVenda() {
		return itemVenda;
	}

	public void setItemVenda(ItemVenda itemVenda) {
		this.itemVenda = itemVenda;
	}
	
	
}
