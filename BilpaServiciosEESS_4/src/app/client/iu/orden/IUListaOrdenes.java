package app.client.iu.orden;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.ListDataProvider;

import app.client.ProyectoBilpa;
import app.client.dominio.Orden;
import app.client.dominio.Persona;
import app.client.dominio.Tecnico;
import app.client.dominio.data.EstacionDataList;
import app.client.dominio.data.OrdenData;
import app.client.dominio.data.TecnicoData;
import app.client.iu.menu.IUMenuPrincipal;
import app.client.iu.widgets.CustomListBox;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.UtilOrden;
import app.client.utilidades.utilObjects.ButtonImageCell;
import app.client.utilidades.utilObjects.FileDownload;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;
import app.client.utilidades.utilObjects.SuggestBoxAdv;
import app.client.utilidades.utilObjects.filter.orden.ListaOrdenesData;


public class IUListaOrdenes extends Composite{

	private PopupCargando popUp = new PopupCargando("Cargando...");
	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private VerticalPanel vPanelTabla = new VerticalPanel();

	private Label lblTituloPrincipal = new Label("Correctivos activos");

	private Orden ordenSeleccionada;	
	private Persona sesion;
	private FileDownload file = new FileDownload();

	private HorizontalPanel hPanelRdb = new HorizontalPanel();
	private RadioButton rdbEstadoActivas = new RadioButton("Estado", "Correctivos activos");
	private RadioButton rdbEstadoInactivas = new RadioButton("Estado", "Correctivos inactivos");

	private Label lblCantidadOrdenes = new Label(" ");

	private Label lblAbrirOrden = new Label("Abrir correctivo nro ");
	private TextBox txtAbrirOrden = new TextBox();
	private Button btnNuevaOrden = new Button("Nuevo correctivo");

	private HorizontalPanel hP1 = new HorizontalPanel();
	private HorizontalPanel hP2 = new HorizontalPanel();

	private Image imgFiltrar = new Image("img/filter_add.png");
	private Image imgLimpiarFiltros = new Image("img/filter_delete.png");

	private PushButton btnFiltrar = new PushButton(imgFiltrar);
	private PushButton btnLimpiarFiltros = new PushButton(imgLimpiarFiltros);

	private MultiWordSuggestOracle oracleEstados = new MultiWordSuggestOracle();
	private SuggestBoxAdv filterSBoxEstado = new SuggestBoxAdv(oracleEstados);

	private MultiWordSuggestOracle oracleTecnicos = new MultiWordSuggestOracle();
	private SuggestBoxAdv filterSBoxTecnico = new SuggestBoxAdv(oracleTecnicos);

	private CustomListBox filterLBoxSello = new CustomListBox();

	private MultiWordSuggestOracle oracleLocalidades = new MultiWordSuggestOracle();
	private SuggestBoxAdv filterSBoxLocalidad = new SuggestBoxAdv(oracleLocalidades);

	private MultiWordSuggestOracle oracleEstaciones = new MultiWordSuggestOracle();
	private SuggestBoxAdv filterSBoxEstaciones = new SuggestBoxAdv(oracleEstaciones);

	private DateBox filterDBoxDesde = new DateBox();
	private DateBox filterDBoxHasta = new DateBox();

	private Label lblIndicadoresActivos = new Label(" ");
	private Label lblIndicadoresInactivos = new Label(" ");

	private ListDataProvider<OrdenData> dataProvider;
	private CellTable<OrdenData> cellTableOrdenes = new CellTable<OrdenData>();
	private ListaOrdenesData listaOrdenesData = new ListaOrdenesData();
	private ArrayList<OrdenData> ordenes = new ArrayList<OrdenData>();
	private OrdenData ordenFilter;

	private SimplePager.Resources pagerResources;
	private SimplePager pager;

	private GlassPopup glass = new GlassPopup();

	public VerticalPanel getPrincipalPanel() {
		return vPanelPrincipal;
	}


	public IUListaOrdenes(Persona persona)
	{
		sesion = persona;

		setearWidgets();

		obtenerListaOrdenesData();

		initPager();
		setPager(false);

		agregarWidgets();		

		obtenerOrdenes();
		cargarEventos();
		color();
	}

