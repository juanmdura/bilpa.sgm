package app.client.iu.falla;

import java.util.ArrayList;

import app.client.ProyectoBilpa;
import app.client.dominio.FallaReportada;
import app.client.dominio.FallaTecnica;
import app.client.dominio.FallaTipo;
import app.client.dominio.Persona;
import app.client.dominio.SubTipoFalla;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.UtilOrden;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
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
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;


public class IUGestionarFalla extends IUWidgetFalla{

	private HTML htmlTitulo = new HTML("Gestión de fallas");

	private Label lblTituloTiposFallaT = new Label("Tipo de falla");
	private Label lblTituloTiposFallaR = new Label("Tipo de falla");
	private Label lblTituloFallaT = new Label("Fallas detectadas");
	private Label lblTituloFallaR = new Label("Fallas reportadas");
	private Label lblTituloDescFallaT = new Label("Descripción");
	private Label lblTituloDescFallaR = new Label("Descripción");
	
	
	private Persona sesion;

	private TextBox txDescT = new TextBox();
	private TextBox txDescR = new TextBox();

	private Button btnAgregarT = new Button("Agregar");
	private Button btnModificarT = new Button("Modificar");
	private Button bBorrarT = new Button("Limpiar");

	private Button btnModificarR = new Button("Modificar");
	private Button btnAgregarR = new Button("Agregar");
	private Button bBorrarR = new Button("Limpiar");

	private DecoratedTabPanel tabPTipos = new DecoratedTabPanel();
	private VerticalPanel vPanelPrincipal = new VerticalPanel();

	//=====================================================================	
	//Pop Up Fallas Tecnicas
	private DialogBox dialogBoxT = new DialogBox();	
	Label lblDescDialT = new Label("Descripción");
	Label lblTipoDialT = new Label("Tipo de falla detectada");
	TextBox txDescDialT = new TextBox();
	public ListBox listBoxTiposDialFallasT = new ListBox();
	VerticalPanel vPanelDialT = new VerticalPanel();
	VerticalPanel vPanelDialT1 = new VerticalPanel();
	VerticalPanel vPanelDialT2 = new VerticalPanel();
	HorizontalPanel hPanelDialT2 = new HorizontalPanel();
	//=====================================================================

	//=====================================================================	
	//Pop Up Fallas Reportadas	
	private DialogBox dialogBoxR = new DialogBox();	
	Label lblDescDialR = new Label("Descripción");
	Label lblTipoDialR = new Label("Tipo de falla reportada");
	TextBox txDescDialR = new TextBox();
	public ListBox listBoxTiposDialFallasR = new ListBox();
	VerticalPanel vPanelDialR = new VerticalPanel();
	VerticalPanel vPanelDialR1 = new VerticalPanel();
	VerticalPanel vPanelDialR2 = new VerticalPanel();
	HorizontalPanel hPanelDialR2 = new HorizontalPanel();
	//=====================================================================

	public VerticalPanel getVpPrincipal(){
		return this.vPanelPrincipal;
	}

	private VerticalPanel vpFallasT = new VerticalPanel();
	private VerticalPanel vpFallasT2 = new VerticalPanel();
	private HorizontalPanel hpFallasT = new HorizontalPanel();

	private VerticalPanel vpFallasR = new VerticalPanel();
	private VerticalPanel vpFallasR2 = new VerticalPanel();
	private HorizontalPanel hpFallasR = new HorizontalPanel();

	private PopupCargando popUp = new PopupCargando("Cargando...");
	private GlassPopup glass = new GlassPopup();
	
	private Button btnOkDialR = new Button("OK", new ClickHandler() {
		public void onClick(ClickEvent event) {
			dialogBoxR.hide(true);
			glass.hide();
			modificarFallaR();
		}
	});

	private Button btnCancelDialR = new Button("Cancelar", new ClickHandler() {
		public void onClick(ClickEvent event) {
			dialogBoxR.hide(true);
			glass.hide();
		}
	});

	Button btnOkDialT = new Button("OK", new ClickHandler() {
		public void onClick(ClickEvent event) {
			dialogBoxT.hide(true);
			glass.hide();
			modificarFallaT();
		}
	});

	Button btnCancelDialT = new Button("Cancelar", new ClickHandler() {
		public void onClick(ClickEvent event) {
			dialogBoxT.hide(true);
			glass.hide();
		}
	});

