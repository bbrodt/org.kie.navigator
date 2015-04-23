/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.kie.navigator.view.content;

import org.eclipse.wst.server.core.IServer;
import org.kie.navigator.view.server.KieService;

/**
 * ContentNode
 * 
 * <p/>
 * Base implementation of IContentNode.
 * 
 * @author Rob Cernich
 */
public class ContentNode<T extends IContainerNode<?>> implements IContentNode<T> {

    /** The path separator for addresses. */
    public static final String PATH_SEPARATOR = "/"; //$NON-NLS-1$

    private final IServer server;
    private KieService service;
    private IContainerNode parent;
    private T container;
    private final String name;

    protected ContentNode(IServer server, String name) {
        this.server = server;
        this.parent = null;
        this.container = null;
        this.name = name;
    }

    protected ContentNode(T container, String name) {
        this.server = container.getServer();
        this.parent = container instanceof IContainerNode ? (IContainerNode) container : container.getParent();
        this.container = container;
        this.name = name;
    }

    public IContainerNode getParent() {
        return parent;
    }

    public T getContainer() {
        return container;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return getParent().getAddress() + PATH_SEPARATOR + getName();
    }

    public IServer getServer() {
        return server;
    }

    public void dispose() {
        container = null;
        parent = null;
        if (service!=null) {
        	service.dispose();
        	service = null;
        }
    }

    protected KieService getKieService() {
    	 if (service==null) {
    		 service = new KieService(server);
    	 }
    	 return service;
    }
}
