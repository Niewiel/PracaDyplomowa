package pl.niewiel.pracadyplomowa.database.service;

import java.util.ArrayList;

public interface Service<T> {
    ArrayList<T> getAll();

    T getById(int id);

    boolean create(T item);

    boolean delete(T item);

    boolean update(T item);

    void synchronize();
}
