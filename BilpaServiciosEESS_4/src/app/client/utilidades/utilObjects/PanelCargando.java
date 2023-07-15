package app.client.utilidades.utilObjects;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class PanelCargando extends Composite {

	private final Label label;
	private final Image image;

	public PanelCargando() {
		this("Cargando ...", "azul");
	}

	public PanelCargando(String mensaje) {
		this(mensaje, "azul");
	}

	public PanelCargando(String mensaje, String color) {

		label = new Label(mensaje);
		image = new Image(GWT.getHostPageBaseURL() + "img/cargando_" + color + ".gif");

		label.setStyleName("Negrita");
		
		HorizontalPanel panel = new HorizontalPanel();
		panel.setSpacing(5);
		panel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
		panel.add(image);
		panel.add(label);
		panel.setCellWidth(image, "1%");
		panel.setCellWidth(label, "99%");
		
		initWidget(panel);
	}

	public void setMensaje(String mensaje) {
		label.setText(mensaje);
	}

	public String getMensaje() {
		return label.getText();
	}

}
