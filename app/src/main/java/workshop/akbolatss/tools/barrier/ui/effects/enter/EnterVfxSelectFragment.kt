package workshop.akbolatss.tools.barrier.ui.effects.enter

import android.os.Bundle
import androidx.lifecycle.Observer
import org.koin.androidx.scope.lifecycleScope
import org.koin.androidx.viewmodel.scope.viewModel
import timber.log.Timber
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
        Timber.d("init EnterVfxFragment")
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
        observeViewModel()
        setListeners()
    }

    private fun observeViewModel() {
        viewModel.enterVfxList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it) {
                viewModel.loadSelected()
            }
        })
        viewModel.selectedEnterVfx.observe(viewLifecycleOwner, Observer {
            refreshSelected()
            highlightSelected(it)
        })
    }

    private fun refreshSelected() {
        adapter.notifyItemRangeChanged(0, adapter.itemCount, EnterVfxRVA.REFRESH_SELECTED)
    }

    private fun highlightSelected(iEnterVfx: IEnterVfx?) {
        val selectedEnterVfxIndex = adapter.currentList.indexOf(iEnterVfx)
        adapter.notifyItemChanged(selectedEnterVfxIndex, EnterVfxRVA.SELECTED_ITEM_ID)
    }

    private fun setListeners() {
        binding.btnBack.setOnClickListener {
            viewModel.openSettings()
        }
    }
}
