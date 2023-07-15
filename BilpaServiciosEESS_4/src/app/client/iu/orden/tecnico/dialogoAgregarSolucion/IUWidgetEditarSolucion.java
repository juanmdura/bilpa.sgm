package app.client.iu.orden.tecnico.dialogoAgregarSolucion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import app.client.dominio.Comentario;
import app.client.dominio.Contador;
import app.client.dominio.Persona;
import app.client.dominio.Reparacion;
import app.client.dominio.ReparacionSurtidor;
import app.client.dominio.RepuestoLinea;
import app.client.dominio.Solucion;
import app.client.dominio.data.DestinoDelCargoData;
import app.client.iu.orden.tecnico.IUSeguimientoTecnico;
import app.client.iu.orden.tecnico.dialogoAgregarSolucion.tab.IUWidgetVariosTab;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.Constants.OPERACION_WIDGET_SOLUCION;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;

public class IUWidgetEditarSolucion extends IUWidgetSolucion {

	private Solucion solucionARemover;
	private Comentario comentarioARemover;
	private Contador contador;

	public IUWidgetEditarSolucion(Reparacion reparacion, PopupCargando popUpu,
			GlassPopup glass, IUSeguimientoTecnico iuSegTec,
			OPERACION_WIDGET_SOLUCION vieneDe, Persona persona, Solucion solucion) {
		super(reparacion, popUpu, glass, iuSegTec, vieneDe, persona);
		this.solucionARemover = solucion;

		initWidgets();
		setearSolucionEnPantalla();
	}

	protected IUWidgetVariosTab initWidgetVarios() {
		return new IUWidgetVariosTab(popUp, rep, sesion, solucionARemover);
	}

	private void setearSolucionEnPantalla() {
		//Falla
		iuWidgetFallas.cellTableFallas.getSelectionModel().setSelected(solucionARemover.getFallaTecnica(), true);

		//Tarea
		iuWidgetTareas.cellTableTareas.getSelectionModel().setSelected(solucionARemover.getTarea(), true);

		//Repuestos
		for (RepuestoLinea rl : iuSegTec.orden.getRepuestosLineas()) {
			if(rl.getSolucion().getId() == solucionARemover.getId()){
				iuWidgetRepuestos.cellTableRepuestos.getSelectionModel().setSelected(rl.getRepuesto().copiarRepuestoData(), true);
			}
		}

		//Pico
		String textoPico;
		if(rep.getClass().equals(ReparacionSurtidor.class))
		{
			ReparacionSurtidor rs = (ReparacionSurtidor)rep;
			contador = rs.buscarContador(solucionARemover);

			if (contador != null && contador.getPico() != null){
				textoPico = contador.getPico().getNumeroPico()+"";
			}
			else
			{
				textoPico = "N/A";
			}

			iuWidgetVarios.listBoxPicosDialAgregarSolucion.setItemSelectedByText(textoPico);
		}

		//Comentario
		if(solucionARemover.getComentario() != null){
			iuWidgetVarios.dialogoComentario.textArea.setText(solucionARemover.getComentario().getTexto());
			iuWidgetVarios.dialogoComentario.checkBoxImp.setValue(solucionARemover.getComentario().isImprimible());
			comentarioARemover = solucionARemover.getComentario().copiar();
		}
		//Destino del cargo
		iuWidgetVarios.setDestinoDelCargoSeleccionado(new DestinoDelCargoData(solucionARemover.getDestinoDelCargo().getId(), solucionARemover.getDestinoDelCargo().getNombre()));
		//Telefonica
		iuWidgetVarios.checkboxTelefonica.setValue(solucionARemover.isTelefonica());
	}

