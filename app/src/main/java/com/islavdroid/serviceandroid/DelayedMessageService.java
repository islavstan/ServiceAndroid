package com.islavdroid.serviceandroid;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;


public class DelayedMessageService extends IntentService {
    public static final String EXTRA_MESSAGE = "message";
  //  private Handler handler;
  public static final int NOTIFICATION_ID = 999; //Используется для идентификации уведомлений. Число выбирается произвольно



    public DelayedMessageService() {
        super("DelayedMessageService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Метод содержит код, который должен выполняться при получении интента службой
        synchronized (this){
            try {
                wait(10000);
            }catch (InterruptedException i){
                i.printStackTrace();
            }
        }
        String text = intent.getStringExtra(EXTRA_MESSAGE);
        showText(text);


    }

 /*   @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //чтобы тост отобразился в основном потоке приложения handler нужно проинициализировать в этом методе
        handler =new Handler();
        return super.onStartCommand(intent, flags, startId);
    }*/

    private void showText(final String text) {
     /*   handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            }
        });*/

        Intent intent = new Intent(this, MainActivity.class);
        // Объект TaskStackBuilder позволяет работать с историей активностей, используемой кнопкой Назад
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        //Эти строки обеспечивают правильную работу кнопки Назад при запуске активности.
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        //Если подходящий отложенный интент уже существует, оставить его и заменить дополнительные данные данными из нового интента
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
  //строим notification
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setContentText(text)
                .build();

        //Вывести уведомление с использованием службы уведомлений Android
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);



}}
