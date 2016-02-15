package battle.entity;

import com.jme3.math.Vector2f;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO:
 * -Dont send two unit to the same position (same distance from them)
 * -twoLine formation
 * -threeLine formation
 * -triangle formation
 * @author szend
 */
public final class Group {

    int leaderIndex = 0;
    int groupDirection=3;
    Vector2f relPos = new Vector2f();
    Vector2f leaderDest = new Vector2f();
    static final Vector2f directions[]={
    new Vector2f(-1,-1),//Right Down
    new Vector2f(-1,0), //Right
    new Vector2f(-1,1), //Right Up
    new Vector2f(0,-1), //Down
    new Vector2f(0,0),  //None
    new Vector2f(0,1),  //Up
    new Vector2f(1,-1), //Left Down
    new Vector2f(1,0),  //Left
    new Vector2f(1,1)   //Left Up
    };
    List<Unit> units = new ArrayList<>();
    Vector2f formation[];

    public Group() {
        
    }
    
    /**
     * One line formation
     * @param n Index of the unit in the group.
     * @return Returns the relative positon to the leader.
     */
    int posShift=0;
    public Vector2f oneLine(int n){
        for(int i=posShift; i<10; i++){
            relPos.x=Math.round((float)(n+i)/2f);
            relPos.x*=(((n+i+1)%2)==0?1:-1)*directions[groupDirection].y;
            relPos.y=Math.round((float)(n+i)/2f);
            relPos.y*=(((n+i+1)%2)==0?1:-1)*-directions[groupDirection].x;
            if((int)((units.get(n).pos.x+relPos.x)*Unit.map.mapHeight)*(int)(units.get(n).pos.y+relPos.y)<0)
                continue;
            if(Unit.map.grid[(int)((units.get(leaderIndex).pos.x+relPos.x)*Unit.map.mapHeight)+(int)(units.get(leaderIndex).pos.y+relPos.y)].isAccesible() &&
            Unit.map.units[(int)((units.get(leaderIndex).pos.x+relPos.x)*Unit.map.mapHeight)+(int)(units.get(leaderIndex).pos.y+relPos.y)]==null)
            {
                System.out.println(relPos.toString());
                posShift+=i;
                return relPos;
            }
        }
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
            if(false)
            {
                System.out.println("unit: "+units.get(i).pos.toString()+" leader+form: "+getLeader().pos.add(formation[i]));
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
            onLeaderMovedGrid(u);
        }
        else
        {
            if (inPosition()) {
                getLeader().moveTo((int)leaderDest.x,(int)leaderDest.y);
            }
        }
    }
    
    public void onLeaderMovedGrid(Unit leader)
    { 
        this.formation = new Vector2f[units.size()];
        float distance[][]=new float[units.size()][units.size()];
        if(!leader.next.equals(Vector2f.ZERO))
        {
            groupDirection=(int) ((leader.next.x+1)*3+leader.next.y+1);
        }
        for(int i=0; i<units.size(); i++)
        {
            formation[i]=new Vector2f(leader.pos.add(oneLine(i)));
        }
        posShift=0;
        for(int i=0; i<units.size(); i++)
        {
            if(i == leaderIndex)
            {
                continue;
            }
            for(int j=0; j<units.size();j++)
            {
                distance[i][j]=units.get(i).pos.distance(formation[j]);
            }
        }
        for(int i=0; i<units.size(); i++)
        {
            float min=Float.MAX_VALUE;
            int minIndex=0;
            if(i == leaderIndex)
            {
                continue;
            }
            for(int j=0; j<units.size(); j++)
            {
                if(j == leaderIndex)
                {
                    continue;
                }
                if(min>distance[i][j])
                {
                    min=distance[i][j];
                    minIndex=j;
                }
            }
            units.get(i).moveTo((int)formation[minIndex].x,(int)formation[minIndex].y);            
        }
        if(inPosition())
        {
            leader.moveTo((int)leaderDest.x,(int)leaderDest.y);
        }
        else
        {
            //leader.moveTo((int)leader.pos.x, (int)leader.pos.y);
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