	protected void agregarSolucion() {
		Set<RepuestoLinea> repuestosLineaDelDialogo = cargarRepuestosLinea();

		for(RepuestoLinea rl : repuestosLineaDelDialogo){
			if(!validarRepuestoEnOrden(rl)){
				return;
			}
		}

		//repuestos
		//actualizo los repuestos que existian y se modificaron
		for(RepuestoLinea rl : repuestosLineaDelDialogo){
			RepuestoLinea rlQueYaExiste = buscarRepuestoLinea(iuSegTec.orden.getRepuestosLineas(), rl);
			if(rlQueYaExiste != null){
				rlQueYaExiste.setCantidad(rl.getCantidad());
				rlQueYaExiste.setNuevo(rl.isNuevo());
			}else{//o agrego uno nuevo
				iuSegTec.orden.getRepuestosLineas().add(rl);
				rl.setSolucion(solucionARemover);
			}
		}

		//quito los repuestos que se quitaron
		ArrayList<RepuestoLinea> repuestosAEliminar = new ArrayList<RepuestoLinea>();
		for (RepuestoLinea rl : iuSegTec.orden.getRepuestosLineas()) {
			RepuestoLinea rlQueYaExiste = buscarRepuestoLinea2(repuestosLineaDelDialogo, rl);
			if(rlQueYaExiste == null){
				repuestosAEliminar.add(rl);
				rl.setSolucion(null);
			}
		}
		iuSegTec.orden.getRepuestosLineas().removeAll(repuestosAEliminar);

		//solucion
		rep.getSoluciones().remove(solucionARemover);

		//reparacion
		if(rep.getSoluciones().size() == 0){
			iuSegTec.orden.getReparaciones().remove(rep);
		}

		//contadores
		ArrayList<Contador> contadoresARemover = new ArrayList<Contador>();
		if(rep.getTipo() == 1){
			ReparacionSurtidor repSurt = (ReparacionSurtidor)rep; 
			for(Contador contador : repSurt.getContadores()){
				if(contador.getSolucion().equals(solucionARemover)){
					contadoresARemover.add(contador);
					contador.setSolucion(null);
				}
			}
			repSurt.getContadores().removeAll(contadoresARemover);
		}

		//AGREGAR NUEVA SOLUCION
		Solucion nuevaSolucion = agregarSolucion(new HashSet<RepuestoLinea>());//los repuestos se pasan vacios ya que se manejaron en editarRepuestos

		//comentarios
		if(comentarioARemover != null){
			iuSegTec.orden.removerComentario(comentarioARemover);
			solucionARemover.setComentario(null);
		}

		//se setea la solucion de los nuevos repuestos
		for (RepuestoLinea rl : repuestosLineaDelDialogo) {
			rl.setSolucion(nuevaSolucion);
		}

		//se corrige la solucion de los repuestos de la orden
		for(RepuestoLinea rl : iuSegTec.orden.getRepuestosLineas()){
			if(rl.getSolucion().equals(solucionARemover)){
				rl.setSolucion(nuevaSolucion);
			}
		}

		//contadores
		if(rep.getTipo() == 1 && this.contador!= null){
			ReparacionSurtidor repSurt = (ReparacionSurtidor)rep; 
			for(Contador contador : repSurt.getContadores()){
				if(contador.getSolucion().equals(nuevaSolucion)){
					contador.setInicio(this.contador.getInicio());
					contador.setFin(this.contador.getFin());
				}
			}
		}

		if (nuevaSolucion == null){
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Ya ha agregado esa reparación");
			vpu.showPopUp();

		} else {
			glass.hide();
			iuSegTec.cargarSoluciones();

			dialogo.hide();
			dialogo.clear();
		}
	}

	private RepuestoLinea buscarRepuestoLinea(Set<RepuestoLinea> repuestosLineaOrigen, RepuestoLinea rl) {
		for (RepuestoLinea repuestoLineaOrigen : repuestosLineaOrigen) {
			if(repuestoLineaOrigen.getSolucion().equals(solucionARemover) && rl.getRepuesto().getId() == repuestoLineaOrigen.getRepuesto().getId() ){
				return repuestoLineaOrigen;
			}
		}
		return null;
	}

	private RepuestoLinea buscarRepuestoLinea2(Set<RepuestoLinea> repuestosLineaOrigen, RepuestoLinea rl) {
		for (RepuestoLinea repuestoLineaOrigen : repuestosLineaOrigen) {
			if(rl.getSolucion().equals(solucionARemover) && rl.getRepuesto().getId() == repuestoLineaOrigen.getRepuesto().getId() ){
				return repuestoLineaOrigen;
			}
		}
		return null;
	}

	protected boolean validarRepuestoEnOrden(RepuestoLinea repuestoLineaAAgregar) {
		for(RepuestoLinea repuestoLineaDeOrden: iuSegTec.orden.getRepuestosLineas()){
			if(repuestoLineaDeOrden.getRepuesto().getId() == repuestoLineaAAgregar.getRepuesto().getId() && rep.getActivo().getId() == repuestoLineaDeOrden.getActivo().getId() 
					&& !solucionARemover.equals(repuestoLineaDeOrden.getSolucion())){
				
				ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Ya selecciono ese repuesto para ese activo");
				vpu.showPopUp();
				return false;
			}
		}
		return true;
	}
}
