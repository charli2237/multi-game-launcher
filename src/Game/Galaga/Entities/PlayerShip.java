package Game.Galaga.Entities;

import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by AlexVR on 1/25/2020
 */
public class PlayerShip extends BaseEntity{

    private int health = 3,attackCooldown = 30,speed =6,destroyedCoolDown = 60*7;
    private boolean attacking = false, destroyed = false;
    private Animation deathAnimation;
    //private ArrayList<Integer> beePos= new ArrayList<Integer>(20);

     public PlayerShip(int x, int y, int width, int height, BufferedImage sprite, Handler handler) {
        super(x, y, width, height, sprite, handler);

        deathAnimation = new Animation(256,Images.galagaPlayerDeath);

    }

    @Override
    public void tick() {
        super.tick();
        if (destroyed){
            if (destroyedCoolDown<=0){
                destroyedCoolDown=60*7;
                destroyed=false;
                deathAnimation.reset();
                bounds.x=x;
            }else{
                deathAnimation.tick();
                destroyedCoolDown--;
            }
        }else {
        	/**
        	int maxnum = ThreadLocalRandom.current().nextInt(30,101);
        	int minnum = ThreadLocalRandom.current().nextInt(2,11);
        	int bee = ThreadLocalRandom.current().nextInt(0,maxnum);
        	int row = ThreadLocalRandom.current().nextInt(3,15);
        	int col = ThreadLocalRandom.current().nextInt(0,10);
        	ArrayList <Integer> point = new ArrayList<Integer>();
        	point.add(row);
        	point.add(col);
        	boolean check = beePos.contains(point);
        	if (!check) {
        		beePos.addAll(point);
        		handler.getGalagaState().entityManager.entities.add(new EnemyBee(this.x + (width/2), this.y-3,width/2, col, handler, col, col));
        	}*/
            if (attacking) {
                if (attackCooldown <= 0) {
                    attacking = false;
                } else {
                    attackCooldown--;
                }
            }
            if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER) && !attacking) {
                handler.getMusicHandler().playEffect("laser.wav");
                attackCooldown = 30;
                attacking = true;
                handler.getGalagaState().entityManager.entities.add(new PlayerLaser(this.x + (width / 2), this.y - 3, width / 5, height / 2, Images.galagaPlayerLaser, handler, handler.getGalagaState().entityManager));

            }
            if (handler.getKeyManager().left && x > arena.x +2) {//add bounds
                x -= (speed);
            }
            if (handler.getKeyManager().right && x < arena.x + arena.width -width) {
                x += (speed);
            }
            if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_N) && handler.DEBUG) {
            	health--;
            	destroyed=true;
            	handler.getMusicHandler().playEffect("explosion.wav");
            	
            	bounds.x = -10;
        }
            bounds.x = x;
        
        }
        if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_L) && handler.DEBUG && health < 3) {
    		health ++;}
        }
    @Override
    public void render(Graphics g) {
         if (destroyed){
             if (deathAnimation.end){
                 g.drawString("READY",handler.getWidth()/2-handler.getWidth()/12,handler.getHeight()/2);
             }else {
                 g.drawImage(deathAnimation.getCurrentFrame(), x, y, width, height, null);
             }
         }else {
             super.render(g);
         }
    }

    @Override
    public void damage(BaseEntity damageSource) {
        if (damageSource instanceof PlayerLaser){
            return;
        }
        health--;
        destroyed = true;
        handler.getMusicHandler().playEffect("explosion.wav");
    }
    
    
    public int getHealth() {
    	if (health < 1)
    		handler.getScoreManager().removeGalagaCurrentScore(handler.getScoreManager().getGalagaCurrentScore()+100);
        	return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

}
