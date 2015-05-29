package com.cziyeli.project_one;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * send title, public_description, submission_type
 */
public class RedditAdapter extends BaseAdapter {
    PopularSubreddit[] mSubreddits;
    Context mContext;
    // convert XML => View
    LayoutInflater mInflater;
//    JSONArray mJsonArray;

    public RedditAdapter(Context context, LayoutInflater inflater) {
        mContext = context;
        mInflater = inflater;
//        mJsonArray = new JSONArray();
    }

    @Override
    public Object getItem(int position) {
        if (mSubreddits == null != position >= 0 && position < mSubreddits.length) {
            return mSubreddits[position];
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return (mSubreddits == null) ? 0 :mSubreddits.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_reddit_popular, parent, false);
            // create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.headerTextView = (TextView) convertView.findViewById(R.id.header_text);
            holder.subTextView = (TextView) convertView.findViewById(R.id.sub_text);
            holder.thumbnailImageView = (ImageView) convertView.findViewById(R.id.img_thumbnail);
            // hang onto this holder for future recyclage
            convertView.setTag(holder);
        } else { // skip all the expensive inflation and view-finding
            holder = (ViewHolder) convertView.getTag();
        }

        PopularSubreddit subreddit = (PopularSubreddit) getItem(position);
        String title = subreddit.data.mTitle;
        holder.headerTextView.setText(title.toUpperCase());
        String public_description = subreddit.data.mPublicDescription;
        holder.subTextView.setText(public_description);

        String imageURL = subreddit.data.mHeaderImg;

        if (imageURL != null) {
            // Use Picasso to load the image, with placeholder during loading
            Picasso.with(this.mContext)
                    .load(imageURL)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(holder.thumbnailImageView);
        } else {
            // if there is no image url, use placeholder
            holder.thumbnailImageView.setImageResource(R.mipmap.ic_launcher);
        }

        return convertView;
    }

    public void updateData(PopularSubreddit[] data) {
        this.mSubreddits = data;
        notifyDataSetChanged();
    }

    // only have to do inflation and finding by ID once ever per view
    // packages the three subviews every row in list has
    // as a view scrolls out of site, GC dumps everything inside the view but the ViewHolder
    private static class ViewHolder {
        public ImageView thumbnailImageView;
        public TextView headerTextView; // title
        public TextView subTextView; // description
    }

}
