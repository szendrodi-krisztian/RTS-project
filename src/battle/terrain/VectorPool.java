/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battle.terrain;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author szend
 */
public final class VectorPool {

    private static class Loader {

        static final VectorPool instance = new VectorPool();
    }
    private static final int INITIAL_POOL_SIZE = 0;
    private BiMap<MyVector2f, Long> poolMap;
    private long count = 0;
    private long count2 = 0;
    private final ArrayList<Long> freeIndex;

    public static VectorPool getInstance() {
        return Loader.instance;
    }

    private VectorPool() {

        this.freeIndex = new ArrayList<>();
        this.poolMap = (HashBiMap.create(INITIAL_POOL_SIZE));
        for (long i = 0; i < INITIAL_POOL_SIZE; i++) {
            poolMap.put(new MyVector2f(), count++);
            freeIndex.add(i);
        }
    }

    public final MyVector2f createVector(float x, float y) {
        MyVector2f vec;
        Long index = null;
        if (!freeIndex.isEmpty()) {
            index = freeIndex.remove(0);
        }
        if (index == null) {
            vec = new MyVector2f();
            poolMap.put(vec, count++);
            count2++;
            //System.out.println(count+" "+count2+" "+poolMap.size());
        } else {
            vec = poolMap.inverse().get(index);
        }
        vec.x = x;
        vec.y = y;
        return vec;
    }

    public final void destroyVector(MyVector2f c) {
        Long index = poolMap.get(c);
        if(index == null) return;
            freeIndex.add(poolMap.get(c));
    }
}
