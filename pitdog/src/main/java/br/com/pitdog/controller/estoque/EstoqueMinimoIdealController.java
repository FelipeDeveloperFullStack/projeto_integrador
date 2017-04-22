package br.com.pitdog.controller.estoque;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pitdog.model.estoque.EstoqueIdeal;
import br.com.pitdog.model.estoque.EstoqueMinimoIdeal;
import br.com.pitdog.model.estoque.Produto;
import br.com.pitdog.model.type.Situacao;
import br.com.pitdog.service.estoque.EstoqueIdealService;
import br.com.pitdog.service.estoque.EstoqueMinimoIdealService;
import br.com.pitdog.service.estoque.ProdutoService;
import br.com.pitdog.util.DateUtil;
import br.com.pitdog.util.FacesUtil;
import br.com.pitdog.util.RequestContextUtil;

@Named
@ViewScoped
public class EstoqueMinimoIdealController implements Serializable{

	private static final long serialVersionUID = -4477246533592852830L;
	
	private List<EstoqueMinimoIdeal> listaEstoqueMinimo;
	
	private List<EstoqueIdeal> listaEstoqueMinimoIdealPrincipal;
	
	private EstoqueMinimoIdeal estoqueMinimoIdeal;
	
	private boolean desabilitarDiaSemana;
	
	private Date diaSemana;
	
	@Inject
	private ProdutoService produtoService;
	
	@Inject
	private EstoqueMinimoIdealService estoqueMinimoService;

	@Inject	
	private EstoqueIdealService estoqueIdealService;
	
	@PostConstruct
	public void init(){
		novo();
	}
	
	public void novo(){
		this.estoqueMinimoIdeal = new EstoqueMinimoIdeal();
		this.estoqueMinimoIdeal.setEstoqueIdeal(new EstoqueIdeal());
		this.listaEstoqueMinimo = new ArrayList<EstoqueMinimoIdeal>();
		this.listaEstoqueMinimoIdealPrincipal = new ArrayList<EstoqueIdeal>();
	}

	public List<EstoqueMinimoIdeal> getListaEstoqueMinimo() {
		return listaEstoqueMinimo;
	}

	public List<EstoqueIdeal> getListaEstoqueMinimoIdealPrincipal() {
		return listaEstoqueMinimoIdealPrincipal;
	}

	public List<Produto> getProdutos() {
		return produtoService.findBySituation(Situacao.ATIVO);
	}
	
	public Situacao[] getSituacoes(){
		return Situacao.values();
	}
	
	
	public EstoqueMinimoIdeal getEstoqueMinimoIdeal() {
		return estoqueMinimoIdeal == null ? new EstoqueMinimoIdeal() : this.estoqueMinimoIdeal;
	}

	public void setEstoqueMinimoIdeal(EstoqueMinimoIdeal estoqueMinimoIdeal) {
		this.estoqueMinimoIdeal = estoqueMinimoIdeal;
	}
	
	public Date getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana(Date diaSemana) {
		this.diaSemana = diaSemana;
	}

	public void adicionarEstoque(){
		if(estoqueMinimoService.verificarSeExisteProdutoEDiaSemanaNaTabela(listaEstoqueMinimo, estoqueMinimoIdeal)){
			FacesUtil.mensagemWarn("Já existe o produto ("+estoqueMinimoIdeal.getProduto().getDescricaoProduto() 
					+ ") adicionado na lista com o dia da semana ("+estoqueMinimoIdeal.getEstoqueIdeal().getDiaSemana()+")");
		}else{
			this.listaEstoqueMinimo.add(estoqueMinimoIdeal);
			this.estoqueMinimoIdeal = new EstoqueMinimoIdeal();
			this.estoqueMinimoIdeal.setEstoqueIdeal(new EstoqueIdeal());
		}
	}
	
