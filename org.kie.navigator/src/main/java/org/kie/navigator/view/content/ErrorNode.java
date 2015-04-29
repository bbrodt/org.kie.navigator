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

import org.kie.navigator.view.server.IKieResourceHandler;
import org.kie.navigator.view.server.KieResourceHandler;

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
    private final static class ErrorHandler extends KieResourceHandler {
    	public ErrorHandler() {
    		super(null,"Error");
    	}

		/* (non-Javadoc)
		 * @see org.kie.navigator.view.server.IKieResourceHandler#getChildren()
		 */
		@Override
		public List<? extends IKieResourceHandler> getChildren() throws Exception {
			return null;
		}
    }
    
    protected ErrorNode(IContainerNode<?> container, String text) {
        super(container, new ErrorHandler());
        this.text = text;
    }

    public String getText() {
        return text;
    }

	/* (non-Javadoc)
	 * @see org.kie.navigator.view.content.ContentNode#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return false;
	}
}
