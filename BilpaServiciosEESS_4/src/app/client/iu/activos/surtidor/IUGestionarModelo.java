package app.client.iu.activos.surtidor;

import java.util.ArrayList;

import app.client.ProyectoBilpa;
import app.client.dominio.Marca;
import app.client.dominio.ModeloSurtidor;
import app.client.dominio.Persona;
import app.client.iu.rightClick.AdvLabel;
import app.client.iu.rightClick.AdvLabelModeloSurtidor;
import app.client.iu.rightClick.AdvSortableTable;
import app.client.iu.rightClick.Entero;
import app.client.iu.rightClick.RightClickEngineModeloSurtidor;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.UtilCss;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;
import app.client.utilidades.utilObjects.SortableTable;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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

public class IUGestionarModelo extends Composite{

	private HTML htmlTitulo = new HTML("Gestión de modelos de surtidores");

	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private HorizontalPanel hPanelPrincipal = new HorizontalPanel();
	private HorizontalPanel hPanelContenedor = new HorizontalPanel();
	private VerticalPanel vPanel1 =  new VerticalPanel();
	private ListBox listBoxListaMarcas = new ListBox();
	private Persona sesion;
	private ListBox listBoxListaCantidadPicos = new ListBox();
	private ListBox listBoxListaCantidadProductos = new ListBox();
	private HorizontalPanel hPanelDatos = new HorizontalPanel();

	private Label lblMarca = new Label("Marca");
	private Label lblCantidadPicos = new Label("Cantidad de Picos");
	private Label lblCantidadProductos = new Label("Cantidad de Productos");
	private Label lblNombreModelo = new Label("Nombre del Modelo");

	TextBox tNombreModelo = new TextBox();

	private FlexTable tableDatosmodelo = new FlexTable();

	AdvSortableTable tableModelos = new AdvSortableTable();

	private DecoratorPanel decorator = new DecoratorPanel();

	private ArrayList<ModeloSurtidor> modelosSurtidores = new ArrayList<ModeloSurtidor>();

	private PopupCargando popUp = new PopupCargando("Cargando...");
	
	private GlassPopup glass = new GlassPopup();
	
	Button btnGuardar = new Button("Guardar",new ClickHandler() {
		public void onClick(ClickEvent event) {
			validarTodo();
		}
	});


	public VerticalPanel getPrincipalPanel() {
		return vPanelPrincipal;
	}

