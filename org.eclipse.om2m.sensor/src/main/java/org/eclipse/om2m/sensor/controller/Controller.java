package org.eclipse.om2m.sensor.controller;

import org.eclipse.om2m.core.service.CseService;

public class Controller {
	public static CseService CSE;

	public static void setCse(CseService cse) {
		CSE = cse;
	}
}
