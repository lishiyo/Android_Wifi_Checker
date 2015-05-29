package com.cziyeli.project_one;

import com.google.gson.annotations.SerializedName;

/**
 * kind, data: { title, public_description, submission_type, header_img }
 * http://www.reddit.com/subreddits/popular.json
 */

public class PopularSubreddit {

    @SerializedName("kind")
    public String kind;

    @SerializedName("data")
    public PopularSubredditData data;

    public PopularSubreddit() {
        super();
    }

    public static class PopularSubredditData {
        @SerializedName("title")
        public String mTitle;

        @SerializedName("public_description")
        public String mPublicDescription;

        @SerializedName("submission_type")
        public String mSubmissionType;

        @SerializedName("header_img")
        public String mHeaderImg;

        @SerializedName("url")
        public String mUrl;

        public PopularSubredditData(String title, String public_description, String submission_type, String header_img, String url) {
            mTitle = title;
            mPublicDescription = public_description;
            mSubmissionType = submission_type;
            mHeaderImg = header_img;
            mUrl = url;
        }
    }
}