	public void removerEstoqueIdeal(EstoqueMinimoIdeal estoqueMinimoIdeal){
		if(estoqueMinimoIdeal.getId() == null){
			for(int i = 0; i < this.listaEstoqueMinimo.size(); i++){
				if(this.listaEstoqueMinimo.get(i).getProduto().getDescricaoProduto().trim().equals(estoqueMinimoIdeal.getProduto().getDescricaoProduto())){
					this.listaEstoqueMinimo.remove(i);
				}
			}
		}else{
			estoqueMinimoService.remove(estoqueMinimoIdeal.getId());
			this.listaEstoqueMinimo = estoqueMinimoService.findByListProperty(estoqueMinimoIdeal.getProduto().getId(), "produto.id");
		}
	}
	
	private void fecharDialogs(){
		RequestContextUtil.execute("PF('dialogNovoEstoqueIdeal').hide();");
		RequestContextUtil.execute("PF('dialogEditarEstoqueIdeal').hide();");
	}
	
	public boolean desabilitarDiaSemana(){
		return this.desabilitarDiaSemana = true;
	}
	public boolean habilitarDiaSemana(){
		return this.desabilitarDiaSemana = false;
	}

	public List<String> getDiasSemana(){
		LocalDate localDate = LocalDate.now();
		
		List<String> diasS = new ArrayList<String>();
		diasS.add(adicionarDiaSemana(localDate));
		
		LocalDate d1 = localDate.plusDays(1);
		diasS.add(adicionarDiaSemana(d1));
		
		LocalDate d2 = localDate.plusDays(2);
		diasS.add(adicionarDiaSemana(d2));
		
		LocalDate d3 = localDate.plusDays(3);
		diasS.add(adicionarDiaSemana(d3));
		
		LocalDate d4 = localDate.plusDays(4);
		diasS.add(adicionarDiaSemana(d4));
		
		LocalDate d5 = localDate.plusDays(5);
		diasS.add(adicionarDiaSemana(d5));
		
		LocalDate d6 = localDate.plusDays(6);
		diasS.add(adicionarDiaSemana(d6));
		
		LocalDate d7 = localDate.plusDays(7);
		diasS.add(adicionarDiaSemana(d7));
		
		return diasS;
	}
	
	private String adicionarDiaSemana(LocalDate localDate){
		Calendar calendar = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("MMMM"); 
		Locale BRAZIL = new Locale("pt","BR");
		df = DateFormat.getDateInstance(DateFormat.FULL, BRAZIL); 
		System.out.println(", "+calendar.get(Calendar.DAY_OF_MONTH)+" de "+calendar.get(Calendar.YEAR));
		String data = df.format(DateUtil.asDate(localDate)).replace(", "+calendar.get(Calendar.DAY_OF_MONTH)+" de "+calendar.get(Calendar.YEAR), "");
	    return data.substring(0, data.indexOf(","));
	}
	
	public boolean isDesabilitarDiaSemana() {
		return desabilitarDiaSemana;
	}
	
	public void setarEstoqueIdeal(EstoqueIdeal estoqueIdeal){
		this.listaEstoqueMinimo = estoqueMinimoService.findByListProperty(estoqueIdeal.getId(), "estoqueIdeal.id");
		for(EstoqueMinimoIdeal emi : listaEstoqueMinimo){
			this.estoqueMinimoIdeal.setEstoqueIdeal(emi.getEstoqueIdeal());
		}
	}
	
	public void salvar(){
		try {
			estoqueMinimoIdeal.setEstoqueIdeal(estoqueIdealService.salvar(estoqueMinimoIdeal.getEstoqueIdeal()));
			for(EstoqueMinimoIdeal es : listaEstoqueMinimo){
				es.setEstoqueIdeal(estoqueMinimoIdeal.getEstoqueIdeal());
				estoqueMinimoService.save(es);
			}
			fecharDialogs();
			listaEstoqueMinimoIdealPrincipal = estoqueIdealService.findBySituation(estoqueMinimoIdeal.getEstoqueIdeal().getSituacao());
		} catch (RuntimeException e) {
			FacesUtil.mensagemErro(e.getMessage());
		}
	}
	
	public void pesquisar(){
		this.listaEstoqueMinimoIdealPrincipal = new ArrayList<EstoqueIdeal>();
		this.listaEstoqueMinimoIdealPrincipal = estoqueIdealService.pesquisarEstoqueIdeal(estoqueMinimoIdeal.getEstoqueIdeal(), diaSemana);
	}
	

}
