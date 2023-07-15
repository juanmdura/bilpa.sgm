package app.client.utilidades.utilObjects;

import java.util.Date;

import app.client.ProyectoBilpa;
import app.client.dominio.Activo;
import app.client.dominio.EstadoActivo;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.Constants.OPERACION_GARANTIA;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.gwt.user.datepicker.client.DateBox;

public class GarantiaUtil extends Composite{

	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private String FORMATO_FECHA = "dd/MM/yyyy";
	
	VerticalPanel vpSpace = new VerticalPanel();
	Label labelFinPantallaOrden = new Label("Fin garantía");
	//Label labelGarantia = new Label("Garantía");
	//Label labelAbono = new Label("Abono");
	//Label labelSinAbono = new Label("Sin Abono");
	
	public RadioButton radioGarantia = new RadioButton("grupo", "Garantia");
	public RadioButton radioAbono = new RadioButton("grupo", "Abono");
	public RadioButton radioSinAbono = new RadioButton("grupo", "S/ Abono");
	
	public ListBox lbFabricacion = new ListBox();
	
	Label labelInicio = new Label("Inicio");
	Label labelFin = new Label("Fin");
	public DateBox dateBoxInicio = new DateBox();
	public DateBox dateBoxFin = new DateBox();

	private Date fechaServidor;
	Label labelFinDatoOrden = new Label();
	Activo activo;
	
	private GlassPopup glass = new GlassPopup();

