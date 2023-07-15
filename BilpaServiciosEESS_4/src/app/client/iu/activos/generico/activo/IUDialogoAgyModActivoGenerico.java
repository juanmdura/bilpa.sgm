package app.client.iu.activos.generico.activo;

import java.util.List;

import app.client.ProyectoBilpa;
import app.client.dominio.ActivoGenerico;
import app.client.dominio.Estacion;
import app.client.dominio.MarcaActivoGenerico;
import app.client.dominio.ModeloActivoGenerico;
import app.client.dominio.TipoActivoGenerico;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;

import com.google.gwt.core.client.Scheduler;
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
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;

public class IUDialogoAgyModActivoGenerico extends DialogBox{
	private VerticalPanel vPanelDial = new VerticalPanel();
	private HorizontalPanel hPanelBotonera = new HorizontalPanel();
	private DecoratorPanel decorator = new DecoratorPanel();
	private Label lblTitulo;
	private Label lblNombre = new Label("Serie ");
	private Label lblMarca = new Label("Marca ");
	private Label lblModelo = new Label("Modelo ");
	private Label lblFecha = new Label("Fecha ");
	private Label lblDesc = new Label("Descripción ");

	private TextBox txtSerie = new TextBox();
	private TextBox txtDescripcion = new TextBox();
	private DateBox dpFecha = new DateBox();
	private ListBox lbxMarcas = new ListBox();
	private ListBox lbxModelos = new ListBox();

	private GlassPopup glass;
	private PopupCargando popUp;
	private IUGestionarActivoGenerico padre;
	private ActivoGenerico activoGenericoSeleccionado;
	private List<ModeloActivoGenerico> modelos;

