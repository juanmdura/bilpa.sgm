package app.client.iu.activos.generico.tipoMarcaModelo.marca;

import app.client.ProyectoBilpa;
import app.client.dominio.MarcaActivoGenerico;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class IUDialogoEliminarMarcaTipoActivoGenerico extends DialogBox{
	private CellTable<MarcaActivoGenerico> tabla = new CellTable<MarcaActivoGenerico>();
	private DialogBox dialogBoxT = new DialogBox();
	private Label lblTituloDialTEliminar;

	private VerticalPanel vpDialT = new VerticalPanel();
	private HorizontalPanel hpDialT = new HorizontalPanel();
	private HorizontalPanel hp2DialT = new HorizontalPanel();
	
	private IUGestionarMarcaTipoActivoGenerico padre;
	
	private GlassPopup glass;
	private PopupCargando popUp;
	private MarcaActivoGenerico seleccionado;
	
	public IUDialogoEliminarMarcaTipoActivoGenerico(boolean autoHide, IUGestionarMarcaTipoActivoGenerico padre, GlassPopup glass, PopupCargando popUp, final MarcaActivoGenerico tipoActivoGenericoSeleccionado) {
		super(autoHide);
		this.glass = glass;
		this.popUp = popUp;
		this.padre = padre;
		this.glass.show();
		this.seleccionado = tipoActivoGenericoSeleccionado;
		lblTituloDialTEliminar  = new Label("Seguro desea eliminar la marca " + tipoActivoGenericoSeleccionado.getNombre() + "?");
	}

	private Button btnElimDial = new Button("Eliminar", new ClickHandler() {
		public void onClick(ClickEvent event) {
			dialogBoxT.hide(true);
			glass.hide();
			eliminar(seleccionado);
		}
	});

	private Button btnCancelDial = new Button("Cancelar", new ClickHandler() {
		public void onClick(ClickEvent event) {
			dialogBoxT.hide(true);
			glass.hide();
		}
	});

	public DialogBox dialElimTipoActivoGenerico() {
		vpDialT.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hpDialT.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		vpDialT.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		vpDialT.add(lblTituloDialTEliminar);

		dialogBoxT.setAutoHideEnabled(false);

		btnElimDial.setWidth("100px");
		btnCancelDial.setWidth("100px");

		tabla.setWidth("100%");

		hp2DialT.add(btnCancelDial);
		hp2DialT.setSpacing(5);
		hp2DialT.add(btnElimDial);

		vpDialT.add(hpDialT);
		vpDialT.add(hp2DialT);

		vpDialT.setSize("400px", "100px");
		dialogBoxT.setWidget(vpDialT);
		return dialogBoxT;
	}

	private void eliminar(MarcaActivoGenerico object) {
		popUp.show();
		ProyectoBilpa.greetingService.eliminarMarcaActivoGenerico(object,new AsyncCallback<Boolean>() {
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

		ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al eliminar tipo de activo");
		vpu.showPopUp();
	}
	
	private void success(Boolean result) {
		if (result){
			padre.obtenerLista();
			popUp.hide();
			glass.hide();
			hide();
		} else {
			ValidadorPopup vpu = new ValidadorPopup(glass,"Info", "No se puede eliminar una tipo de activo que tiene activos asociados");
			vpu.showPopUp();
		}
	}
}
