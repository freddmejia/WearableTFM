package tm.wearable.wearabletfm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import tm.wearable.wearabletfm.databinding.ActivityLoginBinding
import tm.wearable.wearabletfm.databinding.ActivityMyHealthBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var loginBinding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)
    }
}