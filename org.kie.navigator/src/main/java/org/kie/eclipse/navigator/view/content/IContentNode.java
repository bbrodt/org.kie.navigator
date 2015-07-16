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
package org.kie.eclipse.navigator.view.content;

import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.wst.server.core.IServer;
import org.kie.eclipse.navigator.view.server.IKieResourceHandler;

public interface IContentNode<T extends IContainerNode<?>> {

    /**
     * @return returns the server containing this node.
     */
    IServer getServer();

    /**
     * @return the resource containing this node, if any.
     */
    IContainerNode getParent();

    /**
     * @return the containing node.
     */
    T getContainer();

    /**
     * @return the name of this node.
     */
    String getName();

    /**
     * Frees any resources held by this node.
     */
    void dispose();
    
    /**
     * Check if the tree viewer content for this node is available.
     * 
     * @return true if the node has been loaded (resolved) false if not
     */
    boolean isResolved();
    
    /**
     * Returns the actual tree viewer content for this node
     */
    Object resolveContent();
    IContainerNode<?> getRoot();
    IKieResourceHandler getHandler();
    CommonNavigator getNavigator();
}
