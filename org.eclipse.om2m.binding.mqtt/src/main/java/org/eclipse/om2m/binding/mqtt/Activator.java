/*******************************************************************************
 *  Copyright (c) 2013-2016 LAAS-CNRS (www.laas.fr)
 *  7 Colonel Roche 31077 Toulouse - France
 *  
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Initial Contributors:
 *      Thierry Monteil : Project manager, technical co-manager
 *      Mahdi Ben Alaya : Technical co-manager
 *      Samir Medjiah : Technical co-manager
 *      Khalil Drira : Strategy expert
 *      Guillaume Garzone : Developer
 *      François Aïssaoui : Developer
 *
 *   New contributors :
 *******************************************************************************/
package org.eclipse.om2m.binding.mqtt;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.om2m.binding.mqtt.util.DataMapperRegistry;
import org.eclipse.om2m.binding.service.RestClientService;
import org.eclipse.om2m.core.service.CseService;
import org.eclipse.om2m.datamapping.service.DataMapperService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	private static final Log LOGGER = LogFactory.getLog(Activator.class);

	/** {@link DataMapperServiceTracker} reference */
	private static ServiceTracker<DataMapperService, DataMapperService> dataMapperServiceTracker;
	/** {@link CseService} reference */
	private static ServiceTracker<CseService, CseService> cseServiceTracker;

	/** MQTT Request Handler that connects to the MQTT Broker */
	private static MqttRequestHandler mqttRequestHandler;

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		// Listening on Cse Service
		cseServiceTracker = new ServiceTracker<CseService, CseService>(
				bundleContext, CseService.class,
				new CseServiceTrackerCustomizer());
		cseServiceTracker.open();

		// Listening on DataMapper Service
		dataMapperServiceTracker = new ServiceTracker<DataMapperService, DataMapperService>(
				bundleContext, DataMapperService.class,
				new DataMapperServiceTracker());
		dataMapperServiceTracker.open();
		
		// Registering RestClientService of MQTT
		getContext().registerService(RestClientService.class, new MqttRestClient(), null);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		if (cseServiceTracker != null) {
			cseServiceTracker.close();
			cseServiceTracker = null;
		}
		if (dataMapperServiceTracker != null) {
			dataMapperServiceTracker.close();
			dataMapperServiceTracker = null;
		}
		if (mqttRequestHandler != null){
			mqttRequestHandler.close();
			mqttRequestHandler = null;
		}
	}

	private static class CseServiceTrackerCustomizer implements
			ServiceTrackerCustomizer<CseService, CseService> {

		@Override
		public CseService addingService(ServiceReference<CseService> reference) {
			if (reference == null) {
				return null;
			}
			Object service = Activator.getContext().getService(reference);
			if (service != null && service instanceof CseService) {
				LOGGER.debug("New CseService discovered");
				CseService cse = (CseService) service;
				MqttRequestHandler.setCseService(cse);
				if (mqttRequestHandler == null) {
					new Thread() {
						public void run() {
							LOGGER.info("Creating MQTT Request Handler");
							mqttRequestHandler = new MqttRequestHandler();
						};
					}.start();
				}
				return cse;
			}
			return null;
		}

		@Override
		public void modifiedService(ServiceReference<CseService> reference,
				CseService service) {
			if (service != null) {
				LOGGER.info("CseService modified");
				MqttRequestHandler.setCseService(service);
			}
		}

		@Override
		public void removedService(ServiceReference<CseService> reference,
				CseService service) {
			MqttRequestHandler.setCseService(null);
		}

	}

	private static class DataMapperServiceTracker implements
			ServiceTrackerCustomizer<DataMapperService, DataMapperService> {

		@Override
		public DataMapperService addingService(
				ServiceReference<DataMapperService> reference) {
			if (reference == null) {
				return null;
			}
			Object service = Activator.getContext().getService(reference);
			if (service != null && service instanceof DataMapperService) {
				DataMapperService dms = (DataMapperService) service;
				LOGGER.debug("New DataMapper Service discovered: "
						+ dms.getServiceDataType());
				DataMapperRegistry.register(dms);
				return dms;
			}
			return null;
		}

		@Override
		public void modifiedService(
				ServiceReference<DataMapperService> reference,
				DataMapperService service) {
			if (service != null) {
				DataMapperRegistry.register(service);
			}
		}

		@Override
		public void removedService(
				ServiceReference<DataMapperService> reference,
				DataMapperService service) {
			if (service != null) {
				DataMapperRegistry.remove(service);
			}
		}

	}

}
