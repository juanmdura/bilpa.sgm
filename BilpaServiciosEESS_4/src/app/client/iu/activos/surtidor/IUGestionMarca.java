package app.client.iu.activos.surtidor;

import java.util.ArrayList;

import app.client.ProyectoBilpa;
import app.client.dominio.Marca;
import app.client.dominio.Persona;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.UtilUI;
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
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class IUGestionMarca extends Composite{
	
	private HTML htmlTitulo = new HTML("Gestión de marcas de surtidores");

	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private HorizontalPanel hPanelPrincipal = new HorizontalPanel();
	private HorizontalPanel hPanelContenedor = new HorizontalPanel();

	private ListBox listBoxListaMarcas = new ListBox();
	private TextBox tNombreMarca = new TextBox();

	private Persona sesion;

	private VerticalPanel vPanel1 =  new VerticalPanel();
	private HorizontalPanel hPanelDatos = new HorizontalPanel();
	private VerticalPanel vPanel2 =  new VerticalPanel();

	private Label lblNombreMarca = new Label("Nombre");

	private String nuevoNombreMarca;
	
	private GlassPopup glass = new GlassPopup();

	
	Button btnCancelModif = new Button("Cancelar",new ClickHandler() {
		public void onClick(ClickEvent event) {
			dialogBoxModif.hide(true);
			glass.hide();
		}
	});
	
	Button btnAceptarModif = new Button("Modificar", new ClickHandler() {
		public void onClick(ClickEvent event) {
			modificarMarca();
		}
	});
	
	Button bGuardar = new Button("Guardar",new ClickHandler() {
		public void onClick(ClickEvent event) {
			validarTodo();
		}
	});
	
	//Dialogo de Modificacion========================================================
	final DialogBox dialogBoxModif = new DialogBox();
	private HorizontalPanel hPanelDialModif = new HorizontalPanel();
	HorizontalPanel hPanelDialModif2 = new HorizontalPanel();
	VerticalPanel vPanelDailModif = new VerticalPanel();
	TextBox tDescModif = new TextBox();
	private Label lblDescRepMod = new Label("Nombre");
	
	private PopupCargando popUp = new PopupCargando("Cargando...");

	Button bModificar = new Button("Modificar",new ClickHandler() {
		public void onClick(ClickEvent event) {
			buscarMarca();
		}
	});
	//====================================================

	public VerticalPanel getPrincipalPanel() {
		return vPanelPrincipal;
	}
	
	public IUGestionMarca(Persona persona)
	{
		this.sesion = persona;
		setearWidgets();			//Setea el tamano de los Widgets.
		cargarPanelesConWidgets();	//Agrega los Widget a los paneles.
		cargarLtBoxMarcas();	
		color();
		this.vPanelPrincipal.add(hPanelPrincipal);
	}

	private void cargarLtBoxMarcas(){
		listBoxListaMarcas.clear();
		popUp.show();
		ProyectoBilpa.greetingService.obtenerTodasLasMArcas(new AsyncCallback<ArrayList<Marca>>() {

			public void onFailure(Throwable caught) {
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener las marcas");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<Marca> result) {
				Marca auxiliar;
				for (int i=0; i < result.size(); i++){
					auxiliar = (Marca) result.get(i);
					listBoxListaMarcas.addItem(auxiliar.toString(),String.valueOf(auxiliar.getId()));
				}	
				popUp.hide();
			}
		});		
	}
	
	private void color() {
		htmlTitulo.setStyleName("Titulo");
		lblNombreMarca.setStyleName("Negrita");
		lblDescRepMod.setStyleName("Negrita");
	}
	
	private void setearWidgets() {
		this.htmlTitulo.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		this.vPanelPrincipal.setWidth("800px");
		this.bGuardar.setWidth("100px");
		bGuardar.setTitle("Ingrese el nombre de la Marca y presione Guardar");
		bModificar.setTitle("Seleccione una Marca y presione Modificar para cambiar su nombre");
		tNombreMarca.setTitle("Ingrese el Nombre de la Marca y presione Guardar");
		this.bModificar.setWidth("100px");
		listBoxListaMarcas.setWidth("500px");
		listBoxListaMarcas.setVisibleItemCount(10);
		//tNombreMarca.setWidth("190px");
		tNombreMarca.setWidth("490px");
		tDescModif.setWidth("190px");	
		hPanelDatos.setSpacing(5);
	}
	
	private void cargarPanelesConWidgets() {
		hPanelDatos.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		vPanel1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		this.vPanelPrincipal.add(htmlTitulo);
		vPanel1.add(hPanelDatos);
		//hPanelDatos.add(lblNombreMarca);
		hPanelDatos.add(tNombreMarca);
		vPanel1.setSpacing(20);
		this.vPanel1.add(listBoxListaMarcas);

		this.vPanel2.add(bGuardar);
		this.vPanel2.setSpacing(20);
		this.vPanel2.add(bModificar);

		this.hPanelContenedor.add(vPanel1);
		this.hPanelContenedor.add(vPanel2);

		this.vPanelPrincipal.add(hPanelContenedor);

		this.listBoxListaMarcas.setFocus(false);
		this.listBoxListaMarcas.setTitle("Lista de Marcas");

	}
	
	private void guardarMarca(){
		Marca marca = new Marca(this.tNombreMarca.getText());
		ProyectoBilpa.greetingService.agregarMarca(marca, new AsyncCallback<Boolean>() {

			public void onSuccess(Boolean result) {
				if (result) {
					limpiarCampos();
					cargarLtBoxMarcas();
				} else {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar la marca");
					vpu.showPopUp();
				}
			}

			public void onFailure(Throwable caught) {
			}
		});

		
		
	}
	
	private void validarTodo() {
		if (this.validarCampos()) {
			this.validarNombreDeMarcaDisponible(this.tNombreMarca.getText());
		}
	}
	
	private void validarNombreDeMarcaDisponible(String nombreMarca){
		ProyectoBilpa.greetingService.validarNombreDeMarcaDisponible(nombreMarca,new AsyncCallback<Boolean>() {

			public void onSuccess(Boolean result) {
				if (result) {
					guardarMarca();
				} else {
					DialogBox yaExisteNombre = UtilUI.dialogBoxError("Ya existe una Marca con ese nombre");
					yaExisteNombre.show();
					yaExisteNombre.center();
				}
			}

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar la marca");
				vpu.showPopUp();
			}
		});
	}
	
	
	
	private void buscarMarca(){
		if (this.listBoxListaMarcas.getSelectedIndex() != -1){

			int id = Integer.valueOf(this.listBoxListaMarcas.getValue(this.listBoxListaMarcas.getSelectedIndex()));
			ProyectoBilpa.greetingService.buscarMarca(id, new AsyncCallback<Marca>() {
				public void onSuccess(Marca result) {
					if (result != null){
						DialogBox db = modificacionMarca(result);
						glass.show();
						db.center();
						db.show();
					}else{
						ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar la marca");
						vpu.showPopUp();
					}				
				}

				public void onFailure(Throwable caught) {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al modificar la marca");
					vpu.showPopUp();
				}
			});
		}
	}
	
	private boolean validarCampos(){
		if(this.tNombreMarca.getText().equalsIgnoreCase("")){
			ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Debe ingresar un nombre");
			vpu.showPopUp();
			return false;
		}
		
		if(this.tNombreMarca.getText().trim().equalsIgnoreCase("")){
			ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Debe ingresar un nombre");
			vpu.showPopUp();
			return false;
		}
		
		return true;
	}
	
	private void limpiarCampos(){
		this.tNombreMarca.setText("");
	}
	
	private DialogBox modificacionMarca(Marca marca){
		dialogBoxModif.setAutoHideEnabled(true);
		dialogBoxModif.setTitle("Modificacion de Marca");
		vPanelDailModif.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hPanelDialModif.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		btnCancelModif.setWidth("100px");
		btnAceptarModif.setWidth("100px");		
		
		tDescModif.setText(marca.getNombre());
		

		hPanelDialModif.setSpacing(5);
		

		lblDescRepMod.setWidth("100px");
		
		

		hPanelDialModif.add(lblDescRepMod);		
		hPanelDialModif.add(tDescModif);

		hPanelDialModif2.setSpacing(5);
		hPanelDialModif2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		hPanelDialModif2.add(btnCancelModif);
		hPanelDialModif2.add(btnAceptarModif);

		vPanelDailModif.setSpacing(5);
		vPanelDailModif.setSize("500px", "100px");
		
		vPanelDailModif.add(hPanelDialModif);
		vPanelDailModif.add(hPanelDialModif2);

		dialogBoxModif.setWidget(vPanelDailModif);
		return dialogBoxModif;
	}
	
	private void modificarMarca(){
		nuevoNombreMarca = this.tDescModif.getText();
		buscarMarcaNombre(nuevoNombreMarca);
		dialogBoxModif.hide(true);
		glass.hide();
	}

	private void buscarMarcaNombre(String nombre){
		final int idMarca = Integer.valueOf(this.listBoxListaMarcas.getValue(this.listBoxListaMarcas.getSelectedIndex()));
		ProyectoBilpa.greetingService.buscarMarca(nombre, new AsyncCallback<Marca>() {//Deberia devolver boolean
			public void onSuccess(Marca result) {
				if(result != null){
					ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Ya existe una marca con ese nombre");
					vpu.showPopUp();
				}else{
					modificarMarcaBase(idMarca);
				}
			}

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar la marca");
				vpu.showPopUp();
			}
		});
	}
	
	private void modificarMarcaBase(int idMarca) {
		ProyectoBilpa.greetingService.modificarMarca(idMarca, nuevoNombreMarca, new AsyncCallback<Boolean>() {			
			public void onSuccess(Boolean result) {
				if(result){
					limpiarCampos();
					cargarLtBoxMarcas();
				}else{
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al modificar la marca");
					vpu.showPopUp();
				}				
			}
			
			public void onFailure(Throwable caught) {	
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al modificar la marca");
				vpu.showPopUp();
			}
		});
	}
}
