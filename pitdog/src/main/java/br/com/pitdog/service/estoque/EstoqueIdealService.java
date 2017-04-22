package br.com.pitdog.service.estoque;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.pitdog.model.estoque.EstoqueIdeal;
import br.com.pitdog.model.type.Situacao;
import br.com.sysge.infraestrutura.dao.GenericDaoImpl;

public class EstoqueIdealService extends GenericDaoImpl<EstoqueIdeal, Long>{

	private static final long serialVersionUID = -4926596484151161679L;

	public EstoqueIdeal salvar(EstoqueIdeal estoqueMinimoIdeal){
		try {
			return super.save(consistirEstoqueIdeal(estoqueMinimoIdeal));
		} catch (RuntimeException e) {
			throw new RuntimeException();
		}
	}
	
	private EstoqueIdeal consistirEstoqueIdeal(EstoqueIdeal estoqueMinimoIdeal){
		if(estoqueMinimoIdeal.getId() == null){
			estoqueMinimoIdeal.setSituacao(Situacao.ATIVO);
		}
		return estoqueMinimoIdeal;
	}
	
	public List<EstoqueIdeal> pesquisarEstoqueIdeal(EstoqueIdeal estoqueMinimoIdeal, Date diaSemana){
		try {
			if(diaSemana == null){
				return super.findBySituation(estoqueMinimoIdeal.getSituacao());
			}else{
				estoqueMinimoIdeal.setDiaSemana(converterDataDiaSemana(diaSemana));
				return super.findByParametersForSituation(estoqueMinimoIdeal.getDiaSemana(), 
						estoqueMinimoIdeal.getSituacao(), "diaSemana", "LIKE", "%", "%"); 
			}
		} catch (RuntimeException e) {
			e.getStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private String converterDataDiaSemana(Date dataDiaSemana){
		Calendar calendar = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("MMMM"); 
		Locale BRAZIL = new Locale("pt","BR");
		df = DateFormat.getDateInstance(DateFormat.FULL, BRAZIL); 
	    return df.format(dataDiaSemana).replace(", "+calendar.get(Calendar.DAY_OF_MONTH)+" de "+calendar.get(Calendar.YEAR), "");
	}
}
