package app.client.dominio.data;

import java.util.ArrayList;
import java.util.List;

import app.client.dominio.MarcaActivoGenerico;
import app.client.dominio.ModeloActivoGenerico;
import app.client.dominio.TipoActivoGenerico;


public class TipoActivoGenericoData implements com.google.gwt.user.client.rpc.IsSerializable{

	private int id;
	private String nombre;
	private boolean activo;
	
	private List<MarcaActivoGenerico> marcas = new ArrayList<MarcaActivoGenerico>();
	private List<ModeloActivoGenerico> modelos = new ArrayList<ModeloActivoGenerico>();
	
	public TipoActivoGenericoData(int id2) {
		setId(id2);
	}
	public TipoActivoGenericoData() {
	}

	public TipoActivoGenericoData(TipoActivoGenerico tipoActivoGenerico, List<MarcaActivoGenerico> marcas,	List<ModeloActivoGenerico> modelos) {
		this.setId(tipoActivoGenerico.getId());
		this.setActivo(tipoActivoGenerico.isActivo());
		this.setNombre(tipoActivoGenerico.getNombre());
		this.setMarcas(marcas);
		this.setModelos(modelos);
	}

	public boolean isActivo() {
		return activo;
	}
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public List<MarcaActivoGenerico> getMarcas() {
		return marcas;
	}
	public void setMarcas(List<MarcaActivoGenerico> marcas) {
		this.marcas = marcas;
	}
	public List<ModeloActivoGenerico> getModelos() {
		return modelos;
	}
	
	public List<ModeloActivoGenerico> getModelos(int idMarca) {
		List<ModeloActivoGenerico> modeloss = new ArrayList<ModeloActivoGenerico>();
		for (ModeloActivoGenerico modelo : modelos) {
			if (modelo.getMarca().getId() == idMarca){
				modeloss.add(modelo);
			}
		}
		return modeloss;
	}
	
	public void setModelos(List<ModeloActivoGenerico> modelos) {
		this.modelos = modelos;
	}
	@Override
	public String toString() {
		return nombre;
	}
}
