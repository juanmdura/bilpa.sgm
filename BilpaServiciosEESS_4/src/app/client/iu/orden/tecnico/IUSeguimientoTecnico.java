package app.client.iu.orden.tecnico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
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
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import app.client.ProyectoBilpa;
import app.client.dominio.Activo;
import app.client.dominio.Contador;
import app.client.dominio.Estacion;
import app.client.dominio.IUCorrectivo.IUCorrectivoEnum;
import app.client.dominio.Orden;
import app.client.dominio.Persona;
import app.client.dominio.Reparacion;
import app.client.dominio.RepuestoLinea;
import app.client.dominio.Solucion;
import app.client.dominio.TipoTrabajo;
import app.client.dominio.UtilContador;
import app.client.dominio.data.DatoConsultaHistoricoOrdenes;
import app.client.dominio.data.DatoOrdenesActivasEmpresa;
import app.client.iu.correctivo.IUCorrectivoTitulo;
import app.client.iu.correctivo.IUCorrectivoToolBar;
import app.client.iu.menu.IUMenuPrincipal;
import app.client.iu.menu.IUMenuPrincipalTecnico;
import app.client.iu.orden.IUListaOrdenes;
import app.client.iu.orden.tecnico.dialogoAgregarSolucion.IUWidgetSolucion;
import app.client.iu.orden.tecnico.dialogoAgregarSolucion.IUWidgetSolucionOA;
import app.client.iu.orden.tecnico.tablasReparacion.IUWidgetTablaReparacion;
import app.client.iu.repuesto.IUWidgetRepuesto;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.Constants;
import app.client.utilidades.UtilCss;
import app.client.utilidades.UtilUI;
import app.client.utilidades.utilObjects.ComboFinService;
import app.client.utilidades.utilObjects.DialogoGestionActivos;
import app.client.utilidades.utilObjects.FileDownload;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;

public class IUSeguimientoTecnico extends ScrollPanel{

	FileDownload file = new FileDownload();

	private GlassPopup glass = new GlassPopup();
	private IUWidgetRepuesto iuWidgetRepuesto = new IUWidgetRepuesto();
	private  ListBox listBoxActivosRepuestos = new ListBox();
	private  ListBox listBoxCantidad = new ListBox();

	private VerticalPanel vPanelPrincipal_1 = new VerticalPanel();

	private HorizontalPanel panelHTitulOrden = new HorizontalPanel();
	private HorizontalPanel panelHDatosClienteyCreadaPor_2A = new HorizontalPanel();
	protected HorizontalPanel panelHPrioridadyTecnicoyEstado_2B = new HorizontalPanel();

	private VerticalPanel panelVFallaRepYAgregarActivo2D = new VerticalPanel();
	private HorizontalPanel panelHFallaRepYAgregarActivo2D = new HorizontalPanel();
	private VerticalPanel panelVFallDetectada3D = new VerticalPanel();
	private HorizontalPanel panelHBotonAgregarActivo3D = new HorizontalPanel();

	private VerticalPanel panelVListaFallas_2F = new VerticalPanel();
	private VerticalPanel panelVListaFallas_2FII = new VerticalPanel();

	private VerticalPanel panelVDatosTecnicos = new VerticalPanel();
	private HorizontalPanel panelHRepuestos_2I = new HorizontalPanel();
	private VerticalPanel panelVRepInteriorConHistorico = new VerticalPanel();
	private VerticalPanel panelVHistorico_2G = new VerticalPanel();
	private VerticalPanel panelVOrdenesPendientes_2H = new VerticalPanel();

	private HorizontalPanel panelHBotonesAceptarYCancelar_2I = new HorizontalPanel();

	private HorizontalPanel panelHCreadaPor_3A = new HorizontalPanel();
	private HorizontalPanel panelHDatosCliente_3A = new HorizontalPanel();

	private HorizontalPanel panelHPrioridad_3B = new HorizontalPanel();
	protected HorizontalPanel panelHTecnico_3B = new HorizontalPanel();
	private HorizontalPanel panelHEstado_3B = new HorizontalPanel();
	private HorizontalPanel panelHContacto_3B = new HorizontalPanel();

	private Button btnAnular = new Button("Anular");
	private Button btnFinalizar = new Button("Finalizar");
	private Button btnReparar = new Button("Reparar");
	private Button btnGuardar = new Button("Guardar");
	private Button btnCancelar = new Button("Cancelar");
	
	private Button btnAgregarActivo = new Button("Agregar Activo");

	private FlexTable tableReparaciones = new FlexTable();
	private FlexTable tableDatosTecnicos = new FlexTable();
	private FlexTable tableHistoricoReparaciones = new FlexTable();
	private FlexTable tableOrdenesPendientes = new FlexTable();

	private Label lblDatosEmpresaDato = new Label();
	private Label lblCreadaPor = new Label("Creada Por: ");

	private Label lblCreadaPorDato = new Label();	
	private Label lblPrioridad = new Label("Prioridad: ");
	protected Label lblTecnicoAsignado = new Label("Técnico Asignado: ");
	protected Label lblTecnicoAsignadoDato = new Label();
	private Label lblEstado = new Label("Estado: ");

	private Label lblTituloListaFallasReportadas = new Label("Fallas reportadas por el cliente");
	private Button btnAgregarOtroActivo = new Button("Otro activo");
	private Label lblTituloFallasDetectadas = new Label("Falla detectada por el técnico");
	private Label lblDescripcionFallaDetectada = new Label("Descripción");
	private Button btnAgregarFallaDetectada = new Button("Aceptar");
	private HorizontalPanel panelHFallaDetectada = new HorizontalPanel();
	private Label lblTituloBtnAgregarActivo = new Label("Agregar un activo no reportado");
	private Label lblTituloDeActivoEnSoluciones = new Label("Datos técnicos de la reparación");

