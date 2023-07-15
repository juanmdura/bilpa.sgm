package app.client.utilidades;

import app.client.dominio.Activo;
import app.client.dominio.ActivoGenerico;
import app.client.dominio.Orden;
import app.client.dominio.Persona;
import app.client.iu.menu.IUMenuPrincipal;
import app.client.iu.menu.IUMenuPrincipalAdministrativo;
import app.client.iu.menu.IUMenuPrincipalTecnico;
import app.client.iu.orden.encargado.IUSeguimientoEncargado;
import app.client.iu.orden.inicio.IUSeguimientoInicial;
import app.client.iu.orden.tecnico.IUSeguimientoTecnico;

public class UtilOrden {

	public static int idFallaPendientes = 475;
	public static String textoFallaPendientes = "ELIMINACIÓN DE PENDIENTE";
	
	public static String prioridadOrden(int i){
		if(i==1){
			return "Alta";
		}
		if(i==2){
			return "Normal";
		}
		if(i==3){
			return "Baja";
		}
		return "Error en la prioridad";
	}

	public static String personaAsignadaOrden(int i){
		if(i==1){
			return "Sin Asignar";
		}		
		return "Error en la persona asignada";
	}

	public static int formularioOrdenAMostrar(){
		return 0;
	}

	public static String tiposDeActivos(Activo activo){
		int i = activo.getTipo();
		String tipo = tiposDeActivos(i);
		if(i==6){
			return ((ActivoGenerico)activo).getTipoActivoGenerico().getNombre();
		}
		return tipo;
	}

	public static String tiposDeActivos(int i) {
		if(i==1){
			return "Surtidor";
		}
		if(i==2){
			return "Tanque";
		}
		if(i==3){
			return "Caño";
		}
		if(i==4){
			return "Bomba Sumergible";
		}
		if(i==5){
			return "Pico";
		}
		if(i==6){
			return "Genérico";
		}
		return "Error en el estado";
	}

	public static String tiposDeActivosPlural(int i)
	{
		if(i==1){
			return "Surtidores";
		}
		if(i==2){
			return "Tanques";
		}
		if(i==3){
			return "Caños";
		}
		if(i==4){
			return "Bombas";
		}
		if(i==5){
			return "Picos";
		}
		if(i==6){
			return "Genéricos";
		}
		return "Error en el estado";
	}

	public static String getEstadoId(String texto){
		if(texto.equals("Iniciada")){
			return "1";
		}
		if(texto.endsWith("Pendiente")){
			return "2";
		}
		if(texto.equals("Finalizada")){
			return "3";
		}
		if(texto.equals("Anulada")){
			return "4";
		}
		if(texto.equals("Reparada")){
			return "5";
		}
		if(texto.equals("Iniciada Ducsa")){
			return "6";
		}
		if(texto.endsWith("en Proceso")){
			return "7";
		}
		return "Error en el estado";
	}

	public static String getEstadoTexto(int i){
		if(i==1){
			return "Iniciada";
		}
		if(i==2){
			return "Inspección Pendiente";
		}
		if(i==3){
			return "Finalizada";
		}
		if(i==4){
			return "Anulada";
		}
		if(i==5){
			return "Reparada";
		}
		if(i==6){
			return "Iniciada Ducsa";
		}
		if(i==7){
			return "Reparación en Proceso";
		}
		return "Error en el estado";
	}

	public static String getSello(int i){
		if(i==1){
			return "ANCAP";
		}
		if(i==2){
			return "PETROBRAS";
		}

		return "Error en el sello";
	}

	public static void cargarOrdenSeleccionada(Orden orden, Persona sesion)
	{
		if(sesion.getRol()==3)
		{//Tecnico
			IUSeguimientoTecnico iu = new IUSeguimientoTecnico(orden, sesion);
			IUMenuPrincipalTecnico.getInstancia().agregarWidgetAlMenu(iu.getVPanelPrincipal());
		}


		if(sesion.getRol()==1)
		{//Administrador
			if (orden.getEstadoOrden() == 5)//si esta reparada, no mostramos la combo para cambiar de tecnico
			{
				IUSeguimientoTecnico iu = new IUSeguimientoTecnico(orden, sesion);
				IUMenuPrincipal.getInstancia().agregarWidgetAlMenu(iu.getVPanelPrincipal());
			}
			else if (orden.tieneSoluciones())
			{
				IUSeguimientoEncargado iu = new IUSeguimientoEncargado(orden, sesion);
				IUMenuPrincipal.getInstancia().agregarWidgetAlMenu(iu.getVPanelPrincipal());
			}
			else
			{
				IUSeguimientoInicial iu = new IUSeguimientoInicial(orden, sesion);
				IUMenuPrincipal.getInstancia().agregarWidgetAlMenu(iu.getVPanelPrincipal());				
			}
		}

		//NO HAY ENCARGADOS DE MANTENIMIENTO EN USO, NUNCA DEBERIA DE ENTRAR ACA
		if(sesion.getRol() == 5)//Encargado de Mantenimiento
		{
			if (orden.getEstadoOrden() == 5)//si esta reparada, no mostramos la combo para cambiar de tecnico
			{
				IUSeguimientoTecnico iu = new IUSeguimientoTecnico(orden, sesion);
				IUMenuPrincipal.getInstancia().agregarWidgetAlMenu(iu.getVPanelPrincipal());
			}
			else if (orden.tieneSoluciones())
			{
				IUSeguimientoEncargado iu = new IUSeguimientoEncargado(orden, sesion);
				IUMenuPrincipal.getInstancia().agregarWidgetAlMenu(iu.getVPanelPrincipal());
			}
			else
			{
				if(orden.getTecnicoAsignado() != null)
				{
					if(orden.getTecnicoAsignado().getId() == sesion.getId())//Si esta asignado a este encargado
					{
						IUSeguimientoEncargado iu = new IUSeguimientoEncargado(orden, sesion);
						IUMenuPrincipal.getInstancia().agregarWidgetAlMenu(iu.getVPanelPrincipal());
					}
					else
					{
						IUSeguimientoInicial iu = new IUSeguimientoInicial(orden, sesion);
						IUMenuPrincipal.getInstancia().agregarWidgetAlMenu(iu.getVPanelPrincipal());
					}				
				}
				else
				{
					IUSeguimientoInicial iu = new IUSeguimientoInicial(orden, sesion);
					IUMenuPrincipal.getInstancia().agregarWidgetAlMenu(iu.getVPanelPrincipal());
				}
			}
		}

		if(sesion.getRol()==2)//Administrativo
		{
			IUSeguimientoInicial iu = new IUSeguimientoInicial(orden, sesion);
			IUMenuPrincipalAdministrativo.getInstancia().agregarWidgetAlMenu(iu.getVPanelPrincipal());
		}
	}

	public static int getNumeroDucsaInt(String numeroParteDucsa) {
		try{
			return Integer.valueOf(numeroParteDucsa);
		} catch(Exception e){
			
		}
		return 0;
	}
}
