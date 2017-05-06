package br.com.pitdog.service.estoque;

import java.util.List;

import br.com.pitdog.model.estoque.EstoqueMinimoIdeal;
import br.com.sysge.infraestrutura.dao.GenericDaoImpl;

public class EstoqueMinimoIdealService extends GenericDaoImpl<EstoqueMinimoIdeal, Long>{

	private static final long serialVersionUID = -8366693357335093111L;
	
	public boolean verificarSeExisteProdutoEDiaSemanaNaTabela(List<EstoqueMinimoIdeal> estoques, EstoqueMinimoIdeal estoqueMinimoIdeal) {
		for (EstoqueMinimoIdeal e : estoques) {
			if (e.getProduto().getDescricaoProduto().trim().equals(estoqueMinimoIdeal.getProduto().getDescricaoProduto().trim())) {
				if(e.getEstoqueIdeal().getDiaSemana().trim().equals(estoqueMinimoIdeal.getEstoqueIdeal().getDiaSemana().trim())){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean verificarQuantidadeEstoque(EstoqueMinimoIdeal estoqueMinimoIdeal){
		if(estoqueMinimoIdeal.getQuantidade() == 0.0){
			return true;
		}else{
			return false;
		}
	}

}
