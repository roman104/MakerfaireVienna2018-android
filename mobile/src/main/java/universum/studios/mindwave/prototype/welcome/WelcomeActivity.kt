package universum.studios.mindwave.prototype.welcome

import android.app.Activity
import android.os.Bundle
import android.view.View

/**
 * @author Martin Albedinsky
 */
class WelcomeActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(View(this))
    }
}