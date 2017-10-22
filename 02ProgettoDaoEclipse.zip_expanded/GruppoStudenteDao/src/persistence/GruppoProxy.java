package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import model.Gruppo;
import model.Indirizzo;
import model.Studente;
import persistence.dao.IndirizzoDao;


class GruppoProxy extends Gruppo {
	private DataSource dataSource;

	public GruppoProxy(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Set<Studente> getStudenti() { 
		Set<Studente> studenti = new HashSet<>();
		Connection connection = this.dataSource.getConnection();
		try {
			PreparedStatement statement;
			String query = "select * from studente where gruppo_id = ?";
			statement = connection.prepareStatement(query);
			statement.setLong(1, this.getId());
			ResultSet result = statement.executeQuery();
			IndirizzoDao indirizzoDao = new IndirizzoDaoJDBC(dataSource);
			while (result.next()) {
				Studente studente = new Studente();
				studente.setMatricola(result.getString("matricola"));				
				studente.setNome(result.getString("nome"));
				studente.setCognome(result.getString("cognome"));
				long secs = result.getDate("data_nascita").getTime();
				studente.setDataNascita(new java.util.Date(secs));
				Indirizzo indirizzo = indirizzoDao.findByPrimaryKey(result.getLong("indirizzo_codice"));
				studente.setIndirizzo(indirizzo);
				studenti.add(studente);
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
		this.setStudenti(studenti);
		return super.getStudenti(); 
	}
}
