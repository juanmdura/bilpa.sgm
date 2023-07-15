package app.client.iu.pendientes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;

import app.client.ProyectoBilpa;
import app.client.dominio.EstadoPendiente;
import app.client.dominio.Orden;
import app.client.dominio.Persona;
import app.client.dominio.Sello;
import app.client.dominio.data.PendienteData;
import app.client.dominio.data.PendienteDataUI;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.UtilOrden;
import app.client.utilidades.utilObjects.ButtonImageCell;
import app.client.utilidades.utilObjects.FileDownload;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;
import app.client.utilidades.utilObjects.SuggestBoxAdv;
import app.client.utilidades.utilObjects.filter.FilteredListDataProvider;
import app.client.utilidades.utilObjects.filter.IFilter;
import app.client.utilidades.utilObjects.filter.TextBoxFilter;

public class IUPendientes extends Composite {

	private Label lblTitulo;

	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private HorizontalPanel hPanelPrincipal = new HorizontalPanel();

	private GlassPopup glass = new GlassPopup();
	private PopupCargando popUp = new PopupCargando();

	private CellTable<PendienteData> tabla = new CellTable<PendienteData>();
	private SimplePager.Resources pagerResources;
	private SimplePager pager;

	private RadioButton rdbIni = new RadioButton("Estado", "Pendientes iniciados");
	private RadioButton rdbRep = new RadioButton("Estado", "Pendientes reparados");
	private RadioButton rdbCorrAsignado = new RadioButton("Estado", "Pendientes con correctivo asignado");
	private RadioButton rdbDesc = new RadioButton("Estado", "Pendientes descartados");

	private Label lblIndicadores = new Label("");

	private Image imgFiltrar = new Image("img/filter_add.png");
	private Image imgLimpiarFiltros = new Image("img/filter_delete.png");

	private PushButton btnFiltrar = new PushButton(imgFiltrar);
	private PushButton btnLimpiarFiltros = new PushButton(imgLimpiarFiltros);

	private MultiWordSuggestOracle oracleEstacion = new MultiWordSuggestOracle();
	private SuggestBoxAdv filterEstacion = new SuggestBoxAdv(oracleEstacion);

	private MultiWordSuggestOracle oracleActivo = new MultiWordSuggestOracle();
	private SuggestBoxAdv filterActivo = new SuggestBoxAdv(oracleActivo);

	private TextBoxFilter filterBox = new TextBoxFilter();

	private Button btnAsignarCorrectivoSeleccionados = new Button("Asignar correctivo");
	private Button btnDescartarSeleccionados = new Button("Descartar");
	private Button btnAgregar = new Button("Agregar");

	private Persona sesion;
	private int idSello;
	private List<PendienteData> lista;
	private PendienteDataUI pendienteDataUi;
	private PendienteData pendienteFilter = new PendienteData(); 

	private FileDownload file = new FileDownload();

	private boolean inicio = true;
	private Set<PendienteData> seleccionados;

	public int getIdSello() {
		return idSello;
	}

	public Persona getSesion() {
		return sesion;
	}

	public IUPendientes(Persona sesion, int idSello) {
		super();
		this.sesion = sesion;
		this.idSello = idSello;
		lblTitulo = new Label("Pendientes " + Sello.getSelloById(idSello));

		eventos();
		getPendientes();
		set();
		setearWidgets();
		agregarWidgets();
	}

	private void setIndicadores() {
		lblIndicadores.setText(  " Iniciados " + pendienteDataUi.getCantidadIniciados() + 
							   " | Reparados " + pendienteDataUi.getCantidadReparados() + 
							   " | Descartados " + pendienteDataUi.getCantidadDescartados() +
							   " | Asignados a correctivo " + pendienteDataUi.getCantidadAsignados());
	}

