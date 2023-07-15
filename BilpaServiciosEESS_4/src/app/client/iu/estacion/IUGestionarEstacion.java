package app.client.iu.estacion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import app.client.ProyectoBilpa;
import app.client.dominio.ContactoEmpresa;
import app.client.dominio.EmailEmpresa;
import app.client.dominio.Estacion;
import app.client.dominio.Persona;
import app.client.dominio.Sello;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
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

public class IUGestionarEstacion extends Composite{

	private final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	private HTML htmlTitulo = new HTML("Gestión de estaciones");

	private Persona sesion;

	private VerticalPanel vpPrincipal = new VerticalPanel();

	private HorizontalPanel hpContenedor = new HorizontalPanel();
	private DecoratorPanel decorator = new DecoratorPanel();
	private VerticalPanel vp2 = new VerticalPanel();

	private FlexTable tableDatos = new FlexTable();
	private TextBox txtNombre = new TextBox();
	private TextBox txtNroSerie = new TextBox();
	private TextBox txtRUT = new TextBox();
	private TextBox txtDireccion = new TextBox();
	private TextBox txtTelefono = new TextBox();
	private TextBox txtLocalidad = new TextBox();
	private ListBox listBoxSello = new ListBox();
	private ListBox listBoxTiempo = new ListBox();
	private ListBox listBoxDeptos = new ListBox();
	private TextBox txtZona = new TextBox();
	private TextBox tbEmail = new TextBox();

	private Label lblNombre = new Label("Nombre ");
	private Label lblNroSerie = new Label("Nro de Boca ");
	private Label lblRUT = new Label("RUT ");
	private Label lblDir = new Label("Direccion ");
	private Label lblTel = new Label("Telefono ");
	private Label lblDpto = new Label("Departamento ");
	private Label lblLoc = new Label("Localidad ");
	private Label lblSello = new Label("Sello ");
	private Label lblZona = new Label("Zona ");
	private Label lblEmp = new Label("Empleados ");
	private Label lblTiempo = new Label("Tiempo de Respuesta ");
	private Label lblEmail = new Label("Correo Electrónico ");

	private ListBox listBoxEstaciones = new ListBox();
	private ListBox listBoxEmailsEstaciones = new ListBox();
	private HorizontalPanel hpButtons = new HorizontalPanel();

	private List<Estacion> listaEstaciones = new ArrayList<Estacion>();

	private List<ContactoEmpresa> listaEmpleadosDeEmpresa = new ArrayList<ContactoEmpresa>();
	private List<ContactoEmpresa> listaEmpleadosSinAsignar = new ArrayList<ContactoEmpresa>();

	private List<ContactoEmpresa> listaEmpTEMP = new ArrayList<ContactoEmpresa>();
	private List<ContactoEmpresa> listaEmpSinAsignarTEMP = new ArrayList<ContactoEmpresa>();

	private List<Sello> listaSellos = new ArrayList<Sello>();

	private ListBox listBoxEmpEmpresaDial = new ListBox();
	private ListBox listBoxEmpSinAsignarDial = new ListBox();

	private Estacion estacionSeleccionada;

	private PopupCargando popUp = new PopupCargando("Cargando...");

	private GlassPopup glass = new GlassPopup();
	
	private List<String> mailsEstacion = new ArrayList<String>();

	Button btnAgregarMail = new Button("+", new ClickHandler() {
		public void onClick(ClickEvent event) {
			agregarMail();
		}
	});
	
	Button btnEliminarMail = new Button("-", new ClickHandler() {
		public void onClick(ClickEvent event) {
			eliminarMail();
		}
	});
	
	Button btnModificarMail = new Button("editar", new ClickHandler() {
		public void onClick(ClickEvent event) {
			modificarMail();
		}
	});
	
	Button btnGuardar = new Button("Guardar",
			new ClickHandler() {
		public void onClick(ClickEvent event) {
			if (validarCampos() && validarRUT(txtRUT.getText()))
			{
				guardar();
			}
		}
	});

	Button btnModificar = new Button("Modificar",
			new ClickHandler() {
		public void onClick(ClickEvent event) {
			if (validarCampos() && validarRUTMod(txtRUT.getText() ))
			{
				modificarEstacion();
			}
		}

	});

