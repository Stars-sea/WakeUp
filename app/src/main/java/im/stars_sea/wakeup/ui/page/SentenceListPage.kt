package im.stars_sea.wakeup.ui.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import im.stars_sea.wakeup.data.Sentence
import im.stars_sea.wakeup.ui.component.SentenceList
import im.stars_sea.wakeup.viewmodel.SentenceViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SentenceListPage(
    viewModel: SentenceViewModel,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = { },
    onNavigated: (Sentence) -> Unit = { }
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Column(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxWidth().height(32.dp).padding(4.dp, 0.dp)) {
            IconButton(onClick = onBackPressed, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "返回")
            }

            Text(text = "好句", fontSize = 18.sp, modifier = Modifier.align(Alignment.Center))
        }

        PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
            LeadingIconTab(
                text = { Text(text = "已收藏") },
                onClick = { selectedTabIndex = 0 },
                selected = selectedTabIndex == 0,
                icon = { Icon(
                    if (selectedTabIndex == 0) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "收藏"
                ) }
            )

            LeadingIconTab(
                text = { Text(text = "黑名单") },
                onClick = { selectedTabIndex = 1 },
                selected = selectedTabIndex == 1,
                icon = { Icon(
                    if (selectedTabIndex == 1) Icons.Filled.Delete else Icons.Outlined.Delete,
                    contentDescription = "黑名单"
                ) }
            )
        }

        when (selectedTabIndex) {
            0 -> CollectedSentenceList(viewModel, onNavigated)
            1 -> BannedSentenceList(viewModel, onNavigated)
        }
    }
}

@Composable
private fun CollectedSentenceList(viewModel: SentenceViewModel, onClick: (Sentence) -> Unit) {
    val scope = rememberCoroutineScope()
    SentenceList(
        sentences = viewModel.collected.list,
        icon = { Icon(Icons.Filled.Favorite, contentDescription = "收藏", tint = MaterialTheme.colorScheme.surfaceTint) },
        onClick = onClick,
        onButtonClick = { scope.launch { viewModel.collected.del(it) } }
    )
}

@Composable
private fun BannedSentenceList(viewModel: SentenceViewModel, onClick: (Sentence) -> Unit) {
    val scope = rememberCoroutineScope()
    SentenceList(
        sentences = viewModel.banned.list,
        icon = { Icon(Icons.Filled.Delete, contentDescription = "白名单", tint = MaterialTheme.colorScheme.surfaceTint) },
        onClick = onClick,
        onButtonClick = { scope.launch { viewModel.collected.del(it) } }
    )
}

