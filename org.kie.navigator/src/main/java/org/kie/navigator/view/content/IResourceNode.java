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

/**
 * IResourceNode
 * 
 * <p/>
 * Represents a resource in the management tree. A resource may contain
 * attributes, system metrics, as well as other types of resources.
 * 
 * @author Rob Cernich
 */
public interface IResourceNode extends IContainerNode<ITypeNode> {

    /** Represents the root of the management tree. */
    public static final String ROOT_TYPE = "root"; //$NON-NLS-1$

}