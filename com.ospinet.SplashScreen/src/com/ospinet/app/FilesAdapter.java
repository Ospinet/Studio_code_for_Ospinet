package com.ospinet.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FilesAdapter extends BaseAdapter {
    private static ArrayList<file> filesArrayList;
    private LayoutInflater mInflater;
    private Context mContext;

    public FilesAdapter(Context context, ArrayList<file> results) {
        filesArrayList = results;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return filesArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return filesArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.file_row, null);

            holder = new ViewHolder();

            holder.txtFname = (TextView) convertView.findViewById(R.id.txtFname);
            holder.txtLname = (TextView) convertView.findViewById(R.id.txtLname);
            holder.txtMember_id = (TextView) convertView.findViewById(R.id.txtMember_id);
            holder.txtFilename = (TextView) convertView.findViewById(R.id.txtFilename);
            holder.txtId = (TextView) convertView.findViewById(R.id.txtId);
            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtFname.setText(filesArrayList.get(position).getFname());
        holder.txtLname.setText(filesArrayList.get(position).getLname());
        holder.txtMember_id.setText(filesArrayList.get(position).getmember_id());
        holder.txtFilename.setText(filesArrayList.get(position).getFile_name());
        holder.txtId.setText(filesArrayList.get(position).getid());
        return convertView;

    }
    static class ViewHolder {
        TextView txtFname;
        TextView txtLname;
        TextView txtFilename;
        TextView txtId;
        TextView txtMember_id;
    }

}