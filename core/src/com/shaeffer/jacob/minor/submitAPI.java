package com.shaeffer.jacob.minor;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class submitAPI {

    private Socket socket;
    private GameInfoObject gio;

    public submitAPI(GameInfoObject gio)
    {
        this.gio = gio;
        socket = null;
    }

    public void newHighScore(String username, int score, int ship)
    {
        if(!gio.isOnline().equals("online"))
        {
            removeSocket();
            return;
        }
        connectSocket();
        JSONObject data = new JSONObject();
        try
        {
            data.put("ID", gio.get_id());//"Development_Testing_ID_0");//
            data.put("USER", username);
            data.put("SCORE", score);
            data.put("SHIP", ship);
            //Gdx.app.log("dev output", gio.get_id() + " " + username + " " + score + " " + ship);
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
                .on("success", onSuccess)
                .on("error", onError)
                .emit("newHighscore", data);
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

    private Emitter.Listener onSuccess = new Emitter.Listener()
    {
        @Override
        public void call(Object... args) {
            //Gdx.app.log("dev output", "success callback");
            removeSocket();
        }
    };

    private Emitter.Listener onError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            //Gdx.app.log("dev output", "error callback");
            removeSocket();
        }
    };

    private void removeSocket()
    {
        if(socket == null) return;
        socket.disconnect();
        socket.off("success", onSuccess);
        socket.off("error", onError);
    }
}
