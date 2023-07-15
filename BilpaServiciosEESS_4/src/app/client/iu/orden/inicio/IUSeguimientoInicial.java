package app.client.iu.orden.inicio;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import app.client.ProyectoBilpa;
import app.client.dominio.Activo;
import app.client.dominio.BombaSumergible;
import app.client.dominio.Canio;
import app.client.dominio.Estacion;
import app.client.dominio.FallaReportada;
import app.client.dominio.IUCorrectivo.IUCorrectivoEnum;
import app.client.dominio.Orden;
import app.client.dominio.Pendiente;
import app.client.dominio.Persona;
import app.client.dominio.Reparacion;
import app.client.dominio.Solucion;
import app.client.dominio.Surtidor;
import app.client.dominio.Tanque;
import app.client.dominio.TipoTrabajo;
import app.client.dominio.data.DatoConsultaHistoricoOrdenes;
import app.client.dominio.data.DatoOrdenesActivasEmpresa;
import app.client.dominio.data.PendienteDataUI;
import app.client.iu.correctivo.IUCorrectivoSeleccionarActivoYFalla;
import app.client.iu.correctivo.IUCorrectivoTitulo;
import app.client.iu.correctivo.IUCorrectivoToolBar;
import app.client.iu.correctivo.IUWidgetCorrectivo;
import app.client.iu.correctivo.pendientes.IURepararPendienteTitulo;
import app.client.iu.falla.IUWidgetFalla;
import app.client.iu.menu.IUMenuPrincipal;
import app.client.iu.menu.IUMenuPrincipalAdministrativo;
import app.client.iu.orden.IUListaOrdenes;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.Constants.OPERACION_GARANTIA;
import app.client.utilidades.UtilCss;
import app.client.utilidades.UtilOrden;
import app.client.utilidades.UtilUI;
import app.client.utilidades.utilObjects.FileDownload;
import app.client.utilidades.utilObjects.GarantiaUtil;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;
import app.client.utilidades.utilObjects.PushButtonRow;

public class IUSeguimientoInicial extends IUWidgetCorrectivo {

	FileDownload file = new FileDownload();
	private VerticalPanel vPanelPrincipal_1 = new VerticalPanel();

	private HorizontalPanel hpTituloCorrectivo = new HorizontalPanel();

	//2
	private HorizontalPanel panelHBotones = new HorizontalPanel();
	private HorizontalPanel panelHPrioridadyTecnicoyEstado_2B = new HorizontalPanel();
	private HorizontalPanel panelHActivoYDatosActivo_2C = new HorizontalPanel();

	private VerticalPanel panelVListaFallas_2D = new VerticalPanel();
	private VerticalPanel panelVHistorico_2E = new VerticalPanel();
	private VerticalPanel panelVOrdenesPendientes_2F = new VerticalPanel();
	private VerticalPanel panelVHistorico_2G = new VerticalPanel();
	private HorizontalPanel panelHBotonesAceptarYCancelar_2G = new HorizontalPanel();

	private HorizontalPanel panelHPrioridad_3B = new HorizontalPanel();
	private HorizontalPanel panelHTecnico_3B = new HorizontalPanel();
	private HorizontalPanel panelHEstado_3B = new HorizontalPanel();

	private HorizontalPanel panelHContacto_TipoTrabajo_Preventivo = new HorizontalPanel();
	private HorizontalPanel panelHContacto_TipoTrabajo_Preventivo2 = new HorizontalPanel();

	private Label lblPreventivo = new Label("Preventivo: ");

	private HorizontalPanel panelHActivo_3C = new HorizontalPanel();
	private VerticalPanel panelVDatosActivo_3C = new VerticalPanel();

	private VerticalPanel panelVActivo_TipoActivo_4C = new VerticalPanel();
	private VerticalPanel panelVActivo_ListaActivo_4C = new VerticalPanel();
	private VerticalPanel panelVDatosActivosSeleccionado_4C = new VerticalPanel();

	private HorizontalPanel panelHDatosActivo_Falla_4C = new HorizontalPanel();
	private VerticalPanel panelVDatosFallaActivo_5C_2 = new VerticalPanel();

	private Button btnAgregarFallaReportada = new Button("Agregar");
	private Button btnGuardar = new Button("Guardar");
	private Button btnCancelar = new Button("Cancelar");
	private Button btnAnular = new Button("Anular");

	private FlexTable tableDatosSurtidor = new FlexTable();
	private FlexTable tableDatosTanque = new FlexTable();
	private FlexTable tableDatosCanio = new FlexTable();
	private FlexTable tableDatosBomba = new FlexTable();

	private FlexTable tablaFallasReportadas = new FlexTable();
	private FlexTable tableHistoricoReparaciones = new FlexTable();
	private FlexTable tableListaOrdenesPendientes = new FlexTable();

	private Label lblPrioridad = new Label("Prioridad: ");
	private Label lblTecnicoAsignado = new Label("Tecnico asignado: ");
	private Label lblEstado = new Label("Estado: ");
	private Label lblContacto = new Label("Contacto: ");
	private Label lblContactoDato = new Label();
	private Label lblFallaReportadaCliente = new Label("Tipo de falla reportada");
	private Label lblFallaReportadaCliente2 = new Label("Falla reportada");
	private Label lblTipoCombustibleT = new Label("Combustible");

	private Label lblNumeroDeSerie = new Label("Serie");
	private Label lblModeloSurtidor = new Label("Modelo surtidor");
	private Label lblTipoActivo = new Label("Tipo de activo");
	private Label lblListaActivo = new Label("Lista de activos");
	private Label lblidT = new Label("Id");
	private Label lblDescT = new Label("Descripción");
	private Label lblidC = new Label("Id");
	private Label lblDescC = new Label("Descripción");
	private Label lblidB = new Label("Id");
	private Label lblDescB = new Label("Descripción");
	private Label lblCapT = new Label("Capacidad");

	private Label lblTituloListaFallas = new Label("Fallas reportadas por el cliente");

	private ListBox listBoxPrioridades = new ListBox();	
	private ListBox listBoxTipoActivo = new ListBox();
	private ListBox listBoxListaDeActivos = new ListBox();

	private CheckBox chPreventivo = new CheckBox();

	private GlassPopup glass = new GlassPopup();

	private Map<PushButtonRow, Reparacion> mapButtonsRep = new HashMap<PushButtonRow, Reparacion>();

	private Estacion estacion;
	private Activo activoSeleccionado;
	private Persona sesion;
	private Orden orden;
	private int tipoActivo;
	private ArrayList<DatoConsultaHistoricoOrdenes> historicoOrdenes = new ArrayList<DatoConsultaHistoricoOrdenes>();

	private HorizontalPanel panelHVerHistorico = new HorizontalPanel();
	private VerticalPanel vpHistorico = new VerticalPanel();

