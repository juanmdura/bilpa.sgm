package app.client.utilidades.utilObjects;

import app.client.ProyectoBilpa;
import app.client.iu.widgets.ValidadorPopup;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class ComboHora extends Widget{

	/**
	 * ComboFechaHora es un Widget que tiene un selector de Fecha y 2 combos, una para las horas y otra para los minutos.
	 * Un metodo publico getFecha que devuelve un Date con la fecha seleccionada (fecha, hora y minutos)
	 * Autor JR.
	 */

	private HorizontalPanel contenedor = new HorizontalPanel();
	private ListBox horas = new ListBox();
	private ListBox minutos = new ListBox();
	private Label separador = new Label(":");
	private String actual;
	
	private GlassPopup glass = new GlassPopup();
	
	public ComboHora(){
		obtenerHoraServidor();		
	}

	private void cargarHoras(){
		horas.setVisibleItemCount(1);
		horas.setSize("100%", "100%");
		for (int i = 0; i < 24; i++) {
			horas.addItem(i + "");
		}
	}
	
	private void setearFechaHora(){
		int hora = Integer.valueOf(actual.trim().substring(11, 13));
		int minuto = Integer.valueOf(actual.trim().substring(14, 16));
		
		horas.setItemSelected(hora, true);
		minutos.setItemSelected(minuto, true);
	}

	private void obtenerHoraServidor() {
		ProyectoBilpa.greetingService.obtenerHoraServidor(new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar la hora del servidor");
				vpu.showPopUp();
			}

			public void onSuccess(String result) 
			{
				actual = result;
				cargarHoras();
				cargarMinutos();
				setearFechaHora();
				setearWidgets();
			}
		});	
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
		
		contenedor.setSpacing(5);
		contenedor.add(horas);
		contenedor.add(separador);
		contenedor.add(minutos);
	}

	public int getHora(){
		String hora = horas.getItemText(horas.getSelectedIndex());
		int horaInt = Integer.parseInt(hora);
		return horaInt;
	}

	public int getMinutos(){
		String minuto = minutos.getItemText(minutos.getSelectedIndex());
		int minutoInt = Integer.parseInt(minuto);
		return minutoInt;
	}
	
	public HorizontalPanel getCombo(){
		return contenedor;
	}	
}
