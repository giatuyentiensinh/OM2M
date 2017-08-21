package org.eclipse.om2m.webapp.resourcesbrowser.ui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.om2m.webapp.resourcesbrowser.ui.Activator;
import org.eclipse.om2m.webapp.resourcesbrowser.ui.WelcomeServlet;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author tuyenng
 *
 */
public class Activator implements BundleActivator {

	/** logger */
	private static Log LOGGER = LogFactory.getLog(Activator.class);
	public static String globalContext = System.getProperty(
			"org.eclipse.om2m.globalContext", "");
	public static String uiContext = "/iotworkshop";
	public static String sep = "/";
	/** HTTP service tracker */
	private ServiceTracker<Object, Object> httpServiceTracker;

	@Override
	public void start(BundleContext context) throws Exception {
		if (uiContext.equals("/")) {
			sep = "";
		}

		httpServiceTracker = new ServiceTracker<Object, Object>(context,
				HttpService.class.getName(), null) {
			public void removedService(ServiceReference<Object> reference,
					Object service) {
				LOGGER.info("HttpService removed");
				try {
					LOGGER.info("Unregister " + uiContext + " http context");
					((HttpService) service).unregister(uiContext);
				} catch (IllegalArgumentException e) {
					LOGGER.error("Error unregistring webapp servlet", e);
				}
			}

			public Object addingService(ServiceReference<Object> reference) {

				HttpService httpService = (HttpService) context
						.getService(reference);
				try {
					LOGGER.info("Dang ky " + uiContext + " http context");
					httpService.registerServlet(uiContext,
							new WelcomeServlet(), null, null);
					httpService.registerResources(uiContext + sep + "hust",
							"webapps", null);
				} catch (Exception e) {
					LOGGER.error("Error registring webapp servlet", e);
				}
				return httpService;
			}
		};

		System.out.println("\033[1;32m                                                ");
		System.out.println("   ____ _         _____                         ");
		System.out.println("  / ___(_) ____  |_   _|   _ _   _  ___ ____    ");
		System.out.println(" | |  _| |/ _  |   | || | | | | | |/ _ \\  _ \\   ");
		System.out.println(" | |_| | | (_| |   | || |_| | |_| |  __/ | | |  ");
		System.out.println("  \\____|_|\\____|   |_| \\____|\\___ |\\___|_| |_|  ");
		System.out.println("                             |___/              ");
		System.out.println("  _____ _              ____  _       _          ");
		System.out.println(" |_   _(_) ___ _ __   / ___|(_)_ __ | |__       ");
		System.out.println("   | | | |/ _ \\ |_ \\  \\___ \\| | |_ \\| |_ \\      ");
		System.out.println("   | | | |  __/ | | |  ___) | | | | | | | |     ");
		System.out.println("   |_| |_|\\___|_| |_| |____/|_|_| |_|_| |_|     ");
		System.out.println("                                                ");
		System.out.println("\033[0m                                         ");

		httpServiceTracker.open();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}

}
