package br.com.pitdog.service.gestserv;

import java.util.List;

import br.com.pitdog.model.estoque.Produto;
import br.com.pitdog.model.gestserv.ProdutoOrdemServico;
import br.com.sysge.infraestrutura.dao.GenericDaoImpl;

public class ProdutoOrdemServicoService extends GenericDaoImpl<ProdutoOrdemServico, Long>{

	private static final long serialVersionUID = 590295555955384164L;
	
	public boolean verificarSeExisteProdutoNaTabela(List<ProdutoOrdemServico> listaProdutos, Produto produto) {
		for (ProdutoOrdemServico s : listaProdutos) {
			if (s.getProduto().getId() == produto.getId()) {
				return true;
			}
		}
		return false;
	}

}
