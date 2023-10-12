package com.example.cargive.common;

import com.example.cargive.domain.answer.entity.repository.AnswerRepository;
import com.example.cargive.domain.car.entity.CarRepository;
import com.example.cargive.domain.favorite.entity.repository.FavoritePkInfoRepository;
import com.example.cargive.domain.favorite.entity.repository.FavoriteRepository;
import com.example.cargive.domain.history.entity.HistoryRepository;
import com.example.cargive.domain.member.repository.MemberRepository;
import com.example.cargive.domain.notice.entity.repository.NoticeRepository;
import com.example.cargive.domain.parkingLot.entity.ParkingLotRepository;
import com.example.cargive.domain.question.entity.repository.QuestionRepository;
import com.example.cargive.domain.tag.entity.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ServiceTest {
    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected QuestionRepository questionRepository;

    @Autowired
    protected TagRepository tagRepository;

    @Autowired
    protected CarRepository carRepository;

    @Autowired
    protected FavoriteRepository favoriteRepository;

    @Autowired
    protected FavoritePkInfoRepository favoritePkInfoRepository;

    @Autowired
    protected ParkingLotRepository parkingLotRepository;

    @Autowired
    protected HistoryRepository historyRepository;

    @Autowired
    protected NoticeRepository noticeRepository;

    @Autowired
    protected AnswerRepository answerRepository;

    @BeforeEach
    void setUp() {
        databaseCleaner.execute();
    }
}