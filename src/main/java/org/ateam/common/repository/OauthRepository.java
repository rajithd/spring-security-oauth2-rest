package org.ateam.common.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

@Repository
public class OauthRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final static String FETCH_BY_USERNAME = "select username,password from auth_details where username=?";

    private final static String FETCH_BY_CLIENT_ID = "select client_id, client_secret, resource_ids, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove from oauth_client_details where client_id = ?";

    public User getByUsername(String username) {
        return jdbcTemplate.queryForObject(FETCH_BY_USERNAME, new Object[]{username}, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                User user = new User(resultSet.getString("username"), resultSet.getString("password"), Collections.<GrantedAuthority>emptyList());
                return user;
            }
        });
    }

    public BaseClientDetails getByClientId(String clientId) {
        return jdbcTemplate.queryForObject(FETCH_BY_CLIENT_ID, new Object[]{clientId}, new RowMapper<BaseClientDetails>() {
            @Override
            public BaseClientDetails mapRow(ResultSet rs, int i) throws SQLException {
                BaseClientDetails details = new BaseClientDetails(rs.getString(1), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(7), rs.getString(6));
                details.setClientSecret(rs.getString(2));
                if (rs.getObject(8) != null) {
                    details.setAccessTokenValiditySeconds(Integer.valueOf(rs.getInt(8)));
                }

                if (rs.getObject(9) != null) {
                    details.setRefreshTokenValiditySeconds(Integer.valueOf(rs.getInt(9)));
                }

                String scopes1 = rs.getString(11);
                if (scopes1 != null) {
                    details.setAutoApproveScopes(StringUtils.commaDelimitedListToSet(scopes1));
                }
                return details;
            }
        });
    }


}
