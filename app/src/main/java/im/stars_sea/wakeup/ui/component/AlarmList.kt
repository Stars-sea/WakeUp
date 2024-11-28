package im.stars_sea.wakeup.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import im.stars_sea.wakeup.data.AlarmConf
import im.stars_sea.wakeup.data.AlarmTime

@Composable
fun AlarmList(
    modifier: Modifier,
    alarms: List<AlarmConf>,
    onItemClick: (AlarmConf) -> Unit,
    onItemEnabledChanged: (AlarmConf, Boolean) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        items(alarms.sortedBy { it.time }) { alarm ->
            AlarmItem(
                alarm,
                onClick = onItemClick,
                onEnabledChanged = onItemEnabledChanged,
                modifier = Modifier.padding(24.dp, 0.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun AlarmItem(
    alarm: AlarmConf,
    onClick: (AlarmConf) -> Unit,
    onEnabledChanged: (AlarmConf, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val time by remember { mutableStateOf(alarm.time.toString()) }
    var hint by remember { mutableStateOf("") }
    if (alarm.enabled) {
        val delta = alarm.time - AlarmTime()
        hint = "${delta.toHintString()}后响起"
    }

    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = modifier.fillMaxWidth(),
        onClick = { onClick(alarm) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = time, fontSize = 36.sp)
                Text(text = hint, fontSize = 14.sp, color = Color.Gray)
            }

            Switch(
                alarm.enabled,
                modifier = Modifier.height(12.dp),
                onCheckedChange = { onEnabledChanged(alarm, it) }
            )
        }
    }
}

@Preview
@Composable
fun AlarmItemPreview() {
    AlarmItem(
        AlarmConf(AlarmTime()),
        onClick = { },
        onEnabledChanged = { _: AlarmConf, _: Boolean -> }
    )
}