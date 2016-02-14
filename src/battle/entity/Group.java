package battle.entity;

import com.jme3.math.Vector2f;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author szend
 */
public final class Group {

    int leaderIndex = 0;
    int groupDirection=5;
    Vector2f relPos = new Vector2f();
    Vector2f leaderDest = new Vector2f();
    Vector2f Directions[]={
    new Vector2f(1,0),  //Left
    new Vector2f(1,1),  //Left Up
    new Vector2f(0,1),  //Up
    new Vector2f(-1,1), //Right Up
    new Vector2f(-1,0), //Right
    new Vector2f(-1,-1),//Right Down
    new Vector2f(0,-1), //Down
    new Vector2f(1,-1)  //Left Down
    };
    List<Unit> units = new ArrayList<>();

    public Group() {
        
    }
    
    /**
     * One line formation
     * @param n Index of the unit in the group.
     * @return Returns the relative positon to the leader.
     */
    public Vector2f oneLine(int n){
        relPos.x=Math.round((float)n/2f);
        relPos.x*=(((n+1)%2)==0?-1:1)*Directions[groupDirection].y;
        relPos.y=Math.round((float)n/2f);
        relPos.y*=(((n+1)%2)==0?-1:1)*-Directions[groupDirection].x;    
        System.out.println(n + " " + relPos.toString());
        return relPos;
    }
    
    public boolean inPosition()
    {
        boolean a=true;
        for(int i=0; i<units.size(); i++)
        {
            if(i == leaderIndex)
            {
                continue;
            }
            if(!units.get(i).pos.equals(getLeader().pos.add(oneLine(i))))
            {
                a=false;
            }
        }
        return a;
    }
    
    public Unit getLeader() {
        return units.get(leaderIndex);
    }

    public void join(Unit u) {
        units.add(u);
    }

    public void moveTo(int x, int y) {
        leaderDest.x=x;
        leaderDest.y=y;
        getLeader().moveTo(x, y);
    }

    public void onUnitMovedGrid(Unit u) {
        if (getLeader().equals(u)) {            
            for(int i=0; i<units.size(); i++)
            {
                if(i == leaderIndex)
                {
                    continue;
                }
                units.get(i).moveTo((int)getLeader().pos.x+(int)(oneLine(i)).x,(int)getLeader().pos.y+(int)(oneLine(i)).y);
            }
            if(inPosition())
            {
                getLeader().moveTo(leaderDest);
            }
        }
    }
}

/*
A groupoknak szükségük van egy irányra amerre a group néz.
{{-1,-1},{0,-1},{-1,0},{1,0},{0,1},{1,1}  ,{-1,1}  ,{1,-1}}
 jobb le,le    ,jobb  ,bal  ,fel  ,bal fel,jobb fel,bal le

Először a legegyszerűbb formációt kell létrehoznunk, ez az egysoros alakzat.
A groupok mozgás metódusai formáció függetlenek kellenek, hogy legyenek,
azaz minden formáció egy fügvény, amely megadja a mozgató metódusnak, hogy
az x-edik embernek hova kell állni.
A groupok mozgatását atomira kell szétszedni, azaz ha a leader megmozdul
mozgatni kell utána a groupját, és a leader addig vár amíg az alakzat feláll.
Ez a játékmenet és a programozás szempontjából is a legjobb megoldás.
Mivel a group nyolc irányba nézhet mikor elindul, és nyolc irányba
nézhet a mozgatás után, ezért ezeket mind külön kellene nézni.
Azonban ezeket két fő részre oszthatjuk, amelyek csak 
egységnyi (1 grid-nyi elmozdulás) mozgást írnak le:
-Mozgatás a 8 irányba
-Forgás egyhelyben

Mozgatás 8 iányba:
Akkor történik, ha az "a->b"-be való mozgás során a group egyazon
irányba néz.
Forgás egyhelyben:
Akkor történik, ha a leader már a megfelelő pozíción áll, azonban
a többi unitnak még fel kell vennie a group irányának megfelelő alakzatot.

Ahoz, hogy az alakzat tartása ne lassítsa túlságosan a csapatot, ezért
azokban az esetekben amikor az alakzat tartása egy unittól megköveteli,
hogy két vagy több gridnyi utat tegyen meg, a unit mozgási sebességét
megnöveljük, mintha a helyére "futna".

A group tartalmazhasson "tartalákosokat"(másodlagos unitok), azaz 
olyan unitokat, amelyek nem lőnek, továbbá nem részei a group eredeti 
formációjának, hanem azt a cél szolgálják, hogy ha a formációban 
egy unit meghal, akkor átvegyék a helyét. Ezek a unitok a többi mögött 
helyezkednek el.
A másodlagos unitok továbbá lehetnek olyan unitok amelyek a terep 
adottságai miatt nem tudják elfoglalni ésszerű határok között
az alakzatban betöltött pozíciójukat. Azonban ha a group elmozdul
és a formációban betöltött helyük felszabadul vissza lépnek
az elsődleges unitok közé.

Később a két vagy több soros alakzatokat relatíve könnyedén leírhatjuk
két vagy több darab egy soros groupként.
*/