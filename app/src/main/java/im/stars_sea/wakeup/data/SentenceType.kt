package im.stars_sea.wakeup.data

import im.stars_sea.wakeup.serializer.SentenceTypeSerializer
import kotlinx.serialization.Serializable

@Serializable(with = SentenceTypeSerializer::class)
enum class SentenceType(val code: Char, val desc: String) {
    Anime('a', "动画"),
    Comic('b', "漫画"),
    Game('c', "游戏"),
    Literature('d', "文学"),
    Original('e', "原创"),
    Network('f', "来自网络"),
    Other('g', "其他"),
    Film('h', "影视"),
    Poem('i', "诗词"),
    Netease('j', "网易云"),
    Philosophy('k', "哲学"),
    Joke('l', "抖激灵");

    companion object {
        fun fromInt(ordinal: Int): SentenceType = entries[ordinal]
    }
}