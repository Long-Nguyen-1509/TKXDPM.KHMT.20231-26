package entity.cart;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import common.exception.MediaNotAvailableException;
import entity.media.Media;

public class Cart {
    
    private final List<CartMedia> listCartMedia;
    private static Cart cartInstance;

    public static Cart getCart(){
        if(cartInstance == null) cartInstance = new Cart();
        return cartInstance;
    }

    public static void setCart(Cart cart) {
        cartInstance = cart;
    }

    private Cart(){
        listCartMedia = new ArrayList<>();
    }

    public void addCartMedia(CartMedia cm){
        listCartMedia.add(cm);
    }

    public void removeCartMedia(CartMedia cm){
        listCartMedia.remove(cm);
    }

    public List<CartMedia> getListMedia(){
        return listCartMedia;
    }

    public void emptyCart(){
        listCartMedia.clear();
    }

    public int getTotalMedia(){
        int total = 0;
        for (CartMedia obj : listCartMedia) {
            total += obj.getQuantity();
        }
        return total;
    }

    public int calSubtotal(){
        int total = 0;
        for (CartMedia obj : listCartMedia) {
            total += obj.getPrice()* obj.getQuantity();
        }
        return total;
    }

    public void checkAvailabilityOfProduct() throws SQLException{
        boolean allAvai = true;
        for (CartMedia object : listCartMedia) {
            int requiredQuantity = object.getQuantity();
            int availQuantity = object.getMedia().getQuantity();

            if (requiredQuantity > availQuantity) allAvai = false;
        }
        if (!allAvai) throw new MediaNotAvailableException("Some media not available");
    }

    public CartMedia checkMediaInCart(Media media){
        for (CartMedia cartMedia : listCartMedia) {
            if (cartMedia.getMedia().getId() == media.getId()) return cartMedia;
        }
        return null;
    }

}
