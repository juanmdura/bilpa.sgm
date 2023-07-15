package app.client.iu.listados;

import java.util.ArrayList;
import java.util.Date;

import app.client.ProyectoBilpa;
import app.client.dominio.Estacion;
import app.client.dominio.Orden;
import app.client.dominio.Persona;
import app.client.dominio.Reparacion;
import app.client.dominio.RepuestoLinea;
import app.client.dominio.Solucion;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.UtilOrden;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.VerticalPanel;

@SuppressWarnings("deprecation")
public class IUConsultaRepuestosPorOrden extends Composite{

	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private VerticalPanel panelVtitulo= new VerticalPanel();
	private VerticalPanel panelVSubtitulo= new VerticalPanel();
	private VerticalPanel panelVTabla = new VerticalPanel();
	
	private VerticalPanel panelVDialPrincipal= new VerticalPanel();
	private VerticalPanel panelV2DialPrincipal = new VerticalPanel();
	private VerticalPanel pVDial1= new VerticalPanel();
	private HorizontalPanel pH11 = new HorizontalPanel();
	private VerticalPanel pV12 = new VerticalPanel();
	
	private VerticalPanel pVDial2= new VerticalPanel();
	private HorizontalPanel pH21 = new HorizontalPanel();
	
	private Button btnCerrar = new Button("Cerrar");
	
	private Label lblTituloPrincipal = new Label("Consulta De Activos y Repuestos por Orden");
	private Label lblSubTituloPrincipal = new Label("Seleccione una Orden Finalizada para ver sus datos");
	private Label lblTituloListaActivos = new Label("Lista de Activos Reparados");
	private FlexTable tableFlexListaOrdenes = new FlexTable();
	
	private GlassPopup glass = new GlassPopup();
	
	private Persona sesion;
	private ArrayList<Orden> ordenes = new ArrayList<Orden>();	
	private PopupCargando popUp = new PopupCargando("Cargando...");

	public VerticalPanel getPrincipalPanel() {
		return vPanelPrincipal;
	}

	public IUConsultaRepuestosPorOrden(Persona persona){
		this.sesion = persona;
		setearWidgets();
		agregarWidgets();        
		cargarTablaDatosOrdenesCerradas();
	}

	private void setearWidgets() {
		lblTituloPrincipal.setStyleName("Titulo");
		lblSubTituloPrincipal.setStyleName("SubTitulo");
		lblTituloListaActivos.setStyleName("SubTitulo");
		vPanelPrincipal.setSpacing(10);
		tableFlexListaOrdenes.getRowFormatter().addStyleName(0, "CabezalTabla");
		tableFlexListaOrdenes.setWidth("1200");
		tableFlexListaOrdenes.setCellSpacing(2);
		tableFlexListaOrdenes.setCellPadding(1);

		tableFlexListaOrdenes.addTableListener(new TableListener(){
			
			public void onCellClicked(SourcesTableEvents sender, int row,int cell) {
				if(cell==9){
					abrirOrden(row);                            
				}
			}

		});
		
		panelVDialPrincipal.setWidth("100%");
		panelVDialPrincipal.setSpacing(10);
		pVDial2.setWidth("300");
		
		pVDial1.setSpacing(10);
		pVDial2.setSpacing(10);
	}

	private void agregarWidgets() {
		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal.add(panelVtitulo);
		vPanelPrincipal.add(panelVSubtitulo);
		vPanelPrincipal.add(panelVTabla);
		
		panelVtitulo.add(lblTituloPrincipal);
		panelVSubtitulo.add(lblSubTituloPrincipal);
		panelVTabla.add(tableFlexListaOrdenes);
		
		cargarDialogoOrdenSeleccionada();
	}

	private void cargarDialogoOrdenSeleccionada() {
		panelV2DialPrincipal.add(pVDial1);
		panelV2DialPrincipal.add(pVDial2);
		
		panelVDialPrincipal.add(panelV2DialPrincipal);
		panelVDialPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		panelVDialPrincipal.add(btnCerrar);
		
		
		pH11.add(lblTituloListaActivos);
		pV12.setSpacing(5);
		pVDial1.add(pH11);
		pVDial1.add(pV12);
	}

