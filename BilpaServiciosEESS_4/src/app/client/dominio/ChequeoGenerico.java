package app.client.dominio;

import java.util.ArrayList;
import java.util.List;

import app.client.dominio.data.ChequeoData;
import app.client.dominio.data.ChequeoGenericoData;
import app.client.dominio.data.ItemChequeadoData;


public class ChequeoGenerico extends Chequeo{

	public ChequeoGenerico(){

	}


	public ChequeoGenerico(int id) {
		setId(id);
	}


	@Override
	public ChequeoData getChequeoData() {
		
		ChequeoGenericoData cbd = new ChequeoGenericoData();
		cbd.setChequeoData(this.getUltimaModificacion());
		cbd.setId(this.getId());
		
		List<ItemChequeadoData> itemsChequeadosData = new ArrayList<ItemChequeadoData>();
		for(ItemChequeado ic : getItemsChequeados()) {
			if (ic.getItemChequeo().isActivo()){
				itemsChequeadosData.add(ic.getItemChequeadoData());
			}
		}
		cbd.setItemsChequeados(itemsChequeadosData);
		
		return cbd;
	}


	@Override
	public boolean tieneOtraData() {
		return false;
	}


}
