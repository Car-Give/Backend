package com.example.cargive.common;

import com.example.cargive.domain.member.repository.MemberRepository;
import com.example.cargive.domain.question.entity.repository.QuestionRepository;
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

    @BeforeEach
    void setUp() {
        databaseCleaner.execute();
    }
}