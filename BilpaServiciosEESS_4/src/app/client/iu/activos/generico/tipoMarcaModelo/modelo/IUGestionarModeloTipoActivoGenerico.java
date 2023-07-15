package app.client.iu.activos.generico.tipoMarcaModelo.modelo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import app.client.ProyectoBilpa;
import app.client.dominio.MarcaActivoGenerico;
import app.client.dominio.ModeloActivoGenerico;
import app.client.dominio.Persona;
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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

public class IUGestionarModeloTipoActivoGenerico extends Composite {
	private ModeloActivoGenerico seleccionado;
	private List<ModeloActivoGenerico> lista = new ArrayList<ModeloActivoGenerico>();
	private CellTable<ModeloActivoGenerico> tabla = new CellTable<ModeloActivoGenerico>();
	private SimplePager.Resources pagerResources;
	private SimplePager pager;
	private TextBoxFilter filterBox = new TextBoxFilter();

	private Button btnAgregar = new Button("Agregar");
	private Button btnEditar = new Button("Editar");

	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private HorizontalPanel hPanel1 = new HorizontalPanel();
	private VerticalPanel vPanel2 = new VerticalPanel();
	private VerticalPanel vPanel2Text = new VerticalPanel();
	private VerticalPanel vPanel3 = new VerticalPanel();

	private PopupCargando popUp = new PopupCargando("Cargando...");
	private GlassPopup glass = new GlassPopup();
	private IUDialogoAgyModModeloTipoActivoGenerico dialogo;
	private IUDialogoEliminarModeloTipoActivoGenerico dialogoEliminar;

	private final String seleccioneTipoActivo = "Seleccione una marca para ver sus modelos";
	private final String noExistenModelos = "No existen modelos para esta marca";
	private Label lblTexto = new Label(seleccioneTipoActivo);

	private boolean inicio = true;

	private Persona sesion;
	private MarcaActivoGenerico marcaSeleccionado;

	private final FilteredListDataProvider<ModeloActivoGenerico> listaFiltrada = new FilteredListDataProvider<ModeloActivoGenerico>(new IFilter<ModeloActivoGenerico>()
			{
		public boolean isValid(ModeloActivoGenerico value, String filter) {
			if(filter==null || value==null){
				return true;
			}
			return value.getNombre().toLowerCase().contains(filter.toLowerCase());
		}
			});

