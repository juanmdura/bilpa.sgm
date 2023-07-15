package app.client.iu.preventivos.estacion;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import app.client.ProyectoBilpa;
import app.client.dominio.EstadoVisita;
import app.client.dominio.Organizacion;
import app.client.dominio.Persona;
import app.client.dominio.data.EstacionData;
import app.client.dominio.data.TecnicoData;
import app.client.dominio.data.VisitaDataList;
import app.client.iu.menu.IUMenuPrincipal;
import app.client.iu.menu.IUMenuPrincipalTecnico;
import app.client.iu.preventivos.visitas.IUGestionarVisita;
import app.client.iu.preventivos.visitas.IUVisitasEstaciones;
import app.client.iu.preventivos.visitas.IUVisitasEstacionesDucsa;
import app.client.iu.preventivos.visitas.IUVisitasEstacionesPetrobras;
import app.client.iu.preventivos.visitas.IUVisitasPlantasAncapContratos;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.utilObjects.ButtonImageCell;
import app.client.utilidades.utilObjects.FileDownload;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.DatePickerCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;

public class IUVisitasDeEstacion {

	FileDownload file = new FileDownload();

	private PopupCargando popUp = new PopupCargando("Cargando...");
	private CellTable<VisitaDataList> cellTableVisitas = new CellTable<VisitaDataList>();
	private List<VisitaDataList> visitas = new ArrayList<VisitaDataList>();

	private List<TecnicoData> tecnicosData;
	private List<String> nombreTecnicos;
	private List<String> estados = new ArrayList<String>();

	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private VerticalPanel vPanelTabla = new VerticalPanel();

	private VisitaDataList visitaABorrar;

	private Button agregarVisita = new Button("+");;

	private ListDataProvider<VisitaDataList> dataProvider;

	private Label lblTituloPrincipal = new Label();
	private EstacionData estacion;

	private DialogBox dialogBoxT = new DialogBox();
	private Label lblTituloDialTEliminar = new Label("Seguro desea eliminar esta visita?");

	private VerticalPanel vpDialT = new VerticalPanel();
	private HorizontalPanel hpDialT = new HorizontalPanel();
	private HorizontalPanel hp2DialT = new HorizontalPanel();

	private Image imgVolver = new Image("img/back.png");
	private PushButton btnVolver = new PushButton(imgVolver);
	
	private GlassPopup glass = new GlassPopup();

	private IUGestionarVisita iu;

	private Persona sesion;
	private int sello;
	
	private SimplePager.Resources pagerResources;
	private SimplePager pager;
	
