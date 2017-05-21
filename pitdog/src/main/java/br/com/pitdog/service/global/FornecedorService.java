package br.com.pitdog.service.global;

import java.util.List;

import br.com.pitdog.model.global.Fornecedor;
import br.com.pitdog.model.type.Situacao;
import br.com.sysge.infraestrutura.dao.GenericDaoImpl;

public class FornecedorService extends GenericDaoImpl<Fornecedor, Long> {

	private static final long serialVersionUID = -3438513129762783683L;

	public Fornecedor salvar(Fornecedor fornecedor) {
		try {
			if (fornecedor.getNomeFantasia().trim().equals("")) {
				throw new RuntimeException("O nome fantasia é obrigatório!");
			}
			return super.save(consistirFornecedor(fornecedor));
		} catch (RuntimeException e) {
			e.getStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	public List<Fornecedor> procurarFornecedor(Fornecedor fornecedor) {
		if (fornecedor.getNomeFantasia().trim().isEmpty() && fornecedor.getCnpj().trim().isEmpty()) {
			return super.findBySituation(fornecedor.getSituacao());
		} else if (!fornecedor.getNomeFantasia().trim().isEmpty()) {
			return super.findByParametersForSituation(fornecedor.getNomeFantasia().toUpperCase(), fornecedor.getSituacao(),
					"nomeFantasia", "LIKE", "%", "%");
		} else {
			return super.findByParametersForSituation(fornecedor.getCnpj(), fornecedor.getSituacao(),
					"cnpj", "=", "", "");
		}
	}


	private Fornecedor consistirFornecedor(Fornecedor fornecedor) {
		if (fornecedor.getId() == null) {
			fornecedor.setNomeFantasia(fornecedor.getNomeFantasia().toUpperCase());
			fornecedor.setSituacao(Situacao.ATIVO);
		}else{
			fornecedor.setNomeFantasia(fornecedor.getNomeFantasia().toUpperCase());
		}
		return fornecedor;
	}

}
