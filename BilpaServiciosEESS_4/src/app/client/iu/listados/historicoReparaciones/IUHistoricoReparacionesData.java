package app.client.iu.listados.historicoReparaciones;

import java.util.ArrayList;
import java.util.List;

import app.client.ProyectoBilpa;
import app.client.dominio.Contador;
import app.client.dominio.Estacion;
import app.client.dominio.Reparacion;
import app.client.dominio.ReparacionSurtidor;
import app.client.dominio.RepuestoLinea;
import app.client.dominio.Solucion;
import app.client.dominio.data.DatoListadoReparaciones;
import app.client.dominio.data.DatoOrdenesActivasEmpresa;
import app.client.iu.rightClick.AdvSortableTable;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.UtilCss;
import app.client.utilidades.UtilOrden;
import app.client.utilidades.UtilUI;
import app.client.utilidades.utilObjects.DecoratorPanelAdv;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;
import app.client.utilidades.utilObjects.SortableTable;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class IUHistoricoReparacionesData 
{
	HTML htmlTitulo = new HTML();
	HTML lblTipoActivo = new HTML("Tipo Activo");
	HTML lblActivo = new HTML("Activo");

	HorizontalPanel hPanelCombos = new HorizontalPanel();
	VerticalPanel vPanelPrincipal = new VerticalPanel();
	VerticalPanel vPanelActivos = new VerticalPanel();

	List<DatoListadoReparaciones> listaActivos = new ArrayList<DatoListadoReparaciones>();
	ListBox listBoxTipoActivos = new ListBox();
	ListBox listBoxActivos = new ListBox();
	ArrayList<DecoratorPanelAdv> decoratorPanels = new ArrayList<DecoratorPanelAdv>();

	int tipoActivo;
	int activoSeleccionado;
	int idEstacion;

	private PopupCargando popUp = new PopupCargando("Cargando...");
	DatoOrdenesActivasEmpresa orden;
	
	private GlassPopup glass = new GlassPopup();

	public IUHistoricoReparacionesData(DatoOrdenesActivasEmpresa orden) 
	{
		this.orden = orden;
		popUp.show();

		listBoxTipoActivos.addChangeHandler(new ChangeHandler() 
		{
			public void onChange(ChangeEvent event) 
			{
				cambioTipoActivo();				
			}
		});

		listBoxActivos.addChangeHandler(new ChangeHandler() 
		{
			public void onChange(ChangeEvent event) 
			{
				cambioActivo();				
			}
		});

		vPanelPrincipal.setSpacing(10);
		hPanelCombos.setSpacing(10);

		vPanelPrincipal.setWidth("500px");
		listBoxTipoActivos.setWidth("240px");
		hPanelCombos.setWidth("720px");

		listBoxActivos.setWidth("240px");
		listBoxActivos.setVisibleItemCount(1);
		listBoxActivos.setTitle("Lista de Activos");

		lblActivo.setStyleName("Negrita");
		lblTipoActivo.setStyleName("Negrita");
		htmlTitulo.setStyleName("Subtitulo");

		htmlTitulo.setText("Orden " + orden.getNumero() + " finalizada el " + orden.getFecha().substring(0, orden.getFecha().length()-2) + " por " + orden.getTecnico() );

		cargarTiposActivos();
		obtenerActivosPorTipo();

		hPanelCombos.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hPanelCombos.add(lblTipoActivo);
		hPanelCombos.add(listBoxTipoActivos);
		hPanelCombos.add(lblActivo);
		hPanelCombos.add(listBoxActivos);

		vPanelPrincipal.add(new HTML("______________________________________________________________________________________________________"));
		vPanelPrincipal.add(htmlTitulo);
		vPanelPrincipal.add(hPanelCombos);
	}

	public VerticalPanel getPrincipalPanel() 
	{
		return vPanelPrincipal;
	}

	private void cambioTipoActivo()
	{
		tipoActivo = Integer.valueOf(listBoxTipoActivos.getValue(listBoxTipoActivos.getSelectedIndex()));
		listBoxActivos.clear();
		listBoxActivos.addItem("Todos", String.valueOf(0));
		
		if (tipoActivo == 0)
		{
			for (DatoListadoReparaciones a : listaActivos)
			{
				listBoxActivos.addItem(a.getReparacion().getActivo().toString(), String.valueOf(a.getReparacion().getActivo().getId()));
			}
		}
		else 
		{
			for (DatoListadoReparaciones a : listaActivos)
			{
				if (a.getReparacion().getActivo().getTipo() == tipoActivo)
				{
					listBoxActivos.addItem(a.getReparacion().getActivo().toString(), String.valueOf(a.getReparacion().getActivo().getId()));
				}
			}
		}
		
		cambioActivo();
		
		if (listBoxActivos.getItemCount() > 1)
		{
			listBoxActivos.setSelectedIndex(0);
		}
		else
		{
			listBoxActivos.clear();
			vPanelActivos.clear();
			listBoxActivos.addItem("No hay " + UtilOrden.tiposDeActivosPlural(tipoActivo) + " reparados", String.valueOf(0));
		}
	}

	private void cambioActivo() 
	{
		activoSeleccionado = Integer.valueOf(listBoxActivos.getValue(listBoxActivos.getSelectedIndex()));
		vPanelActivos.clear();

		if (activoSeleccionado > 0)
		{
			for (DecoratorPanelAdv d : decoratorPanels)
			{
				if (d.getId() == activoSeleccionado)
				{
					vPanelActivos.add(d);
				}
			}
		}
		else if (activoSeleccionado == 0)
		{
			for (DecoratorPanelAdv d : decoratorPanels)
			{
				vPanelActivos.add(d);
				UtilUI.agregarEspacioEnBlanco(vPanelActivos, 3);
			}
		}
	}

	private void cargarTiposActivos()
	{
		listBoxTipoActivos.clear();
		ProyectoBilpa.greetingService.obtenerTiposDeActivos(orden.getIdEmpresa(), false, new AsyncCallback<ArrayList<String>>() {
			public void onFailure(Throwable caught) 
			{
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener los tipos de activos");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<String> tiposActivo) 
			{
				listBoxTipoActivos.addItem("Todos",String.valueOf("0"));
				for (int i=0; i < tiposActivo.size(); i++)
				{
					listBoxTipoActivos.addItem(tiposActivo.get(i),String.valueOf(i+1));
				}	
			}
		});		
	}

	private void obtenerActivosPorTipo()
	{
		listBoxActivos.clear();
		listaActivos.clear();

		Estacion estacion = new Estacion();
		estacion.setId(orden.getIdEmpresa());

		ProyectoBilpa.greetingService.obtenerDatoListadoActivosPorTipo(orden, new AsyncCallback<ArrayList<DatoListadoReparaciones>>() {
			public void onSuccess(ArrayList<DatoListadoReparaciones> result) 
			{
				listBoxActivos.addItem("Todos",String.valueOf(0));
				for (DatoListadoReparaciones dtl : result)
				{
					listaActivos.add(dtl);	
					dibujarTablaSoluciones(dtl);
				}	
			}

			public void onFailure(Throwable caught) 
			{
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener activos por tipo");
				vpu.showPopUp();
			}
		});
	}

	private void dibujarTablaSoluciones(DatoListadoReparaciones dtl) 
	{		
		Reparacion r = dtl.getReparacion();
		ArrayList<RepuestoLinea> repuestos = dtl.getRepuestosLinea();

		listBoxActivos.addItem(r.getActivo().toString(),String.valueOf(r.getActivo().getId()));

		VerticalPanel vpDecorator = new VerticalPanel();
		DecoratorPanelAdv decorator = new DecoratorPanelAdv();
		decoratorPanels.add(decorator);
		decorator.setId(dtl.getReparacion().getActivo().getId());

		vpDecorator.setWidth("720px");
		vpDecorator.setSpacing(8);

		Label htmlTituloRep = new Label (UtilOrden.tiposDeActivos(r.getActivo()) + ": " + r.getActivo().toString());
		htmlTituloRep.setStyleName("Subtitulo");
		vpDecorator.add(htmlTituloRep);

		if (r.getSoluciones().size() > 0)
		{
			AdvSortableTable tableFallaTarea = new AdvSortableTable();
			tableFallaTarea.setWidth("700px");

			int fila = 1;

			if (r.tienePicosReparados())
			{
				tablaPicos(r, tableFallaTarea, fila);
			}
			else
			{
				tablaSinPicos(r, tableFallaTarea, fila);
			}

			vpDecorator.add(tableFallaTarea);
			dibujarTablaRepuestos(repuestos, vpDecorator);
		}
		else
		{
			Label htmlNoHaySol = new Label (" Este activo no fue reparado");
			htmlNoHaySol.setStyleName("Negrita");
			htmlNoHaySol.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			vpDecorator.add(htmlNoHaySol);
		}
		
		decorator.add(vpDecorator);
		UtilUI.agregarEspacioEnBlanco(vPanelActivos, 3);
		vPanelActivos.add(decorator);
		vPanelPrincipal.add(vPanelActivos);
	}

	private void tablaPicos(Reparacion r, AdvSortableTable tableFallaTarea,	int fila) 
	{
		agregarFilaTituloFallaTarea(tableFallaTarea, true);
		String textoPico = "";
		for (Solucion s : r.getSoluciones())
		{
			if(r.getClass().equals(ReparacionSurtidor.class))
			{
				ReparacionSurtidor rs = (ReparacionSurtidor)r;
				Contador contador = rs.buscarContador(s);

				if (contador != null && contador.getPico() != null)
				{
					textoPico = contador.getPico().toString();
				}
				else
				{
					textoPico = "N/A";
				}
			}

			UtilCss.aplicarEstiloATabla(tableFallaTarea, fila+1);
			Label lblPico = new Label(textoPico);
			Label lblFalla = new Label(s.getFallaTecnica().getDescripcion());
			Label lblTarea = new Label(s.getTarea().getDescripcion());

			tableFallaTarea.setWidget(fila, 0, lblPico);
			tableFallaTarea.setWidget(fila, 1, lblFalla);
			tableFallaTarea.setWidget(fila, 2, lblTarea);
			fila++;
		}
	}

	private void tablaSinPicos(Reparacion r, AdvSortableTable tableFallaTarea,	int fila) 
	{
		agregarFilaTituloFallaTarea(tableFallaTarea, false);
		for (Solucion s : r.getSoluciones())
		{
			UtilCss.aplicarEstiloATabla(tableFallaTarea, fila+1);
			Label lblFalla = new Label(s.getFallaTecnica().getDescripcion());
			Label lblTarea = new Label(s.getTarea().getDescripcion());

			tableFallaTarea.setWidget(fila, 0, lblFalla);
			tableFallaTarea.setWidget(fila, 1, lblTarea);
			fila++;
		}
	}

	private void agregarFilaTituloFallaTarea(SortableTable table, boolean tienePicos) 
	{		
		table.getRowFormatter().addStyleName(0, "CabezalTabla");

		if (tienePicos)
		{
			table.addColumnHeader("Pico", 0);
			table.addColumnHeader("Falla Detectada", 1);
			table.addColumnHeader("Tarea Realizada", 2);			
		}
		else
		{
			table.addColumnHeader("Falla Detectada", 0);
			table.addColumnHeader("Tarea Realizada", 1);
		}
	}

	private void agregarFilaTituloRepuestos(SortableTable table) 
	{		
		table.getRowFormatter().addStyleName(0, "CabezalTabla");

		table.addColumnHeader("Repuestos", 0);
		table.addColumnHeader("Nro Serie", 1);
		table.addColumnHeader("Precio", 2);
		table.addColumnHeader("Cantidad", 3);
	}

	private void dibujarTablaRepuestos(ArrayList<RepuestoLinea> repuestos, VerticalPanel vpDecorator) 
	{
		AdvSortableTable tableRepuestos = new AdvSortableTable();
		tableRepuestos.setWidth("700px");

		agregarFilaTituloRepuestos(tableRepuestos);

		if (repuestos.size()>0)
		{
			for (int i=0; i < repuestos.size(); i++)
			{
				int fila = i+1;

				UtilCss.aplicarEstiloATabla(tableRepuestos, fila+1);

				Label lblDesc = new Label(repuestos.get(i).getRepuesto().getDescripcion());
				Label lblNroSerie = new Label(repuestos.get(i).getRepuesto().getNroSerie());
				Label lblPrecio = new Label(repuestos.get(i).getRepuesto().getPrecio() + "");
				Label lblCantidad = new Label(repuestos.get(i).getCantidad() + "");

				tableRepuestos.setWidget(fila, 0, lblDesc);
				tableRepuestos.setWidget(fila, 1, lblNroSerie);
				tableRepuestos.setWidget(fila, 2, lblPrecio);
				tableRepuestos.setWidget(fila, 3, lblCantidad);
			}
			vpDecorator.add(tableRepuestos);
			vpDecorator.add(new HTML(" "));
		}
		else
		{
			Label htmlNoHayRepuestos = new Label (" Este activo no tiene repuestos asociados ");
			htmlNoHayRepuestos.setStyleName("Negrita");
			htmlNoHayRepuestos.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			vpDecorator.add(new HTML(" "));
			vpDecorator.add(htmlNoHayRepuestos);
			vpDecorator.add(new HTML(" "));
		}
		popUp.hide();
	}
}
