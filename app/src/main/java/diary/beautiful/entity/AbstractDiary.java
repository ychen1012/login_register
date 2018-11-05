package diary.beautiful.entity;

import io.requery.*;

import java.sql.Timestamp;

@Entity
@Table(name = "user_diary")
public abstract class AbstractDiary implements Persistable {
    @Key
    @Generated
    @Column(name = "diary_id")
    private int diaryId;
    @Column(name = "user_id")
    private int userId;
    @Column(name = "diary_title")
    private String diaryTitle;
    @Column(name = "diary_path")
    private String path;
    @Column(name = "diary_emotion")
    private String diaryEmotion;
    @Column(name = "diary_tag")
    private String diaryTag;
    @Column(name = "is_delete")
    private boolean isDelete;
    @Column(name = "is_encrypt")
    private boolean isEncrypt;
    @Column(name = "sync_time")
    private Timestamp syncTime;
    @Column(name = "diary_create_time")
    private Timestamp diaryCreateTime;
    @Column(name = "diary_last_edit_time")
    private Timestamp diaryLastEditTime;
}
