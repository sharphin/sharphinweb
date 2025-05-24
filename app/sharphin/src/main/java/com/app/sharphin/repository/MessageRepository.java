package com.app.sharphin.repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.app.sharphin.dto.message.MessageDto;

@Repository
public class MessageRepository {
    @Autowired
    NamedParameterJdbcTemplate npjdbc;
    private final RowMapper<MessageDto> MESSAGE_LIST_MAPPER = (rs, row) -> new MessageDto (
        rs.getString("chatroom_id"),
        rs.getString("recieve_user_id"),
        rs.getString("main_message"),
        asLocalDateTime(rs.getTimestamp("create_at")));

    public List<MessageDto> getMessageHistory(String chatroom_id_id) {
        List<MessageDto> user_list;
        try {
            SqlParameterSource param = new MapSqlParameterSource().addValue("chatroom_id", chatroom_id_id);
            StringBuilder sql = new StringBuilder();
            sql.append("select * from public.message where chatroom_id = :chatroom_id");
            user_list = npjdbc.query(sql.toString(),param,MESSAGE_LIST_MAPPER);  
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return user_list;
    }
    public int addMessage(MessageDto message) {
        int result;
        StringBuilder sql = new StringBuilder("INSERT INTO public.message");
        sql.append("(");
        sql.append("chatroom_id,");
        sql.append("recieve_user_id,");
        sql.append("main_message,");
        sql.append("create_at");
        sql.append(") VALUES (");
        sql.append(":chatroom_id,");
        sql.append(":recieve_user_id,");
        sql.append(":main_message,");
        sql.append(":create_at");
        sql.append(")");

        String str = sql.toString();
        SqlParameterSource params = params(message);
        result = npjdbc.update(str,params);
        return result;
    }
    private SqlParameterSource params(MessageDto message) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("chatroom_id", message.chatroom_id())
                .addValue("recieve_user_id", message.recieve_user_id())
                .addValue("main_message", message.main_message())
                .addValue("create_at", message.create_at());
        return params;
    }
    private static LocalDateTime asLocalDateTime(Date value) {
		if (value == null) return null;
		return value.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}
}
