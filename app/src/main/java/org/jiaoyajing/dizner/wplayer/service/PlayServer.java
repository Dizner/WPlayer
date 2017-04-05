package org.jiaoyajing.dizner.wplayer.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import org.jiaoyajing.dizner.wplayer.ContartMothad;
import org.jiaoyajing.dizner.wplayer.R;
import org.jiaoyajing.dizner.wplayer.activity.PlayActivity;
import org.jiaoyajing.dizner.wplayer.javabean.Mp3Info;
import org.jiaoyajing.dizner.wplayer.util.LrcView;
import org.jiaoyajing.dizner.wplayer.util.Mp3Utils;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * start_01
 * puse_01
 * 播放器核心服务
 *
 * @author Dizner
 */
public class PlayServer extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    private MediaPlayer mPlayer;
    private int currentPodtion;//播放进度
    public static List<Mp3Info> mp3Infos;
    private MusicUpdateListener musicUpdateListener;
    private ExecutorService es = Executors.newSingleThreadExecutor();
    public static final int XUNHUAN_PLAY = 1;
    public static final int SUIJI_PLAY = 2;
    public static final int DANQU_PLAY = 3;
    private int play_mode = XUNHUAN_PLAY;
    private boolean isPause = false;
    private NotificationCompat.Builder builder = null;
    private Notification notification = null;
    private RemoteViews contentView;
    private NotificationManager manager = null;
    private ImageView nmuimg;
    private Mp3Receiver receiver;
    private Mp3Info currentMp3;
    private LrcView mLrc;
    private String path;
    private RelativeLayout notView;
    private ImageView btnStart, btnNext, btnPre, ivSongImg;
    private TextView tvName, tvAuthor;
    private static long timer = 1;

    public boolean isPause() {
        return isPause;
    }

    public PlayServer() {
        // TODO Auto-generated constructor stub
    }


    public void setLrcView(LrcView mLrc) {
        this.mLrc = mLrc;
    }

    public class PlayBinder extends Binder {
        public PlayServer getPlayServer() {
            return PlayServer.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
//		throw new UnsupportedOperationException("Not yet implemented");
        return new PlayBinder();
    }

    /**
     * 播放模式
     *
     * @return
     */
    public int getPlay_mode() {
        return play_mode;
    }

    public void setPlay_mode(int play_mode) {
        this.play_mode = play_mode;
    }

    public List<Mp3Info> getMp3Infos() {
        return mp3Infos;
    }

    public void setTimer(long time) {
        Log.d("时间设置为：", "" + time);
        if (mPlayer!=null&&mPlayer.isPlaying()) {
            if (time>0) {
                isRun = true;
            }else {
                isRun = false;
            }
            this.timer = time;
        }else {
            Toast.makeText(this,"未播放状态，不能设置",Toast.LENGTH_SHORT).show();
        }

    }

    public void setMp3Infos(List<Mp3Info> mp3Info) {
        mp3Infos .clear();
        this.mp3Infos.addAll(mp3Info);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer = new MediaPlayer();
        mp3Infos = Mp3Utils.getMp3Info(this);
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnErrorListener(this);
        mp3Infos = Mp3Utils.getMp3Info(this);
        if (mp3Infos.size() > 0)
            startupDateUi();
        initNotification();

        initRecover();
    }

    public void startupDateUi() {
        mp3Infos = Mp3Utils.getMp3Info(this);
        es.execute(undateStatusRunnable);
    }

    private void initRecover() {
        IntentFilter filter = new IntentFilter();
        receiver = new Mp3Receiver();
        filter.addAction("next");
        filter.addAction("start");
        filter.addAction("pre");
        registerReceiver(receiver, filter);

    }

    public void unRegisterRe() {
        unregisterReceiver(receiver);
        stopSelf();
        manager.cancelAll();
    }

    private void initNotification() {
        builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setSmallIcon(R.mipmap.play_logo);
//		builder.setTicker(mp3Infos.get(currentPodtion).getTitle());
        builder.setTicker("耳朵爱上他，然后怀孕啦");
        notification = builder.build();
        notification.flags = Notification.FLAG_NO_CLEAR;


        notView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.notificition_show, null, false);

        btnNext = (ImageView) notView.findViewById(R.id.nnextbtn);
        btnPre = (ImageView) notView.findViewById(R.id.nprebtn);
        btnStart = (ImageView) notView.findViewById(R.id.nstartbtn);
        ivSongImg = (ImageView) notView.findViewById(R.id.nmuimg);
        tvName = (TextView) notView.findViewById(R.id.nname);
        tvAuthor = (TextView) notView.findViewById(R.id.nsauthor);


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendBroadcast(new Intent("next"));
            }
        });
        btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendBroadcast(new Intent("pre"));
            }
        });
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendBroadcast(new Intent("start"));
            }
        });


        contentView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notificition_show);
