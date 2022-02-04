package mrutcka.rpg.rpg;

public class Enemy {

    String name;
    String[] drop = new String[3];
    int hp, arm, dmg;
    int maxHP;

    public Enemy(String name, int hp, int arm, int dmg, String[] drop) {
        this.name = name;
        this.hp = hp;
        this.arm = arm;
        this.dmg = dmg;
        this.drop = drop;
        maxHP = hp;
    }
}