	private Label lblContacto = new Label("Contacto: ");
	private Label lblContactoDato = new Label();

	private HorizontalPanel hpTituloCorrectivo = new HorizontalPanel();
	
	private CheckBox chPreventivo = new CheckBox();

	private ArrayList<TipoTrabajo> listaTipoTrabajo = new ArrayList<TipoTrabajo>();

	private Label lblPrioridadesDato = new Label();
	private Label lblDestinoDelCargo = new Label("Destino del cargo: ");

	private Estacion estacion;
	private Persona sesion;
	public Orden orden;
	private ArrayList<Orden> ordenesPendientes = new ArrayList<Orden>();
	private ArrayList<DatoConsultaHistoricoOrdenes> historicoOrdenes = new ArrayList<DatoConsultaHistoricoOrdenes>();
	private ArrayList<Reparacion> reparacionesOrdenadas = new ArrayList<Reparacion>();

	VerticalPanel vpOrdenesPendientes = new VerticalPanel();
	private HorizontalPanel panelHVerHistorico = new HorizontalPanel();

	private boolean btnHistoricoApretado = false;
	Image imgBtnVerHistorico = new Image("img/look.png");
	PushButton btnVerHistorico = new PushButton(imgBtnVerHistorico);

	private VerticalPanel vpHistorico = new VerticalPanel();
	private VerticalPanel vpHistoricoII = new VerticalPanel();
	private Label lblVerHistorico = new Label(" Histórico de correctivos");

	private Image imgPDF = new Image("img/pdf2.png");
	private PushButton btnPDF = new PushButton(imgPDF);

	private Image imgPDFOP = new Image("img/pdf2.png");
	private PushButton btnPDFOp = new PushButton(imgPDFOP);

	private Image imgPermiso = new Image("img/permiso.png");
	private PushButton btnPermiso = new PushButton(imgPermiso);
	
	protected IUCorrectivoToolBar iUCorrectivoToolBar;
	private IUCorrectivoTitulo iuCorrectivoTitulo;

	private PopupCargando popUp = new PopupCargando("Cargando...");

	IUWidgetSolucion widgetSolucion;
	IUWidgetSolucionOA widgetSolucionOA;

	public int margenHorizontal = 45;
	public int margenVertical = 20;

	public int margenHorizontalOA = 250;
	public int margenVerticalOA = 50;

	//private final String widthTablas = "1150px";
	private final String NA = "N/A";
	public ArrayList<UtilContador> listaUtilCont = new ArrayList<UtilContador>();
	protected String estado = "seguimientoTecnico";
	private IUWidgetTablaReparacion iuWidgetTablaReparacion;

	public ArrayList<RepuestoLinea> repuetosLineaARemover = new ArrayList<RepuestoLinea>();

	public VerticalPanel getVPanelPrincipal() {
		return vPanelPrincipal_1;
	}

	//////////////////////////////////CONSTRUCTOR//////////////////////////////////////////////////////////////
	public IUSeguimientoTecnico(Orden orden, Persona persona)
	{
		if(orden !=null && persona != null){
			popUp.show();
			this.orden = orden;
			iuWidgetTablaReparacion = new IUWidgetTablaReparacion(this, glass);
			sesion = persona;
			estacion = (Estacion)orden.getEmpresa();
			cargarWidgets(); 							//carga los widgets con elementos obtenidos de la BD
			setearWidgets();							//define tamanos, bordes, etc
			agregarWidgets(orden);						//agrega widgets a los paneles
			cargarSoluciones();
			color();
			validarTecnicoLoqueado();
		}
	}

	public Persona getSesion() {
		return sesion;
	}

	public PopupCargando getPopUp(){
		return popUp;
	}

	private void color() {
		lblCreadaPor.setStyleName("Negrita");
		lblPrioridad.setStyleName("Negrita");
		lblTecnicoAsignado.setStyleName("Negrita");
		lblContacto.setStyleName("Negrita");
		lblEstado.setStyleName("Negrita");
		lblTituloListaFallasReportadas.setStyleName("SubTitulo");
		lblTituloDeActivoEnSoluciones.setStyleName("SubTitulo");
	}

	private void validarTecnicoLoqueado(){
		if(sesion.getRol() == 3){
			btnFinalizar.setVisible(false);
			btnAnular.setVisible(false);
			
			btnReparar.setVisible(true);
			iUCorrectivoToolBar.getDateBoxCumplimiento().setEnabled(false);
			iUCorrectivoToolBar.getListBoxTipoDeTrabajo().setEnabled(false);
			iUCorrectivoToolBar.getListBoxTecnicoAsignado().setEnabled(false);
		} else {
			iUCorrectivoToolBar.getDateBoxCumplimiento().setEnabled(true);
			iUCorrectivoToolBar.getListBoxTipoDeTrabajo().setEnabled(true);
			iUCorrectivoToolBar.getListBoxTecnicoAsignado().setEnabled(true);
			btnFinalizar.setVisible(true);
			btnAnular.setVisible(true);
			
			btnReparar.setVisible(false);
		}
	}