	private void obtenerListaOrdenesData() {
		popUp.show();
		ProyectoBilpa.greetingService.obtenerListaOrdenesData(new AsyncCallback<ListaOrdenesData>() {
			public void onFailure(Throwable caught) {
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener lista de ordenes data");
				vpu.showPopUp();
			}

			public void onSuccess(ListaOrdenesData result) {
				listaOrdenesData = result;
				setIndicadores();
				setSugBoxEstado();
				setSugBoxTecnico();
				setListBoxSello();
				setSugBoxLocalidades();
				setSugBoxEstacion();
			}
		});
	}


	private void setIndicadores() {
		lblIndicadoresActivos.setText( listaOrdenesData.getIndicadorIniciadas() + " Iniciadas | " + 
				listaOrdenesData.getIndicadorIniciadasDUCSA() + " Iniciadas Ducsa | " +
				listaOrdenesData.getIndicadorInspeccionPendiente() + " Inspección Pendiente | " +
				listaOrdenesData.getIndicadorReparadas() + " Reparadas " 				
				);

		lblIndicadoresInactivos.setText( listaOrdenesData.getIndicadorFinalizadas() + " Finalizadas | " + 
				listaOrdenesData.getIndicadorAnuladas() + " Anuladas " 
				);
	}

	private void setSugBoxEstado() {
		oracleEstados.clear();
		ArrayList<String> estados = listaOrdenesData.getEstadoActivos();
		if(rdbEstadoInactivas.getValue()){
			estados = listaOrdenesData.getEstadoInactivos();
		}

		for (int i = 0; i < estados.size(); ++i) {
			oracleEstados.add(estados.get(i));
		}
	}

	private void setSugBoxTecnico() { // SETEO WIDGET FILTRO DE TECNICOS
		oracleTecnicos.clear();

		for (TecnicoData t : listaOrdenesData.getTecnicosData()){
			oracleTecnicos.add(t.getNombreCompleto());
		}
	} 

	private void setListBoxSello() { // SETEO DE LIST BOX DE SELLO
		filterLBoxSello.addItem("ANCAP", 1+"");
		filterLBoxSello.addItem("PETROBRAS", 2+"");
	}

	private void setSugBoxLocalidades(){
		oracleLocalidades.clear();

		for (String s : listaOrdenesData.getLocalidades()){
			oracleLocalidades.add(s);
		}
	}

	private void setSugBoxEstacion(){
		oracleEstaciones.clear();

		for (EstacionDataList e : listaOrdenesData.getEstacionesData()){
			oracleEstaciones.add(e.getNombre());
		}
	}

