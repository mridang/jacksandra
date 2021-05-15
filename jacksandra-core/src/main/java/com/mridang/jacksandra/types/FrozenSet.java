package com.mridang.jacksandra.types;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;

/**
 * Wrapped set implementation used during schema introspection to deduce if the set
 * is frozen.
 *
 * The name may sound misleading as this acts a marker and does not provide any
 * immutability guarantees.
 *
 * @author mridang
 */
@SuppressWarnings("NullableProblems")
public class FrozenSet<E> implements Set<E>, Frozen {

    private final Set<E> backingSet;

    public FrozenSet(Set<E> backingSet) {
        this.backingSet = backingSet;
    }

    @Override
    public int size() {
        return backingSet.size();
    }

    @Override
    public boolean isEmpty() {
        return backingSet.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return backingSet.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return backingSet.iterator();
    }

    @Override
    public Object[] toArray() {
        return backingSet.toArray();
    }

    @SuppressWarnings("SuspiciousToArrayCall")
    @Override
    public <T> T[] toArray(T[] a) {
        return backingSet.toArray(a);
    }

    @Override
    public boolean add(E e) {
        return backingSet.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return backingSet.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return backingSet.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return backingSet.addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return backingSet.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return backingSet.removeAll(c);
    }

    @Override
    public void clear() {
        backingSet.clear();
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object o) {
        return backingSet.equals(o);
    }

    @Override
    public int hashCode() {
        return backingSet.hashCode();
    }

    @Override
    public Spliterator<E> spliterator() {
        return backingSet.spliterator();
    }


}
