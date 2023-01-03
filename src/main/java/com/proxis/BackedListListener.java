package com.proxis;

public abstract class BackedListListener<T> {

    public abstract void setOnChanged(ListChangeEvent<T> event);

}