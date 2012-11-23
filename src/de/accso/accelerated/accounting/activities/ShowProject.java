package de.accso.accelerated.accounting.activities;

import de.accso.accelerated.accounting.R;
import de.accso.accelerated.accounting.data.Project;
import de.accso.accelerated.accounting.data.helper.ProjectHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ShowProject extends Activity{
	
	
	public static final String PROJECT_ID = "Project_Id";
	private int currentProjectId;
	private Project currentProject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_project);
		Bundle extras = getIntent().getExtras();
		if (extras != null && extras.containsKey(PROJECT_ID)) {
			currentProjectId = extras.getInt(PROJECT_ID);
		}
		initEditCustomerTable();
	}
	
	@Override
	protected void onResume() {
		super.onResume();		
		if(currentProjectId != 0){
			currentProject = ProjectHelper.getById(this, currentProjectId);
			initView();
		}
	}		
	
	public void initView(){
		((TextView) findViewById(R.id.lbl_project_name)).setText(currentProject.getName());
		((TextView) findViewById(R.id.lbl_project_description)).setText(currentProject.getDescription());
	}
	
	private void initEditCustomerTable() {
		TableLayout headerTable = (TableLayout) findViewById(R.id.show_project_edit);
		headerTable.setScrollContainer(false);
		headerTable.setClickable(true);
		
		
		TableRow headerRow = getHeaderRow();
		headerTable.addView(headerRow);
		TextView text = (TextView) headerRow.findViewById(R.id.table_header_row_text);
		text.setText(R.string.btn_go_edit_project);
		
		headerRow.setClickable(true);
		headerRow.setFocusable(true);
		headerRow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				editProject();
			}
		});
	}
	
	private TableRow getHeaderRow() {
		LayoutInflater vi = getLayoutInflater();
		TableRow headerRow = (TableRow) vi.inflate(R.layout.table_list_header_row, null);
		return headerRow;
	}
	
	public void editProject(){
		Intent intent = new Intent(this, EditProject.class);
		intent.putExtra(EditProject.PROJECT_ID, currentProjectId);
		startActivity(intent);
	}
}
