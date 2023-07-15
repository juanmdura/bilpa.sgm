package app.client.iu.orden.alta;

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
import com.google.gwt.user.datepicker.client.DateBox;

import app.client.ProyectoBilpa;
import app.client.dominio.Activo;
import app.client.dominio.ActivoGenerico;
import app.client.dominio.BombaSumergible;
import app.client.dominio.Canio;
import app.client.dominio.Estacion;
import app.client.dominio.FallaReportada;
import app.client.dominio.IUCorrectivo.IUCorrectivoEnum;
import app.client.dominio.Orden;
import app.client.dominio.Pendiente;
import app.client.dominio.Persona;
import app.client.dominio.Pico;
import app.client.dominio.Reparacion;
import app.client.dominio.ReparacionSurtidor;
import app.client.dominio.Solucion;
import app.client.dominio.Surtidor;
import app.client.dominio.Tanque;
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
import app.client.iu.orden.IUIngresoOrden;
import app.client.iu.orden.IUListaOrdenes;
import app.client.iu.orden.inicio.IUSeguimientoInicial;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.Constants.OPERACION_GARANTIA;
import app.client.utilidades.UtilFechas;
import app.client.utilidades.UtilUI;
import app.client.utilidades.utilObjects.GarantiaUtil;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;

public class IUAltaOrden extends IUWidgetCorrectivo {

	private VerticalPanel vPanelPrincipal_1 = new VerticalPanel();

	// 2
	private VerticalPanel panelVListaFallas_2C = new VerticalPanel();
	private VerticalPanel panelVHistorico_2D = new VerticalPanel();
	private VerticalPanel panelVOrdenesPendientes_2E = new VerticalPanel();
	private VerticalPanel panelVHistorico_2G = new VerticalPanel();
	private HorizontalPanel panelHBotonesAceptarYCancelar_2F = new HorizontalPanel();

	// 3 B
	private VerticalPanel panelVDatosActivo_3B = new VerticalPanel();

	// 4 B
	private VerticalPanel panelVActivo_TipoActivo_4B = new VerticalPanel();
	private VerticalPanel panelVActivo_ListaActivo_4B = new VerticalPanel();

	// 5 B
	private VerticalPanel panelVDatosActivosSeleccionado_4B = new VerticalPanel();
	private HorizontalPanel panelHDatosActivo_Falla_4B = new HorizontalPanel();

	private VerticalPanel panelVDatosFallaActivo_5B_2 = new VerticalPanel();

	private ListBox listBoxTipoActivo;

	private ListBox listBoxListaDeActivos = new ListBox();

	private Label lbNumeroDeSerie = new Label("Serie");
	private Label lbModeloSurtidor = new Label("Modelo surtidor");
	private Label lbTipoCombustibleT = new Label("Combustible");
	private Label lblFallaReportadaCliente = new Label("Tipo de falla reportada");
	private Label lblFallaReportadaCliente2 = new Label("Falla reportada");
	private Label lblidT = new Label("Id");
	private Label lblDescT = new Label("Descripción");
	private Label lblidC = new Label("Id");
	private Label lblDescC = new Label("Descripción");
	private Label lblidB = new Label("Id");
	private Label lblDescB = new Label("Descripción");
	private Label lblCapT = new Label("Capacidad");

	private Button btnAgregarFallaReportada = new Button("Agregar");

	private Label lbTituloListaFallas = new Label("Fallas reportadas por el cliente");
	private Label lbTituloHistoricoReparciones = new Label("Histórico de reparaciones");
	private Label lbTituloOrdenesPendientes = new Label("Ordenes pendientes");

	private IUCorrectivoToolBar iuCorrectivoToolbar;
	private IUCorrectivoTitulo iuCorrectivoTitulo;
	private IUCorrectivoSeleccionarActivoYFalla iuCorrectivoSeleccionarActivo;

	private Button btnGuardar = new Button("Guardar");
	private Button btnCancelar = new Button("Cancelar");

	private FlexTable tableDatosSurtidor = new FlexTable();
	private FlexTable tableDatosTanque = new FlexTable();
	private FlexTable tableDatosCanio = new FlexTable();
	private FlexTable tableDatosBomba = new FlexTable();

	private FlexTable tablaFallasReportadas = new FlexTable();

	private Map<Integer, PushButton> buttonsRemoveMap = new HashMap<Integer, PushButton>();
	IUWidgetFalla iuWidgetFalla = new IUWidgetFalla();

	private Estacion estacion;
	private Activo activoSeleccionado;
	private Pico picoSeleccionado;
	private int tipoActivo;
	private Persona sesion;
	private Orden orden;
	private ArrayList<Reparacion> listaReparaciones = new ArrayList<Reparacion>();
	private ArrayList<DatoConsultaHistoricoOrdenes> historicoOrdenes = new ArrayList<DatoConsultaHistoricoOrdenes>();