	//CONSTRUCTOR
	public IUGestionarFalla(Persona persona) {
		this.sesion = persona;
		//Layout	
		setearWidgets();

		//Tecnicas
		cargarTabInicialT();
		cargarLtBoxTipoFallaT(popUp, listBoxTiposFallasT);

		//Reportadas		
		cargarTabInicialR();
		cargarLtBoxTipoFallaR(popUp, listBoxTiposFallasR);

		color();
		tabPTipos.selectTab(0);
		vPanelPrincipal.add(htmlTitulo);
		vPanelPrincipal.add(tabPTipos);
	}

	private void color() {
		htmlTitulo.setStyleName("Titulo");
	}

	public void setearWidgets(){

		htmlTitulo.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		lblTituloTiposFallaR.setStyleName("Negrita");
		lblTituloTiposFallaT.setStyleName("Negrita");
		lblTituloFallaR.setStyleName("Negrita");
		lblTituloFallaT.setStyleName("Negrita");
		lblTituloDescFallaR.setStyleName("Negrita");
		lblTituloDescFallaT.setStyleName("Negrita");
		
		listBoxTiposFallasR.setWidth("500px");
		listBoxTiposFallasT.setWidth("500px");
		
		tabPTipos.setWidth("600px");
		tabPTipos.setAnimationEnabled(true);

		btnAgregarR.setTitle("Ingrese la descripción de la Falla Reportada y presione Agregar");
		btnAgregarT.setTitle("Ingrese la descripción de la Falla Detectada y presione Agregar");

		btnModificarR.setTitle("Seleccione una Falla Reportada y presione Modificar para cambiar su descripción");
		btnModificarT.setTitle("Seleccione una Falla Detectada y presione Modificar para cambiar su descripción");
	
		txDescT.setWidth("500px");
		txDescR.setWidth("500px");
		txDescT.setTitle("Ingrese la Descripción de la Falla Detectada y presione Agregar");
		txDescR.setTitle("Ingrese la Descripción de la Falla Reportada y presione Agregar");
		
		listBoxFallasT.setWidth("500px");
		listBoxFallasR.setWidth("500px");
		listBoxFallasT.setVisibleItemCount(10);
		listBoxFallasR.setVisibleItemCount(10);
		listBoxFallasT.setTitle("Lista de Fallas Detectadas");
		listBoxFallasR.setTitle("Lista de Fallas Reportadas");
		
		btnAgregarT.setWidth("100px");
		btnModificarT.setWidth("100px");
		bBorrarT.setWidth("100px");
			
		btnAgregarR.setWidth("100px");
		btnModificarR.setWidth("100px");
		bBorrarR.setWidth("100px");
	}

	public void ModificarFallaT(){
		popUp.show();
		if (this.listBoxFallasT.getSelectedIndex() != -1){
			glass.show();
			int id = Integer.valueOf(listBoxFallasT.getValue(this.listBoxFallasT.getSelectedIndex()));
			ProyectoBilpa.greetingService.buscarFallaT(id, new AsyncCallback<FallaTecnica>() {
				public void onSuccess(FallaTecnica falla) {
					DialogBox db = modificacionFallaT(falla);
					db.center();
					db.show();
					popUp.hide();
				}
				public void onFailure(Throwable caught) {
					popUp.hide();
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al modificar falla " + caught.getMessage());
					vpu.showPopUp();
				}
			});
		}
		else
		{
			popUp.hide();
		}
	}

