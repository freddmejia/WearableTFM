package tm.wearable.wearabletfm

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import tm.wearable.wearabletfm.data.adapter.NotificationAdapter
import tm.wearable.wearabletfm.data.interfaces.UIObserverGeneric
import tm.wearable.wearabletfm.data.model.Notification
import tm.wearable.wearabletfm.data.model.User
import tm.wearable.wearabletfm.data.viewmodel.UserViewModel
import tm.wearable.wearabletfm.databinding.ActivityNotificationsBinding
import tm.wearable.wearabletfm.databinding.MainToolbarBinding
import tm.wearable.wearabletfm.utils.*
@AndroidEntryPoint
class NotificationsActivity : AppCompatActivity(), UIObserverGeneric<Notification> {
    private lateinit var notificationAdapter: NotificationAdapter
    private lateinit var binding: ActivityNotificationsBinding
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var user: User
    private lateinit var toast: Toast
    private lateinit var toolbarAppBinding: MainToolbarBinding
    private var notificationsList : ArrayList<Notification> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpToolBar()

        toast = Toast(this)
        val sharedPref = this.getSharedPreferences(
            getString(R.string.shared_preferences), Context.MODE_PRIVATE)

        try {
            user = User(JSONObject(sharedPref!!.getString("user","")))
        }catch (e: java.lang.Exception){
        }
        notificationsList = arrayListOf()
        notificationAdapter = NotificationAdapter(context = this, list =  notificationsList, observer = this)

        binding.rvNotifications.layoutManager = LinearLayoutManager(this)
        binding.rvNotifications.adapter = notificationAdapter
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvNotifications)

        api()
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
            userViewModel.compositionNotifications.collect { result ->
                when(result) {
                    is Result.Success<CompositionObj<ArrayList<Notification>, String>> -> {
                        notificationsList.clear()
                        notificationsList.addAll(result.data.data)
                        notificationAdapter.setNewData(notificationsList)
                    }
                    is Result.Error -> {
                        showToast(message = result.error)
                        backMainApp()
                    }
                    else -> Unit
                }
            }
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
        toolbarAppBinding.titleBar.text = resources.getString(R.string.notifications)
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

    override fun onOkButton(data: Notification) {

    }

    override fun onCancelButton(data: Notification) {

    }

    val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            // No necesitamos mover elementos en este caso, así que devolvemos falso
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            // Acción a realizar cuando se realiza el gesto hacia la izquierda
            // Por ejemplo, puedes eliminar el elemento del adaptador

            val position = viewHolder.adapterPosition
            val noti = notificationsList.get(position)
            notificationsList.removeAt(position)
            notificationAdapter.deleteElement(position = position)
            userViewModel.delete_notification(notification_id = noti.id.toString())
            if (notificationsList.size == 0) {
                showToast(message = resources.getString(R.string.error_no_data))
                backMainApp()
            }
            //adapter.removeItem(position)
        }
    }

    fun backMainApp() {
        finish()
    }
}