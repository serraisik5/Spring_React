package com.example.demo.utils;

import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@UtilityClass
public class CollectionUtils {
    public static <T> List<T> getPage(List<T> sourcelist,int page,int size ){
        if(size<=0 || page<=0){
            throw new IllegalArgumentException("invalid page size serra");
        }
        int fromindex = (page-1)*size;
        if(sourcelist== null || sourcelist.size() <= fromindex){
            return Collections.emptyList();
        }
        return sourcelist.subList(fromindex,Math.min(fromindex+size,sourcelist.size()));

    }
    public static boolean isEmpty(Collection<?> roleNames){
        return Objects.isNull(roleNames) || roleNames.isEmpty();
    }
    public static boolean isNotEmpty(Collection<?> roleNames){
        return !isEmpty(roleNames);
    }
}

