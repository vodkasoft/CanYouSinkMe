package com.vodkasoft.canyousinkme.gamelogic;

import com.vodkasoft.canyousinkme.game.DualMatrix;

public class Game {
    private DualMatrix Boards;
    private Player Player;
    private Player Opponent;
    private int TimeLeft;
    private int PlayerShipsLeft;
    private int OpponentShipsLeft;
    private int PlayerScore;
    private int OpponentScore;

    public Game(Player pPlayer, Player pOpponent){
        this.Boards = new DualMatrix();
    }

    public void sendMisile(int pXCoordinate, int pYCoordinate){

    }

    public void initTime(){

    }

    public void toGameData(){

    }
}
