package br.com.pitdog.controller.global;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pitdog.model.global.Cliente;
import br.com.pitdog.model.type.Situacao;
import br.com.pitdog.service.global.ClienteService;
import br.com.pitdog.util.FacesUtil;
import br.com.pitdog.util.RequestContextUtil;

@Named
@ViewScoped
public class ClienteController implements Serializable{

	private static final long serialVersionUID = -7746205362687258155L;
	
	private Cliente cliente;
	
	private List<Cliente> clientes;
	
	@Inject
	private ClienteService clienteService;
	
	public Situacao[] getSituacoes(){
		return Situacao.values();
	}
	
	@PostConstruct
	public void init(){
		novo();
	}
	
	public void novo(){
		this.cliente = new Cliente();
		this.clientes = new ArrayList<Cliente>();
	}
	
	public void setarCliente(Cliente cliente){
		this.cliente = cliente;
	}
	
	public void pesquisar(){
		clientes = new ArrayList<Cliente>();
		clientes = clienteService.procurarCliente(cliente);
	}
	
	public void salvar(){
		try {
			cliente = clienteService.salvar(cliente);
			FacesUtil.mensagemInfo("Cliente salvo com sucesso!");
			fecharDialogs();
			clientes = clienteService.findBySituation(cliente.getSituacao());
		} catch (RuntimeException e) {
			FacesUtil.mensagemErro(e.getMessage());
		}
	}
	
	private void fecharDialogs(){
		RequestContextUtil.execute("PF('dialogNovoCliente').hide();");
		RequestContextUtil.execute("PF('dialogEditarCliente').hide();");
	}
	
	public void cancelar(){
		clientes = new ArrayList<Cliente>();
	}

	public Cliente getCliente() {
		return cliente == null ? new Cliente() : cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}
	
	

}
