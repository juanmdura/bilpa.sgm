package app.client.utilidades.utilObjects.filter.orden;

import java.util.ArrayList;

import app.client.dominio.data.EstacionData;
import app.client.dominio.data.EstacionDataList;
import app.client.dominio.data.TecnicoData;

public class ListaOrdenesData implements com.google.gwt.user.client.rpc.IsSerializable{
	//Activas
	private int indicadorIniciadas;
	private int indicadorIniciadasDUCSA;
	private int indicadorInspeccionPendiente;
	private int indicadorReparadas;
	
	//Inactivas
	private int indicadorFinalizadas;
	private int indicadorAnuladas;
	
	private ArrayList<String> estadoActivos = new ArrayList<String>();//Hacer enumerado
	private ArrayList<String> estadoInactivos = new ArrayList<String>();//Hacer enumerado
	
	private ArrayList<TecnicoData> tecnicosData = new ArrayList<TecnicoData>();
	
	private ArrayList<String> localidades = new ArrayList<String>();
	private ArrayList<EstacionData> estaciones = new ArrayList<EstacionData>();
	private ArrayList<EstacionDataList> estacionesData = new ArrayList<EstacionDataList>();
	
	public ArrayList<String> getEstadoActivos() {
		return estadoActivos;
	}
	public void setEstadoActivos(ArrayList<String> estadoActivos) {
		this.estadoActivos = estadoActivos;
	}
	public ArrayList<String> getEstadoInactivos() {
		return estadoInactivos;
	}
	public void setEstadoInactivos(ArrayList<String> estadoInactivos) {
		this.estadoInactivos = estadoInactivos;
	}
	
	public int getIndicadorIniciadas() {
		return indicadorIniciadas;
	}
	public void setIndicadorIniciadas(int indicadorIniciadas) {
		this.indicadorIniciadas = indicadorIniciadas;
	}
	public int getIndicadorIniciadasDUCSA() {
		return indicadorIniciadasDUCSA;
	}
	public void setIndicadorIniciadasDUCSA(int indicadorIniciadasDUCSA) {
		this.indicadorIniciadasDUCSA = indicadorIniciadasDUCSA;
	}
	public int getIndicadorInspeccionPendiente() {
		return indicadorInspeccionPendiente;
	}
	public void setIndicadorInspeccionPendiente(int indicadorInspeccionPendiente) {
		this.indicadorInspeccionPendiente = indicadorInspeccionPendiente;
	}
	public int getIndicadorReparadas() {
		return indicadorReparadas;
	}
	public void setIndicadorReparadas(int indicadorReparadas) {
		this.indicadorReparadas = indicadorReparadas;
	}
	public int getIndicadorFinalizadas() {
		return indicadorFinalizadas;
	}
	public void setIndicadorFinalizadas(int indicadorFinalizadas) {
		this.indicadorFinalizadas = indicadorFinalizadas;
	}
	public int getIndicadorAnuladas() {
		return indicadorAnuladas;
	}
	public void setIndicadorAnuladas(int indicadorAnuladas) {
		this.indicadorAnuladas = indicadorAnuladas;
	}
	public ArrayList<TecnicoData> getTecnicosData() {
		return tecnicosData;
	}
	public void setTecnicosData(ArrayList<TecnicoData> tecnicosData) {
		this.tecnicosData = tecnicosData;
	}
	public ArrayList<String> getLocalidades() {
		return localidades;
	}
	public void setLocalidades(ArrayList<String> localidades) {
		this.localidades = localidades;
	}
	public ArrayList<EstacionData> getEstaciones() {
		return estaciones;
	}
	public void setEstaciones(ArrayList<EstacionData> estaciones) {
		this.estaciones = estaciones;
	}
	public ArrayList<EstacionDataList> getEstacionesData() {
		return estacionesData;
	}
	public void setEstacionesData(ArrayList<EstacionDataList> estacionesData) {
		this.estacionesData = estacionesData;
	}
}
