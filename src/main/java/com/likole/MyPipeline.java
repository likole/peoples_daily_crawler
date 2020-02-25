package com.likole;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MyPipeline implements Pipeline {

    public void process(ResultItems resultItems, Task task) {
        String title=resultItems.get("title"); 
        String subtitle=resultItems.get("subtitle"); 
        String source=resultItems.get("source"); 
        String content=resultItems.get("content"); 

        try {
            Connection connection=JdbcUtils.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into articles(`title`,`subtitle`,`source`,`content`) value (?,?,?,?)");
            preparedStatement.setString(1,title);
            preparedStatement.setString(2,subtitle);
            preparedStatement.setString(3,source);
            preparedStatement.setString(4,content);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
