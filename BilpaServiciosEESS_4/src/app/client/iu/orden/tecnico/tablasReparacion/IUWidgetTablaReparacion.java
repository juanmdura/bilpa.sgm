package app.client.iu.orden.tecnico.tablasReparacion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import app.client.dominio.Contador;
import app.client.dominio.Reparacion;
import app.client.dominio.ReparacionSurtidor;
import app.client.dominio.RepuestoLinea;
import app.client.dominio.Solucion;
import app.client.dominio.UtilContador;
import app.client.iu.orden.tecnico.IUSeguimientoTecnico;
import app.client.iu.orden.tecnico.dialogoAgregarSolucion.IUWidgetEditarSolucion;
import app.client.iu.orden.tecnico.dialogoAgregarSolucion.IUWidgetSolucion;
import app.client.utilidades.Constants;
import app.client.utilidades.UtilOrden;
import app.client.utilidades.UtilUI;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PushButtonSolucion;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

@SuppressWarnings("deprecation")
public class IUWidgetTablaReparacion {

	IUSeguimientoTecnico iuSegTec;
	GlassPopup glass;

	//Dialogo eliminar repuesto
	//========================================================	
	private VerticalPanel vpDialRemRep= new VerticalPanel();
	private HorizontalPanel hpDialDialRemRep = new HorizontalPanel();
	private Label lblTituloDialRemRep = new Label("Seguro desea eliminar este repuesto?");
	private HTML htmlDescDialRemRep = new HTML();
	private HorizontalPanel hpDialRemRep = new HorizontalPanel();
	private HorizontalPanel hp2DialRemRep = new HorizontalPanel();
	private DialogBox dialogBoxRemRep = new DialogBox();

	private int numeroDeFilaRepAEliminar;
	private Reparacion reparacionAEliminarUnRepuesto;
	private FlexTable tableRepuestosAEliminarUnRepuesto;

	private Reparacion reparacionAEliminarSolucion;

	private final String NA = "N/A";

	private Button btnElimDialRemRep = new Button("Eliminar",new ClickHandler() {
		public void onClick(ClickEvent event) {
			dialogBoxRemRep.hide(true);
			glass.hide();
			removerRepuestoDeTabla();
		}
	});

	private Button btnCancelDialRemRep = new Button("Cancelar",new ClickHandler() {
		public void onClick(ClickEvent event) {
			dialogBoxRemRep.hide(true);
			glass.hide();
		}
	});

	public IUWidgetTablaReparacion(IUSeguimientoTecnico iuSegTec, GlassPopup glass){
		this.iuSegTec = iuSegTec; 
		this.glass = glass;
	}

