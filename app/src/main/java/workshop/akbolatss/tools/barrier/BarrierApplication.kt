package workshop.akbolatss.tools.barrier

import android.annotation.TargetApi
import android.app.Application
import android.os.Build
import android.provider.Settings
import com.orhanobut.hawk.Hawk
import workshop.akbolatss.tools.barrier.di.KoinInjector

class BarrierApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        KoinInjector.init(this)
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
