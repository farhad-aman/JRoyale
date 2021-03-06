import java.io.File;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * this class contains pekka card in the game
 * @version 1.0
 */
public class Pekka extends Troop
{
    /**
     * creates a new pekka
     */
    public Pekka()
    {
        super("ground",
                "Pekka",
              4, 
              1800, 
              3, 
              "ground", 
              60,
              1, 
              false, 
              1, 
              new int[] {0, 600, 660, 726, 798, 876}, 
              new int[] {0, 325, 357, 393, 432, 474},
              new MediaPlayer(new Media(new File("resources/cards/pekka/pekkaAttack.mp3").toURI().toString())),
              new MediaPlayer(new Media(new File("resources/cards/pekka/pekkaDeath.mp3").toURI().toString())));
    }

    /**
     * loads the proper pics for status//0->for deck(150.jpg)//troops://1->moving to right//2->moving to left//3->fighting to right//4->fighting to left//5->dying to right//6->dying to left//buildings://7->cannon ball//8->cannon turning right//9->cannon turning left//10->inferno.gif//11->spells gif
     * */
    public void loadImages()
    {
        pics.put(0, new Image("resources/cards/pekka/pekka150.png"));
        pics.put(-1, new Image("resources/cards/pekka/pekka150wb.jpg"));
        pics.put(1, new Image("resources/cards/pekka/pekkaRunForward.gif"));
        pics.put(2, new Image("resources/cards/pekka/pekkaRunBackward.gif"));
        pics.put(3, new Image("resources/cards/pekka/pekkaAttackForward.gif"));
        pics.put(4, new Image("resources/cards/pekka/pekkaAttackBackward.gif"));
        pics.put(5, new Image("resources/cards/pekka/pekkaDeathForward.gif"));
        pics.put(6, new Image("resources/cards/pekka/pekkaDeathBackward.gif"));
    }
}
