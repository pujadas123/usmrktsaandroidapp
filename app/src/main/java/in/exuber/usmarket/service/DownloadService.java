package in.exuber.usmarket.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import in.exuber.usmarket.R;
import in.exuber.usmarket.apimodels.downloadfile.downloadfileoutput.DownloadFileOutput;
import in.exuber.usmarket.utils.Api;
import in.exuber.usmarket.utils.Config;
import in.exuber.usmarket.utils.Constants;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class DownloadService extends IntentService {

    public DownloadService() {
        super("DownloadService");
    }

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    private int totalFileSize;

    //Declaring Retrofit log
    private static OkHttpClient.Builder builder;

    @Override
    protected void onHandleIntent(Intent intent) {


        Log.e("Came","Download Service");

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_start_download)
                .setContentTitle("Download")
                .setContentText("Downloading File")
                .setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());

        String fileUrl = intent.getStringExtra(Constants.INTENT_KEY_SELECTED_FILE_URL);

        initDownload(fileUrl);
    }



    //Func - Downloading File
    private void initDownload(String fileUrl) {

        builder = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        final Api api = retrofit.create(Api.class);

        Call<ResponseBody> request = api.downloadFileByUrl(fileUrl.replace(" ","%20"));
        try {

            downloadFile(request.execute().body(),fileUrl);

        } catch (IOException e) {

            e.printStackTrace();

            onDownloadError();


        }
    }

    private void downloadFile(ResponseBody body, String fileUrl) throws IOException {

        String fileName  = getDownloadFileName(fileUrl);
        Log.e("File Name",fileName);
        int count;
        byte data[] = new byte[1024 * 4];
        long fileSize = body.contentLength();
        InputStream bis = new BufferedInputStream(body.byteStream(), 1024 * 8);
        File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "file.zip");
        OutputStream output = new FileOutputStream(outputFile);
        long total = 0;
        long startTime = System.currentTimeMillis();
        int timeCount = 1;
        while ((count = bis.read(data)) != -1) {

            total += count;
            totalFileSize = (int) (fileSize / (Math.pow(1024, 2)));
            double current = Math.round(total / (Math.pow(1024, 2)));

            int progress = (int) ((total * 100) / fileSize);

            long currentTime = System.currentTimeMillis() - startTime;

            DownloadFileOutput downloadFileOutput = new DownloadFileOutput();
            downloadFileOutput.setTotalFileSize(totalFileSize);

            if (currentTime > 1000 * timeCount) {

                downloadFileOutput.setCurrentFileSize((int) current);
                downloadFileOutput.setProgress(progress);
                sendNotification(downloadFileOutput);
                timeCount++;
            }

            output.write(data, 0, count);
        }
        onDownloadComplete();
        output.flush();
        output.close();
        bis.close();

    }

    //Func - Setting Notification
    private void sendNotification(DownloadFileOutput downloadFileOutput){

        notificationBuilder.setProgress(100,downloadFileOutput.getProgress(),false);
        notificationBuilder.setContentText(String.format("Downloaded (%d/%d) MB",downloadFileOutput.getCurrentFileSize(),downloadFileOutput.getTotalFileSize()));
        notificationManager.notify(0, notificationBuilder.build());
    }


    //Func - Setting Notification
    private void onDownloadComplete(){

        DownloadFileOutput downloadFileOutput = new DownloadFileOutput();
        downloadFileOutput.setProgress(100);

        notificationManager.cancel(0);
        notificationBuilder.setProgress(0,0,false);
        notificationBuilder.setContentText("File Downloaded");
        notificationManager.notify(0, notificationBuilder.build());

    }

    //Func - Setting Notification
    private void onDownloadError(){

        notificationManager.cancel(0);
        notificationBuilder.setProgress(0,0,false);
        notificationBuilder.setContentText("Cannot Download File");
        notificationManager.notify(0, notificationBuilder.build());

    }

    //Func - Getting File name
    public static String getDownloadFileName(String fileUrl) {


        String[] parts = fileUrl.split("/");

        return parts[parts.length-1];
    }



    //Retrofit log
    public OkHttpClient.Builder getHttpClient() {

        if (builder == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder client = new OkHttpClient.Builder();
            client.addInterceptor(loggingInterceptor);
            client.writeTimeout(60000, TimeUnit.MILLISECONDS);
            client.readTimeout(60000, TimeUnit.MILLISECONDS);
            client.connectTimeout(60000, TimeUnit.MILLISECONDS);
            return client;
        }
        return builder;
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
