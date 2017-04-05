package org.jiaoyajing.dizner.wplayer.util;

import android.content.Context;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.provider.MediaStore;

import org.jiaoyajing.dizner.wplayer.javabean.Mp3Info;

import java.util.ArrayList;

/**
 * @author Dizner
 *
 */
public class MyUtils {
/**
 */
	public static boolean isAVD(Context context){
    	SensorManager sensor = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor lightsensor = sensor.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(lightsensor==null){
        	return true;
        }else{
        	return false;
        }
    }
/**
 */
	private static boolean isNull = false;
	public static Mp3Info getMp3Info(Context context, long _id) {
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
				MediaStore.Audio.Media._ID + "=" + _id, null,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		Mp3Info mp3Info=null;
		if(cursor!=null){
			if(cursor.moveToNext()){
				mp3Info=new Mp3Info();
				long id=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
				String title=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
				String artist=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
				String album=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
				long albumId=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
				long duration=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
				long size=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
				String url=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
				int isMusic=cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
				if(isMusic!=0){
					mp3Info.setId(id);
					mp3Info.setTitle(title);
					mp3Info.setArtist(artist);
					mp3Info.setAlbum(album);
					mp3Info.setAlbumId(albumId);
					mp3Info.setDuration(duration);
					mp3Info.setSize(size);
					mp3Info.setUrl(url);
				}
			}
		}else{
			isNull=true;
			return new Mp3Info(0, "My CD", "My CD", "My CD",0, 0, 0, null, 0);
		}
		
		cursor.close();
		return mp3Info;
	}
	/**
	 * �пշ���
	 * @return
	 */
	public static boolean isNull(){
		return isNull;
	}
	/**
	 * ���ù�����
	 * 2.2
	 * ��ϵͳ���ݿ��в�ѯ��Ƶ�ļ�
	 * ����Ϊһ����Ƶ�ļ���List����
	 * ���пշ���
	 */
		public static ArrayList<Mp3Info> getMp3Info(Context context) {
			Cursor cursor = context.getContentResolver().query(
					MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
					MediaStore.Audio.Media.DURATION + ">=180000", null,
					MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
			ArrayList<Mp3Info> list=new ArrayList<Mp3Info>();
			if(cursor.getCount()>0){
				for(int i=0;i<cursor.getCount();i++){
					cursor.moveToNext();
					Mp3Info mp3Info=new Mp3Info();
					long id=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
					String title=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
					String artist=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
					String album=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
					long albumId=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
					long duration=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
					long size=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
					String url=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
					int isMusic=cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
					if(isMusic!=0){
						mp3Info.setId(id);
						mp3Info.setTitle(title);
						mp3Info.setArtist(artist);
						mp3Info.setAlbum(album);
						mp3Info.setAlbumId(albumId);
						mp3Info.setDuration(duration);
						mp3Info.setSize(size);
						mp3Info.setUrl(url);
					}
					list.add(mp3Info);
				}
			}else{
				ArrayList<Mp3Info> arrayList = new ArrayList<Mp3Info>();
				arrayList.add(new Mp3Info(0, "My CD", "My CD", "My CD",0, 0, 0, null, 0));
				isNull=true;
				return arrayList;
			}
			
			cursor.close();
			return list;
		}
		/**
		 * Long����ʱ��Ĵ����������������ڴ���
		 * @param time
		 * @return
		 */
		public static String formatTime(long time){
			String min=time/(1000*60)+"";
			String sec=time%(1000*60)+"";
			if(min.length()<2){
				min="0"+time/(1000*60)+"";
			}else{
				min=time/(1000*60)+"";
			}
			if(sec.length()==4){
				sec="0"+(time%(1000*60))+"";
			}else if(sec.length()==3){
				sec="00"+(time%(1000*60))+"";
			}else if(sec.length()==2){
				sec="000"+(time%(1000*60))+"";
			}else if(sec.length()==1){
				sec="0000"+(time%(1000*60))+"";
			}
			return min+":"+sec.trim().substring(0,2);
		}
}
