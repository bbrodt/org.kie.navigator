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

import java.io.IOException;
import java.util.List;

/**
 *
 */
public interface IKieServer extends IKieResourceHandler {

	List<IKieOrganization> getOrganizations() throws IOException;
	List<IKieRepository> getRepositories(IKieOrganization organization) throws IOException;
	List<IKieProject> getProjects(IKieRepository repository) throws IOException;
}
