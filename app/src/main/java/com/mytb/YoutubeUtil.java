package com.mytb;

import android.content.Context;

import com.google.android.gms.common.Scopes;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.PlaylistListResponse;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YoutubeUtil {

    public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    public static final JsonFactory JSON_FACTORY = new JacksonFactory();
    public static final String KEY = "AIzaSyCrarQG6vxxvFUUAfjhzHbGw9-teMzwCgo";
    public static final String[] SCOPES = {Scopes.PROFILE, YouTubeScopes.YOUTUBE};

    /**
     * 根据关键字查询数据youtube视频列表
     *
     * @param context
     * @param keywords
     * @return
     * @throws Exception
     */

    public static List<VideoItem> search(Context context, String keywords) throws Exception {
        MyLog.v("---------------------");
        YouTube youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName(context.getString(R.string.app_name)).build();
        List<VideoItem> items = new ArrayList<VideoItem>();
        YouTube.Search.List query = youtube.search().list("id,snippet");
        query.setKey(KEY);
        query.setType("video");
        query.setFields("items(id/videoId,snippet/title,snippet/description,snippet/thumbnails/default/url)");
        query.setQ(keywords);
        SearchListResponse response = query.execute();
        List<SearchResult> results = response.getItems();
        for (SearchResult result : results) {
            VideoItem item = new VideoItem();
            items.add(item);
        }
        MyLog.v(items.size() + "");
        return items;
    }

    /**
     * 根据类别id查询频道列表
     *
     * @param context
     * @param categoryId
     * @return
     * @throws Exception
     */

    public static List<ChannelItem> searchChannels(Context context, String categoryId) throws Exception {
        MyLog.v("---------------------");
        YouTube youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName(context.getString(R.string.app_name)).build();
        List<ChannelItem> items = new ArrayList<ChannelItem>();
        try {
            YouTube.Channels.List query = youtube.channels().list("id,snippet,brandingSettings,localizations");
            query.setKey(KEY);
            query.setCategoryId(categoryId);
            ChannelListResponse response = query.execute();
            List<Channel> results = response.getItems();

            for (Channel result : results) {
                ChannelItem item = new ChannelItem();
                item.id = result.getId();
                items.add(item);
            }
            MyLog.v(items.size() + "");
        } catch (IOException e) {

        }
        return items;
    }


    /**
     * 根据视频集合id  查询 视频集合
     *
     * @param context
     * @param playlistId
     * @return
     * @throws Exception
     */
    public static List<VideoItem> searchVideoList(Context context, String playlistId) throws Exception {
        List<VideoItem> items = new ArrayList<VideoItem>();
        YouTube youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName(context.getString(R.string.app_name)).build();

        List<PlaylistItem> itemList = new ArrayList<PlaylistItem>();
        // Retrieve the playlist of the channel's uploaded videos.
        YouTube.PlaylistItems.List itemRequest =
                youtube.playlistItems().list("id,contentDetails,snippet");
        itemRequest.setKey(KEY);
        itemRequest.setPlaylistId(playlistId);
        String nextToken = "";
//        do {
        itemRequest.setPageToken(nextToken);
        PlaylistItemListResponse itemListResponse = itemRequest.execute();
        itemList.addAll(itemListResponse.getItems());
//            nextToken = itemListResponse.getNextPageToken();
//        } while (nextToken != null);


        for (PlaylistItem playlistItem : itemList) {
            VideoItem item = new VideoItem();
            item.id = playlistItem.getId();
            items.add(item);
        }
        MyLog.v(items.size() + "");
        return items;
    }

    /**
     * 根据视频集合列表id  查询 视频集合列表
     *
     * @param context
     * @param channelId
     * @return
     * @throws Exception
     */

    public static List<VideoLists> searchVideoLists(Context context, String channelId) throws Exception {
        MyLog.v("---------------------");
        List<VideoLists> items = new ArrayList<VideoLists>();
        YouTube youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName(context.getString(R.string.app_name)).build();

        YouTube.Playlists.List mPlaylistsList = youtube.playlists().
                list("id,snippet,localizations");
        //设置参数
        mPlaylistsList.setKey(KEY);
        mPlaylistsList.setChannelId(channelId);

        //执行得到数据
        PlaylistListResponse playlistListResponse = mPlaylistsList.execute();
        List<Playlist> playlists = playlistListResponse.getItems();

        //判断数据
        if (playlists.isEmpty()) {
            System.out.println("Can't find a playlist with channelId: " + channelId);
            return items;
        }
        //转换数据
        for (Playlist result : playlists) {
            VideoLists item = new VideoLists();
            item.id = result.getId();
            items.add(item);
        }
        MyLog.v(items.size() + "");
        return items;
    }


//    YouTube.Channels.List channelRequest = youtube.channels().list("contentDetails");
//    channelRequest.setMine(true);
//    channelRequest.setFields("items/contentDetails,nextPageToken,pageInfo");
//    ChannelListResponse channelResult = channelRequest.execute();
//
//    List<Channel> channelsList = channelResult.getItems();
//
//    if (channelsList != null) {
//        // The user's default channel is the first item in the list.
//        // Extract the playlist ID for the channel's videos from the
//        // API response.
//        String uploadPlaylistId =
//                channelsList.get(0).getContentDetails().getRelatedPlaylists().getUploads();
//
//        // Define a list to store items in the list of uploaded videos.
//        List<PlaylistItem> playlistItemList = new ArrayList<PlaylistItem>();
//
//        // Retrieve the playlist of the channel's uploaded videos.
//        YouTube.PlaylistItems.List playlistItemRequest =
//                youtube.playlistItems().list("id,contentDetails,snippet");
//        playlistItemRequest.setPlaylistId(uploadPlaylistId);
//
//        // Only retrieve data used in this application, thereby making
//        // the application more efficient. See:
//        // https://developers.google.com/youtube/v3/getting-started#partial
//        playlistItemRequest.setFields(
//                "items(contentDetails/videoId,snippet/title,snippet/publishedAt),nextPageToken,pageInfo");
//
//        String nextToken = "";
//
//        // Call the API one or more times to retrieve all items in the
//        // list. As long as the API response returns a nextPageToken,
//        // there are still more items to retrieve.
//        do {
//            playlistItemRequest.setPageToken(nextToken);
//            PlaylistItemListResponse playlistItemResult = playlistItemRequest.execute();
//
//            playlistItemList.addAll(playlistItemResult.getItems());
//
//            nextToken = playlistItemResult.getNextPageToken();
//        } while (nextToken != null);
//
//        // Prints information about the results.
//        prettyPrint(playlistItemList.size(), playlistItemList.iterator());
//    }


}