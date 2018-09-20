package com.hdpsolution.quanlychitieu.adapter;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hdpsolution.quanlychitieu.interFace.OnItemClickListener;
import com.hdpsolution.quanlychitieu.R;

public class KhoanThuHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView tvGhiChu;
    public TextView tvNumber;
    public TextView tvDate;
    public ImageView imgEdit;
    public ImageView imgSpiner;
    public TextView tvDanhMuc;
    public FloatingActionButton btnAdd;
    OnItemClickListener itemClickListener;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public KhoanThuHolder(View itemView) {
        super(itemView);
        tvGhiChu=itemView.findViewById(R.id.tvLoaiTien);
        tvDate=itemView.findViewById(R.id.tvDate);
        tvNumber=itemView.findViewById(R.id.tvNumber);
        imgEdit=itemView.findViewById(R.id.imgEdit);
        btnAdd=itemView.findViewById(R.id.fab);
        imgSpiner=itemView.findViewById(R.id.imgKhoanThu);
        tvDanhMuc=itemView.findViewById(R.id.tvDanhmuc);
        imgEdit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition());
    }
}