	//	SETEAR WIDGETS================================================================================================	
	private void setearWidgets() {
		setearvPanelPrincipal_1();		

		panelHTitulOrden.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panelHDatosClienteyCreadaPor_2A.setBorderWidth(1);
		panelHPrioridadyTecnicoyEstado_2B.setBorderWidth(1);

		panelHTitulOrden.setWidth("1185px");
		panelHDatosClienteyCreadaPor_2A.setWidth("100%");
		panelHPrioridadyTecnicoyEstado_2B.setWidth("100%");
		panelVFallaRepYAgregarActivo2D.setWidth("100%");
		panelVOrdenesPendientes_2H.setWidth("100%");

		panelHDatosCliente_3A.setWidth("900px");
		panelHCreadaPor_3A.setWidth("260px");

		panelHPrioridad_3B.setWidth("200");
		panelHTecnico_3B.setWidth("400");
		panelHEstado_3B.setWidth("435");
		panelHContacto_3B.setWidth("435");

		lblPrioridadesDato.setWidth("150");
		lblDestinoDelCargo.setWidth("150");
		lblTecnicoAsignadoDato.setWidth("150");

		//Tablas Falla Reclamada, Falla Detectada, Tarea Realizada y Repuesto================================================================================================
		tableReparaciones.setWidth("100%");
		tableReparaciones.setCellSpacing(3);
		tableReparaciones.setCellPadding(1);

		tableDatosTecnicos.setWidth("100%");
		tableDatosTecnicos.setCellSpacing(3);
		tableDatosTecnicos.setCellPadding(1);

		tableHistoricoReparaciones.setWidth("100%");
		tableHistoricoReparaciones.setCellSpacing(3);
		tableHistoricoReparaciones.setCellPadding(1);

		tableOrdenesPendientes.setWidth("100%");
		tableOrdenesPendientes.setCellSpacing(3);
		tableOrdenesPendientes.setCellPadding(1);

		// vPanelPrincipal_1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		btnAgregarFallaDetectada.setWidth("100px");
		btnAgregarActivo.setWidth("120");

		panelVDatosTecnicos.setSpacing(10);

		panelHVerHistorico.setSpacing(10);
		btnVerHistorico.setSize("20px", "20px");
		imgBtnVerHistorico.setSize("20px", "20px");
	}

	private void setearvPanelPrincipal_1() {
		//vPanelPrincipal_1.setWidth("1185px");
		vPanelPrincipal_1.setSpacing(5);
	}

