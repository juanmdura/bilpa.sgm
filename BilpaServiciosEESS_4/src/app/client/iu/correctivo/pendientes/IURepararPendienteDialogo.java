package app.client.iu.correctivo.pendientes;

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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;

import app.client.ProyectoBilpa;
import app.client.dominio.Activo;
import app.client.dominio.FallaReportada;
import app.client.dominio.Pendiente;
import app.client.dominio.Persona;
import app.client.dominio.Sello;
import app.client.dominio.data.PendienteData;
import app.client.dominio.data.PendienteDataUI;
import app.client.iu.correctivo.FallaReportadaUtil;
import app.client.iu.correctivo.IUWidgetCorrectivo;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.UtilOrden;
import app.client.utilidades.utilObjects.FileDownload;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.SuggestBoxAdv;
import app.client.utilidades.utilObjects.filter.FilteredListDataProvider;
import app.client.utilidades.utilObjects.filter.IFilter;
import app.client.utilidades.utilObjects.filter.TextBoxFilter;

public class IURepararPendienteDialogo extends DialogBox  {

	private Label lblTitulo;
	private Label lblSubTitulo;

	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private HorizontalPanel hPanelPrincipal = new HorizontalPanel();
	private HorizontalPanel hPanelButtons = new HorizontalPanel();

	private GlassPopup glass = new GlassPopup();

	private CellTable<PendienteData> tabla = new CellTable<PendienteData>();
	private SimplePager.Resources pagerResources;
	private SimplePager pager;

	private Label lblIndicadores = new Label("");

	private Image imgFiltrar = new Image("img/filter_add.png");
	private Image imgLimpiarFiltros = new Image("img/filter_delete.png");

	private PushButton btnFiltrar = new PushButton(imgFiltrar);
	private PushButton btnLimpiarFiltros = new PushButton(imgLimpiarFiltros);

	private Button btnAceptar = new Button("Aceptar");
	private Button btnCancelar = new Button("Cancelar");

	private MultiWordSuggestOracle oracleActivo = new MultiWordSuggestOracle();
	private SuggestBoxAdv filterActivo = new SuggestBoxAdv(oracleActivo);

	private TextBoxFilter filterBox = new TextBoxFilter();

	private Persona sesion;
	private int idSello;
	private List<PendienteData> lista;
	private PendienteDataUI pendienteDataUi;
	private PendienteData pendienteFilter = new PendienteData(); 

	private FileDownload file = new FileDownload();

	private boolean inicio = true;
	private Set<PendienteData> seleccionados;
	private IUWidgetCorrectivo iuWidgetCorrectivo;

	public int getIdSello() {
		return idSello;
	}

	public Persona getSesion() {
		return sesion;
	}

	public IURepararPendienteDialogo(PendienteDataUI pendienteDataUI, IUWidgetCorrectivo iuWidgetCorrectivo, GlassPopup glass) {
		super();
		this.glass = glass;
		this.pendienteDataUi = pendienteDataUI;
		this.iuWidgetCorrectivo = iuWidgetCorrectivo;
		lblTitulo = new Label("Pendientes " + Sello.getSelloById(idSello));
		lblSubTitulo = new Label("Seleccione los pendientes a reparar en este correctivo");
		eventos();

		//
		setSugBoxActivo();
		load(pendienteDataUi.getPendientes());
		//

		set();
		setearWidgets();
		agregarWidgets();
	}

