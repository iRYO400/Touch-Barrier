package workshop.akbolatss.tools.barrier.ui.effects.enter

import android.os.Bundle
import androidx.lifecycle.Observer
import org.koin.androidx.scope.lifecycleScope
import org.koin.androidx.viewmodel.scope.viewModel
import workshop.akbolatss.tools.barrier.R
import workshop.akbolatss.tools.barrier.base.BaseFragment
import workshop.akbolatss.tools.barrier.databinding.FragmentEnterVfxListBinding

class EnterVfxSelectFragment(
    override val layoutId: Int = R.layout.fragment_enter_vfx_list
) : BaseFragment<FragmentEnterVfxListBinding>() {

    private val viewModel by lifecycleScope.viewModel<EnterVfxSelectViewModel>(this)

    private lateinit var adapter: EnterVfxRVA

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        initRv()
    }

    private fun initRv() {
        adapter = EnterVfxRVA(
            itemClickListener = {
                viewModel.enterVfxSelected(it)
            }, itemSettingsClickListener = {
                openVfxSettings()
            }
        )
        binding.rvEnterVfxList.adapter = adapter
    }

    private fun openVfxSettings() {

    }

    override fun setObserversListeners() {
        viewModel.enterVfxList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
        viewModel.selectedEnterVfx.observe(viewLifecycleOwner, Observer {
            adapter.selectedItemId = it.id
            adapter.notifyItemRangeChanged(0, adapter.itemCount, EnterVfxRVA.SELECTED_ITEM_ID)
        })
    }

}
