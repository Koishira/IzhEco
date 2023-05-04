package com.example.izheco.update;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.tv.TvContract;
import android.net.Uri;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppUpdate {
    private String TAG = "AppUpdate";
    private Context _context = null;
    private String ApkFileUrl = "";
    public boolean DoUpdate(Context context, String serverAddress, int port, String extraParam) {
        this._context = context;
        String strUpdateUrl = String.format("https://%s:%d", serverAddress, port);
        Log.d(TAG, "AppUpdate.DoUpdate(): " + strUpdateUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(strUpdateUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AppUpdateInterface intAppUpdate = retrofit.create(AppUpdateInterface.class);
        Call<AppFileData> serverFileInfo = intAppUpdate.getLatestAppInfo("extraKey","extraValue");
        serverFileInfo.enqueue(new Callback<AppFileData>() {
            @Override
            public void onResponse(Call<AppFileData> call, Response<AppFileData> response) {
                if(!response.isSuccessful()) {
                    Log.d(TAG, "AppUpdate.DoUpdate().onResponse(): " + response.code());
                }
                AppFileData FileInfoOnServer = response.body();
                AppFileData FileInfoOnLocal = getLocalAppInfo();
                ApkFileUrl = String.format("https://%s:%d/%s", serverAddress, port, _context.getPackageName());
                if(!(FileInfoOnServer.getVersionName().equals(FileInfoOnLocal.getVersionName()) && FileInfoOnServer.getVersionCode() == FileInfoOnLocal.getVersionCode())) {
                    Log.d(TAG, "AppUpdate.DoUpdate.onResponse(): Update Start ===>");
                    DownloadAppDialog();
                }
            }

            @Override
            public void onFailure(Call<AppFileData> call, Throwable t) {
                Log.d(TAG, "AppUpdate.DoUpdate.onFailure(): " + t.getMessage());
            }
        });
        return true;
    }

    private AppFileData getLocalAppInfo() {
        AppFileData Return = new AppFileData();
        final PackageManager pm = this._context.getPackageManager();
        final String PackageName = this._context.getPackageName();
        try {
            PackageInfo PI = pm.getPackageInfo(PackageName, 0);
            Return.setVersionName(PI.versionName);
            Return.setVersionCode(PI.versionCode);
        }
        catch (Exception e) {
            Log.d(TAG, "getLocalAppInfo().Exception: " + e.getMessage());
        }
        return Return;
    }

    private void DownloadAppDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this._context)
                .setTitle("New Version available.")
                .setMessage("Please, update app to new version.")
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                redirectStore(ApkFileUrl);
                                Log.d(TAG, "AppUpdate.DownloadAppDialog(): CLick update");
                            }
                        }).setNegativeButton("No, thanks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((Activity)_context).finish();
                    }
                }).create();
        alertDialog.show();
    }

    private void redirectStore(String Url) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this._context.startActivity(intent);
    }
}
