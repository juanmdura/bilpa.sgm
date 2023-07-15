package app.client.iu.correctivo;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;

import app.client.ProyectoBilpa;
import app.client.dominio.IUCorrectivo;
import app.client.dominio.IUCorrectivo.IUCorrectivoEnum;
import app.client.dominio.Orden;
import app.client.dominio.Persona;
import app.client.dominio.Tecnico;
import app.client.dominio.TipoTrabajo;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.UtilFechas;
import app.client.utilidades.UtilOrden;
import app.client.utilidades.UtilUI;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.ListBoxAdv;

public class IUCorrectivoToolBar extends Composite {
	private IUCorrectivoEnum iu;
	private Orden orden;
	private Persona sesion;

	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private HorizontalPanel hPanelPrincipal = new HorizontalPanel();
	private HorizontalPanel hpBotonesDescargas;

	private GlassPopup glass = new GlassPopup();

	private Label lblTecnico = new Label("Técnico asignado ");
	private Label lblTipoTrabajo = new Label("Tipo de trabajo ");
	private Label lblCumplimiento = new Label("Cumplimiento ");

	private ArrayList<TipoTrabajo> listaTipoTrabajo = new ArrayList<TipoTrabajo>();

	private ListBox listBoxTecnicoAsignado = new ListBox();
	private ListBoxAdv listBoxTipoDeTrabajo = new ListBoxAdv();
	private DateBox dateBoxCumplimiento = new DateBox();

	public IUCorrectivoToolBar(Persona sesion, Orden orden, IUCorrectivoEnum iu, HorizontalPanel hpBotonesDescargas) {
		super();
		this.sesion = sesion;
		this.iu = iu;
		this.orden = orden;
		this.hpBotonesDescargas = hpBotonesDescargas;

		set();
		setearWidgets();
		agregarWidgets();
		eventos();

		cargarListaTecnicos();

		if (IUCorrectivo.esAlta(iu)){
			cargarTiposDeTrabajoAlta();
		} else if (IUCorrectivo.esIni(iu) || IUCorrectivo.esTec(iu)){
			cargarTiposDeTrabajoIni();
		}
	}

