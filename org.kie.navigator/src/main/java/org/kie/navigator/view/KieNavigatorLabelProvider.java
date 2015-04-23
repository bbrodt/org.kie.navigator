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
package org.kie.navigator.view;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.server.core.IServer;
import org.kie.navigator.Activator;
import org.kie.navigator.view.content.IAttributeNode;
import org.kie.navigator.view.content.IAttributesContainer;
import org.kie.navigator.view.content.IContainerNode;
import org.kie.navigator.view.content.IContentNode;
import org.kie.navigator.view.content.IErrorNode;
import org.kie.navigator.view.content.IResourceNode;

/**
 * ServerContentLabelProvider
 * 
 * <p/>
 * Label provider implementation for content nodes.
 * 
 * @author Rob Cernich
 */
public class KieNavigatorLabelProvider extends LabelProvider {

    /**
     * Maps node names to any label that should be used.
     * <p/>
     * TODO: this should probably be contributed by an extension,, which should
     * provide icons as well.
     */
    private static Map<String, String> LABELS;

    public String getText(Object element) {
        if (element instanceof IAttributeNode) {
            IAttributeNode attrNode = (IAttributeNode) element;
            String value = attrNode.getValue();
            if (value == null || IAttributeNode.UNDEFINED_VALUE.equals(value)) {
                value = "<undefined>";
            }
            return NLS.bind("{0} : {1}", attrNode.getName(), value);
        } else if (element instanceof IErrorNode) {
            return ((IErrorNode) element).getText();
        } else if (element instanceof IContentNode) {
            return getMappedName(((IContentNode<?>) element).getName());
        } else if (element == KieNavigatorContentProvider.PENDING) {
            return "Loading...";
        }
        return super.getText(element);
    }

    @Override
    public Image getImage(Object element) {
    	if (element instanceof IServer) {
    		if (((IServer)element).getServerState()==IServer.STATE_STARTED)
    			return Activator.getImageDescriptor(Activator.IMG_SERVER_RUN).createImage();
			return Activator.getImageDescriptor(Activator.IMG_SERVER_STOP).createImage();
    	}
    	else if (element instanceof IContainerNode<?>) {
            return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
        } else if (element instanceof IErrorNode) {
            return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_ERROR_TSK);
        } else if (element instanceof IContentNode<?>) {
            return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
        }
        return super.getImage(element);
    }

    private String getMappedName(String name) {
        if (name == null) {
            return "<undefined>";
        }
        if (LABELS.containsKey(name)) {
            return LABELS.get(name);
        }
        return name;
    }

    static {
        LABELS = new HashMap<String, String>();
        LABELS.put(IAttributesContainer.ATTRIBUTES_TYPE, "Attributes");
        LABELS.put("core-service", "Core Services"); //$NON-NLS-1$
        LABELS.put("deployment", "Deployments"); //$NON-NLS-1$
        LABELS.put("extension", "Extensions"); //$NON-NLS-1$
        LABELS.put("interface", "Interfaces"); //$NON-NLS-1$
        LABELS.put("path", "Paths"); //$NON-NLS-1$
        LABELS.put(IResourceNode.ROOT_TYPE, "Server Details");
        LABELS.put("socket-binding-group", "Socket Bindings"); //$NON-NLS-1$
        LABELS.put("subsystem", "Subsystems"); //$NON-NLS-1$
        LABELS.put("system-property", "System Properties"); //$NON-NLS-1$
    }
}
