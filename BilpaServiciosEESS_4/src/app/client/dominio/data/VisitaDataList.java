package app.client.dominio.data;

import java.util.Date;

import app.client.dominio.EstadoVisita;

public class VisitaDataList implements
		com.google.gwt.user.client.rpc.IsSerializable, Comparable<VisitaDataList> {

	private int id;

	private Date fechaProximaVisita;
	private Date fechaUltimaVisita;
	private Date fechaRealizada;
	private TecnicoData tecnicoData;
	private EstacionData estacionData;
	private EstadoVisita estado;
	private int diasSinVisita;
	
	public int getDiasSinVisita() {
		return diasSinVisita;
	}

	public void setDiasSinVisita(int diasSinVisita) {
		this.diasSinVisita = diasSinVisita;
	}

	public TecnicoData getTecnicoData() {
		return tecnicoData;
	}

	public void setTecnicoData(TecnicoData tecnicoData) {
		this.tecnicoData = tecnicoData;
	}

	public VisitaDataList() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Date getFechaUltimaVisita() {
		//return new Date();
		return fechaUltimaVisita;
	}

	public void setFechaUltimaVisita(Date fechaUltimaVisita) {
		this.fechaUltimaVisita = fechaUltimaVisita;
	}

	public EstacionData getEstacionData() {
		return estacionData;
	}

	public void setEstacionData(EstacionData estacionData) {
		this.estacionData = estacionData;
	}

	public EstadoVisita getEstado() {
		return estado;
	}

	public void setEstado(EstadoVisita estado) {
		this.estado = estado;
	}

	public int compareTo(VisitaDataList visitaDataList) {
		/*if (visitaDataList.getFechaUltimaVisita() == null && getFechaUltimaVisita() == null){
			return 0;
		
		} else if (visitaDataList.getFechaUltimaVisita() == null){
			return -1;
		
		} else if (getFechaUltimaVisita() == null){
			return 1;

		} else {
			return getFechaUltimaVisita().compareTo(visitaDataList.getFechaUltimaVisita());
		}*/
		if (diasSinVisita > visitaDataList.diasSinVisita){
			return 1;
		}
		if (diasSinVisita < visitaDataList.diasSinVisita){
			return -1;
		}
		return 0;
	}

	

//	public int getDiasSinVisitas() {
//		int dias = 0;
//		if (fechaUltimaVisita != null) {
////			/*
//			 * dias = (int) (Math.abs(new Date().getTime() -
//			 * fechaUltimaVisita.getTime()) / (24 * 60 * 60 * 1000));
//			 * DateTimeFormat
//			 * formater=DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);
//			 * long d1; long d2; d1 = formater.parse(new
//			 * Date().toString()).getTime(); d2 =
//			 * formater.parse(fechaUltimaVisita.toString()).getTime();
//			 * 
//			 * dias = (int) (Math.abs((d1-d2)/(1000*60*60*24)));
//			 */
//			dias = 18;
//
//		}
//		return dias;
//	}
}
