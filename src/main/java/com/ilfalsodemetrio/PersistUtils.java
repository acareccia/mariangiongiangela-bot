package com.ilfalsodemetrio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by lbrtz on 08/09/16.
 */
public class PersistUtils {

    public static void persistObject(String filename,Object object) throws Exception {
        final FileOutputStream fo = new FileOutputStream(filename);
        final ObjectOutputStream oos = new ObjectOutputStream(fo);
        oos.writeObject(object);
        oos.flush();
        oos.close();
    }

    public static Object loadObject(String filename) throws Exception {
        final FileInputStream fis = new FileInputStream(filename);
        final ObjectInputStream ois = new ObjectInputStream(fis);
        final Object deserializedObject = ois.readObject();

        //log("Object Type to deserialize " + deserializedObject.getClass().getName());
        //users = (Set<ChatUser>) deserializedObject;
        ois.close();
        return deserializedObject;

    }
}
