package universum.studios.mindwave.prototype

/**
 * @author Martin Albedinsky
 */
class Config {

    class Analytics {

        companion object {

            const val CRASHLYTICS_DISABLED = BuildConfig.BUILD_TYPE == "debug"
        }
    }
}