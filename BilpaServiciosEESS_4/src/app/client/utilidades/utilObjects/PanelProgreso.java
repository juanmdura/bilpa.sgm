package app.client.utilidades.utilObjects;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PanelProgreso extends Composite {

	private final Label label;
	private final Image image;
	
	public PanelProgreso() {
		this("Cargando ...", "azul");
	}
	
	public PanelProgreso(String mensaje) {
		this(mensaje, "azul");
	}

	public PanelProgreso(String mensaje, String color) {
		
		label = new InlineLabel(mensaje);
		image = new Image(GWT.getHostPageBaseURL() + "img/progreso_" + color + ".gif");

		VerticalPanel panel = new VerticalPanel();
		panel.setSize("1%", "1%");
		panel.setSpacing(5);
		panel.setVerticalAlignment(VerticalPanel.ALIGN_TOP);
		panel.setHorizontalAlignment(VerticalPanel.ALIGN_LEFT);
		panel.add(label);
		panel.add(image);
		panel.setCellWidth(label, "1%");
		panel.setCellWidth(image, "99%");
		
		initWidget(panel);
	}
	
	public void setMensaje(String mensaje) {
		label.setText(mensaje);
	}
	
	public String getMensaje() {
		return label.getText();
	}

}
