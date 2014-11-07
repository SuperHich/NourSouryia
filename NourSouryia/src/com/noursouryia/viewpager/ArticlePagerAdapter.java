package com.noursouryia.viewpager;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.noursouryia.entity.Article;

/**
 * @author H.L - admin
 *
 */
public class ArticlePagerAdapter extends FragmentStatePagerAdapter {

	private ArrayList<Article> articles;

	public ArticlePagerAdapter(FragmentManager fm) {
		super(fm);

	}

	public ArticlePagerAdapter(FragmentManager fm, ArrayList<Article> articles) {
		super(fm);
		this.articles = articles;
	}

	@Override
	public int getCount() {
		return articles.size();
	}

	@Override
	public Fragment getItem(int position) { 
		return new ArticleNewsFragment(articles.get(position));
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);

	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE; 
	}	


}
