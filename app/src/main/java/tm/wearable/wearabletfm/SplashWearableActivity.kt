package tm.wearable.wearabletfm

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import tm.wearable.wearabletfm.databinding.ActivitySplashWearableBinding

class SplashWearableActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashWearableBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashWearableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            navigateToActivity()
        }, 2000)
    }

    fun navigateToActivity(){
        val prefsUser = this@SplashWearableActivity?.getSharedPreferences(
            resources.getString(R.string.shared_preferences),
            Context.MODE_PRIVATE
        )
        val islogged = prefsUser?.getBoolean("islogged",false)?:false

        var targetActivity = if (islogged) MainAppActivity::class.java else MainActivity::class.java
        startActivity(Intent(this@SplashWearableActivity, targetActivity))
        finish()
    }
}