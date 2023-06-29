
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author pavan kumar
 */
public class Register extends javax.swing.JFrame {

    /**
     * Creates new form Register
     */
    static Connection conn;

    public Register() throws SQLException {
        initComponents();
        try {
            ImageIcon icon = new ImageIcon("C:\\Program Files\\SIMS\\StudentWhite.png");
            setIconImage(icon.getImage());
            setTitle("Register");
            DataBaseConnection dbc = new DataBaseConnection();
            conn = dbc.getCon();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        S_firstname.grabFocus();
    }
    String Spath;

    public void uploadphoto() {
        SetPhoto sp = new SetPhoto();
        boolean b = sp.checkPhoto();
        Spath = sp.getPath();
        if (b) {
            S_labelphoto.setIcon(sp.seticon(Spath, null, S_labelphoto.getWidth(), S_labelphoto.getHeight()));
        }

    }

    public void registerCheck() {
        if (S_firstname.getText().equals("")) {
            S_error.setText("First name is compulsory...");
            S_firstname.grabFocus();
        }  else if (S_lastname.getText().equals("")) {
            S_error.setText("Last name is compulsory...");
            S_lastname.grabFocus();
        } else if (S_fathername.getText().equals("")) {
            S_error.setText("Father name is compulsory...");
            S_fathername.grabFocus();
        } else if (S_mothername.getText().equals("")) {
            S_error.setText("Mother name is compulsory...");
            S_mothername.grabFocus();
        } else if (S_dateofbirth.getDate() == null) {
            S_error.setText("Select your Date of Birth...");
            S_dateofbirth.grabFocus();
        } else if (S_address.getText().equals("")) {
            S_error.setText("Specify your address");
            S_address.requestFocus();
        } else if (S_phoneno.getText().equals("") || S_phoneno.getText().length() != 10) {
            S_error.setText("Provide correct phone number...");
            S_phoneno.grabFocus();
        } else if (S_emailid.getText().equals("") || !S_emailid.getText().endsWith("@gmail.com")) {
            S_error.setText("Provide correct Email ID...");
            S_emailid.grabFocus();
        } else if (S_aadhar.getText().equals("") || S_aadhar.getText().length() != 12) {
            S_error.setText("Provide correct Aadhar number...");
            S_aadhar.grabFocus();
        } else if (!S_male.isSelected() && !S_female.isSelected()) {
            S_error.setText("Select your Gender...");
            S_male.grabFocus();
        } else if (S_10marks.getText().equals("") || S_10marks.getText().length() > 3 || !checkMarks(true, false)) {
            S_error.setText("Provided 10th marks are incorrect...");
            S_10marks.grabFocus();
        }else if (S_intermarks.getText().equals("") || S_intermarks.getText().length() != 3 || !checkMarks(false, true)) {
            S_error.setText("Provided inter marks are incorrect...");
            S_intermarks.grabFocus();
        } else if (S_caste.getText().equals("")) {
            S_error.setText("Specify your Caste...");
            S_caste.grabFocus();
        } else if (S_subcaste.getText().equals("")) {
            S_error.setText("Specify your Subcaste...");
            S_subcaste.grabFocus();
        } else if (S_labelphoto.getIcon() == null) {
            S_error.setText("Please upload your photo...");
            S_uploadphoto.grabFocus();
        } else {
            try {
                S_error.setText("");
                registerstudent();
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }
    String sdateofbirth;

    public void calculateAge() {
        LocalDate ldob, todaydate = LocalDate.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdateofbirth = "" + sdf.format(S_dateofbirth.getDate());
        ldob = LocalDate.parse(sdateofbirth, dtf);

        Period difference = Period.between(ldob, todaydate);
        int year = difference.getYears();
        S_age.setText("" + year);
    }

    public void registerstudent() throws ClassNotFoundException {
        String sname, sgender, mainbranch, scategory;
        sname = S_firstname.getText() + " " + S_lastname.getText();

        sgender = (S_male.isSelected()) ? "male" : "female";
        scategory = S_category.getSelectedItem() + "";
        if (S_branch.getSelectedItem().equals("CSE")) {
            mainbranch = S_specialization.getSelectedItem() + "";
        } else {
            mainbranch = S_branch.getSelectedItem() + "";
        }
        DynamicRollNo o = new DynamicRollNo();
        String sno = o.setNO(mainbranch);
        if (!sno.equals("NAN")) {
            String rollno = o.setRollNo(mainbranch, sno);
            S_regdno.setText(rollno);
            String password = o.password();
            String querry = " insert into " + mainbranch + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            try {
                PreparedStatement pst = conn.prepareStatement(querry);
                pst.setString(1, sno);
                pst.setString(2, rollno);
                pst.setString(3, sname);
                pst.setString(4, S_fathername.getText());
                pst.setString(5, S_mothername.getText());
                pst.setString(6, S_fatheroccupation.getText());
                pst.setString(7, S_motheroccupation.getText());
                pst.setString(8, sdateofbirth);
                pst.setString(9, S_age.getText());
                pst.setString(10, S_address.getText());
                pst.setString(11, S_phoneno.getText());
                pst.setString(12, S_emailid.getText());
                pst.setString(13, S_aadhar.getText());
                pst.setString(14, sgender);
                pst.setString(15, mainbranch);
                pst.setString(16, scategory);
                pst.setString(17, S_10marks.getText());
                pst.setString(18, S_intermarks.getText());
                pst.setString(19, S_caste.getText());
                pst.setString(20, S_subcaste.getText());
                pst.setString(21, Spath);
                pst.setString(22, password);
                pst.executeUpdate();
                int result = JOptionPane.showConfirmDialog(null, "Student has registered Sucessfully with \nRegistration No :" + rollno + "\n Password :" + password, "Sucess", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    clear();
                }
                Spath = null;
                String q = "insert into attendance_" + mainbranch + "(sno,regd,name,dayspresent,totaldays,attendance) values(?,?,?,'0','0','0')";
                PreparedStatement ps = conn.prepareStatement(q);
                ps.setString(1, sno);
                ps.setString(2, rollno);
                ps.setString(3, sname);
                ps.executeUpdate();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Students Limits have reached...");
        }
    }

    public void clear() {
        S_firstname.setText("");
        S_lastname.setText("");
        S_regdno.setText("");
        S_fathername.setText("");
        S_mothername.setText("");
        S_fatheroccupation.setText("");
        S_motheroccupation.setText("");
        S_labelphoto.setIcon(null);
        S_age.setText("");
        S_dateofbirth.setDate(null);
        S_address.setText("");
        S_phoneno.setText("");
        S_emailid.setText("");
        S_aadhar.setText("");
        S_gender.clearSelection();
        S_10marks.setText("");
        S_intermarks.setText("");
        S_caste.setText("");
        S_subcaste.setText("");
        S_firstname.grabFocus();
    }

    public void numbersBoolean(boolean b) {
        S_phoneno.setEditable(b);
        S_aadhar.setEditable(b);
        S_10marks.setEditable(b);
        S_intermarks.setEditable(b);
    }

    public void numbersOnly(int len, int keycode, char keychar, boolean marks) {
        if (keychar >= '0' && keychar <= '9') {
            numbersBoolean(true);
        } else {
            numbersBoolean(false);
        }
        if (keycode == KeyEvent.VK_BACK_SPACE || keycode == KeyEvent.VK_ENTER || keycode == KeyEvent.VK_DELETE || keycode == KeyEvent.VK_COPY || keycode == KeyEvent.VK_CUT || keycode == KeyEvent.VK_PASTE) {
            numbersBoolean(true);
        }
        if (marks) {
            if (keycode == KeyEvent.VK_PERIOD || keychar == '%') {
                numbersBoolean(true);
            }
        }
    }

    public boolean checkMarks(boolean ten, boolean inter) {
        String marks = S_10marks.getText();
        String intermarks = S_intermarks.getText();

        try {
            if (ten) {
                if (marks.length() == 2) {
                    try {
                        int m = Integer.parseInt(marks);
                        return m==10;
                    } catch (NumberFormatException e) {

                    }
                }
                else if ((marks.charAt(1) != '.' && marks.charAt(2) != '%')) {
                    int m = Integer.parseInt(marks);
                    if (m > 200 && m < 600) {
                        return true;
                    }
                } else {
                    return true;
                }
            } else if (inter) {
                if ((intermarks.charAt(1) != '.' && intermarks.charAt(2) != '%')) {
                    int mi = Integer.parseInt(intermarks);
                    if (mi > 350 && mi < 1000) {
                        return true;
                    }

                } else {
                    return true;
                }
            }
        } catch (NumberFormatException ex) {
            return false;
        }
        return false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        S_gender = new javax.swing.ButtonGroup();
        jPanel8 = new javax.swing.JPanel();
        Student_details = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        S_labelphoto = new javax.swing.JLabel();
        S_firstname = new javax.swing.JTextField();
        S_fatheroccupation = new javax.swing.JTextField();
        S_uploadphoto = new javax.swing.JButton();
        S_mothername = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        S_address = new java.awt.TextArea();
        jLabel10 = new javax.swing.JLabel();
        S_phoneno = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        S_emailid = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        S_aadhar = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        S_10marks = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        S_subcaste = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        S_intermarks = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        S_lastname = new javax.swing.JTextField();
        S_male = new javax.swing.JRadioButton();
        S_female = new javax.swing.JRadioButton();
        S_branch = new javax.swing.JComboBox<>();
        jLabel20 = new javax.swing.JLabel();
        S_motheroccupation = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        S_category = new javax.swing.JComboBox<>();
        jLabel38 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        S_specialization = new javax.swing.JComboBox<>();
        S_caste = new javax.swing.JTextField();
        S_regdno = new javax.swing.JLabel();
        S_dateofbirth = new com.toedter.calendar.JDateChooser();
        S_fathername = new javax.swing.JTextField();
        S_age = new javax.swing.JLabel();
        S_register = new javax.swing.JButton();
        S_error = new javax.swing.JLabel();
        Logout = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setBounds(new java.awt.Rectangle(10, 0, 0, 0));
        setPreferredSize(new java.awt.Dimension(1510, 820));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel8.setBackground(new java.awt.Color(50, 34, 116));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Student_details.setBackground(new java.awt.Color(255, 255, 255));
        Student_details.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1), "Student Details", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 1, 30), new java.awt.Color(50, 34, 116))); // NOI18N
        Student_details.setAutoscrolls(true);
        Student_details.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(50, 34, 116));
        jLabel1.setText("First Name");
        Student_details.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 89, 122, 35));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(50, 34, 116));
        jLabel2.setText("Regd No");
        Student_details.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 136, 122, 35));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(50, 34, 116));
        jLabel3.setText("Father Name");
        Student_details.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 183, 122, 35));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(50, 34, 116));
        jLabel4.setText("Occupation");
        Student_details.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(375, 183, 122, 35));

        S_labelphoto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Student_details.add(S_labelphoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(725, 89, 180, 200));

        S_firstname.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        S_firstname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                S_firstnameActionPerformed(evt);
            }
        });
        Student_details.add(S_firstname, new org.netbeans.lib.awtextra.AbsoluteConstraints(153, 89, 204, 35));

        S_fatheroccupation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        S_fatheroccupation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                S_fatheroccupationActionPerformed(evt);
            }
        });
        Student_details.add(S_fatheroccupation, new org.netbeans.lib.awtextra.AbsoluteConstraints(503, 183, 204, 35));

        S_uploadphoto.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        S_uploadphoto.setForeground(new java.awt.Color(50, 34, 116));
        S_uploadphoto.setText("Upload Photo");
        S_uploadphoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                S_uploadphotoActionPerformed(evt);
            }
        });
        Student_details.add(S_uploadphoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(725, 295, 180, 35));

        S_mothername.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        S_mothername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                S_mothernameActionPerformed(evt);
            }
        });
        Student_details.add(S_mothername, new org.netbeans.lib.awtextra.AbsoluteConstraints(153, 236, 204, 35));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(50, 34, 116));
        jLabel8.setText("Address");
        Student_details.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 347, 122, 35));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(50, 34, 116));
        jLabel9.setText("Mother Name");
        Student_details.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 238, 122, 30));

        S_address.setColumns(34);
        S_address.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        S_address.setRows(2);
        S_address.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                S_addressKeyPressed(evt);
            }
        });
        Student_details.add(S_address, new org.netbeans.lib.awtextra.AbsoluteConstraints(153, 347, 803, 66));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(50, 34, 116));
        jLabel10.setText("Phone No");
        Student_details.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 431, 122, 35));

        S_phoneno.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        S_phoneno.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        S_phoneno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                S_phonenoActionPerformed(evt);
            }
        });
        S_phoneno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                S_phonenoKeyPressed(evt);
            }
        });
        Student_details.add(S_phoneno, new org.netbeans.lib.awtextra.AbsoluteConstraints(153, 431, 271, 35));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(50, 34, 116));
        jLabel11.setText("Email ID");
        Student_details.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(494, 431, 122, 35));

        S_emailid.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        S_emailid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                S_emailidActionPerformed(evt);
            }
        });
        Student_details.add(S_emailid, new org.netbeans.lib.awtextra.AbsoluteConstraints(622, 431, 271, 35));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(50, 34, 116));
        jLabel12.setText("Aadhar");
        Student_details.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 484, 122, 35));

        S_aadhar.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        S_aadhar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                S_aadharActionPerformed(evt);
            }
        });
        S_aadhar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                S_aadharKeyPressed(evt);
            }
        });
        Student_details.add(S_aadhar, new org.netbeans.lib.awtextra.AbsoluteConstraints(153, 484, 271, 35));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(50, 34, 116));
        jLabel13.setText("Gender");
        Student_details.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(494, 484, 122, 35));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(50, 34, 116));
        jLabel14.setText("Caste");
        Student_details.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 643, 122, 35));

        S_10marks.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        S_10marks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                S_10marksActionPerformed(evt);
            }
        });
        S_10marks.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                S_10marksKeyPressed(evt);
            }
        });
        Student_details.add(S_10marks, new org.netbeans.lib.awtextra.AbsoluteConstraints(153, 590, 271, 35));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(50, 34, 116));
        jLabel15.setText("Sub Caste");
        Student_details.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(494, 643, 122, 35));

        S_subcaste.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Student_details.add(S_subcaste, new org.netbeans.lib.awtextra.AbsoluteConstraints(622, 643, 271, 35));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(50, 34, 116));
        jLabel16.setText("10th Marks");
        Student_details.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 590, 122, 35));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(50, 34, 116));
        jLabel17.setText("Intermediate");
        Student_details.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(494, 590, 122, 35));

        S_intermarks.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        S_intermarks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                S_intermarksActionPerformed(evt);
            }
        });
        S_intermarks.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                S_intermarksKeyPressed(evt);
            }
        });
        Student_details.add(S_intermarks, new org.netbeans.lib.awtextra.AbsoluteConstraints(622, 590, 271, 35));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(50, 34, 116));
        jLabel18.setText("Name");
        Student_details.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 53, 122, 30));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(50, 34, 116));
        jLabel19.setText("Last Name");
        Student_details.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(375, 89, 122, 35));

        S_lastname.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        S_lastname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                S_lastnameActionPerformed(evt);
            }
        });
        Student_details.add(S_lastname, new org.netbeans.lib.awtextra.AbsoluteConstraints(503, 89, 204, 35));

        S_gender.add(S_male);
        S_male.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        S_male.setText("Male");
        Student_details.add(S_male, new org.netbeans.lib.awtextra.AbsoluteConstraints(622, 484, 98, 35));

        S_gender.add(S_female);
        S_female.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        S_female.setText("Female");
        Student_details.add(S_female, new org.netbeans.lib.awtextra.AbsoluteConstraints(738, 484, 98, 35));

        S_branch.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        S_branch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CSE", "ECE", "EEE", "CIVIL", "MECH" }));
        S_branch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                S_branchActionPerformed(evt);
            }
        });
        Student_details.add(S_branch, new org.netbeans.lib.awtextra.AbsoluteConstraints(153, 537, 123, 35));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(50, 34, 116));
        jLabel20.setText("Course");
        Student_details.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 537, 122, 35));

        S_motheroccupation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        S_motheroccupation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                S_motheroccupationActionPerformed(evt);
            }
        });
        Student_details.add(S_motheroccupation, new org.netbeans.lib.awtextra.AbsoluteConstraints(503, 236, 204, 35));

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(50, 34, 116));
        jLabel22.setText("Occupation");
        Student_details.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(375, 236, 122, 35));

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(50, 34, 116));
        jLabel23.setText("Date Of Birth");
        Student_details.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 289, 122, 35));

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(50, 34, 116));
        jLabel24.setText("category");
        Student_details.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(625, 537, 122, 35));

        S_category.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        S_category.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cat-A", "Cat-B" }));
        Student_details.add(S_category, new org.netbeans.lib.awtextra.AbsoluteConstraints(765, 537, 151, 35));

        jLabel38.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(50, 34, 116));
        jLabel38.setText("Age");
        Student_details.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(375, 294, 122, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(50, 34, 116));
        jLabel7.setText("Specialization");
        Student_details.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(328, 537, 130, 35));

        S_specialization.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        S_specialization.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CSE", "CSD", "CSM", "CBA", "CIC" }));
        Student_details.add(S_specialization, new org.netbeans.lib.awtextra.AbsoluteConstraints(494, 539, 107, -1));

        S_caste.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        S_caste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                S_casteActionPerformed(evt);
            }
        });
        Student_details.add(S_caste, new org.netbeans.lib.awtextra.AbsoluteConstraints(153, 643, 271, 35));

        S_regdno.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Student_details.add(S_regdno, new org.netbeans.lib.awtextra.AbsoluteConstraints(153, 136, 204, 35));

        S_dateofbirth.setBackground(new java.awt.Color(255, 255, 255));
        S_dateofbirth.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        S_dateofbirth.setMaxSelectableDate(new java.util.Date(1167593468000L));
        S_dateofbirth.setMinSelectableDate(new java.util.Date(946668668000L));
        S_dateofbirth.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                S_dateofbirthPropertyChange(evt);
            }
        });
        Student_details.add(S_dateofbirth, new org.netbeans.lib.awtextra.AbsoluteConstraints(153, 289, 204, 35));

        S_fathername.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        S_fathername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                S_fathernameActionPerformed(evt);
            }
        });
        Student_details.add(S_fathername, new org.netbeans.lib.awtextra.AbsoluteConstraints(153, 183, 204, 35));

        S_age.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Student_details.add(S_age, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 300, -1, -1));

        jPanel8.add(Student_details, new org.netbeans.lib.awtextra.AbsoluteConstraints(469, 6, -1, 698));

        S_register.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        S_register.setForeground(new java.awt.Color(50, 34, 116));
        S_register.setText("Register");
        S_register.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                S_registerActionPerformed(evt);
            }
        });
        jPanel8.add(S_register, new org.netbeans.lib.awtextra.AbsoluteConstraints(1138, 716, 140, 40));

        S_error.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        S_error.setForeground(new java.awt.Color(255, 255, 255));
        jPanel8.add(S_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 292, 428, 57));

        Logout.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        Logout.setText("back");
        Logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogoutActionPerformed(evt);
            }
        });
        jPanel8.add(Logout, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 25, 100, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1515, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 817, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void S_firstnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_S_firstnameActionPerformed
        S_lastname.grabFocus();
    }//GEN-LAST:event_S_firstnameActionPerformed

    private void S_fatheroccupationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_S_fatheroccupationActionPerformed
        S_mothername.grabFocus();
    }//GEN-LAST:event_S_fatheroccupationActionPerformed

    private void S_uploadphotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_S_uploadphotoActionPerformed
        uploadphoto();
    }//GEN-LAST:event_S_uploadphotoActionPerformed

    private void S_mothernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_S_mothernameActionPerformed
        S_motheroccupation.grabFocus();
    }//GEN-LAST:event_S_mothernameActionPerformed

    private void S_addressKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_S_addressKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            S_phoneno.grabFocus();
        }
    }//GEN-LAST:event_S_addressKeyPressed

    private void S_phonenoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_S_phonenoActionPerformed
        S_emailid.grabFocus();
    }//GEN-LAST:event_S_phonenoActionPerformed

    private void S_phonenoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_S_phonenoKeyPressed
        int len = S_phoneno.getText().length();
        int a = evt.getKeyCode();
        char keychar = evt.getKeyChar();
        numbersOnly(len, a, keychar, false);
    }//GEN-LAST:event_S_phonenoKeyPressed

    private void S_emailidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_S_emailidActionPerformed
        S_aadhar.grabFocus();
    }//GEN-LAST:event_S_emailidActionPerformed

    private void S_aadharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_S_aadharActionPerformed
        S_10marks.grabFocus();
    }//GEN-LAST:event_S_aadharActionPerformed

    private void S_aadharKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_S_aadharKeyPressed
        int len = S_aadhar.getText().length();
        int a = evt.getKeyCode();
        char keychar = evt.getKeyChar();
        numbersOnly(len, a, keychar, false);
    }//GEN-LAST:event_S_aadharKeyPressed

    private void S_10marksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_S_10marksActionPerformed
        S_intermarks.grabFocus();
    }//GEN-LAST:event_S_10marksActionPerformed

    private void S_10marksKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_S_10marksKeyPressed
        int len = S_10marks.getText().length();
        int a = evt.getKeyCode();
        char keychar = evt.getKeyChar();
        numbersOnly(len, a, keychar, true);
    }//GEN-LAST:event_S_10marksKeyPressed

    private void S_intermarksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_S_intermarksActionPerformed
        S_caste.grabFocus();
    }//GEN-LAST:event_S_intermarksActionPerformed

    private void S_intermarksKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_S_intermarksKeyPressed
        int len = S_intermarks.getText().length();
        int a = evt.getKeyCode();
        char keychar = evt.getKeyChar();
        numbersOnly(len, a, keychar, true);
    }//GEN-LAST:event_S_intermarksKeyPressed

    private void S_lastnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_S_lastnameActionPerformed
        S_fathername.grabFocus();
    }//GEN-LAST:event_S_lastnameActionPerformed

    private void S_branchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_S_branchActionPerformed
        if (S_branch.getSelectedItem().equals("CSE")) {
            S_specialization.setEnabled(true);
            S_specialization.setSelectedIndex(0);
        } else {
            S_specialization.setEnabled(false);
            S_specialization.setSelectedIndex(-1);
        }
    }//GEN-LAST:event_S_branchActionPerformed

    private void S_motheroccupationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_S_motheroccupationActionPerformed
        S_dateofbirth.requestFocusInWindow();
    }//GEN-LAST:event_S_motheroccupationActionPerformed

    private void S_casteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_S_casteActionPerformed
        S_subcaste.grabFocus();
    }//GEN-LAST:event_S_casteActionPerformed

    private void S_dateofbirthPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_S_dateofbirthPropertyChange
        S_address.requestFocus();
        if (S_dateofbirth.getDate() != null) {
            calculateAge();
        }
    }//GEN-LAST:event_S_dateofbirthPropertyChange

    private void S_fathernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_S_fathernameActionPerformed
        S_fatheroccupation.grabFocus();
    }//GEN-LAST:event_S_fathernameActionPerformed

    private void S_registerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_S_registerActionPerformed
        registerCheck();
    }//GEN-LAST:event_S_registerActionPerformed

    private void LogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogoutActionPerformed
        dispose();
        MainFrame mf = new MainFrame();
        mf.setVisible(true);
    }//GEN-LAST:event_LogoutActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        int result = JOptionPane.showConfirmDialog(Register.this, "Do you want to exit", "Closing", JOptionPane.YES_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            dispose();
        }
    }//GEN-LAST:event_formWindowClosing

   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Logout;
    private javax.swing.JTextField S_10marks;
    private javax.swing.JTextField S_aadhar;
    private java.awt.TextArea S_address;
    private javax.swing.JLabel S_age;
    private javax.swing.JComboBox<String> S_branch;
    private javax.swing.JTextField S_caste;
    private javax.swing.JComboBox<String> S_category;
    private com.toedter.calendar.JDateChooser S_dateofbirth;
    private javax.swing.JTextField S_emailid;
    private javax.swing.JLabel S_error;
    private javax.swing.JTextField S_fathername;
    private javax.swing.JTextField S_fatheroccupation;
    private javax.swing.JRadioButton S_female;
    private javax.swing.JTextField S_firstname;
    private javax.swing.ButtonGroup S_gender;
    private javax.swing.JTextField S_intermarks;
    private javax.swing.JLabel S_labelphoto;
    private javax.swing.JTextField S_lastname;
    private javax.swing.JRadioButton S_male;
    private javax.swing.JTextField S_mothername;
    private javax.swing.JTextField S_motheroccupation;
    private javax.swing.JTextField S_phoneno;
    private javax.swing.JLabel S_regdno;
    private javax.swing.JButton S_register;
    private javax.swing.JComboBox<String> S_specialization;
    private javax.swing.JTextField S_subcaste;
    private javax.swing.JButton S_uploadphoto;
    private javax.swing.JPanel Student_details;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel8;
    // End of variables declaration//GEN-END:variables
}
