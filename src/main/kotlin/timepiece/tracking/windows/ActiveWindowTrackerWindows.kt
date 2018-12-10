package timepiece.tracking.windows

import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.Pointer
import timepiece.tracking.ActivityTracker
import java.lang.IllegalStateException

interface User32dll: Library {
    fun GetForegroundWindow(): Pointer
    fun GetWindowText(hWnd: Pointer, text: Any, count: Int)
    fun GetWindowTextLength(hWnd: Pointer): Int
}

class ActiveWindowTrackerWindows: ActivityTracker{
//    [DllImport("user32.dll", CharSet = CharSet.Auto, SetLastError = true)]
//    static extern IntPtr GetForegroundWindow();
//    [DllImport("user32.dll", CharSet = CharSet.Auto, SetLastError = true)]
//    static extern int GetWindowText(IntPtr hWnd, StringBuilder text, int count);
//    [DllImport("user32.dll", CharSet = CharSet.Auto, SetLastError = true)]
//    static extern int GetWindowTextLength(IntPtr hWnd);


    private var lib: User32dll

    init {
        lib = Native.load("user32.dll", User32dll::class.java) ?: throw IllegalStateException()
    }

    override fun trackActivity() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}