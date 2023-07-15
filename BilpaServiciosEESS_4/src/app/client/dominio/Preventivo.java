package app.client.dominio;

import java.util.Date;

import app.client.dominio.data.PreventivoData;

public class Preventivo implements com.google.gwt.user.client.rpc.IsSerializable, Comparable<Preventivo> {

	private int id;
	private Date ultimaModificacion;

	private Chequeo chequeo;

	private Activo activo;

	private Visita visita;

	public Preventivo(){

	}

	public Preventivo(int idPreventivo) {
		this.id = idPreventivo;
	}

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

	public Chequeo getChequeo() {
		return chequeo;
	}

	public void setChequeo(Chequeo chequeo) {
		this.chequeo = chequeo;
	}

	public Activo getActivo() {
		return activo;
	}

	public void setActivo(Activo activo) {
		this.activo = activo;
	}

	public Visita getVisita() {
		return visita;
	}

	public void setVisita(Visita visita) {
		this.visita = visita;
	}

	public PreventivoData getPreventivoData() {
		PreventivoData data = new PreventivoData();
		data.setId(getId());
		data.setIdActivo(getActivo().getId());
		data.setIdTipoActivo(getActivo().getTipo());
		if (getChequeo() != null)data.setIdChequeo(getChequeo().getId());
		return data;
	}

	public int compareTo(Preventivo preventivo) {
		int order = 0;
		if (preventivo.getActivo().getTipo() > getActivo().getTipo()){
			order--;
		} else if (getActivo().getTipo() < getActivo().getTipo()){
			order++;
		}
		return order;
	}

	public boolean tienePendientesVisibles(Chequeo chequeo) {
		for(ItemChequeado ic : chequeo.getItemsChequeados()) {
			for(Pendiente p : ic.getListaDePendientes()){
				if (p.isComentarioVisible()){
					return true;
				}
			}
		}
		return false;
	}

	public boolean tieneAlgunaData(Organizacion organizacion, String estado) {
		if (getChequeo() == null || getChequeo().getItemsChequeados() == null || getChequeo().getItemsChequeados().isEmpty()){
			return false;
		}

		if (chequeoTieneAlgunaData(organizacion, estado, getChequeo())){
			return true;
			
		} else {
			if (chequeo.getClass().equals(ChequeoSurtidor.class)){
				ChequeoSurtidor cs = (ChequeoSurtidor)getChequeo();
				for (Chequeo cPico : cs.getListaDeChequeosPicos()){
					if (chequeoTieneAlgunaData(organizacion, estado, cPico)){
						return true;
					}
				}
				
				for (Chequeo cProducto : cs.getListaDeProductos()){
					if (chequeoTieneAlgunaData(organizacion, estado, cProducto)){
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean chequeoTieneAlgunaData(Organizacion organizacion, String estado, Chequeo chequeo) {
		for(ItemChequeado ic : chequeo.getItemsChequeados()) {
			if(getActivo().getId() > 0 && 
					((chequeo != null && (chequeo.tieneChequeo(estado) || chequeo.tieneOtraData()))
							|| ic.getListaDeCorregidos().size() > 0 || (organizacion.equals(Organizacion.Bilpa) ? ic.getListaDePendientes().size() > 0 : tienePendientesVisibles(chequeo)))) {
				return true;
			}
		}
		return false;
	}


}