	//	AGREGAR WIDGETS================================================================================================
	private void agregarWidgets(Orden orden){

		agregarWidgetsvPanelPrincipal_1();							//vPanelPrincipal
		agregarWidgetsDatosClienteyCreadaPor_2A();			//A
		// agregarWidetPpanelHContacto_TipoTrabajo_Preventivo();
		// agregarWidgetsDatosClienteyPrioridadyTecnicoyEstado_2B();	//B
		agregarWidgetsHPanelListaReparaciones_2D();					//D
		agregarWidgetsHPanelFallaRepYBtnActivo();	

		btnAgregarOtroActivo.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (cargarUtilContadores())
				{	
					solucionOA();
				}
			}
		});
		vPanelPrincipal_1.add(file);
	}

	private void solucionOA() {
		IUWidgetSolucionOA widgetSolucionOA = new IUWidgetSolucionOA(popUp, glass, this, null, false, sesion);
		DialogBox dial = widgetSolucionOA.agregarOtroActivo();
		glass.show();
		dial.show(); 
		dial.center();
		// dial.setPopupPosition(margenHorizontalOA, margenVerticalOA);	
	}

	private void agregarWidgetsHPanelFallaRepYBtnActivo() {
		agregarWidgetsPanelFallaDetectada();
		agregarWidgetsBotonAgregarActivo();

		panelHFallaRepYAgregarActivo2D.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		panelHBotonAgregarActivo3D.setSpacing(5);
		panelHBotonAgregarActivo3D.setWidth("450");
		panelVFallDetectada3D.setWidth("730");
		panelVFallaRepYAgregarActivo2D.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		panelVFallaRepYAgregarActivo2D.add(panelHFallaRepYAgregarActivo2D);		
	}

	private void agregarWidgetsPanelFallaDetectada() {

		panelVFallDetectada3D.setSpacing(5);
		panelHFallaDetectada.setSpacing(5);
		panelHFallaDetectada.add(lblDescripcionFallaDetectada);
		panelHFallaDetectada.add(btnAgregarFallaDetectada);

		panelVFallDetectada3D.add(lblTituloFallasDetectadas);
		panelVFallDetectada3D.add(panelHFallaDetectada);
	}

	private void agregarWidgetsBotonAgregarActivo() {
		btnAgregarActivo.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				cargarPanelActivos();
			}
		});
		panelHBotonAgregarActivo3D.add(lblTituloBtnAgregarActivo);
		panelHBotonAgregarActivo3D.add(btnAgregarActivo);
	}

	public void cargarSoluciones(){
		panelVDatosTecnicos.clear();
		if(orden.tieneSoluciones()){
			panelVDatosTecnicos.setWidth("100%");
			panelVDatosTecnicos.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			panelVDatosTecnicos.add(lblTituloDeActivoEnSoluciones);
			panelVDatosTecnicos.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
			lblTituloDeActivoEnSoluciones.setStyleName("SubTitulo");

			Set<Reparacion> reparacionesOrdenedas = new TreeSet<Reparacion>();
			reparacionesOrdenedas.addAll(orden.getReparaciones());

			for (Reparacion r : orden.getReparaciones()) {
				iuWidgetTablaReparacion.setearRepYSolATabla(r, panelVDatosTecnicos);
			}
		}
	}

	private void cargarPanelActivos() {
		ProyectoBilpa.greetingService.buscarEstacion(estacion.getId(), new AsyncCallback<Estacion>(){
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar estacion: " + caught.getMessage());
				vpu.showPopUp();
			}

			public void onSuccess(Estacion result) {
				if(result != null){
					cargarDialogoActivo(result);
				}
			}
		});
	}

	private void cargarDialogoActivo(Estacion result) {
		DialogoGestionActivos dialog = new DialogoGestionActivos(result, this);
		DialogBox dial = dialog.cargarDialogoActivos();
		dial.show();
		dial.center();
	}

	private void agregarWidgetsvPanelPrincipal_1(){
		vPanelPrincipal_1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hpTituloCorrectivo.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		HorizontalPanel hpBotonesDescargas = new HorizontalPanel();
		hpBotonesDescargas.add(btnPDF);
		hpBotonesDescargas.add(btnPDFOp);
		hpBotonesDescargas.add(btnPermiso);
		
		iuCorrectivoTitulo = new IUCorrectivoTitulo(sesion, orden, IUCorrectivoEnum.TEC);
		iUCorrectivoToolBar = new IUCorrectivoToolBar(sesion, orden, IUCorrectivoEnum.TEC, hpBotonesDescargas);
		
		vPanelPrincipal_1.add(iuCorrectivoTitulo.getPrincipalPanel());
		vPanelPrincipal_1.add(iUCorrectivoToolBar.getPrincipalPanel());

		//UtilUI.agregarEspacioEnBlanco(vPanelPrincipal_1, 20);

		//vPanelPrincipal_1.setWidth("100%");
		vPanelPrincipal_1.add(panelVListaFallas_2FII);		
		// vPanelPrincipal_1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		vPanelPrincipal_1.add(panelVFallaRepYAgregarActivo2D);		
		// vPanelPrincipal_1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);		
		vPanelPrincipal_1.add(panelVDatosTecnicos);
		vPanelPrincipal_1.add(panelHRepuestos_2I);

	}

	public void agregarWidgetsDatosClienteyCreadaPor_2A() {
		if (orden.getNumeroParteDucsa() != null && !orden.getNumeroParteDucsa().equalsIgnoreCase("")){
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

	protected void agregarWidgetpanelHTecnico_3B()
	{
		panelHTecnico_3B.add(lblTecnicoAsignado);
		panelHTecnico_3B.setSpacing(5);
		panelHTecnico_3B.add(lblTecnicoAsignadoDato);
	}

	//D
	private void agregarWidgetsHPanelListaReparaciones_2D(){
		panelVListaFallas_2FII.setSpacing(10);
		// panelVListaFallas_2FII.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		panelVListaFallas_2FII.add(lblTituloListaFallasReportadas);		

		panelVListaFallas_2F.setWidth("100%");
		panelVListaFallas_2FII.setWidth("100%");

		panelVListaFallas_2F.setSpacing(3);
		panelVListaFallas_2F.add(tableReparaciones);

		panelVListaFallas_2FII.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		panelVListaFallas_2FII.add(panelVListaFallas_2F);
		panelVListaFallas_2FII.add(btnAgregarOtroActivo);
	}

	//	CARGAR WIDGETS================================================================================================
	private void cargarWidgets(){	
		agregarFilaTituloListaFallaReclamadas();
		cargarReparacionesEnTabla();
		cargarHistoricoDeEmpresa(estacion);

		cargarOrdenesPendientes(estacion);
		agregarFilaTituloListaOrdenesPendientes();
		cargarWidgetsDatosClienteyCreadaPor_2A(orden);
		cargarWidgetsPanelHDatosClienteyPrioridadyTecnicoyEstado_2B();	
		cargarTodasLasListbox();
		cargarTablaRepuestos();
		obtenerOrdenesActivas();

	}

	private void agregarWidgetsHBotonesAceptarYCancelar_2G() {

		btnCancelar.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				if (sesion.getRol() == 5 || sesion.getRol() == 1)
				{
					IUListaOrdenes iu = new IUListaOrdenes(sesion);
					IUMenuPrincipal.getInstancia().agregarWidgetAlMenu(iu.getPrincipalPanel());
				}
				else if (sesion.getRol() == 3)
				{
					IUListaOrdenes iu = new IUListaOrdenes (sesion);
					IUMenuPrincipalTecnico.getInstancia().agregarWidgetAlMenu(iu.getPrincipalPanel());					
				}
			}
		});

		btnGuardar.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(setearContadores()){
					guardarOrden();
				}
			}
		});
		
		btnAnular.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				anularOrden();
			}
		});
		
		btnFinalizar.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(setearContadores()){
					finalizarOrden();
				}
			}
		});

		btnReparar.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(setearContadores()){
					repararOrden();				
				}
			}
		});
		
		btnAnular.setStyleName("buttonSecondary");
		btnAnular.setWidth("100px");
		btnCancelar.setWidth("100px");
		btnGuardar.setWidth("100px");
		btnFinalizar.setWidth("100px");
		btnReparar.setWidth("100px");
		
		panelHBotonesAceptarYCancelar_2I.setSpacing(8);
		panelHBotonesAceptarYCancelar_2I.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panelHBotonesAceptarYCancelar_2I.add(btnAnular);
		panelHBotonesAceptarYCancelar_2I.add(btnCancelar);
		panelHBotonesAceptarYCancelar_2I.add(btnGuardar);
		panelHBotonesAceptarYCancelar_2I.add(btnFinalizar);
		panelHBotonesAceptarYCancelar_2I.add(btnReparar);

		HorizontalPanel hp = new HorizontalPanel();
		hp.setSize("60px", "60px");
		vPanelPrincipal_1.add(hp);
		
		vPanelPrincipal_1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal_1.add(panelHBotonesAceptarYCancelar_2I);
		UtilUI.agregarEspacioEnBlanco(vPanelPrincipal_1, 15);
		vPanelPrincipal_1.add(vpOrdenesPendientes);
		vPanelPrincipal_1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		vPanelPrincipal_1.add(panelHVerHistorico);
	}

	private void cargarWidgetsDatosClienteyCreadaPor_2A(Orden orden){
		Label lblFecha = new Label();
		Date fecha = orden.getFechaInicio();
		String formattedValue = DateTimeFormat.getFormat("EEEE d MMMM yyyy k:mm").format(fecha);
		lblFecha.setText (String.valueOf(formattedValue));

		lblCreadaPorDato.setText(orden.getCreador().toString());

		if (orden.getNumeroParteDucsa() != null && !orden.getNumeroParteDucsa().equalsIgnoreCase(""))
		{
			lblDatosEmpresaDato.setText(
					String.valueOf("Nro: " + orden.getNumero()) + " - " +
							" Nro Ducsa: " + String.valueOf(orden.getNumeroParteDucsa()) + " - " +
							String.valueOf(lblFecha.getText() + " - ") +
							String.valueOf(estacion.getLocalidad() + " - ") +
							String.valueOf(estacion.getDireccion() + " - ")+
							String.valueOf(estacion.getTelefono() + " - ")+
							estacion.getSello() +
							" - Zona: " + estacion.getZona() 
					);			
		}else{
			lblDatosEmpresaDato.setText(
					String.valueOf("Nro: " + orden.getNumero()) + " - " +
							String.valueOf(lblFecha.getText() + " - ") +
							String.valueOf(estacion.getLocalidad() + " - ") +
							String.valueOf(estacion.getDireccion() + " - ")+
							String.valueOf(estacion.getTelefono() + " - ")+
							estacion.getSello()
							+ " - Zona: " + estacion.getZona());
		}

		lblDatosEmpresaDato.setStyleName("SubTitulo");
		panelHDatosCliente_3A.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panelHDatosCliente_3A.add(lblDatosEmpresaDato);
	}

	private void cargarWidgetsPanelHDatosClienteyPrioridadyTecnicoyEstado_2B() {
		setearPrioridad();
		if (sesion.getRol() == 3){
			setearLabelTecnicoAsignado();			
		}else if(orden.getEstadoOrden() == 5){
			setearLabelTecnicoAsignado();
		}
		setearContacto();
	}

	private void setearContacto() {
		if (orden.getContacto() != null)
		{
			lblContactoDato.setText(orden.getContacto().toString());			
		}
	}

	private void setearPrioridad(){
		lblPrioridadesDato.setText(orden.getPrioridad())	;
	}

	private void setearLabelTecnicoAsignado() {
		String tecnicoAsignado = "";

		if(orden.getTecnicoAsignado()==null){
			ValidadorPopup vpu = new ValidadorPopup(glass, "Error Grave", "Error grave en el tecnico asignado. Contacte a soporte");
			vpu.showPopUp();
			//Nunca deberia entrar aca.
		}else{
			tecnicoAsignado = orden.getTecnicoAsignado().toString();
		}

		lblTecnicoAsignadoDato.setText(tecnicoAsignado.toString());
	}

	private void cargarReparacionesEnTabla(){			
		for(Reparacion r : orden.getReparaciones()){
			reparacionesOrdenadas.add(r);					
		}
		setearReparacionATabla();
	}

	private void agregarFilaTituloListaFallaReclamadas() {		
		//Setea los nombres de las columnas de la tabla		
		HTML activoHTML = new HTML("Activo");
		HTML fallaHTML = new HTML("Falla Reportada");
		HTML comentarioHTML = new HTML("Comentario");
		HTML elegirHTML = new HTML("");

		fallaHTML.setWidth("100%");
		fallaHTML.setWidth("410px");
		comentarioHTML.setWidth("410px");
		elegirHTML.setWidth("14px");

		tableReparaciones.getRowFormatter().setStyleName(0, "CabezalTabla");
		tableReparaciones.setWidget(0, 0, activoHTML);
		tableReparaciones.setWidget(0, 1, fallaHTML);		
		tableReparaciones.setWidget(0, 2, comentarioHTML);
		tableReparaciones.setWidget(0, 3, elegirHTML);
	}

	private void agregarFilaTituloListaOrdenesPendientes() {	
		HTML fechaHTML = new HTML("Fecha");
		fechaHTML.setWidth("280");
		HTML numeroHTML = new HTML("Número");
		numeroHTML.setWidth("280");
		HTML creadaPorHTML = new HTML("Creada Por: ");
		creadaPorHTML.setWidth("280");
		HTML tecnicoHTML = new HTML("Técnico Asignado");
		tecnicoHTML.setWidth("280");
		HTML estadoHTML = new HTML("Estado");
		estadoHTML.setWidth("280");

		//Setea los nombres de las columnas de la tabla
		tableOrdenesPendientes.getRowFormatter().setStyleName(0, "CabezalTabla");
		tableOrdenesPendientes.setWidget(0, 0, fechaHTML);
		tableOrdenesPendientes.setWidget(0, 1, numeroHTML);
		tableOrdenesPendientes.setWidget(0, 2, tecnicoHTML);
		tableOrdenesPendientes.setWidget(0, 3, estadoHTML);
	}

	private boolean setearContadores() {		
		for(UtilContador uC : listaUtilCont){
			if(!setearContadoresDeLaTabla(uC)){
				return false;
			}
		}
		return true;
	}

	private boolean setearContadoresDeLaTabla(UtilContador uC) {		
		Contador cont = uC.getCont();
		String inicio = uC.getTxtIni().getText();
		String fin = uC.getTxtFin().getText();

		if (cont.validarInicioYFin(inicio, fin)){
			cont.setInicio(Float.valueOf(inicio));
			cont.setFin(Float.valueOf(fin));	

			if(Float.valueOf(inicio) > Float.valueOf(fin)){
				ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "El contador inicial no puede ser mayor que el contador final");
				vpu.showPopUp();
				return false;
			}
		}else{
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Los valores " + inicio + " y/o " + fin + " no son validos para los totalizadores \n" + 
					"del pico: " + cont.getPico() + "\n Debe corregirlos antes de continuar.");
			vpu.showPopUp();
			return false;
		}
		return true;
	}

	private void repararOrden() {
		if(orden.tieneSoluciones()){
			if (sesion.getRol() != 3){
				boolean valid = iUCorrectivoToolBar.validarCumplimiento();
				if (valid){
					iUCorrectivoToolBar.setearCumplimiento();
				} else {
					return;
				}
			}
			
			iUCorrectivoToolBar.setearTipoTrabajo();
			setearPreventivo();
			//setElement(vPanelPrincipal_1.getElement());
			//vPanelPrincipal_1.getElement().setScrollTop(0);//setVerticalScrollPosition(0);
			glass.setHeight("2000px");
			final ComboFinService combo = new ComboFinService(glass, orden, sesion);
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				public void execute() {
					combo.getCombo().center();
				}
			});
			combo.getCombo().show();

		}else{
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe agregar al menos una reparacion");
			vpu.showPopUp();
		}
	}



	protected void guardarOrden() {
		if (sesion.getRol() != 3){
			boolean valid = iUCorrectivoToolBar.validarCumplimiento();
			if (valid){
				iUCorrectivoToolBar.setearCumplimiento();
			} else {
				return;
			}
		}
		iUCorrectivoToolBar.setearTipoTrabajo();
		setearPreventivo();

		if(orden.getEstadoOrden() == 5){
			estado = "reparado";
		}

		popUp.show();
		ProyectoBilpa.greetingService.actualizarOrden(orden, estado, sesion, new AsyncCallback<Boolean>() {
			public void onSuccess(Boolean result) {
				if (!result){
					popUp.hide();
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar la orden");
					vpu.showPopUp();

				}else{
					popUp.hide();
					irAListaDeOrdenes();
					//removerRepuestosDeOrden();
				}
			}
			public void onFailure(Throwable caught) {
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar la orden");
				vpu.showPopUp();
			}
		});
	}	

	private void anularOrden(){
		orden.setAnulador(sesion);
		dialogoComentarioAccion("Inserte un comentario para anular la orden");
	}

	private void finalizarOrden(){
		if(orden.tieneSoluciones())
		{
			if (sesion.getRol() != 3){
				boolean valid = iUCorrectivoToolBar.validarCumplimiento();
				if (valid){
					iUCorrectivoToolBar.setearCumplimiento();
				} else {
					return;
				}
			}
			
			iUCorrectivoToolBar.setearTipoTrabajo();
			setearPreventivo();
			cerrarOrden(orden, sesion);
		}
		else
		{
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe agregar al menos una reparacion");
			vpu.showPopUp();
		}

	}

	private void cerrarOrden(Orden orden, Persona sesion){
		popUp.show();
		ProyectoBilpa.greetingService.cerrarOrden(orden, sesion, new AsyncCallback<Boolean>() {

			public void onSuccess(Boolean result) {
				if (!result){
					popUp.hide();
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al finalizar la orden");
					vpu.showPopUp();
				}else{
					popUp.hide();
					irAListaDeOrdenes();
				}
			}
			public void onFailure(Throwable caught) {
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al finalizar la orden");
				vpu.showPopUp();
			}
		});	
	}

	private void anularOrdenBase() {
		popUp.show();
		ProyectoBilpa.greetingService.guardarOrdenAnulada(orden, sesion, new AsyncCallback<Boolean>() {

			public void onSuccess(Boolean result) {
				if (!result){
					popUp.hide();
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar la orden");
					vpu.showPopUp();

				}else{
					popUp.hide();
					irAListaDeOrdenes();
				}

			}
			public void onFailure(Throwable caught) {
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar la orden");
				vpu.showPopUp();
			}
		});
	}

	public DialogBox dialogoComentarioAccion(String titulo){
		glass.show();
		final HorizontalPanel hPanelDialTitulo = new HorizontalPanel();
		final HorizontalPanel hPanelDialModif = new HorizontalPanel();
		final HorizontalPanel hPanelDialModif2 = new HorizontalPanel();
		final VerticalPanel vPanelDailModif = new VerticalPanel();
		final TextBox tDialModifRep = new TextBox();
		final DialogBox dialogComentario = new DialogBox();
		final Label lblTitulo = new Label(titulo);

		Button btnAceptarModif = new Button("Aceptar",new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(!tDialModifRep.getText().equalsIgnoreCase("")){
					orden.setComentarioAnulada(tDialModifRep.getText());
					anularOrdenBase();
					dialogComentario.hide(true);
					glass.hide();
				}
			}
		});

		Button btnCancelModif = new Button("Cancelar",
				new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogComentario.hide(true);
				glass.hide();
			}
		});

		dialogComentario.setAutoHideEnabled(false);
		vPanelDailModif.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		btnAceptarModif.setWidth("100px");
		tDialModifRep.setWidth("500px");
		btnCancelModif.setWidth("100px");
		tDialModifRep.setText("");

		hPanelDialModif.add(tDialModifRep);

		hPanelDialModif2.add(btnCancelModif);
		hPanelDialModif2.setSpacing(5);
		hPanelDialModif2.add(btnAceptarModif);

		hPanelDialTitulo.add(lblTitulo);
		vPanelDailModif.add(hPanelDialTitulo);
		vPanelDailModif.add(hPanelDialModif);
		vPanelDailModif.add(hPanelDialModif2);

		dialogComentario.setWidget(vPanelDailModif);

		dialogComentario.show();
		dialogComentario.center();

		return dialogComentario;
	}

	public  void cargarLtBoxCantidad(){
		int [] retorno = new int [1201];
		for(int i=1; i<1201;i++){
			retorno[i]=i;
			listBoxCantidad.addItem(i+"");
		}
	}

	public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
		List<T> list = new ArrayList<T>(c);
		java.util.Collections.sort(list);
		return list;
	}

	private void setearReparacionATabla()
	{
		for(final Reparacion reparacion : orden.getReparaciones()){
			if (reparacion.getFallaReportada() != null)
			{
				Label lblActivo = new Label();
				Label lblFallaRep = new Label();
				Label lblComentario = new Label();

				Image img = new Image("img/mas.png");
				PushButton btnAgregarSolucion = new PushButton(img);

				btnAgregarSolucion.setSize("15px", "15px");
				img.setSize("15px", "15px");

				lblActivo.setWidth("360");
				lblFallaRep.setWidth("360");
				lblComentario.setWidth("360");

				lblActivo.setText(reparacion.getActivo().toString());
				if (reparacion.getFallaReportada() != null)
				{
					lblFallaRep.setText(reparacion.getFallaReportada().toString());				
				}else{
					lblFallaRep.setText(NA);
				}

				lblComentario.setText(reparacion.getComentario());

				int numRows = tableReparaciones.getRowCount();
				tableReparaciones.setWidget(numRows, 0, lblActivo);
				tableReparaciones.setWidget(numRows, 1, lblFallaRep);
				tableReparaciones.setWidget(numRows, 2, lblComentario);
				tableReparaciones.setWidget(numRows, 3, btnAgregarSolucion);

				agregarBotonTablaSol(reparacion, btnAgregarSolucion);	

				UtilUI.setearCssATabla(tableReparaciones, numRows+1);				
			}
		}	
	}

	private void agregarBotonTablaSol(final Reparacion reparacion, PushButton btnAgregarSolucion) 
	{
		btnAgregarSolucion.setWidth("30");
		btnAgregarSolucion.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				clickAgregarSolucion(reparacion);
			}
		});
	}

	private void clickAgregarSolucion(Reparacion reparacion) {
		if (cargarUtilContadores())
		{	
			widgetSolucion = new IUWidgetSolucion(reparacion, popUp, glass, this, Constants.OPERACION_WIDGET_SOLUCION.VIENE_DE_IU_SEG_TEC, this.sesion);
			widgetSolucion.initWidgets();
			DialogBox dialogo = widgetSolucion.getDialogo();
			dialogo.show();
			dialogo.center();
			//dialogo.setPopupPosition(margenHorizontal, margenVertical);
		}				
	}

	public  void cargarLtBoxActivos_Repuestos(){
		listBoxActivosRepuestos.clear();
		listBoxActivosRepuestos.addItem("Sin Seleccionar");

		Set<Activo> listaActivosReparados = new HashSet<Activo>();
		for(Solucion s : orden.getSoluciones())
		{
			if (!contiene (listaActivosReparados, s.getReparacion().getActivo()))
			{
				listaActivosReparados.add(s.getReparacion().getActivo());				
			}
		}

		for(Activo a : listaActivosReparados)
		{
			listBoxActivosRepuestos.addItem(a.toString(),String.valueOf(a.getId()));
		}
	}


	//No funco el Set
	private boolean contiene(Set<Activo> listaActivosReparados, Activo activo) {
		for (Activo a : listaActivosReparados)
		{
			if (a.getId() == activo.getId())
			{
				return true;
			}
		}
		return false;
	}

	private boolean cargarUtilContadores() {
		for(UtilContador uC : listaUtilCont){
			if (uC.getCont().validarInicioYFin(uC.getTxtIni().getText(), uC.getTxtFin().getText()))
			{
				uC.getCont().setInicio(Float.valueOf(uC.getTxtIni().getText()));
				uC.getCont().setFin(Float.valueOf(uC.getTxtFin().getText()));

			}else{
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Los valores " + uC.getTxtIni().getText() + " y/o " + uC.getTxtFin().getText() + " no son validos para los totalizadores \n" + 
						"del pico: " + uC.getCont().getPico() + "\n Debe corregirlos antes de continuar.");
				vpu.showPopUp();
				return false;
			}
		}
		return true;	
	}

	private void cargarTodasLasListbox(){
		iuWidgetRepuesto.cargarLtBoxTipoRepuesto(popUp);
		cargarLtBoxActivos_Repuestos();
		cargarLtBoxCantidad();
	}

	public void cargarTablaRepuestos(){
		/*HorizontalPanel hp = new HorizontalPanel();

		panelVRepInteriorConHistorico.setSpacing(5);

		hp.setSpacing(10);
		panelVRepInteriorConHistorico.setSpacing(10);
		panelVRepInteriorConHistorico.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		UtilUI.agregarEspacioEnBlanco(panelVRepInteriorConHistorico, 20);
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		panelVRepInteriorConHistorico.add(hp);
		panelHRepuestos_2I.add(panelVRepInteriorConHistorico);		*/	
	}

	private void cargarOrdenesPendientes(Estacion empresa){

		ProyectoBilpa.greetingService.buscarOrdenesPendientes(empresa, new AsyncCallback<ArrayList<Orden>>() {


			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar ordenes pendientes");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<Orden> result) {
				if (result==null){
					vPanelPrincipal_1.remove(panelVOrdenesPendientes_2H);					
				}else{
					Orden auxiliar = new Orden();
					for (int i=0; i<result.size();i++){
						auxiliar = (Orden) result.get(i);
						ordenesPendientes.add(auxiliar);
					}	
				}							
			}
		});		
	}

	private void cargarHistoricoDeEmpresa(Estacion empresa){
		ProyectoBilpa.greetingService.historicoOrdenesFinalizadasEmpresa(empresa, new AsyncCallback<ArrayList<DatoConsultaHistoricoOrdenes>>() {
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar el historico de la orden");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<DatoConsultaHistoricoOrdenes> result) {
				if (result==null){
					panelVRepInteriorConHistorico.remove(panelVHistorico_2G);					
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
		vpHistorico.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vpHistoricoII.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		vpHistorico.setWidth("100%");
		vpHistoricoII.setWidth("100%");

		vpHistorico.setSpacing(10);
		vpHistoricoII.setSpacing(10);

		Label lblTituloHisto = new Label("Histórico de correctivos");
		lblTituloHisto.setStyleName("SubTitulo");
		vpHistorico.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

		vpHistoricoII.add(lblTituloHisto);
		vpHistoricoII.add(vpHistorico);

		for (DatoConsultaHistoricoOrdenes s : historicoOrdenes) {
			Date fecha = s.getFecha();
			String formattedValue = DateTimeFormat.getFormat("EEEE d MMMM yyyy k:mm").format(fecha);

			//LABEL TITULO
			Label nroOrden = new Label("Correctivo: ");
			Label nroOrdenValor = new Label(s.getNro());
			Label finalizada = new Label(" - Finalizada: ");
			Label finalizadaValor = new Label(formattedValue);
			HorizontalPanel hpTitulo = new HorizontalPanel();

			vpHistorico.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hpTitulo.setSpacing(6);
			hpTitulo.add(nroOrden);
			hpTitulo.add(nroOrdenValor);
			hpTitulo.add(finalizada);
			hpTitulo.add(finalizadaValor);

			nroOrden.setStyleName("Negrita");
			finalizada.setStyleName("Negrita");

			vpHistorico.add(hpTitulo);
			//LABEL TITULO

			FlexTable tableSoluciones = new FlexTable();
			tableSoluciones.setWidth("100%");
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
					vPanelPrincipal_1.add(vpHistoricoII);
				}else{
					vPanelPrincipal_1.remove(vpHistoricoII);
				}
			}			
		});
	}

	private void agregarWidgetsVerHistorico()
	{
		panelHVerHistorico.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		lblVerHistorico.setStyleName("Negrita");

		panelHVerHistorico.setSpacing(5);

		VerticalPanel vpSpace = new VerticalPanel();
		vpSpace.setHeight("100px");
		panelHVerHistorico.add(vpSpace);

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

	private void cargarTablaOrdenesPendientes(ArrayList<DatoOrdenesActivasEmpresa> result){

		if(result.size()>1){
			VerticalPanel vpGrande = new VerticalPanel();

			Label lblOrdenesPendientesTitulo = new Label("Lista de correctivos pendientes");
			lblOrdenesPendientesTitulo.setStyleName("SubTitulo");
			vpGrande.setSpacing(3);

			vpGrande.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

			FlexTable tableSoluciones = new FlexTable();
			tableSoluciones.setWidth("100%");
			agregarFilaTituloPendientes(tableSoluciones);


			int i = 1;
			for (DatoOrdenesActivasEmpresa s : result) {
				String numero = orden.getNumero() + "";
				if(!numero.equals(s.getNumero())){
					tableSoluciones.setText(i, 0, s.getFecha());
					tableSoluciones.setText(i, 1, s.getNumero());
					tableSoluciones.setText(i, 2, s.getAutor());
					tableSoluciones.setText(i, 3, s.getTecnico());
					tableSoluciones.setText(i, 4, s.getEstado());
				}

				i++;
				UtilCss.aplicarEstiloATabla(tableSoluciones, i);
			}

			vpOrdenesPendientes.setSpacing(10);
			vpOrdenesPendientes.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

			vpOrdenesPendientes.setWidth("100%");
			vpGrande.setWidth("100%");

			vpOrdenesPendientes.add(lblOrdenesPendientesTitulo);
			vpGrande.add(tableSoluciones);	
			vpOrdenesPendientes.add(vpGrande);			
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
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener ordenes activas de una empresa");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<DatoOrdenesActivasEmpresa> result) {
				cargarTablaOrdenesPendientes(result);	
				agregarWidgetsVerHistorico();
				agregarWidgetsHBotonesAceptarYCancelar_2G();
			}
		});		
	}

	private void irAListaDeOrdenes() {
		if (sesion.getRol() == 5 || sesion.getRol() == 1)
		{
			IUListaOrdenes iu = new IUListaOrdenes(sesion);
			IUMenuPrincipal.getInstancia().agregarWidgetAlMenu(iu.getPrincipalPanel());
		}
		else if (sesion.getRol() == 3)
		{
			IUListaOrdenes iu = new IUListaOrdenes(sesion);
			IUMenuPrincipalTecnico.getInstancia().agregarWidgetAlMenu(iu.getPrincipalPanel());					
		}
	}

	private void setearPreventivo() {
		boolean esPreventivo = chPreventivo.getValue();
		orden.setPreventivo(esPreventivo);
	}

}
