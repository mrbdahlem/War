/**
 * 
 *
 * @author 
 * @version 
 */
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.MouseInputAdapter;


public class Game
{
    private final Canvas canvas;
    private final List<Card> deck;

    private List<Card> playerDeck;
    private List<Card> cpuDeck;

    private Text instructions;
    private Text war;
    private Text player;
    private Text computer;
    private Text playerWins;
    private Text cpuWins;
    private Text playerWinsWar;
    private Text cpuWinsWar;
    private boolean done;

    private final int PLAYER_X;
    private final int CPU_X;
    private final int POT_X;

    private int nextPotY;
                
    /**
     * Create a window that will display and allow the user to play the game
     */
    public Game() {
        deck = Card.loadCards();

        // Prepare the canvas
        canvas = Canvas.getCanvas();
        canvas.clear();
        canvas.setTitle("War - Card Game");   
                
        // Add a mouse handler to deal with user input
        canvas.addMouseHandler(new MouseInputAdapter() {
            public void mouseClicked(MouseEvent e) {
                onClick(e.getButton(), e.getX(), e.getY());
            }
            
            public void mouseMoved(MouseEvent e) {
                onMove(-1, e.getX(), e.getY());
            }

            public void mouseDragged(MouseEvent e) {
                onMove(e.getButton(), e.getX(), e.getY());
            }
        });

        int cardWidth = deck.get(0).getWidth();
        PLAYER_X = 200;
        CPU_X = canvas.getWidth() - PLAYER_X - cardWidth;
        POT_X = (canvas.getWidth() - cardWidth) / 2;

        buildDisplay();

        reset();
    }

    private void buildDisplay() {
        int width = canvas.getWidth();
        instructions = new Text("Click to play", 300, 20, 20,
            "black", false);
        instructions.setX((width - instructions.getWidth())/2);
        
        war = new Text("WAR!", 300, 80, 50, "red", false);
        war.setX((width - war.getWidth()) / 2);

        player = new Text("player", PLAYER_X, 153, 15, "black", false);
        computer = new Text("computer", CPU_X, 153, 15, "black", false);
        playerWins = new Text("wins", PLAYER_X + 50, 190, 20, "green", false);
        cpuWins = new Text("wins", CPU_X + 50, 190, 20, "green", false);
        playerWinsWar = new Text("wins", PLAYER_X + 50, 393, 20, "green", false);
        cpuWinsWar = new Text("wins", CPU_X + 50, 393, 20, "green", false);
    }

    // Manually shuffle the deck
    private void shuffleDeck(List<Card> deck) {
        for (int i = 0; i < deck.size(); i++) {
            int randomIndex = (int) (Math.random() * deck.size());
            Card temp = deck.get(i);
            deck.set(i, deck.get(randomIndex));
            deck.set(randomIndex, temp);
        }
    }
    
    /**
     * Reset the Game and display to the beginning state
     */
    public void reset() {
        canvas.clear();
        
        shuffleDeck(deck); // Shuffle the deck manually

        // Split the deck into two halves
        playerDeck = new ArrayList<>(deck.subList(0, deck.size() / 2));
        cpuDeck = new ArrayList<>(deck.subList(deck.size() / 2, deck.size()));
        
        canvas.pause(true);

        lineUpCards(playerDeck, 10);
        lineUpCards(cpuDeck, canvas.getWidth() - deck.get(0).getWidth() - 10);
        
        instructions.makeVisible();
        player.makeVisible();
        computer.makeVisible();

        canvas.pause(false);
        canvas.redraw();
    }

    private void resetDecks() {
        nextPotY = 390;

        canvas.pause(true);

        lineUpCards(playerDeck, 10);
        lineUpCards(cpuDeck, canvas.getWidth() - deck.get(0).getWidth() - 10);

        canvas.pause(false);
        canvas.redraw();
    }

    private void lineUpCards(List<Card> cards, int x) {
        if (cards.isEmpty()) return;

        int y = canvas.getHeight() - cards.get(0).getHeight() - 10;

        for (int i = cards.size() - 1; i >= 0; i--) {
            Card card = cards.get(i);
            card.setPosition(x, y);
            card.setFaceUp(false);
            card.makeInvisible();
            card.makeVisible();
            y -= 5;
        }
    }
    
