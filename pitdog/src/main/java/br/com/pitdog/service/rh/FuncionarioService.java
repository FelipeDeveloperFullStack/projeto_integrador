package br.com.pitdog.service.rh;

import java.util.List;

import br.com.pitdog.model.rh.Funcionario;
import br.com.pitdog.model.type.Situacao;
import br.com.sysge.infraestrutura.dao.GenericDaoImpl;

public class FuncionarioService extends GenericDaoImpl<Funcionario, Long>{

	private static final long serialVersionUID = -7529766205098384896L;

	public Funcionario salvar(Funcionario funcionario){
		try {
			if(funcionario.getNome().trim().isEmpty()){
				throw new RuntimeException("O nome do funcionário é obrigatório!");
			}
			return super.save(consistirFuncionario(funcionario));
		} catch (RuntimeException e) {
			e.getStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private Funcionario consistirFuncionario(Funcionario funcionario){
		if(funcionario.getId() == null){
			funcionario.setNome(funcionario.getNome().toUpperCase());
			funcionario.setSituacao(Situacao.ATIVO);
		}else{
			funcionario.setNome(funcionario.getNome().toUpperCase());
		}
		return funcionario;
	}
	
	public List<Funcionario> pesquisarFuncionario(Funcionario funcionario){
			if(funcionario.getNome().trim().isEmpty()){
				return super.findBySituation(funcionario.getSituacao());
			}else{
				return super.findByParametersForSituation(funcionario.getNome().toUpperCase(), 
						funcionario.getSituacao(), "nome", "LIKE", "%", "%");
			}
	}
	
	public boolean verificarSeFuncionarioEDiferenteDeNull(Funcionario funcionario){
		if(funcionario != null){
			return true;
		}else{
			return false;
		}
			
	}
	
	public boolean verificarIdNull(Funcionario funcionario){
		if(funcionario.getId() == null){
			return true;
		}else{
			return false;
		}
	}
}
