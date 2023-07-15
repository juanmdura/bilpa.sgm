package app.client.iu.pendientes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

import app.client.ProyectoBilpa;
import app.client.dominio.Estacion;
import app.client.dominio.data.DatoOrdenesActivasEmpresa;
import app.client.dominio.data.PendienteData;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;

public class IUPendientesAsignarCorrectivo extends DialogBox{

	private VerticalPanel vPanelDial = new VerticalPanel();
	private VerticalPanel vpCorrectivos = new VerticalPanel();

	private HorizontalPanel hPanelBotonera = new HorizontalPanel();
	private DecoratorPanel decorator = new DecoratorPanel();
	private Label lblTitulo = new Label("Asignar correctivo a los siguientes pendientes");
	private Label lblTitulo2 = new Label();

	private CellTable<PendienteData> tabla = new CellTable<PendienteData>();
	private ListDataProvider<PendienteData> dataProvider;
	private List<PendienteData> pendientes;

	private CellTable<DatoOrdenesActivasEmpresa> tablaC = new CellTable<DatoOrdenesActivasEmpresa>();
	private ListDataProvider<DatoOrdenesActivasEmpresa> dataProviderC;
	private List<DatoOrdenesActivasEmpresa> correctivos;
	private DatoOrdenesActivasEmpresa correctivoSeleccionado;

	private RadioButton rdbNuevo ;
	private RadioButton rdbExistente;

	private GlassPopup glass;
	private PopupCargando popUp;
	private IUPendientes padre;

	private boolean existenCorrectivoActivos = false;

