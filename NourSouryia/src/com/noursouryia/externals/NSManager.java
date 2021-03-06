package com.noursouryia.externals;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;

import com.noursouryia.IFragmentEnabler;
import com.noursouryia.adapters.IFragmentNotifier;
import com.noursouryia.entity.Article;
import com.noursouryia.entity.Author;
import com.noursouryia.entity.Category;
import com.noursouryia.entity.Comment;
import com.noursouryia.entity.File;
import com.noursouryia.entity.Poll;
import com.noursouryia.entity.PollChoice;
import com.noursouryia.entity.Question;
import com.noursouryia.entity.SearchResult;
import com.noursouryia.entity.Share;
import com.noursouryia.entity.Type;
import com.noursouryia.utils.IMenuOpener;

/**
 * @author H.L - admin
 *
 */
public class NSManager {

	static final String TAG = NSManager.class.getSimpleName();
	
	public static final long DEFAULT_TIMESTAMP 	= -1;
	public static final int DEFAULT_VALUE 	= -1;
	public static final int MAX_ARTICLE_PER_PAGE = 10;
	
	public static final String BASE_URL 		= "http://syrianoor.net/";
	public static final String URL_MATERIALS 	= BASE_URL + "get/materials?";
	public static final String URL_SEARCH 		= BASE_URL + "app/search";
	public static final String URL_TYPES 		= BASE_URL + "get/types";
	public static final String URL_COMMENTS		= BASE_URL + "get/comments?";
	public static final String URL_ADD_COMMENT	= BASE_URL + "add/comments";
	public static final String URL_FILES 		= BASE_URL + "app/files";
	public static final String URL_AUTHORS 		= BASE_URL + "app/authors";
	public static final String URL_ADD_SHARES 	= BASE_URL + "add/shares";
	public static final String URL_POLL 		= BASE_URL + "app/poll";
	public static final String URL_ADD_VOTE 	= BASE_URL + "add/vote";
		
	
	public static final String NAME_EN			= "name_en";
	public static final String NAME_AR 			= "name_ar";
	public static final String LINK 			= "link";
	public static final String CATS 			= "cats";
	public static final String TID		 		= "tid";
	public static final String NAME 			= "name";
	public static final String COUNTRY 			= "country";
	public static final String BODY				= "body";
	public static final String DATE				= "date";
	public static final String COUNT			= "count";
	public static final String QID 				= "qid";
	public static final String QUESTION 		= "question";
	public static final String POLL_CHOICES		= "poll_choices";
	public static final String CHID	 			= "chid";
	public static final String CHTEXT	 		= "chtext";
	public static final String CHVOTES	 		= "chvotes";
	public static final String NID	 			= "nid";
	public static final String TITLE	 		= "title";
	public static final String TYPE	 			= "type";
	public static final String TYPE_A	 		= "type_a";
	public static final String VISITS	 		= "visits";
	public static final String CREATED	 		= "created";
	public static final String FILE_PATH		= "filepath";
	public static final String YOUTUBE_LINK		= "youtube_link";
	public static final String MP4_LINK	 		= "mp4_link";
	public static final String MP3_LINK	 		= "mp3_link";
	public static final String PDF_LINK	 		= "pdf_link";
	public static final String RESULT	 		= "result";
	
	public static final String PREF_ONLINE_MODE	= "pref_online_mode";
	
	private IFragmentNotifier fragmentNotifier;
	
	private static NSManager mInstance = null;
	private static SharedPreferences settings;
	private SharedPreferences.Editor editor;
	private JSONParser jsonParser;
	private Context mContext;
	
	private Article currentArticle;
	
	private IMenuOpener menuOpener;
	
	private IFragmentEnabler fragmentEnabler;
	
	public NSManager(Context context) {
		
		mContext = context;
		jsonParser = new JSONParser();
		settings = PreferenceManager.getDefaultSharedPreferences(context);
		editor = settings.edit();
	}

	public synchronized static NSManager getInstance(Context context) {
		if (mInstance == null)
			mInstance = new NSManager(context);

		return mInstance;
	}
	