	public GarantiaUtil(Activo activo, FlexTable tableDatos, int row, int col, OPERACION_GARANTIA operacion){
		this.activo = activo;
		obtenerHoraServidor(tableDatos, row, col, operacion);
		
		dateBoxInicio.addValueChangeHandler(new ValueChangeHandler<Date>() {
			public void onValueChange(ValueChangeEvent<Date> event) {
				enableDisableDateBoxes();
			}
		});
		
		dateBoxFin.addValueChangeHandler(new ValueChangeHandler<Date>() {
			public void onValueChange(ValueChangeEvent<Date> event) {
				enableDisableDateBoxes();
			}
		});
		
		radioGarantia.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				enableDisableDateBoxes();
			}
		});
		
		radioAbono.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				enableDisableDateBoxes();
			}
		});
		
		radioSinAbono.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				enableDisableDateBoxes();
			}
		});
		
		
		inicializarAniosFabricacion();
	}
	
	private void inicializarAniosFabricacion() {
		
		DateTimeFormat sdf = DateTimeFormat.getFormat("yyyy");
		int anioActual = Integer.valueOf(sdf.format(new Date()));
		int to = anioActual - 20;
		
		lbFabricacion.addItem("Año Fabricación");
		for(int i = anioActual; i >= to; i--){
			lbFabricacion.addItem(String.valueOf(i));
		}
		
	}

	public void getFechaFinSiEstaEnGarantia(FlexTable tableDatos, int row, int col){
		if(estaEnGarantia(activo.getInicioGarantia(), activo.getFinGarantia())){
			labelFinDatoOrden.setText(getFechaFormateada(activo.getFinGarantia()));
		}else{
			labelFinDatoOrden.setText("");
			labelFinPantallaOrden.setText("");
		}
	}
	
	public boolean estaEnGarantia(Date inicio, Date fin) {
		return inicio != null && inicio.getTime() <= fechaServidor.getTime() && fin != null && fin.getTime() >= fechaServidor.getTime();
	}

	public VerticalPanel getPrincipalPanel() {
		return vPanelPrincipal;
	}
	
	public void obtenerHoraServidor(final FlexTable tableDatos, final int row, final int col, final OPERACION_GARANTIA operacion){
		ProyectoBilpa.greetingService.obtenerFechaServidor(new AsyncCallback<Date>() {
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar la hora del servidor");
				vpu.showPopUp();
			}

			public void onSuccess(Date fechaServidorParam) 
			{
				fechaServidor = fechaServidorParam;
				Date fechaServidorMasUnAno = (Date) fechaServidor.clone();
				CalendarUtil.addDaysToDate(fechaServidorMasUnAno, 365);
				
				setearWidgets(tableDatos, row, col, operacion);
				
				if(operacion.equals(OPERACION_GARANTIA.PANTALLA_ORDEN)){
					getFechaFinSiEstaEnGarantia(tableDatos, row, col);
					
				}else if(operacion.equals(OPERACION_GARANTIA.AGREGAR)){
					dateBoxInicio.setValue(fechaServidorParam);		
					dateBoxFin.setValue(fechaServidorMasUnAno);
					
					if(activo != null){
						setearWidgetSiEstaEnGarantia();
					}
					
				}else if(operacion.equals(OPERACION_GARANTIA.MODIFICAR)){
					dateBoxInicio.setValue(activo.getInicioGarantia() != null ? activo.getInicioGarantia() : fechaServidorParam);
					dateBoxFin.setValue(activo.getFinGarantia() != null ? activo.getFinGarantia() : fechaServidorMasUnAno);
					
					setearWidgetSiEstaEnGarantia();
				}
			}
		});	
	}
	
	private void setearWidgets(FlexTable tableDatos, int row, int col, OPERACION_GARANTIA operacion) {
		//labelGarantia.setStyleName("Negrita");
		labelInicio.setStyleName("Negrita");
		labelFin.setStyleName("Negrita");
		labelFinPantallaOrden.setStyleName("Negrita");
		labelFinDatoOrden.setStyleName("Roja");
		
		radioGarantia.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				enableDisableDateBoxes();
			}
		});

	    vpSpace.setWidth("20px");
	    
	    dateBoxInicio.setWidth("80px");
	    dateBoxFin.setWidth("80px");
	    
	    enableDisableDateBoxes();
	    
	    dateBoxInicio.setFormat(new DateBox.DefaultFormat( DateTimeFormat.getFormat(FORMATO_FECHA)));
	    dateBoxFin.setFormat(new DateBox.DefaultFormat( DateTimeFormat.getFormat(FORMATO_FECHA)));
	    
	    if(operacion.equals(OPERACION_GARANTIA.PANTALLA_ORDEN)){
		    tableDatos.setWidget(row, col, vpSpace);
		    
			tableDatos.setWidget(row, col+1, labelFinPantallaOrden);
			tableDatos.setWidget(row, col+2, labelFinDatoOrden);
	    }else{
	    	tableDatos.setWidget(row, col, vpSpace);
	    	
	    	//tableDatos.setWidget(row, col+1, labelGarantia);
	    	tableDatos.setWidget(row, col+1, radioGarantia);
	    	
	    	//tableDatos.setWidget(row, col+3, labelAbono);
	    	tableDatos.setWidget(row, col+2, radioSinAbono);
	    	
	    	//tableDatos.setWidget(row, col+5, labelSinAbono);
	    	tableDatos.setWidget(row, col+3, radioAbono);
	    	
	    	tableDatos.setWidget(row, col+4, lbFabricacion);
	    	
	    	tableDatos.setWidget(row+1, col+1, labelInicio);
	    	tableDatos.setWidget(row+1, col+2, dateBoxInicio);
	    	
	    	tableDatos.setWidget(row+2, col+1, labelFin);
	    	tableDatos.setWidget(row+2, col+2, dateBoxFin);
	    }
	}
	
	public void setearWidgetSiEstaEnGarantia() {
		if(estaEnGarantia(activo.getInicioGarantia(), activo.getFinGarantia())){
			radioGarantia.setChecked(true);
		}else{
			radioGarantia.setChecked(false);
		}
		enableDisableDateBoxes();
	}
	
	public void enableDisableDateBoxes() {
		labelFin.setStyleName("Normal");
		labelInicio.setStyleName("Normal");
		radioGarantia.setStyleName("Normal");
		radioAbono.setStyleName("Normal");
		radioSinAbono.setStyleName("Normal");
		
		if(radioGarantia.isChecked()){
			dateBoxInicio.setEnabled(true);
			dateBoxFin.setEnabled(true);
		}else{
			dateBoxInicio.setEnabled(false);
			dateBoxFin.setEnabled(false);
		}
		
		if(radioAbono.isChecked() || radioSinAbono.isChecked()){
			dateBoxInicio.setEnabled(false);
			dateBoxFin.setEnabled(false);
		}
		
		if (radioGarantia.getValue()){
			radioGarantia.setStyleName("Negrita");
			
		} else if (radioAbono.getValue()){
			radioAbono.setStyleName("Negrita");
		
		} else if (radioSinAbono.getValue()){
			radioSinAbono.setStyleName("Negrita");
		}

		if(radioGarantia.isChecked() && estaEnGarantia(dateBoxInicio.getValue(), dateBoxFin.getValue())){
			labelInicio.setStyleName("NegritaVerde");
			labelFin.setStyleName("NegritaVerde");
		
		} else if (radioGarantia.isChecked()){
			labelInicio.setStyleName("Negrita");
			labelFin.setStyleName("Negrita");
		
		} else {
			labelInicio.setStyleName("Normal");
			labelFin.setStyleName("Normal");
		}
		
	}

	public boolean validarYSetearGarantia(Activo a) {
		
		if(radioGarantia.isChecked()) {
			
			a.setEstado(EstadoActivo.GARANTIA);
			if (dateBoxInicio.getValue() != null && dateBoxFin.getValue() !=null && dateBoxFin.getValue().compareTo(dateBoxInicio.getValue()) > 0){
				Date inicio = dateBoxInicio.getValue();
				Date fin = dateBoxFin.getValue();
				
				DateTimeFormat.getFormat(FORMATO_FECHA).format(inicio);
				DateTimeFormat.getFormat(FORMATO_FECHA).format(fin);
				
				a.setInicioGarantia(inicio);
				a.setFinGarantia(fin);
				
			}else{
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Fecha de inicio y/o fecha de fin de garantía invalida");
				vpu.showPopUp();
				return false;
			}
			
		} else {
			
			a.setInicioGarantia(null);
			a.setFinGarantia(null);
			
			if(radioAbono.isChecked()) {
				a.setEstado(EstadoActivo.ABONO);
			}
			if(radioSinAbono.isChecked()) {
				a.setEstado(EstadoActivo.SIN_ABONO);
			}
			
		}
		return true;
	}

	public String getFechaFormateada(Date fecha) {
		return DateTimeFormat.getFormat(FORMATO_FECHA).format(fecha);
	}

	public int getAnioFabricacion() {
		if(lbFabricacion.getSelectedIndex() == 0) {
			return 0;
		} else {
			return Integer.valueOf(lbFabricacion.getItemText(lbFabricacion.getSelectedIndex()));
		}
	}

	public void setRadioGarantia() {
		radioGarantia.setValue(true);
	}

	public void setRadioAbono() {
		radioAbono.setValue(true);
	}

	public void setRadioSinAbono() {
		radioSinAbono.setValue(true);
	}

	public void setAnioFabricado(int anioFabricacion) {
		int selected = lbFabricacion.getSelectedIndex();
		for(int i = selected; i < lbFabricacion.getItemCount(); i++) {
			if(lbFabricacion.getItemText(i).equalsIgnoreCase(anioFabricacion+"")){
				lbFabricacion.setItemSelected(i, true);
				return;
			}
		}
	}
	
}
