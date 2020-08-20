package forgive.tempfile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;

public class TempFileLapper {
    private Map<TempFiles, Path> files = new EnumMap<>(TempFiles.class);

    public void createAllTempFile(){
        try{
            for(TempFiles fileEnum:TempFiles.values()){
                Path path = Files.createTempFile(fileEnum.fileName, null);
                path.toFile().deleteOnExit();
                files.put(fileEnum, path);
            }
        }catch(Exception e){
            throw new RuntimeException("一時ファイル作成に失敗しました。");
        }
    }

    public BufferedWriter getWriter(TempFiles file) throws IOException{
        try{
            return Files.newBufferedWriter(files.get(file));
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

    public enum TempFiles{
        SRC_FILE_SEPARATE("src_s")
        ;
        private final String fileName;
        TempFiles(String fileName){
            this.fileName = fileName;
        }
    }
}