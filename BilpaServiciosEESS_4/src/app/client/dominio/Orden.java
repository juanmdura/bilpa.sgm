package app.client.dominio;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Orden implements com.google.gwt.user.client.rpc.IsSerializable, Comparable{

	private int numero;
	private Date fechaCumplimiento;
	private Date fechaInicio;
	private Date fechaFin;

	private Date inicioService;
	private Date finService;

	private Date inicioServiceReal;
	private Date finServiceReal;

	private int estadoOrden;
	private Persona tecnicoAsignado;
	private String prioridad;
	private Estacion empresa;

	private Set<RepuestoLinea> repuestosLineas = new HashSet<RepuestoLinea>();
	private Set<Reparacion> reparaciones = new HashSet<Reparacion>();
	private Set<Comentario> comentarios = new HashSet<Comentario>();

	private Persona creador;
	private Persona anulador;
	private Persona finalizador;
	private ContactoEmpresa contacto;

	private TipoTrabajo tipoTrabajo;
	private boolean preventivo;

	private String comentarioAnulada;
	private String numeroParteDucsa;

	private Firma firma;
	private Visita visita;

	public Date getInicioService() {
		return inicioService;
	}

	public void setInicioService(Date inicioService) {
		this.inicioService = inicioService;
	}

	public Date getFinService() {
		return finService;
	}

	public void setFinService(Date finService) {
		this.finService = finService;
	}

	public Date getInicioServiceReal() {
		return inicioServiceReal;
	}

	public void setInicioServiceReal(Date inicioServiceReal) {
		this.inicioServiceReal = inicioServiceReal;
	}

	public Date getFinServiceReal() {
		return finServiceReal;
	}

	public void setFinServiceReal(Date finServiceReal) {
		this.finServiceReal = finServiceReal;
	}

	public String getNumeroParteDucsa() {
		return numeroParteDucsa;
	}

	public void setNumeroParteDucsa(String numeroParteDucsa) {
		this.numeroParteDucsa = numeroParteDucsa;
	}

	public boolean isPreventivo() {
		return preventivo;
	}

	public String esPreventivo() {
		if (preventivo)
			return "Si";
		else
			return "No";
	}

	public void setPreventivo(boolean preventivo) {
		this.preventivo = preventivo;
	}

	public TipoTrabajo getTipoTrabajo() {
		return tipoTrabajo;
	}

	public void setTipoTrabajo(TipoTrabajo tipoTrabajo) {
		this.tipoTrabajo = tipoTrabajo;
	}

	public Date getFechaCumplimiento() {
		return fechaCumplimiento;
	}

	public void setFechaCumplimiento(Date fechaCumplimiento) {
		this.fechaCumplimiento = fechaCumplimiento;
	}

	public Persona getFinalizador() {
		return finalizador;
	}

	public void setFinalizador(Persona finalizador) {
		this.finalizador = finalizador;
	}

	public ContactoEmpresa getContacto() {
		return contacto;
	}

	public void setContacto(ContactoEmpresa contacto) {
		this.contacto = contacto;
	}

	public Set<RepuestoLinea> getRepuestosLineas() {
		return repuestosLineas;
	}

	public Set<Comentario> getComentarios() {
		return comentarios;
	}

	public Set<Comentario> getComentariosImprimibles() {
		Set<Comentario> comentariosImprimibles = new HashSet<Comentario>();
		for (Comentario c : comentarios) 
		{
			if(c.isImprimible())
			{
				comentariosImprimibles.add(c);
			}
		}
		return comentariosImprimibles;
	}

	public void setComentarios(Set<Comentario> comentarios) {
		this.comentarios = comentarios;
	}

	public void setRepuestosLineas(Set<RepuestoLinea> repuestosLineas) {
		this.repuestosLineas = repuestosLineas;
	}

	public String getComentarioAnulada() {
		return comentarioAnulada;
	}

	public void setComentarioAnulada(String comentarioAnulada) {
		this.comentarioAnulada = comentarioAnulada;
	}

	public Persona getAnulador() {
		return anulador;
	}

	public void setAnulador(Persona anulador) {
		this.anulador = anulador;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Set<Reparacion> getReparaciones() {
		return reparaciones;
	}

	public void setReparaciones(Set<Reparacion> reparaciones) {
		this.reparaciones = reparaciones;
	}

	public Persona getCreador() {
		return creador;
	}

	public void setCreador(Persona creador) {
		this.creador = creador;
	}

	public Persona getTecnicoAsignado() {
		return tecnicoAsignado;
	}

	public void setTecnicoAsignado(Persona tecnicoAsignado) {
		this.tecnicoAsignado = tecnicoAsignado;			
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaActual() {
		return new Date();
	}

	public int getEstadoOrden() {
		return estadoOrden;
	}

	public void setEstadoOrden(int estadoOrden) {
		this.estadoOrden = estadoOrden;
	}

	public Persona getPersonaAsignada() {
		return tecnicoAsignado;
	}

	public void setPersonaAsignada(Persona personaAsignada) {
		this.tecnicoAsignado = personaAsignada;
	}

	public String getPrioridad() {
		return prioridad;
	}

	public void setPrioridad(String prioridad) {
		this.prioridad = prioridad;
	}

	public Firma getFirma() {
		return firma;
	}

	public void setFirma(Firma firma) {
		this.firma = firma;
	}

	public Visita getVisita() {
		return visita;
	}

	public void setVisita(Visita visita) {
		this.visita = visita;
	}

	public Orden() {

	}

	public Orden(int numero){
		this.numero = numero;
	}
	public Orden(Date fechaInicio, Estacion empresa, int estado) {
		this.fechaInicio = fechaInicio;
		this.empresa = empresa;
		this.estadoOrden=estado;
	}

	public Orden(int numero, Date fechaInicio, int estadoOrden, Tecnico personaAsignada, String prioridad, String localidad) {
		this.numero = numero;
		this.fechaInicio = fechaInicio;
		this.estadoOrden = estadoOrden;
		this.tecnicoAsignado = personaAsignada;
		this.prioridad = prioridad;
	}

	public Estacion getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Estacion empresa) {
		this.empresa = empresa;
	}

	public void quitarReparacion(Reparacion reparacion){
		/*if(reparaciones.contains(reparacion))
			reparaciones.remove(reparacion);*/
		for (Reparacion rep : reparaciones) {
			if (rep.equals(reparacion)){
				reparaciones.remove(rep);
				break;
			}
		}
	}

	public void agregarReparacion(Reparacion reparacion){
		if(reparacion != null){
			reparaciones.add(reparacion);
		}

	}

	public boolean validarReparacionExistente(Reparacion reparacion){
		//true si ya existe, false si no existe
		if(reparacion != null){
			for (Reparacion r : this.reparaciones) {
				if(reparacion.getActivo().getId() == r.getActivo().getId()){
					return true;
				}
			}
		}
		return false;
	}

	public Orden copiar() {
		Orden copia = new Orden();
		copiarPropiedades(copia);
		return copia;
	}

	public Orden copiarTodo() {
		Orden copia = new Orden();
		copiarPropiedades(copia);
		copiarColecciones(copia);
		return copia;
	}
	private void copiarPropiedades(Orden copia) {
		copia.setNumero(getNumero());
		copia.setFechaInicio(getFechaInicio());
		copia.setEstadoOrden(getEstadoOrden());
		copia.setFechaFin(getFechaFin());
		copia.setInicioService(getInicioService());
		copia.setFinService(getFinService());
		copia.setInicioServiceReal(getInicioServiceReal());
		copia.setFinServiceReal(getFinServiceReal());
		copia.setPreventivo(isPreventivo());
		copia.setFechaCumplimiento(getFechaCumplimiento());
		if (getContacto() != null)
		{
			copia.setContacto(getContacto().copiar());			
		}

		if(getTecnicoAsignado()!= null){
			copia.setPersonaAsignada((Persona)getTecnicoAsignado().copiar());
		}

		if (getTipoTrabajo() != null)
		{
			copia.setTipoTrabajo(getTipoTrabajo().copiar());			
		}
		copia.setCreador((Persona) getCreador().copiar());
		copia.setPrioridad(getPrioridad());
		copia.setNumeroParteDucsa(getNumeroParteDucsa());
		copia.setEmpresa(getEmpresa().copiarTodoSinOrdenes());			
		copia.setReparaciones(new HashSet<Reparacion>());
		copia.setRepuestosLineas((new HashSet<RepuestoLinea>()));
		//copia.setComentarios((new HashSet<Comentario>()));
		copia.setFirma(getFirma() != null ? getFirma().copiar() : null); 
	}

	public void copiarColecciones(Orden copia) {
		for (RepuestoLinea r : repuestosLineas) {
			copia.getRepuestosLineas().add(r.copiar());
		}

		for (Reparacion r : reparaciones) {
			copia.getReparaciones().add(r.copiarTodo());
		}

		for (Comentario c : comentarios) {
			copia.getComentarios().add(c.copiar());
		}
	}

	public Reparacion obtenerReparacion(String toStringActivo, String toStringFalla) {
		for(Reparacion r : this.getReparaciones()){
			if(r.getActivo().toString().equals(toStringActivo) && r.getFallaReportada().getDescripcion().equals(toStringFalla)){
				return r;
			}
		}
		return null;
	}

	public void agregarRepuestoYCantidad(Repuesto repuesto, int cantidad){
		if(repuesto != null && cantidad > 0){
			RepuestoLinea rel = new RepuestoLinea(this,repuesto,cantidad);
			this.repuestosLineas.add(rel);
		}
	}

	public int activosReportados(){
		return this.getReparaciones().size();
	}

	public boolean tieneSoluciones(){
		for(Reparacion r : this.getReparaciones()){
			if(r.tieneSoluciones()){
				return true;
			}
		}
		return false;
	}

	public Set<Solucion> getSoluciones(){
		Set<Solucion> soluciones = new HashSet<Solucion>();
		for(Reparacion r : getReparaciones())
		{
			for (Solucion s : r.getSoluciones())
			{
				soluciones.add(s);
			}
		}
		return soluciones;
	}

	public List<Activo> getActivosReparados(){
		List<Activo> activos = new ArrayList<Activo>();
		for(Reparacion r : getReparaciones())
		{
			activos.add(r.getActivo());
		}
		return activos;
	}

	public Reparacion buscarReparacion(int id){
		for(Reparacion r : this.getReparaciones()){
			if(r.getId() == id){
				return r;
			}
		}
		return null;
	}

	public Reparacion buscarReparacion(Solucion solucion){
		for(Reparacion r : this.getReparaciones()){
			for(Solucion s : r.getSoluciones()){
				if(s.getId() == solucion.getId()){
					return r;
				}
			}
		}
		return null;
	}


	public Reparacion buscarReparacionPorActivo(Activo a){
		for(Reparacion r : this.getReparaciones()){
			if(r.getActivo().equals(a)){
				return r;
			}
		}
		return null;
	}

	public int compareTo(Object o) {
		Orden objeto = (Orden) o;
		if(this.fechaInicio.after(objeto.fechaInicio)){
			return -1;
		}
		if(this.fechaInicio.before(objeto.fechaInicio)){
			return 1;
		}
		return 0;
	}

	public Set<RepuestoLinea> getRepuestosLinea(Activo a)
	{
		Set<RepuestoLinea> rls = new HashSet<RepuestoLinea>();
		for(RepuestoLinea rl : repuestosLineas)
		{
			if(rl.getActivo().equals(a))
			{
				rls.add(rl);
			}
		}
		return rls;
	}

	public Reparacion obtenerReparacionDeEsteActivo(Activo activoSeleccionado) {
		for(Reparacion r : getReparaciones()){
			if(r.getActivo().equals(activoSeleccionado)){
				return r;
			}
		}
		return null;
	}

	public Set<RepuestoLinea> obtenerRepuestosLineasSinSolucion(Reparacion reparacion) {
		Set<RepuestoLinea> rls = new HashSet<RepuestoLinea>();
		for(RepuestoLinea rl : repuestosLineas){
			if(rl.getSolucion() ==  null && rl.getActivo().getId() == reparacion.getActivo().getId()){
				rls.add(rl);
			}
		}
		return rls;
	}

	public void removerComentario(Comentario comentario) {
		if(comentario != null){
			for(Comentario c : getComentarios()){
				if(c.getId() == comentario.getId()){
					getComentarios().remove(c);
					return;
				}
			}
		}
	}

	public boolean tieneSolucion(int idSolucion) {
		for (Solucion solucion : getSoluciones()) {
			if (solucion.getId() == idSolucion){
				return true;
			}
		}
		return false;
	}
}
