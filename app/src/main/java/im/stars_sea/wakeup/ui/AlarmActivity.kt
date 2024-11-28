package im.stars_sea.wakeup.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import im.stars_sea.wakeup.ui.theme.WakeUpTheme
import im.stars_sea.wakeup.ui.theme.WakeUpThemeColor
import im.stars_sea.wakeup.viewmodel.WakeUpViewModel

class AlarmActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setShowWhenLocked(true)

        enableEdgeToEdge()
        setContent { RootContent() }
    }

    @Composable
    private fun RootContent() {
        val viewModel = MainActivity.viewModel

        WakeUpTheme(viewModel.themeColor, viewModel.darkTheme) {
            Scaffold { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    Text("123")
                }
            }
        }
    }
}
