package app.client.dominio.json;

import java.util.ArrayList;
import java.util.List;

public class FinalizarVisitaJson {
	
	int idVisita;
	List<String> mails = new ArrayList<String>();
	
	
	public int getIdVisita() {
		return idVisita;
	}
	public void setIdVisita(int idVisita) {
		this.idVisita = idVisita;
	}
	public List<String> getMails() {
		return mails;
	}
	public void setMails(List<String> mails) {
		this.mails = mails;
	}
	
	
	public FinalizarVisitaJson() {
		super();
	}

}
