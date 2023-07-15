package app.client.iu.orden.tecnico.dialogoAgregarSolucion.tab;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import app.client.ProyectoBilpa;
import app.client.dominio.Comentario;
import app.client.dominio.Persona;
import app.client.dominio.Pico;
import app.client.dominio.Reparacion;
import app.client.dominio.Solucion;
import app.client.dominio.Surtidor;
import app.client.dominio.data.DestinoDelCargoData;
import app.client.iu.orden.tecnico.dialogoAgregarSolucion.DialogoComentario;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.ListBoxAdv;
import app.client.utilidades.utilObjects.PopupCargando;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;

public class IUWidgetVariosTab extends Composite{
	
	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	
	HorizontalPanel pHCentral = new HorizontalPanel();
		
	private FlexTable tableVarios = new FlexTable();
	
	HorizontalPanel pHCargoPicoTeleCom = new HorizontalPanel();
	
	public CheckBox checkboxTelefonica = new CheckBox("Telefónica");
	public ListBoxAdv listBoxPicosDialAgregarSolucion = new ListBoxAdv();
	ListBox listBoxDestinosDelCargo = new ListBox();
	
	Label lblPicos = new Label("Pico");
	Label lblComentario = new Label("Comentarios");
	Label lblTipoCombustible = new Label("Tipo Combustible");
	Label lblTipoCombustibleDato = new Label();
	Label lblDestino = new Label("Destino del Cargo");
	
	private Reparacion rep;
	private DestinoDelCargoData destinoDelCargoSeleccionado;
	
	private List<DestinoDelCargoData> destinosDelCargo = new ArrayList<DestinoDelCargoData>();
	
	private PopupCargando popUp;
	
	private Image imgComentario = new Image("img/comentario.png");
	private PushButton btnComentario = new PushButton(imgComentario);
	
	Solucion solucion;
	public DialogoComentario dialogoComentario;
	
	private Persona sesion;
	
	private GlassPopup glass = new GlassPopup();
	
	public IUWidgetVariosTab(PopupCargando popUp, Reparacion reparacion, Persona persona, Solucion solucion) {
		
		this.popUp = popUp;
		this.rep = reparacion;
		this.sesion = persona;
		this.solucion = solucion;
		
		dialogoComentario = new DialogoComentario(sesion, solucion);
		 
		HorizontalPanel hpSpace = new HorizontalPanel();
		HorizontalPanel hpSpace1 = new HorizontalPanel();
		HorizontalPanel hpSpace2 = new HorizontalPanel();
		HorizontalPanel hpSpace3 = new HorizontalPanel();
		
		hpSpace.setHeight("30px");
		hpSpace1.setHeight("30px");
		hpSpace2.setHeight("30px");
		hpSpace3.setHeight("30px");

		pHCargoPicoTeleCom.setSpacing(10);
		pHCargoPicoTeleCom.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		
		pHCargoPicoTeleCom.add(tableVarios);
		
		vPanelPrincipal.setSize("1220px", "380px");
		pHCargoPicoTeleCom.setWidth("1220px");
		
		tableVarios.setWidget(0, 0, lblComentario);
		tableVarios.setWidget(0, 1, btnComentario);

		tableVarios.setWidget(1, 0, hpSpace1);

		tableVarios.setWidget(2, 0, lblDestino);
		tableVarios.setWidget(2, 1, listBoxDestinosDelCargo);
		
		tableVarios.setWidget(3, 0, hpSpace2);	
		
		int fila = 4;
		if (rep.getActivo().getTipo() == 1){
			tableVarios.setWidget(fila, 0, lblPicos);
			tableVarios.setWidget(fila, 1, lblTipoCombustible);
			
			fila++;
			tableVarios.setWidget(fila, 0, listBoxPicosDialAgregarSolucion);
			tableVarios.setWidget(fila, 1, lblTipoCombustibleDato);
			
			cargarListaPicosDialAgSol((Surtidor)rep.getActivo(), fila);
			
			fila++;
			tableVarios.setWidget(fila, 0, hpSpace);
		}
		

		if(fila > 4){
			fila++;
		}
		tableVarios.setWidget(fila, 0, checkboxTelefonica);
		
		fila++;
		tableVarios.setWidget(fila, 0, hpSpace3);

		
		cargarListaDestinosDelCargo();
		
		pHCentral.add(pHCargoPicoTeleCom);
		
		setear();
		agregar();
		eventos();
	}
	
	public Comentario getComentario(){
		return dialogoComentario.getComentarioData();
	}

