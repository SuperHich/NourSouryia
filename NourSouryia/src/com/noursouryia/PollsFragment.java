package com.noursouryia;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.noursouryia.adapters.IPollListener;
import com.noursouryia.adapters.IPollPropositionListener;
import com.noursouryia.adapters.PollsAdapter;
import com.noursouryia.adapters.QuestionsPollChartsAdapter;
import com.noursouryia.adapters.QuestionsPollsAdapter;
import com.noursouryia.entity.Poll;
import com.noursouryia.entity.PollChoice;
import com.noursouryia.entity.Question;
import com.noursouryia.externals.NSManager;
import com.noursouryia.utils.BaseFragment;
import com.noursouryia.utils.NSFonts;
import com.noursouryia.utils.Utils;

public class PollsFragment extends BaseFragment implements IPollPropositionListener, IPollListener {

	private TextView title_previous_polls, title_polls , txv_empty , txv_wait, txv_empty_prop, current_question;
	private LinearLayout loading, display_polls, buttons;
	private ListView questionsList , propositionsList, listView_prop_charts;
	private RelativeLayout layout_previous_polls ;
	private ImageView btn_vote, btn_results, back_charts ;

	private NSManager mManager ;
	private ArrayList<Poll> allPolls = new  ArrayList<Poll>();
	private ArrayList<PollChoice> allPropositions = new  ArrayList<PollChoice>();

	private PollsAdapter qAdapter ;
	private QuestionsPollsAdapter qpAdapter ;
	private QuestionsPollChartsAdapter qpvAdapter ;

	private Question firstQuestion ;

	private IPollPropositionListener mPropositionsListener ;
	private IPollListener mPollsListener ;

	private PollChoice actualPollChoice;
	private Question actualQuestion ;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = (RelativeLayout) inflater.inflate(R.layout.fragment_polls, container, false);

		rootView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
		mManager = NSManager.getInstance(getActivity());

		title_polls = (TextView) rootView.findViewById(R.id.title_polls);
		current_question =  (TextView) rootView.findViewById(R.id.current_question);
		title_previous_polls = (TextView) rootView.findViewById(R.id.title_previous_polls);
		txv_empty = (TextView) rootView.findViewById(R.id.txv_emptyList);
		txv_wait = (TextView) rootView.findViewById(R.id.txv_wait);
		txv_empty_prop = (TextView) rootView.findViewById(R.id.txv_emptyList_prop);
		
		display_polls = (LinearLayout) rootView.findViewById(R.id.display_polls);
		loading = (LinearLayout) rootView.findViewById(R.id.loading);
		buttons = (LinearLayout) rootView.findViewById(R.id.buttons);
		layout_previous_polls = (RelativeLayout) rootView.findViewById(R.id.layout_previous_polls);
		
		questionsList = (ListView) rootView.findViewById(R.id.listView);
		propositionsList = (ListView) rootView.findViewById(R.id.listView_prop);
		listView_prop_charts = (ListView) rootView.findViewById(R.id.listView_prop_charts); 

		btn_vote = (ImageView) rootView.findViewById(R.id.btn_polls_vote);
		btn_results = (ImageView) rootView.findViewById(R.id.btn_polls_results);
		back_charts = (ImageView) rootView.findViewById(R.id.back_charts);
		
		current_question.setTypeface(NSFonts.getNoorFont());
		title_polls.setTypeface(NSFonts.getNoorFont());
		title_previous_polls.setTypeface(NSFonts.getNoorFont());
		txv_empty.setTypeface(NSFonts.getNoorFont());
		txv_empty_prop.setTypeface(NSFonts.getNoorFont());
		txv_wait.setTypeface(NSFonts.getNoorFont());
		
		back_charts.bringToFront();




		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mPropositionsListener = (IPollPropositionListener) this;
		mPollsListener = (IPollListener) this;

		qAdapter = new PollsAdapter(getActivity(), allPolls, mPollsListener);
		qpAdapter = new QuestionsPollsAdapter(getActivity(), allPropositions, mPropositionsListener);
		

