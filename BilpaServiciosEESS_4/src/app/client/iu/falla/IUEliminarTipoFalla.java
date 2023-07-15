package app.client.iu.falla;

import java.util.ArrayList;

import app.client.ProyectoBilpa;
import app.client.dominio.Persona;
import app.client.dominio.TipoFallaReportada;
import app.client.dominio.TipoFallaTecnica;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class IUEliminarTipoFalla extends Composite{
	
	private HTML htmlTitulo = new HTML("Eliminar tipos de fallas");

	private Persona sesion;

	private ListBox ltBFallasT = new ListBox();
	private ListBox ltBFallasR = new ListBox();

	private Button btnPrincEliminarT = new Button("Eliminar");	
	private Button btnPrincEliminarR = new Button("Eliminar");

	private DecoratedTabPanel tabPTipos = new DecoratedTabPanel();
	private VerticalPanel vpPrincipal = new VerticalPanel();

	//=====================================================================	
	//Pop Up Fallas Tecnicas
	private DialogBox dialogBoxT = new DialogBox();
	private Label lblTituloDialT = new Label("Seguro desea eliminar este tipo de falla?");
	private HTML htmlDescDialT = new HTML();
	private VerticalPanel vpDialT = new VerticalPanel();
	private HorizontalPanel hpDialT = new HorizontalPanel();
	private HorizontalPanel hp2DialT = new HorizontalPanel();
	//=====================================================================
	//=====================================================================	
	//Pop Up Fallas Reclamadas	
	private DialogBox dialogBoxR = new DialogBox();		
	HTML htmlDescDialR = new HTML();
	private Label lblTituloDialR = new Label("Seguro desea eliminar este tipo de falla?");
	VerticalPanel vpDialR = new VerticalPanel();
	HorizontalPanel hpDialR = new HorizontalPanel();
	HorizontalPanel hp2DialR = new HorizontalPanel();
	//=====================================================================

	public VerticalPanel getPrincipalPanel(){
		return this.vpPrincipal;
	}

	private VerticalPanel vpFallasT = new VerticalPanel();
	private VerticalPanel vpFallasT2 = new VerticalPanel();
	private HorizontalPanel hpFallasT = new HorizontalPanel();

	private VerticalPanel vpFallasR = new VerticalPanel();
	private VerticalPanel vpFallasR2 = new VerticalPanel();
	private HorizontalPanel hpFallasR = new HorizontalPanel();

	private PopupCargando popUp = new PopupCargando("Cargando...");
	private GlassPopup glass = new GlassPopup();

	private Button btnElimDialT = new Button("Eliminar", new ClickHandler() {
		public void onClick(ClickEvent event) {
			removerFallaTSeleccionada();
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
			removerFallaRSeleccionada();
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
	public IUEliminarTipoFalla(Persona persona) {
		this.sesion = persona;
		//Layout	
		setearWidgets();

		//Tecnicas
		cargarTabT();
		cargarLtBoxT();

		//Reclamadas		
		cargarTabR();
		cargarLtBoxR();

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
	
	public void eliminarFallaT(){
		if (this.ltBFallasT.getSelectedIndex() != -1){
			glass.show();
			String fallaSeleccionada = this.ltBFallasT.getItemText(this.ltBFallasT.getSelectedIndex());
			DialogBox db = this.dialElimFallaT(fallaSeleccionada);
			db.center();
			db.show();
		}
	}

	public void eliminarFallaR(){
		if (this.ltBFallasR.getSelectedIndex() != -1){
			glass.show();
			String fallaSeleccionada = this.ltBFallasR.getItemText(this.ltBFallasR.getSelectedIndex());
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

	public void removerFallaTSeleccionada(){
		int id = Integer.valueOf(this.ltBFallasT.getValue(this.ltBFallasT.getSelectedIndex()));
		ProyectoBilpa.greetingService.buscarTipoFallaT(id, new AsyncCallback<TipoFallaTecnica>() {
			public void onSuccess(TipoFallaTecnica result) {
				if (result != null){
					ProyectoBilpa.greetingService.eliminarTipoFallaT(result, new AsyncCallback<Boolean>() {
						public void onFailure(Throwable caught) 
						{
							popUp.hide();
							ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener tipos fallas");
							vpu.showPopUp();
						}
						public void onSuccess(Boolean result) 
						{
							cargarLtBoxT();	
						}
					});
				}				
			}
			public void onFailure(Throwable caught) 
			{
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener tipos fallas");
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

	public void removerFallaRSeleccionada()
	{
		int id = Integer.valueOf(this.ltBFallasR.getValue(this.ltBFallasR.getSelectedIndex()));
		ProyectoBilpa.greetingService.buscarTipoFallaR(id, new AsyncCallback<TipoFallaReportada>() {
			
			public void onSuccess(TipoFallaReportada result) {
				
				if (result != null) {
					ProyectoBilpa.greetingService.eliminarTipoFallaR(result, new AsyncCallback<Boolean>() {
						public void onFailure(Throwable caught) {
							popUp.hide();
							ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener tipos fallas");
							vpu.showPopUp();
						}
						public void onSuccess(Boolean result) {
							cargarLtBoxR();						
						}
					});
				}				
			}
			public void onFailure(Throwable caught) 
			{
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener tipos fallas");
				vpu.showPopUp();
			}
		});
	}

	public void setearWidgets(){

		htmlTitulo.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		tabPTipos.setWidth("600px");
		tabPTipos.setAnimationEnabled(true);

		ltBFallasT.setWidth("500px");
		ltBFallasR.setWidth("500px");
		
		ltBFallasR.setTitle("Tipos de Fallas Reportadas");
		ltBFallasT.setTitle("Tipos de Fallas Detectadas");
		
		btnPrincEliminarT.setTitle("Seleccione un Tipo de Falla Detectada y presione Eliminar para borrarla del sistema");
		btnPrincEliminarR.setTitle("Seleccione un Tipo de Falla Reportada y presione Eliminar para borrarla del sistema");
		
		
		ltBFallasT.setVisibleItemCount(10);
		ltBFallasR.setVisibleItemCount(10);

		btnPrincEliminarT.setWidth("100px");
		btnPrincEliminarR.setWidth("100px");
	}

	public void cargarTabT(){
		tabPTipos.add(hpFallasT, "Tipos Fallas Detectadas");

		vpFallasT.setSpacing(20);
		vpFallasT.add(ltBFallasT);	

		vpFallasT2.setSpacing(20);
		vpFallasT2.add(btnPrincEliminarT);

		hpFallasT.add(vpFallasT);
		hpFallasT.add(vpFallasT2);


		btnPrincEliminarT.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				eliminarFallaT();
			}
		});
	}

	public void cargarTabR(){
		tabPTipos.add(hpFallasR, "Tipos Fallas Reportadas");

		vpFallasR.setSpacing(20);
		vpFallasR.add(ltBFallasR);	

		vpFallasR2.setSpacing(20);
		vpFallasR2.add(btnPrincEliminarR);

		hpFallasR.add(vpFallasR);
		hpFallasR.add(vpFallasR2);

		btnPrincEliminarR.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				eliminarFallaR();
			}
		});
	}

	public void cargarLtBoxT(){
		popUp.show();
		ltBFallasT.clear();
		ProyectoBilpa.greetingService.obtenerTiposFallasT(new AsyncCallback<ArrayList<TipoFallaTecnica>>() {

			public void onFailure(Throwable caught) 
			{
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener tipos fallas");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<TipoFallaTecnica> result)
			{
				for (int i=0; i<result.size();i++)
				{
					if(result.get(i).getId() != 1)
					{
						ltBFallasT.addItem(result.get(i).toString(), String.valueOf(result.get(i).getId()));
					}
				}				
				popUp.hide();
			}
		});		
	}



	public void cargarLtBoxR(){
		ltBFallasR.clear();
		ProyectoBilpa.greetingService.obtenerTiposFallasR(new AsyncCallback<ArrayList<TipoFallaReportada>>() {

			public void onFailure(Throwable caught) 
			{
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener tipos fallas");
				vpu.showPopUp();
			}
			
			public void onSuccess(ArrayList<TipoFallaReportada> result) 
			{
				for (int i=0; i<result.size();i++)
				{
					if(result.get(i).getId() != 2)
					{
						ltBFallasR.addItem(result.get(i).toString(), String.valueOf(result.get(i).getId()));
					}
				}				
			}
		});		
	}
}