	public IUGestionarModeloTipoActivoGenerico(Persona sesion) {
		super();
		this.sesion = sesion;

		final SelectionModel<ModeloActivoGenerico> selectionModel1 = new SingleSelectionModel<ModeloActivoGenerico>();
		tabla.setSelectionModel(selectionModel1);
		selectionModel1.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				seleccionado = ((SingleSelectionModel<ModeloActivoGenerico>) selectionModel1).getSelectedObject();
			}
		});

		pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(tabla);
		pager.setPageSize(15);

		setearWidgets();
		agregarWidgets();
		obtenerLista();
		eventos();
	}

	public void setSeleccionado(ModeloActivoGenerico seleccionado) {
		this.seleccionado = seleccionado;
	}

	public MarcaActivoGenerico getMarcaSeleccionado() {
		return marcaSeleccionado;
	}

	public VerticalPanel getPrincipalPanel() {
		return vPanelPrincipal;
	}

	private void eventos() {
		final IUGestionarModeloTipoActivoGenerico iu = this;
		btnAgregar.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (marcaSeleccionado != null){
					dialogo = new IUDialogoAgyModModeloTipoActivoGenerico(false, iu, glass, popUp, null);
					dialogo.show();
					dialogo.center();
				} else {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe seleccionar una marca.");
					vpu.showPopUp();
				}
			}
		});

		btnEditar.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (seleccionado != null){
					dialogo = new IUDialogoAgyModModeloTipoActivoGenerico(false, iu, glass, popUp, seleccionado.copiar());
					dialogo.show();
					dialogo.center();
				} else {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe seleccionar un modelo.");
					vpu.showPopUp();
				}
			}
		});
	}

	public void obtenerLista() {
		if (marcaSeleccionado != null){

			popUp.show();
			lista.clear();

			ProyectoBilpa.greetingService.obtenerModelosActivoGenerico(marcaSeleccionado, new AsyncCallback<List<ModeloActivoGenerico>>() {
				public void onFailure(Throwable caught) {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener modelos");
					vpu.showPopUp();
				}

				public void onSuccess(List<ModeloActivoGenerico> result) {
					for (int i=0; i < result.size(); i++){
						lista.add(result.get(i));
					}		
					if (lista.size() > 0){
						if (inicio){
							inicio = false;
							crearTabla();
						} else {
							cargarLista();
						}
					} else {
						lblTexto.setText(noExistenModelos);
						vPanel2Text.setVisible(true);
						vPanel2.setVisible(false);
					}
					popUp.hide();
				}
			});		
			vPanel2Text.setVisible(false);
			vPanel2.setVisible(true);
		} else {
			lblTexto.setText(seleccioneTipoActivo);
			vPanel2Text.setVisible(true);
			vPanel2.setVisible(false);
		}
	}

	private void setearWidgets() {
		filterBox.setWidth("140px");
		filterBox.getElement().setAttribute("placeHolder", "Filtrar modelos");

		vPanel2Text.add(lblTexto);
		vPanel2Text.setVisible(true);
		vPanel2.setVisible(false);
	}

	private void agregarWidgets() {
		hPanel1.setSpacing(5);
		hPanel1.add(filterBox);
		hPanel1.add(btnAgregar);
		hPanel1.add(btnEditar);

		vPanel2.add(tabla);
		vPanel2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanel2.add(pager);

		vPanel3.setSpacing(5);
		vPanel3.add(hPanel1);
		vPanel3.add(vPanel2);
		vPanel3.add(vPanel2Text);
		vPanelPrincipal.add(vPanel3);
	}

	private void crearTabla() {
		TextColumn<ModeloActivoGenerico> namecolumn = new TextColumn<ModeloActivoGenerico>() {
			@Override
			public String getValue(ModeloActivoGenerico e) {
				return e.getNombre();
			}
		};

		final Column<ModeloActivoGenerico, String> eliminarColumn = new Column<ModeloActivoGenerico, String>(new ButtonImageCell()) {
			@Override
			public String getValue(ModeloActivoGenerico t) {
				return "img/menos.png";
			}
		};

		final IUGestionarModeloTipoActivoGenerico thiss = this;
		eliminarColumn.setFieldUpdater(new FieldUpdater<ModeloActivoGenerico, String>() {
			public void update(int index, ModeloActivoGenerico object, String value) {
				dialogoEliminar = new IUDialogoEliminarModeloTipoActivoGenerico(true, thiss, glass, popUp, object);
				DialogBox dialogBox = dialogoEliminar.dialElimTipoActivoGenerico();
				dialogBox.show();
				dialogBox.center();
			}
		});

		tabla.addColumn(namecolumn,"Modelos");
		tabla.addColumn(eliminarColumn, "");

		namecolumn.setSortable(true);

		List<ModeloActivoGenerico> list = cargarLista();
		ListHandler<ModeloActivoGenerico> columnSortHandlerNombre = new ListHandler<ModeloActivoGenerico>(list);

		columnSortHandlerNombre.setComparator(namecolumn,new Comparator<ModeloActivoGenerico>() {
			public int compare(ModeloActivoGenerico o1, ModeloActivoGenerico o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getNombre().compareTo(o2.getNombre()) : 1;
				}
				return -1;
			}
		});


		tabla.addColumnSortHandler(columnSortHandlerNombre);
		listaFiltrada.addDataDisplay(tabla);

		filterBox.addValueChangeHandler(new IStringValueChanged() {
			public void valueChanged(String newValue) {
				listaFiltrada.setFilter(newValue);
				listaFiltrada.refresh();
			}
		});
	}

	private List<ModeloActivoGenerico> cargarLista() {
		listaFiltrada.getList().clear();
		List<ModeloActivoGenerico> list = listaFiltrada.getList();

		for (ModeloActivoGenerico data : lista) {
			list.add(data);
		}
		tabla.redraw();
		return list;
	}

	public void marcaSeleccionado(MarcaActivoGenerico marcaSeleccionado) {
		this.marcaSeleccionado = marcaSeleccionado;
		obtenerLista();
	}
}
