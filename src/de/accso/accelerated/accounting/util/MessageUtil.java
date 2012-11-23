package de.accso.accelerated.accounting.util;

import java.lang.reflect.Field;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class MessageUtil {
	
	public static void ShowMessage(Context context, String text){
		 Toast t = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		 t.setGravity(Gravity.CENTER, 0, 0);
		 t.show();
	}	
}
