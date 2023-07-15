package app.client.iu.orden.tecnico.dialogoAgregarSolucion;

import app.client.dominio.Comentario;
import app.client.dominio.Persona;
import app.client.dominio.Solucion;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.utilObjects.GlassPopup;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DialogoComentario extends DialogBox {

	private VerticalPanel vp = new VerticalPanel();
	private HorizontalPanel hp1 = new HorizontalPanel();
	private HorizontalPanel hp2 = new HorizontalPanel();
	
	private Label lblTituloComentario = new Label("Comentario de reparación");
	
	TextArea textArea = new TextArea();
	CheckBox checkBoxImp = new CheckBox();

	private Button btnAceptar = new Button("Aceptar");
	private Button btnCancelar = new Button("Cancelar");
	
	private Persona session;
	private Solucion solucion;
	private Comentario comentario;
	
	private GlassPopup glass = new GlassPopup();
	
	public DialogoComentario(Persona session, Solucion solucion) {
		this.session = session;
		this.solucion = solucion;
				
		setWidgets();
		setEvents();
		addWidgets();
		setValues();
	}

	private void setValues() {
		checkBoxImp.setValue(true);
		if(solucion != null && solucion.getComentario() != null){
			checkBoxImp.setValue(solucion.getComentario().isImprimible());
			textArea.setText(solucion.getComentario().getTexto());
		}
	}

	public Comentario getComentarioData() {
		if(solucion != null && solucion.getComentario() != null){
			return solucion.getComentario();
		}else{
			return comentario;
		}
	}
	
	private void addWidgets() {
		vp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hp1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hp2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		vp.setSpacing(5);
		hp1.setSpacing(5);
		hp2.setSpacing(5);
		
		add(vp);

		vp.add(lblTituloComentario);
		vp.add(hp1);
		vp.add(hp2);
		
		hp1.add(checkBoxImp);
		hp1.add(textArea);
		
		hp2.add(btnCancelar);
		hp2.add(btnAceptar);
		
	}

	private void setEvents() {
		btnAceptar.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				aceptar();
			}
		});

		btnCancelar.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				hide();
			}
		});
	}
	
	private boolean validarComentario() 
	{
		String texto = textArea.getText();
		if (texto.length() > 399)
		{
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "No es posible ingresar comentarios con mas de 400 caracteres");
			vpu.showPopUp();
			textArea.setFocus(true);
			return false;
		}
		return true;
	}
	
	private void setWidgets() {
		lblTituloComentario.setStyleName("Titulo");

		btnAceptar.setWidth("100");
		btnCancelar.setWidth("100");
		
		checkBoxImp.setText("Imprimible");
		
		textArea.setSize("500", "150");
		
		checkBoxImp.setStyleName("Negrita");
	}

	private void aceptar() {
		if(validarComentario()){
			Comentario comentarioDataAux = new Comentario();
			comentarioDataAux.setImprimible(checkBoxImp.getValue());
			comentarioDataAux.setTexto(textArea.getText());
			comentarioDataAux.setNombreUsuario(session.getNombreDeUSuario());
			comentario = comentarioDataAux;
			if(solucion != null){
				solucion.setComentario(comentarioDataAux);
			}
			hide();
		}
	}
	
}
