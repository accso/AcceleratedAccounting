package de.accso.accelerated.accounting.data;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import de.accso.accelerated.accounting.data.helper.AddressQuery;
import de.accso.accelerated.accounting.storage.AcceleratedAccountingProvider;

public class Customer {
	
	private static final String LOG_TAG = "Customer";

	private int id;	
	private String name;
	private int addressId;
	private Address address;
	private Context context;
	
	public Customer (Context context, String name, String street, String city, String zip){
		this.context = context;
		this.name = name;
		this.address = new Address(context, street, city, zip);
	}
	
	public Customer (Context context, int id, String name, int addressId){
		this.context = context;
		this.id = id;
		this.name = name;
		this.addressId = addressId;
	}
	
	public String getName(){
		return name;
	}
	
	public Address getAddress(){
		if(address == null && addressId != 0){
			address = AddressQuery.getById(context, addressId);
		}
		return address;
	}
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	} 
	
	public void setName(String name){
		this.name = name;
	}
	
	//Speichert den Kunden - wenn Id == null => Insert
	//Update auch auf die Adresse
	//return - Id vom Kunden
	public int Save(){
		Uri baseUri = AcceleratedAccountingProvider.CONTENT_URI;
		Uri locationsUri = Uri.withAppendedPath(baseUri, AcceleratedAccountingProvider.CUSTOMERS_CONTENT_DIRECTORY);
		
		if(this.id == 0){
		    int addressId = address.Save();
			ContentValues values = new ContentValues();
			values.put(AcceleratedAccountingProvider.COLUMN_CUSTOMER_ADDRESS_ID, addressId);
			values.put(AcceleratedAccountingProvider.COLUMN_CUSTOMER_NAME, name);			
			Uri uri = context.getContentResolver().insert(locationsUri, values);
			id = Integer.parseInt(uri.getLastPathSegment());
			Log.i(LOG_TAG, "Neue Kunde eingefügt: " + id);
		}else{
			address.Save();
			ContentValues values = new ContentValues();
			values.put(AcceleratedAccountingProvider.COLUMN_CUSTOMER_NAME, name);
			context.getContentResolver().update(locationsUri, values, AcceleratedAccountingProvider.COLUMN_CUSTOMER_ID + "==" + this.id, null);
		}
		
		return id;
	}

	
}


