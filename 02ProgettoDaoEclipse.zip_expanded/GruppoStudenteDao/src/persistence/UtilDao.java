package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UtilDao {

	
private DataSource dataSource;

public UtilDao(DataSource dataSource) {
		this.dataSource=dataSource;
	}

public void dropDatabase(){
	
	Connection connection = dataSource.getConnection();
	try {
		String drop = "drop SEQUENCE if EXISTS sequenza_id;"
				+ "drop table if EXISTS studente;"
				+ "drop table if exists afferisce;"
				+ "drop table if exists gruppo;"
				+ "drop table if exists indirizzo;"
				+ "drop table if exists iscrizione;"
				+ "drop table if exists corso;"
				+ "drop table if exists corsodilaurea;"
				+ "drop table if exists dipartimento;";
		PreparedStatement statement = connection.prepareStatement(drop);
		
		statement.executeUpdate();
		System.out.println("Executed drop database");
		
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

public void createDatabase(){
	
	Connection connection = dataSource.getConnection();
	try {
		
	String create = "create SEQUENCE sequenza_id;"
	+ "create table gruppo (\"id\" bigint primary key, nome varchar(255));"
	+ "create table indirizzo(\"codice\" bigint primary key, nome varchar(255));"
	+ "create table studente(matricola CHARACTER(8) primary key,"
		+ "nome VARCHAR(255),cognome VARCHAR(255),"
		+ "data_nascita DATE, gruppo_id bigint REFERENCES gruppo(\"id\"),"
		+ "indirizzo_codice bigint REFERENCES indirizzo(\"codice\") );"
	+ "create table dipartimento(\"codice\" bigint primary key, nome varchar(255));"
	+ "create table corso(\"codice\" bigint primary key, nome varchar(255));"
	+ "create table corsodilaurea(\"codice\" bigint primary key, nome varchar(255),"
		+ "dipartimento_codice bigint REFERENCES dipartimento(\"codice\") );"
	+ "create table afferisce(corsodilaurea_codice bigint REFERENCES corsodilaurea (codice),"
		+ "corso_codice bigint REFERENCES corso (codice),"
		+ "primary key(corsodilaurea_codice, corso_codice));"
	+ "create table iscrizione(studente_matricola character(8) REFERENCES studente(matricola),"
		+ "corso_codice bigint REFERENCES corso(codice), primary key(studente_matricola,corso_codice));";
				
		PreparedStatement statement = connection.prepareStatement(create);
		
		statement.executeUpdate();
		System.out.println("Executed create database");
		
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


public  void resetDatabase() {
		
		Connection connection = dataSource.getConnection();
		try {
			String delete = "delete FROM studente;";
			PreparedStatement statement = connection.prepareStatement(delete);
			
			statement.executeUpdate();

			delete = "delete FROM afferisce;";
			statement = connection.prepareStatement(delete);
			
			statement.executeUpdate();
			
			delete = "delete FROM gruppo;";
			statement = connection.prepareStatement(delete);
			
			statement.executeUpdate();
			
			delete = "delete FROM indirizzo;";
			statement = connection.prepareStatement(delete);
			
			statement.executeUpdate();

			delete = "delete FROM iscrizione;";
			statement = connection.prepareStatement(delete);
			
			statement.executeUpdate();

			delete = "delete FROM corso;";
			statement = connection.prepareStatement(delete);
			
			statement.executeUpdate();
			
			delete = "delete FROM corsodilaurea;";
			statement = connection.prepareStatement(delete);
			
			statement.executeUpdate();

			delete = "delete FROM dipartimento;";
			statement = connection.prepareStatement(delete);
			
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
}
