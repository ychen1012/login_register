package diary.beautiful.entity;

import io.requery.*;

import java.sql.Timestamp;

@Entity
@Table(name = "user")
public abstract class AbstractUser {
    @Key
    @Generated
    @Column(name = "user_id")
    private int userId;
    @Column(name = "User_name")
    private String userName;
    @Column(name = "password")
    private StringBuilder password;
    @Column(name = "create_time")
    private Timestamp createTime;
    @Column(name = "email")
    private String email;
    @Column(name = "storage")
    private int storage;
    @Column(name = "diary_password")
    private int diaryPassword;
}
