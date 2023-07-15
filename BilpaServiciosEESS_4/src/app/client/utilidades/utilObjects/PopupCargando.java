package app.client.utilidades.utilObjects;

public class PopupCargando extends GlassPopup{
	
	public PopupCargando() {
		this(new PanelCargando());
	}

	public PopupCargando(String mensaje) {
		this(new PanelCargando(mensaje));
	}
	
	public PopupCargando(String mensaje, String color) {
		this(new PanelCargando(mensaje, color));
	}
	
	protected PopupCargando(PanelCargando panel) {
		panel.setWidth("150px");
		setWidget(panel);
	}

	public void setMensaje(String mensaje) {
		((PanelCargando) getWidget()).setMensaje(mensaje);
	}

	public String getMensaje() {
		return ((PanelCargando) getWidget()).getMensaje();
	}

}
