package im.stars_sea.wakeup.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import im.stars_sea.wakeup.data.AlarmConf
import im.stars_sea.wakeup.data.AlarmTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    showDialog: Boolean,
    timeConf: AlarmConf?,
    onConfirm: (AlarmConf, AlarmTime) -> Unit,
    onDismiss: (AlarmConf) -> Unit,
    onClickDelete: (AlarmConf) -> Unit
) {
    if (!showDialog || timeConf == null) return

    val timePickerState = rememberTimePickerState(
        initialHour = timeConf.time.hour,
        initialMinute = timeConf.time.minute,
        is24Hour = true
    )

    fun getAlarmTime(): AlarmTime = AlarmTime(
        timePickerState.hour,
        timePickerState.minute
    )

    AlertDialog(
        onDismissRequest = { onDismiss(timeConf) },
        confirmButton = {
            TextButton(onClick = { onConfirm(timeConf, getAlarmTime()) }) {
                Text("完成")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss(timeConf) }) {
                Text("取消")
            }
        },
        text = { DialogContent(timePickerState, onClickDelete, timeConf) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DialogContent(
    timePickerState: TimePickerState,
    onClickDelete: (AlarmConf) -> Unit,
    alarmConf: AlarmConf
) {
    Column {
        TimeInput(timePickerState)

        TextButton(onClick = { onClickDelete(alarmConf) }) {
            Text("删除此闹钟")
        }
    }
}
