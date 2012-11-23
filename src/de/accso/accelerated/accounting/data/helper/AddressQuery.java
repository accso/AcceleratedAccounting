package de.accso.accelerated.accounting.data.helper;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import de.accso.accelerated.accounting.data.Address;
import de.accso.accelerated.accounting.storage.AcceleratedAccountingProvider;

public class AddressQuery {
	
	public static Address getById(Context context, int id){
		Uri uri = Uri.withAppendedPath(AcceleratedAccountingProvider.CONTENT_URI, AcceleratedAccountingProvider.ADDRESSES_CONTENT_DIRECTORY);
		Cursor cursor = context.getContentResolver().query(uri, null, AcceleratedAccountingProvider.COLUMN_ADDRESS_ID+ "==" + id, null, null);
		
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			
			String street = cursor.getString(cursor.getColumnIndex(AcceleratedAccountingProvider.COLUMN_ADDRESS_STREET));
			String city = cursor.getString(cursor.getColumnIndex(AcceleratedAccountingProvider.COLUMN_ADDRESS_CITY));
			String zip = cursor.getString(cursor.getColumnIndex(AcceleratedAccountingProvider.COLUMN_ADDRESS_ZIP));
			
			Address address = new Address(context, id, street, city, zip);
			return address;
		}
		
		return null;
	}

}
