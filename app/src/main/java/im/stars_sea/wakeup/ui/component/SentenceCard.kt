package im.stars_sea.wakeup.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import im.stars_sea.wakeup.data.Sentence
import im.stars_sea.wakeup.data.SentenceType
import im.stars_sea.wakeup.ui.theme.FangSongFontFamily
import im.stars_sea.wakeup.ui.theme.WakeUpThemePreview

@Composable
fun SentenceCard(
    sentence: Sentence,
    modifier: Modifier = Modifier,
    wrap: Boolean = true,
    fontFamily: FontFamily = FangSongFontFamily,
) {
    val lines = if (wrap && sentence.length > 15)
        sentence.hitokoto.split(
            ',', '.', ';',
            '，', '。', '；',
            '/'
        ).map { it.trim() }.filter { it.isNotEmpty() }
    else listOf(sentence.hitokoto)

    Column(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = AbsoluteAlignment.Right
        ) {
            Text(
                text = sentence.from,
                fontSize = 24.sp,
                fontFamily = fontFamily,
                textAlign = TextAlign.Right
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (sentence.fromWho != null)
                Text(
                    text = sentence.fromWho,
                    fontSize = 14.sp,
                    fontFamily = fontFamily,
                    textAlign = TextAlign.Right
                )
        }

        Spacer(modifier = Modifier.height(64.dp))

        Column {
            lines.forEach { line ->
                Text(text = line, fontSize = 18.sp, fontFamily = fontFamily)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SentenceCardPreview1() = WakeUpThemePreview {
    val sentence = Sentence(
        id = 5590,
        uuid = "3f7ffa61-e42f-4bdb-949d-830680c19eae",
        hitokoto = "人闲桂花落，夜静春山空。",
        type = SentenceType.Poem,
        from = "鸟鸣涧",
        fromWho = "王维",
        creator = "a632079",
        creatorUid = 1044,
        reviewer = 1044,
        commitFrom = "api",
        createdAt = "1586266408",
        length = 12
    )
    SentenceCard(sentence = sentence)
}

@Preview(showBackground = true)
@Composable
private fun SentenceCardPreview2() = WakeUpThemePreview {
    val sentence = Sentence(
        id = 4689,
        uuid = "739ed604-6182-4c36-afb9-39f08128c733",
        hitokoto = "嗟叹红颜泪、英雄殁，人世苦多。山河永寂、怎堪欢颜。",
        type = SentenceType.Poem,
        from = "南唐旧梦：山河永寂",
        fromWho = "一寒呵",
        creator = "smallxu",
        creatorUid = 2639,
        reviewer = 4756,
        commitFrom = "web",
        createdAt = "1569327779",
        length = 25
    )
    SentenceCard(sentence = sentence)
}
