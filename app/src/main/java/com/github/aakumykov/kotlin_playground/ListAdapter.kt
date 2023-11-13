package com.github.aakumykov.kotlin_playground

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ListAdapter (
    context: Context,
    private val resource: Int,
    private val list: List<TitleItem>
)
    : ArrayAdapter<ListAdapter.TitleItem>(context, resource, list)
{
    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val viewHolder: ViewHolder

        val itemView = if (convertView == null) {
            val itemView = inflater.inflate(resource, parent, false)
            viewHolder = ViewHolder(itemView)
            itemView.tag = viewHolder
            itemView
        }
        else {
            viewHolder = convertView.tag as ViewHolder
            convertView
        }

        viewHolder.titleView.text = list[position].getTitle()

        return itemView
    }

    private inner class ViewHolder(view: View) {

        val titleView: TextView

        init {
            titleView = view.findViewById(R.id.titleView)
        }
    }

    interface TitleItem {
        fun getTitle(): String
    }

    class SimpleTitleItem constructor(private val title: String) : TitleItem {
        override fun getTitle(): String = title
    }
}