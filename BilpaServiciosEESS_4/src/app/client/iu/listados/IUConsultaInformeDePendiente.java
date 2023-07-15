package app.client.iu.listados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import app.client.ProyectoBilpa;
import app.client.dominio.Organizacion;
import app.client.dominio.data.PendienteData;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.utilObjects.FileDownload;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;

import com.google.gwt.cell.client.DatePickerCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.ListDataProvider;

public class IUConsultaInformeDePendiente extends Composite {

	private Label lblTituloPrincipal = new Label("Informe de pendientes");

	private FileDownload file = new FileDownload();

	private PopupCargando popUp = new PopupCargando("Cargando...");

	private VerticalPanel vPanelPrincipal = new VerticalPanel();

	private HorizontalPanel hPanelPrincipal = new HorizontalPanel();
	private HorizontalPanel hPanelContenedor = new HorizontalPanel();
	private VerticalPanel vPanel1 = new VerticalPanel();

	private VerticalPanel vPanelTabla = new VerticalPanel();

	private Image imgExcel = new Image("img/crear_excel.png");
	private PushButton btnExcel = new PushButton(imgExcel);
	private Button bAceptar = new Button("Aceptar");
	
	private Grid grilla = new Grid(1,7);

	private HorizontalPanel hPanelDatos = new HorizontalPanel();

	private DateBox dbDesde = new DateBox();
	private DateBox dbHasta = new DateBox();
	
	private CheckBox operadorCheck = new CheckBox("Operador");
	private CheckBox ducsaCheck = new CheckBox("Petrolera");
	private CheckBox bilpaCheck = new CheckBox("Bilpa");
	
	private SimplePager.Resources pagerResources;
	private SimplePager pager;
	
	private HorizontalPanel checkPanel = new HorizontalPanel();

	private List<PendienteData> pendientes = new ArrayList<PendienteData>();

	private ListDataProvider<PendienteData> dataProvider = new ListDataProvider<PendienteData>();

	private DecoratorPanel decorator = new DecoratorPanel();
	private GlassPopup glass = new GlassPopup();
	private List<Organizacion> organizaciones = new ArrayList<Organizacion>();
	
	private CellTable<PendienteData> cellTablePendientes = new CellTable<PendienteData>();
	
	private void initPager() {
		pagerResources = GWT.create(SimplePager.Resources.class);		
		pager = new SimplePager(TextLocation.CENTER, pagerResources, true, 100, true);
		pager.setDisplay(cellTablePendientes);
	}

	public IUConsultaInformeDePendiente() {
		btnExcel.setTitle("Genera una planilla con los pendientes que se encuentran en el rango de fecha seleccionado");
		setearEventos();
		setearWidgets();
		initPager();
		cargarPanelesConWidgets();
		setDateFormat();
		cargarGrilla();
		obtenerHoraServidor();
	}

