package f10.net.androidtoolboxdemo.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import f10.net.androidtoolbox.forms.Cell;
import f10.net.androidtoolbox.forms.FormFragment;
import f10.net.androidtoolbox.forms.PickerDialogCell;
import f10.net.androidtoolbox.forms.TextViewCell;
import f10.net.androidtoolboxdemo.R;
import f10.net.androidtoolboxdemo.SQLite.City;

public class CityDetailFragments extends FormFragment {

    private City city;

    private  final static int NameField = 1;
    private  final static int CoordField = 2;


    @Override
    public void configureForm() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.city = (City) bundle.getSerializable("CitySelected");
        }

        addRow(new TextViewCell(NameField, this , "Name", this.city.getName()));


        insertSectionHeader("Coordinate");
        addRow(new CoordinatePickerCell(CoordField, this , "Coordinate", new LatLng(city.getLatitude(), city.getLongitude())));

    }

    @Override
    public void onCellDataChanged(Cell cell, Object newValue) {
        switch (cell.getTag())
        {
            case CoordField:
                LatLng latLng = (LatLng) newValue;
                city.setLongitude(latLng.longitude);
                city.setLatitude(latLng.latitude);
                break;
        }

        // Maybe save here into persistent store
    }


    private class CoordinatePickerCell extends PickerDialogCell<LatLng> implements GoogleMap.OnCameraMoveListener, View.OnClickListener {

        private GoogleMap googleMap;
        private LatLng value;
        private Dialog dialog;

        public CoordinatePickerCell(int tag, FormFragment form, String label, LatLng coord) {
            super(tag, form, label, coord);
        }

        @Override
        protected Dialog onCreateDialog(Context c, LatLng value) {
            final Context context = c;
            final LatLng coord = value;
            final CoordinatePickerCell lis = this;
            Dialog dialog = new Dialog(c);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.map_dialog);

            MapView mv = (MapView) dialog.findViewById(R.id.mapView);
            mv.onCreate(new Bundle());
            mv.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    lis.googleMap = googleMap;
                    MapsInitializer.initialize(context);
                    int zoom = 7;
                    googleMap.addMarker(new MarkerOptions().position(coord));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, zoom));
                    googleMap.setOnCameraMoveListener(lis);
                }
            });
            mv.onResume();

            Button bt = (Button) dialog.findViewById(R.id.buttonSet);
            bt.setOnClickListener(this);
            this.dialog = dialog;

            return dialog;
        }

        @Override
        protected String onUpdatingValueText(LatLng value) {
            return value.toString();
        }

        @Override
        public void onCameraMove() {
            if(this.googleMap != null) this.value = googleMap.getCameraPosition().target;
        }


        @Override
        public void onClick(View view) {
            if(dialog != null) dialog.dismiss();
            setValue(value, true);
        }
    }
}
