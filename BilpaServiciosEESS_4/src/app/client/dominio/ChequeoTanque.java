package app.client.dominio;

import java.util.ArrayList;
import java.util.List;

import app.client.dominio.data.ChequeoData;
import app.client.dominio.data.ChequeoTanqueData;
import app.client.dominio.data.ItemChequeadoData;

public class ChequeoTanque extends Chequeo{
	
	private int tipoDeDescarga;
	private int medidaDelAgua;

	
	public int getTipoDeDescarga() {
		return tipoDeDescarga;
	}

	public void setTipoDeDescarga(int tipoDeDescarga) {
		this.tipoDeDescarga = tipoDeDescarga;
	}

	public int getMedidaDelAgua() {
		return medidaDelAgua;
	}

	public void setMedidaDelAgua(int medidaDelAgua) {
		this.medidaDelAgua = medidaDelAgua;
	}
	
	
	public ChequeoTanque(){
		super();
	}
	
	
	@Override
	public ChequeoData getChequeoData() {
		
		ChequeoTanqueData ctd = new ChequeoTanqueData();
		ctd.setChequeoData(this.getUltimaModificacion());
		ctd.setId(this.getId());
		ctd.setMedidaDelAgua(this.getMedidaDelAgua());
		ctd.setTipoDeDescarga(this.getTipoDeDescarga());
		
		List<ItemChequeadoData> itemsChequeadosData = new ArrayList<ItemChequeadoData>();
		for(ItemChequeado ic : getItemsChequeados()) {
			itemsChequeadosData.add(ic.getItemChequeadoData());
		}
		ctd.setItemsChequeados(itemsChequeadosData);
		
		return ctd;
		
	}

	@Override
	public void merge(Chequeo chequeo) {
		super.merge(chequeo);
		ChequeoTanque chequeoTanque = (ChequeoTanque)chequeo;
		setMedidaDelAgua(chequeoTanque.getMedidaDelAgua());
		setTipoDeDescarga(chequeoTanque.getTipoDeDescarga());
	}
	

	@Override
	public boolean tieneOtraData() {
		return getTipoDeDescarga() > 1 || getMedidaDelAgua() > 0;
	}

}
