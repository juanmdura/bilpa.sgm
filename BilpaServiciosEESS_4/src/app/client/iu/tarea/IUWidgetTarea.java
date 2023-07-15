package app.client.iu.tarea;

import java.util.ArrayList;

import app.client.ProyectoBilpa;
import app.client.dominio.Tarea;
import app.client.dominio.TipoTarea;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.Constants;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;

public class IUWidgetTarea extends Composite
{
	public ListBox listBoxTareas = new ListBox();	
	public ListBox listBoxTiposTareas = new ListBox();

	public ArrayList<TipoTarea> tiposTareasMemoria = new ArrayList<TipoTarea>();
	public ArrayList<Tarea> tareasMemoria = new ArrayList<Tarea>();
	
	private GlassPopup glass = new GlassPopup();

	public IUWidgetTarea() 
	{
		listBoxTiposTareas.addChangeHandler(new ChangeHandler()
		{
			public void onChange(ChangeEvent arg0) 
			{
				cargarListBoxPorTipo(tareasMemoria);
			}
		});
	}

	public void cargarLtBoxTipoTarea(final PopupCargando popUp) {
		listBoxTiposTareas.clear();
		popUp.show();

		ProyectoBilpa.greetingService.obtenerTodasLosTiposTareasActivos(new AsyncCallback<ArrayList<TipoTarea>>() {
			public void onFailure(Throwable caught) 
			{
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener todas los tipo de tareas");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<TipoTarea> result) 
			{
				TipoTarea auxiliar;
				tiposTareasMemoria.clear();				
				listBoxTiposTareas.addItem(Constants.TODOS, -1+"");

				for(TipoTarea ttSinClasificar : result)
				{
					if(ttSinClasificar.getDescripcion().equals(Constants.SIN_CLASIFICAR))
					{
						listBoxTiposTareas.addItem(ttSinClasificar.toString(),String.valueOf(ttSinClasificar.getId()));
						tiposTareasMemoria.add(ttSinClasificar);
					}
				}

				for (int i=0; i<result.size();i++)
				{
					auxiliar = (TipoTarea) result.get(i);
					if(!auxiliar.getDescripcion().equals(Constants.SIN_CLASIFICAR))
					{
						listBoxTiposTareas.addItem(auxiliar.toString(),String.valueOf(auxiliar.getId()));
						tiposTareasMemoria.add(auxiliar);
					}
				}			
				cargarLtBoxTareasBase(popUp);
				popUp.hide();
			}
		});		
	}	

	public void cargarLtBoxTareasBase(final PopupCargando popUp)
	{
		popUp.show();

		tareasMemoria.clear();

		ProyectoBilpa.greetingService.obtenerTodasLasTareas(new AsyncCallback<ArrayList<Tarea>>() {
			public void onFailure(Throwable caught) 
			{
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al cargar los tipos de tareas");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<Tarea> tareas) 
			{
				for (Tarea t : tareas)
				{
					tareasMemoria.add(t);
				}
				cargarListBoxPorTipo(tareas);	
				popUp.hide();
			}
		});		
	}

	public void cargarListBoxPorTipo(ArrayList<Tarea> tareas) 
	{
		listBoxTareas.clear();
		Tarea tarea;
		if (listBoxTiposTareas.getSelectedIndex() == 0)//todos
		{
			for (int i=0; i<tareas.size();i++)
			{
				tarea = tareas.get(i);
				listBoxTareas.addItem(tarea.toString(),String.valueOf(tarea.getId()));
			}
		}
		else
		{
			TipoTarea tipoTarea = buscarTipoTarea(listBoxTiposTareas);
			for (int i=0; i<tareas.size();i++)
			{
				tarea = tareas.get(i);
				if (tarea != null && tarea.getTipoTarea() != null && tarea.getTipoTarea().getId() == tipoTarea.getId())
				{
					listBoxTareas.addItem(tarea.toString(),String.valueOf(tarea.getId()));
				}
			}
		}
	}

	public TipoTarea buscarTipoTarea(ListBox listBox) 
	{
		int idTipoTareaSeleccionado = Integer.valueOf(listBox.getValue(listBox.getSelectedIndex()));
		for (TipoTarea tipoTarea : tiposTareasMemoria)
		{
			if (tipoTarea.getId() == idTipoTareaSeleccionado)
			{
				return tipoTarea;
			}
		}
		return null;
	}
}
