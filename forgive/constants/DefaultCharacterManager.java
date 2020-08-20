package forgive.constants;

public class DefaultCharacterManager implements CharacterManager {
    public boolean isSeparateChar(int c){
        return c == '.' || c == '!'; 
    }
}