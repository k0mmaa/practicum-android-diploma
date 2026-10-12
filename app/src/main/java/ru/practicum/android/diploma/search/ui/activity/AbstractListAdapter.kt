package ru.practicum.android.diploma.search.ui.activity

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class AbstractListAdapter<T>(
    val clickListener: ClickListener<T>,
) : RecyclerView.Adapter<AbstractViewHolder<T>>() {

    var list = ArrayList<T>()

    private var longClickListener: ((T) -> Unit)? = null

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<T>

    override fun onBindViewHolder(holder: AbstractViewHolder<T>, position: Int) {

        val item = list[position]

        holder.bind(item)

        holder.itemView.setOnClickListener {
            clickListener.onClick(item)
        }

        holder.itemView.setOnLongClickListener(null)

        longClickListener?.let { listener ->
            holder.itemView.setOnLongClickListener {
                listener(item)
                true
            }
        }
    }

    fun setOnLongClickListener(listener: (T) -> Unit) {
        longClickListener = listener
    }

    override fun getItemCount(): Int = list.size

    fun interface ClickListener<T> {
        fun onClick(item: T)
    }
}
