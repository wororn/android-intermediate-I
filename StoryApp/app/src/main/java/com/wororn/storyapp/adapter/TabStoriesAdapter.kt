package com.wororn.storyapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wororn.storyapp.componen.response.TabStoriesItem
import com.wororn.storyapp.databinding.ActivityItemStoriesBinding
import com.wororn.storyapp.interfaces.detail.DetailActivity
import com.wororn.storyapp.tools.withDateFormat

class TabStoriesAdapter: PagingDataAdapter<TabStoriesItem, TabStoriesAdapter.ListViewHolder>(DIFF_CALLBACK){

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val tabstoriesbunching = ActivityItemStoriesBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(tabstoriesbunching)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }

    }

    class ListViewHolder(private var itemstoriesbunching: ActivityItemStoriesBinding) : RecyclerView.ViewHolder(itemstoriesbunching.root) {
        fun bind(table: TabStoriesItem) {
            itemstoriesbunching.apply {
                tvName.text = table.name
                tvDesc.text = table.description
                tvCreate.withDateFormat(table.createdAt.toString())
            }

            Glide.with(itemstoriesbunching.root.context)
                .load(table.photoUrl)
                .into(itemstoriesbunching.imgStory)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_DATA, table)
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(itemstoriesbunching.imgStory, "image"),
                        Pair(itemstoriesbunching.tvName, "name"),
                        Pair(itemstoriesbunching.tvCreate, "create"),
                        Pair(itemstoriesbunching.tvDesc, "desc")
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    companion object {
         val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TabStoriesItem>() {
            override fun areItemsTheSame(
                oldItem:TabStoriesItem,
                newItem: TabStoriesItem)
                    : Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem:TabStoriesItem,
                newItem:TabStoriesItem)
                    : Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}