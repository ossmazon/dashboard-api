package com.mazon.dashboard_api.dto;

import com.mazon.dashboard_api.model.User;

public class UserDTO {

    private final Long id;
    private final String name;
    private final String email;
    private final String phone;

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
