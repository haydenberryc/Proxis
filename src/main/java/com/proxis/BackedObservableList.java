package com.proxis;

import java.util.*;

public class BackedObservableList<T> implements List<T> {
    private final List<T> backed;

    public BackedObservableList() {
        backed = new ArrayList();
    }

    public BackedObservableList(List<T> backed) {
        this.backed = backed;
    }

    @Override
    public int size(){
        return backed.size();
    }

    @Override
    public boolean isEmpty() {
        return backed.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return backed.contains(o);
    }

    @Override
    public boolean add(T e) {
        if (backed.add(e)) {
            ListChangeEvent<T> event = new ListChangeEvent(this, backed.indexOf(e), backed.indexOf(e) + 1, true, e);
            notifyListeners(event);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        if (backed.remove(o)) {
            ListChangeEvent<T> event = new ListChangeEvent(this, backed.indexOf(o),

                    backed.indexOf(o) + 1, false, o);
            notifyListeners(event);
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return backed.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return backed.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return backed.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return backed.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return backed.retainAll(c);
    }

    @Override
    public void clear() {
        backed.clear();
    }

    @Override
    public T get(int index) {
        return backed.get(index);
    }

    @Override
    public T set(int index, T element) {
        return backed.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        backed.add(index, element);
    }

    @Override
    public T remove(int index) {
        return backed.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return backed.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return backed.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return backed.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return backed.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return backed.subList(fromIndex, toIndex);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            T currentItem = null;
            int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return backed.size() > currentIndex;
            }

            @Override
            public T next() {

                return currentItem = backed.get(currentIndex++);
            }

            @Override
            public void remove() {
                if (backed.remove(currentItem)) {
                    currentIndex--;
                    notifyListeners(new ListChangeEvent<T>(backed, currentIndex, currentIndex + 1, false, currentItem));
                }
            }
        };
    }

    @Override
    public Object[] toArray() {
        return backed.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return backed.toArray(a);
    }

    private void notifyListeners(ListChangeEvent<T> event) {
        for (BackedListListener<T> listener : listeners) {
            listener.setOnChanged(event);
        }
    }

    private final List<BackedListListener> listeners = new ArrayList();

    public void addListener(BackedListListener<T> listener) {
        listeners.add(listener);
    }
}