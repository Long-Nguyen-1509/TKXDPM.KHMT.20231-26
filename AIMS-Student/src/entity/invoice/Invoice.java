package entity.invoice;

import entity.order.Order;

import java.util.Objects;

public class Invoice {

    private Order order;
    private int amount;
    
    public Invoice(){

    }

    public Invoice(Order order){
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void saveInvoice(){
        
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invoice invoice = (Invoice) o;
        return amount == invoice.amount &&
                Objects.equals(order, invoice.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, amount);
    }
}
