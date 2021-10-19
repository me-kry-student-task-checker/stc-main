package hu.me.iit.malus.thesis.email;

import com.google.common.base.Predicate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.SpringApplication;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceApplicationTest {

    @Test
    public void main() {
        String[] test_args = {"1", "2"};

        try(MockedStatic<SpringApplication> spring = mockStatic(SpringApplication.class)) {
            EmailServiceApplication.main(test_args);

            spring.verify(
                () -> SpringApplication.run(EmailServiceApplication.class, test_args),
                times(1)
            );
        }
    }

    @Test
    public void swaggerApiValidResult() {
        EmailServiceApplication app = new EmailServiceApplication();

        Docket result = app.swaggerApi();
        assertNotNull(result);
    }

    @Test
    public void swaggerApiBuild() {
        try(MockedConstruction<Docket> docket = mockConstruction(Docket.class, (mock, context) -> {
            ApiSelectorBuilder select = mock(ApiSelectorBuilder.class);

            when(mock.select()).thenReturn(select);
            when(select.apis(any())).thenReturn(select);
            when(select.paths(any())).thenReturn(select);
            when(select.build()).thenReturn(mock);
        })) {
            Predicate<RequestHandler> basePackage = RequestHandlerSelectors.basePackage("hu.test");
            try(MockedStatic<RequestHandlerSelectors> requestHandlerSelectors = mockStatic(RequestHandlerSelectors.class)) {
                requestHandlerSelectors.when(() -> RequestHandlerSelectors.basePackage(anyString())).thenReturn(basePackage);

                ApiInfo apiInfo = mock(ApiInfo.class);
                try(MockedConstruction<ApiInfoBuilder> apiInfoBuilder = mockConstruction(ApiInfoBuilder.class, (mock, context) -> {
                    when(mock.version(any())).thenReturn(mock);
                    when(mock.title(any())).thenReturn(mock);
                    when(mock.description(any())).thenReturn(mock);
                    when(mock.build()).thenReturn(apiInfo);
                })) {
                    EmailServiceApplication app = new EmailServiceApplication();
                    app.swaggerApi();

                    assertEquals(1, docket.constructed().size());
                    verify(docket.constructed().get(0), times(1)).select();
                    requestHandlerSelectors.verify(() -> RequestHandlerSelectors.basePackage("hu.me.iit.malus.thesis.email"), times(1));
                    verify(docket.constructed().get(0).select(), times(1)).apis(basePackage);
                    verify(docket.constructed().get(0).select(), times(1)).paths(PathSelectors.any());
                    verify(docket.constructed().get(0).select(), times(1)).build();

                    assertEquals(1, apiInfoBuilder.constructed().size());
                    verify(apiInfoBuilder.constructed().get(0), times(1)).version("1.0");
                    verify(apiInfoBuilder.constructed().get(0), times(1)).title("Email-service API");
                    verify(apiInfoBuilder.constructed().get(0), times(1)).description("Email service API v1.0");
                    verify(apiInfoBuilder.constructed().get(0), times(1)).build();

                    verify(docket.constructed().get(0), times(1)).apiInfo(apiInfo);
                }
            }
        };
    }

}