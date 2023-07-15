package app.client.dominio;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import app.client.dominio.data.ComentarioData;
import app.client.dominio.data.CorregidoData;
import app.client.dominio.data.CorregidoDataSurtidor;
import app.client.dominio.data.DestinoDelCargoData;
import app.client.dominio.data.RepuestoLineaCorregidoData;

public abstract class Corregido implements com.google.gwt.user.client.rpc.IsSerializable {

	private int id;

	private Preventivo preventivo;

	private FallaTecnica falla;

	private Tarea tarea;

	private DestinoDelCargo destinoDelCargo;

	private Set<RepuestoLineaCorregido> listaDeRepuestos = new HashSet<RepuestoLineaCorregido>();

	private String comentario;

	private boolean comentarioVisible;
	
	private Foto foto;
	private Foto foto2;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Preventivo getPreventivo() {
		return preventivo;
	}

	public void setPreventivo(Preventivo preventivo) {
		this.preventivo = preventivo;
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

	public DestinoDelCargo getDestinoDelCargo() {
		return destinoDelCargo;
	}

	public void setDestinoDelCargo(DestinoDelCargo destinoDelCargo) {
		this.destinoDelCargo = destinoDelCargo;
	}

	public Set<RepuestoLineaCorregido> getListaDeRepuestos() {
		return listaDeRepuestos;
	}

	public void setListaDeRepuestos(Set<RepuestoLineaCorregido> listaDeRepuestos) {
		this.listaDeRepuestos = listaDeRepuestos;
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

	
	public Corregido(){

	}

	
	public static Corregido getNewInstance(int tipo) {
		Corregido corregido = null;
		if(tipo == 1){
			corregido = new CorregidoSurtidor();

		} else if (tipo == 2){
			corregido = new CorregidoTanque();

		} else if (tipo == 4){
			corregido = new CorregidoBomba();
			
		} else if (tipo == 6){
			corregido = new CorregidoActivoGenerico();
			
		}
		return corregido;
	}

	public int getTipo() {
		if(getClass().equals(CorregidoSurtidor.class)){
			return 1;

		} else if(getClass().equals(CorregidoTanque.class)){
			return 2;

		} else if(getClass().equals(CorregidoBomba.class)){
			return 4;

		} else if(getClass().equals(CorregidoActivoGenerico.class)){
			return 6;

		}
		return 0;
	}

	public void merge(Corregido corregidoParam) {
		setComentario(corregidoParam.getComentario());
		setComentarioVisible(corregidoParam.isComentarioVisible());
		setDestinoDelCargo(corregidoParam.getDestinoDelCargo());;
		setFalla(corregidoParam.getFalla());
		setTarea(corregidoParam.getTarea());
		setRepuestos(corregidoParam);
		setPreventivo(corregidoParam.getPreventivo());
		setFoto(corregidoParam.getFoto());
		setFoto2(corregidoParam.getFoto2());
	}

	private void setRepuestos(Corregido corregidoParam) {
		
		this.setListaDeRepuestos(corregidoParam.getListaDeRepuestos());
		/*//removidos
		List<RepuestoLineaCorregido> repuestosRemovidos = new ArrayList<RepuestoLineaCorregido>();
		for (RepuestoLineaCorregido rlc : getListaDeRepuestos()){
			boolean vienDelCliente = false;
			for (RepuestoLineaCorregido rlcParam : corregidoParam.getListaDeRepuestos()){
				if (rlc.getId() == rlcParam.getId()){
					vienDelCliente = true;
				}	
			}
			if (!vienDelCliente){
				repuestosRemovidos.add(rlc);
			}
		}
		getListaDeRepuestos().removeAll(repuestosRemovidos);

		//modificados
		for (RepuestoLineaCorregido rlcParam : corregidoParam.getListaDeRepuestos()){
			boolean esNuevo = true;
			for (RepuestoLineaCorregido rlc : getListaDeRepuestos()){
				if (rlc.getId() == rlcParam.getId()){
					esNuevo = false;
					rlc.setCantidad(rlcParam.getCantidad());
					rlc.setNuevo(rlcParam.isNuevo());
					rlc.setRepuesto(rlcParam.getRepuesto());
				}	
			}

			if (esNuevo){
				getListaDeRepuestos().add(rlcParam);
			}
		}*/
	}

	public CorregidoData getData() {
		
		CorregidoData corregidoData = new CorregidoData();
		if (getPreventivo().getActivo().getTipo() == 1){
			CorregidoDataSurtidor corregidoDataSurtidor = new CorregidoDataSurtidor();
			CorregidoSurtidor corregido = (CorregidoSurtidor)this;
			
			if (corregido.getPico() != null){
				corregidoDataSurtidor.setPico(corregido.getPico().getPicoData());
			}
				
			corregidoData = corregidoDataSurtidor;
		}
		corregidoData.setId(getId());
		corregidoData.setFalla(getFalla().copiar());
		corregidoData.setTarea(getTarea().copiar());
		corregidoData.setIdPreventivo(getPreventivo().getId());
		
		ComentarioData comentarioData = new ComentarioData();
		comentarioData.setTexto(getComentario());
		comentarioData.setVisible(isComentarioVisible());
		corregidoData.setComentario(comentarioData);
		
		DestinoDelCargoData destinoDelCargoData = new DestinoDelCargoData();
		destinoDelCargoData.setId(getDestinoDelCargo().getId());
		destinoDelCargoData.setNombre(getDestinoDelCargo().getNombre());
		corregidoData.setDestinoDelCargo(destinoDelCargoData);
		
		if (getFoto()!= null) corregidoData.setUrlFoto(getFoto().getUrl());
		if (getFoto2()!= null) corregidoData.setUrlFoto2(getFoto2().getUrl());
		
		for (RepuestoLineaCorregido rlc : getListaDeRepuestos()){
			corregidoData.getListaDeRepuestos().add(new RepuestoLineaCorregidoData(rlc.getId(), rlc.getRepuesto().getId(), rlc.isNuevo(), rlc.getCantidad(), rlc.getRepuesto().getDescripcion()));
		}
		return corregidoData;
	}

	public Set<RepuestoLinea> getListaDeRepuestosLinea(Orden orden, Solucion solucion) {
		Set<RepuestoLinea> rls = new HashSet<RepuestoLinea>();
		
		for(RepuestoLineaCorregido rlc : getListaDeRepuestos()){
			RepuestoLinea rl = new RepuestoLinea();
			rl.setActivo(getPreventivo().getActivo());
			rl.setCantidad(rlc.getCantidad());
			rl.setNuevo(rlc.isNuevo());
			rl.setOrden(orden);
			rl.setRepuesto(rlc.getRepuesto());
			rl.setSolucion(solucion);
			rls.add(rl);
		}
		
		return rls;
	}

	public Comentario getComentarioObj(Orden orden, Persona usuario, Date fecha) {
		Comentario comentario = new Comentario();
		comentario.setTexto(getComentario());
		comentario.setImprimible(isComentarioVisible());
		comentario.setOrden(orden);
		comentario.setUsuario(usuario);
		comentario.setFecha(fecha);
		return comentario;
	}

}
