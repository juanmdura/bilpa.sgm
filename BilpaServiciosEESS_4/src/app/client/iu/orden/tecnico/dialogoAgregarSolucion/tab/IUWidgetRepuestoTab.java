package app.client.iu.orden.tecnico.dialogoAgregarSolucion.tab;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import app.client.ProyectoBilpa;
import app.client.dominio.Repuesto;
import app.client.dominio.TipoRepuesto;
import app.client.dominio.data.RepuestoData;
import app.client.dominio.data.RepuestoData;
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

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.CellPreviewEvent.Handler;

public class IUWidgetRepuestoTab extends Composite
{
	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private FlexTable tableDatos = new FlexTable();
	
	private Label lblTitulo = new Label("Repuesto utilizado");
	private Label lblTipo = new Label("Tipo de repuesto");
	private Label lblFiltrar = new Label("Filtrar");
	
	public ListBox listBoxTiposRepuestos = new ListBox();

	public ArrayList<TipoRepuesto> tiposRepuestosMemoria = new ArrayList<TipoRepuesto>();
	
	Label lblCantidad = new Label("Cantidad");
	final CheckBox chNuevo = new CheckBox();
	
	private TextBoxFilter filterBox = new TextBoxFilter();
	public CellTable<RepuestoData> cellTableRepuestos = new CellTable<RepuestoData>(10, CellTableSmallResource.INSTANCE);	
	private ArrayList<RepuestoData> todasLasRepuestos = new ArrayList<RepuestoData>();
	Set<RepuestoData> repuestosSeleccionados = new HashSet<RepuestoData>();
	private PopupCargando popUp;
	private SimplePager.Resources pagerResources;
	private SimplePager pager;
	private IUWidgetSolucion iuWidgetSolucion;
	
	private GlassPopup glass = new GlassPopup();
	
	public VerticalPanel getPrincipalPanel() {
		return vPanelPrincipal;
	}
	
	public Set<RepuestoData> getRepuestosSeleccionados() {
		return repuestosSeleccionados;
	}

	public boolean esNuevo() {
		return chNuevo.getValue();
	}
	
