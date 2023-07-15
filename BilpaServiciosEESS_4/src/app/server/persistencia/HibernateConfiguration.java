package app.server.persistencia;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import app.client.dominio.Activo;
import app.client.dominio.ActivoGenerico;
import app.client.dominio.ChequeoGenerico;
import app.client.dominio.CorregidoActivoGenerico;
import app.client.dominio.ReparacionActivoGenerico;
import app.client.dominio.TipoActivoGenerico;
import app.client.dominio.Administrador;
import app.client.dominio.Administrativo;
import app.client.dominio.BombaSumergible;
import app.client.dominio.Calibre;
import app.client.dominio.Canio;
import app.client.dominio.CantidadOrdenes;
import app.client.dominio.Caudal;
import app.client.dominio.Chequeo;
import app.client.dominio.ChequeoBomba;
import app.client.dominio.ChequeoPico;
import app.client.dominio.ChequeoProducto;
import app.client.dominio.ChequeoSurtidor;
import app.client.dominio.ChequeoTanque;
import app.client.dominio.Comentario;
import app.client.dominio.ComentarioChequeo;
import app.client.dominio.ContactoEmpresa;
import app.client.dominio.Contador;
import app.client.dominio.Corregido;
import app.client.dominio.CorregidoBomba;
import app.client.dominio.CorregidoSurtidor;
import app.client.dominio.CorregidoTanque;
import app.client.dominio.DestinoDelCargo;
import app.client.dominio.EmailEmpresa;
import app.client.dominio.Encargado;
import app.client.dominio.Estacion;
import app.client.dominio.FallaReportada;
import app.client.dominio.FallaTecnica;
import app.client.dominio.FallaTipo;
import app.client.dominio.Firma;
import app.client.dominio.Foto;
import app.client.dominio.HistoricoOrden;
import app.client.dominio.ItemChequeado;
import app.client.dominio.ItemChequeo;
import app.client.dominio.Marca;
import app.client.dominio.MarcaActivoGenerico;
import app.client.dominio.Modelo;
import app.client.dominio.ModeloActivoGenerico;
import app.client.dominio.ModeloSurtidor;
import app.client.dominio.Orden;
import app.client.dominio.Pendiente;
import app.client.dominio.Persona;
import app.client.dominio.Pico;
import app.client.dominio.Precinto;
import app.client.dominio.Preventivo;
import app.client.dominio.Producto;
import app.client.dominio.QR;
import app.client.dominio.Reparacion;
import app.client.dominio.ReparacionBomba;
import app.client.dominio.ReparacionCano;
import app.client.dominio.ReparacionSurtidor;
import app.client.dominio.ReparacionTanque;
import app.client.dominio.Repuesto;
import app.client.dominio.RepuestoLinea;
import app.client.dominio.RepuestoLineaCorregido;
import app.client.dominio.Sello;
import app.client.dominio.Solucion;
import app.client.dominio.SubTipoFalla;
import app.client.dominio.Surtidor;
import app.client.dominio.Tanque;
import app.client.dominio.Tarea;
import app.client.dominio.Tecnico;
import app.client.dominio.TipoDescarga;
import app.client.dominio.TipoFallaReportada;
import app.client.dominio.TipoFallaTecnica;
import app.client.dominio.TipoRepuesto;
import app.client.dominio.TipoTarea;
import app.client.dominio.TipoTrabajo;
import app.client.dominio.Visita;

/**
 * Esta clase contiene la configuración de hibernate. Esta configuración entre
 * otras cosas tiene la fábrica de sesiones que se usa internamente en el
 * programa para obtener referencias a las sesiones de Hibernate.
 * 
 * @author Alfonso Odriozola
 */
public class HibernateConfiguration {

	private static SessionFactory	sessionFactory;



	public static SessionFactory getSessionFactory() {
		assertConfigured();
		return sessionFactory;
	}

	public static void configure() {
		assertNotConfigured();
		Configuration conf = new Configuration();
		conf.configure();
		buildSessionFactory(conf);
	}

	public static void configure(File file) {
		assertNotConfigured();
		Configuration configuration = new Configuration();
		if (isPropertiesFile(file.getName())) {
			configuration.setProperties(loadProperties(file));
		} else {
			configuration.configure(file);
		}
		buildSessionFactory(configuration);
	}

	public static boolean isConfigured() {
		return sessionFactory != null;
	}

	public static void configure(String resource) {
		assertNotConfigured();
		Configuration configuration = new Configuration();
		if (isPropertiesFile(resource)) {
			configuration.setProperties(loadProperties(resource));
		} else {
			configuration.configure(resource);
		}
		buildSessionFactory(configuration);
		
	}

	public static void configure(URL url) {
		assertNotConfigured();
		buildSessionFactory(new Configuration().configure(url));
	}

	public static void close() {
		sessionFactory.close();
		sessionFactory = null;
	}

