package com.hdpsolution.quanlychitieu.fragment;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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
import com.hdpsolution.quanlychitieu.MainActivity;
import com.hdpsolution.quanlychitieu.R;
import com.hdpsolution.quanlychitieu.adapter.KhoanChiAdapter;
import com.hdpsolution.quanlychitieu.adapter.KhoanthuAdapter;
import com.hdpsolution.quanlychitieu.adapter.SpinnerAdapter;
import com.hdpsolution.quanlychitieu.comom.Commom;
import com.hdpsolution.quanlychitieu.data.DatabaseHelper;
import com.hdpsolution.quanlychitieu.model.ListItemAddProg;
import com.hdpsolution.quanlychitieu.model.ThuChi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentKhoanChi extends Fragment implements View.OnClickListener {
    RecyclerView recyclerView;
    FloatingActionButton btnAdd;
    Button btnOk;
    Button btnCancel;
    Button btnOk2;
    Button btnCancel2;
    Button btnDelete;
    MaterialDialog dialog;
    MaterialDialog dialogAdd;
    EditText edtName2;
    EditText edtName;
    EditText edtNumber;
    TextView optionOne;
    TextView optionTwo;
    TextView optionThree;
    TextView tvDate;
    DatabaseHelper databaseHelper;
    List<ThuChi> thuChiList;
    ImageView imgCalender;
    KhoanChiAdapter khoanChiAdapter;
    FragmentThongKe fragmentThongKe;
    TextView tvDanhmuc;
    KhoanthuAdapter khoanthuAdapter;
    ImageView imgCustom;
    EditText edtNote;
    Spinner spinerDanhmuc;
    ArrayList<ListItemAddProg> lstAddMore;

    public FragmentKhoanChi() {
        // Required empty public constructor
    }

    MainActivity mainActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mainActivity = (MainActivity) context;

        } catch (Exception e) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_khoan_chi, container, false);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseHelper = new DatabaseHelper(getActivity());
        thuChiList = new ArrayList<>();
        lstAddMore = new ArrayList<>();
        lstAddMore = getAllList();
        btnAdd = view.findViewById(R.id.fab);
        btnAdd.setOnClickListener(this);
        recyclerView = view.findViewById(R.id.reCyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        thuChiList = databaseHelper.getAllKhoanChi();
        fragmentThongKe = new FragmentThongKe();
        if (thuChiList.size() > 0) {
            reFreshData();
        }
    }

    public void reFreshData() {
        thuChiList.clear();
        thuChiList = databaseHelper.getAllKhoanChi();
        khoanChiAdapter = new KhoanChiAdapter(getActivity(), thuChiList, lstAddMore);
        Log.e("sizeChi", thuChiList.size() + "");
        recyclerView.setAdapter(khoanChiAdapter);
        khoanChiAdapter.notifyDataSetChanged();
        mainActivity.updateData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                addLoaiTien();
                break;
//            case R.id.btnSave:
//                Toast.makeText(getActivity(), "Lưu thành công !"+edtLoaiTien.getText().toString(), Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//                break;
//            case R.id.btnCancel:
//                Toast.makeText(getActivity(), "Đã hủy !", Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//                break;
        }
    }

    private void addLoaiTien() {
// lay tg hien tai
        Calendar calendar = Calendar.getInstance();
        // Lấy ra năm - tháng - ngày hiện tại
        int year = calendar.get(calendar.YEAR);
        final int month = calendar.get(calendar.MONTH);
        int day = calendar.get(calendar.DAY_OF_MONTH);
        // set diglog2
        boolean wrapInScrollView = true;
        dialog = new MaterialDialog.Builder(getActivity())
                .customView(R.layout.customdialog, wrapInScrollView)
                .show();
        View view = dialog.getCustomView();
        imgCustom = view.findViewById(R.id.imgCustom);
        edtNote = view.findViewById(R.id.edtNote);
        spinerDanhmuc = view.findViewById(R.id.spinerDanhmuc);
        spinerDanhmuc.setAdapter(new SpinnerAdapter(getActivity(), R.layout.spinner_row, lstAddMore));
        //
        imgCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAdd = new MaterialDialog.Builder(getActivity())
                        .customView(R.layout.customdialogadd, true)
                        .show();
                View view2 = dialogAdd.getCustomView();
                btnOk2 = view2.findViewById(R.id.btnSave);
                btnCancel2 = view2.findViewById(R.id.btnCancel);
                edtName2 = view2.findViewById(R.id.edtName);
                btnOk2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newDanhmuc = edtName2.getText().toString();
                        if (newDanhmuc != null && !newDanhmuc.equals("")) {
                            Commom.danhMuc = newDanhmuc;
                            dialogAdd.dismiss();
                        }

                    }
                });
                dialogAdd.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        try {
                            if (Commom.danhMuc.toString() != null && !Commom.danhMuc.equals("")) {
                                ListItemAddProg item = new ListItemAddProg();
                                item.setData(Commom.danhMuc.toString(), R.drawable.ic_keyboard_black_24dp);
                                lstAddMore.add(item);
                                spinerDanhmuc.setSelection(lstAddMore.size() - 1);
                            }
                        } catch (Exception e) {
                        }
                        ;

                    }
                });
                btnCancel2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogAdd.dismiss();
                    }
                });
            }
        });

        // diaglog 1
        btnOk = view.findViewById(R.id.btnSave);
        btnCancel = view.findViewById(R.id.btnCancel);
        imgCalender = view.findViewById(R.id.imgCalender);
        btnDelete = view.findViewById(R.id.btnDelete);
        btnDelete.setVisibility(View.GONE);
        //get view
        edtName = view.findViewById(R.id.edtName);
        edtNumber = view.findViewById(R.id.edtNumber);
        optionOne = view.findViewById(R.id.tvOne);
        optionTwo = view.findViewById(R.id.tvTwo);
        optionThree = view.findViewById(R.id.tvThree);
        edtNumber.addTextChangedListener(Commom.onTextChangedListener(edtNumber));
        tvDate = view.findViewById(R.id.edtDate);
        // set time hien tai
        String inputString = day + "/" + (month + 1) + "/" + year;
        try {
            SimpleDateFormat fromUser = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
            String reformattedStr = myFormat.format(fromUser.parse(inputString));
            String mDay = "";
            if (day < 10) {
                int i = reformattedStr.lastIndexOf("-");
                int newday = Integer.parseInt(reformattedStr.substring(i + 1, reformattedStr.length()));
                String sdefault = reformattedStr.substring(0, i);
                mDay = sdefault + "-0" + newday;
                tvDate.setText(mDay);
            } else {
                mDay = reformattedStr;
                tvDate.setText(mDay);
            }
            Log.e("Date ", reformattedStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //set OnclickButton
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String note = edtNote.getText().toString().trim();
                String number = edtNumber.getText().toString().replaceAll(",", "").trim();
                String date = tvDate.getText().toString();
                int type = 2;
                int typeSpiner = spinerDanhmuc.getSelectedItemPosition();
                String danhmuc = lstAddMore.get(typeSpiner).getName().toString();
                Log.e("danhmuc", danhmuc);
                Log.e("Save: ", note + "/" + number + "/" + date + "/" + type + "/" + typeSpiner);
                if (note != null && !note.equals("") && number != null && !number.equals("") && date != null && !date.equals("")) {
                    ThuChi thuChi = new ThuChi(note, danhmuc, number, date, type, typeSpiner);
                    if (databaseHelper.addMoney(thuChi)) {
                        Log.e("check", "ok");
                        dialog.dismiss();
                        reFreshData();
                    }
                } else
                    Toast.makeText(getActivity(), "Vui lòng điền đầy đủ thông tin !", Toast.LENGTH_SHORT).show();


            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        imgCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                // Lấy ra năm - tháng - ngày hiện tại
                int year = calendar.get(calendar.YEAR);
                final int month = calendar.get(calendar.MONTH);
                int day = calendar.get(calendar.DAY_OF_MONTH);
                setupDatePicker(year, month, day);
            }
        });
    }

    // set up date
    private void setupDatePicker(int yearNow, int monthNow, final int dayNow) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                try {
                    String inputString = dayOfMonth + "/" + (month + 1) + "/" + year;
                    SimpleDateFormat fromUser = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String reformattedStr = myFormat.format(fromUser.parse(inputString));
                    String mDay = "";
                    if (dayOfMonth < 10) {
                        int i = reformattedStr.lastIndexOf("-");
                        int newday = Integer.parseInt(reformattedStr.substring(i + 1, reformattedStr.length()));
                        String sdefault = reformattedStr.substring(0, i);
                        mDay = sdefault + "-0" + newday;
                        tvDate.setText(mDay);
                    } else {
                        mDay = reformattedStr;
                        tvDate.setText(mDay);
                    }
                    Log.e("Date ", reformattedStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, yearNow, monthNow, dayNow);
        datePickerDialog.show();
    }

    public ArrayList<ListItemAddProg> getAllList() {
        ArrayList<ListItemAddProg> allList = new ArrayList<ListItemAddProg>();
        ListItemAddProg item = new ListItemAddProg();
        item.setData("Khác", R.drawable.icon);
        allList.add(item);
        item = new ListItemAddProg();
        item.setData("Grab,Uber", R.drawable.ic_directions_car_black_24dp);
        allList.add(item);
        item = new ListItemAddProg();
        item.setData("Mua sắm", R.drawable.ic_shopping_basket_black_24dp);
        allList.add(item);
        item = new ListItemAddProg();
        item.setData("Sức khỏe", R.drawable.ic_local_hospital_black_24dp);
        allList.add(item);
        item = new ListItemAddProg();
        item.setData("Xe bus", R.drawable.ic_train_black_24dp);
        allList.add(item);
        item = new ListItemAddProg();
        item.setData("Vé máy bay", R.drawable.ic_airplanemode_active_black_24dp);
        allList.add(item);
        item = new ListItemAddProg();
        item.setData("Cho vay", R.drawable.deal);
        allList.add(item);
        item = new ListItemAddProg();
        item.setData("Kinh doanh", R.drawable.brainstorm);
        allList.add(item);
        item = new ListItemAddProg();
        item.setData("Ăn uống", R.drawable.restaurant);
        allList.add(item);
        item = new ListItemAddProg();
        item.setData("Điện nước", R.drawable.idea);
        allList.add(item);
        item = new ListItemAddProg();
        item.setData("Con cái", R.drawable.baby);
        allList.add(item);
        item = new ListItemAddProg();
        item.setData("Quần áo", R.drawable.clothes);
        allList.add(item);
        item = new ListItemAddProg();
        item.setData("Điện thoại", R.drawable.smartphone);
        allList.add(item);
        item = new ListItemAddProg();
        item.setData("Xăng xe", R.drawable.gas);
        allList.add(item);
        item = new ListItemAddProg();
        item.setData("Du lịch", R.drawable.suitcase);
        allList.add(item);
        item = new ListItemAddProg();
        item.setData("Nhà nghỉ", R.drawable.hotel);
        allList.add(item);
        item = new ListItemAddProg();
        item.setData("Phụ phí", R.drawable.group);
        allList.add(item);
        item = new ListItemAddProg();
        item.setData("Nhà cửa", R.drawable.house);
        allList.add(item);
        item = new ListItemAddProg();
        item.setData("Hiếu hỉ", R.drawable.bouquet);
        allList.add(item);
        item = new ListItemAddProg();
        item.setData("Cafe", R.drawable.coffee);
        allList.add(item);



       /* for (int i = 0; i < 10000; i++) {
            item = new ListItemAddProg();
            item.setData("Google " + i + i, R.drawable.ic_remove);
            allList.add(item);
        }*/

        return allList;
    }


}