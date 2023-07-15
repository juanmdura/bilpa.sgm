package app.client;

import app.client.iu.IULogin;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ProyectoBilpa implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	public static final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {		
		//RootPanel.get().add(IUMenuPrincipal.getInstancia());
		IULogin iu = new IULogin();
		RootPanel.get("application").add(iu);
		
		//GrafPrueba grafPrueba = new GrafPrueba();
		//RootPanel.get().add(grafPrueba);
		//grafPrueba.update();
	}
	
	public static HasWidgets getRoot(){
		return RootPanel.get("application");
	}
	
	public static HasWidgets getSesion(){
		return RootPanel.get("sesion");
	}

}
