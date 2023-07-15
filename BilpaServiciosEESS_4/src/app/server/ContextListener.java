package app.server;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import app.server.persistencia.HibernateConfiguration;

public class ContextListener implements ServletContextListener {

	private static final String HIBERNATE_CFG_PATH_DEFAULT = "/hibernate.cfg.xml";

	
	public void contextDestroyed(ServletContextEvent sce) {
		HibernateConfiguration.close();
		
	}

	
	public void contextInitialized(ServletContextEvent context) {
		HibernateConfiguration.configure(HIBERNATE_CFG_PATH_DEFAULT);
	}
	

}
