package app.client.iu.pendientes;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;

import app.client.ProyectoBilpa;
import app.client.dominio.data.PendienteData;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;

public class IUPendientesDescartar extends DialogBox{
	private VerticalPanel vPanelDial = new VerticalPanel();
	private HorizontalPanel hPanelBotonera = new HorizontalPanel();
	private DecoratorPanel decorator = new DecoratorPanel();
	private Label lblTitulo = new Label("Descartar pendiente");
	
	private Label lblPendientes = new Label("Pendientes a descartar");
	private Label lblMotivo = new Label("Motivo");
	
	private CellTable<PendienteData> tabla = new CellTable<PendienteData>();
	private ListDataProvider<PendienteData> dataProvider;
	
	private TextBox txtNombre = new TextBox();

	private GlassPopup glass;
	private PopupCargando popUp;
	private IUPendientes padre;
	private List<PendienteData> pendientes;

	public IUPendientesDescartar(boolean autoHide, IUPendientes padre, GlassPopup glass, PopupCargando popUp, final List<PendienteData> pendientes) {
		super(autoHide);
		this.glass = glass;
		this.popUp = popUp;
		this.padre = padre;
		this.glass.show();
		this.pendientes = pendientes;
		
		if (pendientes.size()>1){
			lblTitulo.setText("Descartar pendientes");
		}
		
		txtNombre.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (KeyCodes.KEY_ENTER == event.getNativeEvent().getKeyCode())
				{
					descartar();
				}
			}
		});
		
		set();
		cargarPanelesConWidgets();
		color();
		cargar();
		crearTabla();
	}
	
	private void set() {
		tabla.setWidth("100%");
	}

	private Button btnAceptarModif = new Button("Aceptar", new ClickHandler() {
		public void onClick(ClickEvent event) {
			descartar();
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
		lblPendientes.setStyleName("Negrita");
		lblMotivo.setStyleName("Negrita");

		btnAceptarModif.setWidth("100px");
		btnCancelModif.setWidth("100px");
		
		txtNombre.setWidth("500px");
	}

	private void cargarPanelesConWidgets() {
		vPanelDial.setSpacing(10);
		vPanelDial.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hPanelBotonera.setSpacing(10);
		hPanelBotonera.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hPanelBotonera.add(btnCancelModif);
		hPanelBotonera.add(btnAceptarModif);
		vPanelDial.add(decorator);
		add(vPanelDial);
	}

	private void cargar() {
		vPanelDial.setSize("350px", "90px");
		vPanelDial.add(lblTitulo);

		vPanelDial.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		vPanelDial.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		
		vPanelDial.add(lblPendientes);
		vPanelDial.add(tabla);
		
		vPanelDial.add(lblMotivo);
		vPanelDial.add(txtNombre);
		
		vPanelDial.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelDial.add(hPanelBotonera);
		
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	            txtNombre.setFocus(true);
	        }
	    });
	}

	private void descartar() {
		if (nombreEsValido()){
			popUp.show();

			ProyectoBilpa.greetingService.descartarPendiente(pendientes, txtNombre.getText(), padre.getSesion().getId(), new AsyncCallback<Boolean>() {
				public void onFailure(Throwable caught) {
					error();
				}

				public void onSuccess(Boolean result) {
					success(result);
				}
			});
		}
	}
	
	private boolean nombreEsValido() {
		return txtNombre.getText() != null && !txtNombre.getText().isEmpty();
	}

	private void error() {
		popUp.hide();
		glass.hide();

		ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al descartar pendiente");
		vpu.showPopUp();
	}
	
	private void success(Boolean result) {
		popUp.hide();
		if (result){
			padre.getPendientes();
			glass.hide();
			hide();
		} else {
			ValidadorPopup vpu = new ValidadorPopup(new GlassPopup(), "Info", "No fue posible descartar el pendiente");
			vpu.showPopUp();
		}
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
}
