package d.n.f


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import java.net.URLDecoder


class MA : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent

        Log.d("Debug", "Intent: $intent")

        if(intent.getStringExtra("ROM") != null) {
            Log.d("","Sending RA intent")
            sendRAIntent(intent)
        } else {
            showToast("Daijishou to RetroArch NAS path converter installed/running")
        }
        finish()
    }

    private fun sendRAIntent(incomingIntent: Intent) {
        Log.d("Incoming intent", incomingIntent.toString())
        Log.d("Incoming intent", "Extra received:")
        incomingIntent.extras?.keySet()?.forEachIndexed { _, key ->
            run {
                val value = incomingIntent.extras!!.get(key).toString()
                Log.d("Incoming intent", "[EXTRA] Key: $key, Value: $value")
            }
        }

        val (targetPackage, targetClass) = (incomingIntent.getStringExtra("TARGET") ?: "com.retroarch/com.retroarch.browser.retroactivity.RetroActivityFuture").split("/")

        Log.d("Sending RA intent","Target package: $targetPackage")
        Log.d("Sending RA intent","Target class: $targetClass")

        val intent = Intent(Intent.ACTION_MAIN)
        intent.setClassName(targetPackage, targetClass)
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val originalRomPath = URLDecoder.decode(incomingIntent.getStringExtra("ROM"), "UTF-8")

        if(originalRomPath.isEmpty()) {
            showToast("Invalid ROM path")
            finish()
        }

        Log.d("Sending RA intent","Received ROM path: $originalRomPath")

        var nasRoot = incomingIntent.getStringExtra("NAS_ROOT")?.removeSuffix("/") ?: "/storage/N100/ext"
        val romPathParts = originalRomPath.split(":")

        if(romPathParts.size == 1) {
            showToast("Invalid ROM NAS path: $originalRomPath")
            finish()
        }

        val convertedRomPath = "$nasRoot/${romPathParts.last()}"
        Log.d("Sending RA intent","Final ROM path: $convertedRomPath")
        Log.d("Sending RA intent","[EXTRA] Key: ROM, Value: $convertedRomPath")
        intent.putExtra("ROM", convertedRomPath)

        if(incomingIntent.getStringExtra("DNF_DEBUG") != null) {
            showToast("From: $originalRomPath \n To: $convertedRomPath")
        }

        incomingIntent.extras?.keySet()?.forEachIndexed { _, key ->
            incomingIntent.extras?.getString(key)?.let { value ->
                if (key !in listOf("ROM", "NAS_ROOT", "TARGET", "DNF_DEBUG")) {
                    intent.putExtra(key, value)
                    Log.d("Sending RA intent", "[EXTRA] Key: $key, Value: $value")
                }
            }
        }

        Log.d("Full intent", intent.toString())

        startActivity(intent)
        finish()
    }

    private fun showToast(msg: String) {
        val toast = Toast.makeText(this, msg, Toast.LENGTH_LONG)
        toast.show()
    }


}