	private void eventos() {
		listBoxTipoDeTrabajo.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				if (!listBoxTipoDeTrabajo.getValue(listBoxTipoDeTrabajo.getSelectedIndex()).toString().equals("1")){
					dateBoxCumplimiento.setEnabled(true);
				} else {
					dateBoxCumplimiento.setEnabled(false);
				}
			}
		});
	}

	private void set() {
		titulo();
	}

	private void titulo() {
	}

	private void agregarWidgets() {
		vPanelPrincipal.add(hPanelPrincipal);
		hPanelPrincipal.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		HorizontalPanel hpTec = new HorizontalPanel();
		HorizontalPanel hpTip = new HorizontalPanel();
		HorizontalPanel hpCum = new HorizontalPanel();
		HorizontalPanel hpBotones = new HorizontalPanel();

		hPanelPrincipal.setSpacing(25);

		hpTec.setSpacing(8);
		hpTip.setSpacing(8);
		hpCum.setSpacing(8);
		hpBotones.setSpacing(8);

		hPanelPrincipal.add(hpTec);
		hPanelPrincipal.add(hpTip);
		hPanelPrincipal.add(hpCum);
		hPanelPrincipal.add(hpBotones);

		hpTec.add(lblTecnico);
		hpTec.add(listBoxTecnicoAsignado);

		hpTip.add(lblTipoTrabajo);
		hpTip.add(listBoxTipoDeTrabajo);

		hpCum.add(lblCumplimiento);
		hpCum.add(dateBoxCumplimiento);
		
		if (hpBotonesDescargas != null){
			hpBotones.add(hpBotonesDescargas);
		}
	}

	private void setearWidgets() {
		vPanelPrincipal.setWidth("100%");
		hPanelPrincipal.setWidth("100%");

		listBoxTipoDeTrabajo.setWidth("170px");

		lblTecnico.setStyleName("Negrita");
		lblTipoTrabajo.setStyleName("Negrita");
		lblCumplimiento.setStyleName("Negrita");

		dateBoxCumplimiento.setFormat(new DateBox.DefaultFormat 
				(DateTimeFormat.getFormat(UtilFechas.DATE_FORMAT_SHORT)));
	}

	public VerticalPanel getPrincipalPanel() {
		return vPanelPrincipal;
	}

	public ListBoxAdv getListBoxTipoDeTrabajo() {
		return listBoxTipoDeTrabajo;
	}

	public ListBox getListBoxTecnicoAsignado(){
		return listBoxTecnicoAsignado;
	}

	public ArrayList<TipoTrabajo> getListaTipoTrabajo(){
		return listaTipoTrabajo;
	}

	public DateBox getDateBoxCumplimiento(){
		return dateBoxCumplimiento;
	}
	
	public boolean validarCumplimiento() {
		Date now = new Date();
		now.setHours(0);
		now.setMinutes(0);
		now.setSeconds(-1);

		if (getDateBoxCumplimiento().getValue() != null && getDateBoxCumplimiento().getValue().before(now)){		
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "La fecha de cumplimiento debe ser mayor a la fecha de hoy");
			vpu.showPopUp();
			return false;
		}
		return true;
	}

	private void cargarTiposDeTrabajoIni() {
		ProyectoBilpa.greetingService.obtenerTiposDeTrabajo( new AsyncCallback<ArrayList<TipoTrabajo>>() {
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener los tipos de trabajo");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<TipoTrabajo> result) {
				if (result.size() > 0)
				{
					for (int i = 0; i < result.size(); i++) {
						listBoxTipoDeTrabajo.addItem(result.get(i).toString(), result.get(i).getId() + "");
						listaTipoTrabajo.add(result.get(i));
					}			
					listBoxTipoDeTrabajo.setItemSelectedByValue(orden.getTipoTrabajo().getId()+"");

					if (!listBoxTipoDeTrabajo.getValue(listBoxTipoDeTrabajo.getSelectedIndex()).toString().equals("1")){
						if (orden.getFechaCumplimiento() != null){
							dateBoxCumplimiento.setValue(orden.getFechaCumplimiento());
						}
						
						if (sesion.getRol() != 3){
							dateBoxCumplimiento.setEnabled(true);
						} else {
							dateBoxCumplimiento.setEnabled(false);
						}
					} else {
						dateBoxCumplimiento.setEnabled(false);
					}
				}
				else
				{
					listBoxTipoDeTrabajo.addItem("No existen tipos de trabajo", -1 + "");						
				}
			}
		});
	}

	private void cargarTiposDeTrabajoAlta() {
		ProyectoBilpa.greetingService.obtenerTiposDeTrabajo( new AsyncCallback<ArrayList<TipoTrabajo>>() {
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener los tipos de trabajo");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<TipoTrabajo> result) {
				if (result.size() > 0)
				{
					for (int i = 0; i < result.size(); i++) {
						listBoxTipoDeTrabajo.addItem(result.get(i).toString(), result.get(i).getId() + "");
						listaTipoTrabajo.add(result.get(i));
					}					
				}
				else
				{
					listBoxTipoDeTrabajo.addItem("No existen tipos de trabajo", -1 + "");						
				}

				if (!listBoxTipoDeTrabajo.getValue(listBoxTipoDeTrabajo.getSelectedIndex()).toString().equals("1")){
					if (orden.getFechaCumplimiento() != null){
						dateBoxCumplimiento.setValue(orden.getFechaCumplimiento());
					}
					dateBoxCumplimiento.setEnabled(true);
				} else {
					dateBoxCumplimiento.setEnabled(false);
				}
			}
		});
	}

	private void cargarListaTecnicos() {
		ProyectoBilpa.greetingService.cargarTecnicosYEnc(new AsyncCallback<ArrayList<Persona>>() {
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener tecnicos");
				vpu.showPopUp();
			}
			public void onSuccess(ArrayList<Persona> result) {
				listBoxTecnicoAsignado.addItem(UtilOrden.personaAsignadaOrden(1));
				for (int i=0; i<result.size();i++){
					listBoxTecnicoAsignado.addItem(result.get(i).toString(), result.get(i).getId()+"");
				}		
				UtilUI.setearTecnicoDeOrden(orden, listBoxTecnicoAsignado);
			}
		});
	}

	public void setearCumplimiento() {
		Date now = new Date();
		now.setHours(0);
		now.setMinutes(0);
		now.setSeconds(-1);

		if (orden.getTipoTrabajo().getId() != 1){
			if (dateBoxCumplimiento.getValue() != null && dateBoxCumplimiento.getValue().before(now)){
				ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "La fecha de cumplimiento debe ser mayor a la fecha de hoy");
				vpu.showPopUp();
			} else {
				orden.setFechaCumplimiento(dateBoxCumplimiento.getValue());
			}
		} else {
			orden.setFechaCumplimiento(null);
		}
	}

	public void setearTipoTrabajo() 
	{
		int idTipoTrabajo = Integer.valueOf(listBoxTipoDeTrabajo.getValue(listBoxTipoDeTrabajo.getSelectedIndex()));
		orden.setTipoTrabajo(buscarTipoTrabajo(idTipoTrabajo));		
	}

	private TipoTrabajo buscarTipoTrabajo(int idTipoTrabajo) {
		for(TipoTrabajo tt : listaTipoTrabajo)
		{
			if(tt.getId() == idTipoTrabajo)
				return tt;
		}
		return null;
	}

	public void setearTecnicoAsignadoAlta() {
		String tecnicoAsignado = this.listBoxTecnicoAsignado.getItemText(this.listBoxTecnicoAsignado.getSelectedIndex());
		if(!tecnicoAsignado.equals(UtilOrden.personaAsignadaOrden(1))){  	//Si el tecnico asignado No es "Sin asignar"
			orden.setEstadoOrden(2);			
			int id = Integer.valueOf(this.listBoxTecnicoAsignado.getValue(this.listBoxTecnicoAsignado.getSelectedIndex()));
			orden.setTecnicoAsignado(new Tecnico(id));
		}
	}
}
