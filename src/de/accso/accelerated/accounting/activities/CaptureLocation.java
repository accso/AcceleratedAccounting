package de.accso.accelerated.accounting.activities;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import de.accso.accelerated.accounting.R;
import de.accso.accelerated.accounting.data.helper.ColorHelper;
import de.accso.accelerated.accounting.services.CaptureLocationService;

/**
 * Capture the current position and store it in the db.
 * 
 * @author Susanne Braun
 *
 */
public class CaptureLocation extends AcceleratedAccounting {
	
	private List<de.accso.accelerated.accounting.data.Color> colors;
	
	protected static final String LOG_TAG = "CaptureLocation";
	
	
	public static final int LOCATION_TYPE_WORKPLACE = 1;
	
	public static final int LOCATION_TYPE_FREETIME = 2;
	
	
	protected static final int DIALOG_CONFIRMATION_MESSAGE = 1;
	
	/**
	 * Time in milliseconds after which the confirmation dialog will
	 * be dismissed.
	 * TODO: SB this should be configurable!
	 */
	protected static final int TIME_TO_DISMISS_CONFIRMATION_DIALOG = 2500;
	
	
	private Handler timedTasksHandler = new Handler();
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.capture_location);

		// buttons and handlers
		Button okButton = (Button) findViewById(R.id.ok_button);
		okButton.setOnClickListener(okButtonListener);
		
		Button cancelButton = (Button) findViewById(R.id.cancel_button);
		cancelButton.setOnClickListener(cancelButtonListener);
		
		fillColorSpinner();
	}

	protected Dialog onCreateDialog(int id) {
	    Dialog dialog = null;
	    
	    switch(id) {
	    
	    case DIALOG_CONFIRMATION_MESSAGE:
	        dialog = this.createConfirmaitonMessageDialog();
	        break;
	   
	    default:
	        dialog = null;
	    }
	    
	    return dialog;
	}


	private Dialog createConfirmaitonMessageDialog() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		
		dialogBuilder
			.setMessage(getResources().getString(R.string.dialog_confirmation_message))
			.setCancelable(false);
		
		return dialogBuilder.create();
	}
	
	
	/**
	 * OK Button (Capture location) listener
	 */
	private OnClickListener okButtonListener = new OnClickListener() {

		public void onClick(View v) {
			// do something when the button is clicked
			Log.d(LOG_TAG, "OK Button of has been pressed");
			
			Intent intent = new Intent(CaptureLocation.this, CaptureLocationService.class);
			CaptureLocation.this.setUserInputValues(intent);
			startService(intent);
			
			showDialog(DIALOG_CONFIRMATION_MESSAGE);
			CaptureLocation.this.timedTasksHandler.postDelayed(CaptureLocation.this.dismissConfirmationDialog, TIME_TO_DISMISS_CONFIRMATION_DIALOG);
		}
	};
	
	/**
	 * Cancel Button (Capture location) listener
	 */
	private OnClickListener cancelButtonListener = new OnClickListener() {

		public void onClick(View v) {
			// do something when the button is clicked
			Log.d(LOG_TAG, "Cancel Button of has been pressed");
			// close the activity
			CaptureLocation.this.finish();
		}
	};
	
	/**
	 * Task used to dismiss the confirmation dialog after 
	 * short period of time.
	 */
	private Runnable dismissConfirmationDialog = new Runnable() {
		 public void run() {
			 // dismiss dialog
			 dismissDialog(DIALOG_CONFIRMATION_MESSAGE);
			// close the activity
			CaptureLocation.this.finish();
		 }
	};
	

	protected void setUserInputValues(Intent intent) {
		if(intent == null) {
			return;
		}
		
		EditText locationNameInput = (EditText) findViewById(R.id.location_name_input);
		String locationName = locationNameInput.getText().toString();
		intent.putExtra(CaptureLocationService.EXTRAS_LOCATION_NAME_KEY, locationName);
		Log.d(LOG_TAG, "Location name is "+locationName);
		
		Spinner locationTypeSpinner = (Spinner) findViewById(R.id.loction_type_spinner);
		int selectedIndex = locationTypeSpinner.getSelectedItemPosition();
		String locationType = getResources().getStringArray(R.array.location_types)[selectedIndex];
		intent.putExtra(CaptureLocationService.EXTRAS_LOCATION_TYPE_KEY, locationType);
		Log.d(LOG_TAG, "Selected location type is "+selectedIndex);
		
		Spinner colorSpinner = (Spinner) findViewById(R.id.location_color_spinner);
		int selectedIndexColorSpinner = colorSpinner.getSelectedItemPosition();
		String color = colors.get(selectedIndexColorSpinner).getCode();
		intent.putExtra(CaptureLocationService.EXTRAS_LOCATION_COLOR_NAME_KEY, color);
		Log.d(LOG_TAG, "Selected color is "+ color);
	}

	private void fillColorSpinner(){
		Spinner s = (Spinner) this.findViewById(R.id.location_color_spinner);
	    colors = ColorHelper.getAvailableColorsForLocation(this);
	    ArrayAdapter<de.accso.accelerated.accounting.data.Color> adapter = createColorsAdapter();
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s.setAdapter(adapter);
	}
	
	private ArrayAdapter<de.accso.accelerated.accounting.data.Color> createColorsAdapter(){
		
		return new ArrayAdapter<de.accso.accelerated.accounting.data.Color>(this, android.R.layout.simple_spinner_item, colors){
    		@Override
			public View getDropDownView(int position, View convertView,	ViewGroup parent) {
    			return createView(position, convertView, parent, true);
			}

			@Override
    	    public View getView(int position, View convertView, ViewGroup parent) {
    			return createView(position, convertView, parent, false);
    		};
    		
    		private View createView(int position, View convertView, ViewGroup parent, boolean isDropDownView){
    			View row; 		
    			if (null == convertView) {			
    				this.getContext();
					LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
    	            row = vi.inflate(R.layout.spinner_color_item_view, null);
				} else {			
					row = convertView;		
				} 		
    			
    			de.accso.accelerated.accounting.data.Color color = getItem(position);
    			
    			TextView tvColorCode = (TextView) row.findViewById(R.id.spinner_color_code);
    			TextView tvColorName = (TextView) row.findViewById(R.id.spinner_color_text);
    			
    			String t = color.getCode();    			
    			
    			if(tvColorCode != null){
    				tvColorCode.setBackgroundColor(Color.parseColor(t));
    			}
    			if(tvColorName != null){
    				tvColorName.setText(color.getName());
    				if(isDropDownView){
    					tvColorName.setTextSize(20);
    				}
    			}
    			
    			if(isDropDownView){
    				row.setPadding(15, 15, 15, 15);
    			}
    			return row;
    		}
    	};
	}
	
}