package com.example.ex11_storageapp.http3;

import java.util.Objects;

public class ProductDTO {
    private String prodNo;
    private String prodName;
    transient private int prodPrice; //transient : 객체직렬화에서 제외
    //    private static String sv;
    public ProductDTO(){}
    public ProductDTO(String prodNo, String prodName, int prodPrice){
        this.prodNo = prodNo;
        this.prodName = prodName;
        this.prodPrice = prodPrice;
    }

    public String getProdNo() {
        return prodNo;
    }

    public void setProdNo(String prodNo) {
        this.prodNo = prodNo;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public int getProdPrice() {
        return prodPrice;
    }

    public void setProdPrice(int prodPrice) {
        this.prodPrice = prodPrice;
    }

    /**
     * 현재객체의 상품번호와 매개변수객체의 상품번호가 같으면 true를 반환
     * 그외(매개변수객체가 null인 경우,
     *     매개변수객체가 Product가 아닌경우,
     *     매개변수객체가 Product이고 상품번호가 다른경우)는 false를 반환
     * @param obj   the reference object with which to compare.
     * @return
     */
//    @Override
//    public boolean equals(Object obj) {
//        if(obj == null) return false;
//        if(!(obj instanceof Product)) return false;
//        if(obj instanceof Product){
//            if(!this.prodNo.equals(((Product) obj).getProdNo()){
//                return false;
//            }
//            return true;
//        }
//        //return super.equals(obj);
//    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ProductDTO product)) return false;
        return Objects.equals(prodNo, product.prodNo);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(prodNo);
    }
    @Override
    public String toString() {
        return "상품번호:" + prodNo + ", 상품명:" + prodName + ", 가격:" + prodPrice;
    }
}
