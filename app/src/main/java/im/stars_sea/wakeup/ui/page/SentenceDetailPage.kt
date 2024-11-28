package im.stars_sea.wakeup.ui.page

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import im.stars_sea.wakeup.data.Sentence
import im.stars_sea.wakeup.service.WakeUpServiceConnection
import im.stars_sea.wakeup.ui.SentenceListActivity
import im.stars_sea.wakeup.ui.component.SentenceCard
import im.stars_sea.wakeup.viewmodel.SentenceViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SentenceDetailPage(viewModel: SentenceViewModel, modifier: Modifier = Modifier) {
    var isCollected by remember { mutableStateOf(false) }
    var isBanned by remember { mutableStateOf(false) }

    var refreshEnabled by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    suspend fun updateSentence(s: Sentence) {
        viewModel.updateCurrentSentence(s)
        isCollected = viewModel.run { s.isCollected() }
        isBanned = viewModel.run { s.isBanned() }
    }

    suspend fun onSentenceCollected() {
        val result = if (isCollected)
            viewModel.cancelCollectCurrent()
        else viewModel.collectCurrent()

        if (result) isCollected = !isCollected
    }

    suspend fun onSentenceRefreshed() {
        refreshEnabled = false

        updateSentence(viewModel.updateCurrentSentence())
        WakeUpServiceConnection.binder!!.refreshSentence()

        delay(2000)
        refreshEnabled = true
    }

    suspend fun onSentenceBanned() {
        val result = if (isBanned)
            viewModel.unbanCurrentSentence()
        else viewModel.banCurrentSentence()

        if (result) isBanned = !isBanned
    }

    fun onListClicked() {
        val intent = Intent(context, SentenceListActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

        context.startActivity(intent)
    }

    LaunchedEffect(viewModel.currentSentence) {
        updateSentence(viewModel.getOrCreateSentence())
        delay(2000)
        refreshEnabled = true
    }

    Box(modifier = modifier.fillMaxSize()) {
        ElevatedCard(
            modifier = Modifier.align(Alignment.TopEnd).padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Row {
                IconButton(
                    enabled = refreshEnabled,
                    onClick = { scope.launch { onSentenceRefreshed() } }
                ) { Icon(imageVector = Icons.Outlined.Refresh, contentDescription = "刷新") }

                IconButton(onClick = { scope.launch { onSentenceBanned() } }) {
                    Icon(
                        imageVector = if (isBanned) Icons.Filled.Delete else Icons.Outlined.Delete,
                        contentDescription = "不喜欢"
                    )
                }

                IconButton(onClick = { onListClicked() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.List,
                        contentDescription = "列表"
                    )
                }
            }
        }

        SentenceCard(
            viewModel.currentSentence,
            modifier = Modifier.padding(32.dp, 128.dp, 32.dp, 0.dp).fillMaxSize()
        )

        FloatingActionButton(
            modifier = Modifier.padding(16.dp).align(Alignment.BottomEnd),
            onClick = { scope.launch { onSentenceCollected() } }
        ) {
            Icon(
                imageVector = if (isCollected) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "收藏"
            )
        }
    }
}
