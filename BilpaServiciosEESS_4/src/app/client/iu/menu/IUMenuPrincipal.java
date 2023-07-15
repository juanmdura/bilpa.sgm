package app.client.iu.menu;

public class IUMenuPrincipal extends IUMenu {

	//private static IUMenuPrincipal instancia = null;

	public static IUMenuPrincipal getInstancia() {

		if(instancia == null){
			instancia = new IUMenuPrincipal();

		}
		return (IUMenuPrincipal) instancia;
	}

	public IUMenuPrincipal()
	{
		cargarMenues();
	}

	private void cargarMenues()
	{
		cargarMenuOrdenes(); 
		cargarMenuPreventivos();
		cargarMenuPendientes();
		cargarMenuFallas();  
		cargarMenuRepuestos();
		cargarMenuTareas();
		cargarMenuEmpresas();
		cargarMenuActivos();
		cargarMenuUsuarios();
		cargarMenuEstadisticas();	
	}
}


