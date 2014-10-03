package com.noursouryia.externals;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.noursouryia.entity.Author;
import com.noursouryia.entity.Category;
import com.noursouryia.entity.File;
import com.noursouryia.entity.Poll;
import com.noursouryia.entity.PollChoice;
import com.noursouryia.entity.Question;
import com.noursouryia.entity.Type;

public class NSDatabaseManager extends NSDatabase {

	private static final String TAG = NSDatabaseManager.class.getSimpleName();
	
//	private static NSDatabaseManager mInstance;
//	
//	public static NSDatabaseManager getInstance(Context _context){
//
//		if(mInstance == null)
//			mInstance = new NSDatabaseManager(_context);
//
//		return mInstance;
//	}

	public NSDatabaseManager(Context ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	public ArrayList<Type> getAllTypes(){

		open();
		ArrayList<Type> types = new ArrayList<Type>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_TYPES;
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to type
		if (cursor.moveToFirst()) {
			do {
				Type type = new Type();

				type.setNameEn(cursor.getString((cursor.getColumnIndex(NSManager.NAME_EN))));
				type.setNameAr(cursor.getString(cursor.getColumnIndex(NSManager.NAME_AR)));
				type.setLink(cursor.getString((cursor.getColumnIndex(NSManager.LINK))));
				type.setCategories(getCategoriesByType(type.getNameEn()));
				
//				Log.e(TAG,"type : " + type.toString());
				types.add(type);
			} while (cursor.moveToNext());
		}

		return types;
	}
	
	public ArrayList<Category> getCategoriesByType(String type_id){

		ArrayList<Category> cats = new ArrayList<Category>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CATEGORIES + " WHERE " + COL_TYPE_ID + " LIKE '" + type_id + "'";
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to type
		if (cursor.moveToFirst()) {
			do {
				Category category = new Category();

				category.setTid(cursor.getInt(cursor.getColumnIndex(NSManager.TID)));
				category.setName(cursor.getString(cursor.getColumnIndex(NSManager.NAME)));
				category.setLink(cursor.getString((cursor.getColumnIndex(NSManager.LINK))));
//				Log.e(TAG,"Category : " + category.toString());
				cats.add(category);
			} while (cursor.moveToNext());
		}

		return cats;
	}

	public void insertOrUpdateCategory(Category category, String type_id) {
		open();

		ContentValues values = new ContentValues();
		values.put(NSManager.TID, category.getTid());
		values.put(NSManager.NAME, category.getName());
		values.put(NSManager.LINK, category.getLink());
		values.put(COL_TYPE_ID, type_id);

		int up = db.updateWithOnConflict(TABLE_CATEGORIES, values, NSManager.TID + " = ?",
				new String[] {String.valueOf(category.getTid())}, SQLiteDatabase.CONFLICT_REPLACE);

		if(up != 1){
			db.insert(TABLE_CATEGORIES, null, values);
		}
		
	}

	public void insertOrUpdateType(Type type) {
		open();

		ContentValues values = new ContentValues();
		values.put(NSManager.NAME_EN, type.getNameEn());
		values.put(NSManager.NAME_AR, type.getNameAr());
		values.put(NSManager.LINK, type.getLink());
		
		for(Category cat : type.getCategories()){
			insertOrUpdateCategory(cat, type.getNameEn());
		}

		int up = db.updateWithOnConflict(TABLE_TYPES, values, NSManager.NAME_EN + " LIKE ?",
				new String[] {String.valueOf(type.getNameEn())}, SQLiteDatabase.CONFLICT_REPLACE);

		if(up != 1){
			db.insert(TABLE_TYPES, null, values);
		}
		
	}

	
	public ArrayList<File> getAllFiles(){

		ArrayList<File> files = new ArrayList<File>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_FILES;
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				File file = new File();

				file.setTid(cursor.getInt(cursor.getColumnIndex(NSManager.TID)));
				file.setName(cursor.getString(cursor.getColumnIndex(NSManager.NAME)));
				file.setCount(cursor.getInt(cursor.getColumnIndex(NSManager.COUNT)));
				file.setLink(cursor.getString((cursor.getColumnIndex(NSManager.LINK))));
//				Log.e(TAG,"File : " + file.toString());
				files.add(file);
			} while (cursor.moveToNext());
		}

