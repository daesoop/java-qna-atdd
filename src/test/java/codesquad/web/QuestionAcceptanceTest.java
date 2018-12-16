package codesquad.web;

import codesquad.domain.User;
import codesquad.service.QnaService;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import support.test.AcceptanceTest;
import support.test.HtmlFormDataBuilder;

import static org.slf4j.LoggerFactory.getLogger;

public class QuestionAcceptanceTest extends AcceptanceTest {
    private static final Logger log = getLogger(QuestionAcceptanceTest.class);

    @Autowired
    private QnaService qnaService;

    @Test
    public void 질문생성테스트_로그인안됨() {
        ResponseEntity<String> response = template().getForEntity("/questions", String.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void 질문생성테스트_로그인됨() throws Exception{
        ResponseEntity<String> response = basicAuthTemplate().getForEntity("/questions", String.class);
//        softly.assertThat(response.getHeaders().getLocation().getPath()).startsWith("/questions");
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void 질문생성() throws Exception{
        User loginUser = defaultUser();
        HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder.urlEncodedForm()
                .addParameter("title", "please please")
                .addParameter("contents", "please")
                .build();

        ResponseEntity<String> response = basicAuthTemplate(loginUser).postForEntity("/questions", request, String.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        softly.assertThat(qnaService.findById(loginUser.getId())).isNotEmpty();
        softly.assertThat(response.getHeaders().getLocation().getPath()).startsWith("/");
    }

    @Test
    public void 질문수정테스트() throws Exception{

    }

    @Test
    public void 질문삭제테스트_로그인안됨_아이디다를때() throws Exception{
        User loginUser = defaultUser();
        ResponseEntity<String> response = basicAuthTemplate(loginUser).getForEntity("/questions/1", String.class);
        log.debug("body : {}", response.getBody());
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void 질문삭제테스트_로그인됨() throws Exception{
        ResponseEntity<String> response = template().getForEntity("/questions/1", String.class);
        HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder.urlEncodedForm()
                .delete()
                .build();
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
    }

}
