package com.coderstory.toolkit.XposedModule;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Base64;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


/**
 * Created by CoderStory on 2016/6/4.
 */
public class CorePatch implements IXposedHookZygoteInit, IXposedHookLoadPackage {
    private Context PMcontext = null;
    private Context ctx = null;

    public void initZygote(IXposedHookZygoteInit.StartupParam paramStartupParam)
            throws Throwable {
        final    XSharedPreferences prefs = new XSharedPreferences("com.coderstory.toolkit", "UserSettings");
        prefs.makeWorldReadable();

        XposedHelpers.findClass("android.content.pm.PackageParser", null);
        XposedHelpers.findClass("java.util.jar.JarVerifier$VerifierEntry", null);
        one:
        try {
            XposedBridge.hookAllMethods(XposedHelpers.findClass("com.android.org.conscrypt.OpenSSLSignature", null), "engineVerify", new XC_MethodHook() {
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                    prefs.reload();
                    if (!prefs.getBoolean("CorePatcher", false)) {
                        return;
                    }
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
                    prefs.reload();
                    if (!prefs.getBoolean("CorePatcher", false)) {
                        return;
                    }
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
                    prefs.reload();
                    if (!prefs.getBoolean("CorePatcher", false)) {
                        return;
                    }
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
                    prefs.reload();
                    if (!prefs.getBoolean("CorePatcher", false)) {
                        return;
                    }
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

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam paramLoadPackageParam)
            throws Throwable {
        final  XSharedPreferences prefs = new XSharedPreferences("com.coderstory.toolkit", "UserSettings");
        prefs.makeWorldReadable();


        if (("android".equals(paramLoadPackageParam.packageName)) && (paramLoadPackageParam.processName.equals("android"))) {

            Class localClass= XposedHelpers.findClass("com.android.server.pm.PackageManagerService", paramLoadPackageParam.classLoader);
            XposedBridge.hookAllConstructors(XposedHelpers.findClass("com.android.server.pm.PackageManagerService", paramLoadPackageParam.classLoader), new XC_MethodHook() {
                protected void afterHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                    PMcontext = ((Context) paramAnonymousMethodHookParam.args[0]);
                }
            });
            XposedBridge.hookAllMethods(localClass, "compareSignatures", new XC_MethodHook() {
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {

                    prefs.reload();
                    if (!prefs.getBoolean("CorePatcher", false)) {
                        return;
                    }

                    PackageInfo localObject;
                    int k;
                    int j;
                    android.content.pm.Signature[] arrayOfSignature2;
                    int i;
                    int m;
                    String ll;
                    try {
                        localObject = PMcontext.getPackageManager().getPackageInfo("android", PackageManager.GET_SIGNATURES);
                        if (localObject.signatures[0] == null) {
                            return;
                        }
                        ll = Base64.encodeToString(localObject.signatures[0].toByteArray(), 0).replaceAll("\n", "");
                        k = 0;
                        j = 0;
                        android.content.pm.Signature[] arrayOfSignature1 = (android.content.pm.Signature[]) paramAnonymousMethodHookParam.args[0];
                        arrayOfSignature2 = (android.content.pm.Signature[]) paramAnonymousMethodHookParam.args[1];
                        i = k;
                        if (arrayOfSignature1 != null) {
                            i = k;
                            if (arrayOfSignature1.length > 0) {
                                m = arrayOfSignature1.length;
                                k = 0;
                                for (; ; ) {
                                    i = j;
                                    if (k >= m) {
                                        break;
                                    }
                                    if (Base64.encodeToString(arrayOfSignature1[k].toByteArray(), 0).replaceAll("\n", "").equals(ll)) {
                                        j = 1;
                                    }
                                    k += 1;
                                }
                            }
                        }
                        k = i;
                    } catch (Exception p2) {
                        p2.printStackTrace();
                        return;
                    }
                    if (arrayOfSignature2 != null) {
                        k = i;
                        if (arrayOfSignature2.length > 0) {
                            m = arrayOfSignature2.length;
                            j = 0;
                            for (; ; ) {
                                k = i;
                                if (j >= m) {
                                    break;
                                }
                                if (Base64.encodeToString(arrayOfSignature2[j].toByteArray(), 0).replaceAll("\n", "").equals(ll)) {
                                    i = 1;
                                }
                                j += 1;
                            }
                        }
                    }
                    if (k == 0) {
                        paramAnonymousMethodHookParam.setResult(Integer.valueOf(0));
                    }
                }
            });

            XposedBridge.hookAllMethods(localClass, "installPackageAsUser", new XC_MethodHook()
            {
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable
                {
                    prefs.reload();
                    if (!prefs.getBoolean("CorePatcher", false)) {
                        return;
                    }
                    for (int i = 2;; i = 1)
                    {
                        int j = ((Integer)paramAnonymousMethodHookParam.args[i]).intValue();
                        if ((j & 0x80) == 0) {
                            paramAnonymousMethodHookParam.args[i] = Integer.valueOf(j | 0x80);
                        }
                        return;
                    }
                }
            });
        }
        if ("com.android.settings".equals(paramLoadPackageParam.packageName)) {
            XposedBridge.hookAllMethods(XposedHelpers.findClass("com.android.settings.applications.AppOpsDetails", paramLoadPackageParam.classLoader), "isPlatformSigned",new XC_MethodHook() {
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable
                {
                    paramAnonymousMethodHookParam.setResult(Boolean.valueOf(false));
                }
            });
        }

    }
}







