import java.io.File;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * this class contains dragon card in the game
 * @version 1.0
 */
public class Dragon extends Troop
{
    /**
     * creates a new dragon card
     */
    public Dragon() 
    {
        super("both"
                , "Dragon",
              4, 
              1800, 
              3, 
              "both", 
              100,
              3.5, 
              true, 
              1, 
              new int[] {0, 800, 880, 968, 1064, 1168}, 
              new int[] {0, 100, 110, 121, 133, 146},
              new MediaPlayer(new Media(new File("resources/cards/dragon/dragonAttack.mp3").toURI().toString())),
              new MediaPlayer(new Media(new File("resources/cards/dragon/dragonDeath.mp3").toURI().toString())));
    }
    
    /**
     * loads the proper pics for status//0->for deck(150.jpg)//troops://1->moving to right//2->moving to left//3->fighting to right//4->fighting to left//5->dying to right//6->dying to left//buildings://7->cannon ball//8->cannon turning right//9->cannon turning left//10->inferno.gif//11->spells gif
     * */
    public void loadImages()
    {
        pics.put(0, new Image("resources/cards/dragon/dragon150.png"));
        pics.put(-1, new Image("resources/cards/dragon/dragon150wb.jpg"));
        pics.put(1, new Image("resources/cards/dragon/dragonFlyForward.gif"));
        pics.put(2, new Image("resources/cards/dragon/dragonFlyBackward.gif"));
        pics.put(3, new Image("resources/cards/dragon/dragonAttackForward.gif"));
        pics.put(4, new Image("resources/cards/dragon/dragonAttackBackward.gif"));
        pics.put(5, new Image("resources/cards/dragon/dragonDeathForward.gif"));
        pics.put(6, new Image("resources/cards/dragon/dragonDeathBackward.gif"));
    }
}
