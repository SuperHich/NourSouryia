package com.noursouryia;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.noursouryia.entity.Comment;
import com.noursouryia.externals.NSManager;
import com.noursouryia.utils.NSFonts;
import com.noursouryia.utils.Utils;

public class AddCommentFragment extends Fragment {

	protected static final String TAG = AddCommentFragment.class.getSimpleName();
	private EditText edt_name, edt_country, edt_email, edt_comment;
	private TextView txv_name, txv_country, txv_email, txv_comment ;
	private Button btn_remove, btn_add;
	private int articleNID;
	private boolean isCanceled = false;

	public AddCommentFragment() {
		// Empty constructor required for DialogFragment
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(getArguments() != null){
			articleNID 		= getArguments().getInt(NSManager.NID);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_add_comment, container, false);

		txv_name 	= (TextView) rootView.findViewById(R.id.txv_name);
		txv_country = (TextView) rootView.findViewById(R.id.txv_country);
		txv_email 	= (TextView) rootView.findViewById(R.id.txv_email);
		txv_comment = (TextView) rootView.findViewById(R.id.txv_comment);
		edt_name 	= (EditText) rootView.findViewById(R.id.edt_name);
		edt_country = (EditText) rootView.findViewById(R.id.edt_country);
		edt_email 	= (EditText) rootView.findViewById(R.id.edt_email);
		edt_comment = (EditText) rootView.findViewById(R.id.edt_comment);
		btn_remove 	= (Button) rootView.findViewById(R.id.btn_remove);
		btn_add 	= (Button) rootView.findViewById(R.id.btn_add);

		txv_name.setTypeface(NSFonts.getNoorFont());
		txv_country.setTypeface(NSFonts.getNoorFont());
		txv_email.setTypeface(NSFonts.getNoorFont());
		txv_comment.setTypeface(NSFonts.getNoorFont());
		edt_name.setTypeface(NSFonts.getNoorFont());
		edt_country.setTypeface(NSFonts.getNoorFont());
		edt_email.setTypeface(NSFonts.getNoorFont());
		edt_comment.setTypeface(NSFonts.getNoorFont());

		btn_remove.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().onBackPressed();
			}
		});

		btn_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Utils.hideKeyboard(getActivity(), edt_comment);

				if(!isDataReady())
				{
					((MainActivity) getActivity()).showInfoPopup(getString(R.string.fill_empty_data));
				}else{
					addComment();
				}
			}
		});

		return rootView;
	}


	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

	}


	@Override
	public void onDetach() {
		super.onDetach();
		
		isCanceled  = true;
	}
	
	private void addComment(){

		new AsyncTask<Comment, Void, Integer>() {

			ProgressDialog loading;

			@Override
			protected void onPreExecute() {
				loading = new ProgressDialog(getActivity());
				loading.setMessage(getString(R.string.please_wait));
				loading.setCancelable(false);
				loading.show();
			}

			@Override
			protected Integer doInBackground(Comment... params) {
				try{
					if(Utils.isOnline(getActivity())){
						
						Comment comment = new Comment();
						comment.setName(edt_name.getText().toString());
						comment.setCountry(edt_country.getText().toString());
						comment.setEmail(edt_email.getText().toString());
						comment.setBody(edt_comment.getText().toString());
						
						return NSManager.getInstance(getActivity()).addComment(comment, articleNID);
					}
				}catch(Exception e){
					Log.e(TAG, "Error while initData !");
				}
				return null;
			}

			@Override
			protected void onPostExecute(Integer result) {
				loading.dismiss();

				if(isCanceled)
					return;


				if(result != null){
					if(result != NSManager.DEFAULT_VALUE){
						Toast.makeText(getActivity(), getString(R.string.comment_added), Toast.LENGTH_LONG).show();
						getActivity().onBackPressed();
					}else
						Toast.makeText(getActivity(), getString(R.string.comment_not_added), Toast.LENGTH_LONG).show();
				}else{
					((MainActivity)getActivity()).showConnectionErrorPopup();
				}
			}
		}.execute();

	}

	private boolean isDataReady(){

		if(edt_name.getText().toString().equals("") || 
				edt_country.getText().toString().equals("") ||
				edt_email.getText().toString().equals("") ||
				edt_comment.getText().toString().equals(""))
			return false;

		return true;
	}

}
