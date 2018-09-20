package com.hdpsolution.quanlychitieu.comom;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.data.BarEntry;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Commom {
    public static ArrayList<String> lstDateC =new ArrayList<>();
    public static ArrayList<String> lstNumberC =new ArrayList<>();
    public static ArrayList<String> lstDateT =new ArrayList<>();
    public static ArrayList<String> lstNumberT =new ArrayList<>();
    public static String danhMuc=null;
    public static TextWatcher onTextChangedListener(final EditText editText) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                editText.removeTextChangedListener(this);
//
//                try {
//                    String originalString = s.toString();
//
//                    Long longval;
//                    if (originalString.contains(",")) {
//                        originalString = originalString.replaceAll(",", "");
//                    }
//                    longval = Long.parseLong(originalString)*1000;
//                    editText.setText(String.valueOf(longval));
//                    editText.setSelection(editText.getText().length()-3);
//                } catch (NumberFormatException nfe) {
//                    nfe.printStackTrace();
//                }
//                editText.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {
                editText.removeTextChangedListener(this);
                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    if(s.length()<2){
                        longval = Long.parseLong(originalString)*1000;
                    }
                    else {
                        longval = Long.parseLong(originalString);
                    }


                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);
                    //setting text after format to EditText
                    editText.setText(formattedString);
                    editText.setSelection(editText.getText().length()-4);
                } catch (Exception nfe) {
                    nfe.printStackTrace();
                    editText.setSelection(editText.getText().length());
                }

                editText.addTextChangedListener(this);
            }
        };
    }
    public static TextWatcher onTextChangedListener2(final EditText editText,final TextView tv1,final TextView tv2,final TextView tv3) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                editText.removeTextChangedListener(this);
                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    if(s.length()<2){
                        longval = Long.parseLong(originalString)*1000;
                    }
                    else {
                        longval = Long.parseLong(originalString);
                    }
                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);
                    //setting text after format to EditText
                    editText.setText(formattedString);

                    editText.setSelection(editText.getText().length()-4);
                } catch (Exception nfe) {
                    nfe.printStackTrace();
                    editText.setSelection(editText.getText().length());
                }

                editText.addTextChangedListener(this);

            }
        };
    }
    public static ArrayList<BarEntry> mLstKhoanThu;
    public static ArrayList<BarEntry> mLstKhoanChi;

}
