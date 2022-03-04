package com.wwb.commonbase.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassUtils {

    /**
     * 从包package中获取所有的Class
     *
     * @param packageName
     * @return
     */
    public static List<Class<?>> getClasssFromPackage(String packageName) {

        //第一个class类的集合
        List<Class<?>> classes = new ArrayList<Class<?>>();
        //是否循环迭代
        boolean recursive = true;
        //获取包的名字 并进行替换
        String packageDirName = packageName.replace('.', '/');
        //定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            //循环迭代下去
            while (dirs.hasMoreElements()) {
                //获取下一个元素
                URL url = dirs.nextElement();
                //得到协议的名称
                String protocol = url.getProtocol();
                //如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    //获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    //以文件的方式扫描整个包下的文件 并添加到集合中
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
                } else if ("jar".equals(protocol)) {
                    //如果是jar包文件
                    //定义一个JarFile
                    JarFile jar;
                    try {
                        //获取jar
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        //从此jar包 得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        //同样的进行循环迭代
                        while (entries.hasMoreElements()) {
                            //获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            //如果是以/开头的
                            if (name.charAt(0) == '/') {
                                //获取后面的字符串
                                name = name.substring(1);
                            }
                            //如果前半部分和定义的包名相同
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                //如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    //获取包名 把"/"替换成"."
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                //如果可以迭代下去 并且是一个包
                                if ((idx != -1) || recursive) {
                                    //如果是一个.class文件 而且不是目录
                                    if (name.endsWith(".class") && !entry.isDirectory()) {
                                        //去掉后面的".class" 获取真正的类名
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        try {
                                            //添加到classes
                                            classes.add(Class.forName(packageName + '.' + className));
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classes;
    }

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName
     * @param packagePath
     * @param recursive
     * @param classes
     */
    public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, List<Class<?>> classes) {
        //获取此包的目录 建立一个File
        File dir = new File(packagePath);
        //如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        //如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {
            //自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            @Override
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        //循环所有文件
        for (File file : dirfiles) {
            //如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(),
                        file.getAbsolutePath(),
                        recursive,
                        classes);
            } else {
                //如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    //添加到集合中去
                    Class<?> clazz=Class.forName(packageName + '.' + className);
                    if(clazz!=null) {
                        classes.add(clazz);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    /**
//     * 获得包下面的所有的class
//     *
//     * @param pack
//     *            package完整名称
//     * @return List包含所有class的实例
//     */
//    public static List<Class> getClasssFromPackage(String pack) {
//        List<Class> clazzs = new ArrayList<Class>();
//
//        // 是否循环搜索子包
//        boolean recursive = true;
//
//        // 包名字
//        String packageName = pack;
//        // 包名对应的路径名称
//        String packageDirName = packageName.replace('.', '/');
//
//        Enumeration<URL> dirs;
//
//        try {
//            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
//            while (dirs.hasMoreElements()) {
//                URL url = dirs.nextElement();
//
//                String protocol = url.getProtocol();
//
//                if ("file".equals(protocol)) {
//                    System.out.println("file类型的扫描");
//                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
//                    findClassInPackageByFile(packageName, filePath, recursive, clazzs);
//                } else if ("jar".equals(protocol)) {
//                    System.out.println("jar类型的扫描");
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return clazzs;
//    }
//
//    public static void findClassInPackageByFile(String packageName, String filePath, final boolean recursive, List<Class> clazzs) {
//        File dir = new File(filePath);
//        if (!dir.exists() || !dir.isDirectory()) {
//            return;
//        }
//        // 在给定的目录下找到所有的文件，并且进行条件过滤
//        File[] dirFiles = dir.listFiles(new FileFilter() {
//
//            @Override
//            public boolean accept(File file) {
//                boolean acceptDir = recursive && file.isDirectory();// 接受dir目录
//                boolean acceptClass = file.getName().endsWith("class");// 接受class文件
//                return acceptDir || acceptClass;
//            }
//        });
//
//        for (File file : dirFiles) {
//            if (file.isDirectory()) {
//                findClassInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, clazzs);
//            } else {
//                String className = file.getName().substring(0, file.getName().length() - 6);
//                try {
//                    clazzs.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + "." + className));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
}