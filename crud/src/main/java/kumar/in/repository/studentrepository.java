package kumar.in.repository;

import kumar.in.model.student;

import java.sql.*;

public class studentrepository {
    String url="jdbc:mysql://localhost:3306/jdbc";
    String username="root";
    String password="1Kundan@kumar";
//        public void createUser(){
//        try {
//            Connection connection = DriverManager.getConnection(url, username, password);
//            Statement statement=connection.createStatement();
//            String sql="Insert Into student(name,email,age)"+"values('mukul', 'mukul@aas',22)";
//            int result= statement.executeUpdate(sql);
//          if(result==1){
//              System.out.println("create operation successful");
//          }
//          else {
//              System.out.println("creationn failed");
//          }
//
//            connection.close();
//        }
    public void getUserById(){
        try{
            Connection connection=DriverManager.getConnection(url,username,password);
            Statement statement=connection.createStatement();
            String sql="SELECT id,name,email, age " +
                    "FROM student where id = 1";
            ResultSet resultSet= statement.executeQuery(sql);
            resultSet.next();
            student student=mapRow(resultSet);
            System.out.println(student);
            connection.close();
        }
        catch (SQLException e) {
            System.out.println("database connection failed");
            e.printStackTrace();
        }
    }

    private student mapRow(ResultSet resultSet) throws SQLException {
        student student=new student();
        student.setId(resultSet.getLong("id"));
        student.getName(resultSet.getString("name"));
        student.getEmail(resultSet.getString("email"));
        student.getAge(resultSet.getInt("age"));
        return student;
    }

}
