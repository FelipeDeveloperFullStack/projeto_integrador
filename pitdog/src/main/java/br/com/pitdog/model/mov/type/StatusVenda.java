package br.com.pitdog.model.mov.type;

public enum StatusVenda {
	DIGITACAO("Digitação"), CANCELADA("Cancelada"), CONCLUIDA("Concluida");
	
	private String descicao;
	
	private StatusVenda(String descricao){
		this.descicao = descricao;
	}

	public String getDescicao() {
		return descicao;
	}
	
}