//        contentView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notificition_show);
        manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notification.contentView = contentView;
        PendingIntent pendingintent = PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);
        notification.contentIntent = pendingintent;
        Intent intent1 = new Intent(getApplicationContext(), PlayActivity.class);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(getApplicationContext(), 1, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.nmuimg, pendingIntent1);
        Intent intent2 = new Intent("next");
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(getApplicationContext(), 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.nnextbtn, pendingIntent2);
        Intent intent3 = new Intent("start");
        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(getApplicationContext(), 0, intent3, PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.nstartbtn, pendingIntent3);
        Intent intent4 = new Intent("pre");
        PendingIntent pendingIntent4 = PendingIntent.getBroadcast(getApplicationContext(), 0, intent4, PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.nprebtn, pendingIntent4);

    }

    private static boolean isRun;
    Runnable undateStatusRunnable = new Runnable() {

        @Override
        public void run() {
            while (true) {
                if (musicUpdateListener != null && mPlayer != null && mPlayer.isPlaying()) {
                    musicUpdateListener.onPublish(getCurrentProgress(), getCurrentPosition());

                    if (timer >= 0 && isRun)
                        musicUpdateListener.onPublish(timer);
                    timer -= 1000;
                }
                SystemClock.sleep(1000);
            }

        }

    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

//    Runnable undateTimeRunnable = new Runnable() {
//        @Override
//        public void run() {
//            while (true) {
//                musicUpdateListener.onPublish(timer);
//                if (timer > 0) {
//                    timer -= 1000;
//                }else {
//                    break;
//                }
//                Log.d("计时", timer + "");
////                }
//                SystemClock.sleep(1000);
//            }
//
//        }
//
//    };

    //更新进度
    public int getCurrentProgress() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            return mPlayer.getCurrentPosition();
        }
        return 0;
    }

    public Mp3Info getCurrentPosition() {

        return currentMp3;
    }


    //播放
    public void play(int position) {

        if (position >= 0 && position < mp3Infos.size()) {

            final Mp3Info mp3Info = mp3Infos.get(position);
            currentMp3 = mp3Info;
            //记录播放历史
            ContartMothad.addHistory((int) mp3Info.getId(), new ContartMothad.OnaddLikeSuccess() {
                @Override
                public void onSuccess(int songId) {
                    if (songId == 1) {
                        Log.d("记录播放历史", "ID = " + mp3Info.getId() + "结果");
                    }
                }
            });

            contentView.setTextViewText(R.id.nname, mp3Info.getTitle());
            contentView.setTextViewText(R.id.nsauthor, mp3Info.getArtist());
            contentView.setImageViewBitmap(R.id.muimg, BitmapFactory.decodeFile(mp3Info.getSongPic()));
            try {
                mPlayer.reset();
                mPlayer.setDataSource(this, Uri.parse(mp3Info.getUrl()));
                mPlayer.prepare();
                mPlayer.start();
                currentPodtion = position;
                contentView.setImageViewResource(R.id.nstartbtn, R.mipmap.puse_01);
                manager.notify(1, notification);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (musicUpdateListener != null) {
                musicUpdateListener.onChange(currentPodtion);
            }

        }
    }

    //暂停
    public void pause() {
        if (mPlayer.isPlaying()) {
            contentView.setImageViewResource(R.id.nstartbtn, R.mipmap.start_01);
            mPlayer.pause();
            isPause = true;
            if (musicUpdateListener != null) {
                musicUpdateListener.onChange(currentPodtion);
            }
            manager.notify(1, notification);
        }
    }

    //下一曲
    public void next() {
        if (currentPodtion + 1 >= mp3Infos.size() - 1) {
            currentPodtion = 0;
        } else {
            currentPodtion++;
        }
        play(currentPodtion);
    }

    //上一曲
    public void prev() {
        if (currentPodtion - 1 < 0) {
            currentPodtion = mp3Infos.size() - 1;
        } else {
            currentPodtion--;
        }
        play(currentPodtion);
    }

    //开始
    public void start() {
        if (mPlayer != null && !mPlayer.isPlaying()) {
            mPlayer.start();
            contentView.setImageViewResource(R.id.nstartbtn, R.mipmap.puse_01);
            if (musicUpdateListener != null) {
                musicUpdateListener.onChange(currentPodtion);
            }
            manager.notify(1, notification);
        }
    }

    public int getDuration() {
        return mPlayer.getDuration();
    }

    public void seekTo(int msec) {
        mPlayer.seekTo(msec);
    }

    public interface MusicUpdateListener {
        public void onPublish(int progress, Mp3Info mp3);

        public void onChange(int position);

        void onPublish(long timer);
    }

    public void setMusicUpdateListener(MusicUpdateListener musicUpdateListener) {
        this.musicUpdateListener = musicUpdateListener;
    }

    public boolean isPlaying() {
        if (mPlayer != null) {
            return mPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (es != null && !es.isShutdown()) {
            es.shutdown();
        }
    }
//	public void endEs(){
//		if(es!=null&&!es.isShutdown()){
//			es.shutdown();
//		}
//	}
//	public void startEs(){
//		if(es==null||es.isShutdown()){
//			es.execute(undateStatusRunnable);
//		}
//	}

    /**
     * XUNHUAN_PLAY=1;
     * public static final int SHUNXV_PLAY=1;
     * public static final int SUIJI_PLAY=2;
     * public static final int DANQU_PLAY=3;
     *
     * @param mp
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        switch (play_mode) {
            case XUNHUAN_PLAY:
                next();
                break;
            case SUIJI_PLAY://���
                play(new Random().nextInt(mp3Infos.size()));
                break;
            case DANQU_PLAY://����
                play(currentPodtion);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mPlayer.reset();
        return false;
    }

    /**
     * 控制通知栏
     */
    public class Mp3Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {


            String ctrl_code = intent.getAction();//��ȡaction��ǣ��û����ֵ���¼�
            if (mPlayer != null) {
                switch (ctrl_code) {
                    case "start":
                        if (isPlaying()) {
//	        			contentView.setImageViewResource(R.id.nstartbtn, R.drawable.start_1);
                            pause();
                        } else {
                            //play��������õ�����UI�ķ���
//	    					contentView.setImageViewResource(R.id.nstartbtn, R.drawable.puse_01);
                            if (isPause()) {
//				                contentView.setImageViewResource(R.id.nstartbtn, R.drawable.puse_01);
                                start();
                            } else {
                                play(0);
                            }
                        }
                        break;
                    case "next":
                        next();
                        System.out.println("next");
                        break;
                    case "pre":
                        prev();
                        System.out.println("last");
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void serviceExit() {
        stopSelf();
        manager.cancelAll();
    }
}
