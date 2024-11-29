package im.stars_sea.wakeup.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(24.dp, 16.dp)
        )

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = subtitle ?: "", color = MaterialTheme.colorScheme.secondary)
            Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, contentDescription = "")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsItemPreview() {
    SettingsItem(title = "主题颜色", subtitle = "蓝色", onClick = { })
}


@Composable
fun <T> SingleItemsSelector(
    title: String,
    selections: Map<T, String>,
    selected: T,
    onSelectionChanged: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth()) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 16.dp).align(Alignment.CenterHorizontally)
        )

        selections.forEach { (t, u) ->
            Row(
                modifier = Modifier.fillMaxWidth().clickable { onSelectionChanged(t) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = u, modifier = Modifier.padding(24.dp, 0.dp))

                RadioButton(
                    selected = t == selected,
                    modifier = Modifier.padding(16.dp, 0.dp),
                    onClick = { onSelectionChanged(t) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SingleItemsSelectorPreview() {
    SingleItemsSelector(
        title = "Selector",
        selections = mapOf(Pair(0, "Item1"), Pair(1, "Item2")),
        selected = 1,
        onSelectionChanged = { }
    )
}
