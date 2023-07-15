package app.client.dominio.data;

import java.util.List;

import app.client.dominio.Calibre;
import app.client.dominio.Comentario;
import app.client.dominio.Contador;
import app.client.dominio.DestinoDelCargo;
import app.client.dominio.FallaTecnica;
import app.client.dominio.RepuestoLinea;
import app.client.dominio.Tarea;

public class SolucionData {
	private Tarea tarea;
	private FallaTecnica fallaTecnica;
	private boolean telefonica;			//True si se resolvio telefonicamente!
	private int id;
	private DestinoDelCargo destinoDelCargo;
	private Comentario comentario;
	
	private List<RepuestoLinea> repuestos;
	private List<Contador> contadores;
	private Calibre calibre;
	private PrecintoData precinto;
	private PicoData pico;
	private String urlFoto;
	private String urlFoto2;
	
	public Tarea getTarea() {
		return tarea;
	}
	public void setTarea(Tarea tarea) {
		this.tarea = tarea;
	}
	public FallaTecnica getFallaTecnica() {
		return fallaTecnica;
	}
	public void setFallaTecnica(FallaTecnica fallaTecnica) {
		this.fallaTecnica = fallaTecnica;
	}
	public boolean isTelefonica() {
		return telefonica;
	}
	public void setTelefonica(boolean telefonica) {
		this.telefonica = telefonica;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public DestinoDelCargo getDestinoDelCargo() {
		return destinoDelCargo;
	}
	public void setDestinoDelCargo(DestinoDelCargo destinoDelCargo) {
		this.destinoDelCargo = destinoDelCargo;
	}
	public Comentario getComentario() {
		return comentario;
	}
	public void setComentario(Comentario comentario) {
		this.comentario = comentario;
	}
	public String getUrlFoto() {
		return urlFoto;
	}
	public void setUrlFoto(String urlFoto) {
		this.urlFoto = urlFoto;
	}
	public String getUrlFoto2() {
		return urlFoto2;
	}
	public void setUrlFoto2(String urlFoto2) {
		this.urlFoto2 = urlFoto2;
	}
	public Calibre getCalibre() {
		return calibre;
	}
	public void setCalibre(Calibre calibre) {
		this.calibre = calibre;
	}
	public PrecintoData getPrecinto() {
		return precinto;
	}
	public void setPrecinto(PrecintoData precinto) {
		this.precinto = precinto;
	}
	public List<RepuestoLinea> getRepuestos() {
		return repuestos;
	}
	public void setRepuestos(List<RepuestoLinea> repuestos) {
		this.repuestos = repuestos;
	}
	public List<Contador> getContadores() {
		return contadores;
	}
	public void setContadores(List<Contador> contadores) {
		this.contadores = contadores;
	}
	public PicoData getPico() {
		return pico;
	}
	public void setPico(PicoData pico) {
		this.pico = pico;
	}
}
