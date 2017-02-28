package com.shaeffer.jacob.minor;

public class ScoreInfoObject
{
    public static final int IDLE = 0, LOADING = 1, FINISHED = 2;
    public static final int ERROR = 4, INTERNET = 5, WIFI = 6, NA = 7;
    public static final int _16 = 0, _18 = 1, _24 = 2, _26 = 3, _28 = 4, _30 = 5, _32 = 6, _64 = 7;

    private int internalState;
    private int scoresMessage;
    private int rankMessage;
    private boolean usingScoreMessage, usingRankMessage;
    private int fontSize;
    private String[][] scores;
    private String rank;

    public ScoreInfoObject()
    {
        reset();
    }

    public void reset()
    {
        internalState = IDLE;
        scoresMessage = 0;
        rankMessage = 0;
        usingScoreMessage = usingRankMessage = true;
        fontSize = _64;
        scores = null;
        rank = null;
    }

    public int getInternalState(){return internalState;}
    public int getScoresMessage(){return scoresMessage;}
    public int getRankMessage(){return rankMessage;}
    public int getFontSize(){return fontSize;}
    public boolean isUsingScoreMessage(){return usingScoreMessage;}
    public boolean isUsingRankMessage(){return usingRankMessage;}
    public String[][] getScores(){return scores;}
    public String getRank(){return rank;}

    public void setInternalState(int i){internalState = i;}
    public void setScoresMessage(int s){scoresMessage = s;}
    public void setRankMessage(int r){rankMessage = r;}
    public void setFontSize(int f){fontSize = f;}
    public void setUsingScoreMessage(boolean u){usingScoreMessage = u;}
    public void setUsingRankMessage(boolean u){usingRankMessage = u;}
    public void setScores(String[][] s){scores = s;}
    public void setRank(String r){rank = r;}

}
