package wei.mark.standouttest

import android.annotation.TargetApi
import android.app.Application
import android.os.Build
import android.provider.Settings
import com.orhanobut.hawk.Hawk

class BarrierApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        Hawk.init(applicationContext).build()
    }


    companion object {
        lateinit var instance: BarrierApplication
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun canDrawOverApps(): Boolean {
        return Settings.canDrawOverlays(this)
    }
}