	private Image imgPDF = new Image("img/pdf2.png");
	private PushButton btnPDF = new PushButton(imgPDF);

	private Image imgPDFOP = new Image("img/pdf2.png");
	private PushButton btnPDFOp = new PushButton(imgPDFOP);

	private Image imgPermiso = new Image("img/permiso.png");
	private PushButton btnPermiso = new PushButton(imgPermiso);

	private boolean btnHistoricoApretado = false;
	Image imgBtnVerHistorico = new Image("img/look.png");
	PushButton btnVerHistorico = new PushButton(imgBtnVerHistorico);
	private Label lblVerHistorico = new Label("Histórico de correctivos");
	IUWidgetFalla iuWidgetFalla = new IUWidgetFalla();

	private IURepararPendienteTitulo iuRepararPendienteTitulo;
	private VerticalPanel vpPendientes = new VerticalPanel();

	private PopupCargando popUp = new PopupCargando("Cargando...");

	GarantiaUtil garantiaUtil;

	private IUCorrectivoToolBar iUCorrectivoToolBar;
	private IUCorrectivoTitulo iuCorrectivoTitulo;
	private IUCorrectivoSeleccionarActivoYFalla iuCorrectivoSeleccionarActivo;

	public Orden getOrden() {
		return orden;
	}

	public VerticalPanel getVPanelPrincipal() {
		return vPanelPrincipal_1;
	}

	public IUSeguimientoInicial(Orden orden, Persona persona)
	{			
		if(orden !=null && persona != null){				
			this.orden = orden;
			sesion = persona;
			estacion = (Estacion)orden.getEmpresa();

			popUp.show();
			setearWidgets();							//define tamanos, bordes, etc
			cargarWidgets();							//carga los widgets con elementos obtenidos de la BD
			agregarWidgets(orden);						//agrega widgets a los paneles
			color();
		}
	}

	private void color() {
		lblPrioridad.setStyleName("Negrita");
		lblTecnicoAsignado.setStyleName("Negrita");
		lblEstado.setStyleName("Negrita");
		lblContacto.setStyleName("Negrita");
		lblFallaReportadaCliente.setStyleName("Negrita");
		lblFallaReportadaCliente2.setStyleName("Negrita");
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

		lblTituloListaFallas.setStyleName("SubTitulo");
	}

	private void setearWidgets() {
		setearvPanelPrincipal_1();		
		panelHPrioridadyTecnicoyEstado_2B.setBorderWidth(1);

		this.listBoxPrioridades.setWidth("150");

		this.panelVActivo_ListaActivo_4C.setHeight("173px");
		this.listBoxListaDeActivos.setVisibleItemCount(8);
		this.listBoxListaDeActivos.setWidth("300");

		this.panelHActivo_3C.setSpacing(5);

		this.tableDatosSurtidor.setHeight("86");
		this.tableDatosSurtidor.setCellPadding(8);
		this.tableDatosSurtidor.setCellSpacing(10);

		this.tableDatosTanque.setHeight("86");
		this.tableDatosTanque.setCellPadding(8);
		this.tableDatosTanque.setCellSpacing(10);

		this.tableDatosCanio.setHeight("86");
		this.tableDatosCanio.setCellPadding(8);
		this.tableDatosCanio.setCellSpacing(10);

		this.tableDatosBomba.setHeight("86");
		this.tableDatosBomba.setCellPadding(8);
		this.tableDatosBomba.setCellSpacing(10);

		this.panelVDatosActivo_3C.setBorderWidth(1);

		this.panelHDatosActivo_Falla_4C.setWidth("500px");

		this.iuWidgetFalla.listBoxTiposFallasR.setWidth("350px");
		this.iuWidgetFalla.listBoxFallasR.setWidth("350px");

		this.btnAgregarFallaReportada.setWidth("100px");		

		this.panelHActivo_3C.setBorderWidth(1);
		this.panelHActivo_3C.setSpacing(5);

		//Tablas Falla Reclamada, Falla Detectada, Tarea Realizada y Repuesto================================================================================================
		tablaFallasReportadas.setWidth("100%");
		tablaFallasReportadas.setCellSpacing(3);
		tablaFallasReportadas.setCellPadding(1);

		tableHistoricoReparaciones.setWidth("100%");
		tableHistoricoReparaciones.setCellSpacing(5);
		tableHistoricoReparaciones.setCellPadding(3);
		tableHistoricoReparaciones.setBorderWidth(1);

		tableListaOrdenesPendientes.setWidth("100%");
		tableListaOrdenesPendientes.setCellSpacing(5);
		tableListaOrdenesPendientes.setCellPadding(3);
		tableListaOrdenesPendientes.setBorderWidth(1);

		this.btnGuardar.setWidth("100px");
		this.btnCancelar.setWidth("100px");
		this.btnAnular.setWidth("100px");

		this.panelVActivo_ListaActivo_4C.setSpacing(5);
		this.panelVActivo_TipoActivo_4C.setSpacing(5);

		btnVerHistorico.setSize("20px", "20px");
		imgBtnVerHistorico.setSize("20px", "20px");

	}

	private void setearvPanelPrincipal_1() {
		vPanelPrincipal_1.setSpacing(5);
	}

	//	AGREGAR WIDGETS================================================================================================
	private void agregarWidgets(Orden orden){

		agregarWidgetsvPanelPrincipal_1();							//vPanelPrincipal
		agregarWidgetsDatosClienteyCreadaPor_2A();			//A
		agregarWidgetsDatosClienteyPrioridadyTecnicoyEstado_2B();	//B
		agregarWidetPpanelHContacto_TipoTrabajo_Preventivo();
		agregarWidgetsActivoYDatosActivo_2C();						//C
		agregarWidgetsHPanelListaReparaciones_2D();					//D
		agregarWidgetsHBotonesAceptarYCancelar_2G();

	}

	private void agregarWidgetsvPanelPrincipal_1(){
		vPanelPrincipal_1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hpTituloCorrectivo.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		HorizontalPanel hpBotonesDescargas = new HorizontalPanel();
		hpBotonesDescargas.add(btnPDF);
		hpBotonesDescargas.add(btnPDFOp);
		hpBotonesDescargas.add(btnPermiso);

		iuCorrectivoTitulo = new IUCorrectivoTitulo(sesion, orden, IUCorrectivoEnum.INI);
		iUCorrectivoToolBar = new IUCorrectivoToolBar(sesion, orden, IUCorrectivoEnum.INI, hpBotonesDescargas);
		iuCorrectivoSeleccionarActivo = new IUCorrectivoSeleccionarActivoYFalla(sesion, orden, IUCorrectivoEnum.INI, this);


		vPanelPrincipal_1.add(iuCorrectivoTitulo.getPrincipalPanel());
		vPanelPrincipal_1.add(iUCorrectivoToolBar.getPrincipalPanel());

		this.vPanelPrincipal_1.add(panelVOrdenesPendientes_2F);
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSize("60px", "60px");

		HorizontalPanel hp2 = new HorizontalPanel();
		hp2.setSize("60px", "60px");

		this.vPanelPrincipal_1.add(this.panelVListaFallas_2D);
		this.vPanelPrincipal_1.add(hp);
		this.vPanelPrincipal_1.add(vpPendientes);
		this.vPanelPrincipal_1.add(hp2);
		this.vPanelPrincipal_1.add(this.panelHBotones);
		vPanelPrincipal_1.add(panelVHistorico_2G);

		vPanelPrincipal_1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal_1.add(this.panelHBotonesAceptarYCancelar_2G);

		HorizontalPanel esp = new HorizontalPanel();
		esp.setSize("30px", "30px");
		vPanelPrincipal_1.add(esp);

		vPanelPrincipal_1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		vPanelPrincipal_1.add(file);
	}

