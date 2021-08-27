package com.example.musicplayer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.musicplayer.data.Song
import com.example.musicplayer.databinding.ListItemBinding
import javax.inject.Inject

class SongAdapter @Inject constructor(
    private val glide: RequestManager,
    private val listener: OnSongClickListener
): RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

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

    inner class SongViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        return SongViewHolder(ListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.binding.apply {
            tvPrimary.text = songs[position].title
            tvSecondary.text = songs[position].subtitle
            glide.load(songs[position].imageUrl).into(ivItemImage)
            listItemCL.setOnClickListener {
                listener.onClickListener(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return songs.size
    }

}