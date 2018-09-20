package com.hdpsolution.quanlychitieu.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.hdpsolution.quanlychitieu.R;
import com.hdpsolution.quanlychitieu.ads.Idelegate;
import com.hdpsolution.quanlychitieu.ads.MyAdmobController;
import com.hdpsolution.quanlychitieu.chartActivity.mChartActivity;
import com.hdpsolution.quanlychitieu.data.DatabaseHelper;

import java.text.NumberFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentThongKe extends Fragment {
    private TextView mTextMessage;
    LinearLayout spinerLayout;
    private TextView tvMonth;
    private Spinner spinnerMonth;
    private Spinner spinnerYear;
    private DatabaseHelper databaseHelper;
    private TextView tvDanhThu;
    private TextView tvChiTieu;
    private FloatingActionButton floatingActionButton;
    private TextView tvCanDoi;
    private ArrayAdapter<CharSequence> adapterYear;
    private ArrayAdapter<CharSequence> adapter;
    GetFragmentThongKe getFragmentThongKe;
    public FragmentThongKe() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            getFragmentThongKe = (GetFragmentThongKe) context;
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_thong_ke, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvDanhThu=view.findViewById(R.id.tvDoanhthu);
        floatingActionButton=view.findViewById(R.id.fChart);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyAdmobController.showAdsFullBeforeDoAction(new Idelegate() {
                    @Override
                    public void callBack(Object value, int where) {
                        Intent intent=new Intent(getActivity(), mChartActivity.class);
                        startActivity(intent);
                    }
                });


            }
        });
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        tvChiTieu=view.findViewById(R.id.tvChiTieu);
        tvCanDoi=view.findViewById(R.id.tvCandoi);
        databaseHelper=new DatabaseHelper(getActivity());
        spinerLayout=view.findViewById(R.id.spinerLayout);
        mTextMessage = (TextView)view.findViewById(R.id.message);
        tvMonth=view.findViewById(R.id.tvMonth);
        spinnerMonth=view.findViewById(R.id.month_spinner);
        spinnerYear=view.findViewById(R.id.spinerYear);
        setThongkeTatCa();
        BottomNavigationView navigation = (BottomNavigationView)view.findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        // set Spinner Month
        adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapter);
        // set Adapter Spiner Year
        adapterYear = ArrayAdapter.createFromResource(getActivity(),
                R.array.year, android.R.layout.simple_spinner_item);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(adapterYear);
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setThongkeTheoThangNam();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setThongkeTheoThangNam();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getFragmentThongKe.getFragment(this);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    spinerLayout.setVisibility(View.GONE);
                    Log.e("Tong: ", databaseHelper.getTongThu().toString() );
                    setThongkeTatCa();
                    return true;
                case R.id.navigation_dashboard:
                    spinerLayout.setVisibility(View.VISIBLE);
                    tvMonth.setVisibility(View.VISIBLE);
                    spinnerMonth.setVisibility(View.VISIBLE);
                    setThongkeTheoThangNam();
                    return true;
                case R.id.navigation_notifications:
                    spinerLayout.setVisibility(View.VISIBLE);
                    tvMonth.setVisibility(View.GONE);
                    spinnerMonth.setVisibility(View.GONE);
                    setThongkeTheoNam();
                    spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            setThongkeTheoNam();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    return true;
            }
            return false;
        }
    };

    private void setThongkeTheoNam() {
        String day=null;
        int year=Integer.parseInt(spinnerYear.getSelectedItem().toString());
                day="'"+year+"-%'";
        float tongChi= databaseHelper.getTongChiTheoThangNam(day);
        float tongThu= databaseHelper.getTongThuTheoThangNam(day);
        //formatNumber
        NumberFormat myFormat = NumberFormat.getInstance();
        myFormat.setGroupingUsed(true);
        tvDanhThu.setText(myFormat.format(tongThu)+" vnđ");
        tvChiTieu.setText(myFormat.format(tongChi)+" vnđ");
        tvCanDoi.setText((myFormat.format(tongThu-tongChi))+" vnđ");


    }

    private void setThongkeTheoThangNam() {
        String year=spinnerYear.getSelectedItem().toString();
        int month =Integer.parseInt(spinnerMonth.getSelectedItem().toString());
        String day=null;
        String m="";
        for(int i=1;i<=12;i++){
            if(month==i){
                if(month==1){
                    day = "'" +year+ "-01-%'";
                }
                else
                    if(i<10){
                        m="0"+i;
                        day = "'" +year+"-"+m+ "-%'";
                    }
                    else
                day="'"+year+"-"+month+"%'";
            }
        }
        float tongChi= databaseHelper.getTongChiTheoThangNam(day);
        float tongThu= databaseHelper.getTongThuTheoThangNam(day);
        NumberFormat myFormat = NumberFormat.getInstance();
        myFormat.setGroupingUsed(true);
        tvDanhThu.setText(myFormat.format(tongThu)+" vnđ");
        tvChiTieu.setText(myFormat.format(tongChi)+" vnđ");
        tvCanDoi.setText((myFormat.format(tongThu-tongChi))+" vnđ");
    }

    public void setThongkeTatCa() {
        float tt=databaseHelper.getTongThu();
        float tc=databaseHelper.getTongChi();
        NumberFormat myFormat = NumberFormat.getInstance();
        myFormat.setGroupingUsed(true);
        tvDanhThu.setText(myFormat.format(tt)+" vnđ");
        tvChiTieu.setText(myFormat.format(tc)+" vnđ");
        tvCanDoi.setText((myFormat.format(tt-tc))+" vnđ");
    }
    public interface GetFragmentThongKe{
        void getFragment(FragmentThongKe fragmentThongKe);
    }
    public void setGetFragmentThongKe(GetFragmentThongKe getFragmentThongKe) {
        this.getFragmentThongKe = getFragmentThongKe;
    }
}
