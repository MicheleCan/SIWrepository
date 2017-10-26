package persistence.dao;

import java.util.List;
import model.Corso;

public interface CorsoDao {
	
	public void save(Corso corso);  // Create
	
	public Corso findByPrimaryKey(Long codice);     // Retrieve
	public Corso findByPrimaryKeyJoin(Long codice);
	
	public List<Corso> findAll();       
	public List<Corso>	findAllJoin();
	
	public void update(Corso corso); //Update
	public void delete(Corso corso); //Delete
}
