package com.gsafer.smartoperation.controller.util;

import com.gsafer.clouddev.security.facade.InitialFacade;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Configuration of web application with Servlet 3.0 APIs.
 */
@WebListener
public class WebConfigurer implements ServletContextListener
{

    @Inject
    private InitialFacade initialFacade;

    //TODO: INJECT
    //@ManagedProperty("Expression myBundle is undefined on line 27, column 27 in unknown.")
    private String appName = "您的应用";

    public void contextInitialized (ServletContextEvent sce)
    {
        //ServletContext servletContext = sce.getServletContext();
        //servletContext.setInitParameter("primefaces.THEME", "rio");

        List<String> entities = new ArrayList<>(); //添加entity
        entities.add("HospitalOperation");
        entities.add("ProcessorConfig");
        entities.add("ProcessorMap");
        entities.add("ProcessorRoute");
        initialFacade.init(entities, appName);
    }

    @Override
    public void contextDestroyed (ServletContextEvent sce)
    {
    }
}
