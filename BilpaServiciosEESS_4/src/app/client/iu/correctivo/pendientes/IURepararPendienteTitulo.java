package app.client.iu.correctivo.pendientes;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import app.client.dominio.Persona;
import app.client.dominio.data.PendienteDataUI;
import app.client.iu.correctivo.IUWidgetCorrectivo;
import app.client.utilidades.utilObjects.GlassPopup;

public class IURepararPendienteTitulo extends Composite  {
	private GlassPopup glass = new GlassPopup();
	private Label lblTitulo = new Label();
	private Button btnRepararPendientes = new Button("Reparar pendientes");
	
	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private HorizontalPanel hPanelPrincipal = new HorizontalPanel();
	private VerticalPanel vPanelPrincipal2 = new VerticalPanel();
	
	private PendienteDataUI pendienteDataUI;
	private IUWidgetCorrectivo iuWidgetCorrectivo;
	
	public IURepararPendienteTitulo(Persona sesion, IUWidgetCorrectivo iuWidgetCorrectivo) {
		super();
		this.iuWidgetCorrectivo = iuWidgetCorrectivo;
	}
	
	public PendienteDataUI getPendienteDataUI() {
		return pendienteDataUI;
	}

	public void setPendienteDataUI(PendienteDataUI pendienteDataUI) {
		this.pendienteDataUI = pendienteDataUI;
	}

	private void eventos() {
		btnRepararPendientes.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final IURepararPendienteDialogo dialogo = new IURepararPendienteDialogo(pendienteDataUI, iuWidgetCorrectivo, glass);
				glass.show();
				dialogo.show();
				// dialogo.setPopupPosition(com.google.gwt.user.client.Window.getScrollLeft(), 10);
				Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
				    public void execute() {
				    	dialogo.center();
				    }
				}); 
			}
		});
	}

	private void titulo() {
		String text = "Existen ";
		text += pendienteDataUI.getPendientes().size();
		text += " pendientes a reparar";
		lblTitulo = new Label(text);
	}
	
	private void agregarWidgets() {
		vPanelPrincipal.add(hPanelPrincipal);
		
		hPanelPrincipal.add(lblTitulo);
		hPanelPrincipal.add(btnRepararPendientes);
	}

	private void setearWidgets() {
		vPanelPrincipal.setWidth("100%");
		hPanelPrincipal.setWidth("100%");
		vPanelPrincipal2.setWidth("100%");

		lblTitulo.setStyleName("SubTitulo");
		
		hPanelPrincipal.setSpacing(8);
	}

	public VerticalPanel getPrincipalPanel() {
		if (pendienteDataUI.getPendientes().size() > 0){

			titulo();
			setearWidgets();
			agregarWidgets();
			eventos();
			return vPanelPrincipal;
		}
		return new VerticalPanel();
	}

}
