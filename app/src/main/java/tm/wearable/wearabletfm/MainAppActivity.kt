package tm.wearable.wearabletfm

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import tm.wearable.wearabletfm.data.model.User
import tm.wearable.wearabletfm.data.viewmodel.CalendarViewModel
import tm.wearable.wearabletfm.data.viewmodel.UserViewModel
import tm.wearable.wearabletfm.databinding.ActivityLoginBinding
import tm.wearable.wearabletfm.databinding.ActivityMainAppBinding
import tm.wearable.wearabletfm.databinding.BottomBarBinding
import tm.wearable.wearabletfm.databinding.MainToolbarBinding
import tm.wearable.wearabletfm.fragments.DataFragment
import tm.wearable.wearabletfm.fragments.FriendFragment
import tm.wearable.wearabletfm.fragments.HomeFragment
import tm.wearable.wearabletfm.fragments.WearableFragment
import java.text.SimpleDateFormat
import java.util.*

import tm.wearable.wearabletfm.utils.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MainAppActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainAppBinding
    private lateinit var toolbarAppBinding: MainToolbarBinding
    private lateinit var bottomBarBinding: BottomBarBinding
    private var dateSelected = GregorianCalendar()
    private val calendarViewModel: CalendarViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var user: User
    private lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbarAppBinding = MainToolbarBinding.bind(binding.root)
        bottomBarBinding = BottomBarBinding.bind(binding.root)
        setSupportActionBar(toolbarAppBinding.toolbar)

        try {
            sharedPref = this@MainAppActivity.getSharedPreferences(
                getString(R.string.shared_preferences), Context.MODE_PRIVATE)
            user = User(JSONObject(sharedPref!!.getString("user","")))
            getTokenFirebase(user = user)
        }catch (e: java.lang.Exception){
            e.printStackTrace()
        }
        events()
        coroutines()
        chooseSelectionMenu(fragment = HomeFragment.newInstance())

    }

    fun getTokenFirebase(user: User) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            val token = task.result
            userViewModel.update_token(user_id = user.id.toString(), token = token)
        })
    }

    fun events() {

        bottomBarBinding.rvHome.setOnClickListener {
            chooseSelectionMenu(fragment = HomeFragment.newInstance())
        }
        bottomBarBinding.rvWearable.setOnClickListener {
            chooseSelectionMenu(fragment = WearableFragment.newInstance())
        }
        bottomBarBinding.rvFriend.setOnClickListener {
            chooseSelectionMenu(fragment = FriendFragment.newInstance())
        }
        bottomBarBinding.rvData.setOnClickListener {
            chooseSelectionMenu(fragment = DataFragment.newInstance())
        }



        toolbarAppBinding.calendar.setOnClickListener {
            if (dateSelected == null)
                dateSelected = GregorianCalendar()
            val year = dateSelected.get(Calendar.YEAR)
            val month = dateSelected.get(Calendar.MONTH)
            val day = dateSelected.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(this@MainAppActivity, DatePickerDialog.OnDateSetListener{
                    view, year, monthOfYear, dayOfMonth ->
                val newC = GregorianCalendar()
                newC.set(GregorianCalendar.YEAR, year)
                newC.set(GregorianCalendar.MONTH, monthOfYear)
                newC.set(GregorianCalendar.DAY_OF_MONTH, dayOfMonth)
                calendarViewModel.dateSelected(dateSelected = newC, true)
            }, year, month, day)
            dpd.datePicker.maxDate = GregorianCalendar().time.time
            dpd.show()
        }

        toolbarAppBinding.profile.setOnClickListener {
            startActivity(Intent(this,NotificationsActivity::class.java))
        }
    }

    fun coroutines() {
        calendarViewModel.SelectedDate.observe(this@MainAppActivity, androidx.lifecycle.Observer { dateS ->
            when(dateS){
                is Result.Success<*> ->{
                    if (dateS!= null){
                        val result = dateS.data as CalendarViewModel.selectedCalendarByView
                        dateSelected = result.date
                    }
                }
                else -> Unit
            }
        })

    }

    fun chooseSelectionMenu(fragment: Fragment){
        when(fragment){
            is HomeFragment -> {
                appeareance(
                    cardView = bottomBarBinding.cvHome,
                    imageView = bottomBarBinding.imHome,
                    colorCv = R.color.green_b1,
                    colorIm = R.color.white
                )

                appeareance(
                    cardView = bottomBarBinding.cvWearable,
                    imageView = bottomBarBinding.imWearable,
                    colorCv = R.color.white,
                    colorIm = R.color.black
                )

                appeareance(
                    cardView = bottomBarBinding.cvFriend,
                    imageView = bottomBarBinding.imFriend,
                    colorCv = R.color.white,
                    colorIm = R.color.black
                )

                appeareance(
                    cardView = bottomBarBinding.cvData,
                    imageView = bottomBarBinding.imData,
                    colorCv = R.color.white,
                    colorIm = R.color.black
                )
            }
            is WearableFragment -> {

                appeareance(
                    cardView = bottomBarBinding.cvHome,
                    imageView = bottomBarBinding.imHome,
                    colorCv = R.color.white,
                    colorIm = R.color.black
                )

                appeareance(
                    cardView = bottomBarBinding.cvWearable,
                    imageView = bottomBarBinding.imWearable,
                    colorCv = R.color.green_b1,
                    colorIm = R.color.white
                )

                appeareance(
                    cardView = bottomBarBinding.cvFriend,
                    imageView = bottomBarBinding.imFriend,
                    colorCv = R.color.white,
                    colorIm = R.color.black
                )

                appeareance(
                    cardView = bottomBarBinding.cvData,
                    imageView = bottomBarBinding.imData,
                    colorCv = R.color.white,
                    colorIm = R.color.black
                )

            }
            is FriendFragment -> {
                appeareance(
                    cardView = bottomBarBinding.cvHome,
                    imageView = bottomBarBinding.imHome,
                    colorCv = R.color.white,
                    colorIm = R.color.black
                )

                appeareance(
                    cardView = bottomBarBinding.cvWearable,
                    imageView = bottomBarBinding.imWearable,
                    colorCv = R.color.white,
                    colorIm = R.color.black
                )

                appeareance(
                    cardView = bottomBarBinding.cvFriend,
                    imageView = bottomBarBinding.imFriend,
                    colorCv = R.color.green_b1,
                    colorIm = R.color.white
                )

                appeareance(
                    cardView = bottomBarBinding.cvData,
                    imageView = bottomBarBinding.imData,
                    colorCv = R.color.white,
                    colorIm = R.color.black
                )

            }
            is DataFragment -> {
                appeareance(
                    cardView = bottomBarBinding.cvHome,
                    imageView = bottomBarBinding.imHome,
                    colorCv = R.color.white,
                    colorIm = R.color.black
                )

                appeareance(
                    cardView = bottomBarBinding.cvWearable,
                    imageView = bottomBarBinding.imWearable,
                    colorCv = R.color.white,
                    colorIm = R.color.black
                )

                appeareance(
                    cardView = bottomBarBinding.cvFriend,
                    imageView = bottomBarBinding.imFriend,
                    colorCv = R.color.white,
                    colorIm = R.color.black
                )

                appeareance(
                    cardView = bottomBarBinding.cvData,
                    imageView = bottomBarBinding.imData,
                    colorCv = R.color.green_b1,
                    colorIm = R.color.white
                )

            }
        }
        supportFragmentManager.commit {
            setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out)
            replace(R.id.frameContainer,fragment)
            setReorderingAllowed(true)
        }
    }

    fun appeareance(cardView: CardView, imageView: ImageView, colorCv: Int, colorIm: Int){
        cardView.setCardBackgroundColor(ContextCompat.getColor(this, colorCv))
        imageView.setColorFilter(ContextCompat.getColor(this, colorIm), PorterDuff.Mode.SRC_IN)
    }
}