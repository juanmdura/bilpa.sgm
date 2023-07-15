package app.client.iu.widgets;

import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PopupPanel extends VerticalPanel{
	
	private final static String PANEL_STYLE = "";
	
	VerticalPanel vpComponentes = new VerticalPanel();
	
	private String width;
	private String height;
	
	private TituloPopupLabel lbTitulo = new TituloPopupLabel("");
	private Label lbClose = new Label("x");
	
	
	public void setComponente(CellPanel componente) {
		vpComponentes.add(componente);
	}

	public TituloPopupLabel getLbTitulo() {
		return lbTitulo;
	}

	public void setLbTitulo(TituloPopupLabel lbTitulo) {
		this.lbTitulo = lbTitulo;
	}
	
	public Label getLbClose() {
		return lbClose;
	}

	public void setLbClose(Label lbClose) {
		this.lbClose = lbClose;
	}

	
	public PopupPanel(String titulo, String width, String height) {
		this.lbTitulo.setText(titulo);
		this.width = width;
		this.height = height;
		setLayout();
	}
	
	
	public void setLayout() {	
		
		if(this.width != null && this.height != null){
			this.setSize(this.width, this.height);
		}
		this.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		this.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		
		VerticalPanel vp = new VerticalPanel();
		vp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vp.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		vp.setWidth("100%");
		
		HorizontalPanel header = new HorizontalPanel();
		header.setSize("100%", "20px");
		header.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		header.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		lbClose.setStyleName("close");
		lbClose.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		header.add(lbClose);
		
		vpComponentes.setStyleName(PANEL_STYLE);
		vpComponentes.setSize("100%", "100%");
		vpComponentes.setSpacing(20);
		vpComponentes.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vpComponentes.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		HorizontalPanel content = new HorizontalPanel();
		content.setSize("100%", "100%");
		content.add(new HorizontalSpacePanel("15px"));
		content.add(vpComponentes);
		content.add(new HorizontalSpacePanel("15px"));
		
		vp.add(header);
		vp.add(lbTitulo);
		vp.add(content);
		vp.add(new VerticalSpacePanel("15px"));
		
		this.add(vp);
		
    }
	

}
