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

import java.util.List;

/**
 * IContainerNode
 * 
 * <p/>
 * Base interface for container (non-leaf) nodes.
 * 
 * @author Rob Cernich
 */
public interface IContainerNode<T extends IContainerNode<?>> extends IContentNode<T> {

    /**
     * @return the children of this container.
     */
    public List<? extends Object> getChildren();

    /**
     * Loads the content of this container. This method is invoked by the
     * content provider if getChildren() returns null.
     */
    public void load();

    /**
     * Clears the children of this container. After this method has been
     * invoked, getChildren() must return null. This method is called when a
     * refresh is requested.
     */
    public void clearChildren();
    
    /**
     * Check if a node has child nodes. This may be false if the node itself has
     * not been resolved, e.g. a RepositoryNode has not been resolved to a cloned
     * local repository, or a ProjectNode has not resolved to a workspace IProject
     * 
     * @return true if this node has children
     */
    public boolean hasChildren();

}
