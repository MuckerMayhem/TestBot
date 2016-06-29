package bot.listeners.test;


import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.IOException;
import java.util.List;

/**
 * Created by Owner on 2016-06-29.
 */
public class GetLinkStuff
{
    static public String getVideoLength(String videoId) throws IOException
    {
        YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(),
                new HttpRequestInitializer()
                {
                    public void initialize(HttpRequest request) throws IOException
                    {
                    }
                }).setApplicationName("video-test").build();
        YouTube.Videos.List videoRequest = youtube.videos().list("snippet,statistics,contentDetails");
        videoRequest.setId(videoId);
        videoRequest.setKey("AIzaSyAFrLJDx_UBK_XOyRmJuFElr90iFayAuNo");
        VideoListResponse listResponse = videoRequest.execute();
        List<Video> videoList = listResponse.getItems();

        Video targetVideo = videoList.iterator().next();

        return targetVideo.getContentDetails().getDuration();
    }

}