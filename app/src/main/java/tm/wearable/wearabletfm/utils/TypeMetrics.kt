package tm.wearable.wearabletfm.utils

import java.util.HashMap
import tm.wearable.wearabletfm.R
object TypeMetrics {
    var attributes: HashMap<String, String> = HashMap()
    var attrImg: HashMap<String, Int> = HashMap()
    var colorsDetailDataMetrics: HashMap<String, Int> = HashMap()
    //sensor names
    const val heart_rate_name = "HEART_RATE"
    const val steps_name = "STEPS"
    const val distance_name = "DISTANCE"
    const val kals_name = "KCALS"
    const val sleep_name = "SLEEP"

    const val sleep_deep = "SLEEP_DEEP"
    const val sleep_light = "SLEEP_light"
    const val sleep_rem = "SLEEP_REM"
    const val sleep_wake = "SLEEP_WAKE"
    const val sleep_total_bed = "SLEEP_TOTAL_BED"



    init {

        attributes.put(heart_rate_name, "Heart Rate")
        attributes.put(steps_name, "Steps")
        attributes.put(distance_name, "Distance")
        attributes.put(kals_name, "Kcals")
        attributes.put(sleep_name, "Sleep")

        attributes.put(sleep_deep, "Sueño profundo")
        attributes.put(sleep_light, "Sueño ligero")
        attributes.put(sleep_rem, "Sueño rem")
        attributes.put(sleep_wake, "Despierto")
        attributes.put(sleep_total_bed, "Tiempo en cama")


        attrImg.put(heart_rate_name, R.drawable.heart_rate_icon)
        attrImg.put(steps_name, R.drawable.steps_icon)
        attrImg.put(distance_name, R.drawable.distance_icon)
        attrImg.put(kals_name, R.drawable.kcals_icon)
        attrImg.put(sleep_name, R.drawable.sleep_icon)


        colorsDetailDataMetrics.put(heart_rate_name, R.color.color4)
        colorsDetailDataMetrics.put(steps_name, R.color.color1)
        colorsDetailDataMetrics.put(distance_name, R.color.color2)
        colorsDetailDataMetrics.put(kals_name, R.color.color6)
        colorsDetailDataMetrics.put(sleep_name, R.color.color5)

    }

    fun getNameSensor(type: String): String {
        val name = attributes.get(type)
        return name ?: "Sensor"
    }

    fun getIconSensor(type: String): Int {
        val name = attrImg.get(type)
        return name ?: R.drawable.steps_icon
    }

    fun getColorDetailDataMetrics(type: String): Int {
        val color = colorsDetailDataMetrics.get(type)
        return color ?: R.color.purple_200
    }

}