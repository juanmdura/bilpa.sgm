package app.client.iu;

import app.client.ProyectoBilpa;
import app.client.dominio.Administrador;
import app.client.dominio.Administrativo;
import app.client.dominio.Encargado;
import app.client.dominio.Persona;
import app.client.dominio.Tecnico;
import app.client.iu.menu.IUMenuPrincipal;
import app.client.iu.menu.IUMenuPrincipalAdministrativo;
import app.client.iu.menu.IUMenuPrincipalTecnico;
import app.client.iu.widgets.ValidadorPopup;
import app.client.iu.widgets.VerticalSpacePanel;
import app.client.utilidades.utilObjects.GlassPopup;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class IULogin extends Composite{
	
	public static final String USUARIO_WM_TEXT = "Usuario";
	public static final String PASS_WM_TEXT = "Contraseña";

	private Label lblTitulo = new Label("Bilpa SGM");

	private TextBox tUsuario = new TextBox();

	private PasswordTextBox tClave = new PasswordTextBox();
	private Button bAceptar = new Button();

	private VerticalPanel dailogoPrincipal = new VerticalPanel();
	private VerticalPanel panelVerticalContenedor = new VerticalPanel();
	private VerticalPanel panelBig = new VerticalPanel();

	private GlassPopup glass = new GlassPopup();

	private final HasWidgets container;

	public IULogin(){
		
		container = ProyectoBilpa.getRoot();
		
		panelBig.setSize("100%", "400");
		panelBig.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panelBig.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		panelBig.setSpacing(50);
		
		panelVerticalContenedor.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panelVerticalContenedor.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		panelVerticalContenedor.setSpacing(20);
		
		lblTitulo.setStyleName("tituloLabel");
		tUsuario.setAlignment(TextAlignment.CENTER);
		tUsuario.setText(USUARIO_WM_TEXT);
		tUsuario.addStyleName("waterMarkedTextBox");
		
		tClave.setAlignment(TextAlignment.CENTER);
		tClave.setText(PASS_WM_TEXT);
		tClave.addStyleName("waterMarkedTextBox");
		
		tUsuario.setWidth("200px");
		tClave.setWidth("200px");
		bAceptar.setText("Aceptar");
		bAceptar.setWidth("200px");

		VerticalPanel contenido = new VerticalPanel();
		contenido.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		contenido.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		contenido.setSpacing(4);
		contenido.add(tUsuario);
		contenido.add(tClave);
		contenido.add(bAceptar);
		
		dailogoPrincipal.setStyleName("loginPanel");
		dailogoPrincipal.setSize("300px", "135px");
		dailogoPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		dailogoPrincipal.setSpacing(20);
		dailogoPrincipal.add(contenido);
		
		panelVerticalContenedor.add(lblTitulo);
		panelVerticalContenedor.add(new VerticalSpacePanel("20px"));
		panelVerticalContenedor.add(dailogoPrincipal);
		panelBig.add(panelVerticalContenedor);
 
		/*tUsuario.setText("wstenger");
	 	tClave.setText("wss");
	 	validarLogin();*/
	 	
	 	bAceptar.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				validarLogin();
			}
		});

		tClave.addKeyPressHandler(new KeyPressHandler()
		{
			public void onKeyPress(KeyPressEvent event) 
			{
				if (KeyCodes.KEY_ENTER == event.getNativeEvent().getKeyCode())
				{
					validarLogin();
				}
			}
		});
		
		tUsuario.addKeyPressHandler(new KeyPressHandler()
		{
			public void onKeyPress(KeyPressEvent event) 
			{
				if (KeyCodes.KEY_ENTER == event.getNativeEvent().getKeyCode())
				{
					validarLogin();
				}
			}
		});
		
		this.tUsuario.addFocusHandler(new FocusHandler() {
			public void onFocus(FocusEvent event) {
				
				tUsuario.removeStyleName("waterMarkedTextBox");
				if(tUsuario.getText().equalsIgnoreCase(USUARIO_WM_TEXT)){
					tUsuario.setText("");
				}
			}
		});
		
		this.tUsuario.addBlurHandler(new BlurHandler() {
			public void onBlur(BlurEvent event) {
				
				if(tUsuario.getText().length() == 0 || tUsuario.getText().equalsIgnoreCase(USUARIO_WM_TEXT)){
					tUsuario.setText(USUARIO_WM_TEXT);
					tUsuario.addStyleName("waterMarkedTextBox");
				}
			}
		});
		
		this.tClave.addFocusHandler(new FocusHandler() {
			public void onFocus(FocusEvent event) {
				
				tClave.removeStyleName("waterMarkedTextBox");
				if(tClave.getText().equalsIgnoreCase(PASS_WM_TEXT)){
					tClave.setText("");
				}
			}
		});
		
		this.tClave.addBlurHandler(new BlurHandler() {
			public void onBlur(BlurEvent event) {
				
				if(tClave.getText().length() == 0 || tClave.getText().equalsIgnoreCase(PASS_WM_TEXT)){
					tClave.setText(PASS_WM_TEXT);
					tClave.addStyleName("waterMarkedTextBox");
				}
			}
		});

		bAceptar.addKeyPressHandler(new KeyPressHandler()
		{
			public void onKeyPress(KeyPressEvent event) 
			{
				if (KeyCodes.KEY_ENTER == event.getNativeEvent().getKeyCode())
				{
					validarLogin();
				}
			}
		});
		
		initWidget(panelBig);
	}

	private void validarLogin()	{
		String usuario = this.tUsuario.getText();
		String password = this.tClave.getText();

		ProyectoBilpa.greetingService.validarUsuario(usuario, password, new AsyncCallback<Persona>() {

			public void onSuccess(Persona result) {
				if (result == null){
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Usuario y/o Password Incorrecto!!");
					vpu.showPopUp();
					tUsuario.setFocus(true);
				}else{
					int rol = result.getRol();

					if(rol == 1){
						//menu administrador
						Administrador administrador = (Administrador) result;
						cargarMenuAdministrador(administrador);

					}else if(rol == 2){
						//menu administrativo
						Administrativo administrativo = (Administrativo) result;
						cargarMenuAdministrativo(administrativo);

					}else if(rol == 3){
						//menu tecnico
						Tecnico tecnico = (Tecnico) result;
						cargarMenuTecnico(tecnico);

					}else if(rol == 5){
						//menu Encargado Mantenimiento
						Encargado encMantenimiento = (Encargado) result;
						cargarMenuAdministrador(encMantenimiento);
					}
				}
			}
			
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar el Usuario");
				vpu.showPopUp();
			}
		});
	}

	private void cargarMenuAdministrador(Persona persona)
	{
		container.clear();
		IUMenuPrincipal.getInstancia().setSesion(persona);
		container.add(IUMenuPrincipal.getInstancia());
	}

	private void cargarMenuAdministrativo(Administrativo administrativo)
	{
		container.clear();
		IUMenuPrincipalAdministrativo.getInstancia().setSesion(administrativo);
		container.add(IUMenuPrincipalAdministrativo.getInstancia());
	}

	private void cargarMenuTecnico(Tecnico tecnico)
	{
		container.clear();
		IUMenuPrincipalTecnico.getInstancia().setSesion(tecnico);
		container.add(IUMenuPrincipalTecnico.getInstancia());
	}
	
}
