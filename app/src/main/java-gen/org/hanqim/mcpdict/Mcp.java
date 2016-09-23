package org.hanqim.mcpdict;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "MCP".
 */
public class Mcp {

    private Long id;
    /** Not-null value. */
    private String unicode;
    private String mc;
    private String pu;
    private String ct;
    private String sh;
    private String mn;
    private String vn;
    private String jp_go;
    private String jp_kan;
    private String jp_tou;
    private String jp_kwan;
    private String jp_other;

    public Mcp() {
    }

    public Mcp(Long id) {
        this.id = id;
    }

    public Mcp(Long id, String unicode, String mc, String pu, String ct, String sh, String mn, String vn, String jp_go, String jp_kan, String jp_tou, String jp_kwan, String jp_other) {
        this.id = id;
        this.unicode = unicode;
        this.mc = mc;
        this.pu = pu;
        this.ct = ct;
        this.sh = sh;
        this.mn = mn;
        this.vn = vn;
        this.jp_go = jp_go;
        this.jp_kan = jp_kan;
        this.jp_tou = jp_tou;
        this.jp_kwan = jp_kwan;
        this.jp_other = jp_other;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getUnicode() {
        return unicode;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUnicode(String unicode) {
        this.unicode = unicode;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public String getPu() {
        return pu;
    }

    public void setPu(String pu) {
        this.pu = pu;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public String getSh() {
        return sh;
    }

    public void setSh(String sh) {
        this.sh = sh;
    }

    public String getMn() {
        return mn;
    }

    public void setMn(String mn) {
        this.mn = mn;
    }

    public String getVn() {
        return vn;
    }

    public void setVn(String vn) {
        this.vn = vn;
    }

    public String getJp_go() {
        return jp_go;
    }

    public void setJp_go(String jp_go) {
        this.jp_go = jp_go;
    }

    public String getJp_kan() {
        return jp_kan;
    }

    public void setJp_kan(String jp_kan) {
        this.jp_kan = jp_kan;
    }

    public String getJp_tou() {
        return jp_tou;
    }

    public void setJp_tou(String jp_tou) {
        this.jp_tou = jp_tou;
    }

    public String getJp_kwan() {
        return jp_kwan;
    }

    public void setJp_kwan(String jp_kwan) {
        this.jp_kwan = jp_kwan;
    }

    public String getJp_other() {
        return jp_other;
    }

    public void setJp_other(String jp_other) {
        this.jp_other = jp_other;
    }

}
