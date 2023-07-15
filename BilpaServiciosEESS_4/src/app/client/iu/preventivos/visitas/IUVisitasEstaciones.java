package app.client.iu.preventivos.visitas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.client.ProyectoBilpa;
import app.client.dominio.EstadoVisita;
import app.client.dominio.Persona;
import app.client.dominio.data.EstacionData;
import app.client.dominio.data.TecnicoData;
import app.client.dominio.data.VisitaDataList;
import app.client.iu.menu.IUMenuPrincipal;
import app.client.iu.menu.IUMenuPrincipalTecnico;
import app.client.iu.preventivos.estacion.IUVisitasDeEstacion;
import app.client.iu.widgets.ValidadorPopup;
import app.client.resources.BilpaResources;
import app.client.utilidades.utilObjects.ButtonImageCell;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;
import app.client.utilidades.utilObjects.SuggestBoxAdv;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;

public abstract class IUVisitasEstaciones {

	private PopupCargando popUp = new PopupCargando("Cargando...");

	ListDataProvider<VisitaDataList> dataProvider;

	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private VerticalPanel vPanelTabla = new VerticalPanel();

	private MultiWordSuggestOracle oracleEstacion = new MultiWordSuggestOracle();
	private SuggestBoxAdv filterSBoxEstacion = new SuggestBoxAdv(oracleEstacion);

	private MultiWordSuggestOracle oracleZona = new MultiWordSuggestOracle();
	private SuggestBoxAdv filterSBoxZona = new SuggestBoxAdv(oracleZona);

	private MultiWordSuggestOracle oracleEstado = new MultiWordSuggestOracle();
	private SuggestBoxAdv filterSBoxEstado = new SuggestBoxAdv(oracleEstado);

	private MultiWordSuggestOracle oracleTecnico = new MultiWordSuggestOracle();
	private SuggestBoxAdv filterSBoxTecnico = new SuggestBoxAdv(oracleTecnico);

	private Image imgFiltrar = new Image("img/filter_add.png");
	private Image imgLimpiarFiltros = new Image("img/filter_delete.png");

	private PushButton btnFiltrar = new PushButton(imgFiltrar);

	private PushButton btnLimpiarFiltros = new PushButton(imgLimpiarFiltros);

	private ButtonCell editarVisita = new ButtonImageCell();

	private HorizontalPanel hpFiltros = new HorizontalPanel();

	private VisitaDataList visitaFiltro;

	private SimplePager.Resources pagerResources;
	private SimplePager pager;

	private GlassPopup glass = new GlassPopup();

	Map<String, TecnicoData> nombreCompletoTecnicos = new HashMap<String, TecnicoData>();

	protected Label lblTituloPrincipal = new Label();

	private CellTable<VisitaDataList> cellTableVisitas = new CellTable<VisitaDataList>();

	private List<VisitaDataList> visitas = new ArrayList<VisitaDataList>();

	private Persona sesion;
	protected int sello;
	public IUVisitasEstaciones(Persona sesion, int sello) {
		this.sesion = sesion;
		this.sello = sello;
		
		setearWidgets();
		initPager();
		cargarEventos();
		agregarWidgets();
		visitaFiltro = new VisitaDataList();
		obtenerVisitas();
	}

	private void setSugBoxEstacionZona() {
		oracleEstacion.clear();
		oracleZona.clear();
		oracleTecnico.clear();
		oracleEstado.clear();

		for (VisitaDataList v : visitas) {
			oracleEstacion.add(v.getEstacionData().getNombre());
			oracleZona.add(v.getEstacionData().getZona());
			if (v.getEstado() != null)
				oracleEstado.add(v.getEstado().getEstado());
			if (v.getTecnicoData() != null
					&& !v.getTecnicoData().getNombreCompleto().isEmpty()) {
				oracleTecnico.add(v.getTecnicoData().getNombreCompleto());
				nombreCompletoTecnicos.put(v.getTecnicoData()
						.getNombreCompleto(), v.getTecnicoData());
			}
		}
	}

