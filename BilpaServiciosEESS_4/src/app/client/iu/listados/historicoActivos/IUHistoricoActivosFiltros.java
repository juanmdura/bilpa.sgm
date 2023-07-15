package app.client.iu.listados.historicoActivos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.client.ProyectoBilpa;
import app.client.dominio.Activo;
import app.client.dominio.DateDesdeHasta;
import app.client.dominio.Estacion;
import app.client.dominio.Persona;
import app.client.dominio.Tecnico;
import app.client.dominio.data.ActivoData;
import app.client.dominio.data.DatoListadoActivos;
import app.client.dominio.data.EstacionDataList;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.UtilUI;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;
import app.client.utilidades.utilObjects.VerticalPanelAdv;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;

public class IUHistoricoActivosFiltros  extends Composite{

	private HTML htmlTituloPrincipal = new HTML("Histórico de activos");
	private HTML htmlTitulo = new HTML("");
	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private VerticalPanel vPanelData = new VerticalPanel();
	private HorizontalPanel hPanelFiltros = new HorizontalPanel();
	private List<EstacionDataList> listaEstaciones = new ArrayList<EstacionDataList>();
	private ListBox listBoxEstaciones = new ListBox();	
	private ListBox listBoxTipoActivos = new ListBox();
	private ListBox listBoxActivos = new ListBox();
	private ListBox listBoxTecnicos = new ListBox();

	private List<ActivoData> listaActivos = new ArrayList<ActivoData>();
	private ArrayList<VerticalPanelAdv> verticalPanels = new ArrayList<VerticalPanelAdv>();

	private DateBox dbDesde = new DateBox();
	private DateBox dbHasta = new DateBox();
	Date desde;
	Date hasta;

	private Grid grilla = new Grid(3,7);
	private DecoratorPanel decorator = new DecoratorPanel();
	private PopupCargando popUp = new PopupCargando("Cargando...");
	
	private GlassPopup glass = new GlassPopup();

	int idEstacion;
	int idTecnico;
	String idTipoActivo;
	int idActivoSeleccionado;

	Button btnVerHistorico = new Button("Histórico",new ClickHandler() 
	{
		public void onClick(ClickEvent event) 
		{
			if (listBoxActivos.getItemCount() > 0)
			{
				listBoxActivos.setSelectedIndex(0);
			}
			cargarHistorialDeActivos();
		}
	});

	public VerticalPanel getPrincipalPanel() 
	{
		return vPanelPrincipal;
	}

	public IUHistoricoActivosFiltros(Persona persona)
	{
		setearWidgets();			
		cargarDataInicial();	

		cargarGrilla();
		color();

		hPanelFiltros.setSpacing(10);
		vPanelPrincipal.setSpacing(10);
		vPanelData.setSpacing(10);

		vPanelPrincipal.add(htmlTituloPrincipal);
		hPanelFiltros.add(decorator);
		vPanelPrincipal.add(hPanelFiltros);
		vPanelPrincipal.add(htmlTitulo);
		vPanelPrincipal.add(vPanelData);

		listBoxEstaciones.addChangeHandler(new ChangeHandler()
		{
			public void onChange(ChangeEvent arg0) 
			{
				vPanelData.clear();
				obtenerActivosPorTipoYEstacion();
			}
		});

		listBoxEstaciones.addKeyPressHandler(new KeyPressHandler()
		{
			public void onKeyPress(KeyPressEvent event) 
			{
				if (KeyCodes.KEY_ENTER == event.getNativeEvent().getKeyCode())
				{
					vPanelData.clear();
					obtenerActivosPorTipoYEstacion();
				}
			}
		});

		listBoxTecnicos.addChangeHandler(new ChangeHandler() 
		{
			public void onChange(ChangeEvent event) 
			{
				vPanelData.clear();

				if (listBoxActivos.getItemCount()>0)
				{
					listBoxActivos.setSelectedIndex(0);
				}
			}
		});

		listBoxTipoActivos.addChangeHandler(new ChangeHandler() 
		{
			public void onChange(ChangeEvent event) 
			{
				vPanelData.clear();
				cambioTipoActivo();				
			}
		});

		listBoxActivos.addChangeHandler(new ChangeHandler() 
		{
			public void onChange(ChangeEvent event) 
			{
				cambioActivo();				
			}
		});

		btnVerHistorico.addKeyPressHandler(new KeyPressHandler()
		{
			public void onKeyPress(KeyPressEvent event) 
			{
				if (KeyCodes.KEY_ENTER == event.getNativeEvent().getKeyCode())
				{
					vPanelData.clear();
					cargarHistorialDeActivos();
				}
			}
		});
	}

