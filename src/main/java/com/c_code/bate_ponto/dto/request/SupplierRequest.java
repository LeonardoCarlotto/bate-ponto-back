package com.c_code.bate_ponto.dto.request;

import lombok.Data;

@Data
public class SupplierRequest {
    private String name;
    private String cnpj;
    private String email;
    private String phone;
    private String stateRegistration;

    // Getters e Setters manuais
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getStateRegistration() { return stateRegistration; }
    public void setStateRegistration(String stateRegistration) { this.stateRegistration = stateRegistration; }
}
