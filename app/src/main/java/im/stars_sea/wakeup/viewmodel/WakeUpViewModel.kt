package im.stars_sea.wakeup.viewmodel

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import im.stars_sea.wakeup.data.AlarmConf
import im.stars_sea.wakeup.data.WakeUpConfigs
import im.stars_sea.wakeup.serializer.WakeUpConfigsSerializer
import im.stars_sea.wakeup.service.IWakeUpService
import im.stars_sea.wakeup.service.WakeUpServiceConnection
import im.stars_sea.wakeup.ui.component.WakeUpTabItem
import im.stars_sea.wakeup.ui.theme.WakeUpThemeColor
import im.stars_sea.wakeup.util.PersistentListWrapper
import im.stars_sea.wakeup.util.multiProcessDataStore
import kotlinx.collections.immutable.mutate
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.first

class WakeUpViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStore by multiProcessDataStore("configs.json", WakeUpConfigsSerializer)

    private val binder: IWakeUpService? get() = WakeUpServiceConnection.binder

    private val configs @Composable
        get() = dataStore.data.collectAsState(initial = WakeUpConfigs()).value

    val darkTheme @Composable get() = configs.darkTheme
    val themeColor @Composable get() = configs.themeColor

    var selectedTab by mutableStateOf(WakeUpTabItem.Home)

    // TODO
    val alarms = object : PersistentListWrapper<AlarmConf> {
        override suspend fun size(): Int = dataStore.data.first().alarms.size

        override suspend fun clear() {
            dataStore.updateData { configs ->
                configs.copy(alarms = persistentListOf())
            }
        }

        override val list @Composable get() = configs.alarms

        override suspend fun del(item: AlarmConf): Boolean = removeAlarmConf(item)

        override suspend fun add(item: AlarmConf): Boolean {
            addAlarmConf(item)
            return true
        }
    }

    suspend fun setDarkTheme(darkTheme: Boolean?) {
        dataStore.updateData { configs ->
            configs.copy(darkTheme = darkTheme)
        }
    }

    suspend fun setThemeColor(themeColor: WakeUpThemeColor) {
        dataStore.updateData { configs ->
            configs.copy(themeColor = themeColor)
        }
    }

    suspend fun addAlarmConf(alarmConf: AlarmConf, refresh: Boolean = true): AlarmConf {
        dataStore.updateData { configs ->
            configs.copy(
                alarms = configs.alarms.mutate { it.add(alarmConf) }
            )
        }

        if (refresh) binder?.refreshAlarms()
        return alarmConf
    }

    suspend fun removeAlarmConf(alarmConf: AlarmConf, refresh: Boolean = true): Boolean {
        var result = false
        dataStore.updateData { configs ->
            configs.copy(
                alarms = configs.alarms.mutate { result = it.remove(alarmConf) }
            )
        }

        if (refresh) binder?.refreshAlarms()
        return result
    }

    suspend fun setAlarmEnabled(alarmConf: AlarmConf, enabled: Boolean): AlarmConf {
        if (alarmConf.enabled == enabled)
            return alarmConf

        removeAlarmConf(alarmConf,  refresh = false)
        return addAlarmConf(alarmConf.copy(enabled = enabled))
    }
}
