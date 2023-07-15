package app.client.iu.orden.tecnico.dialogoAgregarSolucion.tab;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import app.client.ProyectoBilpa;
import app.client.dominio.Tarea;
import app.client.dominio.TipoTarea;
import app.client.iu.orden.tecnico.dialogoAgregarSolucion.IUWidgetSolucion;
import app.client.iu.widgets.ValidadorPopup;
import app.client.resources.CellTableSmallResource;
import app.client.utilidades.Constants;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;
import app.client.utilidades.utilObjects.filter.FilteredListDataProvider;
import app.client.utilidades.utilObjects.filter.IFilter;
import app.client.utilidades.utilObjects.filter.IStringValueChanged;
import app.client.utilidades.utilObjects.filter.TextBoxFilter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

public class IUWidgetTareaTab extends Composite
{
	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private FlexTable tableDatos = new FlexTable();
	
	private Label lblTitulo = new Label("Tarea realizada");
	private Label lblTipo = new Label("Tipo de tarea");
	private Label lblFiltrar = new Label("Filtrar");
	
	public ListBox listBoxTiposTareas = new ListBox();

	public ArrayList<TipoTarea> tiposTareasMemoria = new ArrayList<TipoTarea>();
	
	private TextBoxFilter filterBox = new TextBoxFilter();
	public CellTable<Tarea> cellTableTareas = new CellTable<Tarea>(10, CellTableSmallResource.INSTANCE);	
	private ArrayList<Tarea> todasLasTareas = new ArrayList<Tarea>();
	private Tarea tareaSeleccionada;
	private PopupCargando popUp;
	private SimplePager.Resources pagerResources;
	private SimplePager pager;
	private IUWidgetSolucion iuWidgetSolucion;
	
	private GlassPopup glass = new GlassPopup();

	public VerticalPanel getPrincipalPanel() {
		return vPanelPrincipal;
	}
	
	public Tarea getTareaSeleccionada() {
		return tareaSeleccionada;
	}
	
