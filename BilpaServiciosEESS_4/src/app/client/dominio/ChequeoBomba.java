package app.client.dominio;

import java.util.ArrayList;
import java.util.List;

import app.client.dominio.data.ChequeoBombaData;
import app.client.dominio.data.ChequeoData;
import app.client.dominio.data.ItemChequeadoData;


public class ChequeoBomba extends Chequeo{

	public ChequeoBomba(){

	}


	@Override
	public ChequeoData getChequeoData() {
		
		ChequeoBombaData cbd = new ChequeoBombaData();
		cbd.setChequeoData(this.getUltimaModificacion());
		cbd.setId(this.getId());
		
		List<ItemChequeadoData> itemsChequeadosData = new ArrayList<ItemChequeadoData>();
		for(ItemChequeado ic : getItemsChequeados()) {
			itemsChequeadosData.add(ic.getItemChequeadoData());
		}
		cbd.setItemsChequeados(itemsChequeadosData);
		
		return cbd;
	}


	@Override
	public boolean tieneOtraData() {
		return false;
	}


}
