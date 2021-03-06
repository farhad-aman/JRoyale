import java.io.File;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * this class contains valkyrie card in the game
 * @version 1.0
 */
public class Valkyrie extends Troop
{
    /**
     * creates a new valkyrie card
     */
    public Valkyrie() 
    {
        super("ground",
                "Valkyrie",
              4, 
              1500, 
              2, 
              "ground", 
              50,
              1, 
              true, 
              1, 
              new int[] {0, 880, 968, 1064, 1170, 1284}, 
              new int[] {0, 120, 132, 145, 159, 175},
              new MediaPlayer(new Media(new File("resources/cards/valkyrie/valkyrieAttack.mp3").toURI().toString())),
              new MediaPlayer(new Media(new File("resources/cards/valkyrie/valkyrieDeath.mp3").toURI().toString())));
    }
    
    /**
     * loads the proper pics for status//0->for deck(150.jpg)//troops://1->moving to right//2->moving to left//3->fighting to right//4->fighting to left//5->dying to right//6->dying to left//buildings://7->cannon ball//8->cannon turning right//9->cannon turning left//10->inferno.gif//11->spells gif
     * */
    public void loadImages()
    {
        pics.put(0, new Image("resources/cards/valkyrie/valkyrie150.png"));
        pics.put(-1, new Image("resources/cards/valkyrie/valkyrie150wb.jpg"));
        pics.put(1, new Image("resources/cards/valkyrie/valkyrieRunForward.gif"));
        pics.put(2, new Image("resources/cards/valkyrie/valkyrieRunBackward.gif"));
        pics.put(3, new Image("resources/cards/valkyrie/valkyrieAttackForward.gif"));
        pics.put(4, new Image("resources/cards/valkyrie/valkyrieAttackBackward.gif"));
        pics.put(5, new Image("resources/cards/valkyrie/valkyrieDeathForward.gif"));
        pics.put(6, new Image("resources/cards/valkyrie/valkyrieDeathBackward.gif"));
    }
}
