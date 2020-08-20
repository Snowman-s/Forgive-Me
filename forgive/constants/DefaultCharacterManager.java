package forgive.constants;

public class DefaultCharacterManager implements CharacterManager {
    @Override
    public boolean isSeparateChar(int c){
        return c == '.' || c == '!'; 
    }

    @Override
    public boolean isSpace(int c){
        return Character.isWhitespace(c); 
    }
}