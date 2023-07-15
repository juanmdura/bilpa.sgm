package app.client.iu.menu;

import app.client.iu.listados.historicoActivos.IUHistoricoActivosFiltros;
import app.client.iu.listados.historicoReparaciones.IUHistoricoReparacionesFiltros;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

public class IUMenuPrincipalTecnico extends IUMenu
{
	//private static IUMenuPrincipalTecnico instancia = null;

	public static IUMenuPrincipalTecnico getInstancia() 
	{
		if(instancia == null)
		{
			instancia = new IUMenuPrincipalTecnico();
		}
		return (IUMenuPrincipalTecnico) instancia;
	}

	public IUMenuPrincipalTecnico()
	{	
		cargarMenues();
	}

	private void cargarMenues()
	{
		cargarMenuOrdenes(); 
		cargarMenuPendientes();
		cargarMenuEstadisticas();
	}

	public void cargarMenuOrdenes(){
		MenuItem menuItemOrden = new MenuItem("Correctivos", false, new Command() 
		{
			public void execute()
			{
				cargarListaOrdenes();
			}
		});

		menuItemOrden.setWidth("200");
		//menuItemOrden.setHeight("30");

		menuBar.addItem(menuItemOrden);

	}

	protected void cargarMenuEstadisticas(){
		MenuBar menuBarConsultas = new MenuBar(true);
		MenuItem menuItemConsultas = new MenuItem("Estadísticas", false, menuBarConsultas);

		MenuItem menuItemHistoricoActivos = new MenuItem("Histórico de activos", false, new Command(){
			public void execute() 
			{
				historicoActivos();
			}
		});

		menuBarConsultas.addItem(menuItemHistoricoActivos);

		MenuItem menuItemHistoricoReparaciones = new MenuItem("Histórico de reparaciones", false, new Command(){
			public void execute() 
			{
				historicoReparaciones();
			}
		});

		menuBarConsultas.addItem(menuItemHistoricoReparaciones);

		menuItemConsultas.setWidth("200");
		menuBar.addItem(menuItemConsultas);
	}
	
	private void historicoActivos()
	{
		validarSiHayWidgetActivo();
		IUHistoricoActivosFiltros iur = new IUHistoricoActivosFiltros(sesion);
		panelVerticalPrincipal.add(iur.getPrincipalPanel());				
	}
	
	private void historicoReparaciones()
	{
		validarSiHayWidgetActivo();
		IUHistoricoReparacionesFiltros iur = new IUHistoricoReparacionesFiltros(sesion);
		panelVerticalPrincipal.add(iur.getPrincipalPanel());				
	}
}
