/*******************************************************************************
 * Copyright (c) 2011, 2012, 2013, 2014 Red Hat, Inc.
 *  All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 *
 * @author Bob Brodt
 ******************************************************************************/

package org.kie.navigator.view.server.jbpm60;

import org.eclipse.wst.server.core.IServer;
import org.kie.navigator.view.server.KieServiceImpl;

/**
 *
 */
public class Kie60ServiceImpl extends KieServiceImpl {

	private final static String KIE_VERSION = "org.jboss.kie.60";
	
	/**
	 * @param server
	 */
	public Kie60ServiceImpl() {
	}

	String getVersion() {
		return KIE_VERSION;
	}
}
