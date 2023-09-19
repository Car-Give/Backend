package com.example.cargive.common;

import com.example.cargive.global.config.JpaAuditingConfig;
import com.example.cargive.global.config.QueryDslConfig;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import({QueryDslConfig.class, JpaAuditingConfig.class})
@ActiveProfiles("test")
public class RepositoryTest {
}