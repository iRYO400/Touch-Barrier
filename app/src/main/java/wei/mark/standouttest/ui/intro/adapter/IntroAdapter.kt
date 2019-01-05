package wei.mark.standouttest.ui.intro.adapter

import android.arch.lifecycle.MutableLiveData
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import wei.mark.standouttest.R

class IntroAdapter(private val list: MutableLiveData<List<IntroAction>>) : RecyclerView.Adapter<IntroAdapter.IntroVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): IntroVH {
        val inflater = LayoutInflater.from(parent.context)
        return IntroVH(inflater.inflate(R.layout.rv_tutorial, parent, false))
    }

    override fun getItemCount(): Int {
        if (list.value == null)
            return 0
        return list.value!!.size
    }

    override fun onBindViewHolder(holder: IntroVH, position: Int) {
//        holder.bind(list[position])
        when (position) {
            0 -> {

            }
            1 -> {
            }
            2 -> {
            }
            3 -> {
            }
            4 -> {
            }
        }
    }


    class IntroVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind() {
        }
    }
}

//class CategoriesAdapter(private var mSelectedList: ArrayList<TagResponse>,
//                        private val clickListener: (TagResponse, Int) -> Unit) :
//        RecyclerView.Adapter<CategoriesAdapter.ComponentVH>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComponentVH {
//        val inflater = LayoutInflater.from(parent.context)
//        return ComponentVH(inflater.inflate(R.layout.rv_category, parent, false))
//    }
//
//    override fun onBindViewHolder(holder: ComponentVH, position: Int) {
//        holder.bind(mSelectedList[position], clickListener)
//    }
//
//    fun onUpdateItem(tagResponse: TagResponse, position: Int) {
//        mSelectedList[position] = tagResponse
//        notifyItemChanged(position)
//    }
//
//    override fun getItemCount(): Int {
//        return mSelectedList.size
//    }
//
//    class ComponentVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        fun bind(tagResponse: TagResponse, clickListener: (TagResponse, Int) -> Unit) {
//            itemView.tvName.text = tagResponse.name
//            fabState(tagResponse.isSelected)
//
//            GlideHelper.loadPreview(itemView.imgPreview, tagResponse.previews!![Random().nextInt(tagResponse.previews.size - 1)])
//
//            itemView.chipGroup.removeAllViews()
//            for (tag in tagResponse.tags!!) {
//                val chip = Chip(itemView.context)
//                chip.text = "#$tag"
//                chip.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorText))
//                chip.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.colorPrimaryDark))
//                itemView.chipGroup.addView(chip)
//            }
//            itemView.fabSelect.setOnClickListener {
//                clickListener(tagResponse, adapterPosition)
//            }
//        }
//
//        private fun fabState(isSelected: Boolean) {
//            if (isSelected) {
//                itemView.fabSelect.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_close_24dp))
//            } else {
//                itemView.fabSelect.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_add_24))
//            }
//        }
//    }
//}