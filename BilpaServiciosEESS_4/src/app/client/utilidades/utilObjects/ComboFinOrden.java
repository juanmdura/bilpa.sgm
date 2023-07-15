package app.client.utilidades.utilObjects;

import java.util.Date;

import app.client.ProyectoBilpa;
import app.client.dominio.Orden;
import app.client.dominio.Persona;
import app.client.iu.menu.IUMenuPrincipal;
import app.client.iu.menu.IUMenuPrincipalTecnico;
import app.client.iu.orden.IUListaOrdenes;
import app.client.iu.widgets.ValidadorPopup;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ComboFinOrden {
	
	private ComboFechaHora comboFecha;
	private DialogBox contenedor;
	private Button bAceptar;
	private Button bCancelar;
	private HorizontalPanel hPanel;
	private VerticalPanel vPanelPrincipal;
	private Label lNotificacion;
	private Orden orden;
	private Date finOrden;
	private Persona sesion;
	private PopupCargando popUp = new PopupCargando("Cargando...");
	
	private GlassPopup glass = new GlassPopup();

	public Date getFinOrden() {
		return finOrden;
	}

	public ComboFinOrden(Orden orden, Persona sesion){
		inicializar();
		estetica();
		setearPaneles();
		eventosBotones();
		this.orden = orden;
		this.sesion = sesion;
	}

	private void inicializar() {
		comboFecha = null;
		contenedor = new DialogBox();
		bAceptar = new Button("Aceptar");
		bCancelar = new Button("Cancelar");
		hPanel = new HorizontalPanel();
		vPanelPrincipal = new VerticalPanel();
		lNotificacion = new Label("");
	}

	private void estetica() {
		contenedor.setGlassEnabled(true);
		contenedor.setAnimationEnabled(true);
		//contenedor.setText("Finalización de Orden de Trabajo");
		contenedor.setText("Ingrese la fecha y la hora en la cual finalizo la orden de trabajo");
		contenedor.setTitle("Ingrese la fecha y la hora en la cual finalizo la orden de trabajo");
		bAceptar.setWidth("80px");
		bCancelar.setWidth("80px");
		lNotificacion.setStyleName("NegritaRoja");
	}

	private void setearPaneles() {
		vPanelPrincipal.add(comboFecha.getCombo());
		hPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hPanel.add(bAceptar);
		hPanel.add(bCancelar);
		hPanel.setSpacing(8);
		vPanelPrincipal.setSpacing(8);
		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal.add(hPanel);
		vPanelPrincipal.add(lNotificacion);
		contenedor.add(vPanelPrincipal);
	}
	
	public DialogBox getCombo(){
		return contenedor;
	}
	
	private void eventosBotones() {
		bAceptar.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				validarFechas();
			}
		});
		
		bCancelar.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				notificar("");
				contenedor.hide();
			}
		});
	}

	private void validarFechas() {
		Date finOrdenSeleccionada = comboFecha.getFecha();
		if(finOrdenSeleccionada.before(orden.getFechaInicio())){
			notificar("La fecha y hora de fin no puede ser menor que la fecha de inicio de la orden");
			//notificar("La fecha de finalización debe ser mayor a la de inicio de la orden");
		}else{
			orden.setFechaFin(finOrdenSeleccionada);
			notificar("");
			contenedor.hide();
			cerrarOrden(orden, sesion);
		}
	}

	private void notificar(String texto) {
		lNotificacion.setText(texto);
	}
	
	private void cerrarOrden(Orden orden, Persona sesion){
		popUp.show();
		ProyectoBilpa.greetingService.cerrarOrden(orden, sesion, new AsyncCallback<Boolean>() {

			public void onSuccess(Boolean result) {
				if (!result){
					popUp.hide();
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al finalizar la orden");
					vpu.showPopUp();
				}else{
					popUp.hide();
					irAListaDeOrdenes();
				}
			}
			public void onFailure(Throwable caught) {
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al finalizar la orden");
				vpu.showPopUp();
			}
		});	
	}
	
	private void irAListaDeOrdenes() {
		if (sesion.getRol() == 5 || sesion.getRol() == 1)
		{
			IUListaOrdenes iu = new IUListaOrdenes(sesion);
			IUMenuPrincipal.getInstancia().agregarWidgetAlMenu(iu.getPrincipalPanel());
		}
		else if (sesion.getRol() == 3)
		{
			IUListaOrdenes iu = new IUListaOrdenes(sesion);
			IUMenuPrincipalTecnico.getInstancia().agregarWidgetAlMenu(iu.getPrincipalPanel());					
		}
	}
	
}
