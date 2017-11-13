package org.functionalrefactoring.models;

public interface Storage<T> {
    void flush(T item);
}
