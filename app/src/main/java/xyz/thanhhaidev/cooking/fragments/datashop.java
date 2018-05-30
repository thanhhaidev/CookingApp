package xyz.thanhhaidev.cooking.fragments;

/**
 * Created by ThanhHaiDev on 30-May-18.
 */
public class datashop {
    private int hinhanh;
    private String ten;

    public datashop(int hinhanh, String ten) {
        this.hinhanh = hinhanh;
        this.ten = ten;
    }

    public int getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(int hinhanh) {
        this.hinhanh = hinhanh;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }
}
