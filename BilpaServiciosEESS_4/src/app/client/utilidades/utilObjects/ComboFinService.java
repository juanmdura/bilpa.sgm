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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ComboFinService {
	

	/**
	 * ComboFinService es un Widget que tiene doble selector de Fecha y hora.
	 * Valida 2 fechas (inicio y fin)
	 * Autor JR.
	 */


	private ComboFecha comboDia;
	private ComboHora comboHoraInicio;
	private ComboHora comboHoraFin;
	
	private DialogBox contenedor;
	private Button bAceptar;
	private Button bCancelar;
	private Label lNotificacion;
	private HorizontalPanel hInicio;
	private HorizontalPanel hFin;
	private HorizontalPanel hBotones;
	private VerticalPanel vPanelPrincipal;
	private Label lTitulo = new Label("Ingrese fecha y hora de reparación");
	private Label lInicio = new Label("Hora de inicio");
	private Label lFin = new Label("Hora de fin");
	private Orden orden;
	private Persona sesion;
	private PopupCargando popUp = new PopupCargando("Cargando...");
	
	private GlassPopup glass;
	
	public Persona getSesion() {
		return sesion;
	}

	public void setSesion(Persona sesion) {
		this.sesion = sesion;
	}

	public Orden getOrden() {
		return orden;
	}

	public void setOrden(Orden orden) {
		this.orden = orden;
	}

	public ComboFinService(GlassPopup glass, Orden orden, Persona sesion){
		this.glass = glass;
		glass.show();
		inicializar();
		setOrden(orden);
		setSesion(sesion);
		estetica();
		setearPaneles();
		eventosBotones();
	}
	
	
	private void inicializar() {
		comboDia = new ComboFecha();
		contenedor = new DialogBox();
		comboHoraInicio = new ComboHora();
		comboHoraFin = new ComboHora();
		
		bAceptar = new Button("Aceptar");
		bCancelar = new Button("Cancelar");
		hInicio = new HorizontalPanel();
		hFin = new HorizontalPanel();
		hBotones = new HorizontalPanel();
		vPanelPrincipal = new VerticalPanel();
		lNotificacion = new Label("");
	}

	private void estetica() {
		contenedor.setGlassEnabled(true);
		contenedor.setAnimationEnabled(true);
		//contenedor.setSize("100%", "100%");
		//vPanelPrincipal.setSize("490px", "150px");
		
		bAceptar.setWidth("80px");
		bCancelar.setWidth("80px");
		lNotificacion.setStyleName("NegritaRoja");
		lTitulo.setStyleName("Titulo");
		lInicio.setStyleName("Negrita");
		lFin.setStyleName("Negrita");
	}
	
	private void setearPaneles() {
		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		vPanelPrincipal.add(lTitulo);
		hBotones.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hFin.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hInicio.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		hInicio.setSpacing(8);
		hFin.setSpacing(8);
		hBotones.setSpacing(8);
		vPanelPrincipal.setSpacing(15);

		hInicio.add(lInicio);
		hInicio.add(comboHoraInicio.getCombo());
		
		hFin.add(lFin);
		hFin.add(comboHoraFin.getCombo());
		
		hBotones.add(bCancelar);
		hBotones.add(bAceptar);
		
		vPanelPrincipal.add(comboDia.getCombo());
		vPanelPrincipal.add(hInicio);
		vPanelPrincipal.add(hFin);
		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal.add(hBotones);
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
				glass.hide();
			}
		});
	}
	
	private void notificar(String texto) {
		lNotificacion.setText(texto);
	}
	
	@SuppressWarnings("deprecation")
	private void validarFechas() {
		Date inicioService = comboDia.getFecha();
		Date finService = comboDia.getFecha();
		
		inicioService.setHours(comboHoraInicio.getHora());
		inicioService.setMinutes(comboHoraInicio.getMinutos());
		finService.setHours(comboHoraFin.getHora());
		finService.setMinutes(comboHoraFin.getMinutos());
		
		if(finService.before(inicioService)){
			notificar("La hora de fin del service no puede ser menor que la hora de inicio del service");
		}else{
			if(orden.getFechaInicio().after(inicioService)){
				notificar("La fecha del service no puede ser menor que la fecha de inicio de la orden");
			}else{
				orden.setEstadoOrden(5);
				orden.setInicioService(inicioService);
				orden.setFinService(finService);
				orden.setFinServiceReal(new Date());
				guardarOrden();
				notificar("");
				
				contenedor.hide();
				glass.hide();
			}
		}
	}
	
	public Date getFecha(){
		return this.comboDia.getFecha();
	}
	
	protected void guardarOrden() 
	{
		
		String estado = "reparado";
	
		popUp.show();
		ProyectoBilpa.greetingService.actualizarOrden(orden,estado, sesion, new AsyncCallback<Boolean>() {
			public void onSuccess(Boolean result) {
				if (!result){
					popUp.hide();
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar la orden");
					vpu.showPopUp();
				}else{
					popUp.hide();
					irAListaDeOrdenes();
				}
			}
			public void onFailure(Throwable caught) {
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar la orden");
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
