package br.com.pitdog.service.global;

import java.util.List;

import br.com.pitdog.model.global.Distribuidor;
import br.com.pitdog.model.type.Situacao;
import br.com.sysge.infraestrutura.dao.GenericDaoImpl;

public class DistribuidorService extends GenericDaoImpl<Distribuidor, Long>{

	private static final long serialVersionUID = -9097166330364563658L;
	
	public Distribuidor salvar(Distribuidor distribuidor) {
		try {
			if (distribuidor.getRazaoSocial().trim().equals("")) {
				throw new RuntimeException("A razão social é obrigatório!");
			}
			return super.save(consistirDistribuidor(distribuidor));
		} catch (RuntimeException e) {
			e.getStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private Distribuidor consistirDistribuidor(Distribuidor distribuidor) {
		if (distribuidor.getId() == null) {
			distribuidor.setRazaoSocial(distribuidor.getRazaoSocial().toUpperCase());
			distribuidor.setSituacao(Situacao.ATIVO);
		}else{
			distribuidor.setRazaoSocial(distribuidor.getRazaoSocial().toUpperCase());
		}
		return distribuidor;
	}
	
	public List<Distribuidor> pesquisarDistribuidor(Distribuidor distribuidor){
		try {
			if(distribuidor.getRazaoSocial().trim().isEmpty()){
				return super.findBySituation(distribuidor.getSituacao());
			}else{
				return super.findByParametersForSituation(distribuidor.getRazaoSocial().toUpperCase(), 
						distribuidor.getSituacao(), "razaoSocial", "LIKE", "%", "%"); 
			}
		} catch (RuntimeException e) {
			e.getStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

}
