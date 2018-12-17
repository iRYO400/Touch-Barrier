package wei.mark.standouttest

import android.app.Application
import com.orhanobut.hawk.Hawk

class BarrierApplication: Application(){

    override fun onCreate() {
        super.onCreate()
        Hawk.init(applicationContext).build()
    }
}