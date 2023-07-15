package app.client.dominio;

import java.util.HashSet;
import java.util.Set;

import app.client.dominio.data.DestinoDelCargoData;

public class ReparacionSurtidor extends Reparacion{

	private Set<Contador> contadores = new HashSet<Contador>();

	public Set<Contador> getContadores() {
		return contadores;
	}

	public void setContadores(Set<Contador> contadores) {
		this.contadores = contadores;
	}

	public ReparacionSurtidor()
	{
		super(1);
	}

	
	public ReparacionSurtidor copiar() {
		return (ReparacionSurtidor)super.copiar();
	}

	
	@Override
	public Reparacion copiarMinimo() {
		ReparacionSurtidor reparacion = (ReparacionSurtidor) super.copiarMinimo();
		reparacion.setContadores(null);
		return reparacion;
	}

	public ReparacionSurtidor copiarTodo() {
		ReparacionSurtidor copia = (ReparacionSurtidor) super.copiarTodo();
		copiarColecciones(copia);
		return copia;
	}

	private void copiarColecciones(ReparacionSurtidor copia) {
		for ( Contador c : contadores) {
			copia.getContadores().add(c.copiarTodo());
		}
	}

	public void agregarPico(int numeroPico){

		if(this.getActivo().getTipo() == 1){
			Surtidor s = (Surtidor) this.getActivo();

			Pico pico = s.buscarPicoPorNumero(numeroPico);
			if(pico != null){
				Contador contador = new Contador();
				contador.setPico(pico);
				contador.setReparacion(this);
				contador.setInicio(0);
				contador.setFin(0);
				this.contadores.add(contador);
			}
		}	 
	}

	public Contador buscarContador(Solucion sol)
	{
		for (Contador cont : contadores)
		{
			if (sol.equals(cont.getSolucion()))
			{
				return cont;
			}
		}
		return null;
	}

	public Solucion agregarSolucion(Orden orden, Set<Solucion> solucionesOrden, Tarea tarea, FallaTecnica fallaTecnica, Set<RepuestoLinea> repuestos, 
									Boolean telefonica, Pico pico, DestinoDelCargoData destinoDelCargo, Comentario comentario){

		if (validarSolucionSurt(tarea, fallaTecnica, telefonica, pico))
		{
			Solucion sol = super.agregarSolucion(orden, solucionesOrden, tarea, fallaTecnica, repuestos, telefonica, pico, destinoDelCargo, comentario);	

			if (sol != null)
			{
				agregarContador(pico, sol);
				return sol;				
			}
			return null;
		}
		return null;
	}
	
	public Solucion agregarSolucion(Orden orden, Set<Solucion> solucionesOrden, Tarea tarea, FallaTecnica fallaTecnica, 
									Set<RepuestoLinea> repuestos, Boolean telefonica, Pico pico, int duracion, 
									DestinoDelCargoData destinoDelCargo, Comentario comentario){
		
		//TODO Verificar si hay que validar el destino del cargo
		if ( validarSolucionSurt(tarea, fallaTecnica, telefonica, pico) )
		{
			Solucion sol = super.agregarSolucion(orden, solucionesOrden, tarea, fallaTecnica, repuestos, telefonica, pico, destinoDelCargo, comentario);
			if (sol != null)
			{
				agregarContador(pico, sol);
				return sol;				
			}
			return null;
		}
		return null;
	}

	private void agregarContador(Pico pico, Solucion sol) {
		if (pico != null )//&& !contadorYaTienePico(pico))
		{
			Contador contador = new Contador(this, sol, pico);
			getContadores().add(contador);			
		}
	}

	public boolean validarSolucionSurt(Tarea tarea, FallaTecnica fallaTecnica, boolean tel, Pico pico) {
		for (Solucion s : getSoluciones())
		{
			if (s.getTarea().equals(tarea) && 
				s.getFallaTecnica().equals(fallaTecnica) && 
				s.isTelefonica() == tel)
			{//ya existe una reparacion con esos datos, ahora hay que validar el pico para ver si se permite o no agregar esta reparacion.
				if(getContadores().size() == 0 && pico == null){//si no se selecciono pico y las reparaciones existentes tampoco tienen pico, entonces esta repitiendo una reparacion
					return false;
				}else{//se selecciono un pico, hay que ver si ya existe una reparacion para ese pico
					for(Contador c : getContadores())
					{
						if (pico != null && c.getPico().equals(pico) && c.getSolucion().equals(s))
						{
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	public boolean tienePrecintosReemplazados() {
		for (Solucion s : getSoluciones()){
			if (s.tienePrecintosReemplazados()){
				return true;
			}
		}
		return false;
	}
}
