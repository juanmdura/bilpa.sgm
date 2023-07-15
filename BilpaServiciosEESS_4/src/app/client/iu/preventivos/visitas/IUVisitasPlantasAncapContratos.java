package app.client.iu.preventivos.visitas;
import app.client.dominio.Persona;

public class IUVisitasPlantasAncapContratos extends IUVisitasEstaciones{

	public IUVisitasPlantasAncapContratos(Persona sesion){
		super(sesion, 6); 
		lblTituloPrincipal.setText("Preventivos Ancap Contratos");
	}
}