	public void setearRepYSolATabla(final Reparacion rep, VerticalPanel panelVDatosTecnicos){
		if (rep.tieneSoluciones())
		{

			Set<Solucion> solucionesOrdenedas = new TreeSet<Solucion>();
			solucionesOrdenedas.addAll(rep.getSoluciones());

			VerticalPanel vp = new VerticalPanel();
			Label lblActivo = new Label(UtilOrden.tiposDeActivos(rep.getActivo()) + ": " + rep.getActivo().toString());

			lblActivo.setStyleName("Negrita");
			vp.setSpacing(5);
			vp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			vp.add(lblActivo);

			final FlexTable tableSoluciones = new FlexTable();
			tableSoluciones.setCellSpacing(3);
			tableSoluciones.setCellPadding(1);
			agregarFilaTituloTablaSoluciones(rep, tableSoluciones);
			tableSoluciones.setWidth("100%");

			int i = 1;
			boolean reparacionTieneRepuestos = false;
			FlexTable tableRepuestos = crearTableRepuestos(rep);
			Set<RepuestoLinea> repuestosLineasAgregados = new HashSet<RepuestoLinea>();
			
			for (Solucion s : solucionesOrdenedas) {
				for(RepuestoLinea rl : s.obtenerRepuestosLinea(iuSegTec.orden.getRepuestosLineas())){
					if(rl.getSolucion() != null){ //EL REPUESTO TIENE SOLUCION
						reparacionTieneRepuestos = true;
						agregarFilaRepuestoEnTabla(tableRepuestos, rl, s, i);
						repuestosLineasAgregados.add(rl);
					}
				}

				TextBox txtContInicio = new TextBox();
				TextBox txtContFin = new TextBox();
				txtContInicio.setSize("100%", "22px");
				txtContFin.setSize("100%", "22px");

				String telefonica = setearTelefonica(s);

				tableSoluciones.setText(i, 0, s.getNumeroEnReparacion()+"");

				Label lblFallaTecnica = new Label(s.getFallaTecnica().getDescripcion());
				Label lblTarea = new Label(s.getTarea().getDescripcion());
				Label lblTele = new Label(telefonica);
				
				lblFallaTecnica.setTitle(s.getFallaTecnica().getDescripcion());
				lblTarea.setTitle(s.getTarea().getDescripcion());

				int col;
				if (rep.getActivo().getTipo() == 1)//SI ES SURTIDOR
				{
					ReparacionSurtidor repSurtidor = (ReparacionSurtidor) rep;
					Contador contador = repSurtidor.buscarContador(s);

					agregarContadoresATablaSol(tableSoluciones, i,txtContInicio, txtContFin, contador);

					col = 4;
				}
				else//ES BOMBA, CANO O TANQUE
				{
					col = 1;				
				}

				tableSoluciones.setWidget(i, col, lblFallaTecnica);
				tableSoluciones.setWidget(i, col+1, lblTarea);
				tableSoluciones.setWidget(i, col+2, lblTele);

				Image img2 = new Image("img/edit.png");
				PushButtonSolucion btnEditarSolucion = new PushButtonSolucion(img2);
				btnEditarSolucion.setSolucion(s);

				btnEditarSolucion.setSize("15px", "15px");
				img2.setSize("15px", "15px");

				tableSoluciones.setWidget(i, col+3, btnEditarSolucion);
				
				btnEditarSolucion.addClickHandler(new ClickHandler() {
					
					public void onClick(ClickEvent event) {
						PushButtonSolucion pushButtonSolucion = (PushButtonSolucion) event.getSource();
						IUWidgetSolucion widgetSolucion = new IUWidgetEditarSolucion(iuSegTec.orden.buscarReparacion(pushButtonSolucion.getSolucion().getReparacion().getId()), iuSegTec.getPopUp(), glass,
								iuSegTec, Constants.OPERACION_WIDGET_SOLUCION.VIENE_DE_IU_SEG_TEC, iuSegTec.getSesion(), pushButtonSolucion.getSolucion());
						
						DialogBox dialogo = widgetSolucion.getDialogo();
						dialogo.show();
						dialogo.center();
						dialogo.setPopupPosition(iuSegTec.margenHorizontal, iuSegTec.margenVertical);
						
					}
				});
				
				
				Image img3 = new Image("img/menos.png");
				PushButtonSolucion btnEliminar = new PushButtonSolucion(img3);
				btnEliminar.setSolucion(s);
				btnEliminar.setRow(tableSoluciones.getRowCount()-1);

				btnEliminar.setSize("15px", "15px");
				img3.setSize("15px", "15px");
				
				tableSoluciones.setWidget(i, col+4, btnEliminar);
				
				btnEliminar.addClickHandler(new ClickHandler() {
					
					public void onClick(ClickEvent event) {
						PushButtonSolucion pushButtonSolucion = (PushButtonSolucion) event.getSource();

						glass.show();
						reparacionAEliminarSolucion = rep;
						DialogBox db = dialElimSolucion(pushButtonSolucion.getSolucion(), pushButtonSolucion.getRow());
						db.center();
						db.show();
						
					}
				});
				
				
				i++;
				UtilUI.setearCssATabla(tableSoluciones, i);
			}

			for(RepuestoLinea rl : iuSegTec.orden.getRepuestosLineas()){
				if(rl.getActivo().getId() == rep.getActivo().getId() && rl.getSolucion() == null){//LOS REPUESTOS QUE NO TIENEN SOLUCIONES ASOCIADAS O SOLUCIONES QUE ESTAN SOLO EN MEMORIA
					reparacionTieneRepuestos = true;
					agregarFilaRepuestoEnTabla(tableRepuestos, rl, null, i);
				}
			}

			vp.add(tableSoluciones);			
			if(reparacionTieneRepuestos){
				vp.add(tableRepuestos);
			}
			vp.setWidth("100%");
			panelVDatosTecnicos.add(vp);

			VerticalPanel vpSpace = new VerticalPanel();
			vpSpace.setHeight("30px");
			panelVDatosTecnicos.add(vpSpace);
		}
	}

