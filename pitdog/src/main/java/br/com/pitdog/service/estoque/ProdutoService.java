package br.com.pitdog.service.estoque;

import java.util.List;

import javax.persistence.Query;

import br.com.pitdog.model.estoque.Produto;
import br.com.pitdog.model.type.Situacao;
import br.com.pitdog.model.type.TipoProduto;
import br.com.sysge.infraestrutura.dao.GenericDaoImpl;

public class ProdutoService extends GenericDaoImpl<Produto, Long>{

	private static final long serialVersionUID = 1704211895445872913L;

	public Produto salvar(Produto produto){
		try {
			if(produto.getTipo() == null){
				throw new RuntimeException("É obrigatório escolher o tipo 'Produto' ou 'Insumo'.");
			}
			if(produto.getDescricaoProduto().trim().isEmpty()){
				throw new RuntimeException("A descrição do produto é obrigatório!");
			}
			if(produto.getCategoria() == null){
				throw new RuntimeException("A categoria é obrigatória!");
			}
			if(produto.getFabricante() == null){
				throw new RuntimeException("O fabricante é obrigatório!");
			}
			if(produto.getCategoria().getCategoria() != null && produto.getFabricante() != null){
				return super.save(consistirProduto(produto));
			}else{
				if(produto.getCategoria().getCategoria() == null){
					throw new RuntimeException("A categoria é obrigatória!");
				}
			}
		} catch (RuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		return new Produto();
	}
	
	private Produto consistirProduto(Produto produto){
		if(produto.getId() == null){
			produto.setSituacao(Situacao.ATIVO);
		}
		return produto;
	}
	
	public List<Produto> pesquisarProduto(Produto produto){
		try {
			if(produto.getDescricaoProduto().trim().isEmpty()){
				return super.findBySituation(produto.getSituacao());
			}else{
				return super.findByParametersForSituation(produto.getDescricaoProduto(), 
						produto.getSituacao(), "descricaoProduto", "LIKE", "%", "%"); 
			}
		} catch (RuntimeException e) {
			e.getStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Produto> pesquisarProdutoNaoInsumo(String pesquisa){
		Query query = getEntityManager().createQuery("FROM " + getEntityClass().getSimpleName() + " p "
				+ "WHERE p.descricaoProduto LIKE :descricaoProduto AND p.situacao = :situacao AND p.tipo = :tipo");
		
		query.setParameter("descricaoProduto", pesquisa + "%");
		query.setParameter("situacao", Situacao.ATIVO);
		query.setParameter("tipo", TipoProduto.PRODUTO);
		
		return query.getResultList();
	}

	public List<Produto> pesquisarInsumos(String pesquisaInsumo) {
		Query query = getEntityManager().createQuery("FROM " + getEntityClass().getSimpleName() + " p "
				+ "WHERE p.descricaoProduto LIKE :descricaoProduto AND p.situacao = :situacao AND p.tipo = :tipo");
		
		query.setParameter("descricaoProduto", pesquisaInsumo + "%");
		query.setParameter("situacao", Situacao.ATIVO);
		query.setParameter("tipo", TipoProduto.INSUMO);
		
		return query.getResultList();
	}
	
}
