package org.kie.eclipse.navigator.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.kie.eclipse.navigator.IKieNavigatorConstants;

public class ServerPropertyPage extends FieldEditorPropertyPage implements IKieNavigatorConstants {

	public ServerPropertyPage() {
	}

	@Override
	protected void createFieldEditors() {
		addField(new BooleanFieldEditor(PREF_USE_DEFAULT_GIT_PATH, "Use default Git Repository Path", getFieldEditorParent()));
		addField(new DirectoryFieldEditor(PREF_GIT_REPO_PATH, "Git Repository Path", getFieldEditorParent()));
	}

}
