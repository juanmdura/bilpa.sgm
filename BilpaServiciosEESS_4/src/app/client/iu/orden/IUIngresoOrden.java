package app.client.iu.orden;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

import app.client.ProyectoBilpa;
import app.client.dominio.Estacion;
import app.client.dominio.Persona;
import app.client.dominio.Sello;
import app.client.dominio.data.EstacionDataList;
import app.client.iu.menu.IUMenuPrincipal;
import app.client.iu.menu.IUMenuPrincipalAdministrativo;
import app.client.iu.orden.alta.IUAltaOrden;
import app.client.iu.widgets.ValidadorPopup;
import app.client.utilidades.utilObjects.GlassPopup;
import app.client.utilidades.utilObjects.PopupCargando;
import app.client.utilidades.utilObjects.filter.FilteredListDataProvider;
import app.client.utilidades.utilObjects.filter.IFilter;
import app.client.utilidades.utilObjects.filter.IStringValueChanged;
import app.client.utilidades.utilObjects.filter.TextBoxFilter;

public class IUIngresoOrden extends Composite{

	private VerticalPanel vPanelPrincipal = new VerticalPanel();
	private HorizontalPanel hPanel1 = new HorizontalPanel();
	private VerticalPanel vPanel2 = new VerticalPanel();
	private VerticalPanel vPanel3 = new VerticalPanel();

	private TextBoxFilter filterBox = new TextBoxFilter();
	private CellTable<EstacionDataList> cellTableEstaciones = new CellTable<EstacionDataList>();	
	private ArrayList<EstacionDataList> todasLasEstaciones = new ArrayList<EstacionDataList>();
	private EstacionDataList estacionSeleccionada1;

	private SimplePager.Resources pagerResources;
	private SimplePager pager;

	private Label lblTituloPrincipal = new Label("Ingresar nuevo correctivo");

	private Button btnNuevaOrden = new Button("Nuevo correctivo");
	private Persona sesion;
	private String numeroParteDucsa;
	private PopupCargando popUp = new PopupCargando("Cargando...");
	private GlassPopup glass = new GlassPopup();

	public final FilteredListDataProvider<EstacionDataList> estacionesFiltradas = new FilteredListDataProvider<EstacionDataList>(new IFilter<EstacionDataList>()
	{
		public boolean isValid(EstacionDataList value, String filter) {
			if(filter==null || value==null){
				return true;
			}
			return value.getNombre().toLowerCase().contains(filter.toLowerCase()) || value.getNumeroSerie().toLowerCase().contains(filter.toLowerCase());
		}
	});

	public VerticalPanel getPrincipalPanel() {
		return vPanelPrincipal;
	}

