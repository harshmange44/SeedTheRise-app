package com.hrsh.seedtherise;

public class InstanceListByNameModel {

    private String inst_name;
    private String inst_logo;

    // Constructor
    public InstanceListByNameModel(String grp_name, String grp_logo) {
        this.inst_name = grp_name;
        this.inst_logo = grp_logo;
    }

    // Getter and Setter
    public String getInstName() {
        return inst_name;
    }

    public void setInstName(String inst_name) {
        this.inst_name = inst_name;
    }

    public String getInstLogo() {
        return inst_logo;
    }

    public void setInstLogo(String inst_logo) {
        this.inst_logo = inst_logo;
    }
}

