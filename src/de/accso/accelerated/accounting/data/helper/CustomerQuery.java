package de.accso.accelerated.accounting.data.helper;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import de.accso.accelerated.accounting.data.Customer;
import de.accso.accelerated.accounting.storage.AcceleratedAccountingProvider;



public class CustomerQuery {
	
	public static Cursor getAll(Context context){
		Uri uri = Uri.withAppendedPath(AcceleratedAccountingProvider.CONTENT_URI, AcceleratedAccountingProvider.CUSTOMERS_CONTENT_DIRECTORY);
	    Cursor cursor = context.getContentResolver().query(uri, null, null, null, AcceleratedAccountingProvider.COLUMN_CUSTOMER_NAME);
	    return cursor;
	}
	
	public static List<Customer> getAllAsList(Context context){
		Uri uri = Uri.withAppendedPath(AcceleratedAccountingProvider.CONTENT_URI, AcceleratedAccountingProvider.CUSTOMERS_CONTENT_DIRECTORY);
	    Cursor cursor = context.getContentResolver().query(uri, null, null, null, AcceleratedAccountingProvider.COLUMN_CUSTOMER_NAME);
	    List<Customer> customers = new ArrayList<Customer>();
	    
		if (cursor != null) {
			cursor.moveToFirst();
			
			do {
				int id = cursor.getInt(cursor.getColumnIndex(AcceleratedAccountingProvider.COLUMN_CUSTOMER_ID));
				String name = cursor.getString(cursor.getColumnIndex(AcceleratedAccountingProvider.COLUMN_CUSTOMER_NAME));
				int addressId = cursor.getInt(cursor.getColumnIndex(AcceleratedAccountingProvider.COLUMN_CUSTOMER_ADDRESS_ID));
				customers.add(new Customer(context, id, name, addressId));
			
			} while (cursor.moveToNext());
		}
	    
		return customers;
	}
	
	public static Customer getById(Context context, int id){
		Uri uri = Uri.withAppendedPath(AcceleratedAccountingProvider.CONTENT_URI, AcceleratedAccountingProvider.CUSTOMERS_CONTENT_DIRECTORY);
		Cursor cursor = context.getContentResolver().query(uri, null, AcceleratedAccountingProvider.COLUMN_CUSTOMER_ID + "==" + id, null, null);
		
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			
			String name = cursor.getString(cursor.getColumnIndex(AcceleratedAccountingProvider.COLUMN_CUSTOMER_NAME));
			int addressId = cursor.getInt(cursor.getColumnIndex(AcceleratedAccountingProvider.COLUMN_CUSTOMER_ADDRESS_ID));
			
			Customer customer = new Customer(context, id, name, addressId);
			return customer;
		}
		
		return null;
	}
}
