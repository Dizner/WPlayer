package org.jiaoyajing.dizner.wplayer.util;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import org.jiaoyajing.dizner.wplayer.javabean.LinkedTable;
import org.jiaoyajing.dizner.wplayer.javabean.Mp3Info;
import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class Mp3Utils {
    private static boolean isNull = false;

    public static Mp3Info getMp3Info(Context context, long _id) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
                MediaStore.Audio.Media._ID + "=" + _id, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        Mp3Info mp3Info = null;
        if (cursor != null) {
            if (cursor.moveToNext()) {
                mp3Info = new Mp3Info();
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
                if (isMusic != 0) {
                    mp3Info.setId(id);
                    mp3Info.setTitle(title);
                    mp3Info.setArtist(artist);
                    mp3Info.setAlbum(album);
                    mp3Info.setAlbumId(albumId);
                    mp3Info.setDuration(duration);
                    mp3Info.setSize(size);
                    mp3Info.setUrl(url);
                    mp3Info.setSongPic(getImagePathWithSongId(context, id, albumId));
                }
            }
        } else {
            isNull = true;
            return null;
        }

        cursor.close();
        return mp3Info;
    }

    public static boolean isNull() {
        return isNull;
    }

    /**
     * 获取Mp3集合
     */
    public static ArrayList<Mp3Info> getMp3Infos(Context context) {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig();
        DbManager db = x.getDb(daoConfig);
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
                MediaStore.Audio.Media.DURATION + ">=180000", null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        ArrayList<Mp3Info> list = new ArrayList<Mp3Info>();
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                Mp3Info mp3Info = new Mp3Info();
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
                if (isMusic != 0) {
                    mp3Info.setId(id);
                    mp3Info.setTitle(title);
                    mp3Info.setArtist(artist);
                    mp3Info.setAlbum(album);
                    mp3Info.setAlbumId(albumId);
                    mp3Info.setDuration(duration);
                    mp3Info.setSize(size);
                    mp3Info.setUrl(url);
                    mp3Info.setSongPic(getImagePathWithSongId(context, id, albumId));
                }
                try {
                    db.saveOrUpdate(mp3Info);
                } catch (DbException e) {
                    Toast.makeText(context, "读取本地音乐出现问题,请检查读取部分代码", Toast.LENGTH_SHORT).show();
                }
                list.add(mp3Info);
            }
            Toast.makeText(context, "扫描到本地歌曲共" + list.size() + "首", Toast.LENGTH_SHORT);
        } else {
            isNull = true;
            return null;
        }

        cursor.close();
        return list;
    }

    /**
     * 获取Mp3集合
     */
    public static List<Mp3Info> getMp3Info(Context context, int listId) {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig();
        DbManager db = x.getDb(daoConfig);
        List<Mp3Info> list = new ArrayList<>();
        if (listId <= 0) {
            try {
                list = db.findAll(Mp3Info.class);
            } catch (DbException e) {
                Toast.makeText(context, "读取本地音乐出现问题,请检查读取部分代码", Toast.LENGTH_SHORT).show();
            }
        } else {
            try {
                List<LinkedTable> linkedTableList = db.selector(LinkedTable.class).where(WhereBuilder.b("listId", "=", listId)).findAll();
                list.clear();
                for (LinkedTable table : linkedTableList) {

                    list.addAll(db.selector(Mp3Info.class).where(WhereBuilder.b("_id", "=", table.getSongId())).findAll());
                }
            } catch (DbException e) {
                Toast.makeText(context, "读取本地音乐出现问题,请检查读取部分代码", Toast.LENGTH_SHORT).show();
            }
        }
        Toast.makeText(context, "扫描到本地歌曲共" + list.size() + "首", Toast.LENGTH_SHORT);
        return list;
    }

    /**
     * 获取Mp3集合
     */
    public static List<Mp3Info> getMp3Info(Context context) {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig();
        DbManager db = x.getDb(daoConfig);
        List<Mp3Info> list = new ArrayList<>();
        try {
            list = db.selector(Mp3Info.class).findAll();

            Log.d("扫描到本地歌曲共3", (list == null ? 0 : list.size()) + "首");
        } catch (DbException e) {
            e.printStackTrace();
            Log.d("", "读取本地音乐出现问题,请检查读取部分代码");
        }
        Log.d("扫描到本地歌曲共", (list == null ? 0 : list.size()) + "首");
        return list == null ? new ArrayList<Mp3Info>() : list;
    }
    /**
     * 获取Mp3集合
     */
    public static List<Mp3Info> getMp3Info(Context context,String tag) {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig();
        DbManager db = x.getDb(daoConfig);
        List<Mp3Info> list = new ArrayList<>();
        switch (tag) {
            case "all":
                try {
                    list = db.selector(Mp3Info.class).findAll();

                    Log.d("扫描到本地歌曲共3", (list == null ? 0 : list.size()) + "首");
                } catch (DbException e) {
                    e.printStackTrace();
                    Log.d("", "读取本地音乐出现问题,请检查读取部分代码");
                }
                break;
            case "like":
                try {
                    list = db.selector(Mp3Info.class).where(WhereBuilder.b("isLike","=",true)).findAll();
                    Log.d("收藏的歌曲：", (list == null ? 0 : list.size()) + "首");
                } catch (DbException e) {
                    e.printStackTrace();
                    Log.d("", "读取本地音乐出现问题,请检查读取部分代码");
                }
                break;
            case "history":
                try {
                    list = db.selector(Mp3Info.class).where(WhereBuilder.b("history","=",true)).findAll();
                    Log.d("历史记录：", (list == null ? 0 : list.size()) + "首");
                } catch (DbException e) {
                    e.printStackTrace();
                    Log.d("", "读取本地音乐出现问题,请检查读取部分代码");
                }
                break;
        }

        Log.d("扫描到本地歌曲共", (list == null ? 0 : list.size()) + "首");
        return list == null ? new ArrayList<Mp3Info>() : list;
    }

    /**
     * 获取Mp3集合
     */
    public static void scanMp3Info(final Context context, final OnScanOverLinister linister) {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig();
        final DbManager db = x.getDb(daoConfig);
        final Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
                MediaStore.Audio.Media.DURATION + ">=180000", null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        final List<Mp3Info> list = new ArrayList<Mp3Info>();

        try {
            db.dropTable(Mp3Info.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (cursor.getCount() > 0) {
                    int s = 0;
                    for (int i = 0; i < cursor.getCount(); i++) {
                        cursor.moveToNext();
                        Mp3Info mp3Info = new Mp3Info();
                        long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                        String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                        String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                        long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                        long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                        long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                        String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                        int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
                        if (isMusic != 0) {
                            mp3Info.setId(id);
                            mp3Info.setTitle(title);
                            mp3Info.setArtist(artist);
                            mp3Info.setAlbum(album);
                            mp3Info.setAlbumId(albumId);
                            mp3Info.setDuration(duration);
                            mp3Info.setSize(size);
                            mp3Info.setUrl(url);
                            mp3Info.setSongPic(getImagePathWithSongId(context, id, albumId));
                        }

                        list.add(mp3Info);
                    }
                    try {
                        db.replace(list);
                        s++;
                    } catch (DbException e) {
                        Toast.makeText(context, "读取本地音乐出现问题,请检查读取部分代码", Toast.LENGTH_SHORT).show();
                    }

                    linister.onScanOver(s);
                    Log.d("", "初始扫描到本地歌曲共----" + s + "首");
                }
                cursor.close();
            }
        }).start();


    }

    public interface OnScanOverLinister {
        void onScanOver(int count);
    }

    public static String formatTime(long time) {
        String min = time / (1000 * 60) + "";
        String sec = time % (1000 * 60) + "";
        if (min.length() < 2) {
            min = "0" + time / (1000 * 60) + "";
        } else {
            min = time / (1000 * 60) + "";
        }
        if (sec.length() == 4) {
            sec = "0" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 3) {
            sec = "00" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 2) {
            sec = "000" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 1) {
            sec = "0000" + (time % (1000 * 60)) + "";
        }
        return min + ":" + sec.trim().substring(0, 2);
    }


    public static Bitmap getArtworkFromFile(Context context, Mp3Info mp3) {
        Bitmap bm = null;
        byte[] art = null;
        String path = null;
        Uri uri = null;
        long songid;
        long albumid;
        songid = mp3.getId();
        albumid = mp3.getAlbumId();
        if (albumid < 0 && songid < 0) {
            throw new IllegalArgumentException("Must specify an album or a song id");
        }
        try {
            if (albumid < 0) {
                uri = Uri.parse("content://media/external/audio/media/" + songid + "/albumart");
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);

                }
            } else {
                uri = ContentUris.withAppendedId(sArtworkUri, albumid);
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);
                }
            }
        } catch (FileNotFoundException ex) {

        }
        if (bm != null) {
            mCachedBit = bm;
        }
        return bm;
    }
    public static Uri getArtworkFromFile(Context context, long songid, long albumid) {
        Bitmap bm = null;
        byte[] art = null;
        String path = null;
        Uri uri = null;
        if (albumid < 0 && songid < 0) {
            throw new IllegalArgumentException("Must specify an album or a song id");
        }
        try {
            if (albumid < 0) {
                uri = Uri.parse("content://media/external/audio/media/" + songid + "/albumart");
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);

                }
            } else {
                uri = ContentUris.withAppendedId(sArtworkUri, albumid);
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);
                }
            }
        } catch (FileNotFoundException ex) {

        }
        if (bm != null) {
            mCachedBit = bm;
        }
        return uri;
    }

    private static final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
    private static final BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
    private static Bitmap mCachedBit = null;


    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     *
     * @param context
     * @param songid
     * @param albumid
     * @return
     */
    @TargetApi(19)
    public static String getImagePathWithSongId(Context context, long songid, long albumid) {
        Uri imageUri = getArtworkFromFile(context, songid, albumid);
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
