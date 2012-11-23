package de.accso.accelerated.accounting.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import de.accso.accelerated.accounting.R;
import de.accso.accelerated.accounting.data.AccountedHoursOfDay;
import de.accso.accelerated.accounting.data.Accounting;

public class SBShowAccountedHoursDay extends Activity {

	private TableLayout accountedHoursListTable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_accounted_hours_day_sb);

		initView();
	}

	private void initView() {
		accountedHoursListTable = (TableLayout) findViewById(R.id.show_accounted_hours_list_table);
		accountedHoursListTable.setScrollContainer(false);
		accountedHoursListTable.setClickable(true);

		addAccountedHours();
	}

	private void addAccountedHours() {
		List<AccountedHoursOfDay> accHours = this.createDummyData();

		int index = 0;
		for (AccountedHoursOfDay accHour : accHours) {
			accountedHoursListTable.addView(getSeparatorRow(accHour
					.getDayString()));
			accountedHoursListTable.addView(getBorder());

			TableRow row = this.getRow(accHour);
			accountedHoursListTable.addView(row);

			if (index < accHours.size() - 1) {
				accountedHoursListTable.addView(getBorder());
			}
			index++;
		}
	}

	private TableRow getRow(AccountedHoursOfDay accHours) {
		LayoutInflater vi = getLayoutInflater();

		TableRow tableRow = (TableRow) vi.inflate(
				R.layout.show_accounted_hours_day_table_row, null);
		ImageView indicatorView = (ImageView) tableRow
				.findViewById(R.id.accounted_hour_indicator);
		indicatorView.setBackgroundDrawable(accHours.getIndicator());

		LinearLayout accountedHoursList = (LinearLayout) tableRow
				.findViewById(R.id.accounted_hours_day_list);

		if (accHours.getAccountedHours() == null
				|| accHours.getAccountedHours().size() == 0) {
			
			TextView noAccountingsText = (TextView) vi.inflate(R.layout.show_accounted_hours_day_no_accountings, null);
			noAccountingsText.setText(getString(R.string.show_accounted_hours_day_no_accountings));

			accountedHoursList.addView(noAccountingsText);
		} else {
			for (Accounting accHour : accHours.getAccountedHours()) {
				View cellview = vi.inflate(
						R.layout.show_accounted_hours_day_table_cell, null);

				TextView durationView = (TextView) cellview
						.findViewById(R.id.accounted_hour_duration_text);
				durationView.setText(String.valueOf(accHour.getDuration())
						+ " h");

				TextView accountView = (TextView) cellview
						.findViewById(R.id.accounted_hour_account_text);
				accountView.setText(accHour.getProjectTask());
				TextView projectNameView = (TextView) cellview
						.findViewById(R.id.acounted_hour_project_text);
				projectNameView.setText(accHour.getProject());
				TextView commentView = (TextView) cellview
						.findViewById(R.id.acounted_hour_comment_text);
				commentView.setText(accHour.getComment());

				accountedHoursList.addView(cellview);
			}
		}

		return tableRow;
	}

	private TableRow getSeparatorRow(String dateString) {
		LayoutInflater vi = getLayoutInflater();
		TableRow tableRow = (TableRow) vi.inflate(
				R.layout.show_accounted_hours_day_table_separator_row, null);

		TextView locationText = (TextView) tableRow
				.findViewById(R.id.accounted_hours_separator_text);
		locationText.setText(dateString);

		return tableRow;
	}

	private View getBorder() {
		LayoutInflater vi = getLayoutInflater();
		View borderView = vi.inflate(R.layout.table_list_border, null);

		return borderView;
	}

	private List<AccountedHoursOfDay> createDummyData() {

		List<AccountedHoursOfDay> accHours = new ArrayList<AccountedHoursOfDay>();

		AccountedHoursOfDay accHour1 = new AccountedHoursOfDay();
		accHour1.setDayString("Heute - 8 h");
		accHour1.setIndicator(getResources().getDrawable(
				R.drawable.recorded_hours_indicator_full));
		

		List<Accounting> accHoursToday = new ArrayList<Accounting>();
		Accounting hour1 = new Accounting();
		hour1.setDuration(8);
		hour1.setProject("Android-Projekt");
		hour1.setProjectTask("ENTW");
		hour1.setComment("Location-Monitor-Feature");
		accHoursToday.add(hour1);
		accHour1.setAccountedHours(accHoursToday);
		accHours.add(accHour1);

		return accHours;
	}

}
