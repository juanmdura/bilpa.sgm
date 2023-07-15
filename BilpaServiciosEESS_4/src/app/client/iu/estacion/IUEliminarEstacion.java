package app.client.iu.estacion;

import java.util.ArrayList;
import java.util.List;

import app.client.ProyectoBilpa;
import app.client.dominio.Estacion;
import app.client.dominio.Persona;
import app.client.iu.widgets.ValidadorPopup;
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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class IUEliminarEstacion extends Composite{
	private HTML htmlTitulo = new HTML("Eliminar estaciones");

	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private HorizontalPanel hPanelPrincipal = new HorizontalPanel();
	private VerticalPanel vPanel1 =  new VerticalPanel();
	private VerticalPanel vPanel2 =  new VerticalPanel();
	private ListBox listBoxEstaciones = new ListBox();

	private Button btnEliminar = new Button("Eliminar");

	private Persona sesion;

	private List<Estacion> listaEstaciones = new ArrayList<Estacion>();

	private PopupCargando popUp = new PopupCargando("Cargando...");
	
	private GlassPopup glass = new GlassPopup();

	public VerticalPanel getVpPrincipal(){
		return this.vPanelPrincipal;
	}

	public IUEliminarEstacion(Persona persona){
		this.sesion = persona;
		setearWidgets();			//Setea el tamano de los Widgets.
		cargarPanelesConWidgets();	//Agrega los Widget a los paneles.
		cargarListBoxEmpresas();		
		color();
	}

	private void setearWidgets() {
		btnEliminar.setWidth("100px");

		listBoxEstaciones.setVisibleItemCount(10);
		listBoxEstaciones.setWidth("350px");

		vPanel1.setSpacing(20);
		vPanel2.setSpacing(20);
	}

	private void cargarPanelesConWidgets() {
		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal.add(htmlTitulo);

		vPanel1.add(listBoxEstaciones);
		vPanel2.add(btnEliminar);

		listBoxEstaciones.setFocus(false);
		hPanelPrincipal.add(vPanel1);
		hPanelPrincipal.add(vPanel2);
		vPanelPrincipal.add(hPanelPrincipal);

		btnEliminar.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				eliminarEstacion();
			}

		});
	}

	private void color() {
		htmlTitulo.setStyleName("Titulo");

	}

	private void cargarListBoxEmpresas() {
		popUp.show();
		listBoxEstaciones.clear();
		listaEstaciones.clear();
		
		ProyectoBilpa.greetingService.obtenerEmpresas(new AsyncCallback<ArrayList<Estacion>>() {

			public void onFailure(Throwable caught) {
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener empresas: " + caught.getMessage());
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<Estacion> result) {
				Estacion auxiliar;
				for (int i=0; i < result.size(); i++){
					auxiliar = result.get(i);
					if(auxiliar.getId() != 1){
						listBoxEstaciones.addItem(auxiliar.toString(), String.valueOf(auxiliar.getId()));
						listaEstaciones.add(auxiliar);
					}
				}
				popUp.hide();
			}
		});		
	}


	public void eliminarEstacion(){
		int id = Integer.valueOf(listBoxEstaciones.getValue(listBoxEstaciones.getSelectedIndex()));
		final Estacion estacion = buscarEstacion (id) ;

		confirma(estacion);

	}

	private void aceptaEliminar(final Estacion estacion) {
		ProyectoBilpa.greetingService.esEstacionSinOrdenes(estacion, new AsyncCallback<Boolean>() {

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al validar la estación");
				vpu.showPopUp();
			}

			public void onSuccess(Boolean result) {
				if(result){
					eliminar(estacion);
				}else{
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "No se puede eliminar esta Estacion, tiene ordenes asignadas");
					vpu.showPopUp();
				}
			}
		});
	}

	private void confirma(final Estacion estacion) {
		final DialogBox dialogBoxConf = new DialogBox();

		Label titulo = new Label("Seguro desea eliminar la E. S. : " + estacion.getNombre());

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
				aceptaEliminar(estacion);
			}
		});

		btnNo.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				dialogBoxConf.hide();	
			}
		});
	}

	private Estacion buscarEstacion(int id) {
		for (Estacion e : listaEstaciones)
		{
			if (e.getId() == id)
			{
				return e;
			}
		}
		return null;
	}

	private void eliminar(Estacion e){
		if(e != null){
			ProyectoBilpa.greetingService.eliminarEstacion(e, new AsyncCallback<Boolean>() {
				public void onFailure(Throwable caught) {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al eliminar la estación");
					vpu.showPopUp();
				}

				public void onSuccess(Boolean result) {
					cargarListBoxEmpresas();
				}
			});
		}
	}
}
