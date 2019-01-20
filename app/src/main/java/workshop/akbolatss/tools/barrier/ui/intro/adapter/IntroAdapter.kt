package workshop.akbolatss.tools.barrier.ui.intro.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.rv_tutorial.view.*
import workshop.akbolatss.tools.barrier.R

class IntroAdapter(private val list: List<IntroAction>,
                   private val clickListener: (IntroAction, Int) -> Unit) : RecyclerView.Adapter<IntroAdapter.IntroVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): IntroVH {
        val inflater = LayoutInflater.from(parent.context)
        return IntroVH(inflater.inflate(R.layout.rv_tutorial, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: IntroVH, position: Int) {
        val introAction = list[position]
        holder.bind(introAction, clickListener)
    }

    class IntroVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(introAction: IntroAction, clickListener: (IntroAction, Int) -> Unit) {
            itemView.grant.setOnClickListener {
                clickListener(introAction, adapterPosition)
            }
            itemView.title.text = itemView.context.getText(introAction.titleResourceId)
            itemView.description.text = itemView.context.getText(introAction.descriptionResourceId)

            if (introAction.imageResourceId != null)
                itemView.action_image.setImageResource(introAction.imageResourceId)
        }
    }
}