	private void filtrar(){
		setearFiltros();

		List<PendienteData> listaFiltrada = new ArrayList<PendienteData>();
		for (PendienteData pd : lista) {
			if (filtraActivo(pd) && filtraTexto(pd)){
				listaFiltrada.add(pd);
			}
		}
		load(listaFiltrada);
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

	private boolean filtroTextoOn() {
		return !filterBox.getText().equals("");
	}

	private boolean filtroActivoOn() {
		return !filterActivo.getText().equals("");
	}

	private void load(List<PendienteData> listaParam) {
		lista = listaParam;
		if (inicio){
			inicio = false;
			crearTabla();
		} else {
			cargarLista();
		}
	}

	private void setSugBoxActivo(){
		oracleActivo.clear();

		for (String e : pendienteDataUi.getActivos()){
			oracleActivo.add(e);
		}
	}

	private void setearFiltros(){
		if (filterActivo.getValue() != ""){
			for (String a : pendienteDataUi.getActivos()){
				if (a.contains(filterActivo.getValue())){
					pendienteFilter.setActivo(a);
				}
			}
		}
	}	

	private void eventos() {
		final IURepararPendienteDialogo instance = this;
		btnAceptar.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				repararPendientes(instance);
			}
		});

		btnCancelar.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
				glass.hide();
			}
		});

		btnFiltrar.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				filtrar();	
			}
		});

		btnLimpiarFiltros.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				load(pendienteDataUi.getPendientes());
				limpiarFiltros();
			}
		});

	}

	private void limpiarFiltros() {
		filterBox.setText("");
		filterActivo.setText("");
	}

	private void set() {
		titulo();

		getPager();

		filterBox.getElement().setAttribute("placeHolder", "Filtrar textos");
		filterBox.setTitle("Filtrar textos");

		filterActivo.getElement().setAttribute("placeHolder", "Filtrar activos");
		filterActivo.setTitle("Filtrar activos");

	}

	private void getPager() {
		pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(tabla);
		pager.setPageSize(16);
	}

	private void titulo() {
		lblTitulo.setText("Pendientes de " + pendienteDataUi.getEstaciones().get(0));
		lblTitulo.setStyleName("Titulo");
	}

	private void agregarWidgets() {
		vPanelPrincipal.add(file);
		vPanelPrincipal.setSpacing(20);
		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal.add(hPanelPrincipal);
		hPanelPrincipal.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		VerticalPanel vpTitulos = new VerticalPanel();

		vpTitulos.add(lblTitulo);
		vpTitulos.add(lblSubTitulo);

		vPanelPrincipal.add(vpTitulos);

		btnCancelar.setWidth("100px");
		btnAceptar.setWidth("100px");

		hPanelButtons.add(btnCancelar);
		hPanelButtons.add(btnAceptar);

		HorizontalPanel hpAcciones = new HorizontalPanel();
		hpAcciones.setWidth("100%");

		HorizontalPanel hp2 = new HorizontalPanel();
		HorizontalPanel hp3 = new HorizontalPanel();
		HorizontalPanel hp4 = new HorizontalPanel();

		hPanelButtons.setSpacing(8);
		hp2.setSpacing(8);
		hp3.setSpacing(8);
		hp4.setSpacing(8);
		hp4.setWidth("25px");

		hpAcciones.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		hpAcciones.add(hp2);
		hp2.add(filterBox);
		hp2.add(filterActivo);
		hp2.add(btnFiltrar);
		hp2.add(btnLimpiarFiltros);

		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		vPanelPrincipal.add(hp2);
		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		addTabla();
		vPanelPrincipal.add(hPanelButtons);
		add(vPanelPrincipal);
	}

	private void addTabla() {
		tabla.addCellPreviewHandler(new Handler<PendienteData>() {
			@Override
			public void onCellPreview(final CellPreviewEvent<PendienteData> event) {
				if (event.getContext().getColumn() < 4){
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

		verFotoColumn.setFieldUpdater(new FieldUpdater<PendienteData, String>() {
			@Override
			public void update(int index, final PendienteData pd, String value) {
				verFoto(pd);
			}
		});

		tabla.addColumn(numeroColumn,"Número");
		tabla.addColumn(textoColumn,"Texto");
		tabla.addColumn(fechaColumn,"Fecha creado");
		tabla.addColumn(activoColumn,"Activo");
		tabla.addColumn(verFotoColumn,"Ver foto");

		numeroColumn.setSortable(true);
		textoColumn.setSortable(true);
		fechaColumn.setSortable(true);
		activoColumn.setSortable(true);

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
					// glass.hide();
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
					//glass.hide();
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
		// glass.show();

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
				load(pendienteDataUi.getPendientes());
			}
		});
	}

	private void repararPendientes(final IURepararPendienteDialogo instance) {
		if (seleccionados != null && seleccionados.size() > 0){
			List<Activo> activos = new ArrayList<Activo>();
			List<FallaReportada> fallasReportadas = new ArrayList<FallaReportada>();
			List<String> comentarios = new ArrayList<String>();
			
			Pendiente pendiente = null;
			for (PendienteData pd : seleccionados ) {
				FallaReportada fallaReportada = new FallaReportada();
				fallaReportada.setDescripcion(UtilOrden.textoFallaPendientes);
				fallaReportada.setId(UtilOrden.idFallaPendientes);
				fallaReportada.setSubTipo(2);
				
				pendiente = new Pendiente();
				pendiente.merge(pd);
			
				comentarios.add(pd.getComentario());
				fallasReportadas.add(fallaReportada);
				activos.add(buscarActivo(iuWidgetCorrectivo.getOrden().getEmpresa().getListaDeActivos(), pd.getIdActivo()));
			}

			FallaReportadaUtil.validar(activos, fallasReportadas, pendiente, comentarios, glass, instance, iuWidgetCorrectivo);
			pendienteDataUi.getPendientes().removeAll(seleccionados);
			load(pendienteDataUi.getPendientes());
			iuWidgetCorrectivo.reloadTituloPendientesAReparar(pendienteDataUi);
		} else {
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe seleccionar al menos un pendiente");
			vpu.showPopUp();
		}
	}

	private Activo buscarActivo(Set<Activo> listaDeActivos, int idActivo) {
		for (Activo activo : listaDeActivos) {
			if (activo.getId() == idActivo){
				return activo;
			}
		}
		return null;
	}

}
