package com.app.sharphin.repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.app.sharphin.dto.room.RoomMemberDto;

@Repository
public class RoomMemberRepository {
    @Autowired
    NamedParameterJdbcTemplate npjdbc;
    private final RowMapper<RoomMemberDto> ROOM_MEMBER_MAPPER = (rs, row) -> new RoomMemberDto (
        rs.getString("room_id"),
        rs.getString("room_member_id"),
        rs.getString("member_auth"),
        asLocalDateTime(rs.getTimestamp("create_at")));

    public List<RoomMemberDto> getRoomMember(String room_id) {
        List<RoomMemberDto> user_list = new ArrayList<>();
        try {
            SqlParameterSource param = new MapSqlParameterSource().addValue("room_id", room_id);
            StringBuilder sql = new StringBuilder();
            sql.append("select * from public.room_member where room_id = :room_id");
            user_list = npjdbc.query(sql.toString(),param,ROOM_MEMBER_MAPPER);  
        } catch (EmptyResultDataAccessException e) {
            return user_list;
        }
        return user_list;
    }
    public int addRoomMember(RoomMemberDto rooms) {
        int result;
        StringBuilder sql = new StringBuilder("INSERT INTO public.room_member");
        sql.append("(");
        sql.append("room_id,");
        sql.append("room_member_id,");
        sql.append("member_auth,");
        sql.append("create_at");
        sql.append(") VALUES (");
        sql.append(":room_id,");
        sql.append(":room_member,");
        sql.append(":member_auth,");
        sql.append(":create_at");
        sql.append(")");

        String str = sql.toString();
        SqlParameterSource params = params(rooms);
        result = npjdbc.update(str,params);
        return result;
    }
    public int updateRoomMember(RoomMemberDto rooms) {
        return 1;
    }
    public int deleteRoomMember(String room_id) {
        return 1;
    }
    public int deleteRoomMember(String room_id,String member_id) {
        return 1;
    }
    private SqlParameterSource params(RoomMemberDto message) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("room_id", message.room_id())
                .addValue("room_member_id", message.room_member_id())
                .addValue("member_auth", message.member_auth())
                .addValue("create_at", message.create_at());
        return params;
    }
    private static LocalDateTime asLocalDateTime(Date value) {
		if (value == null) return null;
		return value.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}
}
