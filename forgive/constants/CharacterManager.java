package forgive.constants;

/**
 * コンパイラが識別する"文字"を管理するクラス。
 */
public interface CharacterManager {
    boolean isSpace(int c);
    boolean isSeparateChar(int c);
}