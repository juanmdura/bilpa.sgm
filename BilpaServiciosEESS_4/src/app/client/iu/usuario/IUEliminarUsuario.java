package app.client.iu.usuario;

import java.util.ArrayList;
import app.client.ProyectoBilpa;
import app.client.dominio.Persona;
import app.client.utilidades.utilObjects.PopupCargando;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class IUEliminarUsuario extends Composite{
	
	private ListBox listBoxListaPersonas = new ListBox();
	private Label lblTitulo = new Label("Eliminar usuarios"); 
	private Button bEliminar = new Button("Eliminar");

	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private HorizontalPanel hPanelPrincipal = new HorizontalPanel();
	private VerticalPanel vPanel1 =  new VerticalPanel();
	private VerticalPanel vPanel2 =  new VerticalPanel();

	private ArrayList<Persona> personas = new ArrayList<Persona>();	
	private Persona sesion;

	final DialogBox dialogBoxModif = new DialogBox();

	private PopupCargando popUp = new PopupCargando("Cargando...");

	Button btnCancelModif = new Button("Cancelar",
			new ClickHandler() {
		public void onClick(ClickEvent event) {
			dialogBoxModif.hide(true);
		}
	});
	//====================================================

	public VerticalPanel getPrincipalPanel() {
		return vPanelPrincipal;
	}

	//CONSTRUCTOR
	public IUEliminarUsuario(Persona persona){
		this.sesion = persona;
		setearWidgets();			//Setea el tamano de los Widgets.
		cargarPanelesConWidgets();	//Agrega los Widget a los paneles.
		cargarLtBoxPersonas();	
		color();
	}

	private void color() {
		lblTitulo.setStyleName("Titulo");		
	}
	
	private void setearWidgets() {
		this.bEliminar.setWidth("100px");

		listBoxListaPersonas.setWidth("350px");
		listBoxListaPersonas.setVisibleItemCount(10);


	}

	private void cargarPanelesConWidgets() {
		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal.add(lblTitulo);
		vPanelPrincipal.add(hPanelPrincipal);
		this.vPanel1.setSpacing(20);
		this.vPanel1.add(listBoxListaPersonas);
		this.vPanel2.add(bEliminar);
		this.vPanel2.setSpacing(20);
		this.hPanelPrincipal.add(vPanel1);
		this.hPanelPrincipal.add(vPanel2);
		this.listBoxListaPersonas.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

			}
		});
		this.listBoxListaPersonas.setFocus(false);
		
		bEliminar.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				eliminarPersona();
			}
		});

	}

	public void cargarLtBoxPersonas(){
		popUp.show();
		listBoxListaPersonas.clear();
		personas.clear();
		ProyectoBilpa.greetingService.obtenerTodosLosUsuarios(new AsyncCallback<ArrayList<Persona>>() {

			public void onFailure(Throwable caught) {
				popUp.hide();
				Window.alert("Error al obtener usuarios: " + caught.getMessage());
			}

			public void onSuccess(ArrayList<Persona> result) {
				Persona auxiliar;
				for (int i=0; i<result.size();i++){
					auxiliar = (Persona) result.get(i);
					if (auxiliar.getId() != sesion.getId())
					{
						listBoxListaPersonas.addItem(auxiliar.toString(),String.valueOf(auxiliar.getId()));
						personas.add(auxiliar);						
					}
				}	
				popUp.hide();
			}
		});		
	}

	public void eliminarPersona(){
		int id = Integer.valueOf(this.listBoxListaPersonas.getValue(this.listBoxListaPersonas.getSelectedIndex()));
		final Persona pAEliminar = buscarPersona (id) ;
			
		confirma(pAEliminar);		
	}
	
	private Persona buscarPersona(int id) {
		for (Persona p : personas)
		{
			if (p.getId() == id)
			{
				return p;
			}
		}
		return null;
	}

	private void eliminar(Persona p){
		if(p != null){
			ProyectoBilpa.greetingService.eliminarPersona(p, new AsyncCallback<Boolean>() {
				public void onFailure(Throwable caught) {
				
				}
				public void onSuccess(Boolean result) {
					cargarLtBoxPersonas();
				}
			});
		}
	}
	
	private void confirma(final Persona persona) {
		final DialogBox dialogBoxConf = new DialogBox();

		Label titulo = new Label("Seguro desea eliminar al usuario: " + persona.toString());

		Button btnSi = new Button("Si");
		Button btnNo = new Button("No");

		final VerticalPanel vpBig = new VerticalPanel();

		HorizontalPanel hp1 = new HorizontalPanel();
		HorizontalPanel hp2 = new HorizontalPanel();

		titulo.setStyleName("Negrita");

		vpBig.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		hp1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hp2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		btnSi.setWidth("100");
		btnNo.setWidth("100");

		hp1.setSpacing(10);
		hp2.setSpacing(10);

		hp1.add(titulo);
		hp2.add(btnSi);
		hp2.add(btnNo);

		vpBig.add(hp1);
		vpBig.add(hp2);

		dialogBoxConf.add(vpBig);

		dialogBoxConf.show();
		dialogBoxConf.center();

		btnSi.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				dialogBoxConf.hide();	
				aceptaEliminar(persona);
			}
		});

		btnNo.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				dialogBoxConf.hide();	
			}
		});
	}
	
	private void aceptaEliminar(final Persona pAEliminar) {
		ProyectoBilpa.greetingService.validarEliminarPersona(pAEliminar, new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
				Window.alert("Error al eliminar al usuario");			
			}
			public void onSuccess(Boolean result) {
				if(!result){
					Window.alert("No se puede eliminar ese usuario, tiene ordenes asignadas");					
				}else{
					eliminar(pAEliminar);
				}
			}
		});
		
	}

}
