package app.client.utilidades.utilObjects;

import java.util.Date;

import app.client.ProyectoBilpa;
import app.client.iu.widgets.ValidadorPopup;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class ComboFecha extends Widget{

	/**
	 * ComboFechaHora es un Widget que tiene un selector de Fecha y 2 combos, una para las horas y otra para los minutos.
	 * Un metodo publico getFecha que devuelve un Date con la fecha seleccionada (fecha, hora y minutos)
	 * Autor JR.
	 */

	private HorizontalPanel contenedor = new HorizontalPanel();
	private DateBox fecha = new DateBox();
	private Label lblDia = new Label("Dia del Service");
	private Date actual;
	
	private GlassPopup glass = new GlassPopup();

	public ComboFecha(){
		obtenerHoraServidor();
		
	}

	private void obtenerHoraServidor() {
		ProyectoBilpa.greetingService.obtenerHoraServidorDate(new AsyncCallback<Date>() {
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar la hora del servidor");
				vpu.showPopUp();
			}

			public void onSuccess(Date result) 
			{
				actual = result;
				setearFecha();
				setearWidgets();
			}
		});	
	}
	
	@SuppressWarnings("deprecation")
	private void setearFecha(){
		fecha.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getLongDateFormat()));
		fecha.setValue(actual, true);
		lblDia.setStyleName("Negrita");
	}
	
	private void setearWidgets(){
		fecha.setTitle("Fecha");
		contenedor.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		contenedor.setSpacing(5);
		contenedor.add(lblDia);
		contenedor.add(fecha);
	}


	public HorizontalPanel getCombo(){
		return contenedor;
	}
	
	public Date getFecha(){
		Date date = fecha.getValue();
		return date;
	}
	
}
