package com.hdpsolution.quanlychitieu.adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hdpsolution.quanlychitieu.R;
import com.hdpsolution.quanlychitieu.comom.Commom;
import com.hdpsolution.quanlychitieu.data.DatabaseHelper;
import com.hdpsolution.quanlychitieu.interFace.OnItemClickListener;
import com.hdpsolution.quanlychitieu.model.ListItemAddProg;
import com.hdpsolution.quanlychitieu.model.ThuChi;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class KhoanthuAdapter extends RecyclerView.Adapter<KhoanThuHolder> {
    private Context mContext;
    private List<ThuChi> mThuChiList;
    private MaterialDialog dialog;
    private Button btnSave;
    TextView optionOne;
    TextView optionTwo;
    TextView optionThree;
    private Button btnDelete;
    private Button btnCancel;
    private EditText edtNote;
    private EditText edtNumber;
    private TextView tvDate;
    private ImageView imgCalender;
    Spinner spinerDanhmuc;
    ArrayList<ListItemAddProg> mLstItemAddProgs;
    private DatabaseHelper databaseHelper;
    private int id;



    public KhoanthuAdapter(Context context, List<ThuChi> thuChiList , ArrayList<ListItemAddProg> listItemAddProgs) {
        this.mContext = context;
        this.mThuChiList = thuChiList;
        this.mLstItemAddProgs=listItemAddProgs;
    }

    @NonNull
    @Override
    public KhoanThuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_item_khoanthu,parent,false);

        return new KhoanThuHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final KhoanThuHolder holder, final int position) {
        databaseHelper=new DatabaseHelper(mContext);
        int [] uriImg={R.drawable.icon,R.drawable.ic_home_black_24dp,R.drawable.ic_gif_black_24dp,R.drawable.ic_grade_black_24dp,R.drawable.ic_work_black_24dp,R.drawable.ic_home_black_24dp
                ,R.drawable.percen,R.drawable.interest,R.drawable.wheel
                ,R.drawable.ic_keyboard_black_24dp};
        NumberFormat myFormat = NumberFormat.getInstance();
        myFormat.setGroupingUsed(true);

        String sNumber = mThuChiList.get(position).getNumber();

        if (sNumber.equals(""))
            sNumber = "0";

        String number=myFormat.format(Float.parseFloat(sNumber));
        holder.tvNumber.setText(number);
        holder.tvGhiChu.setText(mThuChiList.get(position).getNote().toString());
        holder.tvDanhMuc.setText(mThuChiList.get(position).getDanhmuc());
        holder.tvDate.setText(mThuChiList.get(position).getDate().toString());
        holder.imgSpiner.setImageResource(uriImg[mThuChiList.get(position).getTypeSpiner()]);
        holder.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View v, int postions) {
               dialog=new MaterialDialog.Builder(mContext)
                        .customView(R.layout.customdialog, true)
                        .build();
                View view=dialog.getCustomView();
                initView(view,postions);

            }
        });
    }
    private void initView(View view, final int postions) {
        imgCalender=view.findViewById(R.id.imgCalender);
        edtNote=view.findViewById(R.id.edtNote);
        edtNumber=view.findViewById(R.id.edtNumber);
        optionOne=view.findViewById(R.id.tvOne);
        optionTwo=view.findViewById(R.id.tvTwo);
        optionThree=view.findViewById(R.id.tvThree);
        edtNumber.addTextChangedListener(Commom.onTextChangedListener2(edtNumber,optionOne,optionTwo,optionThree));
        tvDate=view.findViewById(R.id.edtDate);
        btnSave=view.findViewById(R.id.btnSave);
        btnDelete=view.findViewById(R.id.btnDelete);
        btnCancel=view.findViewById(R.id.btnCancel);
        spinerDanhmuc=view.findViewById(R.id.spinerDanhmuc);
        spinerDanhmuc.setAdapter(new SpinnerAdapter(mContext, R.layout.spinner_row, mLstItemAddProgs));
        imgCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDatePicker();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                String note=edtNote.getText().toString().trim();
                String number=edtNumber.getText().toString().replaceAll(",", "").trim();
                String date=tvDate.getText().toString().trim();
                int type=1;
                int typeSpiner=spinerDanhmuc.getSelectedItemPosition();
                String danhmuc=mLstItemAddProgs.get(typeSpiner).getName();
                    if(note!=null && !note.equals("") &&number!=null && !number.equals("") &&date!=null && !date.equals("")){
                    ThuChi thuChi=new ThuChi(id,note,danhmuc,number,date,type,typeSpiner);
                    Log.e("Save: ", note+"/"+number+"/"+danhmuc+"/"+typeSpiner );
                    if( databaseHelper.upDateMoney(thuChi)){
                        Log.e("Save: ", "ok");
                        mThuChiList.set(postions,thuChi);
                        notifyDataSetChanged();
                        dialog.dismiss();

                    }
                    else {
                        Log.e("Save", "fail");
                        dialog.dismiss();
                    }
                }
                else
                    Toast.makeText(mContext, R.string.checkIsNull, Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Log.e("ErrinPut", e.getMessage().toString());
                }
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThuChi thuChi2=new ThuChi(id,edtNote.getText().toString(),spinerDanhmuc.getSelectedItem().toString(),edtNumber.getText().toString(),tvDate.getText().toString(),1,spinerDanhmuc.getSelectedItemPosition());
                if( databaseHelper.deleteMoney(thuChi2)){
                    Log.e("Delete: ", "ok");
                    mThuChiList.remove(postions);
                    notifyItemRemoved(postions);
                    dialog.dismiss();
                }
                else {
                    dialog.dismiss();
                    Log.e("Delete: ", "fail");
                }
            }
        });
        edtNote.setText(mThuChiList.get(postions).getNote());
        edtNumber.setText(String.valueOf(mThuChiList.get(postions).getNumber()));
        tvDate.setText(mThuChiList.get(postions).getDate());
        id=mThuChiList.get(postions).getId();
        dialog.show();
    }

    private void getDatePicker() {
        Calendar calendar = Calendar.getInstance();
        // Lấy ra năm - tháng - ngày hiện tại
        int yearNow = calendar.get(calendar.YEAR);
        final int monthNow = calendar.get(calendar.MONTH);
        int dayNow = calendar.get(calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog= new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                try {
                    String inputString =dayOfMonth+"/"+(month+1)+"/"+year;
                    SimpleDateFormat fromUser = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String reformattedStr = myFormat.format(fromUser.parse(inputString));
                    String mDay="";
                    if(dayOfMonth<10){
                        int i=reformattedStr.lastIndexOf("-");
                        int newday= Integer.parseInt(reformattedStr.substring(i+1,reformattedStr.length()));
                        String sdefault=reformattedStr.substring(0,i);
                        mDay=sdefault+"-0"+newday;
                        tvDate.setText(mDay);
                    }
                    else
                    {
                        mDay=reformattedStr;
                        tvDate.setText(mDay);
                    }
                    Log.e("Date ", reformattedStr );
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        },yearNow,monthNow,dayNow);
        datePickerDialog.show();
    }

    @Override
    public int getItemCount() {
        return mThuChiList.size();
    }
}
