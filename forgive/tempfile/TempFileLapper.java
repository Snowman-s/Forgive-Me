package forgive.tempfile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class TempFileLapper {
    private Map<TempFileKey, Path> files = new HashMap<>();

    public void createTempFiles(Set<TempFileKey> keys){
        createTempFiles(keys, true);
    }

    /**
     * 基本的にデバッグ用途
     */
    public void createTempFiles(Set<TempFileKey> keys, boolean deleteOnClose){
        try{
            for(TempFileKey key:keys){
                Path path = Files.createTempFile(key.identifier + key.tempFiles.fileName, null);
                if (deleteOnClose){
                    path.toFile().deleteOnExit();
                }
                files.put(key, path);
            }
        }catch(Exception e){
            throw new RuntimeException("一時ファイル作成に失敗しました。");
        }
    }

    /**
     * Appendモードで書き込みます
     */
    public BufferedWriter getWriter(TempFileKey file){
        try{
            return Files.newBufferedWriter(files.get(file), StandardOpenOption.CREATE,
                                                            StandardOpenOption.WRITE,
                                                            StandardOpenOption.APPEND);
        }catch(Exception e){
            throw new RuntimeException("一時ファイル読み込みに失敗しました。");
        }
    }

    public BufferedReader getReader(TempFileKey file){
        try{
            return Files.newBufferedReader(files.get(file));
        }catch(Exception e){
            throw new RuntimeException("一時ファイル書き込みに失敗しました。");
        }
    }

    /**
     * Appendモードで書き込みます
     */
    public OutputStream getOutputStream(TempFileKey file){
        try{
            return new BufferedOutputStream(Files.newOutputStream(files.get(file),
                                            StandardOpenOption.CREATE, 
                                            StandardOpenOption.WRITE,
                                            StandardOpenOption.APPEND));
        } catch(Exception e) {
            throw new RuntimeException("一時ファイル読み込みに失敗しました。");
        }
    }

    public InputStream getInputStream(TempFileKey file){
        try{
            return new BufferedInputStream(Files.newInputStream(files.get(file)));
        }catch(Exception e){
            throw new RuntimeException("一時ファイル書き込みに失敗しました。");
        }
    }

    public SeekableByteChannel getSeekableByteChannel(TempFileKey file){
        try{
            return Files.newByteChannel(
                files.get(file),
                StandardOpenOption.CREATE, 
                StandardOpenOption.WRITE);
        } catch(Exception e) {
            throw new RuntimeException("一時ファイル読み込みに失敗しました。");
        }
    }

    public enum TempFiles{
        SRC_FILE_SEPARATE("src_s"), OUTPUT_CLASSFILE("out"),
        RUNTIME_CONSTANT_MEMO("run"),
        OPECODE_MEMO("ope"), METHOD_MEMO("met"), STACKMAP_TABLE_MEMO("sta")
        ;
        private final String fileName;
        TempFiles(String fileName){
            this.fileName = fileName;
        }
    }

    public static class TempFileKey {
        private TempFileKey(TempFiles tempFiles, int identifer){
            this.tempFiles = tempFiles;
            this.identifier = identifer;
        }

        private final TempFiles tempFiles;
        private final int identifier;

        public static TempFileKey of(TempFiles tempFiles, int identifer){
            return new TempFileKey(tempFiles, identifer);
        }
        
        public static TempFileKey of(TempFiles tempFiles){
            return new TempFileKey(tempFiles, 0);
        }

        @Override
        public int hashCode() {
            return Objects.hash(tempFiles, identifier);
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof TempFileKey)) return false;

            TempFileKey castObj = (TempFileKey)obj;

            return castObj.identifier == this.identifier && castObj.tempFiles == this.tempFiles;
        }
    }
}