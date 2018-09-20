package com.hdpsolution.quanlychitieu.chartActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.Cartesian;
import com.anychart.anychart.CartesianSeriesRangeColumn;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.EnumsAlign;
import com.anychart.anychart.LegendLayout;
import com.anychart.anychart.Mapping;
import com.anychart.anychart.Pie;
import com.anychart.anychart.Set;
import com.anychart.anychart.ValueDataEntry;
import com.anychart.anychart.chart.common.Event;
import com.anychart.anychart.chart.common.ListenersInterface;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.hdpsolution.quanlychitieu.MainActivity;
import com.hdpsolution.quanlychitieu.R;
import com.hdpsolution.quanlychitieu.comom.Commom;
import com.hdpsolution.quanlychitieu.data.DatabaseHelper;
import com.hdpsolution.quanlychitieu.fragment.MonthYearPickerDialog;
import com.hdpsolution.quanlychitieu.fragment.YearPickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class mChartActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private DatabaseHelper databaseHelper;
    private TextView tvStart;
    private TextView tvEnd;
    private ImageView imgStart;
    private ImageView imgEnd;
    private MaterialDialog dialog;
    private Button btnOk;
    private Button btnCancel;
    private int daysInMonth=0;
    private List<DataEntry> data;
    private AnyChartView anyChartView;
    private Cartesian cartesian;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mchart);
        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        databaseHelper = new DatabaseHelper(this);
        // Lấy ra năm - tháng - ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(calendar.YEAR);
        anyChartView = findViewById(R.id.any_chart_view);
        // barcahr
        getSupportActionBar().setTitle(this.getString(R.string.tab_statistical));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //
        getDataEchYear(year);
    }

    private class CustomDataEntry extends DataEntry {
        public CustomDataEntry(String x, Number edinburgHigh, Number edinburgLow, Number londonHigh, Number londonLow) {
            setValue("x", x);
            setValue("edinburgHigh", edinburgHigh);
            setValue("edinburgLow", edinburgLow);
            setValue("londonHigh", londonHigh);
            setValue("londonLow", londonLow);
        }
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
    //////////////////////////
    private String onMonthSelected(int year,int month){
        String mMonth="";
        for(int i=1;i<=12;i++) {
            if (i == month) {
                if (i == 1) {
                    mMonth = "'" + year + "-01-";
                }
                else
                    if(i<10){
                        mMonth = "'" + year + "-" + "0"+i+"-";
                    }
                    else {
                        mMonth="'" + year + "-" + i+"-";
                    }
            }
        }
        Log.e("month: ",  mMonth);
        return mMonth;
    }
    private void getDataEchMonth(int mYear,int mMonth) {
        cartesian = AnyChart.cartesian();
        cartesian.setTitle("Biểu đồ thống kê chi tiêu cá nhân (vnđ)");
        data=new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(mYear, mMonth-1, 1);
        daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        data.clear();
        String m="";
        for(int i=1;i<=daysInMonth;i++){
            String sMonth=onMonthSelected(mYear,mMonth);
                if(i<10){
                    String request=sMonth+"0"+i+"%'";
                    Log.e("concat", request);
                    float tongChi = databaseHelper.getTongChiTheoThangNam(request);
                    float tongThu = databaseHelper.getTongThuTheoThangNam(request);
                    Log.e("yyyyhbthang:", tongChi + "  " + tongThu + "");
                    data.add(new CustomDataEntry(""+i,0,tongChi,0,tongThu));
                }
                else {
                    String request=sMonth+i+"%'";
                    Log.e("concat", request);
                    float tongChi = databaseHelper.getTongChiTheoThangNam(request);
                    float tongThu = databaseHelper.getTongThuTheoThangNam(request);
                    Log.e("yyyyhbthang:", tongChi + "  " + tongThu + "");
                    data.add(new CustomDataEntry(""+i,0,tongChi,0,tongThu));
                }


            }
        Set set = new Set(data);
        Mapping profit = set.mapAs("{ x: 'x', high: 'londonHigh', low: 'londonLow' }");
        Mapping spend = set.mapAs("{ x: 'x', high: 'edinburgHigh', low: 'edinburgLow' }");
        CartesianSeriesRangeColumn columnProfit = cartesian.rangeColumn(profit);
        columnProfit.setName(getResources().getString(R.string.tab_profit));
        columnProfit.setColor("blue");
        CartesianSeriesRangeColumn columnSpend = cartesian.rangeColumn(spend);
        columnSpend.setName(getResources().getString(R.string.tab_spend));
        columnSpend.setColor("orange");
        cartesian.setXAxis(true);
        cartesian.setYAxis(true);
        cartesian.getYScale()
                .setMinimum(4d);

        cartesian.setLegend(true);

        cartesian.setYGrid(true)
                .setYMinorGrid(true);

        cartesian.getTooltip().setTitleFormat("{%SeriesName} ({%x})");
        anyChartView.setChart(cartesian);

    }
    private void getDataEchYear(int mYear){
        cartesian = AnyChart.cartesian();
        cartesian.setTitle("Biểu đồ thống kê chi tiêu cá nhân (vnđ)");
        data=new ArrayList<>();
        String m="";
        for(int i=1;i<=12;i++){
            if(i==1) {
                //

                String day = null;
                day = "'" +mYear+ "-01-%'";
                float tongChi = databaseHelper.getTongChiTheoThangNam(day);
                float tongThu = databaseHelper.getTongThuTheoThangNam(day);
                data.add(new CustomDataEntry("T"+i,0,tongChi,0,tongThu));
            }
            else {
                if(i<10){
                    m = "0" + i;
                    String day = null;
                    day = "'" +mYear+ "-"+m+"%'";
                    float tongChi = databaseHelper.getTongChiTheoThangNam(day);
                    float tongThu = databaseHelper.getTongThuTheoThangNam(day);
                    Log.e("yyyyhbthang:", tongChi + "  " + tongThu + "");
                    data.add(new CustomDataEntry("T"+i,0,tongChi,0,tongThu));
                }
                else {
                    String day = null;
                    day = "'" +mYear+ "-"+i+"%'";
                    float tongChi = databaseHelper.getTongChiTheoThangNam(day);
                    float tongThu = databaseHelper.getTongThuTheoThangNam(day);
                    Log.e("yyyyhbthang:", tongChi + "  " + tongThu + "");
                    data.add(new CustomDataEntry("T"+i,0,tongChi,0,tongThu));
                }


            }
        }
        Set set = new Set(data);
        Mapping profit = set.mapAs("{ x: 'x', high: 'londonHigh', low: 'londonLow' }");
        Mapping spend = set.mapAs("{ x: 'x', high: 'edinburgHigh', low: 'edinburgLow' }");
        CartesianSeriesRangeColumn columnProfit = cartesian.rangeColumn(profit);
        columnProfit.setName(getResources().getString(R.string.tab_profit));
        columnProfit.setColor("blue");
        CartesianSeriesRangeColumn columnSpend = cartesian.rangeColumn(spend);
        columnSpend.setName(getResources().getString(R.string.tab_spend));
        columnSpend.setColor("orange");
        cartesian.setXAxis(true);
        cartesian.setYAxis(true);
        cartesian.getYScale()
                .setMinimum(4d);

        cartesian.setLegend(true);

        cartesian.setYGrid(true)
                .setYMinorGrid(true);

        cartesian.getTooltip().setTitleFormat("{%SeriesName} ({%x})");
        anyChartView.setChart(cartesian);
       // alertDialog.dismiss();

    }
    private void getDataCustomDay(final String dayBegin,final String dayEnd){
        ArrayList<Float> lstThu=new ArrayList<>();
        ArrayList<Float> lstChi=new ArrayList<>();
        int sumProfit=0;
        int sumSpend=0;
        lstThu= databaseHelper.getTongThuTheoNgay(dayBegin,dayEnd);
        lstChi= databaseHelper.getTongChiTheoNgay(dayBegin,dayEnd);
        for (int i=0;i<lstThu.size();i++){
            sumProfit+=lstThu.get(i);
        }
        for (int i=0;i<lstChi.size();i++){
            sumSpend+=lstChi.get(i);
        }
        Pie pie = AnyChart.pie();
        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                //start activity
               // Intent intent =new Intent(mChartActivity.this, DetailActivity.class);
                
            }
        });
        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry(getResources().getString(R.string.tab_spend), sumSpend));
        data.add(new ValueDataEntry(getResources().getString(R.string.tab_profit), sumProfit));
        pie.setData(data);
        pie.setTitle("Biểu đồ thống kê thu chi cá nhân (vnđ)");

        pie.getLabels().setPosition("outside");

        pie.getLegend().getTitle().setEnabled(true);
        pie.getLegend().getTitle()
                .setText("Thu Chi")
                .setPadding(0d, 0d, 10d, 0d);

        pie.getLegend()
                .setPosition("center-bottom")
                .setItemsLayout(LegendLayout.HORIZONTAL)
                .setAlign(EnumsAlign.CENTER);
        anyChartView.setChart(pie);
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
        daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
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
            String mDayStart="";
            String mDayEnd="";
                int i=reformattedStr.lastIndexOf("-");
                int newday= Integer.parseInt(reformattedStr.substring(i+1,reformattedStr.length()));
                String sdefault=reformattedStr.substring(0,i);
                mDayStart=sdefault+"-01";
                mDayEnd=sdefault+"-"+daysInMonth;
                tvStart.setText(mDayStart);
                tvEnd.setText(mDayEnd);
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
}
