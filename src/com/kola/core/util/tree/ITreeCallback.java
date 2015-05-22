package com.kola.core.util.tree;

public interface ITreeCallback<T,T1> {
	
	void init(T o) throws Exception;

    T1 getId(T o) throws Exception;

    void addChild(T parent, T child) throws Exception;

    T1 getParentId(T o) throws Exception;
}
