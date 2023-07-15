package app.client.iu.falla;

import java.util.ArrayList;

import app.client.ProyectoBilpa;
import app.client.dominio.FallaReportada;
import app.client.dominio.FallaTecnica;
import app.client.dominio.SubTipoFalla;
import app.client.dominio.TipoFallaReportada;
import app.client.dominio.TipoFallaTecnica;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.Constants;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

public class IUWidgetFalla extends Composite
{
	public ListBox listBoxFallasT = new ListBox();	
	public ListBox listBoxTiposFallasT = new ListBox();
	
	public ListBox listBoxFallasR = new ListBox();	
	public ListBox listBoxTiposFallasR = new ListBox();

	protected Label lblTiposFallasT = new Label("Tipo de Falla Detectada");
	protected Label lblTiposFallasR = new Label("Tipo de Falla Reportada");
	protected Label lblFallasT = new Label("Falla Detectada");
	protected Label lblFallasR = new Label("Falla Reportada");
	
	public ArrayList<TipoFallaTecnica> tiposFallasMemoriaT = new ArrayList<TipoFallaTecnica>();
	public ArrayList<FallaTecnica> fallasMemoriaT = new ArrayList<FallaTecnica>();
	
	public ArrayList<TipoFallaReportada> tiposFallasMemoriaR = new ArrayList<TipoFallaReportada>();
	public ArrayList<FallaReportada> fallasMemoriaR = new ArrayList<FallaReportada>();

	private GlassPopup glass = new GlassPopup();
	
	public IUWidgetFalla() 
	{
		listBoxTiposFallasT.addChangeHandler(new ChangeHandler()
		{
			public void onChange(ChangeEvent arg0) 
			{
				cargarListBoxPorTipoT(fallasMemoriaT);
			}
		});
		
		listBoxTiposFallasR.addChangeHandler(new ChangeHandler()
		{
			public void onChange(ChangeEvent arg0) 
			{
				cargarListBoxPorTipoR(fallasMemoriaR);
			}
		});
	}
	
	public void cargarLtBoxTipoFallaT(final PopupCargando popUp, final ListBox listBoxTiposT) {
		listBoxTiposT.clear();
		popUp.show();
		
		ProyectoBilpa.greetingService.obtenerTiposFallasT(new AsyncCallback<ArrayList<TipoFallaTecnica>>() {
			public void onFailure(Throwable caught) 
			{
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener los tipo de fallas");
				vpu.showPopUp();
			}
			
			public void onSuccess(ArrayList<TipoFallaTecnica> tiposFalla) 
			{
				tiposFallasMemoriaT.clear();
				TipoFallaTecnica auxiliar;
				listBoxTiposT.addItem(Constants.TODOS, -1+"");
				
				for(TipoFallaTecnica tftSinClasificar : tiposFalla)
				{
					if(tftSinClasificar.getDescripcion().equals(Constants.SIN_CLASIFICAR))
					{
						listBoxTiposT.addItem(tftSinClasificar.toString(),String.valueOf(tftSinClasificar.getId()));
						tiposFallasMemoriaT.add(tftSinClasificar);
					}
				}
				
				for (int i=0; i<tiposFalla.size();i++)
				{
					auxiliar = (TipoFallaTecnica) tiposFalla.get(i);
					if(!auxiliar.getDescripcion().equals(Constants.SIN_CLASIFICAR))
					{
						listBoxTiposT.addItem(auxiliar.toString(),String.valueOf(auxiliar.getId()));
						tiposFallasMemoriaT.add(auxiliar);
					}
				}	
				
				if(listBoxTiposT.getItemCount() > 0)
				{
					listBoxTiposT.setSelectedIndex(0);
				}
				cargarLtBoxFallaBaseT(popUp);
				popUp.hide();
			}
		});		
	}	
	
	public void cargarLtBoxTipoFallaR(final PopupCargando popUp, final ListBox listBoxTiposR) {
		listBoxTiposR.clear();
		popUp.show();
		
		ProyectoBilpa.greetingService.obtenerTiposFallasR(new AsyncCallback<ArrayList<TipoFallaReportada>>() {
			public void onFailure(Throwable caught) 
			{
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener los tipo de fallas");
				vpu.showPopUp();
			}
			
			public void onSuccess(ArrayList<TipoFallaReportada> tiposFalla) 
			{
				tiposFallasMemoriaR.clear();
				TipoFallaReportada auxiliar;
				listBoxTiposR.addItem(Constants.TODOS, -1+"");
				
				for(TipoFallaReportada tfrSinClasificar : tiposFalla)
				{
					if(tfrSinClasificar.getDescripcion().equals(Constants.SIN_CLASIFICAR))
					{
						listBoxTiposR.addItem(tfrSinClasificar.toString(),String.valueOf(tfrSinClasificar.getId()));
						tiposFallasMemoriaR.add(tfrSinClasificar);
					}
				}
				
				for (int i=0; i<tiposFalla.size();i++)
				{
					auxiliar = (TipoFallaReportada) tiposFalla.get(i);
					if(!auxiliar.getDescripcion().equals(Constants.SIN_CLASIFICAR))
					{
						listBoxTiposR.addItem(auxiliar.toString(),String.valueOf(auxiliar.getId()));
						tiposFallasMemoriaR.add(auxiliar);
					}
				}	
				
				if(listBoxTiposR.getItemCount() > 0)
				{
					listBoxTiposR.setSelectedIndex(0);
				}
				
				cargarLtBoxFallaBaseR(popUp);
				popUp.hide();
			}
		});		
	}	

