package com.example.musicplayer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.data.Song
import com.example.musicplayer.databinding.SwipeItemBinding

class SwipeSongAdapter(
        private val listener: OnSongClickListener
): RecyclerView.Adapter<SwipeSongAdapter.SwipeSongViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Song>() {

        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.mediaId == newItem.mediaId
        }

        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var songs: List<Song>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    inner class SwipeSongViewHolder(val binding: SwipeItemBinding) : RecyclerView.ViewHolder(binding.root)


    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwipeSongAdapter.SwipeSongViewHolder {
        return SwipeSongViewHolder(SwipeItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: SwipeSongViewHolder, position: Int) {
        holder.binding.apply {
            val text = "${songs[position].title} - ${songs[position].subtitle}"
            tvPrimary.text = text
            swipeItemCL.setOnClickListener {
                listener.onClickListener(differ.currentList[position])
            }
        }
    }

}

interface OnSongClickListener{
    fun onClickListener(song: Song)
}