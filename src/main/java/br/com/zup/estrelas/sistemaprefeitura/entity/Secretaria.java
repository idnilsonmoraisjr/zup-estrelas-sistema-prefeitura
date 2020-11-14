package br.com.zup.estrelas.sistemaprefeitura.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.ForeignKey;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.zup.estrelas.sistemaprefeitura.enums.Area;

@Entity
@Table(name = "secretaria")
public class Secretaria implements Serializable {

	private static final long serialVersionUID = 7132691927057835612L;

	@Id
	@Column(name = "id_secretaria")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idSecretaria;
	
	@Column(unique = true, nullable = false)
	@Enumerated(EnumType.STRING)
	private Area area;
	
	@Column(name = "orcamento_projetos", nullable = false)
	private Double orcamentoProjetos;
	
	@Column(name = "orcamento_folha", nullable = false)
	private Double orcamentoFolha;

	@Column(nullable = false)
	private String telefone;
	
	@Column(nullable = false)
	private String endereco;

	@Column(nullable = false)
	private String site;

	@Column(nullable = false)
	private String email;

	@JsonIgnore
	@OneToMany(mappedBy = "secretaria")
	private List<Funcionario> funcionarios;

	@JsonIgnore
	@OneToMany(mappedBy = "secretaria")
	private List<Projeto> projetos;

	public Long getIdSecretaria() {
		return idSecretaria;
	}

	public void setIdSecretaria(Long idSecretaria) {
		this.idSecretaria = idSecretaria;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Double getOrcamentoProjetos() {
		return orcamentoProjetos;
	}

	public void setOrcamentoProjetos(Double orcamentoProjetos) {
		this.orcamentoProjetos = orcamentoProjetos;
	}

	public Double getOrcamentoFolha() {
		return orcamentoFolha;
	}

	public void setOrcamentoFolha(Double orcamentoFolha) {
		this.orcamentoFolha = orcamentoFolha;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Funcionario> getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(List<Funcionario> funcionarios) {
		this.funcionarios = funcionarios;
	}

	public List<Projeto> getProjetos() {
		return projetos;
	}

	public void setProjetos(List<Projeto> projetos) {
		this.projetos = projetos;
	}
}
