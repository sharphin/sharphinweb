package com.app.sharphin.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.app.sharphin.dto.message.SendUserDto;
import com.app.sharphin.dto.user.FollowUserDto;

@Repository
public class FollowUserRepository {
    @Autowired
    JdbcTemplate jdbc;
    @Autowired
    NamedParameterJdbcTemplate npjdbc;

    private LocalDateTime NOW_TIME() {
        return LocalDateTime.now();
    }
    private final RowMapper<SendUserDto> MESSAGE_USER_LIST_MAPPER = (rs, row) -> new SendUserDto (
        rs.getString("followed_user_id"),
        rs.getString("user_name"),
        rs.getString("icon_path"),
        rs.getString("chatroom_id"),
        rs.getString("main_message"));

    public List<SendUserDto> messageUserLists(String from_user_id) {
        List<SendUserDto> user_list;
        try {
            SqlParameterSource param = new MapSqlParameterSource().addValue("follow_user_id", from_user_id);
            StringBuilder sql = new StringBuilder();
            sql.append("select distinct on(followed_user_id) followed_user_id,ful.chatroom_id,user_name,icon_path,mes.main_message ");
            sql.append("from public.follow_user_list as ful ");
            sql.append("inner join public.users  as us on ful.followed_user_id = us.user_id ");
            sql.append("left join public.message as mes on mes.chatroom_id = ful.chatroom_id ");
            sql.append("where follow_user_id = :follow_user_id ");
            sql.append("order by followed_user_id,mes.main_message desc");
            user_list = npjdbc.query(sql.toString(),param,MESSAGE_USER_LIST_MAPPER);  
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return user_list;
    }
    public int findFollowCount(String user_id) {
        int result;
        try {
            SqlParameterSource param = new MapSqlParameterSource().addValue("follow_user_id", user_id);
            String sql = "SELECT COUNT(followed_user_id) FROM public.follow_user_list where follow_user_id = :follow_user_id";
            result = asInt(npjdbc.queryForObject(sql,param,Integer.class));  
        } catch (IncorrectResultSizeDataAccessException e) {
            return 0;
        }
        return result;
    }
    public int findFollowerCount(String user_id) {
        int result;
        try {
            SqlParameterSource param = new MapSqlParameterSource().addValue("followed_user_id", user_id);
            String sql = "SELECT COUNT(follow_user_id) FROM public.follow_user_list where followed_user_id = :followed_user_id";
            result = asInt(npjdbc.queryForObject(sql,param,Integer.class));  
        } catch (IncorrectResultSizeDataAccessException e) {
            return 0;
        }
        return result;
    }
    public List<String> findFollowUser_idList(String user_id) {
        List<String> result = null;
        try {
            SqlParameterSource param = new MapSqlParameterSource().addValue("follow_user_id", user_id);
            String sql = "SELECT followed_user_id FROM public.follow_user_list where follow_user_id = :follow_user_id";
            result = npjdbc.queryForList(sql,param,String.class);  
        } catch (EmptyResultDataAccessException e) {
            return result;
        }
        return result;
    }
    public List<String> findFollowerUser_idList(String user_id) {
        List<String> result = null;
        try {
            SqlParameterSource param = new MapSqlParameterSource().addValue("followed_user_id", user_id);
            String sql = "SELECT follow_user_id FROM public.follow_user_list where followed_user_id = :followed_user_id";
            result = npjdbc.queryForList(sql,param,String.class);  
        } catch (EmptyResultDataAccessException e) {
            return result;
        }
        return result;
    }
    public int addFollow(FollowUserDto user) {
        int result;
        StringBuilder sql = new StringBuilder("INSERT INTO public.follow_user_list");
        sql.append("(");
        sql.append("follow_user_id,");
        sql.append("followed_user_id,");
        sql.append("create_at,");
        sql.append(") VALUES (");
        sql.append(":follow_user_id,");
        sql.append(":followed_user_id,");
        sql.append(":create_at,");
        sql.append(")");

        String str = sql.toString();
        SqlParameterSource params = params(user);
        result = npjdbc.update(str,params);
        return result;
    }

    public int removeFollow(String user_id) {
        String sql = "DELETE FROM public.follow_user_list WHERE follow_user_id = :follow_user_id";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("follow_user_id", user_id);
        int rowNumber = jdbc.update(sql, params);

        return rowNumber;
    }

    private SqlParameterSource params(FollowUserDto user) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("follow_user_id", user.follow_user_id())
                .addValue("followed_user_id", user.followed_user_id())
                .addValue("create_at", NOW_TIME());
        return params;
    }
    /*
    private static LocalDateTime asLocalDateTime(Date value) {
		if (value == null) return null;
		return value.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}
     */
    private static int asInt(Integer value) {
        if (value == null) return 0;
        return value;
    }
}
