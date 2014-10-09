package com.noursouryia.adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.noursouryia.R;
import com.noursouryia.utils.NSFonts;

public class AlphabetAdapter extends ArrayAdapter<String>{
	 Context context;
	    int layoutResourceId;   
	   // BcardImage data[] = null;
	    String[] data=new String[]{};
	    public AlphabetAdapter(Context context, int layoutResourceId, String[] data) {
	        super(context, layoutResourceId, data);
	        this.layoutResourceId = layoutResourceId;
	        this.context = context;
	        this.data = data;
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        Holder holder = null;
	       
	        if(row == null)
	        {
	            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	            row = inflater.inflate(layoutResourceId, parent, false);
	           
	            holder = new Holder();
	            holder.txtTitle = (TextView)row.findViewById(R.id.text1);
	            holder.txtTitle.setTypeface(NSFonts.getNoorFont());
	            
	            row.setTag(holder);
	        }
	        else
	        {
	            holder = (Holder)row.getTag();
	        }
	       
	        String lettre = data[position];
	        holder.txtTitle.setText(lettre);
	       
	        return row;
	    }
	   
	    static class Holder
	    {
	        TextView txtTitle;
	    }
	}