	private DialogBox dialElimSolucion(final Solucion s, final int row) {
		VerticalPanel vpDialRemSol= new VerticalPanel();
		HorizontalPanel hpDialDialRemSol = new HorizontalPanel();
		Label lblTituloDialRemSol = new Label("Seguro desea eliminar esta reparación?");
		Label lblTituloDialRemSol2 = new Label("Se eliminarán también todos sus repuestos asociados.");
		HTML htmlDescDialRemSol = new HTML();
		HorizontalPanel hpDialRemSol = new HorizontalPanel();
		HorizontalPanel hp2DialRemSol = new HorizontalPanel();
		
		vpDialRemSol.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hpDialDialRemSol.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		vpDialRemSol.add(lblTituloDialRemSol);
		vpDialRemSol.add(lblTituloDialRemSol2);
		
		lblTituloDialRemSol.setStyleName("Negrita");
		lblTituloDialRemSol2.setStyleName("Negrita");
		final DialogBox dialogBoxRemSol = new DialogBox();
		dialogBoxRemSol.setAutoHideEnabled(true);

		Button btnElimDialRemSol = new Button("Eliminar",new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBoxRemSol.hide(true);
				glass.hide();
				removerSolucionDeTabla(s, row);
			}
		});

		Button btnCancelDialRemSol = new Button("Cancelar",new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBoxRemSol.hide(true);
				glass.hide();
			}
		});
		
		btnElimDialRemSol.setWidth("100px");
		btnCancelDialRemSol.setWidth("100px");

		htmlDescDialRemSol.setWidth("500px");

		hpDialRemSol.setSpacing(5);
		hpDialRemSol.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hpDialRemSol.add(htmlDescDialRemSol);

		if(s !=null)
		{
			htmlDescDialRemSol.setText(s.getFallaTecnica() + " - " + s.getTarea());
		}

		hp2DialRemSol.add(btnCancelDialRemSol);
		hp2DialRemSol.setSpacing(5);		
		hp2DialRemSol.add(btnElimDialRemSol);

		vpDialRemSol.add(hpDialRemSol);
		vpDialRemSol.add(hp2DialRemSol);

		dialogBoxRemSol.setWidget(vpDialRemSol);
		return dialogBoxRemSol;
	}

	private void removerSolucionDeTabla(Solucion s, int row) {
		for(Reparacion r : iuSegTec.orden.getReparaciones()){
			if(r.getActivo().equals(reparacionAEliminarSolucion.getActivo())){
				r.getSoluciones().remove(s);

				ArrayList<RepuestoLinea> repuestosLineaAEliminar = new ArrayList<RepuestoLinea>();
				for(RepuestoLinea rl : iuSegTec.orden.getRepuestosLineas()){
					if(rl.getSolucion().getId() == s.getId()){
						repuestosLineaAEliminar.add(rl);
					}
				}
				iuSegTec.orden.getRepuestosLineas().removeAll(repuestosLineaAEliminar);
			}
		}
		iuSegTec.cargarSoluciones();
	}

	private String setearTelefonica(Solucion s) {
		String telefonica;
		if(s.isTelefonica()){
			telefonica = "Si";
		}else{
			telefonica = "No";
		}
		return telefonica;
	}

	private void agregarContadoresATablaSol(FlexTable tableSoluciones, int i, TextBox txtContInicio, TextBox txtContFin, Contador contador) {
		if (contador != null &&	contador.getPico() != null)//CONTADORES
		{
			txtContInicio.setText(contador.getInicio() +"");
			txtContFin.setText(contador.getFin() + "");

			Label labelContador = new Label(contador.getPico().toString());
			labelContador.setWidth("200px");

			tableSoluciones.setWidget(i, 1, labelContador);						
			tableSoluciones.setWidget(i, 2, txtContInicio);
			tableSoluciones.setWidget(i, 3, txtContFin);

			UtilContador utilCont = new UtilContador(contador, txtContInicio, txtContFin);
			iuSegTec.listaUtilCont.add(utilCont);

		}else{
			tableSoluciones.setText(i, 1, NA);
			tableSoluciones.setText(i, 2, NA);
			tableSoluciones.setText(i, 3, NA);
		}
	}

	private void agregarFilaTituloTablaSoluciones(Reparacion rep, FlexTable ft) {		
		if (rep.tieneSoluciones())
		{
			//Setea los nombres de las columnas de la tabla		
			HTML nroHTML = new HTML("Nro");
			HTML picoHTML = new HTML("Pico");
			HTML inicioHTML = new HTML("Ini. Tot.");
			HTML finHTML = new HTML("Fin Tot.");
			HTML fallaHTML = new HTML("Falla Detectada");
			HTML tareaRealizadaHTML = new HTML("Tarea Realizada");
			HTML telefonica = new HTML("Tel");

			nroHTML.setWidth("30px");
			picoHTML.setWidth("140px");
			inicioHTML.setWidth("60px");
			finHTML.setWidth("60px");
			fallaHTML.setWidth("400px");
			tareaRealizadaHTML.setWidth("400px");
			telefonica.setWidth("30px");

			ft.getRowFormatter().setStyleName(0, "CabezalTabla");

			if (rep.getActivo().getTipo() == 1)
			{
				ft.setWidget(0, 0, nroHTML);
				ft.setWidget(0, 1, picoHTML);
				ft.setWidget(0, 2, inicioHTML);
				ft.setWidget(0, 3, finHTML);
				ft.setWidget(0, 4, fallaHTML);
				ft.setWidget(0, 5, tareaRealizadaHTML);		
				ft.setWidget(0, 6, telefonica);			
			}
			else
			{
				fallaHTML.setWidth("574px");
				tareaRealizadaHTML.setWidth("574px");
				ft.setWidget(0, 0, nroHTML);
				ft.setWidget(0, 1, fallaHTML);
				ft.setWidget(0, 2, tareaRealizadaHTML);		
				ft.setWidget(0, 3, telefonica);
			}
		}
	}

	private void agregarFilaTituloRepuestos(FlexTable tableRepuestos) {
		tableRepuestos.removeAllRows();
		tableRepuestos.getRowFormatter().setStyleName(0, "CabezalTabla");

		HTML htmlNro = new HTML("Nro");
		HTML htmlRepuesto = new HTML("Repuesto");
		HTML htmlActivo = new HTML("Activo");
		HTML htmlCantidad = new HTML("Cant");
		HTML htmlNuevo = new HTML("Nuevo");

		htmlNro.setWidth("30px");
		htmlActivo.setWidth("293px");
		htmlRepuesto.setWidth("727px");
		htmlCantidad.setWidth("50px");
		htmlNuevo.setWidth("50px");

		tableRepuestos.setWidget(0, 0, htmlNro);
		tableRepuestos.setWidget(0, 1, htmlActivo);
		tableRepuestos.setWidget(0, 2, htmlRepuesto);
		tableRepuestos.setWidget(0, 3, htmlCantidad);
		tableRepuestos.setWidget(0, 4, htmlNuevo);
	}

	private FlexTable crearTableRepuestos(final Reparacion reparacion){
		final FlexTable tableRepuestos = new FlexTable();
		agregarFilaTituloRepuestos(tableRepuestos);		
		tableRepuestos.setWidth("100%");

		tableRepuestos.setCellSpacing(3);
		tableRepuestos.setCellPadding(1);
		tableRepuestos.addTableListener(new TableListener()
		{
			public void onCellClicked(SourcesTableEvents sender, int row, int cell) 
			{
				if (cell == 5)
				{
					glass.show();
					tableRepuestosAEliminarUnRepuesto = tableRepuestos;
					numeroDeFilaRepAEliminar = row;
					reparacionAEliminarUnRepuesto = reparacion;
					DialogBox db = dialElimRepuesto();
					db.center();
					db.show();
				}
			}
		});
		return tableRepuestos;
	}

	private void agregarFilaRepuestoEnTabla(FlexTable tableRepuestos, RepuestoLinea rpLi, Solucion solucion, int i) {
		Label tituloRep = new Label("Repuestos utilizados");
		tituloRep.setStyleName("SubTitulo");

		Label lblNro;
		if(solucion != null){
			lblNro = new Label(solucion.getNumeroEnReparacion()+"");
		}else{
			lblNro = new Label(NA);
		}

		Label lblActivo = new Label(rpLi.getActivo().toString());
		Label lblRep = new Label(rpLi.getRepuesto().toString());
		Label lblCant = new Label(rpLi.getCantidad()+"");
		Label lblNuevo = new Label();

		lblNro.setWidth("30px");
		lblActivo.setWidth("293px");
		lblRep.setWidth("805px");
		lblCant.setWidth("50px");
		lblNuevo.setWidth("50px");

		if (rpLi.isNuevo())
		{
			lblNuevo.setText("Si");				
		}
		else
		{
			lblNuevo.setText("No");
		}

		int numRows = tableRepuestos.getRowCount();

		rpLi.setRow(numRows);

		tableRepuestos.setWidget(numRows, 0, lblNro);
		tableRepuestos.setWidget(numRows, 1, lblActivo);
		tableRepuestos.setWidget(numRows, 2, lblRep);
		tableRepuestos.setWidget(numRows, 3, lblCant);
		tableRepuestos.setWidget(numRows, 4, lblNuevo);

		Image img2 = new Image("img/menos.png");
		PushButton btn2 = new PushButton(img2);

		btn2.setSize("15px", "15px");
		img2.setSize("15px", "15px");

		tableRepuestos.setWidget(numRows, 5, btn2);
		rpLi.setRow(numRows);

		if(i %2==0){
			tableRepuestos.getRowFormatter().addStyleName(numRows, "FilaTabla2");				
		}else{
			tableRepuestos.getRowFormatter().addStyleName(numRows, "FilaTabla1");
		}
	}

	public RepuestoLinea buscarRepLineaPorFila()
	{
		for (RepuestoLinea rl : iuSegTec.orden.getRepuestosLineas())//REPUESTOS SIN SOLUCIONES
		{
			if (rl.getActivo().equals(reparacionAEliminarUnRepuesto.getActivo()) && rl.getRow() == numeroDeFilaRepAEliminar)
			{
				return rl;
			}
		}
		return null;
	}

	public void removerRepuestoDeTabla() 
	{
		RepuestoLinea rlARemover = buscarRepLineaPorFila();
		if(rlARemover!=null)
		{
			iuSegTec.repuetosLineaARemover.add(rlARemover);
			iuSegTec.orden.getRepuestosLineas().remove(rlARemover);
			tableRepuestosAEliminarUnRepuesto.removeRow(numeroDeFilaRepAEliminar);
		}
	}

	public DialogBox dialElimRepuesto()
	{
		vpDialRemRep.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hpDialDialRemRep.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		vpDialRemRep.add(lblTituloDialRemRep);
		lblTituloDialRemRep.setStyleName("Negrita");

		dialogBoxRemRep.setAutoHideEnabled(true);

		btnElimDialRemRep.setWidth("100px");
		btnCancelDialRemRep.setWidth("100px");

		htmlDescDialRemRep.setWidth("500px");

		hpDialRemRep.setSpacing(5);
		hpDialRemRep.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hpDialRemRep.add(htmlDescDialRemRep);

		RepuestoLinea rl = buscarRepLineaPorFila();
		if(rl!=null)
		{
			htmlDescDialRemRep.setText(rl.getRepuesto().getDescripcion());
		}

		hp2DialRemRep.add(btnCancelDialRemRep);
		hp2DialRemRep.setSpacing(5);		
		hp2DialRemRep.add(btnElimDialRemRep);

		vpDialRemRep.add(hpDialRemRep);
		vpDialRemRep.add(hp2DialRemRep);

		dialogBoxRemRep.setWidget(vpDialRemRep);
		return dialogBoxRemRep;
	}

}