	public IUWidgetRepuestoTab(PopupCargando popUp, final IUWidgetSolucion iuWidgetSolucion) 
	{
		this.popUp = popUp;
		this.iuWidgetSolucion = iuWidgetSolucion;
		listBoxTiposRepuestos.addChangeHandler(new ChangeHandler()
		{
			public void onChange(ChangeEvent arg0) 
			{
				cargarRepuestos();
			}
		});
		
		cellTableRepuestos.addCellPreviewHandler(new Handler<RepuestoData>() {
			@Override
			public void onCellPreview(final CellPreviewEvent<RepuestoData> event) {
				if (BrowserEvents.CLICK.equals(event.getNativeEvent().getType())) {

					final RepuestoData value = event.getValue();
					final Boolean state = !event.getDisplay().getSelectionModel().isSelected(value);
					event.getDisplay().getSelectionModel().setSelected(value, state);
					event.setCanceled(true);
				}
			}
		});

		final MultiSelectionModel<RepuestoData> selectModel = new MultiSelectionModel<RepuestoData>();
		final Handler<RepuestoData> selectionEventManager = DefaultSelectionEventManager.createCheckboxManager();
		cellTableRepuestos.setSelectionModel(selectModel, selectionEventManager);
		
		final SelectionModel<RepuestoData> selectionModel1 = new MultiSelectionModel<RepuestoData>();
		cellTableRepuestos.setSelectionModel(selectionModel1);
		cellTableRepuestos.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @SuppressWarnings("unchecked")
			public void onSelectionChange(SelectionChangeEvent event) {
               repuestosSeleccionados = ((MultiSelectionModel<RepuestoData>)cellTableRepuestos.getSelectionModel()).getSelectedSet();
               int i = 0;
               iuWidgetSolucion.lblRepuestosData.setText("");
               for (RepuestoData repuestoData : repuestosSeleccionados) {
            		if (i > 0){
            			iuWidgetSolucion.lblRepuestosData.setText(iuWidgetSolucion.lblRepuestosData.getText().concat(", "));
        			}
            		iuWidgetSolucion.lblRepuestosData.setText(iuWidgetSolucion.lblRepuestosData.getText().concat(repuestoData.getDescripcion()));
        			i++;
               }
           }
       });
		
		pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(cellTableRepuestos);
		pager.setPageSize(7);
		
		setear();
		agregar();
		cargarLtBoxTipoRepuesto();
	}

	public final FilteredListDataProvider<RepuestoData> repuestosFiltradas = new FilteredListDataProvider<RepuestoData>(new IFilter<RepuestoData>()
	{
		public boolean isValid(RepuestoData value, String filter){
			if(filter==null || value==null){
				return true;
			}
			return value.getDescripcion().toLowerCase().contains(filter.toLowerCase()) || value.getNroSerie().toLowerCase().contains(filter.toLowerCase());
		}
	});
	
	private void setear(){
		vPanelPrincipal.setSize("1220px", "380px");
		cellTableRepuestos.setWidth("1220px");
		cellTableRepuestos.setTitle("Utilize la tecla CTL para seleccionar más de un repuesto");
		
		filterBox.setWidth("220px");
		listBoxTiposRepuestos.setWidth("220px");
		
		lblTitulo.setStyleName("SubTitulo");
		lblTipo.setStyleName("Negrita");
		lblFiltrar.setStyleName("Negrita");
		lblCantidad.setStyleName("Negrita");
		
		chNuevo.setValue(true);
		chNuevo.setText("RepuestoData Nuevo ");
		chNuevo.setStyleName("Negrita");
	}
	
	private void agregar(){
		tableDatos.setWidget(0, 0, lblTipo);
		tableDatos.setWidget(0, 1, listBoxTiposRepuestos);
		
		tableDatos.setWidget(1, 0, lblFiltrar);
		tableDatos.setWidget(1, 1, filterBox);
		
		tableDatos.getFlexCellFormatter().setColSpan(2, 0, 4);
		tableDatos.setWidget(2, 0, cellTableRepuestos);
		
		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal.setSpacing(5);
		vPanelPrincipal.add(lblTitulo);
		vPanelPrincipal.add(tableDatos);
		vPanelPrincipal.add(pager);
		
	}
	
	private void cargarLtBoxTipoRepuesto() {
		listBoxTiposRepuestos.clear();
		popUp.show();

		ProyectoBilpa.greetingService.obtenerTodasLosTiposRepuestosActivos(new AsyncCallback<ArrayList<TipoRepuesto>>() {
			public void onFailure(Throwable caught) 
			{
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener todas los tipo de repuestos");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<TipoRepuesto> result) 
			{
				TipoRepuesto auxiliar;
				tiposRepuestosMemoria.clear();				
				listBoxTiposRepuestos.addItem(Constants.TODOS, -1+"");

				for(TipoRepuesto ttSinClasificar : result)
				{
					if(ttSinClasificar.getDescripcion().equals(Constants.SIN_CLASIFICAR))
					{
						listBoxTiposRepuestos.addItem(ttSinClasificar.toString(),String.valueOf(ttSinClasificar.getId()));
						tiposRepuestosMemoria.add(ttSinClasificar);
					}
				}

				for (int i=0; i<result.size();i++)
				{
					auxiliar = (TipoRepuesto) result.get(i);
					if(!auxiliar.getDescripcion().equals(Constants.SIN_CLASIFICAR))
					{
						listBoxTiposRepuestos.addItem(auxiliar.toString(),String.valueOf(auxiliar.getId()));
						tiposRepuestosMemoria.add(auxiliar);
					}
				}			
				cargarRepuestos1();
				popUp.hide();
			}
		});		
	}

	public void cargarRepuestos1()
	{
		popUp.show();
		todasLasRepuestos.clear();

		ProyectoBilpa.greetingService.obtenerTodosLosRepuestos(new AsyncCallback<ArrayList<Repuesto>>() {
			public void onFailure(Throwable caught) 
			{
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al cargar los tipos de repuestos");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<Repuesto> repuestos) 
			{
				for(Repuesto r : repuestos){
					RepuestoData rd = r.copiarRepuestoData();
					todasLasRepuestos.add(rd);
				}
				crearTabla();
				popUp.hide();
			}
		});		
	}

	public TipoRepuesto buscarTipoRepuesto(ListBox listBox) 
	{
		int idTipoRepuestoSeleccionado = Integer.valueOf(listBox.getValue(listBox.getSelectedIndex()));
		for (TipoRepuesto tipoRepuesto : tiposRepuestosMemoria)
		{
			if (tipoRepuesto.getId() == idTipoRepuestoSeleccionado)
			{
				return tipoRepuesto;
			}
		}
		return null;
	}
	
	private void crearTabla(){
		TextColumn<RepuestoData> namecolumn = new TextColumn<RepuestoData>() {
			@Override
			public String getValue(RepuestoData e) {
				return e.getDescripcion();
			}
		};
		
		TextColumn<RepuestoData> serieColumn = new TextColumn<RepuestoData>() {
			@Override
			public String getValue(RepuestoData e) {
				return e.getNroSerie();
			}
		};
		
		List<String> cantidades = new ArrayList<String>();
		for(int i=1; i<1201;i++){
			cantidades.add(i+"");
		}
		
		SelectionCell selectionCellCantidad = new SelectionCell(cantidades);
		Column<RepuestoData, String> cantidadColumn = new Column<RepuestoData, String>(selectionCellCantidad){
			@Override
			public String getValue(RepuestoData e) {
				return e.getCantidad()+"";
			}
		};
		
		cantidadColumn.setFieldUpdater(new FieldUpdater<RepuestoData, String>() {
		    public void update(int index, RepuestoData object, String value) {
		    	object.setCantidad(Integer.valueOf(value));
		    }
		});
		
		Column<RepuestoData, Boolean> nuevoColumn = new Column<RepuestoData, Boolean>(new CheckboxCell(true,false))
		{
		    @Override
		    public Boolean getValue(RepuestoData object)
		    {
		        return object.isEsNuevo();
		    }
		};
		
		nuevoColumn.setFieldUpdater(new FieldUpdater<RepuestoData, Boolean>() {
		    public void update(int index, RepuestoData object, Boolean value) {
		    	object.setEsNuevo(value);
		    }
		});

		cellTableRepuestos.addColumn(namecolumn,"Descripción");
		cellTableRepuestos.addColumn(serieColumn,"Nro Serie");
		cellTableRepuestos.addColumn(cantidadColumn,"Cantidad");
		cellTableRepuestos.addColumn(nuevoColumn,"Nuevo");
		
		namecolumn.setSortable(true);
		serieColumn.setSortable(true);
		cantidadColumn.setSortable(true);
		nuevoColumn.setSortable(true);
		
		cellTableRepuestos.setColumnWidth(namecolumn, 83, Unit.PCT);
		cellTableRepuestos.setColumnWidth(serieColumn, 45, Unit.PCT);
		
		List<RepuestoData> list = cargarRepuestos();
		ListHandler<RepuestoData> columnSortHandlerNombre = new ListHandler<RepuestoData>(list);
		ListHandler<RepuestoData> columnSortHandlerSerie = new ListHandler<RepuestoData>(list);
		ListHandler<RepuestoData> columnSortHandlerCantidad = new ListHandler<RepuestoData>(list);
		ListHandler<RepuestoData> columnSortHandlerNuevo = new ListHandler<RepuestoData>(list);
		
		columnSortHandlerNombre.setComparator(namecolumn,new Comparator<RepuestoData>() {
			public int compare(RepuestoData o1, RepuestoData o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getDescripcion().compareTo(o2.getDescripcion()) : 1;
				}
				return -1;
			}
		});

		columnSortHandlerSerie.setComparator(serieColumn,new Comparator<RepuestoData>() {
			public int compare(RepuestoData o1, RepuestoData o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getNroSerie().compareTo(o2.getNroSerie()) : 1;
				}
				return -1;
			}
		});
		
		columnSortHandlerCantidad.setComparator(cantidadColumn,new Comparator<RepuestoData>() {
			public int compare(RepuestoData o1, RepuestoData o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getCantidad()+"".compareTo(o2.getCantidad()+"") : 1;
				}
				return -1;
			}
		});
		
		columnSortHandlerNuevo.setComparator(nuevoColumn, new Comparator<RepuestoData>() {
			public int compare(RepuestoData o1, RepuestoData o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? String.valueOf(o1.isEsNuevo()).compareTo(String.valueOf(o2.isEsNuevo())) : 1;
				}
				return -1;
			}
		});
		
		cellTableRepuestos.addColumnSortHandler(columnSortHandlerNombre);
		cellTableRepuestos.addColumnSortHandler(columnSortHandlerSerie);
		cellTableRepuestos.addColumnSortHandler(columnSortHandlerCantidad);
		cellTableRepuestos.addColumnSortHandler(columnSortHandlerNuevo);
		
		repuestosFiltradas.addDataDisplay(cellTableRepuestos);

		filterBox.addValueChangeHandler(new IStringValueChanged() {
			public void valueChanged(String newValue) {
				repuestosFiltradas.setFilter(newValue);
				repuestosFiltradas.refresh();
			}
		});
	}
	
	public List<RepuestoData> cargarRepuestos() {
		repuestosFiltradas.getList().clear();
		List<RepuestoData> list = repuestosFiltradas.getList();

		TipoRepuesto tipoRepuesto = buscarTipoRepuesto(listBoxTiposRepuestos);
		
		for (RepuestoData t : todasLasRepuestos) {
			
			if(tipoRepuesto == null || t.getTipoRepuesto().getId() == tipoRepuesto.getId()){
				list.add(t);
			}
		}
		cellTableRepuestos.redraw();
		return list;
	}
}
