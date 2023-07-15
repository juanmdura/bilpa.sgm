package app.client.iu.activos.surtidor;

import java.util.ArrayList;
import java.util.List;

import app.client.ProyectoBilpa;
import app.client.dominio.Estacion;
import app.client.dominio.EstadoActivo;
import app.client.dominio.ModeloSurtidor;
import app.client.dominio.Pico;
import app.client.dominio.Producto;
import app.client.dominio.Surtidor;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.Constants.OPERACION_GARANTIA;
import app.client.utilidades.utilObjects.GarantiaUtil;
import app.client.utilidades.utilObjects.GlassPopup;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class IUModificarSurtidor {

	private IUGestionarSurtidores iu;
	private Surtidor surtidor;

	private HTML lblTitulo = new HTML();
	
	private HTML lblEstaciones = new HTML("Estación");
	private HTML lblNroSerie = new HTML("N° de Serie");
	private HTML lblModeloDeSurtidor = new HTML("Modelo");

	private FlexTable tableDatos = new FlexTable();
	
	private DialogBox dialogBoxModif = new DialogBox();
	private VerticalPanel vPanelDailModif = new VerticalPanel();
	private HorizontalPanel hPanelDialModif = new HorizontalPanel();
	private HorizontalPanel hPanelDialModif2 = new HorizontalPanel();
	private TextBox txtNroSerie = new TextBox();

	private ListBox listBoxSurtidores = new ListBox();
	private ListBox listBoxEstaciones = new ListBox();	

	private List<Estacion> listaEstaciones = new ArrayList<Estacion>();
	private List<ModeloSurtidor> listaModelos = new ArrayList<ModeloSurtidor>();

	private GlassPopup glass = new GlassPopup();
	
	GarantiaUtil garantiaUtil;
	
	private Button btnAceptarModif = new Button("Aceptar", new ClickHandler() {
		public void onClick(ClickEvent event) {
			if (terminoDeCargar){
				dialogBoxModif.hide(true);
				modificarSurtidor();
			}
		}

	});

	private Button btnCancelModif = new Button("Cancelar", new ClickHandler() {
		public void onClick(ClickEvent event) {
			dialogBoxModif.hide(true);
			glass.hide();
		}
	});

	public IUModificarSurtidor(Surtidor surtidor, IUGestionarSurtidores iu) 
	{
		glass.show();
		this.surtidor = surtidor;
		this.iu = iu; 
		lblTitulo.setText("Modificar surtidor " + surtidor.toString());
		cargarSurtidores();	
		cargarEstaciones();
		setearWidgets();	
		cargarPanelesConWidgets();
		color();
		tableDatos.getColumnFormatter().setWidth(3, "80px");
		tableDatos.getColumnFormatter().setWidth(4, "80px");
		tableDatos.getColumnFormatter().setWidth(5, "80px");
		garantiaUtil = new GarantiaUtil(surtidor, tableDatos, 0, 2, OPERACION_GARANTIA.MODIFICAR);
		setRadios();
		setAnioFabricado();
	}


	private void setAnioFabricado() {
		if(surtidor.getAnioFabricacion() != 0) {
			garantiaUtil.setAnioFabricado(surtidor.getAnioFabricacion());
		}
	}


	private void setRadios() {
		
		if(surtidor.getEstado() != null) {
			if(surtidor.getEstado().getEstado().equalsIgnoreCase(EstadoActivo.GARANTIA.getEstado())) {
				garantiaUtil.setRadioGarantia();
			}
			if(surtidor.getEstado().getEstado().equalsIgnoreCase(EstadoActivo.ABONO.getEstado())) {
				garantiaUtil.setRadioAbono();
			}
			if(surtidor.getEstado().getEstado().equalsIgnoreCase(EstadoActivo.SIN_ABONO.getEstado())) {
				garantiaUtil.setRadioSinAbono();
			}
		}
		
	}


	private void color() {
		lblTitulo.setStyleName("SubTitulo");		
		lblModeloDeSurtidor.setStyleName("Negrita");
		lblNroSerie.setStyleName("Negrita");
		lblEstaciones.setStyleName("Negrita");
	}

	private void cargarGrilla()
	{
		tableDatos.setWidth("900px");

		tableDatos.setCellSpacing(5);
		tableDatos.setWidget(0, 0, lblEstaciones);
		tableDatos.setWidget(0, 1, listBoxEstaciones);
		tableDatos.setWidget(1, 0, lblNroSerie);
		tableDatos.setWidget(1, 1, txtNroSerie);
		
		tableDatos.setWidget(2, 0, lblModeloDeSurtidor);
		tableDatos.setWidget(2, 1, listBoxSurtidores);
		
		listBoxSurtidores.setWidth("240px");
		txtNroSerie.setWidth("240px");
		listBoxEstaciones.setWidth("240px");
	}

	private void cargarPanelesConWidgets() {
		dialogBoxModif.add(vPanelDailModif);

		vPanelDailModif.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelDailModif.setSpacing(10);
		vPanelDailModif.add(lblTitulo);

		vPanelDailModif.add(tableDatos);
		txtNroSerie.setText(surtidor.getNumeroSerie());

		vPanelDailModif.add(hPanelDialModif);
		vPanelDailModif.add(hPanelDialModif2);

		hPanelDialModif2.add(btnCancelModif);	
		hPanelDialModif2.add(btnAceptarModif);
		
		cargarGrilla();
	}


	private void setearWidgets() {
		vPanelDailModif.setSize("900px", "200px");
		hPanelDialModif2.setSpacing(5);
		hPanelDialModif2.setSpacing(5);

		txtNroSerie.setWidth("250px");
		
		btnAceptarModif.setWidth("100px");
		btnCancelModif.setWidth("100px");

		listBoxEstaciones.setWidth("240px");
		listBoxSurtidores.setWidth("640px");
	}


	public DialogBox getDialogoModif()
	{
		return dialogBoxModif;
	}


	private void modificarSurtidor() {
		if (validarCampos() )
		{
			if (!nroSeriSonIguales ())
			{
				if(garantiaUtil.validarYSetearGarantia(surtidor)){
					validarSurtidor(new Surtidor(txtNroSerie.getText()));				
				}else{
					glass.hide();
				}
			}
			else
			{
				if(garantiaUtil.validarYSetearGarantia(surtidor)){
					guardarSurtidor();
				}else{
					glass.hide();
				}
				
			}
		}
	}

	private boolean nroSeriSonIguales() 
	{
		String nro1 = surtidor.getNumeroSerie();
		String nro2 = txtNroSerie.getText();
		if (nro1.equals(nro2))
		{
			return true;
		}
		return false;
	}


	private boolean validarCampos(){
		if(this.txtNroSerie.getText().equalsIgnoreCase("")){
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe ingresar un numero de serie");
			vpu.showPopUp();
			return false;
		}

		if(this.txtNroSerie.getText().trim().equalsIgnoreCase("")){
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe ingresar un numero de serie");
			vpu.showPopUp();
			return false;
		}

		return true;
	}

	private void validarSurtidor(final Surtidor s){
		if(s != null){
			ProyectoBilpa.greetingService.validarSurtidorExiste(s, new AsyncCallback<Boolean>() {

				public void onFailure(Throwable caught) {
					glass.hide();
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar el surtidor");
					vpu.showPopUp();
				}
				public void onSuccess(Boolean result) {
					if(!result){
						guardarSurtidor();
					}else{
						glass.hide();
						ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Ya existe un surtidor con ese numero de serie");
						vpu.showPopUp();
					}

				}
			});		
		}
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

	private void volverAIUGestionModelo() {
		txtNroSerie.setText("");
		iu.cargarTabla();
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
				int itemDelModeloSurtidor = 0;
				for (int i=0; i < result.size(); i++)
				{
					auxiliar = (ModeloSurtidor) result.get(i);
					listBoxSurtidores.addItem(auxiliar.toString(),String.valueOf(auxiliar.getId()));
					listaModelos.add(auxiliar);
					
					if (surtidor.getModelo().equals(auxiliar))
					{
						itemDelModeloSurtidor = listBoxSurtidores.getItemCount()-1;
					}
				}	
				listBoxSurtidores.setItemSelected(itemDelModeloSurtidor, true);
			}
		});		
	}
	
	private void cargarEstaciones(){
		listBoxEstaciones.clear();
		listaEstaciones.clear();
		ProyectoBilpa.greetingService.obtenerEmpresas(new AsyncCallback<ArrayList<Estacion>>() {

			public void onFailure(Throwable caught) {
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
				
				for(int i = 0 ; i < listaEstaciones.size() ; i++)
				{
					if (surtidor.getEmpresa().getId() == listaEstaciones.get(i).getId())
					{
						listBoxEstaciones.setItemSelected(i, true);
					}
				}
				terminoDeCargar = true;
			}
		});		
	}
	private boolean terminoDeCargar = false;
	private void guardarSurtidor()
	{
		int idModelo = Integer.valueOf(this.listBoxSurtidores.getValue(this.listBoxSurtidores.getSelectedIndex()));
		int idEstacion = Integer.valueOf(this.listBoxEstaciones.getValue(this.listBoxEstaciones.getSelectedIndex()));
		
		final Estacion estacion = buscarEstacion (idEstacion);
		final ModeloSurtidor modeloSurtidor = buscarModeloSurtidor(idModelo);

		String nroSerie = txtNroSerie.getText().trim();		
		surtidor.setNumeroSerie(nroSerie);
		surtidor.setEmpresa(estacion);
		surtidor.setModelo(modeloSurtidor);
		surtidor.setAnioFabricacion(garantiaUtil.getAnioFabricacion());

		/*int cantidadPicosModeloActual = modeloSurtidor.getCantidadDePicos();
		int cantidadPicosQueTeniaElSurtidor = surtidor.getPicos().size();
		
		if (cantidadPicosModeloActual != cantidadPicosQueTeniaElSurtidor){
			eliminarTodosLosPicosDelSurtidor(modeloSurtidor);
		
		} else {*/
			modificarSurtidor2();	
		//}		
	}


	/*private void eliminarTodosLosPicosDelSurtidor(final ModeloSurtidor modeloSurtidor) {
		ProyectoBilpa.greetingService.eliminarTodosLosPicosDelSurtidor(surtidor.getId(), new AsyncCallback<Boolean>() {

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al modificar el surtidor");
				vpu.showPopUp();
			}
			public void onSuccess(Boolean result) {
				surtidor.getPicos().clear();
				for (int i = 0; i < modeloSurtidor.getCantidadDePicos(); i++) {
					Pico pico = new Pico();
					pico.setProducto(new Producto(1));
					pico.setSurtidor(surtidor);
					pico.setNumeroPico(i+1);
					surtidor.agregarPico(pico);
				}
				modificarSurtidor2();
			}
		});		
	}*/

	private void modificarSurtidor2() {
		ProyectoBilpa.greetingService.modificarSurtidor(surtidor, new AsyncCallback<Boolean>() {

			public void onFailure(Throwable caught) {
				glass.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al modificar el surtidor");
				vpu.showPopUp();
			}
			public void onSuccess(Boolean result) {
				volverAIUGestionModelo();	
				glass.hide();
			}
		});
	}
}
