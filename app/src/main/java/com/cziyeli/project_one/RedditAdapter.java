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

        /*
         ViewHolder pattern - do view-finding in VH and set VH inside view tag
         convertView == reused view (view that just fell off screen), already inflated
         Avoid expensive inflation and view-finding
         */
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_reddit_popular, parent, false);
            holder = new ViewHolder();
            holder.headerTextView = (TextView) convertView.findViewById(R.id.header_text);
            holder.subTextView = (TextView) convertView.findViewById(R.id.sub_text);
            holder.thumbnailImageView = (ImageView) convertView.findViewById(R.id.img_thumbnail);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Get new data and reset this view
        PopularSubreddit subreddit = (PopularSubreddit) getItem(position);
        String title = subreddit.data.mTitle;
        String public_description = subreddit.data.mPublicDescription;
        String imageURL = subreddit.data.mHeaderImg;

        holder.headerTextView.setText(title.toUpperCase());
        holder.subTextView.setText(public_description);

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