	public IUDialogoAgyModActivoGenerico(boolean autoHide, IUGestionarActivoGenerico padre, GlassPopup glass, PopupCargando popUp, final ActivoGenerico activoGenericoSeleccionado,
			List<MarcaActivoGenerico> marcas, final List<ModeloActivoGenerico> modelos) {

		super(autoHide);
		this.glass = glass;
		this.popUp = popUp;
		this.padre = padre;
		this.glass.show();
		this.activoGenericoSeleccionado = activoGenericoSeleccionado;
		this.modelos = modelos;

		cargarMarcas(marcas);
		cargarModelos();
		
		String accion = "";
		if (activoGenericoSeleccionado != null){
			txtSerie.setText(activoGenericoSeleccionado.getSerie());
			if (activoGenericoSeleccionado.getFecha() != null){
				dpFecha.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("yyyy-MM-dd")));
				dpFecha.setValue(activoGenericoSeleccionado.getFecha());
			}
			txtDescripcion.setText(activoGenericoSeleccionado.getDescripcion());
			accion = "Editar ";
		} else {
			accion = "Agregar ";
		}
		lblTitulo = new Label(accion + padre.getTipoActivoSeleccionado().getNombre());

		txtSerie.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (KeyCodes.KEY_ENTER == event.getNativeEvent().getKeyCode())
				{
					sumbit(activoGenericoSeleccionado);
				}
			}
		});
		
		txtDescripcion.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (KeyCodes.KEY_ENTER == event.getNativeEvent().getKeyCode())
				{
					sumbit(activoGenericoSeleccionado);
				}
			}
		});

		lbxMarcas.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				cargarModelos();
			}
		});
		
		lbxModelos.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (KeyCodes.KEY_ENTER == event.getNativeEvent().getKeyCode())
				{
					sumbit(activoGenericoSeleccionado);
				}
			}
		});

		cargarPanelesConWidgets();
		color();
		cargarGrilla();
	}

	private void cargarMarcas(List<MarcaActivoGenerico> marcas) {
		lbxMarcas.clear();
		lbxMarcas.addItem("", "0");
		for (MarcaActivoGenerico marca : marcas) {
			lbxMarcas.addItem(marca.getNombre(), marca.getId()+"");
		}
		
		if (activoGenericoSeleccionado != null && activoGenericoSeleccionado.getMarca() != null){
			for (int i=0; i < lbxMarcas.getItemCount(); i++)
			{
				if (Integer.valueOf(lbxMarcas.getValue(i)) == activoGenericoSeleccionado.getMarca().getId())
				{
					lbxMarcas.setSelectedIndex(i);						
				}
			}
		}
	}

	private void cargarModelos() {
		lbxModelos.clear();
		lbxModelos.addItem("", "0");
		for (ModeloActivoGenerico modelo : this.modelos) {
			if (modelo.getMarca().getId() == Integer.valueOf(lbxMarcas.getValue(lbxMarcas.getSelectedIndex()))) {
				lbxModelos.addItem(modelo.getNombre(), modelo.getId()+"");
			}
		}
		
		if (activoGenericoSeleccionado != null && activoGenericoSeleccionado.getModelo() != null){
			for (int i=0; i < lbxModelos.getItemCount(); i++)
			{
				if (Integer.valueOf(lbxModelos.getValue(i)) == activoGenericoSeleccionado.getModelo().getId())
				{
					lbxModelos.setSelectedIndex(i);						
				}
			}
		}
	}

	private void sumbit(final ActivoGenerico activoGenericoSeleccionado) {
		if (activoGenericoSeleccionado == null){
			agregarActivoGenerico();
		} else {
			editarActivoGenerico();
		}
	}

	private Button btnAceptarModif = new Button("Aceptar", new ClickHandler() {
		public void onClick(ClickEvent event) {
			sumbit(activoGenericoSeleccionado);
		}
	});

	private Button btnCancelModif = new Button("Cancelar", new ClickHandler() {
		public void onClick(ClickEvent event) {
			hide(true);
			glass.hide();
		}
	});

	private void color() {
		lblTitulo.setStyleName("Titulo");
		lblNombre.setStyleName("Negrita");
		lblMarca.setStyleName("Negrita");
		lblModelo.setStyleName("Negrita");
		lblFecha.setStyleName("Negrita");
		lblDesc.setStyleName("Negrita");

		txtSerie.setWidth("250px");
		txtDescripcion.setWidth("250px");
		dpFecha.setWidth("250px");
		lbxMarcas.setWidth("250px");
		lbxModelos.setWidth("250px");

		btnAceptarModif.setWidth("100px");
		btnCancelModif.setWidth("100px");
	}

	private void cargarPanelesConWidgets() {
		vPanelDial.setSpacing(10);
		vPanelDial.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hPanelBotonera.setSpacing(10);
		hPanelBotonera.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hPanelBotonera.add(btnCancelModif);
		hPanelBotonera.add(btnAceptarModif);
		vPanelDial.add(decorator);
		add(vPanelDial);
	}

	private void cargarGrilla() {
		FlexTable table = new FlexTable();
		vPanelDial.setSize("350px", "90px");
		vPanelDial.add(lblTitulo);

		vPanelDial.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);

		table.setWidget(0, 0, lblNombre);
		table.setWidget(0, 1, txtSerie);

		table.setWidget(1, 0, lblMarca);
		table.setWidget(1, 1, lbxMarcas);

		table.setWidget(2, 0, lblModelo);
		table.setWidget(2, 1, lbxModelos);
		
		table.setWidget(3, 0, lblFecha);
		table.setWidget(3, 1, dpFecha);
		
		table.setWidget(4, 0, lblDesc);
		table.setWidget(4, 1, txtDescripcion);

		vPanelDial.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		vPanelDial.add(table);
		vPanelDial.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelDial.add(hPanelBotonera);

		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
			public void execute () {
				txtSerie.setFocus(true);
			}
		});
	}

	private void agregarActivoGenerico() {
		if (nombreEsValido()){
			popUp.show();
			ActivoGenerico activoGenerico = new ActivoGenerico();
			activoGenerico.setTipoActivoGenerico(new TipoActivoGenerico(padre.getTipoActivoSeleccionado().getId()));
			activoGenerico.setSerie(txtSerie.getText());
			activoGenerico.setFecha(dpFecha.getValue());
			activoGenerico.setDescripcion(txtDescripcion.getText());
			activoGenerico.setEmpresa(new Estacion(padre.getEstacionSeleccionada().getId()));
			setMarcaYModelo(activoGenerico);

			ProyectoBilpa.greetingService.guardarActivoGenerico(activoGenerico, new AsyncCallback<Boolean>() {
				public void onFailure(Throwable caught) {
					error();
				}

				public void onSuccess(Boolean result) {
					success(result, true);
				}
			});
		}
	}

	private void setMarcaYModelo(ActivoGenerico activoGenerico) {
		if (lbxMarcas.getSelectedIndex() > -1){
			activoGenerico.setMarca(new MarcaActivoGenerico(Integer.valueOf(lbxMarcas.getValue(lbxMarcas.getSelectedIndex()))));
		}

		if (lbxModelos.getSelectedIndex() > -1){
			activoGenerico.setModelo(new ModeloActivoGenerico(Integer.valueOf(lbxModelos.getValue(lbxModelos.getSelectedIndex()))));
		}
	}

	private void editarActivoGenerico() {
		if (nombreEsValido()){
			popUp.show();
			activoGenericoSeleccionado.setSerie(txtSerie.getText());
			activoGenericoSeleccionado.setFecha(dpFecha.getValue());
			activoGenericoSeleccionado.setDescripcion(txtDescripcion.getText());
			setMarcaYModelo(activoGenericoSeleccionado);
			modificar(true);
		}
	}

	public void modificar(final boolean reload) {
		ProyectoBilpa.greetingService.modificarActivoGenerico(activoGenericoSeleccionado, new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
				error();
			}

			public void onSuccess(Boolean result) {
				success(result, reload);
			}
		});
	}

	private boolean nombreEsValido() {
		return txtSerie.getText() != null && !txtSerie.getText().isEmpty();
	}

	private void error() {
		popUp.hide();
		glass.hide();

		ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar activo");
		vpu.showPopUp();
	}

	private void success(Boolean result, boolean reload) {
		if (result){
			if (reload){
				padre.cargarActivosGenericos();
			}
			popUp.hide();
			glass.hide();
			hide();
		} else {
			ValidadorPopup vpu = new ValidadorPopup(new GlassPopup(), "Info", "Ya existe un activo con el número de serie: " + txtSerie.getText());
			vpu.showPopUp();
			popUp.hide();
		}
	}
}
