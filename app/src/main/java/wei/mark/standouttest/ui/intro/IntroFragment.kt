package wei.mark.standouttest.ui.intro

import android.Manifest
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.PagerSnapHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import kotlinx.android.synthetic.main.intro_fragment.*
import wei.mark.standouttest.R
import wei.mark.standouttest.ui.intro.adapter.IntroAction
import wei.mark.standouttest.ui.intro.adapter.IntroAdapter
import wei.mark.standouttest.ui.settings.SettingsActivity

class IntroFragment : Fragment() {

    companion object {
        fun newInstance() = IntroFragment()
    }

    private lateinit var viewModel: IntroViewModel

    private lateinit var adapter: IntroAdapter

    private lateinit var callback: IntroFragmentCallback

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = (context as SettingsActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.intro_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(IntroViewModel::class.java)

        initAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ready.setOnClickListener {
            hideIntroFragment()
        }
    }

    private fun hideIntroFragment() {
        callback.onCloseIntro()
    }

    private fun toggleNavigationBar() {

        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_SECURE_SETTINGS)
                == PackageManager.PERMISSION_GRANTED) {
            val windowManagerService = ContextCompat.getSystemService(activity!!, WindowManager::class.java)
        }
    }

    private fun initAdapter() {
        adapter = IntroAdapter(getTutorials())
        recycler_view.adapter = adapter
        PagerSnapHelper().attachToRecyclerView(recycler_view)
    }

    private fun getTutorials(): MutableLiveData<List<IntroAction>> {
        val tutorials = ArrayList<IntroAction>()
//        tutorials.add(R.string.tutorial_1, R.drawable.tutorial_1, null)
        val liveData = MutableLiveData<List<IntroAction>>()
        liveData.value = tutorials
        return liveData
    }
}
