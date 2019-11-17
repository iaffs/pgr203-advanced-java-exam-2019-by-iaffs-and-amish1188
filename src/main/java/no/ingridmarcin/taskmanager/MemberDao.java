package no.ingridmarcin.taskmanager;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class MemberDao extends AbstractDao<Member> {


    public MemberDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void insertObject(Member member, PreparedStatement statement) throws SQLException {
        statement.setString(1, member.getMemberName());
        statement.setString(2, member.getMail());
    }

    @Override
    protected Member readObject(ResultSet rs) throws SQLException {
        Member member = new Member();
        member.setId(rs.getLong("id"));
        member.setMemberName(rs.getString("member_name"));
        member.setMail(rs.getString("email"));
        return member;
    }

    public long insert(Member member) throws SQLException {
         return insert(member,
                 "insert into members (member_name, email) values (?, ?)"
         );
    }

    public List<Member> listAll() throws SQLException {
        return listAll(
                "select * from members"
        );
    }

    public Member retrieve(long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from members where id= ?")) {
                statement.setLong(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        return readObject(rs);
                    } else {
                        return null;
                    }
                }
            }
        }
    }

}
