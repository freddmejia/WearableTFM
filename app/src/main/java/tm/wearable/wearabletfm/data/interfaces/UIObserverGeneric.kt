package tm.wearable.wearabletfm.data.interfaces

interface UIObserverGeneric<T> {
    fun onOkButton(data: T)
    fun onCancelButton(data: T)
}