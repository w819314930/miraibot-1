package top.kegurou;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dreamlu.mica.http.HttpRequest;
import net.dreamlu.mica.http.LogLevel;
import top.kegurou.bean.LoliconImageBean;
import top.kegurou.features.Menu;
import top.kegurou.features.Setu;
import top.kegurou.features.Wangyiyun;
import top.kegurou.util.HttpUtil;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void testslf4j() {
        String msg = "点歌牵丝戏";
        String name = msg.substring(2, msg.length());
        System.out.println(name);
        System.out.println(msg.substring(0, 2).equals("点歌"));
    }

    @Test
    public void testMune() {
        System.out.print(Menu.getMenu());
    }

    @Test
    public void testHttpUtilDoGet() {
        StringBuilder sb = new StringBuilder();
        InputStream is = null;
        BufferedReader br = null;
        PrintWriter out = null;
        try {
            // 接口地址
            String apiUrl = "https://api.lolicon.app/setu/?apikey=&size1200=true&r18=0";
            is = HttpUtil.doGet(apiUrl);
            System.out.println(is.toString());
            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;
            // 缓冲逐行读取
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            // System.out.println(sb.toString());
        } catch (Exception e) {
        } finally {
            // 关闭流
            try {
                if (is != null) {
                    is.close();
                }
                if (br != null) {
                    br.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception ignored) {
            }
        }
        System.out.println("sb:");
        System.out.println(sb.toString());
    }

    @Test
    public void testHttpUtilDoPost() {
        StringBuilder sb = new StringBuilder();
        InputStream is = null;
        BufferedReader br = null;
        PrintWriter out = null;
        try {
            // 接口地址
            String url = "http://tianqiapi.com/api?version=v6&appid=99836373&appsecret=NyAv8EAW";
            // String apiUrl =
            is = HttpUtil.doPost(url);
            System.out.println(is.toString());
            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;
            // 缓冲逐行读取
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            // System.out.println(sb.toString());
        } catch (Exception e) {
        } finally {
            // 关闭流
            try {
                if (is != null) {
                    is.close();
                }
                if (br != null) {
                    br.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception ignored) {
            }
        }
        System.out.println("sb:");
        System.out.println(sb.toString());
    }

    @Test
    public void testMicoHttp() {
        String LOLICONAPIURL = "https://api.lolicon.app/setu/";
        String LOLICONAPIKEY = "";
        String r18 = "1";
        String s = null;
        s = HttpRequest.get(LOLICONAPIURL).query("apikey", LOLICONAPIKEY).query("r18", r18).execute().asString();
        System.out.println("String is " + s);
        JSONObject jsonObject = JSON.parseObject(s);
        JSONObject jsonObjectImage = jsonObject.getJSONArray("data").getJSONObject(0);
        LoliconImageBean lBean = JSON.parseObject(jsonObjectImage.toJSONString(), LoliconImageBean.class);
        System.out.println(lBean.getUrl());
        System.out.println(lBean.getUid());
        for (int j = 0; j < lBean.getTags().size(); j++) {
            System.out.println(lBean.getTags().get(j));
        }
    }

    @Test
    public void testNeMusicComment() {
        String CLOUDMUSICAPI = "http://music.163.com/api/search/pc";
        String JUMPURL = "http://music.163.com/song/";
        String MUSICURL = "http://music.163.com/song/media/outer/url?id=";
        String JSONCONTENT = "{\"app\": \"com.tencent.structmsg\", 	\"desc\": \"音乐\", 	\"meta\": { 		\"music\": { 			\"app_type\": 1, 			\"desc\": \"\", 			\"jumpUrl\": \"\", 			\"musicUrl\": \"\", 			\"preview\": \"\", 			\"sourceMsgId\": \"0\", 			\"tag\": \"网易云音乐\", 			\"title\": \"\" 		} 	}, 	\"prompt\": \"\", 	\"ver\": \"0.0.0.1\", 	\"view\": \"music\" }";
        String songName = "牵丝戏";
        String result = HttpRequest.get(CLOUDMUSICAPI).query("s", songName).query("type", 1).execute().asString();
        JSONArray songsJsonArray = JSON.parseObject(result).getJSONObject("result").getJSONArray("songs");
        JSONObject songJsonObject = songsJsonArray.getJSONObject(0);
        JSONObject artisJsonObject = songJsonObject.getJSONArray("artists").getJSONObject(0);
        JSONObject albumJsonObject = songJsonObject.getJSONObject("album");

        String name = songJsonObject.getString("name");
        String id = songJsonObject.getLong("id").toString();
        String artisName = artisJsonObject.getString("name");
        String albumPicUrl = albumJsonObject.getString("picUrl");
        String prompt = "[分享]:" + name;
        System.out.println("测试！！！");
        System.out.println("歌曲名：" + name + "\n歌曲ID:" + id + "\n歌手:" + artisName + "\n封面链接:" + albumPicUrl);

        JSONObject jsonContent = JSON.parseObject(JSONCONTENT);
        JSONObject musicJsonObject = jsonContent.getJSONObject("meta").getJSONObject("music");
        musicJsonObject.put("desc", artisName);
        musicJsonObject.put("jumpUrl", JUMPURL + id);
        musicJsonObject.put("musicUrl", MUSICURL + id);
        musicJsonObject.put("preview", albumPicUrl);
        musicJsonObject.put("title", name);
        jsonContent.put("prompt", prompt);
        System.out.println("---测试---");
        System.out.println(jsonContent.toString());
    }

}
