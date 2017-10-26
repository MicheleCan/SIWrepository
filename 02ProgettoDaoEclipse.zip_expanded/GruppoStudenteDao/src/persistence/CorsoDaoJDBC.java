package persistence;

import model.Studente;
import persistence.dao.CorsoDao;
import persistence.dao.IndirizzoDao;
import persistence.dao.StudenteDao;
import model.Corso;
import model.Indirizzo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


class CorsoDaoJDBC implements CorsoDao {
	private DataSource dataSource;

	public CorsoDaoJDBC(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void save(Corso corso) {
		Connection connection = this.dataSource.getConnection();
		try {
//			Long id = IdBroker.getId(connection);
//			corso.setCodice(codice);
			String insert = "insert into corso(codice, nome) values (?,?)";
			PreparedStatement statement = connection.prepareStatement(insert);
			statement.setLong(1, corso.getCodice());
			statement.setString(2, corso.getNome());

			//connection.setAutoCommit(false);
			//serve in caso gli studenti non siano stati salvati. Il DAO studente apre e chiude una transazione nuova.
			//connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);			
			statement.executeUpdate();
			// salviamo anche tutti gli studenti del corso in CASACATA
			// e anche l'associazione iscrizione deve essere aggiornata
			this.updateStudentiIscrizione(corso, connection);
			//connection.commit();
		} catch (SQLException e) {
			if (connection != null) {
				try {
					connection.rollback();
				} catch(SQLException excep) {
					throw new PersistenceException(e.getMessage());
				}
			} 
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
	}  

	private void updateStudentiIscrizione(Corso corso, Connection connection) throws SQLException {
		StudenteDao studenteDao = new StudenteDaoJDBC(dataSource);
		
		for (Studente studente : corso.getStudenti()) {
			//se lo studente non esiste lo crea
			if (studenteDao.findByPrimaryKey(studente.getMatricola()) == null){
				studenteDao.save(studente);
			}
			
			String insert = "insert into iscrizione(studente_matricola,"
			+ "corso_codice) values (?,?);";
			PreparedStatement statement = connection.prepareStatement(insert);
			statement.setString(1, studente.getMatricola());
			statement.setLong(2, corso.getCodice());
			statement.executeUpdate();

		}
	}
	

	private void removeFromIscrizione(Corso corso, Connection connection) throws SQLException {
		for (Studente studente : corso.getStudenti()) {
			String delete = "delete from iscrizione WHERE matricola = ? and corso = ?";
			PreparedStatement statement = connection.prepareStatement(delete);
			statement.setString(1, studente.getMatricola());
			statement.setLong(2, corso.getCodice());
			statement.executeUpdate();
		}	
	}

	
	/* 
	 * versione con Join
	 */
	public Corso findByPrimaryKeyJoin(Long codice) {
		Connection connection = this.dataSource.getConnection();
		Corso corso = null;
		try {
			PreparedStatement statement;

			String query = "select c.codice as c_codice, c.nome as n_nome,"
				+ " i.studente_matricola as i_matricola from corso c "
				+ "left outer join iscrizione i on c.codice = i.corso_codice "
				+ "where c.codice = ?";
			statement = connection.prepareStatement(query);
			statement.setLong(1, codice);
			ResultSet result = statement.executeQuery();
			boolean primaRiga = true;

			StudenteDao StudenteDao = new StudenteDaoJDBC(this.dataSource);
			
	while (result.next()) {
		if (primaRiga) {
			corso = new Corso();
			corso.setCodice(result.getLong("c_codice"));
			corso.setNome(result.getString("c_nome"));
			primaRiga = false;
		}
		if(result.getString("i_matricola")!=null){
			Studente studente = StudenteDao.findByPrimaryKey(result.getString("i_matricola"));
			corso.addStudente(studente);
		}
	}
	
		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}	
		return corso;
	}
	

	/* 
	 * versione con Lazy Load
	 */
	public Corso findByPrimaryKey(Long codice) {
		Connection connection = this.dataSource.getConnection();
		Corso corso = null;
		try {
			PreparedStatement statement;
			String query = "select * from codice where codice = ?";
			statement = connection.prepareStatement(query);
			statement.setLong(1, codice);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				corso = new CorsoProxy(dataSource);
				corso.setCodice(result.getLong("codice"));				
				corso.setNome(result.getString("nome"));
			}
		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}	
		return corso;
	}



	/* 
	 * versione con Join
	 */
	public List<Corso> findAllJoin() {
		Connection connection = this.dataSource.getConnection();
		HashMap<Long,Corso> corsi = new HashMap();
		try {
			Corso corso;
			PreparedStatement statement;
			String query = "select g.codice as g_codice, g.nome as g_nome,"
				+ " i.studente_matricola as matricola from corso c "
				+ "left outer join iscrizione i on c.codice=s.gruppo_id ";
				
			statement = connection.prepareStatement(query);

			ResultSet result = statement.executeQuery();
			
			StudenteDao studenteDao = new StudenteDaoJDBC(dataSource);
			
			while (result.next()) {
				if(!corsi.containsKey(result.getLong("c_codice")) ) { 
				corso = new Corso();
				corso.setCodice(result.getLong("c_codice"));				
				corso.setNome(result.getString("c_nome"));
				corsi.put(corso.getCodice(),corso);				
				}
				if(result.getString("matricola")!=null) {
					Studente s = studenteDao.findByPrimaryKey(result.getString("matricola"));
					corsi.get(result.getLong("c_codice")).addStudente(s);
				}
				
			}
		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		}	 finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
		ArrayList<Corso> groups = new ArrayList<>(corsi.values());
		return groups;
	}
	
	
	
	/* 
	 * versione con Lazy Load
	 */
	public List<Corso> findAll() {
		Connection connection = this.dataSource.getConnection();
		List<Corso> corsi = new ArrayList<>();
		try {
			Corso corso;
			PreparedStatement statement;
			String query = "select * from corso";
			statement = connection.prepareStatement(query);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				corso = new CorsoProxy(dataSource);
				corso.setCodice(result.getLong("codice"));				
				corso.setNome(result.getString("nome"));
				corsi.add(corso);
			}
		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		}	 finally {
			try {
				connection.close();
	
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
		return corsi;
	}

	public void update(Corso corso) {
		Connection connection = this.dataSource.getConnection();
		try {
			String update = "update gruppo SET nome = ? WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(update);
			statement.setString(1, corso.getNome());
			statement.setLong(2, corso.getCodice());

			//connection.setAutoCommit(false);
			//connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);			
			statement.executeUpdate();
			this.updateStudentiIscrizione(corso, connection); // se abbiamo deciso di propagare gli update seguendo il riferimento
			//connection.commit();
		} catch (SQLException e) {
			if (connection != null) {
				try {
					connection.rollback();
				} catch(SQLException excep) {
					throw new PersistenceException(e.getMessage());
				}
			} 
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
	}

	public void delete(Corso gruppo) {
		Connection connection = this.dataSource.getConnection();
		try {
			String delete = "delete FROM gruppo WHERE id = ? ";
			PreparedStatement statement = connection.prepareStatement(delete);
			statement.setLong(1, gruppo.getCodice());

			connection.setAutoCommit(false);
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);			
			this.removeFromIscrizione(gruppo, connection);     			

			statement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
	}
}
