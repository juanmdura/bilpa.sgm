package app.client.dominio.data;

public class DetalleReporteData implements com.google.gwt.user.client.rpc.IsSerializable {
	
	public static final String VACIA = "rowVacia";
	public static final String SIMPLE = "rowSimple";
	public static final String VALOR = "rowValor";
	public static final String COMPLETA = "rowCompleta";
	
	public static final String BOLD = "bold";
	public static final String BLUE = "blue";
	
	private String label;
	private String valor;
	private String formato;//VACIA, SIMPLE, VALOR, COMPLETA
	private String estilo;// BOLD
	private String color;// BOLD
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getEstilo() {
		return estilo;
	}
	public void setEstilo(String estilo) {
		this.estilo = estilo;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getFormato() {
		return formato;
	}
	public void setFormato(String formato) {
		this.formato = formato;
	}
	
	public DetalleReporteData(String label, String valor, String formato, String estilo, String color) {
		super();
		this.label = label;
		this.valor = valor;
		this.formato = formato;
		this.estilo = estilo;
		this.color = color;
	}
	
	public DetalleReporteData(String label, String valor, String formato, String estilo) {
		super();
		this.label = label;
		this.valor = valor;
		this.formato = formato;
		this.estilo = estilo;
	}
	
	public DetalleReporteData(String label, String valor, String formato) {
		super();
		this.label = label;
		this.valor = valor;
		this.formato = formato;
	}
	
	public DetalleReporteData() {
		super();
	}

}
