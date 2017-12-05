package com.example.demo.game;

/**
 * Created by gargg on 25/10/17.
 */
public class InputEntity {

    private int price;

    private String name;

    private String endDate;

    private String startDate;

    private String category;

    public int getPrice ()
    {
        return price;
    }

    public void setPrice (int price)
    {
        this.price = price;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
