package tm.wearable.wearabletfm.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tm.wearable.wearabletfm.data.model.CDay
import java.util.*
import tm.wearable.wearabletfm.utils.*
class CalendarViewModel : ViewModel() {
    private val _SelectedDate = MutableLiveData<Result<selectedCalendarByView>>(Result.Empty)
    val SelectedDate: LiveData<Result<selectedCalendarByView>> get() = _SelectedDate

    private val _cDays = MutableStateFlow<ArrayList<CDay>>(arrayListOf())
    val cDays: StateFlow<ArrayList<CDay>> = _cDays

    fun dateSelected(dateSelected: GregorianCalendar, isSelected: Boolean = false){
        _SelectedDate.value = Result.Empty
        _SelectedDate.value = Result.Success(selectedCalendarByView(dateSelected, isSelected))
    }
    fun getActualWeek(dateSelected: GregorianCalendar, dateToSelect: GregorianCalendar?= null, isPrevious: Boolean){
        _cDays.value = arrayListOf()
        _cDays.value = CDayUtils.getWeek(dateSelected = dateSelected, dateToSelect = dateToSelect, isPrevious = isPrevious)
    }

    data class selectedCalendarByView(val date: GregorianCalendar, var isSelected : Boolean)
}