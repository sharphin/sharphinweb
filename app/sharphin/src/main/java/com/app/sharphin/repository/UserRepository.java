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

import com.app.sharphin.dto.user.UserDto;

@Repository
public class UserRepository {
    @Autowired
    JdbcTemplate jdbc;
    @Autowired
    NamedParameterJdbcTemplate npjdbc;

    private LocalDateTime NOW_TIME() {
        return LocalDateTime.now();
    }
    private RowMapper<UserDto> USER_ROW_MAPPER = (rs, row) -> new UserDto (
        rs.getString("user_id"),
        rs.getString("user_name"),
        rs.getString("email"),
        rs.getString("password"),
        rs.getString("icon_path"),
        rs.getString("authority"),
        rs.getBoolean("disable"),
        asLocalDateTime(rs.getTimestamp("create_at")),
        asLocalDateTime(rs.getTimestamp("update_at")));
  
    public UserDto findByUserName(String user_id) {
        UserDto result = null;
        try {
            SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user_id);
            String sql = "SELECT * FROM public.users where user_id = :user_id";
            result = npjdbc.queryForObject(sql,param,USER_ROW_MAPPER);  
        } catch (EmptyResultDataAccessException e) {
            return result;
        }
        return result;
    }
    public List<UserDto> getUserList(String column,String userinfo) {
        List<UserDto> result = null;
        try {
            SqlParameterSource param = new MapSqlParameterSource().addValue(column, userinfo);
            String sql = "SELECT * FROM public.users where user_id = :user_id";
            result = npjdbc.query(sql,param,USER_ROW_MAPPER);
        } catch (EmptyResultDataAccessException e) {
            return result;
        }
        return result;
    }
    public int userRegister(UserDto userDto) {
        int result = 0;
        StringBuilder sql = new StringBuilder("INSERT INTO public.users");
        sql.append("(");
        sql.append("user_id,");
        sql.append("user_name,");
        sql.append("email,");
        sql.append("password,");
        sql.append("icon_path,");
        sql.append("authority,");
        sql.append("disable,");
        sql.append("create_at,");
        sql.append("update_at");
        sql.append(") VALUES (");
        sql.append(":user_id,");
        sql.append(":user_name,");
        sql.append(":email,");
        sql.append(":password,");
        sql.append(":icon_path,");
        sql.append(":authority,");
        sql.append(":disable,");
        sql.append(":create_at,");
        sql.append(":update_at");
        sql.append(")");

        String str = sql.toString();
        SqlParameterSource params = insertparams(userDto);
        result = npjdbc.update(str,params);
        return result;
    }
    public int userUpdate(UserDto userDto,String old_user_id) {
        int result = 0;
        StringBuilder sql = new StringBuilder("UPDATE public.users ");
        sql.append("SET ");
        sql.append("user_id = :user_id,");
        sql.append("user_name = :user_name,");
        sql.append("email = :email,");
        sql.append("password = :password,");
        sql.append("authority = :authority,");
        sql.append("disable = :disable,");
        sql.append("create_at = :create_at,");
        sql.append("update_at = :update_at ");
        sql.append("WHERE user_id = :old_user_id");
        SqlParameterSource params = updateparams(userDto,old_user_id);
        result = npjdbc.update(sql.toString(),params);
        return result;
    }
    public int iconUpdate(String user_id,String icon_path) {
        int result = 0;
        StringBuilder sql = new StringBuilder("UPDATE public.users ");
        sql.append("SET icon_path = :icon_path ");
        sql.append("WHERE user_id = :user_id");
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("icon_path", icon_path)
                .addValue("user_id", user_id);
        result = npjdbc.update(sql.toString(),params);
        return result;
    }

    public int deleteOne(String user_id) {
        String sql = "DELETE FROM public.users WHERE user_id = :userId";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("user_id", user_id);
        int rowNumber = jdbc.update(sql, params);

        return rowNumber;
    }

    private SqlParameterSource insertparams(UserDto userDto) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("user_id", userDto.user_id())
                .addValue("user_name", userDto.user_name())
                .addValue("email", userDto.email())
                .addValue("password", userDto.password())
                .addValue("icon_path", userDto.icon_path())
                .addValue("authority", userDto.authority())
                .addValue("disable", userDto.disable())
                .addValue("create_at", NOW_TIME())
                .addValue("update_at", NOW_TIME());
        return params;
    }
    private SqlParameterSource updateparams(UserDto userDto,String old_user_id) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("user_id", userDto.user_id())
                .addValue("user_name", userDto.user_name())
                .addValue("email", userDto.email())
                .addValue("password", userDto.password())
                .addValue("authority", userDto.authority())
                .addValue("disable", userDto.disable())
                .addValue("create_at", NOW_TIME())
                .addValue("update_at", NOW_TIME())
                .addValue("old_user_id", old_user_id);
        return params;
    }
    private static LocalDateTime asLocalDateTime(Date value) {
		if (value == null) return null;
		return value.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}
}
