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

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.server.core.IServer;
import org.kie.navigator.Activator;
import org.kie.navigator.view.content.IContainerNode;
import org.kie.navigator.view.content.IContentNode;
import org.kie.navigator.view.content.IErrorNode;

/**
 * ServerContentLabelProvider
 * 
 * <p/>
 * Label provider implementation for content nodes.
 * 
 * @author Rob Cernich
 */
public class KieNavigatorLabelProvider extends LabelProvider {

	public String getText(Object element) {
		if (element instanceof IContainerNode) {
			return ((IContainerNode<?>) element).getName();
		} else if (element instanceof IContentNode) {
			return ((IContentNode<?>) element).getName();
		} else if (element instanceof IErrorNode) {
			return ((IErrorNode) element).getText();
		} else if (element == KieNavigatorContentProvider.PENDING) {
			return "Loading...";
		}
		return super.getText(element);
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof IServer) {
			if (((IServer) element).getServerState() == IServer.STATE_STARTED)
				return Activator.getImageDescriptor(Activator.IMG_SERVER_RUN).createImage();
			return Activator.getImageDescriptor(Activator.IMG_SERVER_STOP).createImage();
		} else if (element instanceof IContainerNode<?>) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
		} else if (element instanceof IErrorNode) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_ERROR_TSK);
		} else if (element instanceof IContentNode<?>) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
		}
		return super.getImage(element);
	}
}