	private boolean validarRUTMod(String rut) {

		for (Estacion e : listaEstaciones) {
			if (e.getRut().equals(rut) && estacionSeleccionada != null && estacionSeleccionada.getId() != e.getId()) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Notificación", "Ya existe una empresa con ese RUT");
				vpu.showPopUp();
				return false;
			}
		}

		if (rut.length() == 12) {
			try {
				Double.valueOf(rut);	
			} catch(Exception e) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "El RUT solo debe contener numeros");
				vpu.showPopUp();
				return false;
			}
		} else {
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "El RUT debe ser de 12 caracteres");
			vpu.showPopUp();
			return false;
		}
		return true;

	}

	private void agregarMail() {
		
		String mail = tbEmail.getText();
		if(mail != null && !mail.isEmpty()) {
			mailsEstacion.add(mail);
			listBoxEmailsEstaciones.clear();
			for(String m : mailsEstacion) {
				listBoxEmailsEstaciones.addItem(m);
			}
		} else {
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "No puede agregar un mail vacío.");
			vpu.showPopUp();
		}
		
	}
	
	private void eliminarMail() {
		tbEmail.setText("");
		int selectedIndex = listBoxEmailsEstaciones.getSelectedIndex();
		mailsEstacion.remove(selectedIndex);
		listBoxEmailsEstaciones.clear();
		for(String m : mailsEstacion) {
			listBoxEmailsEstaciones.addItem(m);
		}
	}
	
	private void modificarMail() {
		
		String mail = tbEmail.getText();
		if(mail != null && !mail.isEmpty()) {
			int selectedIndex = listBoxEmailsEstaciones.getSelectedIndex();
			mailsEstacion.remove(selectedIndex);
			mailsEstacion.add(mail);
			listBoxEmailsEstaciones.clear();
			for(String m : mailsEstacion) {
				listBoxEmailsEstaciones.addItem(m);
			}
		} else {
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "No puede modificar el mail dejandolo vacío.");
			vpu.showPopUp();
		}
		
	}

	Button btnCancelar = new Button("Limpiar",
			new ClickHandler() {
		public void onClick(ClickEvent event) {
			limpiarCampos();
		}
	});

	public VerticalPanel getVpPrincipal(){
		return this.vpPrincipal;
	}

	public IUGestionarEstacion(Persona persona){
		this.sesion = persona;
		setearWidgets();			//Setea el tamano de los Widgets.
		cargarPanelesConWidgets();	//Agrega los Widget a los paneles.
		cargarLtBoxEmpresas();
		cargarEmpSinAsig();
		cargarSellos();
		cargarDeptos();
		cargarTiempos();
		color();

	}

	private void cargarTiempos() {
		for (int i = 0; i < 73; i++) {
				listBoxTiempo.addItem(i + "", i + "");
		}
		listBoxTiempo.setItemText(0, "N/A");
		listBoxTiempo.setSelectedIndex(48);
	}

	private void setearWidgets() {
		vpPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vpPrincipal.setSpacing(5);
		vp2.setSpacing(10);
		hpButtons.setSpacing(10);

		tableDatos.setCellSpacing(5);
		tableDatos.setCellPadding(2);
		//tableDatos.setBorderWidth(1);

		txtNombre.setWidth("265");
		txtNroSerie.setWidth("265");
		txtRUT.setWidth("265");
		txtDireccion.setWidth("265");
		txtTelefono.setWidth("265");
		txtLocalidad.setWidth("265");
		listBoxSello.setWidth("265");
		listBoxDeptos.setWidth("265");
		listBoxTiempo.setWidth("265");
		txtZona.setWidth("265");
		tbEmail.setWidth("170");

		listBoxEstaciones.setWidth("330");
		listBoxEstaciones.setVisibleItemCount(18);
		
		listBoxEmailsEstaciones.setWidth("265");
		listBoxEmailsEstaciones.setVisibleItemCount(3);

		btnGuardar.setWidth("100");
		btnModificar.setWidth("100");
		btnCancelar.setWidth("100");

	}

	private void cargarPanelesConWidgets() {
		vpPrincipal.add(htmlTitulo);

		vpPrincipal.add(hpContenedor);

		hpContenedor.add(decorator);
		hpContenedor.add(vp2);

		decorator.add(tableDatos);

		tableDatos.setWidget(0,0,lblNombre);
		tableDatos.setWidget(1,0,lblNroSerie);
		tableDatos.setWidget(2,0,lblRUT);
		tableDatos.setWidget(3,0,lblDir);
		tableDatos.setWidget(4,0,lblTel);
		tableDatos.setWidget(5,0,lblLoc);
		tableDatos.setWidget(6,0,lblDpto);
		tableDatos.setWidget(7,0,lblSello);
		tableDatos.setWidget(8,0,lblZona);
		tableDatos.setWidget(9,0,lblTiempo);
		tableDatos.setWidget(10,0,lblEmail);
		tableDatos.setWidget(12,0,lblEmp);

		tableDatos.setWidget(0, 1, txtNombre);
		tableDatos.setWidget(1, 1, txtNroSerie);
		tableDatos.setWidget(2, 1, txtRUT);
		tableDatos.setWidget(3, 1, txtDireccion);
		tableDatos.setWidget(4, 1, txtTelefono);
		tableDatos.setWidget(5, 1, txtLocalidad);
		tableDatos.setWidget(6, 1, listBoxDeptos);
		tableDatos.setWidget(7, 1, listBoxSello);
		tableDatos.setWidget(8, 1, txtZona);
		tableDatos.setWidget(9, 1, listBoxTiempo);
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(tbEmail);
		hp.add(btnAgregarMail);
		hp.add(btnEliminarMail);
		hp.add(btnModificarMail);
		
		tableDatos.setWidget(10, 1, hp);
		tableDatos.setWidget(11, 1, listBoxEmailsEstaciones);
		tableDatos.getCellFormatter().setAlignment(13, 1, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_MIDDLE);

		listBoxEstaciones.addChangeHandler(new ChangeHandler(){
			public void onChange(ChangeEvent event) {
				mostrarDatosEstacionSeleccionada();				
			}
		});
		
		listBoxEmailsEstaciones.addChangeHandler(new ChangeHandler(){
			public void onChange(ChangeEvent event) {
				int i = listBoxEmailsEstaciones.getSelectedIndex();
				cargarMailSeleccionada(i);
			}
		});	

		vp2.add(listBoxEstaciones);
		vp2.add(hpButtons);

		hpButtons.add(btnGuardar);
		hpButtons.add(btnModificar);
		hpButtons.add(btnCancelar);
	}	

	protected void cargarMailSeleccionada(int i) {
		String m = mailsEstacion.get(i);
		tbEmail.setText(m);
	}

	private void cargarLtBoxEmpresas() {
		listBoxEstaciones.clear();
		listaEstaciones.clear();
		popUp.show();
		ProyectoBilpa.greetingService.obtenerEmpresas(new AsyncCallback<ArrayList<Estacion>>() {

			public void onFailure(Throwable caught) {
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener empresas: " + caught.getMessage());
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<Estacion> result) {
				Estacion auxiliar;
				for (int i=0; i < result.size(); i++){
					auxiliar = result.get(i);
					listBoxEstaciones.addItem(auxiliar.toString(),String.valueOf(auxiliar.getId()));
					listaEstaciones.add(auxiliar);
				}
				popUp.hide();
			}
		});		
	}

	private void color() {
		htmlTitulo.setStyleName("Titulo");
	}

	private void mostrarDatosEstacionSeleccionada() {
		//		limpiarCampos();
		int sel = listBoxEstaciones.getSelectedIndex();
		estacionSeleccionada = listaEstaciones.get(sel);
		if(estacionSeleccionada != null){
			cargarDatosEstacionSel();
		}
	}	

	private void cargarDatosEstacionSel( ){
		setearTextBoxes(estacionSeleccionada);
		cargarEmails(estacionSeleccionada);
	}


	private void cargarEmails(Estacion estacionSeleccionada) {
		mailsEstacion = estacionSeleccionada.getEmailList();
		listBoxEmailsEstaciones.clear();
		for(String e : mailsEstacion) {
			listBoxEmailsEstaciones.addItem(e);
		}
	}

	private void setearTextBoxes(Estacion estacion) {
		txtNombre.setText(estacion.getNombre());
		txtLocalidad.setText(estacion.getLocalidad());
		txtDireccion.setText(estacion.getDireccion());
		txtNroSerie.setText(estacion.getNumeroSerie());
		txtRUT.setText(estacion.getRut());
		tbEmail.setText("");
		if (estacion.getSello() != null)
		{
			for (int i = 0 ; i < listBoxSello.getItemCount() ; i ++)
			{
				if (Integer.valueOf(listBoxSello.getValue(i)) == estacion.getSello().getId())
				{
					listBoxSello.setItemSelected(i, true);
				}
			}
		}
		txtTelefono.setText(estacion.getTelefono());
		txtZona.setText(estacion.getZona()+"");

		for (int i = 0 ; i < listBoxDeptos.getItemCount() ; i ++) {
			String dpto = listBoxDeptos.getItemText(i);

			if (dpto.equalsIgnoreCase(estacion.getDepartamento())) {
				listBoxDeptos.setItemSelected(i, true);
			}
		}
		int tiempo = estacion.getTiempoRespuesta();
		listBoxTiempo.setSelectedIndex(tiempo);

	}

	private void limpiarCampos() {
		estacionSeleccionada = null;
		txtNombre.setText("");
		txtNroSerie.setText("");
		txtRUT.setText("");
		txtDireccion.setText("");
		txtTelefono.setText("");
		txtLocalidad.setText("");
		listBoxSello.setItemSelected(0, true);
		txtZona.setText("");
		listBoxEstaciones.setItemSelected(0, false);
		listBoxTiempo.setItemSelected(48, true);
		tbEmail.setText("");
		listBoxEmailsEstaciones.clear();
		iniciarListas();
	}

	private boolean validarCampos() {

		String nombre = txtNombre.getText();
		String nroSerie = txtNroSerie.getText();
		String rut = txtRUT.getText();
		String direccion = txtDireccion.getText();
		String telefono = txtTelefono.getText();
		String localidad = txtLocalidad.getText();

		if(nombre.equalsIgnoreCase("") || nombre.trim().equalsIgnoreCase("")){
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "El nombre no puede ser vacio");
			vpu.showPopUp();
			return false;
		}

		if(nroSerie.equalsIgnoreCase("") || nroSerie.trim().equalsIgnoreCase("")){
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "El Nro de Serie no puede estar vacio");
			vpu.showPopUp();
			return false;
		}

		if(rut.equalsIgnoreCase("") || rut.trim().equalsIgnoreCase("")){
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "El RUT no puede ser vacio");
			vpu.showPopUp();
			return false;
		}

		if(direccion.trim().equalsIgnoreCase("") || direccion.trim().equalsIgnoreCase("")){
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "La direccion no puede estar vacia");
			vpu.showPopUp();
			return false;
		}

		if(telefono.equalsIgnoreCase("") || telefono.trim().equalsIgnoreCase("")){
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "El telefono no puede ser vacio");
			vpu.showPopUp();
			return false;
		}

		if(localidad.equalsIgnoreCase("") || localidad.trim().equalsIgnoreCase("") ){
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "La localidad no puede estar vacia");
			vpu.showPopUp();
			return false;
		}

		try {
			int zona = Integer.valueOf(txtZona.getText());
			if (!(zona > 0 && zona < 1000)) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "La zona debe ser un numero entre el 1 y 1000");
				vpu.showPopUp();
				return false;
			}
		} catch (Exception e) {
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "La zona debe ser un numero entre 1 y 1000");
			vpu.showPopUp();
			return false;
		}	

		RegExp regExp = RegExp.compile(PATTERN_EMAIL);
		MatchResult matcher = regExp.exec(tbEmail.getText());
		boolean matchFound = matcher != null;
		if(!matchFound && !tbEmail.getText().isEmpty()) {//se permite no tener email
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Dirección de correo inválida.");
			vpu.showPopUp();
			return false;
		}

		return true;
	}

	private boolean validarRUT(String rut) {

		for (Estacion e : listaEstaciones) {
			if (e.getRut().equals(rut)) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Ya existe una empresa con ese RUT");
				vpu.showPopUp();
				return false;
			}
		}
		if (rut.length() == 12) {

			try {
				Double.valueOf(rut);	
			} catch(Exception e) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "El RUT solo debe contener numeros");
				vpu.showPopUp();
				return false;
			}
		} else {
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "El RUT debe ser de 12 caracteres");
			vpu.showPopUp();
			return false;
		}
		return true;		
	}

	private void guardar() {
		Estacion e = new Estacion();
		e.setNombre(txtNombre.getText());
		e.setNumeroSerie(txtNroSerie.getText());
		e.setDireccion(txtDireccion.getText());
		e.setTelefono(txtTelefono.getText());

		Sello s = buscarSello(Integer.valueOf(listBoxSello.getValue(listBoxSello.getSelectedIndex())));
		if (s != null) {
			e.setSello(s);			
		}
		e.setZona(Integer.valueOf(txtZona.getText()));
		e.setRut(txtRUT.getText());
		e.setLocalidad(txtLocalidad.getText());
		e.setDepartamento(listBoxDeptos.getItemText(listBoxDeptos.getSelectedIndex()));
		
		Set<EmailEmpresa> listaEE = new HashSet<EmailEmpresa>();
		for(String m : mailsEstacion) {
			EmailEmpresa ee = new EmailEmpresa();
			ee.setEmail(m);	
			listaEE.add(ee);
		}
		e.setListaEmail(listaEE);

		if(listBoxTiempo.getSelectedIndex() == 0) {
			e.setTiempoRespuesta(0);
		} else {
			String tiempo = listBoxTiempo.getItemText(listBoxTiempo.getSelectedIndex());
			e.setTiempoRespuesta(Integer.valueOf(tiempo));
		}

		validarGuardar(e);
	}

	private void validarGuardar(final Estacion estacion) {

		ProyectoBilpa.greetingService.validarNuevaEstacion(estacion, new AsyncCallback<Boolean>() {
			public void onSuccess(Boolean result) {
				if (result) {
					guardarValidado(estacion);
				} else {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Ya existe un Estacion con ese Nombre o Nro de Serie.");
					vpu.showPopUp();
				}
			}

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al validar la Estacion" + caught.getMessage());
				vpu.showPopUp();
			}
		});

	}

	private void guardarValidado(final Estacion estacion) {
		ProyectoBilpa.greetingService.agregarEstacion(estacion, new AsyncCallback<Boolean>() {
			public void onSuccess(Boolean result) {
				if (result) {
					limpiarCampos();
					cargarLtBoxEmpresas();

				} else {
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar la Estacion");
					vpu.showPopUp();
				}
			}

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al guardar la Estacion" + caught.getMessage());
				vpu.showPopUp();
			}
		});

	}

	private void cargarEmpEmpresa( ListBox listBoxEmpEmpresa) {
		listBoxEmpEmpresa.clear();
		for (ContactoEmpresa ce : listaEmpleadosDeEmpresa) {
			listBoxEmpEmpresa.addItem(ce.toString(), String.valueOf(ce.getId()));
		}
	}

	private void cargarEmpEmpresaSinAsignar( ListBox listBoxEmpSinAsignar) {
		listBoxEmpSinAsignar.clear();
		for (ContactoEmpresa ce : listaEmpleadosSinAsignar) {
			listBoxEmpSinAsignar.addItem(ce.toString(), String.valueOf(ce.getId()));
		}
	}

	private void cargarSellos() {
		ProyectoBilpa.greetingService.obtenerSellos(new AsyncCallback<ArrayList<Sello>>() {

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener sellos" + caught.getMessage());
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<Sello> result) {
				Sello auxiliar;
				for (int i=0; i < result.size(); i++){
					auxiliar = result.get(i);
					listaSellos.add(auxiliar);
					listBoxSello.addItem(auxiliar.getNombre(), auxiliar.getId()+"");
				}			
				if(listBoxSello.getItemCount() > 0)
				{
					listBoxSello.setSelectedIndex(1);
				}
			}
		});	

	}

	private void cargarEmpSinAsig() {
		ProyectoBilpa.greetingService.obtenerEmpleadosSinEmpresa(new AsyncCallback<ArrayList<ContactoEmpresa>>() {

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener empleados sin asignar" + caught.getMessage());
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<ContactoEmpresa> result) {
				ContactoEmpresa auxiliar;
				for (int i=0; i < result.size(); i++){
					auxiliar = result.get(i);
					listaEmpleadosSinAsignar.add(auxiliar);
				}			
			}
		});		
	}

	private void actualizarEstacion() {
		// TODO estacionSeleccionada
		ProyectoBilpa.greetingService.actualizarEstacion(estacionSeleccionada, new AsyncCallback<Boolean>() {

			public void onSuccess(Boolean result) {
				if (result)
				{
					//	limpiarCampos();
					//	cargarLtBoxEmpresas();
					actualizarUsuariosSinAsignarParche();
				}else{
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al modificar la Estacion");
					vpu.showPopUp();
				}
			}

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al modificar el usuario: " + caught.getMessage());
				vpu.showPopUp();
			}
		});
	}


	private void actualizarUsuariosSinAsignarParche() {
		ProyectoBilpa.greetingService.actualizarUsuariosSinAsignar(listaEmpleadosSinAsignar, new AsyncCallback<Boolean>() {

			public void onSuccess(Boolean result) {
				if (result) {
					// ValidadorPopup vpu = new ValidadorPopup(glass, "Notificación", "Se ha modificado la Estacion");
					// vpu.showPopUp();
					limpiarCampos();
					cargarLtBoxEmpresas();
				}else{
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al modificar la Estacion");
					vpu.showPopUp();
				}
			}

			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al modificar el usuario: " + caught.getMessage());
				vpu.showPopUp();
			}
		});
	}
	private void iniciarListas() {
		listaEmpTEMP.clear();
		listaEmpSinAsignarTEMP.clear();

		for (ContactoEmpresa ce : listaEmpleadosDeEmpresa) {
			listaEmpTEMP.add(ce.copiar());
		}

		for (ContactoEmpresa ce : listaEmpleadosSinAsignar) {
			listaEmpSinAsignarTEMP.add(ce.copiar());
		}
	}

	private void cargarListBoxEmpleados() {
		cargarEmpEmpresa(listBoxEmpEmpresaDial);
		cargarEmpEmpresaSinAsignar(listBoxEmpSinAsignarDial);
	}


	private void modificarEstacion() {
		estacionSeleccionada.setNombre(txtNombre.getText());
		estacionSeleccionada.setNumeroSerie(txtNroSerie.getText());
		estacionSeleccionada.setRut(txtRUT.getText());
		estacionSeleccionada.setDireccion(txtDireccion.getText());
		estacionSeleccionada.setTelefono(txtTelefono.getText());
		estacionSeleccionada.setLocalidad(txtLocalidad.getText());
		estacionSeleccionada.setDepartamento(listBoxDeptos.getItemText(listBoxDeptos.getSelectedIndex()));

		Sello s = buscarSello(Integer.valueOf(listBoxSello.getValue(listBoxSello.getSelectedIndex())));
		if (s != null) {
			estacionSeleccionada.setSello(s);			
		}

		estacionSeleccionada.setZona(Integer.valueOf(txtZona.getText()));

		if(listBoxTiempo.getSelectedIndex() == 0) {
			estacionSeleccionada.setTiempoRespuesta(0);
		} else {
			String tiempo = listBoxTiempo.getItemText(listBoxTiempo.getSelectedIndex());
			estacionSeleccionada.setTiempoRespuesta(Integer.valueOf(tiempo));
		}

		Set<EmailEmpresa> listaEE = new HashSet<EmailEmpresa>();
		for(String m : mailsEstacion) {
			EmailEmpresa ee = new EmailEmpresa();
			ee.setEmail(m);	
			listaEE.add(ee);
		}
		estacionSeleccionada.setListaEmail(listaEE);

		actualizarEstacion();
	}

	private Sello buscarSello(int value) {
		for (Sello s : listaSellos)
		{
			if (s.getId() == value)
			{
				return s;
			}
		}
		return null;			
	}

	private void cargarDeptos(){
		listBoxDeptos.addItem("Artigas","1");
		listBoxDeptos.addItem("Canelones","2");
		listBoxDeptos.addItem("Cerro Largo","3");
		listBoxDeptos.addItem("Colonia","4");
		listBoxDeptos.addItem("Durazno","5");
		listBoxDeptos.addItem("Flores","6");
		listBoxDeptos.addItem("Florida","7");
		listBoxDeptos.addItem("Lavalleja","8");
		listBoxDeptos.addItem("Maldonado","9");
		listBoxDeptos.addItem("Montevideo","10");
		listBoxDeptos.addItem("Paysandú","11");
		listBoxDeptos.addItem("Río Negro","12");
		listBoxDeptos.addItem("Rivera","13");
		listBoxDeptos.addItem("Rocha","14");
		listBoxDeptos.addItem("Salto","15");
		listBoxDeptos.addItem("San Jose","16");
		listBoxDeptos.addItem("Soriano","17");
		listBoxDeptos.addItem("Tacuarembó","18");
		listBoxDeptos.addItem("Treinta y Tres","19");	

		listBoxDeptos.setSelectedIndex(9);
	}

}