	public void ModificarFallaR()
	{
		popUp.show();
		if (this.listBoxFallasR.getSelectedIndex() != -1)
		{
			glass.show();
			int id = Integer.valueOf(listBoxFallasR.getValue(this.listBoxFallasR.getSelectedIndex()));
			if (id == UtilOrden.idFallaPendientes){
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "No es posible modificar la falla: ELIMINACIÓN DE PENDIENTE");
				vpu.showPopUp();
			}
			ProyectoBilpa.greetingService.buscarFallaReportada(id, new AsyncCallback<FallaReportada>() {
				public void onSuccess(FallaReportada falla) {
					DialogBox db = modificacionFallaR(falla);
					db.center();
					db.show();
					popUp.hide();
				}
				public void onFailure(Throwable caught) {
					popUp.hide();
					glass.hide();
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al modificar falla " + caught.getMessage());
					vpu.showPopUp();
				}
			});
		}
		else
		{
			popUp.hide();
		}
	}


	public void agregarFallaT()
	{
		popUp.show();
		final String descripcion = this.txDescT.getText();
		if(!descripcion.equalsIgnoreCase("") && !descripcion.trim().equalsIgnoreCase("")){
			final FallaTecnica fallaT = new FallaTecnica();
			fallaT.setDescripcion(descripcion);
			fallaT.setSubTipo(Integer.valueOf(listBoxTiposFallasT.getValue(listBoxTiposFallasT.getSelectedIndex())));
			
			ProyectoBilpa.greetingService.agregarFallaTecnica(fallaT, new AsyncCallback<Boolean>() {
				public void onSuccess(Boolean result) {
					if (result) {
						cargarLtBoxTipoFallaT(popUp, listBoxTiposFallasT);
						txDescT.setText("");
					} else {
						popUp.hide();
						ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Ya existe una falla con esa descripción");
						vpu.showPopUp();
					}
				}

				public void onFailure(Throwable caught) {
					popUp.hide();
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar la falla");
					vpu.showPopUp();
				}
			});
		}
		else
		{
			popUp.hide();
		}
	}

	public void agregarFallaR()
	{
		popUp.show();
		final String descripcion = this.txDescR.getText();
		if(!descripcion.equalsIgnoreCase("") && !descripcion.trim().equalsIgnoreCase("")){
			final FallaReportada fallaR = new FallaReportada();
			fallaR.setDescripcion(descripcion);
			fallaR.setSubTipo(Integer.valueOf(listBoxTiposFallasR.getValue(listBoxTiposFallasR.getSelectedIndex())));
			
			ProyectoBilpa.greetingService.agregarFallaReportada(fallaR, new AsyncCallback<Boolean>() {

				public void onSuccess(Boolean result) {
					if (result) {
						cargarLtBoxTipoFallaR(popUp, listBoxTiposFallasR);
						txDescR.setText("");
					} else {
						popUp.hide();
						ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Ya existe una falla con esa descripción");
						vpu.showPopUp();
					}
				}

				public void onFailure(Throwable caught) {
					popUp.hide();
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar la falla");
					vpu.showPopUp();
				}
			});
		}
		else
		{
			popUp.hide();
		}
	}

	public DialogBox modificacionFallaT(FallaTecnica falla){

		dialogBoxT.setTitle("Modificación de falla detectada");

		btnOkDialT.setWidth("100px");
		btnCancelDialT.setWidth("100px");

		txDescDialT.setWidth("400px");
		listBoxTiposDialFallasT.setWidth("400px");
		
		lblDescDialT.setStyleName("Negrita");
		lblTipoDialT.setStyleName("Negrita");
		
		txDescDialT.setText(falla.getDescripcion());
		
		vPanelDialT1.add(lblDescDialT);
		vPanelDialT1.add(txDescDialT);

		vPanelDialT2.add(lblTipoDialT);
		vPanelDialT2.add(listBoxTiposDialFallasT);
		
		hPanelDialT2.add(btnCancelDialT);
		hPanelDialT2.setSpacing(5);		
		hPanelDialT2.add(btnOkDialT);

		vPanelDialT.setSpacing(10);
		
		vPanelDialT.add(vPanelDialT1);
		vPanelDialT.add(vPanelDialT2);
		
		vPanelDialT.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelDialT.add(hPanelDialT2);

		dialogBoxT.setWidget(vPanelDialT);

		setearLbxFallas(falla, listBoxTiposDialFallasT, tiposFallasMemoriaT);
		return dialogBoxT;
	}
	
	public DialogBox modificacionFallaR(FallaReportada falla){

		dialogBoxR.setTitle("Modificación de falla reportada");

		btnOkDialR.setWidth("100px");
		btnCancelDialR.setWidth("100px");

		txDescDialR.setWidth("400px");
		listBoxTiposDialFallasR.setWidth("400px");
		
		lblDescDialR.setStyleName("Negrita");
		lblTipoDialR.setStyleName("Negrita");
		
		txDescDialR.setText(falla.getDescripcion());
		
		vPanelDialR1.add(lblDescDialR);
		vPanelDialR1.add(txDescDialR);

		vPanelDialR2.add(lblTipoDialR);
		vPanelDialR2.add(listBoxTiposDialFallasR);
		
		hPanelDialR2.add(btnCancelDialR);
		hPanelDialR2.setSpacing(5);		
		hPanelDialR2.add(btnOkDialR);

		vPanelDialR.setSpacing(10);
		
		vPanelDialR.add(vPanelDialR1);
		vPanelDialR.add(vPanelDialR2);
		
		vPanelDialR.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelDialR.add(hPanelDialR2);

		dialogBoxR.setWidget(vPanelDialR);

		setearLbxFallas(falla, listBoxTiposDialFallasR, tiposFallasMemoriaR);
		return dialogBoxR;
	}

	public void modificarFallaR()
	{
		final String descDialR = txDescDialR.getText();
		final int idTipoFallaR = Integer.valueOf(listBoxTiposDialFallasR.getValue(listBoxTiposDialFallasR.getSelectedIndex()));
		if (this.listBoxFallasR.getSelectedIndex() != -1)
		{
			int id = Integer.valueOf(this.listBoxFallasR.getValue(this.listBoxFallasR.getSelectedIndex()));

			ProyectoBilpa.greetingService.buscarFallaReportada(id, new AsyncCallback<FallaReportada>() {
				public void onSuccess(FallaReportada fallaAModificar)
				{
					if (fallaAModificar != null){
						fallaAModificar.setDescripcion(descDialR);
						fallaAModificar.setSubTipo(idTipoFallaR);
						
						ProyectoBilpa.greetingService.modificarFallaReportada(fallaAModificar, new AsyncCallback<Boolean>() {

							public void onSuccess(Boolean valido) 
							{
								if (valido) 
								{
									txDescR.setText("");
									cargarLtBoxTipoFallaR(popUp, listBoxTiposFallasR);
								}
								else 
								{
									ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Ya existe una falla con esa descripción");
									vpu.showPopUp();
								}
							}
							
							public void onFailure(Throwable caught) 
							{
								ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al modificar la falla");
								vpu.showPopUp();
							}
						});
					}				
				}
				
				public void onFailure(Throwable caught) 
				{
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al modificar la falla");
					vpu.showPopUp();
				}
			});
		}
	}
	
	public void modificarFallaT()
	{
		final String descDialT = txDescDialT.getText();
		final int idTipoFallaT = Integer.valueOf(listBoxTiposDialFallasT.getValue(listBoxTiposDialFallasT.getSelectedIndex()));
		if (this.listBoxFallasT.getSelectedIndex() != -1)
		{
			int id = Integer.valueOf(this.listBoxFallasT.getValue(this.listBoxFallasT.getSelectedIndex()));

			ProyectoBilpa.greetingService.buscarFallaT(id, new AsyncCallback<FallaTecnica>() {
				public void onSuccess(FallaTecnica fallaAModificar)
				{
					if (fallaAModificar != null){
						fallaAModificar.setDescripcion(descDialT);
						fallaAModificar.setSubTipo(idTipoFallaT);
						
						ProyectoBilpa.greetingService.modificarFallaTecnica(fallaAModificar, new AsyncCallback<Boolean>() {

							public void onSuccess(Boolean valido) 
							{
								if (valido) 
								{
									txDescT.setText("");
									cargarLtBoxTipoFallaT(popUp, listBoxTiposFallasT);
								}
								else 
								{
									ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Ya existe una falla con esa descripción");
									vpu.showPopUp();
								}
							}
							
							public void onFailure(Throwable caught) 
							{
								ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al modificar la falla");
								vpu.showPopUp();
							}
						});
					}				
				}
				
				public void onFailure(Throwable caught) 
				{
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al modificar la falla");
					vpu.showPopUp();
				}
			});
		}
	}

	public void cargarTabInicialT(){
		tabPTipos.add(hpFallasT, "Fallas Detectadas");
		hpFallasT.setTitle("Fallas detectadas por un técnico");

		HorizontalPanel hpFallasT2 = new HorizontalPanel();
		HorizontalPanel hpFallasT3 = new HorizontalPanel();
		HorizontalPanel hpFallasT4 = new HorizontalPanel();
		
		HorizontalPanel hpFallasT8 = new HorizontalPanel();
		HorizontalPanel hpFallasT9 = new HorizontalPanel();
		HorizontalPanel hpFallasT10 = new HorizontalPanel();
		
		VerticalPanel vpFallasT5 = new VerticalPanel();
		VerticalPanel vpFallasT6 = new VerticalPanel();
		VerticalPanel vpFallasT7 = new VerticalPanel();
		
		HorizontalPanel hpFallasSpT2 = new HorizontalPanel();
		HorizontalPanel hpFallasSpT3 = new HorizontalPanel();
		HorizontalPanel hpFallasSpT4 = new HorizontalPanel();
		HorizontalPanel hpFallasSpT5 = new HorizontalPanel();
		HorizontalPanel hpFallasSpT6 = new HorizontalPanel();
		HorizontalPanel hpFallasSpT7 = new HorizontalPanel();
		
		hpFallasSpT2.setWidth("50px");
		hpFallasSpT3.setWidth("50px");
		hpFallasSpT4.setWidth("50px");
		hpFallasSpT5.setWidth("5px");
		hpFallasSpT6.setWidth("5px");
		hpFallasSpT7.setWidth("5px");
		
		vpFallasT.setSpacing(10);
		hpFallasT2.setSpacing(5);
		hpFallasT3.setSpacing(5);
		hpFallasT4.setSpacing(5);
		
		vpFallasT.add(vpFallasT5);
		vpFallasT.add(vpFallasT6);
		vpFallasT.add(vpFallasT7);
		
		hpFallasT8.add(hpFallasSpT5);
		hpFallasT8.add(lblTituloDescFallaT);
		hpFallasT2.add(txDescT);
		hpFallasT2.add(hpFallasSpT2);
		hpFallasT2.add(btnAgregarT);
		
		vpFallasT5.add(hpFallasT8);
		vpFallasT5.add(hpFallasT2);
		

		hpFallasT9.add(hpFallasSpT6);
		hpFallasT9.add(lblTituloTiposFallaT);
		hpFallasT3.add(listBoxTiposFallasT);
		hpFallasT3.add(hpFallasSpT3);
		hpFallasT3.add(bBorrarT);

		vpFallasT6.add(hpFallasT9);
		vpFallasT6.add(hpFallasT3);
		
		
		hpFallasT10.add(hpFallasSpT7);
		hpFallasT10.add(lblTituloFallaT);
		hpFallasT4.add(listBoxFallasT);
		hpFallasT4.add(hpFallasSpT4);
		hpFallasT4.add(btnModificarT);
		
		vpFallasT7.add(hpFallasT10);
		vpFallasT7.add(hpFallasT4);

		hpFallasT.add(vpFallasT);

		btnModificarT.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event) {
				ModificarFallaT();
			}
		});


		btnAgregarT.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event) {
				agregarFallaT();
			}
		});
		
		bBorrarT.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event) {
				limpiarCamposT();
			}

			private void limpiarCamposT() 
			{
				txDescT.setText("");
				cargarLtBoxTipoFallaT(popUp, listBoxTiposFallasT);			
			}
		});
		
		listBoxTiposFallasT.addChangeHandler(new ChangeHandler()
		{
			public void onChange(ChangeEvent arg0) 
			{
				txDescT.setText("");
			}
		});
		
		listBoxFallasT.addChangeHandler(new ChangeHandler()
		{
			public void onChange(ChangeEvent event)
			{
				cargarDatos();				
			}

			private void cargarDatos() 
			{
				if (listBoxFallasT.getSelectedIndex() != -1){
					int id = Integer.valueOf(listBoxFallasT.getValue(listBoxFallasT.getSelectedIndex()));
					ProyectoBilpa.greetingService.buscarFallaT(id, new AsyncCallback<FallaTecnica>() {
						public void onSuccess(FallaTecnica falla) 
						{
							txDescT.setText(falla.getDescripcion());
						}
						
						public void onFailure(Throwable caught) 
						{
							ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar falla " + caught.getMessage());
							vpu.showPopUp();
						}
					});
				}
			}
		});	
	}

	public void cargarTabInicialR(){
		tabPTipos.add(hpFallasR, "Fallas Reportadas");
		hpFallasR.setTitle("Fallas reportadas por un cliente");
		
		HorizontalPanel hpFallasR2 = new HorizontalPanel();
		HorizontalPanel hpFallasR3 = new HorizontalPanel();
		HorizontalPanel hpFallasR4 = new HorizontalPanel();
		
		HorizontalPanel hpFallasR8 = new HorizontalPanel();
		HorizontalPanel hpFallasR9 = new HorizontalPanel();
		HorizontalPanel hpFallasR10 = new HorizontalPanel();
		
		VerticalPanel vpFallasR5 = new VerticalPanel();
		VerticalPanel vpFallasR6 = new VerticalPanel();
		VerticalPanel vpFallasR7 = new VerticalPanel();
		
		HorizontalPanel hpFallasSpR2 = new HorizontalPanel();
		HorizontalPanel hpFallasSpR3 = new HorizontalPanel();
		HorizontalPanel hpFallasSpR4 = new HorizontalPanel();
		HorizontalPanel hpFallasSpR5 = new HorizontalPanel();
		HorizontalPanel hpFallasSpR6 = new HorizontalPanel();
		HorizontalPanel hpFallasSpR7 = new HorizontalPanel();
		
		hpFallasSpR2.setWidth("50px");
		hpFallasSpR3.setWidth("50px");
		hpFallasSpR4.setWidth("50px");
		hpFallasSpR5.setWidth("5px");
		hpFallasSpR6.setWidth("5px");
		hpFallasSpR7.setWidth("5px");
		
		vpFallasR.setSpacing(10);
		hpFallasR2.setSpacing(5);
		hpFallasR3.setSpacing(5);
		hpFallasR4.setSpacing(5);
		
		vpFallasR.add(vpFallasR5);
		vpFallasR.add(vpFallasR6);
		vpFallasR.add(vpFallasR7);
		
		hpFallasR8.add(hpFallasSpR5);
		hpFallasR8.add(lblTituloDescFallaR);
		hpFallasR2.add(txDescR);
		hpFallasR2.add(hpFallasSpR2);
		hpFallasR2.add(btnAgregarR);
		
		vpFallasR5.add(hpFallasR8);
		vpFallasR5.add(hpFallasR2);
		

		hpFallasR9.add(hpFallasSpR6);
		hpFallasR9.add(lblTituloTiposFallaR);
		hpFallasR3.add(listBoxTiposFallasR);
		hpFallasR3.add(hpFallasSpR3);
		hpFallasR3.add(bBorrarR);

		vpFallasR6.add(hpFallasR9);
		vpFallasR6.add(hpFallasR3);
		
		
		hpFallasR10.add(hpFallasSpR7);
		hpFallasR10.add(lblTituloFallaR);
		hpFallasR4.add(listBoxFallasR);
		hpFallasR4.add(hpFallasSpR4);
		hpFallasR4.add(btnModificarR);
		
		vpFallasR7.add(hpFallasR10);
		vpFallasR7.add(hpFallasR4);

		hpFallasR.add(vpFallasR);

		btnModificarR.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event) {
				ModificarFallaR();
			}
		});


		btnAgregarR.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event) {
				agregarFallaR();
			}
		});
		
		bBorrarR.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event) {
				limpiarCamposR();
			}

			private void limpiarCamposR() 
			{
				txDescR.setText("");
				cargarLtBoxTipoFallaR(popUp, listBoxTiposFallasR);			
			}
		});
		
		listBoxTiposFallasR.addChangeHandler(new ChangeHandler()
		{
			public void onChange(ChangeEvent arg0) 
			{
				txDescR.setText("");
			}
		});
		
		listBoxFallasR.addChangeHandler(new ChangeHandler()
		{
			public void onChange(ChangeEvent event)
			{
				cargarDatos();				
			}

			private void cargarDatos() 
			{
				if (listBoxFallasR.getSelectedIndex() != -1){
					int id = Integer.valueOf(listBoxFallasR.getValue(listBoxFallasR.getSelectedIndex()));
					ProyectoBilpa.greetingService.buscarFallaReportada(id, new AsyncCallback<FallaReportada>() {
						public void onSuccess(FallaReportada falla) 
						{
							txDescR.setText(falla.getDescripcion());
						}
						
						public void onFailure(Throwable caught) 
						{
							ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar falla " + caught.getMessage());
							vpu.showPopUp();
						}
					});
				}
			}
		});	
	}
	
	private void setearLbxFallas(FallaTipo falla, ListBox listBoxFalla, ArrayList<? extends SubTipoFalla> tiposFallasMemoria) 
	{
		listBoxFalla.clear();
		for (SubTipoFalla tipo : tiposFallasMemoria)
		{
			listBoxFalla.addItem(tipo.toString(),String.valueOf(tipo.getId()));
		}
		
		int selectItem = 0;
		for (int i = 0 ; i < listBoxFalla.getItemCount() ; i ++)
		{
			if (Integer.valueOf(listBoxFalla.getValue(i)) == falla.getSubTipo())
			{
				selectItem = i;
			}
		}
		listBoxFalla.setSelectedIndex(selectItem);
	}
}
