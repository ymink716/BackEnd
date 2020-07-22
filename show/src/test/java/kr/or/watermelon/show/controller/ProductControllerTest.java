package kr.or.watermelon.show.controller;

import kr.or.watermelon.show.entity.Category;
import kr.or.watermelon.show.entity.Product;
import kr.or.watermelon.show.factory.ProductFactory;
import kr.or.watermelon.show.infra.AbstractContainerBaseTest;
import kr.or.watermelon.show.infra.MockMvcTest;
import org.hamcrest.core.Every;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.function.Function;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcTest
public class ProductControllerTest extends AbstractContainerBaseTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ProductFactory productFactory;

    @DisplayName("키워드 공연 검색")
    @Test
    void searchProductsByKeyword() throws Exception {
        Function<String, Product> titleProductMaker = (s) -> Product.builder().title(s).build();
        productFactory.saveProduct(titleProductMaker, "iu-concert1");
        productFactory.saveProduct(titleProductMaker, "iu-concert2");
        productFactory.saveProduct(titleProductMaker, "cl-concert");

        mockMvc.perform(get("/products/search?keyword=iu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$..title", Every.everyItem(containsString("iu"))));
    }

    @DisplayName("카테고리 공연 검색")
    @Test
    void searchProductsByCategory() throws Exception {
        Function<Category, Product> categoryProductMaker = (c) -> Product.builder().category(c).build();
        productFactory.saveProduct(categoryProductMaker, Category.CONCERT);
        productFactory.saveProduct(categoryProductMaker, Category.CONCERT);
        productFactory.saveProduct(categoryProductMaker, Category.PLAY);

        mockMvc.perform(get("/products/search?category=concert"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));
    }
}
