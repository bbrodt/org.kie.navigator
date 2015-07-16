package org.kie.eclipse.navigator.view.actions;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.kie.eclipse.navigator.view.content.ContentNode;
import org.kie.eclipse.navigator.view.content.IContainerNode;
import org.kie.eclipse.navigator.view.server.KieRepository;

public class RepositoryActionProvider extends KieNavigatorActionProvider {

	public RepositoryActionProvider() {
	}

    public void init(ICommonActionExtensionSite aSite) {
        super.init(aSite);
        addAction(new CloneRepositoryAction(aSite.getStructuredViewer()));
        addAction(new DeleteRepositoryAction(aSite.getStructuredViewer()));
        addAction(new CreateProjectAction(aSite.getStructuredViewer()));
    }

	private class CloneRepositoryAction extends KieNavigatorAction {

		protected CloneRepositoryAction(ISelectionProvider provider, String text) {
			super(provider, text);
		}
		
		public CloneRepositoryAction(ISelectionProvider selectionProvider) {
			this(selectionProvider, "Clone Repository");
		}

		@Override
		public boolean isEnabled() {
            IStructuredSelection selection = getStructuredSelection();
            if (selection != null && !selection.isEmpty()) {
	            IContainerNode<?> container = (IContainerNode<?>) ((IStructuredSelection) selection).getFirstElement();
	            if (container instanceof ContentNode) {
	            	KieRepository handler = (KieRepository) ((ContentNode)container).getHandler();
	            	if (handler==null || !handler.isLoaded())
	            		return true;
	            }
            }
			return false;
		}

		@Override
		public String getToolTipText() {
			return "Clone this Git Repository from the remote server";
		}

		public void run() {
            IStructuredSelection selection = getStructuredSelection();
            if (selection == null || selection.isEmpty()) {
                return;
            }
            IContainerNode<?> container = (IContainerNode<?>) ((IStructuredSelection) selection).getFirstElement();
            System.out.println("Clone Repository: "+container.getName());
        }
	}

	private class DeleteRepositoryAction extends KieNavigatorAction {

		protected DeleteRepositoryAction(ISelectionProvider provider, String text) {
			super(provider, text);
		}
		
		public DeleteRepositoryAction(ISelectionProvider selectionProvider) {
			this(selectionProvider, "Delete Repository");
		}

		@Override
		public String getToolTipText() {
			return "Delete this Git Repository from the remote server";
		}

		public void run() {
            IStructuredSelection selection = getStructuredSelection();
            if (selection == null || selection.isEmpty()) {
                return;
            }
            IContainerNode<?> container = (IContainerNode<?>) ((IStructuredSelection) selection).getFirstElement();
            System.out.println("Delete Repository: "+container.getName());
        }
	}

	private class CreateProjectAction extends KieNavigatorAction {

		protected CreateProjectAction(ISelectionProvider provider, String text) {
			super(provider, text);
		}
		
		public CreateProjectAction(ISelectionProvider selectionProvider) {
			this(selectionProvider, "Create Project...");
		}

		@Override
		public String getToolTipText() {
			return "Create a new Project with the New Drools Project Wizard";
		}

		@Override
		public boolean isEnabled() {
            IStructuredSelection selection = getStructuredSelection();
            if (selection != null && !selection.isEmpty()) {
	            IContainerNode<?> container = (IContainerNode<?>) ((IStructuredSelection) selection).getFirstElement();
	            if (container instanceof ContentNode) {
	            	KieRepository handler = (KieRepository) ((ContentNode)container).getHandler();
	            	if (handler==null || !handler.isLoaded())
	            		return false;
	            }
            }
			return true;
		}

		public void run() {
            IStructuredSelection selection = getStructuredSelection();
            if (selection == null || selection.isEmpty()) {
                return;
            }
            IContainerNode<?> container = (IContainerNode<?>) ((IStructuredSelection) selection).getFirstElement();
            System.out.println("Create Project: "+container.getName());
        }
	}
}
