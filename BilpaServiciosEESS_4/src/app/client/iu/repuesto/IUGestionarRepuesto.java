package app.client.iu.repuesto;

import java.util.ArrayList;

import app.client.ProyectoBilpa;
import app.client.dominio.Persona;
import app.client.dominio.Repuesto;
import app.client.dominio.TipoRepuesto;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class IUGestionarRepuesto extends IUWidgetRepuesto{

	private HTML htmlTitulo = new HTML("Gestión de repuestos");

	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private HorizontalPanel hPanelPrincipal = new HorizontalPanel();
	private HorizontalPanel hPanelContenedor = new HorizontalPanel();

	private TextBox tDescRepuesto = new TextBox();
	private TextBox tNroSerie = new TextBox();
	private TextBox tPrecio = new TextBox();

	private Button bGuardar = new Button("Agregar");
	private Button bModificar = new Button("Modificar");
	private Button bBorrar = new Button("Limpiar");

	private VerticalPanel vPanel1 =  new VerticalPanel();
	private HorizontalPanel hPanelDatos = new HorizontalPanel();
	private VerticalPanel vPanel2 =  new VerticalPanel();

	private Label lblNroSerie = new Label("Nro de Parte");
	private Label lblDescRep = new Label("Descripción");	
	private Label lblPrecio = new Label("Precio");	
	private Label lblTipo = new Label("Tipo");	

	private DecoratorPanel decorator = new DecoratorPanel();
	final Grid grillaIngreso = new Grid(4,2);
	int idTipoDatoSel;

	private GlassPopup glass = new GlassPopup();
	private PopupCargando popUp = new PopupCargando("Cargando...");

	public VerticalPanel getPrincipalPanel() {
		return vPanelPrincipal;
	}

	//Dialogo de Modificacion========================================================
	final DialogBox dialogBoxModif = new DialogBox();
	private ListBox listBoxTiposRepuestosMod = new ListBox();
	private Label lblTituloTiposRepuestosModif = new Label("Tipo de Repuesto");
	private Label lblDescripcionModif = new Label("Descripción");
	private Label lblDialModfNroParte = new Label("Nro de Parte");
	private Label lblDialModifPrecio = new Label("Precio");
	private VerticalPanel vPanelDialModif1 = new VerticalPanel();
	private VerticalPanel vPanelDialModif2 = new VerticalPanel();
	private HorizontalPanel hPanelDialModif3 = new HorizontalPanel();
	VerticalPanel vPanelDailModif = new VerticalPanel();
	TextBox txtDialModifDescRep = new TextBox();
	TextBox txtDialModifPrecioRep = new TextBox();
	TextBox txtDialModifNumeroParteRep = new TextBox();

	Button btnAceptarModif = new Button("Aceptar",new ClickHandler()
	{
		public void onClick(ClickEvent event) 
		{
			dialogBoxModif.hide(true);
			glass.hide();
			modificarRepuestoI();
		}
	});

	Button btnCancelModif = new Button("Cancelar",new ClickHandler() 
	{
		public void onClick(ClickEvent event) 
		{
			dialogBoxModif.hide(true);
			glass.hide();
		}
	});
	//====================================================

	//CONSTRUCTOR
	public IUGestionarRepuesto(Persona persona){
		setearWidgets();			//Setea el tamano de los Widgets.
		cargarPanelesConWidgets();	//Agrega los Widget a los paneles.

		idTipoDatoSel = listBoxTiposRepuestos.getSelectedIndex();
		cargarLtBoxTipoRepuesto(popUp);

		listBoxRepuestos.addChangeHandler(new ChangeHandler(){
			public void onChange(ChangeEvent event) {
				cargarDatos();				
			}
		});	

		bBorrar.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				limpiarCampos();
			}
		});

		cargarGrilla();
		color();
		this.vPanelPrincipal.add(hPanelPrincipal);
	}

	protected void eventos() {
		listBoxTiposRepuestos.addChangeHandler(new ChangeHandler()
		{
			public void onChange(ChangeEvent arg0) 
			{
				limpiarCampos();
			}
		});
	}

	protected void limpiarCampos() {
		this.tDescRepuesto.setText("");
		this.tNroSerie.setText("");
		this.tPrecio.setText("");
		cargarListBoxPorTipo(repuestosMemoria);
	}

	protected void cargarDatos() {
		if(this.listBoxRepuestos.getSelectedIndex() >= 0)
		{
			int id = Integer.valueOf(this.listBoxRepuestos.getValue(this.listBoxRepuestos.getSelectedIndex()));
			ProyectoBilpa.greetingService.buscarRepuesto(id, new AsyncCallback<Repuesto>() {
				public void onSuccess(Repuesto result) {
					if (result != null){
						tDescRepuesto.setText(result.getDescripcion());
						tNroSerie.setText(result.getNroSerie());
						tPrecio.setText(result.getPrecio() + "");
					}else{
						ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar el Repuesto!");
						vpu.showPopUp();
					}				
				}

				public void onFailure(Throwable caught) {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al modificar el Repuesto " + caught.getMessage());
					vpu.showPopUp();
				}
			});
		}
	}

	private void color() {
		htmlTitulo.setStyleName("Titulo");
		lblDescRep.setStyleName("Negrita");
		lblNroSerie.setStyleName("Negrita");
		lblPrecio.setStyleName("Negrita");
		lblTipo.setStyleName("Negrita");
	}

	private void setearWidgets() {
		this.htmlTitulo.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		this.vPanelPrincipal.setWidth("800px");
		this.bGuardar.setWidth("100px");
		this.bModificar.setWidth("100px");
		this.bBorrar.setWidth("100px");
		listBoxRepuestos.setWidth("500px");
		listBoxRepuestos.setVisibleItemCount(10);
		listBoxTiposRepuestos.setWidth("100%");

		bGuardar.setTitle("Ingrese la descripción del Repuesto y su N° de serie y presione Guardar");
		bModificar.setTitle("Seleccione un Repuesto y presione Modificar para cambiar sus datos");

		tDescRepuesto.setTitle("Ingrese la Descripción del Repuesto");
		tNroSerie.setTitle("Ingrese el N° de serie del Repuesto");
		tPrecio.setTitle("Ingrese el Precio Unitario del Repuesto");

		hPanelDatos.setSpacing(5);

	}

	private void cargarPanelesConWidgets() {
		hPanelDatos.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		vPanel1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		this.vPanelPrincipal.add(htmlTitulo);
		vPanel1.add(hPanelDatos);
		hPanelDatos.add(decorator);
		vPanel1.setSpacing(20);
		this.vPanel1.add(listBoxRepuestos);

		this.vPanel2.add(bGuardar);
		this.vPanel2.setSpacing(20);
		this.vPanel2.add(bBorrar);
		this.vPanel2.add(bModificar);

		this.hPanelContenedor.add(vPanel1);
		this.hPanelContenedor.add(vPanel2);

		this.vPanelPrincipal.add(hPanelContenedor);

		this.listBoxRepuestos.setFocus(false);
		this.listBoxRepuestos.setTitle("Lista de Repuestos");

		bGuardar.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				guardarNuevoRepuesto();
			}
		});

		bModificar.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				cargarDialModif_1();
			}
		});	
	}

	public void cargarLtBoxRepuestos(){
		listBoxRepuestos.clear();
		popUp.show();
		ProyectoBilpa.greetingService.obtenerTodosLosRepuestos(new AsyncCallback<ArrayList<Repuesto>>() {

			public void onFailure(Throwable caught) {
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener todos los Repuestos");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<Repuesto> result) {

				Repuesto auxiliar;
				for (int i=0; i<result.size();i++){
					auxiliar = (Repuesto) result.get(i);
					listBoxRepuestos.addItem(auxiliar.datosRepuesto(),String.valueOf(auxiliar.getId()));
				}	
				tDescRepuesto.setText("");
				tNroSerie.setText("");
				tPrecio.setText("");
				popUp.hide();
			}
		});		
	}

	private void guardarNuevoRepuesto(){
		String descripcion = this.tDescRepuesto.getText();
		String nroSerie = this.tNroSerie.getText();
		if(!descripcion.equalsIgnoreCase("") && !descripcion.trim().equalsIgnoreCase("")){

			if(validarPrecio(tPrecio.getText())){	
				ProyectoBilpa.greetingService.validarNuevoRepuesto(descripcion, nroSerie, new AsyncCallback<Integer>() {

					public void onSuccess(Integer result) {
						if (result == 0){
							guardarRepuesto();

						}else if(result == 1){
							tDescRepuesto.setText("");
							tNroSerie.setText("");
							tPrecio.setText("");
							ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Ya existe un repuesto con esa descripción!");
							vpu.showPopUp();

						}else if (result == 2){
							tDescRepuesto.setText("");
							tNroSerie.setText("");
							tPrecio.setText("");
							ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Ya existe un repuesto con ese Nro de Serie!");
							vpu.showPopUp();
						}
					}

					public void onFailure(Throwable caught) {
						ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al validar el repuesto");
						vpu.showPopUp();
					}
				});
			}

		}else{
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe ingresar una descripción");
			vpu.showPopUp();
		}
	}

	private boolean validarPrecio(String precio)
	{
		if(!precio.equalsIgnoreCase("") && !precio.trim().equalsIgnoreCase("")){
			try {
				double precioAux = Double.parseDouble(precio);
				if(precioAux >= 0){
					return true;
				}else{
					ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "El precio debe ser mayor que cero");
					vpu.showPopUp();
					return false;
				}
			} catch (Exception e) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe ingresar un valor valido para el precio del repuesto");
				vpu.showPopUp();
				return false;
			}
		}else{
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe ingresar un precio al repuesto");
			vpu.showPopUp();
		}
		return false;
	}

	public void guardarRepuesto(){

		if (Integer.valueOf(listBoxTiposRepuestos.getValue(listBoxTiposRepuestos.getSelectedIndex())) > -1)
		{
			final String descripcion = this.tDescRepuesto.getText();
			final String nroSerie = this.tNroSerie.getText();
			final double precio = Double.parseDouble(tPrecio.getText());

			if(!descripcion.equalsIgnoreCase("")){
				final Repuesto repuesto = new Repuesto();
				repuesto.setDescripcion(descripcion);
				repuesto.setNroSerie(nroSerie);
				repuesto.setPrecio(precio);
				repuesto.setTipoRepuesto(buscarTipoRepuesto(listBoxTiposRepuestos));

				ProyectoBilpa.greetingService.agregarRepuesto(repuesto, new AsyncCallback<Boolean>() {

					public void onSuccess(Boolean result) {
						if (result) {
							limpiarCampos();
							idTipoDatoSel = listBoxTiposRepuestosMod.getSelectedIndex();
							cargarLtBoxTipoRepuesto(popUp);
							tNroSerie.setFocus(true);
						} else {
							ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar el repuesto");
							vpu.showPopUp();
						}

					}

					public void onFailure(Throwable caught) {
						ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar el repuesto");
						vpu.showPopUp();
					}
				});
			}
		}
		else
		{
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe seleccionar otro tipo de repuesto");
			vpu.showPopUp();
		}
	}

	private void cargarGrilla(){
		decorator.setSize("100%", "100%");
		grillaIngreso.setWidth("488px");

		grillaIngreso.setCellSpacing(5);
		grillaIngreso.setWidget(0, 0, lblNroSerie);
		grillaIngreso.setWidget(0, 1, tNroSerie);
		grillaIngreso.setWidget(1, 0, lblDescRep);
		grillaIngreso.setWidget(1, 1, tDescRepuesto);
		grillaIngreso.setWidget(2, 0, lblPrecio);
		grillaIngreso.setWidget(2, 1, tPrecio);
		grillaIngreso.setWidget(3, 0, lblTipo);
		grillaIngreso.setWidget(3, 1, listBoxTiposRepuestos );

		tNroSerie.setWidth("100%");
		tDescRepuesto.setWidth("100%");
		tPrecio.setWidth("100%");
		decorator.add(grillaIngreso);
	}

	//MODIFICAR REPUESTO==========================================================================================================================
	public void cargarDialModif_1(){
		if (listBoxRepuestos.getSelectedIndex() != -1)
		{
			int id = Integer.valueOf(listBoxRepuestos.getValue(listBoxRepuestos.getSelectedIndex()));
			ProyectoBilpa.greetingService.buscarRepuesto(id, new AsyncCallback<Repuesto>() {
				public void onSuccess(Repuesto result) 
				{
					if (result != null)
					{
						glass.show();
						DialogBox db = crearDialogoModificacionRepuesto(result);
						db.center();
						db.show();
					}
					else
					{
						ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar repuesto");
						vpu.showPopUp();
					}				
				}

				public void onFailure(Throwable caught) 
				{
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar repuesto: " + caught.getMessage());
					vpu.showPopUp();
				}
			});
		}
	}
	public DialogBox crearDialogoModificacionRepuesto(Repuesto repuesto){

		dialogBoxModif.setTitle("Modificacion de Repuesto");
		vPanelDailModif.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		vPanelDailModif.setSpacing(10);
		vPanelDialModif1.setSpacing(10);
		vPanelDialModif2.setSpacing(10);

		txtDialModifDescRep.setWidth("300px");
		txtDialModifNumeroParteRep.setWidth("300px");
		txtDialModifPrecioRep.setWidth("300px");

		btnAceptarModif.setWidth("100px");
		btnCancelModif.setWidth("100px");

		listBoxTiposRepuestosMod.clear();
		listBoxTiposRepuestosMod.setWidth("300px");

		lblDescripcionModif.setStyleName("Negrita");
		lblDialModfNroParte.setStyleName("Negrita");
		lblDialModifPrecio.setStyleName("Negrita");
		lblTituloTiposRepuestosModif.setStyleName("Negrita");

		txtDialModifDescRep.setText(repuesto.getDescripcion());
		txtDialModifPrecioRep.setText(repuesto.getPrecio()+"");
		txtDialModifNumeroParteRep.setText(repuesto.getNroSerie());

		vPanelDialModif1.add(lblDialModfNroParte);
		vPanelDialModif1.add(txtDialModifNumeroParteRep);

		vPanelDialModif1.add(lblDescripcionModif);
		vPanelDialModif1.add(txtDialModifDescRep);

		vPanelDialModif1.add(lblDialModifPrecio);
		vPanelDialModif1.add(txtDialModifPrecioRep);

		vPanelDialModif2.add(lblTituloTiposRepuestosModif);
		vPanelDialModif2.add(listBoxTiposRepuestosMod);

		hPanelDialModif3.add(btnCancelModif);
		hPanelDialModif3.setSpacing(5);
		hPanelDialModif3.add(btnAceptarModif);

		vPanelDailModif.add(vPanelDialModif1);
		vPanelDailModif.add(vPanelDialModif2);
		vPanelDailModif.add(hPanelDialModif3);

		dialogBoxModif.setWidget(vPanelDailModif);

		setearListBoxTipTarModif(repuesto);
		return dialogBoxModif;
	}

	private void setearListBoxTipTarModif(Repuesto repuesto) 
	{
		listBoxTiposRepuestosMod.clear();
		for (TipoRepuesto tipo : tiposRepuestosMemoria)
		{
			listBoxTiposRepuestosMod.addItem(tipo.toString(),String.valueOf(tipo.getId()));
		}

		int selectItem = 0;
		for (int i = 0 ; i < listBoxTiposRepuestosMod.getItemCount() ; i ++)
		{
			if (Integer.valueOf(listBoxTiposRepuestosMod.getValue(i)) == repuesto.getTipoRepuesto().getId())
			{
				selectItem = i;
			}
		}
		listBoxTiposRepuestosMod.setSelectedIndex(selectItem);
	}

	private void modificarRepuestoI(){

		if(listBoxRepuestos.getSelectedIndex() != -1){
			final String descDial = txtDialModifDescRep.getText();
			final String nroSerieDial = txtDialModifNumeroParteRep.getText();
			final String precio = txtDialModifPrecioRep.getText();

			int id = Integer.valueOf(this.listBoxRepuestos.getValue(this.listBoxRepuestos.getSelectedIndex()));

			ProyectoBilpa.greetingService.buscarRepuesto(id, new AsyncCallback<Repuesto>() {
				public void onSuccess(Repuesto result) {
					if (result != null){
						modificarRepuestoII(result, descDial, nroSerieDial, Double.valueOf(precio));
					}			
				}

				public void onFailure(Throwable caught) {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar el repuesto: " + caught.toString());
					vpu.showPopUp();
				}
			});
		}
	}

	private void modificarRepuestoII(final Repuesto repuesto, final String desc, final String nroSerie, final double precio) {
		if(repuesto != null){
			ProyectoBilpa.greetingService.validarNuevoRepuesto(desc, nroSerie, new AsyncCallback<Integer>() {
				public void onSuccess(Integer result) {
					if(result == 1 && !repuesto.getDescripcion().equals(desc)){
						ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Ya existe un repuesto con esa descripción");
						vpu.showPopUp();

					}else if (result == 2 && !repuesto.getNroSerie().equals(nroSerie)){
						ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Ya existe un repuesto con ese nro de serie");
						vpu.showPopUp();

					}else if (result == 0 || (result == 1 && repuesto.getDescripcion().equals(desc) ||  (result == 2 && repuesto.getNroSerie().equals(nroSerie)))){						

						if(validarPrecio(txtDialModifPrecioRep.getText())){

							repuesto.setDescripcion(desc);
							repuesto.setNroSerie(nroSerie);
							repuesto.setPrecio(Double.valueOf(precio));
							repuesto.setTipoRepuesto(buscarTipoRepuesto(listBoxTiposRepuestosMod));
							modificarRepuestoBase(repuesto);		
						}			
					}			
				}

				public void onFailure(Throwable caught) {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al validar el repuesto!");
					vpu.showPopUp();
				}

			});
		}

	}

	private void modificarRepuestoBase(Repuesto repuesto){
		if(repuesto != null){
			ProyectoBilpa.greetingService.modificarRepuestoBase(repuesto, new AsyncCallback<Boolean>() {
				public void onFailure(Throwable caught) {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al modificar el repuesto");
					vpu.showPopUp();
				}

				public void onSuccess(Boolean result) {
					limpiarCampos();
					idTipoDatoSel = listBoxTiposRepuestosMod.getSelectedIndex();
					cargarLtBoxTipoRepuesto(popUp);
				}
			});
		}
	}

	///////////////////////////////////////
}