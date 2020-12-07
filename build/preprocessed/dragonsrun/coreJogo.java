/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dragonsrun;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.TiledLayer;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;


/**
 *
 * @author Rafael Abreu
 */
public class coreJogo extends GameCanvas implements Runnable{

    private boolean isPlay;
    private boolean onGround = true;
    private boolean jumpKey = false;
    private int delay = 40;
    
    private int width;
    private int height;
    private int currentX;
    private int currentY;

    private int aux = 0;
    private int dx = 2;
    private int dy = -20;
    private int qtdPontos = 0;
    
    private Image imgFundo;
    private Image imgMenina; 
    private Image imgDragon;// imagem dragao
    private Image imgStone;

    private Sprite spmenina;
    private Sprite spDragon;
    private Sprite spStone;
    
    private LayerManager lmgr;
    private TiledLayer fundo;
    private Player p;

    
    public coreJogo() throws IOException, MediaException {
        
        super(true);
        delay = 40;
        height = getHeight();
        width =  getWidth();
        currentX = width / 2;
        currentY = 220;
        
        
        try {
            imgMenina = Image.createImage("/run_a.png");
            imgFundo = Image.createImage("/imgFundo.png");
            imgDragon = Image.createImage("/imgDragon3.png");
            imgStone = Image.createImage("/stone.png");
            InputStream stream = getClass().getResourceAsStream("/scream2.wav");
            p = Manager.createPlayer(stream, "audio/x-wav");

        } catch (java.io.IOException e) {
            System.err.println("Não foi possivel achar arquivo png");
        }
        
        spmenina = new Sprite(imgMenina,75,76);
        spDragon = new Sprite(imgDragon,170,150);
        spStone = new Sprite(imgStone,54,75);       
        spmenina.defineReferencePixel(75 / 2, 76 / 2); // eixo de rotacao
        spStone.defineReferencePixel(54 / 2, 75 / 2);
        lmgr = new LayerManager();  
        lmgr.append(spDragon);
        lmgr.append(spmenina);
        lmgr.append(spStone);
        
    } 
    
    public void stop() {
        isPlay = false;
    }
    
    public void start() {
        isPlay = true;
        Thread t = new Thread(this);
        t.start();
    }   
    
    public void run() {
       Graphics  g = getGraphics();
       
       while (isPlay){
           // le teclado
           leTeclado();
           // desenha a tela
           drawScreen(g);
           try {
                Thread.sleep(delay);
            } catch (InterruptedException ie) {
            }
       }
       
    }   
    
    private void drawScreen(Graphics g){
        
        g.setColor(255, 255, 255);
        g.fillRect(0, 0, width, height);
        
        g.drawImage(imgFundo, aux, 0, 0);
        
        g.drawString("X: " + currentX + " - Y: " + currentY , 2, 270, 0);
        lmgr.paint(g, 0, 0);

        

        if (spmenina.collidesWith(spStone, true)) {
            g.drawString("colides", 10, 10, 0);
            try {
                p.start();
            } catch (MediaException ex) {
                ex.printStackTrace();
            }
            currentX = currentX - 50;
        }
        
        flushGraphics();
        
    }
    
    private void leTeclado(){
        
       int keyState = getKeyStates();        
       
       // Move para esquerda
       if ((keyState & GameCanvas.LEFT_PRESSED) !=0 ) {          
          spmenina.setTransform(Sprite.TRANS_MIRROR);
           currentX = Math.max(10, currentX - dx);
           if (currentX > 10) {
               aux += 1;
           }
       }

       // Move para direita
       if ((keyState & GameCanvas.RIGHT_PRESSED) != 0) {
           spmenina.setTransform(Sprite.TRANS_NONE);
           currentX = Math.min(width - 110, currentX + dx);
           if (currentX <= 158) {
           aux -= 1;
           }
       }  
       
       // Pulo
       if ((keyState & GameCanvas.UP_PRESSED) != 0) {
           jumpKey = true;
           currentX += 10;
           aux -= 40;
       }  
        
        // Para baixo  
        if ((keyState & GameCanvas.DOWN_PRESSED) !=0 ) {
           currentY = Math.min(height, currentY+1);          
        }      
          
        spmenina.nextFrame();
        spDragon.nextFrame();
        Physics();
        spmenina.setPosition(currentX, currentY + dy);
        spStone.setPosition(200 + aux, 202);
       
    }
    
    private void Physics() {

        if (onGround) {
            dy = -20;
        }

        if (jumpKey) {
            dy = -160;
        }

    }
}