	public IUIngresoOrden(Persona persona){
		this.sesion = persona;
		
		final SelectionModel<EstacionDataList> selectionModel1 = new SingleSelectionModel<EstacionDataList>();
		cellTableEstaciones.setSelectionModel(selectionModel1);
		selectionModel1.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				estacionSeleccionada1 = ((SingleSelectionModel<EstacionDataList>) selectionModel1).getSelectedObject();
			}
		});

		pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(cellTableEstaciones);
		pager.setPageSize(15);
		
		setearWidgets();
		agregarWidgets();		
		cargarEstaciones1();
	}


	private void setearWidgets() {
		vPanelPrincipal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		vPanelPrincipal.setWidth("750px");
		cellTableEstaciones.setWidth("750px");
		filterBox.setWidth("140px");
		filterBox.getElement().setAttribute("placeHolder", "Filtrar estaciones");
		btnNuevaOrden.setWidth("140px");
	}


	private void agregarWidgets() {
		lblTituloPrincipal.setStyleName("Titulo");
		vPanelPrincipal.add(lblTituloPrincipal);
		vPanelPrincipal.setSpacing(5);

		vPanel2.add(cellTableEstaciones);
		vPanel2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanel2.add(pager);
		hPanel1.add(vPanel2);
		hPanel1.add(vPanel3);
		hPanel1.setSpacing(20);
		vPanel3.setSpacing(20);
		vPanel3.add(filterBox);
		vPanel3.add(btnNuevaOrden);

		vPanelPrincipal.add(hPanel1);

		btnNuevaOrden.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				preguntarSiesDeDucsa();
			}
		});
		
		filterBox.addKeyPressHandler(new KeyPressHandler()
		{
			public void onKeyPress(KeyPressEvent event) 
			{
				if (KeyCodes.KEY_ENTER == event.getNativeEvent().getKeyCode())
				{
					preguntarSiesDeDucsa();
				}
			}
		});
	}

	private void cargarEstaciones1(){
		popUp.show();
		ProyectoBilpa.greetingService.obtenerEmpresasDataList(new AsyncCallback<ArrayList<EstacionDataList>>() {
			public void onFailure(Throwable caught) {
				ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al obtener empresas");
				vpu.showPopUp();
			}

			public void onSuccess(ArrayList<EstacionDataList> result) {
				for (int i=0; i < result.size(); i++){

					if (result.get(i).getId() != 1) // Si no es Bilpa
					{
						todasLasEstaciones.add(result.get(i));
					}
				}			
				crearTabla();
				popUp.hide();
			}
		});		
	}

	private void crearTabla() {
		TextColumn<EstacionDataList> namecolumn = new TextColumn<EstacionDataList>() {
			@Override
			public String getValue(EstacionDataList e) {
				return e.getNombre();
			}
		};
		
		TextColumn<EstacionDataList> serieColumn = new TextColumn<EstacionDataList>() {
			@Override
			public String getValue(EstacionDataList e) {
				return e.getNumeroSerie();
			}
		};
		
		TextColumn<EstacionDataList> selloColumn = new TextColumn<EstacionDataList>() {
			@Override
			public String getValue(EstacionDataList e) {
				return e.getSello();
			}
		};

		cellTableEstaciones.addColumn(namecolumn,"Nombre");
		cellTableEstaciones.addColumn(serieColumn,"Número de serie");
		cellTableEstaciones.addColumn(selloColumn,"Sello");

		namecolumn.setSortable(true);
		serieColumn.setSortable(true);
		selloColumn.setSortable(true);
		
		// cellTableEstaciones.setColumnWidth(namecolumn, 50, Unit.PCT);
		// cellTableEstaciones.setColumnWidth(serieColumn, 50, Unit.PCT);
		// cellTableEstaciones.setColumnWidth(selloColumn, 150, Unit.PCT);

		List<EstacionDataList> list = cargarEstaciones2();
		ListHandler<EstacionDataList> columnSortHandlerNombre = new ListHandler<EstacionDataList>(list);
		ListHandler<EstacionDataList> columnSortHandlerSerie = new ListHandler<EstacionDataList>(list);
		ListHandler<EstacionDataList> columnSortHandlerSello = new ListHandler<EstacionDataList>(list);
		
		columnSortHandlerNombre.setComparator(namecolumn,new Comparator<EstacionDataList>() {
			public int compare(EstacionDataList o1, EstacionDataList o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getNombre().compareTo(o2.getNombre()) : 1;
				}
				return -1;
			}
		});

		
		columnSortHandlerSerie.setComparator(serieColumn,new Comparator<EstacionDataList>() {
			public int compare(EstacionDataList o1, EstacionDataList o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getNumeroSerie().compareTo(o2.getNumeroSerie()) : 1;
				}
				return -1;
			}
		});
		
		columnSortHandlerSello.setComparator(selloColumn,new Comparator<EstacionDataList>() {
			public int compare(EstacionDataList o1, EstacionDataList o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getSello().compareTo(o2.getSello()) : 1;
				}
				return -1;
			}
		});
		
		cellTableEstaciones.addColumnSortHandler(columnSortHandlerNombre);
		cellTableEstaciones.addColumnSortHandler(columnSortHandlerSerie);
		cellTableEstaciones.addColumnSortHandler(columnSortHandlerSello);
		
		estacionesFiltradas.addDataDisplay(cellTableEstaciones);

		filterBox.addValueChangeHandler(new IStringValueChanged() {
			public void valueChanged(String newValue) {
				estacionesFiltradas.setFilter(newValue);
				estacionesFiltradas.refresh();
			}
		});
	}
	
	public List<EstacionDataList> cargarEstaciones2() {
		estacionesFiltradas.getList().clear();
		List<EstacionDataList> list = estacionesFiltradas.getList();

		for (EstacionDataList estacion : todasLasEstaciones) {
			list.add(estacion);
		}
		cellTableEstaciones.redraw();
		return list;
	}

	private void buscarEstacionPorId(){
		if(estacionSeleccionada1 != null){
			popUp.show();
			ProyectoBilpa.greetingService.buscarEstacion(estacionSeleccionada1.getId(), new AsyncCallback<Estacion>() {

				public void onFailure(Throwable caught) {
					popUp.hide();
					ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al buscar la Estacion");
					vpu.showPopUp();
				}

				public void onSuccess(Estacion result) {
					popUp.hide();
					if (result!=null){
						nuevaOrden(result);
					}
				}
			});		
		}
	}

	private void nuevaOrden(Estacion estacionSeleccionada) {
		if (estacionSeleccionada != null){	
			if(sesion.getRol()==1 || sesion.getRol()==5){
				IUAltaOrden	iuAO = new IUAltaOrden(estacionSeleccionada, sesion, numeroParteDucsa);
				IUMenuPrincipal.getInstancia().agregarWidgetAlMenu(iuAO.getVPanelPrincipal());			
			}
			if (sesion.getRol()==2){
				IUAltaOrden	iuAO = new IUAltaOrden(estacionSeleccionada, sesion, numeroParteDucsa);
				IUMenuPrincipalAdministrativo.getInstancia().agregarWidgetAlMenu(iuAO.getVPanelPrincipal());

			}
		}else{
			ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error en la busqueda de la estación. Contacte a soporte");
			vpu.showPopUp();
		}
	}

	private void preguntarSiesDeDucsa() {
		if(estacionSeleccionada1 != null)
		{
			if (estacionSeleccionada1.getSello().equals(Sello.ANCAPs)){
				dialogoDucsa("Ingrese el número de parte de Ducsa");
			} else {
				buscarEstacionPorId();
			}
		}else{
			ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Debe seleccionar una estación");
			vpu.showPopUp();
		}

	}

	public DialogBox dialogoDucsa(String titulo){
		glass.show();
		final HorizontalPanel hPanelDialTitulo = new HorizontalPanel();
		final HorizontalPanel hPanelDialModif = new HorizontalPanel();
		final HorizontalPanel hPanelDialModif2 = new HorizontalPanel();
		final VerticalPanel vPanelDailModif = new VerticalPanel();
		final TextBox txtNumParteDucsa = new TextBox();
		final DialogBox dialogDucsa = new DialogBox();
		final Label lblTitulo = new Label(titulo);
		lblTitulo.setStyleName("Negrita");

		Button btnEsDucsa = new Button("Aceptar",new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(!txtNumParteDucsa.getText().equalsIgnoreCase(""))
				{
					validarNumeroDUCSA(txtNumParteDucsa.getText());
				}
				else{
					ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "El número del parte de Ducsa no es válido");
					vpu.showPopUp();
					txtNumParteDucsa.setFocus(true);
					txtNumParteDucsa.setSelectionRange(0, txtNumParteDucsa.getText().length());
				}

			}

			private void validarNumeroDUCSA(String numeroDucsa) {
				popUp.show();
				ProyectoBilpa.greetingService.validarNumeroDUCSA(numeroDucsa, new AsyncCallback<Boolean>() {
					public void onFailure(Throwable caught) {
						popUp.hide();
						ValidadorPopup vpu = new ValidadorPopup(glass, "Error", "Error al validar el número de Ducsa");
						vpu.showPopUp();
					}

					public void onSuccess(Boolean result) {
						if (result)
						{
							popUp.hide();
							setearNumeroDucsa(txtNumParteDucsa.getText());
							buscarEstacionPorId();
							dialogDucsa.hide(true);
							glass.hide();
						}
						else
						{
							popUp.hide();
							ValidadorPopup vpu = new ValidadorPopup(glass, "Info", "Ya existe una orden con ese numero de parte de DUCSA");
							vpu.showPopUp();
						}
					}
				});	
			}

			private void setearNumeroDucsa(String num) {
				numeroParteDucsa = num;
			}
		});

		Button btnCancelDucsa = new Button("Cancelar",
				new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogDucsa.hide(true);
				glass.hide();
			}
		});

		Button btnIgnorarDucsa = new Button("Ignorar",
				new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogDucsa.hide(true);
				glass.hide();
				buscarEstacionPorId();
			}
		});

		vPanelDailModif.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanelDailModif.setWidth("600px");
		vPanelDailModif.setSpacing(10);
		txtNumParteDucsa.setWidth("200px");
		btnEsDucsa.setWidth("100px");
		btnIgnorarDucsa.setWidth("100px");
		btnCancelDucsa.setWidth("100px");
		txtNumParteDucsa.setText("");

		hPanelDialModif.add(txtNumParteDucsa);

		hPanelDialModif2.add(btnCancelDucsa);
		hPanelDialModif2.setSpacing(5);
		hPanelDialModif2.add(btnEsDucsa);
		hPanelDialModif2.add(btnIgnorarDucsa);

		hPanelDialTitulo.add(lblTitulo);
		vPanelDailModif.add(hPanelDialTitulo);
		vPanelDailModif.add(hPanelDialModif);
		vPanelDailModif.add(hPanelDialModif2);

		dialogDucsa.setWidget(vPanelDailModif);

		dialogDucsa.show();
		dialogDucsa.center();
		return dialogDucsa;
	}
}

