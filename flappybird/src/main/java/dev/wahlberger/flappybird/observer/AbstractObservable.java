package dev.wahlberger.flappybird.observer;

public abstract class AbstractObservable<T> implements Observable<T> {
    private final Collection<Observer<T>> observers;
    private final T observable;

    public AbstractObservable(T observable) {
        this.observers = new HashSet<T>();
        this.observable = observable;
    }

    @Override
    public void addObserver(Observer<T> observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<T> observer) {
        this.observers.remove(observer);
    }

    public void updateObservers() {
        for (Observer<T> observer : this.observers) {
            observer.update(this.observable);
        }
    }
}