package app.client.iu.orden.tecnico.dialogoAgregarSolucion.tab;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import app.client.ProyectoBilpa;
import app.client.dominio.FallaTecnica;
import app.client.dominio.TipoFallaTecnica;
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

public class IUWidgetFallaTab extends Composite
{
	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private FlexTable tableDatos = new FlexTable();
	
	private Label lblTitulo = new Label("Falla detectada");
	private Label lblTipo = new Label("Tipo de falla");
	private Label lblFiltrar = new Label("Filtrar");
	
	public ListBox listBoxTiposFallas = new ListBox();

	public ArrayList<TipoFallaTecnica> tiposFallasMemoria = new ArrayList<TipoFallaTecnica>();
	
	private TextBoxFilter filterBox = new TextBoxFilter();
	public CellTable<FallaTecnica> cellTableFallas = new CellTable<FallaTecnica>(10, CellTableSmallResource.INSTANCE);	
	private ArrayList<FallaTecnica> todasLasFallas = new ArrayList<FallaTecnica>();
	private FallaTecnica fallaSeleccionada;
	private PopupCargando popUp;
	private SimplePager.Resources pagerResources;
	private SimplePager pager;
	IUWidgetSolucion iuWidgetSolucion;
	
	private GlassPopup glass = new GlassPopup();
	
	public VerticalPanel getPrincipalPanel() {
		return vPanelPrincipal;
	}
	
	public FallaTecnica getFallaSeleccionada() {
		return fallaSeleccionada;
	}
	
