package persistence.dao;

import java.util.List;
import model.Gruppo;

public interface GruppoDao {
	
	public void save(Gruppo gruppo);  // Create
	
	public Gruppo findByPrimaryKey(Long id);     // Retrieve
	public Gruppo findByPrimaryKeyJoin(Long id);
	
	public List<Gruppo> findAll();       
	public List<Gruppo>	findAllJoin();
	
	public void update(Gruppo gruppo); //Update
	public void delete(Gruppo gruppo); //Delete
}
