package ru.practicum.android.diploma.search.ui.activity
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


abstract class AbstractViewHolder<T>(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: T)
}
