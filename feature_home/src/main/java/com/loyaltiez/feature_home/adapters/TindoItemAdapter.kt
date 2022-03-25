package com.loyaltiez.feature_home.adapters

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.loyaltiez.core.domain.model.todo.ToDo
import com.loyaltiez.core.domain.model.todo.WeeklyToDo
import com.loyaltiez.feature_home.R
import com.loyaltiez.feature_home.databinding.TindoItemBinding

class TindoItemAdapter(
    private val mApplication: Application,
    val mEditTindoItemClickListener: EditTindoItemClickListener,
    val mDeleteTindoItemClickListener: DeleteTindoItemClickListener
) : ListAdapter<ToDo, TindoItemAdapter.ViewHolder>(ToDoDiffCallback()) {

    // Called when the recycler view needs a view holder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        return ViewHolder.from(parent)
    }

    // Display the data for one list item at position
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // Get the item at the correct position
        val item = getItem(position)

        // Bind the item data to holder Views
        holder.bind(
            item,
            mApplication,
            mEditTindoItemClickListener,
            mDeleteTindoItemClickListener
        )

    }

    class ViewHolder private constructor(private val binding: TindoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Bind the properties of the passed Business to the Views of the ViewHolder
        fun bind(
            item: ToDo,
            application: Application,
            editTindoItemClickListener: EditTindoItemClickListener,
            deleteTindoItemClickListener: DeleteTindoItemClickListener
        ) {

            // Get the resources for the view
            itemView.context.resources

            binding.tindo = item
            binding.editTindoItemClickListener = editTindoItemClickListener
            binding.deleteTindoItemClickListener = deleteTindoItemClickListener

            if (item is WeeklyToDo) {

                binding.chipTodoType.text = "Weekly"

                binding.tvTodoDate.text = item.getDateString()

                binding.tvTodoDate.visibility = View.VISIBLE
                binding.iconDate.visibility = View.VISIBLE
            } else {

                binding.chipTodoType.text = "Daily"
            }

            var favColor: Int? = null

            if (item.favourite){

                binding.iconFav.setColorFilter(R.color.favouriteColor,android.graphics.PorterDuff.Mode.MULTIPLY);
            } else {
                binding.iconFav.setColorFilter(R.color.primaryColor,android.graphics.PorterDuff.Mode.MULTIPLY);
            }

            binding.cardView.setCardBackgroundColor(application.getColor(item.color))

            binding.executePendingBindings()
        }

        /*  Inflate the View from the parent ViewGroup
          Needs to be in a companion object so it can be called on the ViewHolder class rather
          rather than an instance of the ViewHolder class*/
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)

                // inflate the party_item_view view
                val binding =
                    TindoItemBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    )

                return ViewHolder(binding)
            }

        }

    }
}

class ToDoDiffCallback : DiffUtil.ItemCallback<ToDo>() {

    override fun areItemsTheSame(
        oldItem: ToDo,
        newItem: ToDo
    ): Boolean {

        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: ToDo,
        newItem: ToDo
    ): Boolean {

        return oldItem == newItem
    }

}

class EditTindoItemClickListener(val clickListener: (todo: ToDo) -> Unit) {

    fun onClick(todo: ToDo) = clickListener(todo)
}

class DeleteTindoItemClickListener(val clickListener: (todo: ToDo) -> Unit) {

    fun onClick(todo: ToDo) = clickListener(todo)
}

