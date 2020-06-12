package sw2.lab6.teletok.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

@Entity
@Table(name="user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    @NotBlank
    @Size(min=3, max = 45, message = "Debe contener entre 3 y 45 caracteres")
    private String fullname;
    @Column(nullable = false)
    @NotBlank
    @Size(min=3, max = 45, message = "Debe contener entre 3 y 45 caracteres")
    private String username;
    @Column(nullable = false)
    @NotBlank
    @Size(min=3, max = 500, message = "Debe contener entre 3 y 45 caracteres")
    private String password;
    private int enable;
    @Column(name = "role_id", nullable = false)
    private int roleId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}
