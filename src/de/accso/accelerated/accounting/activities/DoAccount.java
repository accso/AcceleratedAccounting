package de.accso.accelerated.accounting.activities;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import de.accso.accelerated.accounting.R;
import de.accso.accelerated.accounting.data.Project;
import de.accso.accelerated.accounting.data.helper.ProjectHelper;
import de.accso.accelerated.accounting.util.DateUtil;


/**
 * TODO: SB Spinner sind noch total kaputt. Gescheite CursorAdapter mit Anbindung zum Provider
 * und Checkboxen implementieren!
 * 
 * 
 * @author Braun
 *
 */
public class DoAccount extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.do_account);

		// TODO: SB buttons and handlers
//		Button okButton = (Button) findViewById(R.id.ok_button);
//		okButton.setOnClickListener(okButtonListener);
//		
//		Button cancelButton = (Button) findViewById(R.id.cancel_button);
//		cancelButton.setOnClickListener(cancelButtonListener);
		CharSequence dateString = DateFormat.format(DateUtil.GERMAN_DAY_FORMAT_LONG, new Date());
		TextView dayText = (TextView) findViewById(R.id.do_accounting_day_text);
		dayText.setText(dateString);
		
		EditText hoursEdit = (EditText) findViewById(R.id.do_accounting_hours_edit);
		hoursEdit.setText("8.0");
		
		fillProjectSpinner();
		fillAccountsSpinner();
	}
	
	
	private void fillProjectSpinner(){
		Spinner spinner = (Spinner) this.findViewById(R.id.do_accounting_project_spinner);
	    List<Project> projects = ProjectHelper.getAllAsList(this);
	    
	    spinner.setAdapter(createProjectAdapter(projects));
	}
	
	
	// TODO: SB is only stubbed!
	private void fillAccountsSpinner() {
		Spinner spinner = (Spinner) this.findViewById(R.id.do_accounting_account_spinner);
	    
	    
	    spinner.setAdapter(createAccountsAdapter());
	}
	
	// TODO: SB is only stubbed!
	private SpinnerAdapter createAccountsAdapter() {
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, R.layout.simple_spinner_item);
		
		adapter.add("Entwicklung");
		
		
		return adapter;
	}

	// TODO: SB is only stubbed!
	private SpinnerAdapter createProjectAdapter(final List<Project> projects){
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, R.layout.simple_spinner_item);
		
		adapter.add("Android-Projekt");
		
		return adapter;
	}
}
