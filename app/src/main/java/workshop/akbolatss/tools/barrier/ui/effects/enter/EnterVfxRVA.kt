package workshop.akbolatss.tools.barrier.ui.effects.enter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import workshop.akbolatss.tools.barrier.R
import workshop.akbolatss.tools.barrier.base.BaseRVA
import workshop.akbolatss.tools.barrier.base.DataBoundViewHolder
import workshop.akbolatss.tools.barrier.databinding.ItemEnterVfxBinding

class EnterVfxRVA(
    private val itemClickListener: (IEnterVfx) -> Unit,
    private val itemSettingsClickListener: (IEnterVfx) -> Unit
) : BaseRVA<IEnterVfx>(DIFF_CALLBACK) {

    companion object {
        const val SELECTED_ITEM_ID = "_selectedItemId"
        const val REFRESH_SELECTED = "_refreshSelected"
    }

    var selectedItemId = -1

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return DataBindingUtil.inflate<ItemEnterVfxBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_enter_vfx,
            parent, false
        )
    }

    override fun onBindViewHolder(
        holder: DataBoundViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else
            bindWithPayload(holder, payloads)
    }

    override fun bind(holder: DataBoundViewHolder, item: IEnterVfx) {
        with(holder.binding as ItemEnterVfxBinding) {
            model = item

            root.setOnClickListener {
                itemClickListener(item)
            }

            imgSettings.setOnClickListener {
                itemSettingsClickListener(item)
            }
        }
    }

    private fun bindWithPayload(
        holder: DataBoundViewHolder,
        payloads: MutableList<Any>
    ) {
        with(holder.binding as ItemEnterVfxBinding) {
            if (payloads.contains(REFRESH_SELECTED))
                clRoot.isSelected = false

            if (payloads.contains(SELECTED_ITEM_ID))
                clRoot.isSelected = true
        }
    }
}

private val DIFF_CALLBACK: DiffUtil.ItemCallback<IEnterVfx> =
    object : DiffUtil.ItemCallback<IEnterVfx>() {

        override fun areItemsTheSame(
            oldItem: IEnterVfx,
            newItem: IEnterVfx
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: IEnterVfx,
            newItem: IEnterVfx
        ): Boolean {
            return oldItem.nameRes == newItem.nameRes
        }
    }
