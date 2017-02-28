package br.com.pitdog.service.estoque;

import java.util.ArrayList;
import java.util.List;

import br.com.pitdog.model.estoque.Produto;
import br.com.pitdog.model.type.Situacao;
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
			produto.setDescricaoProduto(produto.getDescricaoProduto().toUpperCase());
			produto.setSituacao(Situacao.ATIVO);
		}else{
			produto.setDescricaoProduto(produto.getDescricaoProduto().toUpperCase());
		}
		return produto;
	}
	
	public List<Produto> pesquisarProduto(Produto produto){
		try {
			if(produto.getDescricaoProduto().trim().isEmpty()){
				return super.findBySituation(produto.getSituacao());
			}else{
				return super.findByParametersForSituation(produto.getDescricaoProduto().toUpperCase(), 
						produto.getSituacao(), "descricaoProduto", "LIKE", "%", "%"); 
			}
		} catch (RuntimeException e) {
			e.getStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	
	public List<Produto> obterProdutoQuantidadeMinimoEstoque(){
		List<Produto> listaProdutos = new ArrayList<Produto>();
		for(Produto p : super.findBySituation(Situacao.ATIVO)){
			if((p.getQuantidadeEstoque() <= p.getQuantidadeEstoqueMinimo()) && p.isMostrarEstoqueMinimoTelaInicial()){
				listaProdutos.add(p);
			}
		}
		return listaProdutos;
	}
	
	
}
