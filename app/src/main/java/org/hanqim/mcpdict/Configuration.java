package org.hanqim.mcpdict;


import android.content.SharedPreferences;

public class Configuration {
    private static final SharedPreferences sharedPreferences = MCPDictApplication.getSharedPreferences();
    private static final String DEFAULT_VALUE = "0";
    private static final int DEFAULT_VALUE_INT = 0;

    private static final Configuration instance = new Configuration();

    private Configuration() {
        //
    }

    public static Configuration getInstance() {
        return instance;
    }

    private int getStyle(String key) {
        int pref;
        try {
            pref = Integer.parseInt(sharedPreferences.getString(key, DEFAULT_VALUE));
        } catch (NumberFormatException e) {
            pref = DEFAULT_VALUE_INT;
        }
        return pref;
    }

    private String getStr(int id) {
        return MCPDictApplication.getStr(id);
    }

    private int getStyleById(int id) {
        return getStyle(getStr(id));
    }

    public int getMandarinStyle() {
        return getStyleById(R.string.pref_key_mandarin_display);
    }

    public int getCantoneseStyle() {
        return getStyleById(R.string.pref_key_cantonese_romanization);
    }

    public int getKoareanStyle() {
        return getStyleById(R.string.pref_key_korean_display);
    }

    public int getJapaneseStyle() {
        return getStyleById(R.string.pref_key_japanese_display);
    }

    public int getVietnameseStyle() {
        return getStyleById(R.string.pref_key_vietnamese_tone_position);
    }
    public boolean getKuangYonOnly() {
        return sharedPreferences.getBoolean(MCPDictApplication.getStr(R.string.pref_key_kuangx_yonh_only), false);

    }
    public boolean getAllowVariants() {
       return sharedPreferences.getBoolean(MCPDictApplication.getStr(R.string.pref_key_allow_variants), true);

    }
    public boolean getToneInsensitive() {
       return sharedPreferences.getBoolean(MCPDictApplication.getStr(R.string.pref_key_tone_insensitive), false);
    }
}