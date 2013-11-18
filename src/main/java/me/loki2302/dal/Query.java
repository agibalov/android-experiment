package me.loki2302.dal;

public interface Query<TEntity> {
	boolean match(TEntity entity);
}