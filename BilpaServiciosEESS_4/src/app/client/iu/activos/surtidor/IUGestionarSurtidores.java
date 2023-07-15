package app.client.iu.activos.surtidor;

import java.util.ArrayList;
import java.util.List;

import app.client.ProyectoBilpa;
import app.client.dominio.Activo;
import app.client.dominio.Estacion;
import app.client.dominio.ModeloSurtidor;
import app.client.dominio.Persona;
import app.client.dominio.Pico;
import app.client.dominio.Producto;
import app.client.dominio.Surtidor;
import app.client.iu.rightClick.AdvLabel;
import app.client.iu.rightClick.AdvLabelSurtidor;
import app.client.iu.rightClick.AdvSortableTable;
import app.client.iu.rightClick.Entero;
import app.client.iu.rightClick.RightClickEngineSurtidor;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.Constants.OPERACION_GARANTIA;
import app.client.utilidades.utilObjects.GarantiaUtil;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;
import app.client.utilidades.utilObjects.SortableTable;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class IUGestionarSurtidores extends Composite {

	private HTML htmlTitulo = new HTML("Gestión de surtidores");
	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private HorizontalPanel hPanelPrincipal = new HorizontalPanel();
	private HorizontalPanel hPanelContenedor = new HorizontalPanel();
	private VerticalPanel vPanel1 =  new VerticalPanel();
	private List<Estacion> listaEstaciones = new ArrayList<Estacion>();
	private List<ModeloSurtidor> listaModelos = new ArrayList<ModeloSurtidor>();
	private List<Surtidor> listaSurtidores = new ArrayList<Surtidor>();
	private ListBox listBoxSurtidores = new ListBox();
	private ListBox listBoxEstaciones = new ListBox();	
	private Persona sesion;
	private HorizontalPanel hPanelDatos = new HorizontalPanel();
	private TextBox tNroSerie = new TextBox();
	private FlexTable tableDatos = new FlexTable();
	private DecoratorPanel decorator = new DecoratorPanel();
	private AdvSortableTable tableSurtidores = new AdvSortableTable();
	private PopupCargando popUp = new PopupCargando("Cargando...");
	private int idEmpresa;
	Surtidor surtidorSeleccionado;
	GarantiaUtil garantiaUtil;

	private GlassPopup glass = new GlassPopup();
	
	Button bAgregar = new Button("Guardar",new ClickHandler() {
		public void onClick(ClickEvent event) {
			validarTodo();
		}
	});

	public VerticalPanel getPrincipalPanel() {
		return vPanelPrincipal;
	}

	public IUGestionarSurtidores(Persona persona){
		sesion = persona;
		setearWidgets();			
		cargarPanelesConWidgets();
		
		cargarEmpresas();			

		agregarFilaTitulo(tableSurtidores);
		
		cargarGrilla();
		
		color();
		hPanelPrincipal.setSpacing(9);
		hPanelPrincipal.add(decorator);
		vPanelPrincipal.add(hPanelPrincipal);
		vPanelPrincipal.add(tableSurtidores);
		
		listBoxEstaciones.addChangeHandler(new ChangeHandler()
		{
			public void onChange(ChangeEvent arg0) 
			{
				cargarTabla();	
			}
		});
		
		tNroSerie.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER){
					validarTodo();
				}				
			}
		});
	}

	
	private void color() {
		htmlTitulo.setStyleName("Titulo");
		bAgregar.setTitle("Asocia un surtidor a una Estación de Servicio");
	}

	private void cargarGrilla(){
		decorator.setSize("1000px", "120px");
		tableDatos.setWidth("100%");

		tableDatos.getColumnFormatter().setWidth(3, "80px");
		tableDatos.getColumnFormatter().setWidth(4, "80px");
		tableDatos.getColumnFormatter().setWidth(5, "80px");
		
		tableDatos.setCellSpacing(5);
		tableDatos.setWidget(0, 0, label("Estación"));
		tableDatos.setWidget(0, 1, listBoxEstaciones);
		
		tableDatos.setWidget(1, 0, label("N° de Serie"));
		tableDatos.setWidget(1, 1, tNroSerie);

		tableDatos.setWidget(2, 0, label("Modelo"));
		tableDatos.setWidget(2, 1, listBoxSurtidores);
		
		tableDatos.setWidget(2, 6, bAgregar);

		listBoxSurtidores.setWidth("240px");
		tNroSerie.setWidth("240px");
		listBoxEstaciones.setWidth("240px");
		
		decorator.add(tableDatos);
	}

	private Label label(String text) {
		Label label = new InlineLabel(text);
		label.setStyleName("Negrita");
		label.setWordWrap(false);
		return label;
	}

	private void setearWidgets() {
		htmlTitulo.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal.setSpacing(5);
		vPanelPrincipal.setWidth("1000px");
		bAgregar.setWidth("117px");
		decorator.setSize("1000px", "120px");
		listBoxEstaciones.setVisibleItemCount(1);
		listBoxSurtidores.setVisibleItemCount(1);
		listBoxSurtidores.setTitle("Lista de Modelos de Surtidores");
		listBoxEstaciones.setTitle("Lista de Estaciones");
		tableSurtidores.setTitle("Doble click para modificar");

		tNroSerie.setWidth("190px");

		hPanelDatos.setSpacing(5);

		tableSurtidores.setWidth("100%");
	}

	private void cargarPanelesConWidgets() {
		hPanelDatos.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		vPanel1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);	
		vPanelPrincipal.add(htmlTitulo);
		vPanel1.add(hPanelDatos);	
		vPanel1.setSpacing(20);
		hPanelContenedor.add(vPanel1);
	}

	private void cargarEmpresas(){
		listBoxEstaciones.clear();
		listaEstaciones.clear();
		popUp.show();
		ProyectoBilpa.greetingService.obtenerEmpresas(new AsyncCallback<ArrayList<Estacion>>() {

			public void onFailure(Throwable caught) {
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener las estaciones");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<Estacion> result) {
				Estacion auxiliar;
				for (int i=0; i < result.size(); i++){
					auxiliar = (Estacion) result.get(i);
					if(auxiliar.getId() != 1){
						listBoxEstaciones.addItem(auxiliar.toString(),String.valueOf(auxiliar.getId()));
						listaEstaciones.add(auxiliar);						
					}
				}
				listBoxEstaciones.setSelectedIndex(0);
				idEmpresa = Integer.valueOf(listBoxEstaciones.getValue(listBoxEstaciones.getSelectedIndex()));	
				cargarSurtidores();
			}
		});		
	}

	private void cargarSurtidores(){
		listBoxSurtidores.clear();
		listaModelos.clear();
		ProyectoBilpa.greetingService.obtenerTodosLosModelosSurtidores(new AsyncCallback<ArrayList<ModeloSurtidor>>() {

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener las modelos de surtidores");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<ModeloSurtidor> result) {
				ModeloSurtidor auxiliar;
				for (int i=0; i < result.size(); i++){
					auxiliar = (ModeloSurtidor) result.get(i);

					listBoxSurtidores.addItem(auxiliar.toString(),String.valueOf(auxiliar.getId()));
					listaModelos.add(auxiliar);						
				}	
				cargarTabla();
			}
		});		
	}

	private void validarTodo() {
		if (validarCampos()) {
			String numeroSerie = tNroSerie.getText().trim();

			int idModelo = Integer.valueOf(listBoxSurtidores.getValue(listBoxSurtidores.getSelectedIndex()));
			int idEstacion = Integer.valueOf(listBoxEstaciones.getValue(listBoxEstaciones.getSelectedIndex()));
			Surtidor s = new Surtidor();
			final Estacion estacion = buscarEstacion (idEstacion);
			final ModeloSurtidor modeloSurtidor = buscarModeloSurtidor(idModelo);

			for (int i = 0; i < modeloSurtidor.getCantidadDePicos(); i++) {
				Pico pico = new Pico();
				pico.setProducto(new Producto(1));
				pico.setSurtidor(s);
				pico.setNumeroPico(i+1);
				s.agregarPico(pico);
			}
			s.setEmpresa(estacion);
			s.setNumeroSerie(numeroSerie);
			s.setModelo(modeloSurtidor);
			
			s.setAnioFabricacion(garantiaUtil.getAnioFabricacion());
			
			if(garantiaUtil.validarYSetearGarantia(s)){
				validarSurtidor(s);
			}
		}
	}

	private void validarSurtidor(final Surtidor s){
		if(s != null){
			ProyectoBilpa.greetingService.validarSurtidorExiste(s, new AsyncCallback<Boolean>() {

				public void onFailure(Throwable caught) {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar el surtidor");
					vpu.showPopUp();
				}
				public void onSuccess(Boolean result) {
					if(!result){
						guardarSurtidor(s);
						limpiarCampos();
					}else{
						ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Ya existe un surtidor con ese numero de serie");
						vpu.showPopUp();
					}
				}
			});		
		}
	}

	private void guardarSurtidor(Surtidor s){
		if(s != null){
			ProyectoBilpa.greetingService.guardarSurtidor(s, new AsyncCallback<Boolean>() {

				public void onFailure(Throwable caught) {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar el surtidor");
					vpu.showPopUp();
				}
				public void onSuccess(Boolean result) {
					cargarTabla();
				}
			});		
		}
	}

	private boolean validarCampos(){
		if(tNroSerie.getText().equalsIgnoreCase("")){
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe ingresar un numero de serie");
			vpu.showPopUp();
			return false;
		}

		if(tNroSerie.getText().trim().equalsIgnoreCase("")){
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe ingresar un numero de serie");
			vpu.showPopUp();
			return false;
		}

		return true;
	}

	public void cargarTabla(){
		popUp.show();
		tableSurtidores.clear();
		idEmpresa = Integer.valueOf(listBoxEstaciones.getValue(listBoxEstaciones.getSelectedIndex()));
		ProyectoBilpa.greetingService.obtenerTodosLosActivosTipoPorEstacion(1, idEmpresa, new AsyncCallback<ArrayList<Activo>>(){

			public void onFailure(Throwable caught) {
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al cargar los modelos de surtidores");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<Activo> result) {
				tableDatos.getColumnFormatter().setWidth(3, "80px");
				tableDatos.getColumnFormatter().setWidth(4, "80px");
				tableDatos.getColumnFormatter().setWidth(5, "80px");
				garantiaUtil = new GarantiaUtil(null, tableDatos, 0, 2, OPERACION_GARANTIA.AGREGAR);
				dibujarTabla(result);
				popUp.hide();
			}					
		});
	}

	private void agregarFilaTitulo(SortableTable table) {		
		table.getRowFormatter().addStyleName(0, "CabezalTabla");

		table.addColumnHeader("Número de Serie", 0);
		table.addColumnHeader("Marca", 1);
		table.addColumnHeader("Modelo", 2);
		table.addColumnHeader("Cantidad de Picos", 3);
		table.addColumnHeader("Estación", 4);
		table.addColumnHeader("Estado", 5);
		table.addColumnHeader("Inicio Garantía", 6);
		table.addColumnHeader("Fin Garantía", 7);
		table.addColumnHeader("Año Fabricación", 8);
	}

	private Estacion buscarEstacion(int id) {
		for (Estacion e : listaEstaciones)
		{
			if (e.getId() == id)
			{
				return e;
			}
		}
		return null;
	}

	private ModeloSurtidor buscarModeloSurtidor(int id) {
		for (ModeloSurtidor e : listaModelos)
		{
			if (e.getId() == id)
			{
				return e;
			}
		}
		return null;
	}
	
	private void limpiarCampos(){
		tNroSerie.setText("");
		listBoxSurtidores.setItemSelected(0, true);
		//listBoxEstaciones.setItemSelected(0, true);
		tNroSerie.setFocus(true);
	}

	private void dibujarTabla(ArrayList<Activo> activo) {
		Surtidor surtidor;
		tableSurtidores.removeAllRows();
		agregarFilaTitulo(tableSurtidores);
		for (int i=0; i < activo.size(); i++)
		{
			if (idEmpresa == Integer.valueOf(listBoxEstaciones.getValue(listBoxEstaciones.getSelectedIndex())))
			{
				int fila = i+1;
				surtidor = (Surtidor) activo.get(i);

				if(i %2==0){
					tableSurtidores.getRowFormatter().addStyleName(i+1, "FilaTabla1");				
				}else{
					tableSurtidores.getRowFormatter().addStyleName(i+1, "FilaTabla2");
				}

				String fechaInicio = surtidor.getInicioGarantia() != null ? (garantiaUtil.getFechaFormateada(surtidor.getInicioGarantia())) : "-";
				String fechaFin = surtidor.getFinGarantia() != null ? (garantiaUtil.getFechaFormateada(surtidor.getFinGarantia())) : "-";
				
				RightClickEngineSurtidor rceID = new RightClickEngineSurtidor();
				RightClickEngineSurtidor rceMarca = new RightClickEngineSurtidor();
				RightClickEngineSurtidor rceNombre = new RightClickEngineSurtidor();
				RightClickEngineSurtidor rceCantidadPicos = new RightClickEngineSurtidor();
				RightClickEngineSurtidor rceEstacion = new RightClickEngineSurtidor();
				RightClickEngineSurtidor rceEstado = new RightClickEngineSurtidor();
				RightClickEngineSurtidor rceInicioGarantia = new RightClickEngineSurtidor();
				RightClickEngineSurtidor rceFinGarantia = new RightClickEngineSurtidor();
				RightClickEngineSurtidor rceAnioFabricacion = new RightClickEngineSurtidor();

				AdvLabel labelSerie = rceID.configureAdvLabel(new AdvLabelSurtidor(new Entero (fila, tableSurtidores.getCellCount(fila)), surtidor.getNumeroSerie(), surtidor, this));
				AdvLabel labelMarca = rceMarca.configureAdvLabel(new AdvLabelSurtidor(new Entero (fila, tableSurtidores.getCellCount(fila)), surtidor.getModelo().getMarca().toString(), surtidor, this));
				AdvLabel labelModelo = rceNombre.configureAdvLabel(new AdvLabelSurtidor(new Entero (fila, tableSurtidores.getCellCount(fila)), surtidor.getModelo().getNombre(), surtidor, this));
				AdvLabel labelPicos = rceCantidadPicos.configureAdvLabel(new AdvLabelSurtidor(new Entero (fila, tableSurtidores.getCellCount(fila)), surtidor.obtenerCantidadDePicos()+"", surtidor, this));
				AdvLabel labelEmpresa = rceEstacion.configureAdvLabel(new AdvLabelSurtidor(new Entero (fila, tableSurtidores.getCellCount(fila)), surtidor.getEmpresa().getNombre(), surtidor, this));
				AdvLabel labelEstado = rceEstado.configureAdvLabel(new AdvLabelSurtidor(new Entero (fila, tableSurtidores.getCellCount(fila)), " - ", surtidor, this));
				if(surtidor.getEstado() != null) {
					labelEstado = rceEstado.configureAdvLabel(new AdvLabelSurtidor(new Entero (fila, tableSurtidores.getCellCount(fila)), surtidor.getEstado().getEstado(), surtidor, this));
				}
				AdvLabel labelInicio = rceInicioGarantia.configureAdvLabel(new AdvLabelSurtidor(new Entero (fila, tableSurtidores.getCellCount(fila)), fechaInicio, surtidor, this));
				AdvLabel labelFin = rceFinGarantia.configureAdvLabel(new AdvLabelSurtidor(new Entero (fila, tableSurtidores.getCellCount(fila)),  fechaFin, surtidor, this));
				AdvLabel labelAnioFabricacion = rceAnioFabricacion.configureAdvLabel(new AdvLabelSurtidor(new Entero (fila, tableSurtidores.getCellCount(fila)),  surtidor.getAnioFabricacion()+"", surtidor, this));
				
				tableSurtidores.setWidget(fila, 0, labelSerie);
				tableSurtidores.setWidget(fila, 1, labelMarca);
				tableSurtidores.setWidget(fila, 2, labelModelo);
				tableSurtidores.setWidget(fila, 3, labelPicos);
				tableSurtidores.setWidget(fila, 4, labelEmpresa);
				tableSurtidores.setWidget(fila, 5, labelEstado);
				tableSurtidores.setWidget(fila, 6, labelInicio);
				tableSurtidores.setWidget(fila, 7, labelFin);
				tableSurtidores.setWidget(fila, 8, labelAnioFabricacion);
			}
		}
	}	
}
