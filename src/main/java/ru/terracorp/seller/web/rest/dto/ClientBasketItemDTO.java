package ru.terracorp.seller.web.rest.dto;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the ClientBasketItem entity.
 */
public class ClientBasketItemDTO implements Serializable {

    private String id;

    private String idClientBasket;


    private String code;


    private String name;


    private Double qty;


    private Double price;


    private String imglink;


    private String unit;


    private Double reserv;


    private String part;


    private String combo;


    private String stock;


    private String useType;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClientBasketItemDTO clientBasketItemDTO = (ClientBasketItemDTO) o;

        if ( ! Objects.equals(id, clientBasketItemDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ClientBasketItemDTO{" +
            "id=" + id +
            ", idClientBasket='" + idClientBasket + "'" +
            ", code='" + code + "'" +
            ", name='" + name + "'" +
            ", qty='" + qty + "'" +
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
