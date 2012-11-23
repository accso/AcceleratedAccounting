package de.accso.accelerated.accounting.map;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

/**
 * TODO: SB javadoc
 * 
 * @author Susanne Braun
 *
 */
public class LocationMarkerOverlay extends ItemizedOverlay<OverlayItem> {
	
	private ArrayList<OverlayItem> overlays;
	
	private Context ctx;

	public LocationMarkerOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		
		this.overlays  = new ArrayList<OverlayItem>(); 
		this.ctx = context;
	}
	
	
	
	public void addOverlay(OverlayItem overlay) {
	    overlays.add(overlay);
	    populate();
	}

	@Override
	protected OverlayItem createItem(int index) {
		if(index < this.overlays.size()) {
			return overlays.get(index);
		}
		
		return null;
	}

	@Override
	public int size() {
		return overlays.size();
	}

	@Override
	protected boolean onTap(int index) {
		if (index < this.overlays.size()) {
			OverlayItem item = overlays.get(index);

			AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);
			dialog.setTitle(item.getTitle());
			dialog.setMessage(item.getSnippet());
			dialog.show();

			return true;
		}
		
		return false;
	}
	

}
