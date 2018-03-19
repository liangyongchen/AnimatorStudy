package com.hema.animatorstudy.game;

import android.app.Activity;
import android.os.Bundle;

import com.hema.animatorstudy.R;
import com.hema.animatorstudy.game.plane.GameView;

/**
 * 防微信打飞机游戏
 * Created by asus on 2017/12/5.
 */

public class GamePlaneActivity extends Activity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameView = (GameView) findViewById(R.id.gameView);
        //0:combatAircraft
        //1:explosion
        //2:yellowBullet
        //3:blueBullet
        //4:smallEnemyPlane
        //5:middleEnemyPlane
        //6:bigEnemyPlane
        //7:bombAward
        //8:bulletAward
        //9:pause1
        //10:pause2
        //11:bomb
        int[] bitmapIds = {
                R.drawable.plane_plane,
                R.drawable.plane_explosion,
                R.drawable.plane_yellow_bullet,
                R.drawable.plane_blue_bullet,
                R.drawable.plane_small,
                R.drawable.plane_middle,
                R.drawable.plane_big,
                R.drawable.plane_bomb_award,
                R.drawable.plane_bullet_award,
                R.drawable.plane_pause1,
                R.drawable.plane_pause2,
                R.drawable.plane_bomb
        };
        gameView.start(bitmapIds);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (gameView != null) {
            gameView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gameView != null) {
            gameView.destroy();
        }
        gameView = null;
    }
}