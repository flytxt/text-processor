package com.flytxt.parser.marker;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class FlyList<T> implements List<T> {

    private final T[] array;

    private int size;

    @SuppressWarnings("unchecked")
    public FlyList() {
        size = 0;
        array = (T[]) new Object[1500];
    }

    public boolean add(final T element) {
        if (size < array.length - 1) {
            array[size++] = element;
            return true;
        }
        return false;
    }

    public T get(final int index) {
        if (index < size) {
            return array[index];
        }
        return null;
    }

    public void clear() {
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean contains(final Object o) {
        // TODO Auto-generated method stub
        return false;
    }

    public Iterator<T> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object[] toArray() {
        // TODO Auto-generated method stub
        return null;
    }

    public <T> T[] toArray(final T[] a) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean remove(final Object o) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean containsAll(final Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean addAll(final Collection<? extends T> c) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean addAll(final int index, final Collection<? extends T> c) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean removeAll(final Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean retainAll(final Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    public T set(final int index, final T element) {
        // TODO Auto-generated method stub
        return null;
    }

    public void add(final int index, final T element) {
        // TODO Auto-generated method stub

    }

    public T remove(final int index) {
        // TODO Auto-generated method stub
        return null;
    }

    public int indexOf(final Object o) {
        // TODO Auto-generated method stub
        return 0;
    }

    public int lastIndexOf(final Object o) {
        // TODO Auto-generated method stub
        return 0;
    }

    public ListIterator<T> listIterator() {
        // TODO Auto-generated method stub
        return null;
    }

    public ListIterator<T> listIterator(final int index) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<T> subList(final int fromIndex, final int toIndex) {
        // TODO Auto-generated method stub
        return null;
    }

}
