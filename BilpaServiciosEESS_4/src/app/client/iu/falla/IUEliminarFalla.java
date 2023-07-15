package app.client.iu.falla;

import java.util.ArrayList;

import app.client.ProyectoBilpa;
import app.client.dominio.FallaReportada;
import app.client.dominio.FallaTecnica;
import app.client.dominio.Persona;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.UtilOrden;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class IUEliminarFalla extends IUWidgetFalla {
	private HTML htmlTitulo = new HTML("Eliminar fallas");

	private Persona sesion;

	private Button btnPrincEliminarT = new Button("Eliminar");	
	private Button btnPrincEliminarR = new Button("Eliminar");

	private DecoratedTabPanel tabPTipos = new DecoratedTabPanel();
	private VerticalPanel vpPrincipal = new VerticalPanel();

	//=====================================================================	
	//Pop Up Fallas Tecnicas
	private DialogBox dialogBoxT = new DialogBox();
	private Label lblTituloDialT = new Label("Seguro desea eliminar esta falla?");
	private HTML htmlDescDialT = new HTML();
	private VerticalPanel vpDialT = new VerticalPanel();
	private HorizontalPanel hpDialT = new HorizontalPanel();
	private HorizontalPanel hp2DialT = new HorizontalPanel();
	//=====================================================================
	//=====================================================================	
	//Pop Up Fallas Reclamadas	
	private DialogBox dialogBoxR = new DialogBox();		
	HTML htmlDescDialR = new HTML();
	private Label lblTituloDialR = new Label("Seguro desea eliminar esta falla?");
	VerticalPanel vpDialR = new VerticalPanel();
	HorizontalPanel hpDialR = new HorizontalPanel();
	HorizontalPanel hp2DialR = new HorizontalPanel();
	//=====================================================================

	public VerticalPanel getVpPrincipal(){
		return this.vpPrincipal;
	}

	HorizontalPanel hplblFallasT = new HorizontalPanel();
	private HorizontalPanel hpFallasT1 = new HorizontalPanel();
	private VerticalPanel vpFallasT2 = new VerticalPanel();
	private VerticalPanel vpFallasT3 = new VerticalPanel();
	private VerticalPanel vpFallasT1 = new VerticalPanel();

	HorizontalPanel hplblFallasR = new HorizontalPanel();
	private HorizontalPanel hpFallasR1 = new HorizontalPanel();
	private VerticalPanel vpFallasR2 = new VerticalPanel();
	private VerticalPanel vpFallasR3 = new VerticalPanel();
	private VerticalPanel vpFallasR1 = new VerticalPanel();

	private PopupCargando popUp = new PopupCargando("Cargando...");
	private GlassPopup glass = new GlassPopup();

	private Button btnElimDialT = new Button("Eliminar", new ClickHandler() {
		public void onClick(ClickEvent event) {
			removerFallaSeleccionadaT();
			dialogBoxT.hide(true);
			glass.hide();
		}
	});

	private Button btnCancelDialT = new Button("Cancelar", new ClickHandler() {
		public void onClick(ClickEvent event) {
			dialogBoxT.hide(true);
			glass.hide();
		}
	});

	private Button btnElimDialR = new Button("Eliminar", new ClickHandler() {
		public void onClick(ClickEvent event) {
			removerFallaSeleccionadaR();
			dialogBoxR.hide(true);
			glass.hide();
		}
	});

	private Button btnCancelDialR = new Button("Cancelar", new ClickHandler() {
		public void onClick(ClickEvent event) {
			dialogBoxR.hide(true);
			glass.hide();
		}
	});

	//CONSTRUCTOR
	public IUEliminarFalla(Persona persona) {
		this.sesion = persona;
		//Layout	
		setearWidgets();

		//Detectadas
		cargarTabT();
		cargarLtBoxTipoFallaT(popUp, listBoxTiposFallasT);

		//Reportadas		
		cargarTabR();
		cargarLtBoxTipoFallaR(popUp, listBoxTiposFallasR);

		color();
		tabPTipos.selectTab(0);
		vpPrincipal.add(htmlTitulo);
		vpPrincipal.add(tabPTipos);
	}

	private void color() {
		htmlTitulo.setStyleName("Titulo");
		lblTituloDialT.setStyleName("Subtitulo");
		lblTituloDialR.setStyleName("Subtitulo");
	}
	
	public void EliminarFallaT(){
		if (this.listBoxFallasT.getSelectedIndex() != -1){
			glass.show();
			String fallaSeleccionada = this.listBoxFallasT.getItemText(this.listBoxFallasT.getSelectedIndex());
			DialogBox db = this.dialElimFallaT(fallaSeleccionada);
			db.center();
			db.show();
		}
	}

	public void EliminarFallaR(){
		if (listBoxFallasR.getSelectedIndex() != -1){
			int id = Integer.valueOf(listBoxFallasR.getValue(this.listBoxFallasR.getSelectedIndex()));
			if (id == UtilOrden.idFallaPendientes){
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "No es posible eliminar la falla: ELIMINACIÓN DE PENDIENTE");
				vpu.showPopUp();
			}
			
			glass.show();
			String fallaSeleccionada = listBoxFallasR.getItemText(listBoxFallasR.getSelectedIndex());
			DialogBox db = this.dialElimFallaR(fallaSeleccionada);
			db.center();
			db.show();
		}
	}

	public DialogBox dialElimFallaT(String texto){

		vpDialT.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hpDialT.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		vpDialT.add(lblTituloDialT);

		btnElimDialT.setWidth("100px");
		btnCancelDialT.setWidth("100px");

		htmlDescDialT.setWidth("500px");

		htmlDescDialT.setText(texto);

		hpDialT.setSpacing(5);
		hpDialT.add(htmlDescDialT);

		hp2DialT.add(btnCancelDialT);
		hp2DialT.setSpacing(5);		
		hp2DialT.add(btnElimDialT);

		vpDialT.add(hpDialT);
		vpDialT.add(hp2DialT);

		dialogBoxT.setWidget(vpDialT);

		return dialogBoxT;
	}

	public void removerFallaSeleccionadaT(){
	
		int id = Integer.valueOf(this.listBoxFallasT.getValue(this.listBoxFallasT.getSelectedIndex()));
		ProyectoBilpa.greetingService.buscarFallaT(id, new AsyncCallback<FallaTecnica>() {
			public void onSuccess(FallaTecnica result) {
				if (result != null){
					ProyectoBilpa.greetingService.eliminarFallaT(result, new AsyncCallback<Boolean>() {
						public void onFailure(Throwable caught) {
							popUp.hide();
							ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al remover fallas");
							vpu.showPopUp();
						}
						public void onSuccess(Boolean result) {
							cargarLtBoxTipoFallaT(popUp, listBoxTiposFallasT);
						}
					});
				}				
			}
			public void onFailure(Throwable caught) {
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al remover fallas");
				vpu.showPopUp();
			}
		});
	}

	public DialogBox dialElimFallaR(String texto){
		vpDialR.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hpDialR.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		vpDialR.add(lblTituloDialR);

		btnElimDialR.setWidth("100px");
		btnCancelDialR.setWidth("100px");

		htmlDescDialR.setWidth("500px");

		htmlDescDialR.setText(texto);

		hpDialR.setSpacing(5);
		hpDialR.add(htmlDescDialR);

		hp2DialR.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hp2DialR.add(btnCancelDialR);
		hp2DialR.setSpacing(5);		
		hp2DialR.add(btnElimDialR);

		vpDialR.add(hpDialR);
		vpDialR.add(hp2DialR);

		dialogBoxR.setWidget(vpDialR);

		return dialogBoxR;
	}

	public void removerFallaSeleccionadaR(){
		int id = Integer.valueOf(this.listBoxFallasR.getValue(this.listBoxFallasR.getSelectedIndex()));
		ProyectoBilpa.greetingService.buscarFallaReportada(id, new AsyncCallback<FallaReportada>() {
			public void onSuccess(FallaReportada result) {
				if (result != null){
					
					ProyectoBilpa.greetingService.eliminarFallaR(result, new AsyncCallback<Boolean>() {
						public void onFailure(Throwable caught) {
							popUp.hide();
							ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al remover fallas");
							vpu.showPopUp();
						}
						public void onSuccess(Boolean result) 
						{
							cargarLtBoxTipoFallaR(popUp, listBoxTiposFallasR);
						}
					});
				}				
			}
			
			public void onFailure(Throwable caught) {
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al remover fallas");
				vpu.showPopUp();
			}
		});
		
	}

	public void setearWidgets(){

		htmlTitulo.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		tabPTipos.setWidth("600px");
		tabPTipos.setAnimationEnabled(true);

		lblTiposFallasT.setStyleName("Negrita");
		lblFallasT.setStyleName("Negrita");
		
		lblTiposFallasR.setStyleName("Negrita");
		lblFallasR.setStyleName("Negrita");
		
		listBoxTiposFallasT.setWidth("500px");
		listBoxTiposFallasR.setWidth("500px");
		
		listBoxFallasT.setWidth("500px");
		listBoxFallasR.setWidth("500px");
		
		listBoxFallasT.setTitle("Lista de Fallas Detectadas");
		listBoxFallasR.setTitle("Lista de Fallas Reportadas");
		
		btnPrincEliminarT.setTitle("Seleccione una Falla Detectada y presione Eliminar para borrarla del sistema");
		btnPrincEliminarR.setTitle("Seleccione una Falla Reportada y presione Eliminar para borrarla del sistema");
		
		listBoxFallasT.setVisibleItemCount(10);
		listBoxFallasR.setVisibleItemCount(10);

		btnPrincEliminarT.setWidth("100px");
		btnPrincEliminarR.setWidth("100px");
	}

	public void cargarTabT(){
		tabPTipos.add(vpFallasT1, "Fallas Detectadas");

		vpFallasT1.setSpacing(10);
		
		vpFallasT3.add(lblTiposFallasT);
		vpFallasT3.add(listBoxTiposFallasT);

		hpFallasT1.add(listBoxFallasT);
		HorizontalPanel hpSpace = new HorizontalPanel();
		
		hpSpace.setWidth("20px");
		hpFallasT1.add(hpSpace);
		hpFallasT1.add(btnPrincEliminarT);
		
		hplblFallasT.add(lblFallasT);
		vpFallasT2.add(hplblFallasT);
		vpFallasT2.add(hpFallasT1);
		
		vpFallasT1.add(vpFallasT3);
		vpFallasT1.add(vpFallasT2);

		btnPrincEliminarT.addClickHandler(new ClickHandler() 
		{
			public void onClick(ClickEvent event) {
				EliminarFallaT();
			}
		});
	}

	public void cargarTabR(){
		tabPTipos.add(vpFallasR1, "Fallas Reportadas");

		vpFallasR1.setSpacing(10);
		
		vpFallasR3.add(lblTiposFallasR);
		vpFallasR3.add(listBoxTiposFallasR);

		hpFallasR1.add(listBoxFallasR);
		HorizontalPanel hpSpace = new HorizontalPanel();
		
		hpSpace.setWidth("20px");
		hpFallasR1.add(hpSpace);
		hpFallasR1.add(btnPrincEliminarR);
		
		hplblFallasR.add(lblFallasR);
		vpFallasR2.add(hplblFallasR);
		vpFallasR2.add(hpFallasR1);
		
		vpFallasR1.add(vpFallasR3);
		vpFallasR1.add(vpFallasR2);

		btnPrincEliminarR.addClickHandler(new ClickHandler() 
		{
			public void onClick(ClickEvent event) {
				EliminarFallaR();
			}
		});
	}

	public void cargarLtBoxT(){
		popUp.show();
		listBoxFallasT.clear();
		ProyectoBilpa.greetingService.obtenerTodosLasFallasT(new AsyncCallback<ArrayList<FallaTecnica>>() {

			public void onFailure(Throwable caught) {
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener todas las fallas tecnicas");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<FallaTecnica> result) {
				for (int i=0; i<result.size();i++){
					listBoxFallasT.addItem(result.get(i).toString(), String.valueOf(result.get(i).getId()));
				}				
				popUp.hide();
			}
		});		
	}



	public void cargarLtBoxR(){
		listBoxFallasR.clear();
		ProyectoBilpa.greetingService.obtenerTodosLasFallasR(new AsyncCallback<ArrayList<FallaReportada>>() {

			public void onFailure(Throwable caught) {
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener todas las fallas reportadas");
				vpu.showPopUp();
			}
			
			public void onSuccess(ArrayList<FallaReportada> result) {
				for (int i=0; i<result.size();i++){
					listBoxFallasR.addItem(result.get(i).toString(), String.valueOf(result.get(i).getId()));
				}				
			}
		});		
	}

}
