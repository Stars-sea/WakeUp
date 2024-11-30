package im.stars_sea.wakeup.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import im.stars_sea.wakeup.data.Sentence
import im.stars_sea.wakeup.service.WakeUpServiceConnection
import im.stars_sea.wakeup.ui.page.SentenceListPage
import im.stars_sea.wakeup.ui.theme.WakeUpTheme
import im.stars_sea.wakeup.viewmodel.SentenceViewModel
import im.stars_sea.wakeup.viewmodel.WakeUpViewModel
import kotlinx.coroutines.launch

class SentenceListActivity : ComponentActivity() {
    private val viewModel: WakeUpViewModel by viewModels()
    private val sentenceViewModel: SentenceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { RootContent() }
    }

    @Composable
    private fun RootContent() {
        val windowInsetsControllerCompat = WindowCompat.getInsetsController(window, window.decorView)

        WakeUpTheme(viewModel.themeColor, viewModel.darkTheme, windowInsetsControllerCompat) {
            val scope = rememberCoroutineScope()

            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                SentenceListPage(
                    viewModel = sentenceViewModel,
                    modifier = Modifier.padding(innerPadding),
                    onBackPressed = { finish() },
                    onNavigated = { scope.launch { navigate(it, sentenceViewModel) } }
                )
            }
        }
    }

    private suspend fun navigate(sentence: Sentence, sentenceViewModel: SentenceViewModel) {
        sentenceViewModel.updateCurrentSentence(sentence)
        WakeUpServiceConnection.binder!!.refreshSentence()

        finish()
    }
}
