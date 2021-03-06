import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.Iterator;

/**
 * this class contains fireball card in the game
 * @version 1.0
 */
public class Fireball extends Spell
{
    /**
     * the damage of the fireball
     */
    private final int[] damage = {0, 325, 357, 393, 432, 474};

    /**
     * creates a new fireball card
     */
    public Fireball() 
    {
        super("spell", "Fireball", 4, 2.5, 100, new MediaPlayer(new Media(new File("resources/cards/fireball/fireballAttack.mp3").toURI().toString())));
    }

    /**
     * loads the proper pics for status//0->for deck(150.jpg)//troops://1->moving to right//2->moving to left//3->fighting to right//4->fighting to left//5->dying to right//6->dying to left//buildings://7->cannon ball//8->cannon turning right//9->cannon turning left//10->inferno.gif
     * */
    public void loadImages()
    {
        pics.put(0, new Image("resources/cards/fireball/fireball150.png"));
        pics.put(-1, new Image("resources/cards/fireball/fireball150wb.jpg"));
    }

    @Override
    public void step(Creature creature)
    {
        Iterator<Creature> it = GameManager.getInstance().getBattle().getArena().getCreatures().iterator();

        while (it.hasNext()) 
        {
            Creature tempTarget = it.next();

            if(tempTarget.getPosition().distance(creature.getPosition()) <= ((tempTarget.getCard() instanceof King ? 80 : tempTarget.getCard() instanceof Princess ? 60 : tempTarget.getCard() instanceof Building ? 50 : 40) * super.getRange()) && creature.getSide() != tempTarget.getSide())
            {
                tempTarget.getHit(damage[creature.getLevel()], creature.getPosition().getX() < tempTarget.getPosition().getX() ? 6 : 5);
            }
        }
    }
}
