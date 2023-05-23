package tm.wearable.wearabletfm.fragments

import android.content.Context
import android.content.Intent
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
import tm.wearable.wearabletfm.FindFriendActivity

import tm.wearable.wearabletfm.R
import tm.wearable.wearabletfm.data.interfaces.UIObserverFriendRequest
import tm.wearable.wearabletfm.data.interfaces.UIObserverGeneric
import tm.wearable.wearabletfm.data.model.Friend
import tm.wearable.wearabletfm.data.model.FriendRequest
import tm.wearable.wearabletfm.data.model.User
import tm.wearable.wearabletfm.data.viewmodel.FriendViewModel
import tm.wearable.wearabletfm.databinding.FriendFragmentBinding
import tm.wearable.wearabletfm.utils.FriendsAdapter
import tm.wearable.wearabletfm.utils.Utils
import tm.wearable.wearabletfm.utils.WearableDialogs
import tm.wearable.wearabletfm.utils.*
@AndroidEntryPoint
class FriendFragment: Fragment(R.layout.friend_fragment), UIObserverGeneric<Friend>,
    UIObserverFriendRequest {
    private var binding: FriendFragmentBinding? = null

    private val friendViewModel: FriendViewModel by viewModels()
    private lateinit var toast: Toast
    private lateinit var friendsAdapter: FriendsAdapter
    private var friendsList = arrayListOf<Friend>()
    private var tmp_friendsList = arrayListOf<Friend>()
    private var friendsRequestList = arrayListOf<Friend>()
    private lateinit var user: User
    private var acceptOrDeleteFriend = 0
    private lateinit var friendAdded: Friend
    companion object{
        fun newInstance(): FriendFragment {
            return FriendFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentBinding = FriendFragmentBinding.bind(view)
        binding = fragmentBinding!!
        friendsList = arrayListOf()
        tmp_friendsList = arrayListOf()
        friendsRequestList = arrayListOf()
        toast = Toast(this@FriendFragment.context)
        val prefsUser = this@FriendFragment.requireContext()?.getSharedPreferences(
            resources.getString(R.string.shared_preferences),
            Context.MODE_PRIVATE
        )
        user = User(JSONObject(prefsUser!!.getString("user","")))
        friendAdded = Friend(0,"","","","","",0)
        friendsAdapter = FriendsAdapter(this@FriendFragment.requireContext(), list = arrayListOf(), this)
        binding?.rvMyFriends?.layoutManager = LinearLayoutManager(this@FriendFragment.requireContext())
        binding?.rvMyFriends?.adapter = friendsAdapter

        events()
        coroutines()
        api()
    }

    fun events(){
        binding?.addFriend?.setOnClickListener {
            startActivity(Intent(this@FriendFragment.requireContext(), FindFriendActivity::class.java))
        }
        binding?.haveFriends?.setOnClickListener {
            WearableDialogs.friends_request_dialog(
                context = this@FriendFragment.requireContext(),
                observer = this@FriendFragment,
                friendsR = friendsRequestList
            )
            api()
        }
    }

    fun api() {
        Log.e("", "api: user " +user.id.toString() )
        acceptOrDeleteFriend = 0
        friendViewModel.fetch_friends()
        friendViewModel.fetch_friends_request()
    }

    fun coroutines() {
        lifecycleScope.launchWhenCreated {
            friendViewModel.loadingProgress.collect {
                binding?.linear?.isVisible = !it
                binding?.progressBar?.isVisible = it
            }
        }
        lifecycleScope.launchWhenCreated {
            friendViewModel.compositionFetchPossibleFriends.collect { result ->
                when(result) {
                    is Result.Success<CompositionObj<ArrayList<Friend>, String>> -> {

                        binding?.title?.text = resources.getString(R.string.title_add_friend)
                        friendsList.clear()
                        friendsList.addAll(result.data.data)


                        if (friendsList.size == 0 && tmp_friendsList.size > 0){
                            friendsList.clear()
                            friendsList.addAll(tmp_friendsList)
                        }
                        tmp_friendsList.clear()
                        tmp_friendsList.addAll(friendsList)
                        friendsAdapter.setNewData(friendsList)


                        binding?.rvMyFriends?.isVisible = true
                        binding?.placeholder?.isVisible = false
                    }
                    is Result.Error -> {
                        showToast(message = result.error)
                        binding?.rvMyFriends?.isVisible = false
                        binding?.placeholder?.isVisible = true
                        binding?.title?.text = resources.getString(R.string.dont_have_friends)
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            friendViewModel.compositionFetchFriendsRequest.collect { result->
                when(result) {
                    is Result.Success<CompositionObj<ArrayList<Friend>, String>> -> {
                        binding?.haveFriends?.isVisible = true
                        friendsRequestList.clear()
                        friendsRequestList.addAll(result.data.data)
                    }
                    is Result.Error -> {
                        binding?.haveFriends?.isVisible = false
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            friendViewModel.compositionFriendRequest.collect { result ->
                when(result) {
                    is Result.Success<CompositionObj<FriendRequest,String>> -> {
                        val user_id =
                            if (result.data.data.user_id1 == user.id) result.data.data.user_id2
                            else result.data.data.user_id1
                        if (acceptOrDeleteFriend == 0) {
                            val friend = friendsList.find { friend -> friend.id == user_id }
                            if (friend != null)
                                friendsList.remove(friend)
                        }
                        else if(acceptOrDeleteFriend == Utils.friend_added || acceptOrDeleteFriend == Utils.are_friend) {
                            val friend = friendsRequestList.find { friend -> friend.id == user_id }
                            if (friend != null)
                                friendsRequestList.remove(friend)

                            if (friendsRequestList.size == 0)
                                binding?.haveFriends?.isVisible = false

                            if (friendAdded.id > 0){
                                friendAdded.is_friend = Utils.are_friend
                                friendsList.add(friendAdded)
                            }
                        }

                        tmp_friendsList.clear()
                        tmp_friendsList.addAll(friendsList)
                        friendViewModel.set_compositionFetchPossibleFriends(
                            if (tmp_friendsList.size == 0)
                                Result.Error(resources.getString(R.string.error_no_data))
                            else
                                Result.Success(
                                    CompositionObj(
                                        data = tmp_friendsList,
                                        message = resources.getString(R.string.error_data)
                                    )
                                )
                        )

                        showToast(message = result.data.message)

                    }
                    is Result.Error -> {
                        showToast(message = result.error)
                    }
                    else -> Unit
                }
            }
        }
    }

    fun showToast(message: String){
        toast?.cancel()
        toast = Toast.makeText(this@FriendFragment.context,message, Toast.LENGTH_LONG)
        toast.show()
    }

    override fun onAcceptFriend(friend: Friend) {
        friendAdded = friend
        acceptOrDeleteFriend = Utils.friend_added
        friendViewModel.accept_friend_request(user_id1 = friend.id.toString(), user_id2 = user.id.toString())
    }

    override fun onCancelFriend(friend: Friend) {
        friendAdded = Friend(0,"","","","","",0)
        acceptOrDeleteFriend = Utils.are_friend
        friendViewModel.delete_friend_request(user_id1 = friend.id.toString(), user_id2 = user.id.toString())
    }

    override fun onOkButton(data: Friend) {
    }

    override fun onCancelButton(data: Friend) {
        acceptOrDeleteFriend = 0
        friendViewModel.delete_friend_request(user_id1 = user.id.toString(), user_id2 = data.id.toString())
    }

    override fun onResume() {
        super.onResume()
        api()
    }
}