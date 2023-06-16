package tm.wearable.wearabletfm.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import tm.wearable.wearabletfm.ui.AddMedicineActivity

import tm.wearable.wearabletfm.R
import tm.wearable.wearabletfm.data.adapter.MedicinesLittleAdapter
import tm.wearable.wearabletfm.data.interfaces.UIUserHealth
import tm.wearable.wearabletfm.data.interfaces.UIUserProfile
import tm.wearable.wearabletfm.data.model.Health
import tm.wearable.wearabletfm.data.model.Medicine
import tm.wearable.wearabletfm.data.model.User
import tm.wearable.wearabletfm.data.model.shortUser
import tm.wearable.wearabletfm.data.viewmodel.MedicineViewModel
import tm.wearable.wearabletfm.data.viewmodel.UserViewModel
import tm.wearable.wearabletfm.databinding.HomeFragmentBinding
import tm.wearable.wearabletfm.ui.MedicineActivity
import tm.wearable.wearabletfm.utils.CompositionObj
import tm.wearable.wearabletfm.utils.WearableDialogs

@AndroidEntryPoint
class HomeFragment: Fragment(R.layout.home_fragment), UIUserProfile, UIUserHealth {
    private var binding: HomeFragmentBinding ? = null
    private lateinit var user: User
    private lateinit var health: Health
    private lateinit var sharedPref: SharedPreferences
    private lateinit var toast: Toast
    private val userViewModel: UserViewModel by viewModels()
    private val medicineViewModel: MedicineViewModel by viewModels()
    private lateinit var medicinesLittleAdapter: MedicinesLittleAdapter
    companion object{
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentBinding = HomeFragmentBinding.bind(view)
        binding = fragmentBinding!!
        sharedPref = this.requireActivity().getSharedPreferences(
            getString(R.string.shared_preferences), Context.MODE_PRIVATE)
        toast = Toast(this@HomeFragment.requireContext())
        try {
            user = User(JSONObject(sharedPref!!.getString("user","")))
            health = Health(jsonObject = JSONObject(sharedPref!!.getString("health","")))
        }catch (e: java.lang.Exception){

        }

        medicinesLittleAdapter = MedicinesLittleAdapter(this@HomeFragment.requireContext(), arrayListOf())
        binding?.rvMedicines?.layoutManager = LinearLayoutManager(this@HomeFragment.requireContext())
        binding?.rvMedicines?.adapter = medicinesLittleAdapter

        showUserProfile(user = user)
        showUserHealth(health = health)
        events()
        coroutines()
    }

    fun api(){
        medicineViewModel.fetch_three_last_medicines_by_user()
    }

    fun events() {
        binding?.userEdit?.setOnClickListener {
            WearableDialogs.update_profile_user(
                context = this@HomeFragment.requireContext(),
                observer = this,
                user = user,
                message = resources.getString(R.string.error_fill)
            )
        }

        binding?.cvHealth?.setOnClickListener {

            WearableDialogs.update_health_user(
                context = this@HomeFragment.requireContext(),
                observer = this,
                health = health,
                message = resources.getString(R.string.error_fill),
                fragmentManager = this@HomeFragment.requireActivity().supportFragmentManager
            )
        }

        binding?.addMedicine?.setOnClickListener {
            startActivity(Intent(this@HomeFragment.requireActivity(), AddMedicineActivity::class.java))
        }

        binding?.cvMedicines?.setOnClickListener {
            startActivity(Intent(this@HomeFragment.requireActivity(), MedicineActivity::class.java))
        }
    }

    fun coroutines() {
        lifecycleScope.launchWhenCreated {
            userViewModel.loadingProgress.collect {
                binding?.linear?.isVisible = !it
                binding?.progressBar?.isVisible = it
            }
        }
        lifecycleScope.launchWhenCreated {
            userViewModel.compositionUpdateUser.collect { result ->
                when(result) {
                    is tm.wearable.wearabletfm.utils.Result.Success<CompositionObj<shortUser, String>> -> {

                        user.name = result.data.data.name
                        user.email = result.data.data.email
                        showUserProfile(user = user)
                        with(sharedPref?.edit()){
                            var json = JSONObject()

                            json.put("id",user.id.toString())
                            json.put("token",user.token)
                            json.put("name",user.name)
                            json.put("email",user.email)
                            json.put("token_firebase",result.data.data.token_firebase.toString())
                            json.put("image",result.data.data.image.toString())
                            json.put("code_qr",result.data.data.code_qr.toString())
                            json.put("language","es")

                            this?.putString("user",json.toString())
                            this?.apply()
                        }
                        showToast(message = result.data.message)

                    }
                    is tm.wearable.wearabletfm.utils.Result.Error-> {
                        showToast(message =  result.error)
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            userViewModel.compositionUpdateHealthUser.collect { result ->
                when(result) {
                    is tm.wearable.wearabletfm.utils.Result.Success<CompositionObj<Health, String>> -> {
                        health.height = result.data.data.height
                        health.weight = result.data.data.weight
                        health.birthay = result.data.data.birthay
                        health.yearOld = result.data.data.yearOld
                        showUserHealth(health = health)
                        Log.e("", "coroutines: hea "+health.yearOld.toString() + " "+result.data.data.yearOld)
                        with(sharedPref?.edit()){
                            var json = JSONObject()
                            json.put("id",health.id.toString())
                            json.put("height",health.height)
                            json.put("weight",health.weight)
                            json.put("birthay",health.birthay)
                            json.put("yearOld",health.yearOld.toString())
                            this?.putString("health",json.toString())
                            this?.apply()
                        }
                        showToast(message = result.data.message)


                    }
                    is tm.wearable.wearabletfm.utils.Result.Error-> {
                        showToast(message =  result.error)
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            medicineViewModel.compositionMedicines.collect { result->
                when(result) {
                    is tm.wearable.wearabletfm.utils.Result.Success<CompositionObj<ArrayList<Medicine>, String>> ->{
                        medicinesLittleAdapter.setNewData(result.data.data)
                        binding?.rvMedicines?.isVisible = true
                        binding?.noData?.isVisible = false
                    }
                    is tm.wearable.wearabletfm.utils.Result.Error -> {
                        binding?.rvMedicines?.isVisible = false
                        binding?.noData?.isVisible = true
                    }
                    else -> Unit
                }
            }
        }

    }

    override fun save(user: User) {
        userViewModel.update_user(name = user.name, email = user.email, password = user.password)

    }

    override fun save(health: Health) {
        userViewModel.update_health_user(weight = health.weight, height = health.height, birthay = health.birthay)
    }

    override fun showMessage(message: String) {
        showToast(message = message)
    }
    fun showToast(message: String){
        toast?.cancel()
        toast = Toast.makeText(this@HomeFragment.requireContext(),message, Toast.LENGTH_LONG)
        toast.show()
    }

    fun showUserProfile (user: User) {
        binding?.username?.text = user.name
    }
    fun showUserHealth (health: Health) {
        binding?.weightUser?.text = health.weight
        binding?.sizeUser?.text = health.height
        binding?.oldUser?.text = health.yearOld.toString()
    }

    override fun onResume() {
        super.onResume()
        api()
    }

}