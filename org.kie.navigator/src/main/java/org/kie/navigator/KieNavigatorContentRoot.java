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

package org.kie.navigator;

import org.eclipse.ui.navigator.CommonNavigator;


/**
 * This is a placeholder class that replaces the default IWorkspaceRoot used as
 * initial input for CommonViewers. This class is part of the public API and
 * allows other CommonNavigator plugins to embed KIE Navigator content as child
 * or root nodes into their own content.
 */
public class KieNavigatorContentRoot {

	protected CommonNavigator viewer;

	/**
	 * @param kieNavigatorView
	 */
	public KieNavigatorContentRoot(CommonNavigator viewer) {
		this.viewer = viewer;
	}

}
