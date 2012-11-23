package de.accso.accelerated.accounting.data;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import de.accso.accelerated.accounting.storage.AcceleratedAccountingProvider;

public class Address {
	
	private static final String LOG_TAG = "Address";
	
	private int id;
	private String street;
	private String city;
	private String zip;
	private Context context;
	
	public Address(Context context, String street, String city, String zip){
		this.context = context;
		this.street = street;
		this.city = city;
		this.zip = zip;
	}
	
	public Address(Context context, int id, String street, String city, String zip){
		this.context = context;
		this.id = id;
		this.street = street;
		this.city = city;
		this.zip = zip;
	}
	
	public String getStreet(){
		return street;
	}
	
	public String getCity(){
		return city;
	}
	
	public String getZip(){
		return zip;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public void setStreet(String street){
		this.street = street;
	}
	
	public void setCity(String city){
		this.city = city;
	}
	
	public void setZip(String zip){
		this.zip = zip;
	}
	
	//Speichert die Adresse - wenn Id == null => Insert
	public int Save(){
		Uri baseUri = AcceleratedAccountingProvider.CONTENT_URI;
		Uri locationsUri = Uri.withAppendedPath(baseUri, AcceleratedAccountingProvider.ADDRESSES_CONTENT_DIRECTORY);
		
		if(this.id == 0){
			ContentValues values = new ContentValues();
			values.put(AcceleratedAccountingProvider.COLUMN_ADDRESS_STREET, street);
			values.put(AcceleratedAccountingProvider.COLUMN_ADDRESS_CITY, city);
			values.put(AcceleratedAccountingProvider.COLUMN_ADDRESS_ZIP, zip);
		    Uri uri = context.getContentResolver().insert(locationsUri, values);
		    id = Integer.parseInt(uri.getLastPathSegment());
		    Log.i(LOG_TAG, "Neue Adresse eingefügt, Id: " + id);
		} else{
			ContentValues values = new ContentValues();
			values.put(AcceleratedAccountingProvider.COLUMN_ADDRESS_STREET, street);
			values.put(AcceleratedAccountingProvider.COLUMN_ADDRESS_CITY, city);
			values.put(AcceleratedAccountingProvider.COLUMN_ADDRESS_ZIP, zip);
			context.getContentResolver().update(locationsUri, values, AcceleratedAccountingProvider.COLUMN_ADDRESS_ID + "==" + this.id, null);
		}		
		return id;
	}
	
}
