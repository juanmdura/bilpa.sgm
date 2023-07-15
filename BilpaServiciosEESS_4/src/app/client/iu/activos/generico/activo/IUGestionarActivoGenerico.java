package app.client.iu.activos.generico.activo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import app.client.ProyectoBilpa;
import app.client.dominio.ActivoGenerico;
import app.client.dominio.Estacion;
import app.client.dominio.Persona;
import app.client.dominio.Sello;
import app.client.dominio.data.TipoActivoGenericoData;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.utilObjects.ButtonImageCell;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;
import app.client.utilidades.utilObjects.filter.FilteredListDataProvider;
import app.client.utilidades.utilObjects.filter.IFilter;
import app.client.utilidades.utilObjects.filter.IStringValueChanged;
import app.client.utilidades.utilObjects.filter.TextBoxFilter;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

public class IUGestionarActivoGenerico extends Composite {

	private HTML htmlTitulo = new HTML("Gestión de activos genéricos");

	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private VerticalPanel vPanel1 =  new VerticalPanel();
	private VerticalPanel vPanel2 =  new VerticalPanel();
	private HorizontalPanel hPanel1 =  new HorizontalPanel();

	private ListBox listBoxListaSellos = new ListBox();
	private ListBox listBoxListaEstaciones = new ListBox();
	private ListBox listBoxListaTiposActivosGenericos = new ListBox();

	private List<Sello> listaSellos = new ArrayList<Sello>();
	private List<Estacion> listaEstaciones = new ArrayList<Estacion>();
	private List<TipoActivoGenericoData> listaTiposActivos = new ArrayList<TipoActivoGenericoData>();

	private Persona sesion;
	private boolean inicio = true;

	private VerticalPanel vPanelDatos = new VerticalPanel();

	private Label lblSello = new Label ("Sello");	
	private Label lblEstacion = new Label ("Estación");	
	private Label lblTiposActivos = new Label ("Tipo Activo");

	private TextBox tNombre = new TextBox();

	private ActivoGenerico seleccionado;
	private FlexTable tablaContenedora = new FlexTable();
	private CellTable<ActivoGenerico> tabla = new CellTable<ActivoGenerico>();
	private SimplePager.Resources pagerResources;
	private SimplePager pager;
	private TextBoxFilter filterBox = new TextBoxFilter();

	private List<ActivoGenerico> lista = new ArrayList<ActivoGenerico>();

	private PopupCargando popUp = new PopupCargando("Cargando...");

	private GlassPopup glass = new GlassPopup();

	private Label lblTexto = new Label("No existen activos para este tipo");
	private IUDialogoAgyModActivoGenerico dialogo;
	private IUDialogoEliminarActivoGenerico dialogoEliminar;

	private Button btnAgregar = new Button("Agregar");
	private Button btnEditar = new Button("Editar");
	private IUGestionarActivoGenerico iu;

	public VerticalPanel getPrincipalPanel() {
		return vPanelPrincipal;
	}

	public Estacion getEstacionSeleccionada(){
		if (listBoxListaEstaciones.getSelectedIndex() >= 0){
			return listaEstaciones.get(listBoxListaEstaciones.getSelectedIndex());
		}
		return null;
	}

	public TipoActivoGenericoData getTipoActivoSeleccionado(){
		if (listBoxListaTiposActivosGenericos.getSelectedIndex() >= 0){
			return listaTiposActivos.get(listBoxListaTiposActivosGenericos.getSelectedIndex());
		}
		return null;
	}

