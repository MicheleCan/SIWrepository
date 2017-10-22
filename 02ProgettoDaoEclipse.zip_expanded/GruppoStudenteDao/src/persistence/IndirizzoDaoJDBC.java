package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import model.Indirizzo;
import persistence.dao.IndirizzoDao;

public class IndirizzoDaoJDBC implements IndirizzoDao{

	private DataSource dataSource;
	
	public IndirizzoDaoJDBC(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	
	@Override
	public void save(Indirizzo indirizzo) {
		Connection connection = this.dataSource.getConnection();
		try {
		String insert = "insert into indirizzo(codice,nome) values (?,?)";
		PreparedStatement statement;
		statement = connection.prepareStatement(insert);
		statement.setLong(1, indirizzo.getCodice());
		statement.setString(2, indirizzo.getNome());
		statement.executeUpdate();
		
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

	@Override
	public Indirizzo findByPrimaryKey(Long codice) {
		Connection connection = this.dataSource.getConnection();
		Indirizzo indirizzo = null;
		try {
			String query = "select * from indirizzo where codice=?;";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setLong(1, codice);
			ResultSet result = statement.executeQuery();
			if(result.next()) {
				indirizzo = new Indirizzo();
				indirizzo.setCodice(result.getLong("codice"));
				indirizzo.setNome(result.getString("nome"));
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
		return indirizzo;
	}

	@Override
	public List<Indirizzo> findAll() {
		Connection connection = this.dataSource.getConnection();
		List<Indirizzo> indirizzi = new  ArrayList<>();
		try {
			String query = "select * from indirizzo;";
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet result = statement.executeQuery();
			while(result.next()) {
				Indirizzo indirizzo = new Indirizzo();
				indirizzo.setCodice(result.getLong("codice"));
				indirizzo.setNome(result.getString("nome"));
				
				indirizzi.add(indirizzo);
			}
		}catch (SQLException e){
			throw new PersistenceException(e.getMessage());
		}	finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
		
		return indirizzi;
	}

	@Override
	public void update(Indirizzo indirizzo) {
		Connection connection = this.dataSource.getConnection();
		try {
			String query = "update indirizzo SET nome = ? where codice = ?;";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, indirizzo.getNome());
			statement.setLong(2, indirizzo.getCodice());
			statement.executeQuery();		
		}catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		}	finally {
			try {
				connection.close();
			}catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
		
	}

	@Override
	public void delete(Indirizzo indirizzo) {
		Connection connection = this.dataSource.getConnection();
		try {
			String query = "delete * from indirizzo where codice = ?;";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setLong(1, indirizzo.getCodice());
			statement.executeQuery();
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
