package com.ilfalsodemetrio;

import java.io.*;
import java.util.Properties;
import java.util.Set;

/**
 * Created by lbrtz on 08/09/16.
 */
public class FileUtils {

    public static Properties loadResource(String res) {
        final Properties properties = new Properties();
        try (final InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(res)) {
            properties.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
    public static void persistObject(String filename,Object object) throws Exception {
        final FileOutputStream fo = new FileOutputStream("/tmp/"+filename);
        final ObjectOutputStream oos = new ObjectOutputStream(fo);
        oos.writeObject(object);
        oos.flush();
        oos.close();
    }

    public static Object loadObject(String filename) throws Exception {
        final FileInputStream fis = new FileInputStream("/tmp/"+filename);
        final ObjectInputStream ois = new ObjectInputStream(fis);
        final Object deserializedObject = ois.readObject();

        //log("Object Type to deserialize " + deserializedObject.getClass().getName());
        //users = (Set<ChatUser>) deserializedObject;
        ois.close();
        return deserializedObject;

    }
}
