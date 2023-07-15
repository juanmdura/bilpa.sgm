package app.client.dominio;

import com.google.gwt.user.client.ui.TextBox;

public class UtilContador {

	private Contador cont;
	private TextBox txtIni;
	private TextBox txtFin;
	public Contador getCont() {
		return cont;
	}
	public void setCont(Contador cont) {
		this.cont = cont;
	}
	public TextBox getTxtIni() {
		return txtIni;
	}
	public void setTxtIni(TextBox txtIni) {
		this.txtIni = txtIni;
	}
	public TextBox getTxtFin() {
		return txtFin;
	}
	public void setTxtFin(TextBox txtFin) {
		this.txtFin = txtFin;
	}
	public UtilContador(Contador cont, TextBox txtIni, TextBox txtFin) {
		this.cont = cont;
		this.txtIni = txtIni;
		this.txtFin = txtFin;
	}
	public UtilContador() {}
	
	
	
}
