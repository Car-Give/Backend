package com.example.cargive.common;

import com.example.cargive.domain.car.controller.CarController;
import com.example.cargive.domain.car.service.CarService;
import com.example.cargive.domain.favorite.controller.FavoriteCarController;
import com.example.cargive.domain.favorite.controller.FavoritePkGroupController;
import com.example.cargive.domain.favorite.controller.FavoritePkInfoController;
import com.example.cargive.domain.favorite.service.FavoriteCarService;
import com.example.cargive.domain.favorite.service.FavoritePkGroupService;
import com.example.cargive.domain.favorite.service.FavoritePkInfoService;
import com.example.cargive.domain.parkingLot.controller.ParkingLotController;
import com.example.cargive.domain.parkingLot.service.ParkingLotService;
import com.example.cargive.domain.question.controller.QuestionController;
import com.example.cargive.domain.question.controller.QuestionFindController;
import com.example.cargive.domain.question.service.QuestionFindService;
import com.example.cargive.domain.question.service.QuestionService;
import com.example.cargive.domain.tag.controller.TagController;
import com.example.cargive.domain.tag.service.TagCreateService;
import com.example.cargive.domain.tag.service.TagFindService;
import com.example.cargive.domain.tag.service.TagService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(value = {
        QuestionController.class,
        QuestionFindController.class,
        TagController.class,
        CarController.class,
        ParkingLotController.class,
        FavoritePkInfoController.class,
        FavoritePkGroupController.class,
        FavoriteCarController.class
})
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
public abstract class ControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected QuestionService questionService;

    @MockBean
    protected QuestionFindService questionFindService;

    @MockBean
    protected TagService tagService;

    @MockBean
    protected TagFindService tagFindService;

    @MockBean
    protected TagCreateService tagCreateService;

    @MockBean
    protected ParkingLotService parkingLotService;

    @MockBean
    protected CarService carService;

    @MockBean
    protected FavoritePkInfoService favoritePkInfoService;

    @MockBean
    protected FavoritePkGroupService favoritePkGroupService;

    @MockBean
    protected FavoriteCarService favoriteCarService;

    @BeforeEach
    void setUp(WebApplicationContext context, RestDocumentationContextProvider provider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .alwaysDo(print())
                .alwaysDo(log())
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }
}