	public IUGestionarActivoGenerico(Persona persona){
		this.sesion = persona;
		iu = this;

		final SelectionModel<ActivoGenerico> selectionModel1 = new SingleSelectionModel<ActivoGenerico>();
		tabla.setSelectionModel(selectionModel1);
		selectionModel1.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				seleccionado = ((SingleSelectionModel<ActivoGenerico>) selectionModel1).getSelectedObject();
			}
		});
		tabla.setWidth("100%");
		pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(tabla);
		pager.setPageSize(15);

		setearWidgets();			//Setea el tamano de los Widgets.

		cargarPanelesConWidgets();	//Agrega los Widget a los paneles.
		cargarLtBoxSello();	
		color();
		eventos();
		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);



	}

	private void eventos(){
		listBoxListaSellos.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				cargarLtBoxEstaciones(Integer.valueOf(listBoxListaSellos.getValue(listBoxListaSellos.getSelectedIndex())));	
			}
		});

		listBoxListaEstaciones.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				cargarActivosGenericos();	
			}
		});

		listBoxListaTiposActivosGenericos.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				cargarActivosGenericos();
			}
		});

		tNombre.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER){
					validarTodo();
				}				
			}
		});

		btnAgregar.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogo = new IUDialogoAgyModActivoGenerico(false, iu, glass, popUp, null, 
						listaTiposActivos.get(listBoxListaTiposActivosGenericos.getSelectedIndex()).getMarcas(),
						listaTiposActivos.get(listBoxListaTiposActivosGenericos.getSelectedIndex()).getModelos());
				dialogo.show();
				dialogo.center();
			}
		});

		btnEditar.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (seleccionado != null){
					dialogo = new IUDialogoAgyModActivoGenerico(false, iu, glass, popUp, (ActivoGenerico)seleccionado.copiarTodo(),
							listaTiposActivos.get(listBoxListaTiposActivosGenericos.getSelectedIndex()).getMarcas(),
							listaTiposActivos.get(listBoxListaTiposActivosGenericos.getSelectedIndex()).getModelos());
					dialogo.show();
					dialogo.center();
				} else {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe seleccionar un activo.");
					vpu.showPopUp();
				}
			}
		});
	}

	public final FilteredListDataProvider<ActivoGenerico> listaFiltrada = new FilteredListDataProvider<ActivoGenerico>(new IFilter<ActivoGenerico>(){
		public boolean isValid(ActivoGenerico value, String filter) {
			if(filter==null || value==null){
				return true;
			}
			return value.getSerie().toLowerCase().contains(filter.toLowerCase());
		}
	});

	private void color() {
		htmlTitulo.setStyleName("Titulo");

	}

	private void setearWidgets() {
		filterBox.setWidth("140px");
		filterBox.getElement().setAttribute("placeHolder", "Filtrar tipos");

		htmlTitulo.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		listBoxListaSellos.setVisibleItemCount(1);
		listBoxListaSellos.setTitle("Lista de Sellos");
		listBoxListaSellos.setWidth("250px");

		listBoxListaEstaciones.setVisibleItemCount(1);
		listBoxListaEstaciones.setTitle("Lista de Estaciones");
		listBoxListaEstaciones.setWidth("250px");

		listBoxListaTiposActivosGenericos.setVisibleItemCount(1);
		listBoxListaTiposActivosGenericos.setTitle("Lista de Tipos de Activos");
		listBoxListaTiposActivosGenericos.setWidth("250px");

		tNombre.setTitle("Ingrese la descripción del caño");

		tNombre.setWidth("250px");

		tablaContenedora.setCellSpacing(5);
		tablaContenedora.setCellPadding(2);

		lblSello.setStyleName("Negrita");
		lblEstacion.setStyleName("Negrita");

		lblTiposActivos.setStyleName("Negrita");

		tablaContenedora.setWidget(0, 0, lblSello);
		tablaContenedora.setWidget(0, 1, listBoxListaSellos);
		tablaContenedora.setWidget(0, 2, new Label(" "));

		tablaContenedora.setWidget(0, 3, lblEstacion);
		tablaContenedora.setWidget(0, 4, listBoxListaEstaciones);

		tablaContenedora.setWidget(0, 5, new Label(" "));

		tablaContenedora.setWidget(0, 6, lblTiposActivos);
		tablaContenedora.setWidget(0, 7, listBoxListaTiposActivosGenericos);

	}

	private void cargarPanelesConWidgets() {
		vPanelDatos.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		vPanelPrincipal.add(htmlTitulo);
		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelDatos.add(this.tablaContenedora);

		vPanelPrincipal.setSpacing(20);
		vPanelPrincipal.add(vPanelDatos);

		vPanel2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanel2.add(tabla);
		vPanel2.add(pager);

		hPanel1.add(vPanel2);
		vPanelPrincipal.add(hPanel1);
		vPanelPrincipal.add(vPanel1);

		vPanel1.setSpacing(15);
		vPanel1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanel1.add(lblTexto);
	}

	private void cargarLtBoxSello(){
		listBoxListaSellos.clear();
		listaSellos.clear();
		popUp.show();
		ProyectoBilpa.greetingService.obtenerSellos(new AsyncCallback<ArrayList<Sello>>() {

			public void onFailure(Throwable caught) {
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener los sellos");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<Sello> result) {
				Sello auxiliar;
				for (int i=0; i < result.size(); i++){
					auxiliar = (Sello) result.get(i);
					if(auxiliar.getId() != 1){
						listBoxListaSellos.addItem(auxiliar.toString(),String.valueOf(auxiliar.getId()));
						listaSellos.add(auxiliar);

					}
				}
				listBoxListaSellos.setSelectedIndex(Sello.ANCAP_CONTRATOS-2);
				cargarLtBoxEstaciones(Sello.ANCAP_CONTRATOS);
				popUp.hide();
			}
		});		
	}


	private void cargarLtBoxEstaciones(int idSello){
		listBoxListaEstaciones.clear();
		listaEstaciones.clear();
		popUp.show();
		ProyectoBilpa.greetingService.obtenerEstaciones(idSello, new AsyncCallback<List<Estacion>>() {

			public void onFailure(Throwable caught) {
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener las Estaciones");
				vpu.showPopUp();
			}

			public void onSuccess(List<Estacion> result) {
				Estacion auxiliar;
				for (int i=0; i < result.size(); i++){
					auxiliar = (Estacion) result.get(i);
					if(auxiliar.getId() != 1){
						listBoxListaEstaciones.addItem(auxiliar.toString(),String.valueOf(auxiliar.getId()));
						listaEstaciones.add(auxiliar);
					}
				}
				cargarLtBoxTiposActivos();
				popUp.hide();
			}
		});		
	}

	private void cargarLtBoxTiposActivos(){
		listBoxListaTiposActivosGenericos.clear();
		listaTiposActivos.clear();
		popUp.show();
		ProyectoBilpa.greetingService.obtenerTiposActivoGenerico(new AsyncCallback<List<TipoActivoGenericoData>>() {

			public void onFailure(Throwable caught) {
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener tipos de activos genéricos");
				vpu.showPopUp();
			}

			public void onSuccess(List<TipoActivoGenericoData> result) {
				TipoActivoGenericoData auxiliar;
				for (int i=0; i < result.size(); i++){
					auxiliar = (TipoActivoGenericoData) result.get(i);
					// if(auxiliar.getId() != 1){
						listBoxListaTiposActivosGenericos.addItem(auxiliar.toString(),String.valueOf(auxiliar.getId()));
						listaTiposActivos.add(auxiliar);
					//}
				}
				cargarActivosGenericos();
				popUp.hide();
			}
		});		
	}



	public void cargarActivosGenericos() {
		if (listBoxListaEstaciones.getItemCount() == 0){
			return;
		}
		int idEstacion = Integer.valueOf(this.listBoxListaEstaciones.getValue(this.listBoxListaEstaciones.getSelectedIndex()));
		int idTipoActivoGenerico = Integer.valueOf(this.listBoxListaTiposActivosGenericos.getValue(this.listBoxListaTiposActivosGenericos.getSelectedIndex()));

		ProyectoBilpa.greetingService.obtenerActivosGenericos(new Estacion(idEstacion), idTipoActivoGenerico, new AsyncCallback<List<ActivoGenerico>>() {

			public void onSuccess(List<ActivoGenerico> result) {
				lista = result;

				if (lista.size() > 0){
					btnEditar.setVisible(true);
					lblTexto.setVisible(false);
					vPanel2.setVisible(true);

					vPanel1.remove(btnAgregar);
					hPanel1.add(btnAgregar);
					hPanel1.add(btnEditar);

					if (inicio){
						inicio = false;
						crearTabla();
					} else {
						cargarLista();
					}
				} else {
					vPanel1.add(btnAgregar);
					btnEditar.setVisible(false);
					lblTexto.setVisible(true);
					vPanel2.setVisible(false);

				}
			}

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener activos por tipo");
				vpu.showPopUp();
			}
		});
	}

	private void crearTabla() {
		TextColumn<ActivoGenerico> serieColumn = new TextColumn<ActivoGenerico>() {
			@Override
			public String getValue(ActivoGenerico e) {
				return e.getSerie();
			}
		};

		TextColumn<ActivoGenerico> marcaColumn = new TextColumn<ActivoGenerico>() {
			@Override
			public String getValue(ActivoGenerico e) {
				if (e.getMarca() != null){
					return e.getMarca().getNombre();
				}
				return "";
			}
		};

		TextColumn<ActivoGenerico> modeloColumn = new TextColumn<ActivoGenerico>() {
			@Override
			public String getValue(ActivoGenerico e) {
				if (e.getModelo() != null){
					return e.getModelo().getNombre();
				}
				return "";
			}
		};

		TextColumn<ActivoGenerico> fechaColumn = new TextColumn<ActivoGenerico>() {
			@Override
			public String getValue(ActivoGenerico e) {
				if (e.getFecha() != null){
					return DateTimeFormat.getFormat("yyyy-MM-dd").format(e.getFecha());
				} else {
					return "";
				}
			}
		};

		TextColumn<ActivoGenerico> descColumn = new TextColumn<ActivoGenerico>() {
			@Override
			public String getValue(ActivoGenerico e) {
				return e.getDescripcion();
			}
		};

		final Column<ActivoGenerico, String> eliminarColumn = new Column<ActivoGenerico, String>(new ButtonImageCell()) {
			@Override
			public String getValue(ActivoGenerico t) {
				return "img/menos.png";
			}
		};

		eliminarColumn.setFieldUpdater(new FieldUpdater<ActivoGenerico, String>() {
			public void update(int index, ActivoGenerico object, String value) {
				dialogoEliminar = new IUDialogoEliminarActivoGenerico(true, iu, glass, popUp, object);
				DialogBox dialogBox = dialogoEliminar.dialElimTipoActivoGenerico();
				dialogBox.show();
				dialogBox.center();
			}
		});

		tabla.addColumn(serieColumn,"Serie");
		tabla.addColumn(marcaColumn,"Marca");
		tabla.addColumn(modeloColumn,"Modelo");
		tabla.addColumn(fechaColumn,"Fecha creación");
		tabla.addColumn(descColumn,"Descripción");
		tabla.addColumn(eliminarColumn,"");

		serieColumn.setSortable(true);
		marcaColumn.setSortable(true);
		modeloColumn.setSortable(true);
		fechaColumn.setSortable(true);
		descColumn.setSortable(true);
		List<ActivoGenerico> list = cargarLista();

		ListHandler<ActivoGenerico> columnSortHandlerSerie = new ListHandler<ActivoGenerico>(list);
		columnSortHandlerSerie.setComparator(serieColumn, new Comparator<ActivoGenerico>() {
			public int compare(ActivoGenerico o1, ActivoGenerico o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getSerie().compareTo(o2.getSerie()) : 1;
				}
				return -1;
			}
		});

		ListHandler<ActivoGenerico> columnSortHandlerMarca = new ListHandler<ActivoGenerico>(list);
		columnSortHandlerMarca.setComparator(marcaColumn, new Comparator<ActivoGenerico>() {
			public int compare(ActivoGenerico o1, ActivoGenerico o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getMarca().getNombre().compareTo(o2.getMarca().getNombre()) : 1;
				}
				return -1;
			}
		});

		ListHandler<ActivoGenerico> columnSortHandlerModelo = new ListHandler<ActivoGenerico>(list);
		columnSortHandlerModelo.setComparator(modeloColumn, new Comparator<ActivoGenerico>() {
			public int compare(ActivoGenerico o1, ActivoGenerico o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getModelo().getNombre().compareTo(o2.getModelo().getNombre()) : 1;
				}
				return -1;
			}
		});

		ListHandler<ActivoGenerico> columnSortHandlerFecha = new ListHandler<ActivoGenerico>(list);
		columnSortHandlerFecha.setComparator(fechaColumn, new Comparator<ActivoGenerico>() {
			public int compare(ActivoGenerico o1, ActivoGenerico o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getFecha().compareTo(o2.getFecha()) : 1;
				}
				return -1;
			}
		});

		ListHandler<ActivoGenerico> columnSortHandlerDesc = new ListHandler<ActivoGenerico>(list);
		columnSortHandlerDesc.setComparator(descColumn, new Comparator<ActivoGenerico>() {
			public int compare(ActivoGenerico o1, ActivoGenerico o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getDescripcion().compareTo(o2.getDescripcion()) : 1;
				}
				return -1;
			}
		});

		tabla.addColumnSortHandler(columnSortHandlerSerie);
		tabla.addColumnSortHandler(columnSortHandlerMarca);
		tabla.addColumnSortHandler(columnSortHandlerModelo);
		tabla.addColumnSortHandler(columnSortHandlerFecha);
		tabla.addColumnSortHandler(columnSortHandlerDesc);

		listaFiltrada.addDataDisplay(tabla);

		filterBox.addValueChangeHandler(new IStringValueChanged() {
			public void valueChanged(String newValue) {
				listaFiltrada.setFilter(newValue);
				listaFiltrada.refresh();
			}
		});
	}

	private List<ActivoGenerico> cargarLista() {
		listaFiltrada.getList().clear();
		List<ActivoGenerico> list = listaFiltrada.getList();

		for (ActivoGenerico data : lista) {
			list.add(data);
		}
		tabla.redraw();
		return list;
	}

	private void validarTodo() {
		if (this.validarCampos()) {
			crearActivo();
		}
	}

	private void crearActivo(){
		ActivoGenerico activo = new ActivoGenerico();
		String descripcion = tNombre.getText();
		int idEstacion = Integer.valueOf(this.listBoxListaEstaciones.getValue(this.listBoxListaEstaciones.getSelectedIndex()));
		activo.setEmpresa(new Estacion(idEstacion));
		activo.setDescripcion(descripcion);
		guardarActivo(activo);
	}

	private void guardarActivo(ActivoGenerico activo){	
		ProyectoBilpa.greetingService.guardarActivoGenerico(activo, new AsyncCallback<Boolean>() {

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar el activo");
				vpu.showPopUp();
			}
			public void onSuccess(Boolean result) {
				cargarActivosGenericos();
			}
		});		
	}

	private boolean validarCampos(){
		/*if(this.tNombre.getText().equalsIgnoreCase("")){
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe ingresar la descripción del Caño");
			vpu.showPopUp();
			return false;
		}
		if(this.tNombre.getText().trim().equalsIgnoreCase("")){
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe ingresar la descripción del Caño");
			vpu.showPopUp();
			return false;
		}*/

		return true;
	}
}
