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

package org.kie.navigator.view.content;

import org.eclipse.wst.server.core.IServer;

/**
 *
 */
public class ServerNode extends ResourceNode {

	/**
	 * @param server
	 * @param name
	 */
	public ServerNode(IServer server, String name) {
		super(server, name);
		// TODO Auto-generated constructor stub
	}

}