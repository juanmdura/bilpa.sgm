package app.client.iu.correctivo;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import app.client.dominio.IUCorrectivo.IUCorrectivoEnum;
import app.client.dominio.Orden;
import app.client.dominio.Persona;

public class IUCorrectivoTemplate extends Composite {
	private IUCorrectivoEnum iu;
	private Orden orden;
	
	private Label lblTitulo;
	
	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private HorizontalPanel hPanelPrincipal = new HorizontalPanel();
	
	public IUCorrectivoTemplate(Persona sesion, Orden orden, IUCorrectivoEnum iu) {
		super();
		this.iu = iu;
		this.orden = orden;
		
		set();
		setearWidgets();
		agregarWidgets();
		eventos();
	}
	
	private void eventos() {
		
	}

	private void set() {
		titulo();
	}

	private void titulo() {
	}

	private void agregarWidgets() {
		vPanelPrincipal.add(hPanelPrincipal);
		hPanelPrincipal.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hPanelPrincipal.add(lblTitulo);
		hPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
	}

	private void setearWidgets() {
		vPanelPrincipal.setWidth("100%");
		hPanelPrincipal.setWidth("100%");

	}

	public VerticalPanel getPrincipalPanel() {
		return vPanelPrincipal;
	}

}
