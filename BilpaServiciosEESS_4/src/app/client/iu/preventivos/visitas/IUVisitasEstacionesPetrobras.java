package app.client.iu.preventivos.visitas;
import app.client.dominio.Persona;

public class IUVisitasEstacionesPetrobras extends IUVisitasEstaciones{

	public IUVisitasEstacionesPetrobras(Persona sesion){
		super(sesion, 3); 
		lblTituloPrincipal.setText("Preventivos Petrobras");
	}
}
