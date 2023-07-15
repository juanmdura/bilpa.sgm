package app.client.iu.activos.surtidor;

import java.util.ArrayList;

import app.client.ProyectoBilpa;
import app.client.dominio.Marca;
import app.client.dominio.ModeloSurtidor;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.utilObjects.GlassPopup;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class IUModificarModelo {

	private IUGestionarModelo iu;
	private ModeloSurtidor modeloSurtidor;

	private Label lblTitulo = new Label(); 
	private DialogBox dialogBoxModif = new DialogBox();

	private HorizontalPanel hPanelMarca = new HorizontalPanel();
	private HorizontalPanel hPanelNombreModelo = new HorizontalPanel();
	private HorizontalPanel hPanelPicos = new HorizontalPanel();
	private HorizontalPanel hPanelCantidadProductos = new HorizontalPanel();
	
	private HorizontalPanel hPanelDialModif = new HorizontalPanel();
	private HorizontalPanel hPanelDialModif2 = new HorizontalPanel();
	private VerticalPanel vPanelDailModif = new VerticalPanel();

	private TextBox txtNombreModelo = new TextBox();
	private ListBox listBoxListaMarcas = new ListBox();
	private ListBox listBoxListaCantidadPicos = new ListBox();
	private ListBox listBoxListaCantidadProductos = new ListBox();

	private Label lblMarca = new Label("Seleccione una Marca");
	private Label lblCantidadPicos = new Label("Cantidad de Picos");
	private Label lblNombreModelo = new Label("Nombre del Modelo");
	private Label lblCantidadProductos = new Label("Cantidad de Productos");
	
	private ArrayList<Marca> marcas = new ArrayList<Marca>();

	private GlassPopup glass = new GlassPopup();

	private Button btnAceptarModif = new Button("Aceptar",
			new ClickHandler() {
		public void onClick(ClickEvent event) {
			dialogBoxModif.hide(true);
			glass.hide();
			modificarModelo();
		}
	});

	private Button btnCancelModif = new Button("Cancelar",
			new ClickHandler() {
		public void onClick(ClickEvent event) {
			dialogBoxModif.hide(true);
			glass.hide();
		}
	});
	//====================================================

	public IUModificarModelo(ModeloSurtidor modeloSurtidor, IUGestionarModelo iu) 
	{
		glass.show();
		this.modeloSurtidor = modeloSurtidor;
		this.iu = iu; 
		lblTitulo.setText("Modificar Modelo: " + modeloSurtidor.getNombre());
		setearWidgets();	
		cargarPanelesConWidgets();
		cargarLtBoxMarcas();
		cargarLtBoxPicos();
		cargarListBoxCantProductos();
		color();
	}


	private void cargarListBoxCantProductos() {
		for (int i = 0; i < 5; i++) {
			listBoxListaCantidadProductos.addItem(i+"",i+"");
		}
	}

	private void color() {
		lblTitulo.setStyleName("SubTitulo");	
		listBoxListaMarcas.setTitle("Lista de Marcas");
		listBoxListaCantidadPicos.setTitle("Lista de Picos");
		listBoxListaCantidadProductos.setTitle("Lista cantidad productos");
		
		lblMarca.setStyleName("Negrita");
		lblNombreModelo.setStyleName("Negrita");
		lblCantidadPicos.setStyleName("Negrita");
		lblCantidadProductos.setStyleName("Negrita");
	}


	private void cargarPanelesConWidgets() 
	{
		dialogBoxModif.add(vPanelDailModif);

		vPanelDailModif.setSize("100%", "100%");
		
		vPanelDailModif.setSpacing(15);
		vPanelDailModif.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		vPanelDailModif.add(lblTitulo);

		hPanelMarca.add(lblMarca);
		hPanelNombreModelo.add(lblNombreModelo);
		hPanelPicos.add(lblCantidadPicos);
		hPanelCantidadProductos.add(lblCantidadProductos);
		
		hPanelMarca.add(listBoxListaMarcas);
		hPanelNombreModelo.add(txtNombreModelo);
		hPanelPicos.add(listBoxListaCantidadPicos);
		hPanelCantidadProductos.add(listBoxListaCantidadProductos);
		
		vPanelDailModif.add(hPanelMarca);
		vPanelDailModif.add(hPanelNombreModelo);
		vPanelDailModif.add(hPanelPicos);
		vPanelDailModif.add(hPanelCantidadProductos);
		
		txtNombreModelo.setText(modeloSurtidor.getNombre());

		vPanelDailModif.add(hPanelDialModif);
		vPanelDailModif.add(hPanelDialModif2);

		hPanelDialModif2.add(btnCancelModif);		
		hPanelDialModif2.add(btnAceptarModif);
	}


	private void setearWidgets() 
	{
		lblMarca.setWidth("180px");
		lblCantidadPicos.setWidth("180px");
		lblNombreModelo.setWidth("180px");
		lblCantidadProductos.setWidth("180px");
		
		hPanelDialModif2.setSpacing(5);
		hPanelDialModif2.setSpacing(5);

		txtNombreModelo.setWidth("250px");

		btnAceptarModif.setWidth("100px");
		btnCancelModif.setWidth("100px");

		listBoxListaMarcas.setVisibleItemCount(1);
		listBoxListaMarcas.setWidth("250px");
		listBoxListaCantidadPicos.setWidth("250px");
		listBoxListaCantidadProductos.setWidth("250px");
	}


	public DialogBox getDialogoModif()
	{
		return dialogBoxModif;
	}


	private void modificarModelo() {
		if (this.validarCampos()) {
			String nombreModelo = this.txtNombreModelo.getText();

			int idModelo = modeloSurtidor.getMarca().getId();
			//validarModeloDisponible(nombreModelo,idModelo, modeloSurtidor.getId());
		
			modeloSurtidor.setNombre(nombreModelo);
			setearMarca();
			int picos = Integer.valueOf(listBoxListaCantidadPicos.getValue(listBoxListaCantidadPicos.getSelectedIndex()));
			modeloSurtidor.setCantidadDePicos(picos);
			int cantidadProductos = Integer.valueOf(listBoxListaCantidadProductos.getValue(listBoxListaCantidadProductos.getSelectedIndex()));
			modeloSurtidor.setCantidadDeProductos(cantidadProductos);
			guardarModeloABase();
		}	
	}

	private boolean validarCampos(){
		if(txtNombreModelo.getText().equalsIgnoreCase("")){
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe ingresar un nombre");
			vpu.showPopUp();
			return false;
		}

		if(txtNombreModelo.getText().trim().equalsIgnoreCase("")){
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe ingresar un nombre");
			vpu.showPopUp();
			return false;
		}

		return true;
	}

	/*private void validarModeloDisponible(final String nombreMarca, int idMarca, int idModeloExistente){
		ProyectoBilpa.greetingService.validarModeloDisponible(nombreMarca, idMarca, idModeloExistente,new AsyncCallback<Boolean>() {

			public void onSuccess(Boolean result) {
				if (result) {
					modeloSurtidor.setNombre(nombreMarca);
					setearMarca();
					int picos = Integer.valueOf(listBoxListaCantidadPicos.getValue(listBoxListaCantidadPicos.getSelectedIndex()));
					modeloSurtidor.setCantidadDePicos(picos);
					int cantidadProductos = Integer.valueOf(listBoxListaCantidadProductos.getValue(listBoxListaCantidadProductos.getSelectedIndex()));
					modeloSurtidor.setCantidadDeProductos(cantidadProductos);
					guardarModeloABase();
				} else {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Ya existe un modelo con ese nombre");
					vpu.showPopUp();
				}
			}

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar el modelo");
				vpu.showPopUp();
			}
		});
	}*/

	private void setearMarca() {
		for (Marca m : marcas)
		{
			if (m.getId() == Integer.valueOf(listBoxListaMarcas.getValue(listBoxListaMarcas.getSelectedIndex())))
			{
				modeloSurtidor.setMarca(m);						
			}
		}
	}
	
	private void guardarModeloABase() {
		ProyectoBilpa.greetingService.modificarModelo(modeloSurtidor, new AsyncCallback<Boolean>(){
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al modificar el modelo");
				vpu.showPopUp();
			}

			public void onSuccess(Boolean result) {
				volverAIUGestionModelo();
			}				
		});
	}

	private void volverAIUGestionModelo() {
		iu.tableModelos.clear();
		iu.tNombreModelo.setText("");
		iu.cargarTabla();		
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
				marcas = result;
				seleccionarMarca(modeloSurtidor.getMarca());
				seleccionarPico();
				seleccionarCantidadProductos();
			}
		});		

	}

	private void seleccionarPico() {
		listBoxListaCantidadPicos.setSelectedIndex(modeloSurtidor.getCantidadDePicos()-1);		
	}
	
	private void seleccionarCantidadProductos(){
		listBoxListaCantidadProductos.setSelectedIndex(modeloSurtidor.getCantidadDeProductos());
	}
	
	private void cargarLtBoxPicos(){
		for (int i = 1; i < 9; i++) {
			listBoxListaCantidadPicos.addItem(i+"",i+"");
		}
	}

	private void seleccionarMarca(Marca marca) {
		for (int i = 0 ; i < listBoxListaMarcas.getItemCount() ; i ++ )
		{
			int id = Integer.valueOf(listBoxListaMarcas.getValue(i));
			if (id == marca.getId())
			{
				listBoxListaMarcas.setSelectedIndex(i);
				break;
			}
		}
	}
}
