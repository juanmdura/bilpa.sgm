package app.client.iu.orden.tecnico.dialogoAgregarSolucion;

import java.util.ArrayList;

import app.client.ProyectoBilpa;
import app.client.dominio.Activo;
import app.client.dominio.ActivoGenerico;
import app.client.dominio.BombaSumergible;
import app.client.dominio.Canio;
import app.client.dominio.Persona;
import app.client.dominio.Reparacion;
import app.client.dominio.Surtidor;
import app.client.dominio.Tanque;
import app.client.iu.falla.IUWidgetFalla;
import app.client.iu.orden.tecnico.IUSeguimientoTecnico;
import app.client.iu.tarea.IUWidgetTarea;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.Constants;
import app.client.utilidades.Constants.OPERACION_GARANTIA;
import app.client.utilidades.UtilOrden;
import app.client.utilidades.utilObjects.GarantiaUtil;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class IUWidgetSolucionOA {

	VerticalPanel panelVDatosFallaActivo_5C_2 = new VerticalPanel();
	VerticalPanel vpRetPanelActivo = new VerticalPanel();
	VerticalPanel vpDialOtro = new VerticalPanel();
	VerticalPanel panelVFechaInFinDura = new VerticalPanel();	
	VerticalPanel panelSuperiorActivoSeleccionado = new VerticalPanel();
	VerticalPanel panelInferiorDatosActivos = new VerticalPanel();
	VerticalPanel panelHActivoYDatosActivo_2C = new VerticalPanel();
	VerticalPanel panelVDatosActivo_3C = new VerticalPanel();
	VerticalPanel panelVActivo_TipoActivo_4C = new VerticalPanel();
	VerticalPanel panelVActivo_ListaActivo_4C = new VerticalPanel();
	private HorizontalPanel panelHActivo_3C = new HorizontalPanel();

	DialogBox dialogOtro = new DialogBox();

	Button btnAceptarOA = new Button("Siguiente");
	Button btnCancelarOA = new Button("Cancelar");

	ListBox listBoxTipoActivo = new ListBox();
	ListBox listBoxListaDeActivos = new ListBox();

	Label lblNumeroDeSerie = new Label("Serie");
	Label lblModeloSurtidor = new Label("Modelo Surtidor");
	Label lblTipoActivo = new Label("Tipo de Activo");
	Label lblListaActivo = new Label("Lista de Activos");
	Label lblidT = new Label("Id");
	Label lblDescT = new Label("Descripción");
	Label lblidC = new Label("Id");
	Label lblDescC = new Label("Descripción");
	Label lblidB = new Label("Id");
	Label lblDescB = new Label("Descripción");
	Label lblCapT = new Label("Capacidad");
	Label lblTipoCombustibleT = new Label("Tipo de combustible");

	Label lblTituloReportarFallaYTarea = new Label("Reparación para otro activo");

	FlexTable tableDatosSurtidor = new FlexTable();
	FlexTable tableDatosTanque = new FlexTable();
	FlexTable tableDatosActivoGenerico = new FlexTable();
	FlexTable tableDatosBomba = new FlexTable();

	IUWidgetTarea iuWidgetTareasOA = new IUWidgetTarea();
	IUWidgetFalla iuWidgetFallasOA = new IUWidgetFalla();

	IUSeguimientoTecnico iuSegTec;
	GarantiaUtil garantiaUtil;
	Activo activoSeleccionado;
	PopupCargando popUp;
	GlassPopup glass;
	int tipoActivo;
	boolean vieneDeBotonAnteriorDelWidgetSolucion;
	private Persona sesion;

	public IUWidgetSolucionOA(PopupCargando popUpu, GlassPopup glass, final IUSeguimientoTecnico iuSegTec, Activo activo, boolean vieneDeWS, Persona persona) {
		this.popUp = popUpu;
		this.iuSegTec = iuSegTec;
		this.glass = glass;
		this.activoSeleccionado = activo;
		this.vieneDeBotonAnteriorDelWidgetSolucion = vieneDeWS;
		this.sesion = persona;

		cargarTipoActivo();
		cargarDatosActivos();
		
		btnAceptarOA.setWidth("100px");		
		btnCancelarOA.setWidth("100px");
		iuWidgetFallasOA.listBoxFallasT.setWidth("250px");
		iuWidgetFallasOA.listBoxTiposFallasT.setWidth("250px");
		iuWidgetTareasOA.listBoxTareas.setWidth("250px");
		iuWidgetTareasOA.listBoxTiposTareas.setWidth("250px");

		tableDatosSurtidor.setHeight("135");
		tableDatosSurtidor.setCellPadding(20);
		tableDatosSurtidor.setCellSpacing(15);

		tableDatosTanque.setHeight("135");
		tableDatosTanque.setCellPadding(8);
		tableDatosTanque.setCellSpacing(10);

		tableDatosActivoGenerico.setHeight("135");
		tableDatosActivoGenerico.setCellPadding(20);
		tableDatosActivoGenerico.setCellSpacing(15);

		tableDatosBomba.setHeight("135");
		tableDatosBomba.setCellPadding(20);
		tableDatosBomba.setCellSpacing(15);

		lblNumeroDeSerie.setStyleName("Negrita");

		lblTipoActivo.setStyleName("Negrita");
		lblListaActivo.setStyleName("Negrita");
		lblNumeroDeSerie.setStyleName("Negrita");
		lblModeloSurtidor.setStyleName("Negrita");
		lblNumeroDeSerie.setStyleName("Negrita");

		lblTipoCombustibleT.setStyleName("Negrita");

		lblidC.setStyleName("Negrita");
		lblidT.setStyleName("Negrita");
		lblidB.setStyleName("Negrita");
		lblDescC.setStyleName("Negrita");
		lblDescT.setStyleName("Negrita");
		lblCapT.setStyleName("Negrita");	
		lblDescB.setStyleName("Negrita");

		panelVActivo_ListaActivo_4C.setSpacing(5);
		panelVActivo_TipoActivo_4C.setSpacing(5);
		panelHActivoYDatosActivo_2C.setBorderWidth(1);
		panelHActivoYDatosActivo_2C.setWidth("100%");
		listBoxListaDeActivos.setVisibleItemCount(5);
		listBoxListaDeActivos.setWidth("280px");
		panelHActivo_3C.setSpacing(5);
		panelHActivo_3C.setSpacing(5);

		panelInferiorDatosActivos.setWidth("850px");
		panelInferiorDatosActivos.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		btnAceptarOA.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				siguiente();
			}
		});
	}

	private void siguiente() {
		if(activoSeleccionado != null)
		{
			Reparacion reparacion = iuSegTec.orden.obtenerReparacionDeEsteActivo(activoSeleccionado);
			
			if(reparacion == null){
				reparacion = Reparacion.crearReparacion(activoSeleccionado);				
			}
			
			reparacion.setActivo(activoSeleccionado);
			reparacion.setOrden(iuSegTec.orden);
			IUWidgetSolucion widgetSolucion = new IUWidgetSolucion(reparacion, popUp, glass, iuSegTec, Constants.OPERACION_WIDGET_SOLUCION.VIENE_DE_OTRO_ACTIVO, sesion);
			widgetSolucion.initWidgets();

			DialogBox dialogo = widgetSolucion.getDialogo();
			dialogo.show();
			// dialogo.setPopupPosition(iuSegTec.margenHorizontal, iuSegTec.margenVertical);
			dialogo.center();
			
			dialogOtro.hide();
			dialogOtro.remove(vpDialOtro);
		}else{
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe seleccionar un activo");
			vpu.showPopUp();
		}
	}

	public DialogBox agregarOtroActivo()
	{
		vpDialOtro= agregarWidgetsActivoYDatosActivoOA();

		dialogOtro.add(vpDialOtro);
		return dialogOtro;
	}

	VerticalPanel agregarWidgetsActivoYDatosActivoOA(){
		listBoxTipoActivo.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				activoSeleccionado = null;
				cambiarTipoActivo();				
			}
		});

		listBoxListaDeActivos.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				cambiarActivoSeleccionado(Integer.valueOf(listBoxListaDeActivos.getValue(listBoxListaDeActivos.getSelectedIndex())));
			}
		});

		panelHActivoYDatosActivo_2C.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panelHActivoYDatosActivo_2C.setBorderWidth(1);
		panelHActivoYDatosActivo_2C.setSpacing(5);
		panelHActivoYDatosActivo_2C.add(panelSuperiorActivoSeleccionado);
		panelHActivoYDatosActivo_2C.add(panelInferiorDatosActivos);

		//Activos
		panelHActivo_3C.add(panelVActivo_TipoActivo_4C);
		panelHActivo_3C.add(panelVActivo_ListaActivo_4C);
		panelSuperiorActivoSeleccionado.add(panelHActivo_3C);
		panelSuperiorActivoSeleccionado.setSpacing(5);
		panelSuperiorActivoSeleccionado.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);

		//Tipo Activo
		panelVActivo_TipoActivo_4C.add(lblTipoActivo);
		panelVActivo_TipoActivo_4C.add(listBoxTipoActivo);

		//Lista Activo
		panelVActivo_ListaActivo_4C.add(lblListaActivo);
		panelVActivo_ListaActivo_4C.add(listBoxListaDeActivos);	

		//DatosActivos

		//Tabla para Datos de los Surtidores================================================================================================
		tableDatosSurtidor.setWidget(0, 0, lblNumeroDeSerie);
		tableDatosSurtidor.setWidget(0, 2, lblModeloSurtidor);

		tableDatosTanque.setWidget(0, 0, lblidT);
		tableDatosTanque.setWidget(0, 2, lblDescT);
		tableDatosTanque.setWidget(1, 0, lblTipoCombustibleT);
		tableDatosTanque.setWidget(1, 2, lblCapT);		

		tableDatosActivoGenerico.setWidget(0, 0, lblidC);
		tableDatosActivoGenerico.setWidget(0, 2, lblDescC);

		tableDatosBomba.setWidget(0, 0, lblidB);
		tableDatosBomba.setWidget(0, 2, lblDescB);


		if (panelInferiorDatosActivos.getWidgetCount()== 0)
		{			
			panelInferiorDatosActivos.add(tableDatosSurtidor);			//Por defecto se agreaga la tabla de los surtidores
		}		

		HorizontalPanel hpFallaYTarea = new HorizontalPanel();
		hpFallaYTarea.setWidth("600px");
		hpFallaYTarea.setSpacing(5);

		panelVDatosActivo_3C.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panelVDatosActivo_3C.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		//ACEPTAR CANCELAR================================================================================================		
		panelVDatosFallaActivo_5C_2.setWidth("500px");
		panelVDatosFallaActivo_5C_2.setHeight("31px");
		panelVDatosFallaActivo_5C_2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panelVDatosFallaActivo_5C_2.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);

		panelVFechaInFinDura.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

		HorizontalPanel hpLabelYBtnCerrar = new HorizontalPanel();

		panelVDatosFallaActivo_5C_2.add(hpLabelYBtnCerrar);
		hpLabelYBtnCerrar.setSpacing(5);
		hpLabelYBtnCerrar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hpLabelYBtnCerrar.add(btnCancelarOA);
		hpLabelYBtnCerrar.add(btnAceptarOA);

		lblTituloReportarFallaYTarea.setStyleName("Subtitulo");

		vpRetPanelActivo.setSpacing(5);
		hpLabelYBtnCerrar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);


		vpRetPanelActivo.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vpRetPanelActivo.add(lblTituloReportarFallaYTarea);

		btnCancelarOA.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				dialogOtro.hide();
				glass.hide();
				dialogOtro.remove(vpDialOtro);
			}			
		});

		vpRetPanelActivo.add(panelHActivoYDatosActivo_2C);
		vpRetPanelActivo.add(hpLabelYBtnCerrar);

		return vpRetPanelActivo;
	}

	//Va a la base a buscar el activo, y setea el atributo activo de clase
	void cambiarActivoSeleccionado(int idActivoSeleccionado){
		ProyectoBilpa.greetingService.buscarActivo(idActivoSeleccionado, new AsyncCallback<Activo>(){
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar activo");
				vpu.showPopUp();
			}

			public void onSuccess(Activo result) {
				if(result !=null){
					activoSeleccionado = result;	
					mostrarDatosActivoSeleccionado();
				}else{
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar activo");
					vpu.showPopUp();
				}
			}
		});
	}	

	void mostrarDatosActivoSeleccionado(){
		panelInferiorDatosActivos.remove(0);
		if(activoSeleccionado.getTipo()==1){//Surtidor
			Surtidor surtidor = (Surtidor)activoSeleccionado;
			mostrarDatosSurtidorSeleccionado(surtidor);			
		}else{
			if(activoSeleccionado.getTipo()==2){//Tanque
				Tanque tanque = (Tanque)activoSeleccionado;
				mostrarDatosTanqueSeleccionado(tanque);	
			}
			if(activoSeleccionado.getTipo()==4){//Bomba
				BombaSumergible bomba = (BombaSumergible)activoSeleccionado;
				mostrarDatosBombaSeleccionada(bomba);	
			}	

			if(activoSeleccionado.getTipo()==6){//Bomba
				ActivoGenerico generico = (ActivoGenerico)activoSeleccionado;
				mostrarDatosGenericoSeleccionado(generico);	
			}	
			
		}
	}

	void mostrarDatosSurtidorSeleccionado(Surtidor surtidor){
		tableDatosSurtidor.setWidget(0, 0, lblNumeroDeSerie);
		tableDatosSurtidor.setText(0, 1, surtidor.getNumeroSerie());
		tableDatosSurtidor.setWidget(0, 2, lblModeloSurtidor);
		tableDatosSurtidor.setText(0, 3, surtidor.getModelo().toString());

		panelInferiorDatosActivos.add(tableDatosSurtidor);
		
		garantiaUtil = new GarantiaUtil(surtidor, tableDatosSurtidor, 0, 4, OPERACION_GARANTIA.PANTALLA_ORDEN);
	}

	void cambiarTipoActivo(){
		tipoActivo = Integer.valueOf(listBoxTipoActivo.getValue(listBoxTipoActivo.getSelectedIndex()));
		obtenerActivosPorTipo(tipoActivo);
		cambiarPanelDatosActivo();
	}

	void mostrarDatosTanqueSeleccionado(Tanque tanque){
		tableDatosTanque.setWidget(0, 0, lblidT);
		tableDatosTanque.setText(0, 1, String.valueOf(tanque.getId()));
		tableDatosTanque.setWidget(0, 2, lblDescT);
		tableDatosTanque.setText(0, 3, tanque.getDescripcion());
		tableDatosTanque.setWidget(1, 0, lblTipoCombustibleT);
		tableDatosTanque.setText(1, 1, tanque.getProducto().getNombre());
		tableDatosTanque.setWidget(1, 2, lblCapT);
		tableDatosTanque.setText(1, 3, String.valueOf(tanque.getCapacidad()) + " lts");

		panelInferiorDatosActivos.add(tableDatosTanque);

		garantiaUtil = new GarantiaUtil(tanque, tableDatosTanque, 0, 4, OPERACION_GARANTIA.PANTALLA_ORDEN);
	}


	void mostrarDatosGenericoSeleccionado(ActivoGenerico activo) {
		tableDatosActivoGenerico.setWidget(0, 0, lblidC);
		tableDatosActivoGenerico.setText(0, 1, String.valueOf(activo.getId()));
		tableDatosActivoGenerico.setWidget(0, 2, lblDescC);
		tableDatosActivoGenerico.setText(0, 3, activo.toString());

		panelInferiorDatosActivos.add(tableDatosActivoGenerico);

	}

	void mostrarDatosBombaSeleccionada(BombaSumergible bomba) {
		tableDatosBomba.setWidget(0, 0, lblidB);
		tableDatosBomba.setText(0, 1, String.valueOf(bomba.getId()));
		tableDatosBomba.setWidget(0, 2, lblDescB);
		tableDatosBomba.setText(0, 3, bomba.getDescripcion());

		panelInferiorDatosActivos.add(tableDatosBomba);

		garantiaUtil = new GarantiaUtil(bomba, tableDatosBomba, 0, 4, OPERACION_GARANTIA.PANTALLA_ORDEN);

	}

	void cambiarPanelDatosActivo(){
		panelInferiorDatosActivos.remove(0);
		activoSeleccionado=null;
		if(tipoActivo==1){
			panelInferiorDatosActivos.add(tableDatosSurtidor);
			tableDatosSurtidor.setText(0, 1,"");
			tableDatosSurtidor.setText(0, 3,"");
		}
		if(tipoActivo==2){
			panelInferiorDatosActivos.add(tableDatosTanque);
			tableDatosTanque.setText(0, 1, "");
			tableDatosTanque.setText(0, 3, "");
			tableDatosTanque.setText(1, 1, "");
			tableDatosTanque.setText(1, 3, "");
		}
		if(tipoActivo==3){
			panelInferiorDatosActivos.add(tableDatosActivoGenerico);
			tableDatosActivoGenerico.setText(0, 1, "");
			tableDatosActivoGenerico.setText(0, 3, "");
		}
		if(tipoActivo==4){
			panelInferiorDatosActivos.add(tableDatosBomba);
			tableDatosBomba.setText(0, 1, "");
			tableDatosBomba.setText(0, 3, "");
		} 
		
		if(tipoActivo==6){
			panelInferiorDatosActivos.add(tableDatosActivoGenerico);
			tableDatosActivoGenerico.setText(0, 1, "");
			tableDatosActivoGenerico.setText(0, 3, "");
		} 
	}

	private void cargarDatosActivos(){
		//Por defecto se cargan los surtidores
		int tipoActivo = 1;						//Se invoca a obtenerAcitvosPorTipo para traer los surtidores al cargar la pagina.
		obtenerActivosPorTipo(tipoActivo);
	}

	private void cargarTipoActivo(){
		ProyectoBilpa.greetingService.obtenerTiposDeActivos(iuSegTec.orden.getEmpresa().getId(), false, new AsyncCallback<ArrayList<String>>() {
			public void onFailure(Throwable caught) {	
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener tipos de activos");
				vpu.showPopUp();
			}
			public void onSuccess(ArrayList<String> result) {
				for (int i=0; i<result.size();i++){
					String value = String.valueOf(i + 1);
					if (i > 2){
						value = "6";
					}
					listBoxTipoActivo.addItem(result.get(i), value);
				}				
				
				if(vieneDeBotonAnteriorDelWidgetSolucion){
					cargarDatosActivoSiVieneDeAnterior();
				}
			}
		});	
	}

	private void obtenerActivosPorTipo(int tipoActivo){
		ProyectoBilpa.greetingService.obtenerActivosPorTipo(iuSegTec.orden.getEmpresa(), tipoActivo, new AsyncCallback<ArrayList<Activo>>() {
			public void onSuccess(ArrayList<Activo> result) {
				listBoxListaDeActivos.clear();
				Activo a;
				for (int i=0; i<result.size();i++){
					a = result.get(i);
					if (!esActivoReportadoPorElCliente(a))
					{
						if (a.getTipo() == 6){
							ActivoGenerico ag = (ActivoGenerico)a;
							if (ag.getTipoActivoGenerico().getNombre().equals(listBoxTipoActivo.getItemText(listBoxTipoActivo.getSelectedIndex()))){
								listBoxListaDeActivos.addItem(a.toString(), String.valueOf(a.getId()));
							}
						} else {
							listBoxListaDeActivos.addItem(a.toString(), String.valueOf(a.getId()));					
						}
					}
				}   
				
				if(vieneDeBotonAnteriorDelWidgetSolucion && activoSeleccionado != null){
					for(int i = 0 ; i < listBoxListaDeActivos.getItemCount() ; i ++){
						if(Integer.valueOf(listBoxListaDeActivos.getValue(i)) == activoSeleccionado.getId()){
							listBoxListaDeActivos.setSelectedIndex(i);
						}
					}
					cambiarActivoSeleccionado(activoSeleccionado.getId());
				}
			}

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener activos por tipo");
				vpu.showPopUp();
			}
		});
	}

	private boolean esActivoReportadoPorElCliente(Activo activo) {
		for (Reparacion rep : iuSegTec.orden.getReparaciones())
		{
			if (rep.getActivo().equals(activo) && 
					rep.getFallaReportada() != null)
			{
				return true;
			}
		}  
		return false;
	}
	
	private void cargarDatosActivoSiVieneDeAnterior() {
		if(vieneDeBotonAnteriorDelWidgetSolucion && activoSeleccionado != null)//viene del boton anterior del IUWidgetSolucion
		{
			for(int i = 0 ; i < listBoxTipoActivo.getItemCount() ; i ++){
				if(Integer.valueOf(listBoxTipoActivo.getValue(i)) == activoSeleccionado.getTipo()){
					listBoxTipoActivo.setSelectedIndex(i);
				}
			}

			obtenerActivosPorTipo(activoSeleccionado.getTipo());
		}
	}
	
}
