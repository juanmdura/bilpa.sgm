package app.client.iu.usuario;

import java.util.ArrayList;

import app.client.ProyectoBilpa;
import app.client.dominio.Administrador;
import app.client.dominio.Administrativo;
import app.client.dominio.ContactoEmpresa;
import app.client.dominio.Encargado;
import app.client.dominio.Persona;
import app.client.dominio.Tecnico;
import app.client.utilidades.UtilUI;
import app.client.utilidades.utilObjects.PopupCargando;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class IUGestionarUsuario extends Composite {

	private ListBox listBoxListaUsuarios = new ListBox();
	private TextBox txtNombre = new TextBox();
	private TextBox txtApellido = new TextBox();
	private TextBox txtNombreUsuario = new TextBox();
	private PasswordTextBox txtClave = new PasswordTextBox();
	private PasswordTextBox txtClaveRepetida = new PasswordTextBox();
	private TextBox txtEmail = new TextBox();
	private TextBox txtTelefono = new TextBox();
	private String masculino = "Masculino";
	private RadioButton rbMasculino = new RadioButton("Sexo", masculino);
	private String femenino = "Femenino";
	private RadioButton rbFemenino = new RadioButton("Sexo", femenino);
	private ListBox listaRoles = new ListBox();
	private HorizontalPanel hSexo = new HorizontalPanel();
	private VerticalPanel vSexoMasculino = new VerticalPanel();
	private VerticalPanel vSexoFemenino = new VerticalPanel();
	private Persona sesion;
	private HTML htmlTitulo = new HTML("Gestión de usuarios");
	private VerticalPanel vpPrincipal = new VerticalPanel();
	private DecoratorPanel decorator = new DecoratorPanel();
	private VerticalPanel vListaUsuarios = new VerticalPanel();
	private HorizontalPanel hPrincipal = new HorizontalPanel();
	private FlexTable tableDatosUsuario = new FlexTable();
	private HorizontalPanel hBotones = new HorizontalPanel();
	private ArrayList<Persona> usuarios = new ArrayList<Persona>();
	private boolean cambioDeClave = false;

	private Label lblNombre = new Label("Nombre ");
	private Label lblApellido = new Label("Apellido ");
	private Label lblNomU = new Label("Nombre de Usuario ");
	private Label lblCont = new Label("Contraseña ");
	private Label lblConfCont = new Label("Confirmar contraseña ");
	private Label lblEmail = new Label("Email ");
	private Label lblTel = new Label("Telefono ");
	private Label lblSexo = new Label("Sexo ");
	private Label lblRol = new Label("Rol ");
	
	private PopupCargando popUp = new PopupCargando("Cargando...");
	
	Button btnGuardar = new Button("Guardar", new ClickHandler() {
		public void onClick(ClickEvent event) {
			validarTodo();
		}
	});

	Button btnModificar = new Button("Modificar", new ClickHandler() {
		public void onClick(ClickEvent event) {
			// validarTodo();
			modificarUsuario();
		}
	});

	Button btnCancelar = new Button("Limpiar", new ClickHandler() {
		public void onClick(ClickEvent event) {
			limpiarCampos();
		}
	});

	public VerticalPanel getVpPrincipal() {
		return this.vpPrincipal;
	}

	public IUGestionarUsuario(Persona persona) {
		sesion = persona;
		setearWidgets(); // Setea el tamano de los Widgets.
		cargarPanelesConWidgets(); // Agrega los Widget a los paneles.
		cargarLtBoxUsuarios();
		cargarRoles();
		color();
	}

	private void color() {
		htmlTitulo.setStyleName("Titulo");

		lblNombre.setStyleName("Negrita");
		lblApellido.setStyleName("Negrita");
		lblNomU.setStyleName("Negrita");
		lblCont.setStyleName("Negrita");
		lblConfCont.setStyleName("Negrita");
		lblEmail.setStyleName("Negrita");
		lblTel.setStyleName("Negrita");
		lblSexo.setStyleName("Negrita");
		lblRol.setStyleName("Negrita");
	}

	private void setearWidgets() {
		this.btnGuardar.setWidth("100");
		this.btnModificar.setWidth("100");
		this.btnCancelar.setWidth("100");

		this.listBoxListaUsuarios.setWidth("265");

		this.listaRoles.setWidth("265");
		this.vpPrincipal.setSpacing(5);

		this.vpPrincipal.setWidth("800px");
		this.vpPrincipal.setSpacing(5);

		tableDatosUsuario.setCellSpacing(5);
		tableDatosUsuario.setCellPadding(2);

		txtApellido.setWidth("265");
		txtClave.setWidth("265");
		txtClaveRepetida.setWidth("265");
		txtEmail.setWidth("265");
		txtNombre.setWidth("265");
		txtTelefono.setWidth("265");
		txtNombreUsuario.setWidth("265");
		// this.hSexo.setHeight("20");

		tableDatosUsuario.setWidget(0, 0, lblNombre);
		tableDatosUsuario.setWidget(1, 0, lblApellido);
		tableDatosUsuario.setWidget(2, 0, lblNomU);
		tableDatosUsuario.setWidget(3, 0, lblCont);
		tableDatosUsuario.setWidget(4, 0, lblConfCont);
		tableDatosUsuario.setWidget(5, 0, lblEmail);
		tableDatosUsuario.setWidget(6, 0, lblTel);
		tableDatosUsuario.setWidget(7, 0, lblSexo);
		tableDatosUsuario.setWidget(8, 0, lblRol);

		tableDatosUsuario.setWidget(0, 1, this.txtNombre);
		tableDatosUsuario.setWidget(1, 1, this.txtApellido);
		tableDatosUsuario.setWidget(2, 1, this.txtNombreUsuario);
		tableDatosUsuario.setWidget(3, 1, this.txtClave);
		tableDatosUsuario.setWidget(4, 1, this.txtClaveRepetida);
		tableDatosUsuario.setWidget(5, 1, this.txtEmail);
		tableDatosUsuario.setWidget(6, 1, this.txtTelefono);
		tableDatosUsuario.setWidget(7, 1, this.hSexo);
		// tableDatosUsuario.setWidget(6, 1, this.rbMasculino);
		tableDatosUsuario.setWidget(8, 1, this.listaRoles);

		this.listBoxListaUsuarios.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				cargarDatosUsuarioSeleccionado();

			}
		});
		this.rbMasculino.setValue(true);
		vListaUsuarios.setSpacing(10);
		vListaUsuarios
		.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hBotones.setSpacing(10);
		listBoxListaUsuarios.setWidth("320");
		listBoxListaUsuarios.setVisibleItemCount(17);
	}

	private void cargarPanelesConWidgets() {
		this.hBotones.add(this.btnGuardar);
		this.hBotones.add(this.btnModificar);
		this.hBotones.add(this.btnCancelar);
		this.vSexoFemenino.add(this.rbFemenino);
		this.vSexoMasculino.add(this.rbMasculino);
		this.hSexo.add(this.vSexoFemenino);
		this.hSexo.add(this.vSexoMasculino);

		this.vpPrincipal
		.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		this.vpPrincipal.add(htmlTitulo);
		agregarWidgetsAPaneles();
	}

	private void agregarWidgetsAPaneles() {
		this.decorator.add(this.tableDatosUsuario);
		this.vListaUsuarios.add(this.listBoxListaUsuarios);
		this.vListaUsuarios.add(this.hBotones);
		this.hPrincipal.add(this.decorator);
		this.hPrincipal.add(this.vListaUsuarios);
		this.vpPrincipal.add(this.hPrincipal);

	}

	private void cargarLtBoxUsuarios() {
		popUp.show();
		usuarios.clear();
		listBoxListaUsuarios.clear();
		ProyectoBilpa.greetingService
		.obtenerTodosLosUsuarios(new AsyncCallback<ArrayList<Persona>>() {

			public void onFailure(Throwable caught) {
				popUp.hide();
				Window.alert("Error al obtener todos los usuarios");
			}

			public void onSuccess(ArrayList<Persona> result) {
				Persona auxiliar;
				for (int i = 0; i < result.size(); i++) {
					auxiliar = result.get(i);
					listBoxListaUsuarios.addItem(auxiliar.toString(),
							String.valueOf(auxiliar.getId()));
					usuarios.add(auxiliar);
				}
				popUp.hide();
			}
		});

	}

	private boolean validarClavesIguales() {
		String clave1 = this.txtClave.getText();
		String clave2 = this.txtClaveRepetida.getText();
		if (clave1.equals(clave2)) {
			return true;
		}
		return false;
	}

	private boolean validarCampos() {
		String nombre = this.txtNombre.getText();
		String apellido = this.txtApellido.getText();
		String email = this.txtEmail.getText();
		String usuario = this.txtNombreUsuario.getText();
		String clave = this.txtClave.getText();
		if (nombre.equalsIgnoreCase("")) {
			Window.alert("El nombre no puede ser vacio");
			return false;
		}
		if (nombre.trim().equalsIgnoreCase("")) {
			Window.alert("El nombre no puede ser vacio");
			return false;
		}
		if (apellido.equalsIgnoreCase("")) {
			Window.alert("El apellido no puede ser vacio");
			return false;
		}
		if (apellido.trim().equalsIgnoreCase("")) {
			Window.alert("El apellido no puede ser vacio");
			return false;
		}
		if (email.equalsIgnoreCase("")) {
			Window.alert("El email no puede ser vacio");
			return false;
		}
		if (email.trim().equalsIgnoreCase("")) {
			Window.alert("El email no puede ser vacio");
			return false;
		}
		if (usuario.equalsIgnoreCase("")) {
			Window.alert("El nombre de usuario no puede ser vacio");
			return false;
		}
		if (usuario.trim().equalsIgnoreCase("")) {
			Window.alert("El nombre de usuario no puede ser vacio");
			return false;
		}
		if (clave.equalsIgnoreCase("")) {
			Window.alert("La clave no puede ser vacia");
			return false;
		}
		if (clave.trim().equalsIgnoreCase("")) {
			Window.alert("La clave no puede ser vacia");
			return false;
		}
		if (!validarClavesIguales()) {
			Window.alert("Las claves ingresadas deben ser iguales");
			return false;
		}
		return true;
	}

	public void validarNombreDeUsuarioDisponible(String usuario) {

		ProyectoBilpa.greetingService.validarNombreDeUsuarioDisponible(usuario,
				new AsyncCallback<Boolean>() {

			public void onSuccess(Boolean result) {
				if (result) {
					guardar();
				} else {
					DialogBox yaExisteNombre = UtilUI
					.dialogBoxError("Ya existe un usuario con ese nombre de usuario");
					yaExisteNombre.show();
					yaExisteNombre.center();
				}
			}

			public void onFailure(Throwable caught) {
			}
		});
	}

	/*
	 * public boolean validarEmail(String correo) { Pattern pat = null; Matcher
	 * mat = null; pat =Pattern.compile(
	 * "^([0-9a-zA-Z]([_.w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-w]*[0-9a-zA-Z].)+([a-zA-Z]{2,9}.)+[a-zA-Z]{2,3})$"
	 * ); mat = pat.matcher(correo); if (mat.find()) { return true; }else{
	 * return false; } }
	 */
	public void validarTodo() {
		if (this.validarCampos()) {
			this.validarNombreDeUsuarioDisponible(this.txtNombreUsuario
					.getText());
		}
	}

	private void guardar() {
		Persona persona = this.crearPersona();
		if (persona != null) {
			ProyectoBilpa.greetingService.agregarUsuario(persona,
					new AsyncCallback<Boolean>() {

				public void onSuccess(Boolean result) {
					if (result) {
						limpiarCampos();
						cargarLtBoxUsuarios();
					} else {
						DialogBox yaExisteNombre = UtilUI
						.dialogBoxError("Error al guardar el usuario");
						yaExisteNombre.show();
						yaExisteNombre.center();
					}
				}

				public void onFailure(Throwable caught) {
				}
			});
		}
	}

	private Persona crearPersona() {
		String nombre = this.txtNombre.getText();
		String apellido = this.txtApellido.getText();
		String email = this.txtEmail.getText();
		String usuario = this.txtNombreUsuario.getText();
		String clave = this.txtClave.getText();
		String telefono = this.txtTelefono.getText();
		char sexo = this.sexo();

		if (this.listaRoles.getSelectedIndex() == 0) {
			Administrador admin = new Administrador(nombre, apellido, email,
					usuario, clave, sexo, telefono);
			return admin;
		}

		if (this.listaRoles.getSelectedIndex() == 1) {
			Administrativo admin = new Administrativo(nombre, apellido, email,
					usuario, clave, sexo, telefono);
			return admin;
		}

		if (this.listaRoles.getSelectedIndex() == 2) {
			Tecnico tecnico = new Tecnico(nombre, apellido, email, usuario,
					clave, sexo, telefono);
			return tecnico;
		}

		if (this.listaRoles.getSelectedIndex() == 3) {
			ContactoEmpresa cont = new ContactoEmpresa(nombre, apellido, email,
					usuario, clave, sexo, telefono);
			return cont;
		}

		if (this.listaRoles.getSelectedIndex() == 4) {
			Encargado encargado = new Encargado(nombre, apellido, email,
					usuario, clave, sexo, telefono);
			return encargado;
		}
		return null;
	}

	private char sexo() {
		if (this.rbFemenino.getValue() == true) {
			return 'f';
		} else {
			return 'm';
		}
	}

	private void cargarDatosUsuarioSeleccionado() {
		int id = this.listBoxListaUsuarios.getSelectedIndex();
		Persona persona = usuarios.get(id);
		if (persona != null) {
			cargarDatosUsuarioEncontrado(persona);
		}
	}

	private void cargarDatosUsuarioEncontrado(Persona persona) {
		if (persona != null) {
			this.txtApellido.setText(persona.getApellido());
			this.txtEmail.setText(persona.getEmail());
			this.txtNombre.setText(persona.getNombre());
			this.txtNombreUsuario.setText(persona.getNombreDeUSuario());
			this.txtTelefono.setText(persona.getTelefono());
			this.cargarRadioButtonSexo(persona.getSexo());
			cargarRol(persona.getRol());
		}
	}

	private void cargarRadioButtonSexo(char sexo) {
		if (sexo == 'F' || sexo == 'f') {
			this.rbFemenino.setValue(true);
		} else {
			this.rbMasculino.setValue(true);
		}
	}

	private void cargarRoles() {
		this.listaRoles.addItem("Administrador", "1");
		this.listaRoles.addItem("Administrativo", "2");
		this.listaRoles.addItem("Técnico", "3");
		this.listaRoles.addItem("Contacto Empresa", "4");
		this.listaRoles.addItem("Encargado de Mantenimiento", "5");
	}

	public void limpiarCampos() {
		this.txtApellido.setText("");
		this.txtClave.setText("");
		this.txtClaveRepetida.setText("");
		this.txtEmail.setText("");
		this.txtNombre.setText("");
		this.txtNombreUsuario.setText("");
		this.txtTelefono.setText("");
		this.rbMasculino.setValue(true);
		this.listaRoles.setItemSelected(0, false);
		this.cambioDeClave = false;
	}

	private void modificarUsuario() {
		if (this.validarCamposModificacion()) {
			String usuario = this.txtNombreUsuario.getText();
			int id = this.listBoxListaUsuarios.getSelectedIndex();
			Persona persona = usuarios.get(id);
			if (usuario.equalsIgnoreCase(persona.getNombreDeUSuario())) {
				Persona p = crearPersonaModificada();
				if (p != null) {
					ProyectoBilpa.greetingService.modificarPersona(p,
							cambioDeClave, new AsyncCallback<Boolean>() {

						public void onSuccess(Boolean result) {
							if (result) {
								limpiarCampos();
								cargarLtBoxUsuarios();
							} else {
								DialogBox yaExisteNombre = UtilUI
								.dialogBoxError("Error al modificar el usuario");
								yaExisteNombre.show();
								yaExisteNombre.center();
							}
						}

						public void onFailure(Throwable caught) {
						}
					});
				}
			} else {
				ProyectoBilpa.greetingService.validarNombreDeUsuarioDisponible(
						usuario, new AsyncCallback<Boolean>() {
							public void onSuccess(Boolean result) {
								if (result) {
									Persona p = crearPersonaModificada();
									if (p != null) {
										modificar(p);
									}

								} else {
									DialogBox yaExisteNombre = UtilUI
									.dialogBoxError("Ya existe un usuario con ese nombre de usuario");
									yaExisteNombre.show();
									yaExisteNombre.center();
								}
							}

							public void modificar(Persona p) {
								if (p != null) {
									ProyectoBilpa.greetingService.modificarPersona(p,cambioDeClave,new AsyncCallback<Boolean>() {

												public void onSuccess(
														Boolean result) {
													if (result) {
														limpiarCampos();
														cargarLtBoxUsuarios();
													} else {
														DialogBox yaExisteNombre = UtilUI
														.dialogBoxError("Error al modificar el usuario");
														yaExisteNombre
														.show();
														yaExisteNombre
														.center();
													}
												}

												public void onFailure(
														Throwable caught) {
												}
											});
								}

							}

							public void onFailure(Throwable caught) {
							}
						});
			}

		}
	}

	private boolean validarCamposModificacion() {
		String nombre = this.txtNombre.getText();
		String apellido = this.txtApellido.getText();
		String email = this.txtEmail.getText();
		String usuario = this.txtNombreUsuario.getText();

		if (nombre.equalsIgnoreCase("")) {
			Window.alert("El nombre no puede ser vacio");
			return false;
		}
		if (nombre.trim().equalsIgnoreCase("")) {
			Window.alert("El nombre no puede ser vacio");
			return false;
		}
		if (apellido.equalsIgnoreCase("")) {
			Window.alert("El apellido no puede ser vacio");
			return false;
		}
		if (apellido.trim().equalsIgnoreCase("")) {
			Window.alert("El apellido no puede ser vacio");
			return false;
		}
		if (email.equalsIgnoreCase("")) {
			Window.alert("El email no puede ser vacio");
			return false;
		}
		if (email.trim().equalsIgnoreCase("")) {
			Window.alert("El email no puede ser vacio");
			return false;
		}
		if (usuario.equalsIgnoreCase("")) {
			Window.alert("El nombre de usuario no puede ser vacio");
			return false;
		}
		if (usuario.trim().equalsIgnoreCase("")) {
			Window.alert("El nombre de usuario no puede ser vacio");
			return false;
		}
		if (!validarClavesIguales()) {
			Window.alert("Las claves ingresadas deben ser iguales");
			return false;
		}
		int id = this.listBoxListaUsuarios.getSelectedIndex();
		Persona persona = usuarios.get(id);
		int rol = Integer.valueOf(this.listaRoles.getValue(this.listaRoles.getSelectedIndex()));
		
		if (persona.getRol() != rol) {
			Window.alert("El rol no puede ser cambiado");
			return false;
		}
		return true;
	}

	private Persona crearPersonaModificada() {
		int id = this.listBoxListaUsuarios.getSelectedIndex();
		Persona persona = usuarios.get(id);
		if (persona != null) {
			String nombre = this.txtNombre.getText();
			String apellido = this.txtApellido.getText();
			String email = this.txtEmail.getText();
			String usuario = this.txtNombreUsuario.getText();
			String clave = this.txtClave.getText();
			String telefono = this.txtTelefono.getText();
			char sexo = this.sexo();
			persona.setNombre(nombre);
			persona.setApellido(apellido);
			persona.setEmail(email);
			persona.setNombreDeUSuario(usuario);
			persona.setTelefono(telefono);
			persona.setSexo(sexo);
			int rol = Integer.valueOf(this.listaRoles.getValue(this.listaRoles
					.getSelectedIndex()));
			persona.setRol(rol);
			if (!clave.equals("")) {
				persona.setClave(clave);
				this.cambioDeClave = true;
			}
		}
		return persona;
	}

	private void cargarRol(int rol) {
		this.listaRoles.setSelectedIndex(rol - 1);
	}
}
