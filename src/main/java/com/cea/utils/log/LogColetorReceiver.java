package com.cea.utils.log;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Carlos on 12/08/2014.
 */
public class LogColetorReceiver extends BroadcastReceiver{

    public static final long MINUTE = 60*1000;
    public static final long HOUR = 60*MINUTE;
    public static final long DAY = 24*HOUR;

    public LogColetorReceiver(){

    }

    public static void start(Context context, String fileName, String tagFilter, long time, LogLevel level) {
        getApplicationLogPath(context);
        AlarmManager mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, LogColetorReceiver.class);
        intent.putExtra("fileName", fileName);
        String loglvl = level == null ? LogLevel.VERBOSE.toString() : level.toString();
        intent.putExtra("loglevel", loglvl);
        intent.putExtra("tagFilter", tagFilter);
        PendingIntent pedingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10 * 1000, time, pedingIntent);
    }

    private void saveText(String fileName,String directory, String text) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/" + directory);
        myDir.mkdirs();
        File file = new File(myDir, fileName.replaceAll("\\.txt","") + ".txt");
        try {
            FileWriter pw = new FileWriter(file, true);
            pw.write(text);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String tag = intent.getStringExtra("tagFilter");
        String loglvl = intent.getStringExtra("loglevel");
        try {
            Process process = Runtime.getRuntime().exec("logcat -d -v long " + loglvl.toString() + (tag == null ? "" : (" -s " + tag)));
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            StringBuilder log = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if(!line.equals("") && !line.contains("beginning of")) {
                    log.append(line + "\n");
                }
            }
            saveText(intent.getStringExtra("fileName"), getApplicationLogPath(context), log.toString());
            Runtime.getRuntime().exec("logcat -c");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getApplicationLogPath(Context context) {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.getApplicationLabel(context.getApplicationInfo()).toString().replaceAll(" ", "") + "Logs";
    }
}