	public IUVisitasDeEstacion(EstacionData e, final Persona sesion, final int sello) {
		this.sesion = sesion;
		estacion = e;
		this.sello = sello;
		
		btnVolver.setSize("20px", "20px");
		btnVolver.setTitle("Volver a lista de preventivos");
		btnVolver.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				IUVisitasEstaciones iu = new IUVisitasEstacionesDucsa(sesion);
				if (sello == 2){
					iu = new IUVisitasEstacionesDucsa(sesion);
				} else if (sello == 3){
					iu = new IUVisitasEstacionesPetrobras(sesion);
				} else if (sello == 6){
					iu = new IUVisitasPlantasAncapContratos(sesion);
				}
				if (sesion.getRol() == 3){
					IUMenuPrincipalTecnico.getInstancia().agregarWidgetAlMenu(iu.getPrincipalPanel());
				} else {
					IUMenuPrincipal.getInstancia().agregarWidgetAlMenu(iu.getPrincipalPanel());
				}
			}
		});
		initPager();
		setearWidgets();
		obtenerListaVisitaDataPorEmpresa(estacion.getNombre());
		iu = new IUGestionarVisita(this, glass, estacion);
		cargarEventos();
	}
	
	private Button btnElimDial = new Button("Eliminar", new ClickHandler() {
		public void onClick(ClickEvent event) {
			dialogBoxT.hide(true);
			glass.hide();
			eliminarVisita(visitaABorrar);
		}
	});

	private Button btnCancelDial = new Button("Cancelar", new ClickHandler() {
		public void onClick(ClickEvent event) {
			dialogBoxT.hide(true);
			glass.hide();
		}
	});
	
	public DialogBox dialElimVisita(VisitaDataList visitaSeleccionada) {
		visitaABorrar = visitaSeleccionada;
		vpDialT.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hpDialT.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		vpDialT.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		vpDialT.add(lblTituloDialTEliminar);
		
		dialogBoxT.setAutoHideEnabled(false);

		btnElimDial.setWidth("100px");
		btnCancelDial.setWidth("100px");
		
		cellTableVisitas.setWidth("100%");

		hp2DialT.add(btnCancelDial);
		hp2DialT.setSpacing(5);
		hp2DialT.add(btnElimDial);

		vpDialT.add(hpDialT);
		vpDialT.add(hp2DialT);

		vpDialT.setSize("400px", "100px");
		dialogBoxT.setWidget(vpDialT);
		return dialogBoxT;
	}

	private void cargarEventos() {
		agregarVisita.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				glass.show();
				DialogBox dialogo = iu.gethPanelDialNuevaVisita();
				dialogo.show();
				dialogo.center();
			}
		});
	}

	private void setearWidgets() {
		lblTituloPrincipal = new Label("Planificación de visitas:  " + estacion.getNombre());
		lblTituloPrincipal.setStyleName("Titulo");
		
		estados.add("");
		estados.add(EstadoVisita.INICIADA.name());
		estados.add(EstadoVisita.REALIZADA.name());
		agregarWidgets();
	}

	public void obtenerListaVisitaDataPorEmpresa(String nombreEmpresa) {
		popUp.show();
		ProyectoBilpa.greetingService.obtenerListaVistasDataEstacionWeb(
				nombreEmpresa, new AsyncCallback<List<VisitaDataList>>() {
					public void onFailure(Throwable caught) {
						popUp.hide();
						ValidadorPopup vpu = new ValidadorPopup(glass, "Error",
								"Error al obtener lista de data visitas");
						vpu.showPopUp();
					}

					public void onSuccess(List<VisitaDataList> result) {
						visitas = result;
						obtenerListaTecnicoData();
					}
				});
	}

	private void obtenerListaTecnicoData() {
		ProyectoBilpa.greetingService
		.obtenerTodosLosDataTecnico(new AsyncCallback<List<TecnicoData>>() {
			public void onFailure(Throwable caught) {
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error",
						"Error al obtener lista de visitas");
				vpu.showPopUp();
			}

			public void onSuccess(List<TecnicoData> result) {
				tecnicosData = result;
				cargarListaNombresTecnicos();
				cargarTabla();
				popUp.hide();
			}
		});
	}

	private void cargarListaNombresTecnicos() {
		nombreTecnicos = new ArrayList<String>();
		nombreTecnicos.add("");
		for (TecnicoData t : tecnicosData) {
			nombreTecnicos.add(t.getNombreCompleto());
		}
	}

	private void agregarWidgets() {
		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		HorizontalPanel hp = new HorizontalPanel();
		hp.setWidth("100%");
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hp.add(lblTituloPrincipal);
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		hp.add(btnVolver);
		
		vPanelPrincipal.add(hp);
		vPanelPrincipal.setSpacing(20);
		vPanelTabla.setSpacing(5);

		vPanelTabla.add(cellTableVisitas);
		vPanelTabla.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelTabla.add(pager);
		vPanelPrincipal.add(vPanelTabla);
		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		vPanelPrincipal.add(agregarVisita);

		vPanelPrincipal.add(file);
	}

	private void eliminarVisita(VisitaDataList object) {
		if (sesion.getRol() == 1 || sesion.getRol() == 5){
			popUp.show();
			ProyectoBilpa.greetingService.eliminarVisita(object,new AsyncCallback<Boolean>() {
				public void onFailure(Throwable caught) {
					popUp.hide();
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error",	"Error al obtener lista de data visitas");
					vpu.showPopUp();
				}

				public void onSuccess(Boolean result) {
					obtenerListaVisitaDataPorEmpresa(estacion.getNombre());
					popUp.hide();
				}
			});
		}
	}

	private void modificarVisita(VisitaDataList visitaData) {
		if (sesion.getRol() == 1 || sesion.getRol() == 5){
			if (visitaData.getFechaProximaVisita() != null) {
				ProyectoBilpa.greetingService.modificarVisita(visitaData, new AsyncCallback<Boolean>() {
					public void onFailure(Throwable caught) {
						popUp.hide();
						ValidadorPopup vpu = new ValidadorPopup(glass,"Error","Error al obtener lista de visitas");
						vpu.showPopUp();
					}
					public void onSuccess(Boolean result) {
						obtenerListaVisitaDataPorEmpresa(estacion.getNombre());
					}
				});

			} else {
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe ingresar una fecha de visita");
				vpu.showPopUp();
			}
		}
	}

	private void cargarTabla() {
		DatePickerCell fechaVisitaDateCell = new DatePickerCell(DateTimeFormat.getFormat("dd.MM.yyy HH:mm"));
		Column<VisitaDataList, Date> fechaVisitaColumn = new Column<VisitaDataList, Date>(fechaVisitaDateCell) {

			@Override
			public Date getValue(VisitaDataList visitaDataList) {
				if (visitaDataList.getEstado().equals(EstadoVisita.REALIZADA)){
					return visitaDataList.getFechaRealizada();
				}

				return visitaDataList.getFechaProximaVisita();
			}
		};

		fechaVisitaColumn
		.setFieldUpdater(new FieldUpdater<VisitaDataList, Date>() {
			public void update(int index, VisitaDataList object,
					Date value) {
				if (!object.getEstado().equals(EstadoVisita.REALIZADA)) {
					object.setFechaProximaVisita(value);
					modificarVisita(object);
				} else {
					ValidadorPopup vpu = new ValidadorPopup(glass,
							"Error",
							"No se puede modificar una visita que ya fue realizada");
					vpu.showPopUp();
				}
			}
		});

		TextColumn<VisitaDataList> tecnicoAsignadoTextColumn = new TextColumn<VisitaDataList>() {
			@Override
			public String getValue(VisitaDataList e) {
				if (e.getTecnicoData() != null && e.getTecnicoData().getNombreCompleto() != null) {
					return e.getTecnicoData().getNombreCompleto();
				}
				return "";
			}
		};
		
		SelectionCell tecnicoCell = new SelectionCell(nombreTecnicos);
		Column<VisitaDataList, String> tecnicoAsignadoColumn = new Column<VisitaDataList, String>(tecnicoCell) {
			@Override
			public String getValue(VisitaDataList e) {
				if (e.getTecnicoData() != null && e.getTecnicoData().getNombreCompleto() != null) {
					return e.getTecnicoData().getNombreCompleto();
				}
				return "";
			}
		};

		tecnicoAsignadoColumn.setFieldUpdater(new FieldUpdater<VisitaDataList, String>() {
			public void update(int index, VisitaDataList object, String value) {
				if (!object.getEstado().equals(EstadoVisita.REALIZADA)) {
					boolean asignado = false;
					for (TecnicoData td : tecnicosData) {
						if (td.getNombreCompleto().equalsIgnoreCase(value)) {
							object.setTecnicoData(td);
							modificarVisita(object);
							asignado = true;
							return;
						}
					}
					if (!asignado) {
						object.setTecnicoData(null);
						modificarVisita(object);
					}
				} else {
					ValidadorPopup vpu = new ValidadorPopup(glass,"Error","No se puede modificar una visita que ya fue realizada");
					vpu.showPopUp();
				}
			}
		});

		TextColumn<VisitaDataList> estadoLabelColumn = new TextColumn<VisitaDataList>() {
			@Override
			public String getValue(VisitaDataList e) {
				return e.getEstado() + "";
			}
		};

		SelectionCell estadoCell = new SelectionCell(estados);
		Column<VisitaDataList, String> estadoColumn = new Column<VisitaDataList, String>(estadoCell) {
			@Override
			public String getValue(VisitaDataList e) {
				return e.getEstado().name();
			}
		};

		estadoColumn.setFieldUpdater(new FieldUpdater<VisitaDataList, String>() {
			public void update(int index, VisitaDataList visita, String value) {
				if (value.equals(EstadoVisita.INICIADA.name()) || value.equals(EstadoVisita.REALIZADA.name())){

					if ( (visita.getEstado().equals(EstadoVisita.REALIZADA) && value.equals(EstadoVisita.INICIADA.name())) ||
							(visita.getEstado().equals(EstadoVisita.INICIADA) && value.equals(EstadoVisita.REALIZADA.name() ))){
						visita.setEstado(EstadoVisita.getEstadoVisita(value));
						modificarVisita(visita);
					}
				}
				return;
			}
		});

		//		TextColumn<VisitaDataList> estadoColumn = new TextColumn<VisitaDataList>() {
		//			@Override
		//			public String getValue(VisitaDataList e) {
		//				return e.getEstado().getEstado();
		//			}
		//		};

		final Column<VisitaDataList, String> reporteVisitaBilpaColumn = new Column<VisitaDataList, String>(new ButtonCell()) {
			@Override
			public String getValue(VisitaDataList v) {
				return "Descargar";
			}
		};

		reporteVisitaBilpaColumn.setFieldUpdater(new FieldUpdater<VisitaDataList, String>() {
			public void update(int index, VisitaDataList visita, String value) {
				generarReporte(visita, Organizacion.Bilpa);
			}
		});

		final Column<VisitaDataList, String> reporteVisitaOperadorColumn = new Column<VisitaDataList, String>(new ButtonCell()) {
			@Override
			public String getValue(VisitaDataList v) {
				return "Descargar";
			}
		};

		reporteVisitaOperadorColumn.setFieldUpdater(new FieldUpdater<VisitaDataList, String>() {
			public void update(int index, VisitaDataList visita, String value) {
				/*ValidadorPopup vpu = new ValidadorPopup(glass,
						"Info",
						"Aun no esta disponible este reporte");
				vpu.showPopUp();*/
				generarReporte(visita, Organizacion.Operador);
			}
		});

		final Column<VisitaDataList, String> reporteVisitaDucsaColumn = new Column<VisitaDataList, String>(new ButtonCell()) {
			@Override
			public String getValue(VisitaDataList v) {
				return "Descargar";
			}
		};

		reporteVisitaDucsaColumn.setFieldUpdater(new FieldUpdater<VisitaDataList, String>() {
			public void update(int index, VisitaDataList visita, String value) {
				generarReporte(visita, Organizacion.Petrolera);
			}
		});

		final Column<VisitaDataList, String> eliminarVisitaColumn = new Column<VisitaDataList, String>(new ButtonImageCell()) {
			@Override
			public String getValue(VisitaDataList v) {
				return "img/menos.png";
			}
		};

		eliminarVisitaColumn.setFieldUpdater(new FieldUpdater<VisitaDataList, String>() {
			public void update(int index, VisitaDataList object, String value) {
				if (!object.getEstado().equals(EstadoVisita.REALIZADA)) {
					glass.show();
					DialogBox db = dialElimVisita(object);
					db.center();
					db.show();
				} else {
					ValidadorPopup vpu = new ValidadorPopup(glass,
							"Info",
							"No se puede eliminar una visita que ya fue realizada");
					vpu.showPopUp();
				}

			}
		});

		int columnCount = cellTableVisitas.getColumnCount();
		for (int i = 0; i < columnCount; i++) {
			cellTableVisitas.removeColumn(0);
		}

		dataProvider = new ListDataProvider<VisitaDataList>();
		dataProvider.addDataDisplay(cellTableVisitas);
		List<VisitaDataList> list = cargarVisitas();
		ListHandler<VisitaDataList> columnSortHandlerTecnico = new ListHandler<VisitaDataList>(
				list);

		cellTableVisitas.addColumn(fechaVisitaColumn, "Fecha visita");
		if (sesion.getRol() != 3){
			cellTableVisitas.addColumn(tecnicoAsignadoColumn, "Técnico");
			setTecnicoColumnComparator(tecnicoAsignadoColumn, columnSortHandlerTecnico);
		} else {
			cellTableVisitas.addColumn(tecnicoAsignadoTextColumn, "Técnico");
			setTecnicoColumnComparator(tecnicoAsignadoTextColumn, columnSortHandlerTecnico);
		}
		cellTableVisitas.addColumn(estadoLabelColumn, "Estado");
		
		if (sesion.getRol() != 3){
			cellTableVisitas.addColumn(estadoColumn, "Modificar Estado");
		}
		
		cellTableVisitas.addColumn(reporteVisitaOperadorColumn, "Operador");
		if (sesion.getRol() != 3){
			cellTableVisitas.addColumn(reporteVisitaBilpaColumn, "Bilpa");
			cellTableVisitas.addColumn(reporteVisitaDucsaColumn, "Petrolera");
			cellTableVisitas.addColumn(eliminarVisitaColumn, "");
		}

		fechaVisitaColumn.setSortable(true);
		tecnicoAsignadoColumn.setSortable(true);
		tecnicoAsignadoTextColumn.setSortable(true);
		estadoColumn.setSortable(true);

		ListHandler<VisitaDataList> columnSortHandlerFechaVisita = new ListHandler<VisitaDataList>(
				list);
		ListHandler<VisitaDataList> columnSortHandlerEstado = new ListHandler<VisitaDataList>(
				list);
		ListHandler<VisitaDataList> columnSortHandlerEstadoLabel = new ListHandler<VisitaDataList>(
				list);

		columnSortHandlerFechaVisita.setComparator(fechaVisitaColumn,
				new Comparator<VisitaDataList>() {
			public int compare(VisitaDataList o1, VisitaDataList o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? String.valueOf(
							o1.getFechaProximaVisita()).compareTo(
									o2.getFechaProximaVisita() + "") : 1;
				}
				return -1;
			}
		});

		columnSortHandlerEstado.setComparator(estadoColumn,
				new Comparator<VisitaDataList>() {
			public int compare(VisitaDataList o1, VisitaDataList o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? String.valueOf(
							o1.getEstado() + "").compareTo(
									o2.getEstado() + "") : 1;
				}
				return -1;
			}
		});

		columnSortHandlerEstadoLabel.setComparator(estadoLabelColumn,
				new Comparator<VisitaDataList>() {
			public int compare(VisitaDataList o1, VisitaDataList o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? String.valueOf(
							o1.getEstado() + "").compareTo(
									o2.getEstado() + "") : 1;
				}
				return -1;
			}
		});

		cellTableVisitas.addColumnSortHandler(columnSortHandlerFechaVisita);
		cellTableVisitas.addColumnSortHandler(columnSortHandlerTecnico);
		cellTableVisitas.addColumnSortHandler(columnSortHandlerEstado);
		cellTableVisitas.addColumnSortHandler(columnSortHandlerEstadoLabel);

		/*cellTableVisitas.setColumnWidth(fechaVisitaColumn, 60, Unit.PX);
		cellTableVisitas.setColumnWidth(reporteVisitaBilpaColumn, 100, Unit.PX);
		cellTableVisitas.setColumnWidth(reporteVisitaOperadorColumn, 100, Unit.PX);
		cellTableVisitas.setColumnWidth(reporteVisitaDucsaColumn, 100, Unit.PX);*/

	}

	private void setTecnicoColumnComparator( Column<VisitaDataList, String> tecnicoAsignadoColumn, ListHandler<VisitaDataList> columnSortHandlerTecnico) {
		columnSortHandlerTecnico.setComparator(tecnicoAsignadoColumn,
				new Comparator<VisitaDataList>() {
			public int compare(VisitaDataList o1, VisitaDataList o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null && o1.getTecnicoData() != null) {
					return (o2 != null && o2.getTecnicoData() != null) ? String
							.valueOf(
									o1.getTecnicoData()
									.getNombreCompleto())
									.compareTo(
											o2.getTecnicoData()
											.getNombreCompleto()) : 1;
				}
				return -1;
			}
		});
	}

	protected void generarReporte(VisitaDataList visita, Organizacion organizacion) {
		if (visita.getEstado().equals(EstadoVisita.INICIADA) || visita.getEstado().equals(EstadoVisita.REALIZADA))
		{

			ProyectoBilpa.greetingService.crearPDFVisitas(visita.getId(), organizacion, new AsyncCallback<String>() {

				public void onFailure(Throwable caught) {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al crear PDF");
					vpu.showPopUp();
				}

				public void onSuccess(String result) {
					if (result.equals("")) {
						ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al crear PDF");
						vpu.showPopUp();						
					} else {
						file.download();
					}
				}
			});
		} else {
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Para descargar el reporte, la visita debe estar iniciada o finalizada");
			vpu.showPopUp();
		}
	}

	public List<VisitaDataList> cargarVisitas() {
		dataProvider.getList().clear();
		List<VisitaDataList> list = dataProvider.getList();

		for (VisitaDataList visita : visitas) {
			list.add(visita);
		}
		return list;
	}

	public VerticalPanel getPrincipalPanel() {
		return vPanelPrincipal;
	}
	
	private void initPager() {
		pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, true, 100, true);
		pager.setDisplay(cellTableVisitas);
	}
}
