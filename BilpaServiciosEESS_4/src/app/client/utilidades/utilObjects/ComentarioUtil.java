package app.client.utilidades.utilObjects;


public class ComentarioUtil {//se deja de usar... los comentarios ahora son unicos a nivel de solucion.

	/*public DialogBox dialogoComentario = new DialogBox();
	private VerticalPanel vPDialComentario = new VerticalPanel();	
	CellTable<ComentarioData> tableComentarios;	
	private int selectedRow = -1;
	ListDataProvider<ComentarioData> dataProvider = new ListDataProvider<ComentarioData>();
	String horaServidor = "";
	TextArea txtEditarComentario = new TextArea();
	private VerticalPanel pVDialTabla = new VerticalPanel();
	private HorizontalPanel pHDialBotones = new HorizontalPanel();
	private Button btnDialComCerrar = new Button("Cerrar");

	SimplePager pager;
	
	List<ComentarioData> comentarios = new ArrayList<ComentarioData>();
	Persona sesion;
	Solucion solucion;
	GlassPopup glass;
	PopupCargando popUp;
	

	public Solucion getSolucion() {
		return solucion;
	}

	public void setSolucion(Solucion solucion) {
		this.solucion = solucion;
	}

	public Set<ComentarioData> getSetComentarios() {
		Set<ComentarioData> comentariosData = new HashSet<ComentarioData>();
		for(ComentarioData cd : dataProvider.getList())
		{
			comentariosData.add(cd);
		}
		return comentariosData;
	}

	public void setComentarios(List<ComentarioData> comentarios) {
		this.comentarios = comentarios;
	}

	public ComentarioUtil(Persona session, Solucion solucion, final GlassPopup glass, final PopupCargando popUp) {
		this.sesion = session;
		this.glass = glass;
		this.solucion = solucion;
		this.popUp = popUp;
		obtenerComentariosAsync();	
		
		btnDialComCerrar.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event) 
			{
				txtEditarComentario.setText("");
				dialogoComentario.hide();
				glass.hide();
			}
		});
	}
	
	private void agregarWidgetsDialComentario() 
	{
		
		dialogoComentario = new DialogBox();	
		ListHandler<ComentarioData> sortHandler = new ListHandler<ComentarioData>(comentarios);

		tableComentarios = crearTabla();
		tableComentarios.addColumnSortHandler(sortHandler);
		
		crearTablaComentarios();

		pVDialTabla.add(tableComentarios);

		btnDialComCerrar.setWidth("100px");

		pHDialBotones.setSpacing(10);
		
		Label lblTituloComentario = new Label("Editar Comentarios");
		
		vPDialComentario.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		pHDialBotones.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		pHDialBotones.add(btnDialComCerrar);

		HorizontalPanel hpEditarComentario = new HorizontalPanel();
		VerticalPanel vpEditarComentario = new VerticalPanel();
		
		Label lblNuevoComentario = new Label("Nuevo Comentario");
		txtEditarComentario = new TextArea();
		Button btnNuevoComentario = new Button("Agregar");
		Button btnModificarComentario = new Button("Modificar");
		
		btnNuevoComentario.setWidth("100px");
		btnModificarComentario.setWidth("100px");
		
		txtEditarComentario.setWidth("500px");
		txtEditarComentario.setHeight("65");
		
		lblTituloComentario.setStyleName("Titulo");
		lblNuevoComentario.setStyleName("Negrita");
		
		hpEditarComentario.setSpacing(10);
		hpEditarComentario.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hpEditarComentario.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		vpEditarComentario.setSpacing(10);
		vpEditarComentario.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vpEditarComentario.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		hpEditarComentario.add(txtEditarComentario);
		
		vpEditarComentario.add(btnNuevoComentario);
		vpEditarComentario.add(btnModificarComentario);
		
		btnNuevoComentario.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event) {
				if (!txtEditarComentario.getText().equals("") && validarComentario())
				{
					obtenerHoraServidor();
				}
			}

			private void obtenerHoraServidor() 
			{
				ProyectoBilpa.greetingService.obtenerHoraServidor(new AsyncCallback<String>() {
					public void onFailure(Throwable caught) 
					{
						Window.alert("ERROR al Obtener la hora del servidor");
					}

					public void onSuccess(String result) 
					{
						horaServidor = result;
						agregarNuevoComentario();
					}
				});
			}
		});
		
		btnModificarComentario.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) 
			{
				if (validarComentario())
				{
					modificarComentario();
				}
			}
		});
		
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(tableComentarios);
		pager.setPageSize(5);
		
		hpEditarComentario.add(vpEditarComentario);
		
		vPDialComentario.add(lblTituloComentario);
		vPDialComentario.add(pVDialTabla);
		vPDialComentario.add(pager);
		vPDialComentario.add(hpEditarComentario);
		vPDialComentario.add(pHDialBotones);
		
		vPDialComentario.setSize("100%", "100%");
		vPDialComentario.setSpacing(10);
		dialogoComentario.setSize("100%", "100%");
		dialogoComentario.add(vPDialComentario);
		dialogoComentario.setPopupPosition(Window.getClientWidth()/6, Window.getClientHeight()/4);
		
	}
	

	private boolean validarComentario() 
	{
		String texto = txtEditarComentario.getText();
		if (texto.length() > 198)
		{
			Window.alert("No es posible ingresar comentarios con mas de 200 caracteres");
			txtEditarComentario.setFocus(true);
			return false;
		}
		return true;
	}

	private void modificarComentario() 
	{
		if (selectedRow != -1 && dataProvider.getList().get(selectedRow) != null)
		{
			ComentarioData comentarioData = dataProvider.getList().get(selectedRow);
			if (comentarioData.getUsuario().equals(sesion.getNombreDeUSuario()))
			{
				String textoComentario = txtEditarComentario.getText();
				comentarioData.setTexto(textoComentario);
				dataProvider.refresh();
				//tableComentarios.setRowData(dataProvider.getList());
				tableComentarios.redraw();
				txtEditarComentario.setText("");
				selectedRow=-1;
			}
		}
	}
	
	private void agregarNuevoComentario() 
	{
		String textoNuevoComentario = txtEditarComentario.getText();
		ComentarioData nuevoComentarioData = new ComentarioData(0, horaServidor, false, textoNuevoComentario, sesion.getNombreDeUSuario());
		//this.getSolucion().agregarComentario(nuevoComentarioData);
		dataProvider.getList().add(nuevoComentarioData);
		dataProvider.refresh();
		//tableComentarios.setRowData(dataProvider.getList());
		tableComentarios.redraw();
		txtEditarComentario.setText("");
		selectedRow=-1;
	}
	
	private CellTable<ComentarioData> crearTabla() {
		vPDialComentario = new VerticalPanel();
		tableComentarios = new CellTable<ComentarioData>();
		//tableComentarios.setRowData(new ArrayList<ComentarioData>());
		tableComentarios.redraw();
		tableComentarios.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		tableComentarios.setWidth("900px", true);
		tableComentarios.setRowCount(comentarios.size(), true);
		tableComentarios.setVisibleRange(0, comentarios.size());
		
		tableComentarios.addCellPreviewHandler(new CellPreviewEvent.Handler<ComentarioData>(){
			public void onCellPreview(CellPreviewEvent<ComentarioData> event) 
			{
				boolean isClick = "click".equals(event.getNativeEvent().getType());
				if(isClick)
				{
					txtEditarComentario.setText("");
					selectedRow = event.getIndex();
					int cell = event.getColumn();
					filaClicked(cell);
				}
			}

			private void filaClicked(int cell) 
			{
				ComentarioData comentarioData = dataProvider.getList().get(selectedRow);
				if (cell == 0)//checkbox imp
				{
					cambiarImprimible(comentarioData);
				}
				else
				{
					mostrarTexto(comentarioData);	
				}
			}
		});
		return tableComentarios;
	}
	
	private void cambiarImprimible(ComentarioData comentarioData) 
	{
		comentarioData.setImprimible(!comentarioData.isImprimible());
	}

	private void mostrarTexto(ComentarioData comentarioData) 
	{
		if(comentarioData.getUsuario().equals(sesion.getNombreDeUSuario()))
		{
			txtEditarComentario.setText(comentarioData.getTexto());
		}
		else 
		{
			txtEditarComentario.setText("");
		}
	}

	private void obtenerComentariosAsync() {
		comentarios.clear();
		popUp.show();
		ProyectoBilpa.greetingService.obtenerComentarios(solucion, new AsyncCallback<ArrayList<ComentarioData>>() {
			public void onFailure(Throwable caught) {
				Window.alert("ERROR al Obtener los comentarios");
				popUp.hide();
			}

			public void onSuccess(ArrayList<ComentarioData> result) {
				if (result != null)
				{
					comentarios = result;
					agregarWidgetsDialComentario();
				}
				
				popUp.hide();				
			}
		});
	}


	private void crearTablaComentarios() {
		Column<ComentarioData, Boolean> columnImprimible = new Column<ComentarioData, Boolean>(new CheckboxCell()) 
		{
			@Override
			public Boolean getValue(ComentarioData comentario) {
				return comentario.isImprimible();
			}
		};
		
		columnImprimible.setSortable(true);
		tableComentarios.addColumn(columnImprimible, "Imp");
		tableComentarios.setColumnWidth(columnImprimible, 8, Unit.PCT);
		
		Column<ComentarioData, String> columnFecha = new Column<ComentarioData, String>(new TextCell()) 
		{
			@Override
			public String getValue(ComentarioData comentario) {
				return comentario.getFecha().toString();
			}
		};

		columnFecha.setSortable(true);
		tableComentarios.addColumn(columnFecha, "Fecha");
		tableComentarios.setColumnWidth(columnFecha, 16, Unit.PCT);

		Column<ComentarioData, String> columnUsuario = new Column<ComentarioData, String>(new TextCell()) 
		{
			@Override
			public String getValue(ComentarioData comentario) {
				return comentario.getUsuario();
			}
		};

		columnUsuario.setSortable(true);
		tableComentarios.addColumn(columnUsuario, "Usuario");
		tableComentarios.setColumnWidth(columnUsuario, 13, Unit.PCT);
		
		Column<ComentarioData, String> columnTexto = new Column<ComentarioData, String>(new TextCell()) 
		{
			@Override
			public String getValue(ComentarioData comentario) {
				return comentario.getTexto();
			}
		};

		columnTexto.setSortable(true);
		tableComentarios.addColumn(columnTexto, "Comentario");
		tableComentarios.setColumnWidth(columnTexto, 56, Unit.PCT);
		
		dataProvider.addDataDisplay(tableComentarios);
		//List<ComentarioData> list = dataProvider.getList();
		
		//for (ComentarioData comentario : comentarios) {
		//	list.add(comentario);
		//}
		dataProvider.setList(comentarios);
		//
		ListHandler<ComentarioData> columnSortHandlerImp = new ListHandler<ComentarioData>(comentarios);
		ListHandler<ComentarioData> columnSortHandlerFecha = new ListHandler<ComentarioData>(comentarios);
		ListHandler<ComentarioData> columnSortHandlerUsuario = new ListHandler<ComentarioData>(comentarios);
		ListHandler<ComentarioData> columnSortHandlerTexto = new ListHandler<ComentarioData>(comentarios);
		
		columnSortHandlerImp.setComparator(columnImprimible,new Comparator<ComentarioData>() {
			public int compare(ComentarioData o1, ComentarioData o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? new Boolean(o1.isImprimible()).compareTo(new Boolean(o2.isImprimible())) : 1;
				}
				return -1;
			}
		});
		
		columnSortHandlerFecha.setComparator(columnFecha,new Comparator<ComentarioData>() {
			public int compare(ComentarioData o1, ComentarioData o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getFecha().toString().compareTo(o2.getFecha().toString()) : 1;
				}
				return -1;
			}
		});
		
		columnSortHandlerUsuario.setComparator(columnUsuario,new Comparator<ComentarioData>() {
			public int compare(ComentarioData o1, ComentarioData o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getUsuario().compareTo(o2.getUsuario()) : 1;
				}
				return -1;
			}
		});
		
		columnSortHandlerTexto.setComparator(columnTexto,new Comparator<ComentarioData>() {
			public int compare(ComentarioData o1, ComentarioData o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getTexto().compareTo(o2.getTexto()) : 1;
				}
				return -1;
			}
		});
		
		tableComentarios.addColumnSortHandler(columnSortHandlerImp);
		tableComentarios.addColumnSortHandler(columnSortHandlerFecha);
		tableComentarios.addColumnSortHandler(columnSortHandlerUsuario);
		tableComentarios.addColumnSortHandler(columnSortHandlerTexto);
	}
	
	public void setearComentarios() 
	{
		dataProvider.refresh();
		Set<ComentarioData> comentariosOrden = new HashSet<ComentarioData>();
		tarea.setComentarios(comentariosOrden);
		for(ComentarioData cd : dataProvider.getList())
		{
			comentariosOrden.add(new Comentario(cd));
		}
	}*/
}
