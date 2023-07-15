package app.client.utilidades;

import java.util.ArrayList;

import app.client.dominio.FallaTecnica;
import app.client.dominio.Tarea;

public class UtilDominio {

	public static Tarea buscarTarea(ArrayList<Tarea> lista, int id){
		for(Tarea t : lista){
			if(t.getId()==id){
				return t;
			}
		}
		return null;
	}
	
	public static FallaTecnica buscarFallaTecnica(ArrayList<FallaTecnica> lista, int id){
		for(FallaTecnica ft : lista){
			if(ft.getId()==id){
				return ft;
			}
		}
		return null;
	}
}
