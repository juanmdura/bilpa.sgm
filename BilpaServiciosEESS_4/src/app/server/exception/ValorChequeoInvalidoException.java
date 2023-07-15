package app.server.exception;

@SuppressWarnings("serial")
public class ValorChequeoInvalidoException extends Exception{

	public ValorChequeoInvalidoException(String valor) {
		super(valor);
	}

}
