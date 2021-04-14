package com.example.hi_hotfix;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 作者：haoshuai on 2021/4/14 14:15
 * 邮箱：
 * desc：
 */

public class Hotfix {
    public static void fix(Context context, File patchDexFile) throws IllegalAccessException, InvocationTargetException {
        ClassLoader classLoader = context.getClassLoader();
        Field pathListField = findField(classLoader, "pathList");
        Object pathList = pathListField.get(classLoader);
        List<File> files = Arrays.asList(patchDexFile);
        Object[] pathDexElements = makeDexElements(pathList, files, classLoader);
        combineDexElements(pathList,pathDexElements);

    }

    private static void combineDexElements(Object pathList, Object[] pathDexElements) throws IllegalAccessException {
        Field dexElementsField = findField(pathList, "dexElements");
        Object[] original = (Object[]) dexElementsField.get(pathList);
        Object[] combined = (Object[]) Array.newInstance(original.getClass().getComponentType(), pathDexElements.length + original.length);
        System.arraycopy(pathDexElements,0,combined,0,pathDexElements.length);
        System.arraycopy(original,0,combined,pathDexElements.length,original.length);
        dexElementsField.set(pathList,combined);
    }

    private static Object[] makeDexElements(Object pathList, List<File> files, ClassLoader classLoader) throws InvocationTargetException, IllegalAccessException {
        Method method = findMethod(pathList, "makeDexElements", List.class, File.class, List.class, ClassLoader.class);
        if (method==null) return null;
        ArrayList<IOException> exceptions = new ArrayList<>();
        return (Object[]) method.invoke(pathList,files,null,exceptions,classLoader);
    }

    private static Method findMethod(Object instance, String methodName,Class<?>...parameterTypes) {
        for (Class<?> clazz = instance.getClass();clazz!=null;clazz =clazz.getSuperclass()){
            try {
                Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
                if (!method.isAccessible()){
                    method.setAccessible(true);
                }
                return method;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static Field findField(Object instance, String fileName) {
        for (Class<?>clazz = instance.getClass();clazz!=null;clazz= clazz.getSuperclass()){
            try {
                Field field =clazz.getDeclaredField(fileName);
                if (!field.isAccessible()){
                    field.setAccessible(true);
                }
                return field;
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
