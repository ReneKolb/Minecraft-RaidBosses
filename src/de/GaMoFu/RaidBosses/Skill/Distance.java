package de.GaMoFu.RaidBosses.Skill;

public class Distance<T> {

    private T le;

    private double dist;

    public Distance(T object, double distance) {
        this.le = object;
        this.dist = distance;
    }

    public T getObject() {
        return this.le;
    }

    public double getObject2() {
        return this.dist;
    }

}
