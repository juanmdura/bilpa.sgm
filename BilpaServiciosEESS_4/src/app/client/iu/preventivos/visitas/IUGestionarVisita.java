package app.client.iu.preventivos.visitas;

import java.util.List;

import app.client.ProyectoBilpa;
import app.client.dominio.EstadoVisita;
import app.client.dominio.data.EstacionData;
import app.client.dominio.data.TecnicoData;
import app.client.dominio.data.VisitaDataList;
import app.client.iu.preventivos.estacion.IUVisitasDeEstacion;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;

public class IUGestionarVisita {

	private VerticalPanel vPanelDialNuevaVisita = new VerticalPanel();
	private HorizontalPanel hPanelBotonera = new HorizontalPanel();
	
	private Label lblTitulo = new Label();

	private IUVisitasDeEstacion padre;

	private DialogBox dialogBoxNuevaVisita = new DialogBox();

	private Label tecnicoLabel = new Label("Técnico");
	private Label fechaLabel = new Label("Planificada para ");
	private DateBox fechaVisita = new DateBox();

	private EstacionData estacion;

	private ListBox listBoxListaTecnicos = new ListBox();
	private PopupCargando popUp = new PopupCargando("Cargando...");

	private List<TecnicoData> tecnicos;

	DecoratorPanel decorator = new DecoratorPanel();

	private GlassPopup glass = new GlassPopup();
	
	private Button btnAceptarModif = new Button("Aceptar", new ClickHandler() {
		public void onClick(ClickEvent event) {
			dialogBoxNuevaVisita.hide(true);
			glass.hide();
			agendarVisita();
		}
	});

	private void agendarVisita() {
		popUp.show();
		fechaVisita.setWidth("200px");
		listBoxListaTecnicos.setWidth("200px");
		
		if (fechaVisita.getValue() == null){
				//|| fechaVisita.getValue().before(new Date())) {
			popUp.hide();
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe seleccionar una fecha");
			vpu.showPopUp();
			return;
		}
		TecnicoData t = null;
		int posicionTecnico = listBoxListaTecnicos.getSelectedIndex();
		if (posicionTecnico != 0) {
			int tecnicoSeleccionado = listBoxListaTecnicos.getSelectedIndex() - 1;
			t = tecnicos.get(tecnicoSeleccionado);
		}

		VisitaDataList visitaNueva = new VisitaDataList();
		visitaNueva.setFechaProximaVisita(fechaVisita.getValue());

		visitaNueva.setEstacionData(estacion);
		visitaNueva.setTecnicoData(t);

		if (t == null) {
			visitaNueva.setEstado(EstadoVisita.SIN_ASIGNAR);
		} else {
			visitaNueva.setEstado(EstadoVisita.PENDIENTE);
		}

		ProyectoBilpa.greetingService.nuevaVisita(visitaNueva,
				new AsyncCallback<Boolean>() {
					public void onFailure(Throwable caught) {
						popUp.hide();
						glass.hide();
						ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar nueva visita");
						vpu.showPopUp();
					}

					public void onSuccess(Boolean result) {
						popUp.hide();
						padre.obtenerListaVisitaDataPorEmpresa(estacion
								.getNombre());
					}
				});
	}

	private void obtenerTecnicos() {
		popUp.show();
		ProyectoBilpa.greetingService
				.obtenerTodosLosDataTecnico(new AsyncCallback<List<TecnicoData>>() {
					public void onFailure(Throwable caught) {
						popUp.hide();
						ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener lista de data visitas");
						vpu.showPopUp();
					}

					public void onSuccess(List<TecnicoData> result) {
						tecnicos = result;
						cargarListBxTecnicos();
						cargarGrilla();
						popUp.hide();
					}
				});

	}

	private void cargarListBxTecnicos() {
		listBoxListaTecnicos.addItem("");
		for (TecnicoData t : tecnicos) {
			listBoxListaTecnicos.addItem(t.getNombreCompleto());
		}
	}

	private Button btnCancelModif = new Button("Cancelar", new ClickHandler() {
		public void onClick(ClickEvent event) {
			dialogBoxNuevaVisita.hide(true);
			glass.hide();
		}
	});

	public void setPadre(IUVisitasDeEstacion p) {
		padre = p;
	}

	public IUGestionarVisita(IUVisitasDeEstacion p, GlassPopup glass, EstacionData e) {
		estacion = e;
		padre = p;
		lblTitulo.setText("Planificar visita");
		this.glass = glass;
		color();
		cargarPanelesConWidgets();
		obtenerTecnicos();
		
		btnAceptarModif.setWidth("100px");
		btnCancelModif.setWidth("100px");
	}

	private void color() {
		lblTitulo.setStyleName("Titulo");
		tecnicoLabel.setStyleName("Negrita");
		fechaLabel.setStyleName("Negrita");
	}

	private void cargarPanelesConWidgets() {
		vPanelDialNuevaVisita.setSpacing(10);
		vPanelDialNuevaVisita.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hPanelBotonera.setSpacing(10);
		hPanelBotonera.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hPanelBotonera.add(btnCancelModif);
		hPanelBotonera.add(btnAceptarModif);
		vPanelDialNuevaVisita.add(decorator);
		dialogBoxNuevaVisita.add(vPanelDialNuevaVisita);
	}

	public DialogBox gethPanelDialNuevaVisita() {
		return dialogBoxNuevaVisita;
	}

	private void cargarGrilla() {
		vPanelDialNuevaVisita.setSize("350px", "90px");
		vPanelDialNuevaVisita.add(lblTitulo);
		HorizontalPanel hp1 = new HorizontalPanel();
		VerticalPanel vp1 = new VerticalPanel();
		VerticalPanel vp2 = new VerticalPanel();

		vPanelDialNuevaVisita.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		vp1.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		vp2.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		vp1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		vp2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		
		vp1.setSpacing(10);
		vp2.setSpacing(10);
		
		vp1.add(fechaLabel);
		vp1.add(tecnicoLabel);
		
		vp2.add(fechaVisita);
		vp2.add(listBoxListaTecnicos);
		
		vPanelDialNuevaVisita.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

		hp1.add(vp1);
		hp1.add(vp2);
		vPanelDialNuevaVisita.add(hp1);
		
		vPanelDialNuevaVisita.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelDialNuevaVisita.add(hPanelBotonera);
	}
}
