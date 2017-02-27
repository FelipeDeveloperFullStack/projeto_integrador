package br.com.pitdog.service.estoque;

import java.util.ArrayList;
import java.util.List;

import br.com.pitdog.model.estoque.Fabricante;
import br.com.pitdog.model.type.Situacao;
import br.com.sysge.infraestrutura.dao.GenericDaoImpl;

public class FabricanteService extends GenericDaoImpl<Fabricante, Long>{

	private static final long serialVersionUID = -1194731285781682705L;
	
	public Fabricante salvar(Fabricante fabricante){
		try {
			if(fabricante.getRazaoSocial().trim().isEmpty()){
				throw new RuntimeException("A descrição do produto é obrigatório!");
			}
			return super.save(consistirFabricante(fabricante));
		} catch (RuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private Fabricante consistirFabricante(Fabricante fabricante){
		if(fabricante.getId() == null){
			fabricante.setRazaoSocial(fabricante.getRazaoSocial().toUpperCase());
			fabricante.setSituacao(Situacao.ATIVO);
		}else{
			fabricante.setRazaoSocial(fabricante.getRazaoSocial().toUpperCase());
		}
		return fabricante;
	}
	
	public List<Fabricante> pesquisarFabricante(Fabricante fabricante){
		try {
			if(fabricante == null){
				return new ArrayList<Fabricante>();
			}else{
				if(fabricante.getRazaoSocial().trim().isEmpty()){
					return super.findBySituation(fabricante.getSituacao());
				}else{
					return super.findByParametersForSituation(fabricante.getRazaoSocial().toUpperCase(), 
							fabricante.getSituacao(), "razaoSocial", "LIKE", "%", "%"); 
				}
			}
		} catch (RuntimeException e) {
			e.getStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

}
