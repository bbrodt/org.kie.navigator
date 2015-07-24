package org.kie.eclipse.navigator.view.actions.dialogs;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.kie.eclipse.navigator.view.server.IKieProject;
import org.kie.eclipse.navigator.view.server.IKieRepository;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class ProjectRequestDialog extends AbstractKieRequestDialog {

	IKieProject project;
	KieRequestDialogTextField name;
	KieRequestDialogTextField description;
	
	public ProjectRequestDialog(Shell shell, IKieRepository repository) {
		super(shell, "Project", new IKieRequestValidator() {
			@Override
			public String isValid(JsonObject object) {
				JsonValue jv;
				jv = object.get("name");
				String name = jv==null ? null : jv.asString();
				if (name!=null && !name.isEmpty()) {
					try {
						for (IKieProject p : repository.getProjects()) {
							if (p.getName().equals(name))
								return "Project '"+name+"' already exists in this Repository";
						}
					}
					catch (Exception e) {
					}
				}
				else {
					return "Name is required";
				}
				return null;
			}
        });
		this.project = project;
	}
    
	@Override
	protected void createFields(Composite composite) {
        setMessage("Enter the Project details");

		name = new KieRequestDialogTextField(composite, "Name:", "", result, "name");
		name.setChangeListener(new IKieRequestChangeListener() {
			@Override
			public void objectChanged(JsonObject object) {
				validate();
			}
		});
		description = new KieRequestDialogTextField(composite, "Description:", "", result, "description");
	}

	@Override
	public JsonObject getResult() {
		return super.getResult();
	}
	
	
}
