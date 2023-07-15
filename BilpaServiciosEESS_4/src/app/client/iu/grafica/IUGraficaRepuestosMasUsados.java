package app.client.iu.grafica;

import java.util.ArrayList;

import app.client.ProyectoBilpa;
import app.client.dominio.Persona;
import app.client.dominio.data.RepuestoDatoGrafica;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.GraficaTorta;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class IUGraficaRepuestosMasUsados extends Composite {
	
	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private HorizontalPanel hPanel1 = new HorizontalPanel();
	private VerticalPanel vGrafica = new VerticalPanel();
	private Persona sesion;
	
	private GlassPopup glass = new GlassPopup();
	
	public VerticalPanel getPrincipalPanel() {
		return vPanelPrincipal;
	}
	
	public IUGraficaRepuestosMasUsados(Persona sesion){
		this.sesion = sesion;
		setearWidgets();
		agregarWidgets();
		cargarWidgets();
	}
	
	private void cargarWidgets() {
		ProyectoBilpa.greetingService.listaDelos10MasUsados(new AsyncCallback<ArrayList<RepuestoDatoGrafica>>() {

			
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener los 10 repuestos mas usados");
				vpu.showPopUp();
			}
			
			public void onSuccess(ArrayList<RepuestoDatoGrafica> result) {
				if (result.size()>0){
					mostrarGrafica(result);					
				}else{
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "No existen Ordenes con Repuestos");
					vpu.showPopUp();
				}
				
			}			
		});
		
	}

	private void setearWidgets() {
		vPanelPrincipal.setWidth("1200");
		hPanel1.setWidth("1200");
		vPanelPrincipal.setBorderWidth(1);
//		hPanel1.setBorderWidth(1);
		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hPanel1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vGrafica.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
	}

	private void mostrarGrafica(ArrayList<RepuestoDatoGrafica> listaDelos10MasUsados) {

		ArrayList<Integer> porcentajeRepuestos = new ArrayList<Integer>();
		ArrayList<String> nombresRepuesto = new ArrayList<String>();
		for(RepuestoDatoGrafica rDG : listaDelos10MasUsados){
			porcentajeRepuestos.add(rDG.getPorcentaje());
			nombresRepuesto.add(rDG.getRepuesto().getDescripcion());
		}
			
		
		GraficaTorta gt = new GraficaTorta("Grafica de los 10 Repuestos Mas Usados", porcentajeRepuestos,nombresRepuesto);
		
		gt.update();
		vGrafica.add(gt);
	}
	
	private void agregarWidgets() {

		hPanel1.add(vGrafica);
		vPanelPrincipal.add(hPanel1);
	}

}
