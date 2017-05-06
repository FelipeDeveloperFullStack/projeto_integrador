package br.com.pitdog.service.estoque;

import java.util.List;

import br.com.pitdog.model.estoque.Composicao;
import br.com.sysge.infraestrutura.dao.GenericDaoImpl;

public class ComposicaoService extends GenericDaoImpl<Composicao, Long>{

	private static final long serialVersionUID = -5464143346016935835L;
	
	public boolean verificarSeExisteComposicaoNaTabela(List<Composicao> composicoes, Composicao composicao) {
		for (Composicao c : composicoes) {
			if (c.getInsumo().getDescricaoProduto().trim().equals(composicao.getInsumo().getDescricaoProduto().trim())) {
				return true;
			}
		}
		return false;
	}
	
	public boolean verificarQuantidadeComposicao(Composicao composicao){
		if(composicao.getQuantidade() == 0.0){
			return true;
		}else{
			return false;
		}
	}

}
