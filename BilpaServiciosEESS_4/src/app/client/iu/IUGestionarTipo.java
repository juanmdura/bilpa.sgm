package app.client.iu;

import app.client.dominio.Persona;
import app.client.utilidades.utilObjects.PopupCargando;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public abstract class IUGestionarTipo extends Composite{
	protected HTML htmlTitulo = new HTML("Gestion tipos");

	protected ListBox lblListaDatos = new ListBox();

	protected TextBox txtDescDato = new TextBox();

	protected Button btnGuardar = new Button("Guardar");
	protected Button btnModificar = new Button("Modificar");

	protected VerticalPanel vPanelPrincipal = new VerticalPanel();
	protected HorizontalPanel hPanelContenedor = new HorizontalPanel();
	protected VerticalPanel vPanel1 =  new VerticalPanel();
	protected VerticalPanel vPanel2 =  new VerticalPanel();

	protected Persona sesion;

	//Dialogo de Modificacion========================================================
	protected final DialogBox dialogBoxModif = new DialogBox();
	protected HorizontalPanel hPanelDialModif = new HorizontalPanel();
	protected HorizontalPanel hPanelDialModif2 = new HorizontalPanel();
	protected VerticalPanel vPanelDailModif = new VerticalPanel();
	protected TextBox tDialModifRep = new TextBox();

	protected PopupCargando popUp = new PopupCargando("Cargando...");
	
	protected Button btnAceptarModif = new Button("Aceptar",
			new ClickHandler() {
		public void onClick(ClickEvent event) {
			dialogBoxModif.hide(true);
			modificarDato();
		}
	});

	protected Button btnCancelModif = new Button("Cancelar",
			new ClickHandler() {
		public void onClick(ClickEvent event) {
			dialogBoxModif.hide(true);
		}
	});
	
	public VerticalPanel getPrincipalPanel(){
		return this.vPanelPrincipal;
	}

	public IUGestionarTipo(Persona persona){
		this.sesion = persona;
		setearWidgets();			//Setea el tamano de los Widgets.
		cargarPanelesConWidgets();	//Agrega los Widget a los paneles.
		cargarLtBoxDatos();		
		color();
	}

	protected void setearWidgets() {
		this.htmlTitulo.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		this.hPanelContenedor.setWidth("800px");
		this.btnGuardar.setWidth("100px");
		this.btnModificar.setWidth("100px");
		lblListaDatos.setWidth("500px");
		lblListaDatos.setVisibleItemCount(10);
		this.txtDescDato.setWidth("500px");

	}

	protected void cargarPanelesConWidgets() {
		vPanelPrincipal.add(htmlTitulo);
		this.vPanel1.setSpacing(10);
		this.vPanel1.add(txtDescDato);
		this.vPanel1.setSpacing(20);
		this.vPanel1.add(lblListaDatos);

		this.vPanel2.add(btnGuardar);
		this.vPanel2.setSpacing(20);
		this.vPanel2.add(btnModificar);


		this.hPanelContenedor.add(vPanel1);
		this.hPanelContenedor.add(vPanel2);

		this.vPanelPrincipal.add(hPanelContenedor);
		this.lblListaDatos.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				//cargarDescripcionDato();
			}
		});
		txtDescDato.setTitle("Ingrese la Descripción de la Dato y presione Agregar");

		btnModificar.setTitle("Seleccione una Dato y presione Modificar para cambiar su descripción");
		btnGuardar.setTitle("Ingrese la descripcion de la Dato y presione Agregar");
	
		this.lblListaDatos.setFocus(false);
		this.lblListaDatos.setTitle("Lista de Datos");

		btnGuardar.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				guardarNuevoDato();
			}
		});

		this.btnModificar.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				cargarDialModif_1();
			}
		});	
	}

	protected void color() {
		htmlTitulo.setStyleName("Titulo");

	}
	

	protected abstract void guardarDato();
	protected abstract void modificarDato();
	protected abstract void cargarDialModif_1();
	protected abstract void cargarLtBoxDatos();
	protected abstract void guardarNuevoDato();
	//MODIFICAR TAREA==========================================================================================================================
	

	
}