	private static void buildSessionFactory(Configuration conf) {
		conf.addClass(Repuesto.class);
		conf.addClass(FallaTecnica.class);
		conf.addClass(FallaReportada.class);
		conf.addClass(FallaTipo.class);
		conf.addClass(RepuestoLinea.class);
		conf.addClass(Tarea.class);
		conf.addClass(Activo.class);
		conf.addClass(BombaSumergible.class);
		conf.addClass(Canio.class);
		conf.addClass(Solucion.class);
		conf.addClass(Reparacion.class);
		conf.addClass(Surtidor.class);
		conf.addClass(Tanque.class);
		conf.addClass(Tecnico.class);
		conf.addClass(Persona.class);
		conf.addClass(Pico.class);
		conf.addClass(Administrador.class);
		conf.addClass(ContactoEmpresa.class);
		conf.addClass(Administrativo.class);
		conf.addClass(Encargado.class);
		conf.addClass(Estacion.class);
		conf.addClass(Orden.class);
		conf.addClass(Modelo.class);
		conf.addClass(ModeloSurtidor.class);
		conf.addClass(Marca.class);
		conf.addClass(Sello.class);
		conf.addClass(ReparacionSurtidor.class);
		conf.addClass(ReparacionBomba.class);
		conf.addClass(ReparacionCano.class);
		conf.addClass(ReparacionTanque.class);
		conf.addClass(CantidadOrdenes.class);
		conf.addClass(Contador.class);
		conf.addClass(HistoricoOrden.class);
		conf.addClass(TipoTrabajo.class);
		conf.addClass(TipoTarea.class);
		conf.addClass(TipoRepuesto.class);
		conf.addClass(TipoFallaReportada.class);
		conf.addClass(TipoFallaTecnica.class);
		conf.addClass(SubTipoFalla.class);
		conf.addClass(Comentario.class);
		conf.addClass(DestinoDelCargo.class);	
		conf.addClass(Caudal.class);
		conf.addClass(Chequeo.class);
		conf.addClass(ChequeoBomba.class);
		conf.addClass(ChequeoPico.class);
		conf.addClass(ChequeoSurtidor.class);
		conf.addClass(ChequeoTanque.class);
		conf.addClass(Corregido.class);
		conf.addClass(CorregidoBomba.class);
		conf.addClass(CorregidoSurtidor.class);
		conf.addClass(CorregidoTanque.class);
		conf.addClass(ChequeoProducto.class);
		conf.addClass(Firma.class);
		conf.addClass(Foto.class);
		conf.addClass(Pendiente.class);
		conf.addClass(Precinto.class);
		conf.addClass(Preventivo.class);
		conf.addClass(Producto.class);
		conf.addClass(QR.class);
		conf.addClass(RepuestoLineaCorregido.class);
		conf.addClass(Visita.class);	
		conf.addClass(TipoDescarga.class);
		conf.addClass(EmailEmpresa.class);
		conf.addClass(ItemChequeado.class);
		conf.addClass(ItemChequeo.class);
		conf.addClass(Calibre.class);
		conf.addClass(ActivoGenerico.class);
		conf.addClass(TipoActivoGenerico.class);
		conf.addClass(MarcaActivoGenerico.class);
		conf.addClass(ModeloActivoGenerico.class);
		conf.addClass(ChequeoGenerico.class);
		conf.addClass(ComentarioChequeo.class);
		conf.addClass(CorregidoActivoGenerico.class);
		conf.addClass(ReparacionActivoGenerico.class);
		sessionFactory = conf.buildSessionFactory();
	}

	private static void assertConfigured() {
		if (sessionFactory == null) {
			throw new HibernateException("SessionFactory is not configured");
		}
	}

	private static void assertNotConfigured() {
		if (sessionFactory != null) {
			throw new HibernateException("SessionFactory could not be configured twice");
		}
	}

	private static boolean isPropertiesFile(String filename) {
		return filename.endsWith(".properties");
	}

	private static Properties loadProperties(File file) throws HibernateException {
		InputStream is = null;
		try {
			// Abrir el archivo
			is = new FileInputStream(file);

			// Cargar y retornar las propiedades
			Properties properties = new Properties();
			properties.load(is);
			return properties;

		} catch (IOException e) {
			throw new HibernateException("Could not load properties from file '" + file + "'", e);
		} finally {
			// Cerrar el archivo
			if (is != null) {
				try {
					is.close();
				} catch (IOException ignored) {
				}
			}
		}
	}

	private static Properties loadProperties(String resource) throws HibernateException {
		InputStream is = null;
		try {
			// Abrir el archivo
			is = HibernateConfiguration.class.getResourceAsStream(resource);
			if (is == null) {
				throw new HibernateException("Could not load properties from resource'" + resource + "': resource not found");
			}

			// Cargar y retornar las propiedades
			Properties properties = new Properties();
			properties.load(is);
			return properties;

		} catch (IOException e) {
			throw new HibernateException("Could not load properties from file '" + resource + "'", e);
		} finally {
			// Cerrar el archivo
			if (is != null) {
				try {
					is.close();
				} catch (IOException ignored) {
				}
			}
		}
	}


}
