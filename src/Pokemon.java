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
        // --------------- Getters e Setters ---------------

        // Numero da Pokedex
        public int getnumber(){
            return number;
        }
        public void setNumber(int number){
            this.number = number;
        }
        // HP
        public int getHP(){
            return hp;
        }
        public void setHP(int hp){
            this.hp = hp;
        }
        // Att
        public int getAtt(){
            return att;
        }
        public void setAtt(int att){
            this.att = att;
        }
        // Def
        public int getDef(){
            return def;
        }
        public void setDef(int def){
            this.def = def;
        }
        // Nome do Pokemon
        public String getName() {return name;}
        public void setName(String name) {this.name = name;}
        // Type 1
        public String getType1() {return type1;}
        public void setType1(String type1) {this.type1 = type1;}
        //Type 2
        public String getType2() {return type2;}
        public void setType2(String type2) {this.type2 = type2;}
        // abilities
        public String[] getAbilities() {return abilities;}
        public void setAbilities(String[] abilities) {this.abilities = abilities;}
        // Date
        public Date getDate(){return date;}
        public void setDate(Date date){this.date=date;}
        



}