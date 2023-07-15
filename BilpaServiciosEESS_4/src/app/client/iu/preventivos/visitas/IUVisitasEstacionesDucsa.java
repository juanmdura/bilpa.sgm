package app.client.iu.preventivos.visitas;
import app.client.dominio.Persona;

public class IUVisitasEstacionesDucsa extends IUVisitasEstaciones{

	public IUVisitasEstacionesDucsa(Persona sesion){
		super(sesion, 2); 
		lblTituloPrincipal.setText("Preventivos Ducsa");
	}
}
