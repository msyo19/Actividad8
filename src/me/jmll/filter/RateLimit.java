package me.jmll.filter;

import java.io.IOException;
import java.util.Date;
import java.util.Stack;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Servlet Filter implementation class RateLimit
 * Based in RequestRateThrottleFilter of
 * Open Web Application Security Project (OWASP)
 * by Jeff Williams 2007
 */
public class RateLimit implements Filter {
	// Escribe aquí tu código {

    // }
    /**
     * Default constructor. 
     */
    public RateLimit() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
        // TODO Auto-generated method stub
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Hacer el casting de request y response a HttpServletRequest HttpServletResponse
    	// Escribe aquí tu código {

        // }
        // Obtener la sesión
        HttpSession session = req.getSession(true);
        synchronized(session) {
            // LIFO
            Stack accessData = (Stack) session.getAttribute("accessData");
            if (accessData == null){
                accessData = new Stack();
                // Agregando EPOC al stack
                accessData.push(new Date(0));
                session.setAttribute("accessData", accessData);
            }
            // Agregando nueva entrada
            accessData.push(new Date());
            // Elimina el último registro
            if (accessData.size() >= req_limit)
                accessData.removeElementAt(0); 

            // Obtiene el más nuevo
            Date newest = (Date) accessData.get(accessData.size()-1);  
            // Obtiene el anterior
            Date oldest = (Date) accessData.get(0); 
            // delta
            long elapsed = newest.getTime() - oldest.getTime();
            // Header
        	// Escribe aquí tu código {
            res.addIntHeader(XRATE_LIMIT, req_limit);
            // }
            if (elapsed < time_limit) {
            	// Escribe aquí tu código {
                
                // }
                // Mensaje
                res.getWriter().write(String.format("Has alcanzado el máximo número"
                        + " de solicitudes en el periodo definido. Espera %s segundos.", time_limit/1000));
                return;
            } 
        }
        chain.doFilter(request, response);
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
        // Carga configuración de parámetros de inicio
    	// Escribe aquí tu código {

        // }
        logger.info("Iniciando {} con REQ_LIMIT={} TIME_LIMIT={}", RateLimit.class.getName(), req_limit, time_limit);
    }

}
