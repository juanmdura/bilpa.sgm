package app.client.dominio.comparable;

import java.util.Comparator;

import app.client.dominio.Activo;

public class OrdenarActivoPorTipo implements Comparator<Activo>{

	public int compare(Activo a, Activo a1) {
		return a.getTipo() - a1.getTipo();
	}

}
