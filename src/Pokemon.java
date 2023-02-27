import java.util.Date;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
    
    //=====GETTERS & SETTERS=====//
    // number
    public int getNumber(){return number;}
    public void setNumber(int number){this.number = number;}
    // name
    public String getName(){return name;}
    public void setName(String name){this.name = name;}
    // type 1
    public String getType1(){return type1;}
    public void setType1(String type1){this.type1 = type1;}
    // type 2
    public String getType2(){return type2;}
    public void setType2(String type2){this.type2 = type2;}
    // abilities
    public String[] getAbilities(){return abilities;}
    public void setAbilities(String[] abilities){this.abilities = abilities;}
    // hp
    public int getHP(){return hp;}
    public void setHP(int hp){this.hp = hp;}
    // att
    public int getAtt(){return att;}
    public void setAtt(int att){this.att = att;}
    // def
    public int getDef(){return def;}
    public void setDef(int def){this.def = def;}
    // date
    public Date getDate(){return date;}
    public void setDate(Date date){this.date = date;}

    //Métodos

    public byte[] toByteArray() throws IOException { // Converte objeto para array de bytes
    ByteArrayOutputStream baos = new ByteArrayOutputStream(); // Cria um array de bytes
    DataOutputStream dos = new DataOutputStream(baos); // Cria um fluxo de dados

    dos.writeInt(this.getNumber()); // Escreve o numero do pokemon no array de bytes
    dos.writeInt(this.getHP()); // Escreve o Hp do pokemon no array de bytes
    dos.writeInt(this.getAtt()); // Escreve o Att do pokemon no array de bytes
    dos.writeInt(this.getDef()); // Escreve a Def do pokemon no array de bytes
    dos.writeUTF(this.getName()); // Escreve o nome do pokemon no array de bytes
    dos.writeUTF(this.getType1()); // Escreve o Type1 do pokemon no array de bytes
    dos.writeUTF(this.getType2()); // Escreve o Type2 do pokemon no array de bytes
    //abilities
    //date
    dos.close();
    baos.close();

    return baos.toByteArray(); // Retorna o array de bytes
}
public void fromByteArray(byte[] ba) throws IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(ba); // Cria um array de bytes
    DataInputStream dis = new DataInputStream(bais); // Cria um fluxo de dados
    this.number = dis.readInt(); // Le o numero do pokemon do array de bytes
    this.hp = dis.readInt(); // Le o hp do pokemon do array de bytes
    this.att = dis.readInt(); // Le o att do pokemon do array de bytes
    this.def = dis.readInt(); // Le a def do pokemon do array de bytes
    this.name = dis.readUTF(); // Le o nome do pokemon do array de bytes
    this.type1 = dis.readUTF(); // Le o type1 de usuario do array de bytes
    this.type2 = dis.readUTF(); // Le o type2 do array de bytes

}
public short size() throws IOException {
    return (short)this.toByteArray().length;
}


}

