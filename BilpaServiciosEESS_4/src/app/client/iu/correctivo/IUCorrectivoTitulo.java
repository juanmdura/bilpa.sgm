package app.client.iu.correctivo;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import app.client.dominio.IUCorrectivo;
import app.client.dominio.IUCorrectivo.IUCorrectivoEnum;
import app.client.dominio.Orden;
import app.client.dominio.Persona;
import app.client.dominio.Sello;

public class IUCorrectivoTitulo extends Composite {
	private IUCorrectivoEnum iu;
	private Orden orden;
	
	private Label lblTitulo;
	private Label lblSubTitulo;
	private Image imagen;
	
	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private HorizontalPanel hPanelPrincipal = new HorizontalPanel();
	private VerticalPanel vPanelPrincipal2 = new VerticalPanel();
	
	public IUCorrectivoTitulo(Persona sesion, Orden orden, IUCorrectivoEnum iu) {
		super();
		this.iu = iu;
		this.orden = orden;
		
		set();
		setearWidgets();
		agregarWidgets();
		eventos();
	}
	
	public Label getLblSubTitulo() {
		return lblSubTitulo;
	}

	private void eventos() {
		
	}

	private void set() {
		imagen();
		titulo();
		subTitulo();
	}

	private void imagen() {
		if (orden.getEmpresa().getSello().getId() == Sello.PETROBRAS){
			imagen = new Image("img/pbr.png");
			//imagen.setSize("115px", "80px");
		} else {
			imagen = new Image("img/ancap.png");
			//imagen.setSize("115px", "50px");
		}
	}

	private void titulo() {
		String text = "Correctivo ";
		if (IUCorrectivo.esIniOTec(iu)){
			text += orden.getNumero() + " ";
		}
		text+=orden.getEmpresa().getNombre();
		lblTitulo = new Label(text);
	}
	
	private void subTitulo(){
		Label lblFecha = new Label();
		Date fecha = orden.getFechaInicio();
		String fechaFormteada1 = DateTimeFormat.getFormat("EEEE d MMMM yyyy").format(fecha);
		String fechaFormteada2 = DateTimeFormat.getFormat("k:mm").format(fecha);
		lblFecha.setText (String.valueOf(fechaFormteada1));
		
		String text = " ";
		if (IUCorrectivo.esIniOTec(iu)) {
			text += "Creado el " + fechaFormteada1 + " a las " + fechaFormteada2 + " por " + orden.getCreador().toString();
		}
		text = appendTel(text);
		lblSubTitulo = new Label(text);
	}

	private String appendTel(String text) {
		if (orden.getEmpresa().getTelefono() != null && !orden.getEmpresa().getTelefono().equals("")) {
			if (!text.trim().equals("")){
				text += ". Teléfono ";
			} else {
				text += " Teléfono ";
			}
			text += orden.getEmpresa().getTelefono() + ".";
		}
		return text;
	}

	private void agregarWidgets() {
		vPanelPrincipal.add(hPanelPrincipal);
		hPanelPrincipal.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		if (IUCorrectivo.esAlta(iu)){
			//vPanelPrincipal2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		} else {
			//vPanelPrincipal2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		}
		
		vPanelPrincipal2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hPanelPrincipal.add(vPanelPrincipal2);
		hPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		vPanelPrincipal2.add(lblTitulo);
		vPanelPrincipal2.add(lblSubTitulo);
		hPanelPrincipal.add(imagen);
	}

	private void setearWidgets() {
		vPanelPrincipal.setWidth("100%");
		hPanelPrincipal.setWidth("100%");
		vPanelPrincipal2.setWidth("100%");

		lblTitulo.setStyleName("Titulo");
		lblSubTitulo.setStyleName("SubTituloGris");
	}

	public VerticalPanel getPrincipalPanel() {
		return vPanelPrincipal;
	}

}
