package me.loki2302.application;

public interface Query<TEntity> {
	boolean match(TEntity entity);
}