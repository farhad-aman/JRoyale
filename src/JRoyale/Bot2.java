import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Random;

public class Bot2 extends Bot
{

    public Bot2() 
    {
        super("CPU Normal", GameManager.getRandomDeck(2), 2);
    }


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
            ArrayList<Creature> creatures = chosenCard.makeCreature(armiesAndTowers(chosenCard), -1);
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
     * @return the best place to create army considering towers and number of enemy armies
     * */
    private Point2D armiesAndTowers(Card chosenCard){
        int most = 0, up = 0, middle = 0, down = 0, eUp = 0, eDown = 0, eMiddle = 0, downBridge = 0, upBridge = 0;

        for(Creature c : GameManager.getInstance().getBattle().getArena().getCreatures()){
            double y = c.getPosition().getY(), x = c.getPosition().getX();

            if(c.getSide() == 1 && !(c.getCard() instanceof Spell)){
                if (y <= 240) {
                    if(x < 600)
                        eUp++;
                    else if(x >= 600 && x <= 680)
                        upBridge++;
                    else
                        up++;
                }
                else if (y > 240 && y < 480){
                    if(x < 600)
                        eMiddle++;
                    else
                        middle++;
                }
                else {
                    if(x < 600)
                        eDown++;
                    else if(x >= 600 && x <= 680)
                        downBridge++;
                    else
                        down++;
                }
            }
        }
        most = Math.max(Math.max(Math.max(up, middle), Math.max(down, eUp)), Math.max(Math.max(eDown, downBridge), Math.max(upBridge, eMiddle)));

        boolean upDestroyed = GameManager.getInstance().getBattle().getArena().getPlayerUpPrincess().isEliminated();
        boolean downDestroyed = GameManager.getInstance().getBattle().getArena().getPlayerDownPrincess().isEliminated();

        if(chosenCard instanceof Spell){
            return findPoint(-1);
        }
        else if(most == 0)
            return findPoint(0);
        else if(most == up)
            return findPoint(4);
        else if (most == middle)
            return findPoint(6);
        else if(most == down)
            return findPoint(5);
        else if(most == eUp) {
            if(upDestroyed)
                return findPoint(1);
            else
                return findPoint(4);
        }
        else if(most == eDown) {
            if(downDestroyed)
                return findPoint(2);
            else
                return findPoint(5);
        }
        else if(most == downBridge) {
            if(upDestroyed && !(chosenCard instanceof Building))
                return findPoint(8);
            else if(upDestroyed)
                return findPoint(2);
            else
                return findPoint(5);
        }
        else if(most == upBridge) {
            if(upDestroyed && !(chosenCard instanceof Building))
                return findPoint(7);
            else if(upDestroyed)
                return findPoint(1);
            else
                return findPoint(4);
        }
        else {
            if(upDestroyed && downDestroyed)
                return findPoint(3);
            else
                return findPoint(6);
        }
    }
    /**
     * @param status situation to create army//-1->spell//0->arbitrary//1->enemy up princess//2->enemy down princess//3->enemy king//4->up right//5->down right//6-> near to bot king//7->up bridge//8->down bridge
     * @return the appropriate position to create
        * */
    private Point2D findPoint(int status){
        int x = 0, y = 0;
        Random rand = new Random();

        if(status == -1){
            x = rand.nextInt(280);
            y = 80 + rand.nextInt(1120);
        }
        else if(status == 0){
            x = 720 + rand.nextInt(200);
            y = 20 + rand.nextInt(680);
        }
        else if(status == 1){
            x = 440 + rand.nextInt(160);
            y = 40 + rand.nextInt(200);
        }
        else if(status == 2){
            x = 440 + rand.nextInt(160);
            y = 480 + rand.nextInt(200);
        }
        else if(status == 3){
            x = 440 + rand.nextInt(160);
            y = 240 + rand.nextInt(240);
        }
        else if(status == 4){
            x = 720 + rand.nextInt(200);
            y = 40 + rand.nextInt(200);
        }
        else if(status == 5){
            x = 720 + rand.nextInt(200);
            y = 480 + rand.nextInt(200);
        }
        else if(status == 6){
            x = 840 + rand.nextInt(320);
            y = 240 + rand.nextInt(240);
        }
        else if(status == 7){
            x = 600 + rand.nextInt(80);
            y = 100 + rand.nextInt(80);
        }
        else if(status == 8){
            x = 600 + rand.nextInt(80);
            y = 540 + rand.nextInt(80);
        }
        return new Point2D(x, y);
    }
}