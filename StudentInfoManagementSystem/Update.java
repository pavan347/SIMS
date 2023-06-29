
import java.sql.PreparedStatement;
import java.sql.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;


/**
 *
 * @author pavan kumar
 */
public class Update extends javax.swing.JFrame {

    /**
     * Creates new form Update
     */
    static Connection conn;

    public Update() throws SQLException {
        ImageIcon icon = new ImageIcon("C:\\Program Files\\SIMS\\StudentWhite.png");
        setIconImage(icon.getImage());
        setTitle("Update");
        initComponents();
        detailsEnableDisable(false);
        try {
            DataBaseConnection dbc = new DataBaseConnection();
            conn = dbc.getCon();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    String Spath;
    int click;
SetPhoto sp = new SetPhoto();
    public void uploadPhoto() {
        
        boolean b = sp.checkPhoto();
        Spath = sp.getPath();
        if (b) {
            S_labelphoto1.setIcon(sp.seticon(Spath, null, S_labelphoto1.getWidth(), S_labelphoto1.getHeight()));
            click++;
        }

    }

    String oldpath;

    public void displayDetails(String regdno, String branch, boolean next, boolean prev) {
        try {
            S_branchselect.setSelectedItem(branch.toUpperCase());
            S_regdnoselect.setText(regdno);
            String get = String.format("Select * from %s where regd=? ", branch);
            PreparedStatement st = conn.prepareStatement(get);
            st.setString(1, regdno);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                S_regdno1.setText(rs.getString(2));
                S_name1.setText(rs.getString(3));
                S_fathername1.setText(rs.getString(4));
                S_mothername1.setText(rs.getString(5));
                S_fatheroccupation1.setText(rs.getString(6));
                S_motheroccupation1.setText(rs.getString(7));
                S_dateofbirth1.setText(rs.getString(8));
                S_age1.setText(rs.getString(9));
                S_address1.setText(rs.getString(10));
                S_phoneno1.setText(rs.getString(11));
                S_emailid1.setText(rs.getString(12));
                S_aadhar1.setText(rs.getString(13));
                S_gender1.setText(rs.getString(14));
                S_branch1.setText(rs.getString(15));
                S_category1.setText(rs.getString(16));
                S_10marks1.setText(rs.getString(17));
                S_intermarks1.setText(rs.getString(18));
                S_caste1.setText(rs.getString(19));
                S_subcaste1.setText(rs.getString(20));
                oldpath = rs.getString("photo");
                if (oldpath.equals("")) {
                    S_labelphoto1.setIcon(null);
                } else {
                    S_labelphoto1.setIcon(sp.seticon(oldpath, null, S_labelphoto1.getWidth(), S_labelphoto1.getHeight()));
                }
            } else {
                if(next==prev){
                if(!next){
                JOptionPane.showMessageDialog(null, "Make sure that you have entered the correct Registration no");
                }
                }
                S_edit.setEnabled(false);
                S_save.setEnabled(false);
                if (next == prev) {
                    S_next.setEnabled(false);
                    S_prev.setEnabled(false);
                } else if (next) {
                    S_next.setEnabled(false);
                    S_edit.setEnabled(true);
                    S_save.setEnabled(true);
                    displayNextPrev(false, true);
                    JOptionPane.showMessageDialog(null, "You have reached the end...");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

    }

    public void setVisibl() {
        S_edit.setEnabled(true);
        S_save.setEnabled(true);
        S_prev.setEnabled(true);
        S_next.setEnabled(true);
    }

    /**
     *
     * @param next
     * @param prev
     */
    public void displayNextPrev(boolean next, boolean prev) {
        if (!S_regdnoselect.getText().equals("")) {
            String r = S_regdnoselect.getText().substring(0, 8);
            int num = Integer.parseInt(S_regdnoselect.getText().substring(8));
            if (next) {
                num = num + 1;
            } else {
                num = num - 1;
            }

            String b = S_branchselect.getSelectedItem() + "";
            if (num > 0 && num < 10) {
                r = r + "0" + num;
                S_regdnoselect.setText(r);
                click = 0;
                displayDetails(r, b, next, prev);
            } else if (num >= 10) {
                r = r + num;
                S_regdnoselect.setText(r);
                click = 0;
                displayDetails(r, b, next, prev);
            } else {
                JOptionPane.showMessageDialog(null, "You have reached the end...");
                S_prev.setEnabled(false);
            }

        }
    }

    /**
     *
     * @param b
     */
    private void detailsEnableDisable(boolean b) {
        S_name1.setEnabled(b);
        S_fathername1.setEnabled(b);
        S_mothername1.setEnabled(b);
        S_fatheroccupation1.setEnabled(b);
        S_motheroccupation1.setEnabled(b);
        S_uploadphoto1.setEnabled(b);
        S_address1.setEnabled(b);
        S_phoneno1.setEnabled(b);
        S_emailid1.setEnabled(b);
        S_aadhar1.setEnabled(b);
        S_10marks1.setEnabled(b);
        S_intermarks1.setEnabled(b);
        S_caste1.setEnabled(b);
        S_subcaste1.setEnabled(b);
    }

    /**
     *
     */
    public void savedetails() {
        try {
            String branchselect = "" + S_branchselect.getSelectedItem();
            String querry = "update " + branchselect + " set name=?,fathername=?,mothername=?,foccupation=?,moccupation=?,address=?,phoneno=?,emailid=?,aadhar=?,10marks=?,intermarks=?,caste=?,subcaste=?,photo=? where regd= ?";
            PreparedStatement ps = conn.prepareStatement(querry);
            ps.setString(1, S_name1.getText());
            ps.setString(2, S_fathername1.getText());
            ps.setString(3, S_mothername1.getText());
            ps.setString(4, S_fatheroccupation1.getText());
            ps.setString(5, S_motheroccupation1.getText());
            ps.setString(6, S_address1.getText());
            ps.setString(7, S_phoneno1.getText());
            ps.setString(8, S_emailid1.getText());
            ps.setString(9, S_aadhar1.getText());
            ps.setString(10, S_10marks1.getText());
            ps.setString(11, S_intermarks1.getText());
            ps.setString(12, S_caste1.getText());
            ps.setString(13, S_subcaste1.getText());
            if (click > 0) {
                ps.setString(14, Spath);
            } else {
                ps.setString(14, oldpath);
            }
            ps.setString(15, S_regdnoselect.getText());
            ps.executeUpdate();
            String attenbranch = "attendance_" + S_branchselect.getSelectedItem();
            String query = "update " + attenbranch + " set name=? where regd= ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, S_name1.getText());
            pst.setString(2, S_regdnoselect.getText());
            pst.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void find() {
        if (!S_regdnoselect.getText().equals("")) {
            S_next.setEnabled(true);
            S_prev.setEnabled(true);
            S_edit.setEnabled(true);
            S_save.setEnabled(true);
            displayDetails((S_regdnoselect.getText()).toUpperCase(), "" + S_branchselect.getSelectedItem(), false, false);
            detailsEnableDisable(false);
        } else {
            JOptionPane.showMessageDialog(null, "Please enter your registration number...");
            S_regdnoselect.grabFocus();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel10 = new javax.swing.JPanel();
        Student_details5 = new javax.swing.JPanel();
        jLabel125 = new javax.swing.JLabel();
        jLabel126 = new javax.swing.JLabel();
        jLabel127 = new javax.swing.JLabel();
        jLabel128 = new javax.swing.JLabel();
        S_labelphoto1 = new javax.swing.JLabel();
        S_name1 = new javax.swing.JTextField();
        S_fathername1 = new javax.swing.JTextField();
        S_fatheroccupation1 = new javax.swing.JTextField();
        S_uploadphoto1 = new javax.swing.JButton();
        S_mothername1 = new javax.swing.JTextField();
        jLabel129 = new javax.swing.JLabel();
        jLabel130 = new javax.swing.JLabel();
        S_address1 = new java.awt.TextArea();
        jLabel131 = new javax.swing.JLabel();
        S_phoneno1 = new javax.swing.JTextField();
        jLabel132 = new javax.swing.JLabel();
        S_emailid1 = new javax.swing.JTextField();
        jLabel133 = new javax.swing.JLabel();
        S_aadhar1 = new javax.swing.JTextField();
        jLabel134 = new javax.swing.JLabel();
        jLabel135 = new javax.swing.JLabel();
        S_10marks1 = new javax.swing.JTextField();
        jLabel136 = new javax.swing.JLabel();
        S_subcaste1 = new javax.swing.JTextField();
        jLabel137 = new javax.swing.JLabel();
        S_caste1 = new javax.swing.JTextField();
        jLabel138 = new javax.swing.JLabel();
        S_intermarks1 = new javax.swing.JTextField();
        jLabel139 = new javax.swing.JLabel();
        S_motheroccupation1 = new javax.swing.JTextField();
        jLabel140 = new javax.swing.JLabel();
        jLabel141 = new javax.swing.JLabel();
        jLabel142 = new javax.swing.JLabel();
        jLabel143 = new javax.swing.JLabel();
        S_gender1 = new javax.swing.JLabel();
        S_dateofbirth1 = new javax.swing.JLabel();
        S_regdno1 = new javax.swing.JLabel();
        S_category1 = new javax.swing.JLabel();
        S_branch1 = new javax.swing.JLabel();
        S_age1 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel144 = new javax.swing.JLabel();
        S_regdnoselect = new javax.swing.JTextField();
        jLabel145 = new javax.swing.JLabel();
        S_branchselect = new javax.swing.JComboBox<>();
        S_find = new javax.swing.JButton();
        S_prev = new javax.swing.JButton();
        S_next = new javax.swing.JButton();
        S_print = new javax.swing.JButton();
        S_edit = new javax.swing.JButton();
        back = new javax.swing.JButton();
        S_save = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setBounds(new java.awt.Rectangle(10, 0, 0, 0));
        setPreferredSize(new java.awt.Dimension(1510, 820));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel10.setBackground(new java.awt.Color(50, 34, 116));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Student_details5.setBackground(new java.awt.Color(255, 255, 255));
        Student_details5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1), "Student Details", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 1, 30), new java.awt.Color(50, 34, 116))); // NOI18N
        Student_details5.setAutoscrolls(true);
        Student_details5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel125.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel125.setForeground(new java.awt.Color(50, 34, 116));
        jLabel125.setText("Name");
        Student_details5.add(jLabel125, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 95, 122, 35));

        jLabel126.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel126.setForeground(new java.awt.Color(50, 34, 116));
        jLabel126.setText("Regd No");
        Student_details5.add(jLabel126, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 142, 122, 35));

        jLabel127.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel127.setForeground(new java.awt.Color(50, 34, 116));
        jLabel127.setText("Father Name");
        Student_details5.add(jLabel127, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 198, 122, 35));

        jLabel128.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel128.setForeground(new java.awt.Color(50, 34, 116));
        jLabel128.setText("Occupation");
        Student_details5.add(jLabel128, new org.netbeans.lib.awtextra.AbsoluteConstraints(375, 198, 122, 35));

        S_labelphoto1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Student_details5.add(S_labelphoto1, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 95, 180, 200));

