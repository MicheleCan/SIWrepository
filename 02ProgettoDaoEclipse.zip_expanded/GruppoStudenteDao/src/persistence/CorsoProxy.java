package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import model.Corso;
import model.Indirizzo;
import model.Studente;
import persistence.dao.StudenteDao;

public class CorsoProxy extends Corso {

	private DataSource dataSource;

	public CorsoProxy(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Set<Studente> getStudenti() { 
		Set<Studente> studenti = new HashSet<>();
		Connection connection = this.dataSource.getConnection();
		try {
			PreparedStatement statement;
			String query = "select studente_matricola from iscrizione where corso_codice = ?";
			statement = connection.prepareStatement(query);
			statement.setLong(1, this.getCodice());
			ResultSet result = statement.executeQuery();

			StudenteDao studenteDao = new StudenteDaoJDBC(dataSource);

			while (result.next()) {
				Studente studente = studenteDao.findByPrimaryKey(result.getString("studente_matricola"));
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

