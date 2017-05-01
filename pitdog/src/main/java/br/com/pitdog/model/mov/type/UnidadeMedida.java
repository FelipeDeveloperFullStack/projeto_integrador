package br.com.pitdog.model.mov.type;

public enum UnidadeMedida {
	
	UNIDADE("UN"),
	QUILOGRAMA("KG"),
	LITRO("LT"),
	CAIXA("CX"),
	MILILITRO("ML"),
	PEÇA("PC"),
	FARDO("FD"),
	FRASCO("FR"),
	PACOTE("PTE"),
	GRAMA("G");

	private String unidadeMedida;
	
	UnidadeMedida(String unidadeMedida){
		this.unidadeMedida = unidadeMedida;
	}
	
	public String getUnidadeMedida(){
		return this.unidadeMedida;
	}

}
