package br.com.zup.estrelas.sistemaprefeitura.dto;

public class FuncionarioDto {

	private String nome;
	private String cpf;
	private Double salario;
	private Long idSecretaria; 
	private String funcao;
	private Boolean concursado;
	
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public Double getSalario() {
		return salario;
	}
	public void setSalario(Double salario) {
		this.salario = salario;
	}
	public Long getIdSecretaria() {
		return idSecretaria;
	}
	public void setIdSecretaria(Long idSecretaria) {
		this.idSecretaria = idSecretaria;
	}
	public String getFuncao() {
		return funcao;
	}
	public void setFuncao(String funcao) {
		this.funcao = funcao;
	}
	public Boolean getConcursado() {
		return concursado;
	}
	public void setConcursado(Boolean concursado) {
		this.concursado = concursado;
	}
	
}
