package com.anarchy.deviceretriever.data.source.retriever;

import com.anarchy.deviceretriever.data.Info;

import java.util.List;

import rx.Observable;

/**
 * Version 2.1.1
 * <p>
 * Date: 16/10/20 10:23
 * <p/>
 */
public interface Retriever {

    List<Info> retrieve();
}
