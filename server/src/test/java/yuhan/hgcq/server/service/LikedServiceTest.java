package yuhan.hgcq.server.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.expression.AccessException;
import org.springframework.transaction.annotation.Transactional;
import yuhan.hgcq.server.domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class LikedServiceTest {
    @Autowired
    LikedService ls;

    @Autowired
    PhotoService ps;

    @Autowired
    AlbumService as;

    @Autowired
    MemberService ms;

    @Autowired
    TeamService ts;

    Long m1Id;
    Long m2Id;
    Long m3Id;
    Long m4Id;

    Long t1Id;
    Long t2Id;

    Long a1Id;
    Long a2Id;

    Long p1Id;
    Long p2Id;

    @BeforeEach
    void setUp() {
        Member m1 = new Member("m1", "m1@test.com", "1234");
        Member m2 = new Member("m2", "m2@test.com", "1234");
        Member m3 = new Member("m3", "m3@test.com", "1234");
        Member m4 = new Member("m4", "m4@test.com", "1234");

        m1Id = ms.join(m1);
        m2Id = ms.join(m2);
        m3Id = ms.join(m3);
        m4Id = ms.join(m4);

        Team t1 = new Team(m1, "t1");
        Team t2 = new Team(m1, "t2");

        t1Id = ts.create(t1);
        t2Id = ts.create(t2);

        Album a1 = new Album(t1, LocalDate.now(), "a1", "Seoul", "test");
        Album a2 = new Album(t1, LocalDate.now(), "a2", "Seoul", "test");

        try {
            a1Id = as.create(m1, a1);
            a2Id = as.create(m1, a2);
        } catch (AccessException e) {
            fail();
        }

        Photo p1 = new Photo(a1, "p1", "/t1/a1/p1", LocalDateTime.now());
        Photo p2 = new Photo(a1, "p2", "/t1/a1/p2", LocalDateTime.now());

        p1Id = ps.create(p1);
        p2Id = ps.create(p2);
    }

    @Test
    @DisplayName("좋아요 추가")
    void add() {
        Member m1 = ms.search(m1Id);
        Photo p1 = ps.search(p1Id);

        Liked l1 = new Liked(m1, p1);
        ls.add(l1);

        Liked find = ls.search(m1, p1);

        assertThat(find).isEqualTo(l1);
        assertThat(find.getIsLiked()).isTrue();
    }

    @Test
    @DisplayName("좋아요 삭제")
    void remove() {
        Member m1 = ms.search(m1Id);
        Photo p1 = ps.search(p1Id);

        Liked l1 = new Liked(m1, p1);
        ls.add(l1);

        Liked find = ls.search(m1, p1);
        ls.remove(find);

        find = ls.search(m1, p1);
        assertThat(find.getIsLiked()).isFalse();
    }
    
    @Test
    @DisplayName("좋아요한 사진 리스트")
    void photoList() {
        Member m1 = ms.search(m1Id);
        Photo p1 = ps.search(p1Id);
        Photo p2 = ps.search(p2Id);

        Liked l1 = new Liked(m1, p1);
        Liked l2 = new Liked(m1, p2);

        ls.add(l1);
        ls.add(l2);

        List<Photo> likeList = ls.searchAll(m1);
        assertThat(likeList).hasSize(2).contains(p1, p2);
    }

}