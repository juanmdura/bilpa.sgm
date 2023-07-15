package app.client.iu.rightClick;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;

import app.client.dominio.ModeloSurtidor;

public class AdvLabelModeloSurtidor extends AdvLabel{

	private ModeloSurtidor modeloSurtidor;
	
	public ModeloSurtidor getModelo() {
		return modeloSurtidor;
	}

	public void setModelo(ModeloSurtidor modelo) {
		this.modeloSurtidor = modelo;
	}

	public AdvLabelModeloSurtidor(Entero entero, String text, ModeloSurtidor modelo, Composite iu) {
		super(entero, text, iu);
		this.modeloSurtidor = modelo;
	}
	
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event, this.getModelo());
	}
}
