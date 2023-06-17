package tm.wearable.wearabletfm.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import tm.wearable.wearabletfm.utils.*
class CalendarViewModel : ViewModel() {
    private val _SelectedDate = MutableLiveData<Result<selectedCalendarByView>>(Result.Empty)
    val SelectedDate: LiveData<Result<selectedCalendarByView>> get() = _SelectedDate


    fun dateSelected(dateSelected: GregorianCalendar, isSelected: Boolean = false){
        _SelectedDate.value = Result.Empty
        _SelectedDate.value = Result.Success(selectedCalendarByView(dateSelected, isSelected))
    }
    data class selectedCalendarByView(val date: GregorianCalendar, var isSelected : Boolean)
}