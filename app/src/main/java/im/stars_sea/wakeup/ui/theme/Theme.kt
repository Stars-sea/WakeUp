package im.stars_sea.wakeup.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import im.stars_sea.wakeup.ui.theme.blue.DarkBlueScheme
import im.stars_sea.wakeup.ui.theme.blue.LightBlueScheme
import im.stars_sea.wakeup.ui.theme.green.DarkGreenScheme
import im.stars_sea.wakeup.ui.theme.green.LightGreenScheme
import im.stars_sea.wakeup.ui.theme.red.DarkRedScheme
import im.stars_sea.wakeup.ui.theme.red.LightRedScheme

enum class WakeUpThemeColor {
    Blue,   // Base: 0xFF87CEEB
    Green,  // Base: 0xFF7FE997
    Red,    // Base: 0xFFEC938F
    Dynamic
}

@Composable
fun WakeUpTheme(
    themeColor: WakeUpThemeColor,
    darkTheme: Boolean? = null,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val isDarkTheme = darkTheme ?: isSystemInDarkTheme()

    val colorScheme = when (themeColor) {
        WakeUpThemeColor.Dynamic -> if (isDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        WakeUpThemeColor.Blue -> if (isDarkTheme) DarkBlueScheme else LightBlueScheme
        WakeUpThemeColor.Green -> if (isDarkTheme) DarkGreenScheme else LightGreenScheme
        WakeUpThemeColor.Red -> if (isDarkTheme) DarkRedScheme else LightRedScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
