package com.hdpsolution.quanlychitieu.chartActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.hdpsolution.quanlychitieu.R;
import com.hdpsolution.quanlychitieu.ads.MyBaseActivityWithAds;
import com.hdpsolution.quanlychitieu.comom.Commom;
import com.hdpsolution.quanlychitieu.data.DatabaseHelper;
import com.hdpsolution.quanlychitieu.fragment.MonthYearPickerDialog;
import com.hdpsolution.quanlychitieu.fragment.YearPickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ChartActivity extends MyBaseActivityWithAds implements DatePickerDialog.OnDateSetListener, View.OnClickListener {
    private BarChart barChart;
    private ArrayList<BarEntry> lstKhoanThu;
    private ArrayList<BarEntry> lstKhoanChi;
    private ArrayList<BarEntry> lstKhoanThuBackup;
    private ArrayList<BarEntry> lstKhoanChiBackup;
    private DatabaseHelper databaseHelper;
    private TextView tvStart;
    private TextView tvEnd;
    private ImageView imgStart;
    private ImageView imgEnd;
    private MaterialDialog dialog;
    private Button btnAll;
    private Button btnT;
    private Button btnC;
    private Button btnOk;
    private Button btnCancel;
    private RadioButton rbtnCustom;
    private RadioButton rbtnMonth;
    private RadioButton rbtnYear;
    private ArrayList<String> months;
    private int daysInMonth=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        databaseHelper = new DatabaseHelper(this);
        getSupportActionBar().setTitle(this.getString(R.string.tab_statistical));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Calendar calendar = Calendar.getInstance();
        // Lấy ra năm - tháng - ngày hiện tại
        int year = calendar.get(calendar.YEAR);
        lstKhoanThu = new ArrayList<>();
        lstKhoanChi = new ArrayList<>();
        lstKhoanThuBackup=new ArrayList<>();
        lstKhoanChiBackup=new ArrayList<>();
        barChart = findViewById(R.id.barChart);
        btnAll=findViewById(R.id.btnAll);
        rbtnCustom=findViewById(R.id.rbtnCustom);
        rbtnMonth=findViewById(R.id.rbtnMonth);
        rbtnYear=findViewById(R.id.rbtnYear);
        btnC=findViewById(R.id.btnC);
        btnT=findViewById(R.id.btnT);
        //setOnclick
        btnAll.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnT.setOnClickListener(this);
        barChart.setScaleEnabled(true);
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setPinchZoom(false);
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getXAxis().setDrawLabels(true);
        barChart.animateY(2000);
        barChart.getLegend().setEnabled(false);   // Hide the legend
        barChart.setHighlightFullBarEnabled(false);
        months=new ArrayList<>();
        months.add("T1");
        months.add("T2");
        months.add("T3");
        months.add("T4");
        months.add("T5");
        months.add("T6");
        months.add("T7");
        months.add("T8");
        months.add("T9");
        months.add("T10");
        months.add("T11");
        months.add("T12");
        getDataEchYear(year);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //do whatever
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private String onMonthSelected(int year,int month){
        String mMonth="";
        for(int i=1;i<=12;i++) {
            if (i == month) {
                if (i == 1) {
                    mMonth = "'" + year + "-01%'";
                }
                else
                    if(i<10){
                        mMonth = "'" + year + "-" + "0"+i+"%'";
                    }
                    else {
                        mMonth="'" + year + "-" + i+"%'";
                    }
            }
        }
        Log.e("month: ",  mMonth);
        return mMonth;
    }
    private void getDataEchMonth(int mYear,int mMonth) {
        barChart.refreshDrawableState();
        barChart.removeAllViews();
        lstKhoanThu.clear();
        lstKhoanChi.clear();
        lstKhoanChiBackup.clear();
        lstKhoanThuBackup.clear();
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(mYear, mMonth-1, 1);
        daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        ArrayList<String> months=new ArrayList<>();
        for (int i=1;i<=daysInMonth;i++){
            months.add(i+"");
        }
        String sMonth=onMonthSelected(mYear,mMonth);
        databaseHelper.getTongThuTheoThang(sMonth);
        databaseHelper.getTongChiTheoThang(sMonth);
                    for (int j = 1; j <=daysInMonth; j++) {
                        String check=sMonth.substring(0,sMonth.length()-2);
                        String checkMonth=check.concat("-"+j+"%'");
                        Log.e("mMonth", checkMonth);
                        String date=String.valueOf(j);

                        int keyT= Commom.lstDateT.indexOf(date);
                        int keyC= Commom.lstDateC.indexOf(date);
                        Log.e("checkey", keyT+"/"+keyC+"");
                        if(keyT!=-1) {
                            int checkIndex = checkMonth.lastIndexOf("-");
                            if (checkMonth.indexOf(date, checkIndex) != -1) {

                                float number = Float.parseFloat(Commom.lstNumberT.get(keyT));
                                lstKhoanThu.add(new BarEntry((float) j, number));
                                lstKhoanThuBackup.add(new BarEntry((float) j, number));

                            }
                        }
                            else
                                lstKhoanThu.add(new BarEntry((float)j,0));
                                lstKhoanThuBackup.add(new BarEntry((float)j,0));
                        if(keyC!=-1) {
                            int checkIndex = checkMonth.lastIndexOf("-");
                            if (checkMonth.indexOf(date, checkIndex) != -1) {
                                float number = Float.parseFloat(Commom.lstNumberC.get(keyC));
                                lstKhoanChi.add(new BarEntry((float) j, number));
                                lstKhoanChiBackup.add(new BarEntry((float) j, number));
                            }
                        }
                            else
                                lstKhoanChi.add(new BarEntry((float)j,0));
                                lstKhoanChiBackup.add(new BarEntry((float)j,0));
                        }
        BarDataSet barDataSet= new BarDataSet(lstKhoanThu,this.getString(R.string.tab_profit));
        barDataSet.setColor(this.getResources().getColor(R.color.coloBarsetProfit));
        //
        XAxis mAxis=barChart.getXAxis();
        MyXAxisValueFormat format=new MyXAxisValueFormat(months);
        mAxis.setValueFormatter(format);
        mAxis.setAxisMinimum(0);
        mAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        BarDataSet barDataSet1= new BarDataSet(lstKhoanChi,this.getString(R.string.tab_spend));
        barDataSet1.setColor(ColorTemplate.getHoloBlue());
        if(lstKhoanThu.isEmpty() && lstKhoanChi.isEmpty()){
            Toast.makeText(this, R.string.dataNull, Toast.LENGTH_SHORT).show();
        }
        BarData barData=new BarData(barDataSet,barDataSet1);
        float groupSpace=0.06f;
        float barSpace=0.02f;
        float barWidth=0.35f;
        barChart.notifyDataSetChanged();
        barChart.invalidate();
        barChart.setData(barData);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(months.size());
        barData.setBarWidth(barWidth);
        barChart.groupBars(0,groupSpace,barSpace);

    }
    private void getDataEchYear(int mYear){
        barChart.removeAllViews();
        barChart.refreshDrawableState();
        lstKhoanThu.clear();
        lstKhoanChi.clear();
        lstKhoanThuBackup.clear();
        lstKhoanChiBackup.clear();
        String m="";
        for(int i=1;i<=12;i++){
            if(i==1) {
                //

                String day = null;
                day = "'" +mYear+ "-01-%'";
                float tongChi = databaseHelper.getTongChiTheoThangNam(day);
                float tongThu = databaseHelper.getTongThuTheoThangNam(day);
                Log.e("yyyyhbthang1:", tongChi + "  " + tongThu + "");
                lstKhoanThu.add(new BarEntry((float)i, tongThu));
                lstKhoanChi.add(new BarEntry((float)i, tongChi));
                lstKhoanThuBackup.add(new BarEntry((float)i-1, tongThu));
                lstKhoanChiBackup.add(new BarEntry((float)i-1, tongChi));
            }
            else {
                if(i<10){
                    m = "0" + i;
                    String day = null;
                    day = "'" +mYear+ "-"+m+"%'";
                    float tongChi = databaseHelper.getTongChiTheoThangNam(day);
                    float tongThu = databaseHelper.getTongThuTheoThangNam(day);
                    Log.e("yyyyhbthang:", tongChi + "  " + tongThu + "");
                    lstKhoanThu.add(new BarEntry(((float) i-1), tongThu));
                    lstKhoanChi.add(new BarEntry(((float) i-1), tongChi));
                    lstKhoanThuBackup.add(new BarEntry(((float) i-1), tongThu));
                    lstKhoanChiBackup.add(new BarEntry(((float) i-1), tongChi));
                }
                else {
                    String day = null;
                    day = "'" +mYear+ "-"+i+"%'";
                    float tongChi = databaseHelper.getTongChiTheoThangNam(day);
                    float tongThu = databaseHelper.getTongThuTheoThangNam(day);
                    Log.e("yyyyhbthang:", tongChi + "  " + tongThu + "");
                    lstKhoanThu.add(new BarEntry(((float) i-1), tongThu));
                    lstKhoanChi.add(new BarEntry(((float) i-1), tongChi));
                    lstKhoanThuBackup.add(new BarEntry(((float) i-1), tongThu));
                    lstKhoanChiBackup.add(new BarEntry(((float) i-1), tongChi));
                }


            }
        }
        BarDataSet barDataSet= new BarDataSet(lstKhoanThu,"Khoan thu");
        barDataSet.setColor(ColorTemplate.rgb("#fff"));
        //
        XAxis mAxis=barChart.getXAxis();
        MyXAxisValueFormat format=new MyXAxisValueFormat(months);
        mAxis.setValueFormatter(format);
        mAxis.setAxisMinimum(0);
        mAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        BarDataSet barDataSet1= new BarDataSet(lstKhoanChi,"Khoan chi");
        barDataSet1.setColor(ColorTemplate.getHoloBlue());
        BarData barData=new BarData(barDataSet,barDataSet1);
        barChart.notifyDataSetChanged();
        barChart.invalidate();
        float groupSpace=0.1f;
        float barSpace=0.03f;
        float barWidth=0.3f;
        barChart.setData(barData);
        barData.setBarWidth(barWidth);
        barChart.groupBars(0,groupSpace,barSpace);


    }
    private void getDataCustomDay(String dayBegin, String dayEnd){
        barChart.refreshDrawableState();
        barChart.removeAllViews();
        lstKhoanThu.clear();
        lstKhoanChi.clear();
        lstKhoanChiBackup.clear();
        lstKhoanThuBackup.clear();
        Log.e("getDataCustomDay:",dayBegin + "."+dayEnd );
        databaseHelper.getTongThuTheoNgay(dayBegin,dayEnd);
        databaseHelper.getTongChiTheoNgay(dayBegin,dayEnd);
        for (int j = 0; j <Commom.lstNumberT.size(); j++) {
                    float number = Float.parseFloat(Commom.lstNumberT.get(j));
                    lstKhoanThu.add(new BarEntry((float) j+1, number));
                    lstKhoanThuBackup.add(new BarEntry((float) j+1, number));
                }
        for (int j = 0; j <Commom.lstNumberC.size(); j++) {
            float numberC = Float.parseFloat(Commom.lstNumberC.get(j));
            lstKhoanChi.add(new BarEntry((float) j+1, numberC));
            lstKhoanChiBackup.add(new BarEntry((float) j+1, numberC));
        }
        ArrayList<String> months=new ArrayList<>();
        for (int i=1;i<=Commom.lstDateT.size();i++){
            months.add(i+"");
            Log.e("Datee", Commom.lstDateT.size()+"" );
        }
        BarDataSet barDataSet= new BarDataSet(lstKhoanThu,this.getString(R.string.tab_profit));
        barDataSet.setColor(ColorTemplate.rgb("#fff"));
        //
        XAxis mAxis=barChart.getXAxis();
        MyXAxisValueFormat format=new MyXAxisValueFormat(months);
        mAxis.setValueFormatter(format);
        mAxis.setAxisMinimum(0);
        mAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        BarDataSet barDataSet1= new BarDataSet(lstKhoanChi,this.getString(R.string.tab_spend));
        barDataSet1.setColor(ColorTemplate.getHoloBlue());
        if(lstKhoanThu.isEmpty() && lstKhoanChi.isEmpty()){
            Toast.makeText(this, R.string.dataNull, Toast.LENGTH_SHORT).show();
        }
        BarData barData=new BarData(barDataSet,barDataSet1);
        float groupSpace=0.06f;
        float barSpace=0.02f;
        float barWidth=0.35f;
        barChart.notifyDataSetChanged();
        barChart.invalidate();
        barChart.setData(barData);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(months.size());
        barData.setBarWidth(barWidth);
        barChart.groupBars(0,groupSpace,barSpace);

    }
    public void pickMonth(View view) {
        MonthYearPickerDialog pd = new MonthYearPickerDialog();
        pd.setListener(this);
        pd.show(getFragmentManager(), "MonthYearPickerDialog");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date=year+"/"+month+"/"+dayOfMonth;
        if(date.equals(year+"/0/0")){
            getDataEchYear(year);
        }
        else {
            getDataEchMonth(year,month);
        }

    }

    public void pickYear(View view) {
        YearPickerDialog pd = new YearPickerDialog();
        pd.setListenerYear(this);
        pd.show(getFragmentManager(), "YearPickerDialog");
    }

    public void customDay(View view) {
        Calendar calendar = Calendar.getInstance();
        // Lấy ra năm - tháng - ngày hiện tại
        final int year = calendar.get(calendar.YEAR);
        final int month = calendar.get(calendar.MONTH);
        final int day = calendar.get(calendar.DAY_OF_MONTH);
        // Lấy ngày tháng  từ 1 tuần trước

        // show dialog
        dialog=new MaterialDialog.Builder(this)
                .customView(R.layout.customdialogpicdate, true)
                .show();
        View viewDialog=dialog.getCustomView();
        tvStart=viewDialog.findViewById(R.id.tvStart);
        tvEnd=viewDialog.findViewById(R.id.tvEnd);
        SimpleDateFormat fromUser = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-d");
        String inputString =day+"/"+(month+1)+"/"+year;
        try {

            String reformattedStr = myFormat.format(fromUser.parse(inputString));
            String mDay="";
            if(day<10){
                int i=reformattedStr.lastIndexOf("-");
                int newday= Integer.parseInt(reformattedStr.substring(i+1,reformattedStr.length()));
                String sdefault=reformattedStr.substring(0,i);
                mDay=sdefault+"-0"+newday;
                }
                else
                {
                    mDay=reformattedStr;
                tvStart.setText(mDay);
                tvEnd.setText(mDay);
            }
            Log.e("Date ", reformattedStr );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        imgStart=viewDialog.findViewById(R.id.imgStart);
        imgEnd=viewDialog.findViewById(R.id.imgEnd);
        btnCancel=viewDialog.findViewById(R.id.btnCancel);
        btnOk=viewDialog.findViewById(R.id.btnOk);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataDayCustom();
                dialog.dismiss();
            }
        });
        // set onclick
        imgStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupDatePicker(year,month,day,tvStart);
            }
        });
        imgEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupDatePicker(year,month,day,tvEnd);
            }
        });

    }
    // set data custom day
    private void setDataDayCustom() {
        String start="";
        String end="";
        String soure=tvStart.getText().toString();
        String soureEnd=tvEnd.getText().toString();
        int i=soure.lastIndexOf("-");
        int newday= Integer.parseInt(soure.substring(i+1,soure.length()));
        String sdefault=soure.substring(0,i);
        if(newday<10){
            start="'"+sdefault+"-0"+newday+"'";
        }
        else
        {
            start="'"+sdefault+"-"+newday+"'";
        }
        int j=soureEnd.lastIndexOf("-");
        int newdayE= Integer.parseInt(soureEnd.substring(i+1,soureEnd.length()));
        String sdefaultE=soureEnd.substring(0,i);
        if(newdayE<10){
            end="'"+sdefaultE+"-0"+newdayE+"'";
        }
        else
        {
            end="'"+sdefaultE+"-"+newdayE+"'";
        }
        Log.e("concat", start+"" +"/"+end);
//        String start="'"+tvStart.getText().toString()+"'";
//        String end="'"+tvEnd.getText().toString()+"'";
        getDataCustomDay(start,end);
    }

    private void setupDatePicker(int yearNow, int monthNow, final int dayNow,final TextView tv) {
        DatePickerDialog datePickerDialog= new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                SimpleDateFormat fromUser = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-d");
                String inputString =dayOfMonth+"/"+(month+1)+"/"+year;
                try {

                    String reformattedStr = myFormat.format(fromUser.parse(inputString));
                    tv.setText(reformattedStr);
                    Log.e("Date ", reformattedStr );
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        },yearNow,monthNow,dayNow);
        datePickerDialog.show();
    }
    private ArrayList<BarEntry> retunAllT(){
        ArrayList<BarEntry> lstThu=new ArrayList();
        for (int i=0;i<lstKhoanThuBackup.size();i++){
            float x=lstKhoanThuBackup.get(i).getX();
            float y=lstKhoanThuBackup.get(i).getY();
        lstThu.add(new BarEntry(x,y));
        }
        return lstThu;
    }
    private ArrayList<BarEntry> retunAllC(){
        ArrayList<BarEntry> lstThu=new ArrayList();
        for (int i=0;i<lstKhoanChiBackup.size();i++){
            float x=lstKhoanChiBackup.get(i).getX();
            float y=lstKhoanChiBackup.get(i).getY();
            lstThu.add(new BarEntry(x,y));
        }
        return lstThu;
    }
    private ArrayList<BarEntry> retunAllmonthT(){
        ArrayList<BarEntry> lstThu=new ArrayList();
        for (int i=0;i<lstKhoanThuBackup.size();i++){
            float x=lstKhoanThuBackup.get(i).getX();
            float y=lstKhoanThuBackup.get(i).getY();
            lstThu.add(new BarEntry(x,y));
        }
        return lstThu;
    }
    private ArrayList<BarEntry> retunAllmonthC(){
        ArrayList<BarEntry> lstThu=new ArrayList();
        for (int i=0;i<lstKhoanChiBackup.size();i++){
            float x=lstKhoanChiBackup.get(i).getX();
            float y=lstKhoanChiBackup.get(i).getY();
            lstThu.add(new BarEntry(x,y));
        }
        return lstThu;
    }
    private ArrayList<BarEntry> retunAllDayT(){
        ArrayList<BarEntry> lstThu=new ArrayList();
        for (int i=0;i<lstKhoanThuBackup.size();i++){
            float x=lstKhoanThuBackup.get(i).getX();
            float y=lstKhoanThuBackup.get(i).getY();
            lstThu.add(new BarEntry(x,y));
        }
        return lstThu;
    }
    private ArrayList<BarEntry> retunAllDayC(){
        ArrayList<BarEntry> lstThu=new ArrayList();
        for (int i=0;i<lstKhoanChiBackup.size();i++){
            float x=lstKhoanChiBackup.get(i).getX();
            float y=lstKhoanChiBackup.get(i).getY();
            lstThu.add(new BarEntry(x,y));
        }
        return lstThu;
    }
    private ArrayList<String> getAlldayInMonth(){
        ArrayList<String> day=new ArrayList<>();
        for (int i=0;i<daysInMonth;i++){
            day.add(i+1+"");
        }
        return day;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAll:
                if(rbtnYear.isChecked()) {
                    thongKeThuChiTheoNam();
                }
                if(rbtnMonth.isChecked()){
                    thongKeThuChiTheoThang();
                }
                if(rbtnCustom.isChecked()){
                    thongKeThuChiTheoNgay();
                }
                break;
            case R.id.btnT:
                if(rbtnYear.isChecked()) {
                    thongKeThuTheoNam();
                }
                if(rbtnMonth.isChecked()){
                    thongKeThuTheoThang();
                }
                if(rbtnCustom.isChecked()){
                    thongKeThuTheoNgay();
                }
                break;
            case R.id.btnC:
                if(rbtnYear.isChecked()) {
                    thongKeChitheoNam();
                }
                if(rbtnMonth.isChecked()){
                    thongKeChiTheoThang();
                }
                if(rbtnCustom.isChecked()){
                thongKeChiTheoNgay();
                }
                break;
        }
    }
    private void thongKeThuChiTheoNam(){
        if (lstKhoanThu.size() == 0 || lstKhoanChi.size() == 0) {
            lstKhoanThu.clear();
            lstKhoanChi.clear();
            lstKhoanThu = retunAllT();
            lstKhoanChi = retunAllC();
            BarDataSet barDataSet = new BarDataSet(lstKhoanThu, this.getString(R.string.tab_profit));
            barDataSet.setColor(this.getResources().getColor(R.color.coloBarsetProfit));
            //
            XAxis mAxis = barChart.getXAxis();
            MyXAxisValueFormat format = new MyXAxisValueFormat(months);
            mAxis.setValueFormatter(format);
            mAxis.setAxisMinimum(0);
            mAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
            BarDataSet barDataSet1 = new BarDataSet(lstKhoanChi, this.getString(R.string.tab_spend));
            barDataSet1.setColor(ColorTemplate.getHoloBlue());
            BarData barData = new BarData(barDataSet, barDataSet1);
            barChart.notifyDataSetChanged();
            barChart.invalidate();
            float groupSpace = 0.06f;
            float barSpace = 0.02f;
            float barWidth = 0.3f;
            barChart.setData(barData);
            barData.setBarWidth(barWidth);
            barChart.groupBars(0, groupSpace, barSpace);
        } else {
            BarDataSet barDataSet = new BarDataSet(lstKhoanThu, this.getString(R.string.tab_profit));
            barDataSet.setColor(this.getResources().getColor(R.color.coloBarsetProfit));
            //
            XAxis mAxis = barChart.getXAxis();
            MyXAxisValueFormat format = new MyXAxisValueFormat(months);
            mAxis.setValueFormatter(format);
            mAxis.setAxisMinimum(0);
            mAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
            BarDataSet barDataSet1 = new BarDataSet(lstKhoanChi, this.getString(R.string.tab_spend));
            barDataSet1.setColor(ColorTemplate.getHoloBlue());
            BarData barData = new BarData(barDataSet, barDataSet1);
            barChart.notifyDataSetChanged();
            barChart.invalidate();
            float groupSpace = 0.06f;
            float barSpace = 0.02f;
            float barWidth = 0.3f;
            barChart.setData(barData);
            barData.setBarWidth(barWidth);
            barChart.groupBars(0, groupSpace, barSpace);

        }
    }
    private void thongKeThuTheoNam(){
        lstKhoanChi.clear();
        if (lstKhoanThu.size() == 0) {
            lstKhoanThu = retunAllT();
            BarDataSet barDataSet = new BarDataSet(lstKhoanThu, this.getString(R.string.tab_profit));
            barDataSet.setColor(this.getResources().getColor(R.color.coloBarsetProfit));
            BarData data = new BarData(barDataSet);
            data.setBarWidth(0.35f);
            barChart.setData(data);
            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new MyXAxisValueFormat(months));
            barChart.notifyDataSetChanged();
            barChart.invalidate();
        } else {
            BarDataSet barDataSet = new BarDataSet(lstKhoanThu, this.getString(R.string.tab_profit));
            barDataSet.setColor(this.getResources().getColor(R.color.coloBarsetProfit));
            BarData data = new BarData(barDataSet);
            data.setBarWidth(0.35f);
            barChart.setData(data);
            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new MyXAxisValueFormat(months));
            barChart.notifyDataSetChanged();
            barChart.invalidate();
        }
        BarDataSet barDataSet = new BarDataSet(lstKhoanThu, this.getString(R.string.tab_profit));
        barDataSet.setColor(this.getResources().getColor(R.color.coloBarsetProfit));
        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.35f);
        barChart.setData(data);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormat(months));
        barChart.notifyDataSetChanged();
        barChart.invalidate();
        Log.e("cheksize", lstKhoanChiBackup.size() + "");
    }
    private void thongKeChitheoNam(){
        lstKhoanThu.clear();
        if (lstKhoanChi.size() == 0) {
            lstKhoanChi = retunAllC();
            BarDataSet barDataSetC = new BarDataSet(lstKhoanChi, this.getString(R.string.tab_spend));
            barDataSetC.setColor(ColorTemplate.getHoloBlue());
            BarData dataC = new BarData(barDataSetC);
            dataC.setBarWidth(0.35f);
            barChart.setData(dataC);
            XAxis xAxisx = barChart.getXAxis();
            xAxisx.setValueFormatter(new MyXAxisValueFormat(months));
            barChart.notifyDataSetChanged();
            barChart.invalidate();
        } else {
            BarDataSet barDataSetC = new BarDataSet(lstKhoanChi, this.getString(R.string.tab_spend));
            barDataSetC.setColor(ColorTemplate.getHoloBlue());
            BarData dataC = new BarData(barDataSetC);
            dataC.setBarWidth(0.35f);
            barChart.setData(dataC);
            XAxis xAxisx = barChart.getXAxis();
            xAxisx.setValueFormatter(new MyXAxisValueFormat(months));
            barChart.notifyDataSetChanged();
            barChart.invalidate();
        }
        BarDataSet barDataSetC = new BarDataSet(lstKhoanChi, this.getString(R.string.tab_spend));
        barDataSetC.setColor(ColorTemplate.getHoloBlue());
        BarData dataC = new BarData(barDataSetC);
        dataC.setBarWidth(0.35f);
        barChart.setData(dataC);
        XAxis xAxisx = barChart.getXAxis();
        xAxisx.setValueFormatter(new MyXAxisValueFormat(months));
        barChart.notifyDataSetChanged();
        barChart.invalidate();
        Log.e("cheksize", lstKhoanThuBackup.size() + "");
    }
    private void thongKeThuChiTheoThang(){
        ArrayList<String> day=new ArrayList<>();
        lstKhoanThu = retunAllmonthT();
        lstKhoanChi = retunAllmonthC();
        day=getAlldayInMonth();
        Log.e("thongKeThuChiTheoThang", day.size()+"" );
        BarDataSet barDataSet= new BarDataSet(lstKhoanThu,this.getString(R.string.tab_profit));
        barDataSet.setColor(this.getResources().getColor(R.color.coloBarsetProfit));
        //
        XAxis mAxis=barChart.getXAxis();
        MyXAxisValueFormat format=new MyXAxisValueFormat(day);
        mAxis.setValueFormatter(format);
        mAxis.setAxisMinimum(0);
        mAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        BarDataSet barDataSet1= new BarDataSet(lstKhoanChi,this.getString(R.string.tab_spend));
        barDataSet1.setColor(ColorTemplate.getHoloBlue());
        if(lstKhoanThu.isEmpty() && lstKhoanChi.isEmpty()){
            Toast.makeText(this, this.getString(R.string.dataNull), Toast.LENGTH_SHORT).show();
        }
        BarData barData=new BarData(barDataSet,barDataSet1);
        float groupSpace=0.06f;
        float barSpace=0.02f;
        float barWidth=0.35f;
        barChart.notifyDataSetChanged();
        barChart.invalidate();
        barChart.setData(barData);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(months.size());
        barData.setBarWidth(barWidth);
        barChart.groupBars(0,groupSpace,barSpace);
    }
    private void thongKeThuTheoThang(){
        lstKhoanChi.clear();
        ArrayList<String> day=new ArrayList<>();
        day=getAlldayInMonth();
        Log.e("thongKeThuChiTheoThang1", day.size()+"" );
        if (lstKhoanThu.size() == 0) {
            lstKhoanThu = retunAllmonthT();
            BarDataSet barDataSet = new BarDataSet(lstKhoanThu, this.getString(R.string.tab_profit));
            barDataSet.setColor(this.getResources().getColor(R.color.coloBarsetProfit));
            BarData data = new BarData(barDataSet);
            data.setBarWidth(0.3f);
            barChart.setData(data);
            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new MyXAxisValueFormat(day));
            barChart.notifyDataSetChanged();
            barChart.invalidate();
        } else {
            BarDataSet barDataSet = new BarDataSet(lstKhoanThu, this.getString(R.string.tab_profit));
            barDataSet.setColor(this.getResources().getColor(R.color.coloBarsetProfit));
            BarData data = new BarData(barDataSet);
            data.setBarWidth(0.3f);
            barChart.setData(data);
            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new MyXAxisValueFormat(day));
            barChart.notifyDataSetChanged();
            barChart.invalidate();
        }
        BarDataSet barDataSet = new BarDataSet(lstKhoanThu, this.getString(R.string.tab_profit));
        barDataSet.setColor(this.getResources().getColor(R.color.coloBarsetProfit));
        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.3f);
        barChart.setData(data);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormat(day));
        barChart.notifyDataSetChanged();
        barChart.invalidate();
        Log.e("cheksize", lstKhoanChiBackup.size() + "");
    }
    private void thongKeChiTheoThang(){
        ArrayList<String> day=new ArrayList<>();
        day=getAlldayInMonth();
        Log.e("thongKeThuChiTheoThang2", day.size()+"" );
        lstKhoanThu.clear();
        if (lstKhoanChi.size() == 0) {
            lstKhoanChi = retunAllmonthC();
            BarDataSet barDataSetC = new BarDataSet(lstKhoanChi, this.getResources().getString(R.string.tab_spend));
            barDataSetC.setColor(ColorTemplate.getHoloBlue());
            BarData dataC = new BarData(barDataSetC);
            dataC.setBarWidth(0.3f);
            barChart.setData(dataC);
            XAxis xAxisx = barChart.getXAxis();
            xAxisx.setValueFormatter(new MyXAxisValueFormat(day));
            barChart.notifyDataSetChanged();
            barChart.invalidate();
        } else {
            BarDataSet barDataSetC = new BarDataSet(lstKhoanChi, this.getResources().getString(R.string.tab_spend));
            barDataSetC.setColor(ColorTemplate.getHoloBlue());
            BarData dataC = new BarData(barDataSetC);
            dataC.setBarWidth(0.3f);
            barChart.setData(dataC);
            XAxis xAxisx = barChart.getXAxis();
            xAxisx.setValueFormatter(new MyXAxisValueFormat(day));
            barChart.notifyDataSetChanged();
            barChart.invalidate();
        }
        BarDataSet barDataSetC = new BarDataSet(lstKhoanChi, this.getResources().getString(R.string.tab_spend));
        barDataSetC.setColor(ColorTemplate.getHoloBlue());
        BarData dataC = new BarData(barDataSetC);
        dataC.setBarWidth(0.3f);
        barChart.setData(dataC);
        XAxis xAxisx = barChart.getXAxis();
        xAxisx.setValueFormatter(new MyXAxisValueFormat(day));
        barChart.notifyDataSetChanged();
        barChart.invalidate();
        Log.e("cheksize", lstKhoanThuBackup.size() + "");
    }
    private void thongKeThuChiTheoNgay(){
        ArrayList<String> day=new ArrayList<>();
        lstKhoanThu = retunAllDayT();
        lstKhoanChi = retunAllDayC();
        for (int i=1;i<=Commom.lstDateT.size();i++){
            day.add(i+"");
            Log.e("Datee", Commom.lstDateT.size()+"" );
        }
        Log.e("thongKeThuChiTheoThang", day.size()+"" );
        BarDataSet barDataSet= new BarDataSet(lstKhoanThu,this.getResources().getString(R.string.tab_profit));
        barDataSet.setColor(this.getResources().getColor(R.color.coloBarsetProfit));
        //
        XAxis mAxis=barChart.getXAxis();
        MyXAxisValueFormat format=new MyXAxisValueFormat(day);
        mAxis.setValueFormatter(format);
        mAxis.setAxisMinimum(0);
        mAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        BarDataSet barDataSet1= new BarDataSet(lstKhoanChi,this.getResources().getString(R.string.tab_spend));
        barDataSet1.setColor(ColorTemplate.getHoloBlue());
        if(lstKhoanThu.isEmpty() && lstKhoanChi.isEmpty()){
            Toast.makeText(this, this.getResources().getString(R.string.dataNull), Toast.LENGTH_SHORT).show();
        }
        BarData barData=new BarData(barDataSet,barDataSet1);
        float groupSpace=0.06f;
        float barSpace=0.02f;
        float barWidth=0.35f;
        barChart.notifyDataSetChanged();
        barChart.invalidate();
        barChart.setData(barData);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(months.size());
        barData.setBarWidth(barWidth);
        barChart.groupBars(0,groupSpace,barSpace);
    }
    private void thongKeThuTheoNgay(){
        lstKhoanChi.clear();
        ArrayList<String> day=new ArrayList<>();
        for (int i=1;i<=Commom.lstDateT.size();i++){
            day.add(i+"");
            Log.e("Datee", Commom.lstDateT.size()+"" );
        }
        Log.e("thongKeThuChiTheoThang1", day.size()+"" );
        if (lstKhoanThu.size() == 0) {
            lstKhoanThu = retunAllDayT();
            BarDataSet barDataSet = new BarDataSet(lstKhoanThu, this.getString(R.string.tab_profit));
            barDataSet.setColor(this.getResources().getColor(R.color.coloBarsetProfit));
            BarData data = new BarData(barDataSet);
            data.setBarWidth(0.3f);
            barChart.setData(data);
            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new MyXAxisValueFormat(day));
            barChart.notifyDataSetChanged();
            barChart.invalidate();
        } else {
            BarDataSet barDataSet = new BarDataSet(lstKhoanThu, this.getString(R.string.tab_profit));
            barDataSet.setColor(ColorTemplate.rgb("#fff"));
            BarData data = new BarData(barDataSet);
            data.setBarWidth(0.3f);
            barChart.setData(data);
            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new MyXAxisValueFormat(day));
            barChart.notifyDataSetChanged();
            barChart.invalidate();
        }
        BarDataSet barDataSet = new BarDataSet(lstKhoanThu, this.getString(R.string.tab_profit));
        barDataSet.setColor(ColorTemplate.rgb("#fff"));
        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.3f);
        barChart.setData(data);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormat(day));
        barChart.notifyDataSetChanged();
        barChart.invalidate();
        Log.e("cheksize", lstKhoanChiBackup.size() + "");
    }
    private void thongKeChiTheoNgay(){
        lstKhoanThu.clear();
        ArrayList<String> day=new ArrayList<>();
        for (int i=1;i<=Commom.lstNumberC.size();i++){
            day.add(i+"");
            Log.e("Datee", Commom.lstDateT.size()+"" );
        }
        Log.e("thongKeThuChiTheoThang1", day.size()+"" );
        if (lstKhoanChi.size() == 0) {
            lstKhoanChi = retunAllDayC();
            BarDataSet barDataSet = new BarDataSet(lstKhoanChi, this.getString(R.string.tab_spend));
            barDataSet.setColor(ColorTemplate.getHoloBlue());
            BarData data = new BarData(barDataSet);
            data.setBarWidth(0.3f);
            barChart.setData(data);
            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new MyXAxisValueFormat(day));
            barChart.notifyDataSetChanged();
            barChart.invalidate();
        } else {
            BarDataSet barDataSet = new BarDataSet(lstKhoanChi, this.getString(R.string.tab_spend));
            barDataSet.setColor(ColorTemplate.getHoloBlue());
            BarData data = new BarData(barDataSet);
            data.setBarWidth(0.3f);
            barChart.setData(data);
            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new MyXAxisValueFormat(day));
            barChart.notifyDataSetChanged();
            barChart.invalidate();
        }
        BarDataSet barDataSet = new BarDataSet(lstKhoanChi, this.getString(R.string.tab_spend));
        barDataSet.setColor(ColorTemplate.getHoloBlue());
        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.3f);
        barChart.setData(data);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormat(day));
        barChart.notifyDataSetChanged();
        barChart.invalidate();
        Log.e("cheksize", lstKhoanChiBackup.size() + "");
    }

}
