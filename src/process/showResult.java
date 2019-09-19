package process;

import util.DBconnecter;
import util.StringUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class showResult {
    public static void main(String args[]){
        Connection connection = new DBconnecter().conn;
        double cokyw1 = 0, cocnx1 = 0, coskm1 = 0, codat1 = 0;
        double cokyw2 = 0, cocnx2 = 0, coskm2 = 0, codat2 = 0;
        double cokyw3 = 0, cocnx3 = 0, coskm3 = 0, codat3 = 0;
        double cokyw4 = 0, cocnx4 = 0, coskm4 = 0, codat4 = 0;
        try {
            String sql = "select * from evaluation_result";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                cokyw1 += resultSet.getDouble("k1");
                cocnx1 += resultSet.getDouble("c1");
                coskm1 += resultSet.getDouble("s1");
                codat1 += resultSet.getDouble("d1");
                cokyw2 += resultSet.getDouble("k2");
                cocnx2 += resultSet.getDouble("c2");
                coskm2 += resultSet.getDouble("s2");
                codat2 += resultSet.getDouble("d2");
                cokyw3 += resultSet.getDouble("k3");
                cocnx3 += resultSet.getDouble("c3");
                coskm3 += resultSet.getDouble("s3");
                codat3 += resultSet.getDouble("d3");
                cokyw4 += resultSet.getDouble("k4");
                cocnx4 += resultSet.getDouble("c4");
                coskm4 += resultSet.getDouble("s4");
                codat4 += resultSet.getDouble("d4");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        cokyw1 = cokyw1/387; cocnx1 = cocnx1/387; coskm1 = coskm1/387; codat1 = codat1/387;
        cokyw2 = cokyw2/387; cocnx2 = cocnx2/387; coskm2 = coskm2/387; codat2 = codat2/387;
        cokyw3 = cokyw3/387; cocnx3 = cocnx3/387; coskm3 = coskm3/387; codat3 = codat3/387;
        cokyw4 = cokyw4/387; cocnx4 = cocnx4/387; coskm4 = coskm4/387; codat4 = codat4/387;
        System.out.println("coKyw1 = "+cokyw1+"; "+"coCnx1 = "+cocnx1+"; "+"coSkm1 = "+coskm1+"; "+"coDat1 = "+codat1);
        System.out.println("coKyw2 = "+cokyw2+"; "+"coCnx2 = "+cocnx2+"; "+"coSkm2 = "+coskm2+"; "+"coDat2 = "+codat2);
        System.out.println("coKyw3 = "+cokyw3+"; "+"coCnx3 = "+cocnx3+"; "+"coSkm3 = "+coskm3+"; "+"coDat3 = "+codat3);
        System.out.println("coKyw4 = "+cokyw4+"; "+"coCnx4 = "+cocnx4+"; "+"coSkm4 = "+coskm4+"; "+"coDat4 = "+codat4);
    }
}
