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

import com.app.sharphin.dto.room.RoomsDto;

@Repository
public class RoomsRepository {
    @Autowired
    NamedParameterJdbcTemplate npjdbc;
    private final RowMapper<RoomsDto> ROOM_LIST_MAPPER = (rs, row) -> new RoomsDto (
        rs.getString("room_id"),
        rs.getString("room_owner"),
        rs.getString("room_name"),
        rs.getString("icon_path"),
        asLocalDateTime(rs.getTimestamp("create_at")));

    public List<RoomsDto> getRoomList(String room_id) {
        List<RoomsDto> user_list = new ArrayList<>();
        try {
            SqlParameterSource param = new MapSqlParameterSource().addValue("room_id", room_id);
            StringBuilder sql = new StringBuilder();
            sql.append("select * from public.rooms where room_id = :room_id");
            user_list = npjdbc.query(sql.toString(),param,ROOM_LIST_MAPPER);  
        } catch (EmptyResultDataAccessException e) {
            return user_list;
        }
        return user_list;
    }
    public int buildRoom(RoomsDto rooms) {
        int result;
        StringBuilder sql = new StringBuilder("INSERT INTO public.rooms");
        sql.append("(");
        sql.append("room_id,");
        sql.append("room_owner,");
        sql.append("room_name,");
        sql.append("icon_path,");
        sql.append("create_at");
        sql.append(") VALUES (");
        sql.append(":room_id,");
        sql.append(":room_owner,");
        sql.append(":room_name,");
        sql.append(":icon_path,");
        sql.append(":create_at");
        sql.append(")");

        String str = sql.toString();
        SqlParameterSource params = params(rooms);
        result = npjdbc.update(str,params);
        return result;
    }
    public int updateRoom(RoomsDto rooms) {
        return 1;
    }
    public int deleteRoom(String room_id) {
        return 1;
    }
    private SqlParameterSource params(RoomsDto message) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("room_id", message.room_id())
                .addValue("room_owner", message.room_owner())
                .addValue("room_name", message.room_name())
                .addValue("icon_path", message.icon_path())
                .addValue("create_at", message.create_at());
        return params;
    }
    private static LocalDateTime asLocalDateTime(Date value) {
		if (value == null) return null;
		return value.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}
}
