package im.stars_sea.wakeup.ui.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import im.stars_sea.wakeup.ui.component.SettingsItem
import im.stars_sea.wakeup.ui.component.SingleItemsSelector
import im.stars_sea.wakeup.ui.theme.WakeUpThemeColor
import im.stars_sea.wakeup.viewmodel.WakeUpViewModel
import kotlinx.coroutines.launch

private val darkModeSelections = mapOf(
    Pair(null, "跟随系统"),
    Pair(false, "关闭"),
    Pair(true, "启用")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(viewModel: WakeUpViewModel, modifier: Modifier = Modifier) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var selectedSection by remember { mutableIntStateOf(0) }

    suspend fun hideSheet() {
        selectedSection = 0
        sheetState.hide()
    }

    suspend fun openSheet(section: Int) {
        if (section == 0) {
            hideSheet()
            return
        }
        selectedSection = section
        sheetState.show()
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "设置", fontSize = 28.sp, modifier = Modifier.padding(24.dp))

            IconButton(onClick = { }, modifier = Modifier.padding(16.dp, 0.dp)) { // TODO
                Icon(Icons.Outlined.Info, contentDescription = "关于")
            }
        }

        SettingsItem(
            title = "主题颜色",
            subtitle = viewModel.themeColor.colorName,
            onClick = { scope.launch { openSheet(1) } }
        )

        SettingsItem(
            title = "暗色模式",
            subtitle = darkModeSelections[viewModel.darkTheme],
            onClick = { scope.launch { openSheet(2) } }
        )
    }

    if (selectedSection != 0)
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { scope.launch { hideSheet() } }
        ) {
            when (selectedSection) {
                1 -> ThemeColorSelector(
                    selected = viewModel.themeColor,
                    modifier = Modifier.padding(24.dp, 16.dp),
                    onSelectionChanged = { scope.launch {
                        viewModel.setThemeColor(it)
                        hideSheet()
                    } }
                )
                2 -> DarkModeSelector(
                    selected = viewModel.darkTheme,
                    modifier = Modifier.padding(24.dp, 16.dp),
                    onSelectionChanged = { scope.launch {
                        viewModel.setDarkTheme(it)
                        hideSheet()
                    } },
                )
            }
        }
}

@Composable
private fun ThemeColorSelector(
    selected: WakeUpThemeColor,
    onSelectionChanged: (WakeUpThemeColor) -> Unit,
    modifier: Modifier = Modifier
) {
    val selections = WakeUpThemeColor.entries.associateWith { it.colorName }

    SingleItemsSelector(
        title = "主题颜色",
        selections = selections,
        selected = selected,
        onSelectionChanged = { onSelectionChanged(it) },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun ThemeColorSelectorPreview() {
    ThemeColorSelector(WakeUpThemeColor.Blue, { })
}

@Composable
private fun DarkModeSelector(
    selected: Boolean?,
    onSelectionChanged: (Boolean?) -> Unit,
    modifier: Modifier = Modifier
) = SingleItemsSelector(
    title = "暗色模式",
    selections = darkModeSelections,
    selected = selected,
    onSelectionChanged = { onSelectionChanged(it) },
    modifier = modifier
)

@Preview(showBackground = true)
@Composable
private fun DarkModeSelectorPreview() {
    DarkModeSelector(null, { })
}
