package ru.terracorp.seller.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ClientBasketItem.
 */

@Document(collection = "client_basket_item")
public class ClientBasketItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("id_client_basket")
    private String idClientBasket;

    @Field("code")
    private String code;

    @Field("name")
    private String name;

    @Field("qty")
    private Double qty;

    @Field("qtycalc")
    private Double qtycalc;

    @Field("price")
    private Double price;

    @Field("imglink")
    private String imglink;

    @Field("unit")
    private String unit;

    @Field("reserv")
    private Double reserv;

    @Field("part")
    private String part;

    @Field("combo")
    private String combo;

    @Field("stock")
    private String stock;

    @Field("use_type")
    private String useType;

    @Field("info")
    private String info;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdClientBasket() {
        return idClientBasket;
    }

    public void setIdClientBasket(String idClientBasket) {
        this.idClientBasket = idClientBasket;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImglink() {
        return imglink;
    }

    public void setImglink(String imglink) {
        this.imglink = imglink;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getReserv() {
        return reserv;
    }

    public void setReserv(Double reserv) {
        this.reserv = reserv;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getCombo() {
        return combo;
    }

    public void setCombo(String combo) {
        this.combo = combo;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getUseType() {
        return useType;
    }

    public void setUseType(String useType) {
        this.useType = useType;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Double getQtycalc() {
        return qtycalc;
    }

    public void setQtycalc(Double qtycalc) {
        this.qtycalc = qtycalc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientBasketItem clientBasketItem = (ClientBasketItem) o;
        if(clientBasketItem.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, clientBasketItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ClientBasketItem{" +
            "id=" + id +
            ", idClientBasket='" + idClientBasket + "'" +
            ", code='" + code + "'" +
            ", name='" + name + "'" +
            ", qty='" + qty + "'" +
            ", qtycalc='" + qtycalc + "'" +
            ", price='" + price + "'" +
            ", imglink='" + imglink + "'" +
            ", unit='" + unit + "'" +
            ", reserv='" + reserv + "'" +
            ", part='" + part + "'" +
            ", combo='" + combo + "'" +
            ", stock='" + stock + "'" +
            ", useType='" + useType + "'" +
            ", info='" + info + "'" +
            '}';
    }
}
