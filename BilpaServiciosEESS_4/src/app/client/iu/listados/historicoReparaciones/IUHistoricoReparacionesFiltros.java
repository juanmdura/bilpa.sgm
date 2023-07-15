package app.client.iu.listados.historicoReparaciones;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.client.ProyectoBilpa;
import app.client.dominio.DateDesdeHasta;
import app.client.dominio.Orden;
import app.client.dominio.Persona;
import app.client.dominio.data.DatoOrdenesActivasEmpresa;
import app.client.dominio.data.EstacionDataList;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;

public class IUHistoricoReparacionesFiltros  extends Composite{

	private HTML htmlTitulo = new HTML("Histórico de reparaciones");
	private Label lblEstacion = new Label("");
	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private VerticalPanel vPanelOrdenes = new VerticalPanel();
	private HorizontalPanel hPanelPrincipal = new HorizontalPanel();
	private HorizontalPanel hPanelContenedor = new HorizontalPanel();
	private VerticalPanel vPanel1 =  new VerticalPanel();
	private List<EstacionDataList> listaEstaciones = new ArrayList<EstacionDataList>();
	private ListBox listBoxEstaciones = new ListBox();	
	private HorizontalPanel hPanelDatos = new HorizontalPanel();

	private DateBox dbDesde = new DateBox();
	private DateBox dbHasta = new DateBox();

	private FlexTable tableDatos = new FlexTable();
	private Grid grilla = new Grid(1,7);
	private DecoratorPanel decorator = new DecoratorPanel();

	private PopupCargando popUp = new PopupCargando("Cargando...");
	
	private GlassPopup glass = new GlassPopup();

	private int idEstacion;

	Button btnVerHistorico = new Button("Histórico",new ClickHandler() 
	{
		public void onClick(ClickEvent event) 
		{
			cargarOrdenes();
		}
	});

	public VerticalPanel getPrincipalPanel() 
	{
		return vPanelPrincipal;
	}

	public IUHistoricoReparacionesFiltros(Persona persona)
	{
		setearWidgets();			
		cargarPanelesConWidgets();
		
		obtenerHoraServidor();
		cargarEstaciones();

		cargarGrilla();
		color();

		hPanelPrincipal.setSpacing(10);
		vPanelPrincipal.setSpacing(10);
		vPanelOrdenes.setSpacing(10);

		hPanelPrincipal.add(decorator);
		vPanelPrincipal.add(hPanelPrincipal);
		vPanelPrincipal.add(lblEstacion);
		vPanelPrincipal.add(vPanelOrdenes);

		listBoxEstaciones.addChangeHandler(new ChangeHandler()
		{
			public void onChange(ChangeEvent arg0) 
			{
				vPanelOrdenes.clear();
				idEstacion = Integer.valueOf(listBoxEstaciones.getValue(listBoxEstaciones.getSelectedIndex()));
				lblEstacion.setText("");
			}
		});

		listBoxEstaciones.addKeyPressHandler(new KeyPressHandler()
		{
			public void onKeyPress(KeyPressEvent event) 
			{
				if (KeyCodes.KEY_ENTER == event.getNativeEvent().getKeyCode())
				{
					cargarOrdenes();
				}
			}
		});
		
		btnVerHistorico.addKeyPressHandler(new KeyPressHandler()
		{
			public void onKeyPress(KeyPressEvent event) 
			{
				if (KeyCodes.KEY_ENTER == event.getNativeEvent().getKeyCode())
				{
					cargarOrdenes();
				}
			}
		});
	}

