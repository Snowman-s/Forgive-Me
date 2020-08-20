package forgive.tempfile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.EnumMap;
import java.util.Map;

public class TempFileLapper {
    private Map<TempFiles, Path> files = new EnumMap<>(TempFiles.class);

    public void createAllTempFile(){
        createAllTempFile(true);
    }

    /**
     * 基本的にデバッグ用途
     */
    public void createAllTempFile(boolean deleteOnClose){
        try{
            for(TempFiles fileEnum:TempFiles.values()){
                Path path = Files.createTempFile(fileEnum.fileName, null);
                if (deleteOnClose){
                    path.toFile().deleteOnExit();
                }
                files.put(fileEnum, path);
            }
        }catch(Exception e){
            throw new RuntimeException("一時ファイル作成に失敗しました。");
        }
    }

    /**
     * Appendモードで書き込みます
     */
    public BufferedWriter getWriter(TempFiles file){
        try{
            return Files.newBufferedWriter(files.get(file), StandardOpenOption.CREATE,
                                                            StandardOpenOption.WRITE,
                                                            StandardOpenOption.APPEND);
        }catch(Exception e){
            throw new RuntimeException("一時ファイル読み込みに失敗しました。");
        }
    }

    public BufferedReader getReader(TempFiles file){
        try{
            return Files.newBufferedReader(files.get(file));
        }catch(Exception e){
            throw new RuntimeException("一時ファイル書き込みに失敗しました。");
        }
    }

    /**
     * Appendモードで書き込みます
     */
    public OutputStream getOutputStream(TempFiles file){
        try{
            return new BufferedOutputStream(Files.newOutputStream(files.get(file),
                                            StandardOpenOption.CREATE, 
                                            StandardOpenOption.WRITE,
                                            StandardOpenOption.APPEND));
        }catch(Exception e){
            throw new RuntimeException("一時ファイル読み込みに失敗しました。");
        }
    }

    public InputStream getInputStream(TempFiles file){
        try{
            return new BufferedInputStream(Files.newInputStream(files.get(file)));
        }catch(Exception e){
            throw new RuntimeException("一時ファイル書き込みに失敗しました。");
        }
    }

    public enum TempFiles{
        SRC_FILE_SEPARATE("src_s"), OUTPUT_CLASSFILE("out")
        ;
        private final String fileName;
        TempFiles(String fileName){
            this.fileName = fileName;
        }
    }
}