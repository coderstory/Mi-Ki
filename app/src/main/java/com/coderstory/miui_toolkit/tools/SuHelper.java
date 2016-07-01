package com.coderstory.miui_toolkit.tools;
/**
 * 指向su命令的帮助类
 * Created by cc on 2016/6/7.
 */
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import com.coderstory.miui_toolkit.R;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
public abstract class SuHelper {
    public final boolean execute() {
        boolean retval = false;
        try {
            ArrayList<String> commands = getCommandsToExecute();
            if (null != commands && commands.size() > 0) {
                Process process = Runtime.getRuntime().exec("su");
                DataOutputStream os = new DataOutputStream(process.getOutputStream());
                for (String currCommand : commands) {
                    os.writeBytes(currCommand + "\n");
                    os.flush();
                }
                os.writeBytes("exit\n");
                os.flush();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        process.getInputStream()));
                int read;
                char[] buffer = new char[4096];
                StringBuilder output = new StringBuilder();
                while ((read = reader.read(buffer)) > 0) {
                    output.append(buffer, 0, read);
                }
                reader.close();
                try {
                    int suProcessRetval = process.waitFor();
                    retval = 255 != suProcessRetval;
                    System.out.println("BBBB: " + output.toString());
                } catch (Exception ex) {
                    //Log.e("Error executing root action", ex);
                }
            }
        } catch (IOException ex) {
            Log.w("ROOT", "Can't get root access", ex);
        }  catch (Exception ex) {
            Log.w("ROOT", "Error executing internal operation", ex);
        }
        return retval;
    }
    protected abstract ArrayList<String> getCommandsToExecute() throws UnsupportedEncodingException;

    /*实现弹窗确定执行某条命令*/
    public static void showTips(final String commandText, String messageText, final Context mContext) {
        AlertDialog builder = new AlertDialog.Builder(mContext)
                .setTitle(R.string.Tips_Title)
                .setMessage(messageText)
                .setPositiveButton(R.string.Btn_Sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Runtime.getRuntime().exec(new String[]{"su", "-c", commandText});
                        } catch (IOException e) {
                            Log.d("su", e.getMessage());
                            new AlertDialog.Builder(mContext).setTitle(R.string.Tips_Title_Error).setMessage(
                                    e.getMessage()).setPositiveButton(R.string.Btn_Sure, null).show();
                        }
                    }
                })
                .setNegativeButton(R.string.Btn_Cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
        builder.show();
    }
}