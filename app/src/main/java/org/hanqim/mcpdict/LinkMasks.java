package org.hanqim.mcpdict;

import android.annotation.SuppressLint;

import org.hanqim.mcpdict.views.Masks;
import java.util.HashMap;
import java.util.Map;

public class LinkMasks {

    @SuppressLint("UseSparseArrays")
    private static final Map<Integer, Integer> COPY_MENU_ITEM_TO_MASK = new HashMap<Integer, Integer>();
    static {
        COPY_MENU_ITEM_TO_MASK.put(R.id.menu_item_copy_hz,       Masks.MASK_HZ);
        COPY_MENU_ITEM_TO_MASK.put(R.id.menu_item_copy_unicode,  Masks.MASK_UNICODE);
        COPY_MENU_ITEM_TO_MASK.put(R.id.menu_item_copy_all,      Masks.MASK_ALL_READINGS);
        COPY_MENU_ITEM_TO_MASK.put(R.id.menu_item_copy_mc,       Masks.MASK_MC);
        COPY_MENU_ITEM_TO_MASK.put(R.id.menu_item_copy_pu,       Masks.MASK_PU);
        COPY_MENU_ITEM_TO_MASK.put(R.id.menu_item_copy_ct,       Masks.MASK_CT);
        COPY_MENU_ITEM_TO_MASK.put(R.id.menu_item_copy_kr,       Masks.MASK_KR);
        COPY_MENU_ITEM_TO_MASK.put(R.id.menu_item_copy_vn,       Masks.MASK_VN);
        COPY_MENU_ITEM_TO_MASK.put(R.id.menu_item_copy_jp_all,   Masks.MASK_JP_ALL);
        COPY_MENU_ITEM_TO_MASK.put(R.id.menu_item_copy_jp_go,    Masks.MASK_JP_GO);
        COPY_MENU_ITEM_TO_MASK.put(R.id.menu_item_copy_jp_kan,   Masks.MASK_JP_KAN);
        COPY_MENU_ITEM_TO_MASK.put(R.id.menu_item_copy_jp_tou,   Masks.MASK_JP_TOU);
        COPY_MENU_ITEM_TO_MASK.put(R.id.menu_item_copy_jp_kwan,  Masks.MASK_JP_KWAN);
        COPY_MENU_ITEM_TO_MASK.put(R.id.menu_item_copy_jp_other, Masks.MASK_JP_OTHER);
    }

    private static final int[] DICT_LINK_MASKS = {
            Masks.MASK_MC, Masks.MASK_PU, Masks.MASK_CT, Masks.MASK_KR, Masks.MASK_VN
    };

    private static final String[] DICT_LINK_BASES = {
            "http://ytenx.org/zim?kyonh=1&dzih=",                               // plus UTF-8 encoded string
            "http://www.zdic.net/sousuo/?q=",                                   // plus UTF-8 encoded string
            "http://humanum.arts.cuhk.edu.hk/Lexis/lexi-can/search.php?q=",     // plus Big5 encoded string
            "http://hanja.naver.com/hanja?q=",                                  // plus UTF-8 encoded string
            "http://hanviet.org/hv_timchu.php?unichar=",                        // plus UTF-8 encoded string
    };  // Bases of links to external dictionaries

}
