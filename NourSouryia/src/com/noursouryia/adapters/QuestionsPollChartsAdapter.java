package com.noursouryia.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.noursouryia.R;
import com.noursouryia.entity.PollChoice;
import com.noursouryia.utils.CustomProgress;
import com.noursouryia.utils.NSFonts;

public class QuestionsPollChartsAdapter extends ArrayAdapter<PollChoice> {

	ArrayList<PollChoice> propositions = new ArrayList<PollChoice>();
	LayoutInflater inflater;
	Context mContext;
	int total_votes ;

	public QuestionsPollChartsAdapter(Context context, ArrayList<PollChoice> propositions)
	{
		super(context, 0, propositions);
		this.propositions = propositions;
		inflater= LayoutInflater.from(context);
		mContext = context;

	}

	class ChildViewHolder
	{
		TextView txv_proposition, txv_percentage;
		CustomProgress progress;
		LinearLayout row_bg ;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final ChildViewHolder holder;
		
		if(convertView==null)
		{
			holder = new ChildViewHolder();
			convertView = inflater.inflate(R.layout.poll_question_charts_row, null);

			holder.txv_proposition 	= (TextView) convertView.findViewById(R.id.txv_proposition);
			holder.txv_percentage 	= (TextView) convertView.findViewById(R.id.txv_percentage);
			holder.row_bg 		= (LinearLayout) convertView.findViewById(R.id.row_bg);

			holder.txv_proposition.setTypeface(NSFonts.getNoorFont());
			holder.txv_percentage.setTypeface(NSFonts.getLatin());

//			holder.row_bg.setBackgroundResource(R.drawable.polls_list_selector);

			convertView.setTag(holder);
		}
		else {
			holder = (ChildViewHolder)convertView.getTag();
		}

		holder.row_bg.setTag(position);
		
		total_votes = 0 ;
		for (int i = 0; i < propositions.size(); i++) {
			total_votes += propositions.get(i).getChvotes();
		}
		
		holder.progress 	= (CustomProgress) convertView.findViewById(R.id.progress);
		holder.progress.useRectangleShape();
		holder.progress.setMaximumPercentage(0);
		
//		holder.progress.setBackgroundColor(mContext.getResources().getColor(R.color.blue_top));
//		holder.progress.setProgressColor(mContext.getResources().getColor(R.color.drawer_bg));
//		holder.progress.setMax(total_votes);
		
		
		Log.e("chhvotes total size", total_votes+"");
		
		PollChoice proposition = propositions.get(position);
		holder.txv_proposition.setText(proposition.getChtext());
		
		if (total_votes != 0){
		double percentage =  (proposition.getChvotes() * 100.0 / total_votes);
		
		holder.txv_percentage.setText(((int)percentage)+"%");
		holder.progress.setMaximumPercentage( (float)  (percentage / 100.0));
		
		Log.e("percentage", "number of votes = "+ proposition.getChvotes() + " and percentage = "+percentage+"%");
		}
		
		return convertView;
	}



}