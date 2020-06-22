package sw2.lab6.teletok.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name="post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    @NotBlank
    @Size(min=3, max = 45, message = "Debe contener entre 3 y 45 caracteres")
    private String description;
    @Column(name = "creation_date", nullable = false)
    private Date creationDate;
    @Column(name = "media_url", nullable = false)
    private String mediaUrl;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(targetEntity=PostComment.class, mappedBy="post",cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("id ASC")
    private List<PostComment> comments = new ArrayList<>();
    @OneToMany(targetEntity=PostLike.class, mappedBy="post",cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostLike> likes = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<PostComment> getComments() {
        return comments;
    }

    public void setComments(List<PostComment> comments) {
        this.comments = comments;
    }

    public List<PostLike> getLikes() {
        return likes;
    }

    public void setLikes(List<PostLike> likes) {
        this.likes = likes;
    }

    public String getCreationDateDiff() {
        Date startDate = creationDate;
        Date endDate   = new Date();
        long duration  = endDate.getTime() - startDate.getTime();
        long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
        long diffInDays = TimeUnit.MILLISECONDS.toDays(duration);
        if (diffInSeconds < 60){
            return diffInSeconds + " segundos";
        } else if (diffInMinutes < 60){
            return diffInMinutes + " minutos";
        } else if (diffInHours < 24){
            return diffInHours + " horas";
        } else {
            return diffInDays + " días";
        }
    }
}
