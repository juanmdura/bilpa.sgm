package app.client.iu.listados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import app.client.ProyectoBilpa;
import app.client.dominio.Persona;
import app.client.dominio.Sello;
import app.client.dominio.data.OrdenData;
import app.client.iu.widgets.ValidadorPopup;
import app.client.resources.CellTableResource;
import app.client.utilidades.utilObjects.FileDownload;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

public class IUCierrePetroleras extends Composite{
	
	private Label lblTituloPrincipal = new Label("Cierre de petroleras");
	//private Label lblSubTituloPrincipal = new Label("Seleccione una rango de fechas para generar la planilla con los datos de cierre");
	
	private Persona sesion;
	
	FileDownload file = new FileDownload();
	
	
	private Label lblPetrolera = new Label("");
	private VerticalPanel vPanelPrincipal = new VerticalPanel();

	private HorizontalPanel hPanelPrincipal = new HorizontalPanel();
	private HorizontalPanel hPanelContenedor = new HorizontalPanel();
	private VerticalPanel vPanel1 =  new VerticalPanel();
	
	private VerticalPanel vPanelTabla = new VerticalPanel();
	
	private ListBox listBoxPetroleras = new ListBox();	
	private HorizontalPanel hPanelDatos = new HorizontalPanel();

	private DateBox dbDesde = new DateBox();
	private DateBox dbHasta = new DateBox();

	private FlexTable tableDatos = new FlexTable();
	private Grid grilla = new Grid(1,7);
	private DecoratorPanel decorator = new DecoratorPanel();

	private PopupCargando popUp = new PopupCargando("Cargando...");
	
	private Image imgExcel = new Image("img/crear_excel.png");
	private PushButton btnExcel = new PushButton(imgExcel);
	
	private CellTable<OrdenData> cellTableOrdenes = new CellTable<OrdenData>();	
	private ArrayList<OrdenData> ordenes = new ArrayList<OrdenData>();
	private SimplePager.Resources pagerResources;
	private SimplePager pager;
	ListDataProvider<OrdenData> dataProvider = new ListDataProvider<OrdenData>();
	
	private GlassPopup glass = new GlassPopup();
	
	private static final ProvidesKey<OrdenData> KEY_PROVIDER = new ProvidesKey<OrdenData>() {
		public Object getKey(OrdenData item) {
			return item.getNumero();
		}
	};
	
	Button btnGenerarExcel = new Button("Ver",new ClickHandler() {
		public void onClick(ClickEvent event) {
			generarExcel();
		}
	});

	public VerticalPanel getPrincipalPanel() {
		return vPanelPrincipal;
	}
	
	public List<OrdenData> cargarData() {
		dataProvider.getList().clear();
		List<OrdenData> list = dataProvider.getList();

		for (OrdenData orden : ordenes) {
			list.add(orden);
		}
		cellTableOrdenes.redraw();
		return list;
	}

