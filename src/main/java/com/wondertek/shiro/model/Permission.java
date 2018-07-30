package com.wondertek.shiro.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "t_permission")
public class Permission implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "permission_name")
    private String permissionName;


    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;// 一个权限对应一个角色

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", permissionName='" + permissionName + '\'' +
                ", role=" + role +
                '}';
    }
}