	private void initPager() {
		pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, true, 100, true);
		pager.setDisplay(cellTableOrdenes);
	}

	private void cargarEventos() 
	{
		rdbEstadoActivas.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				cambiarOrdenes(false);
				rdbEstadoActivas.setStyleName("Negrita");
				rdbEstadoInactivas.removeStyleName("Negrita");
			}
		});

		rdbEstadoInactivas.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				cambiarOrdenes(true);
				rdbEstadoInactivas.setStyleName("Negrita");
				rdbEstadoActivas.removeStyleName("Negrita");
			}
		});

		txtAbrirOrden.addKeyPressHandler(new KeyPressHandler()
		{
			public void onKeyPress(KeyPressEvent event) 
			{
				if (KeyCodes.KEY_ENTER == event.getNativeEvent().getKeyCode()) {
					buscarOrdenParaCargar();
				}
			}
		});

		btnNuevaOrden.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				IUIngresoOrden iur = new IUIngresoOrden(sesion);
				if (sesion.getRol() != 3){
					IUMenuPrincipal.getInstancia().agregarWidgetAlMenu(iur.getPrincipalPanel());
				}
			}
		});
		
		btnFiltrar.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				obtenerOrdenes();
			}
		});

		btnLimpiarFiltros.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				limpiarFiltros();
			}
		});

		filterLBoxSello.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				SelectElement selectElement = SelectElement.as(filterLBoxSello.getElement());
				/*if(Integer.valueOf(filterLBoxSello.getValue(filterLBoxSello.getSelectedIndex())) == -1){
					selectElement.getStyle().setColor("grey");
				}else{
					selectElement.getStyle().setColor("black");
				}*/
			}
		});
	}

	private void color() 
	{
		lblCantidadOrdenes.setStyleName("Negrita");
		lblAbrirOrden.setStyleName("Negrita");
	}

	private void setearWidgets() 
	{
		rdbEstadoActivas.setValue(true);
		rdbEstadoActivas.setStyleName("Negrita");
		lblTituloPrincipal.setStyleName("Titulo");

		filterSBoxEstado.setPlaceHolderText("Filtrar estado");
		filterSBoxEstado.setTitle("Filtrar estado");
		filterSBoxTecnico.setPlaceHolderText("Filtrar técnico");
		filterSBoxTecnico.setTitle("Filtrar técnico");
		filterSBoxLocalidad.setPlaceHolderText("Filtrar localidad");
		filterSBoxLocalidad.setTitle("Filtrar localidad");
		filterSBoxEstaciones.setPlaceHolderText("Filtrar estación");
		filterSBoxEstaciones.setTitle("Filtrar estación");

		filterDBoxDesde.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("yyyy-MM-dd")));
		filterDBoxHasta.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("yyyy-MM-dd")));

		//cellTableOrdenes.setWidth("100%");
		btnNuevaOrden.setTitle("Ir a nuevo correctivo");
		btnNuevaOrden.setWidth("135px");
		
		txtAbrirOrden.setTitle("Ingrese un numero de orden para abrir y presione ENTER");
		txtAbrirOrden.setWidth("60");
	}

	private void agregarWidgets() 
	{
		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hPanelRdb.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hP2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		vPanelPrincipal.add(lblTituloPrincipal);
		vPanelPrincipal.setSpacing(20);
		vPanelTabla.setSpacing(5);

		vPanelTabla.add(hPanelRdb);

		filtros();

		vPanelTabla.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelTabla.add(cellTableOrdenes);		
		vPanelTabla.add(pager);

		hPanelRdb.setWidth("100%");		
		hP2.setSpacing(3);

		hPanelRdb.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		hP1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

		hPanelRdb.add(hP1);		
		hPanelRdb.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		hPanelRdb.add(hP2);

		hP1.add(rdbEstadoActivas);
		hP1.add(rdbEstadoInactivas);

		hP1.setSpacing(5);
		hP1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hP1.add(lblIndicadoresActivos);
		hP1.add(lblIndicadoresInactivos);

		lblIndicadoresInactivos.setVisible(false);

		hP2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hP2.add(lblAbrirOrden);
		hP2.add(txtAbrirOrden);
		
		if (sesion.getRol() != 3){
			hP2.add(btnNuevaOrden);
		}
		
		hP2.add(lblCantidadOrdenes);

		vPanelPrincipal.add(vPanelTabla);
		vPanelPrincipal.add(file);

	}

	private void filtros() {
		HorizontalPanel hpFiltros = new HorizontalPanel();

		btnFiltrar.setTitle("Filtrar...");

		imgLimpiarFiltros.setSize("20px", "20px");
		imgFiltrar.setSize("20px", "20px");
		btnFiltrar.setSize("20px", "20px");
		btnLimpiarFiltros.setSize("20px", "20px");

		btnLimpiarFiltros.setTitle("Limpiar filtros...");

		hpFiltros.setSpacing(2);

		hpFiltros.add(filterSBoxEstado);
		hpFiltros.add(filterSBoxEstaciones);
		hpFiltros.add(filterLBoxSello);
		hpFiltros.add(filterSBoxLocalidad);
		hpFiltros.add(filterSBoxTecnico);
		hpFiltros.add(filterDBoxDesde);
		hpFiltros.add(filterDBoxHasta);
		
		HorizontalPanel hp1 = new HorizontalPanel();
		HorizontalPanel hp2 = new HorizontalPanel();
		HorizontalPanel hp3 = new HorizontalPanel();
		hp2.setWidth("8px");
		hp3.setWidth("8px");
		
		hp1.add(hp3);
		hp1.add(btnFiltrar);
		hp1.add(hp2);
		hp1.add(btnLimpiarFiltros);
		
		hpFiltros.add(hp1);

		filterSBoxEstado.setSize("165px", "27px");
		filterSBoxTecnico.setSize("165px", "27px");
		filterSBoxEstaciones.setSize("165px", "27px");
		filterSBoxLocalidad.setSize("165px", "27px");
		filterDBoxDesde.setSize("165px", "27px");
		filterDBoxHasta.setSize("165px", "27px");

		filterLBoxSello.addItem("Filtrar sello", "-1");

		filterDBoxDesde.getElement().setAttribute("placeHolder", "Fecha inicio desde");
		filterDBoxHasta.getElement().setAttribute("placeHolder", "Fecha inicio hasta");

		vPanelTabla.add(hpFiltros);
	}

	private void crearTabla() {
		TextColumn<OrdenData> numeroColumn = new TextColumn<OrdenData>() {
			@Override
			public String getValue(OrdenData e) {
				return e.getNumero()+"";
			}
		};

		TextColumn<OrdenData> estadoColumn = new TextColumn<OrdenData>() {
			@Override
			public String getValue(OrdenData e) {
				return e.getEstado();
			}
		};

		TextColumn<OrdenData> tecnicoColumn = new TextColumn<OrdenData>() {
			@Override
			public String getValue(OrdenData e) {
				return e.getTecnico()+"";
			}
		};

		TextColumn<OrdenData> fechaIniColumn = new TextColumn<OrdenData>() {
			@Override
			public String getValue(OrdenData e) {
				return e.getFechaInicio().toString();
			}
		};

		TextColumn<OrdenData> fechaCumplColumn = new TextColumn<OrdenData>() {
			@Override
			public String getValue(OrdenData e) {
				if (e.getFechaCumplimiento() != null){
					return e.getFechaCumplimiento().toString().substring(0, 10);
				}
				return "";
			}
		};

		TextColumn<OrdenData> estacionColumn = new TextColumn<OrdenData>() {
			@Override
			public String getValue(OrdenData e) {
				return e.getEstacion();
			}
		};

		TextColumn<OrdenData> plazoColumn = new TextColumn<OrdenData>() {
			@Override
			public String getValue(OrdenData e) {
				return e.getPlazo().toString();

			}
		};

		TextColumn<OrdenData> localidadColumn = new TextColumn<OrdenData>() {
			@Override
			public String getValue(OrdenData e) {
				return e.getLocalidad();
			}
		};

		TextColumn<OrdenData> selloColumn = new TextColumn<OrdenData>() {
			@Override
			public String getValue(OrdenData e) {
				return e.getSello();
			}
		};

		final Column<OrdenData, String> abrirOrdenColumn = new Column<OrdenData, String>(new ButtonImageCell()) {
			@Override
			public String getValue(OrdenData orden) {
				return "img/look.png";
			}
		};

		final Column<OrdenData, String> pdfBilpaColumn = new Column<OrdenData, String>(new ButtonImageCell()) {
			@Override
			public String getValue(OrdenData orden) {
				return "img/pdf2.png";
			}
		};

		final Column<OrdenData, String> pdfOperadorColumn = new Column<OrdenData, String>(new ButtonImageCell()) {
			@Override
			public String getValue(OrdenData orden) {
				return "img/pdf2.png";
			}
		};

		pdfOperadorColumn.setFieldUpdater(new FieldUpdater<OrdenData, String>() {
			public void update(int index, OrdenData orden, String value) {
				abrirPdf(orden.getNumero(), false);
			}
		});

		pdfBilpaColumn.setFieldUpdater(new FieldUpdater<OrdenData, String>() {
			public void update(int index, OrdenData orden, String value) {
				abrirPdf(orden.getNumero(), true);
			}
		});

		abrirOrdenColumn.setFieldUpdater(new FieldUpdater<OrdenData, String>() {
			public void update(int index, OrdenData orden, String value) {
				if(rdbEstadoActivas.getValue()){
					buscarOrdenSeleccionada(orden.getNumero());
				}else{
					buscarOrdenAbrirPdf(orden.getNumero());
				}
			}
		});

		int columnCount = cellTableOrdenes.getColumnCount();
		for(int i = 0 ; i < columnCount ; i ++){
			cellTableOrdenes.removeColumn(0);
		}
		// COLUMNAS AGREGADAS
		cellTableOrdenes.addColumn(numeroColumn,"Número");
		cellTableOrdenes.addColumn(estadoColumn,"Estado");
		cellTableOrdenes.addColumn(estacionColumn,"Estación");
		cellTableOrdenes.addColumn(selloColumn,"Sello");
		cellTableOrdenes.addColumn(localidadColumn,"Localidad");
		cellTableOrdenes.addColumn(tecnicoColumn,"Técnico");
		cellTableOrdenes.addColumn(fechaIniColumn,"Inicio");
		cellTableOrdenes.addColumn(fechaCumplColumn,"Cumplimiento");
		cellTableOrdenes.addColumn(plazoColumn,"Plazo");
		if (rdbEstadoActivas.getValue()){
			cellTableOrdenes.addColumn(abrirOrdenColumn, "Abrir");
		}
		cellTableOrdenes.addColumn(pdfBilpaColumn, "Bilpa");
		cellTableOrdenes.addColumn(pdfOperadorColumn, "Operador");

		numeroColumn.setSortable(true);
		estadoColumn.setSortable(true);
		tecnicoColumn.setSortable(true);
		fechaIniColumn.setSortable(true);
		estacionColumn.setSortable(true);
		plazoColumn.setSortable(true);
		localidadColumn.setSortable(true);
		selloColumn.setSortable(true);
		fechaCumplColumn.setSortable(true);
		abrirOrdenColumn.setSortable(false);
		pdfBilpaColumn.setSortable(false);
		pdfOperadorColumn.setSortable(false);

		dataProvider = new ListDataProvider<OrdenData>();
		dataProvider.addDataDisplay(cellTableOrdenes);
		List<OrdenData> list = cargarOrdenes();

		ListHandler<OrdenData> columnSortHandlerNumero = new ListHandler<OrdenData>(list);
		ListHandler<OrdenData> columnSortHandlerEstado = new ListHandler<OrdenData>(list);
		ListHandler<OrdenData> columnSortHandlerTecnico = new ListHandler<OrdenData>(list);
		ListHandler<OrdenData> columnSortHandlerFecIni = new ListHandler<OrdenData>(list);
		ListHandler<OrdenData> columnSortHandlerFecCump = new ListHandler<OrdenData>(list);
		ListHandler<OrdenData> columnSortHandlerEstacion = new ListHandler<OrdenData>(list);
		ListHandler<OrdenData> columnSortHandlerPlazo = new ListHandler<OrdenData>(list);
		ListHandler<OrdenData> columnSortHandlerLocalidad = new ListHandler<OrdenData>(list);
		ListHandler<OrdenData> columnSortHandlerSello = new ListHandler<OrdenData>(list);

		columnSortHandlerNumero.setComparator(numeroColumn,new Comparator<OrdenData>() {
			public int compare(OrdenData o1, OrdenData o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return Integer.valueOf(o1.getNumero()).compareTo(o2.getNumero());
				}
				return -1;
			}
		});

		columnSortHandlerEstado.setComparator(estadoColumn,new Comparator<OrdenData>() {
			public int compare(OrdenData o1, OrdenData o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? String.valueOf(o1.getEstado()).compareTo(o2.getEstado()+"") : 1;
				}
				return -1;
			}
		});

		columnSortHandlerTecnico.setComparator(tecnicoColumn,new Comparator<OrdenData>() {
			public int compare(OrdenData o1, OrdenData o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? String.valueOf(o1.getTecnico()).compareTo(o2.getTecnico()+"") : 1;
				}
				return -1;
			}
		});

		columnSortHandlerFecIni.setComparator(fechaIniColumn,new Comparator<OrdenData>() {
			public int compare(OrdenData o1, OrdenData o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? String.valueOf(o1.getFechaInicio()).compareTo(o2.getFechaInicio()+"") : 1;
				}
				return -1;
			}
		});

		columnSortHandlerFecCump.setComparator(fechaCumplColumn,new Comparator<OrdenData>() {
			public int compare(OrdenData o1, OrdenData o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? String.valueOf(o1.getFechaCumplimiento()).compareTo(o2.getFechaCumplimiento()+"") : 1;
				}
				return -1;
			}
		});

		columnSortHandlerEstacion.setComparator(estacionColumn,new Comparator<OrdenData>() {
			public int compare(OrdenData o1, OrdenData o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? String.valueOf(o1.getEstacion()).compareTo(o2.getEstacion()+"") : 1;
				}
				return -1;
			}
		});

		columnSortHandlerPlazo.setComparator(plazoColumn,new Comparator<OrdenData>() {
			public int compare(OrdenData o1, OrdenData o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return o1.getPlazo().toString().compareTo(o2.getPlazo().toString());
				}
				return -1;
			}
		});

		columnSortHandlerLocalidad.setComparator(localidadColumn,new Comparator<OrdenData>() {
			public int compare(OrdenData o1, OrdenData o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? String.valueOf(o1.getLocalidad()).compareTo(o2.getLocalidad()+"") : 1;
				}
				return -1;
			}
		});

		columnSortHandlerSello.setComparator(selloColumn,new Comparator<OrdenData>() {
			public int compare(OrdenData o1, OrdenData o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? String.valueOf(o1.getSello()).compareTo(o2.getSello()+"") : 1;
				}
				return -1;
			}
		});


		cellTableOrdenes.addColumnSortHandler(columnSortHandlerNumero);
		cellTableOrdenes.addColumnSortHandler(columnSortHandlerEstado);
		cellTableOrdenes.addColumnSortHandler(columnSortHandlerTecnico);
		cellTableOrdenes.addColumnSortHandler(columnSortHandlerFecIni);
		cellTableOrdenes.addColumnSortHandler(columnSortHandlerFecCump);
		cellTableOrdenes.addColumnSortHandler(columnSortHandlerEstacion);
		cellTableOrdenes.addColumnSortHandler(columnSortHandlerPlazo);
		cellTableOrdenes.addColumnSortHandler(columnSortHandlerLocalidad);
		cellTableOrdenes.addColumnSortHandler(columnSortHandlerSello);
	}





	public List<OrdenData> cargarOrdenes() {
		dataProvider.getList().clear();
		List<OrdenData> list = dataProvider.getList();

		for (OrdenData orden : ordenes) {
			list.add(orden);
		}
		return list;
	}

	private void buscarOrdenAbrirPdf(int numeroOrden)
	{	
		popUp.show();
		ProyectoBilpa.greetingService.buscarOrden(numeroOrden, new AsyncCallback<Orden>() {
			public void onFailure(Throwable caught) 
			{
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar la orden");
				vpu.showPopUp();
			}

			public void onSuccess(Orden result) 
			{
				if(result!=null)
				{
					abrirPdf(result.getNumero(), true);
					popUp.hide();
				}
				else
				{
					ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "No se encontro la orden");
					vpu.showPopUp();
				}
			}
		});
	}

	private void abrirPdf(int numero, Boolean pdfBilpa) 
	{
		ProyectoBilpa.greetingService.crearPDF(numero, pdfBilpa, new AsyncCallback<String>() {

			public void onFailure(Throwable caught) 
			{
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al crear PDF");
				vpu.showPopUp();
			}

			public void onSuccess(String result) 
			{
				if (result.equals(""))
				{
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al crear PDF");
					vpu.showPopUp();						
				}
				else
				{
					file.download();
				}
			}
		});
	}

	private void buscarOrdenSeleccionada(int numeroOrden) {	
		popUp.show();
		ProyectoBilpa.greetingService.buscarOrden(numeroOrden, new AsyncCallback<Orden>() {
			public void onFailure(Throwable caught) {
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar la orden");
				vpu.showPopUp();
			}

			public void onSuccess(Orden result) {
				if(result != null) {
					txtAbrirOrden.setText("");
					abrirOrdenSeleccionada(result);
					popUp.hide();
				} else {
					popUp.hide();
					ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "No se encontro la orden");
					vpu.showPopUp();
				}
			}
		});
	}

	private void abrirOrdenSeleccionada(Orden result) {
		if(sesion.getRol()==3 && !sesion.equals(result.getTecnicoAsignado()))
		{
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "No se puede abrir el correctivo nro: " + result.getNumero() + "\nLos Técnicos solo pueden ver correctivos asignados a si mismos");
			vpu.showPopUp();
			return;
		}
		if (result.getEstadoOrden() == 3 || result.getEstadoOrden() == 4)//cerrada o anulada
		{
			abrirPdf(result.getNumero(), true);
		}
		else
		{
			ordenSeleccionada = result;
			UtilOrden.cargarOrdenSeleccionada(result, sesion);						
		}
	}

	private void cambiarOrdenes(boolean inactivas)
	{
		String texto = inactivas ? "inactivas" : "activas";
		lblTituloPrincipal.setText("Ordenes " + texto);

		if(inactivas){
			lblIndicadoresInactivos.setVisible(true);
			lblIndicadoresActivos.setVisible(false);
		}else{
			lblIndicadoresActivos.setVisible(true);
			lblIndicadoresInactivos.setVisible(false);
		}
		setSugBoxEstado();
		setSugBoxTecnico();
		setPager(inactivas);
		obtenerOrdenes();
	}

	private void obtenerOrdenes()	
	{
		popUp.show();
		setearFiltros();
		ProyectoBilpa.greetingService.obtenerOrdenes(ordenFilter, new AsyncCallback<ArrayList<OrdenData>>() {
			public void onFailure(Throwable caught) 
			{
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener ordenes");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<OrdenData> result) 
			{
				ordenes = result;
				crearTabla();
				popUp.hide();
			}
		});
	}

	private void setearFiltros(){
		ordenFilter = new OrdenData();
		ordenFilter.setActiva(rdbEstadoActivas.getValue());

		if (sesion.getRol() == 3){
			ordenFilter.setTecnico(((Tecnico)sesion).getNombreDeUSuario());
		}

		if(listaOrdenesData.getEstadoActivos().contains(filterSBoxEstado.getValue()) || listaOrdenesData.getEstadoInactivos().contains(filterSBoxEstado.getValue())){
			ordenFilter.setEstado(UtilOrden.getEstadoId(filterSBoxEstado.getValue()));
		}

		if (filterSBoxTecnico.getValue() != ""){  // LIST BOX DE TECNICO

			TecnicoData aux = new TecnicoData();

			for (TecnicoData t : listaOrdenesData.getTecnicosData()){

				if (t.getNombreCompleto().contains(filterSBoxTecnico.getValue())){
					aux = t;
				}
			}
			String nomUsu = aux.getNombreUsuario();
			ordenFilter.setTecnico(nomUsu);
		}

		if (filterLBoxSello.getSelectedIndex() > 0){ //  LIST BOX DE SELLO
			ordenFilter.setSello(filterLBoxSello.getItemText(filterLBoxSello.getSelectedIndex()));
		}

		if (filterSBoxLocalidad.getValue() != ""){  // LIST BOX DE LOCALIDAD SETEADO

			for (String loc : listaOrdenesData.getLocalidades()){
				if (loc.contains(filterSBoxLocalidad.getValue()))
					ordenFilter.setLocalidad(loc);
			}
		}

		if (filterSBoxEstaciones.getValue() != ""){

			for (EstacionDataList e : listaOrdenesData.getEstacionesData()){

				if (e.getNombre().contains(filterSBoxEstaciones.getValue())){
					ordenFilter.setEstacion(e.getNombre());				
					break;
				}
			}
		}

		if (filterDBoxDesde.getValue() != null){
			ordenFilter.setFechaInicio(DateTimeFormat.getFormat("yyyy-MM-dd").format(filterDBoxDesde.getValue()));
		}

		if (filterDBoxHasta.getValue() != null){
			ordenFilter.setFechaFin(filterDBoxHasta.getValue());
		}
	}	

	private void limpiarFiltros() {
		filterSBoxEstado.setText("");
		filterSBoxTecnico.setText("");
		filterLBoxSello.clear();
		filterLBoxSello.addItem("Filtrar sello", "-1");
		filterSBoxLocalidad.setText("");
		filterSBoxEstaciones.setText("");
		filterDBoxDesde.setValue(null);
		filterDBoxHasta.setValue(null);

		obtenerOrdenes();
	}

	private void setPager(boolean inactivas){
		if(inactivas){
			pager.setVisible(true);
			pager.setPageSize(50);
		}else{
			pager.setVisible(false);
			pager.setPageSize(1000);
		}
	}

	private void buscarOrdenParaCargar() 
	{
		if (validarNumeroOrden())
		{
			int numeroOrden = Integer.valueOf(txtAbrirOrden.getText()); 
			buscarOrdenSeleccionada(numeroOrden);
		}
		else
		{
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "El numero de orden debe ser un numero");
			vpu.showPopUp();
			txtAbrirOrden.setFocus(true);
			txtAbrirOrden.setSelectionRange(0, txtAbrirOrden.getText().length());
		}
	}

	private boolean validarNumeroOrden() 
	{
		String text = txtAbrirOrden.getText();
		if (text.length() > 0)
		{
			try
			{
				Integer.valueOf(text);
			}
			catch(Exception e)
			{
				return false;
			}
		}
		else
		{
			return false;
		}
		return true;
	}

}