	private HorizontalPanel panelHVerHistorico = new HorizontalPanel();
	Image imgBtnVerHistorico = new Image("img/look.png");
	PushButton btnVerHistorico = new PushButton(imgBtnVerHistorico);
	private VerticalPanel vpHistorico = new VerticalPanel();
	private Label lblVerHistorico = new Label("Histórico de correctivos de la estación");
	private PopupCargando popUp = new PopupCargando("Cargando...");

	private IURepararPendienteTitulo iuRepararPendienteTitulo;
	private VerticalPanel vpPendientes = new VerticalPanel();
	
	private GlassPopup glass = new GlassPopup();

	GarantiaUtil garantiaUtil;

	public VerticalPanel getVPanelPrincipal() {
		return vPanelPrincipal_1;
	}
	
	public Orden getOrden() {
		return orden;
	}

	public IUAltaOrden(Estacion estacion, Persona persona, String numeroParteDucsa) {
		if (estacion != null && persona != null) {
			this.estacion = estacion;
			sesion = persona;
			crearOrden(numeroParteDucsa);

			cargarWidgets();// carga los widgets con elementos obtenidos de la

			color();
		}
	}

	private void color() {
		lbNumeroDeSerie.setStyleName("Negrita");
		lbModeloSurtidor.setStyleName("Negrita");
		lbNumeroDeSerie.setStyleName("Negrita");
		lbTipoCombustibleT.setStyleName("Negrita");

		lblidC.setStyleName("Negrita");
		lblidT.setStyleName("Negrita");
		lblidB.setStyleName("Negrita");
		lblDescC.setStyleName("Negrita");
		lblDescT.setStyleName("Negrita");
		lblDescB.setStyleName("Negrita");

		lblFallaReportadaCliente.setStyleName("Negrita");
		lblFallaReportadaCliente2.setStyleName("Negrita");
		lbTituloOrdenesPendientes.setStyleName("Negrita");
		lbTituloHistoricoReparciones.setStyleName("Negrita");
		lbTituloListaFallas.setStyleName("SubTitulo");

	}

