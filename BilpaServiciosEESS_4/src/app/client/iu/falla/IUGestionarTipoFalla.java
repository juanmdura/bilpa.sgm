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
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class IUGestionarTipoFalla extends Composite{

	private HTML htmlTitulo = new HTML("Gestión de tipos fallas");

	private ListBox ltBFallasT = new ListBox();
	private ListBox ltBFallasR = new ListBox();

	private Persona sesion;

	private TextBox txDescT = new TextBox();
	private TextBox txDescR = new TextBox();

	private Button btnAgregarT = new Button("Agregar");
	private Button btnModificarT = new Button("Modificar");

	private Button btnModificarR = new Button("Modificar");
	private Button btnAgregarR = new Button("Agregar");

	private DecoratedTabPanel tabPTipos = new DecoratedTabPanel();
	private VerticalPanel vPanelPrincipal = new VerticalPanel();

	//=====================================================================	
	//Pop Up Fallas Tecnicas
	private DialogBox dialogBoxT = new DialogBox();		
	private TextBox txDescDialT = new TextBox();
	private VerticalPanel vPanelDialT = new VerticalPanel();
	private HorizontalPanel hPanelDialT = new HorizontalPanel();
	private HorizontalPanel hPanel2DialT = new HorizontalPanel();
	//=====================================================================

	//=====================================================================	
	//Pop Up Fallas Reportadas	
	private DialogBox dialogBoxR = new DialogBox();		
	TextBox txDescDialR = new TextBox();
	VerticalPanel vPanelDialR = new VerticalPanel();
	HorizontalPanel hPanelDialR = new HorizontalPanel();
	HorizontalPanel hPanel2DialR = new HorizontalPanel();
	//=====================================================================

	public VerticalPanel getPrincipalPanel()
	{
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
		public void onClick(ClickEvent event) 
		{
			modificarFallaR();
			dialogBoxR.hide(true);
			glass.hide();
		}
	});

	private Button btnCancelDialR = new Button("Cancelar", new ClickHandler() {
		public void onClick(ClickEvent event) 
		{
			dialogBoxR.hide(true);
			glass.hide();
		}
	});

	Button btnOkDialT = new Button("OK", new ClickHandler() {
		public void onClick(ClickEvent event)
		{
			modificarFallaT();
			dialogBoxT.hide(true);
			glass.hide();
		}
	});

	Button btnCancelDialT = new Button("Cancelar", new ClickHandler() {
		public void onClick(ClickEvent event) 
		{
			dialogBoxT.hide(true);
			glass.hide();
		}
	});

	public IUGestionarTipoFalla(Persona persona) 
	{
		this.sesion = persona;

		setearWidgets();

		//Tecnicas
		cargarTabInicialT();
		cargarLtBoxInicialT();

		//Reportadas		
		cargarTabInicialR();
		cargarLtBoxInicialR();

		color();
		tabPTipos.selectTab(0);
		vPanelPrincipal.add(htmlTitulo);
		vPanelPrincipal.add(tabPTipos);
	}

	private void color() 
	{
		htmlTitulo.setStyleName("Titulo");
	}

	public void setearWidgets()
	{
		htmlTitulo.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

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
		
		ltBFallasT.setWidth("500px");
		ltBFallasR.setWidth("500px");
		ltBFallasT.setVisibleItemCount(10);
		ltBFallasR.setVisibleItemCount(10);
		ltBFallasR.setTitle("Lista de Fallas Reportadas");
		ltBFallasT.setTitle("Lista de Fallas Detectadas");
		btnAgregarT.setWidth("100px");
		btnModificarT.setWidth("100px");

		btnAgregarR.setWidth("100px");
		btnModificarR.setWidth("100px");
	}

	public void ModificarFallaT()
	{
		if (this.ltBFallasT.getSelectedIndex() != -1)
		{
			glass.show();
			int id = Integer.valueOf(this.ltBFallasT.getValue(this.ltBFallasT.getSelectedIndex()));

			ProyectoBilpa.greetingService.buscarTipoFallaT(id, new AsyncCallback<TipoFallaTecnica>() {
				public void onSuccess(TipoFallaTecnica result) 
				{
					if (result != null)
					{
						DialogBox db = crearDialogoModificacionFallaT(result);
						db.center();
						db.show();
					}				
				}

				public void onFailure(Throwable caught) 
				{
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al modificar Falla " + caught.getMessage());
					vpu.showPopUp();
				}
			});			
		}
	}

	public void ModificarFallaR()
	{
		if (this.ltBFallasR.getSelectedIndex() != -1)
		{
			glass.show();
			int id = Integer.valueOf(ltBFallasR.getValue(this.ltBFallasR.getSelectedIndex()));
			
			ProyectoBilpa.greetingService.buscarTipoFallaR(id, new AsyncCallback<TipoFallaReportada>() {
				
				public void onSuccess(TipoFallaReportada result) 
				{
					DialogBox db = crearDialogoModificacionFallaR(result.getDescripcion());
					db.center();
					db.show();
				}
				
				public void onFailure(Throwable caught) 
				{
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al modificar Falla " + caught.getMessage());
					vpu.showPopUp();
				}
			});
		}
	}

	public void agregarFallaT()
	{
		final String descripcion = this.txDescT.getText();
		if(!descripcion.equalsIgnoreCase("") && !descripcion.trim().equalsIgnoreCase("")){
			final TipoFallaTecnica fallaT = new TipoFallaTecnica();
			fallaT.setDescripcion(descripcion);

			ProyectoBilpa.greetingService.agregarTipoFallaT(fallaT, new AsyncCallback<Boolean>() {

				public void onSuccess(Boolean result) 
				{
					if (result) 
					{
						txDescT.setText("");
						cargarLtBoxInicialT();
					} 
					else 
					{
						ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Ya existe un tipo de falla técnica con ese nombre");
						vpu.showPopUp();
					}
				}

				public void onFailure(Throwable caught) 
				{
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar la falla");
					vpu.showPopUp();
				}
			});
		}
	}

	public void agregarFallaR()
	{
		final String descripcion = this.txDescR.getText();
		if(!descripcion.equalsIgnoreCase("") && !descripcion.trim().equalsIgnoreCase(""))
		{
			final TipoFallaReportada fallaR = new TipoFallaReportada();
			fallaR.setDescripcion(descripcion);

			ProyectoBilpa.greetingService.agregarTipoFallaR(fallaR, new AsyncCallback<Boolean>() {
				public void onSuccess(Boolean result) 
				{
					if (result) {
						cargarLtBoxInicialR();
						txDescR.setText("");
					}
					else
					{
						ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Ya existe un tipo de falla reportada con ese nombre");
						vpu.showPopUp();
					}
				}

				public void onFailure(Throwable caught) 
				{
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar la falla");
					vpu.showPopUp();
				}
			});
		}
	}

	public void cargarTabInicialT()
	{
		tabPTipos.add(hpFallasT, "Fallas Detectadas");
		hpFallasT.setTitle("Fallas detectadas por un técnico");
		vpFallasT.add(txDescT);
		vpFallasT.setSpacing(20);
		vpFallasT.add(ltBFallasT);	

		vpFallasT2.add(btnAgregarT);
		vpFallasT2.setSpacing(20);
		vpFallasT2.add(btnModificarT);

		hpFallasT.add(vpFallasT);
		hpFallasT.add(vpFallasT2);


		btnModificarT.addClickHandler(new ClickHandler() 
		{
			public void onClick(ClickEvent event) 
			{
				ModificarFallaT();
			}
		});


		btnAgregarT.addClickHandler(new ClickHandler() 
		{
			public void onClick(ClickEvent event) 
			{
				agregarFallaT();
			}
		});
	}

	public void cargarTabInicialR()
	{
		tabPTipos.add(hpFallasR, "Fallas Reportadas");
		hpFallasR.setTitle("Fallas reportadas por un cliente");
		vpFallasR.add(txDescR);
		vpFallasR.setSpacing(20);
		vpFallasR.add(ltBFallasR);	
		vpFallasR2.add(btnAgregarR);
		vpFallasR2.setSpacing(20);
		vpFallasR2.add(btnModificarR);

		hpFallasR.add(vpFallasR);
		hpFallasR.add(vpFallasR2);

		btnModificarR.addClickHandler(new ClickHandler() 
		{
			public void onClick(ClickEvent event) 
			{
				ModificarFallaR();
			}
		});


		btnAgregarR.addClickHandler(new ClickHandler() 
		{
			public void onClick(ClickEvent event) 
			{
				agregarFallaR();
			}
		});
	}

	public void cargarLtBoxInicialT()
	{
		popUp.show();
		ltBFallasT.clear();
		ProyectoBilpa.greetingService.obtenerTiposFallasT(new AsyncCallback<ArrayList<TipoFallaTecnica>>() {
			public void onFailure(Throwable caught) 
			{
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener tipos de fallas detectadas");
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

	public void cargarLtBoxInicialR()
	{
		popUp.show();
		ltBFallasR.clear();
		ProyectoBilpa.greetingService.obtenerTiposFallasR(new AsyncCallback<ArrayList<TipoFallaReportada>>() {

			public void onFailure(Throwable caught) 
			{
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al modificar tipo de falla reportada");
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
				popUp.hide();
			}
		});		
	}

	public DialogBox crearDialogoModificacionFallaT(TipoFallaTecnica objFallaT)
	{
		final TipoFallaTecnica fallaT = objFallaT;
		vPanelDialT.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		dialogBoxT.setTitle("Modificacion de Falla Técnica");

		btnOkDialT.setWidth("100px");
		btnCancelDialT.setWidth("100px");

		txDescDialT.setWidth("500px");

		txDescDialT.setText(fallaT.getDescripcion());
		hPanelDialT.setSpacing(5);

		hPanelDialT.add(txDescDialT);

		hPanel2DialT.add(btnCancelDialT);
		hPanel2DialT.setSpacing(5);		
		hPanel2DialT.add(btnOkDialT);

		vPanelDialT.add(hPanelDialT);
		vPanelDialT.add(hPanel2DialT);

		dialogBoxT.setWidget(vPanelDialT);

		return dialogBoxT;
	}
	
	public DialogBox crearDialogoModificacionFallaR(String texto)
	{
		dialogBoxR.setTitle("Modificacion de Falla Reclamada");

		btnOkDialR.setWidth("100px");
		btnCancelDialR.setWidth("100px");

		txDescDialR.setWidth("500px");

		txDescDialR.setText(texto);
		hPanelDialR.setSpacing(5);

		hPanelDialR.add(txDescDialR);

		hPanel2DialR.add(btnCancelDialR);
		hPanel2DialR.setSpacing(5);		
		hPanel2DialR.add(btnOkDialR);

		vPanelDialR.add(hPanelDialR);
		vPanelDialR.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelDialR.add(hPanel2DialR);

		dialogBoxR.setWidget(vPanelDialR);

		return dialogBoxR;
	}

	public void modificarFallaR()
	{
		final String descDialR = this.txDescDialR.getText();

		if (this.ltBFallasR.getSelectedIndex() != -1)
		{
			int id = Integer.valueOf(this.ltBFallasR.getValue(this.ltBFallasR.getSelectedIndex()));

			ProyectoBilpa.greetingService.buscarTipoFallaR(id, new AsyncCallback<TipoFallaReportada>() {
				public void onSuccess(TipoFallaReportada result) 
				{
					if (result != null)
					{
						result.setDescripcion(descDialR);

						ProyectoBilpa.greetingService.modificarTipoFallaR(result, new AsyncCallback<Boolean>() {
							public void onSuccess(Boolean result) 
							{
								if (result)
								{
									cargarLtBoxInicialR();
								}
								else 
								{
									ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Ya existe un tipo de falla reportada con ese nombre");
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
		final String descDialT = this.txDescDialT.getText();

		if (this.ltBFallasT.getSelectedIndex() != -1)
		{
			int id = Integer.valueOf(this.ltBFallasT.getValue(this.ltBFallasT.getSelectedIndex()));

			ProyectoBilpa.greetingService.buscarTipoFallaT(id, new AsyncCallback<TipoFallaTecnica>() {
				public void onSuccess(TipoFallaTecnica result) 
				{
					if (result != null)
					{
						result.setDescripcion(descDialT);

						ProyectoBilpa.greetingService.modificarTipoFallaT(result, new AsyncCallback<Boolean>() {
							public void onSuccess(Boolean result) 
							{
								if (result)
								{
									cargarLtBoxInicialT();
								}
								else 
								{
									ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Ya existe un tipo de falla técnica con ese nombre");
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
}
