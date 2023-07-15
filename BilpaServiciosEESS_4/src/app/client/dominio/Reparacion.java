package app.client.dominio;

import java.util.HashSet;
import java.util.Set;

import app.client.dominio.data.DestinoDelCargoData;

public class Reparacion implements com.google.gwt.user.client.rpc.IsSerializable, Comparable{

	private int id;
	private int tipo;
	private Orden orden;
	private Activo activo;
	private FallaReportada fallaReportada;
	private String comentario;
	private Set<Solucion> soluciones = new HashSet<Solucion>();
	private Pendiente pendiente;
	
	public Pendiente getPendiente() {
		return pendiente;
	}

	public void setPendiente(Pendiente pendiente) {
		this.pendiente = pendiente;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public Orden getOrden() {
		return orden;
	}

	public void setOrden(Orden orden) {
		this.orden = orden;
	}
	public Activo getActivo() {
		return activo;
	}
	public void setActivo(Activo activo) {
		this.activo = activo;
	}
	public FallaReportada getFallaReportada() {
		return fallaReportada;
	}
	public void setFallaReportada(FallaReportada fallaReportada) {
		this.fallaReportada = fallaReportada;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public Set<Solucion> getSoluciones() {
		return soluciones;
	}
	public void setSoluciones(Set<Solucion> soluciones) {
		this.soluciones = soluciones;
	}
	public Reparacion(){}

	public Reparacion(int tipo)
	{
		this.tipo = tipo;
	}

	public Reparacion(FallaReportada fallaReportada, Activo activo, int tipo){
		this.fallaReportada = fallaReportada;
		this.activo = activo;
		this.tipo = tipo; 
	}
	
	public Reparacion(FallaReportada fallaReportada, Activo activo, int tipo, Orden orden){
		this.fallaReportada = fallaReportada;
		this.activo = activo;
		this.tipo = tipo; 
		this.orden = orden;
	}
	
	public void set(FallaReportada fallaReportada, Activo activo, int tipo, Orden orden) {
		this.fallaReportada = fallaReportada;
		this.activo = activo;
		this.tipo = tipo; 
		this.orden = orden;
	}

	
	public static Reparacion getReparacion(Activo activo){
		if (activo.getTipo() == 1){
			return new ReparacionSurtidor();
		} else if (activo.getTipo() == 2){
			return new ReparacionTanque();
		} else if (activo.getTipo() == 3 ){
			return new ReparacionCano();
		} else if (activo.getTipo() == 4 ){
			return new ReparacionBomba();
		} else if (activo.getTipo() == 6 ){
			return new ReparacionActivoGenerico();
		}
		return null;
	}

	public Solucion agregarSolucion(Orden orden, Set<Solucion> solucionesOrden, Tarea tarea, FallaTecnica fallaTecnica, 
									Set<RepuestoLinea> repuestos, Boolean telefonica, Pico pico, DestinoDelCargoData destinoDelCargo,
									Comentario comentario){
		
		if (validarSolucion(solucionesOrden, tarea, fallaTecnica, telefonica, pico))
		{
			Solucion solucion = new Solucion(tarea, fallaTecnica, telefonica, this, destinoDelCargo, comentario);
			
			for(RepuestoLinea rl : repuestos){
				orden.getRepuestosLineas().add(rl);
				rl.setSolucion(solucion);
			}
			
			orden.agregarReparacion(this);
			soluciones.add(solucion);
			return solucion;				
		}						
		return null;
	}

	public boolean validarSolucion(Set<Solucion> soluciones, Tarea tarea, FallaTecnica fallaTecnica, Boolean telefonica, Pico pico)
	{
		if (pico != null)
		{
			return true;
		}

		if(tarea != null && fallaTecnica != null){
			for (Solucion sol : soluciones)
			{
				if (sol.getReparacion().getActivo().equals(activo) &&
						sol.getTarea().equals(tarea) && 
						sol.getFallaTecnica().equals(fallaTecnica) && 
						sol.isTelefonica() == telefonica)
				{
					return false;						
				}
			}
		}
		return true;
	}

	public void quitarSolucion(Solucion solucion){
		if(solucion != null)
		{
			soluciones.remove(solucion);	
		}		
	}

	public int compareTo(Object o) {
		Reparacion reparacion = (Reparacion) o;
		if(reparacion.getActivo().equals(this.getActivo())){
			return 0;
		}
		return 1;
	}

	@Override
	public String toString()
	{
		if (fallaReportada == null)
		{
			return "N/A" + " - " + activo.toString();
		}
		return fallaReportada.getDescripcion() + " - " + activo.toString();	
	}

	public boolean tieneSoluciones(){
		if(this.getSoluciones().size() > 0){
			return true;
		}
		return false;
	}

	public boolean validarFallaTecnicaAgregada(FallaTecnica fall){
		for (Solucion s : this.getSoluciones()) {
			if(s.getFallaTecnica().getId() == fall.getId()){
				return true; //esta falla ya fue agregada
			}
		}
		return false;
	}

	public Solucion buscarSolucion(Solucion sol){
		for (Solucion s : this.getSoluciones()) {
			if (s.equals(sol))
				return s;
		}
		return null;
	}

	public static Reparacion crearReparacion(Activo activo)
	{
		if (activo.getTipo() == 1)
		{
			return new ReparacionSurtidor();
		}
		if (activo.getTipo() == 2)
		{
			return new ReparacionTanque();
		}
		if (activo.getTipo() == 3)
		{
			return new ReparacionCano();
		}
		if (activo.getTipo() == 4)
		{
			return new ReparacionBomba();
		}
		if (activo.getTipo() == 6)
		{
			return new ReparacionActivoGenerico();
		}
		return null;
	}

	public static Reparacion crearReparacion(Orden orden, FallaReportada fallaReportada, Activo activo)
	{
		Reparacion rep = new Reparacion();
		if (activo.getTipo() == 1)
		{
			rep = new ReparacionSurtidor();			
		}else if (activo.getTipo() == 2)
		{
			rep = new ReparacionTanque();
		}else if (activo.getTipo() == 3)
		{
			rep =  new ReparacionCano();
		}else if (activo.getTipo() == 4)
		{
			rep =  new ReparacionBomba();
		}else if (activo.getTipo() == 6)
		{
			rep =  new ReparacionActivoGenerico();
		}else {
			return null;
		}
		rep.setOrden(orden);
		rep.setFallaReportada(fallaReportada);
		rep.setActivo(activo);
		return rep;
	}

	public Reparacion copiar() {
		Reparacion copia = crearReparacion(getActivo());
		copiarPropiedades(copia);
		copia.setSoluciones(new HashSet<Solucion>());
		return copia;
	}

	public Reparacion copiarMinimo() {
		Reparacion copia = crearReparacion(getActivo());
		copia.setId(getId());
		copia.setTipo(getTipo());
		copia.setComentario(getComentario());
		copia.setActivo(getActivo().copiar());
		
		for (Solucion s : getSoluciones()) {
			copia.getSoluciones().add(s.copiarTodo());
		}
		
		if (getFallaReportada() != null)
		{
			copia.setFallaReportada(getFallaReportada().copiar());			
		}
		
		return copia;
	}
	
	protected void copiarPropiedades(Reparacion copia) {
		copia.setActivo(getActivo().copiarTodo());
		copia.setTipo(getTipo());
		copia.setComentario(getComentario());
		if (getFallaReportada() != null)
		{
			copia.setFallaReportada(getFallaReportada().copiar());			
		}
		copia.setId(getId());
		copia.setOrden(getOrden().copiar());
		copia.setSoluciones(new HashSet<Solucion>());
	}

	public Reparacion copiarTodo() {
		Reparacion copia = crearReparacion(getActivo());
		copiarPropiedades(copia);
		copiarColecciones(copia);
		return copia;
	}

	protected void copiarColecciones(Reparacion copia) {
		for (Solucion s : getSoluciones()) {
			copia.getSoluciones().add(s.copiarTodo());
		}
	}

	public boolean equals(Reparacion rep)
	{
		return rep.getId() == getId();
	}

	public boolean tieneAlgunaSolucionTelefonica()
	{
		for (Solucion s : getSoluciones()) 
		{
			if(s.isTelefonica()){
				return true;
			}
		}
		return false;
	}

	public boolean tienePicosReparados()
	{
		if(getClass().equals(ReparacionSurtidor.class))
		{
			ReparacionSurtidor rs = (ReparacionSurtidor)this;
			for (Solucion s : getSoluciones()) 
			{
				Contador contador = rs.buscarContador(s);

				if (contador != null && contador.getPico() != null)
				{
					return true;
				}
			}
		}
		return false;
	}

	public int getProximoNumeroEnReparacion() {
		int numeroEnRepMax = getSoluciones().size() + 1;
		for(int numeroEnRepAux = 1 ; numeroEnRepAux <= numeroEnRepMax ; numeroEnRepAux ++){
			if(!existeSolucionConNumeroEnReparacion(numeroEnRepAux)){
				return numeroEnRepAux;
			}
		}
		return 0;
	}

	private boolean existeSolucionConNumeroEnReparacion(int numeroEnRepAux) {
		for(Solucion s : soluciones){
			if(s.getNumeroEnReparacion() == numeroEnRepAux){
				return true;
			}
		}
		return false;
	}

	public boolean tieneSolucionesConComentariosImprimibles() {
		for(Solucion s : soluciones){
			if(s.getComentario() != null && s.getComentario().isImprimible()){
				return true;
			}
		}
		return false;
	}
}
