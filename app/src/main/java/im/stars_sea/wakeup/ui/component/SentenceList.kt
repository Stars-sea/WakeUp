package im.stars_sea.wakeup.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import im.stars_sea.wakeup.data.Sentence
import im.stars_sea.wakeup.ui.theme.WakeUpTheme
import im.stars_sea.wakeup.ui.theme.WakeUpThemeColor

@Composable
fun SentenceList(
    sentences: List<Sentence>,
    icon: @Composable () -> Unit,
    onClick: (Sentence) -> Unit,
    onButtonClick: (Sentence) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(sentences) {
            SentenceItem(
                sentence = it,
                icon = icon,
                onClick = { onClick(it) },
                onButtonClick = { onButtonClick(it) },
                modifier = Modifier.padding(16.dp, 8.dp)
            )
        }
    }
}

@Composable
private fun SentenceItem(
    sentence: Sentence,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(modifier = modifier.fillMaxWidth(), onClick = onClick) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = sentence.hitokoto,
                    fontSize = 18.sp,
                    softWrap = false,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = sentence.author,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    softWrap = false,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(
                modifier = Modifier.width(32.dp),
                onClick = onButtonClick,
                content = icon
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SentenceListPreview() {
    WakeUpTheme(WakeUpThemeColor.Blue) {
        SentenceList(
            sentences = listOf(Sentence.Empty, Sentence.Empty),
            icon = { Icon(Icons.Outlined.Favorite, contentDescription = "收藏", tint = MaterialTheme.colorScheme.surfaceTint) },
            onClick = { },
            onButtonClick = { }
        )
    }
}
