package app.client.dominio;


public class TipoFallaTecnica extends SubTipoFalla{
	
	public TipoFallaTecnica() {
		super(1);
	}

	public TipoFallaTecnica copiar() {
		TipoFallaTecnica copia = new TipoFallaTecnica();
		copiarPropiedades(copia);
		return copia;
	}

	public TipoFallaTecnica copiarTodo() {
		TipoFallaTecnica copia = new TipoFallaTecnica();
		copiarPropiedades(copia);
		copiarColecciones(copia);
		return copia;
	}
	private void copiarPropiedades(TipoFallaTecnica copia) {
		copia.setId(getId());
		copia.setTipo(getTipo());
		copia.setDescripcion(getDescripcion());
	}

	public void copiarColecciones(TipoFallaTecnica copia) {
		
	}

	public boolean equals(TipoFallaTecnica ft) {
		return getDescripcion().equals(ft.getDescripcion());
	}
}
