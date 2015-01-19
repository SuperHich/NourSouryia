package com.noursouryia.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.noursouryia.R;
import com.noursouryia.entity.Article;
import com.noursouryia.entity.Poll;
import com.noursouryia.utils.NSFonts;

public class PollsAdapter extends ArrayAdapter<Poll> implements OnClickListener {

	ArrayList<Poll> polls = new ArrayList<Poll>();
	IPollListener listener;
	LayoutInflater inflater;
	Context mContext;

	public PollsAdapter(Context context, ArrayList<Poll> polls, IPollListener mPollsListener)
	{
		super(context, 0, polls);
		this.polls = polls;
		inflater= LayoutInflater.from(context);
		mContext = context;
		listener = mPollsListener;

	}

	class ChildViewHolder
	{
		TextView txv_question;
		ImageView puce;
		RelativeLayout row_bg ;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ChildViewHolder holder;
		
		if(convertView==null)
		{
			holder = new ChildViewHolder();
			convertView = inflater.inflate(R.layout.poll_questions_row, null);

			holder.txv_question 	= (TextView) convertView.findViewById(R.id.txv_question);
			holder.row_bg 		= (RelativeLayout) convertView.findViewById(R.id.row_bg);

			holder.txv_question.setTypeface(NSFonts.getNoorFont());

			holder.row_bg.setBackgroundResource(R.drawable.polls_list_selector);

			convertView.setTag(holder);
		}
		else {
			holder = (ChildViewHolder) convertView.getTag();
		}

		Poll poll = polls.get(position);
		holder.txv_question.setText(poll.getQuestion());
		
		holder.row_bg.setTag(position);
		holder.row_bg.setOnClickListener(this);

		
		return convertView;
	}

	@Override
	public void onClick(View v) {

		int position = (Integer) v.getTag();
		listener.onPollClicked(position);
		
	}


}