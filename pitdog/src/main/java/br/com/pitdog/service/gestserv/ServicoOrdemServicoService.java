package br.com.pitdog.service.gestserv;

import java.util.List;

import br.com.pitdog.model.gestserv.Servico;
import br.com.pitdog.model.gestserv.ServicoOrdemServico;
import br.com.sysge.infraestrutura.dao.GenericDaoImpl;

public class ServicoOrdemServicoService extends GenericDaoImpl<ServicoOrdemServico, Long> {

	private static final long serialVersionUID = -9133422263268014887L;

	public boolean verificarSeExisteServicoNaTabela(List<ServicoOrdemServico> listaServicos, Servico servico) {
		for (ServicoOrdemServico s : listaServicos) {
			if (s.getServico().getId() == servico.getId()) {
				return true;
			}
		}
		return false;
	}

}
