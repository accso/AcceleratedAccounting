package de.accso.accelerated.accounting.activities;



import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import de.accso.accelerated.accounting.R;

public class ShowRecordedAndAccountedHours extends TabActivity {
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final TabHost tabHost = getTabHost();       
        
        String tab1Text = getString(R.string.accounting_tabs_recorded_hours_text);
        View tabView = buildTabView(this, tab1Text);
        tabHost.addTab(tabHost.newTabSpec(tab1Text)
                .setIndicator(tabView)
                .setContent(new Intent(this, SBShowRecordedHoursDay.class)));

        String tab2Text = getString(R.string.accounting_tabs_accounted_hours_text);
        View tabView2 = buildTabView(this, tab2Text);
        tabHost.addTab(tabHost.newTabSpec(tab2Text)
                .setIndicator(tabView2)
                .setContent(new Intent(this, SBShowAccountedHoursDay.class)));
        
    }

	private View buildTabView(ShowRecordedAndAccountedHours showRecordedAndAccountedHours,	String label) {
		LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final LinearLayout ll = (LinearLayout) li.inflate(R.layout.tab_indicator, null);
		final TextView tv = (TextView) ll.findViewById(R.id.tab_tv);
		tv.setText(label);
		return ll;
	}

}
