package battle.terrain;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author szend
 * @param <T> The pooled object
 */
public final class ObjectPool<T> {

    private final static class Builder<T> {

        private final Class<T> reference;

        public Builder(Class<T> c) {
            reference = c;
        }

        public T build(Object... args) throws Exception {
            Class<?>[] argClass = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                argClass[i] = args[i].getClass();
            }
            Constructor<T> constructor = reference.getConstructor(argClass);
            T asd = constructor.newInstance(args);
            return asd;
        }

        public T reInitialise(T inst, Object... args) {
            try {
                Class<?>[] argClass = new Class<?>[args.length];
                for (int i = 0; i < args.length; i++) {
                    argClass[i] = args[i].getClass();
                }
                Method m = reference.getMethod("initialise", argClass);
                m.setAccessible(true);
                m.invoke(inst, args);
                return inst;
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                Logger.getLogger(ObjectPool.class.getName()).log(Level.SEVERE, null, e);
            }
            return null;
        }

    }

    private Builder<T> builder;

    private static final int INITIAL_POOL_SIZE = 0;
    private final BiMap<T, Long> poolMap;
    private long count = 0;
    private final ArrayList<Long> freeIndex;

    public ObjectPool(Class<T> c) {
        builder = new Builder<>(c);
        this.freeIndex = new ArrayList<>();
        this.poolMap = (HashBiMap.create(INITIAL_POOL_SIZE));
        for (long i = 0; i < INITIAL_POOL_SIZE; i++) {
            try {
                poolMap.put(builder.build(), count++);
                freeIndex.add(i);
            } catch (Exception ex) {
                Logger.getLogger(ObjectPool.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public T create(Object... args) {
        T vec = null;
        try {
            Long index = null;
            if (!freeIndex.isEmpty()) {
                index = freeIndex.remove(0);
            }
            if (index == null) {
                vec = builder.build(args);
                poolMap.put(vec, count++);
            } else {
                vec = poolMap.inverse().get(index);
                builder.reInitialise(vec, args);
            }
        } catch (Exception ex) {
            Logger.getLogger(ObjectPool.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vec;
    }

    public void destroy(T c) {
        Long index = poolMap.get(c);
        if (index == null) {
            return;
        }
        freeIndex.add(poolMap.get(c));
    }
}
