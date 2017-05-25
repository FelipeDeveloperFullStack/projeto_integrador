package br.com.pitdog.controller.mov;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pitdog.model.mov.Venda;
import br.com.pitdog.service.mov.VendaService;

@Named
@ViewScoped
public class VendaListController implements Serializable{

	private static final long serialVersionUID = 8644665195188273211L;

	

	@Inject
	private VendaService vendaService;
	
	@Inject
	private VendaController vendaController;

	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyyy");
	
	private List<Venda> vendas = new ArrayList<>();
	
	private String cliente;
	
	private Date dataInicio;
	
	private Date dataFim;
	
	public void pesquisar(){
		vendas = vendaService.listarVenda(cliente, dataInicio, dataFim);
	}
	
	public void editar(Venda venda) throws IOException{
		venda = vendaService.findById(venda.getId());
		vendaController.setVenda(venda);
		FacesContext.getCurrentInstance().getExternalContext().redirect("p_venda.xhtml");
	}
	
	public void addVenda() throws IOException{
		vendaController.novaVenda();
		FacesContext.getCurrentInstance().getExternalContext().redirect("p_venda.xhtml");
	}

	public List<Venda> getVendas() {
		return vendas;
	}

	public void setVendas(List<Venda> vendas) {
		this.vendas = vendas;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
	
	
	
}