	private void setearEventos() {
		btnExcel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				generarExcel();
			}
		});
		
		bAceptar.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				mostrarTabla();
			}
		});

	}
	
	private void cargarGrilla()	{
		grilla.setSize("500px", "90px");
		grilla.setCellSpacing(15);	
		grilla.setWidget(0, 0, label("Desde"));
		grilla.setWidget(0, 1, dbDesde);
		grilla.setWidget(0, 2, label("Hasta"));
		grilla.setWidget(0, 3, dbHasta);
		grilla.setWidget(0, 4, checkPanel);
		grilla.setWidget(0, 5, bAceptar);
		grilla.setWidget(0, 6, btnExcel);
		decorator.add(grilla);
	}
	
	private Label label(String text) {
		Label label = new InlineLabel(text);
		label.setStyleName("Negrita");
		label.setWordWrap(false);
		return label;
	}
	
	private void setDateFormat() {
		dbDesde.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getLongDateFormat()));
		dbHasta.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getLongDateFormat()));
	}
	
	private void mostrarTabla(){
		if (validarFechas()) {
			Date inicio = dbDesde.getValue();
			Date fin = dbHasta.getValue();	
			cargarOrganizaciones();
			obtenerInformePendientes(inicio, fin);

		} else {
			popUp.hide();
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info",
					"La fecha de fin debe ser mayor a la fecha de inicio");
			vpu.showPopUp();
		}
	}
	
	private void cargarOrganizaciones(){
		organizaciones.clear();
		
		if(bilpaCheck.getValue()){
			organizaciones.add(Organizacion.Bilpa);
		}
		
		if(ducsaCheck.getValue()){
			organizaciones.add(Organizacion.Petrolera);
		}
		
		if(operadorCheck.getValue()){
			organizaciones.add(Organizacion.Operador);
		}
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

	private boolean validarFechas() {
		Date inicio = dbDesde.getValue();
		Date fin = dbHasta.getValue();

		if (inicio.after(fin)) {
			return false;
		}

		int compareTo = inicio.compareTo(fin);
		if (compareTo == 0) {
			return false;
		}

		return true;
	}

	private void generarExcel() {
		popUp.show();
		if (validarFechas()) {
			Date inicio = dbDesde.getValue();
			Date fin = dbHasta.getValue();	
			cargarOrganizaciones();
			getExcelFromServer(inicio, fin, organizaciones);

		} else {
			popUp.hide();
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info",
					"La fecha de fin debe ser mayor a la fecha de inicio");
			vpu.showPopUp();
		}
	}
	
	private void getExcelFromServer(Date inicio, Date fin, List<Organizacion> organizaciones) {
			
		ProyectoBilpa.greetingService.crearExcelPendientes(inicio, fin, organizaciones, new AsyncCallback<String>() {

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al crear el EXCEL");
				vpu.showPopUp();
			}

			public void onSuccess(String result) {
				if (result.equals("")) {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al crear el EXCEL");
					vpu.showPopUp();							
				}else {
					vPanelTabla.setVisible(false);
					file.download();	
					popUp.hide();
				}
			}
		});
	}
	
	private void obtenerInformePendientes(Date inicio, Date fin) {
		popUp.show();
		cargarOrganizaciones();
		ProyectoBilpa.greetingService.obtenerTodosLosPendientesVisibles(inicio, fin, organizaciones, new AsyncCallback<List<PendienteData>>() {
			public void onFailure(Throwable caught) {
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener todas las ordenes activas");
				vpu.showPopUp();
			}
			public void onSuccess(List<PendienteData> result) {
				//if(result.size() > 0 ){
					vPanelTabla.setVisible(true);
					pendientes = result;
					crearTabla();	
				//}
				popUp.hide();
			}	
		});
	}

	private void setearWidgets() {
		this.vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal.setSpacing(5);
		imgExcel.setSize("40px", "40px");
		btnExcel.setSize("40px", "40px");
		this.vPanelPrincipal.setWidth("800px");
		
		bAceptar.setWidth("100px");
		
		operadorCheck.setValue(true);
		ducsaCheck.setValue(true);
		bilpaCheck.setValue(true);

		lblTituloPrincipal.setStyleName("Titulo");

		vPanelPrincipal.add(lblTituloPrincipal);
		vPanelTabla.setVisible(false);
		cellTablePendientes.setWidth("1200px", true);
		hPanelDatos.setSpacing(5);
	}
	
	private void cargarPanelesConWidgets() {
		hPanelDatos.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		vPanel1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);	
		vPanel1.add(hPanelDatos);		
		vPanel1.setSpacing(20);
		hPanelContenedor.add(vPanel1);
		
		hPanelPrincipal.setSpacing(10);
		vPanelPrincipal.setSpacing(10);
	
		checkPanel.setSpacing(5);

		checkPanel.add(bilpaCheck);
		checkPanel.add(ducsaCheck);
		checkPanel.add(operadorCheck);

		hPanelPrincipal.add(decorator);
		vPanelPrincipal.add(hPanelPrincipal);
		
		vPanelPrincipal.add(file);
		
		vPanelTabla.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelTabla.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		vPanelTabla.add(cellTablePendientes);			
		vPanelTabla.add(pager);
		vPanelPrincipal.add(vPanelTabla);

	}

	public VerticalPanel getPrincipalPanel() {
		return vPanelPrincipal;
	}
	
	protected void crearTabla() {	
		TextColumn<PendienteData> idColumn = new TextColumn<PendienteData>() {
			@Override
			public String getValue(PendienteData o) {
				return String.valueOf(o.getId());
			}
		};
		TextColumn<PendienteData> comentarioColumn = new TextColumn<PendienteData>() {
			@Override
			public String getValue(PendienteData o) {
				return o.getComentario();
			}
		};
		
		DatePickerCell plazoCell = new DatePickerCell(
				DateTimeFormat.getFormat("dd/MM/yyy"));
		
		Column<PendienteData, Date> plazoColumn = new Column<PendienteData, Date>(
				plazoCell) {
			
			@Override
			public Date getValue(PendienteData object) {
				return object.getPlazo();
			}
		};
		
		TextColumn<PendienteData> destinatarioColumn = new TextColumn<PendienteData>() {
			@Override
			public String getValue(PendienteData o) {
				return o.getDestinatario();
			}
		};
		
		TextColumn<PendienteData> activoColumn = new TextColumn<PendienteData>() {
			@Override
			public String getValue(PendienteData o) {
				return o.getActivo();
			}
		};

		TextColumn<PendienteData> estacionColumn = new TextColumn<PendienteData>() {
			@Override
			public String getValue(PendienteData o) {
				return o.getEmpresa();
			}
		};
		
		
		int columnCount = cellTablePendientes.getColumnCount();

		for (int i = 0; i < columnCount; i++) {
			cellTablePendientes.removeColumn(0);
		}

		cellTablePendientes.addColumn(idColumn, "Número");
		cellTablePendientes.addColumn(estacionColumn, "Estación");
		cellTablePendientes.addColumn(activoColumn, "Activo");
		cellTablePendientes.addColumn(comentarioColumn, "Comentario");
		cellTablePendientes.addColumn(plazoColumn, "Plazo");
		cellTablePendientes.addColumn(destinatarioColumn, "Destinatario");
		
		idColumn.setSortable(true);
		comentarioColumn.setSortable(true);
		plazoColumn.setSortable(true);
		destinatarioColumn.setSortable(true);
		activoColumn.setSortable(true);
		estacionColumn.setSortable(true);
		
		cellTablePendientes.setColumnWidth(idColumn, 10, Unit.PCT);
		cellTablePendientes.setColumnWidth(comentarioColumn, 25, Unit.PCT);
		cellTablePendientes.setColumnWidth(plazoColumn, 10, Unit.PCT);
		cellTablePendientes.setColumnWidth(destinatarioColumn, 25, Unit.PCT);
		cellTablePendientes.setColumnWidth(activoColumn, 25, Unit.PCT);
		cellTablePendientes.setColumnWidth(estacionColumn, 20, Unit.PCT);
		
		
		dataProvider = new ListDataProvider<PendienteData>();
		dataProvider.addDataDisplay(cellTablePendientes);
		
		List<PendienteData> list = cargarData();
		
		ListHandler<PendienteData> columnSortHandlerId = new ListHandler<PendienteData>(
				list);
		ListHandler<PendienteData> columnSortHandlerComentario = new ListHandler<PendienteData>(
				list);
		ListHandler<PendienteData> columnSortHandlerPlazo = new ListHandler<PendienteData>(
				list);
		ListHandler<PendienteData> columnSortHandlerDestinatario = new ListHandler<PendienteData>(
				list);
		ListHandler<PendienteData> columnSortHandlerActivo = new ListHandler<PendienteData>(
				list);
		ListHandler<PendienteData> columnSortHandlerEstacion = new ListHandler<PendienteData>(
				list);
	
		columnSortHandlerId.setComparator(idColumn,
				new Comparator<PendienteData>() {
					public int compare(PendienteData o1, PendienteData o2) {
						if (o1 == o2) {
							return 0;
						}

						if (o1 != null) {
							return (o2 != null) ? String
									.valueOf(o1.getId()).compareTo(
											String.valueOf(o2.getId())) : 1;
						}
						return -1;
					}
				});

		columnSortHandlerComentario.setComparator(comentarioColumn,
				new Comparator<PendienteData>() {
					public int compare(PendienteData o1, PendienteData o2) {
						if (o1 == o2) {
							return 0;
						}

						if (o1 != null) {
							return (o2 != null) ? o1.getComentario().compareTo(
									o2.getComentario()) : 1;
						}
						return -1;
					}
				});

		columnSortHandlerPlazo.setComparator(plazoColumn,
				new Comparator<PendienteData>() {
					public int compare(PendienteData o1, PendienteData o2) {
						if (o1 == o2) {
							return 0;
						}
						
						if (o1 != null) {
							return (o2 != null) ? String.valueOf(o1.getPlazo())
									.compareTo(o2.getPlazo() + "") : 1;
						}
						
						return -1;
	
					}
				});

		columnSortHandlerDestinatario.setComparator(destinatarioColumn,
				new Comparator<PendienteData>() {
					public int compare(PendienteData o1, PendienteData o2) {
						if (o1 == o2) {
							return 0;
						}

						if (o1 != null) {
							return (o2 != null) ? o1.getDestinatario().compareTo(
									o2.getDestinatario()) : 1;
						}
						return -1;
					}
				});

		
		columnSortHandlerActivo.setComparator(activoColumn,
				new Comparator<PendienteData>() {
					public int compare(PendienteData o1, PendienteData o2) {
						if (o1 == o2) {
							return 0;
						}

						if (o1 != null) {
							return (o2 != null) ? o1.getActivo().compareTo(
									o2.getActivo()) : 1;
						}
						return -1;
					}
				});
		
	columnSortHandlerEstacion.setComparator(estacionColumn,
				new Comparator<PendienteData>() {
					public int compare(PendienteData o1, PendienteData o2) {
						if (o1 == o2) {
							return 0;
						}

						if (o1 != null) {
							return (o2 != null) ? o1.getEmpresa().compareTo(
									o2.getEmpresa()) : 1;
						}
						return -1;
					}
				});

		
		cellTablePendientes.addColumnSortHandler(columnSortHandlerId);
		cellTablePendientes.addColumnSortHandler(columnSortHandlerComentario);
		cellTablePendientes.addColumnSortHandler(columnSortHandlerPlazo);
		cellTablePendientes.addColumnSortHandler(columnSortHandlerDestinatario);
		cellTablePendientes.addColumnSortHandler(columnSortHandlerActivo);
		cellTablePendientes.addColumnSortHandler(columnSortHandlerEstacion);
	}
	
	public List<PendienteData> cargarData() {
		dataProvider.getList().clear();
		List<PendienteData> list = dataProvider.getList();
		for (PendienteData orden : pendientes) {
			list.add(orden);
		}
		return list;
	}

}
