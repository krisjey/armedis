package com.github.armedis.annotation;

import java.lang.reflect.Field;

public class FruitInfoUtil {
    public static void getFruitInfo(Class<?> clazz) {

        String strFruitName = " 과일 이름 :";
        String strFruitColor = " 과일 색 :";
        String strFruitProvider = "과일 파는 곳";

        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(FruitName.class)) {
                FruitName fruitName = field.getAnnotation(FruitName.class);
                strFruitName = strFruitName + fruitName.value();
                System.out.println(strFruitName);
            }
            else if (field.isAnnotationPresent(FruitColor.class)) {
                FruitColor fruitColor = field.getAnnotation(FruitColor.class);
                strFruitColor = strFruitColor + fruitColor.fruitColor().toString();
                System.out.println(strFruitColor);
            }
            else if (field.isAnnotationPresent(FruitProvider.class)) {
                FruitProvider fruitProvider = field.getAnnotation(FruitProvider.class);
                strFruitProvider = " 과일 파는 곳의 ID: " + fruitProvider.id() + " 지점 이름 : " + fruitProvider.name()
                        + " 지점 주소: " + fruitProvider.address();
                System.out.println(strFruitProvider);
            }
        }
    }
}