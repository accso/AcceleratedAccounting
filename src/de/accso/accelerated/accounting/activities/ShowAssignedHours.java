package de.accso.accelerated.accounting.activities;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ShowAssignedHours extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
			super.onCreate(savedInstanceState);
			TextView textview = new TextView(this);
	        textview.setText("Aufgezeichnete Stunden");
	        setContentView(textview);

			
			
	}
	
}