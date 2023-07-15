package app.client.dominio.data;

import java.util.HashSet;
import java.util.Set;

import app.client.dominio.FallaTecnica;
import app.client.dominio.Tarea;

public class CorregidoData  implements com.google.gwt.user.client.rpc.IsSerializable {
	private int id;
	private int idPreventivo;
	private FallaTecnica falla;
	private Tarea tarea;
	private DestinoDelCargoData destinoDelCargo;
	private Set<RepuestoLineaCorregidoData> listaDeRepuestos = new HashSet<RepuestoLineaCorregidoData>();
	private String urlFoto;
	private String urlFoto2;
	private ComentarioData comentario;
	private int idItemChequeado;
	private String textoItemChequado;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdItemChequeado() {
		return idItemChequeado;
	}
	public void setIdItemChequeado(int idItemChequeado) {
		this.idItemChequeado = idItemChequeado;
	}
	public String getTextoItemChequado() {
		return textoItemChequado;
	}
	public void setTextoItemChequado(String textoItemChequado) {
		this.textoItemChequado = textoItemChequado;
	}
	public int getIdPreventivo() {
		return idPreventivo;
	}
	public void setIdPreventivo(int idPreventivo) {
		this.idPreventivo = idPreventivo;
	}
	public FallaTecnica getFalla() {
		return falla;
	}
	public void setFalla(FallaTecnica falla) {
		this.falla = falla;
	}
	public Tarea getTarea() {
		return tarea;
	}
	public void setTarea(Tarea tarea) {
		this.tarea = tarea;
	}
	public DestinoDelCargoData getDestinoDelCargo() {
		return destinoDelCargo;
	}
	public void setDestinoDelCargo(DestinoDelCargoData destinoDelCargo) {
		this.destinoDelCargo = destinoDelCargo;
	}
	public Set<RepuestoLineaCorregidoData> getListaDeRepuestos() {
		return listaDeRepuestos;
	}
	public void setListaDeRepuestos(Set<RepuestoLineaCorregidoData> listaDeRepuestos) {
		this.listaDeRepuestos = listaDeRepuestos;
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
	public ComentarioData getComentario() {
		return comentario;
	}
	public void setComentario(ComentarioData comentario) {
		this.comentario = comentario;
	}
	
}
