import java.util.Date;

public class Pokemon {
    int number, hp, att, def;
    String name, type1, type2;
    String[] abilities;
    Date date;

    public Pokemon(int number, String name, String type1, String type2, String[] abilities, int hp, int att, int def, Date date){
        super();
        this.number = number;
        this.name = name;
        this.type1 = type1;
        this.type2 = type2;
        this.abilities = abilities;
        this.hp = hp;
        this.att = att;
        this.def = def;
        this.date = date;
    }
}