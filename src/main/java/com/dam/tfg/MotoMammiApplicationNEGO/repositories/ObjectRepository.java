package com.dam.tfg.MotoMammiApplicationNEGO.repositories;

import java.util.List;

public interface ObjectRepository<T> {
    public void storeList(List<T> t);

    public void store(T t);

    public List<T> retrieve();

    public T search(String name);

    public T delete(int id);
}
