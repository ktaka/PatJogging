package jp.itnav.patjogger;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeshHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeshHistoryFragment extends Fragment implements GetLogs.GetLogListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final LatLng OdakaLocation = new LatLng(37.562762, 140.996247);

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GoogleMap map;


    public MeshHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MeshHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MeshHistoryFragment newInstance(String param1, String param2) {
        MeshHistoryFragment fragment = new MeshHistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mesh_history, container, false);

        map = ((SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map)).getMap();
        LatLng location = OdakaLocation;
        map.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(location, 15.0f)));

        SeekBar seekBar = (SeekBar)v.findViewById(R.id.seekBar);
        seekBar.setMax(2);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("SeekBar", "progress=" + progress);
                //BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(DemoMeshImgs[progress]);
                if (overlay != null) {
                    overlay.remove();
                }
                GroundOverlayOptions options = new GroundOverlayOptions();
                options.image(meshImgList.get(progress));
                options.anchor(0.5f, 0.5f);
                options.position(OdakaLocation, 3000.0f, 3000.0f);
                overlay = map.addGroundOverlay(options);
                overlay.setTransparency(0.5f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        GetLogs getLogs = new GetLogs(this);

        setOverlayMesh(seekBar, OdakaLocation);

        return v;
    }

    private static int DemoMeshImgs[] = {
            R.drawable.mesh_dummy,
            R.drawable.mesh_dummy_1,
            R.drawable.mesh_dummy_2
    };

    GroundOverlay overlay = null;
    ArrayList<BitmapDescriptor> meshImgList;

    void setOverlayMesh(SeekBar seekBar, LatLng location) {
        GroundOverlayOptions options = new GroundOverlayOptions();
        meshImgList = new ArrayList<BitmapDescriptor>();
        for (int i = 0; i < DemoMeshImgs.length; i++) {
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(DemoMeshImgs[i]);
            meshImgList.add(bitmap);
        }
        options.image(meshImgList.get(0));
        options.anchor(0.5f, 0.5f);
        options.position(location, 3000.0f, 3000.0f);
        overlay = map.addGroundOverlay(options);
        overlay.setTransparency(0.5f);
    }

    public void onResult(List<GetLogs.VisitLog> result, int requestType) {

    }

    public void onUserHistory(List<LatLng> result) {

    }

    void buildMesh() {

    }
}
