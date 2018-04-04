package universum.studios.mindwave.prototype

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric
import universum.studios.mindwave.prototype.Config.Analytics

/**
 * @author Martin Albedinsky
 */
abstract class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics.Builder().core(CrashlyticsCore.Builder().disabled(Analytics.CRASHLYTICS_DISABLED).build()).build())
    }
}