	public void cargarLtBoxFallaBaseR(final PopupCargando popUp)
	{
		popUp.show();

		fallasMemoriaR.clear();

		ProyectoBilpa.greetingService.obtenerTodosLasFallasR(new AsyncCallback<ArrayList<FallaReportada>>() {
			public void onFailure(Throwable caught) 
			{
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al cargar los tipos de tareas");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<FallaReportada> fallas) 
			{
				for (FallaReportada ft : fallas)
				{
					fallasMemoriaR.add(ft);
				}
				cargarListBoxPorTipoR(fallas);	
				popUp.hide();
			}
		});		
	}
	
	public void cargarLtBoxFallaBaseT(final PopupCargando popUp)
	{
		popUp.show();

		fallasMemoriaT.clear();

		ProyectoBilpa.greetingService.obtenerTodosLasFallasT(new AsyncCallback<ArrayList<FallaTecnica>>() {
			public void onFailure(Throwable caught) 
			{
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al cargar los tipos de tareas");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<FallaTecnica> fallas) 
			{
				for (FallaTecnica ft : fallas)
				{
					fallasMemoriaT.add(ft);
				}
				cargarListBoxPorTipoT(fallas);	
				popUp.hide();
			}
		});		
	}

	public void cargarListBoxPorTipoR(ArrayList<FallaReportada> fallasR) 
	{
		listBoxFallasR.clear();
		FallaReportada falla;
		if (listBoxTiposFallasR.getSelectedIndex() == 0)//todos
		{
			for (int i=0; i<fallasR.size();i++)
			{
				falla = fallasR.get(i);
				listBoxFallasR.addItem(falla.toString(), String.valueOf(falla.getId()));
			}
		}
		else
		{
			TipoFallaReportada tipoFalla = buscarTipoFallaR(listBoxTiposFallasR);
			for (int i=0; i<fallasR.size();i++)
			{
				falla = fallasR.get(i);
				if (falla != null && falla.getSubTipo() == tipoFalla.getId())
				{
					listBoxFallasR.addItem(falla.toString(), String.valueOf(falla.getId()));
				}
			}
		}
	}
	
	public void cargarListBoxPorTipoT(ArrayList<FallaTecnica> fallasT) 
	{
		listBoxFallasT.clear();
		FallaTecnica falla;
		
		if (listBoxTiposFallasT.getSelectedIndex() == 0)//todos
		{
			for (int i=0; i<fallasT.size();i++)
			{
				falla = fallasT.get(i);
				listBoxFallasT.addItem(falla.toString(),String.valueOf(falla.getId()));
			}
		}
		else
		{
			SubTipoFalla tipoFalla = buscarTipoFallaT(listBoxTiposFallasT);
			for (int i=0; i<fallasT.size();i++)
			{
				falla = fallasT.get(i);
				if (falla != null && falla.getSubTipo() == tipoFalla.getId())
				{
					listBoxFallasT.addItem(falla.toString(),String.valueOf(falla.getId()));
				}
			}
		}
	}

	public TipoFallaTecnica buscarTipoFallaT(ListBox listBox) 
	{
		int idTipoFallaSeleccionada = Integer.valueOf(listBox.getValue(listBox.getSelectedIndex()));
		for (TipoFallaTecnica tipoFalla : tiposFallasMemoriaT)
		{
			if (tipoFalla.getId() == idTipoFallaSeleccionada)
			{
				return tipoFalla;
			}
		}
		return null;
	}
	
	public TipoFallaReportada buscarTipoFallaR(ListBox listBox) 
	{
		int idTipoFallaSeleccionada = Integer.valueOf(listBox.getValue(listBox.getSelectedIndex()));
		for (TipoFallaReportada tipoFalla : tiposFallasMemoriaR)
		{
			if (tipoFalla.getId() == idTipoFallaSeleccionada)
			{
				return tipoFalla;
			}
		}
		return null;
	}
}