	public boolean setOnLineMode(boolean isOnline){
		editor.putBoolean(PREF_ONLINE_MODE, isOnline);
		return editor.commit();
	}

	public boolean isOnlineMode(){
		return settings.getBoolean(PREF_ONLINE_MODE, true);		
	}
	
	public void setNotificationSettings(boolean isRegistered){
		editor.putBoolean("notifs", isRegistered);
		editor.commit();
	}
	
	public boolean isNotificationEnabled(){
		return settings.getBoolean("notifs", false);
	}
	
	public void setNotificationSoundSettings(boolean isRegistered){
		editor.putBoolean("notifs_sound", isRegistered);
		editor.commit();
	}
	
	public boolean isNotificationSoundEnabled(){
		return settings.getBoolean("notifs_sound", false);
	}
	
	public ArrayList<Type> getTypes() {

		ArrayList<Type> types = new ArrayList<Type>();
		JSONArray array = jsonParser.getJSONFromUrl(URL_TYPES);
		if (array != null) 
			for (int i = 0; i < array.length(); i++) {
				try {
					JSONObject jObj = array.getJSONObject(i);
					Type type = new Type();
					type.setNameEn(jObj.getString(NAME_EN));
					type.setNameAr(jObj.getString(NAME_AR));
					type.setLink(jObj.getString(LINK));
					
					if(type.getLink().equals("#")){
						try{
							JSONArray jCats = (JSONArray) jObj.get(CATS);
							for (int j = 0; j < jCats.length(); j++) {
								JSONObject jCat = jCats.getJSONObject(j);
								Category category = new Category();
								category.setLink(jCat.getString(LINK));
								category.setName(jCat.getString(NAME));
								category.setTid(jCat.getInt(TID));

								Log.i(TAG,"Category " + j + " : " + category.toString());

								type.getCategories().add(category);
							}
						}catch(Exception ex){
							ex.printStackTrace();
						}
					}

					Log.i(TAG, type.toString());

					types.add(type);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		
		return types;
	}

	
	public ArrayList<Comment> getCommentsByID(int nid) {

		ArrayList<Comment> comments = new ArrayList<Comment>();
		String url = URL_COMMENTS + "nid=" + nid;
		
		Log.i(TAG, "url= " + url);
		
		JSONArray array = jsonParser.getJSONFromUrl(url);
		if (array != null) 
			for (int i = 0; i < array.length(); i++) {
				try {
					JSONObject jObj = array.getJSONObject(i);
					Comment comment = new Comment();
					comment.setName(jObj.getString(NAME));
					comment.setCountry(jObj.getString(COUNTRY));
					comment.setBody(jObj.getString(BODY));
					comment.setDate(jObj.getString(DATE));
					
					Log.i(TAG, comment.toString());

					comments.add(comment);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		return comments;
	}
	
	public ArrayList<File> getFiles() {

		ArrayList<File> files = new ArrayList<File>();
//		JSONArray array = jsonParser.getJSONFromUrl(URL_FILES+"?page="+pageNb);
		JSONArray array = jsonParser.getJSONFromUrl(URL_FILES+"?NumPager=2500");
		if (array != null) 
			for (int i = 0; i < array.length(); i++) {
				try {
					JSONObject jObj = array.getJSONObject(i);
					File file = new File();
					file.setTid(jObj.getInt(TID));
					file.setName(jObj.getString(NAME));
					file.setCount(jObj.getInt(COUNT));
					file.setLink(jObj.getString(LINK));
					
					Log.i(TAG, file.toString());

					files.add(file);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		return files;
	}
	
	public ArrayList<Author> getAuthors() {

		ArrayList<Author> authors = new ArrayList<Author>();
		JSONArray array = jsonParser.getJSONFromUrl(URL_AUTHORS+"?NumPager=2500");
//		JSONArray array = jsonParser.getJSONFromUrl(URL_AUTHORS+"?page="+pageNb);
		if (array != null) 
			for (int i = 0; i < array.length(); i++) {
				try {
					JSONObject jObj = array.getJSONObject(i);
					Author author = new Author();
					author.setTid(jObj.getInt(TID));
					author.setName(jObj.getString(NAME));
					author.setCount(jObj.getInt(COUNT));
					author.setLink(jObj.getString(LINK));
					
					Log.i(TAG, author.toString());

					authors.add(author);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		return authors;
	}
	
	public ArrayList<Poll> getPolls() {

		ArrayList<Poll> polls = new ArrayList<Poll>();
		JSONArray array = jsonParser.getJSONFromUrl(URL_POLL);
		if (array != null) 
			for (int i = 0; i < array.length(); i++) {
				try {
					JSONObject jObj = array.getJSONObject(i);
					Poll poll = new Poll();
					poll.setQid(jObj.getInt(QID));
					poll.setQuestion(jObj.getString(QUESTION));
					poll.setDate(jObj.getString(DATE));
					poll.setLink(jObj.getString(LINK));
					
					Log.i(TAG, poll.toString());

					polls.add(poll);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		return polls;
	}
	
	
	public Question getQuestionVotes(String questionLink) {

		Question question = new Question();
		try {
			JSONObject jObj = jsonParser.getJSONObjectFromUrl(questionLink);

			question.setQid(jObj.getInt(QID));
			question.setQuestion(jObj.getString(QUESTION));

			try{
				JSONArray jCats = (JSONArray) jObj.get(POLL_CHOICES);
				for (int i = 0; i < jCats.length(); i++) {
					JSONObject jCat = jCats.getJSONObject(i);
					PollChoice pollChoice = new PollChoice();
					pollChoice.setChid(jCat.getInt(CHID));
					pollChoice.setChtext(jCat.getString(CHTEXT));
					pollChoice.setChvotes(jCat.getInt(CHVOTES));

					Log.i(TAG, pollChoice.toString());

					question.getPollChoices().add(pollChoice);
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}


			Log.i(TAG, question.toString());

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return question;
	}
	
	
	public ArrayList<Article> getArticles(String url, long timeStamp, int numPager, int page) {

		ArrayList<Article> articles = new ArrayList<Article>();
		
		if(url == null)
			url = URL_MATERIALS;
		
		if(timeStamp != DEFAULT_TIMESTAMP)
			url += "&timestamp=" + timeStamp;
		if(numPager != DEFAULT_VALUE)
			url += "&NumPager=" + numPager;
		if(page != DEFAULT_VALUE)
			url += "&page=" + page;

		Log.i(TAG, "url " + url);
		
		JSONArray array = jsonParser.getJSONFromUrl(url);
		if (array != null) 
			for (int i = 0; i < array.length(); i++) {
				try {
					JSONObject jObj = array.getJSONObject(i);
					Article article = new Article();
					article.setNid(jObj.getInt(NID));
					article.setTitle(jObj.getString(TITLE));
					article.setBody(jObj.getString(BODY));
					article.setType(jObj.getString(TYPE));
					if(jObj.has(TYPE_A))
						article.setTypeAr(jObj.getString(TYPE_A));
					article.setVisits(jObj.getInt(VISITS));
					article.setCreated(jObj.getString(CREATED));
					if(jObj.has(NAME) && !jObj.getString(NAME).equals("null"))
						article.setName(jObj.getString(NAME));
					if(jObj.has(TID) && !jObj.getString(TID).equals("null"))
						article.setTid(jObj.getInt(TID));
					if(jObj.has(YOUTUBE_LINK))
						article.setYoutubeLink(jObj.getString(YOUTUBE_LINK));
					if(jObj.has(MP4_LINK))
						article.setMp4Link(jObj.getString(MP4_LINK));
					if(jObj.has(MP3_LINK))
						article.setMp3Link(jObj.getString(MP3_LINK));
					if(jObj.has(PDF_LINK))
						article.setPdfLink(jObj.getString(PDF_LINK));
					
					try{
						if(jObj.has(FILE_PATH)){
							JSONObject jPaths = jObj.getJSONObject(FILE_PATH);
							int j = 0;
							while(jPaths.has(""+j)){
								String path = jPaths.getString(""+j);
								article.getFilePath().add(path);

								Log.i(TAG, j + " " + path);
								j++;
							}
						}
					}catch(Exception ex){
						ex.printStackTrace();
					}
					
					Log.i(TAG, article.toString());

					articles.add(article);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		return articles;
	}
	
	public ArrayList<Article> getArticlesByUrl(String url) {

		ArrayList<Article> articles = new ArrayList<Article>();
		
		Log.i(TAG, "url " + url);
		
		JSONParser jsonParser = new JSONParser();
		JSONArray array = jsonParser.getJSONFromUrl(url);
		if (array != null) 
			for (int i = 0; i < array.length(); i++) {
				try {
					JSONObject jObj = array.getJSONObject(i);
					Article article = new Article();
					article.setNid(jObj.getInt(NID));
					article.setTitle(jObj.getString(TITLE));
					article.setBody(jObj.getString(BODY));
					article.setType(jObj.getString(TYPE));
					if(jObj.has(TYPE_A))
						article.setTypeAr(jObj.getString(TYPE_A));
					article.setVisits(jObj.getInt(VISITS));
					article.setCreated(jObj.getString(CREATED));
					if(jObj.has(NAME) && !jObj.getString(NAME).equals("null"))
						article.setName(jObj.getString(NAME));
					if(jObj.has(TID) && !jObj.getString(TID).equals("null"))
						article.setTid(jObj.getInt(TID));
					if(jObj.has(YOUTUBE_LINK))
						article.setYoutubeLink(jObj.getString(YOUTUBE_LINK));
					if(jObj.has(MP4_LINK))
						article.setMp4Link(jObj.getString(MP4_LINK));
					if(jObj.has(MP3_LINK))
						article.setMp3Link(jObj.getString(MP3_LINK));
					if(jObj.has(PDF_LINK))
						article.setPdfLink(jObj.getString(PDF_LINK));
					
					try{
						if(jObj.has(FILE_PATH)){
							JSONObject jPaths = jObj.getJSONObject(FILE_PATH);
							int j = 0;
							while(jPaths.has(""+j)){
								String path = jPaths.getString(""+j);
								article.getFilePath().add(path);

//								Log.i(TAG, j + " " + path);
								j++;
							}
						}
					}catch(Exception ex){
						ex.printStackTrace();
					}
					
//					Log.i(TAG, article.toString());
					Log.e(TAG, article.getNid() + " : " + article.getTitle() + article.getType());
					articles.add(article);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		return articles;
	}

	public SearchResult searchArticlesByKeyword(String keyword, int page) {

		SearchResult searchResult = new SearchResult();
		
		String url = URL_SEARCH + "?title=" + keyword.replaceAll(" ", "%20");
		
		if(page != NSManager.DEFAULT_VALUE)
			url += "&page=" + page;
		
		Log.i(TAG, ">>> url: " + url);
		
		JSONObject jSearchObj = jsonParser.getJSONObjectFromUrl(url);
		try{
			searchResult.setTotal_items(Integer.parseInt(jSearchObj.getString("total_items")));
			
			JSONArray array = jSearchObj.getJSONArray("data");
			if (array != null) 
				for (int i = 0; i < array.length(); i++) {
					try {
						JSONObject jObj = array.getJSONObject(i);
						Article article = new Article();
						article.setNid(jObj.getInt(NID));
						article.setTitle(jObj.getString(TITLE));
						article.setBody(jObj.getString(BODY));
						article.setType(jObj.getString(TYPE));
						if(jObj.has(TYPE_A))
							article.setTypeAr(jObj.getString(TYPE_A));
						article.setVisits(jObj.getInt(VISITS));
						article.setCreated(jObj.getString(CREATED));
						if(jObj.has(NAME) && !jObj.getString(NAME).equals("null"))
							article.setName(jObj.getString(NAME));
						if(jObj.has(TID) && !jObj.getString(TID).equals("null"))
							article.setTid(jObj.getInt(TID));
						if(jObj.has(YOUTUBE_LINK))
							article.setYoutubeLink(jObj.getString(YOUTUBE_LINK));
						if(jObj.has(MP4_LINK))
							article.setMp4Link(jObj.getString(MP4_LINK));
						if(jObj.has(MP3_LINK))
							article.setMp3Link(jObj.getString(MP3_LINK));
						if(jObj.has(PDF_LINK))
							article.setPdfLink(jObj.getString(PDF_LINK));

						try{
							if(jObj.has(FILE_PATH)){
								JSONObject jPaths = jObj.getJSONObject(FILE_PATH);
								int j = 0;
								while(jPaths.has(""+j)){
									String path = jPaths.getString(""+j);
									article.getFilePath().add(path);

									//								Log.i(TAG, j + " " + path);
									j++;
								}
							}
						}catch(Exception ex){
							ex.printStackTrace();
						}

						//					Log.i(TAG, article.toString());
						Log.e(TAG, article.getNid() + " : " + article.getTitle() + article.getType());
						searchResult.getArticles().add(article);

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return searchResult;
	}

	
	public int addComment(Comment comment, int articleNID) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("nid", String.valueOf(articleNID)));
		params.add(new BasicNameValuePair("name", comment.getName()));
		params.add(new BasicNameValuePair("country", comment.getCountry()));
		params.add(new BasicNameValuePair("email", comment.getEmail()));
		params.add(new BasicNameValuePair("body", comment.getCountry()));

		JSONObject json = jsonParser.getJSONObjectFromUrl(URL_ADD_COMMENT, params);
		
		try{
			return json.getInt(RESULT);
		}catch (JSONException e) {
			e.printStackTrace();
		}
		
		// return default
		return DEFAULT_VALUE;
	}
	
	public int addVote(int chid, int questionID) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(QID, String.valueOf(questionID)));
		params.add(new BasicNameValuePair(CHID, String.valueOf(chid)));

		JSONObject json = jsonParser.getJSONObjectFromUrl(URL_ADD_VOTE, params);
		
		try{
			return json.getInt(RESULT);
		}catch (JSONException e) {
			e.printStackTrace();
		}
		
		// return default
		return DEFAULT_VALUE;
	}
	
	public int addShare(Share share) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("title", share.getTitle()));
		params.add(new BasicNameValuePair("writer", share.getWriter()));
		params.add(new BasicNameValuePair("email", share.getEmail()));
		params.add(new BasicNameValuePair("msg", share.getMessage()));

		JSONObject json = jsonParser.getJSONObjectFromUrl(URL_ADD_SHARES, params);
		
		try{
			return json.getInt(RESULT);
		}catch (JSONException e) {
			e.printStackTrace();
		}
		
		// return default
		return DEFAULT_VALUE;
	}
	
	public static long getTimeStamp(Calendar calendar){
		
		if(calendar == null)
			calendar = Calendar.getInstance();
		
		Timestamp ts = new Timestamp(calendar.getTimeInMillis());
		return ts.getTime()/1000L;
//		return calendar.getTimeInMillis();
	}
	
	public void switchView(final View firstLayout, final View secondeLayout) {
		final Animation in = new AlphaAnimation(0.0f, 1.0f);
		in.setDuration(200);

		final Animation out = new AlphaAnimation(1.0f, 0.0f);
		out.setDuration(200);

		AnimationSet as = new AnimationSet(true);
		as.addAnimation(out);
		in.setStartOffset(200);
		as.addAnimation(in);

		out.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation animation) {
				firstLayout.setVisibility(View.GONE);
				secondeLayout.setVisibility(View.VISIBLE);
				secondeLayout.startAnimation(in);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
		});

		firstLayout.startAnimation(out);

	}
	
	public IFragmentNotifier getFragmentNotifier() {
		return fragmentNotifier;
	}

	public void setFragmentNotifier(IFragmentNotifier fragmentNotifier) {
		this.fragmentNotifier = fragmentNotifier;
	}

	public Article getCurrentArticle() {
		return currentArticle;
	}

	public void setCurrentArticle(Article currentArticle) {
		this.currentArticle = currentArticle;
	}

	public IMenuOpener getMenuOpener() {
		return menuOpener;
	}

	public void setMenuOpener(IMenuOpener menuOpener) {
		this.menuOpener = menuOpener;
	}

	public IFragmentEnabler getFragmentEnabler() {
		return fragmentEnabler;
	}

	public void setFragmentEnabler(IFragmentEnabler fragmentEnabler) {
		this.fragmentEnabler = fragmentEnabler;
	}

}