    private void playRound() {
        war.makeInvisible();
        playerWins.makeInvisible();
        cpuWins.makeInvisible();
        playerWinsWar.makeInvisible();
        cpuWinsWar.makeInvisible();

        resetDecks();

        if (playerDeck.isEmpty() || cpuDeck.isEmpty()) {
            displayWinner();
            return;
        }
    
        // Flip top cards (remove the first card)
        Card playerCard = playerDeck.remove(0);
        Card cpuCard = cpuDeck.remove(0);

        List<Card> pot = new ArrayList<>();
        pot.add(playerCard);
        pot.add(cpuCard);
    
        // Display cards
        playerCard.setPosition(PLAYER_X, 200);
        cpuCard.setPosition(CPU_X, 200);
        playerCard.turnFaceUp();
        cpuCard.turnFaceUp();
    
        // Determine round winner
        int playerValue = playerCard.getValue();
        int cpuValue = cpuCard.getValue();

        if (playerValue > cpuValue) {
            playerDeck.addAll(pot);
            playerWins.makeVisible();
            cpuWins.makeInvisible();
        } else if (cpuValue > playerValue) {
            cpuDeck.addAll(pot);
            playerWins.makeInvisible();
            cpuWins.makeVisible();
        } else {
            handleWar(playerCard, cpuCard, pot);
        }
    }
        
    private void handleWar(Card playerCard, Card cpuCard, List<Card> pot) {
        flashRed();
        war.makeVisible();
    
        // Each player places 3 face-down cards (if they have enough cards)
        for (int i = 0; i < 3; i++) {
            if (playerDeck.size() > 1) {
                Card pCard = playerDeck.remove(0);
                pot.add(pCard);
                pCard.setFaceUp(false);
                pCard.setPosition(POT_X - 10, nextPotY);
                pCard.makeInvisible();
                pCard.makeVisible();
                nextPotY += 5;
            }
            wait(200); canvas.redraw();

            if (cpuDeck.size() > 1) {
                Card cCard = cpuDeck.remove(0);
                pot.add(cCard);
                cCard.setFaceUp(false);
                cCard.setPosition(POT_X + 10, nextPotY);
                cCard.makeInvisible();
                cCard.makeVisible();
                nextPotY += 5;
            }
            wait(300); canvas.redraw();
        }
    
        // Each player places one face-up card (if they have any left)
        if (!playerDeck.isEmpty() && !cpuDeck.isEmpty()) {
            Card playerWarCard = playerDeck.remove(0);
            Card cpuWarCard = cpuDeck.remove(0);
            
            playerWarCard.setPosition(PLAYER_X, 400);
            cpuWarCard.setPosition(CPU_X, 400);
            playerWarCard.turnFaceUp();
            cpuWarCard.turnFaceUp();
    
            pot.add(playerWarCard);
            pot.add(cpuWarCard);
    
            // Determine war winner
            int playerValue = playerWarCard.getValue();
            int cpuValue = cpuWarCard.getValue();
    
            if (playerValue > cpuValue) {
                playerDeck.addAll(pot); 
                playerWinsWar.makeVisible();              
            } else if (cpuValue > playerValue) {
                cpuDeck.addAll(pot);
                cpuWinsWar.makeVisible();
            } else {
                handleWar(playerWarCard, cpuWarCard, pot); // Recursive war
            }
        }
    }
        
    private void displayWinner() {
        // TODO: Make this graphical
        if (playerDeck.isEmpty()) {
            System.out.println("CPU Wins!");
        } else {
            System.out.println("Player Wins!");
        }
    }
        
    /**
     * Handle the user clicking in the window
     * @param button the button that was pressed
     * @param x the x coordinate of the mouse position
     * @param y the y coordinate of the mouse position
     */
    private void onClick(int button, int x, int y) {
        if (!done) {
            playRound();
        }
        else {
            reset();
        }
    }

    /**
     * Handle the user moving the mouse in the window
     * @param button the button that was pressed, or -1 if no button was pressed
     * @param x the x coordinate of the mouse position
     * @param y the y coordinate of the mouse position
     */
    private void onMove(int button, int x, int y) {
        
    }
    
    /**
     * Quickly flash the window background red
     */
    public void flashRed() {
        // The user messed up, show them they made a mistake
        canvas.setBackgroundColor("red");
        canvas.redraw();
        wait(500);
        
        canvas.setBackgroundColor("white");
        canvas.redraw();
    }
    
    /**
     * Wait for a specified number of milliseconds before finishing. This
     * provides an easy way to specify a small delay which can be used when
     * producing animations.
     *
     * @param milliseconds the number
     */
    public static void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            // ignoring exceptions at the moment
        }
    }

    /**
     * Run the game
     */
    public static void main(String[] args) {
        // Start the game
        new Game();
    }
}
