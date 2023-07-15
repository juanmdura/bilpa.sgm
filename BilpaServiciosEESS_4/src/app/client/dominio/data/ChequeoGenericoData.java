package app.client.dominio.data;

import java.util.List;


public class ChequeoGenericoData extends ChequeoData {

	private List<String> items;
	
	public ChequeoGenericoData(){
		
	}

	public List<String> getItems() {
		return items;
	}

	public void setItems(List<String> items) {
		this.items = items;
	}
}
