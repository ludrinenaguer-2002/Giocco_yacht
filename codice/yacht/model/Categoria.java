package yacht.model;

/**
 * Le 12 categorie del gioco Yacht.
 */
public enum Categoria {
    ONES("Ones"),
    TWOS("Twos"),
    THREES("Threes"),
    FOURS("Fours"),
    FIVES("Fives"),
    SIXES("Sixes"),
    FULL_HOUSE("Full House"),
    FOUR_OF_KIND("Four of a Kind"),
    LITTLE_STRAIGHT("Little Straight"),
    BIG_STRAIGHT("Big Straight"),
    CHOICE("Choice"),
    YACHT("Yacht");
    
    private final String nome;
    
    Categoria(String n) {
        this.nome = n;

    }
    
    public String getNome() {
        return nome;
    }

}