		questionsList.setAdapter(qAdapter);
		propositionsList.setAdapter(qpAdapter);
		
//		qpvAdapter = new QuestionsPollChartsAdapter(getActivity(), allPropositions);
//		listView_prop_charts.setAdapter(qpvAdapter);


		initData();

		btn_vote.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					ImageView view = (ImageView) v;
					view.getDrawable().setColorFilter(0x77ffffff,PorterDuff.Mode.SRC_ATOP);
					view.invalidate();
					break;
				}
				case MotionEvent.ACTION_UP: {

					if (actualPollChoice != null)
						voteProposition(actualPollChoice.getChid(), actualQuestion.getQid());
					else 
						Toast.makeText(getActivity(), getString(R.string.choice_vote), Toast.LENGTH_SHORT).show();

				}
				case MotionEvent.ACTION_CANCEL: {
					ImageView view = (ImageView) v;
					view.getDrawable().clearColorFilter();
					view.invalidate();
					break;
				}
				}

				return true;
			}


		});


		btn_results.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					ImageView view = (ImageView) v;
					view.getDrawable().setColorFilter(0x77ffffff,PorterDuff.Mode.SRC_ATOP);
					view.invalidate();
					break;
				}
				case MotionEvent.ACTION_UP: {
					
					layout_previous_polls.setVisibility(View.GONE);
					buttons.setVisibility(View.GONE);
					back_charts.setVisibility(View.VISIBLE);
					back_charts.bringToFront();
					
//					qpvAdapter.notifyDataSetChanged();
					
					qpvAdapter = new QuestionsPollChartsAdapter(getActivity(), allPropositions);
					listView_prop_charts.setAdapter(qpvAdapter);
					
					mManager.switchView(propositionsList, listView_prop_charts);


				}
				case MotionEvent.ACTION_CANCEL: {
					ImageView view = (ImageView) v;
					view.getDrawable().clearColorFilter();
					view.invalidate();
					break;
				}
				}

				return true;
			}
		});

		
		back_charts.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					ImageView view = (ImageView) v;
					view.getDrawable().setColorFilter(0x77ffffff,PorterDuff.Mode.SRC_ATOP);
					view.invalidate();
					break;
				}
				case MotionEvent.ACTION_UP: {
					
					layout_previous_polls.setVisibility(View.VISIBLE);
					buttons.setVisibility(View.VISIBLE);
					back_charts.setVisibility(View.GONE);
					
					mManager.switchView( listView_prop_charts, propositionsList);


				}
				case MotionEvent.ACTION_CANCEL: {
					ImageView view = (ImageView) v;
					view.getDrawable().clearColorFilter();
					view.invalidate();
					break;
				}
				}

				return true;
			}
		});


	}

	private void initData(){

		new AsyncTask<Void, Void, ArrayList<Poll>>() {

			@Override
			protected void onPreExecute() {
				loading.setVisibility(View.VISIBLE);
				display_polls.setVisibility(View.GONE);
			}

			@Override
			protected ArrayList<Poll> doInBackground(Void... params) {
				try{
					if(Utils.isOnline(getActivity())){

						allPolls.addAll(mManager.getPolls());


						int qid = allPolls.get(0).getQid();
						String questionLink = "http://syrianoor.net/get/poll?qid="+qid ;

						firstQuestion = mManager.getQuestionVotes(questionLink);

						return allPolls;
					}
				}catch(Exception e){
					Log.e(TAG, "Error while initData !");
				}

				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<Poll> result) {

				loading.setVisibility(View.GONE);
				display_polls.setVisibility(View.VISIBLE);


				if(result != null && firstQuestion != null){


					allPropositions.addAll(firstQuestion.getPollChoices());
					current_question.setText(firstQuestion.getQuestion());

					actualQuestion = firstQuestion;

					qAdapter.notifyDataSetChanged();
					qpAdapter.notifyDataSetChanged();


				}else
					((MainActivity)getActivity()).showConnectionErrorPopup();

				toggleEmptyMessage();
			}
		}.execute();

	}

	private void toggleEmptyMessage() {
		if(allPolls.size() == 0)
			txv_empty.setVisibility(View.VISIBLE);
		else
			txv_empty.setVisibility(View.GONE);

		if (firstQuestion != null) {
		if(firstQuestion.getPollChoices().size() == 0)
			txv_empty_prop.setVisibility(View.VISIBLE);
		else
			txv_empty_prop.setVisibility(View.GONE);
		}

	}


	@Override
	public void onPollPropositionClicked(int position) {

		Log.e("ITEM CLICKED", "ITEM CLICKED = "+position);

		actualPollChoice = actualQuestion.getPollChoices().get(position);

		for (int i = 0; i < propositionsList.getChildCount(); i++) {

			if (i != position){

				RelativeLayout anyListRow = (RelativeLayout) propositionsList.getChildAt(i);
				ImageView myPuce = (ImageView) anyListRow.findViewById(R.id.puce);
				myPuce.setImageResource(R.drawable.puce_poll_proposition);

			}

		}

	}

	@Override
	public void onPollClicked(int position) {
		Log.e("ITEM CLICKED", "ITEM CLICKED = "+position);


		changeQuestion(allPolls.get(position).getQid());


	}

	private void changeQuestion(final int qid){

		new AsyncTask<Void, Void, Question>() {

			ProgressDialog mloading ;

			@Override
			protected void onPreExecute() {

				mloading = new ProgressDialog(getActivity());
				mloading.setCancelable(false);
				mloading.setMessage(getString(R.string.please_wait));

				mloading.show();

				allPropositions.clear();

			}

			@Override
			protected Question doInBackground(Void... params) {
				try{
					if(Utils.isOnline(getActivity())){

						String questionLink = "http://syrianoor.net/get/poll?qid="+qid ;
						Question mQuestion = mManager.getQuestionVotes(questionLink);

						return mQuestion;
					}
				}catch(Exception e){
					Log.e(TAG, "Error while initData !");
				}

				return null;
			}

			@Override
			protected void onPostExecute(Question result) {

				mloading.dismiss();


				if(result != null){

					Log.e("propositions", result.getPollChoices().size()+"");

					actualQuestion = result;

					allPropositions.addAll(result.getPollChoices());
					current_question.setText(result.getQuestion());
					
					
					qpAdapter.notifyDataSetChanged();


					for (int i = 0; i < propositionsList.getChildCount(); i++) {

						RelativeLayout anyListRow = (RelativeLayout) propositionsList.getChildAt(i);
						ImageView myPuce = (ImageView) anyListRow.findViewById(R.id.puce);
						myPuce.setImageResource(R.drawable.puce_poll_proposition);
					}

					actualPollChoice = null ;

				}else
					((MainActivity) getActivity()).showConnectionErrorPopup();

				toggleEmptyMessage();
			}
		}.execute();

	}


	private void voteProposition(final int chid, final int qid) {

		new AsyncTask<Void, Void, Integer>() {

			SweetAlertDialog mloading ;

			@Override
			protected void onPreExecute() {

				mloading  = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE) ;
				mloading.setTitleText(getString(R.string.please_wait));
				mloading.show();
				
			}

			@Override
			protected Integer doInBackground(Void... params) {
				try{
					if(Utils.isOnline(getActivity())){


						int voteResult = mManager.addVote(chid, qid);

						return voteResult;
					}

				}catch(Exception e){
					Log.e(TAG, "Error while initData !");
				}

				return null;
			}

			@Override
			protected void onPostExecute(Integer result) {

				mloading.dismiss();


				if(result != null){


					if (result == -1) { 

						SweetAlertDialog sw = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE) ;
						sw.setTitleText(getString(R.string.problem));
						sw.setContentText(getString(R.string.please_retry));
						sw.show();

					} else if (result == 0) {

						SweetAlertDialog sw = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE) ;
						sw.setTitleText(getString(R.string.problem));
						sw.setContentText(getString(R.string.problem_vote));
						sw.show();

					} else if (result == 1) {

						SweetAlertDialog sw = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE) ;
						sw.setTitleText(getString(R.string.success_vote));
						sw.show();


					}



				}else
					((MainActivity) getActivity()).showConnectionErrorPopup();

				toggleEmptyMessage();
			}
		}.execute();


	}



}

