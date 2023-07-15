package app.client.iu.repuesto;

import app.client.ProyectoBilpa;
import app.client.dominio.Persona;
import app.client.dominio.Repuesto;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class IUEliminarRepuesto extends IUWidgetRepuesto{
	private Label lblTitulo = new Label("Eliminar repuestos");
	private Label lblTituloTiposRepuestos = new Label("Tipo de Repuesto");
	private Label lblTituloRepuestos = new Label("Repuestos");

	private Button bEliminar = new Button("Eliminar");

	private VerticalPanel vPanelPrincipal =  new VerticalPanel();
	private VerticalPanel vPanelPrincipal1 = new VerticalPanel();
	private HorizontalPanel hPanelPrincipal2 = new HorizontalPanel();
	private HorizontalPanel hPanel1 = new HorizontalPanel();
	private HorizontalPanel hPanelSpace = new HorizontalPanel();
	private HorizontalPanel hPanelSpace2 = new HorizontalPanel();
	
	private VerticalPanel vPanel1 =  new VerticalPanel();

	private PopupCargando popUp = new PopupCargando("Cargando...");
	
	private GlassPopup glass = new GlassPopup();
	
	//Dialogo eliminar
	//========================================================	
	private DialogBox dialogBoxT = new DialogBox();
	private Label lblTituloDialT = new Label("Seguro desea eliminar esta repuesto?");
	private HTML htmlDescDialT = new HTML();
	private VerticalPanel vpDialT = new VerticalPanel();
	private HorizontalPanel hpDialT = new HorizontalPanel();
	private HorizontalPanel hp2DialT = new HorizontalPanel();
	
	private Button btnElimDial = new Button("Eliminar",new ClickHandler() {
		public void onClick(ClickEvent event) {
			dialogBoxT.hide(true);
			eliminarRepuesto();
		}
	});

	private Button btnCancelDial = new Button("Cancelar",new ClickHandler() {
		public void onClick(ClickEvent event) {
			dialogBoxT.hide(true);
		}
	});
	//========================================================
	
	public VerticalPanel getPrincipalPanel() {
		return vPanelPrincipal;
	}

	//CONSTRUCTOR
	public IUEliminarRepuesto(Persona persona){
		setearWidgets();			//Setea el tamano de los Widgets.
		cargarPanelesConWidgets();	//Agrega los Widget a los paneles.
		cargarLtBoxTipoRepuesto(popUp);
			
		color();
	}


	private void color() 
	{
		lblTitulo.setStyleName("Titulo");
		lblTituloTiposRepuestos.setStyleName("Negrita");
		lblTituloRepuestos.setStyleName("Negrita");
		
		lblTituloDialT.setStyleName("Subtitulo");
	}
	
	private void setearWidgets() {
		vPanelPrincipal1.setWidth("800px");
		hPanelPrincipal2.setWidth("800px");
		bEliminar.setWidth("100px");

		listBoxRepuestos.setWidth("500px");
		listBoxTiposRepuestos.setWidth("500px");
		
		listBoxRepuestos.setVisibleItemCount(10);

		hPanelSpace.setWidth("30px");
		hPanelSpace2.setHeight("10px");
	}

	private void cargarPanelesConWidgets() {
		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		vPanelPrincipal.setSpacing(10);
		vPanelPrincipal.add(lblTitulo);
		
		vPanelPrincipal.add(vPanelPrincipal1);
		vPanelPrincipal.add(hPanelSpace2);
		vPanelPrincipal.add(hPanelPrincipal2);		
		
		vPanelPrincipal1.add(lblTituloTiposRepuestos);
		vPanelPrincipal1.add(listBoxTiposRepuestos);
		
		hPanelPrincipal2.add(vPanel1);
		
		vPanel1.add(lblTituloRepuestos);
		vPanel1.add(hPanel1);
		
		hPanel1.add(listBoxRepuestos);
		hPanel1.add(hPanelSpace);
		hPanel1.add(bEliminar);
		
		listBoxRepuestos.setFocus(false);
		listBoxRepuestos.setTitle("Lista de Repuestos");
		bEliminar.setTitle("Seleccione una Repuesto y presione Eliminar para borrarla del sistema");

		bEliminar.addClickHandler(new ClickHandler() 
		{
			public void onClick(ClickEvent event) {
				eliminarRepuesto1();
			}
		});
	}

	public void eliminarRepuesto(){
		int id = Integer.valueOf(this.listBoxRepuestos.getValue(this.listBoxRepuestos.getSelectedIndex()));
		
		ProyectoBilpa.greetingService.buscarRepuesto(id, new AsyncCallback<Repuesto>() {
			public void onSuccess(Repuesto result) 
			{
				if (result != null)
				{
					ProyectoBilpa.greetingService.eliminarRepuesto(result, new AsyncCallback<Boolean>() {
						public void onFailure(Throwable caught) 
						{
							popUp.hide();
							ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al eliminar las repuestos");
							vpu.showPopUp();
						}
						
						public void onSuccess(Boolean result) 
						{
							cargarLtBoxRepuestosBase(popUp);
						}
					});
				}
			}
			
			public void onFailure(Throwable caught) 
			{
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al eliminar repuestos");
				vpu.showPopUp();
			}
		});
	}
	
	public DialogBox dialElimRepuesto(String texto)
	{
		vpDialT.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hpDialT.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		vpDialT.add(lblTituloDialT);
		dialogBoxT.setAutoHideEnabled(true);

		btnElimDial.setWidth("100px");
		btnCancelDial.setWidth("100px");

		htmlDescDialT.setWidth("500px");
		htmlDescDialT.setText(texto);

		hpDialT.setSpacing(5);
		hpDialT.add(htmlDescDialT);

		hp2DialT.add(btnCancelDial);
		hp2DialT.setSpacing(5);		
		hp2DialT.add(btnElimDial);

		vpDialT.add(hpDialT);
		vpDialT.add(hp2DialT);

		dialogBoxT.setWidget(vpDialT);
		return dialogBoxT;
	}
	
	public void eliminarRepuesto1()
	{
		if (listBoxRepuestos.getSelectedIndex() != -1)
		{
			String repuestoSeleccionada = listBoxRepuestos.getItemText(listBoxRepuestos.getSelectedIndex());
			DialogBox db = dialElimRepuesto(repuestoSeleccionada);
			db.center();
			db.show();
		}
	}
}
