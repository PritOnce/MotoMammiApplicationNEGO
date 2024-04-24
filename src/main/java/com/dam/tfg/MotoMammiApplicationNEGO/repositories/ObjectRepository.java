package com.dam.tfg.MotoMammiApplicationNEGO.repositories;

public interface ObjectRepository<T> {
    public void store(T t);

    public T retrieve();

    public T search(String name);

    public T delete(int id);
}
