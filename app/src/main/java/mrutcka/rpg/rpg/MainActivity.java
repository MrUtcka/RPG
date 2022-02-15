package mrutcka.rpg.rpg;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressBar hpBar;
    ProgressBar hpEnemyBar;
    Random random;
    Intent intent;
    Enemy enemy;
    Player player;
    Story story;
    TextView statusStory;
    TextView status;
    TextView enemyStatus;

    Button[] buttons = new Button[5];
    Enemy[] enemies = new Enemy[4];
    String[] inventory = new String[3];
    String[] drop = new String[3];

    boolean isBattle = false;
    boolean isInventory = false;

    int currStory = 0;
    int countWin = 0;
    int ultimate = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hpBar = (ProgressBar) findViewById(R.id.hp);
        hpEnemyBar = (ProgressBar) findViewById(R.id.hpEnemy);

        status = (TextView) findViewById(R.id.status);
        enemyStatus = (TextView) findViewById(R.id.enemy);
        statusStory = (TextView) findViewById(R.id.Story);

        buttons[0] = (Button) findViewById(R.id.battle);
        buttons[1] = (Button) findViewById(R.id.inventory);
        buttons[2] = (Button) findViewById(R.id.skip);
        buttons[3] = (Button) findViewById(R.id.exit);
        buttons[4] = (Button) findViewById(R.id.ultimate);

        buttons[0].setOnClickListener(this);
        buttons[1].setOnClickListener(this);
        buttons[2].setOnClickListener(this);
        buttons[3].setOnClickListener(this);
        buttons[4].setOnClickListener(this);

        player = new Player(100, 0, 25);
        story = new Story();
        random = new Random();

        for(int i = 0; i < inventory.length; i++) { inventory[i] = "null"; }
        setDrop();
        setEnemies();
        story.setStory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //BeforeGame

    private void setDrop() {
        drop[0] = "LittleHealth Potion";
        drop[1] = "Health Potion";
        drop[2] = "MegaHealth Potion";
    }

    private void setEnemies() {
        enemies[0] = new Enemy("Knight", 30, 15, 10, drop);
        enemies[1] = new Enemy("Archer", 25, 5, 15, drop);
        enemies[2] = new Enemy("Wizard", 15, 0, 25, drop);
        enemies[3] = new Enemy("GunMan", 25, 5, 25, drop);
        enemy = new Enemy("", 1, 0, 0, drop);
        clear(2);
    }

    private void setEnemy(int i) {
        enemy = enemies[i];
        hpEnemyBar.setMax(enemy.hp);
        countWin++;
        ultimate--;
        toast(enemy.name);
        update(1);
    }

    //GUI

    private void update(int i) {
        for(int j = 0; j < inventory.length; j++) { buttons[j].setVisibility(View.VISIBLE); }
        status.setText("HP: " + (player.hp < 0 ? 0 : player.hp) + "/" + player.maxHP + "\n" + "ARM: " + player.arm + "\n" + "DMG: " + player.dmg);
        enemyStatus.setText("ENEMY HP: " + (enemy.hp < 0 ? 0 : enemy.hp) + "/" + enemy.maxHP + "\n" + "ENEMY ARM: " + enemy.arm + "\n" + "ENEMY DMG: " + enemy.dmg);

        hpBar.setProgress(player.hp);
        hpEnemyBar.setProgress(enemy.hp);

        if(!isBattle && !isInventory) { buttons[0].setText("Next!"); buttons[1].setText("Inventory!"); buttons[2].setText("Skip!"); }
        if(isInventory && !isBattle) { for(int j = 0; j < inventory.length; j++) { guiInventory(j); }}
        else if(isBattle && !isInventory) { buttons[0].setText("Attack!"); buttons[1].setText("Defence!"); buttons[2].setText("Skip!"); }

        if(i == 0) { clear(2); }
    }

    //Battle

    private void battle(int i) {
        setEnemy(i);
        update(0);

        isBattle = true;
    }

    private void die() {
        if(player.hp <= 0) {
            isBattle = false;

            intent = new Intent(this, Menu.class);
            intent.putExtra("wins", countWin);
            startActivity(intent);
        }
        if(enemy.hp <= 0) { isBattle = false; add(); }
    }

    private void next() {
        statusStory.setText(story.story[currStory][0]);

        buttons[0].setText(story.story[currStory][0]);
        buttons[1].setText(story.story[currStory][0]);
        buttons[2].setText(story.story[currStory][0]);
    }

    //Save system



    //Inventory system

    private void use(int i) {
        if(inventory[i].equals("LittleHealth Potion")) { player.hp += 15; inventory[i] = "null"; buttons[i].setVisibility(View.INVISIBLE); }
        else if(inventory[i].equals("Health Potion")) { player.hp += 25; inventory[i] = "null"; buttons[i].setVisibility(View.INVISIBLE); }
        else if(inventory[i].equals("MegaHealth Potion")) { player.hp += 45; inventory[i] = "null"; buttons[i].setVisibility(View.INVISIBLE); }
    }

    private void add() {
        for(int i = 0; i < inventory.length; i++) {
            if(inventory[i].equals("null")) { inventory[i] = drop[random.nextInt(inventory.length)]; break; }
        }
    }

    private void guiInventory(int i) {
        if(inventory[i].equals("null")) { buttons[i].setVisibility(View.INVISIBLE); }
        else { buttons[i].setText(inventory[i]); }
    }

    //Buttons

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.battle:
                if(!isBattle && !isInventory) { next(); }
                else if(!isBattle && isInventory) { use(0); }
                else {
                    player.attack(enemy);
                    die();
                }

                break;
            case R.id.inventory:
                if(!isBattle && !isInventory) { isInventory = true; }
                else if(!isBattle && isInventory) { use(1); }
                else {
                    player.defence(enemy);
                    die();
                }

                break;
            case R.id.skip:
                if(!isBattle && isInventory) { use(2); }
                if(isBattle && !isInventory) { ultimate--; player.hp -= enemy.dmg >= player.arm ? enemy.dmg - player.arm : player.arm - enemy.dmg; }

                break;
            case R.id.ultimate:
                if(isBattle && !isInventory && ultimate <= 0) { player.hp += 50; enemy.hp = 0; ultimate = 3; isBattle = false; }
                else if(ultimate > 0) { toast("ULTIMATE NO READY!"); }

                break;
            case R.id.exit:
                if(isInventory) { isInventory = false; }
                else { System.exit(0); }

                break;
        }
        update(isBattle == true ? 1 : 0);
    }

    //System

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void clear(int i) {
        if(i == 0) { enemyStatus.setText("Вы"); }
        else if(i == 1) { status.setText("Враг"); }
        else { enemyStatus.setText("Враг"); status.setText("Вы"); }
    }
}