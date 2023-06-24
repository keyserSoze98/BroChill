package com.keysersoze.brochill

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.TweetViewHolder>() {

    private val tweets: MutableList<Tweet> = mutableListOf()

    fun setTweets(tweetList: List<Tweet>) {
        tweets.clear()
        tweets.addAll(tweetList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tweet, parent, false)
        return TweetViewHolder(view)
    }

    override fun onBindViewHolder(holder: TweetViewHolder, position: Int) {
        val tweet = tweets[position]
        holder.bind(tweet)
    }

    override fun getItemCount(): Int {
        return tweets.size
    }

    inner class TweetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tweetTextView: TextView = itemView.findViewById(R.id.tweetTextView)

        fun bind(tweet: Tweet) {
            tweetTextView.text = tweet.tweet
        }
    }
}
