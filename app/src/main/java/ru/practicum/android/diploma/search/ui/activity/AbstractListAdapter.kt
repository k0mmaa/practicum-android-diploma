package ru.practicum.android.diploma.search.ui.activity

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.practicum.android.diploma.domain.models.Identifiable

abstract class AbstractListAdapter<T>(
    val clickListener: ClickListener<T>,
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, AbstractViewHolder<T>>(diffCallback) {

    private var longClickListener: ((T) -> Unit)? = null

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<T>

    override fun onBindViewHolder(holder: AbstractViewHolder<T>, position: Int) {
        val item = getItem(position)

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

    fun interface ClickListener<T> {
        fun onClick(item: T)
    }
}

class CommonDiffCallback<T : Identifiable> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem::class == newItem::class &&
            oldItem.getIdentifier() == newItem.getIdentifier()
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.hasSameContent(newItem)
    }
}
