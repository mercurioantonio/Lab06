package it.polito.tdp.meteo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.polito.tdp.meteo.model.Citta;
import it.polito.tdp.meteo.model.Rilevamento;

public class MeteoDAO {
	
	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	

	
	
	
	
	
	
	public List<Citta> getAllCitta() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione GROUP BY Localita ORDER BY data ASC";

		List<Citta> citta = new ArrayList<Citta>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				Citta c = new Citta(rs.getString("Localita"));
				citta.add(c);
			}

			conn.close();
			return citta;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	
	public Double getUmiditaMedia(int mese, Citta citta) {
		
		final String sql = "SELECT AVG(Umidita) AS U FROM situazione WHERE localita=? AND MONTH(data)=?";
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, citta.getNome());
			st.setString(2, Integer.toString(mese));
			
			ResultSet rs = st.executeQuery();
			
			rs.next();
			Double u = rs.getDouble("U");
			
			conn.close();
			return u;
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	
	
	
	
	
	
	
	

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, Citta citta) {

		final String sql = "SELECT Localita, Data, Umidita "
				+ "FROM situazione "
				+ "WHERE Localita=? AND MONTH(DATA)=? ORDER BY data ASC";
		
		List<Rilevamento> Rilevamenti = new ArrayList<Rilevamento>();
		
		try {
			
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setString(1, citta.getNome());
			st.setString(2, Integer.toString(mese));
			
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				
				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				Rilevamenti.add(r);
				
			}
			
			conn.close();
			return Rilevamenti;
			
		} catch(SQLException e) {
			
			e.printStackTrace();
			throw new RuntimeException(e);
			
		}
	}
	

}
