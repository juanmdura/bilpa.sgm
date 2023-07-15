package app.client.iu.activos.surtidor;

import java.util.ArrayList;
import java.util.List;

import app.client.ProyectoBilpa;
import app.client.dominio.Activo;
import app.client.dominio.Estacion;
import app.client.dominio.Persona;
import app.client.dominio.Pico;
import app.client.dominio.Producto;
import app.client.dominio.Surtidor;
import app.client.dominio.data.EstacionDataList;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class IUGestionarPico {

	private HTML htmlTitulo = new HTML("Gestión de picos");

	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private ListBox listBoxListaEstaciones = new ListBox();

	private ListBox listBoxListaCombustible = new ListBox();
	private ListBox listBoxListaSurtidores = new ListBox();
	private ListBox listBoxListaPicos = new ListBox();
	private ListBox listBoxListaPicosEnEstacion = new ListBox();
	private List<EstacionDataList> listaEstaciones = new ArrayList<EstacionDataList>();
	private List<Producto> listaCombustibles = new ArrayList<Producto>();
	
	private Persona sesion;

	private boolean terminoDeCargar = false;

	List<Surtidor> surtidores = new ArrayList<Surtidor>();

	private Label lEstacion = new Label("Estación");
	private Label lSurtidor = new Label("Surtidor");
	private Label lPico = new Label("N° de pico");
	private Label lPicoEstacion = new Label("Nº en estación");
	private Label lCombustible = new Label("Tipo de combustible");
	private PopupCargando popUp = new PopupCargando("Cargando...");

	private Grid datosIngreso = new Grid();

	DecoratorPanel decorator = new DecoratorPanel();

	private FlexTable tablesTanques = new FlexTable();

	private GlassPopup glass = new GlassPopup();

	Button bCancelar = new Button("Cancelar", new ClickHandler() {
		public void onClick(ClickEvent event) {

		}
	});

	public VerticalPanel getPrincipalPanel() {
		return vPanelPrincipal;
	}

	public IUGestionarPico(Persona persona) {
		listBoxListaEstaciones.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				if (terminoDeCargar)
					obtenerSurtidoresBase();
			}
		});

		listBoxListaSurtidores.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				if (terminoDeCargar)
					cargarLtBoxPicos();
			}
		});

		listBoxListaPicos.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				if (terminoDeCargar)
					seleccionarTipoCombustible();
			}
		});

		listBoxListaCombustible.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				validarCampos();
			}
		});
		
		listBoxListaPicosEnEstacion.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				validarCampos();
			}
		});
		
		this.sesion = persona;
		setearWidgets(); // Setea el tamano de los Widgets.
		crearGrillaIngreso();
		obtenerEstaciones();
		cargarListBoxPicosEnEstacion();
		obtenerProductos();
		color();
		vPanelPrincipal
		.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal.setSpacing(10);
		this.vPanelPrincipal.add(htmlTitulo);
		this.vPanelPrincipal.add(decorator);
		this.vPanelPrincipal.add(tablesTanques);
	}

	private void crearGrillaIngreso() {
		datosIngreso = new Grid(6, 2);
		HorizontalPanel panel = new HorizontalPanel();
		panel.setSpacing(5);
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panel.setBorderWidth(1);
		panel.setWidth("100%");
		bCancelar.setWidth("100px");
		panel.add(bCancelar);

		datosIngreso.setCellSpacing(5);
		datosIngreso.setWidget(0, 0, lEstacion);
		datosIngreso.setWidget(0, 1, listBoxListaEstaciones);

		listBoxListaEstaciones.setWidth("100%");
		datosIngreso.setWidget(1, 0, lSurtidor);
		datosIngreso.setWidget(1, 1, listBoxListaSurtidores);

		datosIngreso.setWidget(2, 0, lPico);
		datosIngreso.setWidget(2, 1, listBoxListaPicos);
		listBoxListaPicos.setWidth("100%");

		listBoxListaSurtidores.setWidth("100%");
		datosIngreso.setWidget(3, 0, lPicoEstacion);
		datosIngreso.setWidget(3, 1, listBoxListaPicosEnEstacion);
		listBoxListaPicosEnEstacion.setWidth("100%");

		datosIngreso.setWidget(4, 0, lCombustible);
		datosIngreso.setWidget(4, 1, listBoxListaCombustible);
		listBoxListaCombustible.setWidth("100%");
		decorator.add(datosIngreso);
	}

	private void color() {
		htmlTitulo.setStyleName("Titulo");
		lEstacion.setStyleName("Negrita");
		lSurtidor.setStyleName("Negrita");
		lCombustible.setStyleName("Negrita");
		lPico.setStyleName("Negrita");
		lPicoEstacion.setStyleName("Negrita");
	}

	private void setearWidgets() {
		listBoxListaCombustible.setTitle("Lista de Combustibles");
		listBoxListaEstaciones.setTitle("Lista de Estaciones");
		listBoxListaPicos.setTitle("Lista de Picos");
		listBoxListaSurtidores.setTitle("Lista de Surtidores");
		listBoxListaPicosEnEstacion.setTitle("Lista de Picos en Estación");
	}

	private void obtenerEstaciones() {
		listBoxListaEstaciones.clear();
		listaEstaciones.clear();
		popUp.show();
		ProyectoBilpa.greetingService
		.obtenerEmpresasDataList(new AsyncCallback<ArrayList<EstacionDataList>>() {

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener las estaciones");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<EstacionDataList> result) {
				EstacionDataList auxiliar;
				for (int i = 0; i < result.size(); i++) {
					auxiliar = (EstacionDataList) result.get(i);
					if (auxiliar.getId() != 1) {
						listBoxListaEstaciones.addItem(
								auxiliar.toString(),
								String.valueOf(auxiliar.getId()));
						listaEstaciones.add(auxiliar);
					}
				}
				obtenerSurtidoresBase();
				listBoxListaEstaciones.setSelectedIndex(0);
			}
		});
	}

	private void obtenerSurtidoresBase() {
		popUp.show();
		int idEstacion = Integer.valueOf(this.listBoxListaEstaciones
				.getValue(this.listBoxListaEstaciones.getSelectedIndex()));
		final EstacionDataList estacionDataList = buscarEstacion(idEstacion);

		Estacion estacion = new Estacion();
		estacion.setId(estacionDataList.getId());

		ProyectoBilpa.greetingService.obtenerActivosPorTipo(estacion, 1,
				new AsyncCallback<ArrayList<Activo>>() {

			public void onSuccess(ArrayList<Activo> result) {
				listBoxListaSurtidores.clear();
				surtidores.clear();
				for (int i = 0; i < result.size(); i++) {
					listBoxListaSurtidores.addItem(result.get(i)
							.toString(), String.valueOf(result.get(i)
									.getId()));
					surtidores.add((Surtidor) result.get(i));
					cargarLtBoxPicos();
				}
				listBoxListaSurtidores.setSelectedIndex(0);
			}

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener activos por tipo");
				vpu.showPopUp();
			}
		});
	}

	private void cargarLtBoxPicos() {
		int idSurtidor = Integer.valueOf(this.listBoxListaSurtidores
				.getValue(this.listBoxListaSurtidores.getSelectedIndex()));
		final Surtidor surtidor = buscarSurtidor(idSurtidor);
		listBoxListaPicos.clear();
		Pico picoSeleccionado = null;

		for (Pico pico : surtidor.getPicos()) {
			if (picoSeleccionado == null) {
				picoSeleccionado = pico;
			}
			listBoxListaPicos.addItem(pico.getNumeroPico() + "", "" + pico.getId());
		}
		listBoxListaPicos.setSelectedIndex(0);

		if (picoSeleccionado != null) {
			listBoxListaPicosEnEstacion.setSelectedIndex(picoSeleccionado
					.getNumeroEnLaEstacion());
		} 

		seleccionarTipoCombustible();
	}

	private void seleccionarTipoCombustible() {
		int selected = this.listBoxListaPicos.getSelectedIndex();

		if (selected >= 0) {
			int idPico = Integer.valueOf(this.listBoxListaPicos
					.getValue(selected));
			int idSurtidor = Integer.valueOf(this.listBoxListaSurtidores
					.getValue(this.listBoxListaSurtidores.getSelectedIndex()));
			final Surtidor surtidor = buscarSurtidor(idSurtidor);
			Pico pico = surtidor.buscarPico(idPico);

			listBoxListaPicosEnEstacion.setSelectedIndex(pico.getNumeroEnLaEstacion());
			listBoxListaCombustible.setSelectedIndex(pico.getProducto().getId()-1);
			/*if (pico.getTipoCombusitble().equalsIgnoreCase("N/A")) {
				listBoxListaCombustible.setSelectedIndex(0);
			}
			if (pico.getTipoCombusitble().equalsIgnoreCase(
					TipoCombustible.Super95SP)) {
				listBoxListaCombustible.setSelectedIndex(1);
			}
			if (pico.getTipoCombusitble().equalsIgnoreCase(
					TipoCombustible.Especial87SP)) {
				listBoxListaCombustible.setSelectedIndex(2);
			}
			if (pico.getTipoCombusitble().equalsIgnoreCase(
					TipoCombustible.Premium97SP)) {
				listBoxListaCombustible.setSelectedIndex(3);
			}
			if (pico.getTipoCombusitble().equalsIgnoreCase(
					TipoCombustible.GasOil)) {
				listBoxListaCombustible.setSelectedIndex(4);
			}
			if (pico.getTipoCombusitble().equalsIgnoreCase(
					TipoCombustible.GasOilEspecial)) {
				listBoxListaCombustible.setSelectedIndex(5);
			}
			if (pico.getTipoCombusitble().equalsIgnoreCase(
					TipoCombustible.Super95SPEthanol)) {
				listBoxListaCombustible.setSelectedIndex(6);
			}
			if (pico.getTipoCombusitble().equalsIgnoreCase(
					TipoCombustible.Especial87SPEthanol)) {
				listBoxListaCombustible.setSelectedIndex(7);
			}
			if (pico.getTipoCombusitble().equalsIgnoreCase(
					TipoCombustible.Premium97SPEthanol)) {
				listBoxListaCombustible.setSelectedIndex(8);
			}
			if (pico.getTipoCombusitble().equalsIgnoreCase(
					TipoCombustible.Queroseno)) {
				listBoxListaCombustible.setSelectedIndex(9);
			}*/
		}
		terminoDeCargar = true;
		popUp.hide();
	}

	private void obtenerProductos() {
		listBoxListaCombustible.clear();
		ProyectoBilpa.greetingService
		.obtenerTiposCombustibles(new AsyncCallback<List<Producto>>() {

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener el combustible");
				vpu.showPopUp();
			}

			public void onSuccess(List<Producto> result) {
				listaCombustibles.clear();
				for (int i = 0; i < result.size(); i++) {
					listBoxListaCombustible.addItem(result.get(i).getNombre(), result.get(i).getId()+"");
					listaCombustibles.add(result.get(i));
				}
			}
		});
	}

	private EstacionDataList buscarEstacion(int id) {
		for (EstacionDataList e : listaEstaciones) {
			if (e.getId() == id) {
				return e;
			}
		}
		return null;
	}

	private Surtidor buscarSurtidor(int idSurtidor) {
		for (Surtidor s : surtidores) {
			if (s.getId() == idSurtidor) {
				return s;
			}
		}
		return null;
	}

	private void cargarListBoxPicosEnEstacion() {
		for (int i = 0; i < 101; i++) {
			listBoxListaPicosEnEstacion.addItem(i + "", i + "");
		}
	}

	protected void validarCampos() {
		int idPico = Integer.valueOf(this.listBoxListaPicos.getValue(this.listBoxListaPicos.getSelectedIndex()));
		int idSurtidor = Integer.valueOf(this.listBoxListaSurtidores.getValue(this.listBoxListaSurtidores.getSelectedIndex()));
		int idPicoEnEstacion = Integer.valueOf(this.listBoxListaPicosEnEstacion.getValue(this.listBoxListaPicosEnEstacion.getSelectedIndex()));
		final Surtidor surtidor = buscarSurtidor(idSurtidor);
		Pico pico = surtidor.buscarPico(idPico);
		pico.setNumeroEnLaEstacion(idPicoEnEstacion);
		pico.setProducto(buscarProducto(Integer.valueOf(listBoxListaCombustible.getValue(listBoxListaCombustible.getSelectedIndex()))));
		ProyectoBilpa.greetingService.actualizarSurtidor(surtidor, new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar el pico");
				vpu.showPopUp();
			}

			public void onSuccess(Boolean result) {
			}
		});
	}
	
	private Producto buscarProducto(Integer id) {
		for (Producto p : listaCombustibles){
			if (p.getId() == id){
				return p;
			}
		}
		return null;
	}
}
