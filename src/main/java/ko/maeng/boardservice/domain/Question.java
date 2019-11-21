package ko.maeng.boardservice.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
public class Question{

    //Entity는 반드시 파라미터가 없는 (no-arg) public 혹은 protected 생성자가 있어야 한다.
    //하이버네이트에서 리플렉션을 통해 객체를 생성할 때 기본 생성자를 호출하게 되는데,
    //사용자가 생성자를 재정의해서 사용할 경우 해당 엔티티의 객체를 생성할 수 없게 된다.

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    private String title;

    //@Lob은 length가 긴 데이터가 필요할 경우 사용.
    @Lob
    private String contents;

    private LocalDateTime createDate;

    @OneToMany(mappedBy = "question")
    @OrderBy("id ASC")
    private List<Answer> answers;

    public Question(){}

    public Question(User writer, String title, String contents) {
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.createDate = LocalDateTime.now();
    }

    public String getFormattedCreateDate(){
        if(createDate == null){
            return "";
        }
        return createDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    }


    public void update(String title, String contents) {
        this.title = title;
        this.contents = contents;
        this.createDate = LocalDateTime.now();
    }

    public boolean isSameWriter(User loginUser) {
        return this.writer.equals(loginUser);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result +((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        Question other = (Question) obj;
        if(id == null){
            if(other.id != null)
                return false;
        } else if(!id.equals(other.id))
            return false;
        return true;
    }
}
