package com.cea.utils.ui.inject;

import android.view.View;
import java.lang.reflect.Field;

/**
 * Created by Carlos on 13/05/2014.
 */
public class Inject {
    public static <T> void inject(T instance, View view) {
        Field[] fields = instance.getClass().getDeclaredFields();
        try {
            for(Field field : fields){
                InjectView annotation = field.getAnnotation(InjectView.class);
                if(annotation != null){
                    field.setAccessible(true);
                    if(annotation.value() != -1) {
                        field.set(instance, view.findViewById(annotation.value()));
                    }
                    else if(!annotation.tag().equals("")){
                        field.set(instance, view.findViewWithTag(annotation.tag()));
                    }
                }
            }

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}