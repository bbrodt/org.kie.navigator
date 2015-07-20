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

package org.kie.eclipse.navigator.view.server;

import java.util.List;

/**
 *
 */
public class KieOrganization extends KieResourceHandler implements IKieOrganization {

	/**
	 * 
	 */
	public KieOrganization(IKieServer service, String name) {
		super(service, name);
	}
	
	public List<? extends IKieResourceHandler> getChildren() throws Exception {
		return getDelegate().getRepositories(this);
	}
	
	public boolean isLoaded() {
		return true;
	}
}