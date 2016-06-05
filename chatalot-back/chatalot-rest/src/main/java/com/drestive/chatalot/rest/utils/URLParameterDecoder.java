package com.drestive.chatalot.rest.utils;

import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by mustafa on 03/06/2016.
 */
public class URLParameterDecoder {

    public static Map<String, List<String>> splitQuery(URL url) {
        if (url == null || StringUtils.isEmpty(url.getQuery())) {
            return Collections.emptyMap();
        }
        return Arrays.stream(url.getQuery().split("&")).map(URLParameterDecoder::splitQueryParameter).collect(Collectors
                .groupingBy(AbstractMap.SimpleImmutableEntry::getKey, LinkedHashMap::new,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
    }

    public static AbstractMap.SimpleImmutableEntry<String, String> splitQueryParameter(String it) {
        final int idx = it.indexOf("=");
        final String key = idx > 0 ? it.substring(0, idx) : it;
        final String value = idx > 0 && it.length() > idx + 1 ? it.substring(idx + 1) : null;
        return new AbstractMap.SimpleImmutableEntry<>(key, value);
    }
}