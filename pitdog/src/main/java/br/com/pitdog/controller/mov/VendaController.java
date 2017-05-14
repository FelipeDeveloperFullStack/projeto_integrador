package br.com.pitdog.controller.mov;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pitdog.model.mov.ItemVenda;
import br.com.pitdog.model.mov.Venda;
import br.com.pitdog.service.estoque.ComposicaoService;
import br.com.pitdog.service.estoque.ProdutoService;
import br.com.pitdog.service.mov.VendaService;

@Named
@ViewScoped
public class VendaController implements Serializable{

	private static final long serialVersionUID = 8644665195188273211L;

	private Venda venda;

	private ItemVenda itemVenda;

	@Inject
	private VendaService vendaService;

	@Inject
	private ProdutoService produtoService;
	
	@Inject
	private ComposicaoService composicaoService;

	@PostConstruct
	public void init() {
		novo();
	}

	public void novo() {
		this.itemVenda = new ItemVenda();
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public ItemVenda getItemVenda() {
		return itemVenda;
	}

	public void setItemVenda(ItemVenda itemVenda) {
		this.itemVenda = itemVenda;
	}
	
}
