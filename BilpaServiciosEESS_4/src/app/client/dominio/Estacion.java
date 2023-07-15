package app.client.dominio;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import app.client.dominio.data.EmailEmpresaData;
import app.client.dominio.data.EstacionData;
import app.client.dominio.data.EstacionDataList;

public class Estacion implements com.google.gwt.user.client.rpc.IsSerializable{

	private String numeroSerie;
	private Sello sello;
	private int zona;
	private Set<Activo> listaDeActivos = new HashSet<Activo>();
	private int tiempoRespuesta;
	private int id;
	private String nombre;	
	private String localidad;
	private String departamento;
	private String direccion;
	private String telefono;
	private String rut;
	private Foto foto;
	private Foto fotoChica;
	private boolean inactiva;
	private Set<Orden> listaOrdenes = new HashSet<Orden>();
	// private Set<ContactoEmpresa> listaContactos = new HashSet<ContactoEmpresa>();
	private Set<EmailEmpresa> listaEmail = new HashSet<EmailEmpresa>();
	private Date fechaUltimaVisita;
	
	public Date getFechaUltimaVisita() {
		return fechaUltimaVisita;
	}

	public void setFechaUltimaVisita(Date fechaUltimaVisita) {
		this.fechaUltimaVisita = fechaUltimaVisita;
	}
	
	public int getTiempoRespuesta() {
		return tiempoRespuesta;
	}

	public void setTiempoRespuesta(int tiempoRespuesta) {
		this.tiempoRespuesta = tiempoRespuesta;
	}

	public Estacion copiar() {
		Estacion copia = new Estacion();
		this.copiarPropiedades(copia);
		return copia;
	}

	public Estacion copiarTodo() {
		Estacion copia = new Estacion();
		copiarPropiedades(copia);
		copiarColecciones(copia);
		return copia;
	}
	
	public Estacion copiarConContactos() {
		Estacion copia = new Estacion();
		copiarPropiedades(copia);
		return copia;
	}
	
	public void copiarPropiedades(Estacion copia) {
		copia.setId(getId());
		copia.setNombre(getNombre());
		copia.setLocalidad(getLocalidad());
		copia.setDepartamento(getDepartamento());
		copia.setDireccion(getDireccion());
		copia.setTelefono(getTelefono());
		copia.setRut(getRut());
		copia.setNumeroSerie(getNumeroSerie());
		copia.setZona(getZona());
		copia.setSello(getSello().copiar());
		copia.setTiempoRespuesta(getTiempoRespuesta());
		copia.setFoto(getFoto());
		copia.setFotoChica(getFotoChica());
	}
	
	public void copiarColecciones(Estacion copia) {
		for (Orden o : this.getListaOrdenes()) {
			copia.getListaOrdenes().add(o.copiar());
		}
		for (Activo a : this.getListaDeActivos()) {
			copia.getListaDeActivos().add(a.copiar());
		}
		for (EmailEmpresa ee : getListaEmail()) {
			copia.getListaEmail().add(ee.copiar());
		}
	}
	
	public Estacion copiarTodoSinOrdenes() {
		Estacion copia = new Estacion();
		copiarPropiedades(copia);
		copiarListaDeActivos(copia);
		copiarEmail(copia);
		return copia;
	}
	
	private void copiarEmail(Estacion copia) {
		for (EmailEmpresa ee : getListaEmail()) {
			copia.getListaEmail().add(ee.copiar());
		}
	}

	public void copiarListaDeActivos(Estacion copia) {
		for (Activo a : getListaDeActivos()) {
			copia.getListaDeActivos().add(a.copiar());
		}
	}

	public Set<Activo> getListaDeActivos() {
		Set<Activo> activos = new HashSet<Activo>();
	/*	for (Activo a : listaDeActivos)
		{
			if (a.getTipo() == 1)
			{
				activos.add((Surtidor)a);
			}
			else if (a.getTipo() == 2)
			{
				activos.add((Tanque)a);
			}
			else if (a.getTipo() == 3 )
			{
				activos.add((Canio)a);
			}
			else if (a.getTipo() == 4)
			{
				activos.add((BombaSumergible)a);
			}
			return listaDeActivos;
		}*/
			
		return listaDeActivos;
	}

	public void setListaDeActivos(Set<Activo> listaDeActivos) {
		this.listaDeActivos = listaDeActivos;
	}
	
	public Foto getFoto() {
		return foto;
	}

	public void setFoto(Foto foto) {
		this.foto = foto;
	}

	public Foto getFotoChica() {
		return fotoChica;
	}

	public void setFotoChica(Foto fotoChica) {
		this.fotoChica = fotoChica;
	}

	public Sello getSello() {
		return sello;
	}

	public void setSello(Sello sello) {
		this.sello = sello;
	}

	public String getNumeroSerie() {
		return numeroSerie;
	}

	public void setNumeroSerie(String numeroSerie) {
		this.numeroSerie = numeroSerie;
	}

	public int getZona() {
		return zona;
	}

	public void setZona(int zona) {
		this.zona = zona;
	}

	public Set<EmailEmpresa> getListaEmail() {
		return listaEmail;
	}

	public void setListaEmail(Set<EmailEmpresa> listaEmail) {
		this.listaEmail = listaEmail;
	}

