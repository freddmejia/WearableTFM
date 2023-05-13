package tm.wearable.wearabletfm.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import tm.wearable.wearabletfm.MainAppActivity
import tm.wearable.wearabletfm.data.viewmodel.UserViewModel
import tm.wearable.wearabletfm.databinding.ActivityLoginBinding
import tm.wearable.wearabletfm.databinding.ActivityMyHealthBinding

import tm.wearable.wearabletfm.R
import tm.wearable.wearabletfm.data.model.User

import tm.wearable.wearabletfm.utils.*
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var loginBinding: ActivityLoginBinding
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var toast: Toast
    private var isRegister = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        toast = Toast(this)
        loginBinding.cvUsername.isVisible = false
        loginBinding.tvRegNew.text = resources.getString(R.string.new_user)
        loginBinding.tvTitle.text = resources.getString(R.string.access_title)
        events()
        coroutines()
    }

    fun events() {
        loginBinding.tvRegNew.setOnClickListener {
            isRegister = !isRegister

            loginBinding.cvUsername.isVisible = isRegister
            loginBinding.tvRegNew.text = if (isRegister) resources.getString(R.string.access_title) else
                resources.getString(R.string.new_user)
            loginBinding.tvTitle.text = if (isRegister) resources.getString(R.string.register_title) else
                resources.getString(R.string.access_title)
        }

        loginBinding.cvLogin.setOnClickListener {
            val  email = loginBinding.etEmail.text.toString()
            val password = loginBinding.etPassword.text.toString()
            val username = loginBinding.etUsername.text.toString()
            if (email.trim().isEmpty() && password.trim().isEmpty()){
                showToast(message = resources.getString(R.string.error_fill))
                return@setOnClickListener
            }
            if (isRegister) {
                if (email.trim().isEmpty()) {
                    showToast(message = resources.getString(R.string.error_fill))
                    return@setOnClickListener
                }
                userViewModel.register(
                    username = username,
                    email = email,
                    password = password,
                    password2 = password
                )
            }
            else{
                userViewModel.login(
                    email = email,
                    password = password
                )
            }
        }
    }

    fun coroutines() {
        lifecycleScope.launchWhenCreated {
            userViewModel.compositionLogin.collect { result ->
                when(result){
                    is Result.Success<CompositionObj<User,String>> ->{
                        val sharedPref = this@LoginActivity.getSharedPreferences(
                            getString(R.string.shared_preferences), Context.MODE_PRIVATE)
                        with(sharedPref?.edit()){
                            var json = JSONObject()

                            json.put("id",result.data.data.id)
                            json.put("token",result.data.data.token)
                            json.put("name",result.data.data.name)
                            json.put("email",result.data.data.email)
                            json.put("token_firebase",result.data.data.token_firebase.toString())
                            json.put("image",result.data.data.image.toString())
                            json.put("code_qr",result.data.data.code_qr.toString())
                            json.put("language","es")


                            this?.putString("user",json.toString())
                            this?.putString("token",result.data.data.token)
                            this?.putBoolean("islogged",true)
                            json = JSONObject()
                            json.put("id",result.data.data.health?.id.toString())
                            json.put("height",result.data.data.health?.height.toString())
                            json.put("weight",result.data.data.health?.weight.toString())
                            json.put("birthay",result.data.data.health?.birthay.toString())
                            json.put("yearOld",result.data.data.health?.yearOld.toString())
                            this?.putString("health",json.toString())
                            this?.apply()
                        }
                        finish()
                        startActivity(
                            Intent(this@LoginActivity, MainAppActivity::class.java)
                        )
                    }
                    is  Result.Error -> {
                        showToast(message =  result.error)
                    }
                    else -> Unit
                }
            }
        }


        lifecycleScope.launchWhenCreated {
            userViewModel.loadingProgress.collect {
                loginBinding.linear.isVisible = !it
                loginBinding.progressBar.isVisible = it
            }
        }
    }

    fun showToast(message: String){
        toast?.cancel()
        toast = Toast.makeText(this@LoginActivity,message,Toast.LENGTH_LONG)
        toast.show()
    }
}