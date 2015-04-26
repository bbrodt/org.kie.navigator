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

import java.util.Collections;
import java.util.List;

import org.eclipse.wst.server.core.IServer;
import org.kie.navigator.view.server.IKieOrganization;

/**
 * ContainerNode
 * 
 * <p>
 * Abstract implementation of a container node. Delegates loading and takes care
 * of error handling.
 * 
 * @author Rob Cernich
 */
public abstract class ContainerNode<T extends IContainerNode<?>> extends ContentNode<T> implements IContainerNode<T> {

    private IErrorNode error;

    protected ContainerNode(IServer server, String name) {
        super(server, name);
    }

    protected ContainerNode(T container, String name) {
        super(container, name);
    }

    public final List<? extends Object> getChildren() {
        if (error != null) {
            return Collections.singletonList(error);
        }
        return delegateGetChildren();
    }

    public final void clearChildren() {
        clearError();
        delegateClearChildren();
    }

    protected void setError(IErrorNode error) {
        clearError();
        this.error = error;
    }

    @Override
    public void dispose() {
        clearChildren();
        super.dispose();
    }

    public final void load() {
        if (getServer().getServerState() != IServer.STATE_STARTED) {
            setError(new ErrorNode(this, "Not connected"));
            return;
        }
        try {
            delegateLoad();
            clearError();
        } catch (Exception e) {
            setError(new ErrorNode(this, "Loading error"));
        }
    }

    protected abstract List<? extends Object> delegateGetChildren();

    protected abstract void delegateClearChildren();

    protected abstract void delegateLoad() throws Exception;

    private void clearError() {
        if (error != null) {
            error.dispose();
            error = null;
        }
    }
}
