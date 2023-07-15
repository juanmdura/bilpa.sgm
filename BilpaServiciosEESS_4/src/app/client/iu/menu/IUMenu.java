package app.client.iu.menu;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import app.client.ProyectoBilpa;
import app.client.dominio.Estacion;
import app.client.dominio.Orden;
import app.client.dominio.Persona;
import app.client.dominio.Sello;
import app.client.iu.IULogin;
import app.client.iu.activos.bomba.IUGestionarBomba;
import app.client.iu.activos.generico.activo.IUGestionarActivoGenerico;
import app.client.iu.activos.generico.tipoMarcaModelo.IUGestionarTipoMarcaModeloMain;
import app.client.iu.activos.surtidor.IUGestionMarca;
import app.client.iu.activos.surtidor.IUGestionarModelo;
import app.client.iu.activos.surtidor.IUGestionarPico;
import app.client.iu.activos.surtidor.IUGestionarSurtidores;
import app.client.iu.activos.tanque.IUGestionarTanque;
import app.client.iu.estacion.IUEliminarEstacion;
import app.client.iu.estacion.IUGestionarEstacion;
import app.client.iu.falla.IUEliminarFalla;
import app.client.iu.falla.IUEliminarTipoFalla;
import app.client.iu.falla.IUGestionarFalla;
import app.client.iu.falla.IUGestionarTipoFalla;
import app.client.iu.listados.IUCierrePetroleras;
import app.client.iu.listados.IUConsultaHistoricoOrdenesPorTecnico;
import app.client.iu.listados.IUConsultaInformeDePendiente;
import app.client.iu.listados.IUReporteVisitas;
import app.client.iu.listados.historicoActivos.IUHistoricoActivosFiltros;
import app.client.iu.listados.historicoReparaciones.IUHistoricoReparacionesFiltros;
import app.client.iu.orden.IUListaOrdenes;
import app.client.iu.orden.alta.IUAltaOrden;
import app.client.iu.orden.inicio.IUSeguimientoInicial;
import app.client.iu.pendientes.IUPendientes;
import app.client.iu.preventivos.visitas.IUVisitasEstacionesDucsa;
import app.client.iu.preventivos.visitas.IUVisitasEstacionesPetrobras;
import app.client.iu.preventivos.visitas.IUVisitasPlantasAncapContratos;
import app.client.iu.repuesto.IUEliminarRepuesto;
import app.client.iu.repuesto.IUEliminarTipoRepuesto;
import app.client.iu.repuesto.IUGestionarRepuesto;
import app.client.iu.repuesto.IUGestionarTipoRepuesto;
import app.client.iu.tarea.IUEliminarTarea;
import app.client.iu.tarea.IUEliminarTipoTarea;
import app.client.iu.tarea.IUGestionarTarea;
import app.client.iu.tarea.IUGestionarTipoTarea;
import app.client.iu.usuario.IUEliminarUsuario;
import app.client.iu.usuario.IUGestionarUsuario;

public abstract class IUMenu extends Composite {

	protected MenuBar menuBar = new MenuBar(false);
	private HorizontalPanel panelHorizontalMenuBar = new HorizontalPanel();
	private VerticalPanel vBig = new VerticalPanel();
	// private HorizontalPanel hNorte = new HorizontalPanel();
	// private DecoratorPanel decSur = new DecoratorPanel();
	protected VerticalPanel panelVerticalPrincipal = new VerticalPanel();
	private int contadorDeWidgets = 0;
	private Label lblLogOut = new Label("Cerrar sesión");
	HorizontalPanel hpLogOut = new HorizontalPanel();

	protected static IUMenu instancia = null;

	protected Persona sesion;

	public Persona getSesion() {
		return sesion;
	}

	public void setSesion(Persona sesion) {
		this.sesion = sesion;
		cargarListaOrdenes(); // Por defecto se muestra la lista de ordenes del
								// tecnico. Poner esta linea en otro lado!!!
	}

