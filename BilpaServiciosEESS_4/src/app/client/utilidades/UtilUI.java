package app.client.utilidades;

import java.util.ArrayList;

import app.client.ProyectoBilpa;
import app.client.dominio.Orden;
import app.client.dominio.Repuesto;
import app.client.dominio.Tarea;
import app.client.iu.orden.tecnico.IUSeguimientoTecnico;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.utilObjects.GlassPopup;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public abstract class UtilUI extends Composite{

	private static final ListBox listBoxTareas = new ListBox();
	private static final ListBox listBoxRepuestos = new ListBox();
	private static ListBox listBoxCantidad = new ListBox();
	private IUSeguimientoTecnico iu;
	
	private static GlassPopup glass = new GlassPopup();

	public static void setearCssATabla (FlexTable t, int i)
	{
		if(i %2==0){
			t.getRowFormatter().addStyleName(i-1, "FilaTabla1");				
		}else{
			t.getRowFormatter().addStyleName(i-1, "FilaTabla2");
		}
	}

	public static void limpiarTabla(FlexTable table) {
		while(table.getRowCount() > 1 )
		{
			table.removeRow(1);
		}
	}
	
	public static void agregarEspacioEnBlanco (CellPanel panel, int space)
	{
		HorizontalPanel hpEmpty = new HorizontalPanel();
		hpEmpty.setHeight(space + "px");	
		panel.add(hpEmpty);
	}
	
	public static void agregarLabelEnBlanco (CellPanel panel, int space)
	{
		Label sep = new Label("");
		sep.setWidth("100px");
		sep.setWidth(space + "px");	
		panel.add(sep);
	}

	public static DialogBox dialogBoxError(String texto){

		final DialogBox dError = new DialogBox(true);

		dError.setTitle("Error");

		dError.setText(texto);
		dError.setAnimationEnabled(true);
		dError.setAutoHideEnabled(true);

		dError.center();
		dError.show();

		HorizontalPanel hp = new HorizontalPanel();
		hp.setWidth("200");
		hp.setHeight("40");
		dError.add(hp);


		//dError.setPixelSize(200, 200);
		Button bClose = new Button("Cerrar");
		bClose.setWidth("80");

		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		hp.add(bClose);

		bClose.addClickHandler(new ClickHandler() {


			public void onClick(ClickEvent event) {
				dError.hide();	
				dError.clear();
			}
		});


		return dError;

	}

	public static DialogBox dialogBoxWarning(String texto){

		final DialogBox dError = new DialogBox(true);

		dError.setTitle("Atencion!");

		dError.setText(texto);
		dError.setAnimationEnabled(true);
		dError.setAutoHideEnabled(true);

		dError.center();
		dError.show();

		HorizontalPanel hp = new HorizontalPanel();
		hp.setWidth("200");
		hp.setHeight("40");
		dError.add(hp);


		//dError.setPixelSize(200, 200);
		Button bClose = new Button("Cerrar");
		bClose.setWidth("80");

		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		hp.add(bClose);

		bClose.addClickHandler(new ClickHandler() {


			public void onClick(ClickEvent event) {
				dError.hide();	
				dError.clear();
			}
		});


		return dError;

	}


	public static DialogBox dialogBoxAgregarSolucion(){

		final DialogBox dialogo = new DialogBox(true);

		final HorizontalPanel pHBig = new HorizontalPanel();

		final VerticalPanel sp1 = new VerticalPanel();
		final VerticalPanel sp2 = new VerticalPanel();
		final VerticalPanel sp3 = new VerticalPanel();
		final VerticalPanel sp4 = new VerticalPanel();
		final VerticalPanel sp5 = new VerticalPanel();
		final VerticalPanel sp6 = new VerticalPanel();
		final VerticalPanel sp7 = new VerticalPanel();
		final VerticalPanel sp8 = new VerticalPanel();
		final VerticalPanel sp9 = new VerticalPanel();
		final VerticalPanel spLateral = new VerticalPanel();
		final VerticalPanel spLateral2 = new VerticalPanel();

		final VerticalPanel pV1 = new VerticalPanel();

		final HorizontalPanel pH11 = new HorizontalPanel();
		final HorizontalPanel pH12 = new HorizontalPanel();
		final HorizontalPanel pH13 = new HorizontalPanel();
		final HorizontalPanel pH14 = new HorizontalPanel();

		final VerticalPanel pV111 = new VerticalPanel();
		final VerticalPanel pV112 = new VerticalPanel();

		final VerticalPanel pV131 = new VerticalPanel();
		final VerticalPanel pV132 = new VerticalPanel();
		final VerticalPanel pV133 = new VerticalPanel();

		final VerticalPanel pV141 = new VerticalPanel();

		final Label lblGestionarDatosTecnicos = new Label("Gestionar Datos Técnico");
		final Label lblTareaRealizada = new Label("Tarea Realizada");
		final Label lblRepuesto = new Label("Repuesto");
		final Label lblCantidad = new Label("Cantidad");



		final Button btnAgregarTarea =  new Button("Agregar");
		final Button btnAgregarRepuesto =  new Button("Agregar");
		final Button btnCancelar =  new Button("Cancelar");

		sp1.setWidth("181");
		sp2.setWidth("30");
		sp3.setWidth("30");
		sp4.setWidth("30");
		sp5.setWidth("480");
		sp6.setWidth("30");
		sp7.setWidth("30");
		sp8.setWidth("30");
		sp9.setWidth("30");
		spLateral.setWidth("15");
		spLateral2.setWidth("15");

		listBoxTareas.setWidth("300");
		listBoxRepuestos.setWidth("300");
		listBoxCantidad.setWidth("120");

		btnAgregarTarea.setWidth("80");
		btnAgregarRepuesto.setWidth("80");
		btnCancelar.setWidth("80");

		CheckBox checkboxTelefonica = new CheckBox("Telefonica");

		btnCancelar.addClickHandler(new ClickHandler(){


			public void onClick(ClickEvent event) {
				dialogo.hide();

			}

		});

		pV1.setWidth("500");
		pV1.setHeight("200");

		pV111.add(lblTareaRealizada);
		pV111.add(listBoxTareas);

		pV112.add(btnAgregarTarea);

		pV131.add(lblRepuesto);
		pV131.add(listBoxRepuestos);
		pV132.add(lblCantidad);
		pV132.add(listBoxCantidad);
		pV133.add(btnAgregarRepuesto);

		pV141.add(btnCancelar);

		pH11.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		pH13.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		pH14.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		pH14.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);

		pH11.add(pV111);
		pH11.add(sp1);
		pH11.add(pV112);

		pH12.add(checkboxTelefonica);

		pH13.add(pV131);
		pH13.add(sp2);
		pH13.add(pV132);
		pH13.add(sp3);
		pH13.add(pV133);
		pH13.add(sp4);

		pH14.add(sp5);
		pH14.add(pV141);

		pV1.add(lblGestionarDatosTecnicos);	
		pV1.add(pH11);	
		pV1.add(pH12);	
		pV1.add(pH13);	
		pV1.add(pH14);

		pHBig.add(spLateral);
		pHBig.add(pV1);
		pHBig.add(spLateral2);

		dialogo.add(pHBig);
		cargarLtBoxTareas();
		cargarLtBoxRepuestos();
		cargarLtBoxCantidad();
		return dialogo;

	}

	public static void cargarLtBoxTareas(){
		listBoxTareas.clear();
		ProyectoBilpa.greetingService.obtenerTodasLasTareas(new AsyncCallback<ArrayList<Tarea>>() {


			public void onFailure(Throwable caught) {

			}


			public void onSuccess(ArrayList<Tarea> result) {
				listBoxTareas.addItem("Sin Seleccionar");
				Tarea auxiliar;
				for (int i=0; i<result.size();i++){
					auxiliar = (Tarea) result.get(i);

					listBoxTareas.addItem(auxiliar.toString(),String.valueOf(auxiliar.getId()));


				}			
			}
		});		
	}

	public static void cargarLtBoxRepuestos(){
		listBoxRepuestos.clear();
		ProyectoBilpa.greetingService.obtenerTodosLosRepuestos(new AsyncCallback<ArrayList<Repuesto>>() {


			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener los repuestos " + caught.getMessage());
				vpu.showPopUp();
			}


			public void onSuccess(ArrayList<Repuesto> result) {
				listBoxRepuestos.addItem("Sin Seleccionar");
				Repuesto auxiliar;
				for (int i=0; i<result.size();i++){
					auxiliar = (Repuesto) result.get(i);

					listBoxRepuestos.addItem(auxiliar.toString(),String.valueOf(auxiliar.getId()));


				}			
			}
		});		
	}

	public static void cargarLtBoxCantidad(){
		int [] retorno = new int [1000];
		for(int i=0; i<1000;i++){
			retorno[i]=i;
			listBoxCantidad.addItem(i+"");
		}

	}
	public static DialogBox dialogoComentarioAnulada(){

		final DialogBox dialogComentario = new DialogBox();
		HorizontalPanel hPanelDialModif = new HorizontalPanel();
		HorizontalPanel hPanelDialModif2 = new HorizontalPanel();
		VerticalPanel vPanelDailModif = new VerticalPanel();
		TextBox tDialModifRep = new TextBox();

		Button btnAceptarModif = new Button("OK",
				new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogComentario.hide(true);
				//modificarDescRep();
			}
		});

		Button btnCancelModif = new Button("Cancelar",
				new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogComentario.hide(true);
			}
		});

		dialogComentario.setAutoHideEnabled(true);
		dialogComentario.setTitle("Modificacion de Repuesto");
		vPanelDailModif.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		btnAceptarModif.setWidth("100px");
		tDialModifRep.setWidth("500px");
		btnCancelModif.setWidth("100px");
		tDialModifRep.setText("");

		hPanelDialModif.add(tDialModifRep);

		hPanelDialModif2.add(btnCancelModif);
		hPanelDialModif2.setSpacing(5);
		hPanelDialModif2.add(btnAceptarModif);

		vPanelDailModif.add(hPanelDialModif);
		vPanelDailModif.add(hPanelDialModif2);

		dialogComentario.setWidget(vPanelDailModif);

		dialogComentario.show();
		dialogComentario.center();

		return dialogComentario;
	}

	public static String formatearFecha(String fechaStr) 
	{
		//return fechaStr.substring(0, fechaStr.length()-2);
		return fechaStr.substring(0, 19);
	}

	public static void setearTecnicoDeOrden(Orden orden, ListBox listBoxTecnicoAsignado) 
	{
		String textoTecnicoAsignado = "";
		int idTecnicoAsignado = 0;

		if(orden.getTecnicoAsignado()==null)
		{
			textoTecnicoAsignado = UtilOrden.personaAsignadaOrden(1);
		}
		else
		{
			textoTecnicoAsignado = orden.getTecnicoAsignado().toString();
			idTecnicoAsignado = orden.getTecnicoAsignado().getId();
			
			for(int i = 0; i < listBoxTecnicoAsignado.getItemCount(); i++)
			{
				
				if(!listBoxTecnicoAsignado.getValue(i).equals(UtilOrden.personaAsignadaOrden(1)) &&
						Integer.valueOf(listBoxTecnicoAsignado.getValue(i)) == idTecnicoAsignado)
				{
					listBoxTecnicoAsignado.setSelectedIndex(i);
				}		
			}	
		}
	}
}

