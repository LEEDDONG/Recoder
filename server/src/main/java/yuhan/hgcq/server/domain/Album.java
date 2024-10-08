package yuhan.hgcq.server.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Album {
    @Id @GeneratedValue
    @Column(name = "album_id")
    private Long id;

    private LocalDate startDate;
    private LocalDate endDate;
    private String name;
    private Boolean isDeleted;
    private LocalDate deletedAt;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    private List<Chat> chats = new ArrayList<>();

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    private List<Photo> photos = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @PrePersist
    @PreUpdate
    private void validate() {
        if (team == null) {
            throw new IllegalStateException("group is null");
        }
    }

    public Album(Team team, LocalDate startDate, LocalDate endDate, String name) {
        if (team == null) {
            throw new IllegalStateException("Team cannot be null");
        }
        this.team = team;
        this.startDate = startDate;
        this.endDate = endDate;
        this.name = name;
        this.isDeleted = false;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void changeEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void deleteAlbum() {
        isDeleted = true;

        deletedAt = LocalDate.now();
    }

    public void cancelDeleteAlbum() {
        isDeleted = false;

        deletedAt = null;
    }

    /* 테스트용(추후 삭제) */
    public void test(LocalDate date) {
        isDeleted = true;
        deletedAt = date;
    }

    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", name='" + name + '\'' +
                ", isDeleted=" + isDeleted +
                ", deletedAt=" + deletedAt +
                '}';
    }
}
