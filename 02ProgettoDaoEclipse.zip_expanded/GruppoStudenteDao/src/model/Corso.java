package model;

import java.util.HashSet;
import java.util.Set;

public class Corso {

	private long codice;
	private String nome;
	private Set<Studente> studenti;
	


	public Corso() {
		studenti = new HashSet<>();
	}
	
	public Corso(long codice, String nome) {
		this();
		this.codice = codice;
		this.nome = nome;
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
	
	public Set<Studente> getStudenti() {
		return studenti;
	}

	public void setStudenti(Set<Studente> studenti) {
		this.studenti = studenti;
	}
	
	public void addStudente(Studente studente){
		this.getStudenti().add(studente);         // fondamentale usare .getStudenti() per il proxy!!!
	}
	
	public void removeStudente(Studente studente){
		this.getStudenti().remove(studente);     // fondamentale usare .getStudenti() per il proxy!!!
	}
	
	public String toString() {
		StringBuffer str = new StringBuffer("[");
		str.append(this.getCodice() + ", " + this.getNome());
		str.append(", {");
		for (Studente s : this.getStudenti()) {
			str.append(s.toString());
		}
		str.append("}\n");
		return str.toString();
	}
}
