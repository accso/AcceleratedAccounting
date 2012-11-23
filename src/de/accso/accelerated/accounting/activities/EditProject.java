package de.accso.accelerated.accounting.activities;

import de.accso.accelerated.accounting.R;
import de.accso.accelerated.accounting.data.Project;
import de.accso.accelerated.accounting.data.helper.ProjectHelper;
import de.accso.accelerated.accounting.util.MessageUtil;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EditProject extends Activity{
	
	public static final String PROJECT_ID = "Project_Id";
	public static final String CUSTOMER_ID = "Customer_Id";
	private int currentEditProjectId;
	private Project currentEditProject;
	private int customerId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_project);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			if(extras.containsKey(PROJECT_ID)){
				currentEditProjectId = extras.getInt(PROJECT_ID);
				currentEditProject = ProjectHelper.getById(this, currentEditProjectId);
				customerId = currentEditProject.getCustomer().getId();
			}else{
				customerId = extras.getInt(CUSTOMER_ID);
			}
			initView();
		}
		setHeadingText();
	}
	
	public void saveProject(View v)	{
		String name = ((EditText) findViewById(R.id.txt_edit_project_name)).getText().toString();
		String description = ((EditText) findViewById(R.id.txt_edit_project_description)).getText().toString();
		
		if(currentEditProject == null){
			currentEditProject = new Project(this, name, description, customerId);
		} else {
			currentEditProject.setName(name);
			currentEditProject.setDescription(description);
		}
		currentEditProject.Save();
		currentEditProjectId = currentEditProject.getId();
		setHeadingText();
		MessageUtil.ShowMessage(this, "Projekt wurde gespeichert.");
	}
	
	public void goBack(View v){
		finish();
	}
	
	public void initView(){
		if(currentEditProject != null){
			((EditText) findViewById(R.id.txt_edit_project_name)).setText(currentEditProject.getName());
			((EditText) findViewById(R.id.txt_edit_project_description)).setText(currentEditProject.getDescription());
		}
	}
	
	private void setHeadingText(){
		if(currentEditProjectId == 0){
			((TextView) findViewById(R.id.lbl_edit_project_heading)).setText("Projekt erfassen");
		}else{
			((TextView) findViewById(R.id.lbl_edit_project_heading)).setText("Projekt bearbeiten");
		}
	}
}
