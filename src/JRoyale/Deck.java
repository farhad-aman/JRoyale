import java.util.HashSet;

public class Deck 
{
    private HashSet<Card> cards;
    
    public Deck()
    {
        cards = new HashSet<>();
    }

    public void addCard(Card card)
    {
        if(cards.size() < 8)
        {
            cards.add(card);
        }
    }

    public HashSet<Card> getCards() {
        return cards;
    }

    public void setCards(HashSet<Card> cards)
    {
        if(cards.size() == 8)
        {
            this.cards = cards;
        }
    }
}