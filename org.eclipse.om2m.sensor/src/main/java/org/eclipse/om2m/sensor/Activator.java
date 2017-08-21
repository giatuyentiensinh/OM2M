package org.eclipse.om2m.sensor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.om2m.core.service.CseService;
import org.eclipse.om2m.interworking.service.InterworkingService;
import org.eclipse.om2m.sensor.controller.Controller;
import org.eclipse.om2m.sensor.controller.LifeCycleManager;
import org.eclipse.om2m.sensor.controller.ObseveSSE;
import org.eclipse.om2m.sensor.controller.ReceiverDataSensor;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {

	private static String SENSOR_PORT = System.getProperty(
			"org.eclipse.om2m.sensor.port", "5678");

	/** Logger */
	private static Log logger = LogFactory.getLog(Activator.class);
	/** SCL service tracker */
	private ServiceTracker<Object, Object> cseServiceTracker;

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		logger.info("Register SensorService.");
		bundleContext.registerService(InterworkingService.class.getName(),
				new REMoteRouter(), null);
		logger.info("SensorService is registered.");

		// cse service
		cseServiceTracker = new ServiceTracker<Object, Object>(bundleContext,
				CseService.class.getName(), null) {
			public void removedService(ServiceReference<Object> reference,
					Object service) {
				logger.info("CseService removed");
			}

			public Object addingService(ServiceReference<Object> reference) {
				logger.info("CseService discovered");
				CseService cseService = (CseService) this.context
						.getService(reference);
				Controller.setCse(cseService);
				new Thread() {
					public void run() {
						try {
							ObseveSSE.start();
							LifeCycleManager.start();
							ReceiverDataSensor.start(Integer
									.parseInt(SENSOR_PORT));
						} catch (Exception e) {
							logger.error("Sensor monitor error", e);
						}
					}
				}.start();
				return cseService;
			}
		};
		cseServiceTracker.open();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		try {
			LifeCycleManager.stop();
			ReceiverDataSensor.stop();
			ObseveSSE.stop();
			cseServiceTracker.close();
			logger.info("STOPPED Sensor plugin");
		} catch (Exception e) {
			logger.error("Stop Sensor error", e);
		}
	}

}
