package de.processes;


import de.constants.Constants;
import de.model.User;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//see https://howtodoinjava.com/jaxb/write-object-to-xml/
public class DataManager {
    public final static String directory = "E:\\Studium\\Semester 4\\LabSW\\Code_Server\\LordOfWarServer\\src\\main\\resources\\data\\";
    public final static String extension = ".txt";
    public final static Path userIDPath = Paths.get(DataManager.directory + "userID.txt");

    public static Path usernameToPath(String username) {
        return Paths.get(directory + username + extension);
    }

    public synchronized static boolean userToFile(User user) {
        try {
            File file = usernameToPath(user.getUsername()).toFile();
            if (file.createNewFile()) {
                try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
                    writer.write(Integer.valueOf(user.getUserID()).toString());
                    writer.write(Constants.STRINGSEPERATOR);
                    writer.write(user.getUsername());
                    writer.write(Constants.STRINGSEPERATOR);
                    writer.write(user.getPassword());
                    writer.write(Constants.STRINGSEPERATOR);
                    writer.write(Integer.valueOf(user.getScore()).toString());
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public synchronized static User fileToUser(File file) {
        User user = null;
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file.toPath().toString()), StandardCharsets.UTF_8))) {
                String line = reader.readLine();
                String[] data = line.split(Constants.STRINGSEPERATOR);
                user=new User(data[1],data[2],Integer.parseInt(data[3]),Integer.parseInt(data[0]),null);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return user;
    }

    public synchronized static boolean isFile(String username) {
        File file = usernameToPath(username).toFile();
        return file.exists();
    }

    public synchronized static Integer getNextID() {

        File currentDirFile = new File(".");

        String helper = currentDirFile.getAbsolutePath();
        try {
            String currentDir = helper.substring(0, helper.length() - currentDirFile.getCanonicalPath().length());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileChannel channel = new RandomAccessFile(userIDPath.toFile(), "rw").getChannel();
             FileLock lock = channel.tryLock();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            int bufferSize = 1024;
            if (bufferSize > channel.size()) {
                bufferSize = (int) channel.size();
            }
            ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);

            while (channel.read(byteBuffer) > 0) {
                out.write(byteBuffer.array(), 0, byteBuffer.position());
                byteBuffer.clear();
            }
            Integer id = Integer.parseInt(new String(out.toByteArray(), StandardCharsets.UTF_8));
            if (id != null) {
                Integer nextID = id + 1;
                byteBuffer = ByteBuffer.wrap(nextID.toString().getBytes(StandardCharsets.UTF_8));
                channel.truncate(0);
                channel.write(byteBuffer);
                //todo test (should overwrite)
                if (lock != null) {
                    lock.release();
                }
            }
            return id;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
