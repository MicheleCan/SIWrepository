package persistence.dao;

import java.util.List;

import model.Indirizzo;


public interface IndirizzoDao {

	public void save(Indirizzo indirizzo);  // Create
	public Indirizzo findByPrimaryKey(Long codice);     // Retrieve
	public List<Indirizzo> findAll();       
	public void update(Indirizzo indirizzo); //Update
	public void delete(Indirizzo indirizzo); //Delete	
	
}
