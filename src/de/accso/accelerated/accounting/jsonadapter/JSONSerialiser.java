package de.accso.accelerated.accounting.jsonadapter;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.DateFormat;
import de.accso.accelerated.accounting.data.Accounting;
import de.accso.accelerated.accounting.data.Timesheet;

public class JSONSerialiser {
	
	public static final String JSON_DAY_FORMAT = "yyyy-MM-dd";
	
	
	
	public static JSONObject getJSONTimesheet(Timesheet timesheet) throws JSONException {
		JSONObject jsonTimesheet = new JSONObject();
		
		jsonTimesheet.put("year", timesheet.getYear());
		jsonTimesheet.put("month", timesheet.getMonth());
		jsonTimesheet.put("accountings", getJSONAccountings(timesheet.getAccountings()));
		
		return jsonTimesheet;
	}

	private static JSONArray getJSONAccountings(List<Accounting> accountings) throws JSONException {
		JSONArray jsonAccountings = new JSONArray();
		
		for(Accounting accounting: accountings) {
			jsonAccountings.put(getJSONAccounting(accounting));
		}
		
		return jsonAccountings;
	}

	private static JSONObject getJSONAccounting(Accounting accounting) throws JSONException {
		JSONObject jsonAccount = new JSONObject();
		
		jsonAccount.put("date", getJSONDate(accounting.getDay()));
		jsonAccount.put("duration", accounting.getDuration());
		jsonAccount.put("project", accounting.getProject());
		jsonAccount.put("project_task", accounting.getProjectTask());
		jsonAccount.put("comment", accounting.getComment());
		
		return jsonAccount;
	}

	private static String getJSONDate(Date day) {
		CharSequence jsonDayString = DateFormat.format(JSON_DAY_FORMAT, day);
		
		return jsonDayString.toString();
	}

}
