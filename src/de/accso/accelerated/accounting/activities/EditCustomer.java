package de.accso.accelerated.accounting.activities;

import de.accso.accelerated.accounting.R;
import de.accso.accelerated.accounting.data.Customer;
import de.accso.accelerated.accounting.data.helper.CustomerQuery;
import de.accso.accelerated.accounting.util.MessageUtil;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EditCustomer extends Activity{
	
	public static final String CUSTOMER_ID = "Customer_Id";
	private int currentEditCustomerId;
	private Customer currentEditCustomer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_customer);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			currentEditCustomerId = extras.getInt(CUSTOMER_ID);
			currentEditCustomer = CustomerQuery.getById(this, currentEditCustomerId);
			initView();
		}
		setHeadingText();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}	
	
	public void saveCustomer(View v)
	{
		String name = ((EditText) findViewById(R.id.txt_edit_customer_name)).getText().toString();
		String street = ((EditText) findViewById(R.id.txt_edit_customer_street)).getText().toString();
		String city = ((EditText) findViewById(R.id.txt_edit_customer_city)).getText().toString();
		String zip = ((EditText) findViewById(R.id.txt_edit_customer_zip)).getText().toString();
		
		if(currentEditCustomer == null){
			currentEditCustomer = new Customer(this, name, street, city, zip);
			currentEditCustomerId = currentEditCustomer.getId();
		} else {
			currentEditCustomer.setName(name);
			currentEditCustomer.getAddress().setStreet(street);
			currentEditCustomer.getAddress().setCity(city);
			currentEditCustomer.getAddress().setZip(zip);
		}
		currentEditCustomer.Save();
		setHeadingText();
		MessageUtil.ShowMessage(this, "Kunde wurde gespeichert.");
	}
	
	public void goBack(View v){
		finish();
	}
	
	public void initView(){
		((EditText) findViewById(R.id.txt_edit_customer_name)).setText(currentEditCustomer.getName());
		((EditText) findViewById(R.id.txt_edit_customer_street)).setText(currentEditCustomer.getAddress().getStreet());
		((EditText) findViewById(R.id.txt_edit_customer_city)).setText(currentEditCustomer.getAddress().getCity());
		((EditText) findViewById(R.id.txt_edit_customer_zip)).setText(currentEditCustomer.getAddress().getZip());
	}
	
	private void setHeadingText(){
		if(currentEditCustomerId == 0){
			((TextView) findViewById(R.id.lbl_edit_customer_heading)).setText("Kunden erfassen");
		}else{
			((TextView) findViewById(R.id.lbl_edit_customer_heading)).setText("Kunden bearbeiten");
		}
	}
	
}
