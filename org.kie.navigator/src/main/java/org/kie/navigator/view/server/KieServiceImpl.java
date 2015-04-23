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

package org.kie.navigator.view.server;

import org.eclipse.wst.server.core.IServer;

/**
 *
 */
public class KieServiceImpl implements IKieServiceImpl {

	private IServer server;
	
	/**
	 * @param server
	 */
	public KieServiceImpl() {
		
	}
	
	public void setServer(IServer server) {
		this.server = server;
	}

	
}