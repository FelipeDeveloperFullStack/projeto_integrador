package br.com.pitdog.service.global;

import java.util.ArrayList;
import java.util.List;

import br.com.pitdog.model.global.Categoria;
import br.com.pitdog.model.type.Situacao;
import br.com.sysge.infraestrutura.dao.GenericDaoImpl;

public class CategoriaService extends GenericDaoImpl<Categoria, Long>{

	private static final long serialVersionUID = -5409502690679918381L;
	
	public Categoria salvar(Categoria categoria){
		try {
			if(categoria.getCategoria().trim().isEmpty()){
				throw new RuntimeException("A categoria é obrigatório!");
			}
			return super.save(consistirCategoria(categoria));
		} catch (RuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private Categoria consistirCategoria(Categoria categoria){
		if(categoria.getId() == null){
			categoria.setCategoria(categoria.getCategoria().toUpperCase());
			categoria.setSituacao(Situacao.ATIVO);
		}else{
			categoria.setCategoria(categoria.getCategoria().toUpperCase());
		}
		return categoria;
	}
	
	public List<Categoria> pesquisarCategoria(Categoria categoria){
		try {
			if(categoria == null){
				return new ArrayList<Categoria>();
			}else{
				if(categoria.getCategoria().trim().isEmpty()){
					return super.findBySituation(categoria.getSituacao());
				}else{
					return super.findByParametersForSituation(categoria.getCategoria().toUpperCase(), 
							categoria.getSituacao(), "categoria", "LIKE", "%", "%"); 
				}
			}
		} catch (RuntimeException e) {
			e.getStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

}
