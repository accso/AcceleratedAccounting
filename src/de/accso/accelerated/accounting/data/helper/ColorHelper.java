package de.accso.accelerated.accounting.data.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.accso.accelerated.accounting.data.Color;
import de.accso.accelerated.accounting.storage.AcceleratedAccountingProvider;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class ColorHelper {

	public static List<Color> getAvailableColorsForLocation(Context context){
		Uri uri = Uri.withAppendedPath(AcceleratedAccountingProvider.CONTENT_URI, AcceleratedAccountingProvider.AVAILABLE_COLORS_FOR_LOCATION_CONTENT_DIRECTORY);
		String [] projection = new String [] { AcceleratedAccountingProvider.COLOR_TABLE + "." + AcceleratedAccountingProvider.COLUMN_COLOR_CODE, 
											   AcceleratedAccountingProvider.COLOR_TABLE + "." + AcceleratedAccountingProvider.COLUMN_COLOR_NAME };
		Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
		List<Color> colors = new ArrayList<Color>();
	    
		if (cursor != null) {
			cursor.moveToFirst();
			
			do {
				String colorName = cursor.getString(cursor.getColumnIndex(AcceleratedAccountingProvider.COLUMN_COLOR_NAME));
				String colorCode = cursor.getString(cursor.getColumnIndex(AcceleratedAccountingProvider.COLUMN_COLOR_CODE));
				colors.add(new Color(colorName, colorCode));
			} while (cursor.moveToNext());
		}
	    
		return colors;
	}
}
