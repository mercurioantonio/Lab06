package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
    
	private List<Citta> leCitta;
	private List<Citta> best;
	
	
	public List<Citta> getLeCitta(){
		return leCitta;
	}
	
	public Model() {
		
		MeteoDAO dao = new MeteoDAO();
		this.leCitta = dao.getAllCitta();

	}

	// of course you can change the String output with what you think works best
	public Double getUmiditaMedia(int mese, Citta citta) {
		MeteoDAO dao = new MeteoDAO();
		return dao.getUmiditaMedia(mese, citta);
	}
	
	
	public List<Citta> trovaSequenza(int mese) {
		
		List<Citta> parziale = new ArrayList<>();
		this.best = null;
		
		MeteoDAO dao = new MeteoDAO();
		
		for(Citta c : leCitta) {
			c.setRilevamenti(dao.getAllRilevamentiLocalitaMese(mese, c));
		}
		
		cerca(parziale, 0);
		return best;			
	}
	

	public void cerca(List<Citta> parziale, int livello) {
		
		if(livello == NUMERO_GIORNI_TOTALI) {
			Double costo = calcolaCosto(parziale);
			if(best == null || costo < calcolaCosto(best)) {
				best = new ArrayList<>(parziale);
			}
		}
		else {
			for(Citta prova : leCitta) {
				if(aggiuntaValida(prova, parziale)) {
					parziale.add(prova);
					cerca(parziale, livello+1);
					parziale.remove(parziale.size()-1);
				}
			}
		}
	}
	
	private Double calcolaCosto (List<Citta> parziale) {
		double costo = 0.0;
		
		for(int giorno=1; giorno<NUMERO_GIORNI_TOTALI; giorno++) {
			Citta c = parziale.get(giorno-1);
			double umid = c.getRilevamenti().get(giorno-1).getUmidita();
		}
	
		for(int giorno=2; giorno<NUMERO_GIORNI_TOTALI; giorno++) {
			if(!parziale.get(giorno-1).equals(parziale.get(giorno-2))) {
				costo += COST;
			}
		}
		return costo;
	}
	
	private boolean aggiuntaValida(Citta prova, List<Citta> parziale) {
		int conta=0;
		
		for(Citta precedente : parziale) {
			if (precedente.equals(prova)) 
				conta++;
		}
		
		if(conta >= NUMERO_GIORNI_CITTA_MAX)
			return false;
		
		if(parziale.size() == 0)
			return true;
		
		if (parziale.size()==1 || parziale.size()==2) {
			return parziale.get(parziale.size()-1).equals(prova);
		}
		
		if(parziale.get(parziale.size()-1).equals(prova))
			return true;
		
		if (parziale.get(parziale.size()-1).equals(parziale.get(parziale.size()-2)) && parziale.get(parziale.size()-2).equals(parziale.get(parziale.size()-3)))
			return true;
		
		return false;
		
	}
	
	

}
