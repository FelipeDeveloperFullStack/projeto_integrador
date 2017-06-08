package br.com.pitdog.service.estoque;

import java.util.List;

import javax.persistence.Query;

import br.com.pitdog.model.estoque.Composicao;
import br.com.pitdog.model.estoque.Produto;
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
	
	public List<Composicao> removerComposicao(Composicao composicao, List<Composicao> composicoes){
		if(composicao.getId() == null){
			for(int i = 0; i < composicoes.size(); i++){
				if(composicoes.get(i).getInsumo().getDescricaoProduto().trim().equals(composicao.getInsumo().getDescricaoProduto())){
					composicoes.remove(i);
				}
			}
		}else{
			super.remove(composicao.getId());
			composicoes = super.findByListProperty(composicao.getProduto().getId(), "produto.id");
		}
		return composicoes;
	}
	
	@SuppressWarnings("unchecked")
	public List<Composicao> listarComposicaProduto(Produto produto){
		Query query = getEntityManager().createQuery("FROM " + getEntityClass().getSimpleName() + " c WHERE c.produto = :produto");
		query.setParameter("produto", produto);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Composicao> procurarComposicoesPorProduto(Produto produto){
		Query query = getEntityManager().createQuery("SELECT c FROM "+getEntityClass().getSimpleName() + " c "
				+ "WHERE c.produto = :produto");
		query.setParameter("produto", produto);
		return query.getResultList();
	}
	

}
