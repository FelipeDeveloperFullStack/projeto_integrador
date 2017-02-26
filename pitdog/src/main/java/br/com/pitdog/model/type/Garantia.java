package br.com.pitdog.model.type;

public enum Garantia {
	
	NAO("Não"),
	SIM("Sim");
	
	private String garantia;
	
	Garantia(String garantia){
		this.garantia = garantia;
	}
	
	public String getGarantia(){
		return this.garantia;
	}

}
