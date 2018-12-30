package wingsoloar.com.buding_oa.homepage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.BaseObject;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.Location;
import com.tencent.lbssearch.object.param.Geo2AddressParam;
import com.tencent.lbssearch.object.result.Geo2AddressResultObject;
import com.tencent.lbssearch.object.result.Geo2AddressResultObject.ReverseAddressResult;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapActivity;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;

import java.util.List;

import wingsoloar.com.buding_oa.R;

/**
 * Created by wingsolarxu on 2017/8/2.
 */

public class Homepage_attendence_map extends MapActivity implements  TencentLocationListener
{

    MapView mapview=null;
    private TextView textView;
    float lat=39.984154f ;
    float lng=116.307490f;
    private String location;
    private ListView locationList;
    private LocationAdapter adapter;
    private String city;
    private List<ReverseAddressResult.Poi> pois;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_attendence_map);
        textView = (TextView) findViewById(R.id.attendence_position);
        mapview = (MapView) findViewById(R.id.mapview);
        locationList = findViewById(R.id.dattendence_map_listview);
        mapview.onCreate(savedInstanceState);

        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            // We don't have permission so prompt the user
//            ActivityCompat.requestPermissions(
//                    this,
//                    PERMISSIONS_LOCATION,
//                    REQUEST_EXTERNAL_STORAGE
//            );
//        }

        TencentLocationRequest tencentLocationRequest = TencentLocationRequest.create().setRequestLevel(0).setInterval(100000);

        final TencentLocationManager tencentLocationManager = TencentLocationManager.getInstance(getBaseContext());

        int error= tencentLocationManager.requestLocationUpdates(tencentLocationRequest, this);
        Log.e(error+"","zzz");
    }

    @Override
    public void onLocationChanged (TencentLocation tencentLocation,int i, String s){

        Log.e("","zzz");

        lat = (float) tencentLocation.getLatitude();
        lng = (float) tencentLocation.getLongitude();
        city = tencentLocation.getCity();

        Geo2AddressParam param = new Geo2AddressParam().location(new Location().lat(lat).lng(lng));
        param.get_poi(true);
        TencentSearch tencentSearch = new TencentSearch(getBaseContext());
        tencentSearch.geo2address(param, new HttpResponseListener() {
            @Override
            public void onSuccess(int i, BaseObject baseObject) {
                if (i == 0) {
                    Geo2AddressResultObject oj = (Geo2AddressResultObject) baseObject;
                    pois = oj.result.pois;
                    city = oj.result.address_component.city;

                    location = oj.result.address;
                    LatLng lg = new LatLng(lat, lng);

                    TencentMap tencentMap = mapview.getMap();
                    Marker marker = tencentMap.addMarker(new MarkerOptions()
                            .position(lg)
                            .title("您的位置")
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker())
                            .draggable(true));
                    tencentMap.setZoom(17);
                    tencentMap.animateTo(lg);

                    adapter = new LocationAdapter(getBaseContext(), pois);
                    locationList.setAdapter(adapter);

                    textView.setText(pois.get(0).title);

                    // searchNearby(lat,lng);

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Homepage_attendence_map.this,
                                    "定位失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(int i, String s, Throwable throwable) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Homepage_attendence_map.this,
                                "定位失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    public void onStatusUpdate (String s,int i, String s1){

    }


    @Override
    protected void onDestroy() {
        mapview.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mapview.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mapview.onResume();
        super.onResume();
    }

    @Override
    protected void onStop() {
        mapview.onStop();
        super.onStop();
    }


class LocationAdapter extends ArrayAdapter<ReverseAddressResult.Poi> {

        private List<ReverseAddressResult.Poi> list;
        private LayoutInflater inflater;

        public LocationAdapter(@NonNull Context context, List<ReverseAddressResult.Poi> list) {
            super(context, -1,list);
            this.list=list;
            this.inflater=LayoutInflater.from(getContext());
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            String title=list.get(position).title;
            String address=list.get(position).address;
            final ViewHolder holder;
            if (convertView == null) {
                convertView=inflater.inflate(R.layout.location_child,parent,false);
                holder=new ViewHolder();
                holder.title_tv=convertView.findViewById(R.id.location_child_title);
                holder.address_tv=convertView.findViewById(R.id.location_child_address);
                convertView.setTag(holder);
            }else{
                holder= (ViewHolder) convertView.getTag();
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(Homepage_attendence_map.this,Homepage_attendence_main.class);
                    intent.putExtra("location",city+" · "+holder.title_tv.getText());
                    intent.putExtra("lat",lat+"");
                    intent.putExtra("lng",lng+"");
                    startActivity(intent);
                    finish();
                }
            });
            holder.title_tv.setText(title);
            holder.address_tv.setText(address);

            return convertView;
        }
    }

    private class ViewHolder{
        TextView title_tv;
        TextView address_tv;
    }

}


