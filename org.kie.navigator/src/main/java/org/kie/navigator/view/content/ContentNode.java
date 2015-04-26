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
import org.kie.navigator.view.server.KieServer;

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

    protected final IServer server;
    /*
     * TODO: handler should be a generic IKieResourceHandler
     */
    protected KieServer handler;
    protected IContainerNode parent;
    protected T container;
    protected final String name;

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

    public IServer getServer() {
        return server;
    }

    public void dispose() {
        container = null;
        parent = null;
        if (handler!=null) {
        	handler.dispose();
        	handler = null;
        }
    }

    /**
     * TODO: handler should be a generic IKieResourceHandler
     * @return
     */
    protected KieServer getHandler() {
    	 return handler;
    }
    
    public boolean isResolved() {
    	return true;
    }
    
    public Object resolveContent() {
    	return this;
    }
}
