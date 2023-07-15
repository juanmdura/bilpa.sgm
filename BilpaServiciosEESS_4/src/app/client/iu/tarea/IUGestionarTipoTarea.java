package app.client.iu.tarea;

import java.util.ArrayList;

import app.client.ProyectoBilpa;
import app.client.dominio.Persona;
import app.client.dominio.TipoTarea;
import app.client.iu.IUGestionarTipo;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.utilObjects.GlassPopup;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

public class IUGestionarTipoTarea extends IUGestionarTipo{

	ArrayList<TipoTarea> tiposDeTarea = new ArrayList<TipoTarea>();
	
	private GlassPopup glass = new GlassPopup();

	public IUGestionarTipoTarea(Persona persona) {
		super(persona);
		htmlTitulo.setText("Gestión de tipos de tareas");
	}

	@Override
	protected void guardarDato(){
		final String descripcion = this.txtDescDato.getText();
		if(!descripcion.equalsIgnoreCase("")){
			final TipoTarea tipoTarea = new TipoTarea();
			tipoTarea.setDescripcion(descripcion);
			agregarTipoTarea(tipoTarea);
		}
	}

	private void agregarTipoTarea(final TipoTarea tipoTarea) {
		ProyectoBilpa.greetingService.agregarTipoTarea(tipoTarea, new AsyncCallback<Boolean>() {

			public void onSuccess(Boolean result) {
				if (result) 
				{
					cargarLtBoxDatos();
					txtDescDato.setText("");
					txtDescDato.setFocus(true);
				} 
				else
				{
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar el tipo de tarea");
					vpu.showPopUp();
				}
			}

			public void onFailure(Throwable caught) 
			{
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar el tipo de tarea");
				vpu.showPopUp();
			}
		});
	}

	@Override
	protected void modificarDato()
	{
		final String descDialT = this.tDialModifRep.getText();
		if (!descDialT.equalsIgnoreCase(""))
		{
			int id = Integer.valueOf(this.lblListaDatos.getValue(this.lblListaDatos.getSelectedIndex()));
			ProyectoBilpa.greetingService.buscarTipoTarea(id, new AsyncCallback<TipoTarea>() {
				public void onSuccess(TipoTarea result) {
					if (result != null){
						result.setDescripcion(descDialT);
						modificarTarea_ValidarSiExiste_3(result);
					}			
				}
				public void onFailure(Throwable caught) {

				}
			});
			this.txtDescDato.setText("");	
		}else{
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "La descripción de la tarea no puede ser vacia");
			vpu.showPopUp();
		}

	}

	@Override
	protected void cargarDialModif_1(){
		if (this.lblListaDatos.getSelectedIndex() != -1){
			int id = Integer.valueOf(this.lblListaDatos.getValue(this.lblListaDatos.getSelectedIndex()));
			ProyectoBilpa.greetingService.buscarTipoTarea(id, new AsyncCallback<TipoTarea>() {				
				public void onSuccess(TipoTarea result) {
					if (result != null){
						DialogBox db = crearDialogoModificacionDato(result);
						db.center();
						db.show();
					}else{
						ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Ya existe un tipo de tarea con esa descripción");
						vpu.showPopUp();
					}				
				}

				public void onFailure(Throwable caught) {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar tipo de tarea: " + caught.getMessage());
					vpu.showPopUp();
				}
			});
		}
	}

	private  void modificarTarea_ValidarSiExiste_3(final TipoTarea tipoTarea) {
		if(tipoTarea != null){
			ProyectoBilpa.greetingService.validarTipoTareaExiste(tipoTarea.getDescripcion(), new AsyncCallback<Boolean>() {
				public void onSuccess(Boolean result) {
					if (result){
						modificarTareaBase_Final(tipoTarea);
					}else{
						ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Ya existe un tipo de tarea con esa descripción");
						vpu.showPopUp();
					}				
				}
				public void onFailure(Throwable caught) {
				}
			});
		}
	}

	private void modificarTareaBase_Final(TipoTarea tipoTarea){
		if(tipoTarea != null){
			ProyectoBilpa.greetingService.modificarTipoTareaBase(tipoTarea, new AsyncCallback<Boolean>() {
				public void onFailure(Throwable caught) {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al modificar el tipo de tarea: " + caught.getMessage());
					vpu.showPopUp();
				}

				public void onSuccess(Boolean result) {
					cargarLtBoxDatos();
				}
			});
		}
	}

	private DialogBox crearDialogoModificacionDato(TipoTarea tarea){

		dialogBoxModif.setAutoHideEnabled(true);
		dialogBoxModif.setTitle("Modificación de Dato");
		vPanelDailModif.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		btnAceptarModif.setWidth("100px");
		tDialModifRep.setWidth("500px");
		btnCancelModif.setWidth("100px");
		tDialModifRep.setText(tarea.getDescripcion());

		hPanelDialModif.add(tDialModifRep);

		hPanelDialModif2.add(btnCancelModif);
		hPanelDialModif2.setSpacing(5);
		hPanelDialModif2.add(btnAceptarModif);

		vPanelDailModif.add(hPanelDialModif);
		vPanelDailModif.add(hPanelDialModif2);

		dialogBoxModif.setWidget(vPanelDailModif);
		return dialogBoxModif;
	}

	@Override
	protected void cargarLtBoxDatos() {
		lblListaDatos.clear();
		popUp.show();
		ProyectoBilpa.greetingService.obtenerTodasLosTiposTareasActivos(new AsyncCallback<ArrayList<TipoTarea>>() {
			public void onFailure(Throwable caught) {
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener todas los tipo de tareas");
				vpu.showPopUp();
			}
			public void onSuccess(ArrayList<TipoTarea> result) {
				TipoTarea auxiliar;
				for (int i=0; i<result.size();i++){
					auxiliar = (TipoTarea) result.get(i);
					if (auxiliar.getId() != 1)//Sin Clasificar
					{
						lblListaDatos.addItem(auxiliar.toString(),String.valueOf(auxiliar.getId()));
					}
				}			
				popUp.hide();
			}
		});		

	}

	@Override
	protected void guardarNuevoDato() {
		String descripcion = this.txtDescDato.getText();
		if(!descripcion.equalsIgnoreCase("") && !descripcion.trim().equalsIgnoreCase("")){
			ProyectoBilpa.greetingService.validarTipoTareaExiste(descripcion, new AsyncCallback<Boolean>() {

				public void onSuccess(Boolean result) {
					if (result){
						guardarDato();
					}else{
						ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Ya existe un tipo de tarea con esa descripción");
						vpu.showPopUp();
					}				
				}
				public void onFailure(Throwable caught) {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar los tipos de tareas");
					vpu.showPopUp();
				}
			});
		}
	}
}
