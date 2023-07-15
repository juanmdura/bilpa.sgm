package app.client.iu.activos.generico.tipoMarcaModelo.tipo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import app.client.ProyectoBilpa;
import app.client.dominio.Persona;
import app.client.dominio.data.TipoActivoGenericoData;
import app.client.iu.activos.generico.tipoMarcaModelo.IUGestionarTipoMarcaModeloMain;
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
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

public class IUGestionarTipoActivoGenerico extends Composite {
	private TipoActivoGenericoData seleccionado;
	private List<TipoActivoGenericoData> lista = new ArrayList<TipoActivoGenericoData>();
	private CellTable<TipoActivoGenericoData> tabla = new CellTable<TipoActivoGenericoData>();
	private SimplePager.Resources pagerResources;
	private SimplePager pager;
	private TextBoxFilter filterBox = new TextBoxFilter();

	private Button btnAgregar = new Button("Agregar");
	private Button btnEditar = new Button("Editar");

	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private HorizontalPanel hPanel1 = new HorizontalPanel();
	private VerticalPanel vPanel2 = new VerticalPanel();
	private VerticalPanel vPanel3 = new VerticalPanel();

	private PopupCargando popUp = new PopupCargando("Cargando...");
	private GlassPopup glass = new GlassPopup();
	private IUDialogoAgyModTipoActivoGenerico dialogo;
	private IUDialogoEliminarTipoActivoGenerico dialogoEliminar;

	private boolean inicio = true;
	private IUGestionarTipoMarcaModeloMain main;
	private Persona sesion;

	public final FilteredListDataProvider<TipoActivoGenericoData> listaFiltrada = new FilteredListDataProvider<TipoActivoGenericoData>(new IFilter<TipoActivoGenericoData>()
			{
		public boolean isValid(TipoActivoGenericoData value, String filter) {
			if(filter==null || value==null){
				return true;
			}
			return value.getNombre().toLowerCase().contains(filter.toLowerCase());
		}
			});

	public IUGestionarTipoActivoGenerico(Persona sesion, final IUGestionarTipoMarcaModeloMain main) {
		super();
		this.sesion = sesion;
		this.main = main;

		final SelectionModel<TipoActivoGenericoData> selectionModel1 = new SingleSelectionModel<TipoActivoGenericoData>();
		tabla.setSelectionModel(selectionModel1);
		selectionModel1.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				seleccionado = ((SingleSelectionModel<TipoActivoGenericoData>) selectionModel1).getSelectedObject();
				main.tipoActivoSeleccionado(seleccionado);
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

	public VerticalPanel getPrincipalPanel() {
		return vPanelPrincipal;
	}

	private void eventos() {
		final IUGestionarTipoActivoGenerico iu = this;
		btnAgregar.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogo = new IUDialogoAgyModTipoActivoGenerico(false, iu, glass, popUp, null);
				dialogo.show();
				dialogo.center();
			}
		});

		btnEditar.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (seleccionado != null){
					dialogo = new IUDialogoAgyModTipoActivoGenerico(false, iu, glass, popUp, seleccionado);
					dialogo.show();
					dialogo.center();
				} else {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe seleccionar un tipo de activo.");
					vpu.showPopUp();
				}
			}
		});
	}

	public void obtenerLista() {
		popUp.show();
		lista.clear();
		ProyectoBilpa.greetingService.obtenerTiposActivoGenerico(new AsyncCallback<List<TipoActivoGenericoData>>() {
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener tipos de activos");
				vpu.showPopUp();
			}

			public void onSuccess(List<TipoActivoGenericoData> result) {
				for (int i=0; i < result.size(); i++){
					lista.add(result.get(i));
				}		
				if (inicio){
					inicio = false;
					crearTabla();
				} else {
					cargarLista();
				}
				main.tipoActivoSeleccionado(null);
				popUp.hide();
			}
		});		
	}

	private void setearWidgets() {
		//tableTipos.setWidth("350px");
		filterBox.setWidth("140px");
		filterBox.getElement().setAttribute("placeHolder", "Filtrar tipos");

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
		vPanelPrincipal.add(vPanel3);
	}

	private void crearTabla() {
		TextColumn<TipoActivoGenericoData> namecolumn = new TextColumn<TipoActivoGenericoData>() {
			@Override
			public String getValue(TipoActivoGenericoData e) {
				return e.getNombre();
			}
		};

		final Column<TipoActivoGenericoData, String> eliminarColumn = new Column<TipoActivoGenericoData, String>(new ButtonImageCell()) {
			@Override
			public String getValue(TipoActivoGenericoData t) {
				return "img/menos.png";
			}
		};

		final IUGestionarTipoActivoGenerico thiss = this;
		eliminarColumn.setFieldUpdater(new FieldUpdater<TipoActivoGenericoData, String>() {
			public void update(int index, TipoActivoGenericoData object, String value) {
				dialogoEliminar = new IUDialogoEliminarTipoActivoGenerico(true, thiss, glass, popUp, object);
				DialogBox dialogBox = dialogoEliminar.dialElimTipoActivoGenerico();
				dialogBox.show();
				dialogBox.center();
			}
		});

		tabla.addColumn(namecolumn,"Tipos");
		tabla.addColumn(eliminarColumn, "");

		namecolumn.setSortable(true);

		List<TipoActivoGenericoData> list = cargarLista();
		ListHandler<TipoActivoGenericoData> columnSortHandlerNombre = new ListHandler<TipoActivoGenericoData>(list);

		columnSortHandlerNombre.setComparator(namecolumn,new Comparator<TipoActivoGenericoData>() {
			public int compare(TipoActivoGenericoData o1, TipoActivoGenericoData o2) {
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

	public List<TipoActivoGenericoData> cargarLista() {
		listaFiltrada.getList().clear();
		List<TipoActivoGenericoData> list = listaFiltrada.getList();

		for (TipoActivoGenericoData data : lista) {
			list.add(data);
		}
		tabla.redraw();
		return list;
	}
}
