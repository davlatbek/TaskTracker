package com.projectse.aads.task_tracker.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by root on 12.10.16.
 */
public class ShPrefUtils {
    private static String FILE_NAME = "ttpreferences";
    private static String KEY_PLAY_SOUNDS = "IS_PLAY_SOUNDS";

    public static void setSoundsPlay(Context context, boolean fl){

        SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor  editor = sd.edit();

        editor.putBoolean(KEY_PLAY_SOUNDS, fl);

        editor.commit();
    }

    public static boolean isPlaySounds(Context context){

        SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        boolean isPlay = sd.getBoolean(KEY_PLAY_SOUNDS, true);

        return isPlay;
    }
}
