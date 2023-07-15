package app.client.iu.pendientes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

import app.client.ProyectoBilpa;
import app.client.dominio.Activo;
import app.client.dominio.ActivoGenerico;
import app.client.dominio.Estacion;
import app.client.dominio.EstadoPendiente;
import app.client.dominio.Persona;
import app.client.dominio.data.DestinoDelCargoData;
import app.client.dominio.data.PendienteData;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.UtilOrden;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.filter.FilteredListDataProvider;
import app.client.utilidades.utilObjects.filter.IFilter;
import app.client.utilidades.utilObjects.filter.IStringValueChanged;
import app.client.utilidades.utilObjects.filter.TextBoxFilter;

public class IUPendientesSeleccionarActivoYEstacion extends DialogBox {
	IUPendientes iuWidgetPendientes;

	private Label lblTitulo;
	
	private TextBox txtTexto = new TextBox();
	private ListBox lbxDestinosDelCargo = new ListBox();
	private DateBox dbPlazo = new DateBox();
	
	private CheckBox chbVisible = new CheckBox("Visible");
	private Label lblTexto = new Label("Texto ");
	private Label lblDestino = new Label("Destino del cargo ");
	private Label lblPlazo = new Label("Plazo ");
	
	private Label lblEstacionT = new Label("Estación ");
	private Label lblActivoT = new Label("Activo ");
	private Label lblEstacionV = new Label();
	private Label lblActivoV = new Label();
	
	private Button btnBuscar = new Button("Buscar");
	private Button btnCancelar = new Button("Cancelar");
	private Button btnAceptar = new Button("Aceptar");

	private CellTable<Activo> tabla = new CellTable<Activo>();
	private SimplePager.Resources pagerResources;
	private SimplePager pager;
	private TextBoxFilter filterBox = new TextBoxFilter();
	private List<Activo> lista = new ArrayList<Activo>();
	private boolean inicio = true;
	private Activo seleccionadoActivo;

	private CellTable<Estacion> tabla2 = new CellTable<Estacion>();
	private SimplePager.Resources pagerResources2;
	private SimplePager pager2;
	private TextBoxFilter filterBox2 = new TextBoxFilter();
	private List<Estacion> lista2 = new ArrayList<Estacion>();
	private Estacion seleccionadoEstacion;
	private boolean inicio2 = true;
	
	private GlassPopup glass = new GlassPopup();


	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private HorizontalPanel hPanelPrincipal = new HorizontalPanel();

	private VerticalPanel vPanel1 = new VerticalPanel();
	private HorizontalPanel hPanel1 = new HorizontalPanel();
	private VerticalPanel vPanel3 = new VerticalPanel();

	private VerticalPanel vPanel12 = new VerticalPanel();
	private HorizontalPanel hPanel12 = new HorizontalPanel();
	private VerticalPanel vPanel32 = new VerticalPanel();

	private PendienteData pendienteAEditar;
	private Persona sesion;
	
	private SelectionModel<Activo> selectionModelActivo;
	private SelectionModel<Estacion> selectionModelEstacion;
	
	public IUPendientesSeleccionarActivoYEstacion(Persona sesion, IUPendientes iuWidgetPendientes, PendienteData pendienteAEditar) {
		super(true);
		this.sesion = sesion;
		this.iuWidgetPendientes = iuWidgetPendientes;
		this.pendienteAEditar = pendienteAEditar;
		
		if (pendienteAEditar != null){
			 lblTitulo = new Label("Editar pendiente");
		} else {
			lblTitulo = new Label("Agregar pendiente");
		}
		init();
		set();
		setearWidgets();
		agregarWidgets();
		eventos();
	}

	private void setEditar() {
		if (pendienteAEditar != null){
			txtTexto.setText(pendienteAEditar.getComentario());
			chbVisible.setValue(pendienteAEditar.isComentarioVisible());
			dbPlazo.setValue(pendienteAEditar.getPlazo());
			lbxDestinosDelCargo.setSelectedIndex(pendienteAEditar.getIdDestinoDelCargo()-1);
			selectionModelEstacion.setSelected(buscarEstacion(pendienteAEditar.getIdEstacion()), true);
		}
	}

