package app.client.iu.listados.historicoActivos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import app.client.dominio.Activo;
import app.client.dominio.Comentario;
import app.client.dominio.Contador;
import app.client.dominio.Reparacion;
import app.client.dominio.ReparacionSurtidor;
import app.client.dominio.RepuestoLinea;
import app.client.dominio.Solucion;
import app.client.dominio.data.DatoListadoActivos;
import app.client.iu.rightClick.AdvSortableTable;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.UtilCss;
import app.client.utilidades.UtilOrden;
import app.client.utilidades.UtilUI;
import app.client.utilidades.utilObjects.DecoratorPanelAdv;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;
import app.client.utilidades.utilObjects.SortableTable;
import app.client.utilidades.utilObjects.VerticalPanelAdv;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class IUHistoricoActivosData 
{
	HTML htmlTitulo = new HTML();
	HTML lblActivo = new HTML("Activo");

	HorizontalPanel hPanelCombos = new HorizontalPanel();
	VerticalPanelAdv vPanelPrincipal = new VerticalPanelAdv();
	VerticalPanel vPanelActivos = new VerticalPanel();

	ArrayList<DecoratorPanelAdv> decoratorPanels = new ArrayList<DecoratorPanelAdv>();
	Set<Integer> numerosDeOrdenDeEsteActivo = new HashSet<Integer>(); 

	int idEstacion;
	Activo activo;
	DatoListadoActivos dla;

	private PopupCargando popUp;
	
	private GlassPopup glass = new GlassPopup();

	public IUHistoricoActivosData(DatoListadoActivos dla, PopupCargando popUp) 
	{
		this.dla = dla;
		this.popUp = popUp;

		htmlTitulo.setStyleName("Subtitulo");
		if (dla.getReparaciones().size() > 0)
		{
			this.activo = dla.getReparaciones().get(0).getActivo();

			int idActivoValidacion = activo.getId();
			for(Reparacion r : dla.getReparaciones())
			{
				if (idActivoValidacion != r.getActivo().getId())
				{
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error en los activos obtenidos!");
					vpu.showPopUp();
				}
			}

			vPanelPrincipal.setSpacing(10);
			vPanelPrincipal.setWidth("500px");

			dibujarTablaSoluciones();
		}
		else
		{
			//htmlTitulo.setText("No existen datos para mostrar.");
			//vPanelPrincipal.add(new HTML("__________________________________________________________________________________________________________________________________________________________"));
			//vPanelPrincipal.add(htmlTitulo);
		}
	}

	public VerticalPanelAdv getPrincipalPanel() 
	{
		return vPanelPrincipal;
	}

	private void dibujarTablaSoluciones() 
	{		
		ArrayList<Reparacion> reparaciones = dla.getReparaciones();
		ArrayList<RepuestoLinea> repuestos = dla.getRepuestosLinea();
		ArrayList<Comentario> comentarios = dla.getComentarios();

		if (reparacionesTienenSolucion(reparaciones))
		{
			vPanelPrincipal.add(new HTML("__________________________________________________________________________________________________________________________________________________________"));
			vPanelPrincipal.add(htmlTitulo);
			htmlTitulo.setText(UtilOrden.tiposDeActivos(activo) + " " + activo.toString() );

			VerticalPanel vpDecorator = new VerticalPanel();
			DecoratorPanelAdv decorator = new DecoratorPanelAdv();
			decoratorPanels.add(decorator);
			decorator.setId(activo.getId());

			vpDecorator.setWidth("1070px");
			vpDecorator.setSpacing(8);

			int fila = 1;
			AdvSortableTable tableFallaTarea = new AdvSortableTable();
			tableFallaTarea.setWidth("1050px");

			boolean tieneReparacionesDePicos = dla.tieneReparacionesDePicos();
			agregarFilaTituloFallaTarea(tableFallaTarea, tieneReparacionesDePicos);

			for (Reparacion r : reparaciones)
			{
				if (r.getSoluciones().size() > 0)
				{
					numerosDeOrdenDeEsteActivo.add(r.getOrden().getNumero());
					if (tieneReparacionesDePicos)
					{
						fila = tablaPicos(r, tableFallaTarea, fila);
					}
					else
					{
						fila = tablaSinPicos(r, tableFallaTarea, fila);
					}

					vpDecorator.add(tableFallaTarea);
					popUp.hide();
				}
				else
				{
					/*Label htmlNoHaySol = new Label (" Este activo no fue reparado");
				htmlNoHaySol.setStyleName("Negrita");
				htmlNoHaySol.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
				vpDecorator.add(htmlNoHaySol);*/
					popUp.hide();
				}
			}
			dibujarTablaRepuestos(repuestos, vpDecorator);

			UtilUI.agregarEspacioEnBlanco(vPanelActivos, 3);
			vPanelActivos.add(decorator);

			dibujarTablaComentarios(comentarios, vpDecorator);
			decorator.add(vpDecorator);

			UtilUI.agregarEspacioEnBlanco(vPanelActivos, 3);
			vPanelActivos.add(decorator);

			vPanelPrincipal.add(vPanelActivos);
		}
	}

	private boolean reparacionesTienenSolucion(ArrayList<Reparacion> reparaciones) 
	{
		for (Reparacion r : reparaciones)
		{
			if (r.getSoluciones().size() > 0)
			{
				return true;
			}
		}
		return false;
	}

	private int tablaPicos(Reparacion r, AdvSortableTable tableFallaTarea,	int fila) 
	{
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

			String fechaInicioService = UtilUI.formatearFecha(r.getOrden().getInicioService().toString());

			UtilCss.aplicarEstiloATabla(tableFallaTarea, fila+1);
			Label lblOrden = new Label(r.getOrden().getNumero()+"");
			Label lblFechaRep = new Label(fechaInicioService);
			Label lblFalla = new Label(s.getFallaTecnica().getDescripcion());
			Label lblTarea = new Label(s.getTarea().getDescripcion());
			Label lblPico = new Label(textoPico);
			Label lblTecnico = new Label(r.getOrden().getTecnicoAsignado()+"");

			tableFallaTarea.setWidget(fila, 0, lblOrden);
			tableFallaTarea.setWidget(fila, 1, lblFechaRep);
			tableFallaTarea.setWidget(fila, 2, lblFalla);
			tableFallaTarea.setWidget(fila, 3, lblTarea);
			tableFallaTarea.setWidget(fila, 4, lblPico);
			tableFallaTarea.setWidget(fila, 5, lblTecnico);
			fila++;
			
			/*lblOrden.setTitle(r.getComentario());
			lblFechaRep.setTitle(r.getComentario());
			lblFalla.setTitle(r.getComentario());
			lblTarea.setTitle(r.getComentario());
			lblPico.setTitle(r.getComentario());
			lblTecnico.setTitle(r.getComentario());*/
		}
		return fila;
	}

	private int tablaSinPicos(Reparacion r, AdvSortableTable tableFallaTarea,	int fila) 
	{
		for (Solucion s : r.getSoluciones())
		{
			String fechaInicioService = UtilUI.formatearFecha(r.getOrden().getInicioService().toString());

			UtilCss.aplicarEstiloATabla(tableFallaTarea, fila+1);
			Label lblOrden = new Label(r.getOrden().getNumero()+"");
			Label lblFechaRep = new Label(fechaInicioService);
			Label lblFalla = new Label(s.getFallaTecnica().getDescripcion());
			Label lblTarea = new Label(s.getTarea().getDescripcion());
			Label lblTecnico = new Label(r.getOrden().getTecnicoAsignado()+"");

			tableFallaTarea.setWidget(fila, 0, lblOrden);
			tableFallaTarea.setWidget(fila, 1, lblFechaRep);
			tableFallaTarea.setWidget(fila, 2, lblFalla);
			tableFallaTarea.setWidget(fila, 3, lblTarea);
			tableFallaTarea.setWidget(fila, 4, lblTecnico);
			fila++;
			
			/*lblOrden.setTitle(r.getComentario());
			lblFechaRep.setTitle(r.getComentario());
			lblFalla.setTitle(r.getComentario());
			lblTarea.setTitle(r.getComentario());
			lblTecnico.setTitle(r.getComentario());*/
		}
		return fila;
	}

	private void agregarFilaTituloFallaTarea(SortableTable table, boolean tienePicos) 
	{		
		table.getRowFormatter().addStyleName(0, "CabezalTabla");

		if (tienePicos)
		{
			table.addColumnHeader("Orden", 0);
			table.addColumnHeader("Fecha Reparación", 1);
			table.addColumnHeader("Falla Detectada", 2);
			table.addColumnHeader("Tarea Realizada", 3);			
			table.addColumnHeader("Pico", 4);
			table.addColumnHeader("Técnico", 5);
		}
		else
		{
			table.addColumnHeader("Orden", 0);
			table.addColumnHeader("Fecha Reparación", 1);
			table.addColumnHeader("Falla Detectada", 2);
			table.addColumnHeader("Tarea Realizada", 3);			
			table.addColumnHeader("Técnico", 4);
		}
	}

	private void agregarFilaTituloRepuestos(SortableTable table) 
	{		
		table.getRowFormatter().addStyleName(0, "CabezalTabla");

		table.addColumnHeader("Orden", 0);
		table.addColumnHeader("Repuesto", 1);
		table.addColumnHeader("Nro Serie", 2);
		table.addColumnHeader("Cantidad", 3);
		table.addColumnHeader("Técnico", 4);
	}

	private void agregarFilaTituloComentarios(SortableTable table) 
	{		
		table.getRowFormatter().addStyleName(0, "CabezalTabla");

		table.addColumnHeader("Orden", 0);
		table.addColumnHeader("Comentario", 1);
		table.addColumnHeader("Fecha", 2);
		table.addColumnHeader("Usuario", 3);
		table.addColumnHeader("Imprimible", 4);
	}

	private void dibujarTablaRepuestos(ArrayList<RepuestoLinea> repuestos, VerticalPanel vpDecorator) 
	{
		AdvSortableTable tableRepuestos = new AdvSortableTable();
		tableRepuestos.setWidth("1050px");
		agregarFilaTituloRepuestos(tableRepuestos);

		if (repuestos.size()>0)
		{
			for (int i=0; i < repuestos.size(); i++)
			{
				int fila = i+1;

				UtilCss.aplicarEstiloATabla(tableRepuestos, fila+1);

				Label lblOrden = new Label(repuestos.get(i).getOrden().getNumero()+"");
				Label lblDesc = new Label(repuestos.get(i).getRepuesto().getDescripcion());
				Label lblNroSerie = new Label(repuestos.get(i).getRepuesto().getNroSerie());
				Label lblCantidad = new Label(repuestos.get(i).getCantidad() + "");
				Label lblTecnico = new Label(repuestos.get(i).getOrden().getTecnicoAsignado() + "");

				tableRepuestos.setWidget(fila, 0, lblOrden);
				tableRepuestos.setWidget(fila, 1, lblDesc);
				tableRepuestos.setWidget(fila, 2, lblNroSerie);
				tableRepuestos.setWidget(fila, 3, lblCantidad);
				tableRepuestos.setWidget(fila, 4, lblTecnico);
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

	private void dibujarTablaComentarios(ArrayList<Comentario> comentarios, VerticalPanel vpDecorator) 
	{
		AdvSortableTable tableComentarios = new AdvSortableTable();
		tableComentarios.setWidth("1050px");

		if (comentarios.size()>0)
		{
			for (int i=0; i < comentarios.size(); i++)
			{
				Comentario com = comentarios.get(i);
				/*if (numerosDeOrdenDeEsteActivo.contains(com.getOrden()))
				{
					if (i ==0)
					{
						agregarFilaTituloComentarios(tableComentarios);
					}
					
					int fila = i+1;

					UtilCss.aplicarEstiloATabla(tableComentarios, fila+1);

					Label lblOrden = new Label(com.getOrden()+"");
					Label lblDesc = new Label(com.getTexto());
					Label lblFecha = new Label(com.getFecha());
					Label lblUsuario = new Label(com.getUsuario() + "");
					Label lblImp = new Label(com.isImprimible() ? "Si" : "No");

					tableComentarios.setWidget(fila, 0, lblOrden);
					tableComentarios.setWidget(fila, 1, lblDesc);
					tableComentarios.setWidget(fila, 2, lblFecha);
					tableComentarios.setWidget(fila, 3, lblUsuario);
					tableComentarios.setWidget(fila, 4, lblImp);
				}*/
			}
			vpDecorator.add(tableComentarios);
			vpDecorator.add(new HTML(" "));
		}
		else
		{
			/*Label htmlNoHayComentarios = new Label (" Este activo no tiene comentarios asociados ");
			htmlNoHayComentarios.setStyleName("Negrita");
			htmlNoHayComentarios.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			vpDecorator.add(new HTML(" "));
			vpDecorator.add(htmlNoHayComentarios);
			vpDecorator.add(new HTML(" "));*/
		}
		popUp.hide();
	}

}
