package im.stars_sea.wakeup.data

import kotlinx.serialization.Serializable
import java.util.Calendar

@Serializable
data class AlarmTime(val hour: Int, val minute: Int): Comparable<AlarmTime> {
    val minutesOfDay: Int get() = hour * 60 + minute

    constructor(): this(Calendar.getInstance())

    constructor(calendar: Calendar): this(
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE)
    )

    constructor(totalMinutes: Int): this(
        totalMinutes / 60 % 24,
        totalMinutes % 60
    )

    operator fun plus(other: AlarmTime): AlarmTime = AlarmTime(
        minutesOfDay + other.minutesOfDay
    )

    operator fun minus(other: AlarmTime): AlarmTime = AlarmTime(
         if (minutesOfDay > other.minutesOfDay)
             minutesOfDay - other.minutesOfDay
         else 24 * 60 * 60 - other.minutesOfDay + minutesOfDay
    )

    override fun compareTo(other: AlarmTime): Int = compareValuesBy(this, other, AlarmTime::minutesOfDay)

    fun toCalendar(): Calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    fun toHintString(): String {
        var str = "$minute 分钟"
        if (hour > 0) str = "$hour 小时 $str"
        return str
    }

    override fun toString(): String = "%02d:%02d".format(hour, minute)
}