	public IUWidgetFallaTab(PopupCargando popUp, final IUWidgetSolucion iuWidgetSolucion) 
	{
		this.popUp = popUp;
		this.iuWidgetSolucion = iuWidgetSolucion;
		
		listBoxTiposFallas.addChangeHandler(new ChangeHandler()
		{
			public void onChange(ChangeEvent arg0) 
			{
				cargarFallas();
			}
		});
		
		final SelectionModel<FallaTecnica> selectionModel1 = new SingleSelectionModel<FallaTecnica>();
		cellTableFallas.setSelectionModel(selectionModel1);
		selectionModel1.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				fallaSeleccionada = ((SingleSelectionModel<FallaTecnica>) selectionModel1).getSelectedObject();
				iuWidgetSolucion.lblFallaData.setText(fallaSeleccionada.getDescripcion());
			}
		});
		
		pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(cellTableFallas);
		pager.setPageSize(9);
		
		setear();
		agregar();
		cargarLtBoxTipoFalla();
	}

	public final FilteredListDataProvider<FallaTecnica> fallasFiltradas = new FilteredListDataProvider<FallaTecnica>(new IFilter<FallaTecnica>()
	{
		public boolean isValid(FallaTecnica value, String filter){
			if(filter==null || value==null){
				return true;
			}
			return value.getDescripcion().toLowerCase().contains(filter.toLowerCase()) || buscarTipoFalla(value.getSubTipo()).getDescripcion().contains(filter.toLowerCase());
		}
	});
	
	private void setear(){
		vPanelPrincipal.setSize("1220px", "380px");
		cellTableFallas.setWidth("1220px");
		filterBox.setWidth("220px");
		listBoxTiposFallas.setWidth("220px");
		
		lblTitulo.setStyleName("SubTitulo");
		lblTipo.setStyleName("Negrita");
		lblFiltrar.setStyleName("Negrita");
	}
	
	private void agregar(){
		tableDatos.setWidget(0, 0, lblTipo);
		tableDatos.setWidget(0, 1, listBoxTiposFallas);
		
		tableDatos.setWidget(1, 0, lblFiltrar);
		tableDatos.setWidget(1, 1, filterBox);
		
		tableDatos.getFlexCellFormatter().setColSpan(2, 0, 2);
		tableDatos.setWidget(2, 0, cellTableFallas);
		
		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal.setSpacing(5);
		vPanelPrincipal.add(lblTitulo);
		vPanelPrincipal.add(tableDatos);
		vPanelPrincipal.add(pager);
		
	}
	
	public void cargarLtBoxTipoFalla() {
		listBoxTiposFallas.clear();
		popUp.show();

		ProyectoBilpa.greetingService.obtenerTiposFallasT(new AsyncCallback<ArrayList<TipoFallaTecnica>>() {
			public void onFailure(Throwable caught) 
			{
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener todas los tipo de fallas");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<TipoFallaTecnica> result) 
			{
				TipoFallaTecnica auxiliar;
				tiposFallasMemoria.clear();				
				listBoxTiposFallas.addItem(Constants.TODOS, -1+"");

				for(TipoFallaTecnica ttSinClasificar : result)
				{
					if(ttSinClasificar.getDescripcion().equals(Constants.SIN_CLASIFICAR))
					{
						listBoxTiposFallas.addItem(ttSinClasificar.toString(),String.valueOf(ttSinClasificar.getId()));
						tiposFallasMemoria.add(ttSinClasificar);
					}
				}

				for (int i=0; i<result.size();i++)
				{
					auxiliar = (TipoFallaTecnica) result.get(i);
					if(!auxiliar.getDescripcion().equals(Constants.SIN_CLASIFICAR))
					{
						listBoxTiposFallas.addItem(auxiliar.toString(),String.valueOf(auxiliar.getId()));
						tiposFallasMemoria.add(auxiliar);
					}
				}			
				cargarFallas1();
				popUp.hide();
			}
		});		
	}

	public void cargarFallas1()
	{
		popUp.show();
		todasLasFallas.clear();

		ProyectoBilpa.greetingService.obtenerTodosLasFallasT(new AsyncCallback<ArrayList<FallaTecnica>>() {
			public void onFailure(Throwable caught) 
			{
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al cargar los tipos de fallas");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<FallaTecnica> fallas) 
			{
				todasLasFallas = fallas;
				crearTabla();
				popUp.hide();
			}
		});		
	}

	private TipoFallaTecnica buscarTipoFalla(ListBox listBox) 
	{
		int idTipoFallaSeleccionado = Integer.valueOf(listBox.getValue(listBox.getSelectedIndex()));
		for (TipoFallaTecnica tipoFalla : tiposFallasMemoria)
		{
			if (tipoFalla.getId() == idTipoFallaSeleccionado)
			{
				return tipoFalla;
			}
		}
		return null;
	}
	
	private TipoFallaTecnica buscarTipoFalla(int id) 
	{
		for (TipoFallaTecnica tipoFalla : tiposFallasMemoria)
		{
			if (tipoFalla.getId() == id)
			{
				return tipoFalla;
			}
		}
		return null;
	}
	
	private void crearTabla(){
		TextColumn<FallaTecnica> namecolumn = new TextColumn<FallaTecnica>() {
			@Override
			public String getValue(FallaTecnica e) {
				return e.getDescripcion();
			}
		};
		
		TextColumn<FallaTecnica> tipoColumn = new TextColumn<FallaTecnica>() {
			@Override
			public String getValue(FallaTecnica e) {
				return buscarTipoFalla(e.getSubTipo()).getDescripcion();
			}
		};

		cellTableFallas.addColumn(namecolumn,"Descripción");
		cellTableFallas.addColumn(tipoColumn,"Tipo de falla");

		namecolumn.setSortable(true);
		tipoColumn.setSortable(true);
		
		cellTableFallas.setColumnWidth(namecolumn, 23, Unit.PCT);
		cellTableFallas.setColumnWidth(tipoColumn, 14, Unit.PCT);

		List<FallaTecnica> list = cargarFallas();
		ListHandler<FallaTecnica> columnSortHandlerNombre = new ListHandler<FallaTecnica>(list);
		ListHandler<FallaTecnica> columnSortHandlerTipo = new ListHandler<FallaTecnica>(list);
		
		columnSortHandlerNombre.setComparator(namecolumn,new Comparator<FallaTecnica>() {
			public int compare(FallaTecnica o1, FallaTecnica o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getDescripcion().compareTo(o2.getDescripcion()) : 1;
				}
				return -1;
			}
		});

		columnSortHandlerTipo.setComparator(tipoColumn,new Comparator<FallaTecnica>() {
			public int compare(FallaTecnica o1, FallaTecnica o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? buscarTipoFalla(o1.getSubTipo()).getDescripcion().compareTo(buscarTipoFalla(o2.getSubTipo()).getDescripcion()) : 1;
				}
				return -1;
			}
		});
		
		cellTableFallas.addColumnSortHandler(columnSortHandlerNombre);
		cellTableFallas.addColumnSortHandler(columnSortHandlerTipo);
		
		fallasFiltradas.addDataDisplay(cellTableFallas);

		filterBox.addValueChangeHandler(new IStringValueChanged() {
			public void valueChanged(String newValue) {
				fallasFiltradas.setFilter(newValue);
				fallasFiltradas.refresh();
			}
		});
	}
	
	public List<FallaTecnica> cargarFallas() {
		fallasFiltradas.getList().clear();
		List<FallaTecnica> list = fallasFiltradas.getList();

		TipoFallaTecnica tipoFalla = buscarTipoFalla(listBoxTiposFallas);
		
		for (FallaTecnica t : todasLasFallas) {
			
			if(tipoFalla == null || t.getSubTipo() == tipoFalla.getId()){
				list.add(t);
			}
		}
		cellTableFallas.redraw();
		return list;
	}
}
