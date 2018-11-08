package diary.beautiful.entity;

import io.requery.*;

import java.sql.Timestamp;

@Entity
@Table(name = "user_diary")
public abstract class AbstractDiary implements Persistable {
    @Key
    @Generated
    @Column(name = "diary_id")
    int diaryId;
    @Column(name = "user_id")
    int userId;
    @Column(name = "diary_title")
    String diaryTitle;
    @Column(name = "diary_path")
    String path;
    @Column(name = "diary_emotion")
    String diaryEmotion;
    @Column(name = "diary_tag")
    String diaryTag;
    @Column(name = "is_delete")
    boolean isDelete;
    @Column(name = "is_encrypt")
    boolean isEncrypt;
    @Column(name = "sync_time")
    Timestamp syncTime;
    @Column(name = "diary_create_time")
    Timestamp diaryCreateTime;
    @Column(name = "diary_last_edit_time")
    Timestamp diaryLastEditTime;
}