	private void setearWidgets() {
		iuCorrectivoToolbar.getDateBoxCumplimiento().setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat(UtilFechas.DATE_FORMAT_SHORT)));

		listBoxListaDeActivos.setVisibleItemCount(8);

		btnGuardar.setWidth("100");
		btnCancelar.setWidth("100");
		panelVHistorico_2G.setWidth("1185px");

		panelVActivo_ListaActivo_4B.setSpacing(5);
		panelVActivo_TipoActivo_4B.setSpacing(5);

		btnVerHistorico.setSize("20px", "20px");
		imgBtnVerHistorico.setSize("20px", "20px");
	}

	private void agregarWidgets(Estacion estacion) {
		vPanelPrincipal_1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		vPanelPrincipal_1.add(iuCorrectivoTitulo.getPrincipalPanel());
		vPanelPrincipal_1.add(iuCorrectivoToolbar.getPrincipalPanel());

		HorizontalPanel hp2 = new HorizontalPanel();
		hp2.setSize("60px", "60px");
		
		vPanelPrincipal_1.add(panelVListaFallas_2C);
		vPanelPrincipal_1.setSpacing(5);
		vPanelPrincipal_1.add(vpPendientes);
		vPanelPrincipal_1.add(hp2);
		vPanelPrincipal_1.add(panelVHistorico_2D);
		vPanelPrincipal_1.setSpacing(5);
		vPanelPrincipal_1.add(panelVOrdenesPendientes_2E);
		vPanelPrincipal_1.add(panelVHistorico_2G);
		vPanelPrincipal_1.setSpacing(5);
		vPanelPrincipal_1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal_1.add(panelHBotonesAceptarYCancelar_2F);
		vPanelPrincipal_1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

		//agregarWidgetsHPanelActivoYDatosActivo_2B(); // B
		agregarWidgetsHPanelListaFallas_2C(); // C
		agregarWidgetsHPanelBotonesAceptarYCancelar_2F(); // F
	}

	// B
	private void agregarWidgetsHPanelActivoYDatosActivo_2B() {
		listBoxListaDeActivos.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				cambiarActivoSeleccionado();
			}
		});

		iuCorrectivoToolbar.getListBoxTipoDeTrabajo().addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				if (!iuCorrectivoToolbar.getListBoxTipoDeTrabajo().getValue(iuCorrectivoToolbar.getListBoxTipoDeTrabajo().getSelectedIndex()).toString().equals("1")){
					iuCorrectivoToolbar.getDateBoxCumplimiento().setEnabled(true);
				} else {
					iuCorrectivoToolbar.getDateBoxCumplimiento().setEnabled(false);
				}
			}
		});

		// Tabla para Datos de los
		// Surtidores====================================
		// ============================================================
		tableDatosSurtidor.setWidget(0, 0, lbNumeroDeSerie);
		tableDatosSurtidor.setWidget(0, 2, lbModeloSurtidor);

		lblidT.setStyleName("Negrita");
		lblDescT.setStyleName("Negrita");
		lblCapT.setStyleName("Negrita");

		tableDatosTanque.setWidget(0, 0, lblidT);
		tableDatosTanque.setWidget(0, 2, lblDescT);
		tableDatosTanque.setWidget(1, 0, lbTipoCombustibleT);
		tableDatosTanque.setWidget(1, 2, lblCapT);

		tableDatosCanio.setWidget(0, 0, lblidC);
		tableDatosCanio.setWidget(0, 2, lblDescC);

		tableDatosBomba.setWidget(0, 0, lblidB);
		tableDatosBomba.setWidget(0, 2, lblDescB);

		panelVDatosActivosSeleccionado_4B.add(tableDatosSurtidor); 

		panelVDatosActivo_3B.add(panelVDatosActivosSeleccionado_4B);
		panelVDatosActivo_3B.setSpacing(5);
		panelVDatosActivo_3B.add(panelHDatosActivo_Falla_4B);

		panelVDatosActivo_3B.setSpacing(5);

		// Datos Activos FALLA
		FlexTable tableFallas = new FlexTable();
		panelVDatosActivo_3B.add(tableFallas);

		tableFallas.setCellPadding(5);
		tableFallas.setCellSpacing(5);
		tableFallas.setWidget(0, 0, lblFallaReportadaCliente);
		tableFallas.setWidget(0, 1, iuWidgetFalla.listBoxTiposFallasR);
		tableFallas.setWidget(1, 0, lblFallaReportadaCliente2);
		tableFallas.setWidget(1, 1, iuWidgetFalla.listBoxFallasR);
		tableFallas.setWidget(1, 2, panelVDatosFallaActivo_5B_2);
		iuWidgetFalla.cargarLtBoxTipoFallaR(popUp, iuWidgetFalla.listBoxTiposFallasR);

		// FALLA
		// 2================================================================
		// ================================
		btnAgregarFallaReportada.addClickHandler(new ClickHandler() {
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
		panelVDatosFallaActivo_5B_2.add(btnAgregarFallaReportada);
	}

	// C
	private void agregarWidgetsHPanelListaFallas_2C() {
		panelVListaFallas_2C.setSpacing(5);
		panelVListaFallas_2C.add(lbTituloListaFallas);
		panelVListaFallas_2C.setSpacing(3);

		HorizontalPanel hp = new HorizontalPanel();
		hp.add(tablaFallasReportadas);
		hp.add(iuCorrectivoSeleccionarActivo.getBuscarActivo());
		panelVListaFallas_2C.add(hp);
	}

	// F
	private void agregarWidgetsHPanelBotonesAceptarYCancelar_2F() {
		panelHBotonesAceptarYCancelar_2F.add(btnCancelar);
		panelHBotonesAceptarYCancelar_2F.setSpacing(8);
		panelHBotonesAceptarYCancelar_2F.add(btnGuardar);

		btnGuardar.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				guardarOrden();
			}
		});

		btnCancelar.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				cancelarOrden();
			}
		});
	}

	// CARGAR
	// WIDGETS============================================================
	// ====================================
	private void cargarWidgets() {
		cargarDatosActivos();
		
		agregarFilaTituloListaFallaReclamadas();

		obtenerPendientes();

		cargarHistoricoDeEmpresa(estacion);

		obtenerOrdenesActivas();
		agregarWidgetsVerHistorico();

	}

	private void cargarDatosActivos() {
		// Por defecto se cargan los surtidores
		int tipoActivo = 1; // Se invoca a obtenerAcitvosPorTipo para traer los
		// surtidores al cargar la pagina.
		obtenerActivosPorTipo(tipoActivo);
	}

	private void agregarFilaTituloListaFallaReclamadas() {
		// Setea los nombres de las columnas de la tabla
		HTML activoHTML = new HTML("Activo");
		activoHTML.setWidth("378");
		HTML fallaHTML = new HTML("Descripción de falla reportada");
		fallaHTML.setWidth("378");
		HTML comentarioHTML = new HTML("Comentario");
		comentarioHTML.setWidth("378");
		tablaFallasReportadas.getRowFormatter().setStyleName(0, "CabezalTabla");
		tablaFallasReportadas.setWidget(0, 0, activoHTML);
		tablaFallasReportadas.setWidget(0, 1, fallaHTML);
		tablaFallasReportadas.setWidget(0, 2, comentarioHTML);
	}

	// METODOS DE ACCESO A
	// BD====================================================
	// ============================================
	private boolean validarCampos() {
		boolean valid = true;
		if (listaReparaciones.size() <= 0) {
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe agregar al menos un falla para un activo");
			vpu.showPopUp();
			valid = false;
		}

		if (!valid){
			return false;
		}
		valid = iuCorrectivoToolbar.validarCumplimiento();

		return valid;
	}

	private void cambiarTipoActivo() {
		tipoActivo = Integer.valueOf(listBoxTipoActivo.getValue(listBoxTipoActivo.getSelectedIndex()));
		obtenerActivosPorTipo(tipoActivo);
		cambiarPanelDatosActivo();
	}

	private void obtenerActivosPorTipo(int tipoActivo) {
		ProyectoBilpa.greetingService.obtenerActivosPorTipo(estacion, tipoActivo, new AsyncCallback<ArrayList<Activo>>() {
			public void onSuccess(ArrayList<Activo> result) {
				listBoxListaDeActivos.clear();
				for (int i = 0; i < result.size(); i++) {
					listBoxListaDeActivos.addItem(result.get(i).toString(), String.valueOf(result.get(i).getId()));
				}
			}

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al Obtener Activos Por Tipo");
				vpu.showPopUp();
			}
		});
	}

	// MOSTRAR DATOS ACTIVO
	// SELECCIONADO==========================================
	// ======================================================
	private void mostrarDatosActivoSeleccionado() {
		if (activoSeleccionado.getTipo() == 1) {// Surtidor
			Surtidor surtidor = (Surtidor) activoSeleccionado;
			mostrarDatosSurtidorSeleccionado(surtidor);
		} else {
			panelVDatosActivosSeleccionado_4B.remove(0);
			if (activoSeleccionado.getTipo() == 2) {// Tanque
				Tanque tanque = (Tanque) activoSeleccionado;
				mostrarDatosTanqueSeleccionado(tanque);
			}
			if (activoSeleccionado.getTipo() == 3) {// Canio
				Canio canio = (Canio) activoSeleccionado;
				mostrarDatosCanioSeleccionado(canio);
			}
			if (activoSeleccionado.getTipo() == 4) {// Bomba
				BombaSumergible bomba = (BombaSumergible) activoSeleccionado;
				mostrarDatosBombaSeleccionada(bomba);
			}
			if (activoSeleccionado.getTipo() == 6) {// Generico
				ActivoGenerico gen = (ActivoGenerico) activoSeleccionado;
				mostrarDatosActivoGenericoSeleccionada(gen);
			}
		}
	}

	private void cambiarPanelDatosActivo() {
		panelVDatosActivosSeleccionado_4B.remove(0);
		activoSeleccionado = null;
		if (tipoActivo == 1) {
			tableDatosSurtidor.setText(0, 1, "");
			tableDatosSurtidor.setText(0, 3, "");
			tableDatosSurtidor.setText(1, 3, "");
			panelVDatosActivosSeleccionado_4B.add(tableDatosSurtidor);
		}
		if (tipoActivo == 2) {
			panelVDatosActivosSeleccionado_4B.add(tableDatosTanque);
			tableDatosTanque.setText(0, 1, "");
			tableDatosTanque.setText(0, 3, "");
			tableDatosTanque.setText(1, 1, "");
			tableDatosTanque.setText(1, 3, "");
		}
		if (tipoActivo == 3) {
			panelVDatosActivosSeleccionado_4B.add(tableDatosCanio);
			tableDatosCanio.setText(0, 1, "");
			tableDatosCanio.setText(0, 3, "");
		}
		if (tipoActivo == 4) {
			panelVDatosActivosSeleccionado_4B.add(tableDatosBomba);
			tableDatosBomba.setText(0, 1, "");
			tableDatosBomba.setText(0, 3, "");
		}
		if (tipoActivo == 6) {
			//todo activo generico
		}
	}

	private void mostrarDatosSurtidorSeleccionado(Surtidor surtidor) 
	{
		tableDatosSurtidor.setText(0, 3, "");

		tableDatosSurtidor.setWidget(0, 0, lbNumeroDeSerie);
		tableDatosSurtidor.setText(0, 1, surtidor.getNumeroSerie());
		tableDatosSurtidor.setWidget(0, 2, lbModeloSurtidor);
		tableDatosSurtidor.setText(0, 3, surtidor.getModelo().toString());

		garantiaUtil = new GarantiaUtil(surtidor, tableDatosSurtidor, 0, 4, OPERACION_GARANTIA.PANTALLA_ORDEN);
	}

	private void mostrarDatosTanqueSeleccionado(Tanque tanque) {
		tableDatosTanque.setWidget(0, 0, lblidT);
		tableDatosTanque.setText(0, 1, String.valueOf(tanque.getId()));
		tableDatosTanque.setWidget(0, 2, lblDescT);
		tableDatosTanque.setText(0, 3, tanque.getDescripcion());
		tableDatosTanque.setWidget(1, 0, lbTipoCombustibleT);
		tableDatosTanque.setText(1, 1, tanque.getProducto().getNombre());
		tableDatosTanque.setWidget(1, 2, lblCapT);
		tableDatosTanque.setText(1, 3, String.valueOf(tanque.getCapacidad()) + " lts");

		panelVDatosActivosSeleccionado_4B.add(tableDatosTanque);

		garantiaUtil = new GarantiaUtil(tanque, tableDatosTanque, 1, 4, OPERACION_GARANTIA.PANTALLA_ORDEN);
	}

	private void mostrarDatosCanioSeleccionado(Canio canio) {
		tableDatosCanio.setWidget(0, 0, lblidC);
		tableDatosCanio.setText(0, 1, String.valueOf(canio.getId()));
		tableDatosCanio.setWidget(0, 2, lblDescC);
		tableDatosCanio.setText(0, 3, canio.getDescripcion());

		panelVDatosActivosSeleccionado_4B.add(tableDatosCanio);

	}

	private void mostrarDatosBombaSeleccionada(BombaSumergible bomba) {
		tableDatosBomba.setWidget(0, 0, lblidB);
		tableDatosBomba.setText(0, 1, String.valueOf(bomba.getId()));
		tableDatosBomba.setWidget(0, 2, lblDescB);
		tableDatosBomba.setText(0, 3, bomba.getDescripcion());

		panelVDatosActivosSeleccionado_4B.add(tableDatosBomba);
		garantiaUtil = new GarantiaUtil(bomba, tableDatosBomba, 0, 4, OPERACION_GARANTIA.PANTALLA_ORDEN);
	}

	private void mostrarDatosActivoGenericoSeleccionada(ActivoGenerico gen) {
		//todo activo generico
	}

	private FallaReportada buscarFallaRep(int id) {
		return null;
	}

	// Va a la base a buscar el activo, y setea el atributo activo de clase
	private void cambiarActivoSeleccionado() {
		int idActivoSeleccionado = Integer.valueOf(listBoxListaDeActivos.getValue(listBoxListaDeActivos.getSelectedIndex()));
		ProyectoBilpa.greetingService.buscarActivo(idActivoSeleccionado, new AsyncCallback<Activo>() {
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar activo");
				vpu.showPopUp();
			}

			public void onSuccess(Activo result) {
				if (result != null) {
					activoSeleccionado = result;
					mostrarDatosActivoSeleccionado();
				} else {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar activo");
					vpu.showPopUp();
				}
			}
		});
	}

	// Se invoca desde el constructor de IUAltaOrden.
	private void cargarDatosEmpresa(Orden orden, String numeroParteDucsa) {
		this.orden = orden;
		orden.setCreador(sesion);
		if (numeroParteDucsa != null && !numeroParteDucsa.equalsIgnoreCase("")){
			orden.setNumeroParteDucsa(numeroParteDucsa);
		}

		iuCorrectivoToolbar  = new IUCorrectivoToolBar(sesion, orden, IUCorrectivoEnum.ALTA, null);
		iuCorrectivoTitulo = new IUCorrectivoTitulo(sesion, orden, IUCorrectivoEnum.ALTA);
		iuCorrectivoSeleccionarActivo = new IUCorrectivoSeleccionarActivoYFalla(sesion, orden, IUCorrectivoEnum.ALTA, this);

		if (numeroParteDucsa != null && !numeroParteDucsa.equalsIgnoreCase("")) {
			String text = " Nro Ducsa " + String.valueOf(orden.getNumeroParteDucsa());
			iuCorrectivoTitulo.getLblSubTitulo().setText(iuCorrectivoTitulo.getLblSubTitulo().getText() + text);
		}

		agregarWidgets(estacion); // agrega widgets a los paneles
		setearWidgets(); // define tamanos, bordes, etc
	}

	// Pasa la empresa y la persona que la creo. Genera un nuevo numero de orden
	private void crearOrden(final String numeroParteDucsa) {
		ProyectoBilpa.greetingService.obtenerNuevaOrdenSinGuardarEnBase(estacion, sesion, sesion, new AsyncCallback<Orden>() {

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener la orden");
				vpu.showPopUp();
			}

			public void onSuccess(Orden orden) {
				cargarDatosEmpresa(orden, numeroParteDucsa);
			}
		});
	}

	private void guardarOrden() {
		if (validarCampos()) {
			setearOrden(orden);
			String estado = "alta";
			popUp.show();
			// Orden orden = orden.copiarTodo();
			ProyectoBilpa.greetingService.guardarOrden(orden, estado,sesion, new AsyncCallback<Boolean>() {

				public void onSuccess(Boolean result) {

					if (!result) {
						ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener la orden");
						vpu.showPopUp();
					} else {
						if (sesion.getRol() == 1 || sesion.getRol() == 5) {
							IUListaOrdenes iuLO = new IUListaOrdenes(sesion);
							popUp.hide();
							IUMenuPrincipal.getInstancia().agregarWidgetAlMenu(iuLO.getPrincipalPanel());
						}
						if (sesion.getRol() == 2) {
							IUListaOrdenes iuLO = new IUListaOrdenes(sesion);
							popUp.hide();
							IUMenuPrincipalAdministrativo.getInstancia().agregarWidgetAlMenu(iuLO.getPrincipalPanel());
						}
					}
					popUp.hide();
				}
				public void onFailure(Throwable caught) {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar la orden");
					vpu.showPopUp();
				}
			});
		}
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
			rep.setOrden(orden);
			rep.setComentario("");
			rep.setActivo(activo);

			if (picoSeleccionado != null) 
			{
				if (rep.getTipo() == 1) {
					ReparacionSurtidor repSurt = (ReparacionSurtidor) rep;
					repSurt.agregarPico(picoSeleccionado.getNumeroPico());
				}
			}

			// if (!orden.validarReparacionExistente(rep))
			{
				fallaRep.setActivo(activo);
				setearFallaATabla(rep, fallaRep, comentario);
				orden.agregarReparacion(rep);
				listaReparaciones.add(rep);
			} 
			/*else
			{
				ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Ya a reportado ese activo");
				vpu.showPopUp();
			}*/
		}
	}

	private void quitarReparacion(int fila) {
		Reparacion rep = listaReparaciones.get(fila - 1);
		if (listaReparaciones.contains(rep)) {
			listaReparaciones.remove(rep);
		}
		orden.quitarReparacion(rep);
		tablaFallasReportadas.removeRow(fila);
		removerBotonDelMap(fila);
		
		if (rep.getPendiente() != null){
			iuRepararPendienteTitulo.getPendienteDataUI().getPendientes().add(rep.getPendiente().getPendienteData());
			reloadTituloPendientesAReparar(iuRepararPendienteTitulo.getPendienteDataUI());
		}
	}

	private void removerBotonDelMap(int fila) {
		buttonsRemoveMap.remove(fila);
		for (int i = fila; i <= buttonsRemoveMap.size(); i++) {
			buttonsRemoveMap.put(i, buttonsRemoveMap.get(i + 1));
		}
	}

	private int obtenerFilaSeleccionada(PushButton btn) {
		for (int i = 1; i <= buttonsRemoveMap.size(); i++) {
			if (buttonsRemoveMap.get(i).equals(btn)) {
				return i;
			}
		}
		return -1;
	}

	private void setearFallaATabla(final Reparacion rep, FallaReportada fallaRep, String comentario) {

		final int numRows = tablaFallasReportadas.getRowCount();
		TextBox txComentario = new TextBox();
		txComentario.setText(comentario);
		txComentario.setWidth("377");


		Image imbRemove = new Image("img/menos.png");
		final PushButton btnRemove = new PushButton(imbRemove);
		btnRemove.setSize("15px", "15px");
		imbRemove.setSize("15px", "15px");


		btnRemove.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				quitarReparacion(obtenerFilaSeleccionada(btnRemove));
			}
		});

		tablaFallasReportadas.setWidget(numRows, 1, new Label(rep.getFallaReportada().getDescripcion()));
		tablaFallasReportadas.setWidget(numRows, 2, txComentario);
		tablaFallasReportadas.setWidget(numRows, 3, btnRemove);

		buttonsRemoveMap.put(numRows, btnRemove);
		tablaFallasReportadas.setWidget(numRows, 0, new Label(rep
				.getActivo().toString()));
		if (numRows % 2 == 0) {
			tablaFallasReportadas.getRowFormatter().addStyleName(numRows, "FilaTabla1");
		} else {
			tablaFallasReportadas.getRowFormatter().addStyleName(numRows, "FilaTabla2");
		}
	}

	private Orden setearOrden(Orden nuevaOrden) {
		validarCampos();
		setearComentariosDeFallas();
		setearPrioridad();
		iuCorrectivoToolbar.setearTipoTrabajo();
		iuCorrectivoToolbar.setearCumplimiento();
		iuCorrectivoToolbar.setearTecnicoAsignadoAlta();
		setearPreventivo();
		return nuevaOrden;
	}

	private void setearPreventivo() {
		orden.setPreventivo(false);
	}

	private void setearPrioridad() 
	{
		orden.setPrioridad("Normal");
	}

	private void setearComentariosDeFallas() {
		for (int i = 0; i < listaReparaciones.size(); i++) {
			cargarComentariosDeLaTabla(i, (Reparacion) listaReparaciones.get(i));
		}
	}

	private void cargarComentariosDeLaTabla(int numeroFila, Reparacion rep) {
		TextBox TxComentario = (TextBox) tablaFallasReportadas.getWidget(numeroFila + 1, 2);
		String comentario = TxComentario.getText();
		rep.setComentario(comentario);
	}

	private void cancelarOrden() {
		popUp.show();
		ProyectoBilpa.greetingService.eliminarOrden(orden, sesion,
				new AsyncCallback<Boolean>() {

			public void onSuccess(Boolean result) {
				if (!result) {
					UtilUI
					.dialogBoxError("Error al eliminar la orden");

				} else {
					if (sesion.getRol() == 1 || sesion.getRol() == 5) {
						IUIngresoOrden iuLO = new IUIngresoOrden(sesion);
						IUMenuPrincipal.getInstancia()
						.agregarWidgetAlMenu(
								iuLO.getPrincipalPanel());
					}
					if (sesion.getRol() == 2) {
						IUIngresoOrden iuLO = new IUIngresoOrden(sesion);
						IUMenuPrincipalAdministrativo.getInstancia()
						.agregarWidgetAlMenu(
								iuLO.getPrincipalPanel());
					}
				}
				popUp.hide();
			}

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al Cancelar la Operacion");
				vpu.showPopUp();
			}
		});
	}

	private void obtenerOrdenesActivas() {
		ProyectoBilpa.greetingService.ordenesActivasDeUnaEmpresa(estacion,
				new AsyncCallback<ArrayList<DatoOrdenesActivasEmpresa>>() {

			public void onFailure(Throwable caught) {

			}

			public void onSuccess(
					ArrayList<DatoOrdenesActivasEmpresa> result) {
				cargarTablaOrdenesPendientes(result);

			}

		});

	}

	private void cargarTablaOrdenesPendientes(
			ArrayList<DatoOrdenesActivasEmpresa> result) {

		if (result.size() > 1) {
			VerticalPanel vpGrande = new VerticalPanel();
			Label lblOrdenesPendientesTitulo = new Label("Lista de órdenes pendientes");
			lblOrdenesPendientesTitulo.setStyleName("SubTitulo");
			vpGrande.setSpacing(5);
			//vpGrande.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

			FlexTable tableSoluciones = new FlexTable();
			tableSoluciones.setWidth("1150px");
			tableSoluciones.setCellSpacing(3);
			tableSoluciones.setCellPadding(1);
			agregarFilaTituloPendientes(tableSoluciones);

			int i = 1;
			for (DatoOrdenesActivasEmpresa s : result) {
				String numero = orden.getNumero() + "";
				if (!numero.equals(s.getNumero())) {
					tableSoluciones.setText(i, 0, s.getFecha());
					tableSoluciones.setText(i, 1, s.getNumero());
					tableSoluciones.setText(i, 2, s.getAutor());
					tableSoluciones.setText(i, 3, s.getTecnico());
					tableSoluciones.setText(i, 4, s.getEstado());
				}

				i++;
				if (i % 2 == 0) {
					tableSoluciones.getRowFormatter().addStyleName(i - 1,
							"FilaTabla1");
				} else {
					tableSoluciones.getRowFormatter().addStyleName(i - 1,
							"FilaTabla2");
				}
			}

			//vpGrande.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
			vpGrande.add(lblOrdenesPendientesTitulo);
			vpGrande.add(tableSoluciones);
			vPanelPrincipal_1.add(vpGrande);
			// vPanelPrincipal_1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			vPanelPrincipal_1.add(panelHVerHistorico);
		}
	}

	private void agregarFilaTituloPendientes(FlexTable ft) {
		// Setea los nombres de las columnas de la tabla
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

	private void cargarHistoricoDeEmpresa(Estacion empresa) {

		ProyectoBilpa.greetingService.historicoOrdenesFinalizadasEmpresa(
				empresa,
				new AsyncCallback<ArrayList<DatoConsultaHistoricoOrdenes>>() {

					public void onFailure(Throwable caught) {
						ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar el historico de la orden");
						vpu.showPopUp();
					}

					public void onSuccess(
							ArrayList<DatoConsultaHistoricoOrdenes> result) {
						if (result == null) {
							vPanelPrincipal_1.remove(panelVHistorico_2G);
						} else {
							DatoConsultaHistoricoOrdenes auxiliar = new DatoConsultaHistoricoOrdenes();
							for (int i = 0; i < result.size(); i++) {
								auxiliar = (DatoConsultaHistoricoOrdenes) result
										.get(i);
								historicoOrdenes.add(auxiliar);
							}

							if (result.size() > 0) {
								cargarTablaHistoricoOrdenes();

							}
						}
					}

				});

	}

	private void cargarTablaHistoricoOrdenes() {

		vpHistorico.setSpacing(10);

		//vpHistorico.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		Label lblTituloHisto = new Label("Historico de órdenes de la Estación");
		lblTituloHisto.setStyleName("SubTitulo");
		vpHistorico.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		vpHistorico.add(lblTituloHisto);

		for (DatoConsultaHistoricoOrdenes s : historicoOrdenes) {
			Date fecha = s.getFecha();
			String formattedValue = DateTimeFormat.getFormat(
					"EEEE d MMMM yyyy k:mm").format(fecha);

			// LABEL TITULO
			Label nroOrden = new Label(" Orden: ");
			Label nroOrdenValor = new Label(s.getNro());
			Label finalizada = new Label(" - Finalizada: ");
			Label finalizadaValor = new Label(formattedValue);
			HorizontalPanel hpTitulo = new HorizontalPanel();
			hpTitulo.setSpacing(5);
			hpTitulo.add(nroOrden);
			hpTitulo.add(nroOrdenValor);
			hpTitulo.add(finalizada);
			hpTitulo.add(finalizadaValor);

			nroOrden.setStyleName("Negrita");
			finalizada.setStyleName("Negrita");

			vpHistorico.add(hpTitulo);
			// LABEL TITULO

			FlexTable tableSoluciones = new FlexTable();
			tableSoluciones.setWidth("1150px");
			tableSoluciones.setCellSpacing(3);
			tableSoluciones.setCellPadding(1);
			agregarTituloOrdenesHistorico(tableSoluciones);

			int i = 1;
			for (Reparacion r : s.getReparaciones()) {
				for (Solucion sol : r.getSoluciones()) {
					tableSoluciones.setText(i, 0, r.getActivo().toString());
					tableSoluciones.setText(i, 1, formattedValue);
					tableSoluciones.setText(i, 2, sol.getFallaTecnica()
							.getDescripcion());
					tableSoluciones.setText(i, 3, sol.getTarea()
							.getDescripcion());

					i++;
					if (i % 2 == 0) {
						tableSoluciones.getRowFormatter().addStyleName(i - 1,
								"FilaTabla1");
					} else {
						tableSoluciones.getRowFormatter().addStyleName(i - 1,
								"FilaTabla2");
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
					//vPanelPrincipal_1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					vPanelPrincipal_1.add(vpHistorico);
				}else{
					vPanelPrincipal_1.remove(vpHistorico);
				}
			}			
		});
	}
	private boolean btnHistoricoApretado = false;

	private void agregarWidgetsVerHistorico() {
		//panelHVerHistorico.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		panelHVerHistorico.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		lblVerHistorico.setStyleName("Negrita");

		panelHVerHistorico.setSpacing(5);
		panelHVerHistorico.add(lblVerHistorico);
		panelHVerHistorico.add(btnVerHistorico);
	}

	private void agregarTituloOrdenesHistorico(FlexTable ft) {
		// Setea los nombres de las columnas de la tabla

		HTML activoHTML = new HTML("Fecha");
		activoHTML.setWidth("280");
		HTML fechaHTML = new HTML("Activo");
		fechaHTML.setWidth("280");
		HTML fallaHTML = new HTML("Falla Detectada");
		fallaHTML.setWidth("280");
		HTML tareaHTML = new HTML("Tarea Realizada");
		tareaHTML.setWidth("280");

		ft.getRowFormatter().setStyleName(0, "CabezalTabla");
		ft.setWidget(0, 0, fechaHTML);
		ft.setWidget(0, 1, activoHTML);
		ft.setWidget(0, 2, fallaHTML);
		ft.setWidget(0, 3, tareaHTML);

	}
	
	private void obtenerPendientes(){
		final IUAltaOrden instance = this;
		
		ProyectoBilpa.greetingService.obtenerPendientesDeEstacion(estacion.getId(), new AsyncCallback<PendienteDataUI>(){
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar pendientes");
				vpu.showPopUp();
			}

			public void onSuccess(PendienteDataUI result) {
				reloadTituloPendientesAReparar(result);
				// cargarReparacionesEnTabla();
			}
		});
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
