package im.stars_sea.wakeup.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import im.stars_sea.wakeup.ui.component.WakeUpBottomBar
import im.stars_sea.wakeup.ui.component.WakeUpTabItem
import im.stars_sea.wakeup.ui.page.HomePage
import im.stars_sea.wakeup.ui.page.SentenceDetailPage
import im.stars_sea.wakeup.ui.page.SettingsPage
import im.stars_sea.wakeup.ui.theme.WakeUpTheme
import im.stars_sea.wakeup.viewmodel.SentenceViewModel
import im.stars_sea.wakeup.viewmodel.WakeUpViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: WakeUpViewModel by viewModels()
    private val sentenceViewModel: SentenceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent { RootContent() }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val tab = intent?.getSerializableExtra("tab", WakeUpTabItem::class.java)
        if (tab != null) viewModel.selectedTab = tab
    }

    @Composable
    private fun RootContent() {
        val snackBarHostState = remember { SnackbarHostState() }
        val windowInsetsControllerCompat = WindowCompat.getInsetsController(window, window.decorView)

        WakeUpTheme(viewModel.themeColor, viewModel.darkTheme, windowInsetsControllerCompat) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                snackbarHost = { SnackbarHost(snackBarHostState) },
                bottomBar = {
                    WakeUpBottomBar(viewModel.selectedTab) {
                        viewModel.selectedTab = it
                    }
                }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    when (viewModel.selectedTab) {
                        WakeUpTabItem.Home -> HomePage(viewModel)
                        WakeUpTabItem.Sentences -> SentenceDetailPage(
                            sentenceViewModel,
                            snackBarHostState
                        )

                        WakeUpTabItem.Settings -> SettingsPage(
                            viewModel,
                            sentenceViewModel
                        )
                    }
                }
            }
        }
    }
}