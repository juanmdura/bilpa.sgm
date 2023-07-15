package app.client.iu.repuesto;

import java.util.ArrayList;

import app.client.ProyectoBilpa;
import app.client.dominio.Persona;
import app.client.dominio.TipoRepuesto;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class IUEliminarTipoRepuesto extends Composite{

	private Label lblTitulo = new Label("Eliminar tipos de repuestos");
	private ListBox listBoxListaRepuestos = new ListBox();

	private Persona sesion;
	private VerticalPanel vPanelPrincipal =  new VerticalPanel();
	private HorizontalPanel hPanelPrincipal = new HorizontalPanel();
	private VerticalPanel vPanel1 =  new VerticalPanel();
	private VerticalPanel vPanel2 =  new VerticalPanel();

	private PopupCargando popUp = new PopupCargando("Cargando...");
	
	private GlassPopup glass = new GlassPopup();
	
	private Button bEliminar1 =  new Button ("Eliminar", new ClickHandler() {
		public void onClick(ClickEvent event) {
			eliminarRepuesto1();			
		}
	});
	
	//Dialogo eliminar
	//========================================================	
	private DialogBox dialogBoxT = new DialogBox();
	private Label lblTituloDialT = new Label("Seguro desea eliminar este tipo de repuesto?");
	private HTML htmlDescDialT = new HTML();
	private VerticalPanel vpDialT = new VerticalPanel();
	private HorizontalPanel hpDialT = new HorizontalPanel();
	private HorizontalPanel hp2DialT = new HorizontalPanel();
	
	private Button btnElimDial = new Button("Eliminar", new ClickHandler() {
		public void onClick(ClickEvent event) {
			dialogBoxT.hide(true);
			eliminarTipoRepuesto();
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
	public IUEliminarTipoRepuesto(Persona persona){
		this.sesion = persona;
		setearWidgets();			//Setea el tamano de los Widgets.
		cargarPanelesConWidgets();	//Agrega los Widget a los paneles.
		cargarLtBoxRepuestos();	
		color();
	}


	private void color() {
		lblTitulo.setStyleName("Titulo");
		
	}
	
	private void setearWidgets() {
		this.hPanelPrincipal.setWidth("800px");
		this.bEliminar1.setWidth("100px");

		listBoxListaRepuestos.setWidth("500px");
		listBoxListaRepuestos.setVisibleItemCount(10);


	}

	private void cargarPanelesConWidgets() {
		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		this.vPanelPrincipal.add(lblTitulo);
		vPanelPrincipal.add(hPanelPrincipal);
		this.vPanel1.setSpacing(20);
		this.vPanel1.add(listBoxListaRepuestos);
		this.vPanel2.add(bEliminar1);
		this.vPanel2.setSpacing(20);
		this.hPanelPrincipal.add(vPanel1);
		this.hPanelPrincipal.add(vPanel2);
		this.listBoxListaRepuestos.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				//	cargarDescripcionRepuesto();
			}
		});
		this.listBoxListaRepuestos.setFocus(false);
		this.listBoxListaRepuestos.setTitle("Lista de Repuestos");
		bEliminar1.setTitle("Seleccione una Repuesto y presione Eliminar para borrarla del sistema");

		

	}

	public void cargarLtBoxRepuestos(){
		popUp.show();
		listBoxListaRepuestos.clear();
		ProyectoBilpa.greetingService.obtenerTodasLosTiposRepuestosActivos(new AsyncCallback<ArrayList<TipoRepuesto>>() {

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar tipos de repuestos");
				vpu.showPopUp();

			}

			public void onSuccess(ArrayList<TipoRepuesto> result) {
				TipoRepuesto auxiliar;
				for (int i=0; i<result.size();i++){
					auxiliar = (TipoRepuesto) result.get(i);
					if (auxiliar.getId() != 1)//Sin Clasificar
					{
						listBoxListaRepuestos.addItem(auxiliar.toString(),String.valueOf(auxiliar.getId()));
					}
				}	
				popUp.hide();
			}
		});		
	}

	public void eliminarTipoRepuesto(){
		int id = Integer.valueOf(this.listBoxListaRepuestos.getValue(this.listBoxListaRepuestos.getSelectedIndex()));
		ProyectoBilpa.greetingService.buscarTipoRepuesto(id, new AsyncCallback<TipoRepuesto>() {
			public void onSuccess(TipoRepuesto result) {
				if (result != null){
					ProyectoBilpa.greetingService.eliminarTipoRepuesto(result, new AsyncCallback<Boolean>() {
						public void onFailure(Throwable caught) {
							ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al eliminar tipos de repuestos");
							vpu.showPopUp();
						}
						public void onSuccess(Boolean result) {
							cargarLtBoxRepuestos();
						}
					});
				}				
			}
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al eliminar tipos de repuestos");
				vpu.showPopUp();
			}
		});
	}
	public DialogBox dialElimTipoRepuesto(String texto)
	{
		vpDialT.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hpDialT.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		vpDialT.add(lblTituloDialT);
		lblTituloDialT.setStyleName("Negrita");
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
		if (listBoxListaRepuestos.getSelectedIndex() != -1)
		{
			String repuestoSeleccionada = listBoxListaRepuestos.getItemText(listBoxListaRepuestos.getSelectedIndex());
			DialogBox db = dialElimTipoRepuesto(repuestoSeleccionada);
			db.center();
			db.show();
		}
	}
}
