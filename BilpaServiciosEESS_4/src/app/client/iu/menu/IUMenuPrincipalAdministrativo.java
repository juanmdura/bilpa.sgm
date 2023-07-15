package app.client.iu.menu;

import app.client.ProyectoBilpa;
import app.client.dominio.Estacion;
import app.client.dominio.Persona;
import app.client.dominio.Tecnico;
import app.client.iu.IULogin;
import app.client.iu.grafica.IUGraficaRepuestosMasUsados;
import app.client.iu.listados.IUConsultaHistoricoOrdenesPorTecnico;
import app.client.iu.listados.IUConsultaRepuestosPorOrden;
import app.client.iu.orden.IUIngresoOrden;
import app.client.iu.orden.IUListaOrdenes;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.UtilUI;
import app.client.utilidades.utilObjects.GlassPopup;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class IUMenuPrincipalAdministrativo extends Composite {
	
	private MenuBar menuBar = new MenuBar(false);
	private VerticalPanel vBig = new VerticalPanel();
	//private DecoratorPanel hNorte = new DecoratorPanel();
	//private DecoratorPanel hSur = new DecoratorPanel();
	private HorizontalPanel panelHorizontal = new HorizontalPanel();
	private VerticalPanel panelVerticalPrincipal = new VerticalPanel();
	private int contadorDeWidgets = 0;
	private Persona sesion;
	private Label lblLogOut = new Label("Cerrar Sesión"); 
	private HorizontalPanel phLogOut = new HorizontalPanel();
	
	private GlassPopup glass = new GlassPopup();
	
	public Persona getSesion() {
		return sesion;
	}

	public void setSesion(Persona sesion) {
		this.sesion = sesion;
		cargarListaOrdenes();//Por defecto se muestra la lista de ordenes del tecnico. Poner esta linea en otro lado!!!
	}

	private static IUMenuPrincipalAdministrativo instancia = null;
	
	public static IUMenuPrincipalAdministrativo getInstancia() {
	
			if(instancia == null){
				instancia = new IUMenuPrincipalAdministrativo();
				
			}
			return instancia;
	}
	
	public IUMenuPrincipalAdministrativo(){	
		this.instancia = this;
		this.cargarMenuOrdenes(); 
		this.cargarMenuConsultas();
		this.cargarMenuGraficos(); 

		this.lblLogOut.setStyleName("Subrayado");
		lblLogOut.addMouseOverHandler(new MouseOverHandler(){
			public void onMouseOver(MouseOverEvent event) {
				lblLogOut.setStyleName("MouseOver");
			}			
		});

		lblLogOut.addMouseOutHandler(new MouseOutHandler(){
			public void onMouseOut(MouseOutEvent event) {
				lblLogOut.removeStyleName("MouseOver");
				lblLogOut.setStyleName("Subrayado");

			}						
		});
		
		this.panelHorizontal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panelVerticalPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		this.panelVerticalPrincipal.add(this.phLogOut);
		//this.phLogOut.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		panelVerticalPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		this.phLogOut.add(this.lblLogOut);
		
		this.panelHorizontal.add(menuBar);
		this.panelVerticalPrincipal.add(panelHorizontal);
		this.panelVerticalPrincipal.setSpacing(5);
		
		this.menuBar.setWidth("1200px");
		this.menuBar.setHeight("35px");
		this.menuBar.setWidth("100%");
//		RootPanel.get().setWidgetPosition((Widget)RootPanel.get().getWidget(1),-1,-1);
		//this.menuBar.setStyleName("Body");
		color();
		//vBig.add(hNorte);
		vBig.add(panelVerticalPrincipal);
		//vBig.add(hSur);
		

		lblLogOut.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				logOut();
			}
		});
		
		initWidget(vBig);
	}
	
	public void color(){
		vBig.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
//		menuBar.setStyleName("FondoPrincipal");
		//hNorte.setStyleName("Cabezal");
		//hSur.setStyleName("Cabezal");
		
		vBig.setWidth("100%");
		//hNorte.setSize("100%", "110px");
		//hSur.setSize("100%", "140px");
		panelVerticalPrincipal.setWidth("100%");
		panelHorizontal.setWidth("100%");
	}
	
	public void agregarWidgetAlMenu(Widget widget){
		validarSiHayWidgetActivo();
		panelVerticalPrincipal.add(widget);
	}
	
	private void cargarListaOrdenes() {
		validarSiHayWidgetActivo();
		IUListaOrdenes iuListaOrdenes = new IUListaOrdenes(sesion);
		panelVerticalPrincipal.add(iuListaOrdenes.getPrincipalPanel());
	}
	
	public void cargarMenuOrdenes(){
		MenuBar menuBarOrden = new MenuBar(true);
		MenuItem menuItemOrden = new MenuItem("Ordenes", false, menuBarOrden);

		MenuItem menuItemNuevaOrden = new MenuItem("Nuevo Correctivo", false, new Command() {
			public void execute() {
				validarSiHayWidgetActivo();
				IUIngresoOrden iur = new IUIngresoOrden(sesion);
				panelVerticalPrincipal.add(iur.getPrincipalPanel());
			}
		});
		menuBarOrden.addItem(menuItemNuevaOrden);
		
		MenuItem menuItemSeguirOrden = new MenuItem("Lista de órdenes", false, new Command() {
			public void execute() {
				validarSiHayWidgetActivo();
				IUListaOrdenes iuListaOrdenes = new IUListaOrdenes(sesion);
				panelVerticalPrincipal.add(iuListaOrdenes.getPrincipalPanel());
			}
		});
		
		menuBarOrden.addItem(menuItemSeguirOrden);
		
		menuItemOrden.setWidth("200");
		menuItemOrden.setHeight("30");
		
		menuBar.addItem(menuItemOrden);
		
	}
	
	
	public void cargarMenuEstadisticas(){
		MenuBar menuBarConsultas = new MenuBar(true);
		MenuItem menuItemConsultas = new MenuItem("Estadisticas", false, menuBarConsultas);

		MenuItem menuItemConsultaDatosFactura = new MenuItem("Listado de Datos para Facturar", false, new Command() {
			public void execute() {
				validarSiHayWidgetActivo();
				IUConsultaRepuestosPorOrden iur = new IUConsultaRepuestosPorOrden(sesion);
				panelVerticalPrincipal.add(iur.getPrincipalPanel());
			}
		});
		menuBarConsultas.addItem(menuItemConsultaDatosFactura);
		
		MenuItem menuItemConsultaHistoricoFallas = new MenuItem("Historico de órdenes por Tecnico", false, new Command(){
			public void execute() {
				validarSiHayWidgetActivo();
				IUConsultaHistoricoOrdenesPorTecnico iur = new IUConsultaHistoricoOrdenesPorTecnico(sesion);
				panelVerticalPrincipal.add(iur.getPrincipalPanel());
			}
		});
		
		menuBarConsultas.addItem(menuItemConsultaHistoricoFallas);
		
		MenuItem menuItemConsultaRepuestosMasUSados = new MenuItem("Grafico de Repuestos Mas Usados", false, new Command(){
			public void execute() {
				validarSiHayWidgetActivo();
				IUGraficaRepuestosMasUsados iur = new IUGraficaRepuestosMasUsados(sesion);
				panelVerticalPrincipal.add(iur.getPrincipalPanel());
			}
		});
		
		menuBarConsultas.addItem(menuItemConsultaRepuestosMasUSados);
		
		MenuItem menuItemConsultaTiemposDeRepuesta = new MenuItem("Tiempos de Respuesta", false, new Command(){
			public void execute() {
				validarSiHayWidgetActivo();	
			}
		});
		
//		menuBarConsultas.addItem(menuItemConsultaTiemposDeRepuesta);
		
		menuItemConsultas.setWidth("200");
		menuBar.addItem(menuItemConsultas);
	}
	
	public void cargarMenuConsultas(){
		MenuBar menuBarConsultas = new MenuBar(true);
		MenuItem menuItemConsultas = new MenuItem("Consultas", false, menuBarConsultas);

		MenuItem menuItemConsultaDatosFactura = new MenuItem("Consulta De Activos y Repuestos por Orden", false, new Command() {
			public void execute() {
				validarSiHayWidgetActivo();
				IUConsultaRepuestosPorOrden iur = new IUConsultaRepuestosPorOrden(sesion);
				panelVerticalPrincipal.add(iur.getPrincipalPanel());
			}
		});
		menuBarConsultas.addItem(menuItemConsultaDatosFactura);
		
		MenuItem menuItemConsultaHistoricoFallas = new MenuItem("Historico de órdenes por Técnico", false, new Command(){
			public void execute() {
				validarSiHayWidgetActivo();
				IUConsultaHistoricoOrdenesPorTecnico iur = new IUConsultaHistoricoOrdenesPorTecnico(sesion);
				panelVerticalPrincipal.add(iur.getPrincipalPanel());
			}
		});
		
		menuBarConsultas.addItem(menuItemConsultaHistoricoFallas);
		
		MenuItem menuItemConsultaRepuestosMasUSados = new MenuItem("Repuestos mas Usados", false, new Command(){
			public void execute() {
				validarSiHayWidgetActivo();
				IUGraficaRepuestosMasUsados iur = new IUGraficaRepuestosMasUsados(sesion);
				panelVerticalPrincipal.add(iur.getPrincipalPanel());
			}
		});
		
//		menuBarConsultas.addItem(menuItemConsultaRepuestosMasUSados);
		
		MenuItem menuItemConsultaTiemposDeRepuesta = new MenuItem("Tiempos de Respuesta", false, new Command(){
			public void execute() {
				ProyectoBilpa.greetingService.buscarEstacion(1, new AsyncCallback<Estacion>(){

					
					public void onFailure(Throwable caught) {
						ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar tiempos de respuesta: " + caught.getMessage());
						vpu.showPopUp();
					}

					
					public void onSuccess(Estacion result) {
						if(result != null){
							//DialogoGestionActivos dialog = new DialogoGestionActivos(result, null);
							//panelVerticalPrincipal.add(dialog.cargarDialogoActivos());
							validarSiHayWidgetActivo();
						}
						
					}
					
				});
				
			}
		});
		
//		menuBarConsultas.addItem(menuItemConsultaTiemposDeRepuesta);
		
		menuItemConsultas.setWidth("200");
		menuBar.addItem(menuItemConsultas);
	}
	
	public void cargarMenuGraficos(){
		MenuBar menuBarGraficos = new MenuBar(true);
		MenuItem menuItemGrafico = new MenuItem("Graficas", false, menuBarGraficos);

		MenuItem menuItemGraficaRepuestos = new MenuItem("Grafico de Repuestos Mas Usados", false, new Command() {
			public void execute() {
				validarSiHayWidgetActivo();
				IUGraficaRepuestosMasUsados iur = new IUGraficaRepuestosMasUsados(sesion);
				panelVerticalPrincipal.add(iur.getPrincipalPanel());
			}
		});
		menuBarGraficos.addItem(menuItemGraficaRepuestos);
		
		MenuItem menuItemGraficoVisitas = new MenuItem("Visitas a un Cliente", false, new Command(){
			public void execute() {
				validarSiHayWidgetActivo();
				
			}
		});
		
		menuBarGraficos.addItem(menuItemGraficoVisitas);
		
		menuItemGrafico.setWidth("200");
//		menuBar.addItem(menuItemGrafico);
	}
	
	
	public void validarSiHayWidgetActivo(){
		if(this.contadorDeWidgets == 1){
			panelVerticalPrincipal.remove(2);
		}
		if(this.contadorDeWidgets == 0){
			contadorDeWidgets += 1;
		}
	}
	
	public void cargarGraficaPruebaConMuchaFruta(){
		
	}
	
	public void buscarTecnico(int id){
		ProyectoBilpa.greetingService.validarUsuario("jdura", "password", new AsyncCallback<Persona>() {

			
			
			public void onSuccess(Persona result) {
				if (result == null){
					DialogBox loginError = UtilUI.dialogBoxError("Usuario y/o Password Incorrecto!!");
					loginError.show();
					loginError.center();
				}else{
					
						Tecnico tecnico = (Tecnico) result;
						
			
					
				}
				
			}

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al validar usuario: " + caught.getMessage());
				vpu.showPopUp();
			}
		});
	}
	
	private void logOut(){
		this.sesion = null;
		IULogin iu = new IULogin();

		ProyectoBilpa.getRoot().clear();
		ProyectoBilpa.getRoot().add(iu);
	}

}
