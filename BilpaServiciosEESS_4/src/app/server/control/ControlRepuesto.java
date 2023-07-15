package app.server.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.client.dominio.Activo;
import app.client.dominio.Orden;
import app.client.dominio.Repuesto;
import app.client.dominio.RepuestoLinea;
import app.client.dominio.TipoRepuesto;
import app.client.dominio.data.RepuestoDatoGrafica;
import app.server.persistencia.DaoTransaction;
import app.server.persistencia.RepuestoDao;
import app.server.persistencia.RepuestoLineaDao;

public class ControlRepuesto {

    private static ControlRepuesto instancia = null;

    public static ControlRepuesto getInstancia() {
        if(instancia == null){
            instancia = new ControlRepuesto();
        }
        return instancia;
    }

    public static void setInstancia(ControlRepuesto instancia) {
        ControlRepuesto.instancia = instancia;
    }

    private ControlRepuesto (){

    }

    public int validarNuevoRepuesto(String descripcion, String nroSerie){
        ArrayList<Repuesto> todosLosRepuestos = obtenerTodosLosRepuestos();
        Repuesto auxiliar;
        for (int i=0; i<todosLosRepuestos.size();i++){
            auxiliar = (Repuesto) todosLosRepuestos.get(i);
            if (descripcion.equals(auxiliar.getDescripcion())){
                return 1;                
            }
            if (nroSerie.equals(auxiliar.getNroSerie())){
                return 2;                
            }
        }
        return 0;
    }

    public ArrayList<Repuesto> obtenerTodosLosRepuestos(){

        ArrayList<Repuesto> todosLosRepuestos = new ArrayList<Repuesto>();

        DaoTransaction tx = new DaoTransaction();

        try {
            tx.begin();
            List<Repuesto> lista = new RepuestoDao().obtenerRepuestosActivos();
            for (Repuesto r : lista) {
                todosLosRepuestos.add(r.copiar());
            }

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }finally{
        	tx.close();
        }
        Collections.sort(todosLosRepuestos);
        return todosLosRepuestos;
    }

    public boolean agregarRepuesto(Repuesto repuesto) {
        if(repuesto != null){
            DaoTransaction tx = new DaoTransaction();

            try {
                tx.begin();

                RepuestoDao dao = new RepuestoDao();

                dao.save(repuesto);

                tx.commit();

                return true;

            } catch (Exception e) {
                tx.rollback();
                e.printStackTrace();
            }finally{
            	tx.close();
            }
        }
        return false;
    }

    public Repuesto buscarRepuesto(int id){
        if(id > 0){
            DaoTransaction tx = new DaoTransaction();
            try {
                tx.begin();
              
                RepuestoDao dao = new RepuestoDao();
                Repuesto retorno = new Repuesto();

                retorno = dao.get(id);
                return retorno.copiar();
            } catch (Exception e) {
                tx.rollback();
                e.printStackTrace();
            } finally{
            	tx.close();
            }
        }
        return null;
    }

    public boolean modificarRepuesto(Repuesto repuesto){
        if(repuesto != null){
            DaoTransaction tx = new DaoTransaction();
            try {
                tx.begin();
                RepuestoDao dao = new RepuestoDao();
                dao.merge(repuesto);
                tx.commit();
                return true;
            } catch (Exception e) {
                tx.rollback();
                e.printStackTrace();
            }finally{
            	tx.close();
            }
        }
        return false;
    }

    public boolean eliminarRepuesto(Repuesto repuesto){
        DaoTransaction tx = new DaoTransaction();
        try {
            tx.begin();
            RepuestoDao dao = new RepuestoDao();
            repuesto.setInactiva(true);
            dao.merge(repuesto);
            //dao.delete(repuesto);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
            return false;
        }finally{
        	tx.close();
        }
    }

    public ArrayList<RepuestoDatoGrafica> los10MasUsados(){
        ArrayList<RepuestoDatoGrafica> listaRepuestosDatoGrafica = cargarRepuestosDatosGrafica();
        ArrayList<RepuestoDatoGrafica> retorno10RepuestosDatoGrafica = new ArrayList<RepuestoDatoGrafica>();
        Collections.sort(listaRepuestosDatoGrafica);//empezo la fruta
        
        if(listaRepuestosDatoGrafica.size() < 10){
            for (int i = 0; i < listaRepuestosDatoGrafica.size(); i++) {
                RepuestoDatoGrafica rd = listaRepuestosDatoGrafica.get(i);
                if(rd.getPorcentaje() == 0){
                    
                }else{
                    
                    retorno10RepuestosDatoGrafica.add(rd);
                }
            }
        }else{
            for (int i = 0; i < 10; i++) {
                RepuestoDatoGrafica rd = listaRepuestosDatoGrafica.get(i);
                retorno10RepuestosDatoGrafica.add(rd);
            }
        }
        
        
        return retorno10RepuestosDatoGrafica;
    }
    
    
    
