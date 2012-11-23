package de.accso.accelerated.accounting.activities.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import de.accso.accelerated.accounting.R;

public class TileAdapter extends BaseAdapter implements ListAdapter {

	
	private static Integer[] TILES = { R.drawable.tile_img_accounting,
			R.drawable.tile_img_push, R.drawable.tile_img_fetch,
			R.drawable.tile_img_projects, R.drawable.tile_img_locations,
			R.drawable.tile_img_customers, R.drawable.tile_img_start,
			R.drawable.tile_img_stop, R.drawable.tile_img_settings}; 
	
	private static Integer[] TILE_ACTIONS = { R.string.main_tile_hours,
			R.string.main_tile_push, R.string.main_tile_fetch,
			R.string.main_tile_projects, R.string.main_tile_locations,
			R.string.main_tile_customers, R.string.main_tile_start,
			R.string.main_tile_stop, R.string.main_tile_settings}; 
	
	public static final int TILE_HOURS = 0;
	
	public static final int TILE_PUSH = 1;
	
	public static final int TILE_LOCATION = 4;
	
	public static final int TILE_CUSTOMERS = 5;
	
	public static final int TILE_START = 6;
	
	public static final int TILE_STOP = 7;
	
	private Context ctx;
	
	

	public TileAdapter(Context ctx) {
		super();
		this.ctx = ctx;
	}

	@Override
	public int getCount() {
		return TILES.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return TILES[position];
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View tileView = null;
		
		if(convertView == null) {
			LayoutInflater inflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			tileView = inflater.inflate(R.layout.main_tile, null);
		} else {
			tileView = convertView;
		}
		
		TextView textView = (TextView) tileView.findViewById(R.id.main_tile_name);
		textView.setText(TILE_ACTIONS[position]);
		
		ImageView imgView = (ImageView) tileView.findViewById(R.id.main_tile_icon);
		imgView.setImageResource(TILES[position]);
		
		return tileView;
	}

	
	
}
