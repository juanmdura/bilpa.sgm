package app.client.iu.repuesto;

import java.util.ArrayList;

import app.client.ProyectoBilpa;
import app.client.dominio.Persona;
import app.client.dominio.TipoRepuesto;
import app.client.iu.IUGestionarTipo;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.utilObjects.GlassPopup;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

public class IUGestionarTipoRepuesto extends IUGestionarTipo{

	ArrayList<TipoRepuesto> tiposDeRepuesto = new ArrayList<TipoRepuesto>();
	
	private GlassPopup glass = new GlassPopup();

	public IUGestionarTipoRepuesto(Persona persona) {
		super(persona);
		htmlTitulo.setText("Gestión de tipos de repuestos");
	}

	@Override
	protected void guardarDato(){
		final String descripcion = this.txtDescDato.getText();
		if(!descripcion.equalsIgnoreCase("")){
			final TipoRepuesto tipoRepuesto = new TipoRepuesto();
			tipoRepuesto.setDescripcion(descripcion);
			agregarTipoRepuesto(tipoRepuesto);
		}
	}

	private void agregarTipoRepuesto(final TipoRepuesto tipoRepuesto) {
		ProyectoBilpa.greetingService.agregarTipoRepuesto(tipoRepuesto, new AsyncCallback<Boolean>() {

			public void onSuccess(Boolean result) {
				if (result) 
				{
					cargarLtBoxDatos();
					txtDescDato.setText("");
					txtDescDato.setFocus(true);
				} 
				else
				{
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar el tipo de repuesto");
					vpu.showPopUp();
				}
			}

			public void onFailure(Throwable caught) 
			{
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar el tipo de repuesto");
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
			ProyectoBilpa.greetingService.buscarTipoRepuesto(id, new AsyncCallback<TipoRepuesto>() {
				public void onSuccess(TipoRepuesto result) {
					if (result != null){
						result.setDescripcion(descDialT);
						modificarRepuesto_ValidarSiExiste_3(result);
					}			
				}
				public void onFailure(Throwable caught) {

				}
			});
			this.txtDescDato.setText("");	
		}else{
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "La descripción de la repuesto no puede ser vacia");
			vpu.showPopUp();
		}

	}

	@Override
	protected void cargarDialModif_1(){
		if (this.lblListaDatos.getSelectedIndex() != -1){
			int id = Integer.valueOf(this.lblListaDatos.getValue(this.lblListaDatos.getSelectedIndex()));
			ProyectoBilpa.greetingService.buscarTipoRepuesto(id, new AsyncCallback<TipoRepuesto>() {				
				public void onSuccess(TipoRepuesto result) {
					if (result != null){
						DialogBox db = crearDialogoModificacionDato(result);
						db.center();
						db.show();
					}else{
						ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Ya existe un tipo de repuesto con esa descripción");
						vpu.showPopUp();
					}				
				}

				public void onFailure(Throwable caught) {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar tipo de repuesto: " + caught.getMessage());
					vpu.showPopUp();
				}
			});
		}
	}

	private  void modificarRepuesto_ValidarSiExiste_3(final TipoRepuesto tipoRepuesto) {
		if(tipoRepuesto != null){
			ProyectoBilpa.greetingService.validarTipoRepuestoExiste(tipoRepuesto.getDescripcion(), new AsyncCallback<Boolean>() {
				public void onSuccess(Boolean result) {
					if (result){
						modificarRepuestoBase_Final(tipoRepuesto);
					}else{
						ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Ya existe un tipo de repuesto con esa descripción");
						vpu.showPopUp();
					}				
				}
				public void onFailure(Throwable caught) {
				}
			});
		}
	}

	private void modificarRepuestoBase_Final(TipoRepuesto tipoRepuesto){
		if(tipoRepuesto != null){
			ProyectoBilpa.greetingService.modificarTipoRepuestoBase(tipoRepuesto, new AsyncCallback<Boolean>() {
				public void onFailure(Throwable caught) {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al modificar el tipo de repuesto: " + caught.getMessage());
					vpu.showPopUp();
				}

				public void onSuccess(Boolean result) {
					cargarLtBoxDatos();
				}
			});
		}
	}

	private DialogBox crearDialogoModificacionDato(TipoRepuesto repuesto){

		dialogBoxModif.setAutoHideEnabled(true);
		dialogBoxModif.setTitle("Modificación de Dato");
		vPanelDailModif.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		btnAceptarModif.setWidth("100px");
		tDialModifRep.setWidth("500px");
		btnCancelModif.setWidth("100px");
		tDialModifRep.setText(repuesto.getDescripcion());

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
		ProyectoBilpa.greetingService.obtenerTodasLosTiposRepuestosActivos(new AsyncCallback<ArrayList<TipoRepuesto>>() {
			public void onFailure(Throwable caught) {
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener todas los tipo de repuestos");
				vpu.showPopUp();
			}
			public void onSuccess(ArrayList<TipoRepuesto> result) {
				TipoRepuesto auxiliar;
				for (int i=0; i<result.size();i++){
					auxiliar = (TipoRepuesto) result.get(i);
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
			ProyectoBilpa.greetingService.validarTipoRepuestoExiste(descripcion, new AsyncCallback<Boolean>() {

				public void onSuccess(Boolean result) {
					if (result){
						guardarDato();
					}else{
						ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Ya existe un tipo de repuesto con esa descripción");
						vpu.showPopUp();
					}				
				}
				public void onFailure(Throwable caught) {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar los tipos de repuestos");
					vpu.showPopUp();
				}
			});
		}
	}
}
