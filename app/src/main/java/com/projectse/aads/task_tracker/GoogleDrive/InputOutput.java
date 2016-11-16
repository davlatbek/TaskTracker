package com.projectse.aads.task_tracker.GoogleDrive;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Liza on 16.11.2016 г..
 */
public class InputOutput {
    private static final int BUF_SZ = 4096;
    private static final String TAG = "InputOutput";

    public static boolean copy(InputStream is, OutputStream os) {
        boolean success = true;
        byte[] buf = null;
        BufferedInputStream bufIS = null;
        if (is != null) try {
            bufIS = new BufferedInputStream(is);
            buf = new byte[BUF_SZ];
            int cnt;
            while ((cnt = bufIS.read(buf)) >= 0) {
                os.write(buf, 0, cnt);
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            success = false;
        }
        finally {
            try {
                if (bufIS != null) bufIS.close();
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
                success = true;
            }
        }

        return success;
    }
}
