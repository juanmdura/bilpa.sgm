package app.client.iu.activos.generico.tipoMarcaModelo.marca;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import app.client.ProyectoBilpa;
import app.client.dominio.MarcaActivoGenerico;
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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

public class IUGestionarMarcaTipoActivoGenerico extends Composite {
	private MarcaActivoGenerico seleccionado;
	private List<MarcaActivoGenerico> lista = new ArrayList<MarcaActivoGenerico>();
	private CellTable<MarcaActivoGenerico> tabla = new CellTable<MarcaActivoGenerico>();
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
	private IUDialogoAgyModMarcaTipoActivoGenerico dialogo;
	private IUDialogoEliminarMarcaTipoActivoGenerico dialogoEliminar;

	private final String seleccioneTipoActivo = "Seleccione un tipo de activo para ver sus marcas";
	private final String noExistenMarcas = "No existen marcas para este tipo de activo";
	private Label lblTexto = new Label(seleccioneTipoActivo);

	private boolean inicio = true;
	private IUGestionarTipoMarcaModeloMain main;
	private Persona sesion;
	private TipoActivoGenericoData tipoActivoSeleccionado;

	private final FilteredListDataProvider<MarcaActivoGenerico> listaFiltrada = new FilteredListDataProvider<MarcaActivoGenerico>(new IFilter<MarcaActivoGenerico>()
			{
		public boolean isValid(MarcaActivoGenerico value, String filter) {
			if(filter==null || value==null){
				return true;
			}
			return value.getNombre().toLowerCase().contains(filter.toLowerCase());
		}
			});

	public IUGestionarMarcaTipoActivoGenerico(Persona sesion, final IUGestionarTipoMarcaModeloMain main) {
		super();
		this.sesion = sesion;
		this.main = main;

		final SelectionModel<MarcaActivoGenerico> selectionModel1 = new SingleSelectionModel<MarcaActivoGenerico>();
		tabla.setSelectionModel(selectionModel1);
		selectionModel1.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				seleccionado = ((SingleSelectionModel<MarcaActivoGenerico>) selectionModel1).getSelectedObject();
				main.marcaSeleccionado(seleccionado);
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

	public void setSeleccionado(MarcaActivoGenerico seleccionado) {
		this.seleccionado = seleccionado;
	}

	public TipoActivoGenericoData getTipoActivoSeleccionado() {
		return tipoActivoSeleccionado;
	}

	public VerticalPanel getPrincipalPanel() {
		return vPanelPrincipal;
	}

	private void eventos() {
		final IUGestionarMarcaTipoActivoGenerico iu = this;
		btnAgregar.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (tipoActivoSeleccionado != null){
					dialogo = new IUDialogoAgyModMarcaTipoActivoGenerico(false, iu, glass, popUp, null);
					dialogo.show();
					dialogo.center();
				} else {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe seleccionar un tipo de activo.");
					vpu.showPopUp();
				}
			}
		});

		btnEditar.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (seleccionado != null){
					dialogo = new IUDialogoAgyModMarcaTipoActivoGenerico(false, iu, glass, popUp, seleccionado.copiar());
					dialogo.show();
					dialogo.center();
				} else {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe seleccionar una marca.");
					vpu.showPopUp();
				}
			}
		});
	}

	public void obtenerLista() {
		if (tipoActivoSeleccionado != null){

			popUp.show();
			lista.clear();
			ProyectoBilpa.greetingService.obtenerMarcasActivoGenerico(tipoActivoSeleccionado, new AsyncCallback<List<MarcaActivoGenerico>>() {
				public void onFailure(Throwable caught) {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener marcas");
					vpu.showPopUp();
				}

				public void onSuccess(List<MarcaActivoGenerico> result) {
					for (int i=0; i < result.size(); i++){
						lista.add(result.get(i));
					}		
					load();
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

	private void load() {
		if (lista.size() > 0){
			if (inicio){
				inicio = false;
				crearTabla();
			} else {
				cargarLista();
			}
		} else {
			lblTexto.setText(noExistenMarcas);
			vPanel2Text.setVisible(true);
			vPanel2.setVisible(false);
		}
		main.marcaSeleccionado(null);
		popUp.hide();
	}

	private void setearWidgets() {
		filterBox.setWidth("140px");
		filterBox.getElement().setAttribute("placeHolder", "Filtrar marcas");

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
		TextColumn<MarcaActivoGenerico> namecolumn = new TextColumn<MarcaActivoGenerico>() {
			@Override
			public String getValue(MarcaActivoGenerico e) {
				return e.getNombre();
			}
		};

		final Column<MarcaActivoGenerico, String> eliminarColumn = new Column<MarcaActivoGenerico, String>(new ButtonImageCell()) {
			@Override
			public String getValue(MarcaActivoGenerico t) {
				return "img/menos.png";
			}
		};

		final IUGestionarMarcaTipoActivoGenerico thiss = this;
		eliminarColumn.setFieldUpdater(new FieldUpdater<MarcaActivoGenerico, String>() {
			public void update(int index, MarcaActivoGenerico object, String value) {
				dialogoEliminar = new IUDialogoEliminarMarcaTipoActivoGenerico(true, thiss, glass, popUp, object);
				DialogBox dialogBox = dialogoEliminar.dialElimTipoActivoGenerico();
				dialogBox.show();
				dialogBox.center();
			}
		});

		tabla.addColumn(namecolumn,"Marcas");
		tabla.addColumn(eliminarColumn, "");

		namecolumn.setSortable(true);

		List<MarcaActivoGenerico> list = cargarLista();
		ListHandler<MarcaActivoGenerico> columnSortHandlerNombre = new ListHandler<MarcaActivoGenerico>(list);

		columnSortHandlerNombre.setComparator(namecolumn,new Comparator<MarcaActivoGenerico>() {
			public int compare(MarcaActivoGenerico o1, MarcaActivoGenerico o2) {
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

	private List<MarcaActivoGenerico> cargarLista() {
		listaFiltrada.getList().clear();
		List<MarcaActivoGenerico> list = listaFiltrada.getList();

		for (MarcaActivoGenerico data : lista) {
			list.add(data);
		}
		tabla.redraw();
		return list;
	}

	public void tipoActivoSeleccionado(TipoActivoGenericoData tipoActivoSeleccionado) {
		this.tipoActivoSeleccionado = tipoActivoSeleccionado;
		obtenerLista();
	}
}
