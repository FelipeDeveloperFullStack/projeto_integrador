package br.com.pitdog.model.type;

public enum TipoProduto {
	
	PRODUTO("Produto"),
	INSUMO("Insumo");
	
	private String tipo;
	
	TipoProduto(String tipo){
		this.tipo = tipo;
	}
	
	public String getTipo(){
		return this.tipo;
	}

}
