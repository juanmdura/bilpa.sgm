package app.client.iu.activos.tanque;

import java.util.ArrayList;
import java.util.List;

import app.client.ProyectoBilpa;
import app.client.dominio.Activo;
import app.client.dominio.Estacion;
import app.client.dominio.Persona;
import app.client.dominio.Producto;
import app.client.dominio.Tanque;
import app.client.iu.rightClick.AdvLabel;
import app.client.iu.rightClick.AdvLabelSurtidor;
import app.client.iu.rightClick.AdvLabelTanque;
import app.client.iu.rightClick.Entero;
import app.client.iu.rightClick.RightClickEngineSurtidor;
import app.client.iu.rightClick.RightClickEngineTanque;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.Constants.OPERACION_GARANTIA;
import app.client.utilidades.UtilCss;
import app.client.utilidades.UtilUI;
import app.client.utilidades.utilObjects.GarantiaUtil;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;

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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class IUGestionarTanque extends Composite{

	private HTML htmlTitulo = new HTML("Gestión de tanques");
	private PopupCargando popUp = new PopupCargando("Cargando...");
	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private HorizontalPanel hPanelPrincipal = new HorizontalPanel();
	private HorizontalPanel hPanelContenedor = new HorizontalPanel();
	private VerticalPanel vPanel1 =  new VerticalPanel();
	private ListBox listBoxListaEstaciones = new ListBox();
	private ListBox listBoxCombusitbles = new ListBox();
	private List<Producto> listaCombustibles = new ArrayList<Producto>();

	private List<Estacion> listaEstaciones = new ArrayList<Estacion>();

	private Persona sesion;
	private HorizontalPanel hPanelDatos = new HorizontalPanel();

	private Label lblEstacion = new Label ("Estación");
	private Label lblCombustible = new Label ("Tipo de combustible");
	private Label lblCapacidad = new Label ("Capacidad");
	private Label lblDescripcion = new Label ("Descripción");

	private TextBox tCapacidad = new TextBox();
	private TextBox tDescripcion = new TextBox();

	private FlexTable tableDatos = new FlexTable();
	DecoratorPanel decorator = new DecoratorPanel();
	private FlexTable tableTanques = new FlexTable();

	private ArrayList<Activo> tanques = new ArrayList<Activo>();

	GarantiaUtil garantiaUtil;

	private GlassPopup glass = new GlassPopup();

	Button bGuardar = new Button("Guardar",new ClickHandler() {
		public void onClick(ClickEvent event) {
			validarTodo();
		}
	});

	private void mostrarTanques() {
		cargarTablaTanques();
	}

	public VerticalPanel getPrincipalPanel() {
		return vPanelPrincipal;
	}

	public IUGestionarTanque(Persona persona)
	{
		this.sesion = persona;
		setearWidgets();			//Setea el tamano de los Widgets.

		cargarPanelesConWidgets();	//Agrega los Widget a los paneles.
		cargarLtBoxEstaciones();	
		cargarLtBoxCombustibles();

		color();
		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		this.vPanelPrincipal.add(hPanelPrincipal);
		this.vPanelPrincipal.add(tableTanques);

		crearTablaTanques();

		listBoxListaEstaciones.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				cargarTablaTanques();	
			}
		});

		tDescripcion.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER){
					validarTodo();
				}				
			}
		});

		tCapacidad.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER){
					validarTodo();
				}				
			}
		});
	}

	private void color() {
		htmlTitulo.setStyleName("Titulo");

	}

	public void crearTablaTanques()
	{
		tableTanques.removeAllRows();
		tableTanques.removeStyleName("CabezalTabla");
		tableTanques.setTitle("Doble click para modificar");
		tableTanques.setCellPadding(2);
		tableTanques.setText(0, 0, "Descripción");
		tableTanques.setText(0, 1, "Tipo de Combustible");
		tableTanques.setText(0, 2, "Capacidad Ltrs");
		tableTanques.setText(0, 3, "Estado");
		tableTanques.setText(0, 4, "Inicio Garantía");
		tableTanques.setText(0, 5, "Fin Garantía");
		tableTanques.setText(0, 6, "Año Fabricación");

		tableTanques.getRowFormatter().addStyleName(0, "CabezalTabla");
	}

	private void setearWidgets() {
		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		htmlTitulo.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal.setWidth("800px");
		bGuardar.setWidth("100px");

		decorator.setSize("730px", "120px");	
		//tableTanques.setWidth("500px");

		listBoxListaEstaciones.setVisibleItemCount(1);
		listBoxListaEstaciones.setTitle("Lista de Estaciones");
		listBoxCombusitbles.setTitle("Lista de Combustibles");
		tCapacidad.setTitle("Ingrese la capacidad del Tanque en Litros");
		tDescripcion.setTitle("Ingrese la descripción del tanque");

		tCapacidad.setWidth("190px");
		tDescripcion.setWidth("190px");
		listBoxCombusitbles.setWidth("100%");
		listBoxListaEstaciones.setWidth("100%");
		hPanelDatos.setSpacing(5);

		tableDatos.setCellSpacing(5);
		tableDatos.setCellPadding(2);

		lblEstacion.setStyleName("Negrita");
		lblCombustible.setStyleName("Negrita");
		lblCapacidad.setStyleName("Negrita");
		lblDescripcion.setStyleName("Negrita");

		tableDatos.setWidget(0, 0, lblEstacion);
		tableDatos.setWidget(1, 0, lblCombustible);
		tableDatos.setWidget(2, 0, lblCapacidad);
		tableDatos.setWidget(3, 0, lblDescripcion);

		tableDatos.setWidget(0, 1, listBoxListaEstaciones);
		tableDatos.setWidget(1, 1, listBoxCombusitbles);
		tableDatos.setWidget(2, 1, tCapacidad);
		tableDatos.setWidget(3, 1, tDescripcion);
		tableDatos.setWidget(3, 4, bGuardar);

		decorator.add(tableDatos);
	}

	private void cargarPanelesConWidgets() {
		hPanelDatos.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hPanelDatos.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hPanelContenedor.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanel1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanel1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);	
		vPanelPrincipal.add(htmlTitulo);
		vPanel1.add(hPanelDatos);	
		hPanelDatos.add(decorator);

		vPanel1.setSpacing(20);
		hPanelContenedor.add(vPanel1);
		vPanelPrincipal.add(hPanelContenedor);
	}

	private void cargarLtBoxEstaciones(){
		listBoxListaEstaciones.clear();
		listaEstaciones.clear();
		popUp.show();
		ProyectoBilpa.greetingService.obtenerEmpresas(new AsyncCallback<ArrayList<Estacion>>() {

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener las Estaciones");
				vpu.showPopUp();
				popUp.hide();
			}

			public void onSuccess(ArrayList<Estacion> result) {
				Estacion auxiliar;
				for (int i=0; i < result.size(); i++){
					auxiliar = (Estacion) result.get(i);
					if(auxiliar.getId() != 1){
						listBoxListaEstaciones.addItem(auxiliar.toString(),String.valueOf(auxiliar.getId()));
						listaEstaciones.add(auxiliar);						
					}
				}
				cargarTablaTanques();
				popUp.hide();
			}
		});		
	}

	private void cargarLtBoxCombustibles(){
		listBoxCombusitbles.clear();

		ProyectoBilpa.greetingService.obtenerTiposCombustibles(new AsyncCallback<List<Producto>>() {

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener las Estaciones");
				vpu.showPopUp();
			}
			public void onSuccess(List<Producto> result) {
				listaCombustibles.clear();
				for (int i=0; i < result.size(); i++){
					listBoxCombusitbles.addItem(result.get(i).getNombre(), result.get(i).getId()+"");
					listaCombustibles.add(result.get(i));
				}	
				listBoxCombusitbles.setSelectedIndex(0);
			}
		});		
	}


	private void validarTodo() {
		if (this.validarCampos()) {
			crearTanque();
		}
	}

	private void crearTanque(){
		if (listBoxCombusitbles.getSelectedIndex() > 0){

			Tanque tanque = new Tanque();
			String descripcion = tDescripcion.getText();
			int capacidad = Integer.valueOf(this.tCapacidad.getText());
			int idEstacion = Integer.valueOf(this.listBoxListaEstaciones.getValue(this.listBoxListaEstaciones.getSelectedIndex()));
			final Estacion estacion = buscarEstacion (idEstacion);
			tanque.setCapacidad(capacidad);
			tanque.setProducto(buscarProducto(Integer.valueOf(listBoxCombusitbles.getValue(listBoxCombusitbles.getSelectedIndex()))));
			tanque.setEmpresa(estacion);
			tanque.setDescripcion(descripcion);
			tanque.setAnioFabricacion(garantiaUtil.getAnioFabricacion());
			
			if(garantiaUtil.validarYSetearGarantia(tanque)){
				guardarTanque(tanque, estacion);
			}
		} else {
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe seleccionar un combustible");
			vpu.showPopUp();
		}
	}

	private Producto buscarProducto(Integer id) {
		for (Producto p : listaCombustibles){
			if (p.getId() == id){
				return p;
			}
		}
		return null;
	}
	
	private void guardarTanque(Tanque tanque, final Estacion estacion){	
		ProyectoBilpa.greetingService.guardarTanque(tanque, new AsyncCallback<Boolean>() {

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar el tanque");
				vpu.showPopUp();
			}
			public void onSuccess(Boolean result) {
				cargarTablaTanques();
			}
		});		
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

	private boolean validarCampos(){
		if(this.tCapacidad.getText().equalsIgnoreCase("")){
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe ingresar la capacidad del tanque");
			vpu.showPopUp();
			return false;
		}
		if(this.tCapacidad.getText().trim().equalsIgnoreCase("")){
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe ingresar la capacidad del tanque");
			vpu.showPopUp();
			return false;
		}
		if(this.tDescripcion.getText().equalsIgnoreCase("")){
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe ingresar la descripción del tanque");
			vpu.showPopUp();
			return false;
		}
		if(this.tDescripcion.getText().trim().equalsIgnoreCase("")){
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe ingresar la descripción del tanque");
			vpu.showPopUp();
			return false;
		}
		try {
			int capacidad = Integer.valueOf(this.tCapacidad.getText());
			if(capacidad <= 0){
				ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "La capacidad del tanque debe ser mayor a cero");
				vpu.showPopUp();
				return false;
			}
		} catch (Exception e) {
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "La capacidad del tanque debe estar expresada con números y en litros");
			vpu.showPopUp();
			return false;
		} 
		return true;
	}

	public void cargarTablaTanques() {
		int tipoActivo = 2;
		int idEstacion = Integer.valueOf(this.listBoxListaEstaciones.getValue(this.listBoxListaEstaciones.getSelectedIndex()));
		final Estacion estacion = buscarEstacion (idEstacion);

		ProyectoBilpa.greetingService.obtenerActivosPorTipo(estacion,tipoActivo, new AsyncCallback<ArrayList<Activo>>() {

			public void onSuccess(ArrayList<Activo> result) {
				tanques = result;
				garantiaUtil = new GarantiaUtil(null, tableDatos, 0, 2, OPERACION_GARANTIA.AGREGAR);
				dibujarTabla();
			}

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener activos por tipo");
				vpu.showPopUp();
			}
		});
	}

	public void dibujarTabla() 
	{
		UtilUI.limpiarTabla(tableTanques);
		tableTanques.clear();
		tCapacidad.setText("");
		tDescripcion.setText("");

		for (int i = 0; i < tanques.size(); i++) 
		{
			Tanque tanque = (Tanque) tanques.get(i);

			String fechaInicio = tanque.getInicioGarantia() != null ? (garantiaUtil.getFechaFormateada(tanque.getInicioGarantia())) : "-";
			String fechaFin = tanque.getFinGarantia() != null ? (garantiaUtil.getFechaFormateada(tanque.getFinGarantia())) : "-";

			RightClickEngineTanque rceDesc = new RightClickEngineTanque();
			RightClickEngineTanque rceComb = new RightClickEngineTanque();
			RightClickEngineTanque rceCap = new RightClickEngineTanque();
			RightClickEngineTanque rceEstado = new RightClickEngineTanque();
			RightClickEngineTanque rceInicioGarantia = new RightClickEngineTanque();
			RightClickEngineTanque rceFinGarantia = new RightClickEngineTanque();
			RightClickEngineTanque rceAnioFabricacion = new RightClickEngineTanque();
			
			AdvLabel labelDesc = rceDesc.configureAdvLabel(new AdvLabelTanque(new Entero (i, tableTanques.getCellCount(i)), tanque.getDescripcion(), tanque, this));
			AdvLabel labelCom = rceComb.configureAdvLabel(new AdvLabelTanque(new Entero (i, tableTanques.getCellCount(i)), tanque.getProducto().getNombre(), tanque, this));
			AdvLabel labelCap = rceCap.configureAdvLabel(new AdvLabelTanque(new Entero (i, tableTanques.getCellCount(i)), tanque.getCapacidad() + "", tanque, this));
			AdvLabel labelEstado = rceEstado.configureAdvLabel(new AdvLabelTanque(new Entero (i, tableTanques.getCellCount(i)), " - ", tanque, this));
			if(tanque.getEstado() != null) {
				labelEstado = rceEstado.configureAdvLabel(new AdvLabelTanque(new Entero (i, tableTanques.getCellCount(i)), tanque.getEstado().getEstado(), tanque, this));
			}
			AdvLabel labelInicio = rceInicioGarantia.configureAdvLabel(new AdvLabelTanque(new Entero (i, tableTanques.getCellCount(i)), fechaInicio, tanque, this));
			AdvLabel labelFin = rceFinGarantia.configureAdvLabel(new AdvLabelTanque(new Entero (i, tableTanques.getCellCount(i)),  fechaFin, tanque, this));
			AdvLabel labelAnioFabricacion = rceAnioFabricacion.configureAdvLabel(new AdvLabelTanque(new Entero (i, tableTanques.getCellCount(i)),  tanque.getAnioFabricacion()+"", tanque, this));
			
			tableTanques.setWidget(i+1, 0, labelDesc);
			tableTanques.setWidget(i+1, 1, labelCom);
			tableTanques.setWidget(i+1, 2, labelCap);
			tableTanques.setWidget(i+1, 3, labelEstado);
			tableTanques.setWidget(i+1, 4, labelInicio);
			tableTanques.setWidget(i+1, 5, labelFin);
			tableTanques.setWidget(i+1, 6, labelAnioFabricacion);

			UtilCss.aplicarEstiloATabla(tableTanques, i+2);
		}

		setearEstacionEnListBox();
	}

	private void setearEstacionEnListBox() {
		if(tanques.size() > 0)
		{
			for (int i=0; i < listBoxListaEstaciones.getItemCount(); i++)
			{
				if (Integer.valueOf(listBoxListaEstaciones.getValue(i)) == tanques.get(0).getEmpresa().getId())
				{
					listBoxListaEstaciones.setSelectedIndex(i);						
				}
			}
		}
	}

}
