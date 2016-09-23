package org.hanqim.mcpdict.views;

public interface Masks {
    int MASK_HZ             = 0x1;
    int MASK_UNICODE        = 0x2;
    int MASK_MC             = 0x4;
    int MASK_PU             = 0x8;
    int MASK_CT             = 0x10;
    int MASK_SH             = 0x20;
    int MASK_MN             = 0x40;
    int MASK_KR             = 0x80;
    int MASK_VN             = 0x100;
    int MASK_JP_GO          = 0x200;
    int MASK_JP_KAN         = 0x400;
    int MASK_JP_TOU         = 0x800;
    int MASK_JP_KWAN        = 0x1000;
    int MASK_JP_OTHER       = 0x2000;
    int MASK_JP_ALL         = MASK_JP_GO | MASK_JP_KAN | MASK_JP_TOU | MASK_JP_KWAN | MASK_JP_OTHER;
    int MASK_ALL_READINGS   = MASK_MC | MASK_PU | MASK_CT | MASK_KR | MASK_VN | MASK_JP_ALL;
    int MASK_FAVORITE       = 0x4000;
}
