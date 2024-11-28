package im.stars_sea.wakeup.viewmodel

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.MultiProcessDataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.lifecycle.AndroidViewModel
import im.stars_sea.wakeup.data.AlarmConf
import im.stars_sea.wakeup.data.WakeUpConfigs
import im.stars_sea.wakeup.serializer.WakeUpConfigsSerializer
import im.stars_sea.wakeup.service.IWakeUpService
import im.stars_sea.wakeup.service.WakeUpServiceConnection
import im.stars_sea.wakeup.ui.component.WakeUpTabItem
import im.stars_sea.wakeup.ui.theme.WakeUpThemeColor
import kotlinx.collections.immutable.mutate

class WakeUpViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStore = MultiProcessDataStoreFactory.create(
        serializer = WakeUpConfigsSerializer,
        produceFile = { application.dataStoreFile("configs.json") }
    )

    private val binder: IWakeUpService? get() = WakeUpServiceConnection.binder

    private val configs @Composable
        get() = dataStore.data.collectAsState(initial = WakeUpConfigs()).value
    val alarms @Composable get() = configs.alarms
    val darkTheme @Composable get() = configs.darkTheme
    val themeColor @Composable get() = configs.themeColor

    var selectedTab by mutableStateOf(WakeUpTabItem.Home)

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