	public IUWidgetTareaTab(PopupCargando popUp, final IUWidgetSolucion iuWidgetSolucion) 
	{
		this.popUp = popUp;
		this.iuWidgetSolucion = iuWidgetSolucion;
		listBoxTiposTareas.addChangeHandler(new ChangeHandler()
		{
			public void onChange(ChangeEvent arg0) 
			{
				cargarTareas();
			}
		});
		
		final SelectionModel<Tarea> selectionModel1 = new SingleSelectionModel<Tarea>();
		cellTableTareas.setSelectionModel(selectionModel1);
		selectionModel1.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				tareaSeleccionada = ((SingleSelectionModel<Tarea>) selectionModel1).getSelectedObject();
				iuWidgetSolucion.lblTareaData.setText(tareaSeleccionada.getDescripcion());
			}
		});
		
		pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(cellTableTareas);
		pager.setPageSize(9);
		
		setear();
		agregar();
		cargarLtBoxTipoTarea();
	}

	public final FilteredListDataProvider<Tarea> tareasFiltradas = new FilteredListDataProvider<Tarea>(new IFilter<Tarea>()
	{
		public boolean isValid(Tarea value, String filter){
			if(filter==null || value==null){
				return true;
			}
			return value.getDescripcion().toLowerCase().contains(filter.toLowerCase()) || value.getTipoTarea().getDescripcion().toLowerCase().contains(filter.toLowerCase());
		}
	});
	
	private void setear(){
		vPanelPrincipal.setSize("1220px", "380px");
		cellTableTareas.setWidth("1220px");
		filterBox.setWidth("220px");
		listBoxTiposTareas.setWidth("220px");
		
		lblTitulo.setStyleName("SubTitulo");
		lblTipo.setStyleName("Negrita");
		lblFiltrar.setStyleName("Negrita");
	}
	
	private void agregar(){
		tableDatos.setWidget(0, 0, lblTipo);
		tableDatos.setWidget(0, 1, listBoxTiposTareas);
		
		tableDatos.setWidget(1, 0, lblFiltrar);
		tableDatos.setWidget(1, 1, filterBox);
		
		tableDatos.getFlexCellFormatter().setColSpan(2, 0, 2);
		tableDatos.setWidget(2, 0, cellTableTareas);
		
		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal.setSpacing(5);
		vPanelPrincipal.add(lblTitulo);
		vPanelPrincipal.add(tableDatos);
		vPanelPrincipal.add(pager);
		
		//decorator.add(vPanelPrincipal);
	}
	
	private void cargarLtBoxTipoTarea() {
		listBoxTiposTareas.clear();
		popUp.show();

		ProyectoBilpa.greetingService.obtenerTodasLosTiposTareasActivos(new AsyncCallback<ArrayList<TipoTarea>>() {
			public void onFailure(Throwable caught) 
			{
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener todas los tipo de tareas");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<TipoTarea> result) 
			{
				TipoTarea auxiliar;
				tiposTareasMemoria.clear();				
				listBoxTiposTareas.addItem(Constants.TODOS, -1+"");

				for(TipoTarea ttSinClasificar : result)
				{
					if(ttSinClasificar.getDescripcion().equals(Constants.SIN_CLASIFICAR))
					{
						listBoxTiposTareas.addItem(ttSinClasificar.toString(),String.valueOf(ttSinClasificar.getId()));
						tiposTareasMemoria.add(ttSinClasificar);
					}
				}

				for (int i=0; i<result.size();i++)
				{
					auxiliar = (TipoTarea) result.get(i);
					if(!auxiliar.getDescripcion().equals(Constants.SIN_CLASIFICAR))
					{
						listBoxTiposTareas.addItem(auxiliar.toString(),String.valueOf(auxiliar.getId()));
						tiposTareasMemoria.add(auxiliar);
					}
				}			
				cargarTareas1();
				popUp.hide();
			}
		});		
	}

	public void cargarTareas1()
	{
		popUp.show();
		todasLasTareas.clear();

		ProyectoBilpa.greetingService.obtenerTodasLasTareas(new AsyncCallback<ArrayList<Tarea>>() {
			public void onFailure(Throwable caught) 
			{
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al cargar los tipos de tareas");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<Tarea> tareas) 
			{
				todasLasTareas = tareas;
				crearTabla();
				popUp.hide();
			}
		});		
	}

	public TipoTarea buscarTipoTarea(ListBox listBox) 
	{
		int idTipoTareaSeleccionado = Integer.valueOf(listBox.getValue(listBox.getSelectedIndex()));
		for (TipoTarea tipoTarea : tiposTareasMemoria)
		{
			if (tipoTarea.getId() == idTipoTareaSeleccionado)
			{
				return tipoTarea;
			}
		}
		return null;
	}
	
	private void crearTabla(){
		TextColumn<Tarea> namecolumn = new TextColumn<Tarea>() {
			@Override
			public String getValue(Tarea e) {
				return e.getDescripcion();
			}
		};
		
		TextColumn<Tarea> serieColumn = new TextColumn<Tarea>() {
			@Override
			public String getValue(Tarea e) {
				return e.getTipoTarea().getDescripcion();
			}
		};

		cellTableTareas.addColumn(namecolumn,"Descripción");
		cellTableTareas.addColumn(serieColumn,"Tipo de tarea");

		namecolumn.setSortable(true);
		serieColumn.setSortable(true);
		
		cellTableTareas.setColumnWidth(namecolumn, 23, Unit.PCT);
		cellTableTareas.setColumnWidth(serieColumn, 14, Unit.PCT);

		List<Tarea> list = cargarTareas();
		ListHandler<Tarea> columnSortHandlerNombre = new ListHandler<Tarea>(list);
		ListHandler<Tarea> columnSortHandlerTipo = new ListHandler<Tarea>(list);
		
		columnSortHandlerNombre.setComparator(namecolumn,new Comparator<Tarea>() {
			public int compare(Tarea o1, Tarea o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getDescripcion().compareTo(o2.getDescripcion()) : 1;
				}
				return -1;
			}
		});

		columnSortHandlerTipo.setComparator(serieColumn,new Comparator<Tarea>() {
			public int compare(Tarea o1, Tarea o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getTipoTarea().getDescripcion().compareTo(o2.getTipoTarea().getDescripcion()) : 1;
				}
				return -1;
			}
		});
		
		cellTableTareas.addColumnSortHandler(columnSortHandlerNombre);
		cellTableTareas.addColumnSortHandler(columnSortHandlerTipo);
		
		tareasFiltradas.addDataDisplay(cellTableTareas);

		filterBox.addValueChangeHandler(new IStringValueChanged() {
			public void valueChanged(String newValue) {
				tareasFiltradas.setFilter(newValue);
				tareasFiltradas.refresh();
			}
		});
	}
	
	public List<Tarea> cargarTareas() {
		tareasFiltradas.getList().clear();
		List<Tarea> list = tareasFiltradas.getList();

		TipoTarea tipoTarea = buscarTipoTarea(listBoxTiposTareas);
		
		for (Tarea t : todasLasTareas) {
			
			if(tipoTarea == null || t.getTipoTarea().getId() == tipoTarea.getId()){
				list.add(t);
			}
		}
		cellTableTareas.redraw();
		return list;
	}
}
