package de.example.tsa.testmvp.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by Teguh Santoso on 20.11.2017.
 */
@Entity(tableName = "products")
public class Product implements Serializable{

    private static final long serialVersionUID = 2552005252883370670L;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id")
    private int itemId;

    @ColumnInfo(name = "barcode_id")
    private String barcodeId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "sum_orders")
    private int sumOfOrders;

    @ColumnInfo(name = "description")
    private String description;

    public Product(String barcodeId, String name, int sumOfOrders, String description) {
        this.barcodeId = barcodeId;
        this.name = name;
        this.sumOfOrders = sumOfOrders;
        this.description = description;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getBarcodeId() {
        return barcodeId;
    }

    public void setBarcodeId(String barcodeId) {
        this.barcodeId = barcodeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSumOfOrders() {
        return sumOfOrders;
    }

    public void setSumOfOrders(int sumOfOrders) {
        this.sumOfOrders = sumOfOrders;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
