package app.client.iu.tarea;

import java.util.ArrayList;

import app.client.ProyectoBilpa;
import app.client.dominio.Persona;
import app.client.dominio.Tarea;
import app.client.dominio.TipoTarea;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.Constants;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class IUGestionarTarea  extends IUWidgetTarea 
{
	private Label lblTitulo = new Label("Gestión de tareas"); 
	private Label lblTituloTiposTareas = new Label("Tipo de tarea");
	private Label lblTituloTareas = new Label("Tareas");
	private Label lblDescripcion = new Label("Descripción");

	private TextBox txtDescTarea = new TextBox();
	private ListBox listBoxTiposTareasMod = new ListBox();

	private Button btnGuardar = new Button("Agregar");
	private Button btnModificar = new Button("Modificar");
	private Button btnBorrar = new Button("Limpiar");

	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private HorizontalPanel hPanelContenedor = new HorizontalPanel();

	private VerticalPanel vPanelTareas_BotonGuardar = new VerticalPanel();
	private VerticalPanel vPanelTiposTarea = new VerticalPanel();
	private VerticalPanel vPanelTareas = new VerticalPanel();

	private HorizontalPanel hPanelSpace = new HorizontalPanel();
	private HorizontalPanel hPanelSpace2 = new HorizontalPanel();
	private HorizontalPanel hPanelSpace3 = new HorizontalPanel();
	private HorizontalPanel hPanelSpace4 = new HorizontalPanel();
	private VerticalPanel vPanel1 =  new VerticalPanel();

	private HorizontalPanel hPanelDescripcion_BotonGuardar =  new HorizontalPanel();
	private HorizontalPanel hPanelTipos_BotonBorrar =  new HorizontalPanel();
	private HorizontalPanel hPanelListBoxTareas_BotonModificar =  new HorizontalPanel();

	private VerticalPanel vPanelBotonGuardar =  new VerticalPanel();
	private VerticalPanel vPanelTareas_BotonMod =  new VerticalPanel();


	//Dialogo de Modificacion========================================================
	final DialogBox dialogBoxModif = new DialogBox();
	private Label lblTituloTiposTareasModif = new Label("Tipo de Tarea");
	private Label lblDescripcionModif = new Label("Descripción");
	private VerticalPanel vPanelDialModif1 = new VerticalPanel();
	private VerticalPanel vPanelDialModif2 = new VerticalPanel();
	private HorizontalPanel hPanelDialModif3 = new HorizontalPanel();
	VerticalPanel vPanelDailModif = new VerticalPanel();
	TextBox txtDialModifRep = new TextBox();

	private GlassPopup glass = new GlassPopup();
	private PopupCargando popUp = new PopupCargando("Cargando...");

	int idTipoDatoSel;

	Button btnAceptarModif = new Button("Aceptar",new ClickHandler()
	{
		public void onClick(ClickEvent event) 
		{
			dialogBoxModif.hide(true);
			modificarTareaIni();
			glass.hide();
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

	public VerticalPanel getPrincipalPanel() 
	{
		return vPanelPrincipal;
	}

	//CONSTRUCTOR
	public IUGestionarTarea(Persona persona)
	{
		setearWidgets();			//Setea el tamano de los Widgets.
		cargarPanelesConWidgets();	//Agrega los Widget a los paneles.
		idTipoDatoSel = listBoxTiposTareas.getSelectedIndex();
		cargarLtBoxTipoTarea();
		color();
	}

	private void color() 
	{
		lblTitulo.setStyleName("Titulo");
		lblDescripcion.setStyleName("Negrita");
		lblTituloTiposTareas.setStyleName("Negrita");
		lblTituloTareas.setStyleName("Negrita");
	}

	private void setearWidgets() 
	{
		lblTitulo.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		vPanelTareas_BotonGuardar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		vPanelTiposTarea.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		vPanelTareas.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		hPanelContenedor.setWidth("800px");
		btnGuardar.setWidth("100px");
		btnModificar.setWidth("100px");
		btnBorrar.setWidth("100px");
		txtDescTarea.setWidth("500px");
		listBoxTiposTareas.setWidth("500px");
		listBoxTareas.setWidth("500px");
		listBoxTareas.setVisibleItemCount(10);

		hPanelSpace.setWidth("30px");
		hPanelSpace2.setWidth("30px");
		hPanelSpace3.setWidth("30px");
		hPanelSpace4.setWidth("30px");
	}

	private void cargarPanelesConWidgets() 
	{
		vPanelPrincipal.add(lblTitulo);

		hPanelContenedor.add(vPanel1);
		vPanelPrincipal.add(hPanelContenedor);

		vPanel1.setSpacing(20);
		vPanel1.add(vPanelTareas_BotonGuardar);
		vPanel1.add(vPanelTiposTarea);
		vPanel1.add(vPanelTareas_BotonMod);

		hPanelDescripcion_BotonGuardar.add(txtDescTarea);
		hPanelDescripcion_BotonGuardar.add(hPanelSpace2);
		hPanelDescripcion_BotonGuardar.add(vPanelBotonGuardar);

		hPanelTipos_BotonBorrar.add(listBoxTiposTareas);
		hPanelTipos_BotonBorrar.add(hPanelSpace4);
		hPanelTipos_BotonBorrar.add(btnBorrar);

		hPanelListBoxTareas_BotonModificar.add(listBoxTareas);
		hPanelListBoxTareas_BotonModificar.add(hPanelSpace3);
		hPanelListBoxTareas_BotonModificar.add(btnModificar);

		vPanelBotonGuardar.add(btnGuardar);

		vPanelTiposTarea.add(lblTituloTiposTareas);		
		vPanelTiposTarea.add(hPanelTipos_BotonBorrar);		

		vPanelTareas_BotonGuardar.add(lblDescripcion);
		vPanelTareas_BotonGuardar.add(hPanelDescripcion_BotonGuardar);	

		vPanelTareas_BotonMod.add(lblTituloTareas);
		vPanelTareas_BotonMod.add(hPanelListBoxTareas_BotonModificar);

		txtDescTarea.setTitle("Ingrese la Descripción de la Tarea y presione Agregar");
		btnModificar.setTitle("Seleccione una Tarea y presione Modificar para cambiar su descripción y/o tipo");
		btnGuardar.setTitle("Ingrese la descripcion de la Tarea y presione Agregar");

		listBoxTareas.setFocus(false);
		listBoxTareas.setTitle("Lista de Tareas");

		btnGuardar.addClickHandler(new ClickHandler() 
		{
			public void onClick(ClickEvent event) 
			{
				guardarNuevaTarea();
			}
		});

		btnModificar.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event) 
			{
				cargarDialModif_1();
			}
		});	

		btnBorrar.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				limpiarCampos();
			}
		});

		listBoxTareas.addChangeHandler(new ChangeHandler()
		{
			public void onChange(ChangeEvent arg0) 
			{
				cargarDatos();
			}
		});

	}

	private void cargarDatos() {
		if (listBoxTareas.getSelectedIndex() != -1)
		{
			int id = Integer.valueOf(listBoxTareas.getValue(listBoxTareas.getSelectedIndex()));
			ProyectoBilpa.greetingService.buscarTarea(id, new AsyncCallback<Tarea>() {
				public void onSuccess(Tarea result) 
				{
					if (result != null)
					{
						txtDescTarea.setText(result.getDescripcion());
					}
				}

				public void onFailure(Throwable caught) 
				{
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar tarea: " + caught.getMessage());
					vpu.showPopUp();
				}
			});
		}

	}

	private void guardarNuevaTarea(){
		String descripcion = txtDescTarea.getText();
		if (Integer.valueOf(listBoxTiposTareas.getValue(listBoxTiposTareas.getSelectedIndex())) > -1)
		{
			if(!descripcion.equalsIgnoreCase("") && !descripcion.trim().equalsIgnoreCase("") )
			{
				ProyectoBilpa.greetingService.validarTareaExiste(descripcion, new AsyncCallback<Boolean>() {

					public void onSuccess(Boolean result) 
					{
						if (result)
						{
							guardarTarea();
						}
						else
						{
							ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Ya existe un tarea con esa descripción");
							vpu.showPopUp();
						}				
					}

					public void onFailure(Throwable caught) 
					{
						ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar nueva tarea");
						vpu.showPopUp();
					}
				});
			}
		}
		else
		{
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe seleccionar otro tipo de tarea");
			vpu.showPopUp();
		}
	}

	public void guardarTarea(){
		final String descripcion = txtDescTarea.getText();
		if(!descripcion.equalsIgnoreCase("")){
			final Tarea tarea = new Tarea();
			tarea.setDescripcion(descripcion);
			tarea.setTipoTarea(buscarTipoTarea(listBoxTiposTareas));

			ProyectoBilpa.greetingService.agregarTarea(tarea, new AsyncCallback<Boolean>() {
				public void onSuccess(Boolean result) 
				{
					if (result) 
					{
						idTipoDatoSel = listBoxTiposTareas.getSelectedIndex();
						cargarLtBoxTipoTarea();
						txtDescTarea.setText("");
						txtDescTarea.setFocus(true);
					} 
					else
					{
						ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar tarea");
						vpu.showPopUp();
					}
				}

				public void onFailure(Throwable caught) {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar tarea");
					vpu.showPopUp();
				}
			});

		}
	}

	//MODIFICAR TAREA==========================================================================================================================
	public void cargarDialModif_1(){
		if (listBoxTareas.getSelectedIndex() != -1)
		{
			int id = Integer.valueOf(listBoxTareas.getValue(listBoxTareas.getSelectedIndex()));
			ProyectoBilpa.greetingService.buscarTarea(id, new AsyncCallback<Tarea>() {
				public void onSuccess(Tarea result) 
				{
					if (result != null)
					{
						glass.show();
						DialogBox db = crearDialogoModificacionTarea(result);
						db.center();
						db.show();
					}
					else
					{
						ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Ya existe un tarea con esa descripción");
						vpu.showPopUp();
					}				
				}

				public void onFailure(Throwable caught) 
				{
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar tarea: " + caught.getMessage());
					vpu.showPopUp();
				}
			});
		}
	}

	public DialogBox crearDialogoModificacionTarea(Tarea tarea){

		dialogBoxModif.setAutoHideEnabled(true);
		dialogBoxModif.setTitle("Modificacion de Tarea");
		vPanelDailModif.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelDailModif.setSpacing(10);

		btnAceptarModif.setWidth("100px");
		txtDialModifRep.setWidth("300px");
		btnCancelModif.setWidth("100px");
		listBoxTiposTareasMod.setWidth("300px");
		lblDescripcionModif.setStyleName("Negrita");
		lblTituloTiposTareasModif.setStyleName("Negrita");
		txtDialModifRep.setText(tarea.getDescripcion());

		vPanelDialModif1.add(lblDescripcionModif);
		vPanelDialModif1.add(txtDialModifRep);

		vPanelDialModif2.add(lblTituloTiposTareasModif);
		vPanelDialModif2.add(listBoxTiposTareasMod);

		hPanelDialModif3.add(btnCancelModif);
		hPanelDialModif3.setSpacing(5);
		hPanelDialModif3.add(btnAceptarModif);

		vPanelDailModif.add(vPanelDialModif1);
		vPanelDailModif.add(vPanelDialModif2);
		vPanelDailModif.add(hPanelDialModif3);

		dialogBoxModif.setWidget(vPanelDailModif);

		setearListBoxTipTarModif(tarea);
		return dialogBoxModif;
	}

	private void setearListBoxTipTarModif(Tarea tarea) 
	{
		listBoxTiposTareasMod.clear();
		for (TipoTarea tipo : tiposTareasMemoria)
		{
			listBoxTiposTareasMod.addItem(tipo.toString(),String.valueOf(tipo.getId()));
		}

		int selectItem = 0;
		for (int i = 0 ; i < listBoxTiposTareasMod.getItemCount() ; i ++)
		{
			if (Integer.valueOf(listBoxTiposTareasMod.getValue(i)) == tarea.getTipoTarea().getId())
			{
				selectItem = i;
			}
		}
		listBoxTiposTareasMod.setSelectedIndex(selectItem);
	}

	private void modificarTareaIni(){
		final String descDialT = txtDialModifRep.getText();
		final TipoTarea tipoTareaAModif = buscarTipoTarea(listBoxTiposTareasMod);

		if (!descDialT.equalsIgnoreCase("")){
			int id = Integer.valueOf(listBoxTareas.getValue(listBoxTareas.getSelectedIndex()));

			ProyectoBilpa.greetingService.buscarTarea(id, new AsyncCallback<Tarea>() {
				public void onSuccess(Tarea result) 
				{
					if (result != null)
					{
						result.setDescripcion(descDialT);
						result.setTipoTarea(tipoTareaAModif);
						modificarTarea_ValidarSiExiste(result);
					}			
				}

				public void onFailure(Throwable caught) 
				{
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar tarea: " + caught.getMessage());
					vpu.showPopUp();
				}
			});
			txtDescTarea.setText("");	
		}
		else
		{
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "La descripcion de la tarea no puede ser vacia");
			vpu.showPopUp();
		}

	}

	private void modificarTarea_ValidarSiExiste(final Tarea tarea) 
	{
		if(tarea != null)
		{
			ProyectoBilpa.greetingService.validarTareaExiste(tarea, new AsyncCallback<Boolean>() {
				public void onSuccess(Boolean result) 
				{
					if (result)
					{
						modificarTareaBase_Final(tarea);
					}
					else
					{
						ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Ya existe un tarea con esa descripción");
						vpu.showPopUp();
					}				
				}
				public void onFailure(Throwable caught) 
				{
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al validar si existe la tarea: " + caught.getMessage());
					vpu.showPopUp();
				}
			});
		}
	}

	private void modificarTareaBase_Final(Tarea tarea)
	{
		if(tarea != null)
		{
			ProyectoBilpa.greetingService.modificarTareaBase(tarea, new AsyncCallback<Boolean>() {
				public void onFailure(Throwable caught) 
				{
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al modificar la tarea en base: " + caught.getMessage());
					vpu.showPopUp();
				}

				public void onSuccess(Boolean result) 
				{
					idTipoDatoSel = listBoxTiposTareasMod.getSelectedIndex();
					cargarLtBoxTipoTarea();
				}
			});
		}
	}

	protected void cargarLtBoxTipoTarea()
	{

		listBoxTiposTareas.clear();
		popUp.show();

		ProyectoBilpa.greetingService.obtenerTodasLosTiposTareasActivos(new AsyncCallback<ArrayList<TipoTarea>>() {
			public void onFailure(Throwable caught) 
			{
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener todas los tipo de tareas");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<TipoTarea> result) 
			{
				TipoTarea auxiliar;
				tiposTareasMemoria.clear();
				listBoxTiposTareas.addItem(Constants.TODOS, -1+"");

				for (int i=0; i<result.size();i++)
				{
					auxiliar = (TipoTarea) result.get(i);
					listBoxTiposTareas.addItem(auxiliar.toString(),String.valueOf(auxiliar.getId()));
					tiposTareasMemoria.add(auxiliar);
				}			

				if (idTipoDatoSel >= 0 && listBoxTiposTareas.getItemCount()>0 && idTipoDatoSel < listBoxTiposTareas.getItemCount())
				{
					listBoxTiposTareas.setSelectedIndex(idTipoDatoSel);
				}

				cargarLtBoxTareasBase();
				popUp.hide();
			}
		});		
	}	

	public void cargarLtBoxTareasBase()
	{
		popUp.show();
		tareasMemoria.clear();

		ProyectoBilpa.greetingService.obtenerTodasLasTareas(new AsyncCallback<ArrayList<Tarea>>() {
			public void onFailure(Throwable caught) 
			{
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al cargar los tipos de tareas");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<Tarea> result) 
			{
				for (Tarea t : result)
				{
					tareasMemoria.add(t);
				}
				cargarListBoxPorTipo(result);	
				popUp.hide();
			}
		});		
	}

	protected void limpiarCampos() {
		txtDescTarea.setText("");
		cargarListBoxPorTipo(tareasMemoria);
	}
}
