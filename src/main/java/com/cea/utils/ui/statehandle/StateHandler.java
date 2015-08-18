package com.cea.utils.ui.statehandle;

import android.os.Bundle;

import java.lang.reflect.Field;

/**
 * Created by Carlos on 14/08/2015.
 */
public class StateHandler {
    public static void saveInstanceState(Object instance, Bundle outState){
        Field[] fields = instance.getClass().getDeclaredFields();
        try {
            for(Field field : fields){
                ManagedState annotation = field.getAnnotation(ManagedState.class);
                if(annotation != null){
                    field.setAccessible(true);
                    outState.putSerializable(field.getName(), (java.io.Serializable) field.get(instance));
                }
            }

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void restoreInstanceState(Object instance, Bundle savedInstanceState){
        Field[] fields = instance.getClass().getDeclaredFields();
        try {
            for(Field field : fields){
                ManagedState annotation = field.getAnnotation(ManagedState.class);
                if(annotation != null){
                    field.setAccessible(true);
                    Object fieldValue = savedInstanceState.get(field.getName());
                    field.set(instance, fieldValue);
                }
            }

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
