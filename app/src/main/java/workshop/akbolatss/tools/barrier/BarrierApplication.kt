package workshop.akbolatss.tools.barrier

import android.annotation.TargetApi
import android.app.Application
import android.os.Build
import android.provider.Settings
import com.orhanobut.hawk.Hawk

class BarrierApplication : Application() {

    companion object {
        lateinit var instance: BarrierApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Hawk.init(applicationContext).build()
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun canDrawOverApps(): Boolean {
        return Settings.canDrawOverlays(this)
    }
}