/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testcode;

/**
 *
 * @author ss
 */
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.KBDLLHOOKSTRUCT;
import com.sun.jna.platform.win32.WinUser.LowLevelKeyboardProc;
import com.sun.jna.platform.win32.WinUser.MSG;

public class KeyHook {

    private static HHOOK hhk;
    private static LowLevelKeyboardProc keyboardHook;
    private static User32 lib;

    public static void blockWindowsKey() {
        if (isWindows()) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    lib = User32.INSTANCE;
                    HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
                    keyboardHook = new LowLevelKeyboardProc() {
                        public LRESULT callback(int nCode, WPARAM wParam, KBDLLHOOKSTRUCT info) {
                            if (nCode >= 0) {
                                switch (info.vkCode) {
                                    case 0x5B:
                                    case 0x5C:
                                    case 0XA5:
                                    case 0XA3:
                                    case 0X2E:
                                        return new LRESULT(1);
                                    default: //do nothing
                                    }
                            }
                            Pointer ptr = info.getPointer();
                            long peer = Pointer.nativeValue(ptr);
                            return lib.CallNextHookEx(hhk, nCode, wParam, new WinDef.LPARAM(peer));
                        }
                    };
                    hhk = lib.SetWindowsHookEx(13, keyboardHook, hMod, 0);

                    // This bit never returns from GetMessage
                    int result;
                    MSG msg = new MSG();
                    while ((result = lib.GetMessage(msg, null, 0, 0)) != 0) {
                        if (result == -1) {
                            break;
                        } else {
                            lib.TranslateMessage(msg);
                            lib.DispatchMessage(msg);
                        }
                    }
                    lib.UnhookWindowsHookEx(hhk);
                }
            }).start();
        }
    }

    public static void unblockWindowsKey() {
        if (isWindows() && lib != null) {
            lib.UnhookWindowsHookEx(hhk);
        }
    }

    public static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("win") >= 0);
    }

    public static void main(String args[]) {

        KeyHook.isWindows();
        KeyHook.blockWindowsKey();

    }
}
