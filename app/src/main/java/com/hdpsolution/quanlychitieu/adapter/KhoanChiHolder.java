package com.hdpsolution.quanlychitieu.adapter;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hdpsolution.quanlychitieu.interFace.OnItemClickListenerC;
import com.hdpsolution.quanlychitieu.R;

public class KhoanChiHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    OnItemClickListenerC itemClickListenerC;
    public TextView tvGhiChu;
    public TextView tvNumber;
    public TextView tvDate;
    public ImageView imgEdit;
    public ImageView imgSpiner;
    public TextView tvDanhMuc;
    public FloatingActionButton btnAdd;
    public KhoanChiHolder(View itemView) {
        super(itemView);
        tvGhiChu=itemView.findViewById(R.id.tvLoaiTienChi);
        tvDate=itemView.findViewById(R.id.tvDateC);
        tvNumber=itemView.findViewById(R.id.tvNumberChi);
        imgEdit=itemView.findViewById(R.id.imgEditC);
        btnAdd=itemView.findViewById(R.id.fab);
        imgSpiner=itemView.findViewById(R.id.imgKhoanChi);
        tvDanhMuc=itemView.findViewById(R.id.tvDanhmucChi);
        imgEdit.setOnClickListener(this);
    }

    public void setItemClickListener(OnItemClickListenerC itemClickListener) {
        this.itemClickListenerC = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListenerC.onClick(v,getAdapterPosition());
    }
}
