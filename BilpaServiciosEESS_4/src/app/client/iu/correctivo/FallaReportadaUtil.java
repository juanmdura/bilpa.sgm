package app.client.iu.correctivo;

import java.util.List;

import com.google.gwt.user.client.ui.DialogBox;

import app.client.dominio.Activo;
import app.client.dominio.FallaReportada;
import app.client.dominio.Pendiente;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.utilObjects.GlassPopup;

public class FallaReportadaUtil {

	public static void validar(List<Activo> seleccionados, List<FallaReportada> seleccionados2, Pendiente pendiente, List<String> comentarios, GlassPopup glass, DialogBox dialog, IUWidgetCorrectivo iuWidgetCorrectivo) {
		if (hayActivoSeleccionado(seleccionados) && hayFallaReportadaSeleccionado(seleccionados2)){
			glass.hide();
			dialog.hide();
			iuWidgetCorrectivo.agregarNuevaFallaAListaDeFallas(seleccionados2, seleccionados, comentarios, pendiente);
		} else {
			if (hayActivoSeleccionado(seleccionados)){
				ValidadorPopup vpu = new ValidadorPopup(new GlassPopup(), "Info", "Debe seleccionar un activo");
				vpu.showPopUp();
			
			} else if (hayFallaReportadaSeleccionado(seleccionados2)){
				ValidadorPopup vpu = new ValidadorPopup(new GlassPopup(), "Info", "Debe seleccionar un falla");
				vpu.showPopUp();
			}
		}
	}
	
	public static boolean hayActivoSeleccionado(List<Activo> seleccionados){
		return seleccionados != null && seleccionados.size() > 0;
	}
	
	public static boolean hayFallaReportadaSeleccionado(List<FallaReportada> seleccionados){
		return seleccionados != null && seleccionados.size() > 0;
	}
}
