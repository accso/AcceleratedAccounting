package de.accso.accelerated.accounting.activities;

import java.util.List;
import de.accso.accelerated.accounting.R;
import de.accso.accelerated.accounting.data.Customer;
import de.accso.accelerated.accounting.data.helper.CustomerQuery;
import de.accso.accelerated.accounting.storage.AcceleratedAccountingProvider;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ShowCustomers extends Activity {
	
	private List<Customer> customers;
	private ArrayAdapter<Customer> customerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_customers);
		initView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		customers.clear();
		customers.addAll(CustomerQuery.getAllAsList(this));
		customerAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.customer_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		 // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.customer_menu_item_create_new:
	    	this.createNewCustomer();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	private void initView(){
		initAddCustomerTable();
		ListView list = (ListView) findViewById(R.id.show_customers_list);
		customers = CustomerQuery.getAllAsList(this);
		customerAdapter = createCustomerAdapter();
	    list.setAdapter(customerAdapter);
	    list.setOnItemClickListener(createOnItemClickListener());
    }
	
	private void initAddCustomerTable() {
		TableLayout headerTable = (TableLayout) findViewById(R.id.show_customers_add_customer);
		headerTable.setScrollContainer(false);
		headerTable.setClickable(true);
		
		
		TableRow headerRow = getHeaderRow();
		headerTable.addView(headerRow);
		TextView text = (TextView) headerRow.findViewById(R.id.table_header_row_text);
		text.setText(R.string.add_new_customer);
		
		headerRow.setClickable(true);
		headerRow.setFocusable(true);
		headerRow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				createNewCustomer();
			}
		});
	}
	
	private TableRow getHeaderRow() {
		LayoutInflater vi = getLayoutInflater();
		TableRow headerRow = (TableRow) vi.inflate(R.layout.table_list_header_row, null);
		return headerRow;
	}
	
	private OnItemClickListener createOnItemClickListener(){
		return new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{     
				Customer customer =  customerAdapter.getItem(position);
			    showCustomer(customer.getId());
			}
		};
	}
	
	private ArrayAdapter<Customer> createCustomerAdapter(){
		return new ArrayAdapter<Customer>(this, R.id.show_customers_list, customers){
			
			
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
    			
    			Customer customer = getItem(position);
    			
    			TextView tvCustomerName = (TextView) row.findViewById(R.id.show_list_line_1);
    			TextView tvCity = (TextView) row.findViewById(R.id.show_list_line_2);
    			if(tvCustomerName != null){
    				tvCustomerName.setText(customer.getName());
    			}
    			if(tvCity != null){
    				tvCity.setText(customer.getAddress().getCity());
    			}
    		     		
    			return row;
    		};
    	};
	}
	
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.show_customers_list) {
			super.onCreateContextMenu(menu, v, menuInfo);  
			MenuInflater inflater = getMenuInflater();  
			inflater.inflate(R.menu.customer_context_menu, menu);
			Cursor cursor = (Cursor) customerAdapter.getItem(((AdapterContextMenuInfo) menuInfo).position);
		    String name = cursor.getString(cursor.getColumnIndex(AcceleratedAccountingProvider.COLUMN_CUSTOMER_NAME));
			menu.setHeaderTitle(name);
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Cursor cursor = (Cursor) customerAdapter.getItem(info.position);
	    int id = cursor.getInt(cursor.getColumnIndex(AcceleratedAccountingProvider.COLUMN_CUSTOMER_ID));
		
	    switch (item.getItemId()) {
	    case R.id.customer_context_menu_edit_customer:
	    	editCustomer(id);
	    	return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    	
	    }
	}
	
	public void createNewCustomer(View v){
		createNewCustomer();
	}
	
	private void createNewCustomer(){
		Intent intent = new Intent(this, EditCustomer.class);
		startActivity(intent);
	}
	
	private void editCustomer(int id){
		Intent intent = new Intent(this, EditCustomer.class);
		intent.putExtra(EditCustomer.CUSTOMER_ID, id);
		startActivity(intent);
	}
	
	private void showCustomer(int id){
		Intent intent = new Intent(this, ShowCustomer.class);
		intent.putExtra(EditCustomer.CUSTOMER_ID, id);
		startActivity(intent);
	}
	
	
}
