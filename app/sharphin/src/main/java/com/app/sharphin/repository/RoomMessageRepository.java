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

import com.app.sharphin.dto.room.RoomMessageDto;

@Repository
public class RoomMessageRepository {
    @Autowired
    NamedParameterJdbcTemplate npjdbc;
    private final RowMapper<RoomMessageDto> ROOM_LIST_MAPPER = (rs, row) -> new RoomMessageDto (
        rs.getString("room_id"),
        rs.getString("send_user_id"),
        rs.getString("main_message"),
        asLocalDateTime(rs.getTimestamp("create_at")));

    public List<RoomMessageDto> getMessageHistory(String room_id) {
        List<RoomMessageDto> user_list;
        try {
            SqlParameterSource param = new MapSqlParameterSource().addValue("chatroom_id", room_id);
            StringBuilder sql = new StringBuilder();
            sql.append("select * from public.room_message where chatroom_id = :chatroom_id order by create_at");
            user_list = npjdbc.query(sql.toString(),param,ROOM_LIST_MAPPER);  
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return user_list;
    }
    public int addRoomMessage(RoomMessageDto message) {
        int result;
        StringBuilder sql = new StringBuilder("INSERT INTO public.room_message");
        sql.append("(");
        sql.append("room_id,");
        sql.append("send_user_id,");
        sql.append("main_message,");
        sql.append("create_at");
        sql.append(") VALUES (");
        sql.append(":room_id,");
        sql.append(":send_user_id,");
        sql.append(":main_message,");
        sql.append(":create_at");
        sql.append(")");

        String str = sql.toString();
        SqlParameterSource params = params(message);
        result = npjdbc.update(str,params);
        return result;
    }
    public int deleteRoomMessage(String room_id) {
        return 1;
    }
    private SqlParameterSource params(RoomMessageDto message) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("room_id", message.room_id())
                .addValue("send_user_id", message.send_user_id())
                .addValue("main_message", message.main_message())
                .addValue("create_at", message.create_at());
        return params;
    }
    private static LocalDateTime asLocalDateTime(Date value) {
		if (value == null) return null;
		return value.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}
}
