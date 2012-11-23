package de.accso.accelerated.accounting.activities;

import java.util.List;

import de.accso.accelerated.accounting.R;
import de.accso.accelerated.accounting.data.Customer;
import de.accso.accelerated.accounting.data.Project;
import de.accso.accelerated.accounting.data.helper.CustomerQuery;
import de.accso.accelerated.accounting.data.helper.ProjectHelper;
import de.accso.accelerated.accounting.util.StringUtil;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ShowCustomer extends Activity{
	
	public static final String CUSTOMER_ID = "Customer_Id";
	private int currentCustomerId;
	private Customer currentCustomer;
	private List<Project> projects;
	private ArrayAdapter<Project> projectAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_customer);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			currentCustomerId = extras.getInt(CUSTOMER_ID);
			initProjectView();
		}
		initEditCustomerTable();
		initAddProjectTable();
	}
	
	@Override
	protected void onResume() {
		super.onResume();		
		if(currentCustomerId != 0){
			currentCustomer = CustomerQuery.getById(this, currentCustomerId);
			initView();
			projects.clear();
			projects.addAll(ProjectHelper.getByCustomerIdAsList(this, currentCustomerId));
			projectAdapter.notifyDataSetChanged();
		}
	}	
	
	public void initView(){
		
		StringBuilder sb = new StringBuilder();
		if(!StringUtil.isNullOrEmpty(currentCustomer.getAddress().getStreet())){
			sb.append(currentCustomer.getAddress().getStreet() + "\r\n");
		} 
		if(!StringUtil.isNullOrEmpty(currentCustomer.getAddress().getZip())){
			sb.append(currentCustomer.getAddress().getZip() + " ");
		}
		if(!StringUtil.isNullOrEmpty(currentCustomer.getAddress().getCity())){
			sb.append(currentCustomer.getAddress().getCity());
		}
		
		((TextView) findViewById(R.id.lbl_customer_name)).setText(currentCustomer.getName());
		((TextView) findViewById(R.id.lbl_address_info)).setText(sb.toString());
	}
	
	private void initEditCustomerTable() {
		TableLayout headerTable = (TableLayout) findViewById(R.id.show_customer_edit);
		headerTable.setScrollContainer(false);
		headerTable.setClickable(true);
		
		
		TableRow headerRow = getHeaderRow();
		headerTable.addView(headerRow);
		TextView text = (TextView) headerRow.findViewById(R.id.table_header_row_text);
		text.setText(R.string.btn_go_edit_customer);
		
		headerRow.setClickable(true);
		headerRow.setFocusable(true);
		headerRow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				editCustomer();
			}
		});
	}
	
	private TableRow getHeaderRow() {
		LayoutInflater vi = getLayoutInflater();
		TableRow headerRow = (TableRow) vi.inflate(R.layout.table_list_header_row, null);
		return headerRow;
	}
	
	private void initAddProjectTable() {
		TableLayout headerTable = (TableLayout) findViewById(R.id.show_customer_add_project);
		headerTable.setScrollContainer(false);
		headerTable.setClickable(true);
		
		
		TableRow headerRow = getHeaderRow();
		headerTable.addView(headerRow);
		TextView text = (TextView) headerRow.findViewById(R.id.table_header_row_text);
		text.setText(R.string.btn_go_create_project);
		
		headerRow.setClickable(true);
		headerRow.setFocusable(true);
		headerRow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				createProject();
			}
		});
	}
	
	private void initProjectView(){
		projects = ProjectHelper.getByCustomerIdAsList(this, currentCustomerId);
		ListView list = (ListView) findViewById(R.id.show_projects_list);
		projectAdapter = createProjectAdapter();
	    list.setAdapter(projectAdapter);
	    list.setOnItemClickListener(createOnItemClickListener());
		if (projects.size() == 0){
			((TextView) findViewById(R.id.lbl_projects_list_no_rows)).setText("Keine Projekte vorhanden");
		}
	}
	
	public void editCustomer(){
		Intent intent = new Intent(this, EditCustomer.class);
		intent.putExtra(EditCustomer.CUSTOMER_ID, currentCustomerId);
		startActivity(intent);
	}
	
	public void createProject(){
		Intent intent = new Intent(this, EditProject.class);
		intent.putExtra(EditProject.CUSTOMER_ID, currentCustomerId);
		startActivity(intent);
	}	
	
	private OnItemClickListener createOnItemClickListener(){
		return new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{     
				Project project =  projectAdapter.getItem(position);
			    showProject(project.getId());
			}
		};
	}
	
	
	private ArrayAdapter<Project> createProjectAdapter(){
		return new ArrayAdapter<Project>(this, R.id.show_projects_list, projects){
    		@Override
    	    public View getView(int position, View convertView, ViewGroup parent) {
    			View row; 		
    			if (null == convertView) {			
    				this.getContext();
					LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
    	            row = vi.inflate(R.layout.two_line_list_line_view, null);
				} else {			
					row = convertView;		
				} 		
    			
    			Project project = getItem(position);
    			
    			TextView tvProjectName = (TextView) row.findViewById(R.id.show_list_line_1);
    			TextView tvProjectDescription = (TextView) row.findViewById(R.id.show_list_line_2);
    			if(tvProjectName != null){
    				tvProjectName.setText(project.getName());
    			}
    			if(tvProjectDescription != null){
    				String description = project.getDescription();
    				if(description.length() > 20){
    				    description = description.substring(0, 20) + "...";
    				}
    				tvProjectDescription.setText(description);
    			}
    		     		
    			return row;
    		};
    	};
	}
	
	private void showProject(int id){
		Intent intent = new Intent(this, ShowProject.class);
		intent.putExtra(ShowProject.PROJECT_ID, id);
		startActivity(intent);
	}
	
}
