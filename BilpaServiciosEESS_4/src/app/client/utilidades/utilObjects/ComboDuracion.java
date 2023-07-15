package app.client.utilidades.utilObjects;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;

public class ComboDuracion {
	
	/**
	 * ComboDuracion es un Widget que tiene un combo con minutos fraccionado de a 15 minutos.
	 * Un metodo publico int que devuelve la cantidad de minutos seleccionados
	 * Autor JR.
	 */
	
	private HorizontalPanel contenedor = new HorizontalPanel();
	private ListBox minutos = new ListBox();
	
	public ComboDuracion(){
		cargarMinutos();
		setearWidgets();
	}

	private void cargarMinutos(){
		minutos.setVisibleItemCount(1);
		minutos.setSize("100%", "100%");
		minutos.addItem("Sin Seleccionar");
		for (int i = 1; i < 33; i++) {
			textoEnLista(i);
		}
	}
	
	private void textoEnLista(int i) {
		if(i == 4){
			minutos.addItem(i * 15 + " - 1 Hora");
		}else if(i == 8){
			minutos.addItem(i * 15 + " - 2 Horas");
		}else if(i == 12){
			minutos.addItem(i * 15 + " - 3 Horas");
		}else if(i == 16){
			minutos.addItem(i * 15 + " - 4 Horas");
		}else if(i == 20){
			minutos.addItem(i * 15 + " - 5 Horas");
		}else if(i == 24){
			minutos.addItem(i * 15 + " - 6 Horas");
		}else if(i == 28){
			minutos.addItem(i * 15 + " - 7 Horas");
		}else if(i == 32){
			minutos.addItem(i * 15 + " - 8 Horas");
		}else{
			minutos.addItem(i * 15 + "");
		}		
	}

	public int getMinutos(){
		int index = minutos.getSelectedIndex();
		return index * 15;
	}
	
	public HorizontalPanel getCombo(){
		return contenedor;
	}
	
	private void setearWidgets(){
		minutos.setTitle("Minutos");
		contenedor.setSpacing(5);
		contenedor.add(minutos);
	}

}
