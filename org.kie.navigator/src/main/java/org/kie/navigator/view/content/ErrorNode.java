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
 * ErrorNode
 * 
 * <p/>
 * Represents an error that occurred while attempting to retrieve a resource's
 * children.
 * 
 * @author Rob Cernich
 */
public class ErrorNode extends ContentNode<IContainerNode<?>> implements IErrorNode {

    public static final String ERROR_TYPE = "error"; //$NON-NLS-1$

    private final String text;

    protected ErrorNode(IContainerNode<?> container, String text) {
        super(container, ERROR_TYPE);
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
