package br.com.pitdog.model.mov.type;

public enum TipoPE {
	
	PEDIDO("Pedido"),
	ENTRADA("Entrada");
	
	private String tipoPE;
	
	TipoPE(String tipoPE){
		this.tipoPE = tipoPE;
	}
	
	public String getTipoPE(){
		return this.tipoPE;
	}

}
