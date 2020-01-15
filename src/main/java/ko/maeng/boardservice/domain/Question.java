package ko.maeng.boardservice.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
public class Question extends BaseEntity{

    //Entity는 반드시 파라미터가 없는 (no-arg) public 혹은 protected 생성자가 있어야 한다.
    //하이버네이트에서 리플렉션을 통해 객체를 생성할 때 기본 생성자를 호출하게 되는데,
    //사용자가 생성자를 재정의해서 사용할 경우 해당 엔티티의 객체를 생성할 수 없게 된다.

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
    @JsonProperty
    private User writer;

    @JsonProperty
    private String title;

    //@Lob은 length가 긴 데이터가 필요할 경우 사용.
    @Lob
    @JsonProperty
    private String contents;

    @JsonProperty
    private Integer countOfAnswer = 0;

    @OneToMany(mappedBy = "question")
    @OrderBy("id DESC")
    private List<Answer> answers;

    public Question(){}

    public Question(User writer, String title, String contents) {
        this.writer = writer;
        this.title = title;
        this.contents = contents;
    }

    public void update(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public boolean isSameWriter(User loginUser) {
        return this.writer.equals(loginUser);
    }

    public void addAnswer() {
        this.countOfAnswer += 1;
    }

    public void deleteAnswer(){
        this.countOfAnswer -= 1;
    }
}
