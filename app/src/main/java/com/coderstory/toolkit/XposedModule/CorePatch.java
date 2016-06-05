package com.coderstory.toolkit.XposedModule;


import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;


/**
 * Created by CoderStory on 2016/6/4.
 */
public class CorePatch implements IXposedHookZygoteInit {
    ;

    public void initZygote(IXposedHookZygoteInit.StartupParam paramStartupParam)
            throws Throwable {
        XSharedPreferences prefs = new XSharedPreferences("com.coderstory.toolkit", "UserSettings");
        prefs.makeWorldReadable();
        prefs.reload();
        if (!prefs.getBoolean("CorePatcher", false)) {
            XposedBridge.log("CorePatcher:false");
            return;
        }
        XposedHelpers.findClass("android.content.pm.PackageParser", null);
        XposedHelpers.findClass("java.util.jar.JarVerifier$VerifierEntry", null);
        one:
        try {
            XposedBridge.hookAllMethods(XposedHelpers.findClass("com.android.org.conscrypt.OpenSSLSignature", null), "engineVerify", new XC_MethodHook() {
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                    System.out.println("engineVerify");
                    paramAnonymousMethodHookParam.setResult(Boolean.valueOf(true));
                }
            });
        } catch (Exception e) {
            break one;
        }
        two:
        try {
            XposedHelpers.findAndHookMethod("java.security.MessageDigest", null, "isEqual", byte[].class, byte[].class, new XC_MethodHook() {
                protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                    paramAnonymousMethodHookParam.setResult(Boolean.valueOf(true));
                }
            });
        } catch (Exception e) {
            break two;
        }
        three:
        try {
            XposedHelpers.findAndHookMethod("java.security.Signature", null, "verify", byte[].class, new XC_MethodHook() {
                protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                    if (((((java.security.Signature) paramAnonymousMethodHookParam.thisObject).getAlgorithm().toLowerCase().equals("sha1withrsa")) || (((java.security.Signature) paramAnonymousMethodHookParam.thisObject).getAlgorithm().toLowerCase().equals("rsa-sha1")) || (((java.security.Signature) paramAnonymousMethodHookParam.thisObject).getAlgorithm().toLowerCase().equals("1.3.14.3.2.26with1.2.840.113549.1.1.1"))) && (Integer.valueOf(XposedHelpers.getIntField(paramAnonymousMethodHookParam.thisObject, "state")).intValue() == 3)) {
                        paramAnonymousMethodHookParam.setResult(Boolean.valueOf(true));
                    }
                }
            });
        } catch (Exception e) {
            break three;
        }
        try {
            XposedHelpers.findAndHookMethod("java.security.Signature", null, "verify", byte[].class, Integer.TYPE, Integer.TYPE, new XC_MethodHook() {
                protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {

                    if (((((java.security.Signature) paramAnonymousMethodHookParam.thisObject).getAlgorithm().toLowerCase().equals("sha1withrsa")) || (((java.security.Signature) paramAnonymousMethodHookParam.thisObject).getAlgorithm().toLowerCase().equals("rsa-sha1")) || (((java.security.Signature) paramAnonymousMethodHookParam.thisObject).getAlgorithm().toLowerCase().equals("1.3.14.3.2.26with1.2.840.113549.1.1.1"))) && (Integer.valueOf(XposedHelpers.getIntField(paramAnonymousMethodHookParam.thisObject, "state")).intValue() == 3)) {
                        paramAnonymousMethodHookParam.setResult(Boolean.valueOf(true));
                    }
                }
            });
        } catch (Exception e) {
            return;
        }
        return;
    }
}





