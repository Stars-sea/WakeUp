package im.stars_sea.wakeup.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import im.stars_sea.wakeup.R

val FangSongFontFamily = FontFamily(
    Font(
        resId = R.font.fang_song_normal,
        weight = FontWeight.Normal,
        style = FontStyle.Normal
    ),
    Font(
        resId = R.font.fang_song_medium,
        weight = FontWeight.Medium,
        style = FontStyle.Normal
    )
)

val Typography = Typography()