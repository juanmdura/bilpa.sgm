package app.client.dominio;


public class FallaTecnica extends FallaTipo{

	public FallaTecnica() {
		super(1);
	}

	public FallaTecnica(int idFalla) {
		super(1);
		setId(idFalla);
	}

	public FallaTecnica copiar() {
		FallaTecnica copia = new FallaTecnica();
		copiarPropiedades(copia);
		return copia;
	}

	public FallaTecnica copiarTodo() {
		FallaTecnica copia = new FallaTecnica();
		copiarPropiedades(copia);
		copiarColecciones(copia);
		return copia;
	}
	private void copiarPropiedades(FallaTecnica copia) {
		copia.setId(getId());
		copia.setTipo(getTipo());
		copia.setDescripcion(getDescripcion());
		copia.setActivo(getActivo());
		copia.setSubTipo(getSubTipo());
	}

	public void copiarColecciones(FallaTecnica copia) {
		
	}

	public boolean equals(FallaTecnica ft) {
		return getDescripcion().equals(ft.getDescripcion());
	}
}
