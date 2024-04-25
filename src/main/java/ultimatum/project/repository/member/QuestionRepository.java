package ultimatum.project.repository.member;


import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.question.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}