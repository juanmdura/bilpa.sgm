package app.client.iu.correctivo;

import java.util.List;

import com.google.gwt.user.client.ui.Composite;

import app.client.dominio.Activo;
import app.client.dominio.FallaReportada;
import app.client.dominio.Orden;
import app.client.dominio.Pendiente;
import app.client.dominio.data.PendienteDataUI;

public abstract class IUWidgetCorrectivo extends Composite {

	public abstract void agregarNuevaFallaAListaDeFallas(List<FallaReportada> seleccionados2, List<Activo> seleccionados, List<String> comentarios, Pendiente pendiente);
	public abstract Orden getOrden();
	public abstract void reloadTituloPendientesAReparar(PendienteDataUI pendienteDataUI);
}
