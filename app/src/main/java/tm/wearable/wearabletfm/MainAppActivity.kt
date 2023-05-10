package tm.wearable.wearabletfm

import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import dagger.hilt.android.AndroidEntryPoint
import tm.wearable.wearabletfm.data.viewmodel.UserViewModel
import tm.wearable.wearabletfm.databinding.ActivityLoginBinding
import tm.wearable.wearabletfm.databinding.ActivityMainAppBinding
import tm.wearable.wearabletfm.databinding.BottomBarBinding
import tm.wearable.wearabletfm.databinding.MainToolbarBinding
import tm.wearable.wearabletfm.fragments.DataFragment
import tm.wearable.wearabletfm.fragments.FriendFragment
import tm.wearable.wearabletfm.fragments.HomeFragment
import tm.wearable.wearabletfm.fragments.WearableFragment

@AndroidEntryPoint
class MainAppActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainAppBinding
    private lateinit var toolbarAppBinding: MainToolbarBinding
    private lateinit var bottomBarBinding: BottomBarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbarAppBinding = MainToolbarBinding.bind(binding.root)
        bottomBarBinding = BottomBarBinding.bind(binding.root)
        setSupportActionBar(toolbarAppBinding.toolbar)

        events()
        chooseSelectionMenu(fragment = HomeFragment.newInstance())
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