package br.com.pitdog.service.mov;

import br.com.pitdog.model.mov.Venda;
import br.com.sysge.infraestrutura.dao.GenericDaoImpl;

public class VendaService extends GenericDaoImpl<Venda, Long>{

	private static final long serialVersionUID = 1L;

	@Override
	public Venda save(Venda entity) {
		checarNumero(entity);
		return super.save(entity);
	}
	
	private void checarNumero(Venda venda){
		if(venda.getNumero() <= 0){
			Integer numero = (Integer) geEntityManager().createQuery("select max(v.numero) from Venda v")
			  .getSingleResult();
			if(numero == null || numero == 0){
				venda.setNumero(1);
			}else{
				venda.setNumero(numero + 1);
			}
		}
	}
}
