package app.client.iu.listados;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.client.ProyectoBilpa;
import app.client.dominio.Persona;
import app.client.dominio.Sello;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.utilObjects.FileDownload;
import app.client.utilidades.utilObjects.GlassPopup;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;

public class IUConsultaOrdenesSello {

	FileDownload file = new FileDownload();

	private VerticalPanel vPanelBig = new VerticalPanel();
	private HorizontalPanel hPanelPrincipal = new HorizontalPanel();
	private HorizontalPanel hPanelListBoxSello = new HorizontalPanel();

	private VerticalPanel panelVtitulo= new VerticalPanel();
	private VerticalPanel panelVSubtitulo= new VerticalPanel();

	private Label lblTituloPrincipal = new Label("Listado de órdenes por Sello");
	private Label lblSubTituloPrincipal = new Label("Seleccione un sello y un rango de fechas para ver las órdenes");

	private List<Sello> sellos = new ArrayList<Sello>();
	private ListBox listBoxSellos = new ListBox();
	private Label lblTituloSellos = new Label("Sello");

	DatePicker dPinicio = new DatePicker();
	DatePicker dPFin = new DatePicker();

	Date inicio = new Date();
	Date fin = new Date();

	private Sello selloSeleccionado;
	
	private VerticalPanel panelVFechaInicio= new VerticalPanel();
	private VerticalPanel panelVFechaFin= new VerticalPanel();

	private Label lblFechaInicio = new Label("Fecha de Inicio");
	private Label lblFechaFin = new Label("Fecha de Fin");

	private VerticalPanel panelVBotones= new VerticalPanel();

	Button btnVer = new Button("Ver");	
	
	private GlassPopup glass = new GlassPopup();

	private Persona sesion;

	public VerticalPanel getPrincipalPanel() {
		return vPanelBig;
	}

	public IUConsultaOrdenesSello(Persona persona){
		this.sesion = persona;
		Date fecha = new Date();
		dPinicio.setValue(fecha, true);
		dPFin.setValue(fecha, true);

		setearWidgets();
		agregarWidgets();
		cargarWidgets();
		color();

		dPinicio.addValueChangeHandler(new ValueChangeHandler<Date>() {
			public void onValueChange(ValueChangeEvent<Date> event) {
				inicio = event.getValue();
			}
		});

		dPFin.addValueChangeHandler(new ValueChangeHandler<Date>() {
			public void onValueChange(ValueChangeEvent<Date> event) {
				fin = event.getValue();

			}
		});

		btnVer.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				fechas();
			}
		});	
	}


	private void color() {
		lblTituloSellos.setStyleName("Negrita");
		lblFechaInicio.setStyleName("Negrita");
		lblFechaFin.setStyleName("Negrita");

	}

	private void cargarWidgets() {
		ProyectoBilpa.greetingService.obtenerSellos(new AsyncCallback<ArrayList<Sello>>() {

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener los sellos");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<Sello> result) {
				for (Sello s : result)
				{
					if (!s.getNombre().equalsIgnoreCase("N/A"))
					{
						listBoxSellos.addItem(s.getNombre(), s.getNombre());
						sellos.add(s);
					}
				}                
			}
		});

	}

	private Sello buscarSello(String nombre)
	{
		for (Sello s : sellos)
		{
			if (s.getNombre().equals(nombre))
			{
				return s;
			}
		}
		return null;
	}

	private void setearWidgets() {
		lblTituloPrincipal.setStyleName("Titulo");
		lblSubTituloPrincipal.setStyleName("SubTitulo");

		btnVer.setWidth("100px");

		listBoxSellos.setWidth("150px");

		hPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		hPanelPrincipal.setSpacing(3);		
		panelVFechaInicio.setSpacing(10);
		panelVFechaFin.setSpacing(10);
		panelVBotones.setSpacing(10);

	}

	private void agregarWidgets() {

		hPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelBig.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		panelVtitulo.add(lblTituloPrincipal);
		panelVSubtitulo.add(lblSubTituloPrincipal);

		hPanelListBoxSello.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hPanelListBoxSello.setSpacing(5);
		hPanelListBoxSello.add(lblTituloSellos);
		hPanelListBoxSello.add(listBoxSellos);

		panelVFechaInicio.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panelVFechaFin.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		panelVFechaInicio.add(lblFechaInicio);
		panelVFechaInicio.add(dPinicio);
		panelVFechaFin.add(lblFechaFin);
		panelVFechaFin.add(dPFin);

		hPanelPrincipal.add(panelVFechaInicio);
		hPanelPrincipal.add(panelVFechaFin);
		hPanelPrincipal.add(panelVBotones);

		panelVBotones.add(btnVer);

		vPanelBig.setSpacing(5);
		vPanelBig.add(panelVtitulo);
		vPanelBig.add(panelVSubtitulo);

		vPanelBig.setSpacing(10);
		vPanelBig.add(hPanelListBoxSello);
		vPanelBig.add(hPanelPrincipal);

		vPanelBig.add(file);
	}

	private void fechas(){
		if(inicio.getTime() < fin.getTime())
		{
			selloSeleccionado = buscarSello(listBoxSellos.getItemText(listBoxSellos.getSelectedIndex()));

			if (selloSeleccionado != null)
			{
				ProyectoBilpa.greetingService.ordenesPorSelloYFechas(selloSeleccionado, inicio, fin, new AsyncCallback<Boolean>() {
					public void onFailure(Throwable caught) {
						ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar ordenes por sello");
						vpu.showPopUp();
					}

					public void onSuccess(Boolean result) {
						if (result)
						{
							crearArchivo();						
						}
						else
						{
							ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "No se encontraron ordenes para ese Sello en ese periodo de tiempo");
							vpu.showPopUp();
						}
					}
				});				
			}
		}
		else
		{
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "La fecha de fin debe ser mayor a la fecha de inicio");
			vpu.showPopUp();
		}
	}

	private void crearArchivo() {
		ProyectoBilpa.greetingService.crearExcelOrdenesSello(selloSeleccionado, inicio, fin, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al crear PDF");
				vpu.showPopUp();
			}

			public void onSuccess(String result) {
				if (result.equals(""))
				{
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al crear PDF");
					vpu.showPopUp();						
				}
				else
				{
					file.download();
				}
			}
		});
	}
}
