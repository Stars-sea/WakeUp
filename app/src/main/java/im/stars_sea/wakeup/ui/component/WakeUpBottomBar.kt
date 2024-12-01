package im.stars_sea.wakeup.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import im.stars_sea.wakeup.ui.theme.WakeUpThemePreview

enum class WakeUpTabItem {
    Home, Sentences, Settings
}

@Composable
fun WakeUpBottomBar(selectedTab: WakeUpTabItem, onClick: (WakeUpTabItem) -> Unit) {
    NavigationBar {
        NavigationBarItem(
            label = { Text(text = "主页") },
            selected = selectedTab == WakeUpTabItem.Home,
            onClick = { onClick(WakeUpTabItem.Home) },
            icon = {
                Icon(
                    imageVector = if (selectedTab == WakeUpTabItem.Home) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "主页"
                )
            }
        )

        NavigationBarItem(
            label = { Text(text = "好句") },
            selected = selectedTab == WakeUpTabItem.Sentences,
            onClick = { onClick(WakeUpTabItem.Sentences) },
            icon = {
                Icon(
                    imageVector = if (selectedTab == WakeUpTabItem.Sentences) Icons.Filled.Search else Icons.Outlined.Search,
                    contentDescription = "好句"
                )
            }
        )

        NavigationBarItem(
            label = { Text(text = "设置") },
            selected = selectedTab == WakeUpTabItem.Settings,
            onClick = { onClick(WakeUpTabItem.Settings) },
            icon = {
                Icon(
                    imageVector = if (selectedTab == WakeUpTabItem.Settings) Icons.Filled.Settings else Icons.Outlined.Settings,
                    contentDescription = "主页"
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WakeUpBottomBarPreview() = WakeUpThemePreview {
    WakeUpBottomBar(WakeUpTabItem.Home) { }
}
