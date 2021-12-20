package androidNinja.playStore.android.mynotesapp.fragments.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidNinja.playStore.android.mynotesapp.R
import androidNinja.playStore.android.mynotesapp.data.models.NotesEntity
import androidNinja.playStore.android.mynotesapp.data.models.Priority
import androidNinja.playStore.android.mynotesapp.fragments.list.ListFragmentDirections
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    var dataList = emptyList<NotesEntity>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.title_text).text = dataList[position].title
        holder.itemView.findViewById<TextView>(R.id.description_text).text = dataList[position].description

        holder.itemView.findViewById<ConstraintLayout>(R.id.row_background).setOnClickListener{
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(dataList[position])
            holder.itemView.findNavController().navigate(action)
        }

        val priority = dataList[position].priority
        when (priority) {
            Priority.HIGH ->
                holder.itemView.findViewById<CardView>(R.id.priority_indicator)
                    .setCardBackgroundColor(
                        ContextCompat.getColor(
                            holder.itemView.context,
                            R.color.red
                        )
                    )
            Priority.LOW ->
                holder.itemView.findViewById<CardView>(R.id.priority_indicator)
                    .setCardBackgroundColor(
                        ContextCompat.getColor(
                            holder.itemView.context,
                            R.color.green
                        )
                    )
            Priority.MEDIUM ->
                holder.itemView.findViewById<CardView>(R.id.priority_indicator)
                    .setCardBackgroundColor(
                        ContextCompat.getColor(
                            holder.itemView.context,
                            R.color.yellow
                        )
                    )
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(notesEntity: List<NotesEntity>){
        val notesDiffUtil = NotesDiffUtil(dataList, notesEntity)
        val notesDiffResult = DiffUtil.calculateDiff(notesDiffUtil)
        this.dataList=notesEntity
        notesDiffResult.dispatchUpdatesTo(this)
    }
}