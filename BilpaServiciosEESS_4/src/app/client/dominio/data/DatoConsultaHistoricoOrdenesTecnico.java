package app.client.dominio.data;


public class DatoConsultaHistoricoOrdenesTecnico implements com.google.gwt.user.client.rpc.IsSerializable, Comparable<Object>
{
	
	private String nombreTecnico;
	private int cantidadOrdenes;
	
	public String getNombreTecnico() {
		return nombreTecnico;
	}
	public void setNombreTecnico(String nombreTecnico) {
		this.nombreTecnico = nombreTecnico;
	}
	public int getCantidadOrdenes() {
		return cantidadOrdenes;
	}
	public void setCantidadOrdenes(int cantidadOrdenes) {
		this.cantidadOrdenes = cantidadOrdenes;
	}
	
	public DatoConsultaHistoricoOrdenesTecnico(String nombre, int cantidad){
		this.cantidadOrdenes = cantidad;
		this.nombreTecnico = nombre;
	}

	public DatoConsultaHistoricoOrdenesTecnico(){
		
	}
	
	public int compareTo(Object o) 
	{
		if (o.getClass().equals(DatoConsultaHistoricoOrdenesTecnico.class))
		{
			DatoConsultaHistoricoOrdenesTecnico dt = (DatoConsultaHistoricoOrdenesTecnico)o;
			
			if (dt.getCantidadOrdenes() == cantidadOrdenes)
			{
				return 0;
			}
			else if (dt.getCantidadOrdenes() > cantidadOrdenes)
			{
				return -1;
			}
			else if (dt.getCantidadOrdenes() < cantidadOrdenes)
			{
				return 1;
			}
		}
		return 0;
	}
	
}
