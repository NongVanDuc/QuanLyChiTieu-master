package com.hdpsolution.quanlychitieu.chartActivity;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;

public class MyXAxisValueFormat implements IAxisValueFormatter {
   private ArrayList<String> mValues;
    public MyXAxisValueFormat(ArrayList values) {
        this.mValues=values;
    }
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int intValue = (int) value;

        if (mValues.size() > intValue && intValue >= 0) return mValues.get((int)value);

        return "";
    }

}
