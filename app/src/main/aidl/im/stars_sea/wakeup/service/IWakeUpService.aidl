package im.stars_sea.wakeup.service;

import im.stars_sea.wakeup.data.Sentence;

interface IWakeUpService {
    void notifyString(String text);

    void notifySentence(in Sentence sentence);

    void refreshSentence();

    void refreshAlarms();

    void init();
}