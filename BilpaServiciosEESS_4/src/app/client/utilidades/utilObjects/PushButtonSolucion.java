package app.client.utilidades.utilObjects;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;

import app.client.dominio.Solucion;

public class PushButtonSolucion extends PushButton {
	private Solucion solucion;
	private int row;

	public PushButtonSolucion(Image upImage) {
		super(upImage);
	}

	public Solucion getSolucion() {
		return solucion;
	}

	public void setSolucion(Solucion solucion) {
		this.solucion = solucion;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}
	
}