	public IUGestionarModelo(Persona persona){
		this.sesion = persona;
		setearWidgets();			//Setea el tamano de los Widgets.
		cargarPanelesConWidgets();	//Agrega los Widget a los paneles.
		cargarLtBoxMarcas();	
		cargarLtBoxPicos();
		cargarListBoxProductos();
		agregarFilaTitulo(tableModelos);
		cargarTabla();
		color();
		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal.setSpacing(5);
		vPanelPrincipal.add(hPanelPrincipal);
		vPanelPrincipal.add(decorator);
		vPanelPrincipal.setSpacing(10);
		vPanelPrincipal.add(tableModelos);

		tableModelos.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				
			}
		});
	}

	private void color() {
		htmlTitulo.setStyleName("Titulo");
		lblMarca.setStyleName("Negrita");
		lblNombreModelo.setStyleName("Negrita");
		lblCantidadPicos.setStyleName("Negrita");		
		lblCantidadProductos.setStyleName("Negrita");
	}

	private void setearWidgets() {
		decorator.setSize("500px", "120px");	
		tableModelos.setWidth("500px");
		vPanelPrincipal.setSpacing(5);

		this.htmlTitulo.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		this.vPanelPrincipal.setWidth("800px");
		this.btnGuardar.setWidth("100px");

		listBoxListaMarcas.setTitle("Lista de Marcas");	
		listBoxListaCantidadPicos.setTitle("Lista de Picos");	
		listBoxListaCantidadProductos.setTitle("Lista de cantidad de Productos");
		tNombreModelo.setTitle("Ingrese el Nombre del Modelo");		
		tableModelos.setTitle("Doble Click para Modificar");
		
		listBoxListaMarcas.setVisibleItemCount(1);
		listBoxListaMarcas.setWidth("100%");
		listBoxListaCantidadPicos.setWidth("100%");
		listBoxListaCantidadProductos.setWidth("100%");
		tNombreModelo.setWidth("190px");

		hPanelDatos.setSpacing(5);

		tableDatosmodelo.setCellSpacing(5);
		tableDatosmodelo.setCellPadding(2);			

		tableDatosmodelo.setWidget(0, 1, lblMarca);
		tableDatosmodelo.setWidget(1, 1, lblNombreModelo);
		tableDatosmodelo.setWidget(2, 1, lblCantidadPicos);
		tableDatosmodelo.setWidget(3, 1, lblCantidadProductos);

		tableDatosmodelo.setWidget(0, 2, listBoxListaMarcas);
		tableDatosmodelo.setWidget(1, 2, tNombreModelo);
		tableDatosmodelo.setWidget(2, 2, listBoxListaCantidadPicos);
		tableDatosmodelo.setWidget(3, 2, listBoxListaCantidadProductos);
		tableDatosmodelo.setWidget(3, 4, btnGuardar);
		decorator.add(tableDatosmodelo);		
	}

	private void cargarPanelesConWidgets() {
		hPanelDatos.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		vPanel1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);	

		vPanelPrincipal.add(htmlTitulo);
		vPanel1.add(hPanelDatos);	

		hPanelDatos.add(decorator);

		hPanelContenedor.add(vPanel1);
		vPanelPrincipal.add(hPanelContenedor);
	}

	private void cargarLtBoxMarcas(){
		listBoxListaMarcas.clear();
		ProyectoBilpa.greetingService.obtenerTodasLasMArcas(new AsyncCallback<ArrayList<Marca>>() {

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener las marcas");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<Marca> result) {
				Marca auxiliar;
				for (int i=0; i < result.size(); i++){
					auxiliar = (Marca) result.get(i);
					listBoxListaMarcas.addItem(auxiliar.toString(),String.valueOf(auxiliar.getId()));
				}	
			}
		});		
	}

	private void cargarLtBoxPicos(){
		for (int i = 1; i < 9; i++) {
			listBoxListaCantidadPicos.addItem(i+"",i+"");
		}
	}
	
	private void cargarListBoxProductos(){
		for(int i= 0; i<5; i++){
			listBoxListaCantidadProductos.addItem(i+"",i+"");
		}
	}

	private void guardarModelo(){
		int picos = Integer.valueOf(this.listBoxListaCantidadPicos.getValue(this.listBoxListaCantidadPicos.getSelectedIndex()));
		int cantidadProductos = Integer.valueOf(this.listBoxListaCantidadProductos.getValue(this.listBoxListaCantidadProductos.getSelectedIndex()));
		int id = Integer.valueOf(this.listBoxListaMarcas.getValue(this.listBoxListaMarcas.getSelectedIndex()));

		final ModeloSurtidor modelo = new ModeloSurtidor();
		modelo.setCantidadDePicos(picos);
		modelo.setCantidadDeProductos(cantidadProductos);
		modelo.setNombre(tNombreModelo.getText());

		ProyectoBilpa.greetingService.buscarMarca(id, new AsyncCallback<Marca>() {
			public void onSuccess(Marca result) {
				modelo.setMarca(result);
				modelo.setTipo(1);
				guardarModeloABase(modelo);		
			}

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al modificar la marca");
				vpu.showPopUp();
			}
		});		
	}

	private void guardarModeloABase(final ModeloSurtidor modelo) {
		ProyectoBilpa.greetingService.agregarModelo(modelo, new AsyncCallback<Boolean>(){
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar el modelo");
				vpu.showPopUp();
			}

			public void onSuccess(Boolean result) {
				tableModelos.clear();
				tNombreModelo.setText("");
				cargarTabla();
			}					
		});
	}

	private void validarTodo() {
		if (this.validarCampos()) {
			String nombreModelo = this.tNombreModelo.getText();

			int idMarca = Integer.valueOf(this.listBoxListaMarcas.getValue(this.listBoxListaMarcas.getSelectedIndex()));
			//this.validarModeloDisponible(nombreModelo,idMarca);
			guardarModelo();
		}
	}

	/*private void validarModeloDisponible(final String nombreModelo, int idMarca){
		ProyectoBilpa.greetingService.validarModeloDisponible(nombreModelo, idMarca, -1,new AsyncCallback<Boolean>() {

			public void onSuccess(Boolean result) {
				if (result) {
					guardarModelo();
				} else {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Ya existe un modelo con el nombre: " + nombreModelo + " para la marca seleccionada." );
					vpu.showPopUp();
				}
			}

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar el modelo");
				vpu.showPopUp();
			}
		});
	}*/

	private boolean validarCampos(){
		if(this.tNombreModelo.getText().equalsIgnoreCase("")){
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe ingresar un nombre");
			vpu.showPopUp();
			return false;
		}

		if(this.tNombreModelo.getText().trim().equalsIgnoreCase("")){
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe ingresar un nombre");
			vpu.showPopUp();
			return false;
		}

		return true;
	}

	void cargarTabla(){
		popUp.show();
		ProyectoBilpa.greetingService.obtenerTodosLosModelosSurtidores(new AsyncCallback<ArrayList<ModeloSurtidor>>(){

			public void onFailure(Throwable caught) {
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al cargar los modelos de surtidores");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<ModeloSurtidor> result) {
				modelosSurtidores = result;
				dibujarTabla();	
				popUp.hide();
			}		
		});
	}

	public void dibujarTabla() {
		ModeloSurtidor modeloSurtidor;				

		for (int i=0; i < modelosSurtidores.size(); i++){
			int fila = i+1;
			modeloSurtidor = (ModeloSurtidor) modelosSurtidores.get(i);

			UtilCss.aplicarEstiloATabla(tableModelos, fila+1);

			RightClickEngineModeloSurtidor rceID = new RightClickEngineModeloSurtidor();
			RightClickEngineModeloSurtidor rceMarca = new RightClickEngineModeloSurtidor();
			RightClickEngineModeloSurtidor rceNombre = new RightClickEngineModeloSurtidor();
			RightClickEngineModeloSurtidor rceCantidadPicos = new RightClickEngineModeloSurtidor();
			RightClickEngineModeloSurtidor rceCantidadProductos = new RightClickEngineModeloSurtidor();

			AdvLabel labelMarca = rceMarca.configureAdvLabel(new AdvLabelModeloSurtidor(new Entero (fila, tableModelos.getCellCount(fila)), modeloSurtidor.getMarca().getNombre(), modeloSurtidor, this));
			AdvLabel labelNombre = rceNombre.configureAdvLabel(new AdvLabelModeloSurtidor(new Entero (fila, tableModelos.getCellCount(fila)), modeloSurtidor.getNombre(), modeloSurtidor, this));
			AdvLabel labelPicos = rceCantidadPicos.configureAdvLabel(new AdvLabelModeloSurtidor(new Entero (fila, tableModelos.getCellCount(fila)), modeloSurtidor.getCantidadDePicos()+"", modeloSurtidor, this));
			AdvLabel labelProductos = rceCantidadProductos.configureAdvLabel(new AdvLabelModeloSurtidor(new Entero (fila, tableModelos.getCellCount(fila)), modeloSurtidor.getCantidadDeProductos()+"", modeloSurtidor, this));

/*			labelMarca.setWidth("100px");
			labelNombre.setWidth("100px");
			labelPicos.setWidth("100px");*/
			
/*			tableModelos.setValue(fila, 0, labelId);
			tableModelos.setValue(fila, 1, labelMarca);
			tableModelos.setValue(fila, 2, labelNombre);
			tableModelos.setValue(fila, 3, labelPicos);*/

//			tableModelos.setWidget(fila, 0, labelId);
			tableModelos.setWidget(fila, 0, labelMarca);
			tableModelos.setWidget(fila, 1, labelNombre);
			tableModelos.setWidget(fila, 2, labelPicos);
			tableModelos.setWidget(fila, 3, labelProductos);
			
		}
	}	

	public ModeloSurtidor getModeloSurtidor(int id)
	{
		for (ModeloSurtidor ms : modelosSurtidores)
		{
			if (ms.getId() == id)
			{
				return ms;
			}
		}
		return null;
	}

	private void agregarFilaTitulo(SortableTable table) {		
		
		table.addColumnHeader("Marca", 0);
		table.addColumnHeader("Modelo", 1);
		table.addColumnHeader("Cantidad de Picos", 2);
		table.addColumnHeader("Cantidad de Productos", 3);
		table.getRowFormatter().addStyleName(0, "CabezalTabla");
	}
}
