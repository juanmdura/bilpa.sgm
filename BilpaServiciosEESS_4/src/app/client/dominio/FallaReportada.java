package app.client.dominio;

public class FallaReportada extends FallaTipo{

	public FallaReportada() {
		super(2);
	}

	public FallaReportada copiar() {
		FallaReportada copia = new FallaReportada();
		copiarPropiedades(copia);
		return copia;
	}

	public FallaReportada copiarTodo() {
		FallaReportada copia = new FallaReportada();
		copiarPropiedades(copia);
		copiarColecciones(copia);
		return copia;
	}
	private void copiarPropiedades(FallaReportada copia) {
		copia.setId(getId());
		copia.setTipo(getTipo());
		copia.setDescripcion(getDescripcion());
		copia.setActivo(getActivo());
		copia.setSubTipo(getSubTipo());
	}

	public void copiarColecciones(FallaReportada copia) {
		
	}
	
	public boolean equals(FallaReportada fr) {
		return getDescripcion().equals(fr.getDescripcion());
	}

}
