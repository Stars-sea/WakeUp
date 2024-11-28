package im.stars_sea.wakeup.data

import im.stars_sea.wakeup.serializer.PersistentListSerializer
import im.stars_sea.wakeup.ui.theme.WakeUpThemeColor
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
data class WakeUpConfigs(
    @Serializable(with = PersistentListSerializer::class)
    val alarms: PersistentList<AlarmConf> = persistentListOf(),

    val darkTheme: Boolean? = null,
    val themeColor: WakeUpThemeColor = WakeUpThemeColor.Dynamic
)
