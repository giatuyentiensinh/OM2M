package org.eclipse.om2m.socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.om2m.core.service.CseService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;

public class Activator implements BundleActivator {

	private static String address = System.getProperty(
			"org.eclipse.om2m.cseBaseAddress", "127.0.0.1");
	private static String socketio_port = System.getProperty(
			"org.eclipse.om2m.sensor.socketio.port", "9092");

	public static final String SSE_TOPIC_MSG = "msg";
	public static final String SSE_TOPIC_OBS = "obj";

	/** Logger */
	private static Log logger = LogFactory.getLog(Activator.class);
	/** SCL service tracker */
	private ServiceTracker<Object, Object> cseServiceTracker;
	private SocketIOServer server;
	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		logger.info("Register Socket.");

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
				new Thread() {
					public void run() {
						logger.info("Start soket");
						Configuration config = new Configuration();
						config.setHostname(address);
						config.setPort(Integer.parseInt(socketio_port));
						server = new SocketIOServer(config);
						server.addEventListener(SSE_TOPIC_MSG, Object.class,
								new DataListener<Object>() {
									@Override
									public void onData(SocketIOClient client,
											Object data, AckRequest ackRequest) {
										System.out.println(data);
										server.getBroadcastOperations()
												.sendEvent(SSE_TOPIC_MSG, data);
									}
								});
						server.addEventListener(SSE_TOPIC_OBS, Object.class,
								new DataListener<Object>() {
									@Override
									public void onData(SocketIOClient client,
											Object data, AckRequest ackRequest) {
										System.out.println(data);
										server.getBroadcastOperations()
												.sendEvent(SSE_TOPIC_OBS, data);
									}
								});
						server.start();
					}
				}.start();
				return cseService;
			}
		};
		cseServiceTracker.open();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		server.stop();
		logger.info("stop socket");
	}

}
