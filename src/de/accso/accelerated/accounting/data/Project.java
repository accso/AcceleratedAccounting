package de.accso.accelerated.accounting.data;

import de.accso.accelerated.accounting.data.helper.CustomerQuery;
import de.accso.accelerated.accounting.storage.AcceleratedAccountingProvider;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

public class Project implements Comparable<Project> {
	
	private int id;
	private String name;
	private String description;
	private int customerId;
	private Customer customer;
	private Context context;
	
	public Project(Context context, String name, String description, int customerId){
		this.context = context;
		this.name = name;
		this.description = description;
		this.customerId = customerId;
	}
	
	public Project(Context context, int id, String name, String description, int customerId){
		this(context, name, description, customerId);
		this.id = id;
	}
	
	public int getId(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public Customer getCustomer(){
		if(customer == null && customerId != 0){
			customer = CustomerQuery.getById(context, customerId);
		}
		return customer;
	}
	
	//Speichert das Projekt - wenn Id == null => Insert
	//return - Id vom Projekt
	public int Save(){
		Uri baseUri = AcceleratedAccountingProvider.CONTENT_URI;
		Uri locationsUri = Uri.withAppendedPath(baseUri, AcceleratedAccountingProvider.PROJECTS_CONTENT_DIRECTORY);
		
		if(this.id == 0){
			ContentValues values = new ContentValues();
			values.put(AcceleratedAccountingProvider.COLUMN_PROJECT_NAME, name);
			values.put(AcceleratedAccountingProvider.COLUMN_PROJECT_DESCRIPTION, description);
			values.put(AcceleratedAccountingProvider.COLUMN_PROJECT_CUSTOMER_ID, customerId);
			Uri uri = context.getContentResolver().insert(locationsUri, values);
			id = Integer.parseInt(uri.getLastPathSegment());
		}else{
			ContentValues values = new ContentValues();
			values.put(AcceleratedAccountingProvider.COLUMN_PROJECT_NAME, name);
			values.put(AcceleratedAccountingProvider.COLUMN_PROJECT_DESCRIPTION, description);
			context.getContentResolver().update(locationsUri, values, AcceleratedAccountingProvider.COLUMN_PROJECT_ID + "==" + this.id, null);
		}
		return id;
	}
	
	public int compareTo(Project arg0) {
		return Integer.valueOf(id).compareTo(Integer.valueOf(arg0.id));
	}
	
	public boolean equals(Object o) {
		if(((Project) o) != null){
			return ((Project) o).getId() == this.id;
		}
		return false;
	}
}
