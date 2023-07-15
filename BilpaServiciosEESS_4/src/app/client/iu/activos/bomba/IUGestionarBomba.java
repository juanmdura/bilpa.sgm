package app.client.iu.activos.bomba;

import java.util.ArrayList;
import java.util.List;

import app.client.ProyectoBilpa;
import app.client.dominio.Activo;
import app.client.dominio.BombaSumergible;
import app.client.dominio.Estacion;
import app.client.dominio.Persona;
import app.client.iu.rightClick.AdvLabel;
import app.client.iu.rightClick.AdvLabelBomba;
import app.client.iu.rightClick.Entero;
import app.client.iu.rightClick.RightClickEngineBomba;
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

public class IUGestionarBomba extends Composite {

	private HTML htmlTitulo = new HTML("Gestión de bombas sumergibles");

	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private HorizontalPanel hPanelPrincipal = new HorizontalPanel();
	private HorizontalPanel hPanelContenedor = new HorizontalPanel();
	private VerticalPanel vPanel1 = new VerticalPanel();
	private ListBox listBoxListaEstaciones = new ListBox();
	private List<Estacion> listaEstaciones = new ArrayList<Estacion>();
	private Persona sesion;

	DecoratorPanel decorator = new DecoratorPanel();
	private HorizontalPanel hPanelDatos = new HorizontalPanel();
	private Label lblEstacion = new Label("Estación");
	private Label lblDescripcion = new Label("Descripción");
	private TextBox tDescripcion = new TextBox();
	private FlexTable tableDatos = new FlexTable();
	private FlexTable tablesBombas = new FlexTable();
	private ArrayList<Activo> bombas = new ArrayList<Activo>();
	private PopupCargando popUp = new PopupCargando("Cargando...");
	
	private GlassPopup glass = new GlassPopup();
	
	GarantiaUtil garantiaUtil;
	
	Button bGuardar = new Button("Guardar", new ClickHandler() {
		public void onClick(ClickEvent event) {
			validarTodo();
		}
	});

	public VerticalPanel getPrincipalPanel() {
		return vPanelPrincipal;
	}