	public IUPendientesAsignarCorrectivo(boolean autoHide, IUPendientes padre, GlassPopup glass, PopupCargando popUp, final List<PendienteData> pendientes) {
		super(autoHide);
		this.glass = glass;
		this.popUp = popUp;
		this.padre = padre;
		this.glass.show();
		this.pendientes = pendientes;

		rdbNuevo = new RadioButton("Correctivo", "Correctivo nuevo");
		rdbExistente = new RadioButton("Correctivo", "Correctivo existente");

		rdbExistente.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				tablaC.setVisible(true);
				rdbNuevo.removeStyleName("Negrita");
				rdbExistente.setStyleName("Negrita");
			}
		});

		rdbNuevo.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				tablaC.setVisible(false);
				rdbNuevo.setStyleName("Negrita");
				rdbExistente.removeStyleName("Negrita");
			}
		});

		final SelectionModel<DatoOrdenesActivasEmpresa> selectionModel1 = new SingleSelectionModel<DatoOrdenesActivasEmpresa>();
		tablaC.setSelectionModel(selectionModel1);
		selectionModel1.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				correctivoSeleccionado = ((SingleSelectionModel<DatoOrdenesActivasEmpresa>) selectionModel1).getSelectedObject();
			}
		});

		init();
		set();
		crearTabla();
		cargarPanelesConWidgets();
		color();
	}

	private void init() {
		ProyectoBilpa.greetingService.ordenesActivasDeUnaEmpresa(new Estacion(pendientes.get(0).getIdEstacion()), new AsyncCallback<ArrayList<DatoOrdenesActivasEmpresa>>() {
			@Override
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener correctivos");
				vpu.showPopUp();				
			}

			@Override
			public void onSuccess(ArrayList<DatoOrdenesActivasEmpresa> result) {
				correctivos = result;
				if (correctivos != null && correctivos.size() > 0){
					existenCorrectivoActivos = true;
				}
				setCorrectivos();
			}
		});
	}

	private void setCorrectivos() {
		if (existenCorrectivoActivos){
			creartablaC();
			lblTitulo2.setText("Seleccione en que correctivo de " + pendientes.get(0).getEmpresa() + " se van a reparar estos pendientes");
			rdbExistente.setValue(true);
			rdbNuevo.setValue(false);
			vpCorrectivos.setVisible(true);
			tablaC.setVisible(true);
		} else {
			rdbExistente.setValue(false);
			rdbNuevo.setValue(true);
			vpCorrectivos.setVisible(false);
			tablaC.setVisible(false);
		}
	}

	private void set() {
		setWidth("750px");
		decorator.setWidth("100%");
		vPanelDial.setSpacing(10);
		vPanelDial.setWidth("100%");
		tabla.setWidth("100%");
		tablaC.setWidth("100%");
		lblTitulo.setWidth("100%");
		lblTitulo2.setWidth("100%");

		btnAceptarModif.setWidth("100px");
		btnCancelModif.setWidth("100px");
	}

	private Button btnAceptarModif = new Button("Aceptar", new ClickHandler() {
		public void onClick(ClickEvent event) {
			asignar();
		}
	});

	private Button btnCancelModif = new Button("Cancelar", new ClickHandler() {
		public void onClick(ClickEvent event) {
			hide(true);
			glass.hide();
		}
	});

	private void color() {
		lblTitulo.setStyleName("Titulo");
		lblTitulo2.setStyleName("Titulo");
	}

	private void cargarPanelesConWidgets() {
		vPanelDial.setSpacing(10);
		vPanelDial.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hPanelBotonera.setSpacing(10);
		hPanelBotonera.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hPanelBotonera.add(btnCancelModif);
		hPanelBotonera.add(btnAceptarModif);
		vPanelDial.add(decorator);

		HorizontalPanel hpSpace = new HorizontalPanel();
		HorizontalPanel hpRadios = new HorizontalPanel();
		VerticalPanel vp2 = new VerticalPanel();

		vpCorrectivos.setSpacing(8);
		hpSpace.setHeight("60px");

		hpRadios.add(rdbExistente);
		hpRadios.add(rdbNuevo);

		vPanelDial.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelDial.add(lblTitulo);
		vPanelDial.add(tabla);

		vPanelDial.add(hpSpace);

		vpCorrectivos.add(lblTitulo2);
		vpCorrectivos.add(hpRadios);
		vpCorrectivos.add(tablaC);

		vPanelDial.add(vpCorrectivos);

		vPanelDial.add(hPanelBotonera);

		add(vPanelDial);
	}

	private void asignar() {
		int numero = 0;
		if (rdbExistente.getValue()){
			if (correctivoSeleccionado == null) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe seleccionar un correctivo");
				vpu.showPopUp();
				return;
			}
			numero = Integer.valueOf(correctivoSeleccionado.getNumero());
		}
		padre.asignarCorrectivoAPendientes(pendientes, numero);
		glass.hide();
		hide();
	}

	private void crearTabla() {
		TextColumn<PendienteData> numeroColumn = new TextColumn<PendienteData>() {
			@Override
			public String getValue(PendienteData e) {
				return e.getId()+"";
			}
		};

		TextColumn<PendienteData> textoColumn = new TextColumn<PendienteData>() {
			@Override
			public String getValue(PendienteData e) {
				return e.getComentario();
			}
		};

		tabla.addColumn(numeroColumn,"Número");
		tabla.addColumn(textoColumn,"Texto");

		numeroColumn.setSortable(true);
		textoColumn.setSortable(true);

		dataProvider = new ListDataProvider<PendienteData>();
		dataProvider.addDataDisplay(tabla);
		List<PendienteData> list = cargarLista();

		ListHandler<PendienteData> columnSortHandlerNumero = new ListHandler<PendienteData>(list);
		columnSortHandlerNumero.setComparator(numeroColumn, new Comparator<PendienteData>() {
			public int compare(PendienteData o1, PendienteData o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getId()+"".compareTo(o2.getId()+"") : 1;
				}
				return -1;
			}
		});

		ListHandler<PendienteData> columnSortHandlerDesc = new ListHandler<PendienteData>(list);
		columnSortHandlerDesc.setComparator(textoColumn, new Comparator<PendienteData>() {
			public int compare(PendienteData o1, PendienteData o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getComentario().compareTo(o2.getComentario()) : 1;
				}
				return -1;
			}
		});

		tabla.addColumnSortHandler(columnSortHandlerNumero);
		tabla.addColumnSortHandler(columnSortHandlerDesc);
	}

	private List<PendienteData> cargarLista() {
		dataProvider.getList().clear();
		List<PendienteData> list = dataProvider.getList();

		for (PendienteData data : pendientes) {
			list.add(data);
		}
		tabla.redraw();
		return list;
	}

	private List<DatoOrdenesActivasEmpresa> cargarListaC() {
		dataProviderC.getList().clear();
		List<DatoOrdenesActivasEmpresa> list = dataProviderC.getList();

		for (DatoOrdenesActivasEmpresa data : correctivos) {
			list.add(data);
		}
		tablaC.redraw();
		return list;
	}

	private void creartablaC() {
		TextColumn<DatoOrdenesActivasEmpresa> numeroColumn = new TextColumn<DatoOrdenesActivasEmpresa>() {
			@Override
			public String getValue(DatoOrdenesActivasEmpresa e) {
				return e.getNumero()+"";
			}
		};

		TextColumn<DatoOrdenesActivasEmpresa> textoColumn = new TextColumn<DatoOrdenesActivasEmpresa>() {
			@Override
			public String getValue(DatoOrdenesActivasEmpresa e) {
				return e.getEstado();
			}
		};

		TextColumn<DatoOrdenesActivasEmpresa> tecnicoColumn = new TextColumn<DatoOrdenesActivasEmpresa>() {
			@Override
			public String getValue(DatoOrdenesActivasEmpresa e) {
				return e.getTecnico()+"";
			}
		};

		TextColumn<DatoOrdenesActivasEmpresa> fechaIniColumn = new TextColumn<DatoOrdenesActivasEmpresa>() {
			@Override
			public String getValue(DatoOrdenesActivasEmpresa e) {
				return e.getFecha();
			}
		};

		tablaC.addColumn(numeroColumn,"Número");
		tablaC.addColumn(textoColumn,"Estado");
		tablaC.addColumn(tecnicoColumn,"Técnico");
		tablaC.addColumn(fechaIniColumn,"Inicio");

		numeroColumn.setSortable(true);
		textoColumn.setSortable(true);
		tecnicoColumn.setSortable(true);
		fechaIniColumn.setSortable(true);

		dataProviderC = new ListDataProvider<DatoOrdenesActivasEmpresa>();
		dataProviderC.addDataDisplay(tablaC);
		List<DatoOrdenesActivasEmpresa> list = cargarListaC();

		ListHandler<DatoOrdenesActivasEmpresa> columnSortHandlerNumero = new ListHandler<DatoOrdenesActivasEmpresa>(list);
		columnSortHandlerNumero.setComparator(numeroColumn, new Comparator<DatoOrdenesActivasEmpresa>() {
			public int compare(DatoOrdenesActivasEmpresa o1, DatoOrdenesActivasEmpresa o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getNumero().compareTo(o2.getNumero()) : 1;
				}
				return -1;
			}
		});

		ListHandler<DatoOrdenesActivasEmpresa> columnSortHandlerDesc = new ListHandler<DatoOrdenesActivasEmpresa>(list);
		columnSortHandlerDesc.setComparator(textoColumn, new Comparator<DatoOrdenesActivasEmpresa>() {
			public int compare(DatoOrdenesActivasEmpresa o1, DatoOrdenesActivasEmpresa o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getEstado().compareTo(o2.getEstado()) : 1;
				}
				return -1;
			}
		});

		ListHandler<DatoOrdenesActivasEmpresa> columnSortHandlerTecnico = new ListHandler<DatoOrdenesActivasEmpresa>(list);
		ListHandler<DatoOrdenesActivasEmpresa> columnSortHandlerFecIni = new ListHandler<DatoOrdenesActivasEmpresa>(list);

		columnSortHandlerTecnico.setComparator(tecnicoColumn,new Comparator<DatoOrdenesActivasEmpresa>() {
			public int compare(DatoOrdenesActivasEmpresa o1, DatoOrdenesActivasEmpresa o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? String.valueOf(o1.getTecnico()).compareTo(o2.getTecnico()+"") : 1;
				}
				return -1;
			}
		});
		columnSortHandlerFecIni.setComparator(fechaIniColumn,new Comparator<DatoOrdenesActivasEmpresa>() {
			public int compare(DatoOrdenesActivasEmpresa o1, DatoOrdenesActivasEmpresa o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? String.valueOf(o1.getFecha()).compareTo(o2.getFecha()+"") : 1;
				}
				return -1;
			}
		});


		tablaC.addColumnSortHandler(columnSortHandlerNumero);
		tablaC.addColumnSortHandler(columnSortHandlerDesc);
		tablaC.addColumnSortHandler(columnSortHandlerTecnico);
		tablaC.addColumnSortHandler(columnSortHandlerFecIni);
	} 
}
