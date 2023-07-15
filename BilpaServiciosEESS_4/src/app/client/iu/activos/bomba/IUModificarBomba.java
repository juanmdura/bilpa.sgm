package app.client.iu.activos.bomba;

import java.util.ArrayList;
import java.util.List;

import app.client.ProyectoBilpa;
import app.client.dominio.BombaSumergible;
import app.client.dominio.Estacion;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.Constants.OPERACION_GARANTIA;
import app.client.utilidades.utilObjects.GarantiaUtil;
import app.client.utilidades.utilObjects.GlassPopup;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
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

public class IUModificarBomba extends Composite{
	private IUGestionarBomba iu;
	private BombaSumergible bomba;
	private FlexTable tableDatos = new FlexTable();
	private HTML lblTitulo = new HTML();
	private DialogBox dialogBoxModif = new DialogBox();
	private VerticalPanel vPanelDailModif = new VerticalPanel();
	private HorizontalPanel hPanelDialModif3 = new HorizontalPanel();

	private TextBox txtDescripcion = new TextBox();	
	private Label lblDescripcion = new Label ("Descripción");
	
	private Label lblEstacion = new Label ("Estación");
	private ListBox listBoxEstaciones = new ListBox();
	private List<Estacion> listaEstaciones = new ArrayList<Estacion>();
	
	private GlassPopup glass = new GlassPopup();
	
	GarantiaUtil garantiaUtil;
	
	private Button btnAceptarModif = new Button("Aceptar",new ClickHandler() {
		public void onClick(ClickEvent event) {
			dialogBoxModif.hide(true);
			glass.hide();
			modificarBomba();
		}

	});

	private Button btnCancelModif = new Button("Cancelar",new ClickHandler() {
		public void onClick(ClickEvent event) {
			dialogBoxModif.hide(true);
			glass.hide();
		}
	});
	//====================================================

	public IUModificarBomba(BombaSumergible bomba, IUGestionarBomba iu) 
	{
		glass.show();
		this.bomba = bomba;
		this.iu = iu; 
		lblTitulo.setText("Modificar bomba " + bomba.toString());
		setearWidgets();	
		cargarLtBoxEstaciones();
		cargarPanelesConWidgets();
		color();
		
		txtDescripcion.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER){
					modificarBomba();
				}				
			}
		});
		
		garantiaUtil = new GarantiaUtil(bomba, tableDatos, 0, 2, OPERACION_GARANTIA.MODIFICAR);
	}

	private void color() {
		lblTitulo.setStyleName("SubTitulo");		
		lblDescripcion.setStyleName("Negrita");
		lblEstacion.setStyleName("Negrita");
	}


	private void cargarPanelesConWidgets() {
		dialogBoxModif.add(vPanelDailModif);

		vPanelDailModif.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		vPanelDailModif.add(lblTitulo);
		vPanelDailModif.setSpacing(10);
		
		tableDatos.setWidget(0, 0, lblEstacion);
		tableDatos.setWidget(0, 1, listBoxEstaciones);
		tableDatos.setWidget(1, 0, lblDescripcion);
		tableDatos.setWidget(1, 1, txtDescripcion);
		txtDescripcion.setText(bomba.getDescripcion());

		vPanelDailModif.add(tableDatos);
		vPanelDailModif.add(hPanelDialModif3);

		hPanelDialModif3.add(btnCancelModif);		
		hPanelDialModif3.add(btnAceptarModif);
	}


	private void setearWidgets() {
		vPanelDailModif.setSize("420px", "200px");
		hPanelDialModif3.setSpacing(5);
		hPanelDialModif3.setSpacing(5);

		txtDescripcion.setWidth("250px");
		listBoxEstaciones.setWidth("250px");
		btnAceptarModif.setWidth("100px");
		btnCancelModif.setWidth("100px");
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
					if (Integer.valueOf(listBoxEstaciones.getValue(i)) == bomba.getEmpresa().getId())
					{
						listBoxEstaciones.setSelectedIndex(i);						
					}
				}
			}
		});		
	}

	public DialogBox getDialogoModif()
	{
		return dialogBoxModif;
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

	private void modificarBomba() {
		if (validarCampos())
		{
			bomba.setDescripcion(txtDescripcion.getText());
			if(garantiaUtil.validarYSetearGarantia(bomba)){
				modificarBombaBase();
			}else{
				glass.hide();
			}
		}
	}

	private void modificarBombaBase(){	
		int idEstacion = Integer.valueOf(this.listBoxEstaciones.getValue(this.listBoxEstaciones.getSelectedIndex()));
		final Estacion estacion = buscarEstacion (idEstacion);
		bomba.setEmpresa(estacion);
		
		ProyectoBilpa.greetingService.modificarBomba(bomba, new AsyncCallback<Boolean>() {

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar el Caño");
				vpu.showPopUp();
			}
			public void onSuccess(Boolean result) {
				if (result) {
					volverAIUGestionBomba();	
				} else {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al modificar el Caño");
					vpu.showPopUp();
				}
			}
		});	
	}	

	private boolean validarCampos(){
		if(this.txtDescripcion.getText().equalsIgnoreCase("")){
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe ingresar la descripción del Caño");
			vpu.showPopUp();
			return false;
		}
		if(this.txtDescripcion.getText().trim().equalsIgnoreCase("")){
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe ingresar la descripción del Caño");
			vpu.showPopUp();
			return false;
		}

		return true;
	}

	private void volverAIUGestionBomba() {
		dialogBoxModif.hide();
		glass.hide();
		iu.cargarTablaBombas();		
	}	

}

