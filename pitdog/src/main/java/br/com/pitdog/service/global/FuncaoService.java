package br.com.pitdog.service.global;

import java.util.List;

import br.com.pitdog.model.global.Funcao;
import br.com.pitdog.model.type.Situacao;
import br.com.sysge.infraestrutura.dao.GenericDaoImpl;

public class FuncaoService extends GenericDaoImpl<Funcao, Long>{

	private static final long serialVersionUID = -2180695283888033454L;
	
	public Funcao salvar(Funcao funcao) {
		try {
			if (funcao.getFuncao().trim().equals("")) {
				throw new RuntimeException("A função é obrigatória!");
			}
			return super.save(consistirFuncao(funcao));
		} catch (RuntimeException e) {
			e.getStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private Funcao consistirFuncao(Funcao funcao) {
		if (funcao.getId() == null) {
			funcao.setFuncao(funcao.getFuncao().toUpperCase());
			funcao.setSituacao(Situacao.ATIVO);
		}else{
			funcao.setFuncao(funcao.getFuncao().toUpperCase());
		}
		return funcao;
	}
	
	public List<Funcao> pesquisarFuncao(Funcao funcao){
		try {
			if(funcao.getFuncao().trim().isEmpty()){
				return super.findBySituation(funcao.getSituacao());
			}else{
				return super.findByParametersForSituation(funcao.getFuncao().toUpperCase(), 
						funcao.getSituacao(), "funcao", "LIKE", "%", "%"); 
			}
		} catch (RuntimeException e) {
			e.getStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public void verificarSeExisteFuncaoCadastradoComMesmaDescricao(Funcao funcao){
		List<Funcao> funcoes = super.findAll();
		for(Funcao f : funcoes){
			verificarDescricaoIgual(f, funcao);
		}
	}
	
	private void verificarDescricaoIgual(Funcao f, Funcao funcao){
		if(funcao.getFuncao().trim().equalsIgnoreCase(f.getFuncao())){
			if(funcao.getId() == null){
				mostrarMensagemParaUsuario(f);
			}else{
				if(f.getId() != funcao.getId()){
					mostrarMensagemParaUsuario(f);
				}
			}
		}
	}
	
	private void mostrarMensagemParaUsuario(Funcao f){
		throw new RuntimeException("Existe a função "+f.getFuncao()+" "
				+ "de código "+f.getId()+" já está cadastrada, "
				+ "por favor escolha outra função!");
	}

}
