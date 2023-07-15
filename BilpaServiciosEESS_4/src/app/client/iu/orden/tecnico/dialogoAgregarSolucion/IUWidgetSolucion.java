package app.client.iu.orden.tecnico.dialogoAgregarSolucion;

import java.util.HashSet;
import java.util.Set;

import app.client.dominio.Persona;
import app.client.dominio.Reparacion;
import app.client.dominio.ReparacionSurtidor;
import app.client.dominio.Repuesto;
import app.client.dominio.RepuestoLinea;
import app.client.dominio.Solucion;
import app.client.dominio.Surtidor;
import app.client.dominio.data.RepuestoData;
import app.client.iu.orden.tecnico.IUSeguimientoTecnico;
import app.client.iu.orden.tecnico.dialogoAgregarSolucion.tab.IUWidgetFallaTab;
import app.client.iu.orden.tecnico.dialogoAgregarSolucion.tab.IUWidgetRepuestoTab;
import app.client.iu.orden.tecnico.dialogoAgregarSolucion.tab.IUWidgetTareaTab;
import app.client.iu.orden.tecnico.dialogoAgregarSolucion.tab.IUWidgetVariosTab;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.Constants;
import app.client.utilidades.Constants.OPERACION_GARANTIA;
import app.client.utilidades.UtilOrden;
import app.client.utilidades.utilObjects.GarantiaUtil;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class IUWidgetSolucion {

	protected DialogBox dialogo = new DialogBox(false);
	
	private HorizontalPanel pHCentral = new HorizontalPanel();
	private HorizontalPanel pHInferior = new HorizontalPanel();

	private VerticalPanel pVFalla1 = new VerticalPanel();
	private VerticalPanel pVFalla2 = new VerticalPanel();

	private VerticalPanel pVTarea1 = new VerticalPanel();
	private VerticalPanel pVTarea2 = new VerticalPanel();		

	private VerticalPanel pVRepuesto1 = new VerticalPanel();
	private VerticalPanel pVRepuesto2 = new VerticalPanel();
	
	private VerticalPanel pVVarios1 = new VerticalPanel();
	private VerticalPanel pVVarios2 = new VerticalPanel();

	private VerticalPanel pVBig = new VerticalPanel();

	private Button btnAceptar =  new Button("Aceptar");
	private Button btnAnteriorOA = new Button("Anterior");
	private Button btnAnterior = new Button("Anterior");
	private Button btnCancelar =  new Button("Cancelar");

	private FlexTable tableSelectedData = new FlexTable(); 
	private Label lblActivo = new Label("Activo");
	private Label lblActivoData = new Label();
	private Label lblFalla = new Label("Falla");
	public Label lblFallaData = new Label();
	private Label lblTarea = new Label("Tarea");
	public Label lblTareaData = new Label();
	private Label lblRepuesto = new Label("Repuestos");
	public Label lblRepuestosData = new Label();
	
	private DecoratedTabPanel tabPSolucion = new DecoratedTabPanel();
	
	protected IUWidgetTareaTab iuWidgetTareas;
	protected IUWidgetFallaTab iuWidgetFallas;
	protected IUWidgetRepuestoTab iuWidgetRepuestos;
	protected IUWidgetVariosTab iuWidgetVarios;

	//Dialogo eliminar
	//========================================================	
	private DialogBox dialogBoxCR = new DialogBox();
	private Label lblTituloDialCR = new Label("No ha seleccionado ningún repuesto, desea continuar?");
	private HTML htmlDescDialCR = new HTML();
	private VerticalPanel vpDialCR = new VerticalPanel();
	private HorizontalPanel hpDialCR = new HorizontalPanel();
	private HorizontalPanel hp2DialCR = new HorizontalPanel();

	private Button btnSiDialCR = new Button("Si",new ClickHandler() {
		public void onClick(ClickEvent event) {
			dialogBoxCR.hide(true);
			agregarSolucion();
		}
	});

	private Button btnNoDialCR = new Button("No",new ClickHandler() {
		public void onClick(ClickEvent event) {
			dialogBoxCR.hide(true);
		}
	});
	//========================================================

	Reparacion rep;
	
	PopupCargando popUp;
	GarantiaUtil garantiaUtil;
	IUSeguimientoTecnico iuSegTec;
	protected GlassPopup glass;
	Constants.OPERACION_WIDGET_SOLUCION vieneDe;
	
	protected Persona sesion;
	
	public DialogBox getDialogo() {
		return dialogo;
	}

	public IUWidgetSolucion(Reparacion reparacion, PopupCargando popUpu, GlassPopup glass, IUSeguimientoTecnico iuSegTec, Constants.OPERACION_WIDGET_SOLUCION vieneDe, Persona persona) {
		this.rep = reparacion;
		this.popUp = popUpu;
		this.iuSegTec = iuSegTec;
		this.glass = glass;
		this.vieneDe = vieneDe;
		this.sesion = persona;

		glass.show();

		setear();
		eventos();
		cargarBotonesNormal();
	}

	public void setSelectedData() {
		lblActivo.setStyleName("Negrita");
		lblFalla.setStyleName("Negrita");
		lblTarea.setStyleName("Negrita");
		lblRepuesto.setStyleName("Negrita");
		
		tableSelectedData.setWidget(0, 0, lblActivo);
		tableSelectedData.setWidget(0, 1, lblActivoData);
		tableSelectedData.setWidget(1, 0, lblFalla);
		tableSelectedData.setWidget(1, 1, lblFallaData);
		tableSelectedData.setWidget(2, 0, lblTarea);
		tableSelectedData.setWidget(2, 1, lblTareaData);
		tableSelectedData.setWidget(3, 0, lblRepuesto);
		tableSelectedData.setWidget(3, 1, lblRepuestosData);
		
		lblActivoData.setText(UtilOrden.tiposDeActivos(rep.getActivo()) + " " + rep.getActivo().toString());
		lblFallaData.setText(iuWidgetFallas.getFallaSeleccionada() != null ? iuWidgetFallas.getFallaSeleccionada().getDescripcion(): "");
		lblTareaData.setText(iuWidgetTareas.getTareaSeleccionada() != null ? iuWidgetTareas.getTareaSeleccionada().getDescripcion(): "");
		
		Set<RepuestoData> repuestosSeleccionados = iuWidgetRepuestos.getRepuestosSeleccionados();
		int i = 0;
		for (RepuestoData repuestoData : repuestosSeleccionados) {
			if (i > 0){
				lblRepuestosData.getText().concat(", ");
			}
			lblRepuestosData.getText().concat(repuestoData.getDescripcion());
			i++;
		}
		
	}

	private void agregarWidgets() {
		FlexTable tableGarantiaActivos = new FlexTable();
		garantiaUtil = new GarantiaUtil(rep.getActivo(), tableGarantiaActivos, 0, 0, OPERACION_GARANTIA.PANTALLA_ORDEN);
		
		pVBig.setSpacing(5);
		pHCentral.setSpacing(5);
		pHInferior.setSpacing(5);

		pVBig.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		pVFalla2.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);

		pVFalla1.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		pVFalla1.add(pVFalla2);
		pVFalla2.add(iuWidgetFallas.getPrincipalPanel());

		pVTarea1.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		pVTarea1.add(pVTarea2);
		pVTarea2.add(iuWidgetTareas.getPrincipalPanel());

		pVRepuesto1.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		pVRepuesto1.add(pVRepuesto2);
		pVRepuesto2.add(iuWidgetRepuestos.getPrincipalPanel());
		
		pVVarios1.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		pVVarios1.add(pVVarios2);
		pVVarios2.add(iuWidgetVarios.getPrincipalPanel());

		HorizontalPanel hpSpace = new HorizontalPanel();
		hpSpace.setHeight("30px");		

		dialogo.setAutoHideEnabled(false);

		pVBig.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		pVBig.add(tableGarantiaActivos);
		pVBig.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		pVBig.add(tabPSolucion);

		hpSpace.setWidth("10px");

		tabPSolucion.add(pVFalla1, "Falla");
		tabPSolucion.add(pVTarea1, "Tarea");
		tabPSolucion.add(pVRepuesto1, "Repuestos");
		tabPSolucion.add(pVVarios1, "Varios");
		tabPSolucion.selectTab(0);
		
		pVBig.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		pVBig.add(tableSelectedData);
		pVBig.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		pVBig.add(pHInferior);

		dialogo.add(pVBig);
	}

	public void initWidgets() {
		this.iuWidgetTareas = new IUWidgetTareaTab(popUp, this);
		this.iuWidgetFallas = new IUWidgetFallaTab(popUp, this);
		this.iuWidgetRepuestos = new IUWidgetRepuestoTab(popUp, this);
		this.iuWidgetVarios = initWidgetVarios();
		
		setSelectedData();
		agregarWidgets();
	}

	protected IUWidgetVariosTab initWidgetVarios() {
		return new IUWidgetVariosTab(popUp, rep, sesion, null);
	}

	private void cargarBotonesNormal() {
		pVVarios1.setVisible(false);
		pVRepuesto1.setVisible(false);
		pVTarea1.setVisible(true);
		pVFalla1.setVisible(true);

		pHInferior.clear();
		pHInferior.add(btnCancelar);

		if(vieneDe == Constants.OPERACION_WIDGET_SOLUCION.VIENE_DE_OTRO_ACTIVO){
			pHInferior.add(btnAnteriorOA);
		}

		//pHInferior.add(btnSiguiente);
		pHInferior.add(btnAceptar);
	}

	private void setear() {
		dialogo.setWidth("100%");

		btnAceptar.setWidth("100px");
		btnCancelar.setWidth("100px");
		btnAnteriorOA.setWidth("100px");
		btnAnterior.setWidth("100px");
		//btnSiguiente.setWidth("100px");
	}

	private void eventos() {
		
		btnAceptar.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				validarSolucion();
			}
		});

		btnCancelar.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				dialogo.hide();
				glass.hide();
			}
		});

		btnAnterior.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				cargarBotonesNormal();
			}
		});

		/*btnSiguiente.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				siguiente();
			}
		});*/

		btnAnteriorOA.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				anteriorOA();
			}
		});
	}

	private void siguiente(){
				
		pVRepuesto1.setVisible(true);
		pVVarios1.setVisible(true);
		pVTarea1.setVisible(false);
		pVFalla1.setVisible(false);

		pHInferior.clear();

		pHInferior.add(btnCancelar);
		pHInferior.add(btnAnterior);
		pHInferior.add(btnAceptar);
		
	}

	private void anteriorOA(){
		IUWidgetSolucionOA widgetSolucionOA = new IUWidgetSolucionOA(popUp, glass, iuSegTec, rep.getActivo(), true, sesion);

		DialogBox dial = widgetSolucionOA.agregarOtroActivo();
		glass.show();
		dial.show(); 
		dial.setPopupPosition(iuSegTec.margenHorizontalOA, iuSegTec.margenVerticalOA);	

		this.dialogo.hide();
	}			

	protected void validarSolucion() 
	{
		if(iuWidgetFallas.getFallaSeleccionada() != null){
			if(iuWidgetTareas.getTareaSeleccionada() != null){
				if(iuWidgetRepuestos.getRepuestosSeleccionados().size() > 0){
					agregarSolucion();
				}else{
					DialogBox db = dialConfirmarRepuestos();
					db.center();
					
					db.show();
				}
			}else{
				ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe seleccionar una tarea");
				vpu.showPopUp();
			}
		}else{
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe seleccionar una falla");
			vpu.showPopUp();
		}
	}

	protected void agregarSolucion() {
		Set<RepuestoLinea> repuestos = cargarRepuestosLinea();

		for(RepuestoLinea rl : repuestos){
			if(!validarRepuestoEnOrden(rl.getRepuesto())){
				return;
			}
		}

		Solucion sol = agregarSolucion(repuestos);
		
		if (sol == null){
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Ya ha agregado esa reparación");
			vpu.showPopUp();
			
		} else {
			glass.hide();
			iuSegTec.cargarSoluciones();

			dialogo.hide();
			dialogo.clear();
		}
	}

	protected Solucion agregarSolucion(Set<RepuestoLinea> repuestos) {
		Solucion sol;
		if (rep.getActivo().getTipo() == 1){
			Surtidor s = (Surtidor) rep.getActivo();
			ReparacionSurtidor repSurt = (ReparacionSurtidor) rep;

			sol = repSurt.agregarSolucion(
					iuSegTec.orden, iuSegTec.orden.getSoluciones(), 
					iuWidgetTareas.getTareaSeleccionada(), 
					iuWidgetFallas.getFallaSeleccionada(), 
					repuestos, 
					iuWidgetVarios.getTelefonicaChecked(), 
					iuWidgetVarios.getPicoSeleccionado(s), 
					iuWidgetVarios.getDestinoDelCargoSeleccionado(), 
					iuWidgetVarios.getComentario());						
		} else {
			sol = rep.agregarSolucion(
					iuSegTec.orden, iuSegTec.orden.getSoluciones(), 
					iuWidgetTareas.getTareaSeleccionada(), 
					iuWidgetFallas.getFallaSeleccionada(), 
					repuestos, 
					iuWidgetVarios.getTelefonicaChecked(), 
					null, 
					iuWidgetVarios.getDestinoDelCargoSeleccionado(), 
					iuWidgetVarios.getComentario());								
		}

		return sol;
	}

	protected boolean validarRepuestoEnOrden(Repuesto repuesto) {
		for(RepuestoLinea repLin: iuSegTec.orden.getRepuestosLineas()){
			if(repLin.getRepuesto().getId() == repuesto.getId() && rep.getActivo().getId() == repLin.getActivo().getId()){
				
				ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Ya selecciono ese repuesto para ese activo");
				vpu.showPopUp();
				return false;
			}
		}
		return true;
	}

	protected Set<RepuestoLinea> cargarRepuestosLinea() {
		Set<RepuestoLinea> repuestos = new HashSet<RepuestoLinea>();
		for(RepuestoData r : iuWidgetRepuestos.getRepuestosSeleccionados()){
			RepuestoLinea rl = new RepuestoLinea();
			rl.setActivo(rep.getActivo());
			rl.setOrden(iuSegTec.orden);
			rl.setRepuesto(r.copiarRepuesto());
			rl.setCantidad(r.getCantidad() == 0 ? 1 : r.getCantidad());//TODO ESTA RARO, CUANDO ES 1 VIENE EN 0, SINO VIENE BIEN
			rl.setNuevo(r.isEsNuevo());
			repuestos.add(rl);
		}

		return repuestos;
	}

	public DialogBox dialConfirmarRepuestos()
	{
		vpDialCR.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hpDialCR.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		vpDialCR.add(lblTituloDialCR);
		dialogBoxCR.setAutoHideEnabled(true);

		btnSiDialCR.setWidth("100px");
		btnNoDialCR.setWidth("100px");

		htmlDescDialCR.setWidth("500px");
		//htmlDescDialCR.setText(texto);

		hpDialCR.setSpacing(5);
		hpDialCR.add(htmlDescDialCR);

		hp2DialCR.add(btnNoDialCR);
		hp2DialCR.setSpacing(5);		
		hp2DialCR.add(btnSiDialCR);

		vpDialCR.add(hpDialCR);
		vpDialCR.add(hp2DialCR);

		dialogBoxCR.setWidget(vpDialCR);
		return dialogBoxCR;
	}
	
}
