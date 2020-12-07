/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dragonsrun;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.*;

/**
 * @author Rafael Abreu
 */
public class Midlet extends MIDlet implements CommandListener {

    private Display display;
    private Command cmdExit;
    private coreJogo cj;
    
    Midlet() throws Exception {
        display = Display.getDisplay(this);
       
        if (( cj = new coreJogo())!= null){
            cmdExit = new Command("SAIR",Command.EXIT,0);
            cj.addCommand(cmdExit);
            cj.setCommandListener(this);
        }
    }
    
    public void startApp() {
        
        if (cj != null){
            display.setCurrent(cj);
            cj.start();
        }
        
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command c, Displayable d) {
        if (c==cmdExit) {
            System.gc();  // chama o grabage collector
            destroyApp(false);
            notifyDestroyed();
        }
    }
}
