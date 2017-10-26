package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import model.Dipartimento;
import persistence.dao.DipartimentoDao;

public class DipartimentoDaoJDBC implements DipartimentoDao{

	private DataSource dataSource;
	
	public DipartimentoDaoJDBC(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	
	@Override
	public void save(Dipartimento dipartimento) {
		Connection connection = this.dataSource.getConnection();
		try {
		String insert = "insert into dipartimento(codice,nome) values (?,?)";
		PreparedStatement statement;
		statement = connection.prepareStatement(insert);
		statement.setLong(1, dipartimento.getCodice());
		statement.setString(2, dipartimento.getNome());
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
	public Dipartimento findByPrimaryKey(Long codice) {
		Connection connection = this.dataSource.getConnection();
		 Dipartimento dipartimento = null;
		try {
			String query = "select * from indirizzo where codice=?;";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setLong(1, codice);
			ResultSet result = statement.executeQuery();
			if(result.next()) {
				dipartimento = new Dipartimento();
				dipartimento.setCodice(result.getLong("codice"));
				dipartimento.setNome(result.getString("nome"));
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
		return dipartimento;
	}

	@Override
	public List<Dipartimento> findAll() {
		Connection connection = this.dataSource.getConnection();
		List<Dipartimento> dipartimenti = new  ArrayList<>();
		try {
			String query = "select * from indirizzo;";
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet result = statement.executeQuery();
			while(result.next()) {
				Dipartimento dipartimento = new Dipartimento();
				dipartimento.setCodice(result.getLong("codice"));
				dipartimento.setNome(result.getString("nome"));
				
				dipartimenti.add(dipartimento);
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
		
		return dipartimenti;
	}

	@Override
	public void update(Dipartimento dipartimento) {
		Connection connection = this.dataSource.getConnection();
		try {
			String query = "update indirizzo SET nome = ? where codice = ?;";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, dipartimento.getNome());
			statement.setLong(2, dipartimento.getCodice());
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
	public void delete(Dipartimento dipartimento) {
		Connection connection = this.dataSource.getConnection();
		try {
			String query = "delete * from indirizzo where codice = ?;";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setLong(1, dipartimento.getCodice());
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
