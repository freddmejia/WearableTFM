package tm.wearable.wearabletfm.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

import tm.wearable.wearabletfm.R
import tm.wearable.wearabletfm.data.model.Health
import tm.wearable.wearabletfm.data.model.User
import tm.wearable.wearabletfm.databinding.HomeFragmentBinding

@AndroidEntryPoint
class HomeFragment: Fragment(R.layout.home_fragment) {
    private var binding: HomeFragmentBinding ? = null
    private lateinit var user: User
    private lateinit var health: Health
    private lateinit var sharedPref: SharedPreferences
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

        try {
            user = User(JSONObject(sharedPref!!.getString("user","")))
            health = Health(jsonObject = JSONObject(sharedPref!!.getString("health","")))
        }catch (e: java.lang.Exception){

        }

        binding?.username?.text = user.name
        binding?.weightUser?.text = health.weight
        binding?.sizeUser?.text = health.height
        binding?.oldUser?.text = health.yearOld.toString()
        events()
    }

    fun events(){
        binding?.userEdit?.setOnClickListener {

        }

        binding?.cvHealth?.setOnClickListener {

        }
    }
}