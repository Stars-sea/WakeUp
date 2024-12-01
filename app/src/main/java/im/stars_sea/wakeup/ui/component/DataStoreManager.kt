package im.stars_sea.wakeup.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import im.stars_sea.wakeup.ui.theme.WakeUpThemeColor
import im.stars_sea.wakeup.ui.theme.WakeUpThemePreview
import im.stars_sea.wakeup.viewmodel.SentenceViewModel
import im.stars_sea.wakeup.viewmodel.WakeUpViewModel
import kotlinx.coroutines.launch

@Composable
fun DataStoreManager(
    viewModel: WakeUpViewModel,
    sentenceViewModel: SentenceViewModel,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    Column(modifier.fillMaxWidth().padding(24.dp, 0.dp)) {
        Text(
            text = "应用数据",
            style = MaterialTheme.typography.titleMedium, // 18
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(0.dp, 0.dp, 0.dp, 16.dp)
        )

        SettingsItem(
            text = "应用设置",
            detailText = "",
            buttonText = "还原"
        ) {
            scope.launch {
                viewModel.setDarkTheme(null)
                viewModel.setThemeColor(WakeUpThemeColor.Dynamic)
            }
        }

        SettingsItem(
            text = "闹钟列表",
            detailText = "${viewModel.alarms.list.size} 条",
            buttonText = "清除"
        ) { scope.launch { viewModel.alarms.clear() } }

        SettingsItem(
            text = "好句收藏",
            detailText = "${sentenceViewModel.collected.list.size} 条",
            buttonText = "清除"
        ) { scope.launch { sentenceViewModel.collected.clear() } }

        SettingsItem(
            text = "黑名单",
            detailText = "${sentenceViewModel.banned.list.size} 条",
            buttonText = "清除"
        ) { scope.launch { sentenceViewModel.banned.clear() } }
    }
}

@Composable
private fun SettingsItem(
    text: String,
    detailText: String,
    buttonText: String,
    onClick: () -> Unit
) = Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = text, style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = detailText,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary
        )
    }

    TextButton(onClick = onClick) {
        Text(text = buttonText)
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsItemPreview() = WakeUpThemePreview {
    SettingsItem("好句收藏", "10 条", "清除") { }
}