	public void getPendientes() {
		popUp.show();
		ProyectoBilpa.greetingService.obtenerPendientes(idSello, getSelectedRDB(), new AsyncCallback<PendienteDataUI>() {
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener pendientes");
				vpu.showPopUp();
			}

			public void onSuccess(PendienteDataUI result) {
				popUp.hide();
				pendienteDataUi = result;
				setSugBoxEstacion();
				setSugBoxActivo();
				setIndicadores();
				load(result.getPendientes());	
			}
		});
	}

	private void filtrar(){
		setearFiltros();

		List<PendienteData> listaFiltrada = new ArrayList<PendienteData>();
		for (PendienteData pd : lista) {
			if (filtraEstacion(pd) && filtraActivo(pd) && filtraTexto(pd)){
				listaFiltrada.add(pd);
			}
		}
		load(listaFiltrada);
	}

	private boolean filtraEstacion(PendienteData pd){
		return !filtroEstacionOn() || cumpleConFiltroEstacion(pd);
	}

	private boolean filtraActivo(PendienteData pd){
		return !filtroActivoOn() || cumpleConFiltroActivo(pd);
	}

	private boolean filtraTexto(PendienteData pd){
		return !filtroTextoOn() || cumpleConFiltroTexto(pd);
	}

	private boolean cumpleConFiltroTexto(PendienteData pd) {
		return pd.getComentario().contains(filterBox.getText());
	}

	private boolean cumpleConFiltroActivo(PendienteData pd) {
		return pd.getActivo().contains(filterActivo.getText());
	}

	private boolean cumpleConFiltroEstacion(PendienteData pd) {
		return pd.getEmpresa().contains(filterEstacion.getText());
	}

	private boolean filtroTextoOn() {
		return !filterBox.getText().equals("");
	}

	private boolean filtroActivoOn() {
		return !filterActivo.getText().equals("");
	}

	private boolean filtroEstacionOn() {
		return !filterEstacion.getText().equals("");
	}

	private void load(List<PendienteData> result) {
		lista = result;
		if (inicio){
			inicio = false;
			crearTabla();
		} else {
			cargarLista();
		}
	}

	private EstadoPendiente getSelectedRDB(){
		if (rdbIni.getValue())return EstadoPendiente.INICIADO;
		if (rdbDesc.getValue())return EstadoPendiente.DESCARTADO;
		if (rdbCorrAsignado.getValue())return EstadoPendiente.CORRECTIVO_ASIGNADO;
		if (rdbRep.getValue())return EstadoPendiente.REPARADO;
		return EstadoPendiente.INICIADO;
	}

	private void setSugBoxEstacion(){
		oracleEstacion.clear();

		for (String e : pendienteDataUi.getEstaciones()){
			oracleEstacion.add(e);
		}
	}

	private void setSugBoxActivo(){
		oracleActivo.clear();

		for (String e : pendienteDataUi.getActivos()){
			oracleActivo.add(e);
		}
	}

	private void setearFiltros(){
		if (filterEstacion.getValue() != ""){
			for (String e : pendienteDataUi.getEstaciones()){
				if (e.contains(filterEstacion.getValue())){
					pendienteFilter.setEmpresa(e);
				}
			}
		}

		if (filterActivo.getValue() != ""){
			for (String a : pendienteDataUi.getActivos()){
				if (a.contains(filterActivo.getValue())){
					pendienteFilter.setActivo(a);
				}
			}
		}
	}	

	final IUPendientes instance = this;

	private void setNegrita() {
		if (rdbIni.getValue()){
			rdbIni.setStyleName("Negrita");
		} else {
			rdbIni.removeStyleName("Negrita");
		}
		
		if (rdbRep.getValue()){
			rdbRep.setStyleName("Negrita");
		} else {
			rdbRep.removeStyleName("Negrita");
		}
		
		if (rdbDesc.getValue()){
			rdbDesc.setStyleName("Negrita");
		} else {
			rdbDesc.removeStyleName("Negrita");
		}
		
		if (rdbCorrAsignado.getValue()){
			rdbCorrAsignado.setStyleName("Negrita");
		} else {
			rdbCorrAsignado.removeStyleName("Negrita");
		}
	}

	private void eventos() {
		ClickHandler rdbClickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				inicio = true;
				vPanelPrincipal.remove(tabla);
				vPanelPrincipal.remove(pager);
				tabla = new CellTable<PendienteData>();

				addTabla();

				lista = new ArrayList<PendienteData>();
				listaFiltrada = getListaFiltrada();
				getPendientes();
				setNegrita();
				
				if (rdbIni.getValue()){
					btnAgregar.setVisible(true);
					btnAsignarCorrectivoSeleccionados.setVisible(true);
					btnDescartarSeleccionados.setVisible(true);

				} else {
					btnAgregar.setVisible(false);
					btnAsignarCorrectivoSeleccionados.setVisible(false);
					btnDescartarSeleccionados.setVisible(false);
				}
			}

		};
		rdbIni.addClickHandler(rdbClickHandler);
		rdbDesc.addClickHandler(rdbClickHandler);
		rdbRep.addClickHandler(rdbClickHandler);
		rdbCorrAsignado.addClickHandler(rdbClickHandler);

		btnFiltrar.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				filtrar();	
			}
		});

		btnLimpiarFiltros.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				getPendientes();
				limpiarFiltros();
			}
		});

		btnAgregar.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				agregarEditar(instance, null);
			}
		});

		btnAsignarCorrectivoSeleccionados.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (seleccionados != null && seleccionados.size() > 0){
					List<PendienteData> pendientes = new ArrayList<PendienteData>();
					pendientes.addAll(seleccionados);
					dialogoAsignarCorrectivo(instance, pendientes);
				} else {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe seleccionar al menos un pendiente");
					vpu.showPopUp();
				}
			}
		});

		btnDescartarSeleccionados.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (seleccionados != null && seleccionados.size() > 0){
					List<PendienteData> pendientes = new ArrayList<PendienteData>();
					pendientes.addAll(seleccionados);
					dialogoDescartar(instance, pendientes);
				} else {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe seleccionar al menos un pendiente");
					vpu.showPopUp();
				}
			}
		});
	}

	private void limpiarFiltros() {
		filterEstacion.setText("");
		filterBox.setText("");
		filterActivo.setText("");
	}

	private void set() {
		titulo();

		rdbIni.setValue(true);
		rdbIni.setStyleName("Negrita");
		
		getPager();

		filterBox.getElement().setAttribute("placeHolder", "Filtrar textos");
		filterBox.setTitle("Filtrar textos");

		filterActivo.getElement().setAttribute("placeHolder", "Filtrar activos");
		filterActivo.setTitle("Filtrar activos");

		filterEstacion.setPlaceHolderText("Filtrar estación");
		filterEstacion.setTitle("Filtrar estación");
	}

	private void getPager() {
		pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(tabla);
		pager.setPageSize(16);
	}

	private void titulo() {
		lblTitulo.setStyleName("Titulo");
	}

	private void agregarWidgets() {
		vPanelPrincipal.add(file);
		vPanelPrincipal.setSpacing(20);
		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal.add(hPanelPrincipal);
		hPanelPrincipal.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hPanelPrincipal.add(lblTitulo);
		hPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		HorizontalPanel hpRadios = new HorizontalPanel();
		HorizontalPanel hpAcciones = new HorizontalPanel();
		hpAcciones.setWidth("100%");

		HorizontalPanel hp2 = new HorizontalPanel();
		HorizontalPanel hp3 = new HorizontalPanel();
		HorizontalPanel hp4 = new HorizontalPanel();
		
		hp2.setSpacing(8);
		hp3.setSpacing(8);
		hp4.setSpacing(8);
		hp4.setWidth("25px");
		
		hpRadios.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		
		hpRadios.add(rdbIni);
		hpRadios.add(rdbRep);
		hpRadios.add(rdbDesc);
		hpRadios.add(rdbCorrAsignado);
		
		hpRadios.add(hp4);
		hpRadios.add(lblIndicadores);

		hpAcciones.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		hpAcciones.add(hp2);
		hp2.add(filterBox);
		hp2.add(filterEstacion);
		hp2.add(filterActivo);
		hp2.add(btnFiltrar);
		hp2.add(btnLimpiarFiltros);

		hpAcciones.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		hp3.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		hp3.add(btnAsignarCorrectivoSeleccionados);
		hp3.add(btnDescartarSeleccionados);
		hp3.add(btnAgregar);
		hpAcciones.add(hp3);

		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		vPanelPrincipal.add(hpRadios);
		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal.add(hpAcciones);

		addTabla();
	}

	private void addTabla() {
		tabla.addCellPreviewHandler(new Handler<PendienteData>() {
			@Override
			public void onCellPreview(final CellPreviewEvent<PendienteData> event) {
				if (event.getContext().getColumn() < 5){
					if (BrowserEvents.CLICK.equals(event.getNativeEvent().getType())) {

						final PendienteData value = event.getValue();
						final Boolean state = !event.getDisplay().getSelectionModel().isSelected(value);
						event.getDisplay().getSelectionModel().setSelected(value, state);
						event.setCanceled(true);
					}
				}
			}
		});

		final MultiSelectionModel<PendienteData> selectModel = new MultiSelectionModel<PendienteData>();
		final Handler<PendienteData> selectionEventManager = DefaultSelectionEventManager.createCheckboxManager();
		tabla.setSelectionModel(selectModel, selectionEventManager);

		tabla.setSelectionModel(selectModel);
		selectModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				seleccionados = ((MultiSelectionModel<PendienteData>) selectModel).getSelectedSet();
			}
		});

		tabla.setWidth("100%");

		getPager();

		vPanelPrincipal.add(tabla);
		vPanelPrincipal.add(pager);
	}

	private void setearWidgets() {
		vPanelPrincipal.setWidth("100%");
		hPanelPrincipal.setWidth("100%");

	}

	public VerticalPanel getPrincipalPanel() {
		return vPanelPrincipal;
	}

	public FilteredListDataProvider<PendienteData> listaFiltrada = getListaFiltrada();

	private FilteredListDataProvider<PendienteData> getListaFiltrada() {
		return new FilteredListDataProvider<PendienteData>(new IFilter<PendienteData>(){
			public boolean isValid(PendienteData value, String filter) {
				if(filter==null || value==null){
					return true;
				}

				return value.getComentario().toLowerCase().contains(filter.toLowerCase()); 
			}
		});
	}

	private void crearTabla() {
		TextColumn<PendienteData> numeroColumn = new TextColumn<PendienteData>() {
			@Override
			public String getValue(PendienteData e) {
				return e.getId()+"";
			}
		};

		TextColumn<PendienteData> textoColumn = new TextColumn<PendienteData>() {
			@Override
			public String getValue(PendienteData e) {
				return e.getComentario();
			}
		};

		TextColumn<PendienteData> estacionColumn = new TextColumn<PendienteData>() {
			@Override
			public String getValue(PendienteData e) {
				return e.getEmpresa();
			}
		};

		/*TextColumn<PendienteData> creadorColumn = new TextColumn<PendienteData>() {
			@Override
			public String getValue(PendienteData e) {
				return e.getCreador();
			}
		};*/

		TextColumn<PendienteData> fechaColumn = new TextColumn<PendienteData>() {
			@Override
			public String getValue(PendienteData e) {
				if (e.getFechaCreado() != null && e.getFechaCreado().toString().contains(".")){
					return e.getFechaCreado().toString().substring(0, e.getFechaCreado().toString().indexOf("."));
				}
				return e.getFechaCreado() != null ? e.getFechaCreado().toString() : "";
			}
		};

		TextColumn<PendienteData> activoColumn = new TextColumn<PendienteData>() {
			@Override
			public String getValue(PendienteData e) {
				return e.getActivo() != null ? e.getActivo().toString() : "";
			}
		};

		final Column<PendienteData, String> verFotoColumn = new Column<PendienteData, String>(new ButtonCell()) {

			@Override
			public void render(Context context, PendienteData e, SafeHtmlBuilder sb) {
				if (e.getUrlFoto() != null){
					super.render(context, e, sb);
				}
			}

			@Override
			public String getValue(PendienteData e) {
				return "Ver";
			}
		};

		final Column<PendienteData, String> editarColumn = new Column<PendienteData, String>(new ButtonCell()) {
			@Override
			public void render(Context context, PendienteData e, SafeHtmlBuilder sb) {
				super.render(context, e, sb);
			}

			@Override
			public String getValue(PendienteData e) {
				return "Editar";
			}
		};

		/*final Column<PendienteData, String> detalleColumn = new Column<PendienteData, String>(new ButtonImageCell()) {
			@Override
			public String getValue(PendienteData e) {
				return "img/look.png";
			}
		};*/

		final Column<PendienteData, String> asignarCorrectivoColumn = new Column<PendienteData, String>(new ButtonCell()) {
			@Override
			public String getValue(PendienteData e) {
				return "Asignar";
			}
		};

		final Column<PendienteData, String> correctivoColumn = new Column<PendienteData, String>(new ButtonCell()) {
			@Override
			public String getValue(PendienteData e) {
				if (rdbCorrAsignado.getValue()){
					return "Abrir corrrectivo " + e.getNumeroCorrectivoAsignado();
				} else if (rdbRep.getValue()) {
					return "Abrir corrrectivo " + e.getOrdenReparado();
				}
				return "";
			}
		};

		asignarCorrectivoColumn.setFieldUpdater(new FieldUpdater<PendienteData, String>() {
			public void update(int index, PendienteData pd, String value) {
				List<PendienteData> pendientes = new ArrayList<PendienteData>();
				pendientes.add(pd);
				dialogoAsignarCorrectivo(instance, pendientes);
			}
		});

		Column<PendienteData, String> descartarColumn = new Column<PendienteData, String>(new ButtonCell()) {
			@Override
			public String getValue(PendienteData e) {
				return "Descartar";
			}
		};

		editarColumn.setFieldUpdater(new FieldUpdater<PendienteData, String>() {
			public void update(int index, PendienteData pd, String value) {
				agregarEditar(instance, pd);
			}
		});

		correctivoColumn.setFieldUpdater(new FieldUpdater<PendienteData, String>() {
			public void update(int index, PendienteData pd, String value) {
				buscarOrden(pd);
			}
		});

		descartarColumn.setFieldUpdater(new FieldUpdater<PendienteData, String>() {
			public void update(int index, PendienteData pd, String value) {
				List<PendienteData> pendientes = new ArrayList<PendienteData>();
				pendientes.add(pd);
				dialogoDescartar(instance, pendientes);
			}
		});

		verFotoColumn.setFieldUpdater(new FieldUpdater<PendienteData, String>() {
			@Override
			public void update(int index, final PendienteData pd, String value) {
				verFoto(pd);
			}
		});

		final Column<PendienteData, String> eliminarColumn = new Column<PendienteData, String>(new ButtonImageCell()) {
			@Override
			public String getValue(PendienteData v) {
				return "img/menos.png";
			}
		};

		eliminarColumn.setFieldUpdater(new FieldUpdater<PendienteData, String>() {
			public void update(int index, PendienteData pd, String value) {
				dialogoEliminar(instance, pd);
			}
		});

		tabla.addColumn(numeroColumn,"Número");
		tabla.addColumn(textoColumn,"Texto");
		tabla.addColumn(estacionColumn,"Estación");
		// tabla.addColumn(creadorColumn,"Creador");
		tabla.addColumn(fechaColumn,"Fecha creado");
		tabla.addColumn(activoColumn,"Activo");
		tabla.addColumn(verFotoColumn,"Ver foto");
		tabla.addColumn(editarColumn,"Editar");
		//tabla.addColumn(detalleColumn,"Detalle");

		if (rdbIni.getValue()){
			tabla.addColumn(descartarColumn,"Descartar");
			tabla.addColumn(asignarCorrectivoColumn,"Asignar correctivo");
		}

		if (rdbCorrAsignado.getValue()){
			tabla.addColumn(correctivoColumn, "Correctivo asignado");

		} else if (rdbRep.getValue()){
			tabla.addColumn(correctivoColumn, "Reparado en correctivo");
		}

		tabla.addColumn(eliminarColumn,"Eliminar");

		numeroColumn.setSortable(true);
		textoColumn.setSortable(true);
		estacionColumn.setSortable(true);
		//creadorColumn.setSortable(true);
		fechaColumn.setSortable(true);
		activoColumn.setSortable(true);
		/*verFotoColumn.setSortable(true);
		editarColumn.setSortable(true);
		detalleColumn.setSortable(true);
		asignarCorrectivoColumn.setSortable(true);
		descartarColumn.setSortable(true);*/

		List<PendienteData> list = cargarLista();

		ListHandler<PendienteData> columnSortHandlerNumero = new ListHandler<PendienteData>(list);
		columnSortHandlerNumero.setComparator(numeroColumn, new Comparator<PendienteData>() {
			public int compare(PendienteData o1, PendienteData o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? Integer.valueOf(o1.getId()).compareTo(Integer.valueOf(o2.getId())) : 1;
				}
				return -1;
			}
		});

		ListHandler<PendienteData> columnSortHandlerDesc = new ListHandler<PendienteData>(list);
		columnSortHandlerDesc.setComparator(textoColumn, new Comparator<PendienteData>() {
			public int compare(PendienteData o1, PendienteData o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getComentario().compareTo(o2.getComentario()) : 1;
				}
				return -1;
			}
		});

		ListHandler<PendienteData> columnSortHandlerEstacion = new ListHandler<PendienteData>(list);
		columnSortHandlerDesc.setComparator(estacionColumn, new Comparator<PendienteData>() {
			public int compare(PendienteData o1, PendienteData o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getEmpresa().compareTo(o2.getEmpresa()) : 1;
				}
				return -1;
			}
		});

		ListHandler<PendienteData> columnSortHandlerFecha = new ListHandler<PendienteData>(list);
		columnSortHandlerFecha.setComparator(fechaColumn, new Comparator<PendienteData>() {
			public int compare(PendienteData o1, PendienteData o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getFechaCreado().compareTo(o2.getFechaCreado()) : 1;
				}
				return -1;
			}
		});

		ListHandler<PendienteData> columnSortHandlerActivo = new ListHandler<PendienteData>(list);
		columnSortHandlerActivo.setComparator(activoColumn, new Comparator<PendienteData>() {
			public int compare(PendienteData o1, PendienteData o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getActivo().compareTo(o2.getActivo()) : 1;
				}
				return -1;
			}
		});

		tabla.addColumnSortHandler(columnSortHandlerNumero);
		tabla.addColumnSortHandler(columnSortHandlerDesc);
		tabla.addColumnSortHandler(columnSortHandlerEstacion);
		tabla.addColumnSortHandler(columnSortHandlerFecha);
		tabla.addColumnSortHandler(columnSortHandlerActivo);

		listaFiltrada.addDataDisplay(tabla);

	}

	private void verFoto(final PendienteData pd) {
		if (pd.getUrlFoto() != null){
			Button btnDescargarFoto = new Button("Descargar");
			Button btnCerrarFoto = new Button("Cerrar");

			Image image = new Image(pd.getUrlFoto());
			final DialogBox imagePopup = new DialogBox(true);
			imagePopup.addCloseHandler(new CloseHandler<PopupPanel>() {
				@Override
				public void onClose(CloseEvent<PopupPanel> event) {
					glass.hide();
				}
			});
			Label lbl = new Label(pd.getComentario());

			HorizontalPanel hp = new HorizontalPanel();
			hp.setWidth("100%");
			hp.add(lbl);
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			hp.add(btnDescargarFoto);

			VerticalPanel vp = new VerticalPanel();
			vp.getElement().getStyle().setBackgroundColor("white");
			vp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			vp.setSpacing(10);

			vp.add(hp);
			imagePopup.setWidget(vp);

			showFoto(vp, imagePopup, image);

			btnCerrarFoto.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					glass.hide();
					imagePopup.hide();
				}
			});

			btnDescargarFoto.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					descargarFoto(pd);
				}
			});
		}
	}

	private void showFoto(final VerticalPanel vp, final PopupPanel imagePopup, final Image fullImage) {
		glass.show();

		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				// Image fullImage = new Image(optionImageName);
				fullImage.setAltText("Cargando imagen ...");
				fullImage.addLoadHandler(new LoadHandler() {
					@Override
					public void onLoad(LoadEvent event) {
						imagePopup.center();
					}
				});
				vp.add(fullImage);
				imagePopup.center();
			}

			@Override
			public void onFailure(Throwable reason) {

			}
		});
	}

	private void buscarOrden(PendienteData pd) {	
		int numeroOrden = pd.getNumeroCorrectivoAsignado();
		if (rdbRep.getValue()){
			numeroOrden = pd.getOrdenReparado();
		}
		popUp.show();
		ProyectoBilpa.greetingService.buscarOrden(numeroOrden, new AsyncCallback<Orden>() {
			public void onFailure(Throwable caught) {
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar la orden");
				vpu.showPopUp();
			}

			public void onSuccess(Orden result) {
				popUp.hide();
				if(result != null) {
					if (result.getEstadoOrden() == 3 || result.getEstadoOrden() == 4)//cerrada o anulada
					{
						abrirPdf(result.getNumero(), true);
					}
					else
					{
						UtilOrden.cargarOrdenSeleccionada(result, sesion);						
					}
				} else {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "No se encontró el correctivo");
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

	private void descargarFoto(PendienteData pd) {
		ProyectoBilpa.greetingService.descargarFotoPendiente(pd, new AsyncCallback<String>() {

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

	private List<PendienteData> cargarLista() {
		listaFiltrada.getList().clear();
		List<PendienteData> list = listaFiltrada.getList();

		for (PendienteData data : lista) {
			list.add(data);
		}
		tabla.redraw();
		return list;
	}

	public void agregarPendiente(PendienteData pd) {
		ProyectoBilpa.greetingService.guardarPendiente(pd, null, new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener pendientes");
				vpu.showPopUp();
			}

			public void onSuccess(Boolean result) {
				getPendientes();
			}
		});
	}

	public void asignarCorrectivoAPendientes(List<PendienteData> pendientes, int numeroOrden) {
		if (pendientes.size() == 0){
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe seleccionar al menos un pendiente");
			vpu.showPopUp();
			return;
		}

		if (!validarAsignarCorrectivoAPendientes(pendientes)){
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Todos los pendientes seleccionados deben pertenecer a una misma estación");
			vpu.showPopUp();
			return;
		}

		ProyectoBilpa.greetingService.asignarCorrectivoAPendientes(sesion.getId(), numeroOrden, pendientes, new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener pendientes");
				vpu.showPopUp();
			}

			public void onSuccess(Boolean result) {
				getPendientes();
			}
		});
	}

	private boolean validarAsignarCorrectivoAPendientes(List<PendienteData> pendientes) {
		int idEstacion = pendientes.get(0).getIdEstacion();
		for (PendienteData pendienteData : pendientes) {
			if (pendienteData.getIdEstacion() != idEstacion){
				return false;
			}
		}
		return true;
	}

	private void dialogoDescartar(IUPendientes instance, List<PendienteData> pendientes) {
		if (pendientes.size() == 0){
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe seleccionar al menos un pendiente");
			vpu.showPopUp();
			return;
		}

		glass.show();

		final IUPendientesDescartar iu = new IUPendientesDescartar(true, instance, glass, popUp, pendientes);
		iu.setPopupPositionAndShow(new PositionCallback() {
			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {
				int left = (Window.getClientWidth() - offsetWidth) / 2;
				int top = (Window.getClientHeight() - offsetHeight) / 2;
				iu.setPopupPosition(left, top);
			}
		});

		iu.addCloseHandler(new CloseHandler<PopupPanel>() {
			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				glass.hide();
			}
		});
	}

	private void dialogoEliminar(IUPendientes instance, PendienteData pd) {
		glass.show();

		final IUPendientesEliminar iu = new IUPendientesEliminar(true, instance, glass, popUp, pd);
		iu.setPopupPositionAndShow(new PositionCallback() {
			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {
				int left = (Window.getClientWidth() - offsetWidth) / 2;
				int top = (Window.getClientHeight() - offsetHeight) / 2;
				iu.setPopupPosition(left, top);
			}
		});

		iu.addCloseHandler(new CloseHandler<PopupPanel>() {
			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				glass.hide();
			}
		});
	}

	private void dialogoAsignarCorrectivo(IUPendientes instance, List<PendienteData> pendientes) {
		glass.show();

		final IUPendientesAsignarCorrectivo iu = new IUPendientesAsignarCorrectivo(true, instance, glass, popUp, pendientes);
		iu.setPopupPositionAndShow(new PositionCallback() {
			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {
				int left = (Window.getClientWidth() - offsetWidth) / 2;
				int top = (Window.getClientHeight() - offsetHeight) / 6;
				iu.setPopupPosition(left, top);
			}
		});

		iu.addCloseHandler(new CloseHandler<PopupPanel>() {
			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				glass.hide();
			}
		});
	}
	
	private void agregarEditar(final IUPendientes instance, PendienteData pd) {
		glass.show();

		final IUPendientesSeleccionarActivoYEstacion iu = new IUPendientesSeleccionarActivoYEstacion(sesion, instance, pd);
		iu.setPopupPositionAndShow(new PositionCallback() {
			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {
				int left = (Window.getClientWidth() - offsetWidth) / 2;
				// int top = (Window.getClientHeight() - offsetHeight) / 3;
				int top = 20;
				iu.setPopupPosition(left, top);
			}
		});

		iu.addCloseHandler(new CloseHandler<PopupPanel>() {
			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				glass.hide();
			}
		});
	}
}
