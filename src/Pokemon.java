import java.util.Date;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

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

    /* --------------------------
     * OBJETO PARA ARRAY DE BYTES
     * --------------------------
     * cria um array de bytes
     * cria um fluxo de dados
     * escreve os atributos do pokemon no array de bytes
     * retorna o array de bytes
     */
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(this.getNumber());
        dos.writeUTF(this.getName());
        dos.writeUTF(this.getType1());
        dos.writeUTF(this.getType2());
        // abilities
        dos.writeInt(this.getHP());
        dos.writeInt(this.getAtt());
        dos.writeInt(this.getDef());
        // date

        dos.close();
        baos.close();
        return baos.toByteArray();
    }

    /* ------------------
     * LER ARRAY DE BYTES
     * ------------------
     * cria um array de bytes
     * cria um fluxo de dados
     * le e atribui, do array de bytes, os atributos do pokemon
     */
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        this.number = dis.readInt();
        this.name = dis.readUTF();
        this.type1 = dis.readUTF();
        this.type2 = dis.readUTF();
        // abilities
        this.hp = dis.readInt();
        this.att = dis.readInt();
        this.def = dis.readInt();
        // date
    }

    /* -------------------------
     * TAMANHO DO ARRAY DE BYTES
     * -------------------------
     * retorna o tamanho do array de bytes
     */
    public short size() throws IOException {
        return (short)this.toByteArray().length;
    }
}