	private void cargarTablaDatosOrdenesCerradas(){
		popUp.show();
		limpiarTabla();
		ProyectoBilpa.greetingService.obtenerOrdenesCerradas(new AsyncCallback<ArrayList<Orden>>() {

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener todas las ordenes inactivas");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<Orden> result) {
				if (result.size() > 0)
				{
					for (int i=0; i<result.size();i++){
						ordenes.add(result.get(i));
						agregarOrdenATabla(result.get(i), i);
						popUp.hide();
					}
				}
				else
				{
					popUp.hide();
					ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "No existen ordenes cerradas para mostrar en este listado");
					vpu.showPopUp();
				}
				
			}

			private void agregarOrdenATabla(Orden orden, int i) {
				if(i %2==0){
					tableFlexListaOrdenes.getRowFormatter().addStyleName(i+1, "FilaTabla1");				
				}else{
					tableFlexListaOrdenes.getRowFormatter().addStyleName(i+1, "FilaTabla2");
				}
				Label lblNumero = new Label("Numero");
				Label lblEstado = new Label("Estado");
				Label lblTecnico = new Label("Técnico");
				Label lblPrioridad = new Label("Prioridad");
				Label lblFecha = new Label("Fecha");
				Label lblEstacion = new Label("Estación");
				Label lblSerie = new Label("N° de Serie");
				Label lblLocalidad = new Label("Localidad");
				Label lblSello = new Label("Sello");

				lblNumero.setWidth("30");
				lblEstado.setWidth("143");
				lblTecnico.setWidth("121");
				lblPrioridad.setWidth("26");
				lblEstacion.setWidth("130");
				lblSerie.setWidth("101");
				lblLocalidad.setWidth("120");
				lblSello.setWidth("110");

				Button btnAbrir = new Button("Ver");                

				btnAbrir.setWidth("30");

				Estacion estacion = (Estacion)orden.getEmpresa();                

				lblNumero.setText(String.valueOf(orden.getNumero()));
				lblEstado.setText(UtilOrden.getEstadoTexto(orden.getEstadoOrden()));
				lblTecnico.setText (orden.getPersonaAsignada() == null ? UtilOrden.personaAsignadaOrden(1) : orden.getPersonaAsignada().toString());
				lblPrioridad.setText (orden.getPrioridad());
		
				Date fecha = orden.getFechaInicio();
				DateTimeFormat sdf = DateTimeFormat.getFormat("dd/MM/yyyy HH:mm");
				lblFecha.setText (sdf.format(fecha));
				
				lblEstacion.setText (estacion.getNombre());
				lblSerie.setText (String.valueOf(estacion.getNumeroSerie()));
				lblLocalidad.setText (estacion.getLocalidad());
				lblSello.setText (estacion.getSello().getNombre());

				tableFlexListaOrdenes.setWidget(i+1, 0, lblNumero);
				tableFlexListaOrdenes.setWidget(i+1, 1, lblEstado);
				tableFlexListaOrdenes.setWidget(i+1, 2, lblTecnico);
				tableFlexListaOrdenes.setWidget(i+1, 3, lblPrioridad);
				tableFlexListaOrdenes.setWidget(i+1, 4, lblFecha);
				tableFlexListaOrdenes.setWidget(i+1, 5, lblEstacion);
				tableFlexListaOrdenes.setWidget(i+1, 6, lblSerie);
				tableFlexListaOrdenes.setWidget(i+1, 7, lblLocalidad);
				tableFlexListaOrdenes.setWidget(i+1, 8, lblSello);
				tableFlexListaOrdenes.setWidget(i+1, 9, btnAbrir);

			}
		});    
	}

	private void limpiarTabla(){
		tableFlexListaOrdenes.clear();
		tableFlexListaOrdenes.setHTML(0, 0, "Número");
		tableFlexListaOrdenes.setHTML(0, 1, "Estado");
		tableFlexListaOrdenes.setHTML(0, 2, "Técnico");
		tableFlexListaOrdenes.setHTML(0, 3, "Prioridad");
		tableFlexListaOrdenes.setHTML(0, 4, "Fecha y Hora Inicio");
		tableFlexListaOrdenes.setHTML(0, 5, "Estación");
		tableFlexListaOrdenes.setHTML(0, 6, "Número de Serie");
		tableFlexListaOrdenes.setHTML(0, 7, "Localidad");
		tableFlexListaOrdenes.setHTML(0, 8, "Sello");
	}

	private void abrirOrden(int i) {
		
		int numeroOrden = Integer.valueOf(tableFlexListaOrdenes.getText(i, 0));
		buscarOrdenSelleccionada(numeroOrden);
	}

	private void buscarOrdenSelleccionada(int numeroOrden){    
		Orden orden = buscarOrden(numeroOrden);
		if(orden != null){
			DialogBox dialBoxOrdenSeleccionada = verListadoDeOrdenSeleccionada(orden);
			dialBoxOrdenSeleccionada.show();
			dialBoxOrdenSeleccionada.setPopupPosition(220, 200);
		}else{
			ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al seleccionar la orden");
			vpu.showPopUp();
		}
	}
	
	public Orden buscarOrden(int numeroOrden){
		for(Orden o: this.ordenes){
			if(o.getNumero()==numeroOrden){
				return o;
			}
		}
		return null;
	}

	private void cargarTablaActivo(Reparacion rep) {	
		VerticalPanel vpSpace = new VerticalPanel();
		pV12.clear();
		vpSpace.setSize("20", "10");
		Label la = new Label(UtilOrden.tiposDeActivos(rep.getActivo()) + " - " + rep.getActivo().toString());
		la.setStyleName("Negrita");
		FlexTable ft = new FlexTable();
		HTML htmlFalla =  new HTML("Falla Tecnica");
		HTML htmlTarea =  new HTML("Tarea Realizada");
		ft.getRowFormatter().addStyleName(0,"CabezalTabla");

		ft.setHTML(0, 0, htmlFalla.getText());
		ft.setHTML(0, 1, htmlTarea.getText());
		ft.addStyleName("cw-FlexTable");
		ft.setCellSpacing(2);
		ft.setCellPadding(1);
		ft.setWidth("450");

		int i = 1;
		for (Solucion sol : rep.getSoluciones()) {
			Label fTecnica = new Label(sol.getFallaTecnica().getDescripcion());
			Label tarea = new Label(sol.getTarea().getDescripcion());
			ft.setWidget(i,0,fTecnica);
			ft.setWidget(i, 1, tarea);
			if(i %2==0){
				ft.getRowFormatter().addStyleName(i, "FilaTabla1");				
			}else{
				ft.getRowFormatter().addStyleName(i, "FilaTabla2");
			}
			i ++;
		}
		
		if(rep.getSoluciones().size()>0){
			pV12.add(la);
			pV12.add(ft);
			pV12.add(vpSpace);
			
		}
	}



	private void cargarTablaRepuestos(Orden orden){
		if(orden != null){
			Label la = new Label("Repuestos Utilizados");
			la.setStyleName("SubTitulo");
			FlexTable ft = new FlexTable();
			ft.setHTML(0, 0, "Descripcion del Repuesto");
			ft.setHTML(0, 1, "Cantidad");
			ft.getRowFormatter().addStyleName(0, "CabezalTabla");
			ft.setCellSpacing(2);
			ft.setCellPadding(1);
			ft.setWidth("450");
			int i=1;
			for (RepuestoLinea rep : orden.getRepuestosLineas()) {
				Label repuesto = new Label(rep.getRepuesto().getDescripcion());
				Label cantidad = new Label(rep.getCantidad()+"");
				ft.setWidget(i,0,repuesto);
				ft.setWidget(i, 1, cantidad);
				if(i %2==0){
					ft.getRowFormatter().addStyleName(i, "FilaTabla1");				
				}else{
					ft.getRowFormatter().addStyleName(i, "FilaTabla2");
				}
				i++;
			}
			
			pVDial2.clear();
			pVDial2.add(la);
			pVDial2.add(ft);
		}
	}
	
	private DialogBox verListadoDeOrdenSeleccionada(Orden orden){
		final DialogBox dialogoRetorno = new DialogBox();
		dialogoRetorno.add(panelVDialPrincipal);
		
		if(orden != null){

			for (Reparacion rep : orden.getReparaciones()) {
				cargarTablaActivo(rep);
			}
			cargarTablaRepuestos(orden);
		}
		
		btnCerrar.addClickHandler(new ClickHandler(){

			
			public void onClick(ClickEvent event) {
				dialogoRetorno.hide();
				
			}
			
		});
		
		return dialogoRetorno; 
	}

}