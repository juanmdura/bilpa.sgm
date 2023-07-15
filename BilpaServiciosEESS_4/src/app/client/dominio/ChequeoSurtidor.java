package app.client.dominio;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import app.client.dominio.data.ChequeoSurtidorData;
import app.client.dominio.data.ItemChequeadoData;

public class ChequeoSurtidor extends Chequeo {

	private Set<ChequeoProducto> listaDeProductos = new HashSet<ChequeoProducto>();
	private Set<ChequeoPico> listaDeChequeosPicos = new HashSet<ChequeoPico>();


	public Set<ChequeoProducto> getListaDeProductos() {
		return listaDeProductos;
	}

	public void setListaDeProductos(Set<ChequeoProducto> listaDeProductos) {
		this.listaDeProductos = listaDeProductos;
	}

	public Set<ChequeoPico> getListaDeChequeosPicos() {
		return listaDeChequeosPicos;
	}

	public void setListaDeChequeosPicos(Set<ChequeoPico> listaDeChequeosPicos) {
		this.listaDeChequeosPicos = listaDeChequeosPicos;
	}


	public ChequeoSurtidor(){
		super();
	}


	@Override
	public void merge(Chequeo chequeo) {

		super.merge(chequeo);
		ChequeoSurtidor cs = (ChequeoSurtidor) chequeo;

		boolean productoNuevo = true;
		if (cs.getListaDeProductos() != null && cs.getListaDeProductos().size() > 0){
			ChequeoProducto cp = cs.getListaDeProductos().iterator().next();
			for (ChequeoProducto cp2 : this.getListaDeProductos()){
				if (cp.getProducto().getId() == cp2.getProducto().getId()){
					cp2.merge(cp);
					productoNuevo = false;
				}
			}

			if (productoNuevo) {
				this.getListaDeProductos().add(cp);
			}
		}

		boolean picoNuevo = true;
		if (cs.getListaDeChequeosPicos() != null && cs.getListaDeChequeosPicos().size() > 0){
			ChequeoPico cp = cs.getListaDeChequeosPicos().iterator().next();
			for (ChequeoPico cp2 : this.getListaDeChequeosPicos()){
				if (cp.getPico().getId() == cp2.getPico().getId()){
					cp2.merge(cp);
					picoNuevo = false;
				}
			}

			if (picoNuevo) {
				this.getListaDeChequeosPicos().add(cp);
			}
		}
	}

	@Override
	public ChequeoSurtidorData getChequeoData() {
		ChequeoSurtidorData csd = new ChequeoSurtidorData();
		List<ItemChequeadoData> itemsChequeadosData = new ArrayList<ItemChequeadoData>();
		for(ItemChequeado ic : getItemsChequeados()) {
			itemsChequeadosData.add(ic.getItemChequeadoData());
		}
		csd.setItemsChequeados(itemsChequeadosData);
		return csd;
	}

	public ChequeoProducto buscarChequeoProducto(int idProducto) {
		for (ChequeoProducto cp : getListaDeProductos()){
			if (cp.getProducto() != null && cp.getProducto().getId() == idProducto){
				return cp;
			}
		}
		return null;
	}

	public ChequeoPico buscarChequeoPico(int idPico) {
		for (ChequeoPico cp : getListaDeChequeosPicos()){
			if (cp.getPico() != null && cp.getPico().getId() == idPico){
				return cp;
			}
		}
		return null;
	}

	public boolean tieneChequeo(String estado) {
		boolean tieneChequeoSurtidor = tieneChequeoSurtidor(estado);
		boolean tieneChequeoProducto = tieneChequeoProducto(estado);
		boolean tieneChequeoPico = tieneChequeoPico(estado);
		return tieneChequeoSurtidor || tieneChequeoProducto || tieneChequeoPico;
	}

	public boolean tieneChequeoPico(String estado) {
		for (ChequeoPico cp : getListaDeChequeosPicos()){
			Set<ItemChequeado> ics = cp.getItemsChequeados();
			for (ItemChequeado ic : ics){
				if (ic.getValor() != null && ic.getValor().equalsIgnoreCase(estado)){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean tieneChequeoProducto(String estado) {
		for (ChequeoProducto cp : getListaDeProductos()){
			Set<ItemChequeado> ics = cp.getItemsChequeados();
			for (ItemChequeado ic : ics){
				if (ic.getValor() != null && ic.getValor().equalsIgnoreCase(estado)){
					return true;
				}
			}
		}
		return false;
	}

	public boolean tieneChequeoSurtidor(String estado) {
		Set<ItemChequeado> ics = getItemsChequeados();
		for (ItemChequeado ic : ics){
			if (ic.getValor() != null && ic.getValor().equalsIgnoreCase(estado)){
				return true;
			}
		}
		return false;
	}
	
	public boolean estaCompleto(Activo activo) {
		if (!super.estaCompleto(activo)){
			return false;
		}
		
		if (activo.getTipo() == 1 && ((Surtidor)activo).getPicos().size() > 0 && getListaDeChequeosPicos().size() == 0){
			return false;
		}
		
		for (ChequeoPico chequeoPico : getListaDeChequeosPicos()){
			if (!chequeoPico.estaCompleto(activo)){
				return false;
			}
		}
		
		if (activo.getTipo() == 1 && ((Surtidor)activo).getModelo().getCantidadDeProductos() > 0 && getListaDeProductos().size() == 0){
			return false;
		}
		
		for (ChequeoProducto chequeoProducto : getListaDeProductos()){
			if (!chequeoProducto.estaCompleto(activo)){
				return false;
			}
		}
		return true;
	}
	

	@Override
	public boolean tieneOtraData() {
		return tieneValoresPorManguera();
	}
	
	public boolean tieneValoresPorManguera() {
		for (ChequeoPico cp : getListaDeChequeosPicos()){
			 if (cp.tieneValoresPorManguera()){
				 return true;
			 }
		 }
		return false;
	}

}
