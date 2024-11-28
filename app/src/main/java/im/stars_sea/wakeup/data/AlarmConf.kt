package im.stars_sea.wakeup.data

import kotlinx.serialization.Serializable

@Serializable
data class AlarmConf(
    val time: AlarmTime = AlarmTime(),
    val enabled: Boolean = true
)
