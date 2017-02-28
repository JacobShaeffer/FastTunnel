package com.shaeffer.jacob.minor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class ScoreboardAPI {
    private Socket socket;
    private GameInfoObject gio;
    private ScoreInfoObject sio;

    private boolean loadingScores, loadingRank;
    private int loadingState;
    private float timeSinceLast;
    private float timeBetween;
    private float totalTime;
    IO.Options options;

    public ScoreboardAPI(GameInfoObject gio, ScoreInfoObject sio)
    {
        this.gio = gio;
        this.sio = sio;
        reset();

        options = new IO.Options();
        options.forceNew = true;
    }

    public void reset()
    {
        socket = null;

        loadingScores = loadingRank = true;
        loadingState = 0;
        timeBetween = .5f;
        timeSinceLast = 0f;
        totalTime = 0f;
        sio.reset();
    }

    public void update(float delta)
    {
        if(sio.getInternalState() == ScoreInfoObject.LOADING)
        {
            if(loadingScores || loadingRank)
            {
                if(timeSinceLast >= timeBetween)
                {
                    updateLoading();
                    timeSinceLast = 0;
                }
                if(totalTime >= 15)
                {
                    loadingScores = loadingRank = false;
                    sio.setRankMessage(ScoreInfoObject.NA);
                    sio.setScoresMessage(ScoreInfoObject.ERROR);
                    sio.setFontSize(ScoreInfoObject._24);
                    sio.setInternalState(ScoreInfoObject.FINISHED);
                }
                timeSinceLast += delta;
                totalTime += delta;
            }
        }
    }

    private void updateLoading()
    {
        if(loadingState == 3)
            loadingState = 0;
        else
            loadingState++;

        if(loadingRank) sio.setRankMessage(loadingState);
        if(loadingScores) sio.setScoresMessage(loadingState);
    }

    public void getGlobalScores()
    {

        String online = gio.isOnline();
        if(!online.equals("online"))
        {
            removeSocket();
            loadingScores = loadingRank = false;
            sio.setInternalState(ScoreInfoObject.FINISHED);
            if(online.equals("noWifi"))
                sio.setScoresMessage(ScoreInfoObject.WIFI);
            else
                sio.setScoresMessage(ScoreInfoObject.INTERNET);
            sio.setRankMessage(ScoreInfoObject.NA);
            sio.setFontSize(ScoreInfoObject._24);
            return;
        }
        connectSocket();
        JSONObject data = new JSONObject();
        try
        {
            data.put("ID", gio.get_id());
        }
        catch (JSONException e)
        {
            //Gdx.app.log("JSONException", e.getMessage());
        }
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                //Gdx.app.log("dev output", "Connected to server");
            }
        })
                .on("returnTOP", onTopReturned)
                .on("returnRank", onReturnRank)
                .on("error", onError)

                .emit("topTen")
                .emit("myRank", data);
    }

    private void connectSocket()
    {
        try
        {
            if (socket == null)
                socket = IO.socket(gio.getHttpBase() + gio.getPort());
            socket.connect();
        }
        catch (Exception e)
        {
            //Gdx.app.log("dev output", e.getMessage());
        }
    }

    private void removeSocket()
    {
        if(socket == null) return;
        socket.disconnect();
        socket.off("returnTOP", onTopReturned);
        socket.off("returnRank", onReturnRank);
        socket.off("error", onError);
    }

    private Emitter.Listener onError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            //Gdx.app.log("dev output", "error callback");
            removeSocket();
        }
    };

    private Emitter.Listener onTopReturned = new Emitter.Listener()
    {
        @Override
        public void call(Object... args) {
            //Gdx.app.log("dev output", "returnTOP callback");
            try
            {
                JSONArray data = (JSONArray)args[0];
                loadingScores = false;
                sio.setFontSize(ScoreInfoObject._32);
                sio.setUsingScoreMessage(false);
                String[][] temp = new String[data.length()][3];
                for(int i=0; i<data.length(); i++)
                {
                    //Gdx.app.log("dev output", data.getJSONObject(i).getString("username"));
                    temp[i][0] = data.getJSONObject(i).getString("username");
                    temp[i][1] = data.getJSONObject(i).getString("score");
                    temp[i][2] = data.getJSONObject(i).getString("ship");
                }
                sio.setScores(temp);
            }
            catch (JSONException e)
            {
                //Gdx.app.log("dev output", "JSON exception: " + e);
            }
            if(!loadingRank)
            {
                removeSocket();
                sio.setInternalState(ScoreInfoObject.FINISHED);
            }
        }
    };

    private Emitter.Listener onReturnRank = new Emitter.Listener() {
        @Override
        public void call(Object... args) {//TODO: if rank 1st check against highscores
            //Gdx.app.log("dev output", "returnRank callback");
            try
            {
                JSONArray data = (JSONArray)args[0];
                loadingRank = false;
                String checkUID = data.getJSONObject(0).getString("uniqueID");
                if(checkUID.equals("null"))
                    sio.setRankMessage(ScoreInfoObject.NA);
                else
                {
                    sio.setUsingRankMessage(false);
                    sio.setRank(data.getJSONObject(0).getString("rank"));
                }
            }
            catch (JSONException e)
            {
                //Gdx.app.log("dev output", "JSON exception: " + e);
            }
            if(!loadingScores)
            {
                removeSocket();
                sio.setInternalState(ScoreInfoObject.FINISHED);
            }
        }
    };
}
