package app.client.iu.listados;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.ProvidesKey;

import app.client.ProyectoBilpa;
import app.client.dominio.Organizacion;
import app.client.dominio.Persona;
import app.client.dominio.Sello;
import app.client.dominio.data.OrdenData;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.utilObjects.FileDownload;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;

public class IUReporteVisitas extends Composite{
	
	private Label lblTituloPrincipal = new Label("Reporte visitas preventivas");
	
	private Persona sesion;
	
	FileDownload file = new FileDownload();
	
	private RadioButton rdbOperador = new RadioButton("via", "Operador");
	private RadioButton rdbPetrolera = new RadioButton("via", "Petrolera");
	private RadioButton rdbBilpa = new RadioButton("via", "Bilpa");
	
	private Label lblPetrolera = new Label("");
	private VerticalPanel vPanelPrincipal = new VerticalPanel();

	private HorizontalPanel hPanelRadios = new HorizontalPanel();
	
	private HorizontalPanel hPanelPrincipal = new HorizontalPanel();
	private HorizontalPanel hPanelContenedor = new HorizontalPanel();
	private VerticalPanel vPanel1 =  new VerticalPanel();
	
	private VerticalPanel vPanelTabla = new VerticalPanel();
	
	private ListBox listBoxPetroleras = new ListBox();	
	private HorizontalPanel hPanelDatos = new HorizontalPanel();

	private DateBox dbDesde = new DateBox();
	private DateBox dbHasta = new DateBox();

	private FlexTable tableDatos = new FlexTable();
	private Grid grilla = new Grid(1,8);
	private DecoratorPanel decorator = new DecoratorPanel();

	private PopupCargando popUp = new PopupCargando("Cargando...");
	private Button btnAceptar = new Button("Aceptar");
	private GlassPopup glass = new GlassPopup();
	
	private static final ProvidesKey<OrdenData> KEY_PROVIDER = new ProvidesKey<OrdenData>() {
		public Object getKey(OrdenData item) {
			return item.getNumero();
		}
	};
	
	Button btnGenerarExcel = new Button("Ver",new ClickHandler() {
		public void onClick(ClickEvent event) {
			generar();
		}
	});

	public VerticalPanel getPrincipalPanel() {
		return vPanelPrincipal;
	}
	