	public IUGestionarBomba(Persona persona){
		this.sesion = persona;
		setearWidgets(); // Setea el tamano de los Widgets.
		cargarPanelesConWidgets(); // Agrega los Widget a los paneles.
		cargarLtBoxEstaciones();
		color();
		
		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal.add(hPanelPrincipal);
		vPanelPrincipal.add(tablesBombas);
		
		crearTablaBombas();
		
		listBoxListaEstaciones.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				cargarTablaBombas();
			}
		});
		
		tDescripcion.addKeyPressHandler(new KeyPressHandler() {
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

	private void crearTablaBombas() {
		tablesBombas.setTitle("Doble Click para Modificar");
		tablesBombas.setCellPadding(2);
		tablesBombas.setText(0, 0, "Descripción");
		tablesBombas.setText(0, 1, "Inicio Garantía");
		tablesBombas.setText(0, 2, "Fin Garantía");
		tablesBombas.getRowFormatter().addStyleName(0, "CabezalTabla");
	}

	private void setearWidgets() {
		htmlTitulo.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal.setWidth("600px");
		bGuardar.setWidth("100px");

		decorator.setSize("600px", "120px");
		tablesBombas.setWidth("450px");

		listBoxListaEstaciones.setVisibleItemCount(1);
		
		listBoxListaEstaciones.setTitle("Lista de Estaciones");
		tDescripcion.setTitle("Ingrese la descripción de la bomba sumergible");

		tDescripcion.setWidth("100%");

		listBoxListaEstaciones.setWidth("100%");
		hPanelDatos.setSpacing(5);

		tableDatos.setCellSpacing(5);
		tableDatos.setCellPadding(2);

		lblEstacion.setStyleName("Negrita");
		lblDescripcion.setStyleName("Negrita");

		tableDatos.setWidget(0, 0, lblEstacion);
		tableDatos.setWidget(1, 0, lblDescripcion);

		tableDatos.setWidget(0, 1, listBoxListaEstaciones);
		tableDatos.setWidget(1, 1, tDescripcion);
		tableDatos.setWidget(2, 5, bGuardar);

		decorator.add(tableDatos);
	}

	private void cargarPanelesConWidgets() {
		hPanelDatos.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		vPanel1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		vPanelPrincipal.add(htmlTitulo);
		vPanel1.add(hPanelDatos);
		hPanelDatos.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hPanelDatos.add(this.decorator);

		vPanel1.setSpacing(20);
		hPanelContenedor.add(vPanel1);
		vPanelPrincipal.add(hPanelContenedor);
	}

	private void cargarLtBoxEstaciones() {
		popUp.show();
		listBoxListaEstaciones.clear();
		listaEstaciones.clear();
		ProyectoBilpa.greetingService.obtenerEmpresas(new AsyncCallback<ArrayList<Estacion>>() {
			public void onFailure(Throwable caught) {
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener las Estaciones");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<Estacion> result) {
				Estacion auxiliar;
				for (int i = 0; i < result.size(); i++) {
					auxiliar = (Estacion) result.get(i);
					if (auxiliar.getId() != 1) {
						listBoxListaEstaciones.addItem(auxiliar.toString(), String.valueOf(auxiliar.getId()));
						listaEstaciones.add(auxiliar);
					}
				}
				cargarTablaBombas();
				popUp.hide();
			}
		});
	}

	private void validarTodo() {
		if (this.validarCampos()) {
			crearBomba();
		}
	}

	private void crearBomba() {
		BombaSumergible bomba = new BombaSumergible();
		String descripcion = tDescripcion.getText();

		int idEstacion = Integer.valueOf(this.listBoxListaEstaciones.getValue(this.listBoxListaEstaciones.getSelectedIndex()));
		final Estacion estacion = buscarEstacion(idEstacion);

		bomba.setEmpresa(estacion);
		bomba.setDescripcion(descripcion);
		
		if(garantiaUtil.validarYSetearGarantia(bomba)){
			guardarBomba(bomba, estacion);
		}
	}

	private void guardarBomba(BombaSumergible bomba, final Estacion estacion) {
		ProyectoBilpa.greetingService.guardarBomba(bomba, new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar el Caño");
				vpu.showPopUp();
			}

			public void onSuccess(Boolean result) {
				cargarTablaBombas();
			}
		});
	}

	private Estacion buscarEstacion(int id) {
		for (Estacion e : listaEstaciones) {
			if (e.getId() == id) {
				return e;
			}
		}
		return null;
	}

	private boolean validarCampos() {
		if (this.tDescripcion.getText().equalsIgnoreCase("")) {
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe ingresar la descripción del Caño");
			vpu.showPopUp();
			return false;
		}
		if (this.tDescripcion.getText().trim().equalsIgnoreCase("")) {
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe ingresar la descripción del Caño");
			vpu.showPopUp();
			return false;
		}

		return true;
	}

	public void cargarTablaBombas() {
		popUp.show();
		int idEstacion = Integer.valueOf(this.listBoxListaEstaciones.getValue(this.listBoxListaEstaciones.getSelectedIndex()));
		final Estacion estacion = buscarEstacion(idEstacion);
		int tipoActivo = 4;
		
		ProyectoBilpa.greetingService.obtenerActivosPorTipo(estacion, tipoActivo, new AsyncCallback<ArrayList<Activo>>() {
			public void onSuccess(ArrayList<Activo> result) {
				bombas = result;
				tDescripcion.setText("");
				garantiaUtil = new GarantiaUtil(null, tableDatos, 0, 2, OPERACION_GARANTIA.AGREGAR);
				dibujarTabla();
				popUp.hide();
			}

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al Obtener Activos Por Tipo");
				vpu.showPopUp();
				popUp.hide();
			}
		});
	}

	public void dibujarTabla() {
		UtilUI.limpiarTabla(tablesBombas);
		
		for (int i = 0; i < bombas.size(); i++) {
			BombaSumergible bomba = (BombaSumergible) bombas.get(i);

			String fechaInicio = bomba.getInicioGarantia() != null ? (garantiaUtil.getFechaFormateada(bomba.getInicioGarantia())) : "-";
			String fechaFin = bomba.getFinGarantia() != null ? (garantiaUtil.getFechaFormateada(bomba.getFinGarantia())) : "-";
			
			RightClickEngineBomba rceDesc = new RightClickEngineBomba();
			RightClickEngineBomba rceInicioGarantia = new RightClickEngineBomba();
			RightClickEngineBomba rceFinGarantia = new RightClickEngineBomba();
			
			AdvLabel labelDesc = rceDesc.configureAdvLabel(new AdvLabelBomba(new Entero (i, tablesBombas.getCellCount(i)), bomba.getDescripcion(), bomba, this));
			AdvLabel labelInicio = rceInicioGarantia.configureAdvLabel(new AdvLabelBomba(new Entero (i, tablesBombas.getCellCount(i)), fechaInicio, bomba, this));
			AdvLabel labelFin = rceFinGarantia.configureAdvLabel(new AdvLabelBomba(new Entero (i, tablesBombas.getCellCount(i)),  fechaFin, bomba, this));
			
			tablesBombas.setWidget(i + 1, 0,labelDesc);
			tablesBombas.setWidget(i + 1, 1,labelInicio);
			tablesBombas.setWidget(i + 1, 2,labelFin);
			
			UtilCss.aplicarEstiloATabla(tablesBombas, i + 2);
		}
	}
}


