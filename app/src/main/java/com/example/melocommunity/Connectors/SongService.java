package com.example.melocommunity.Connectors;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.melocommunity.models.Song;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SongService {

    public static final String TAG = "SongService";

    private final ArrayList<Song> songs = new ArrayList<>();
    private final SharedPreferences sharedPreferences;
    private final RequestQueue queue;
    Song song;

    public SongService(Context context) {
        sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }
    public Song getSong() {
        return song;
    }


    public Song getCurrentlyPlaying(final UserService.VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/me/player/currently-playing";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    Gson gson = new Gson();
                    JSONObject object = response.optJSONObject("item");
                    String artist="";
                    String imageUrl="";

                    try {
                        JSONArray arrayArtists = object.getJSONArray("artists");
                        JSONArray arrayImages = object.getJSONArray("images");
                        Log.d("Images", object.toString());
                        artist = arrayArtists.getJSONObject(0).getString("name");
                        //imageUrl = arrayImages.getJSONObject(0).getString("url");
                        song.setArtist(artist);
                        //song.setImageUrl(imageUrl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    song = gson.fromJson(object.toString(), Song.class);


                    callBack.onSuccess();
                }, error -> {
                    // TODO: Handle error

                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
        return song;
    }

    public ArrayList<Song> getRecentlyPlayedTracks(final UserService.VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/me/player/recently-played";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    Gson gson = new Gson();
                    JSONArray jsonArray = response.optJSONArray("items");
                    for (int n = 0; n < jsonArray.length(); n++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(n);
                            object = object.optJSONObject("track");
                            JSONArray arrayArtists = object.getJSONArray("artists");
                            JSONObject album = object.getJSONObject("album");
                            JSONArray arrayImages = album.getJSONArray("images");
                            Song song = gson.fromJson(object.toString(), Song.class);
                            song.setArtist(arrayArtists.getJSONObject(0).getString("name"));
                            song.setImageUrl(arrayImages.getJSONObject(0).getString("url"));
                            songs.add(song);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    callBack.onSuccess();
                }, error -> {
                    // TODO: Handle error

                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
        return songs;
    }

    public void addSongToLibrary(Song song) {
        JSONObject payload = preparePutPayload(song);
        JsonObjectRequest jsonObjectRequest = prepareSongLibraryRequest(payload);
        queue.add(jsonObjectRequest);
    }

    private JsonObjectRequest prepareSongLibraryRequest(JSONObject payload) {
        return new JsonObjectRequest(Request.Method.PUT, "https://api.spotify.com/v1/me/tracks", payload, response -> {
        }, error -> {
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
    }

    private JSONObject preparePutPayload(Song song) {
        JSONArray idarray = new JSONArray();
        idarray.put(song.getId());
        JSONObject ids = new JSONObject();
        try {
            ids.put("ids", idarray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ids;
    }



    public ArrayList<Song> getTopTracks(final UserService.VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/me/top/tracks?limit=50";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    Gson gson = new Gson();
                    JSONArray jsonArray = response.optJSONArray("items");
                    Log.i(TAG, "arraylist: " + jsonArray);
                    for (int n = 0; n < jsonArray.length(); n++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(n);
                            object = object.optJSONObject("album");
                            JSONArray arrayArtists = object.getJSONArray("artists");

                            JSONArray arrayImages = object.getJSONArray("images");
                            Song song = gson.fromJson(object.toString(), Song.class);
                            song.setId(jsonArray.getJSONObject(n).getString("id"));
                            song.setName(jsonArray.getJSONObject(n).getString("name"));
                            song.setRelease(jsonArray.getJSONObject(n).getInt("duration_ms"));
                            song.setArtist(arrayArtists.getJSONObject(0).getString("name"));
                            song.setImageUrl(arrayImages.getJSONObject(0).getString("url"));
                            songs.add(song);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    callBack.onSuccess();
                }, error -> {
                    // TODO: Handle error

                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
        return songs;
    }



    public ArrayList<Song> getSearchTracks(final UserService.VolleyCallBack callBack, String songName) {
        String endpoint = "https://api.spotify.com/v1/search?q=" + songName + "&type=track&limit=50";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    Gson gson = new Gson();
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = response.getJSONObject("tracks").optJSONArray("items");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.i(TAG, "arraylistSearch: " + jsonArray);
                    for (int n = 0; n < jsonArray.length(); n++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(n);
                            object = object.optJSONObject("album");
                            JSONArray arrayArtists = object.getJSONArray("artists");

                            JSONArray arrayImages = object.getJSONArray("images");
                            Song song = gson.fromJson(object.toString(), Song.class);
                            song.setId(jsonArray.getJSONObject(n).getString("id"));
                            song.setName(jsonArray.getJSONObject(n).getString("name"));
                            song.setRelease(jsonArray.getJSONObject(n).getInt("duration_ms"));
                            song.setArtist(arrayArtists.getJSONObject(0).getString("name"));
                            song.setImageUrl(arrayImages.getJSONObject(0).getString("url"));
                            songs.add(song);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    callBack.onSuccess();
                }, error -> {
                    // TODO: Handle error

                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
        return songs;
    }

}
