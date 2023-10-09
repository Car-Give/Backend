package com.example.cargive.global.service;

import com.example.cargive.common.ServiceTest;
import com.example.cargive.global.base.BaseException;
import com.example.cargive.global.base.BaseResponseStatus;
import com.example.cargive.global.config.S3MockConfig;
import io.findify.s3mock.S3Mock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Import(S3MockConfig.class)
@DisplayName("S3 [Service Layer] -> S3Service 테스트")
public class S3ServiceTest extends ServiceTest {
    @Autowired
    private S3Service s3Service;

    @Autowired
    private S3Mock s3Mock;

    private final String FILE_PATH = "src/test/resources/files/";

    @AfterEach
    public void tearDown() {
        s3Mock.stop();
    }

    @Nested
    @DisplayName("파일 업로드")
    class uploadFiles {
        String DIRECTORY_NAME = "test";

        @Test
        @DisplayName("빈 파일이면 업로드에 실패한다")
        void throwExceptionByEmptyFile() {
            // given
            MultipartFile nullFile = null;
            MultipartFile emptyFile = new MockMultipartFile(DIRECTORY_NAME, "empty.png", "image/png", new byte[]{});

            // when - then
            assertThatThrownBy(() -> s3Service.upload(nullFile, DIRECTORY_NAME))
                    .isInstanceOf(BaseException.class);
            assertThatThrownBy(() -> s3Service.upload(emptyFile, DIRECTORY_NAME))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("파일 업로드에 성공한다")
        void success() throws Exception {
            // given
            String fileName = "test.png";
            String contentType = "image/png";
            MultipartFile file = createMockMultipartFile(DIRECTORY_NAME, fileName, contentType);

            // when
            String uploadFileUrl = s3Service.upload(file, DIRECTORY_NAME);

            // then
            assertThat(uploadFileUrl).contains("test.png");
            assertThat(uploadFileUrl).contains(DIRECTORY_NAME);
        }
    }

    private MultipartFile createMockMultipartFile(String dir, String fileName, String contentType) throws IOException {
        try (FileInputStream stream = new FileInputStream(FILE_PATH + fileName)) {
            return new MockMultipartFile(dir, fileName, contentType, stream);
        }
    }
}
