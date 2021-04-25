package com.mridang.jacksandra.types;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.UnaryOperator;

/**
 * Wrapped list implementation used during schema introspection to deduce if the list
 * is frozen.
 *
 * The name may sound misleading as this acts a marker and does not provide any
 * immutability guarantees.
 *
 * @author mridang
 */
@SuppressWarnings("NullableProblems")
public class FrozenList<E> implements List<E>, Frozen {

    private final List<E> backingList;

    public FrozenList(List<E> backingList) {
        this.backingList = backingList;
    }

    @Override
    public int size() {
        return backingList.size();
    }

    @Override
    public boolean isEmpty() {
        return backingList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return backingList.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return backingList.iterator();
    }

    @Override
    public Object[] toArray() {
        return backingList.toArray();
    }

    @SuppressWarnings("SuspiciousToArrayCall")
    @Override
    public <T> T[] toArray(T[] a) {
        return backingList.toArray(a);
    }

    public boolean add(E e) {
        return backingList.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return backingList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return backingList.containsAll(c);
    }

    public boolean addAll(Collection<? extends E> c) {
        return backingList.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        return backingList.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return backingList.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return backingList.retainAll(c);
    }

    public void replaceAll(UnaryOperator<E> operator) {
        backingList.replaceAll(operator);
    }

    public void sort(Comparator<? super E> c) {
        backingList.sort(c);
    }

    @Override
    public void clear() {
        backingList.clear();
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object o) {
        return backingList.equals(o);
    }

    @Override
    public int hashCode() {
        return backingList.hashCode();
    }

    @Override
    public E get(int index) {
        return backingList.get(index);
    }

    public E set(int index, E element) {
        return backingList.set(index, element);
    }

    public void add(int index, E element) {
        backingList.add(index, element);
    }

    @Override
    public E remove(int index) {
        return backingList.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return backingList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return backingList.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return backingList.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return backingList.listIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return backingList.subList(fromIndex, toIndex);
    }

    @Override
    public Spliterator<E> spliterator() {
        return backingList.spliterator();
    }


}
