package com.goldenringstu.note.Entities;


public class Theme {
    private int THEME_CODE;
    private String title, desc;
    private int imageRes;
    private boolean checked = false;

    public Theme(int THEME_CODE, String title, String desc, int imageRes) {
        this.THEME_CODE = THEME_CODE;
        this.title = title;
        this.desc = desc;
        this.imageRes = imageRes;
    }

    public int getTHEME_CODE() {
        return THEME_CODE;
    }

    public void setTHEME_CODE(int THEME_CODE) {
        this.THEME_CODE = THEME_CODE;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}