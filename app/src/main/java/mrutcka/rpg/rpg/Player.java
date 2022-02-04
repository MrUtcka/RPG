package mrutcka.rpg.rpg;

public class Player {

    MainActivity main = new MainActivity();

    int hp, arm, dmg;
    int maxHP;

    public Player(int hp, int arm, int dmg) {
        this.hp = hp;
        this.arm = arm;
        this.dmg = dmg;
        maxHP = hp;
    }

    public void attack(Enemy enemy) {
        enemy.hp -= dmg >= enemy.arm ? dmg - enemy.arm : enemy.arm - dmg;
        hp -= enemy.dmg >= arm ? enemy.dmg - arm : arm - enemy.dmg;
    }

    public void defence(Enemy enemy) {
        hp -= (enemy.dmg >= arm ? enemy.dmg - arm : arm - enemy.dmg) / 2;
        enemy.hp -= (dmg >= enemy.arm ? dmg - enemy.arm : enemy.arm - dmg) / 2;
    }
}
