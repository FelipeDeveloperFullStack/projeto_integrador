package br.com.pitdog.model.global;


import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import br.com.pitdog.model.type.Situacao;
import br.com.sysge.infraestrutura.dao.GenericDomain;

@Entity
@Table(name = "tbl_cliente")
public class Cliente extends GenericDomain{

	private static final long serialVersionUID = -4365055249547093538L;

	private String telefone;
	
	private String logradouro;
	
	private String bairro;
	
	private String cidade;
	
	private String nomeDaPessoaFisica;
	
	@Enumerated(EnumType.STRING)
	private Situacao situacao;

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getNomeDaPessoaFisica() {
		return nomeDaPessoaFisica;
	}

	public void setNomeDaPessoaFisica(String nomeDaPessoaFisica) {
		this.nomeDaPessoaFisica = nomeDaPessoaFisica;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}
	
	
	
}
