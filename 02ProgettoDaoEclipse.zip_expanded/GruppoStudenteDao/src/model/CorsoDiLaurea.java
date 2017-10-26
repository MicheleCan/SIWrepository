package model;

import java.util.HashSet;
import java.util.Set;

public class CorsoDiLaurea {

	private long codice;
	private String nome;
	private Dipartimento dipartimento;
	private Set<Corso> corsi;
	
	
	public CorsoDiLaurea() {
		corsi = new HashSet<>();
	}
	
	public CorsoDiLaurea(long codice, String nome, Dipartimento dipartimento) {
		this();
		this.codice = codice;
		this.nome = nome;
		this.dipartimento = dipartimento;
	}

	public long getCodice() {
		return codice;
	}

	public void setCodice(long codice) {
		this.codice = codice;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Dipartimento getDipartimento() {
		return dipartimento;
	}

	public void setDipartimento(Dipartimento dipartimento) {
		this.dipartimento = dipartimento;
	}

	public Set<Corso> getCorsi() {
		return corsi;
	}

	public void setCorsi(Set<Corso> corsi) {
		this.corsi = corsi;
	}
	
	
}
