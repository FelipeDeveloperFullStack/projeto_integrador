package br.com.sysge.infraestrutura.reports;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ReportServlet" , urlPatterns = {"/ReportServlet"})
public class ReportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ReportServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		response.setHeader("application/pdf", "Content-Type");  
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline;");
		
		HttpSession session = request.getSession(true);
		Object teste = session.getAttribute("teste");
		System.out.println(teste);
		
		//String titulo = "pedido.pdf";
		
		/*response.reset();
		response.setContentType("application/force-download");
		response.setHeader("Content-Disposition", "attachment;filename=\""+ titulo + "\";");
		FacesContext.getCurrentInstance().responseComplete();*/
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
