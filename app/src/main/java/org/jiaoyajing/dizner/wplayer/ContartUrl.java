package org.jiaoyajing.dizner.wplayer;

/**
 * Created by Dizner on 2017/3/14.
 */

public interface ContartUrl {
    String BASE_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?";
    String SEARCH_URL = BASE_URL+"search/get/";
    String BAIDU_SEARCH_URL = BASE_URL+"from=webapp_music&method=baidu.ting.search.catalogSug&format=json&callback=&query=";
    String SONG_DETIL_URL = "http://ting.baidu.com/data/music/links?songIds=";
    //type = 1-新歌榜,2-热歌榜,11-摇滚榜,12-爵士,16-流行,21-欧美金曲榜,22-经典老歌榜,23-情歌对唱榜,24-影视金曲榜,25-网络歌曲榜
    String FENLEI_URL = BASE_URL+"format=json&callback&from=webapp_music&method=baidu.ting.billboard.billList&size=10&";
    String TUIJIAN_URL = BASE_URL +"format=json&callback&from=webapp_music&method=baidu.ting.song.getRecommandSongList&num=20&song_id=";
}
