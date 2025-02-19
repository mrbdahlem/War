
public class Card {
    private int xPosition;
    private int yPosition;
    private Image image;
    private String rank;
    private String suit;
    private boolean isVisible;
    private boolean isFaceUp;

    private static Image backImage = new Image("cards/back.png", -1, 175);


    public Card(int rank, String suit) {
        if (rank > 1 && rank < 11) {
            this.rank = Integer.toString(rank);
        } else if (rank == 1) {
            this.rank = "A";
        } else if (rank == 11) {
            this.rank = "J";
        } else if (rank == 12) {
            this.rank = "Q";
        } else if (rank == 13) {
            this.rank = "K";
        }

        this.suit = suit;

        String filename = this.rank + this.suit + ".png";
        this.image = new Image(("cards/" + filename).toLowerCase(), -1, 175);

        this.isFaceUp = false;
        this.isVisible = false;
        this.xPosition = rank * 50;
        this.yPosition = 0;
    }

    public void makeVisible() {
        if (!isVisible) {
            isVisible = true;
            if (isFaceUp) {
                image.makeVisible();
            } else {
                backImage.makeVisible();
            }
        }
    }

    public void makeInvisible() {
        if (isVisible) {
            if (isFaceUp) {
                image.makeInvisible();
            } else {
                backImage.makeInvisible();
            }
            isVisible = false;
        }
    }

}