	public IUCierrePetroleras(Persona persona)	{
		pagerResources = GWT.create(SimplePager.Resources.class);
		/*pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(cellTableOrdenes);
		pager.setPageSize(25);*/
		
		setearWidgets();			
		cargarPanelesConWidgets();

		obtenerHoraServidor();
		cargarPetroleras();
		setDateFormat();

		cargarGrilla();
		color();

/*		listBoxPetroleras.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent arg0) {
				vPanelOrdenes.clear();
				idEstacion = Integer.valueOf(listBoxPetroleras.getValue(listBoxPetroleras.getSelectedIndex()));
				lblEstacion.setText("");
			}
		});

		listBoxPetroleras.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (KeyCodes.KEY_ENTER == event.getNativeEvent().getKeyCode()) {
					cargarOrdenes();
				}
			}
		});
		
*/
		
		btnExcel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				generarExcel();
			}
		});
	}

	private void cargarPetroleras() {
		ProyectoBilpa.greetingService.obtenerSellos(new AsyncCallback<ArrayList<Sello>>() {

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener sellos" + caught.getMessage());
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<Sello> result) {
				for (Sello sello : result){
					listBoxPetroleras.addItem(sello.getNombre(), sello.getId()+"");
				}
			}
		});	

	}
	
	private void obtenerHoraServidor() {
		ProyectoBilpa.greetingService.obtenerHoraServidorDate(new AsyncCallback<Date>() {
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar la hora del servidor");
				vpu.showPopUp();
			}

			public void onSuccess(Date result) {
				dbDesde.setValue(result);
				dbHasta.setValue(result);
			}
		});	
	}

	private void generarExcel() {
		popUp.show();
		if(validarFechas()){
			int idPetrolera = Integer.valueOf(listBoxPetroleras.getValue(listBoxPetroleras.getSelectedIndex()));
			Sello sello = crearPetrolera(idPetrolera);
			
			Date inicio = dbDesde.getValue();
			Date fin = dbHasta.getValue();
			
			getExcelFromServer(sello, inicio, fin);
			
		}else{
			popUp.hide();
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "La fecha de fin debe ser mayor a la fecha de inicio");
			vpu.showPopUp();
		}
	}

	private Sello crearPetrolera(int idPetrolera) {
		Sello sello = new Sello();
		sello.setId(idPetrolera);
		
		if(idPetrolera == 3){
			sello.setNombre("Petrobras");
		}
		if(idPetrolera == 2){
			sello.setNombre("Ancap");
		}
		return sello;
	}

	private void getExcelFromServer(Sello selloSeleccionado, Date inicio, Date fin) {
		ProyectoBilpa.greetingService.crearExcelOrdenesSello(selloSeleccionado, inicio, fin, new AsyncCallback<String>() {

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al crear el EXCEL");
				vpu.showPopUp();
			}

			public void onSuccess(String result) {
				if (result.equals("")) {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al crear el EXCEL");
					vpu.showPopUp();							
				}else {
					file.download();
					obtenerOrdenesActivas();
					popUp.hide();
				}
			}
		});
	}
	
	private boolean validarFechas() {
		Date inicio = dbDesde.getValue();
		Date fin = dbHasta.getValue();
		
		if(inicio.after(fin)){
			return false;
		}
		
		int compareTo = inicio.compareTo(fin);
		if(compareTo == 0){
			return false;
		}
		
		return true;
	}

	private void color() {
		lblPetrolera.setStyleName("SubTitulo");
		btnExcel.setTitle("Genera una planilla con los datos correspondientes a la petrolera y rango de fechas seleccionado");
	}

	private void cargarGrilla()	{
		grilla.setSize("500px", "90px");

		grilla.setCellSpacing(5);
		grilla.setWidget(0, 0, label("Petrolera"));
		grilla.setWidget(0, 1, listBoxPetroleras);

		grilla.setWidget(0, 2, label("Desde"));
		grilla.setWidget(0, 3, dbDesde);

		grilla.setWidget(0, 4, label("Hasta"));
		grilla.setWidget(0, 5, dbHasta);

		grilla.setWidget(0, 6, btnExcel);

		listBoxPetroleras.setWidth("270px");
		decorator.add(grilla);
	}

	private Label label(String text) {
		Label label = new InlineLabel(text);
		label.setStyleName("Negrita");
		label.setWordWrap(false);
		return label;
	}

	@SuppressWarnings("deprecation")
	private void setearWidgets() {
		this.vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal.setSpacing(5);
		imgExcel.setSize("40px", "40px");
		btnExcel.setSize("40px", "40px");
		this.vPanelPrincipal.setWidth("800px");
		this.btnGenerarExcel.setWidth("100px");

		listBoxPetroleras.setVisibleItemCount(1);
		listBoxPetroleras.setTitle("Lista de Petroleras");
		
		lblTituloPrincipal.setStyleName("Titulo");
		//lblSubTituloPrincipal.setStyleName("SubTitulo");
		
		vPanelPrincipal.add(lblTituloPrincipal);
		//vPanelPrincipal.add(lblSubTituloPrincipal);
		
		hPanelDatos.setSpacing(5);

		tableDatos.setCellSpacing(5);
		tableDatos.setCellPadding(2);
		tableDatos.setBorderWidth(1);
	}

	private void cargarPanelesConWidgets() {
		hPanelDatos.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		vPanel1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);	
		vPanel1.add(hPanelDatos);	
		hPanelDatos.add(this.tableDatos);
		vPanel1.setSpacing(20);
		hPanelContenedor.add(vPanel1);
		
		hPanelPrincipal.setSpacing(10);
		vPanelPrincipal.setSpacing(10);
	

		hPanelPrincipal.add(decorator);
		vPanelPrincipal.add(hPanelPrincipal);
		vPanelPrincipal.add(lblPetrolera);
		
		vPanelPrincipal.add(file);
		vPanelPrincipal.add(vPanelTabla);
		//vPanelPrincipal.add(pager);
	}

	private void setDateFormat() {
		dbDesde.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getLongDateFormat()));
		dbHasta.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getLongDateFormat()));
	}


	private void obtenerOrdenesActivas() {
		popUp.show();
		
		int idPetrolera = Integer.valueOf(listBoxPetroleras.getValue(listBoxPetroleras.getSelectedIndex()));
		Sello sello = crearPetrolera(idPetrolera);
		
		Date inicio = dbDesde.getValue();
		Date fin = dbHasta.getValue();
		
		ProyectoBilpa.greetingService.obtenerTodasLasOrdenesActivasDataExcel(sello, inicio, fin, new AsyncCallback<ArrayList<OrdenData>>() {
			public void onFailure(Throwable caught) {
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener todas las ordenes activas");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<OrdenData> result) {
				vPanelTabla.clear();
				if(result.size() > 0 ){
					ordenes.clear();

					for (int i = 0; i < result.size(); i++) {
						ordenes.add(result.get(i));
					}
					crearTabla();
					cargarData();
					popUp.hide();
				}
			}
		});
	}


	protected void crearTabla() {
		
		cellTableOrdenes = new CellTable<OrdenData>(ordenes.size(), CellTableResource.INSTANCE, KEY_PROVIDER);
		
		cellTableOrdenes.redraw();
		cellTableOrdenes.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		cellTableOrdenes.setWidth("1200px", true);
		cellTableOrdenes.setRowCount(ordenes.size(), true);
		cellTableOrdenes.setVisibleRange(0, ordenes.size());
		
		vPanelTabla.add(cellTableOrdenes);
		
		TextColumn<OrdenData> numeroColumn = new TextColumn<OrdenData>() {
			@Override
			public String getValue(OrdenData o) {
				return String.valueOf(o.getNumero());
			}
		};
		TextColumn<OrdenData> estadoColumn = new TextColumn<OrdenData>() {
			@Override
			public String getValue(OrdenData o) {
				return o.getEstado();
			}
		};
		TextColumn<OrdenData> tecnicoColumn = new TextColumn<OrdenData>() {
			@Override
			public String getValue(OrdenData o) {
				return o.getTecnico();
			}
		};
		TextColumn<OrdenData> prioridadColumn = new TextColumn<OrdenData>() {
			@Override
			public String getValue(OrdenData o) {
				return o.getPrioridad();
			}
		};
		TextColumn<OrdenData> fechaColumn = new TextColumn<OrdenData>() {
			@Override
			public String getValue(OrdenData o) {
				return o.getFechaInicio();
			}
		};
		TextColumn<OrdenData> estacionColumn = new TextColumn<OrdenData>() {
			@Override
			public String getValue(OrdenData o) {
				return o.getEstacion();
			}
		};
		TextColumn<OrdenData> bocaColumn = new TextColumn<OrdenData>() {
			@Override
			public String getValue(OrdenData o) {
				return o.getNumeroBoca();
			}
		};
		TextColumn<OrdenData> localidadColumn = new TextColumn<OrdenData>() {
			@Override
			public String getValue(OrdenData o) {
				return o.getLocalidad();
			}
		};
		TextColumn<OrdenData> selloColumn = new TextColumn<OrdenData>() {
			@Override
			public String getValue(OrdenData o) {
				return o.getSello();
			}
		};
		
		cellTableOrdenes.addColumn(numeroColumn,"Número");
		cellTableOrdenes.addColumn(estadoColumn,"Estado");
		cellTableOrdenes.addColumn(tecnicoColumn,"Técnico");
		cellTableOrdenes.addColumn(prioridadColumn,"Prioridad");
		cellTableOrdenes.addColumn(fechaColumn,"Fecha Inicio");
		cellTableOrdenes.addColumn(estacionColumn,"Estación");
		cellTableOrdenes.addColumn(bocaColumn,"Nro Boca");
		cellTableOrdenes.addColumn(localidadColumn,"Localidad");
		cellTableOrdenes.addColumn(selloColumn,"Sello");
		
		numeroColumn.setSortable(true);
		estadoColumn.setSortable(true);
		tecnicoColumn.setSortable(true);
		prioridadColumn.setSortable(true);
		fechaColumn.setSortable(true);
		estacionColumn.setSortable(true);
		bocaColumn.setSortable(true);
		localidadColumn.setSortable(true);
		selloColumn.setSortable(true);
		
		cellTableOrdenes.setColumnWidth(numeroColumn, 10, Unit.PCT);
		cellTableOrdenes.setColumnWidth(tecnicoColumn, 25, Unit.PCT);
		cellTableOrdenes.setColumnWidth(prioridadColumn, 10, Unit.PCT);
		cellTableOrdenes.setColumnWidth(estadoColumn, 25, Unit.PCT);
		cellTableOrdenes.setColumnWidth(fechaColumn, 20, Unit.PCT);
		cellTableOrdenes.setColumnWidth(estacionColumn, 30, Unit.PCT);
		cellTableOrdenes.setColumnWidth(bocaColumn, 12, Unit.PCT);
		cellTableOrdenes.setColumnWidth(localidadColumn, 25, Unit.PCT);
		cellTableOrdenes.setColumnWidth(selloColumn, 15, Unit.PCT);
		
		List<OrdenData> list = cargarData();
		ListHandler<OrdenData> columnSortHandlerNumero = new ListHandler<OrdenData>(list);
		ListHandler<OrdenData> columnSortHandlerTecnico = new ListHandler<OrdenData>(list);
		ListHandler<OrdenData> columnSortHandlerPrioridad = new ListHandler<OrdenData>(list);
		ListHandler<OrdenData> columnSortHandlerEstado = new ListHandler<OrdenData>(list);
		ListHandler<OrdenData> columnSortHandlerFecha = new ListHandler<OrdenData>(list);
		ListHandler<OrdenData> columnSortHandlerEstacion = new ListHandler<OrdenData>(list);
		ListHandler<OrdenData> columnSortHandlerBoca = new ListHandler<OrdenData>(list);
		ListHandler<OrdenData> columnSortHandlerLoca = new ListHandler<OrdenData>(list);
		ListHandler<OrdenData> columnSortHandlerSello = new ListHandler<OrdenData>(list);
		
		columnSortHandlerNumero.setComparator(numeroColumn,new Comparator<OrdenData>() {
			public int compare(OrdenData o1, OrdenData o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? String.valueOf(o1.getNumero()).compareTo(String.valueOf(o2.getNumero())) : 1;
				}
				return -1;
			}
		});
		
		columnSortHandlerTecnico.setComparator(tecnicoColumn,new Comparator<OrdenData>() {
			public int compare(OrdenData o1, OrdenData o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getTecnico().compareTo(o2.getTecnico()) : 1;
				}
				return -1;
			}
		});
		
		columnSortHandlerPrioridad.setComparator(prioridadColumn,new Comparator<OrdenData>() {
			public int compare(OrdenData o1, OrdenData o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getPrioridad().compareTo(o2.getPrioridad()) : 1;
				}
				return -1;
			}
		});
		
		columnSortHandlerEstado.setComparator(estadoColumn,new Comparator<OrdenData>() {
			public int compare(OrdenData o1, OrdenData o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getEstado().compareTo(o2.getEstado()) : 1;
				}
				return -1;
			}
		});
		
		columnSortHandlerFecha.setComparator(fechaColumn,new Comparator<OrdenData>() {
			public int compare(OrdenData o1, OrdenData o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? String.valueOf(o1.getFechaInicio()).compareTo(o2.getFechaInicio()+"") : 1;
				}
				return -1;
			}
		});
		
		columnSortHandlerEstacion.setComparator(estacionColumn,new Comparator<OrdenData>() {
			public int compare(OrdenData o1, OrdenData o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getEstacion().compareTo(o2.getEstacion()) : 1;
				}
				return -1;
			}
		});
		
		columnSortHandlerBoca.setComparator(bocaColumn,new Comparator<OrdenData>() {
			public int compare(OrdenData o1, OrdenData o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getNumeroBoca().compareTo(o2.getNumeroBoca()+"") : 1;
				}
				return -1;
			}
		});
		
		columnSortHandlerLoca.setComparator(localidadColumn,new Comparator<OrdenData>() {
			public int compare(OrdenData o1, OrdenData o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getLocalidad().compareTo(o2.getLocalidad()) : 1;
				}
				return -1;
			}
		});
		
		columnSortHandlerSello.setComparator(selloColumn,new Comparator<OrdenData>() {
			public int compare(OrdenData o1, OrdenData o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getSello().compareTo(o2.getSello()) : 1;
				}
				return -1;
			}
		});
		
		cellTableOrdenes.addColumnSortHandler(columnSortHandlerNumero);
		cellTableOrdenes.addColumnSortHandler(columnSortHandlerTecnico);
		cellTableOrdenes.addColumnSortHandler(columnSortHandlerPrioridad);
		cellTableOrdenes.addColumnSortHandler(columnSortHandlerEstado);
		cellTableOrdenes.addColumnSortHandler(columnSortHandlerFecha);
		cellTableOrdenes.addColumnSortHandler(columnSortHandlerEstacion);
		cellTableOrdenes.addColumnSortHandler(columnSortHandlerBoca);
		cellTableOrdenes.addColumnSortHandler(columnSortHandlerSello);
		cellTableOrdenes.addColumnSortHandler(columnSortHandlerLoca);
		
		dataProvider.addDataDisplay(cellTableOrdenes);	
	}
}
