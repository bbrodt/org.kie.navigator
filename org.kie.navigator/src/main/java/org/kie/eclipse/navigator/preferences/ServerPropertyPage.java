package org.kie.eclipse.navigator.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.wst.server.core.IServer;
import org.kie.eclipse.navigator.IKieNavigatorConstants;

public class ServerPropertyPage extends FieldEditorPropertyPage implements IKieNavigatorConstants {

	public ServerPropertyPage() {
		super(GRID);
	}

	@Override
	protected void createFieldEditors() {
		IServer server = getResourceHandler().getServer();
		addField(new ReadonlyStringFieldEditor("Server Name:", server.getName(), getFieldEditorParent()));
		addField(new ReadonlyStringFieldEditor("Host Name:", server.getHost(), getFieldEditorParent()));
		
		StringFieldEditor stringEditor;
		PasswordStringFieldEditor passwordEditor;
		IntegerFieldEditor intEditor;
		
		stringEditor = new StringFieldEditor(PREF_SERVER_USERNAME, "Username:", getFieldEditorParent());
		addField(stringEditor);
		
		passwordEditor = new PasswordStringFieldEditor(PREF_SERVER_PASSWORD, "Password:", getFieldEditorParent());
		addField(passwordEditor);
		
		stringEditor = new StringFieldEditor(PREF_SERVER_KIE_APPLICATION_NAME, "KIE Application Name:", getFieldEditorParent());
		addField(stringEditor);

		intEditor = new IntegerFieldEditor(PREF_SERVER_HTTP_PORT, "HTTP Port:", getFieldEditorParent());
		addField(intEditor);
		
		intEditor = new IntegerFieldEditor(PREF_SERVER_GIT_PORT, "Git Port:", getFieldEditorParent());
		addField(intEditor);
		
		addField(new BooleanFieldEditor(PREF_USE_DEFAULT_GIT_PATH, "Use default Git Repository Path", getFieldEditorParent()));
		addField(new DirectoryFieldEditor(PREF_GIT_REPO_PATH, "Git Repository Path", getFieldEditorParent()));
	}
}
