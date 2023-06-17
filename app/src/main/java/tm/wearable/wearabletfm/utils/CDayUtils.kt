package tm.wearable.wearabletfm.utils

import tm.wearable.wearabletfm.data.model.CDay
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CDayUtils {
    companion object{
        var daysLetter : HashMap<Int, String> = hashMapOf(
            2 to "L", 3 to "M",
            4 to "X", 5 to "J",
            6 to "V", 7 to "S",
            1 to "D",
        )

        fun getDayLetter(dayIndex: Int): String {
            val name = daysLetter.get(dayIndex)
            return name.toString()
        }
        val TAG = "CDayUtils"


        fun getWeek(dateSelected: GregorianCalendar, dateToSelect: GregorianCalendar?= GregorianCalendar(), isPrevious: Boolean): ArrayList<CDay>{
            var listCDay = arrayListOf<CDay>()
            val actualDate = GregorianCalendar()
            var initialDate = GregorianCalendar()
            var calendar = GregorianCalendar()
            calendar.time = dateSelected.time
            calendar.add(
                Calendar.DATE,
                if (!isPrevious) 0 else -6
            )
            initialDate.time = dateSelected.time
            while (listCDay.size < 7){
                var newDate = GregorianCalendar()
                newDate.time = calendar.time

                if (newDate.time <= actualDate.time ) {
                    calendar.add(Calendar.DATE, +1)
                }
                else{
                    initialDate.add(Calendar.DATE, -1)
                    newDate.time = initialDate.time
                }
                listCDay.add(
                    CDay(
                        dayText = getDayLetter(
                            dayIndex = newDate.get(Calendar.DAY_OF_WEEK)
                        ),
                        dayNumber = newDate.get(Calendar.DAY_OF_MONTH),
                        date = newDate,
                        isSelected = if (dateToSelect != null) SimpleDateFormat("yyyy-MM-dd").format(dateToSelect.time) == SimpleDateFormat("yyyy-MM-dd").format(newDate.time) else false
                    )
                )
            }

            var nel =  listCDay.sortedWith(compareBy { it.date.time }).toCollection(ArrayList())
            return nel
        }

        fun getDateByNumber(dateSelected: GregorianCalendar, number: Int): GregorianCalendar {
            val newDateSelected = GregorianCalendar()
            newDateSelected.time = dateSelected.time
            newDateSelected.add(Calendar.DATE, number)
            return newDateSelected
        }

    }
}