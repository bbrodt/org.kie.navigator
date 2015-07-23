package org.kie.eclipse.navigator.view.actions.dialogs;

import java.io.IOException;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.kie.eclipse.navigator.view.server.IKieOrganization;
import org.kie.eclipse.navigator.view.server.IKieServer;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class OrganizationRequestDialog extends AbstractKieRequestDialog {

	IKieServer server;
	KieRequestDialogTextField name;
	KieRequestDialogTextField description;
	KieRequestDialogTextField owner;
	
	public OrganizationRequestDialog(Shell shell, IKieServer server) {
		super(shell, "Organizational Unit", new IKieRequestValidator() {
			@Override
			public String isValid(JsonObject object) {
				JsonValue jv;
				jv = object.get("name");
				String name = jv==null ? null : jv.asString();
				jv = object.get("owner");
				String owner = jv==null ? null : jv.asString();
				if (name!=null && !name.isEmpty()) {
					try {
						for (IKieOrganization org : server.getOrganizations()) {
							if (org.getName().equals(name))
								return "Organizational Unit '"+name+"' already exists";
						}
					}
					catch (IOException e) {
					}
				}
				else {
					return "Name is required";
				}
				if (owner==null || owner.isEmpty())
					return "Owner is required";
				return null;
			}
        });
		this.server = server;
	}
    
	@Override
	protected void createFields(Composite composite) {
        setMessage("Enter the Organizational Unit details");

		name = new KieRequestDialogTextField(composite, "Name:", "", result, "name");
		name.setChangeListener(new IKieRequestChangeListener() {
			@Override
			public void objectChanged(JsonObject object) {
				validate();
			}
		});
		description = new KieRequestDialogTextField(composite, "Description:", "", result, "description");
		owner = new KieRequestDialogTextField(composite, "Owner:", "", result, "owner");
		owner.setChangeListener(new IKieRequestChangeListener() {
			@Override
			public void objectChanged(JsonObject object) {
				validate();
			}
		});
	}
}
