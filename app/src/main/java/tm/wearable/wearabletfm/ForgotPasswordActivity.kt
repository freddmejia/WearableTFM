package tm.wearable.wearabletfm

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import tm.wearable.wearabletfm.data.adapter.NotificationAdapter
import tm.wearable.wearabletfm.data.model.Notification
import tm.wearable.wearabletfm.data.model.User
import tm.wearable.wearabletfm.data.repository.api.forgotPass
import tm.wearable.wearabletfm.data.viewmodel.UserViewModel
import tm.wearable.wearabletfm.databinding.ActivityForgotPasswordBinding
import tm.wearable.wearabletfm.databinding.ActivityNotificationsBinding
import tm.wearable.wearabletfm.databinding.MainToolbarBinding
import tm.wearable.wearabletfm.utils.CompositionObj
import tm.wearable.wearabletfm.utils.Result

@AndroidEntryPoint
class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var user: User
    private lateinit var toast: Toast
    private lateinit var toolbarAppBinding: MainToolbarBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpToolBar()
        user = User()
        toast = Toast(this)
        sharedPreferences = this.getSharedPreferences(
            getString(R.string.shared_preferences), Context.MODE_PRIVATE)

        showLayoutPass(isVisible = false)

        api()
        events()
        coroutines()

    }

    fun api() {
        userViewModel.fetch_notification_by_user(user_id = user.id.toString())
    }

    fun coroutines(){
        lifecycleScope.launchWhenCreated {
            userViewModel.loadingProgress.collect {
                binding.linear.isVisible = !it
                binding.progressBar.isVisible = it
            }
        }

        lifecycleScope.launchWhenCreated {
            userViewModel.compositionRecoverAccount.collect {result ->
                when(result) {
                    is Result.Success<CompositionObj<forgotPass, String>> -> {
                        user.id = result.data.data.id
                        user.name = result.data.data.name
                        user.email = result.data.data.email
                        showLayoutPass(isVisible = true)
                        showToast(message = result.data.message)
                    }
                    is Result.Error -> {
                        showLayoutPass(isVisible = false)
                        showToast(message = result.error)
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            userViewModel.compositionLogin.collect {result ->
                when(result) {
                    is Result.Success<CompositionObj<User, String>> -> {
                        with(sharedPreferences?.edit()){
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
                        val intent = Intent(this@ForgotPasswordActivity, MainAppActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)

                        finish()
                    }
                    is Result.Error -> {
                        showLayoutPass(isVisible = false)
                        showToast(message = result.error)
                    }
                    else -> Unit
                }
            }
        }

    }


    fun events() {
        binding.cvSend.setOnClickListener {
            val email = binding.etUsername.text.toString()
            if (email.trim().isEmpty() ){
                showToast(message = resources.getString(R.string.error_fill))
                return@setOnClickListener
            }

            userViewModel.forgot_password_step_one(email = email)

        }

        binding.cvSave.setOnClickListener {
            val password = binding.etPassword.text.toString()
            val password1 = binding.etPassword1.text.toString()
            if (password.trim().isEmpty() && password1.trim().isEmpty() && password == password1 ){
                showToast(message = resources.getString(R.string.error_fill))
                return@setOnClickListener
            }

            userViewModel.update_password_step_last(
                user_id = user.id.toString(),
                name = user.name,
                email = user.email,
                password = password,
                c_password = password1
            )

        }
    }

    fun showToast(message: String){
        toast?.cancel()
        toast = Toast.makeText(this,message, Toast.LENGTH_LONG)
        toast.show()
    }

    fun setUpToolBar() {
        toolbarAppBinding = MainToolbarBinding.bind(binding.root)
        setSupportActionBar(toolbarAppBinding.toolbar)
        toolbarAppBinding.titleBar.text = resources.getString(R.string.recover_account)
        toolbarAppBinding.profile.isVisible = false
        toolbarAppBinding.calendar.isVisible = false
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->
            {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showLayoutPass(isVisible : Boolean) {
        binding.cvPassword.isVisible = isVisible
        binding.cvPassword1.isVisible = isVisible
        binding.cvSave.isVisible = isVisible
    }
}