	public DestinoDelCargoData getDestinoDelCargoSeleccionado() {
		return destinoDelCargoSeleccionado;
	}

	public void setDestinoDelCargoSeleccionado(DestinoDelCargoData destinoDelCargoSeleccionado) {
		this.destinoDelCargoSeleccionado = destinoDelCargoSeleccionado;
		for (int i = 0; i < destinosDelCargo.size() ; i++) {
			if(destinosDelCargo.get(i).getId() == this.destinoDelCargoSeleccionado.getId()){
				listBoxDestinosDelCargo.setSelectedIndex(i);
			}
		}
	}

	public VerticalPanel getPrincipalPanel() {
		return vPanelPrincipal;
	}
	
	private void cargarListaDestinosDelCargo(){

		listBoxDestinosDelCargo.clear();
		
		ProyectoBilpa.greetingService.obtenerDestinosDelCargo(new AsyncCallback<ArrayList<DestinoDelCargoData>>() {
			public void onFailure(Throwable caught) 
			{
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al cargar los destinos del cargo.");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<DestinoDelCargoData> destinos) 
			{
				for(DestinoDelCargoData d : destinos){
					destinosDelCargo.add(d);
					listBoxDestinosDelCargo.addItem(d.getNombre());
				}
				destinoDelCargoSeleccionado = destinosDelCargo.get(0);
				
				if(solucion != null){
					setDestinoDelCargoSeleccionado(new DestinoDelCargoData(solucion.getDestinoDelCargo().getId(), solucion.getDestinoDelCargo().getNombre()));
				}
			}
		});
		
	}

	private void cargarListaPicosDialAgSol(Surtidor surtidor, int filaPico){
		listBoxPicosDialAgregarSolucion.clear();
		Set<Pico> listaPicos = surtidor.getPicos();

		tableVarios.setText(filaPico, 3, "");
		listBoxPicosDialAgregarSolucion.addItem("N/A", "-2");

		for(Pico p: listaPicos)
		{
			listBoxPicosDialAgregarSolucion.addItem(p.getNumeroPico()+"", String.valueOf(p.getId()));
		}
	}	
	
	private void setear() {
		
		lblPicos.setStyleName("Negrita");
		lblComentario.setStyleName("Negrita");
		lblTipoCombustible.setStyleName("Negrita");
		checkboxTelefonica.setStyleName("Negrita");
		lblDestino.setStyleName("Negrita");

		lblTipoCombustible.setWidth("150px");
		lblPicos.setWidth("150px");
		lblComentario.setWidth("150x");
		lblDestino.setWidth("150px");
		listBoxPicosDialAgregarSolucion.setWidth("50px");
		
		btnComentario.setSize("40px", "40px");
		imgComentario.setSize("40px", "40px");
		btnComentario.setTitle("Agregar un comentario al destino del cargo");
		
	}
	
	private void eventos() {
		
		listBoxPicosDialAgregarSolucion.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				mostrarDatosPicoSeleccionado();				
			}
		});
		
		listBoxDestinosDelCargo.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				mostrarDatosDestinoSeleccionado();				
			}
		});
		
		btnComentario.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				dialogoComentario.show();
				dialogoComentario.center();
			}
		});
		
	}
	
	private void mostrarDatosPicoSeleccionado(){		
		int idPico = Integer.valueOf(listBoxPicosDialAgregarSolucion.getValue(listBoxPicosDialAgregarSolucion.getSelectedIndex()));
		Surtidor s = (Surtidor)rep.getActivo();
		Pico pico = s.buscarPico(idPico);
		if(pico != null){		
			lblTipoCombustibleDato.setText(pico.getProducto().getNombre());
		}
	}
	
	private void mostrarDatosDestinoSeleccionado(){
		destinoDelCargoSeleccionado = destinosDelCargo.get(this.listBoxDestinosDelCargo.getSelectedIndex());
	}
	
	public Pico getPicoSeleccionado(Surtidor surt){
		if (listBoxPicosDialAgregarSolucion.getSelectedIndex()!=0){
			if (listBoxPicosDialAgregarSolucion.getSelectedIndex()>=0){
				int idPicoSeleccionado = Integer.valueOf(listBoxPicosDialAgregarSolucion.getValue(listBoxPicosDialAgregarSolucion.getSelectedIndex()));
				return surt.buscarPico(idPicoSeleccionado);
			}
		}
		return null;
	}	
	
	private void agregar(){
		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal.setSpacing(5);
		
		vPanelPrincipal.add(pHCargoPicoTeleCom);
	}
	
	public boolean getTelefonicaChecked(){
		return this.checkboxTelefonica.getValue();
	}

}
