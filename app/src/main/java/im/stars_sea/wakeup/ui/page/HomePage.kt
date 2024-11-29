package im.stars_sea.wakeup.ui.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import im.stars_sea.wakeup.data.AlarmConf
import im.stars_sea.wakeup.data.AlarmTime
import im.stars_sea.wakeup.ui.component.AlarmList
import im.stars_sea.wakeup.ui.component.TimePickerDialog
import im.stars_sea.wakeup.viewmodel.WakeUpViewModel
import kotlinx.coroutines.launch

@Composable
fun HomePage(viewModel: WakeUpViewModel, modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()

    var showDialog by remember { mutableStateOf(false) }
    var dialogTimeConf by remember { mutableStateOf<AlarmConf?>(null) }

    fun openDialog(initialTime: AlarmConf) {
        showDialog = true
        dialogTimeConf = initialTime
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "闹钟", fontSize = 28.sp)

            IconButton(onClick = { openDialog(AlarmConf()) }) {
                Icon(Icons.Filled.Add, "Add Alarm")
            }
        }

        Box {
            AlarmList(
                Modifier.fillMaxSize(),
                viewModel.alarms.list,
                onItemClick = { time -> openDialog(time) },
                onItemEnabledChanged = { conf: AlarmConf, enabled: Boolean ->
                    scope.launch { viewModel.setAlarmEnabled(conf, enabled) }
                }
            )

            TimePickerDialog(
                showDialog = showDialog,
                timeConf = dialogTimeConf,
                onConfirm = { alarmConf: AlarmConf, alarmTime: AlarmTime ->
                    showDialog = false
                    scope.launch {
                        viewModel.removeAlarmConf(alarmConf, refresh = false)
                        viewModel.addAlarmConf(AlarmConf(alarmTime))
                    }
                },
                onDismiss = { showDialog = false },
                onClickDelete = { conf ->
                    showDialog = false
                    scope.launch { viewModel.removeAlarmConf(conf) }
                }
            )
        }
    }
}