	private void cargarDataInicial() 
	{
		obtenerHoraServidor();
		cargarEstaciones();
		cargarTiposActivos();
		cargarTecnicos();
		listBoxActivos.addItem("N/A", "0");
	}

	private void obtenerHoraServidor() 
	{
		ProyectoBilpa.greetingService.obtenerHoraServidorDate(new AsyncCallback<Date>() {
			public void onFailure(Throwable caught) {
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar la hora del servidor");
				vpu.showPopUp();
			}

			public void onSuccess(Date result) 
			{
				dbDesde.setValue(result);
				dbHasta.setValue(result);
			}
		});	
	}

	private void cargarEstaciones()
	{
		listBoxEstaciones.clear();
		listBoxEstaciones.addItem("Sin Seleccionar", String.valueOf(0));
		listaEstaciones.clear();
		//popUp.show();

		ProyectoBilpa.greetingService.obtenerEmpresasDataList(new AsyncCallback<ArrayList<EstacionDataList>>() {

			public void onFailure(Throwable caught) 
			{
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener las estaciones");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<EstacionDataList> result) 
			{
				EstacionDataList auxiliar;
				for (int i=0; i < result.size(); i++)
				{
					auxiliar = (EstacionDataList) result.get(i);
					if(auxiliar.getId() != 1)
					{
						listBoxEstaciones.addItem(auxiliar.toString(),String.valueOf(auxiliar.getId()));
						listaEstaciones.add(auxiliar);						
					}
				}
				listBoxEstaciones.setSelectedIndex(0);
				idEstacion = Integer.valueOf(listBoxEstaciones.getValue(listBoxEstaciones.getSelectedIndex()));	
				popUp.hide();
			}
		});		
	}

	private void cargarTecnicos()
	{
		listBoxTecnicos.clear();
		ProyectoBilpa.greetingService.obtenerTodosLosTecnicos(new AsyncCallback<ArrayList<Tecnico>>() {
			public void onFailure(Throwable caught) 
			{
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener los tecnicos");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<Tecnico> tecnicos) 
			{
				listBoxTecnicos.addItem("Todos",String.valueOf("0"));
				for (int i=0; i < tecnicos.size(); i++)
				{
					listBoxTecnicos.addItem(tecnicos.get(i).toString(),String.valueOf(tecnicos.get(i).getId()));
				}	
			}
		});	
	}

	private void cargarTiposActivos()
	{
		listBoxTipoActivos.clear();
		int idEstacion = Integer.valueOf(listBoxEstaciones.getValue(listBoxEstaciones.getSelectedIndex()));
		ProyectoBilpa.greetingService.obtenerTiposDeActivos(idEstacion, false, new AsyncCallback<ArrayList<String>>() {
			public void onFailure(Throwable caught) 
			{
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener los tipos de activos");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<String> tiposActivo) 
			{
				listBoxTipoActivos.addItem("Todos",String.valueOf("0"));
				for (int i=0; i < tiposActivo.size(); i++)
				{
					listBoxTipoActivos.addItem(tiposActivo.get(i),String.valueOf(i+1));
				}	
			}
		});		
	}

	private void color() 
	{
		htmlTitulo.setStyleName("Titulo");
		htmlTituloPrincipal.setStyleName("Titulo");
		btnVerHistorico.setTitle("Lista el histórico del activo seleccionado");
	}

	private void cargarGrilla()
	{
		grilla.setCellSpacing(20);
		grilla.setWidget(0, 0, label("Estación"));
		grilla.setWidget(0, 1, listBoxEstaciones);

		grilla.setWidget(0, 2, label("Tipo Activo"));
		grilla.setWidget(0, 3, listBoxTipoActivos);

		grilla.setWidget(0, 4, label("Activo"));
		grilla.setWidget(0, 5, listBoxActivos);

		grilla.setWidget(1, 0, label("Técnico"));
		grilla.setWidget(1, 1, listBoxTecnicos);

		grilla.setWidget(1, 2, label("Desde"));
		grilla.setWidget(1, 3, dbDesde);

		grilla.setWidget(1, 4, label("Hasta"));
		grilla.setWidget(1, 5, dbHasta);

		grilla.setWidget(2, 3, btnVerHistorico);

		dbDesde.setWidth("255px");
		dbHasta.setWidth("255px");
		listBoxEstaciones.setWidth("255px");
		listBoxTipoActivos.setWidth("255px");
		listBoxActivos.setWidth("255px");
		listBoxTecnicos.setWidth("255px");

		decorator.add(grilla);
	}

	private Label label(String text) 
	{
		Label label = new InlineLabel(text);
		label.setStyleName("Negrita");
		label.setWordWrap(false);
		return label;
	}

	private void setearWidgets() 
	{
		htmlTitulo.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		htmlTituloPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelPrincipal.setSpacing(5);
		vPanelPrincipal.setWidth("800px");
		btnVerHistorico.setWidth("100px");

		listBoxActivos.setTitle("Lista de Activos");
		listBoxEstaciones.setTitle("Lista de Estaciones");
	}

	private void cambioTipoActivo()
	{
		idTipoActivo = listBoxTipoActivos.getValue(listBoxTipoActivos.getSelectedIndex());
		listBoxActivos.clear();
		listBoxActivos.addItem("Todos", String.valueOf(0));

		if (idTipoActivo.equals("0"))
		{
			for (ActivoData a : listaActivos)
			{
				listBoxActivos.addItem(a.getDescripcion(), String.valueOf(a.getId()));
			}
		}
		else 
		{
			for (ActivoData a : listaActivos)
			{
				if (a.getTipo() == idTipoActivo)
				{
					listBoxActivos.addItem(a.getDescripcion(), String.valueOf(a.getId()));
				}
			}
		}

		cambioActivo();

		if (listBoxActivos.getItemCount() > 1)
		{
			listBoxActivos.setSelectedIndex(0);
		}
		else
		{
			listBoxActivos.clear();
			vPanelData.clear();
			listBoxActivos.addItem("No hay " + idTipoActivo.toLowerCase() + " reparados", String.valueOf(0));
		}
		popUp.hide();
	}

	private ArrayList<Activo> obtenerActivosPorTipoYEstacion() 
	{
		listBoxActivos.clear();
		idTipoActivo = "0";
		idEstacion = Integer.valueOf(listBoxEstaciones.getValue(listBoxEstaciones.getSelectedIndex()));

		if (idEstacion > 0)
		{
			Estacion e = new Estacion();
			e.setId(idEstacion);

			ProyectoBilpa.greetingService.obtenerDataActivosPorTipo(e, Integer.valueOf(idTipoActivo), new AsyncCallback<ArrayList<ActivoData>>() {
				public void onFailure(Throwable caught) 
				{
					popUp.hide();
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener los activos");
					vpu.showPopUp();
				}

				public void onSuccess(ArrayList<ActivoData> activos) 
				{
					listBoxActivos.clear();
					listaActivos.clear();
					listBoxActivos.addItem("Todos", String.valueOf(0));

					ActivoData auxiliar;
					for (int i=0; i < activos.size(); i++)
					{
						auxiliar = (ActivoData) activos.get(i);
						if(auxiliar.getId() != 1)
						{
							listBoxActivos.addItem(auxiliar.getDescripcion(), String.valueOf(auxiliar.getId()));
							listaActivos.add(auxiliar);						
						}
					}
					listBoxActivos.setSelectedIndex(0);
					popUp.hide();
				}
			});	
		}
		else
		{
			listBoxActivos.addItem("N/A", String.valueOf(0));
			listBoxTipoActivos.setSelectedIndex(0);
		}

		return null;
	}

	private void cargarHistorialDeActivos()
	{
		popUp.show();
		idEstacion = Integer.valueOf(listBoxEstaciones.getValue(listBoxEstaciones.getSelectedIndex()));
		idTecnico = Integer.valueOf(listBoxTecnicos.getValue(listBoxTecnicos.getSelectedIndex()));
		idTipoActivo = listBoxTipoActivos.getValue(listBoxTipoActivos.getSelectedIndex());

		desde = dbDesde.getValue();
		hasta = dbHasta.getValue();

		if (idEstacion > 0)
		{
			Estacion estacion = new Estacion();
			estacion.setId(idEstacion);

			Tecnico tecnico = new Tecnico();
			tecnico.setId(idTecnico);

			ProyectoBilpa.greetingService.obtenerDataActivosPorTipo(estacion, desde, hasta, tecnico, Integer.valueOf(idTipoActivo), new AsyncCallback<ArrayList<DatoListadoActivos>>() {
				public void onSuccess(ArrayList<DatoListadoActivos> result) 
				{
					vPanelData.clear();
					verticalPanels.clear();
					if (result.size() > 0 && result.get(0).getReparaciones().size() > 0)
					{
						cargarTituloEstacion(result.get(0).getReparaciones().get(0).getOrden().getEmpresa().getNombre());
						for (DatoListadoActivos dla : result)
						{
							if (dla.getReparaciones().size() > 0)
							{
								dla.setDesde(desde);
								dla.setHasta(hasta);
								IUHistoricoActivosData ul = new IUHistoricoActivosData(dla, popUp);

								VerticalPanelAdv vp = ul.getPrincipalPanel();
								vp.setId(dla.getReparaciones().get(0).getActivo().getId());
								vp.setTipo(dla.getReparaciones().get(0).getActivo().getTipo());
								vPanelData.add(vp);
								verticalPanels.add(vp);
							}
						}
					}
					else
					{
						noExistenActivosParaMostrar();
					}
					popUp.hide();
				}

				public void onFailure(Throwable caught) 
				{
					popUp.hide();
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener activos por tipo");
					vpu.showPopUp();
				}
			});			
		}
		else
		{
			noExistenActivosParaMostrar();
		}
	}

	private void noExistenActivosParaMostrar() 
	{
		vPanelData.clear();
		Label lblNoHayOrdenes = new Label("No existen activos para mostrar");
		lblNoHayOrdenes.setStyleName("SubTitulo");
		vPanelData.add(lblNoHayOrdenes);
		popUp.hide();
	}

	private void cargarTituloEstacion(final String nombreEstacion) 
	{		
		ProyectoBilpa.greetingService.formatearFechas(desde, hasta, new AsyncCallback<DateDesdeHasta>() {
			public void onFailure(Throwable caught) 
			{
				popUp.hide();
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar la hora del servidor");
				vpu.showPopUp();
			}

			public void onSuccess(DateDesdeHasta result) 
			{
				popUp.hide();
				htmlTitulo.setText("Reparaciones entre el " + result.getDesde() + " y " + result.getHasta() + ", estación " + nombreEstacion) ;
			}
		});	
	}

	private void cambioActivo() 
	{
		vPanelData.clear();
		idActivoSeleccionado = Integer.valueOf(listBoxActivos.getValue(listBoxActivos.getSelectedIndex()));
		idTipoActivo = listBoxTipoActivos.getValue(listBoxTipoActivos.getSelectedIndex());

		if (idActivoSeleccionado > 0)
		{
			for (VerticalPanelAdv vp : verticalPanels)
			{
				if (vp.getId() == idActivoSeleccionado)
				{
					vPanelData.add(vp);
				}
			}
		}
		else if (idActivoSeleccionado == 0)//TODOS LOS ACTIVOS
		{
			for (VerticalPanelAdv vp : verticalPanels)
			{
				if (vp.getTipo() == Integer.valueOf(idTipoActivo) || 
						idTipoActivo.equalsIgnoreCase("0"))//NO HAY FILTRO DE ACTIVOS
				{
					vPanelData.add(vp);
					UtilUI.agregarEspacioEnBlanco(vPanelData, 3);
				}
			}
		}
	}
}