	public IUReporteVisitas(Persona persona)	{
		setearWidgets();			
		cargarPanelesConWidgets();

		obtenerHoraServidor();
		cargarPetroleras();
		setDateFormat();

		cargarGrilla();
		color();
		
		btnAceptar.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				generar();
			}
		});
	}

	private void cargarPetroleras() {
		ProyectoBilpa.greetingService.obtenerSellos(new AsyncCallback<ArrayList<Sello>>() {

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener sellos" + caught.getMessage());
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<Sello> result) {
				for (Sello sello : result){
					listBoxPetroleras.addItem(sello.getNombre(), sello.getId()+"");
				}
				listBoxPetroleras.setSelectedIndex(1);
			}
		});	
	}
	
	private void obtenerHoraServidor() {
		ProyectoBilpa.greetingService.obtenerHoraServidorDate(new AsyncCallback<Date>() {
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar la hora del servidor");
				vpu.showPopUp();
			}

			public void onSuccess(Date result) {
				Date date1 = (Date) result.clone();
				date1.setYear(date1.getYear()-1);
				
				dbDesde.setValue(date1);
				dbHasta.setValue(result);
			}
		});	
	}

	private void generar() {
		popUp.show();
		if(validarFechas()){
			int idPetrolera = Integer.valueOf(listBoxPetroleras.getValue(listBoxPetroleras.getSelectedIndex()));
			Sello sello = crearPetrolera(idPetrolera);
			
			Date inicio = dbDesde.getValue();
			Date fin = dbHasta.getValue();
			
			getFromServer(sello, inicio, fin);
			
		}else{
			popUp.hide();
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "La fecha de fin debe ser mayor a la fecha de inicio");
			vpu.showPopUp();
		}
	}

	private Sello crearPetrolera(int idPetrolera) {
		Sello sello = new Sello();
		sello.setId(idPetrolera);
		
		if(idPetrolera == 3){
			sello.setNombre("Petrobras");
		}
		if(idPetrolera == 2){
			sello.setNombre("Ancap");
		}
		return sello;
	}

	private Organizacion getOrganizacion() {
		if (rdbBilpa.getValue()){
			return Organizacion.Bilpa;
		}
		if (rdbOperador.getValue()){
			return Organizacion.Operador;
		}
		return Organizacion.Petrolera;
	}

	private void getFromServer(Sello selloSeleccionado, Date inicio, Date fin) {
		ProyectoBilpa.greetingService.reporteVisitas(selloSeleccionado, inicio, fin, getOrganizacion(), new AsyncCallback<String>() {

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al crear el reporte de visitas!");
				vpu.showPopUp();
			}

			public void onSuccess(String result) {
				if (result.equals("")) {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al crear el reporte de visitas");
					vpu.showPopUp();							
				}else {
					file.download();
				}
				popUp.hide();
			}
		});
	}
	
	private boolean validarFechas() {
		return true;
	}

	private void color() {
		lblPetrolera.setStyleName("SubTitulo");
		btnAceptar.setTitle("Genera una planilla con los datos correspondientes a la petrolera y rango de fechas seleccionado");
		btnAceptar.setWidth("100px");
	}

	private void cargarGrilla()	{
		grilla.setSize("500px", "90px");

		grilla.setCellSpacing(5);
		grilla.setWidget(0, 0, label("Petrolera"));
		grilla.setWidget(0, 1, listBoxPetroleras);

		grilla.setWidget(0, 2, label("Desde"));
		grilla.setWidget(0, 3, dbDesde);

		grilla.setWidget(0, 4, label("Hasta"));
		grilla.setWidget(0, 5, dbHasta);

		grilla.setWidget(0, 6, hPanelRadios);
		grilla.setWidget(0, 7, btnAceptar);

		decorator.add(grilla);
	}

	private Label label(String text) {
		Label label = new InlineLabel(text);
		label.setStyleName("Negrita");
		label.setWordWrap(false);
		return label;
	}

	@SuppressWarnings("deprecation")
	private void setearWidgets() {
		hPanelRadios.setSpacing(6);
		hPanelRadios.add(rdbPetrolera);
		hPanelRadios.add(rdbBilpa);
		hPanelRadios.add(rdbOperador);

		rdbPetrolera.setValue(true);
		
		this.vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal.setSpacing(5);
		this.vPanelPrincipal.setWidth("800px");
		this.btnGenerarExcel.setWidth("100px");

		listBoxPetroleras.setVisibleItemCount(1);
		listBoxPetroleras.setTitle("Lista de Petroleras");
		
		lblTituloPrincipal.setStyleName("Titulo");
		
		vPanelPrincipal.add(lblTituloPrincipal);
		
		hPanelDatos.setSpacing(5);

		tableDatos.setCellSpacing(5);
		tableDatos.setCellPadding(2);
		tableDatos.setBorderWidth(1);
	}

	private void cargarPanelesConWidgets() {
		hPanelDatos.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		vPanel1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);	
		vPanel1.add(hPanelDatos);	
		hPanelDatos.add(this.tableDatos);
		vPanel1.setSpacing(20);
		hPanelContenedor.add(vPanel1);
		
		hPanelPrincipal.setSpacing(10);
		vPanelPrincipal.setSpacing(10);
	

		hPanelPrincipal.add(decorator);
		vPanelPrincipal.add(hPanelPrincipal);
		vPanelPrincipal.add(lblPetrolera);
		
		vPanelPrincipal.add(file);
		vPanelPrincipal.add(vPanelTabla);
		//vPanelPrincipal.add(pager);
	}

	private void setDateFormat() {
		dbDesde.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getLongDateFormat()));
		dbHasta.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getLongDateFormat()));
	}
}
