package app.client.iu.listados;

import java.util.ArrayList;
import java.util.Date;

import app.client.ProyectoBilpa;
import app.client.dominio.Persona;
import app.client.dominio.data.DatoConsultaHistoricoOrdenesTecnico;
import app.client.iu.rightClick.Entero;
import app.client.iu.rightClick.LabelComparable;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;
import app.client.utilidades.utilObjects.SortableTable;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
	
public class IUConsultaHistoricoOrdenesPorTecnico extends Composite{

	private VerticalPanel vPanelBig = new VerticalPanel();
	private HorizontalPanel hPanelPrincipal = new HorizontalPanel();
	
	private VerticalPanel panelVtitulo= new VerticalPanel();
	private VerticalPanel panelVSubtitulo= new VerticalPanel();
	
	private Label lblTituloPrincipal = new Label("Histórico de órdenes por técnico");
	private Label lblSubTituloPrincipal = new Label("Seleccione una rango de fechas para listar la cantidad de órdenes reparadas por cada técnico");
	
	private SortableTable tableDatos = new SortableTable();
	
	DateTimeFormat dateFormat = DateTimeFormat.getLongDateFormat();
	DateBox dPinicio = new DateBox();
	DateBox dPFin = new DateBox();
	
	Date inicio = new Date();
	Date fin = new Date();
	
	private VerticalPanel panelVFechaInicio= new VerticalPanel();
	private VerticalPanel panelVFechaFin= new VerticalPanel();
	
	private Label lblFechaInicio = new Label("Fecha de Inicio");
	private Label lblFechaFin = new Label("Fecha de Fin");
	
	private VerticalPanel panelVBotones= new VerticalPanel();
	
	Button btnVer = new Button("Ver");
	Button btnBorrar = new Button("Borrar");

	private PopupCargando popUp = new PopupCargando("Cargando...");
	
	private GlassPopup glass = new GlassPopup();
	
	private Persona sesion;

	public VerticalPanel getPrincipalPanel() {
		return vPanelBig;
	}

	public IUConsultaHistoricoOrdenesPorTecnico(Persona persona){
		dPinicio.addValueChangeHandler(new ValueChangeHandler<Date>() {
			public void onValueChange(ValueChangeEvent<Date> event) {
				inicio = event.getValue();
			}
		});

		dPFin.addValueChangeHandler(new ValueChangeHandler<Date>() {
			public void onValueChange(ValueChangeEvent<Date> event) {
				fin = event.getValue();
				
			}
		});
		
		btnVer.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				fechas();
			}
		});
		
		btnBorrar.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				borrar();
			}
		});
		
		this.sesion = persona;
		dPinicio.setFormat(new DateBox.DefaultFormat(dateFormat));
		Date d = new Date();
		dPinicio.setValue(d, true);
		dPFin.setValue(d, true);

		setearWidgets();
		agregarWidgets(); 
	}


	private void setearWidgets() {
		lblTituloPrincipal.setStyleName("Titulo");
		lblSubTituloPrincipal.setStyleName("SubTitulo");
		
		vPanelBig.setSpacing(10);
		
		tableDatos.setWidth("500px");
		tableDatos.setCellSpacing(5);
		tableDatos.setCellPadding(3);
		
		btnVer.setWidth("100px");
		btnBorrar.setWidth("100px");
		tableDatos.getRowFormatter().addStyleName(0, "CabezalTabla");
		tableDatos.addColumnHeader("Técnico", 0);
		tableDatos.addColumnHeader("Órdenes Reparadas", 1);
		hPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hPanelPrincipal.setSpacing(20);
		panelVFechaInicio.setSpacing(10);
		panelVFechaFin.setSpacing(10);
		panelVBotones.setSpacing(10);
		
	}

	private void agregarWidgets() {
		
		hPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelBig.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panelVtitulo.add(lblTituloPrincipal);
		panelVSubtitulo.add(lblSubTituloPrincipal);
		
		hPanelPrincipal.add(tableDatos);
		lblFechaInicio.setStyleName("Negrita");
		lblFechaFin.setStyleName("Negrita");
		
		panelVFechaInicio.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panelVFechaFin.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		panelVFechaInicio.add(lblFechaInicio);
		panelVFechaInicio.add(dPinicio);
		panelVFechaFin.add(lblFechaFin);
		panelVFechaFin.add(dPFin);
		
		hPanelPrincipal.add(panelVFechaInicio);
		hPanelPrincipal.add(panelVFechaFin);
		hPanelPrincipal.add(panelVBotones);
		
		panelVBotones.add(btnVer);
		panelVBotones.add(btnBorrar);
		
		vPanelBig.add(panelVtitulo);
		vPanelBig.add(panelVSubtitulo);
		vPanelBig.add(hPanelPrincipal);
	}



	private void borrar() 
	{
		for(int i = 1 ; i < tableDatos.getRowCount() ; i++)
		{
			tableDatos.removeAllRowsExceptHeader();
		}
	}
	
	private void fechas(){
		popUp.show();
		if(inicio.getTime() < fin.getTime()){
			ProyectoBilpa.greetingService.obtenerHistoricoOrdenesTecnicosDelimitado(inicio, fin,new AsyncCallback<ArrayList<DatoConsultaHistoricoOrdenesTecnico>>() {

				public void onFailure(Throwable caught) 
				{
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener la consulta");
					vpu.showPopUp();
				}

				public void onSuccess(ArrayList<DatoConsultaHistoricoOrdenesTecnico> result) 
				{
					for (int i=0; i<result.size();i++)
					{
						DatoConsultaHistoricoOrdenesTecnico dt = (DatoConsultaHistoricoOrdenesTecnico) result.get(i) ;

						LabelComparable tecnico = new LabelComparable(new Entero(1, i), dt.getNombreTecnico());
						LabelComparable cantidad = new LabelComparable(new Entero(2, i), dt.getCantidadOrdenes()+"");
						int a = i+1;
						
						if(i %2==0)
						{
							tableDatos.getRowFormatter().addStyleName(i+1, "FilaTabla1");				
						}
						else
						{
							tableDatos.getRowFormatter().addStyleName(i+1, "FilaTabla2");
						}
						
						tableDatos.setValue(a, 0, tecnico);
						tableDatos.setValue(a, 1, cantidad);
					}       
					popUp.hide();
				}
			});
		}
		else
		{
			popUp.hide();
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "La fecha de fin debe ser mayor a la fecha de inicio");
			vpu.showPopUp();
		}
	}
}
