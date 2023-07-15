package app.client.iu.orden.encargado;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import app.client.ProyectoBilpa;
import app.client.dominio.Orden;
import app.client.dominio.Persona;
import app.client.iu.orden.tecnico.IUSeguimientoTecnico;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.UtilOrden;
import app.client.utilidades.utilObjects.GlassPopup;

public class IUSeguimientoEncargado extends IUSeguimientoTecnico {
	
	int idTecnicoAsignado;
	private GlassPopup glass = new GlassPopup();
	
	public IUSeguimientoEncargado(Orden orden, Persona persona) {
		super(orden, persona);
		agregarWidgetpanelHTecnico_3B();
	}

	@Override
	protected void agregarWidgetpanelHTecnico_3B() {	
		panelHTecnico_3B.add(this.lblTecnicoAsignado);
		panelHTecnico_3B.setSpacing(5);
		panelHTecnico_3B.add(iUCorrectivoToolBar.getListBoxTecnicoAsignado());
	}

	/*private void cargarListaTecnicos() {
		ProyectoBilpa.greetingService.cargarTecnicosYEnc(new AsyncCallback<ArrayList<Persona>>() {
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener los técnicos");
				vpu.showPopUp();
			}
			public void onSuccess(ArrayList<Persona> result) {
				listBoxTecnicoAsignado.addItem(UtilOrden.personaAsignadaOrden(1));
				for (int i=0; i<result.size();i++){
					listBoxTecnicoAsignado.addItem(result.get(i).toString(), result.get(i).getId()+"");
				}		
				UtilUI.setearTecnicoDeOrden(orden, listBoxTecnicoAsignado);
				idTecnicoAsignado = Integer.valueOf(listBoxTecnicoAsignado.getValue(listBoxTecnicoAsignado.getSelectedIndex()));
			}
		});
	}*/

	private void setearTecnicoAsignado() {
		String tecnicoAsignado = iUCorrectivoToolBar.getListBoxTecnicoAsignado().getItemText(iUCorrectivoToolBar.getListBoxTecnicoAsignado().getSelectedIndex());
		if(!tecnicoAsignado.equals(UtilOrden.personaAsignadaOrden(1))){  	//Si el tecnico asignado No es "Sin asignar"

			if(orden.getEstadoOrden() != 5){
				orden.setEstadoOrden(2);
			}

			int id = Integer.valueOf(iUCorrectivoToolBar.getListBoxTecnicoAsignado().getValue(iUCorrectivoToolBar.getListBoxTecnicoAsignado().getSelectedIndex()));

			ProyectoBilpa.greetingService.buscarUsuario(id, new AsyncCallback<Persona>(){			
				public void onFailure(Throwable caught) {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar el tecnico");
					vpu.showPopUp();
				}

				public void onSuccess(Persona result) {					
					orden.setTecnicoAsignado(result);
					guardarOrdenFinal();
				}				
			});		
		}else{
			dialogoConfirmarSinAsignacion();
		}
	}

	public DialogBox dialogoConfirmarSinAsignacion(){

		final HorizontalPanel hPanelDial1 = new HorizontalPanel();
		final HorizontalPanel hPanelDial2 = new HorizontalPanel();
		final VerticalPanel vPanelDailModif = new VerticalPanel();
		final DialogBox dialogConfirmar = new DialogBox();
		final Label lblTexto = new Label("Desea dejar la orden sin asignar?");

		Button btnSi = new Button("Si", new ClickHandler() {
			public void onClick(ClickEvent event) {
				orden.setTecnicoAsignado(null);
				guardarOrdenFinal();
				dialogConfirmar.hide(true);					
			}
		});

		Button btnNo = new Button("No",	new ClickHandler() {
			public void onClick(ClickEvent event) 
			{
				dialogConfirmar.hide(true);
			}
		});

		vPanelDailModif.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		btnSi.setWidth("100px");
		btnNo.setWidth("100px");
		hPanelDial1.add(lblTexto);

		lblTexto.setStyleName("Negrita");

		hPanelDial1.setSpacing(15);


		hPanelDial2.add(btnNo);
		hPanelDial2.setSpacing(5);
		hPanelDial2.add(btnSi);

		vPanelDailModif.add(hPanelDial1);
		vPanelDailModif.add(hPanelDial2);

		dialogConfirmar.setWidget(vPanelDailModif);

		dialogConfirmar.show();
		dialogConfirmar.center();

		return dialogConfirmar;
	}

	@Override
	protected void guardarOrden() {
		setearTecnicoAsignado();
	}

	private void guardarOrdenFinal(){
		int idTecnicoAsigandoFinal = Integer.valueOf(iUCorrectivoToolBar.getListBoxTecnicoAsignado().getValue(iUCorrectivoToolBar.getListBoxTecnicoAsignado().getSelectedIndex()));
		
		if(idTecnicoAsignado != idTecnicoAsigandoFinal){
			estado = "seguimientoInicial";
		}
		super.guardarOrden();	
	}

}
