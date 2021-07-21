import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Random;

public class Bot3 extends Bot
{


    public Bot3() 
    {
        super("CPU Hard", GameManager.getRandomDeck(3), 3);
    }

    /**
     * manages the bot decisions in each frame
     * */
    @Override
    public void step()
    {
        Battle battle = GameManager.getInstance().getBattle();
        Random random = new Random();
        int cardNumber;
        cardNumber = random.nextInt(4);
        cardNumber += 4;
        Card chosenCard = battle.getBotCardsQueue().get(cardNumber);

        if(chosenCard.getCost() <= battle.getBotElixirBar().getElixir())
        {
            ArrayList<Creature> creatures = chosenCard.makeCreature(findPoint(findPopulated(chosenCard)), -1);
            for(Creature c : creatures)
            {
                battle.getArena().getCreatures().add(c);
            }
            battle.getBotCardsQueue().remove(cardNumber);
            battle.getBotCardsQueue().add(0, chosenCard);
            battle.getBotElixirBar().takeExir(chosenCard.getCost());
        }
    }
    /**
     * finds the most populated [place for the armies
     * @param chosenCard to decide
     * @return the most populated place
     * */
    private int findPopulated(Card chosenCard){
        int most = 0, up = 0, middle = 0, down = 0, eUp = 0, eDown = 0, eMiddle = 0, downBridge = 0, upBridge = 0, side = 1;

        if(chosenCard instanceof Rage)
            side = -1;

        for(Creature c : GameManager.getInstance().getBattle().getArena().getCreatures())
        {
            double Y = c.getPosition().getY(), X = c.getPosition().getX();
            int effect = 1;

            if(c.getSide() == side && !(c.getCard() instanceof Spell) && chosenCard.canHit(chosenCard.getTarget(), c.getCard().getType()))
            {
                effect += considerEnemy(chosenCard, c.getCard());

                if (Y <= 240)
                {
                    if(X < 600)
                        eUp += effect;
                    else if(X >= 600 && X <= 680)
                        upBridge += effect;
                    else
                        up += effect;
                }
                else if (Y > 240 && Y < 480)
                {
                    if(X < 600)
                        eMiddle += effect;
                    else
                        middle += effect;
                }
                else
                {
                    if(X < 600)
                        eDown += effect;
                    else if(X >= 600 && X <= 680)
                        downBridge += effect;
                    else
                        down += effect;
                }
            }
        }
        boolean upDestroyed = GameManager.getInstance().getBattle().getArena().getPlayerUpPrincess().isEliminated();
        boolean downDestroyed = GameManager.getInstance().getBattle().getArena().getPlayerDownPrincess().isEliminated();

        if(!(chosenCard  instanceof Rage)){
            if (upDestroyed && !downDestroyed)
                eUp++;
            if (downDestroyed && !upDestroyed)
                eDown++;
            if (upDestroyed && downDestroyed)
                eMiddle += 2;
        }

        most = upBridge;
        if(most <= downBridge)
            most = downBridge;
        if(most <= eDown)
            most = eDown;
        if(most <= eUp)
            most = eUp;
        if(most <= eMiddle)
            most = eMiddle;
        if(most <= up)
            most = up;
        if(most <= down)
            most = down;
        if(most <= middle)
            most = middle;

        if(!(chosenCard instanceof Rage) && chosenCard instanceof Spell)
        {
            return -1;
        }
        else if(most == up)
            return 4;
        else if (most == middle)
            return 6;
        else if(most == down)
            return 5;
        else if(most == eUp)
        {
            if(upDestroyed)
                return 1;
            else
                return 4;
        }
        else if(most == eDown)
        {
            if(downDestroyed)
                return 2;
            else
                return 5;
        }
        else if(most == downBridge)
        {
            if(downDestroyed && !(chosenCard instanceof Building))
                return 8;
            else if(downDestroyed)
                return 2;
            else
                return 5;
        }
        else if(most == upBridge)
        {
            if(upDestroyed && !(chosenCard instanceof Building))
                return 7;
            else if(upDestroyed)
                return 1;
            else
                return 4;
        }
        else if(most == eMiddle)
        {
            if(upDestroyed && downDestroyed)
                return 3;
            else
                return 6;
        }
        return 0;
    }
    /**
     * checks the enemy type and effect
     * @param chosenCard to create
     * @param enemy to consider its type
     * */
    private int considerEnemy(Card chosenCard, Card enemy) {
        int effect = 0;

        if(enemy.canHit(enemy.getTarget(), chosenCard.getType()))
            return -1;
        if(chosenCard instanceof Valkyrie && enemy instanceof Barbarians)
            return 2;
        if((chosenCard instanceof Fireball || chosenCard instanceof Arrows) && (enemy instanceof Barbarians || enemy instanceof Archer))
            return 2;
        /*
        *
        * TODO: considering other type of the creatures
        *
         */



        return 1;
    }

    /**
     * creates a point for the given status
     * @param status situation to create army//-2->rage//-1->spell//0->arbitrary//1->enemy up princess//2->enemy down princess//3->enemy king//4->up right//5->down right//6-> near to bot king//7->up bridge//8->down bridge
     * @return the appropriate position to create
     * */
    private Point2D findPoint(int status)
    {
        int x = 0, y = 0;
        Random rand = new Random();

        if(status == -2)
        {
            x = 920 + rand.nextInt(200);
            y = 120 + rand.nextInt(480);
        }
        if(status == -1)
        {
            x = rand.nextInt(280);
            y = 80 + rand.nextInt(1120);
        }
        else if(status == 0)
        {
            x = 720 + rand.nextInt(200);
            y = 20 + rand.nextInt(680);
        }
        else if(status == 1)
        {
            x = 440 + rand.nextInt(160);
            y = 40 + rand.nextInt(200);
        }
        else if(status == 2)
        {
            x = 440 + rand.nextInt(160);
            y = 480 + rand.nextInt(200);
        }
        else if(status == 3)
        {
            x = 440 + rand.nextInt(160);
            y = 240 + rand.nextInt(240);
        }
        else if(status == 4)
        {
            x = 720 + rand.nextInt(200);
            y = 40 + rand.nextInt(200);
        }
        else if(status == 5)
        {
            x = 720 + rand.nextInt(200);
            y = 480 + rand.nextInt(200);
        }
        else if(status == 6)
        {
            x = 840 + rand.nextInt(320);
            y = 240 + rand.nextInt(240);
        }
        else if(status == 7)
        {
            x = 600 + rand.nextInt(80);
            y = 100 + rand.nextInt(80);
        }
        else if(status == 8)
        {
            x = 600 + rand.nextInt(80);
            y = 540 + rand.nextInt(80);
        }
        return new Point2D(x, y);
    }
}