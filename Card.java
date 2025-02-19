
public class Card {
    private int xPosition;
    private int yPosition;
    private Image image;
    private String rank;
    private String suit;
    private boolean isVisible;
    private boolean isFaceUp;

    private Image backImage;

    String filename;

    public Card(int rank, String suit) {
        if (rank > 1 && rank < 11) {
            this.rank = Integer.toString(rank);
            filename = this.rank;
        } else if (rank == 1) {
            this.rank = "Ace";
            filename = "a";
        } else if (rank == 11) {
            this.rank = "Jack";
            filename = "j";
        } else if (rank == 12) {
            this.rank = "Queen";
            filename = "q";
        } else if (rank == 13) {
            this.rank = "King";
            filename = "k";
        }

        this.suit = suit;

        filename += this.suit.substring(0, 1).toLowerCase() + ".png";
        this.image = new Image("cards/" + filename, -1, 175);
        this.backImage = new Image("cards/back.png", -1, 175);

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
                backImage.makeInvisible();
            } else {
                backImage.makeVisible();
                image.makeInvisible();
            }
        }
    }

    public void makeInvisible() {
        if (isVisible) {
            isVisible = false;
            image.makeInvisible();
            backImage.makeInvisible();            
        }
    }

    public void turnFaceUp() {
        this.isFaceUp = true;
    }

    public void turnFaceDown() {
        this.isFaceUp = false;
    }

    public void setFaceUp(boolean faceUp) {
        this.isFaceUp = faceUp;
    }

    public void setX(int x) {
        this.xPosition = x;
        image.setX(x);
        backImage.setX(x);
    }

    public void setY(int y) {
        this.yPosition = y;
        image.setY(y);
        backImage.setY(y);
    }

    public void setPosition(int x, int y) {
        this.xPosition = x;
        this.yPosition = y;
        image.setPosition(x, y);
        backImage.setPosition(x, y);
    }

    public int getX() {
        return xPosition;
    }

    public int getY() {
        return yPosition;
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }
    
    public boolean isVisible() {
        return isVisible;
    }

    public boolean isFaceUp() {
        return isFaceUp;
    }

    public String getRank() {
        return rank;
    }

    public String getSuit() {
        return suit;
    }
}
