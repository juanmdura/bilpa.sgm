package app.client.dominio.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author dfleitas
 *
 */
public class ChequeoSurtidorJson {
	
	private int idPreventivo;
	private Date ultimaModificacion;
	private List<ItemChequeadoJson> itemsChequeados = new ArrayList<ItemChequeadoJson>();
	
	
	public int getIdPreventivo() {
		return idPreventivo;
	}
	public void setIdPreventivo(int idPreventivo) {
		this.idPreventivo = idPreventivo;
	}
	public Date getUltimaModificacion() {
		return ultimaModificacion;
	}
	public void setUltimaModificacion(Date ultimaModificacion) {
		this.ultimaModificacion = ultimaModificacion;
	}
	public List<ItemChequeadoJson> getItemsChequeados() {
		return itemsChequeados;
	}
	public void setItemsChequeados(List<ItemChequeadoJson> itemsChequeados) {
		this.itemsChequeados = itemsChequeados;
	}
	
	
	public ChequeoSurtidorJson() {
		super();
	}

}
