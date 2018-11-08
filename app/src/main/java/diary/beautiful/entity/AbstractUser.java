package diary.beautiful.entity;

import io.requery.*;

import java.sql.Timestamp;

@Entity
@Table(name = "user")
public abstract class AbstractUser implements Persistable {
    @Key
    @Generated
    @Column(name = "user_id")
    int userId;
    @Column(name = "User_name")
    String userName;
    @Column(name = "password")
    StringBuilder password;
    @Column(name = "create_time")
    Timestamp createTime;
    @Column(name = "email")
    String email;
    @Column(name = "storage")
    int storage;
    @Column(name = "diary_password")
    int diaryPassword;
}
