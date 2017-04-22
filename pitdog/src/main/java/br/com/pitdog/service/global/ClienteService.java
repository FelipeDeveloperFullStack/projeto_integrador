package br.com.pitdog.service.global;

import java.util.List;

import br.com.pitdog.model.global.Cliente;
import br.com.pitdog.model.type.Situacao;
import br.com.sysge.infraestrutura.dao.GenericDaoImpl;

public class ClienteService extends GenericDaoImpl<Cliente, Long> {

	private static final long serialVersionUID = -3438513129762783683L;

	public Cliente salvar(Cliente cliente) {
		try {
			if (cliente.getNomeDaPessoaFisica().isEmpty()) {
				throw new RuntimeException("O nome do cliente é obrigatório!");
			}
			if (cliente.getLogradouro().isEmpty()) {
				throw new RuntimeException("O endereço do cliente é obrigatório!");
			}
			if (cliente.getBairro().isEmpty()) {
				throw new RuntimeException("O bairro é obrigatório!");
			}
			if (cliente.getCidade().isEmpty()) {
				throw new RuntimeException("A cidade é obrigatório!");
			}
			return super.save(consistirCliente(cliente));
		} catch (RuntimeException e) {
			e.getStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public List<Cliente> procurarCliente(Cliente cliente) {
		if (cliente.getNomeDaPessoaFisica().trim().isEmpty() && cliente.getTelefone().isEmpty()) {
			return super.findBySituation(cliente.getSituacao());
		} else {
				if(!cliente.getNomeDaPessoaFisica().trim().isEmpty()){
					return super.findByParametersForSituation(cliente.getNomeDaPessoaFisica(),
							cliente.getSituacao(), "nomeDaPessoaFisica", "LIKE", "%", "%");
				}else{
					return super.findByParametersForSituation(cliente.getTelefone(), cliente.getSituacao(),
							"telefone", "=", "", "");
				}
		}
	}


	private Cliente consistirCliente(Cliente cliente) {
		if (cliente.getId() == null) {
			cliente.setSituacao(Situacao.ATIVO);
		}
		return cliente;
	}

}
