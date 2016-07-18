package util;


import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class YoutubeUtil{
    public static String[] getVideoInfo(String videoId) throws IOException{
        
        YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), request -> {}).setApplicationName("video-test").build();

        YouTube.Videos.List videoRequest = youtube.videos().list("snippet,statistics,contentDetails");
        videoRequest.setId(videoId);
        videoRequest.setKey("AIzaSyAFrLJDx_UBK_XOyRmJuFElr90iFayAuNo");
        VideoListResponse listResponse = videoRequest.execute();
        List<Video> videoList = listResponse.getItems();

        Video video = videoList.iterator().next();

        String[] info = new String[4];
        info[0] = video.getSnippet().getTitle();//Video title
        info[1] = video.getSnippet().getChannelTitle();//Channel title
        info[2] = video.getContentDetails().getDuration();//Duration
        if(video.getContentDetails().getRegionRestriction() != null){
            List<String> blockedRegions = video.getContentDetails().getRegionRestriction().getBlocked();
            Collections.sort(blockedRegions);

            info[3] = String.join(", ", blockedRegions);//Blocked regions
        }
        else info[3] = "";

        return info;
    }

    public static String formatTime(String time){
        StringBuilder builder = new StringBuilder();

        if(time.contains("W")){
            int weeks = Integer.parseInt(time.replaceAll(".*[\\D]([\\d]+)W.*", "$1"));
            builder.append(weeks).append(weeks == 1 ? " week, " : " weeks, ");
            //return "Too damn long"
        }

        if(time.contains("D")){
            int days = Integer.parseInt(time.replaceAll(".*[\\D]([\\d]+)D.*", "$1"));
            builder.append(days).append(days == 1 ? " day, " : " days, ");
        }

        if(time.contains("H")){
            String hours = String.format("%02d", Integer.parseInt(time.replaceAll(".*[\\D]([\\d]+)H.*", "$1")));
            builder.append(hours).append(":");
        }

        if(time.contains("M")){
            String minutes = String.format("%02d", Integer.parseInt(time.replaceAll(".*[\\D]([\\d]+)M.*", "$1")));
            builder.append(minutes).append(":");
        }
        else builder.append("00:");//Minutes always show

        if(time.contains("S")){
            String seconds = String.format("%02d", Integer.parseInt(time.replaceAll(".*[\\D]([\\d]+)S.*", "$1")));
            builder.append(seconds);
        }
        else builder.append("00");//Seconds always show

        return builder.toString();
    }
}