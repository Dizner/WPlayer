package org.jiaoyajing.dizner.wplayer.util;

import android.content.Context;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;

import org.jiaoyajing.dizner.wplayer.ContartUrl;
import org.jiaoyajing.dizner.wplayer.javabean.Mp3Info;
import org.jiaoyajing.dizner.wplayer.javabean.NewMp3;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Dizner on 2017/3/14.
 */

public class NetUtils {
    public static String getMp3Url(String songId) {
        String key = "3go8&$8*3*3h0k(2)2";
        byte[] keyBytes = key.getBytes();
        byte[] searchBytes = songId.getBytes();

        for (int i = 0; i < searchBytes.length; ++i) {
            searchBytes[i] ^= keyBytes[i % keyBytes.length];
        }

        MessageDigest mdInst = null;
        try {
            mdInst = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        mdInst.update(searchBytes);
        byte[] result = Base64.encode(mdInst.digest(), 1);
        String params = new String(result);
        params = params.replace("+", "-");
        params = params.replace("/", "_");
//        System.out.println("http://m2.music.126.net/" + params + "/" + songId + ".mp3");
        return "http://m2.music.126.net/" + params + "/" + songId + ".mp3";
    }

    public static void search(String kw, final OnResqutSuccess2 success) {
        Log.d("请求参数", ContartUrl.BAIDU_SEARCH_URL + kw);
        final List<Mp3Info> sList = new ArrayList<>();
        RequestParams entity = new RequestParams(ContartUrl.BAIDU_SEARCH_URL + kw);
        x.http().get(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                String re = result.replace("(", "").replace(");", "");
//                result.replace(");","");
                final List<String> srts = new ArrayList<String>();
                try {
                    JSONObject js = new JSONObject(re);
                    JSONArray song = js.getJSONArray("song");
                    for (int i = 0; i < song.length(); i++) {
                        srts.add(song.getJSONObject(i).getString("songid"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("一次解析结果：", srts.size() + "");
//                Log.d("请求结果",re);
//                Gson gson = new Gson();
//                NetMp3Info json = gson.fromJson(result, NetMp3Info.class);
                for (String srt : srts) {
                    NetUtils.getMp3(srt, new NetUtils.OnResqutSuccess() {
                        @Override
                        public void onSuccess(String result) {
                            Gson gson = new Gson();
                            NewMp3 json = null;
                            try {
                                json = gson.fromJson(result, NewMp3.class);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.d("JSON解析", "失败");
                            }

                            Mp3Info mp3 = new Mp3Info();
                            NewMp3.DataBean.SongListBean mp3Bean = json.getData().getSongList().get(0);
                            mp3.setAlbum(mp3Bean.getAlbumName());
                            mp3.setAlbumId(mp3Bean.getAlbumId());
                            mp3.setArtist(mp3Bean.getArtistName());
                            mp3.setDuration(mp3Bean.getTime() * 1000);
                            mp3.setId(mp3Bean.getSongId());
                            mp3.setLrcUrl(mp3Bean.getLrcLink());
                            mp3.setSize(mp3Bean.getSize());
                            mp3.setTitle(mp3Bean.getSongName());
                            mp3.setShowUrl(mp3Bean.getShowLink());
                            mp3.setUrl(mp3Bean.getSongLink());
                            mp3.setSongPic(mp3Bean.getSongPicRadio());
                            mp3.setIsMusic(1);
                            Log.d("MP3", mp3.toString());
                            sList.add(mp3);
                            Log.d("size", sList.size() + "");
                            if (sList.size() == srts.size()) {

                                Log.d("二次解析结果", sList.size() + "");
                                success.onSuccess(sList);
                            }
                        }
                    });
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 获取分类列表
     * @param type
     * @param success
     */
    public static void getTYpeList(int type,int page, final OnResqutSuccess2 success) {
        Log.d("分类列表请求参数", ContartUrl.FENLEI_URL +"offset="+page+"&type="+ type);
        final List<Mp3Info> sList = new ArrayList<>();
        RequestParams entity = new RequestParams(ContartUrl.FENLEI_URL +"offset="+page+"&type="+ type);
        x.http().get(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                String re = result.replace("(", "").replace(");", "");
//                result.replace(");","");
                final List<String> srts = new ArrayList<String>();
                try {
                    JSONObject js = new JSONObject(re);
                    JSONArray song = js.getJSONArray("song_list");
                    for (int i = 0; i < song.length(); i++) {
                        srts.add(song.getJSONObject(i).getString("song_id"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("一次解析结果：", srts.size() + "");
//                Log.d("请求结果",re);
//                Gson gson = new Gson();
//                NetMp3Info json = gson.fromJson(result, NetMp3Info.class);
                for (String srt : srts) {
                    NetUtils.getMp3(srt, new NetUtils.OnResqutSuccess() {
                        @Override
                        public void onSuccess(String result) {
                            Gson gson = new Gson();
                            NewMp3 json = null;
                            try {
                                json = gson.fromJson(result, NewMp3.class);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.d("JSON解析", "失败");
                            }

                            Mp3Info mp3 = new Mp3Info();
                            NewMp3.DataBean.SongListBean mp3Bean = json.getData().getSongList().get(0);
                            mp3.setAlbum(mp3Bean.getAlbumName());
                            mp3.setAlbumId(mp3Bean.getAlbumId());
                            mp3.setArtist(mp3Bean.getArtistName());
                            mp3.setDuration(mp3Bean.getTime() * 1000);
                            mp3.setId(mp3Bean.getSongId());
                            mp3.setLrcUrl(mp3Bean.getLrcLink());
                            mp3.setSize(mp3Bean.getSize());
                            mp3.setTitle(mp3Bean.getSongName());
                            mp3.setShowUrl(mp3Bean.getShowLink());
                            mp3.setUrl(mp3Bean.getSongLink());
                            mp3.setSongPic(mp3Bean.getSongPicRadio());
                            mp3.setIsMusic(1);
                            Log.d("MP3", mp3.toString());
                            sList.add(mp3);
                            Log.d("size", sList.size() + "");
                            if (sList.size() == srts.size()) {

                                Log.d("二次解析结果", sList.size() + "");
                                success.onSuccess(sList);
                            }
                        }
                    });
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 获取推荐列表
     * @param songId
     * @param success
     */
    public static void getTuiJian(String songId, final OnResqutSuccess2 success) {
        Log.d("推荐列表请求参数", ContartUrl.TUIJIAN_URL + songId);
        final List<Mp3Info> sList = new ArrayList<>();
        RequestParams entity = new RequestParams(ContartUrl.TUIJIAN_URL + songId);
        x.http().get(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                String re = result.replace("(", "").replace(");", "");
//                result.replace(");","");
                final List<String> srts = new ArrayList<String>();
                try {
                    JSONObject js = new JSONObject(re);
                    JSONArray song = js.getJSONArray("song");
                    for (int i = 0; i < song.length(); i++) {
                        srts.add(song.getJSONObject(i).getString("songid"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("一次解析结果：", srts.size() + "");
//                Log.d("请求结果",re);
//                Gson gson = new Gson();
//                NetMp3Info json = gson.fromJson(result, NetMp3Info.class);
                for (String srt : srts) {
                    NetUtils.getMp3(srt, new NetUtils.OnResqutSuccess() {
                        @Override
                        public void onSuccess(String result) {
                            Gson gson = new Gson();
                            NewMp3 json = null;
                            try {
                                json = gson.fromJson(result, NewMp3.class);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.d("JSON解析", "失败");
                            }

                            Mp3Info mp3 = new Mp3Info();
                            NewMp3.DataBean.SongListBean mp3Bean = json.getData().getSongList().get(0);
                            mp3.setAlbum(mp3Bean.getAlbumName());
                            mp3.setAlbumId(mp3Bean.getAlbumId());
                            mp3.setArtist(mp3Bean.getArtistName());
                            mp3.setDuration(mp3Bean.getTime() * 1000);
                            mp3.setId(mp3Bean.getSongId());
                            mp3.setShowUrl(mp3Bean.getShowLink());
                            mp3.setUrl(mp3Bean.getSongLink());
                            mp3.setSize(mp3Bean.getSize());
                            mp3.setTitle(mp3Bean.getSongName());
                            mp3.setUrl(mp3Bean.getShowLink());
                            mp3.setSongPic(mp3Bean.getSongPicRadio());
                            mp3.setIsMusic(1);
                            Log.d("MP3", mp3.toString());
                            sList.add(mp3);
                            Log.d("size", sList.size() + "");
                            if (sList.size() == srts.size()) {

                                Log.d("二次解析结果", sList.size() + "");
                                success.onSuccess(sList);
                            }
                        }
                    });
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 获取歌词文件
     *
     * @param context
     * @param lrcUrl
     * @param songId
     * @param success
     */
    public static void getLrc(Context context, String lrcUrl, String songId, final OnResqutSuccess success) {

        RequestParams params = new RequestParams(lrcUrl);
        //自定义保存路径，Environment.getExternalStorageDirectory()：SD卡的根目录
        params.setSaveFilePath(Environment.getExternalStorageDirectory() + File.separator + "WPlayerMusic" + File.separator + "lrc" + File.separator + songId + ".lrc");
        //自动为文件命名
//        params.setCacheDirName("lrc");
//        params.setAutoRename(true);
        x.http().get(params, new Callback.CommonCallback<File>() {
                    @Override
                    public void onSuccess(File result) {
                        try {
                            String path = result.getCanonicalPath();
                            success.onSuccess(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        ex.printStackTrace();
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                }
        );
    }

    /**
     * 获取歌词文件
     *
     * @param context
     * @param success
     */
    public static void downLoad(final Context context, final Mp3Info mp3Info, final OnResqutSuccess success) {
        final DbManager db = x.getDb(new DbManager.DaoConfig());
        String songlink = mp3Info.getUrl();
        String songName = mp3Info.getTitle();
        RequestParams params = new RequestParams(songlink);
        //自定义保存路径，Environment.getExternalStorageDirectory()：SD卡的根目录
        params.setSaveFilePath(Environment.getExternalStorageDirectory() + File.separator + "WPlayerMusic" + File.separator + songName + ".mp3");
        //自动为文件命名
//        params.setCacheDirName("lrc");
//        params.setAutoRename(true);
        x.http().get(params, new Callback.CommonCallback<File>() {
                    @Override
                    public void onSuccess(File result) {
                        try {
                            String path = result.getCanonicalPath();
                            success.onSuccess(path);
                            Log.d("下载完成", path);
                            mp3Info.setUrl(path);
                            db.save(mp3Info);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        ex.printStackTrace();
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                }
        );
    }

    /**
     * 获取单个mp3的详细信息
     * @param songId
     * @param success
     */
    public static void getMp3(String songId, final OnResqutSuccess success) {
        Log.d("请求参数", ContartUrl.SONG_DETIL_URL + songId);
        RequestParams entity = new RequestParams(ContartUrl.SONG_DETIL_URL + songId);
        x.http().get(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("原始数据：", result);
                success.onSuccess(result.replace("\\\\", "\\").replace("\\/", "/"));
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public interface OnResqutSuccess {
        void onSuccess(String result);
    }

    public interface OnResqutSuccess2 {
        void onSuccess(List<Mp3Info> result);
    }
}
