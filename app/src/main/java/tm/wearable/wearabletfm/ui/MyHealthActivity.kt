package tm.wearable.wearabletfm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import tm.wearable.wearabletfm.databinding.ActivityMyHealthBinding

class MyHealthActivity : AppCompatActivity() {
    private lateinit var myHealthBinding: ActivityMyHealthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myHealthBinding = ActivityMyHealthBinding.inflate(layoutInflater)
        setContentView(myHealthBinding.root)
    }
}