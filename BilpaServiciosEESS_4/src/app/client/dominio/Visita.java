package app.client.dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import app.client.dominio.data.ActivoData;
import app.client.dominio.data.VisitaData;
import app.client.dominio.data.VisitaDataList;

public class Visita implements com.google.gwt.user.client.rpc.IsSerializable {

	private int id;

	private Set<Preventivo> listaDePreventivos = new HashSet<Preventivo>();

	private Date fechaCreada = new Date();
	private Date fechaRealizada;

	private Date fechaProximaVisita;
	private Date fechaInicio;
	private Date fechaFin;

	private Firma firma;

	private Estacion estacion;

	private Tecnico tecnico;

	private EstadoVisita estado;

	private boolean inactiva;
	
	private boolean notificada;
	
	
	public Visita() {}
	
	
	public boolean isInactiva() {
		return inactiva;
	}

	public void setInactiva(boolean inactiva) {
		this.inactiva = inactiva;
	}

	public boolean isNotificada() {
		return notificada;
	}
	public void setNotificada(boolean notificada) {
		this.notificada = notificada;
	}
	public void setEstado(EstadoVisita estado) {
		this.estado = estado;
	}

	public Estacion getEstacion() {
		return estacion;
	}

	public void setEstacion(Estacion estacion) {
		this.estacion = estacion;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Set<Preventivo> getListaDePreventivos() {
		return listaDePreventivos;
	}

	public void setListaDePreventivos(Set<Preventivo> listaDePreventivos) {
		this.listaDePreventivos = listaDePreventivos;
	}

	public Firma getFirma() {
		return firma;
	}

	public void setFirma(Firma firma) {
		this.firma = firma;
	}

	public Tecnico getTecnico() {
		return tecnico;
	}

	public void setTecnico(Tecnico tecnico) {
		this.tecnico = tecnico;
	}

	public Date getFechaCreada() {
		return fechaCreada;
	}

	public void setFechaCreada(Date fechaCreada) {
		this.fechaCreada = fechaCreada;
	}

	public Date getFechaRealizada() {
		return fechaRealizada;
	}

	public void setFechaRealizada(Date fechaRealizada) {
		this.fechaRealizada = fechaRealizada;
	}

	public Date getFechaProximaVisita() {
		return fechaProximaVisita;
	}

	public void setFechaProximaVisita(Date fechaProximaVisita) {
		this.fechaProximaVisita = fechaProximaVisita;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public EstadoVisita getEstado() {
		return estado;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public VisitaDataList getVisitaDataList() {

		VisitaDataList aux = new VisitaDataList();

		aux.setId(id);
		aux.setEstacionData(estacion.getEstacionData());
		aux.setFechaUltimaVisita(getEstacion().getFechaUltimaVisita());
		aux.setEstado(estado);
		aux.setFechaRealizada(fechaRealizada);
		aux.setFechaUltimaVisita(estacion.getFechaUltimaVisita());
		
		aux.setFechaProximaVisita(fechaProximaVisita);
		if (tecnico != null) {
			aux.setTecnicoData(tecnico.getTecnicoData());
		}

		//aux.setDiasSinVisita(app.server.UtilFechas.getDiasSinVisitas(getEstacion().getFechaUltimaVisita()));
		return aux;
	}

	public VisitaData getVisitaData() {

		VisitaData aux = new VisitaData();

		aux.setId(id);
		aux.setNombreEstacion(estacion.getNombre());
		aux.setIdEstacion(estacion.getId());

		// aux.setFechaInicio(app.server.UtilFechas.getFormattedDate(fechaInicio));
		// aux.setFechaFin(app.server.UtilFechas.getFormattedDate(fechaFin));

		if (firma != null){
			aux.setUrl(firma.getUrl());
			aux.setComentarioFirma(firma.getComentario());
		}
		aux.setEstado(EstadoVisita.getEstadoVisitaStr(getEstado().name()).toString());
		
		
		// String fechaProx = app.server.UtilFechas.getMonthForInt(fechaProximaVisita);
		// aux.setFechaProximaVisita(fechaProx);

		List<ActivoData> listaActivos = new ArrayList<ActivoData>();
		for (Activo a : estacion.getListaDeActivos()) {
			String nombreGenerico = null;
			try{
				nombreGenerico = ((ActivoGenerico)a).getTipoActivoGenerico().getNombre();
			} catch (Exception e){
				
			}
			ActivoData activoData = new ActivoData(a.getId(), String.valueOf(a.getTipo()), a.toString(), nombreGenerico);
			if (a.getQr() != null){
				activoData.setCodigoQR(Integer.valueOf(a.getQr().getCodigo()));
			}
			listaActivos.add(activoData);
		}
		Collections.sort(listaActivos);
		aux.setListaActivos(listaActivos);

		return aux;
	}
	
	public boolean estaCompleta() {
		if (getListaDePreventivos().isEmpty()){
			return false;
		}
		
		for(Preventivo p : getListaDePreventivos()) {
			if (p.getChequeo() == null){
				return false;
			}
					
		}
		return true;
	}
	
	public static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static String DATE_FORMAT_SHORT = "yyyy-MM-dd";

	public List<Corregido> getCorregidos() {
		List<Corregido> corregidos = new ArrayList<Corregido>();
		for (Preventivo p : getListaDePreventivos()){
			for (ItemChequeado i : p.getChequeo().getItemsChequeados()){
				corregidos.addAll(i.getListaDeCorregidos());
			}
		}
		return corregidos;
	}

}
