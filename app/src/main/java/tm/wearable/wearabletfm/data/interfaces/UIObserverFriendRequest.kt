package tm.wearable.wearabletfm.data.interfaces

import tm.wearable.wearabletfm.data.model.Friend

interface UIObserverFriendRequest {
    fun onAcceptFriend(friend: Friend)
    fun onCancelFriend(friend: Friend)
}