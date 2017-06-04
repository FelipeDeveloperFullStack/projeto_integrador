package br.com.pitdog.controller.relatorio;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pitdog.model.global.Cliente;
import br.com.pitdog.model.mov.Venda;
import br.com.pitdog.model.mov.type.StatusVenda;
import br.com.pitdog.model.rh.Funcionario;
import br.com.pitdog.model.type.Situacao;
import br.com.pitdog.service.global.ClienteService;
import br.com.pitdog.service.mov.VendaService;
import br.com.pitdog.service.rh.FuncionarioService;
import br.com.pitdog.util.FacesUtil;

@Named
@ViewScoped
public class RelatorioController implements Serializable{

	private static final long serialVersionUID = 5055090123363554350L;
	
	@Inject
	private ClienteService clienteService;
	
	@Inject
	private FuncionarioService funcionarioService;
	
	@Inject
	private VendaService vendaService;
	
	private Cliente cliente;
	
	private Funcionario funcionario;
	
	private Venda venda;
	
	private Date dataInicial;
	
	private Date dataFinal;
	
	@PostConstruct
	public void init(){
		cliente = new Cliente();
		funcionario = new Funcionario();
		venda = new Venda();
	}
	
	public List<Cliente> getClientes(){
		return clienteService.findBySituation(Situacao.ATIVO);
	}
	
	public List<Funcionario> getFuncionarios(){
		return funcionarioService.findBySituation(Situacao.ATIVO);
	}
	
	public List<StatusVenda> getStatusVenda(){
		return vendaService.buscarStatusVenda();
	}
	
	public void gerarRelatorioVendaPorPeriodo(){
		try {
			vendaService.gerarRelatorioVendaPorPeriodo(funcionario.getNome(),
					cliente.getNomeDaPessoaFisica(), venda.getStatus(), dataInicial, dataFinal);
		} catch (RuntimeException e) {
			FacesUtil.mensagemErro(e.getMessage());
		}
	}
	
	public void gerarRelatorioCliente(){
		try {
			clienteService.gerarRelatorioCliente();
		} catch (RuntimeException e) {
			FacesUtil.mensagemErro(e.getMessage());
		}
	}

	public Cliente getCliente() {
		return cliente;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

}
