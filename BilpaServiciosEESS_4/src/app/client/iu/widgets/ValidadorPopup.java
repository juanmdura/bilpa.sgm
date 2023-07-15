package app.client.iu.widgets;

import app.client.utilidades.utilObjects.GlassPopup;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * 
 * @author diemar7
 *
 */
public class ValidadorPopup {

	private PopupDialogBox popup = new PopupDialogBox();
	private PopupPanel vp;
	
	private String mensaje;
	

	public ValidadorPopup(GlassPopup glass, String titulo, String mensaje) {
		this.popup.setGlass(glass);
		this.mensaje = mensaje;
		vp = new PopupPanel(titulo, "400px", "150px");
		setLayout();
	}


	public void setLayout() {
		
		VerticalPanel validador = new VerticalPanel();
		validador.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		validador.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		VerticalPanel componentes = new VerticalPanel();
		componentes.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		componentes.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		Label lbMensaje = new Label(mensaje);
		componentes.add(lbMensaje);
		
		vp.setComponente(componentes);
		
		setEventos();
		
		popup.setWidget(vp);
		popup.center();
		
	}
	
	private void setEventos() {
		
		vp.getLbClose().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				popup.close();
			}
		});

	}
	
	public DialogBox showPopUp(){
		popup.show();
		return popup;
	}

}
