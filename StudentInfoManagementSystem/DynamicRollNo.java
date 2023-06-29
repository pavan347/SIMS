import java.sql.*;
import java.util.Calendar;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 *
 * @author pavan kumar
 */
public class DynamicRollNo {
    static Connection con;
    public DynamicRollNo() throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mainproject", "root", "21AJ1A0535");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    Calendar cal = Calendar.getInstance();
    String year = (Integer.toString(cal.getWeekYear())).substring(2);
    String collegeCode = "AJ",branchCode = "1A",SpecializationCode = "",rollno, number;
    int no;
    public String setNO(String branch) {
        try {
            String query = "select sno from " + branch + " order by sno desc limit 1";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                no = Integer.parseInt(rs.getString(1)) + 1;
            } else {
                no = 1;
            }
            if (no > 99) {
                no = no - 100;
                if (no >= 0 && no < 10) {
                    number = "a" + no;
                } else if (no >= 10 && no < 20) {
                    no = no - 10;
                    number = "b" + no;
                } else if (no >= 20 && no < 30) {
                    no = no - 20;
                    number = "c" + no;
                } else {
                    number = "NAN";
                }
            }
            if (no < 10) {
                number = "0" + no;
            } else {
                number = "" + no;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        return number;
    }

    public String setRollNo(String branch, String num) {
        switch (branch) {
            case "CIVIL" -> SpecializationCode = "01";
            case "EEE" -> SpecializationCode = "02";
            case "MECH" -> SpecializationCode = "03";
            case "ECE" -> SpecializationCode = "04";
            case "CSE" -> SpecializationCode = "05";
            case "CSM" -> SpecializationCode = "42";
            case "CBA" -> SpecializationCode = "58";
            case "CSD" -> SpecializationCode = "44";
            case "CIC" -> SpecializationCode = "47";
            default -> {
            }
        }
        rollno = year + collegeCode + branchCode + SpecializationCode + num;
        return rollno;
    }
    public String password() {
        String characters = "1234567890";
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 7; i++) {
            int randomindex = random.nextInt(characters.length());
            char randomchar = characters.charAt(randomindex);
            password.append(randomchar);
        }
        return password.toString();
    }
}