	public void agregarWidgetsDatosClienteyCreadaPor_2A() {

		if (orden.getNumeroParteDucsa() != null && !orden.getNumeroParteDucsa().equalsIgnoreCase("")) {
			String text = " Nro Ducsa " + String.valueOf(orden.getNumeroParteDucsa());
			iuCorrectivoTitulo.getLblSubTitulo().setText(iuCorrectivoTitulo.getLblSubTitulo().getText() + text);
		}

		btnPDF.setSize("25px", "25px");
		imgPDF.setSize("25px", "25px");

		btnPDFOp.setSize("25px", "25px");
		imgPDFOP.setSize("25px", "25px");

		btnPermiso.setSize("25px", "25px");
		imgPermiso.setSize("25px", "25px");

		btnPermiso.setTitle("Descargar permiso de trabajo");
		btnPDF.setTitle("Descargar reporte (via Bilpa)");
		btnPDFOp.setTitle("Descargar reporte (via Operador)");

		btnPermiso.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				ProyectoBilpa.greetingService.crearPDFPermiso(orden, new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al crear PDF");
						vpu.showPopUp();
					}

					public void onSuccess(String result) {
						if (result.equals("")) {
							ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al crear PDF");
							vpu.showPopUp();						
						} else {
							file.download();
						}
					}
				});
			}
		});

		btnPDF.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				crearPDF(true);
			}
		});

		btnPDFOp.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				crearPDF(false);
			}
		});
	}

	private void agregarWidetPpanelHContacto_TipoTrabajo_Preventivo()
	{
		panelHContacto_TipoTrabajo_Preventivo2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		if (orden.getContacto() != null)
		{
			lblContactoDato.setText(orden.getContacto().toString());			
		}
		else
		{
			lblContactoDato.setText("Sin Registrar");
		}

		if(orden.isPreventivo())
		{
			chPreventivo.setValue(true);
		}
		else
		{
			chPreventivo.setValue(false);
		}

		panelHContacto_TipoTrabajo_Preventivo.setBorderWidth(1);
		panelHContacto_TipoTrabajo_Preventivo.setWidth("100%");
		panelHContacto_TipoTrabajo_Preventivo.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		panelHContacto_TipoTrabajo_Preventivo2.setSpacing(5);
		panelHContacto_TipoTrabajo_Preventivo.setSpacing(5);

		lblPreventivo.setStyleName("Negrita");

		panelHContacto_TipoTrabajo_Preventivo2.add(lblContacto);		
		panelHContacto_TipoTrabajo_Preventivo2.add(lblContactoDato);

		UtilUI.agregarLabelEnBlanco(panelHContacto_TipoTrabajo_Preventivo2, 100);

		UtilUI.agregarLabelEnBlanco(panelHContacto_TipoTrabajo_Preventivo2, 100);

		panelHContacto_TipoTrabajo_Preventivo2.add(lblPreventivo);
		panelHContacto_TipoTrabajo_Preventivo2.add(chPreventivo);

		panelHContacto_TipoTrabajo_Preventivo.add(panelHContacto_TipoTrabajo_Preventivo2);
	}

	private void agregarWidgetsDatosClienteyPrioridadyTecnicoyEstado_2B() {

		panelHPrioridadyTecnicoyEstado_2B.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		panelHPrioridad_3B.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		panelHTecnico_3B.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		panelHEstado_3B.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		panelHPrioridadyTecnicoyEstado_2B.add(panelHPrioridad_3B);
		panelHPrioridadyTecnicoyEstado_2B.add(panelHTecnico_3B);
		panelHPrioridadyTecnicoyEstado_2B.add(panelHEstado_3B);

		this.panelHPrioridadyTecnicoyEstado_2B.setSpacing(5);

		panelHPrioridad_3B.add(this.lblPrioridad);
		panelHPrioridad_3B.setSpacing(5);
		panelHPrioridad_3B.add(this.listBoxPrioridades);	

		panelHTecnico_3B.add(this.lblTecnicoAsignado);
		panelHTecnico_3B.setSpacing(5);

		panelHEstado_3B.add(this.lblEstado);
		panelHEstado_3B.setSpacing(10);
	}

	//B
	private void agregarWidgetsActivoYDatosActivo_2C(){

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

		this.panelHActivoYDatosActivo_2C.add(this.panelHActivo_3C);
		this.panelHActivoYDatosActivo_2C.setSpacing(5);
		this.panelHActivoYDatosActivo_2C.add(this.panelVDatosActivo_3C);

		//Activos
		this.panelHActivo_3C.add(this.panelVActivo_TipoActivo_4C);
		this.panelVHistorico_2E.setSpacing(5);
		this.panelHActivo_3C.add(this.panelVActivo_ListaActivo_4C);

		//Tipo Activo
		this.panelVActivo_TipoActivo_4C.add(this.lblTipoActivo);
		this.panelVActivo_TipoActivo_4C.add(this.listBoxTipoActivo);

		//Lista Activo
		this.panelVActivo_ListaActivo_4C.add(this.lblListaActivo);
		this.panelVActivo_ListaActivo_4C.add(this.listBoxListaDeActivos);	

		//DatosActivos

		//Tabla para Datos de los Surtidores================================================================================================
		this.tableDatosSurtidor.setWidget(0, 0, lblNumeroDeSerie);
		this.tableDatosSurtidor.setWidget(0, 2, lblModeloSurtidor);
		this.tableDatosTanque.setWidget(0, 0, lblidT);
		this.tableDatosTanque.setWidget(0, 2, lblDescT);
		this.tableDatosTanque.setWidget(1, 0, lblTipoCombustibleT);
		this.tableDatosTanque.setWidget(1, 2, lblCapT);		

		this.tableDatosCanio.setWidget(0, 0, lblidC);
		this.tableDatosCanio.setWidget(0, 2, lblDescC);

		this.tableDatosBomba.setWidget(0, 0, lblidB);
		this.tableDatosBomba.setWidget(0, 2, lblDescB);

		panelVDatosActivosSeleccionado_4C.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		this.panelVDatosActivosSeleccionado_4C.add(this.tableDatosSurtidor);			//Por defecto se agreaga la tabla de los surtidores

		this.panelVDatosActivo_3C.add(this.panelVDatosActivosSeleccionado_4C);
		this.panelVDatosActivo_3C.setSpacing(5);

		FlexTable tableFallas = new FlexTable();
		this.panelVDatosActivo_3C.add(tableFallas);

		tableFallas.setCellPadding(5);
		tableFallas.setCellSpacing(5);
		tableFallas.setWidget(0, 0, lblFallaReportadaCliente);
		tableFallas.setWidget(0, 1, iuWidgetFalla.listBoxTiposFallasR);
		tableFallas.setWidget(1, 0, lblFallaReportadaCliente2);
		tableFallas.setWidget(1, 1, iuWidgetFalla.listBoxFallasR);
		tableFallas.setWidget(1, 2, panelVDatosFallaActivo_5C_2);

		iuWidgetFalla.cargarLtBoxTipoFallaR(popUp, iuWidgetFalla.listBoxTiposFallasR);

		//FALLA 2================================================================================================
		this.btnAgregarFallaReportada.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				int ind = iuWidgetFalla.listBoxFallasR.getSelectedIndex();
				if (ind >=0 && validarIdFallaRep(iuWidgetFalla.listBoxFallasR.getValue(ind)))
				{
					int idFallaRep = Integer.valueOf(iuWidgetFalla.listBoxFallasR.getValue(ind));
					buscarFallaRep(idFallaRep);					
				}
			}
			private boolean validarIdFallaRep(String idFallaRep) {
				try
				{
					if ( Integer.valueOf(idFallaRep) > -1)
					{
						return true;
					}
					else
					{
						ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe seleccionar una falla reportada");
						vpu.showPopUp();
						return false;
					}
				}
				catch(NumberFormatException e)
				{
					ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe seleccionar una falla reportada");
					vpu.showPopUp();
					return false;
				}
			}
		});
		this.panelVDatosFallaActivo_5C_2.add(btnAgregarFallaReportada);
	}

	//D
	private void agregarWidgetsHPanelListaReparaciones_2D(){
		this.panelVListaFallas_2D.setSpacing(5);
		this.panelVListaFallas_2D.add(this.lblTituloListaFallas);
		this.panelVListaFallas_2D.setSpacing(3);
		this.panelVListaFallas_2D.add(this.tablaFallasReportadas);

		panelVListaFallas_2D.setSpacing(5);
		panelVListaFallas_2D.add(lblTituloListaFallas);
		panelVListaFallas_2D.setSpacing(3);

		HorizontalPanel hp = new HorizontalPanel();
		hp.add(tablaFallasReportadas);
		hp.add(iuCorrectivoSeleccionarActivo.getBuscarActivo());
		panelVListaFallas_2D.add(hp);

	}

	//	CARGAR WIDGETS================================================================================================
	private void cargarWidgets(){	
		cargarDatosActivos();
		cargarTipoActivo();
		agregarFilaTituloListaFallaReclamadas();

		cargarHistoricoDeEmpresa(this.estacion);		//

		obtenerPendientes();

		agregarFilaTituloListaHistoricoReparaciones();

		cargarOrdenesPendientes(this.estacion);		//
		agregarFilaTituloListaOrdenesPendientes();
		obtenerOrdenesActivas();
		agregarWidgetsVerHistorico();
	}



	private void agregarWidgetsHBotonesAceptarYCancelar_2G() {
		this.btnCancelar.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				IUListaOrdenes iu = new IUListaOrdenes(sesion);
				IUMenuPrincipal.getInstancia().agregarWidgetAlMenu(iu.getPrincipalPanel());	
			}
		});

		this.btnGuardar.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				setearOrden();
			}
		});

		btnAnular.setStyleName("buttonSecondary");
		btnAnular.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				anularOrden();
			}
		});

		HorizontalPanel esp = new HorizontalPanel();
		esp.setWidth("10");

		HorizontalPanel esp2 = new HorizontalPanel();
		esp2.setWidth("10");

		this.panelHBotonesAceptarYCancelar_2G.add(btnAnular);
		this.panelHBotonesAceptarYCancelar_2G.add(esp);
		this.panelHBotonesAceptarYCancelar_2G.add(btnCancelar);
		this.panelHBotonesAceptarYCancelar_2G.add(esp2);
		this.panelHBotonesAceptarYCancelar_2G.add(btnGuardar);


	}

	private void cambiarTipoActivo(){
		tipoActivo = Integer.valueOf(listBoxTipoActivo.getValue(listBoxTipoActivo.getSelectedIndex()));
		obtenerActivosPorTipo(tipoActivo);
		cambiarPanelDatosActivo();
	}

	//Va a la base a buscar el activo, y setea el atributo activo de clase
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
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Es null");
					vpu.showPopUp();
				}
			}
		});
	}	

	private FallaReportada buscarFallaRep(int id){
		return null;
	}

	private void obtenerPendientes(){
		final IUSeguimientoInicial instance = this;

		ProyectoBilpa.greetingService.obtenerPendientesDeEstacion(orden.getEmpresa().getId(), new AsyncCallback<PendienteDataUI>(){
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar pendientes");
				vpu.showPopUp();
			}

			public void onSuccess(PendienteDataUI result) {
				reloadTituloPendientesAReparar(result);
				cargarReparacionesEnTabla();
			}
		});
	}

	private void cargarReparacionesEnTabla(){
		ProyectoBilpa.greetingService.obtenerTodosLasReparaciones(orden, new AsyncCallback<ArrayList<Reparacion>>() {
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener las reparaciones de la orden");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<Reparacion> result) {
				for(Reparacion r : result){
					setearFallaATabla(r, r.getActivo(), r.getFallaReportada(), r.getComentario());
					// setearReparacionATabla(r);						
				}
			}
		});
	}

	public void agregarNuevaFallaAListaDeFallas(List<FallaReportada> fallaReps, List<Activo> activos, List<String> comentarios, Pendiente pendiente)
	{
		for (int i = 0; i < fallaReps.size(); i++) {
			Activo activo = activos.get(i);
			FallaReportada fallaRep = fallaReps.get(0);
			String comentario = comentarios.size() > 0 ? comentarios.get(i) : "";
			Reparacion rep = Reparacion.crearReparacion(activo);
			rep.setPendiente(pendiente);
			rep.setFallaReportada(fallaRep);
			rep.setOrden(this.orden);


			rep.setComentario(comentario);
			rep.setActivo(activo);

			orden.agregarReparacion(rep);
			setearFallaATabla(rep, activo, fallaRep, comentario);
			agregarFallaAOrden(activo, fallaRep);				
		}
	}

	private void agregarFallaAOrden(Activo activo, FallaReportada fallaRep){
		fallaRep.setActivo(activo);		
	}

	private void mostrarDatosActivoSeleccionado(){

		if(this.activoSeleccionado.getTipo()==1){//Surtidor
			Surtidor surtidor = (Surtidor)this.activoSeleccionado;
			mostrarDatosSurtidorSeleccionado(surtidor);			
		}else{
			this.panelVDatosActivosSeleccionado_4C.remove(0);
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
	private void mostrarDatosSurtidorSeleccionado(Surtidor surtidor){

		tableDatosSurtidor.setText(0, 3, "");

		tableDatosSurtidor.setWidget(0, 0, lblNumeroDeSerie);
		tableDatosSurtidor.setText(0, 1, surtidor.getNumeroSerie());
		tableDatosSurtidor.setWidget(0, 2, lblModeloSurtidor);
		tableDatosSurtidor.setText(0, 3, surtidor.getModelo().toString());

		garantiaUtil = new GarantiaUtil(surtidor, tableDatosSurtidor, 0, 4, OPERACION_GARANTIA.PANTALLA_ORDEN);
	}

	private void mostrarDatosTanqueSeleccionado(Tanque tanque){
		this.tableDatosTanque.setWidget(0, 0, lblidT);
		this.tableDatosTanque.setText(0, 1, String.valueOf(tanque.getId()));
		this.tableDatosTanque.setWidget(0, 2, lblDescT);
		this.tableDatosTanque.setText(0, 3, tanque.getDescripcion());
		this.tableDatosTanque.setWidget(1, 0, lblTipoCombustibleT);
		this.tableDatosTanque.setText(1, 1, tanque.getProducto().getNombre());
		this.tableDatosTanque.setWidget(1, 2, lblCapT);
		this.tableDatosTanque.setText(1, 3, String.valueOf(tanque.getCapacidad()) + " lts");

		this.panelVDatosActivosSeleccionado_4C.add(tableDatosTanque);

		garantiaUtil = new GarantiaUtil(tanque, tableDatosTanque, 1, 4, OPERACION_GARANTIA.PANTALLA_ORDEN);
	}


	private void mostrarDatosCanioSeleccionado(Canio canio) {
		this.tableDatosCanio.setWidget(0, 0, lblidC);
		this.tableDatosCanio.setText(0, 1, String.valueOf(canio.getId()));
		this.tableDatosCanio.setWidget(0, 2, lblDescC);
		this.tableDatosCanio.setText(0, 3, canio.getDescripcion());

		this.panelVDatosActivosSeleccionado_4C.add(tableDatosCanio);

	}

	private void mostrarDatosBombaSeleccionada(BombaSumergible bomba) {
		this.tableDatosBomba.setWidget(0, 0, lblidB);
		this.tableDatosBomba.setText(0, 1, String.valueOf(bomba.getId()));
		this.tableDatosBomba.setWidget(0, 2, lblDescB);
		this.tableDatosBomba.setText(0, 3, bomba.getDescripcion());

		this.panelVDatosActivosSeleccionado_4C.add(tableDatosBomba);
		garantiaUtil = new GarantiaUtil(bomba, tableDatosBomba, 0, 4, OPERACION_GARANTIA.PANTALLA_ORDEN);

	}

	private void cambiarPanelDatosActivo(){

		this.panelVDatosActivosSeleccionado_4C.remove(0);
		this.activoSeleccionado=null;
		if(this.tipoActivo==1){
			this.tableDatosSurtidor.setText(0, 1,"");
			this.tableDatosSurtidor.setText(0, 3,"");
			//			this.listBoxPicosDeSurtidor.clear();
			this.tableDatosSurtidor.setText(1, 3,"");
			this.panelVDatosActivosSeleccionado_4C.add(tableDatosSurtidor);
		}
		if(this.tipoActivo==2){
			this.panelVDatosActivosSeleccionado_4C.add(tableDatosTanque);
			this.tableDatosTanque.setText(0, 1, "");
			this.tableDatosTanque.setText(0, 3, "");
			this.tableDatosTanque.setText(1, 1, "");
			this.tableDatosTanque.setText(1, 3, "");
		}
		if(this.tipoActivo==3){
			this.panelVDatosActivosSeleccionado_4C.add(tableDatosCanio);
			this.tableDatosCanio.setText(0, 1, "");
			this.tableDatosCanio.setText(0, 3, "");
		}
		if(this.tipoActivo==4){
			this.panelVDatosActivosSeleccionado_4C.add(tableDatosBomba);
			this.tableDatosBomba.setText(0, 1, "");
			this.tableDatosBomba.setText(0, 3, "");
		}
	}

	private void cargarTipoActivo(){
		ProyectoBilpa.greetingService.obtenerTiposDeActivos(this.estacion.getId(), true, new AsyncCallback<ArrayList<String>>() {
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
	private void cargarDatosActivos(){
		//Por defecto se cargan los surtidores
		int tipoActivo = 1;						//Se invoca a obtenerAcitvosPorTipo para traer los surtidores al cargar la pagina.
		obtenerActivosPorTipo(tipoActivo);
	}

	private void agregarFilaTituloListaFallaReclamadas() {		
		//Setea los nombres de las columnas de la tabla		
		HTML activoHTML = new HTML("Activo");
		activoHTML.setWidth("378");
		HTML fallaHTML = new HTML("Descripción de falla reportada");
		fallaHTML.setWidth("378");
		HTML comentarioHTML = new HTML("Comentario");
		comentarioHTML.setWidth("378");
		tablaFallasReportadas.getRowFormatter().setStyleName(0,"CabezalTabla");
		tablaFallasReportadas.setWidget(0, 0, activoHTML);
		tablaFallasReportadas.setWidget(0, 1, fallaHTML);		
		tablaFallasReportadas.setWidget(0, 2, comentarioHTML);
	}



	private void cargarOrdenesPendientes(Estacion empresa){

		ProyectoBilpa.greetingService.buscarOrdenesPendientes(empresa, new AsyncCallback<ArrayList<Orden>>() {


			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar ordenes pendientes: " + caught.getMessage());
				vpu.showPopUp();
			}


			public void onSuccess(ArrayList<Orden> result) {
				if (result==null){
					vPanelPrincipal_1.remove(panelVOrdenesPendientes_2F);					
				}							
			}
		});		
	}

	private void agregarFilaTituloListaHistoricoReparaciones() {		
		//Setea los nombres de las columnas de la tabla
		HTML fechaHTML = new HTML("Activo");
		fechaHTML.setWidth("280");
		HTML activoHTML = new HTML("Fecha");
		activoHTML.setWidth("280");
		HTML fallaHTML = new HTML("Falla Tecnica");
		fallaHTML.setWidth("280");
		HTML solucionHTML = new HTML("Reparación");
		solucionHTML.setWidth("280");
		tableHistoricoReparaciones.getRowFormatter().setStyleName(0,"CabezalTabla");
		tableHistoricoReparaciones.setWidget(0, 0, fechaHTML);
		tableHistoricoReparaciones.setWidget(0, 1, activoHTML);
		tableHistoricoReparaciones.setWidget(0, 2, fallaHTML);
		tableHistoricoReparaciones.setWidget(0, 3, solucionHTML);
	}
	private void agregarFilaTituloListaOrdenesPendientes() {	
		HTML fechaHTML = new HTML("Fecha");
		fechaHTML.setWidth("378");
		HTML activoHTML = new HTML("Activo");
		activoHTML.setWidth("378");
		HTML fallaHTML = new HTML("Falla Tecnica");
		fallaHTML.setWidth("378");

		//Setea los nombres de las columnas de la tabla
		tableListaOrdenesPendientes.getRowFormatter().setStyleName(0,"CabezalTabla");
		tableListaOrdenesPendientes.setWidget(0, 0, fechaHTML);
		tableListaOrdenesPendientes.setWidget(0, 1, activoHTML);
		tableListaOrdenesPendientes.setWidget(0, 2, fallaHTML);

	}

	private void setearFallaATabla(Reparacion rep, Activo activo, FallaReportada fallaRep, String comentario){
		int numRows = tablaFallasReportadas.getRowCount();
		TextBox txComentario = new TextBox();
		txComentario.setText(comentario);
		txComentario.setWidth("377");

		Image imbRemove = new Image("img/menos.png");
		final PushButtonRow btnRemove = new PushButtonRow(imbRemove);
		btnRemove.setSize("15px", "15px");
		imbRemove.setSize("15px", "15px");
		btnRemove.setRow(numRows);

		btnRemove.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				quitarReparacion(btnRemove);
			}
		});

		tablaFallasReportadas.setWidget(numRows, 1, new Label(fallaRep.toString()));
		tablaFallasReportadas.setWidget(numRows, 2, txComentario);
		tablaFallasReportadas.setWidget(numRows, 3, btnRemove);

		mapButtonsRep.put(btnRemove, rep);

		tablaFallasReportadas.setWidget(numRows, 0, new Label (activo.toString()));		
		if(numRows %2==0){
			tablaFallasReportadas.getRowFormatter().addStyleName(numRows, "FilaTabla1");				
		}else{
			tablaFallasReportadas.getRowFormatter().addStyleName(numRows, "FilaTabla2");
		}
	}

	private void quitarReparacion(PushButtonRow button){
		if (orden.getReparaciones().size()>1){
			Reparacion rep = mapButtonsRep.get(button);
			orden.quitarReparacion(rep);

			tablaFallasReportadas.removeRow(button.getRow());
			mapButtonsRep.remove(rep);

			if (rep.getPendiente() != null){
				iuRepararPendienteTitulo.getPendienteDataUI().getPendientes().add(rep.getPendiente().getPendienteData());
				reloadTituloPendientesAReparar(iuRepararPendienteTitulo.getPendienteDataUI());
			}
		} else {
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debes agregar otra falla antes de eliminar esta.");
			vpu.showPopUp();
		}
	}

	private void setearTecnicoAsignado() {
		String tecnicoAsignado = this.iUCorrectivoToolBar.getListBoxTecnicoAsignado().getItemText(this.iUCorrectivoToolBar.getListBoxTecnicoAsignado().getSelectedIndex());
		if(!tecnicoAsignado.equals(UtilOrden.personaAsignadaOrden(1))){  	//Si el tecnico asignado No es "Sin asignar"
			orden.setEstadoOrden(2);			
			int id = Integer.valueOf(this.iUCorrectivoToolBar.getListBoxTecnicoAsignado().getValue(this.iUCorrectivoToolBar.getListBoxTecnicoAsignado().getSelectedIndex()));
			ProyectoBilpa.greetingService.buscarUsuario(id, new AsyncCallback<Persona>(){


				public void onFailure(Throwable caught) {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar el tecnico");
					vpu.showPopUp();
				}

				public void onSuccess(Persona result) {					
					orden.setTecnicoAsignado(result);
					guardarOrden();
				}				
			});		
		}else{
			glass.show();
			dialogoConfirmarSinAsignacion();
		}
	}

	private void setearPrioridad() {
		orden.setPrioridad("Normal");
	}

	private void cargarComentariosDeLaTabla() {
		Reparacion rep=null;
		for (int row=1;row<this.tablaFallasReportadas.getRowCount();row++){
			for (int col=0;col<this.tablaFallasReportadas.getCellCount(row);col++){
				if(col==0){
					Label lblActivo = (Label)this.tablaFallasReportadas.getWidget(row, 0);
					Label lblFallaReportada = (Label)this.tablaFallasReportadas.getWidget(row, 1);
					String toStringActivo = lblActivo.getText();
					String toStringFalla = lblFallaReportada.getText();
					rep = orden.obtenerReparacion(toStringActivo, toStringFalla);				
				}

				if(rep!=null){
					if(col==2){
						Widget w = this.tablaFallasReportadas.getWidget(row, col);
						if(w.getClass().equals(TextBox.class)){
							TextBox TxComentario = (TextBox)this.tablaFallasReportadas.getWidget(row,col);
							String comentario = TxComentario.getText();					
							rep.setComentario(comentario);							
						}
					}
				}
			}
		}
	}

	private boolean validarCampos() {
		return iUCorrectivoToolBar.validarCumplimiento();
	}

	private void setearOrden(){
		if (validarCampos()){
			if (orden.getReparaciones().size() > 0 && orden.getEstadoOrden() == 6){//si es iniciada ducsa y se agrego una reparcion, se pone sin asignar
				orden.setEstadoOrden(2);
			}
			cargarComentariosDeLaTabla();
			setearPrioridad();
			setearTecnicoAsignado();				
			setearTipoTrabajo();
			iUCorrectivoToolBar.setearCumplimiento();
			setearPreventivo();
		}
	}

	private void guardarOrden() {
		popUp.show();
		String estado = "seguimientoInicial";
		ProyectoBilpa.greetingService.actualizarOrden(orden,estado, sesion, new AsyncCallback<Boolean>() {

			public void onSuccess(Boolean result) {
				if (!result){
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar la orden");
					vpu.showPopUp();
				}else{

					if(sesion.getRol()==1 || sesion.getRol()==5){
						IUListaOrdenes	iuLO = new IUListaOrdenes(sesion);
						IUMenuPrincipal.getInstancia().agregarWidgetAlMenu(iuLO.getPrincipalPanel());

					}
					if(sesion.getRol()==2){
						IUListaOrdenes	iuLO = new IUListaOrdenes(sesion);
						IUMenuPrincipalAdministrativo.getInstancia().agregarWidgetAlMenu(iuLO.getPrincipalPanel());
					}
				}
				popUp.hide();

			}
			public void onFailure(Throwable caught) {
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar la orden");
				vpu.showPopUp();
			}
		});
	}	

	private void anularOrden(){
		this.orden.setAnulador(this.sesion);
		this.dialogoComentarioAnulada();
	}

	private void anularOrdenBase() {
		ProyectoBilpa.greetingService.guardarOrdenAnulada(orden, sesion, new AsyncCallback<Boolean>() {

			public void onSuccess(Boolean result) {
				if (!result){
					UtilUI.dialogBoxError("Error al anular la orden");

				}else{
					if(sesion.getRol()==2){
						IUListaOrdenes	iuLO = new IUListaOrdenes(sesion);
						IUMenuPrincipalAdministrativo.getInstancia().agregarWidgetAlMenu(iuLO.getPrincipalPanel());						
					}

					if(sesion.getRol()==1 || sesion.getRol()==5){
						IUListaOrdenes	iuLO = new IUListaOrdenes(sesion);
						IUMenuPrincipal.getInstancia().agregarWidgetAlMenu(iuLO.getPrincipalPanel());
					}

				}

			}
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar la orden");
				vpu.showPopUp();
			}
		});
	}

	public DialogBox dialogoComentarioAnulada(){
		glass.show();
		final HorizontalPanel hPanelDialModif = new HorizontalPanel();
		final HorizontalPanel hPanelDialModif2 = new HorizontalPanel();
		final VerticalPanel vPanelDailModif = new VerticalPanel();
		final TextBox tDialModifRep = new TextBox();
		final Label lblTitulo = new Label("Ingrese un motivo para anular el correctivo");
		lblTitulo.setStyleName("Negrita");
		final DialogBox dialogAnulada = new DialogBox();
		Button btnAceptarModif = new Button("OK",new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(!tDialModifRep.getText().equalsIgnoreCase("")){
					orden.setComentarioAnulada(tDialModifRep.getText());
					anularOrdenBase();
					dialogAnulada.hide(true);
					glass.hide();
				}
			}
		});

		Button btnCancelModif = new Button("Cancelar", new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogAnulada.hide(true);
				glass.hide();
			}
		});

		dialogAnulada.setAutoHideEnabled(false);
		vPanelDailModif.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		btnAceptarModif.setWidth("100px");
		tDialModifRep.setWidth("500px");
		btnCancelModif.setWidth("100px");
		tDialModifRep.setText("");

		vPanelDailModif.add(lblTitulo);
		hPanelDialModif.add(tDialModifRep);

		hPanelDialModif2.add(btnCancelModif);
		hPanelDialModif2.setSpacing(5);
		hPanelDialModif2.add(btnAceptarModif);

		vPanelDailModif.add(hPanelDialModif);
		vPanelDailModif.add(hPanelDialModif2);

		dialogAnulada.setWidget(vPanelDailModif);

		dialogAnulada.show();
		dialogAnulada.center();
		return dialogAnulada;
	}

	public DialogBox dialogoConfirmarSinAsignacion(){

		final HorizontalPanel hPanelDial1 = new HorizontalPanel();
		final HorizontalPanel hPanelDial2 = new HorizontalPanel();
		final VerticalPanel vPanelDailModif = new VerticalPanel();
		final DialogBox dialogConfirmar = new DialogBox();
		final Label lblTexto = new Label("Desea dejar el correctivo sin asignar?");

		Button btnSi = new Button("Si", new ClickHandler() {
			public void onClick(ClickEvent event) {
				orden.setTecnicoAsignado(null);
				guardarOrden();

				dialogConfirmar.hide(true);	
				glass.hide();
			}
		});

		Button btnNo = new Button("No",	new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogConfirmar.hide(true);
				glass.hide();
			}
		});

		vPanelDailModif.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		btnSi.setWidth("100px");
		btnNo.setWidth("100px");
		lblTexto.setStyleName("Negrita");

		hPanelDial1.setSpacing(15);

		hPanelDial1.add(lblTexto);
		hPanelDial2.add(btnNo);
		hPanelDial2.setSpacing(5);
		hPanelDial2.add(btnSi);

		vPanelDailModif.add(hPanelDial1);
		vPanelDailModif.add(hPanelDial2);

		dialogConfirmar.setWidget(vPanelDailModif);

		dialogConfirmar.show();
		dialogConfirmar.center();

		return dialogConfirmar;
	}

	private void cargarTablaOrdenesPendientes(ArrayList<DatoOrdenesActivasEmpresa> result){
		if(result.size()>1){

			VerticalPanel vpGrande = new VerticalPanel();
			Label lblOrdenesPendientesTitulo = new Label("Lista de órdenes pendientes");
			lblOrdenesPendientesTitulo.setStyleName("SubTitulo");
			vpGrande.setSpacing(3);

			FlexTable tablePendientes = new FlexTable();
			tablePendientes.setWidth("1150px");
			tablePendientes.setCellSpacing(2);
			agregarFilaTituloPendientes(tablePendientes);


			int i = 1;
			for (DatoOrdenesActivasEmpresa s : result) {
				String numero = orden.getNumero() + "";
				if(!numero.equals(s.getNumero())){
					tablePendientes.setText(i, 0, s.getFecha());
					tablePendientes.setText(i, 1, s.getNumero());
					tablePendientes.setText(i, 2, s.getAutor());
					tablePendientes.setText(i, 3, s.getTecnico());
					tablePendientes.setText(i, 4, s.getEstado());
				}

				i++;
				UtilCss.aplicarEstiloATabla(tablePendientes, i);
			}

			vpGrande.add(lblOrdenesPendientesTitulo);
			vpGrande.add(tablePendientes);		
			vPanelPrincipal_1.add(vpGrande);	
			vPanelPrincipal_1.add(panelHVerHistorico);
		}		
	}

	private void agregarFilaTituloPendientes(FlexTable ft) {		
		//Setea los nombres de las columnas de la tabla		

		HTML fallaHTML = new HTML("Fecha inicio");
		fallaHTML.setWidth("162");
		HTML tareaRealizadaHTML = new HTML("Número correctivo");
		tareaRealizadaHTML.setWidth("162");
		HTML telefonica = new HTML("Autor");
		telefonica.setWidth("151");
		HTML tecnico = new HTML("Técnico asignado");
		tecnico.setWidth("151");
		HTML estado = new HTML("Estado");
		estado.setWidth("151");

		ft.getRowFormatter().setStyleName(0, "CabezalTabla");
		ft.setWidget(0, 0, fallaHTML);
		ft.setWidget(0, 1, tareaRealizadaHTML);		
		ft.setWidget(0, 2, telefonica);
		ft.setWidget(0, 3, tecnico);		
		ft.setWidget(0, 4, estado);
	}

	private void obtenerOrdenesActivas(){
		ProyectoBilpa.greetingService.ordenesActivasDeUnaEmpresa(estacion, new AsyncCallback<ArrayList<DatoOrdenesActivasEmpresa>>() {

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar las ordenes activas");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<DatoOrdenesActivasEmpresa> result) {
				cargarTablaOrdenesPendientes(result);				
			}
		});		
	}

	//__________________________________________________HISTORICO________________________________________________
	private void cargarHistoricoDeEmpresa(Estacion empresa){

		ProyectoBilpa.greetingService.historicoOrdenesFinalizadasEmpresa(empresa, new AsyncCallback<ArrayList<DatoConsultaHistoricoOrdenes>>() {


			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar el historico de la orden");
				vpu.showPopUp();
			}


			public void onSuccess(ArrayList<DatoConsultaHistoricoOrdenes> result) {
				if (result==null){
					vPanelPrincipal_1.remove(panelVHistorico_2G);					
				}else{
					DatoConsultaHistoricoOrdenes auxiliar = new DatoConsultaHistoricoOrdenes();
					for (int i=0; i<result.size();i++){
						auxiliar = (DatoConsultaHistoricoOrdenes) result.get(i);
						historicoOrdenes.add(auxiliar);
					}
					if(result.size()>0){
						cargarTablaHistoricoOrdenes();

					}
				}							
			}

		});		

	}

	private void cargarTablaHistoricoOrdenes(){
		vpHistorico.setSpacing(10);

		Label lblTituloHisto = new Label("Histórico de órdenes de la estación");
		lblTituloHisto.setStyleName("SubTitulo");

		vpHistorico.add(lblTituloHisto);
		for (DatoConsultaHistoricoOrdenes s : historicoOrdenes) {
			Date fecha = s.getFecha();
			String formattedValue = DateTimeFormat.getFormat("EEEE d MMMM yyyy k:mm").format(fecha);

			//LABEL TITULO
			Label nroOrden = new Label(" Correctivo: ");
			Label nroOrdenValor = new Label(s.getNro());
			Label finalizada = new Label(" - Finalizada: ");
			Label finalizadaValor = new Label(formattedValue);
			HorizontalPanel hpTitulo = new HorizontalPanel();

			hpTitulo.add(nroOrden);
			hpTitulo.add(nroOrdenValor);
			hpTitulo.add(finalizada);
			hpTitulo.add(finalizadaValor);

			nroOrden.setStyleName("Negrita");
			finalizada.setStyleName("Negrita");

			vpHistorico.add(hpTitulo);
			//LABEL TITULO

			FlexTable tableSoluciones = new FlexTable();
			tableSoluciones.setWidth("1150px");
			tableSoluciones.setCellSpacing(3);
			tableSoluciones.setCellPadding(1);
			agregarTituloOrdenesHistorico(tableSoluciones);
			int i = 1;
			for (Reparacion r : s.getReparaciones()) {
				for (Solucion sol : r.getSoluciones()) {
					tableSoluciones.setText(i, 0, r.getActivo().toString());
					tableSoluciones.setText(i, 1,formattedValue );
					tableSoluciones.setText(i, 2, sol.getFallaTecnica().getDescripcion());
					tableSoluciones.setText(i, 3, sol.getTarea().getDescripcion());
					i++;

					if(i %2==0){
						tableSoluciones.getRowFormatter().addStyleName(i-1, "FilaTabla1");				
					}else{
						tableSoluciones.getRowFormatter().addStyleName(i-1, "FilaTabla2");
					}
				}
			}
			vpHistorico.add(tableSoluciones);
		}

		btnVerHistorico.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				if(btnHistoricoApretado == true){
					btnHistoricoApretado = false;
				}else{
					btnHistoricoApretado = true;
				}
				manejarHistorico();
			}

			private void manejarHistorico() {
				if (btnHistoricoApretado == true){
					vPanelPrincipal_1.add(vpHistorico);
				}else{
					vPanelPrincipal_1.remove(vpHistorico);
				}
			}			
		});



	}

	private void agregarWidgetsVerHistorico()
	{
		panelHVerHistorico.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		lblVerHistorico.setStyleName("Negrita");
		panelHVerHistorico.setSpacing(5);
		panelHVerHistorico.add(lblVerHistorico);
		panelHVerHistorico.add(btnVerHistorico);

	}

	private void agregarTituloOrdenesHistorico(FlexTable ft){
		//Setea los nombres de las columnas de la tabla	

		HTML activoHTML = new HTML("Fecha");
		activoHTML.setWidth("280");
		HTML fechaHTML = new HTML("Activo");
		fechaHTML.setWidth("280");
		HTML fallaHTML = new HTML("Falla detectada");
		fallaHTML.setWidth("280");
		HTML tareaHTML = new HTML("Tarea realizada");
		tareaHTML.setWidth("280");

		ft.getRowFormatter().setStyleName(0, "CabezalTabla");
		ft.setWidget(0, 0, fechaHTML);
		ft.setWidget(0, 1, activoHTML);
		ft.setWidget(0, 2, fallaHTML);
		ft.setWidget(0, 3, tareaHTML);
	}

	private void crearPDF(boolean pdfBilpa) {
		ProyectoBilpa.greetingService.crearPDF(orden.getNumero(), pdfBilpa, new AsyncCallback<String>() {

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al crear PDF");
				vpu.showPopUp();
			}

			public void onSuccess(String result) {
				if (result.equals(""))
				{
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al crear PDF");
					vpu.showPopUp();						
				}
				else
				{
					//abrirPDF(result);
					file.download();
				}
			}
		});			
	}

	private void setearPreventivo() {
		boolean esPreventivo = chPreventivo.getValue();
		orden.setPreventivo(esPreventivo);
	}

	private void setearTipoTrabajo() 
	{
		int idTipoTrabajo = Integer.valueOf(this.iUCorrectivoToolBar.getListBoxTipoDeTrabajo().getValue(this.iUCorrectivoToolBar.getListBoxTipoDeTrabajo().getSelectedIndex()));
		orden.setTipoTrabajo(buscarTipoTrabajo(idTipoTrabajo));		
	}

	private TipoTrabajo buscarTipoTrabajo(int idTipoTrabajo) {
		for(TipoTrabajo tt : iUCorrectivoToolBar.getListaTipoTrabajo())
		{
			if(tt.getId() == idTipoTrabajo)
				return tt;
		}
		return null;
	}

	@Override
	public void reloadTituloPendientesAReparar(PendienteDataUI pendienteDataUI) {
		if (iuRepararPendienteTitulo != null){
			vpPendientes.remove(iuRepararPendienteTitulo.getPrincipalPanel());
		}

		iuRepararPendienteTitulo = new IURepararPendienteTitulo(sesion, this);
		iuRepararPendienteTitulo.setPendienteDataUI(pendienteDataUI);
		vpPendientes.add(iuRepararPendienteTitulo.getPrincipalPanel());

	}
}


