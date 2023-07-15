package app.client.utilidades.utilObjects;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class ComboFechaHora extends Widget{

	/**
	 * ComboFechaHora es un Widget que tiene un selector de Fecha y 2 combos, una para las horas y otra para los minutos.
	 * Un metodo publico getFecha que devuelve un Date con la fecha seleccionada (fecha, hora y minutos)
	 * Autor JR.
	 */

	private HorizontalPanel contenedor = new HorizontalPanel();
	private ListBox horas = new ListBox();
	private ListBox minutos = new ListBox();
	private DateBox fecha = new DateBox();
	private Label separador = new Label(":");

	public ComboFechaHora(){
		cargarHoras();
		cargarMinutos();
		setearFechaHora();
		setearWidgets();
	}

	private void cargarHoras(){
		horas.setVisibleItemCount(1);
		horas.setSize("100%", "100%");
		for (int i = 0; i < 24; i++) {
			horas.addItem(i + "");
		}
	}
	
	@SuppressWarnings("deprecation")
	private void setearFechaHora(){
		Date actual = new Date();
		
		fecha.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getLongDateFormat()));
		fecha.setValue(actual, true);
		fecha.setSize("100%", "100%");
		fecha.setWidth("100%");
		
		int minuto = actual.getMinutes();
		int hora = actual.getHours();
		
		horas.setItemSelected(hora, true);
		minutos.setItemSelected(minuto, true);
	}

	private void cargarMinutos(){
		minutos.setVisibleItemCount(1);
		minutos.setSize("100%", "100%");
		for (int i = 0; i < 60; i++) {
			textoEnLista(i);
		}
	}

	private void textoEnLista(int i) {
		if(i == 0 || i == 1 || i == 2 || i == 3 || i == 4 || i == 5 || i == 6 || i == 7 || i == 8 || i == 9){
			minutos.addItem("0" + i);
		}else{
			minutos.addItem(i + "");
		}
	}

	private void setearWidgets(){
		horas.setTitle("Horas");
		minutos.setTitle("Minutos");
		fecha.setTitle("Fecha");
		
		contenedor.setSpacing(5);
		contenedor.add(fecha);
		contenedor.add(horas);
		contenedor.add(separador);
		contenedor.add(minutos);
	}

	private int getHora(){
		String hora = horas.getItemText(horas.getSelectedIndex());
		int horaInt = Integer.parseInt(hora);
		return horaInt;
	}

	private int getMinutos(){
		String minuto = minutos.getItemText(minutos.getSelectedIndex());
		int minutoInt = Integer.parseInt(minuto);
		return minutoInt;
	}
	
	public HorizontalPanel getCombo(){
		return contenedor;
	}
	
	@SuppressWarnings("deprecation")
	public Date getFecha(){
		Date date = fecha.getValue();
		date.setHours(getHora());
		date.setMinutes(getMinutos());
		return date;
	}
	
}
