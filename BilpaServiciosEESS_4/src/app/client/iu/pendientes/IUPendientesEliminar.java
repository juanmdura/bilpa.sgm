package app.client.iu.pendientes;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
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

import app.client.ProyectoBilpa;
import app.client.dominio.data.PendienteData;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;

public class IUPendientesEliminar extends DialogBox{
	private VerticalPanel vPanelDial = new VerticalPanel();
	private HorizontalPanel hPanelBotonera = new HorizontalPanel();
	private DecoratorPanel decorator = new DecoratorPanel();
	private Label lblTitulo = new Label("Seguro desea eliminar el pendiente?");
	private Label lblTexto;

	private GlassPopup glass;
	private PopupCargando popUp;
	private IUPendientes padre;
	private PendienteData seleccionado;

	public IUPendientesEliminar(boolean autoHide, IUPendientes padre, GlassPopup glass, PopupCargando popUp, final PendienteData seleccionado) {
		super(autoHide);
		this.glass = glass;
		this.popUp = popUp;
		this.padre = padre;
		this.glass.show();
		this.seleccionado = seleccionado;

		lblTexto = new Label(seleccionado.getComentario());
		
		set();
		cargarPanelesConWidgets();
		color();
		cargar();
	}

	private void set() {
		setWidth("100%");
		vPanelDial.setWidth("100%");
		decorator.setWidth("100%");
		lblTexto.setWidth("100%");
	}

	private Button btnAceptarModif = new Button("Aceptar", new ClickHandler() {
		public void onClick(ClickEvent event) {
			eliminar();
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

		btnAceptarModif.setWidth("100px");
		btnCancelModif.setWidth("100px");

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
		vPanelDial.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelDial.add(lblTitulo);
		vPanelDial.add(lblTexto);
		vPanelDial.add(hPanelBotonera);
	}

	private void eliminar() {
		popUp.show();

		ProyectoBilpa.greetingService.eliminarPendiente(seleccionado.getId(), new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
				error();
			}

			public void onSuccess(Boolean result) {
				success(result);
			}
		});
	}

	private void error() {
		popUp.hide();
		glass.hide();

		ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al eliminar pendiente");
		vpu.showPopUp();
	}

	private void success(Boolean result) {
		popUp.hide();
		if (result){
			padre.getPendientes();
			glass.hide();
			hide();
		} else {
			ValidadorPopup vpu = new ValidadorPopup(new GlassPopup(), "Info", "No fue posible eliminar el pendiente");
			vpu.showPopUp();
		}
	}
}