	private void setearWidgets() {
		lblTituloPrincipal.setStyleName("Titulo");

		filterSBoxEstacion.setPlaceHolderText("Filtrar estacion");
		filterSBoxEstacion.setTitle("Filtrar estacion");

		filterSBoxTecnico.setPlaceHolderText("Filtrar técnico");
		filterSBoxTecnico.setTitle("Filtrar técnico");

		filterSBoxZona.setPlaceHolderText("Filtrar zona");
		filterSBoxZona.setTitle("Filtrar zona");

		filterSBoxEstado.setPlaceHolderText("Filtrar estado");
		filterSBoxEstado.setTitle("Filtrar estado");
	}

	private void initPager() {
		pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, true, 100,
				true);
		pager.setDisplay(cellTableVisitas);
	}

	private void obtenerListaVisitaDataConFiltro() {
		EstacionData estacionData = null;
		TecnicoData tecnicoData;

		String estacion = filterSBoxEstacion.getValue();
		String zona = filterSBoxZona.getValue();
		String tecnico = filterSBoxTecnico.getValue();
		String estado = filterSBoxEstado.getValue();

		if (estacion.isEmpty() && zona.isEmpty() && tecnico.isEmpty() && estado.isEmpty()) {
			visitaFiltro = new VisitaDataList();
			obtenerVisitas();
		} else {

			visitaFiltro = new VisitaDataList();
			if (!estacion.isEmpty()) {
				if (estacionData == null)
					estacionData = new EstacionData();
				estacionData.setNombre(estacion);
			}

			if (!zona.isEmpty()) {
				if (estacionData == null)
					estacionData = new EstacionData();
				estacionData.setZona(zona);
			}

			if (!estado.isEmpty()){
				visitaFiltro.setEstado(EstadoVisita.getEstadoVisita(estado));;
			}

			visitaFiltro.setEstacionData(estacionData);

			if (!tecnico.isEmpty()) {
				tecnicoData = nombreCompletoTecnicos.get(filterSBoxTecnico.getValue());
				visitaFiltro.setTecnicoData(tecnicoData);
			}

			obtenerVisitas();
		}
	}

	private void obtenerVisitas() {
		popUp.show();
		ProyectoBilpa.greetingService.obtenerListaVisitasDataConFiltro(visitaFiltro, sello, new AsyncCallback<List<VisitaDataList>>() {
			public void onFailure(Throwable caught) {
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass,"Error","Error al obtener lista de visitas");
				vpu.showPopUp();
			}

			public void onSuccess(List<VisitaDataList> result) {
				visitas = result;
				cargarTabla();
				setSugBoxEstacionZona();
				popUp.hide();
			}
		});
	}

	public List<VisitaDataList> cargarVisitas() {
		dataProvider.getList().clear();
		List<VisitaDataList> list = dataProvider.getList();

		Collections.sort(visitas, new Comparator<VisitaDataList>() {
	        public int compare(VisitaDataList o1, VisitaDataList o2) {
	        	if (o1.getDiasSinVisita() > o2.getDiasSinVisita()){
	    			return -1;
	    		}
	    		if (o1.getDiasSinVisita() < o2.getDiasSinVisita()){
	    			return 1;
	    		}
	    		return 0;
	        }
	    });
		
		for (VisitaDataList visita : visitas) {
			list.add(visita);
		}
		return list;
	}

	private void cargarEventos() {
		btnFiltrar.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				obtenerListaVisitaDataConFiltro();
			}
		});

		btnLimpiarFiltros.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				limpiarFiltros();
			}
		});
	}

	private void limpiarFiltros() {
		filterSBoxZona.setText("");
		filterSBoxEstado.setText("");
		filterSBoxTecnico.setText("");
		filterSBoxEstacion.setText("");

		visitaFiltro = new VisitaDataList();
		obtenerVisitas();
	}

	private void agregarWidgets() {
		vPanelPrincipal
		.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		vPanelPrincipal.add(lblTituloPrincipal);
		vPanelPrincipal.setSpacing(20);
		vPanelTabla.setSpacing(5);

		filtros();

		vPanelTabla.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelTabla.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		vPanelTabla.add(hpFiltros);
		vPanelTabla.add(cellTableVisitas);
		vPanelTabla.add(pager);
		vPanelPrincipal.add(vPanelTabla);
	}

	private void filtros() {

		btnFiltrar.setTitle("Filtrar...");

		imgLimpiarFiltros.setSize("20px", "20px");
		imgFiltrar.setSize("20px", "20px");
		btnFiltrar.setSize("20px", "20px");
		btnLimpiarFiltros.setSize("20px", "20px");

		btnLimpiarFiltros.setTitle("Limpiar filtros...");

		hpFiltros.setSpacing(2);

		hpFiltros.add(filterSBoxEstacion);
		hpFiltros.add(filterSBoxZona);
		hpFiltros.add(filterSBoxEstado);
		hpFiltros.add(filterSBoxTecnico);

		hpFiltros.add(btnFiltrar);
		hpFiltros.add(btnLimpiarFiltros);

		filterSBoxEstado.setSize("135px", "27px");
		filterSBoxEstacion.setSize("135px", "27px");
		filterSBoxTecnico.setSize("135px", "27px");
		filterSBoxZona.setSize("135px", "27px");

	}

	private void cargarTabla() {
		TextColumn<VisitaDataList> estacionColumn = new TextColumn<VisitaDataList>() {
			@Override
			public String getValue(VisitaDataList e) {
				if (e.getEstacionData() != null) {
					return e.getEstacionData().getNombre();
				}
				return "";
			}
		};

		TextColumn<VisitaDataList> ultimaVisitaColumn = new TextColumn<VisitaDataList>() {
			@Override
			public String getValue(VisitaDataList e) {
				if (e.getFechaUltimaVisita() != null) {
					return DateTimeFormat.getFormat("dd/MM/yyyy HH:mm").format(
							e.getFechaUltimaVisita());
				}
				return "";
			}
		};

		TextColumn<VisitaDataList> proximaVisitaColumn = new TextColumn<VisitaDataList>() {
			@Override
			public String getValue(VisitaDataList e) {
				if (e.getEstado().equals(EstadoVisita.REALIZADA)) {
					return "";
				}
				
				if (e.getFechaProximaVisita() != null) {
					return DateTimeFormat.getFormat("dd/MM/yyyy hh:mm").format(
							e.getFechaProximaVisita());
				}
				return "";
			}
		};

		TextColumn<VisitaDataList> tecnicoAsignadoColumn = new TextColumn<VisitaDataList>() {
			@Override
			public String getValue(VisitaDataList e) {
				if (e.getEstado().equals(EstadoVisita.REALIZADA)) {
					return "";
				}
				
				if (e.getTecnicoData() != null) {
					return e.getTecnicoData().getNombreCompleto();
				}
				return "";
			}
		};

		TextColumn<VisitaDataList> zonaColumn = new TextColumn<VisitaDataList>() {
			@Override
			public String getValue(VisitaDataList e) {
				if (e.getEstacionData() != null) {
					return e.getEstacionData().getZona();
				}
				return "";
			}
		};

		TextColumn<VisitaDataList> estadoColumn = new TextColumn<VisitaDataList>() {
			@Override
			public String getValue(VisitaDataList v) {
				String texto = "Sin visita planificada";
				if (v.getEstado().equals(EstadoVisita.INICIADA)){
					texto = "INICIADA | Visita en curso";

				} else if (v.getEstado().equals(EstadoVisita.SIN_ASIGNAR)){ 
					texto = "SIN ASIGNAR | Falta asignar la visita";

				} else if (v.getEstado().equals(EstadoVisita.PENDIENTE)){
					texto = "PENDIENTE | Esperando se realice la visita";

				} else if (v.getEstado().equals(EstadoVisita.REALIZADA)){
					texto = "REALIZADA | Se realizó la última visita";

				} else if (v.getEstado().equals(EstadoVisita.SIN_VISITAS)){
					texto = "SIN VISITAS | Estación sin visitas";
				}
				return texto;
			}
		};

		TextColumn<VisitaDataList> diasSinVisitaColumn = new TextColumn<VisitaDataList>() {
			@Override
			public String getValue(VisitaDataList e) {
				return e.getDiasSinVisita() + "";
			}
		};

		Column<VisitaDataList, ImageResource> imageColumn = new Column<VisitaDataList, ImageResource>(
				new ImageResourceCell()) {

			@Override
			public ImageResource getValue(VisitaDataList object) {

				if (object.getDiasSinVisita() > 180) {
					return BilpaResources.INSTANCE.getAdvertenciaResource();
				}
				return null;
			}
		};

		final Column<VisitaDataList, String> editarVisitaColumn = new Column<VisitaDataList, String>(
				editarVisita) {
			@Override
			public String getValue(VisitaDataList orden) {
				return "img/look.png";
			}
		};

		editarVisitaColumn
		.setFieldUpdater(new FieldUpdater<VisitaDataList, String>() {
			public void update(int index, VisitaDataList object, String value) {
				IUVisitasDeEstacion iu = new IUVisitasDeEstacion(object.getEstacionData(), sesion, sello);
				if (sesion.getRol() == 3){
					IUMenuPrincipalTecnico.getInstancia().agregarWidgetAlMenu(iu.getPrincipalPanel());
				} else {
					IUMenuPrincipal.getInstancia().agregarWidgetAlMenu(iu.getPrincipalPanel());
				}
			}
		});

		int columnCount = cellTableVisitas.getColumnCount();
		for (int i = 0; i < columnCount; i++) {
			cellTableVisitas.removeColumn(0);
		}

		cellTableVisitas.addColumn(estacionColumn, "Estación");
		cellTableVisitas.addColumn(zonaColumn, "Zona");
		cellTableVisitas.addColumn(estadoColumn, "Estado");
		cellTableVisitas.addColumn(tecnicoAsignadoColumn, "Técnico asignado");
		cellTableVisitas.addColumn(ultimaVisitaColumn, "Última visita");
		cellTableVisitas.addColumn(proximaVisitaColumn, "Próxima visita");
		cellTableVisitas.addColumn(diasSinVisitaColumn, "Días sin visitas");
		cellTableVisitas.addColumn(imageColumn, "");
		cellTableVisitas.addColumn(editarVisitaColumn, "");

		estacionColumn.setSortable(true);
		ultimaVisitaColumn.setSortable(true);
		proximaVisitaColumn.setSortable(true);
		tecnicoAsignadoColumn.setSortable(true);
		zonaColumn.setSortable(true);
		estadoColumn.setSortable(true);
		diasSinVisitaColumn.setSortable(true);
		editarVisitaColumn.setSortable(true);

		dataProvider = new ListDataProvider<VisitaDataList>();
		dataProvider.addDataDisplay(cellTableVisitas);
		List<VisitaDataList> list = cargarVisitas();

		ListHandler<VisitaDataList> columnSortHandlerEstacion = new ListHandler<VisitaDataList>(
				list);
		ListHandler<VisitaDataList> columnSortHandlerUltimaVisita = new ListHandler<VisitaDataList>(
				list);
		ListHandler<VisitaDataList> columnSortHandlerProximaVisita = new ListHandler<VisitaDataList>(
				list);
		ListHandler<VisitaDataList> columnSortHandlerTecnico = new ListHandler<VisitaDataList>(
				list);
		ListHandler<VisitaDataList> columnSortHandlerZona = new ListHandler<VisitaDataList>(
				list);
		ListHandler<VisitaDataList> columnSortHandlerEstado = new ListHandler<VisitaDataList>(
				list);
		ListHandler<VisitaDataList> columnSortHandlerDiasSinVisita = new ListHandler<VisitaDataList>(
				list);

		columnSortHandlerEstacion.setComparator(estacionColumn,
				new Comparator<VisitaDataList>() {

			public int compare(VisitaDataList o1, VisitaDataList o2) {
				if (o1 == o2) {
					return 0;
				}
				if (o1 != null && o1.getEstacionData() != null) {
					return (o2 != null && o2.getEstacionData() != null) ? String
							.valueOf(o1.getEstacionData().getNombre())
							.compareTo(o2.getEstacionData().getNombre())
							: 1;
				}
				return -1;
			}
		});

		columnSortHandlerUltimaVisita.setComparator(ultimaVisitaColumn,
				new Comparator<VisitaDataList>() {
			public int compare(VisitaDataList o1, VisitaDataList o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? String.valueOf(
							o1.getFechaUltimaVisita()).compareTo(
									o2.getFechaUltimaVisita() + "") : 1;
				}
				return -1;
			}
		});

		columnSortHandlerTecnico.setComparator(tecnicoAsignadoColumn,
				new Comparator<VisitaDataList>() {
			public int compare(VisitaDataList o1, VisitaDataList o2) {
				
				TecnicoData tec1 = o1.getTecnicoData();
				TecnicoData tec2 = o2.getTecnicoData();
				
				if(o1.getEstado().equals(EstadoVisita.REALIZADA)) {
					tec1 = null;
				}
				
				if(o2.getEstado().equals(EstadoVisita.REALIZADA)) {
					tec2 = null;
				}
				
				if(tec1 != null) {
					if(tec2 != null) {
						return tec1.getNombreCompleto().compareTo(tec2.getNombreCompleto());
					} else {
						return 1;
					}
				}
				
				if((tec1 == null && tec2 == null) || tec2 == null) {
					return 0;
				}
				
				return -1;
			}
		});

		columnSortHandlerProximaVisita.setComparator(proximaVisitaColumn,
				new Comparator<VisitaDataList>() {
			public int compare(VisitaDataList o1, VisitaDataList o2) {
				
				Date fecha1 = o1.getFechaProximaVisita();
				Date fecha2 = o2.getFechaProximaVisita();
				
				if(o1.getEstado().equals(EstadoVisita.REALIZADA)) {
					fecha1 = null;
				}
				
				if(o2.getEstado().equals(EstadoVisita.REALIZADA)) {
					fecha2 = null;
				}
				
				if (fecha1 != null) {
					if(fecha2 != null) {
						return fecha1.compareTo(fecha2);
					} else {
						return 1;
					}
				}
				
				if((fecha1 == null && fecha2 == null) || fecha2 == null) {
					return 0;
				}
				
				return -1;
			}
		});

		columnSortHandlerZona.setComparator(zonaColumn,
				new Comparator<VisitaDataList>() {
			public int compare(VisitaDataList o1, VisitaDataList o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null && o2.getEstacionData() != null) ? String
							.valueOf(o1.getEstacionData().getZona())
							.compareTo(o2.getEstacionData().getZona())
							: 1;
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
					return (o2 != null && o2.getEstacionData() != null) ? String
							.valueOf(o1.getEstado().getEstado())
							.compareTo(o2.getEstado().getEstado())
							: 1;
				}
				return -1;
			}
		});

		columnSortHandlerDiasSinVisita.setComparator(diasSinVisitaColumn,
				new Comparator<VisitaDataList>() {
			public int compare(VisitaDataList o1, VisitaDataList o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return Integer.valueOf(o1.getDiasSinVisita())
							.compareTo(
									Integer.valueOf(o2
											.getDiasSinVisita()));
				}
				return -1;
			}
		});

		cellTableVisitas.addColumnSortHandler(columnSortHandlerEstacion);
		cellTableVisitas.addColumnSortHandler(columnSortHandlerUltimaVisita);
		cellTableVisitas.addColumnSortHandler(columnSortHandlerTecnico);
		cellTableVisitas.addColumnSortHandler(columnSortHandlerProximaVisita);
		cellTableVisitas.addColumnSortHandler(columnSortHandlerEstacion);
		cellTableVisitas.addColumnSortHandler(columnSortHandlerZona);
		cellTableVisitas.addColumnSortHandler(columnSortHandlerEstado);
		cellTableVisitas.addColumnSortHandler(columnSortHandlerDiasSinVisita);

	}

	public VerticalPanel getPrincipalPanel() {
		return vPanelPrincipal;
	}

}
