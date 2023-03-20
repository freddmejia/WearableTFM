package tm.wearable.wearabletfm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import tm.wearable.wearabletfm.databinding.ActivityMainBinding
import tm.wearable.wearabletfm.ui.LoginActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.cvContinue.setOnClickListener {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }
    }
}