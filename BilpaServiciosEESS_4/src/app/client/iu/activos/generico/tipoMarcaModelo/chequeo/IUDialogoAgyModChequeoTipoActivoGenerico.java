package app.client.iu.activos.generico.tipoMarcaModelo.chequeo;

import app.client.ProyectoBilpa;
import app.client.dominio.data.ItemChequeoData;
import app.client.dominio.data.TipoActivoGenericoData;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;

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

public class IUDialogoAgyModChequeoTipoActivoGenerico extends DialogBox{
	private VerticalPanel vPanelDialNuevaVisita = new VerticalPanel();
	private HorizontalPanel hPanelBotonera = new HorizontalPanel();
	private DecoratorPanel decorator = new DecoratorPanel();
	private Label lblTitulo = new Label();
	private Label fechaLabel = new Label("Nombre ");
	private TextBox txtNombre = new TextBox();

	private GlassPopup glass;
	private PopupCargando popUp;
	private IUGestionarChequeoTipoActivoGenerico padre;
	private ItemChequeoData seleccionado;

	public IUDialogoAgyModChequeoTipoActivoGenerico(boolean autoHide, IUGestionarChequeoTipoActivoGenerico padre, GlassPopup glass, PopupCargando popUp, final ItemChequeoData seleccionado) {
		super(autoHide);
		this.glass = glass;
		this.popUp = popUp;
		this.padre = padre;
		this.glass.show();
		this.seleccionado = seleccionado;
		
		if (seleccionado != null){
			txtNombre.setText(seleccionado.getTexto());
		}
		
		txtNombre.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (KeyCodes.KEY_ENTER == event.getNativeEvent().getKeyCode())
				{
					submit();
				}
			}
		});
		cargarPanelesConWidgets();
		color();
		cargarGrilla();
	}

	private void submit() {
		if (seleccionado == null){
			agregar();
		} else {
			editar();
		}
	}
	
	private Button btnAceptarModif = new Button("Aceptar", new ClickHandler() {
		public void onClick(ClickEvent event) {
			submit();
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
		fechaLabel.setStyleName("Negrita");

		btnAceptarModif.setWidth("100px");
		btnCancelModif.setWidth("100px");
		
		txtNombre.setWidth("300px");
	}

	private void cargarPanelesConWidgets() {
		vPanelDialNuevaVisita.setSpacing(10);
		vPanelDialNuevaVisita.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hPanelBotonera.setSpacing(10);
		hPanelBotonera.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hPanelBotonera.add(btnCancelModif);
		hPanelBotonera.add(btnAceptarModif);
		vPanelDialNuevaVisita.add(decorator);
		add(vPanelDialNuevaVisita);
	}

	private void cargarGrilla() {
		vPanelDialNuevaVisita.setSize("350px", "90px");
		vPanelDialNuevaVisita.add(lblTitulo);
		HorizontalPanel hp1 = new HorizontalPanel();
		VerticalPanel vp1 = new VerticalPanel();
		VerticalPanel vp2 = new VerticalPanel();

		vPanelDialNuevaVisita.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		vp1.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		vp2.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		vp1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		vp2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

		vp1.setSpacing(10);
		vp2.setSpacing(10);

		vp1.add(fechaLabel);
		vp2.add(txtNombre);

		vPanelDialNuevaVisita.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

		hp1.add(vp1);
		hp1.add(vp2);
		vPanelDialNuevaVisita.add(hp1);

		vPanelDialNuevaVisita.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelDialNuevaVisita.add(hPanelBotonera);
		
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	            txtNombre.setFocus(true);
	        }
	    });
	}

	private void agregar() {
		if (nombreEsValido()){
			popUp.show();
			ItemChequeoData item = new ItemChequeoData();
			item.setTexto(txtNombre.getText());
			item.setNombre(txtNombre.getText().trim().replaceAll("[^a-zA-Z0-9]+",""));
			item.setTipo("GENERICO");
			item.setTipoActivoGenerico(new TipoActivoGenericoData(padre.getTipoActivoSeleccionado().getId()));

			ProyectoBilpa.greetingService.agregarItemChequeo(item, new AsyncCallback<Boolean>() {
				public void onFailure(Throwable caught) {
					error();
				}

				public void onSuccess(Boolean result) {
					success(result);
				}
			});
		}
	}
	
	private void editar() {
		if (nombreEsValido()){
			popUp.show();
			seleccionado.setTexto(txtNombre.getText());
			seleccionado.setNombre(txtNombre.getText().trim().replaceAll("[^a-zA-Z0-9]+",""));

			ProyectoBilpa.greetingService.modificarItemChequeo(seleccionado, new AsyncCallback<Boolean>() {
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

		ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar nueva visita");
		vpu.showPopUp();
	}
	
	private void success(Boolean result) {
		popUp.hide();
		if (result){
			padre.obtenerLista();
			glass.hide();
			padre.setSeleccionado(null);
			hide();
		} else {
			ValidadorPopup vpu = new ValidadorPopup(new GlassPopup(), "Info", "Ya existe un chequeo con el nombre: " + txtNombre.getText());
			vpu.showPopUp();
		}
	}
}
