package de.example.tsa.testmvp.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import de.example.tsa.testmvp.entities.Product;

/**
 * Created by Teguh Santoso on 20.11.2017.
 */
@Dao
public interface ProductDAO {

    @Query("SELECT * FROM products")
    List<Product> getAll();

    @Query("SELECT * FROM products WHERE name LIKE :name")
    List<Product> getAllByName(String name);

    @Insert
    long insertProduct(Product product);

    @Update
    int update(Product product);

    @Delete
    void delete(Product product);

    @Delete
    void deleteAll(Product... products);

}