    public ArrayList<RepuestoDatoGrafica> cargarRepuestosDatosGrafica(){
        ArrayList<RepuestoDatoGrafica> listaRepuestosDatoGrafica= new ArrayList<RepuestoDatoGrafica>();
        ArrayList<RepuestoLinea> listaRepuestosLinea = obtenerTodosLosRepuestosLinea();
        ArrayList<Repuesto> listaRepuestos = obtenerTodosLosRepuestos();
        
        int totalCantidades = calcularTotalCantidades(listaRepuestosLinea);
        
        for(Repuesto rep : listaRepuestos){        
            RepuestoDatoGrafica repDatoGraf = new RepuestoDatoGrafica();
            repDatoGraf.setRepuesto(rep);
            repDatoGraf.setCantidad(totalCantidadDeUnRepuesto(rep, listaRepuestosLinea));
            repDatoGraf.setPorcentaje(calcularPorcentajeDeCantidadDeUnRepuesto(repDatoGraf, totalCantidades));
            listaRepuestosDatoGrafica.add(repDatoGraf);
        }
        return listaRepuestosDatoGrafica;
    }

    private int calcularPorcentajeDeCantidadDeUnRepuesto(RepuestoDatoGrafica repDatoGraf, int totalCantidades) {
        int cantidadDeEsteRepuesto = repDatoGraf.getCantidad();
        if(cantidadDeEsteRepuesto>0){
        	return (cantidadDeEsteRepuesto * 100)/totalCantidades;
        	
        }
        return 0;
    }

    public int totalCantidadDeUnRepuesto(Repuesto rep, ArrayList<RepuestoLinea> repuestosLinea){
        int suma=0;
    
        for(RepuestoLinea rL : repuestosLinea){
            if(rep.getId()==rL.getRepuesto().getId()){
                suma+=rL.getCantidad();
            }
            
        }
        return suma;
    }
    
    public int calcularTotalCantidades(ArrayList<RepuestoLinea> repuestosLinea){
        int totalCantidad=0;
        for(RepuestoLinea rL : repuestosLinea){
            totalCantidad+=rL.getCantidad();            
        }
        return totalCantidad;
    }
    
    public ArrayList<RepuestoLinea> obtenerTodosLosRepuestosLinea(){

        ArrayList<RepuestoLinea> repuestosLinea = new ArrayList<RepuestoLinea>();

        DaoTransaction tx = new DaoTransaction();

        try {
            tx.begin();

            RepuestoLineaDao dao = new RepuestoLineaDao();
            List<RepuestoLinea> lista = dao.list();

            for (RepuestoLinea rL : lista) {
                repuestosLinea.add(rL);
            }
            return repuestosLinea;

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally{
            tx.close();
        }

        return repuestosLinea;
    }

	public ArrayList<Repuesto>obtenerRepuestos(TipoRepuesto tipo){
		ArrayList<Repuesto> repuestosPorTipo = new ArrayList<Repuesto>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			RepuestoDao dao = new RepuestoDao();
			List<Repuesto> lista = dao.obtenerRepuestos(tipo);
			for (Repuesto r : lista) {
				repuestosPorTipo.add(r.copiar());
			}
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally{
        	tx.close();
        }
		//Collections.sort(repuestosPorTipo);
		return repuestosPorTipo;
	}

	public ArrayList<RepuestoLinea> obtenerTodosLosRepuestosLinea(Orden o, Activo a) {
		ArrayList<RepuestoLinea> repuestosLinea = new ArrayList<RepuestoLinea>();

        DaoTransaction tx = new DaoTransaction();

        try {
            tx.begin();

            RepuestoLineaDao dao = new RepuestoLineaDao();
            List<RepuestoLinea> lista = dao.obtenerRepuestosLinea(o, a);

            for (RepuestoLinea rL : lista) 
            {
            	//if (rL.getActivo().getId() == a.getId() && rL.getOrden().getNumero() == o.getNumero())
            	{
            		repuestosLinea.add(rL.copiar());
            	}
            }
            return repuestosLinea;

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally{
            tx.close();
        }

        return repuestosLinea;
	}
}
