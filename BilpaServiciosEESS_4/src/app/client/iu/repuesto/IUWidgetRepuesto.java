package app.client.iu.repuesto;

import java.util.ArrayList;

import app.client.ProyectoBilpa;
import app.client.dominio.Repuesto;
import app.client.dominio.TipoRepuesto;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.Constants;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;

public class IUWidgetRepuesto extends Composite
{
	public ListBox listBoxRepuestos = new ListBox();	
	public ListBox listBoxTiposRepuestos = new ListBox();

	public ArrayList<TipoRepuesto> tiposRepuestosMemoria = new ArrayList<TipoRepuesto>();
	public ArrayList<Repuesto> repuestosMemoria = new ArrayList<Repuesto>();
	
	private GlassPopup glass = new GlassPopup();

	public IUWidgetRepuesto() 
	{
		eventos();
	}

	protected void eventos() {
		listBoxTiposRepuestos.addChangeHandler(new ChangeHandler()
		{
			public void onChange(ChangeEvent arg0) 
			{
				cargarListBoxPorTipo(repuestosMemoria);
			}
		});
	}

	public void cargarLtBoxTipoRepuesto(final PopupCargando popUp) {
		listBoxTiposRepuestos.clear();
		popUp.show();

		ProyectoBilpa.greetingService.obtenerTodasLosTiposRepuestosActivos(new AsyncCallback<ArrayList<TipoRepuesto>>() {
			public void onFailure(Throwable caught) 
			{
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener todas los tipo de repuestos");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<TipoRepuesto> result) 
			{
				tiposRepuestosMemoria.clear();
				TipoRepuesto auxiliar;
				listBoxTiposRepuestos.addItem(Constants.TODOS, -1+"");

				for(TipoRepuesto trSinClasificar : result)
				{
					if(trSinClasificar.getDescripcion().equals(Constants.SIN_CLASIFICAR))
					{
						listBoxTiposRepuestos.addItem(trSinClasificar.toString(),String.valueOf(trSinClasificar.getId()));
						tiposRepuestosMemoria.add(trSinClasificar);
					}
				}

				for (int i=0; i<result.size();i++)
				{
					auxiliar = (TipoRepuesto) result.get(i);
					if(!auxiliar.getDescripcion().equals(Constants.SIN_CLASIFICAR))
					{
						listBoxTiposRepuestos.addItem(auxiliar.toString(),String.valueOf(auxiliar.getId()));
						tiposRepuestosMemoria.add(auxiliar);
					}
				}			
				cargarLtBoxRepuestosBase(popUp);
				popUp.hide();
			}
		});		
	}	

	public void cargarLtBoxRepuestosBase(final PopupCargando popUp)
	{
		popUp.show();

		repuestosMemoria.clear();

		ProyectoBilpa.greetingService.obtenerTodosLosRepuestos(new AsyncCallback<ArrayList<Repuesto>>() {
			public void onFailure(Throwable caught) 
			{
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al cargar los tipos de repuestos");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<Repuesto> repuestos) 
			{
				for (Repuesto t : repuestos)
				{
					repuestosMemoria.add(t);
				}
				cargarListBoxPorTipo(repuestos);	
				popUp.hide();
			}
		});		
	}

	public void cargarListBoxPorTipo(ArrayList<Repuesto> repuestos) 
	{
		listBoxRepuestos.clear();
		Repuesto repuesto;
		if (listBoxTiposRepuestos.getSelectedIndex() == 0)//todos
		{
			for (int i=0; i<repuestos.size();i++)
			{
				repuesto = repuestos.get(i);
				listBoxRepuestos.addItem(repuesto.toString(),String.valueOf(repuesto.getId()));
			}
		}
		else
		{
			TipoRepuesto tipoRepuesto = buscarTipoRepuesto(listBoxTiposRepuestos);
			for (int i=0; i<repuestos.size();i++)
			{
				repuesto = repuestos.get(i);
				if (repuesto != null && repuesto.getTipoRepuesto() != null && repuesto.getTipoRepuesto().getId() == tipoRepuesto.getId())
				{
					listBoxRepuestos.addItem(repuesto.toString(),String.valueOf(repuesto.getId()));
				}
			}
		}
	}

	public TipoRepuesto buscarTipoRepuesto(ListBox listBox) 
	{
		int idTipoRepuestoSeleccionado = Integer.valueOf(listBox.getValue(listBox.getSelectedIndex()));
		for (TipoRepuesto tipoRepuesto : tiposRepuestosMemoria)
		{
			if (tipoRepuesto.getId() == idTipoRepuestoSeleccionado)
			{
				return tipoRepuesto;
			}
		}
		return null;
	}
}