	private void obtenerHoraServidor() 
	{
		ProyectoBilpa.greetingService.obtenerHoraServidorDate(new AsyncCallback<Date>() {
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar la hora del servidor");
				vpu.showPopUp();
			}

			public void onSuccess(Date result) 
			{
				dbDesde.setValue(result);
				dbHasta.setValue(result);
			}
		});	
	}
	
	private void cargarOrdenes() 
	{
		vPanelOrdenes.clear();
		idEstacion = Integer.valueOf(listBoxEstaciones.getValue(listBoxEstaciones.getSelectedIndex()));
		EstacionDataList e = buscarEstacion(idEstacion);
		cargarTituloEstacion();
		obtenerOrdenesEstacion(e);
	}

	private void color() 
	{
		htmlTitulo.setStyleName("Titulo");
		lblEstacion.setStyleName("SubTitulo");
		btnVerHistorico.setTitle("Lista el histórico del activo seleccionado");
	}

	private void cargarGrilla()
	{
		grilla.setSize("500px", "90px");

		grilla.setCellSpacing(5);
		grilla.setWidget(0, 0, label("Estación"));
		grilla.setWidget(0, 1, listBoxEstaciones);

		grilla.setWidget(0, 2, label("Desde"));
		grilla.setWidget(0, 3, dbDesde);

		grilla.setWidget(0, 4, label("Hasta"));
		grilla.setWidget(0, 5, dbHasta);

		grilla.setWidget(0, 6, btnVerHistorico);

		listBoxEstaciones.setWidth("270px");
		decorator.add(grilla);
	}

	private Label label(String text) 
	{
		Label label = new InlineLabel(text);
		label.setStyleName("Negrita");
		label.setWordWrap(false);
		return label;
	}

	private void setearWidgets() {
		this.htmlTitulo.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		this.vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal.setSpacing(5);
		this.vPanelPrincipal.setWidth("800px");
		this.btnVerHistorico.setWidth("100px");

		listBoxEstaciones.setVisibleItemCount(1);
		listBoxEstaciones.setTitle("Lista de Estaciones");

		hPanelDatos.setSpacing(5);

		tableDatos.setCellSpacing(5);
		tableDatos.setCellPadding(2);
		tableDatos.setBorderWidth(1);
	}

	private void cargarPanelesConWidgets() {
		hPanelDatos.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		vPanel1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);	
		this.vPanelPrincipal.add(htmlTitulo);
		vPanel1.add(hPanelDatos);	
		hPanelDatos.add(this.tableDatos);
		vPanel1.setSpacing(20);
		hPanelContenedor.add(vPanel1);
	}

	private void cargarTituloEstacion() 
	{
		dbDesde.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getMediumDateTimeFormat()));
		dbHasta.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getMediumDateTimeFormat()));		
		formatearFechas(dbDesde.getValue(), dbHasta.getValue());
	}
	
	private void formatearFechas(Date desde, Date hasta) 
	{
		ProyectoBilpa.greetingService.formatearFechas(desde, hasta, new AsyncCallback<DateDesdeHasta>() {
			public void onFailure(Throwable caught) 
			{
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar la hora del servidor");
				vpu.showPopUp();
			}

			public void onSuccess(DateDesdeHasta result) 
			{
				lblEstacion.setText("Estación " + buscarEstacion(idEstacion).getNombre() + " desde " + result.getDesde() + " hasta " + result.getHasta());
				lblEstacion.setStyleName("Titulo");				
			}
		});	
	}

	private EstacionDataList buscarEstacion(int id) 
	{
		for (EstacionDataList e : listaEstaciones)
		{
			if (e.getId() == id)
			{
				return e;
			}
		}
		return null;
	}

	private ArrayList<Orden> obtenerOrdenesEstacion(EstacionDataList e) 
	{
		Date desde = dbDesde.getValue();
		Date hasta = dbHasta.getValue();

		ProyectoBilpa.greetingService.ordenesPorFecha(e, desde, hasta, new AsyncCallback<ArrayList<DatoOrdenesActivasEmpresa>>() {

			public void onFailure(Throwable caught) 
			{
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener las estaciones");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<DatoOrdenesActivasEmpresa> ordenes) 
			{
				vPanelOrdenes.clear();
				if (ordenes.size() > 0)
				{
					for (DatoOrdenesActivasEmpresa o : ordenes)
					{
						IUHistoricoReparacionesData ul = new IUHistoricoReparacionesData(o);
						vPanelOrdenes.add(ul.getPrincipalPanel());
					}
				}
				else
				{
					Label lblNoHayOrdenes = new Label("No existen órdenes para mostrar");
					lblNoHayOrdenes.setStyleName("Negrita");
					vPanelOrdenes.add(lblNoHayOrdenes);
				}
				popUp.hide();
			}
		});	
		return null;
	}

	private void cargarEstaciones()
	{
		listBoxEstaciones.clear();
		listaEstaciones.clear();
		popUp.show();

		ProyectoBilpa.greetingService.obtenerEmpresasDataList(new AsyncCallback<ArrayList<EstacionDataList>>() {

			public void onFailure(Throwable caught) 
			{
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener las estaciones");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<EstacionDataList> result) 
			{
				EstacionDataList auxiliar;
				for (int i=0; i < result.size(); i++){
					auxiliar = (EstacionDataList) result.get(i);
					if(auxiliar.getId() != 1){
						listBoxEstaciones.addItem(auxiliar.toString(),String.valueOf(auxiliar.getId()));
						listaEstaciones.add(auxiliar);						
					}
				}
				listBoxEstaciones.setSelectedIndex(0);
				idEstacion = Integer.valueOf(listBoxEstaciones.getValue(listBoxEstaciones.getSelectedIndex()));	
				//cargarTituloEstacion();
				popUp.hide();
			}
		});		
	}
}
