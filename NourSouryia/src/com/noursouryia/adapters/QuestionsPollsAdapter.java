package com.noursouryia.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.noursouryia.R;
import com.noursouryia.entity.PollChoice;
import com.noursouryia.utils.NSFonts;

public class QuestionsPollsAdapter extends ArrayAdapter<PollChoice> implements OnClickListener {

	ArrayList<PollChoice> propositions = new ArrayList<PollChoice>();
	LayoutInflater inflater;
	IPollPropositionListener listener;
	Context mContext;

	public QuestionsPollsAdapter(Context context, ArrayList<PollChoice> propositions, IPollPropositionListener mPropositionsListener)
	{
		super(context, 0, propositions);
		this.propositions = propositions;
		inflater= LayoutInflater.from(context);
		listener = mPropositionsListener;
		mContext = context;
		
		

	}

	class ChildViewHolder
	{
		TextView txv_proposition;
		ImageView puce;
		RelativeLayout row_bg ;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final ChildViewHolder holder;
		
		if(convertView==null)
		{
			holder = new ChildViewHolder();
			convertView = inflater.inflate(R.layout.poll_question_propositions_row, null);

			holder.txv_proposition 	= (TextView) convertView.findViewById(R.id.txv_proposition);
			holder.puce 	= (ImageView) convertView.findViewById(R.id.puce);
			holder.row_bg 		= (RelativeLayout) convertView.findViewById(R.id.row_bg);

			holder.txv_proposition.setTypeface(NSFonts.getNoorFont());

			holder.row_bg.setBackgroundResource(R.drawable.polls_list_selector);

			convertView.setTag(holder);
		}
		else {
			holder = (ChildViewHolder)convertView.getTag();
		}

		Log.e("proposiitions size", propositions.size()+"");
		
		PollChoice proposition = propositions.get(position);
		holder.txv_proposition.setText(proposition.getChtext());

		holder.row_bg.setTag(position);
		holder.row_bg.setOnClickListener(this);
		
		return convertView;
	}

	@Override
	public void onClick(View v) {

		int position = (Integer) v.getTag();
		listener.onPollPropositionClicked(position);
		
		
		RelativeLayout im = (RelativeLayout) v;
        ImageView myPuce = (ImageView) im.findViewById(R.id.puce);
        myPuce.setImageResource(R.drawable.puce_poll_proposition_selected);
		
	}


}