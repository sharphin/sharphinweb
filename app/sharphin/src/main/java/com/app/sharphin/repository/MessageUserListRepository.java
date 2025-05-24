package com.app.sharphin.repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.app.sharphin.dto.MessageUserListDto;
import com.app.sharphin.dto.ViewMessageUserListDto;
@Repository
public class MessageUserListRepository {
    @Autowired
    JdbcTemplate jdbc;
    @Autowired
    NamedParameterJdbcTemplate npjdbc;

    private LocalDateTime NOW_TIME() {
        return LocalDateTime.now();
    }
    private RowMapper<MessageUserListDto> USER_LIST_MAPPER = (rs, row) -> new MessageUserListDto (
        rs.getString("user_id_1"),
        rs.getString("user_id_2"),
        asLocalDateTime(rs.getTimestamp("create_at")));

    private RowMapper<ViewMessageUserListDto> USER_ICON_LIST_MAPPER = (rs, row) -> new ViewMessageUserListDto (
        rs.getString("user_id_2"),
        rs.getString("icon_path"));

    public List<MessageUserListDto> messageUsers(String from_user_id) {
        List<MessageUserListDto> user_list = null;
        try {
            SqlParameterSource param = new MapSqlParameterSource().addValue("user_id1", from_user_id);
            String sql = "SELECT * FROM public.massage_user_list where user_id_1 = :user_id1";
            user_list = npjdbc.query(sql,param,USER_LIST_MAPPER);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return user_list;
    }
    public List<ViewMessageUserListDto> messageUserIcons(String from_user_id) {
        List<ViewMessageUserListDto> user_list = null;
        try {
            SqlParameterSource param = new MapSqlParameterSource().addValue("user_id1", from_user_id);
            StringBuilder sql = new StringBuilder();
            sql.append("select mul.user_id_2,icon_path FROM public.message_user_list as mul ");
            sql.append("inner join public.users as us on mul.user_id_2 = us.user_id where user_id_1 = :user_id1");
            user_list = npjdbc.query(sql.toString(),param,USER_ICON_LIST_MAPPER);  
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return user_list;
    }
    private static LocalDateTime asLocalDateTime(Date value) {
		if (value == null) return null;
		return value.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}
}
