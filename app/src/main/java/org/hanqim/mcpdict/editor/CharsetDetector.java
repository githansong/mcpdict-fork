package org.hanqim.mcpdict.editor;

public class CharsetDetector {
    private static CharsetDetector ourInstance = new CharsetDetector();

    public static CharsetDetector getInstance() {
        return ourInstance;
    }

    public static final char FIRST_HANGUL = 0xAC00;
    public static final char LAST_HANGUL = 0xD7A3;

    public static final char FIRST_HANZI = 0x4E00;
    public static final char LAST_HANZI = 0x9FA5;

    public static final char FIRST_HIRA = 0x3041;
    public static final char LAST_HIRA = 0x309F;
    public static final char FIRST_KATA = 0x30A1;
    public static final char LAST_KATA = 0x30FF;

    private CharsetDetector() {

    }

    public boolean isChinese(char c) {
        return c >= FIRST_HANZI && c <= LAST_HANZI;
    }

    public boolean isChinese(String s) {
        int len = s.length();
        boolean detect = true;
        for(int i=0; i < len; i++) {
            if(!isChinese(s.charAt(i))) {
                detect = false;
                break;
            }
        }
        return detect;
    }

    public boolean isHangul(char c) {
        return c >= FIRST_HANGUL && c<= LAST_HANGUL;
    }

    public boolean isHangul(String s) {
        int len = s.length();
        boolean detect = true;
        for(int i=0; i < len; i++) {
            if(!isHangul(s.charAt(i))) {
                detect = false;
                break;
            }
        }
        return detect;
    }

    public boolean isKana(char c) {
        return c >= FIRST_HIRA && c <= LAST_KATA;
    }

    public boolean isKana(String s) {
        int len = s.length();
        boolean detect = true;
        for(int i=0; i < len; i++) {
            if(!isKana(s.charAt(i))) {
                detect = false;
                break;
            }
        }
        return detect;
    }

}
