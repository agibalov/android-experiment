package me.loki2302.ui;


public interface AElementVisitor<TResult> {
	TResult visit(AContainer e);
	TResult visit(ALabel e);
	TResult visit(AButton e);
}