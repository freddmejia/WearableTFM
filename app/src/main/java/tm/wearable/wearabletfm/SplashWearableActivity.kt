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

            isUserLoggued()
        }, 2000)
    }

    fun isUserLoggued(){
        val prefsUser = this@SplashWearableActivity?.getSharedPreferences(
            resources.getString(R.string.shared_preferences),
            Context.MODE_PRIVATE
        )
        val islogged = prefsUser!!.getBoolean("islogged",false)
        if (!islogged){
            startActivity(
                Intent(this@SplashWearableActivity, MainActivity::class.java)
            )
            finish()
            return
        }
        startActivity(
            Intent(this@SplashWearableActivity, MainAppActivity::class.java)
        )
        finish()
    }
}