		return files;
	}

	public void insertOrUpdateCategory(File file) {
		open();

		ContentValues values = new ContentValues();
		values.put(NSManager.TID, file.getTid());
		values.put(NSManager.NAME, file.getName());
		values.put(NSManager.COUNT, file.getCount());
		values.put(NSManager.LINK, file.getLink());

		int up = db.updateWithOnConflict(TABLE_FILES, values, NSManager.TID + " = ?",
				new String[] {String.valueOf(file.getTid())}, SQLiteDatabase.CONFLICT_REPLACE);

		if(up != 1){
			db.insert(TABLE_CATEGORIES, null, values);
		}
	}
	
	public ArrayList<Author> getAllAuthors(){

		ArrayList<Author> authors = new ArrayList<Author>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_AUTHORS;
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				Author author = new Author();

				author.setTid(cursor.getInt(cursor.getColumnIndex(NSManager.TID)));
				author.setName(cursor.getString(cursor.getColumnIndex(NSManager.NAME)));
				author.setCount(cursor.getInt(cursor.getColumnIndex(NSManager.COUNT)));
				author.setLink(cursor.getString((cursor.getColumnIndex(NSManager.LINK))));
//				Log.e(TAG,"Author : " + author.toString());
				authors.add(author);
			} while (cursor.moveToNext());
		}

		return authors;
	}

	public void insertOrUpdateCategory(Author author) {
		open();

		ContentValues values = new ContentValues();
		values.put(NSManager.TID, author.getTid());
		values.put(NSManager.NAME, author.getName());
		values.put(NSManager.COUNT, author.getCount());
		values.put(NSManager.LINK, author.getLink());

		int up = db.updateWithOnConflict(TABLE_AUTHORS, values, NSManager.TID + " = ?",
				new String[] {String.valueOf(author.getTid())}, SQLiteDatabase.CONFLICT_REPLACE);

		if(up != 1){
			db.insert(TABLE_CATEGORIES, null, values);
		}
	}
	
	
	public ArrayList<Poll> getAllPolls(){

		open();
		ArrayList<Poll> polls = new ArrayList<Poll>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_POLLS;
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to type
		if (cursor.moveToFirst()) {
			do {
				Poll poll = new Poll();

				poll.setQid(cursor.getInt((cursor.getColumnIndex(NSManager.QID))));
				poll.setQuestion(cursor.getString(cursor.getColumnIndex(NSManager.QUESTION)));
				poll.setDate(cursor.getString(cursor.getColumnIndex(NSManager.DATE)));
				poll.setLink(cursor.getString((cursor.getColumnIndex(NSManager.LINK))));
				
//				Log.e(TAG,"type : " + type.toString());
				polls.add(poll);
			} while (cursor.moveToNext());
		}

		return polls;
	}
	
	public Question getQuestionById(int qid){

		Question question = new Question();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_QUESTIONS + " WHERE " + NSManager.QID + " = " + qid;
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to type
		if (cursor.moveToFirst()) {
			question.setQid(cursor.getInt(cursor.getColumnIndex(NSManager.QID)));
			question.setQuestion(cursor.getString(cursor.getColumnIndex(NSManager.QUESTION)));
			question.setPollChoices(getPollChoicesById(question.getQid()));
			return question;
		}

		return null;
	}
	
	public ArrayList<PollChoice> getPollChoicesById(int qid){

		ArrayList<PollChoice> pollChoices = new ArrayList<PollChoice>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_POLL_CHOICES;
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				PollChoice pc = new PollChoice();

				pc.setChid(cursor.getInt((cursor.getColumnIndex(NSManager.CHID))));
				pc.setChtext(cursor.getString(cursor.getColumnIndex(NSManager.CHTEXT)));
				pc.setChvotes(cursor.getInt(cursor.getColumnIndex(NSManager.CHVOTES)));
				
				pollChoices.add(pc);
			} while (cursor.moveToNext());
		}

		return pollChoices;
	}
	

	public void insertOrUpdatePoll(Poll poll) {
		open();

		ContentValues values = new ContentValues();
		values.put(NSManager.QID, poll.getQid());
		values.put(NSManager.QUESTION, poll.getQuestion());
		values.put(NSManager.DATE, poll.getDate());
		values.put(NSManager.LINK, poll.getLink());

		int up = db.updateWithOnConflict(TABLE_POLLS, values, NSManager.QID + " = ?",
				new String[] {String.valueOf(poll.getQid())}, SQLiteDatabase.CONFLICT_REPLACE);

		if(up != 1){
			db.insert(TABLE_POLLS, null, values);
		}
		
	}

	public void insertOrUpdateQuestion(Question question) {
		open();

		ContentValues values = new ContentValues();
		values.put(NSManager.QID, question.getQid());
		values.put(NSManager.QUESTION, question.getQuestion());
		
		for(PollChoice pc : question.getPollChoices()){
			insertOrUpdatePollChoice(pc, question.getQid());
		}

		int up = db.updateWithOnConflict(TABLE_TYPES, values, NSManager.QID + " = ?",
				new String[] {String.valueOf(question.getQid())}, SQLiteDatabase.CONFLICT_REPLACE);

		if(up != 1){
			db.insert(TABLE_TYPES, null, values);
		}
		
	}
	
	public void insertOrUpdatePollChoice(PollChoice pollChoice, int qid) {
		open();

		ContentValues values = new ContentValues();
		values.put(NSManager.CHID, pollChoice.getChid());
		values.put(NSManager.CHTEXT, pollChoice.getChtext());
		values.put(NSManager.CHVOTES, pollChoice.getChvotes());
		values.put(NSManager.QID, qid);

		int up = db.updateWithOnConflict(TABLE_POLL_CHOICES, values, NSManager.CHID + " = ?",
				new String[] {String.valueOf(pollChoice.getChid())}, SQLiteDatabase.CONFLICT_REPLACE);

		if(up != 1){
			db.insert(TABLE_POLL_CHOICES, null, values);
		}
		
	}
}