	private Estacion buscarEstacion(int id) {
		for (Estacion e : lista2) {
			if (e.getId() == id){
				return e;
			}
		}
		return null;
	}
	
	private Activo buscarActivo(int id) {
		for (Activo a : lista) {
			if (a.getId() == id){
				return a;
			}
		}
		return null;
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
	}

	private void getActivos() {
		glass.show();
		ProyectoBilpa.greetingService.obtenerActivosDeEstacion(seleccionadoEstacion.getId(), new AsyncCallback<List<Activo>>() {
			public void onFailure(Throwable caught) {
				glass.hide();
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
				
				if (pendienteAEditar != null){
					selectionModelActivo.setSelected(buscarActivo(pendienteAEditar.getIdActivo()), true);
				}
				glass.hide();
			}
		});
		
	}

	private void init(){
		ProyectoBilpa.greetingService.obtenerEstaciones(iuWidgetPendientes.getIdSello(), new AsyncCallback<List<Estacion>>() {
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener estaciones");
				vpu.showPopUp();
			}

			public void onSuccess(List<Estacion> result) {
				lista2 = result;
				if (inicio2){
					inicio2 = false;
					crearTabla2();
				} else {
					cargarLista2();
				}	
				setEditar();
			}
		});	
		
		ProyectoBilpa.greetingService.obtenerDestinosDelCargo(new AsyncCallback<ArrayList<DestinoDelCargoData>>() {
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener destinos del cargo");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<DestinoDelCargoData> result) {
				for (DestinoDelCargoData destinoDelCargoData : result) {
					lbxDestinosDelCargo.addItem(destinoDelCargoData.getNombre(), destinoDelCargoData.getId()+"");
				}
			}
		});
	}
	
	private void set() {
		lblTexto.setStyleName("Negrita");
		chbVisible.setStyleName("Negrita");
		lblDestino.setStyleName("Negrita");
		lblPlazo.setStyleName("Negrita");
		lblEstacionT.setStyleName("Negrita");
		lblActivoT.setStyleName("Negrita");
		
		dbPlazo.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("yyyy-MM-dd")));
		dbPlazo.setWidth("250px");
		
		//activo
		selectionModelActivo = new SingleSelectionModel<Activo>();
		tabla.setSelectionModel(selectionModelActivo);
		selectionModelActivo.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				seleccionadoActivo = ((SingleSelectionModel<Activo>) selectionModelActivo).getSelectedObject();
				lblActivoV.setText(seleccionadoActivo !=null ? seleccionadoActivo.toString() : "");
			}
		});

		txtTexto.setWidth("100%");
		txtTexto.getElement().setAttribute("placeHolder", "Ingrese el texto del pendiente");
		tabla.setWidth("100%");

		pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(tabla);
		pager.setPageSize(16);

		//estacion reportada
		selectionModelEstacion = new SingleSelectionModel<Estacion>();
		tabla2.setSelectionModel(selectionModelEstacion);
		selectionModelEstacion.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				seleccionadoEstacion = ((SingleSelectionModel<Estacion>) selectionModelEstacion).getSelectedObject();
				lblEstacionV.setText(seleccionadoEstacion.getNombre());
				// seleccionadoActivo = null;
				getActivos();
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
		HorizontalPanel hpTexto = new HorizontalPanel();
		hpTexto.add(lblTexto);
		hpTexto.add(txtTexto);

		HorizontalPanel hpDestinoYVisible = new HorizontalPanel();
		hpDestinoYVisible.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hpDestinoYVisible.add(lblDestino);
		hpDestinoYVisible.add(lbxDestinosDelCargo);
		
		hpDestinoYVisible.add(lblPlazo);
		hpDestinoYVisible.add(dbPlazo);
		
		hpDestinoYVisible.add(chbVisible);

		HorizontalPanel hpEstacionActivo = new HorizontalPanel();
		hpEstacionActivo.add(lblEstacionT);
		hpEstacionActivo.add(lblEstacionV);
		
		hpEstacionActivo.add(lblActivoT);
		hpEstacionActivo.add(lblActivoV);
		
		vPanelPrincipal.add(hpTexto);
		vPanelPrincipal.add(hpDestinoYVisible);
		vPanelPrincipal.add(hpEstacionActivo);
		
		add(vPanelPrincipal);
		
		hpTexto.setWidth("100%");
		hpDestinoYVisible.setWidth("100%");
		hpEstacionActivo.setWidth("100%");
		
		hpDestinoYVisible.setSpacing(10);
		hpTexto.setSpacing(10);
		hpEstacionActivo.setSpacing(10);
		
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
		filterBox2.getElement().setPropertyString("placeholder", "Filtrar estaciones");
		vPanelPrincipal.setWidth("100%");
		hPanelPrincipal.setWidth("100%");

		btnBuscar.setWidth("100px");
		btnAceptar.setWidth("100px");
		btnCancelar.setWidth("100px");
		
		lblTitulo.setStyleName("Titulo");
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
		
		tabla.setVisible(false);

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

		hpNombre.add(filterBox2);
	}

	private void mostrarBusqueda() {
		glass.show();
		show();
		center();
	}

	private void seleccionar() {
		validar();
	}

	private void validar() {
		if (txtTexto.getText() == null || txtTexto.getText().equals("")){
			ValidadorPopup vpu = new ValidadorPopup(new GlassPopup(), "Info", "Debe ingresar el texto del pendiente");
			vpu.showPopUp();
			return;
		}
		
		if (lbxDestinosDelCargo.getSelectedIndex()<0){
			ValidadorPopup vpu = new ValidadorPopup(new GlassPopup(), "Info", "Debe seleccionar un destino del cargo");
			vpu.showPopUp();
			return;
		}
		
		if (seleccionadoActivo != null && seleccionadoEstacion != null){
			glass.hide();
			hide();
			
			PendienteData pd = new PendienteData();
			if (pendienteAEditar != null){
				pd = pendienteAEditar;
			
			} else {
				pd.setEstado(EstadoPendiente.INICIADO);
			}

			pd.setIdActivo(seleccionadoActivo.getId());
			pd.setComentario(txtTexto.getText());
			pd.setIdCreador(sesion.getId());
			pd.setIdDestinoDelCargo(Integer.valueOf(lbxDestinosDelCargo.getSelectedValue()));
			pd.setPlazo(dbPlazo.getValue());		
			pd.setComentarioVisible(chbVisible.getValue());
			
			iuWidgetPendientes.agregarPendiente(pd);
		} else {
			if (seleccionadoActivo == null){
				ValidadorPopup vpu = new ValidadorPopup(new GlassPopup(), "Info", "Debe seleccionar una activo");
				vpu.showPopUp();
			
			} else if (seleccionadoEstacion == null){
				ValidadorPopup vpu = new ValidadorPopup(new GlassPopup(), "Info", "Debe seleccionar un estacion");
				vpu.showPopUp();
			}
		}
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

	public final FilteredListDataProvider<Estacion> listaFiltrada2 = new FilteredListDataProvider<Estacion>(new IFilter<Estacion>(){
		public boolean isValid(Estacion value, String filter) {
			if(filter==null || value==null){
				return true;
			}

			return value.toString().toLowerCase().contains(filter.toLowerCase()); 
		}
	});

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
		tabla.setVisible(true);
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

	private List<Estacion> cargarLista2() {
		listaFiltrada2.getList().clear();
		List<Estacion> list = listaFiltrada2.getList();

		for (Estacion data : lista2) {
			list.add(data);
		}
		tabla2.redraw();
		return list;
	}

	private void crearTabla2() {
		TextColumn<Estacion> descColumn = new TextColumn<Estacion>() {
			@Override
			public String getValue(Estacion e) {
				return e.toString();
			}
		};

		tabla2.addColumn(descColumn,"Descripción");

		descColumn.setSortable(true);
		List<Estacion> list = cargarLista2();

		ListHandler<Estacion> columnSortHandlerDesc = new ListHandler<Estacion>(list);
		columnSortHandlerDesc.setComparator(descColumn, new Comparator<Estacion>() {
			public int compare(Estacion o1, Estacion o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.toString().compareTo(o2.toString()) : 1;
				}
				return -1;
			}
		});

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
