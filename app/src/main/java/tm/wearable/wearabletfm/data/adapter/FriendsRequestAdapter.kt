package tm.wearable.wearabletfm.data.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tm.wearable.wearabletfm.data.interfaces.UIObserverFriendRequest
import tm.wearable.wearabletfm.data.model.Friend
import tm.wearable.wearabletfm.databinding.FriendRequestItemBinding
import tm.wearable.wearabletfm.R
class FriendsRequestAdapter (val context: Context, var list: List<Friend>, val observer: UIObserverFriendRequest,
                             val alertDialogBuilder: androidx.appcompat.app.AlertDialog):
    RecyclerView.Adapter<FriendsRequestAdapter.HolderFriend>() {

    class HolderFriend(binding: FriendRequestItemBinding): RecyclerView.ViewHolder(binding.root){
        private val binding = binding
        fun binData(shortUser: Friend, observer: UIObserverFriendRequest, alertDialogBuilder: androidx.appcompat.app.AlertDialog){
            binding.nameUser.text = shortUser.name
            binding.addFriend.setOnClickListener{
                alertDialogBuilder.dismiss()
                observer.onAcceptFriend(friend = shortUser)
            }
            binding.deleteFriend.setOnClickListener {
                alertDialogBuilder.dismiss()
                observer.onCancelFriend(friend = shortUser)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderFriend {
        val binding = FriendRequestItemBinding.bind(LayoutInflater.from(context).inflate(R.layout.friend_request_item,parent, false))
        return HolderFriend(
            binding = binding
        )
    }

    override fun onBindViewHolder(holder: HolderFriend, position: Int) {
        holder.binData(shortUser = list[position], observer = observer, alertDialogBuilder = alertDialogBuilder)

    }

    override fun getItemCount() = list.size

    fun setNewData(arrayList: List<Friend>) {
        list = arrayList
        notifyDataSetChanged()
    }
}