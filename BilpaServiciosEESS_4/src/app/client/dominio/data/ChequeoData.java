package app.client.dominio.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class ChequeoData implements com.google.gwt.user.client.rpc.IsSerializable {

	private int id;
	private boolean fueModificado;
	private Date ultimaModificacion;
	
	private List<ItemChequeadoData> itemsChequeados = new ArrayList<ItemChequeadoData>();
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean fueModificado() {
		return getUltimaModificacion() != null;
	}

	public void setFueModificado() {
		this.fueModificado = fueModificado();
	}

	public Date getUltimaModificacion() {
		return ultimaModificacion;
	}

	public void setUltimaModificacion(Date ultimaModificacion) {
		this.ultimaModificacion = ultimaModificacion;
	}

	public void setChequeoData(Date ultimaModificacion) {
		setUltimaModificacion(ultimaModificacion);
		setFueModificado();		
	}
	
	public List<ItemChequeadoData> getItemsChequeados() {
		return itemsChequeados;
	}

	public void setItemsChequeados(List<ItemChequeadoData> itemsChequeados) {
		this.itemsChequeados = itemsChequeados;
	}

	
	public ChequeoData(){
		
	}
}
