package app.client.iu.activos.generico.tipoMarcaModelo;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import app.client.dominio.MarcaActivoGenerico;
import app.client.dominio.Persona;
import app.client.dominio.data.TipoActivoGenericoData;
import app.client.iu.activos.generico.tipoMarcaModelo.chequeo.IUGestionarChequeoTipoActivoGenerico;
import app.client.iu.activos.generico.tipoMarcaModelo.marca.IUGestionarMarcaTipoActivoGenerico;
import app.client.iu.activos.generico.tipoMarcaModelo.modelo.IUGestionarModeloTipoActivoGenerico;
import app.client.iu.activos.generico.tipoMarcaModelo.tipo.IUGestionarTipoActivoGenerico;

public class IUGestionarTipoMarcaModeloMain extends Composite {
	private Label lblTituloPrincipal = new Label("Gestión de tipos de activos genéricos");
	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private HorizontalPanel hPanelPrincipal = new HorizontalPanel();
	
	private IUGestionarTipoActivoGenerico iuGestionarTipoActivo;
	private IUGestionarMarcaTipoActivoGenerico iuGestionarMarcaTipoActivo;
	private IUGestionarModeloTipoActivoGenerico iuGestionarModeloTipoActivo;
	private IUGestionarChequeoTipoActivoGenerico iuGestionarChequeoTipoActivo;
	
	public IUGestionarTipoMarcaModeloMain(Persona sesion) {
		super();
		
		hPanelPrincipal.setSpacing(25);
		iuGestionarTipoActivo = new IUGestionarTipoActivoGenerico(sesion, this);
		iuGestionarMarcaTipoActivo = new IUGestionarMarcaTipoActivoGenerico(sesion, this);
		iuGestionarModeloTipoActivo = new IUGestionarModeloTipoActivoGenerico(sesion);
		iuGestionarChequeoTipoActivo = new IUGestionarChequeoTipoActivoGenerico(sesion, this);
		
		hPanelPrincipal.add(iuGestionarTipoActivo.getPrincipalPanel());
		hPanelPrincipal.add(iuGestionarMarcaTipoActivo.getPrincipalPanel());
		hPanelPrincipal.add(iuGestionarModeloTipoActivo.getPrincipalPanel());
		hPanelPrincipal.add(iuGestionarChequeoTipoActivo.getPrincipalPanel());
		
		lblTituloPrincipal.setStyleName("Titulo");
		
		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal.setSpacing(5);
		
		vPanelPrincipal.add(lblTituloPrincipal);
		vPanelPrincipal.add(hPanelPrincipal);
	}
	
	public VerticalPanel getPrincipalPanel() {
		return vPanelPrincipal;
	}

	public void tipoActivoSeleccionado(TipoActivoGenericoData seleccionado){
		iuGestionarMarcaTipoActivo.tipoActivoSeleccionado(seleccionado);
		iuGestionarChequeoTipoActivo.tipoActivoSeleccionado(seleccionado);
		iuGestionarModeloTipoActivo.marcaSeleccionado(null);
		iuGestionarModeloTipoActivo.setSeleccionado(null);
	}

	public void marcaSeleccionado(MarcaActivoGenerico seleccionado) {
		iuGestionarModeloTipoActivo.marcaSeleccionado(seleccionado);
	}

	/*public void chequeoSeleccionado(ItemChequeoData seleccionado) {
		iuGestionarChequeoTipoActivo.chequeoSeleccionado(seleccionado);
	}*/
}
