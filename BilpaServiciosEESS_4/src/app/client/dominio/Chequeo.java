package app.client.dominio;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import app.client.dominio.data.ChequeoData;

public abstract class Chequeo implements com.google.gwt.user.client.rpc.IsSerializable {

	private int id;
	private Date ultimaModificacion;

	private Set<ItemChequeado> itemsChequeados = new HashSet<ItemChequeado>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getUltimaModificacion() {
		return ultimaModificacion;
	}

	public void setUltimaModificacion(Date ultimaModificacion) {
		this.ultimaModificacion = ultimaModificacion;
	}

	public Set<ItemChequeado> getItemsChequeados() {
		return itemsChequeados;
	}

	public void setItemsChequeados(Set<ItemChequeado> itemsChequeados) {
		this.itemsChequeados = itemsChequeados;
	}


	public Chequeo(){

	}

	public void merge(Chequeo chequeo) {
		for(ItemChequeado ic : chequeo.getItemsChequeados()) {
			ItemChequeado icv = getItemChequeadoByChequeo(ic.getItemChequeo());
			if(icv != null) {
				icv.setPendiente(ic.isPendiente());
				icv.setValor(ic.getValor());
			} else {
				this.getItemsChequeados().add(ic);
			}
		}
		this.setUltimaModificacion(chequeo.getUltimaModificacion());
	}

	private ItemChequeado getItemChequeadoByChequeo(ItemChequeo itemChequeo) {
		for(ItemChequeado ic : this.getItemsChequeados()) {
			if(ic.getItemChequeo().equals(itemChequeo)) {
				return ic;
			}
		}
		return null;
	}

	public abstract ChequeoData getChequeoData();

	public boolean tieneChequeo(String estado) {
		Set<ItemChequeado> ics = getItemsChequeados();
		if (estado.isEmpty()){
			for (ItemChequeado ic : ics){
				if ((ic.getValor() != null && ( ic.getValor().equalsIgnoreCase("B") || ic.getValor().equalsIgnoreCase("R"))) 
						|| ic.isPendiente()) {
					return true;
				}
			}
		} else {
			for (ItemChequeado ic : ics){
				if ((ic.getValor() != null && ic.getValor().equalsIgnoreCase(estado)) || ic.isPendiente()){
					return true;
				}
			}
		}
		return false;
	}

	public boolean estaCompleto(Activo activo) {
		for (ItemChequeado ic : getItemsChequeados()) {
			if ((ic.getValor() == null || ic.getValor().isEmpty()) && !ic.isPendiente()){
				return false;
			}
		}
		return true;
	}

	public List<ItemChequeado> getItemsChequeados(String valor) {
		List<ItemChequeado> itemsChequeados = new ArrayList<ItemChequeado>();
		for(ItemChequeado ic : getItemsChequeados()){
			if (ic.getValor() != null && ic.getValor().equals(valor)){
				itemsChequeados.add(ic);
			}
		}
		return itemsChequeados;
	}

	public TipoChequeo getTipo() {
		if (getClass().equals(ChequeoTanque.class)){
			return TipoChequeo.Tanque;
		}
		if (getClass().equals(ChequeoBomba.class)){
			return TipoChequeo.Bomba;
		}
		if (getClass().equals(ChequeoSurtidor.class)){
			return TipoChequeo.Surtidor;
		}
		if (getClass().equals(ChequeoProducto.class)){
			return TipoChequeo.Producto;
		}
		if (getClass().equals(ChequeoPico.class)){
			return TipoChequeo.Pico;
		}
		if (getClass().equals(ChequeoGenerico.class)){
			return TipoChequeo.Generico;
		}
		return null;
	}

	public abstract boolean tieneOtraData();
	
}
