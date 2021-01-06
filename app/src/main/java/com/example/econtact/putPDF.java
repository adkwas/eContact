package com.example.econtact;

public class putPDF {
    public String name;
    public String url;
    public String typeData;

    public putPDF(){
    }

    public putPDF(String name, String url, String typeData) {
        this.name = name;
        this.url = url;
        this.typeData = typeData;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTypeData() {
        return typeData;
    }

    public void setTypeData(String typeData) {
        this.typeData = typeData;
    }


}
