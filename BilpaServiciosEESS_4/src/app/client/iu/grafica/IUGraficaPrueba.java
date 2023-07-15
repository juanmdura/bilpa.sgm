package app.client.iu.grafica;

import java.util.ArrayList;

import app.client.dominio.Persona;
import app.client.utilidades.utilObjects.GraficaTorta;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

public class IUGraficaPrueba extends Composite {
	
	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private VerticalPanel vGrafica = new VerticalPanel();
	private Persona sesion;
	
	public VerticalPanel getPrincipalPanel() {
		return vPanelPrincipal;
	}
	
	public IUGraficaPrueba(Persona sesion){
		this.sesion = sesion;
		setearWidgets();
		agregarWidgets();		
	}
	
	private void setearWidgets() {
		ArrayList<Integer> j = new ArrayList<Integer>();
		ArrayList<String> j2 = new ArrayList<String>();
		
		j.add(40);
		j.add(30);
		j.add(30);
		j2.add("frutas");
		j2.add("calzados");
		j2.add("mierda");
		
		GraficaTorta gt = new GraficaTorta("Grafica pa probar", j,j2);
		gt.update();
		vGrafica.add(gt);
	}
	
	private void agregarWidgets() {
		vPanelPrincipal.add(vGrafica);
	}

}
