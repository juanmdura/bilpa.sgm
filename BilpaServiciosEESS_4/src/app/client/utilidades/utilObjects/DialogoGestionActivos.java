package app.client.utilidades.utilObjects;

import java.util.ArrayList;
import java.util.Set;

import app.client.ProyectoBilpa;
import app.client.dominio.Activo;
import app.client.dominio.BombaSumergible;
import app.client.dominio.Canio;
import app.client.dominio.Estacion;
import app.client.dominio.FallaTecnica;
import app.client.dominio.Pico;
import app.client.dominio.Surtidor;
import app.client.dominio.Tanque;
import app.client.iu.orden.tecnico.IUSeguimientoTecnico;
import app.client.iu.widgets.ValidadorPopup;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DialogoGestionActivos {

	private VerticalPanel vPanelPrincipal_1 = new VerticalPanel();

	private HorizontalPanel panelHTitulOrden = new HorizontalPanel();
	private VerticalPanel panelVActivoYDatosActivo_2B = new VerticalPanel();
	private HorizontalPanel panelHBotonesAceptarYCancelar_2F = new HorizontalPanel();

	private HorizontalPanel panelHActivo_3B = new HorizontalPanel();
	private VerticalPanel panelVDatosActivo_3B = new VerticalPanel();

	private VerticalPanel panelVActivo_TipoActivo_4B = new VerticalPanel();
	private VerticalPanel panelVActivo_ListaActivo_4B = new VerticalPanel();

	private VerticalPanel panelVDatosActivosSeleccionado_4B = new VerticalPanel();

	private HorizontalPanel panelHDatosActivoSeleccionado_5B_1 = new HorizontalPanel();

	private VerticalPanel panelVFallDetectada3D = new VerticalPanel();
	private Label lblTituloFallasDetectadas = new Label("Falla Detectada por el Técnico");
	private Label lblDescripcionFallaDetectada = new Label("Descripción");
	private Button btnAgregarFallaDetectada = new Button("Agregar");
	private HorizontalPanel panelHFallaDetectada = new HorizontalPanel();
	private ListBox listBoxListaFallasDetectadas = new ListBox();


	private ListBox listBoxTipoActivo = new ListBox();
	private ListBox listBoxListaDeActivos = new ListBox();
	private ListBox listBoxPicosDeSurtidor = new ListBox();

	private Label lbTitulo = new Label("Gestión de activos");
	private Label lbTipoActivo = new Label("Tipo de activo");
	private Label lbListaActivo = new Label("Lista de activos");

	private Label lbNumeroDeSerie = new Label("Numero de serie");
	private Label lbModeloSurtidor = new Label("Modelo");
	private Label lbPico = new Label("Numero de pico");
	private Label lbTipoCombustible = new Label("Combustible");

	private Button btnGuardar = new Button("Guardar");
	private Button btnCancelar = new Button("Cancelar");

	private FlexTable tableDatosSurtidor = new FlexTable();
	private FlexTable tableDatosTanque = new FlexTable();
	private FlexTable tableDatosCanio = new FlexTable();
	private FlexTable tableDatosBomba = new FlexTable();

	private DialogBox dialRetorno = new DialogBox();

	private Estacion estacion;
	private Activo activoSeleccionado;
	private int tipoActivo;
	private FallaTecnica fallaDetectadaSeleccionada;
	private ArrayList<FallaTecnica> listaFallasTecnicas = new ArrayList<FallaTecnica>();
	private IUSeguimientoTecnico iu;

	private GlassPopup glass = new GlassPopup();

	public VerticalPanel getVPanelPrincipal() {
		return vPanelPrincipal_1;
	}

	//////////////////////////////////CONSTRUCTOR//////////////////////////////////////////////////////////////
	public DialogoGestionActivos(Estacion estacion, IUSeguimientoTecnico iu){
		if(iu!=null){
			this.iu=iu;			
		}
		this.estacion = estacion;
		setearWidgets();					//define tamanos, bordes, etc
		agregarWidgets(estacion);			//agrega widgets a los paneles
		cargarWidgets();					//carga los widgets con elementos obtenidos de la BD		
	}

	
	public DialogBox cargarDialogoActivos(){		
		dialRetorno.add(this.vPanelPrincipal_1);
		return dialRetorno;
	}

	private void setearWidgets() {
		dialRetorno.setAnimationEnabled(true);
		this.vPanelPrincipal_1.setWidth("495px");
		this.vPanelPrincipal_1.setSpacing(5);
		this.vPanelPrincipal_1.setBorderWidth(1);

		this.panelHTitulOrden.setWidth("495px");
		this.panelVActivoYDatosActivo_2B.setWidth("495");

		//		panelVFallDetectada3D.setBorderWidth(1);
		panelVFallDetectada3D.setSpacing(10);
		this.panelVFallDetectada3D.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		this.panelVFallDetectada3D.setWidth("495");

		this.panelHActivo_3B.setWidth("400px");
		this.panelVActivo_TipoActivo_4B.setWidth("150px");
		this.panelVActivo_ListaActivo_4B.setWidth("250px");

		this.panelHBotonesAceptarYCancelar_2F.setSpacing(5);

		this.listBoxListaDeActivos.setVisibleItemCount(8);
		this.listBoxListaDeActivos.setWidth("300");

		//this.panelHActivo_3B.setBorderWidth(1);
		this.panelHActivo_3B.setSpacing(5);

		this.tableDatosSurtidor.setHeight("80");
		this.tableDatosSurtidor.setCellPadding(3);
		this.tableDatosSurtidor.setCellSpacing(5);

		this.tableDatosTanque.setHeight("80");
		this.tableDatosTanque.setCellPadding(3);
		this.tableDatosTanque.setCellSpacing(5);

		this.tableDatosCanio.setHeight("80");
		this.tableDatosCanio.setCellPadding(3);
		this.tableDatosCanio.setCellSpacing(5);

		this.tableDatosBomba.setHeight("80");
		this.tableDatosBomba.setCellPadding(3);
		this.tableDatosBomba.setCellSpacing(5);

		this.panelVDatosActivo_3B.setWidth("545px");
		this.panelVDatosActivo_3B.setBorderWidth(1);

		this.listBoxPicosDeSurtidor.setWidth("150");

		this.panelVDatosActivosSeleccionado_4B.setWidth("495px");
		this.panelHDatosActivoSeleccionado_5B_1.setWidth("495px");

		this.panelHActivo_3B.setSpacing(5);

		this.btnGuardar.setWidth("100");
		this.btnCancelar.setWidth("100");
		this.btnAgregarFallaDetectada.setWidth("100");


		this.panelVActivo_ListaActivo_4B.setSpacing(5);
		this.panelVActivo_TipoActivo_4B.setSpacing(5);
	}


	private void agregarWidgets(Estacion estacion){
		this.vPanelPrincipal_1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		this.vPanelPrincipal_1.add(this.lbTitulo);
		agregarWidgetsHPanelActivoYDatosActivo_2B();							//B	
		agregarWidetsHBotonesAceptarYCancelar_2F();
		agregarWidgetsPanelFallaRep();
		this.vPanelPrincipal_1.add(panelVActivoYDatosActivo_2B);
		vPanelPrincipal_1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		this.vPanelPrincipal_1.add(panelVFallDetectada3D);
		this.vPanelPrincipal_1.add(panelHBotonesAceptarYCancelar_2F);
	}

	private void agregarWidetsHBotonesAceptarYCancelar_2F() {

		this.vPanelPrincipal_1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		panelHBotonesAceptarYCancelar_2F.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		this.panelHBotonesAceptarYCancelar_2F.add(btnCancelar);
		this.panelHBotonesAceptarYCancelar_2F.add(btnGuardar);

		btnCancelar.addClickHandler(new ClickHandler(){

			
			public void onClick(ClickEvent event) {
				dialRetorno.hide();				
			}			
		});

		btnGuardar.addClickHandler(new ClickHandler(){

			
			public void onClick(ClickEvent event) {
				if(!listBoxListaFallasDetectadas.getItemText(listBoxListaFallasDetectadas.getSelectedIndex()).equalsIgnoreCase("Sin Seleccionar")){
					if(activoSeleccionado!=null){
						buscarFallaDetectadaDeLaLista(listBoxListaFallasDetectadas.getSelectedIndex());
						
						if(fallaDetectadaSeleccionada!= null){
							dialRetorno.hide();
							//TODO iu.activoSeleccionado = activoSeleccionado;
//							iu.agregarFallaTecnicaANuevaReparacion(fallaDetectadaSeleccionada);
							ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "DialogoGestionActivos linea 215");
							vpu.showPopUp();
						}						
					}else{
						ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe seleccionar un activo");
						vpu.showPopUp();
					}
				}else{
					ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe seleccionar una Falla Detectada");
					vpu.showPopUp();
				}
			}
		});

		this.panelHBotonesAceptarYCancelar_2F.add(btnGuardar);

	}

	private FallaTecnica buscarFallaDetectadaDeLaLista(int id){
		for(FallaTecnica ft : listaFallasTecnicas){
			if(ft.getId()==id){
				fallaDetectadaSeleccionada = ft;
				return ft;
			}
		}
		return null;
	}



	private void cargarWidgets(){
		this.cargarDatosActivos();
		this.cargarTipoActivo();
		this.cargarFallasDetectadas();
	}

	private void cargarDatosActivos(){
		//Por defecto se cargan los surtidores
		int tipoActivo = 1;						//Se invoca a obtenerAcitvosPorTipo para traer los surtidores al cargar la pagina.
		obtenerActivosPorTipo(tipoActivo);
	}

	private void cargarTipoActivo(){
		ProyectoBilpa.greetingService.obtenerTiposDeActivos(estacion.getId(), false, new AsyncCallback<ArrayList<String>>() {
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener tipos de activos");
				vpu.showPopUp();
			}
			public void onSuccess(ArrayList<String> result) {
				for (int i=0; i<result.size();i++){
					listBoxTipoActivo.addItem(result.get(i), String.valueOf(i + 1));
				}				
			}
		});	
	}

	private void agregarWidgetsHPanelActivoYDatosActivo_2B(){

		listBoxTipoActivo.addChangeHandler(new ChangeHandler() {

			
			public void onChange(ChangeEvent event) {
				cambiarTipoActivo();				
			}
		});

		listBoxListaDeActivos.addChangeHandler(new ChangeHandler() {

			
			public void onChange(ChangeEvent event) {
				cambiarActivoSeleccionado();

			}
		});


		this.panelVActivoYDatosActivo_2B.add(this.panelHActivo_3B);
		this.panelVActivoYDatosActivo_2B.setSpacing(5);
		this.panelVActivoYDatosActivo_2B.add(this.panelVDatosActivo_3B);

		//Lista Activo
		VerticalPanel espacioV1 = new VerticalPanel();
		VerticalPanel espacioV2 = new VerticalPanel();
		espacioV1.setWidth("30");
		espacioV1.setHeight("30");
		espacioV2.setWidth("30");

		//Activos
		this.panelHActivo_3B.add(this.panelVActivo_TipoActivo_4B);
		this.panelHActivo_3B.add(espacioV1);
		this.panelHActivo_3B.add(this.panelVActivo_ListaActivo_4B);

		//Tipo Activo
		this.panelVActivo_TipoActivo_4B.add(this.lbTipoActivo);
		this.panelVActivo_TipoActivo_4B.add(this.listBoxTipoActivo);

		this.panelVActivo_ListaActivo_4B.add(this.lbListaActivo);
		this.panelVActivo_ListaActivo_4B.add(this.listBoxListaDeActivos);	

		//DatosActivos

		//Tabla para Datos de los Surtidores================================================================================================
		this.tableDatosSurtidor.setText(0, 0, lbNumeroDeSerie.getText());
		this.tableDatosSurtidor.setText(0, 2, lbModeloSurtidor.getText());
		this.tableDatosSurtidor.setText(1, 0, lbPico.getText());

		this.tableDatosSurtidor.setWidget(1, 1, listBoxPicosDeSurtidor);

		this.listBoxPicosDeSurtidor.addChangeHandler(new ChangeHandler() {

			
			public void onChange(ChangeEvent event) {
				Pico pico = buscarPicoSeleccionado();
				//TODO PICO 				activoSeleccionado = pico;
				mostrarDatosPicoSeleccionado(pico);

			}
		});

		this.tableDatosSurtidor.setText(1, 2, lbTipoCombustible.getText());

		this.tableDatosTanque.setText(0, 0, "Id");
		this.tableDatosTanque.setText(0, 2, "Descripción");
		this.tableDatosTanque.setText(1, 0, "Tipo de Combustible");
		this.tableDatosTanque.setText(1, 2, "Capacidad");		

		this.tableDatosCanio.setText(0, 0, "Id");
		this.tableDatosCanio.setText(0, 2, "Descripción");

		this.tableDatosBomba.setText(0, 0, "Id");
		this.tableDatosBomba.setText(0, 2, "Descripción");

		this.panelVDatosActivosSeleccionado_4B.add(this.tableDatosSurtidor);			//Por defecto se agreaga la tabla de los surtidores

		this.panelVDatosActivo_3B.add(this.panelVDatosActivosSeleccionado_4B);
		this.panelVDatosActivo_3B.setSpacing(5);
	}

	private void cambiarTipoActivo(){
		tipoActivo = Integer.valueOf(listBoxTipoActivo.getValue(listBoxTipoActivo.getSelectedIndex()));
		obtenerActivosPorTipo(tipoActivo);
		cambiarPanelDatosActivo();
	}

	private void obtenerActivosPorTipo(int tipoActivo){

		ProyectoBilpa.greetingService.obtenerActivosPorTipo(this.estacion, tipoActivo, new AsyncCallback<ArrayList<Activo>>() {

			public void onSuccess(ArrayList<Activo> result) {
				listBoxListaDeActivos.clear();
				for (int i=0; i<result.size();i++){
					listBoxListaDeActivos.addItem(result.get(i).toString(), String.valueOf(result.get(i).getId()));
				}         
			}

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener activos por tipo");
				vpu.showPopUp();
			}
		});
	}

	private void cambiarPanelDatosActivo(){

		this.panelVDatosActivosSeleccionado_4B.remove(0);
		this.activoSeleccionado=null;
		if(this.tipoActivo==1){
			this.tableDatosSurtidor.setText(0, 1,"");
			this.tableDatosSurtidor.setText(0, 3,"");
			this.listBoxPicosDeSurtidor.clear();
			this.tableDatosSurtidor.setText(1, 3,"");
			this.panelVDatosActivosSeleccionado_4B.add(tableDatosSurtidor);
		}
		if(this.tipoActivo==2){
			this.panelVDatosActivosSeleccionado_4B.add(tableDatosTanque);
			this.tableDatosTanque.setText(0, 1, "");
			this.tableDatosTanque.setText(0, 3, "");
			this.tableDatosTanque.setText(1, 1, "");
			this.tableDatosTanque.setText(1, 3, "");
		}
		if(this.tipoActivo==3){
			this.panelVDatosActivosSeleccionado_4B.add(tableDatosCanio);
			this.tableDatosCanio.setText(0, 1, "");
			this.tableDatosCanio.setText(0, 3, "");
		}
		if(this.tipoActivo==4){
			this.panelVDatosActivosSeleccionado_4B.add(tableDatosBomba);
			this.tableDatosBomba.setText(0, 1, "");
			this.tableDatosBomba.setText(0, 3, "");
		}
	}

	private void mostrarDatosSurtidorSeleccionado(Surtidor surtidor){
		this.listBoxPicosDeSurtidor.clear();
		Set<Pico> listaPicos = surtidor.getPicos();

		this.tableDatosSurtidor.setText(1, 3, "");
		this.listBoxPicosDeSurtidor.addItem("Sin Seleccionar", "-2");

		for(Pico p: listaPicos){
			this.listBoxPicosDeSurtidor.addItem(p.getNumeroPico()+"", String.valueOf(p.getId()));
		}

		this.tableDatosSurtidor.setText(0, 0, lbNumeroDeSerie.getText());
		this.tableDatosSurtidor.setText(0, 1, surtidor.getNumeroSerie());
		this.tableDatosSurtidor.setText(0, 2, lbModeloSurtidor.getText());
		this.tableDatosSurtidor.setText(0, 3, surtidor.getModelo().toString());
		this.tableDatosSurtidor.setText(1, 0, lbPico.getText());
		this.tableDatosSurtidor.setWidget(1, 1, listBoxPicosDeSurtidor);
		this.tableDatosSurtidor.setText(1, 2, lbTipoCombustible.getText());


	}

	private void mostrarDatosTanqueSeleccionado(Tanque tanque){
		this.tableDatosTanque.setText(0, 0, "Id");
		this.tableDatosTanque.setText(0, 1, String.valueOf(tanque.getId()));
		this.tableDatosTanque.setText(0, 2, "Descripción");
		this.tableDatosTanque.setText(0, 3, tanque.getDescripcion());
		this.tableDatosTanque.setText(1, 0, "Tipo de Combustible");
		this.tableDatosTanque.setText(1, 1, tanque.getProducto().getNombre());
		this.tableDatosTanque.setText(1, 2, "Capacidad");
		this.tableDatosTanque.setText(1, 3, String.valueOf(tanque.getCapacidad()) + " lts");

		this.panelVDatosActivosSeleccionado_4B.add(tableDatosTanque);
	}


	private void mostrarDatosCanioSeleccionado(Canio canio) {
		this.tableDatosCanio.setText(0, 0, "Id");
		this.tableDatosCanio.setText(0, 1, String.valueOf(canio.getId()));
		this.tableDatosCanio.setText(0, 2, "Descripción");
		this.tableDatosCanio.setText(0, 3, canio.getDescripcion());

		this.panelVDatosActivosSeleccionado_4B.add(tableDatosCanio);

	}

	private void mostrarDatosBombaSeleccionada(BombaSumergible bomba) {
		this.tableDatosBomba.setText(0, 0, "Id");
		this.tableDatosBomba.setText(0, 1, String.valueOf(bomba.getId()));
		this.tableDatosBomba.setText(0, 2, "Descripción");
		this.tableDatosBomba.setText(0, 3, bomba.getDescripcion());

		this.panelVDatosActivosSeleccionado_4B.add(tableDatosBomba);

	}

	private void cambiarActivoSeleccionado(){
		int idActivoSeleccionado = Integer.valueOf(listBoxListaDeActivos.getValue(listBoxListaDeActivos.getSelectedIndex()));
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
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "es null");
					vpu.showPopUp();
				}
			}
		});
	}	

	private void mostrarDatosPicoSeleccionado(Pico pico){		

		if(pico != null){		
			this.tableDatosSurtidor.setText(1, 3, pico.getProducto().getNombre());
			//TODO PICO 	this.activoSeleccionado = pico;
		}
	}

	private void mostrarDatosActivoSeleccionado(){

		if(this.activoSeleccionado.getTipo()==1){//Surtidor
			Surtidor surtidor = (Surtidor)this.activoSeleccionado;
			mostrarDatosSurtidorSeleccionado(surtidor);			
		}else{
			this.panelVDatosActivosSeleccionado_4B.remove(0);
			if(this.activoSeleccionado.getTipo()==2){//Tanque
				Tanque tanque = (Tanque)this.activoSeleccionado;
				mostrarDatosTanqueSeleccionado(tanque);	
			}
			if(this.activoSeleccionado.getTipo()==3){//Canio
				Canio canio = (Canio)this.activoSeleccionado;
				mostrarDatosCanioSeleccionado(canio);	
			}
			if(this.activoSeleccionado.getTipo()==4){//Bomba
				BombaSumergible bomba = (BombaSumergible)this.activoSeleccionado;
				mostrarDatosBombaSeleccionada(bomba);	
			}	
		}

	}

	private void agregarWidgetsPanelFallaRep() {

		panelVFallDetectada3D.setSpacing(5);
		panelHFallaDetectada.setSpacing(5);
		panelHFallaDetectada.add(lblDescripcionFallaDetectada);
		
		panelHFallaDetectada.add(listBoxListaFallasDetectadas);

		HorizontalPanel hpSp = new HorizontalPanel();
		hpSp.setWidth("30");
		panelHFallaDetectada.add(hpSp);
		//		panelHFallaDetectada.add(btnAgregarFallaDetectada);

		this.panelVFallDetectada3D.add(lblTituloFallasDetectadas);
		this.panelVFallDetectada3D.add(this.panelHFallaDetectada);

		//groupBoxGestionActivo.add(w)

	}

	private Pico buscarPicoSeleccionado(){
		if (this.listBoxPicosDeSurtidor.getSelectedIndex()>=0){
			int idPicoSeleccionado = Integer.valueOf(this.listBoxPicosDeSurtidor.getValue(this.listBoxPicosDeSurtidor.getSelectedIndex()));
			Activo activo = this.estacion.buscarActivoDeEmpresa(idPicoSeleccionado);
			if(activo.getTipo()==5){
				//TODO PICO Pico pico =(Pico)activo;
				//return pico;
			}
		}
		return null;
	}

	private void cargarFallasDetectadas(){
		ProyectoBilpa.greetingService.obtenerTodosLasFallasT(new AsyncCallback<ArrayList<FallaTecnica>>() {

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener todas las fallas");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<FallaTecnica> result) {
				listBoxListaFallasDetectadas.addItem("Sin Seleccionar");
				for (int i=0; i<result.size();i++){
					listaFallasTecnicas.add(result.get(i));
					listBoxListaFallasDetectadas.addItem(result.get(i).toString(), String.valueOf(result.get(i).getId()));
				}				
			}
		});		
	}
}
