package app.client.iu.activos.tanque;

import java.util.ArrayList;
import java.util.List;

import app.client.ProyectoBilpa;
import app.client.dominio.Estacion;
import app.client.dominio.EstadoActivo;
import app.client.dominio.Producto;
import app.client.dominio.Tanque;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.Constants.OPERACION_GARANTIA;
import app.client.utilidades.utilObjects.GarantiaUtil;
import app.client.utilidades.utilObjects.GlassPopup;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class IUModificarTanque extends Composite{
	private IUGestionarTanque iu;
	private Tanque tanque;

	private HTML lblTitulo = new HTML();
	private DialogBox dialogBoxModif = new DialogBox();
	private VerticalPanel vPanelDailModif = new VerticalPanel();

	private FlexTable tableDatos = new FlexTable();

	private HorizontalPanel hPanelDialModifAceptarCancelar = new HorizontalPanel();

	private TextBox txtCapacidad = new TextBox();
	private TextBox txtDescripcion = new TextBox();	

	private Label lblEstacion = new Label ("Estación");
	private Label lblCombustible = new Label ("Tipo de Combustible");
	private Label lblCapacidad = new Label ("Capacidad");
	private Label lblDescripcion = new Label ("Descripción");

	private List<Estacion> listaEstaciones = new ArrayList<Estacion>();
	private List<Producto> listaCombustibles = new ArrayList<Producto>();

	private ListBox listBoxEstaciones = new ListBox();
	private ListBox listBoxCombusitbles = new ListBox();

	private GlassPopup glass = new GlassPopup();

	GarantiaUtil garantiaUtil;

	private Button btnAceptarModif = new Button("Aceptar", new ClickHandler() {
		public void onClick(ClickEvent event) {
			dialogBoxModif.hide(true);
			glass.hide();
			modificarTanque();
		}

	});

	private Button btnCancelModif = new Button("Cancelar", new ClickHandler() {
		public void onClick(ClickEvent event) {
			dialogBoxModif.hide(true);
			glass.hide();
		}
	});
	//====================================================

	public IUModificarTanque(Tanque tanque, IUGestionarTanque iu) 
	{
		glass.show();
		this.tanque = tanque;
		this.iu = iu; 
		lblTitulo.setText("Modificar tanque " + tanque.toString());
		setearWidgets();	
		cargarLtBoxEstaciones();	
		cargarLtBoxCombustibles();
		cargarPanelesConWidgets();
		color();
		garantiaUtil = new GarantiaUtil(tanque, tableDatos, 0, 2, OPERACION_GARANTIA.MODIFICAR);
		setRadios();
		setAnioFabricado();
	}

	private void setAnioFabricado() {
		if(tanque.getAnioFabricacion() != 0) {
			garantiaUtil.setAnioFabricado(tanque.getAnioFabricacion());
		}
	}

	private void setRadios() {
		if(tanque.getEstado() != null) {
			if(tanque.getEstado().getEstado().equalsIgnoreCase(EstadoActivo.GARANTIA.getEstado())) {
				garantiaUtil.setRadioGarantia();
			}
			if(tanque.getEstado().getEstado().equalsIgnoreCase(EstadoActivo.ABONO.getEstado())) {
				garantiaUtil.setRadioAbono();
			}
			if(tanque.getEstado().getEstado().equalsIgnoreCase(EstadoActivo.SIN_ABONO.getEstado())) {
				garantiaUtil.setRadioSinAbono();
			}
		}
	}

	private void color() {
		lblTitulo.setStyleName("SubTitulo");		
		lblEstacion.setStyleName("Negrita");
		lblCombustible.setStyleName("Negrita");
		lblCapacidad.setStyleName("Negrita");
		lblDescripcion.setStyleName("Negrita");
	}


	private void cargarPanelesConWidgets() {
		dialogBoxModif.add(vPanelDailModif);
		vPanelDailModif.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		vPanelDailModif.add(lblTitulo);
		vPanelDailModif.add(tableDatos);
		vPanelDailModif.add(hPanelDialModifAceptarCancelar);

		tableDatos.setWidth("590px");

		tableDatos.setCellSpacing(5);
		tableDatos.setWidget(0, 0, lblEstacion);
		tableDatos.setWidget(0, 1, listBoxEstaciones);
		tableDatos.setWidget(1, 0, lblCombustible);
		tableDatos.setWidget(1, 1, listBoxCombusitbles);
		tableDatos.setWidget(2, 0, lblCapacidad);
		tableDatos.setWidget(2, 1, txtCapacidad);
		tableDatos.setWidget(3, 0, lblDescripcion);
		tableDatos.setWidget(3, 1, txtDescripcion);

		txtCapacidad.setText(tanque.getCapacidad()+"");
		txtDescripcion.setText(tanque.getDescripcion());

		hPanelDialModifAceptarCancelar.add(btnCancelModif);		
		hPanelDialModifAceptarCancelar.add(btnAceptarModif);
	}

	private void setearWidgets() {
		vPanelDailModif.setSize("620px", "200px");
		hPanelDialModifAceptarCancelar.setSpacing(5);
		hPanelDialModifAceptarCancelar.setSpacing(5);

		lblCapacidad.setWidth("150px");
		lblCombustible.setWidth("150px");
		lblDescripcion.setWidth("150px");
		lblEstacion.setWidth("150px");

		listBoxCombusitbles.setWidth("250px");
		listBoxEstaciones.setWidth("250px");
		txtCapacidad.setWidth("250px");
		txtDescripcion.setWidth("250px");

		btnAceptarModif.setWidth("100px");
		btnCancelModif.setWidth("100px");
	}


	public DialogBox getDialogoModif()
	{
		return dialogBoxModif;
	}


	private void modificarTanque() {
		if (validarCampos())
		{
			tanque.setCapacidad(Integer.valueOf(txtCapacidad.getText()));
			tanque.setDescripcion(txtDescripcion.getText());

			int idEstacion = Integer.valueOf(this.listBoxEstaciones.getValue(this.listBoxEstaciones.getSelectedIndex()));
			final Estacion estacion = buscarEstacion (idEstacion);
			tanque.setProducto(buscarProducto(Integer.valueOf(listBoxCombusitbles.getValue(listBoxCombusitbles.getSelectedIndex()))));
			tanque.setEmpresa(estacion);
			tanque.setAnioFabricacion(garantiaUtil.getAnioFabricacion());
			
			if(garantiaUtil.validarYSetearGarantia(tanque)){
				modificarTanqueValidado();
			}else{
				glass.hide();
			}
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

	private void modificarTanqueValidado(){	
		ProyectoBilpa.greetingService.modificarTanque(tanque, new AsyncCallback<Boolean>() {

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al modificar el tanque");
				vpu.showPopUp();
			}
			public void onSuccess(Boolean result) {
				if (result) {
					volverAIUGestionarTanque();	
				} else {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al modificar el Tanque");
					vpu.showPopUp();
				}
			}
		});		
	}	

	private boolean validarCampos(){
		if(this.txtCapacidad.getText().equalsIgnoreCase("")){
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe ingresar la capacidad del tanque");
			vpu.showPopUp();
			return false;
		}
		if(this.txtCapacidad.getText().trim().equalsIgnoreCase("")){
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe ingresar la capacidad del tanque");
			vpu.showPopUp();
			return false;
		}
		if(this.txtDescripcion.getText().equalsIgnoreCase("")){
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe ingresar la descripción del tanque");
			vpu.showPopUp();
			return false;
		}
		if(this.txtDescripcion.getText().trim().equalsIgnoreCase("")){
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe ingresar la descripción del tanque");
			vpu.showPopUp();
			return false;
		}
		try {
			int capacidad = Integer.valueOf(this.txtCapacidad.getText());
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

	private void volverAIUGestionarTanque() 
	{
		dialogBoxModif.hide();
		glass.hide();
		txtCapacidad.setText("");		
		iu.cargarTablaTanques();		
	}	

	private void cargarLtBoxEstaciones(){
		listBoxEstaciones.clear();
		listaEstaciones.clear();
		ProyectoBilpa.greetingService.obtenerEmpresas(new AsyncCallback<ArrayList<Estacion>>() {

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener las Estaciones");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<Estacion> estaciones) {
				Estacion estacion;
				for (int i=0; i < estaciones.size(); i++){
					estacion = (Estacion) estaciones.get(i);
					if(estacion.getId() != 1)
					{
						listBoxEstaciones.addItem(estacion.toString(),String.valueOf(estacion.getId()));
						listaEstaciones.add(estacion);						
					}
				}
				for (int i=0; i < listBoxEstaciones.getItemCount(); i++)
				{
					if (Integer.valueOf(listBoxEstaciones.getValue(i)) == tanque.getEmpresa().getId())
					{
						listBoxEstaciones.setSelectedIndex(i);						
					}
				}
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
				for (int i=0; i < result.size(); i++)
				{
					listBoxCombusitbles.addItem(result.get(i).getNombre(), result.get(i).getId()+"");
					listaCombustibles.add(result.get(i));
				}	
				setearCombustible(tanque.getProducto().getNombre());
			}
		});		
	}

	private void setearCombustible(String combustible) {
		for (int i=0; i < listBoxCombusitbles.getItemCount(); i++)
		{
			if (combustible.equals(listBoxCombusitbles.getItemText(i)))
			{
				listBoxCombusitbles.setSelectedIndex(i);
				break;
			}				
		}
	}
}

