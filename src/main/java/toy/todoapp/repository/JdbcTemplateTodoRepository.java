package toy.todoapp.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import toy.todoapp.domain.Todo;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class JdbcTemplateTodoRepository implements TodoRepository {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcTemplateTodoRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("todo")
                .usingGeneratedKeyColumns("todo_id")
                .usingColumns("member_id", "content", "status");

    }

    @Override
    public Todo save(Todo todo) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(todo);
        Number key = jdbcInsert.executeAndReturnKey(param);
        todo.setTodoId(key.longValue());
        return todo;
    }

    @Override
    public Optional<Todo> findById(Long todoId) {
        String sql = "select todo_id, member_id, content, status, created_at from todo where todo_id = :todoId";

        try {
            Map<String, Object> param = Map.of("todoId", todoId);
            Todo todo = template.queryForObject(sql, param, todoRowMapper());
            return Optional.of(todo);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Todo> findAll(Long memberId) {
        String sql = "select todo_id, member_id, content, status, created_at from todo where member_id = :memberId";
        Map<String, Object> param = Map.of("memberId", memberId);
        List<Todo> todos = template.query(sql, param, todoRowMapper());
        return todos;
    }

    @Override
    public int update(Long todoId, TodoUpdateDto updateDto) {
        String sql = "update todo " +
                "set content=:content, status=:status " +
                "where todo_id=:todoId";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("content", updateDto.getContent())
                .addValue("status", updateDto.getStatus().toString())
                .addValue("todoId", todoId);

        int updatedRowCnt = template.update(sql, param);
        return updatedRowCnt;

    }

    @Override
    public int deleteById(Long todoId) {
        String sql = "delete from todo where todo_id = :todoId";
        Map<String, Object> param = Map.of("todoId", todoId);
        return template.update(sql, param);
    }

    private RowMapper<Todo> todoRowMapper() {
        return BeanPropertyRowMapper.newInstance(Todo.class);
    }
}
