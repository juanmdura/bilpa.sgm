package app.client.dominio;

import java.util.Date;

import app.client.dominio.data.PendienteData;
import app.client.dominio.data.PendienteDataList;


public class Pendiente implements com.google.gwt.user.client.rpc.IsSerializable {
	
	private int id;
	private Foto foto;
	private EstadoPendiente estado;
	private String comentario;
	private boolean comentarioVisible = false;
	private Date plazo;
	private Date fechaCreado;
	private Date fechaModificado;
	private Organizacion organizacion;
	private Activo activo;
	private Date fechaReparado;
	
	private String motivoDescarte;
	private Persona descartador;
	private Date fechaDescarte;
	
	private Orden ordenCreado;
	private Orden ordenAsignada;
	private Orden ordenReparado;
	private Solucion solucion;
	
	private Preventivo preventivo;
	private Corregido corregido;
	private Persona creador;
	
	public Preventivo getPreventivo() {
		return preventivo;
	}

	public void setPreventivo(Preventivo preventivo) {
		this.preventivo = preventivo;
	}

	public Date getFechaReparado() {
		return fechaReparado;
	}

	public void setFechaReparado(Date fechaReparado) {
		this.fechaReparado = fechaReparado;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Solucion getSolucion() {
		return solucion;
	}

	public void setSolucion(Solucion solucion) {
		this.solucion = solucion;
	}

	public Orden getOrdenCreado() {
		return ordenCreado;
	}

	public void setOrdenCreado(Orden ordenCreado) {
		this.ordenCreado = ordenCreado;
	}

	public Orden getOrdenReparado() {
		return ordenReparado;
	}

	public void setOrdenReparado(Orden ordenReparado) {
		this.ordenReparado = ordenReparado;
	}
	
	public Orden getOrdenAsignada() {
		return ordenAsignada;
	}

	public void setOrdenAsignada(Orden ordenAsignada) {
		this.ordenAsignada = ordenAsignada;
	}

	public EstadoPendiente getEstado() {
		return estado;
	}

	public void setEstado(EstadoPendiente estado) {
		this.estado = estado;
	}

	public Organizacion getOrganizacion() {
		return organizacion;
	}

	public void setOrganizacion(Organizacion organizacion) {
		this.organizacion = organizacion;
	}

	public Foto getFoto() {
		return foto;
	}

	public void setFoto(Foto foto) {
		this.foto = foto;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public boolean isComentarioVisible() {
		return comentarioVisible;
	}

	public void setComentarioVisible(boolean comentarioVisible) {
		this.comentarioVisible = comentarioVisible;
	}	
	
	public Date getPlazo() {
		return plazo;
	}

	public void setPlazo(Date plazo) {
		this.plazo = plazo;
	}
	
	public Date getFechaCreado() {
		return fechaCreado;
	}

	public void setFechaCreado(Date fechaCreado) {
		this.fechaCreado = fechaCreado;
	}

	public Date getFechaModificado() {
		return fechaModificado;
	}

	public void setFechaModificado(Date fechaModificado) {
		this.fechaModificado = fechaModificado;
	}
	
	public Activo getActivo() {
		return activo;
	}

	public void setActivo(Activo activo) {
		this.activo = activo;
	}

	public String getMotivoDescarte() {
		return motivoDescarte;
	}

	public void setMotivoDescarte(String motivoDescarte) {
		this.motivoDescarte = motivoDescarte;
	}

	public Persona getDescartador() {
		return descartador;
	}

	public void setDescartador(Persona descartador) {
		this.descartador = descartador;
	}

	public Date getFechaDescarte() {
		return fechaDescarte;
	}

	public void setFechaDescarte(Date fechaDescarte) {
		this.fechaDescarte = fechaDescarte;
	}
	
	public Corregido getCorregido() {
		return corregido;
	}

	public void setCorregido(Corregido corregido) {
		this.corregido = corregido;
	}

	public Persona getCreador() {
		return creador;
	}

	public void setCreador(Persona creador) {
		this.creador = creador;
	}

	public Pendiente(){
		super();
	}

	public Pendiente(int id2) {
		setId(id2);
	}

	public PendienteData getPendienteData() {
		PendienteData pd = new PendienteData();
		pd.setId(this.getId());
		pd.setComentario(this.getComentario());
		pd.setComentarioVisible(this.isComentarioVisible());
		if (this.getFoto() != null) pd.setUrlFoto(this.getFoto().getUrl());
		if (this.getFoto() != null) pd.setPathFoto(this.getFoto().getPath());
		pd.setPlazo(this.getPlazo());
		pd.setDestinatario(getOrganizacion().getOrganizacion());
		pd.setIdDestinoDelCargo(Organizacion.getId(getOrganizacion()));
		
		pd.setEstado(this.getEstado());
		pd.setIdActivo(getActivo() != null ? getActivo().getId() : 0);
		pd.setTipoActivo(getActivo() != null ? getActivo().getTipo() : 0);
		
		pd.setFechaReparado(getFechaReparado());
		pd.setFechaCreado(getFechaCreado());
		pd.setIdEstacion(getActivo() != null ? getActivo().getEmpresa().getId() : 0);
		if (getActivo() != null){
			pd.setEmpresa(getActivo().getEmpresa().getNombre());
			pd.setActivo(getActivo().toString());
		}
		pd.setMotivoDescarte(this.getMotivoDescarte());
		if (getOrdenAsignada() != null) pd.setNumeroCorrectivoAsignado(getOrdenAsignada().getNumero());
		
		if (getOrdenCreado() != null){
			pd.setOrdenCreado(getOrdenCreado().getNumero());
		}
		
		if (getOrdenReparado() != null){
			pd.setOrdenReparado(getOrdenReparado().getNumero());
		}
		// pd.setFechaReparado(app.server.UtilFechas.getFormattedDate(getFechaReparado()));
		pd.setIdPreventivo(getPreventivo() != null ? this.getPreventivo().getId() : 0);
		if (getCreador() != null){
			pd.setIdCreador(getCreador().getId());
			pd.setCreador(getCreador().toString());
		}
		return pd;
	}

	public PendienteDataList getPendienteDataList() {
		
		PendienteDataList pd = new PendienteDataList();
		pd.setId(this.getId());
		pd.setComentario(this.getComentario());
		pd.setComentarioVisible(this.isComentarioVisible());
		pd.setPlazo(this.getPlazo());
		pd.setEstado(this.getEstado());
		if (this.getFoto() != null) pd.setUrlFoto(this.getFoto().getUrl());
		pd.setDestinatario(getOrganizacion().getOrganizacion());
		pd.setPlazoVencido(getPlazo() != null && getPlazo().before(new Date()));
		pd.setFechaReparado(getFechaReparado());
		return pd;
		
	}

	public void merge(PendienteData pd) {
		this.setComentario(pd.getComentario());
		this.setComentarioVisible(pd.isComentarioVisible());
		this.setPlazo(pd.getPlazo());
		this.setEstado(pd.getEstado());
		this.setMotivoDescarte(pd.getMotivoDescarte());
		this.setId(pd.getId());
		
		Date now = new Date();
		if (pd.getId() == 0){
			this.setFechaCreado(now);
		} else {
			this.setFechaModificado(now);
		}
		
		if (pd.getIdDestinoDelCargo() > 0){
			this.setOrganizacion(Organizacion.getById(pd.getIdDestinoDelCargo()));
		} else {
			this.setOrganizacion(Organizacion.valueOf(pd.getDestinatario()));
		}
		setFechaReparado(pd.getFechaReparado());
	}
}
