package app.client.dominio;

import java.util.HashSet;
import java.util.Set;

import app.client.dominio.data.DestinoDelCargoData;

public class Solucion implements com.google.gwt.user.client.rpc.IsSerializable, Comparable{

	private Tarea tarea;
	private FallaTecnica fallaTecnica;
	private boolean telefonica;			//True si se resolvio telefonicamente!
	private int id;
	private Reparacion reparacion;
	private int numeroEnReparacion;
	private DestinoDelCargo destinoDelCargo;
	private Comentario comentario;
	private Corregido corregido;
	
	private Calibre calibre;
	private Precinto precinto;

	private Foto foto;
	private Foto foto2;
	
	public Calibre getCalibre() {
		return calibre;
	}

	public void setCalibre(Calibre calibre) {
		this.calibre = calibre;
	}

	public Precinto getPrecinto() {
		return precinto;
	}

	public void setPrecinto(Precinto precinto) {
		this.precinto = precinto;
	}

	public Foto getFoto() {
		return foto;
	}

	public void setFoto(Foto foto) {
		this.foto = foto;
	}

	public Foto getFoto2() {
		return foto2;
	}

	public void setFoto2(Foto foto2) {
		this.foto2 = foto2;
	}

	public Comentario getComentario() {
		return comentario;
	}

	public void setComentario(Comentario comentario) {
		this.comentario = comentario;
	}

	public DestinoDelCargo getDestinoDelCargo() {
		return destinoDelCargo;
	}

	public void setDestinoDelCargo(DestinoDelCargo destinoDelCargo) {
		this.destinoDelCargo = destinoDelCargo;
	}

	public int getNumeroEnReparacion() {
		return numeroEnReparacion;
	}

	public void setNumeroEnReparacion(int numeroEnReparacion) {
		this.numeroEnReparacion = numeroEnReparacion;
	}

	public Reparacion getReparacion() {
		return reparacion;
	}

	public void setReparacion(Reparacion reparacion) {
		this.reparacion = reparacion;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isTelefonica() {
		return telefonica;
	}

	public void setTelefonica(boolean telefonica) {
		this.telefonica = telefonica;
	}

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

	public Solucion(){

	}

	public Solucion(Tarea tarea, FallaTecnica fallaTecnica, Boolean telefonica, Reparacion rep, 
						DestinoDelCargoData destinoDelCargo, Comentario comentario){
		this.fallaTecnica = fallaTecnica;
		this.tarea = tarea;
		this.telefonica = telefonica;
		this.reparacion = rep;
		//numeroEnReparacion = rep.getSoluciones().size() + 1;
		numeroEnReparacion = rep.getProximoNumeroEnReparacion();
		if (destinoDelCargo != null){
			this.destinoDelCargo = new DestinoDelCargo(destinoDelCargo);
		}
		if(comentario != null){
			setearComentarios(comentario);
		}
	}

	public Solucion(int id) {
		setId(id);
	}
	
	public Corregido getCorregido() {
		return corregido;
	}

	public void setCorregido(Corregido corregido) {
		this.corregido = corregido;
	}

	private void setearComentarios(Comentario comentarioData) {
//			DateTimeFormat fmt = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");
//			Date fecha = fmt.parse(comentarioData.getFecha()+":00");
			
			comentario = new Comentario(null, comentarioData.isImprimible(), comentarioData.getTexto(), comentarioData.getUsuario());
	}

	public boolean equals(Solucion sol)
	{
		return sol.getReparacion().equals(reparacion) && sol.getNumeroEnReparacion() == numeroEnReparacion;
	}

	@Override
	public String toString()
	{
		return fallaTecnica.getDescripcion() + " - " + tarea.getDescripcion();
	}
	
	public Solucion copiar() {
		Solucion copia = new Solucion();
		copiarPropiedades(copia);
		return copia;
	}

	public Solucion copiarTodo() {
		Solucion copia = new Solucion();
		copiarPropiedades(copia);
		copiarColecciones(copia);
		return copia;
	}
	private void copiarPropiedades(Solucion copia) {
		copia.setId(getId());
		copia.setFallaTecnica(getFallaTecnica().copiar());
		if( getDestinoDelCargo() != null ){
			copia.setDestinoDelCargo(getDestinoDelCargo().copiar());
		}
		copia.setTarea(getTarea().copiar());
		copia.setTelefonica(isTelefonica());
		copia.setReparacion(getReparacion().copiar());
		copia.setNumeroEnReparacion(getNumeroEnReparacion());
		copia.setComentario(getComentario() != null ? getComentario().copiar() : null);
		
		copia.setCalibre(getCalibre() != null ? getCalibre().copiar() : null);
		copia.setPrecinto(getPrecinto() != null ? getPrecinto().copiar() : null);
		copia.setFoto(getFoto() != null ? getFoto() : null); 
		copia.setFoto2(getFoto2() != null ? getFoto2() : null); 
	}

	public void copiarColecciones(Solucion copia) {
	}

	public int compareTo(Object o) {
		Solucion s = (Solucion)o;
		if (s.numeroEnReparacion < this.numeroEnReparacion)
		{
			return 1;
		}
		else if (s.numeroEnReparacion > this.numeroEnReparacion)
		{
			return -1;
		}
		else
		{
			return 0;
		}		
	}

	public Set<RepuestoLinea> obtenerRepuestosLinea(Set<RepuestoLinea> repuestosLinea) {
		HashSet<RepuestoLinea> retorno = new HashSet<RepuestoLinea>();
		for(RepuestoLinea rl : repuestosLinea){
			if(rl.getSolucion() != null && rl.getSolucion().equals(this)){
				retorno.add(rl);
			}
		}
		return retorno;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public void merge(Solucion solucionParam) {
		setId(solucionParam.getId());
		setTarea(solucionParam.getTarea());
		setFallaTecnica(solucionParam.getFallaTecnica());
		setComentario(solucionParam.getComentario());
		setCalibre(solucionParam.getCalibre());
		setPrecinto(solucionParam.getPrecinto());
		setDestinoDelCargo(solucionParam.getDestinoDelCargo());
		setReparacion(solucionParam.getReparacion());
		setPrecinto(solucionParam.getPrecinto());
		setCalibre(solucionParam.getCalibre());
		setNumeroEnReparacion(solucionParam.getNumeroEnReparacion());
	}

	public boolean tienePrecintos(){
		return getCalibre()!= null && (
				getCalibre().getCalibre1() != null && getCalibre().getCalibre2() != null && getCalibre().getCalibre3() != null && getCalibre().getCalibre1() > 0 && getCalibre().getCalibre2() > 0 && getCalibre().getCalibre3() > 0);
	}
	
	public boolean tienePrecintosReemplazados(){
		return getCalibre() != null && (
				getCalibre().getCalibre4() != null && getCalibre().getCalibre5() != null && getCalibre().getCalibre6() != null && getCalibre().getCalibre4() > 0 && getCalibre().getCalibre5() > 0 && getCalibre().getCalibre6() > 0);
	}

	public Pico getPico(Set<Contador> contadores) {
		if (getCalibre() != null){
			return getCalibre().getPico();
		}
		
		for (Contador contador : contadores){
			if (contador.getSolucion().getId() == getId()){
				return contador.getPico();
			}
		}
		return null;
	}

	public Contador getContadores(Set<Contador> contadores) {
		for (Contador contador : contadores) {
			if (contador.getSolucion().getId() == getId()){
				return contador;
			}
		}
		return null;
	}
}