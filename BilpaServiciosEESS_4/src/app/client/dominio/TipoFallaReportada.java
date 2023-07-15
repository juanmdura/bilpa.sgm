package app.client.dominio;

public class TipoFallaReportada extends SubTipoFalla{

	public TipoFallaReportada() {
		super(2);
	}

	public TipoFallaReportada copiar() {
		TipoFallaReportada copia = new TipoFallaReportada();
		copiarPropiedades(copia);
		return copia;
	}

	public TipoFallaReportada copiarTodo() {
		TipoFallaReportada copia = new TipoFallaReportada();
		copiarPropiedades(copia);
		copiarColecciones(copia);
		return copia;
	}
	private void copiarPropiedades(TipoFallaReportada copia) {
		copia.setId(getId());
		copia.setTipo(getTipo());
		copia.setDescripcion(getDescripcion());
	}

	public void copiarColecciones(TipoFallaReportada copia) {
		
	}
	
	public boolean equals(TipoFallaReportada fr) {
		return getDescripcion().equals(fr.getDescripcion());
	}

}
