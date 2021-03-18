package dev.wahlberger.flappybird.observer;

import java.util.Collection;
import java.util.HashSet;

public abstract class AbstractObservable<T> implements Observable<T> {
    private final Collection<Observer<T>> observers;

    public AbstractObservable() {
        this.observers = new HashSet<Observer<T>>();
    }

    @Override
    public void addObserver(Observer<T> observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<T> observer) {
        this.observers.remove(observer);
    }

    public void updateObservers(T observable) {
        for (Observer<T> observer : this.observers) {
            observer.update(observable);
        }
    }
}