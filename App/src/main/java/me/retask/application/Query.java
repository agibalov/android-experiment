package me.retask.application;

public interface Query<TEntity> {
	boolean match(TEntity entity);
}