        S_name1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        S_name1.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        S_name1.setSelectionColor(new java.awt.Color(0, 0, 255));
        Student_details5.add(S_name1, new org.netbeans.lib.awtextra.AbsoluteConstraints(153, 95, 270, 35));

        S_fathername1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Student_details5.add(S_fathername1, new org.netbeans.lib.awtextra.AbsoluteConstraints(153, 198, 204, 35));

        S_fatheroccupation1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Student_details5.add(S_fatheroccupation1, new org.netbeans.lib.awtextra.AbsoluteConstraints(503, 198, 204, 35));

        S_uploadphoto1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        S_uploadphoto1.setForeground(new java.awt.Color(50, 34, 116));
        S_uploadphoto1.setText("Upload Photo");
        S_uploadphoto1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                S_uploadphoto1ActionPerformed(evt);
            }
        });
        Student_details5.add(S_uploadphoto1, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 301, 180, 35));

        S_mothername1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Student_details5.add(S_mothername1, new org.netbeans.lib.awtextra.AbsoluteConstraints(153, 251, 204, 35));

        jLabel129.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel129.setForeground(new java.awt.Color(50, 34, 116));
        jLabel129.setText("Address");
        Student_details5.add(jLabel129, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 357, 122, 35));

        jLabel130.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel130.setForeground(new java.awt.Color(50, 34, 116));
        jLabel130.setText("Mother Name");
        Student_details5.add(jLabel130, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 251, 122, 35));

        S_address1.setBackground(new java.awt.Color(255, 255, 255));
        S_address1.setColumns(34);
        S_address1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        S_address1.setRows(2);
        Student_details5.add(S_address1, new org.netbeans.lib.awtextra.AbsoluteConstraints(153, 357, 827, 66));

        jLabel131.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel131.setForeground(new java.awt.Color(50, 34, 116));
        jLabel131.setText("Phone No");
        Student_details5.add(jLabel131, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 441, 122, 35));

        S_phoneno1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        S_phoneno1.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        Student_details5.add(S_phoneno1, new org.netbeans.lib.awtextra.AbsoluteConstraints(153, 441, 271, 35));

        jLabel132.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel132.setForeground(new java.awt.Color(50, 34, 116));
        jLabel132.setText("Email ID");
        Student_details5.add(jLabel132, new org.netbeans.lib.awtextra.AbsoluteConstraints(494, 441, 122, 35));

        S_emailid1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Student_details5.add(S_emailid1, new org.netbeans.lib.awtextra.AbsoluteConstraints(622, 441, 271, 35));

        jLabel133.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel133.setForeground(new java.awt.Color(50, 34, 116));
        jLabel133.setText("Aadhar");
        Student_details5.add(jLabel133, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 494, 122, 35));

        S_aadhar1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Student_details5.add(S_aadhar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(153, 494, 271, 35));

        jLabel134.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel134.setForeground(new java.awt.Color(50, 34, 116));
        jLabel134.setText("Gender");
        Student_details5.add(jLabel134, new org.netbeans.lib.awtextra.AbsoluteConstraints(494, 494, 122, 35));

        jLabel135.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel135.setForeground(new java.awt.Color(50, 34, 116));
        jLabel135.setText("Caste");
        Student_details5.add(jLabel135, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 653, 122, 35));

        S_10marks1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        S_10marks1.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        Student_details5.add(S_10marks1, new org.netbeans.lib.awtextra.AbsoluteConstraints(153, 600, 143, 35));

        jLabel136.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel136.setForeground(new java.awt.Color(50, 34, 116));
        jLabel136.setText("Sub Caste");
        Student_details5.add(jLabel136, new org.netbeans.lib.awtextra.AbsoluteConstraints(494, 653, 122, 35));

        S_subcaste1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Student_details5.add(S_subcaste1, new org.netbeans.lib.awtextra.AbsoluteConstraints(622, 653, 271, 35));

        jLabel137.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel137.setForeground(new java.awt.Color(50, 34, 116));
        jLabel137.setText("10th Marks");
        Student_details5.add(jLabel137, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 600, 122, 35));

        S_caste1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Student_details5.add(S_caste1, new org.netbeans.lib.awtextra.AbsoluteConstraints(153, 653, 271, 35));

        jLabel138.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel138.setForeground(new java.awt.Color(50, 34, 116));
        jLabel138.setText("Intermediate");
        Student_details5.add(jLabel138, new org.netbeans.lib.awtextra.AbsoluteConstraints(494, 600, 122, 35));

        S_intermarks1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Student_details5.add(S_intermarks1, new org.netbeans.lib.awtextra.AbsoluteConstraints(622, 600, 132, 35));

        jLabel139.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel139.setForeground(new java.awt.Color(50, 34, 116));
        jLabel139.setText("Branch");
        Student_details5.add(jLabel139, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 547, 122, 35));

        S_motheroccupation1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Student_details5.add(S_motheroccupation1, new org.netbeans.lib.awtextra.AbsoluteConstraints(503, 251, 204, 35));

        jLabel140.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel140.setForeground(new java.awt.Color(50, 34, 116));
        jLabel140.setText("Occupation");
        Student_details5.add(jLabel140, new org.netbeans.lib.awtextra.AbsoluteConstraints(375, 251, 122, 35));

        jLabel141.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel141.setForeground(new java.awt.Color(50, 34, 116));
        jLabel141.setText("Date Of Birth");
        Student_details5.add(jLabel141, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 304, 122, 35));

        jLabel142.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel142.setForeground(new java.awt.Color(50, 34, 116));
        jLabel142.setText("category");
        Student_details5.add(jLabel142, new org.netbeans.lib.awtextra.AbsoluteConstraints(622, 547, 87, 35));

        jLabel143.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel143.setForeground(new java.awt.Color(50, 34, 116));
        jLabel143.setText("Age");
        Student_details5.add(jLabel143, new org.netbeans.lib.awtextra.AbsoluteConstraints(375, 304, 122, 35));

        S_gender1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        S_gender1.setForeground(new java.awt.Color(255, 0, 0));
        Student_details5.add(S_gender1, new org.netbeans.lib.awtextra.AbsoluteConstraints(622, 494, 122, 35));

        S_dateofbirth1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        S_dateofbirth1.setForeground(new java.awt.Color(255, 0, 0));
        Student_details5.add(S_dateofbirth1, new org.netbeans.lib.awtextra.AbsoluteConstraints(153, 304, 204, 35));

        S_regdno1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        S_regdno1.setForeground(new java.awt.Color(255, 51, 51));
        Student_details5.add(S_regdno1, new org.netbeans.lib.awtextra.AbsoluteConstraints(153, 147, 204, 30));

        S_category1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        S_category1.setForeground(new java.awt.Color(255, 0, 0));
        Student_details5.add(S_category1, new org.netbeans.lib.awtextra.AbsoluteConstraints(727, 547, 122, 35));

        S_branch1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        S_branch1.setForeground(new java.awt.Color(255, 0, 0));
        Student_details5.add(S_branch1, new org.netbeans.lib.awtextra.AbsoluteConstraints(153, 547, 271, 35));

        S_age1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        S_age1.setForeground(new java.awt.Color(255, 0, 0));
        Student_details5.add(S_age1, new org.netbeans.lib.awtextra.AbsoluteConstraints(503, 304, 204, 35));

        jPanel10.add(Student_details5, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 25, -1, 707));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel144.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel144.setForeground(new java.awt.Color(50, 34, 116));
        jLabel144.setText("Registration No");
        jPanel9.add(jLabel144, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 44, -1, 30));

        S_regdnoselect.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        S_regdnoselect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                S_regdnoselectS_regdnoselectActionPerformed(evt);
            }
        });
        jPanel9.add(S_regdnoselect, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 80, 204, 35));

        jLabel145.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel145.setForeground(new java.awt.Color(50, 34, 116));
        jLabel145.setText("Branch");
        jPanel9.add(jLabel145, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 127, 100, 30));

        S_branchselect.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        S_branchselect.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CSE", "CSD", "CSM", "CBA", "CIC", "ECE", "EEE", "CIVIL", "MECH" }));
        jPanel9.add(S_branchselect, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 163, 108, 33));

        S_find.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        S_find.setForeground(new java.awt.Color(50, 34, 116));
        S_find.setText("Find");
        S_find.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                S_findActionPerformed(evt);
            }
        });
        jPanel9.add(S_find, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 226, 108, 38));

        jPanel10.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 70, 269, 302));

        S_prev.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        S_prev.setText("<<");
        S_prev.setEnabled(false);
        S_prev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                S_prevActionPerformed(evt);
            }
        });
        jPanel10.add(S_prev, new org.netbeans.lib.awtextra.AbsoluteConstraints(148, 486, 80, 40));

        S_next.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        S_next.setText(">>");
        S_next.setEnabled(false);
        S_next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                S_nextActionPerformed(evt);
            }
        });
        jPanel10.add(S_next, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 486, 80, 40));

        S_print.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        S_print.setText("Print");
        S_print.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                S_printActionPerformed(evt);
            }
        });
        jPanel10.add(S_print, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 410, 108, 40));

        S_edit.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        S_edit.setText("Edit");
        S_edit.setEnabled(false);
        S_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                S_editActionPerformed(evt);
            }
        });
        jPanel10.add(S_edit, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 410, 108, 40));

        back.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        back.setText("back");
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });
        jPanel10.add(back, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 100, 40));

        S_save.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        S_save.setText("save");
        S_save.setEnabled(false);
        S_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                S_saveActionPerformed(evt);
            }
        });
        jPanel10.add(S_save, new org.netbeans.lib.awtextra.AbsoluteConstraints(338, 410, 108, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        int result = JOptionPane.showConfirmDialog(Update.this, "Do you want to exit", "Closing", JOptionPane.YES_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            dispose();
        }
    }//GEN-LAST:event_formWindowClosing

    private void S_uploadphoto1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_S_uploadphoto1ActionPerformed
        click = 0;
        uploadPhoto();
    }//GEN-LAST:event_S_uploadphoto1ActionPerformed

    private void S_regdnoselectS_regdnoselectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_S_regdnoselectS_regdnoselectActionPerformed
        find();
    }//GEN-LAST:event_S_regdnoselectS_regdnoselectActionPerformed

    private void S_findActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_S_findActionPerformed
        find();
    }//GEN-LAST:event_S_findActionPerformed

    private void S_prevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_S_prevActionPerformed
        displayNextPrev(false, true);
        S_next.setEnabled(true);
    }//GEN-LAST:event_S_prevActionPerformed

    private void S_nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_S_nextActionPerformed
        displayNextPrev(true, false);
        S_prev.setEnabled(true);
    }//GEN-LAST:event_S_nextActionPerformed

    private void S_printActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_S_printActionPerformed
        try {
            Student student = new Student();
            student.setTitle(S_regdnoselect.getText().toUpperCase());
            student.setVisible(true);
            student.displaydetails(S_regdnoselect.getText(), S_branchselect.getSelectedItem() + "");
            student.remove();
            student.printDetails(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }//GEN-LAST:event_S_printActionPerformed

    private void S_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_S_editActionPerformed
        detailsEnableDisable(true);
    }//GEN-LAST:event_S_editActionPerformed

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
        dispose();
        MainFrame mf = new MainFrame();
        mf.setVisible(true);
    }//GEN-LAST:event_backActionPerformed

    private void S_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_S_saveActionPerformed
        if (S_phoneno1.getText().length() == 10 && S_aadhar1.getText().length() == 12 && S_10marks1.getText().length() == 3 && S_intermarks1.getText().length() == 3) {
            savedetails();
            detailsEnableDisable(false);
            displayDetails((S_regdnoselect.getText()).toUpperCase(), "" + S_branchselect.getSelectedItem(), false, false);
        } else {
            JOptionPane.showMessageDialog(null, "once check your details,");
        }
    }//GEN-LAST:event_S_saveActionPerformed

 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField S_10marks1;
    private javax.swing.JTextField S_aadhar1;
    private java.awt.TextArea S_address1;
    private javax.swing.JLabel S_age1;
    private javax.swing.JLabel S_branch1;
    private javax.swing.JComboBox<String> S_branchselect;
    private javax.swing.JTextField S_caste1;
    private javax.swing.JLabel S_category1;
    private javax.swing.JLabel S_dateofbirth1;
    private javax.swing.JButton S_edit;
    private javax.swing.JTextField S_emailid1;
    private javax.swing.JTextField S_fathername1;
    private javax.swing.JTextField S_fatheroccupation1;
    private javax.swing.JButton S_find;
    private javax.swing.JLabel S_gender1;
    private javax.swing.JTextField S_intermarks1;
    private javax.swing.JLabel S_labelphoto1;
    private javax.swing.JTextField S_mothername1;
    private javax.swing.JTextField S_motheroccupation1;
    private javax.swing.JTextField S_name1;
    private javax.swing.JButton S_next;
    private javax.swing.JTextField S_phoneno1;
    private javax.swing.JButton S_prev;
    private javax.swing.JButton S_print;
    private javax.swing.JLabel S_regdno1;
    private javax.swing.JTextField S_regdnoselect;
    private javax.swing.JButton S_save;
    private javax.swing.JTextField S_subcaste1;
    private javax.swing.JButton S_uploadphoto1;
    private javax.swing.JPanel Student_details5;
    private javax.swing.JButton back;
    private javax.swing.JLabel jLabel125;
    private javax.swing.JLabel jLabel126;
    private javax.swing.JLabel jLabel127;
    private javax.swing.JLabel jLabel128;
    private javax.swing.JLabel jLabel129;
    private javax.swing.JLabel jLabel130;
    private javax.swing.JLabel jLabel131;
    private javax.swing.JLabel jLabel132;
    private javax.swing.JLabel jLabel133;
    private javax.swing.JLabel jLabel134;
    private javax.swing.JLabel jLabel135;
    private javax.swing.JLabel jLabel136;
    private javax.swing.JLabel jLabel137;
    private javax.swing.JLabel jLabel138;
    private javax.swing.JLabel jLabel139;
    private javax.swing.JLabel jLabel140;
    private javax.swing.JLabel jLabel141;
    private javax.swing.JLabel jLabel142;
    private javax.swing.JLabel jLabel143;
    private javax.swing.JLabel jLabel144;
    private javax.swing.JLabel jLabel145;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel9;
    // End of variables declaration//GEN-END:variables
}
