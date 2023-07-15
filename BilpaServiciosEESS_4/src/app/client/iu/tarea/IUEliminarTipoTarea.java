package app.client.iu.tarea;

import java.util.ArrayList;

import app.client.ProyectoBilpa;
import app.client.dominio.Persona;
import app.client.dominio.TipoTarea;
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

public class IUEliminarTipoTarea extends Composite{

	private Label lblTitulo = new Label("Eliminar tipos de tareas");
	private ListBox listBoxListaTareas = new ListBox();

	private Persona sesion;
	private VerticalPanel vPanelPrincipal =  new VerticalPanel();
	private HorizontalPanel hPanelPrincipal = new HorizontalPanel();
	private VerticalPanel vPanel1 =  new VerticalPanel();
	private VerticalPanel vPanel2 =  new VerticalPanel();

	private PopupCargando popUp = new PopupCargando("Cargando...");
	
	private GlassPopup glass = new GlassPopup();
	
	private Button bEliminar1 =  new Button ("Eliminar", new ClickHandler() {
		public void onClick(ClickEvent event) {
			eliminarTarea1();			
		}
	});
	
	//Dialogo eliminar
	//========================================================	
	private DialogBox dialogBoxT = new DialogBox();
	private Label lblTituloDialT = new Label("Seguro desea eliminar este tipo de tarea?");
	private HTML htmlDescDialT = new HTML();
	private VerticalPanel vpDialT = new VerticalPanel();
	private HorizontalPanel hpDialT = new HorizontalPanel();
	private HorizontalPanel hp2DialT = new HorizontalPanel();
	
	private Button btnElimDial = new Button("Eliminar", new ClickHandler() {
		public void onClick(ClickEvent event) {
			dialogBoxT.hide(true);
			eliminarTipoTarea();
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
	public IUEliminarTipoTarea(Persona persona){
		this.sesion = persona;
		setearWidgets();			//Setea el tamano de los Widgets.
		cargarPanelesConWidgets();	//Agrega los Widget a los paneles.
		cargarLtBoxTareas();	
		color();
	}


	private void color() {
		lblTitulo.setStyleName("Titulo");
		
	}
	
	private void setearWidgets() {
		this.hPanelPrincipal.setWidth("800px");
		this.bEliminar1.setWidth("100px");

		listBoxListaTareas.setWidth("500px");
		listBoxListaTareas.setVisibleItemCount(10);


	}

	private void cargarPanelesConWidgets() {
		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		this.vPanelPrincipal.add(lblTitulo);
		vPanelPrincipal.add(hPanelPrincipal);
		this.vPanel1.setSpacing(20);
		this.vPanel1.add(listBoxListaTareas);
		this.vPanel2.add(bEliminar1);
		this.vPanel2.setSpacing(20);
		this.hPanelPrincipal.add(vPanel1);
		this.hPanelPrincipal.add(vPanel2);
		this.listBoxListaTareas.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				//	cargarDescripcionTarea();
			}
		});
		this.listBoxListaTareas.setFocus(false);
		this.listBoxListaTareas.setTitle("Lista de Tareas");
		bEliminar1.setTitle("Seleccione una Tarea y presione Eliminar para borrarla del sistema");

		

	}

	public void cargarLtBoxTareas(){
		popUp.show();
		listBoxListaTareas.clear();
		ProyectoBilpa.greetingService.obtenerTodasLosTiposTareasActivos(new AsyncCallback<ArrayList<TipoTarea>>() {

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar tipos de tareas");
				vpu.showPopUp();

			}

			public void onSuccess(ArrayList<TipoTarea> result) {
				TipoTarea auxiliar;
				for (int i=0; i<result.size();i++){
					auxiliar = (TipoTarea) result.get(i);
					if (auxiliar.getId() != 1)//Sin Clasificar
					{
						listBoxListaTareas.addItem(auxiliar.toString(),String.valueOf(auxiliar.getId()));
					}
				}	
				popUp.hide();
			}
		});		
	}

	public void eliminarTipoTarea(){
		int id = Integer.valueOf(this.listBoxListaTareas.getValue(this.listBoxListaTareas.getSelectedIndex()));
		ProyectoBilpa.greetingService.buscarTipoTarea(id, new AsyncCallback<TipoTarea>() {
			public void onSuccess(TipoTarea result) {
				if (result != null){
					ProyectoBilpa.greetingService.eliminarTipoTarea(result, new AsyncCallback<Boolean>() {
						public void onFailure(Throwable caught) {
							ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al eliminar tipos de tareas");
							vpu.showPopUp();
						}
						public void onSuccess(Boolean result) {
							cargarLtBoxTareas();
						}
					});
				}				
			}
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al eliminar tipos de tareas");
				vpu.showPopUp();
			}
		});
	}
	public DialogBox dialElimTipoTarea(String texto)
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
	
	public void eliminarTarea1()
	{
		if (listBoxListaTareas.getSelectedIndex() != -1)
		{
			String tareaSeleccionada = listBoxListaTareas.getItemText(listBoxListaTareas.getSelectedIndex());
			DialogBox db = dialElimTipoTarea(tareaSeleccionada);
			db.center();
			db.show();
		}
	}
}
