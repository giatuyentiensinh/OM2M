package org.eclipse.om2m.webapp.resourcesbrowser.ui;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author tuyenng
 *
 */
public class WelcomeServlet extends HttpServlet {
	/** Serial Version UID */
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws ServletException,
			IOException {
		// String cseBaseContext = (System.getProperty(
		// "org.eclipse.om2m.cseBaseContext", "/om2m").equals("/") ? "/~"
		// : System.getProperty("org.eclipse.om2m.cseBaseContext", "/om2m")
		// + "/");
		httpServletResponse.sendRedirect(Activator.globalContext
				+ Activator.uiContext + Activator.sep + "hust/index.html");
		// httpServletResponse.sendRedirect(Activator.globalContext
		// + Activator.uiContext + Activator.sep
		// + "welcome/index.html?context="
		// + System.getProperty("org.eclipse.om2m.globalContext", "")
		// + cseBaseContext + "&" + "cseId="
		// + System.getProperty("org.eclipse.om2m.cseBaseId", "in-cse"));
	}
}
