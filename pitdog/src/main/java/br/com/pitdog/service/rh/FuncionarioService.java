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
			funcionario.setSituacao(Situacao.ATIVO);
		}
		return funcionario;
	}
	
	public List<Funcionario> pesquisarFuncionario(Funcionario funcionario){
			if(funcionario.getNome().trim().isEmpty()){
				return super.findBySituation(funcionario.getSituacao());
			}else{
				return super.findByParametersForSituation(funcionario.getNome(), 
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
	
	public void verificarSeExisteFuncionarioCadastradoComMesmaDescricao(Funcionario funcionario){
		List<Funcionario> funcionarios = super.findAll();
		for(Funcionario f : funcionarios){
			verificarDescricaoIgual(f, funcionario);
		}
	}
	
	private void verificarDescricaoIgual(Funcionario f, Funcionario funcionario){
		if(funcionario.getNome().trim().equalsIgnoreCase(f.getNome())){
			if(funcionario.getId() == null){
				mostrarMensagemParaUsuario(f);
			}else{
				if(f.getId() != funcionario.getId()){
					mostrarMensagemParaUsuario(f);
				}
			}
		}
	}
	
	private void mostrarMensagemParaUsuario(Funcionario f){
		throw new RuntimeException("Existe o funcionário "+f.getNome()+" "
				+ "de código "+f.getId()+" já está cadastrado, "
				+ "por favor escolha outro nome para o funcionário!");
	}
}
