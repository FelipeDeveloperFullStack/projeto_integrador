package br.com.pitdog.controller.relatorio;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.pitdog.model.mov.Venda;
import br.com.pitdog.service.mov.VendaService;


@WebServlet("/pages/mov/compomNaoFiscalVenda")
public class CompomNaoFiscalVenda extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@Inject
	private VendaService service;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
   
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Venda venda = service.findById(Long.parseLong(request.getParameter("venda")));
		if(venda == null) return;
		
		StringBuilder builder = new StringBuilder();
		builder.append("<html>");
		builder.append("Venda: " + venda.getNumero());
		builder.append(" Cliente: " + venda.getCliente().getNomeDaPessoaFisica());
		builder.append("<br/>");
		builder.append("Endereço: ");
		builder.append(venda.getCliente().getLogradouro() + " ");
		builder.append(venda.getCliente().getBairro() + " ");
		builder.append(venda.getCliente().getCidade() + " ");
		builder.append("<br/>");
		builder.append("Telefone: ");
		builder.append(venda.getCliente().getTelefone());
		builder.append("<br/>");
		builder.append("______________________Itens______________________");
		builder.append("<br/>");
		venda.getItens().forEach((item)  ->{
			builder.append("________________________________________________");
			builder.append("<br/>");
			builder.append((item.getNumero() + 1) + " : " + item.getProduto().getDescricaoProduto());
			builder.append("<br/>");
			builder.append("Valor " + item.getValorLiquido());
			builder.append("<br/>");
			if(!item.getInsumosAdicionais().isEmpty()){
				builder.append("Adicionais: ");
			}
			item.getInsumosAdicionais().forEach((adicional)  ->{
				builder.append(adicional.getInsumo().getDescricaoProduto());
				builder.append(", ");
			}); 
			builder.append("<br/>");
			if(!item.getInsumosRemovidos().isEmpty()){
				builder.append("Remover: ");
			}
			
			item.getInsumosRemovidos().forEach((adicional) ->{
				builder.append(adicional.getInsumo().getDescricaoProduto());
				builder.append(", ");
			}); 
			
		});
		builder.append("<br/>");
		builder.append("Data: " + dateFormat.format(venda.getData()));
		builder.append(" Total: " + venda.getValorLiquido());
		
		builder.append("<br/>");
		builder.append("<script>window.print()</script>");
		builder.append("</html>");
		
		response.getWriter().append(builder.toString());
	}

}
