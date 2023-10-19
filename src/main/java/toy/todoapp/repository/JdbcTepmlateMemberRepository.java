package toy.todoapp.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import toy.todoapp.domain.Member;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class JdbcTepmlateMemberRepository implements MemberRepository {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcTepmlateMemberRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member")
                .usingGeneratedKeyColumns("member_id");
    }

    @Override
    public Member save(Member member) {
//        String sql = "insert into member (username, email, password) " +
//                "values (:username, :email, :password)";

        SqlParameterSource param = new BeanPropertySqlParameterSource(member);
        Number key = jdbcInsert.executeAndReturnKey(param);
//        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
//        template.update(sql, param, keyHolder);

//        Long key = keyHolder.getKey().longValue();
        member.setMemberId(key.longValue());
        return member;
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        String sql = "select member_id, username, email, password from member where member_id = :memberId";

        try {
            Map<String, Object> param = Map.of("memberId", memberId);
            Member member = template.queryForObject(sql, param, memberRowMapper());
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = "select member_id, username, email, password from member where email = :email";

        try {
            Map<String, Object> param = Map.of("email", email);
            Member member = template.queryForObject(sql, param, memberRowMapper());
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public int deleteById(Long memberid) {
        String sql = "delete from member where member_id = :memberid";
        Map<String, Object> param = Map.of("memberid", memberid);
        return template.update(sql, param);
    }

    private RowMapper<Member> memberRowMapper() {
        return BeanPropertyRowMapper.newInstance(Member.class);
    }
}
