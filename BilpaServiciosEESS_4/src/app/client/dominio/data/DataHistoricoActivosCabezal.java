package app.client.dominio.data;

import java.util.ArrayList;
import java.util.Date;

import app.client.dominio.Tecnico;

public class DataHistoricoActivosCabezal implements com.google.gwt.user.client.rpc.IsSerializable{
	
	private ArrayList<EstacionDataList> estaciones = new ArrayList<EstacionDataList>();
	private ArrayList<Tecnico> tecnicos = new ArrayList<Tecnico>();
	private ArrayList<String> tiposActivos = new ArrayList<String>();
	private Date horaServidor;
	
	public DataHistoricoActivosCabezal(ArrayList<EstacionDataList> estaciones,ArrayList<Tecnico> tecnicos, ArrayList<String> tiposActivos, Date hora) 
	{
		this.estaciones = estaciones;
		this.tecnicos = tecnicos;
		this.tiposActivos = tiposActivos;
		this.horaServidor = hora;
	}

	public DataHistoricoActivosCabezal(){}

	public ArrayList<EstacionDataList> getEstaciones() {
		return estaciones;
	}

	public void setEstaciones(ArrayList<EstacionDataList> estaciones) {
		this.estaciones = estaciones;
	}

	public ArrayList<Tecnico> getTecnicos() {
		return tecnicos;
	}

	public void setTecnicos(ArrayList<Tecnico> tecnicos) {
		this.tecnicos = tecnicos;
	}

	public ArrayList<String> getTiposActivos() {
		return tiposActivos;
	}

	public void setTiposActivos(ArrayList<String> tiposActivos) {
		this.tiposActivos = tiposActivos;
	}

	public Date getHoraServidor() {
		return horaServidor;
	}

	public void setHoraServidor(Date horaServidor) {
		this.horaServidor = horaServidor;
	}
}