	public String toString(){
		return this.getNombre() + " - " + this.getNumeroSerie();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass()	.equals(Estacion.class))
		{
			Estacion estacion = (Estacion)obj;
			return estacion.getId()==getId();
		}
		return false;
	}

	public Estacion(){
		
	}

	public Estacion(String nroSerie, String nombre, Sello sello){
		this.setNombre(nombre);
		this.setSello(sello);
		this.setNumeroSerie(nroSerie);
	}

	public Estacion(int id) {
		setId(id);
	}

	public ArrayList<Surtidor> obtenerSurtidores(){
		ArrayList<Surtidor> retorno = new ArrayList<Surtidor>();
		Surtidor surtidor;
		for (Activo activo : this.getListaDeActivos()) {
			if(activo.getTipo() == 1){
				surtidor = (Surtidor) activo;
				retorno.add(surtidor);
			}
		}
		return retorno;
	}

	public ArrayList<Tanque> obtenerTanques(){
		ArrayList<Tanque> retorno = new ArrayList<Tanque>();
		Tanque tanque;
		for (Activo activo : this.getListaDeActivos()) {
			if(activo.getTipo() == 2){
				tanque = (Tanque) activo;
				retorno.add(tanque);
			}
		}
		return retorno;
	}

	public Activo buscarActivoDeEmpresa(int id){
		for(Activo a : this.getListaDeActivos()){
			if(a.getId()==id){
				return a;
			}
		}
		return null;
	}

	public boolean validarEstacion()
	{
		try{
			if (zona > 0 && 
					zona < 1000)
			{
				return true;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return false;
	}

	/*	
	public List<Orden> obtenerListaOrdenesActivas(){
		List<Orden> todasLasOrdenesActivas = this.getListaOrdenes();
		Orden ordenActiva;
		for (int i = 0; i < todasLasOrdenesActivas.size(); i++) {
			ordenActiva = (Orden) todasLasOrdenesActivas.get(i);
			if(ordenActiva.getEstadoOrden()==3){
				todasLasOrdenesActivas.add(ordenActiva);
			}
		}
		return todasLasOrdenesActivas;
	}

	public List<Orden> obtenerListaOrdenesInactivas(){
		List<Orden> todasLasOrdenesInactivas = this.getListaOrdenes();
		Orden ordenInactiva;
		for (int i = 0; i < todasLasOrdenesInactivas.size(); i++) {
			ordenInactiva = (Orden) todasLasOrdenesInactivas.get(i);
			if(ordenInactiva.getEstadoOrden()==3){
				todasLasOrdenesInactivas.add(ordenInactiva);
			}
		}
		return todasLasOrdenesInactivas;
	}
	 */
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public String getRut() {
		return rut;
	}

	public void setRut(String rut) {
		this.rut = rut;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public boolean isInactiva() {
		return inactiva;
	}
	
	public void setInactiva(boolean inactiva) {
		this.inactiva = inactiva;
	}

	public Set<Orden> getListaOrdenes() {
		return listaOrdenes;
	}

	public void setListaOrdenes(Set<Orden> listaOrdenes) {
		this.listaOrdenes = listaOrdenes;
	}


	public boolean asociarActivo(Estacion empresa, Activo activo){
		if(activo.getEmpresa()==null){
			activo.setEmpresa(empresa);
			return true;
		}
		return false;
	}
	
	public EstacionData getEstacionData(){
		EstacionData estacionData = new EstacionData();
		
		estacionData.setDepartamento(departamento);
		estacionData.setDireccion(direccion);
		estacionData.setId(id);
		estacionData.setLocalidad(localidad);
		estacionData.setNombre(nombre);
		estacionData.setNumeroSerie(numeroSerie);
		estacionData.setRut(rut);
		estacionData.setSello(sello.getNombre());
		estacionData.setTelefono(telefono);
		estacionData.setTiempoRespuesta(tiempoRespuesta + "");
		estacionData.setZona(zona + ""); 
		estacionData.setListaEmail(getEmailList());
		
		return estacionData;
	}
	
	public List<String> getEmailList(){
		List<String> retorno = new ArrayList<String>();
		for(EmailEmpresa e : getListaEmail()) {
			retorno.add(e.getEmail());
		}
		return retorno;
	}

	public ArrayList<EmailEmpresaData> getEmailsData() {
		ArrayList<EmailEmpresaData> retorno = new ArrayList<EmailEmpresaData>();
				
		for (EmailEmpresa emailEmpresa : listaEmail) {
			
			EmailEmpresaData email = new EmailEmpresaData();
			
			email.setEmail(emailEmpresa.getEmail());
			email.setId(emailEmpresa.getId());
			
			retorno.add(email);
		}		
		return retorno;
	}

	public EstacionDataList copiarEstacionAEstDataList() {
		EstacionDataList est = new EstacionDataList();
		if (est != null) {
			est.setId(getId());
			est.setNombre(getNombre());
			est.setNumeroSerie(getNumeroSerie());
			est.setTr(getTiempoRespuesta());
			if (getSello()!=null) est.setSello(getSello().getNombre());
		}
		return est;
	}

	public boolean tieneSurtidoresEnAbono() {
		for (Activo activo : listaDeActivos) {
			if (activo.getTipo() == 1 && activo.getEstado() == EstadoActivo.ABONO){
				return true;
			}
		}
		return false;
	}

}
