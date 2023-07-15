package app.client.iu.correctivo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

import app.client.ProyectoBilpa;
import app.client.dominio.Activo;
import app.client.dominio.ActivoGenerico;
import app.client.dominio.FallaReportada;
import app.client.dominio.IUCorrectivo.IUCorrectivoEnum;
import app.client.dominio.Orden;
import app.client.dominio.Persona;
import app.client.dominio.TipoFallaReportada;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.Constants;
import app.client.utilidades.UtilOrden;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;
import app.client.utilidades.utilObjects.filter.FilteredListDataProvider;
import app.client.utilidades.utilObjects.filter.IFilter;
import app.client.utilidades.utilObjects.filter.IStringValueChanged;
import app.client.utilidades.utilObjects.filter.TextBoxFilter;

public class IUCorrectivoSeleccionarActivoYFalla extends DialogBox {
	IUWidgetCorrectivo iuWidgetCorrectivo;
	private IUCorrectivoEnum iu;
	private Orden orden;

	private Label lblTitulo = new Label("Seleccionar falla y activo");
	
	private Button btnBuscar = new Button("Buscar");
	private Button btnCancelar = new Button("Cancelar");
	private Button btnAceptar = new Button("Aceptar");

	private CellTable<Activo> tabla = new CellTable<Activo>();
	private SimplePager.Resources pagerResources;
	private SimplePager pager;
	private TextBoxFilter filterBox = new TextBoxFilter();
	private List<Activo> lista = new ArrayList<Activo>();
	private boolean inicio = true;
	private Activo seleccionado;

	private CellTable<FallaReportada> tabla2 = new CellTable<FallaReportada>();
	private SimplePager.Resources pagerResources2;
	private SimplePager pager2;
	private TextBoxFilter filterBox2 = new TextBoxFilter();
	private ListBox listBoxTiposFallasR = new ListBox();
	private Label lblTiposFallasR = new Label("Tipos de fallas ");
	
	private FallaReportada seleccionado2;
	private boolean inicio2 = true;
	private List<FallaReportada> listaFallasR = new ArrayList<FallaReportada>();
	private List<TipoFallaReportada> tiposFalla;
	
	private GlassPopup glass = new GlassPopup();


	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private HorizontalPanel hPanelPrincipal = new HorizontalPanel();

	private VerticalPanel vPanel1 = new VerticalPanel();
	private HorizontalPanel hPanel1 = new HorizontalPanel();
	private VerticalPanel vPanel3 = new VerticalPanel();

	private VerticalPanel vPanel12 = new VerticalPanel();
	private HorizontalPanel hPanel12 = new HorizontalPanel();
	private VerticalPanel vPanel32 = new VerticalPanel();

	public IUCorrectivoSeleccionarActivoYFalla(Persona sesion, Orden orden, IUCorrectivoEnum iu, IUWidgetCorrectivo iuWidgetCorrectivo) {
		super();
		this.iu = iu;
		this.orden = orden;
		this.iuWidgetCorrectivo = iuWidgetCorrectivo;

		init();
		set();
		setearWidgets();
		agregarWidgets();
		eventos();
	}

	public Button getBuscarActivo() {
		return btnBuscar;
	}

