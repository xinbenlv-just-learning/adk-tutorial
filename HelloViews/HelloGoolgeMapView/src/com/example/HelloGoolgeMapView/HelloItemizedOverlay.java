package com.example.HelloGoolgeMapView;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class HelloItemizedOverlay extends ItemizedOverlay<OverlayItem>
{
	private Context mContext;
    private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
    public HelloItemizedOverlay(Drawable defaultMarker) {
      super(boundCenterBottom(defaultMarker));
    }
    public void addOverlay(OverlayItem overlay) {
        mOverlays.add(overlay);
        populate();
    }

@Override
protected OverlayItem createItem(int i) {
  return mOverlays.get(i);
}

@Override
public int size() {
  return mOverlays.size();
}

public HelloItemizedOverlay(Drawable defaultMarker, Context context) {
  super(defaultMarker);
  mContext = context;
}


@Override
protected boolean onTap(int index) {
  OverlayItem item = mOverlays.get(index);
  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
  dialog.setTitle(item.getTitle());
  dialog.setMessage(item.getSnippet());
  dialog.show();
  return true;
}





}
