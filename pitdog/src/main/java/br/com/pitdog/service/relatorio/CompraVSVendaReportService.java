package br.com.pitdog.service.relatorio;

import java.util.List;

import javax.inject.Inject;

import br.com.pitdog.model.mov.ItemPedido;
import br.com.pitdog.model.mov.ItemVenda;
import br.com.pitdog.model.mov.Venda;
import br.com.pitdog.model.mov.type.TipoPE;
import br.com.pitdog.service.mov.ItemPedidoService;
import br.com.pitdog.service.mov.VendaService;

public class CompraVSVendaReportService {
	
	@Inject
	private VendaService vendaService;
	
	@Inject
	private ItemPedidoService itemPedidoService;
	
	public void gerarRelatorioCompraVSVenda(){
		for(Venda v : vendaService.findAll()){
			for(ItemVenda item : v.getItens()){
				List<ItemPedido> itensPedido = itemPedidoService.findByListProperty(item.getProduto(), "produto");
				for(ItemPedido itemPedido : itensPedido){
					if(itemPedido.getPedido().getTipoPe() == TipoPE.ENTRADA){
						
					}
				}
			}
		}
	}

}