	public IUMenu() {
		lblLogOut.setStyleName("Subrayado");
		lblLogOut.addMouseOverHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				lblLogOut.setStyleName("MouseOver");
			}
		});

		lblLogOut.addMouseOutHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				lblLogOut.removeStyleName("MouseOver");
				lblLogOut.setStyleName("Subrayado");
			}
		});

		panelVerticalPrincipal
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panelHorizontalMenuBar
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panelVerticalPrincipal
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		panelVerticalPrincipal
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		hpLogOut.setSize("100%", "100%");
		// hpLogOut.setStyleName("ElAzul");

		hpLogOut.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		hpLogOut.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		hpLogOut.add(lblLogOut);
		// hNorte.add(hpLogOut);

		ProyectoBilpa.getSesion().add(hpLogOut);

		panelHorizontalMenuBar.add(menuBar);
		panelVerticalPrincipal.add(panelHorizontalMenuBar);
		panelVerticalPrincipal.setSpacing(5);

		menuBar.setWidth("1200px");
		menuBar.setHeight("35px");
		menuBar.setWidth("100%");

		color();

		// vBig.add(hNorte);
		vBig.add(panelVerticalPrincipal);
		// vBig.add(decSur);

		lblLogOut.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				logOut();
			}
		});

		initWidget(vBig);
	}

	public void color() {
		vBig.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vBig.setWidth("100%");
		panelVerticalPrincipal.setWidth("100%");
		panelHorizontalMenuBar.setWidth("100%");
	}

	public void agregarWidgetAlMenu(Widget widget) {
		validarSiHayWidgetActivo();
		panelVerticalPrincipal.add(widget);
	}

	protected void cargarListaOrdenes() {
		validarSiHayWidgetActivo();
		IUListaOrdenes iu = new IUListaOrdenes(sesion);
		//IUReporteVisitas iu = new IUReporteVisitas(sesion);
		panelVerticalPrincipal.add(iu.getPrincipalPanel());
		// TODO QUITAR
		
		/*ProyectoBilpa.greetingService.buscarEstacion(36, new AsyncCallback<Estacion>() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess(Estacion result) {
				IUAltaOrden	iu = new IUAltaOrden(result, sesion, 0);
				panelVerticalPrincipal.add(iu.getVPanelPrincipal());
			}
		});*/
		
		/*ProyectoBilpa.greetingService.buscarOrden(7486, new AsyncCallback<Orden>() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess(Orden result) {
				IUSeguimientoInicial iu = new IUSeguimientoInicial(result, sesion);
				panelVerticalPrincipal.add(iu.getVPanelPrincipal());
			}
		});*/
		
		/*ProyectoBilpa.greetingService.buscarOrden(66956, new AsyncCallback<Orden>() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess(Orden result) {
				IUSeguimientoTecnico iu = new IUSeguimientoTecnico(result, sesion);
				panelVerticalPrincipal.add(iu.getVPanelPrincipal());
			}
		});*/
	}

	protected void cargarMenuOrdenes() {
		MenuItem menuItemOrden = new MenuItem("Correctivos", false, new Command() 
		{
			public void execute()
			{
				validarSiHayWidgetActivo();
				IUListaOrdenes iuListaOrdenes = new IUListaOrdenes(sesion);
				panelVerticalPrincipal.add(iuListaOrdenes.getPrincipalPanel());
			}
		});
		menuItemOrden.setWidth("200");

		menuBar.addItem(menuItemOrden);
	}
	
	protected void cargarMenuPendientes() {
		MenuBar menuBarPreventivos = new MenuBar(true);
		MenuItem menuItemCorrectivos = new MenuItem("Pendientes", false, menuBarPreventivos);

		MenuItem menuItemPreventivosDucsa = new MenuItem("Pendientes Ducsa", false,
				new Command() {
					public void execute() {
						validarSiHayWidgetActivo();
						IUPendientes iur = new IUPendientes(sesion, Sello.ANCAP);
						panelVerticalPrincipal.add(iur.getPrincipalPanel());
					}
				});
		menuBarPreventivos.addItem(menuItemPreventivosDucsa);

		MenuItem menuItemPreventivosPetrobras = new MenuItem("Pendientes Petrobras", false,
				new Command() {
					public void execute() {
						validarSiHayWidgetActivo();
						IUPendientes iur = new IUPendientes(sesion, Sello.PETROBRAS);
						panelVerticalPrincipal.add(iur.getPrincipalPanel());
					}
				});
		menuBarPreventivos.addItem(menuItemPreventivosPetrobras);
		
		MenuItem menuItemPreventivosAncapContratos = new MenuItem("Preventivos Ancap Contratos", false,
				new Command() {
					public void execute() {
						validarSiHayWidgetActivo();
						IUPendientes iur = new IUPendientes(sesion, Sello.ANCAP_CONTRATOS);
						panelVerticalPrincipal.add(iur.getPrincipalPanel());
					}
				});
		menuBarPreventivos.addItem(menuItemPreventivosAncapContratos);
		
		menuItemCorrectivos.setWidth("200");
		menuItemCorrectivos.setHeight("30");

		menuBar.addItem(menuItemCorrectivos);
	}
	
	protected void cargarMenuPreventivos() {
		MenuBar menuBarPreventivos = new MenuBar(true);
		MenuItem menuItemCorrectivos = new MenuItem("Preventivos", false, menuBarPreventivos);

		MenuItem menuItemPreventivosDucsa = new MenuItem("Preventivos Ducsa", false,
				new Command() {
					public void execute() {
						validarSiHayWidgetActivo();
						IUVisitasEstacionesDucsa iur = new IUVisitasEstacionesDucsa(sesion);
						panelVerticalPrincipal.add(iur.getPrincipalPanel());
					}
				});
		menuBarPreventivos.addItem(menuItemPreventivosDucsa);

		MenuItem menuItemPreventivosPetrobras = new MenuItem("Preventivos Petrobras", false,
				new Command() {
					public void execute() {
						validarSiHayWidgetActivo();
						IUVisitasEstacionesPetrobras iur = new IUVisitasEstacionesPetrobras(sesion);
						panelVerticalPrincipal.add(iur.getPrincipalPanel());
					}
				});
		menuBarPreventivos.addItem(menuItemPreventivosPetrobras);
		
		MenuItem menuItemPreventivosAncapContratos = new MenuItem("Preventivos Ancap Contratos", false,
				new Command() {
					public void execute() {
						validarSiHayWidgetActivo();
						IUVisitasPlantasAncapContratos iur = new IUVisitasPlantasAncapContratos(sesion);
						panelVerticalPrincipal.add(iur.getPrincipalPanel());
					}
				});
		menuBarPreventivos.addItem(menuItemPreventivosAncapContratos);
		
		menuItemCorrectivos.setWidth("200");
		menuItemCorrectivos.setHeight("30");

		menuBar.addItem(menuItemCorrectivos);
	}
	
	public void cargarMenuFallas() {
		MenuBar menuBarFallas = new MenuBar(true);
		MenuItem menuItemFalla = new MenuItem("Fallas", false, menuBarFallas);

		MenuItem menuItemNuevaFalla = new MenuItem("Gestionar fallas", false,
				new Command() {
					public void execute() {
						IUGestionarFalla gF = new IUGestionarFalla(sesion);
						validarSiHayWidgetActivo();
						panelVerticalPrincipal.add(gF.getVpPrincipal());
					}
				});
		menuBarFallas.addItem(menuItemNuevaFalla);

		MenuItem menuItemEliminarFalla = new MenuItem("Eliminar fallas", false,
				new Command() {
					public void execute() {
						IUEliminarFalla elimF = new IUEliminarFalla(sesion);
						validarSiHayWidgetActivo();
						panelVerticalPrincipal.add(elimF.getVpPrincipal());
					}
				});
		menuBarFallas.addItem(menuItemEliminarFalla);

		MenuItem menuItemTipoFalla = new MenuItem("Gestionar tipos", false,
				new Command() {
					public void execute() {
						validarSiHayWidgetActivo();
						IUGestionarTipoFalla iur = new IUGestionarTipoFalla(
								sesion);
						panelVerticalPrincipal.add(iur.getPrincipalPanel());
					}
				});

		menuBarFallas.addItem(menuItemTipoFalla);

		MenuItem menuItemEliminarTipos = new MenuItem("Eliminar tipos", false,
				new Command() {
					public void execute() {
						validarSiHayWidgetActivo();
						IUEliminarTipoFalla iur = new IUEliminarTipoFalla(
								sesion);
						panelVerticalPrincipal.add(iur.getPrincipalPanel());
					}
				});

		menuBarFallas.addItem(menuItemEliminarTipos);

		menuItemFalla.setWidth("200");
		menuBar.addItem(menuItemFalla);

	}


	public void cargarMenuTareas() {
		MenuBar menuBarTareas = new MenuBar(true);
		MenuItem menuItemTareas = new MenuItem("Tareas", false, menuBarTareas);

		MenuItem menuItemNuevaTarea = new MenuItem("Gestionar tareas", false,
				new Command() {
					public void execute() {
						validarSiHayWidgetActivo();
						IUGestionarTarea iur = new IUGestionarTarea(sesion);
						panelVerticalPrincipal.add(iur.getPrincipalPanel());
					}
				});
		menuBarTareas.addItem(menuItemNuevaTarea);

		MenuItem menuItemEliminarTarea = new MenuItem("Eliminar tareas", false,
				new Command() {
					public void execute() {
						validarSiHayWidgetActivo();
						IUEliminarTarea iur = new IUEliminarTarea(sesion);
						panelVerticalPrincipal.add(iur.getPrincipalPanel());
					}
				});

		menuBarTareas.addItem(menuItemEliminarTarea);

		MenuItem menuItemTipoTarea = new MenuItem("Gestionar tipos", false,
				new Command() {
					public void execute() {
						validarSiHayWidgetActivo();
						IUGestionarTipoTarea iur = new IUGestionarTipoTarea(
								sesion);
						panelVerticalPrincipal.add(iur.getPrincipalPanel());
					}
				});

		menuBarTareas.addItem(menuItemTipoTarea);

		MenuItem menuItemEliminarTipos = new MenuItem("Eliminar tipos", false,
				new Command() {
					public void execute() {
						validarSiHayWidgetActivo();
						IUEliminarTipoTarea iur = new IUEliminarTipoTarea(
								sesion);
						panelVerticalPrincipal.add(iur.getPrincipalPanel());
					}
				});

		menuBarTareas.addItem(menuItemEliminarTipos);

		menuItemTareas.setWidth("200");
		menuBar.addItem(menuItemTareas);
	}

	public void cargarMenuRepuestos() {
		MenuBar menuBarRepuestos = new MenuBar(true);
		MenuItem menuItemRepuesto = new MenuItem("Repuestos", false,
				menuBarRepuestos);

		MenuItem menuItemNuevoRepuesto = new MenuItem("Gestionar repuestos",
				false, new Command() {
					public void execute() {
						validarSiHayWidgetActivo();
						IUGestionarRepuesto iur = new IUGestionarRepuesto(
								sesion);
						panelVerticalPrincipal.add(iur.getPrincipalPanel());
					}
				});
		menuBarRepuestos.addItem(menuItemNuevoRepuesto);

		MenuItem menuItemEliminarRepuesto = new MenuItem("Eliminar repuestos",
				false, new Command() {
					public void execute() {
						validarSiHayWidgetActivo();
						IUEliminarRepuesto iur = new IUEliminarRepuesto(sesion);
						panelVerticalPrincipal.add(iur.getPrincipalPanel());
					}
				});

		menuBarRepuestos.addItem(menuItemEliminarRepuesto);

		MenuItem menuItemTipoRepuesto = new MenuItem("Gestionar tipos", false,
				new Command() {
					public void execute() {
						validarSiHayWidgetActivo();
						IUGestionarTipoRepuesto iur = new IUGestionarTipoRepuesto(
								sesion);
						panelVerticalPrincipal.add(iur.getPrincipalPanel());
					}
				});

		menuBarRepuestos.addItem(menuItemTipoRepuesto);

		MenuItem menuItemEliminarTipos = new MenuItem("Eliminar tipos", false,
				new Command() {
					public void execute() {
						validarSiHayWidgetActivo();
						IUEliminarTipoRepuesto iur = new IUEliminarTipoRepuesto(
								sesion);
						panelVerticalPrincipal.add(iur.getPrincipalPanel());
					}
				});

		menuBarRepuestos.addItem(menuItemEliminarTipos);

		menuItemRepuesto.setWidth("200");
		menuBar.addItem(menuItemRepuesto);
	}

	public void cargarMenuEmpresas() {
		MenuBar menuBarEmpresas = new MenuBar(true);
		MenuItem menuItemEmpresas = new MenuItem("Estaciones", false,
				menuBarEmpresas);

		MenuItem menuItemNuevaEmpresa = new MenuItem("Gestionar estaciones",
				false, new Command() {
					public void execute() {
						validarSiHayWidgetActivo();
						IUGestionarEstacion iur = new IUGestionarEstacion(
								sesion);
						panelVerticalPrincipal.add(iur.getVpPrincipal());
					}
				});
		menuBarEmpresas.addItem(menuItemNuevaEmpresa);

		MenuItem menuItemEliminarEmpresa = new MenuItem("Eliminar estaciones",
				false, new Command() {
					public void execute() {
						validarSiHayWidgetActivo();
						IUEliminarEstacion iur = new IUEliminarEstacion(sesion);
						panelVerticalPrincipal.add(iur.getVpPrincipal());
					}
				});

		menuBarEmpresas.addItem(menuItemEliminarEmpresa);

		menuItemEmpresas.setWidth("200");
		menuBar.addItem(menuItemEmpresas);
	}

	public void cargarMenuActivos() {
		MenuBar menuBarActivos = new MenuBar(true);
		MenuItem menuItemActivos = new MenuItem("Activos", false,
				menuBarActivos);

		MenuItem menuItemGestionarMarcas = new MenuItem("Gestionar marcas",
				false, new Command() {
					public void execute() {
						validarSiHayWidgetActivo();
						IUGestionMarca iur = new IUGestionMarca(sesion);
						panelVerticalPrincipal.add(iur.getPrincipalPanel());
					}
				});

		MenuItem menuItemGestionarModelos = new MenuItem("Gestionar modelos",
				false, new Command() {
					public void execute() {
						validarSiHayWidgetActivo();
						IUGestionarModelo iur = new IUGestionarModelo(sesion);
						panelVerticalPrincipal.add(iur.getPrincipalPanel());
					}
				});

		MenuItem menuItemGestionarSurtidores = new MenuItem(
				"Gestionar surtidores", false, new Command() {
					public void execute() {
						validarSiHayWidgetActivo();
						IUGestionarSurtidores iur = new IUGestionarSurtidores(
								sesion);
						panelVerticalPrincipal.add(iur.getPrincipalPanel());
					}
				});

		MenuItem menuItemGestionarPicos = new MenuItem("Gestionar picos",
				false, new Command() {
					public void execute() {
						validarSiHayWidgetActivo();
						IUGestionarPico iur = new IUGestionarPico(sesion);
						panelVerticalPrincipal.add(iur.getPrincipalPanel());
					}
				});

		MenuBar subMenuSurtidores = new MenuBar(true);
		subMenuSurtidores.addItem(menuItemGestionarMarcas);
		subMenuSurtidores.addItem(menuItemGestionarModelos);
		subMenuSurtidores.addItem(menuItemGestionarSurtidores);
		subMenuSurtidores.addItem(menuItemGestionarPicos);

		Command commandTanques = new Command() {
			public void execute() {
				validarSiHayWidgetActivo();
				IUGestionarTanque iur = new IUGestionarTanque(sesion);
				panelVerticalPrincipal.add(iur.getPrincipalPanel());
			}
		};

		Command commandBombas = new Command() {
			public void execute() {
				validarSiHayWidgetActivo();
				IUGestionarBomba iur = new IUGestionarBomba(sesion);
				panelVerticalPrincipal.add(iur.getPrincipalPanel());
			}
		};

		Command commandGenerico = new Command() {
			public void execute() {
				validarSiHayWidgetActivo();
				IUGestionarActivoGenerico iur = new IUGestionarActivoGenerico(sesion);
				panelVerticalPrincipal.add(iur.getPrincipalPanel());
			}
		};

		MenuItem menuItemGestionarTiposGenericos = new MenuItem("Gestionar activos genéricos",
				false, new Command() {
					public void execute() {
						validarSiHayWidgetActivo();
						IUGestionarActivoGenerico iur = new IUGestionarActivoGenerico(sesion);
						panelVerticalPrincipal.add(iur.getPrincipalPanel());
					}
				});
		
		MenuItem menuItemGestionarActivosGenericos = new MenuItem("Gestionar tipos genéricos",
				false, new Command() {
					public void execute() {
						validarSiHayWidgetActivo();
						IUGestionarTipoMarcaModeloMain iur = new IUGestionarTipoMarcaModeloMain(sesion);
						panelVerticalPrincipal.add(iur.getPrincipalPanel());
					}
				});
		
		MenuBar subMenuGenericos = new MenuBar(true);
		subMenuGenericos.addItem(menuItemGestionarTiposGenericos);
		subMenuGenericos.addItem(menuItemGestionarActivosGenericos);
		
		menuBarActivos.addItem("Bombas", commandBombas);
		menuBarActivos.addItem("Surtidores", subMenuSurtidores);
		menuBarActivos.addItem("Tanques", commandTanques);
		menuBarActivos.addItem("Genéricos", subMenuGenericos);

		menuItemActivos.setWidth("200");
		menuBar.addItem(menuItemActivos);
	}

	public void cargarMenuUsuarios() {
		MenuBar menuBarUsuarios = new MenuBar(true);
		MenuItem menuItemUsuario = new MenuItem("Usuarios", false,
				menuBarUsuarios);

		MenuItem menuItemNuevoUsuario = new MenuItem("Gestionar usuarios",
				false, new Command() {
					public void execute() {
						validarSiHayWidgetActivo();
						IUGestionarUsuario iur = new IUGestionarUsuario(sesion);
						panelVerticalPrincipal.add(iur.getVpPrincipal());
					}
				});
		menuBarUsuarios.addItem(menuItemNuevoUsuario);

		MenuItem menuItemEliminarUsuario = new MenuItem("Eliminar usuarios",
				false, new Command() {
					public void execute() {
						validarSiHayWidgetActivo();
						IUEliminarUsuario iur = new IUEliminarUsuario(sesion);
						panelVerticalPrincipal.add(iur.getPrincipalPanel());
					}
				});

		menuBarUsuarios.addItem(menuItemEliminarUsuario);

		menuItemUsuario.setWidth("200");
		menuBar.addItem(menuItemUsuario);
	}

	protected void cargarMenuEstadisticas() {
		MenuBar menuBarConsultas = new MenuBar(true);
		MenuItem menuItemConsultas = new MenuItem("Estadísticas", false,
				menuBarConsultas);

		MenuItem menuItemRP = new MenuItem("Reporte preventivos",
				false, new Command() {
					public void execute() {
						validarSiHayWidgetActivo();
						IUReporteVisitas iur = new IUReporteVisitas(sesion);
						panelVerticalPrincipal.add(iur.getPrincipalPanel());
					}
				});

		menuBarConsultas.addItem(menuItemRP);
		
		MenuItem menuItemCierreMensual = new MenuItem("Cierre de petroleras",
				false, new Command() {
					public void execute() {
						validarSiHayWidgetActivo();
						IUCierrePetroleras iur = new IUCierrePetroleras(sesion);
						panelVerticalPrincipal.add(iur.getPrincipalPanel());
					}
				});

		menuBarConsultas.addItem(menuItemCierreMensual);

		MenuItem menuItemHistoricoActivos = new MenuItem("Histórico activos",
				false, new Command() {
					public void execute() {
						validarSiHayWidgetActivo();
						IUHistoricoActivosFiltros iur = new IUHistoricoActivosFiltros(
								sesion);
						panelVerticalPrincipal.add(iur.getPrincipalPanel());
					}
				});

		menuBarConsultas.addItem(menuItemHistoricoActivos);

		MenuItem menuItemHistoricoReparaciones = new MenuItem(
				"Histórico reparaciones", false, new Command() {
					public void execute() {
						validarSiHayWidgetActivo();
						IUHistoricoReparacionesFiltros iur = new IUHistoricoReparacionesFiltros(
								sesion);
						panelVerticalPrincipal.add(iur.getPrincipalPanel());
					}
				});

		menuBarConsultas.addItem(menuItemHistoricoReparaciones);

		MenuItem menuItemConsultaHistoricoFallas = new MenuItem(
				"Ordenes por técnico", false, new Command() {
					public void execute() {
						validarSiHayWidgetActivo();
						IUConsultaHistoricoOrdenesPorTecnico iur = new IUConsultaHistoricoOrdenesPorTecnico(
								sesion);
						panelVerticalPrincipal.add(iur.getPrincipalPanel());
					}
				});

		// menuBarConsultas.addItem(menuItemConsultaHistoricoFallas);
		
		MenuItem menuItemInformeDePendientes = new MenuItem("Informe de pendientes", false, new Command() {
			public void execute() {
				validarSiHayWidgetActivo();
				IUConsultaInformeDePendiente iur = new IUConsultaInformeDePendiente();
				panelVerticalPrincipal.add(iur.getPrincipalPanel());
			}
		});
		menuBarConsultas.addItem(menuItemInformeDePendientes);
		
		menuItemConsultas.setWidth("250");
		menuBar.addItem(menuItemConsultas);
	}

	public void validarSiHayWidgetActivo() {
		contadorDeWidgets = panelVerticalPrincipal.getWidgetCount();
		if (contadorDeWidgets > 1) {
			panelVerticalPrincipal.remove(1);
		}
	}

	private void logOut() {
		sesion = null;
		instancia = null;

		lblLogOut.removeStyleName("MouseOver");
		lblLogOut.setStyleName("Subrayado");

		ProyectoBilpa.getSesion().clear();

		IULogin iu = new IULogin();

		ProyectoBilpa.getRoot().clear();
		ProyectoBilpa.getRoot().add(iu);

	}

}