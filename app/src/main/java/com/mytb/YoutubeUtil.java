package com.mytb;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.common.Scopes;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
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
    public static final String KEY = "AIzaSyALzlQojFizSoRWcsmACzJTws50N_VVKc0";
    public static final String[] SCOPES = {Scopes.PROFILE, YouTubeScopes.YOUTUBE};

    @NonNull
    private static YouTube initYoutube(Context context, GoogleAccountCredential credential) {
        YouTube youTube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                credential).setApplicationName(context.getString(R.string.app_name)).build();
        return youTube;
    }

    public static List<Video> search(Context context, String keywords) throws Exception {
        MyLog.v("---------------------");
        YouTube youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName(context.getString(R.string.app_name)).build();
        List<Video> items = new ArrayList<Video>();
        YouTube.Search.List query = youtube.search().list("id,snippet");
        query.setKey(KEY);
        query.setType("video");
        query.setFields("items(id/videoId,snippet/title,snippet/description,snippet/thumbnails/default/url)");
        query.setQ(keywords);
        SearchListResponse response = query.execute();
        List<SearchResult> results = response.getItems();
        for (SearchResult result : results) {
            Video item = new Video();
            items.add(item);
            MyLog.v(result.getId().getVideoId());
        }
        return items;
    }

    public static List<ChannelItem> searchChannels(Context context, GoogleAccountCredential credential, String channelId) throws Exception {
        MyLog.v("---------------------");
        YouTube youtube = initYoutube(context, credential);
        List<ChannelItem> items = new ArrayList<ChannelItem>();
        try {
            YouTube.Channels.List query = youtube.channels().list("id,snippet,brandingSettings,localizations");
            query.setKey(KEY);
            query.setId(channelId);
            ChannelListResponse response = query.execute();
            List<Channel> results = response.getItems();

            for (Channel result : results) {
                ChannelItem item = new ChannelItem();
                MyLog.v(result.getId());
            }
        } catch (IOException e) {

        }
        return items;
    }


    public static List<VideoList> searchVideoList(Context context, GoogleAccountCredential credential, String playlistId) throws Exception {
        MyLog.v("---------------------" + credential.getSelectedAccountName());
        List<VideoList> items = new ArrayList<VideoList>();
        YouTube youtube = initYoutube(context, credential);

        List<PlaylistItem> playlistItemList = new ArrayList<PlaylistItem>();
        // Retrieve the playlist of the channel's uploaded videos.
        YouTube.PlaylistItems.List playlistItemRequest =
                youtube.playlistItems().list("id,contentDetails,snippet");
        playlistItemRequest.setPlaylistId(playlistId);
        String nextToken = "";
        do {
            playlistItemRequest.setPageToken(nextToken);
            PlaylistItemListResponse playlistItemResult = playlistItemRequest.execute();
            playlistItemList.addAll(playlistItemResult.getItems());
            nextToken = playlistItemResult.getNextPageToken();
        } while (nextToken != null);

        MyLog.v(playlistItemList.size() + "");
        for (PlaylistItem playlistItem : playlistItemList) {
            VideoLists item = new VideoLists();
            MyLog.v(playlistItem.getId());
        }

        return items;
    }


    public static List<VideoLists> searchVideoLists(Context context, GoogleAccountCredential credential, String playlistId) throws Exception {
        MyLog.v("---------------------");
        List<VideoLists> items = new ArrayList<VideoLists>();
        YouTube youtube = initYoutube(context, credential);
        PlaylistListResponse playlistListResponse = youtube.playlists().
                list("snippet,localizations").setId(playlistId).execute();
        List<Playlist> playlistList = playlistListResponse.getItems();

        if (playlistList.isEmpty()) {
            System.out.println("Can't find a playlist with ID: " + playlistId);
            return items;
        }

        for (Playlist result : playlistList) {
            VideoLists item = new VideoLists();
            MyLog.v(result.getId());
        }

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