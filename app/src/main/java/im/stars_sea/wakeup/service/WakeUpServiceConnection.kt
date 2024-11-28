package im.stars_sea.wakeup.service

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import im.stars_sea.wakeup.data.SentenceType
import kotlinx.coroutines.runBlocking

private const val TAG = "WakeUpServiceConnection"

object WakeUpServiceConnection : ServiceConnection {
    var binder: IWakeUpService? = null
        private set

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        binder = IWakeUpService.Stub.asInterface(service)!!
        Log.i(TAG, "Service connected")

        binder!!.init()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        binder = null
        Log.i(TAG, "Service disconnected")
    }
}