	private void eventos() {
		btnBuscar.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				mostrarBusqueda();
			}
		});

		btnAceptar.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				seleccionar();
			}
		});
		
		btnCancelar.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				glass.hide();
				hide();
			}
		});
		
		listBoxTiposFallasR.addChangeHandler(new ChangeHandler()
		{
			public void onChange(ChangeEvent arg0) 
			{
				int idTipo = Integer.valueOf(listBoxTiposFallasR.getValue(listBoxTiposFallasR.getSelectedIndex()));
				obtenerFallas(idTipo);
			}
		});
	}

	private void init() {
		ProyectoBilpa.greetingService.obtenerActivosDeEstacion(orden.getEmpresa().getId(), new AsyncCallback<List<Activo>>() {
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener activos");
				vpu.showPopUp();
			}

			public void onSuccess(List<Activo> result) {
				lista = result;
				if (inicio){
					inicio = false;
					crearTabla();
				} else {
					cargarLista();
				}	
			}
		});
		
		cargarLtBoxTipoFallaR();

		/*ProyectoBilpa.greetingService.obtenerTiposFallasR(new AsyncCallback<ArrayList<TipoFallaReportada>>() {
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener los tipo de fallas");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<TipoFallaReportada> result) {
				tiposFalla = result;
				obtenerFallas(null);
			}
		});*/
	}
	
	private void cargarLtBoxTipoFallaR() {
		listBoxTiposFallasR.clear();
		final PopupCargando popUp = new PopupCargando("Cargando...");
		popUp.show();
		
		ProyectoBilpa.greetingService.obtenerTiposFallasR(new AsyncCallback<ArrayList<TipoFallaReportada>>() {
			public void onFailure(Throwable caught) 
			{
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener los tipos de fallas");
				vpu.showPopUp();
			}
			
			public void onSuccess(ArrayList<TipoFallaReportada> result) 
			{
				tiposFalla = result;
				// tiposFallasMemoriaR.clear();
				TipoFallaReportada auxiliar;
				listBoxTiposFallasR.addItem(Constants.TODOS, -1+"");
				
				for(TipoFallaReportada tfrSinClasificar : tiposFalla)
				{
					if(tfrSinClasificar.getDescripcion().equals(Constants.SIN_CLASIFICAR))
					{
						listBoxTiposFallasR.addItem(tfrSinClasificar.toString(),String.valueOf(tfrSinClasificar.getId()));
						// tiposFallasMemoriaR.add(tfrSinClasificar);
					}
				}
				
				for (int i=0; i<tiposFalla.size();i++)
				{
					auxiliar = (TipoFallaReportada) tiposFalla.get(i);
					if(!auxiliar.getDescripcion().equals(Constants.SIN_CLASIFICAR))
					{
						listBoxTiposFallasR.addItem(auxiliar.toString(),String.valueOf(auxiliar.getId()));
						// tiposFallasMemoriaR.add(auxiliar);
					}
				}	
				
				if(listBoxTiposFallasR.getItemCount() > 0)
				{
					listBoxTiposFallasR.setSelectedIndex(0);
				}
				
				obtenerFallas(-1);
				popUp.hide();
			}
		});		
	}	

	private void obtenerFallas(int idTipo) {		
		ProyectoBilpa.greetingService.obtenerFallasRPorTipo(idTipo, new AsyncCallback<List<FallaReportada>>() {
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener fallas");
				vpu.showPopUp();
			}

			public void onSuccess(List<FallaReportada> result) {
				listaFallasR = result;
				if (inicio2){
					inicio2 = false;
					crearTabla2();
				} else {
					cargarLista2();
				}	
			}
		});	
	}
	
	private void set() {
		//activo
		final SelectionModel<Activo> selectionModel1 = new SingleSelectionModel<Activo>();
		tabla.setSelectionModel(selectionModel1);
		selectionModel1.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				seleccionado = ((SingleSelectionModel<Activo>) selectionModel1).getSelectedObject();
			}
		});

		tabla.setWidth("100%");

		pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(tabla);
		pager.setPageSize(16);

		//falla reportada
		final SelectionModel<FallaReportada> selectionModel2 = new SingleSelectionModel<FallaReportada>();
		tabla2.setSelectionModel(selectionModel2);
		selectionModel2.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				seleccionado2 = ((SingleSelectionModel<FallaReportada>) selectionModel2).getSelectedObject();
			}
		});

		tabla2.setWidth("100%");

		pagerResources2 = GWT.create(SimplePager.Resources.class);
		pager2 = new SimplePager(TextLocation.CENTER, pagerResources2, false, 0, true);
		pager2.setDisplay(tabla2);
		pager2.setPageSize(16);
	}

	private void agregarWidgets() {
		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal.add(lblTitulo);
		
		add(vPanelPrincipal);
		vPanelPrincipal.add(hPanelPrincipal);
		hPanelPrincipal.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

		cargarWidgetsBusqueda2();
		cargarWidgetsBusqueda();
		
		HorizontalPanel hpButtons = new HorizontalPanel();
		hpButtons.setSpacing(10);
		hpButtons.add(btnCancelar);
		hpButtons.add(btnAceptar);
		vPanelPrincipal.add(hpButtons);
	}

	private void setearWidgets() {
		hPanelPrincipal.setSpacing(30);
		filterBox.getElement().setPropertyString("placeholder", "Filtrar activos");
		filterBox2.getElement().setPropertyString("placeholder", "Filtrar fallas");
		vPanelPrincipal.setWidth("100%");
		hPanelPrincipal.setWidth("100%");

		btnBuscar.setWidth("100px");
		btnAceptar.setWidth("100px");
		btnCancelar.setWidth("100px");
		
		lblTitulo.setStyleName("Titulo");
		lblTiposFallasR.setStyleName("Negrita");
	}

	public VerticalPanel getPrincipalPanel() {
		return vPanelPrincipal;
	}

	private void cargarWidgetsBusqueda() {
		vPanelPrincipal.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		hPanelPrincipal.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		hPanelPrincipal.add(vPanel3);
		vPanel3.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		
		vPanel3.add(hPanel1);
		vPanel3.add(vPanel1);

		vPanel1.add(tabla);
		vPanel1.add(pager);

		HorizontalPanel hpTipo = new HorizontalPanel();
		HorizontalPanel hpNombre = new HorizontalPanel();

		hPanel1.setSpacing(8);
		hPanel1.add(hpTipo);
		hPanel1.add(hpNombre);

		// hpTipo.add(listBoxTipoActivo);
		hpNombre.add(filterBox);
	}

	private void cargarWidgetsBusqueda2() {
		vPanelPrincipal.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		hPanelPrincipal.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		vPanel32.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		hPanelPrincipal.add(vPanel32);

		vPanel32.add(hPanel12);
		vPanel32.add(vPanel12);

		vPanel12.add(tabla2);
		vPanel12.add(pager2);

		HorizontalPanel hpTipo = new HorizontalPanel();
		HorizontalPanel hpNombre = new HorizontalPanel();

		hPanel12.setSpacing(8);
		hPanel12.add(hpTipo);
		hPanel12.add(hpNombre);

		HorizontalPanel hpSpace = new HorizontalPanel();
		hpSpace.setWidth("8px");
		hpTipo.add(lblTiposFallasR);
		hpTipo.add(hpSpace);
		hpTipo.add(listBoxTiposFallasR);
		
		hpNombre.add(filterBox2);
	}

	private void mostrarBusqueda() {
		glass.show();
		show();
		center();
	}

	private void seleccionar() {
		List<Activo> seleccionados = new ArrayList<Activo>();
		seleccionados.add(seleccionado);
		
		List<FallaReportada> seleccionados2 = new ArrayList<FallaReportada>();
		seleccionados2.add(seleccionado2);
		
		FallaReportadaUtil.validar(seleccionados, seleccionados2, null, new ArrayList<String>(), glass, this, iuWidgetCorrectivo);
	}

	public final FilteredListDataProvider<Activo> listaFiltrada = new FilteredListDataProvider<Activo>(new IFilter<Activo>(){
		public boolean isValid(Activo value, String filter) {
			if(filter==null || value==null){
				return true;
			}

			boolean match = false;
			if (value.getTipo() == 6){
				match = ((ActivoGenerico)value).getTipoActivoGenerico().getNombre().toLowerCase().contains(filter.toLowerCase());
			} else {
				match = UtilOrden.tiposDeActivos(value).toLowerCase().contains(filter.toLowerCase());
			}
			return value.toString().toLowerCase().contains(filter.toLowerCase()) || match; 
		}
	});

	public final FilteredListDataProvider<FallaReportada> listaFiltrada2 = new FilteredListDataProvider<FallaReportada>(new IFilter<FallaReportada>(){
		public boolean isValid(FallaReportada value, String filter) {
			if(filter==null || value==null){
				return true;
			}

			boolean match = buscarTipoFalla(value.getTipo()).toLowerCase().contains(filter.toLowerCase());
			return value.toString().toLowerCase().contains(filter.toLowerCase()) || match; 
		}
	});

	private String buscarTipoFalla(Integer tipo) {
		for(TipoFallaReportada tfr : tiposFalla){
			if (tfr.getId() == tipo){
				return tfr.getDescripcion();
			}
		}
		return "";
	}

	private List<Activo> cargarLista() {
		listaFiltrada.getList().clear();
		List<Activo> list = listaFiltrada.getList();

		for (Activo data : lista) {
			list.add(data);
		}
		tabla.redraw();
		return list;
	}

	private void crearTabla() {
		TextColumn<Activo> tipoColumn = new TextColumn<Activo>() {
			@Override
			public String getValue(Activo e) {
				if (e.getTipo() == 6){
					return ((ActivoGenerico)e).getTipoActivoGenerico().getNombre();
				}
				return UtilOrden.tiposDeActivos(e);
			}
		};

		TextColumn<Activo> descColumn = new TextColumn<Activo>() {
			@Override
			public String getValue(Activo e) {
				return e.toString();
			}
		};

		tabla.addColumn(tipoColumn,"Tipo activo");
		tabla.addColumn(descColumn,"Descripción");

		tipoColumn.setSortable(true);
		descColumn.setSortable(true);
		List<Activo> list = cargarLista();

		ListHandler<Activo> columnSortHandlerTipo = new ListHandler<Activo>(list);
		columnSortHandlerTipo.setComparator(tipoColumn, new Comparator<Activo>() {
			public int compare(Activo o1, Activo o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getTipo()+"".compareTo(o2.getTipo()+"") : 1;
				}
				return -1;
			}
		});

		ListHandler<Activo> columnSortHandlerDesc = new ListHandler<Activo>(list);
		columnSortHandlerDesc.setComparator(descColumn, new Comparator<Activo>() {
			public int compare(Activo o1, Activo o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.toString().compareTo(o2.toString()) : 1;
				}
				return -1;
			}
		});

		tabla.addColumnSortHandler(columnSortHandlerTipo);
		tabla.addColumnSortHandler(columnSortHandlerDesc);

		listaFiltrada.addDataDisplay(tabla);

		filterBox.addValueChangeHandler(new IStringValueChanged() {
			public void valueChanged(String newValue) {
				listaFiltrada.setFilter(newValue);
				listaFiltrada.refresh();
			}
		});
	}

	private List<FallaReportada> cargarLista2() {
		listaFiltrada2.getList().clear();
		List<FallaReportada> list = listaFiltrada2.getList();

		for (FallaReportada data : listaFallasR) {
			list.add(data);
		}
		tabla2.redraw();
		return list;
	}

	private void crearTabla2() {
		TextColumn<FallaReportada> tipoColumn = new TextColumn<FallaReportada>() {
			@Override
			public String getValue(FallaReportada e) {
				return buscarTipoFalla(e.getSubTipo());
			}
		};

		TextColumn<FallaReportada> descColumn = new TextColumn<FallaReportada>() {
			@Override
			public String getValue(FallaReportada e) {
				return e.toString();
			}
		};

		tabla2.addColumn(tipoColumn,"Tipo falla");
		tabla2.addColumn(descColumn,"Descripción");

		tipoColumn.setSortable(true);
		descColumn.setSortable(true);
		List<FallaReportada> list = cargarLista2();

		ListHandler<FallaReportada> columnSortHandlerTipo = new ListHandler<FallaReportada>(list);
		columnSortHandlerTipo.setComparator(tipoColumn, new Comparator<FallaReportada>() {
			public int compare(FallaReportada o1, FallaReportada o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o2 != null) {
					return (o1 != null) ? buscarTipoFalla(o1.getSubTipo()).compareTo(buscarTipoFalla(o2.getSubTipo())) : 1;
				}
				return -1;
			}
		});

		ListHandler<FallaReportada> columnSortHandlerDesc = new ListHandler<FallaReportada>(list);
		columnSortHandlerDesc.setComparator(descColumn, new Comparator<FallaReportada>() {
			public int compare(FallaReportada o1, FallaReportada o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.toString().compareTo(o2.toString()) : 1;
				}
				return -1;
			}
		});

		tabla2.addColumnSortHandler(columnSortHandlerTipo);
		tabla2.addColumnSortHandler(columnSortHandlerDesc);

		listaFiltrada2.addDataDisplay(tabla2);

		filterBox2.addValueChangeHandler(new IStringValueChanged() {
			public void valueChanged(String newValue) {
				listaFiltrada2.setFilter(newValue);
				listaFiltrada2.refresh();
			}
		});
	}
}
